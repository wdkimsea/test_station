package cn.guanmai.station.tools;

import cn.guanmai.station.bean.category.PurchaseSpecBean;
import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.invoicing.InStockDetailInfoBean;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.bean.invoicing.SupplySkuBean;
import cn.guanmai.station.bean.invoicing.param.InStockCreateParam;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.invoicing.InStockServiceImpl;
import cn.guanmai.station.impl.invoicing.StockServiceImpl;
import cn.guanmai.station.impl.invoicing.SupplierServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.invoicing.InStockService;
import cn.guanmai.station.interfaces.invoicing.StockService;
import cn.guanmai.station.interfaces.invoicing.SupplierService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

import org.testng.Assert;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* 
* @author liming 
* @date Mar 27, 2019 2:19:47 PM 
* @des 成品入库工具类
* @version 1.0 
*/
public class InStockTool {
	private InStockService inStockService;
	private StockService stockService;
	private SupplierService supplierService;
	private OrderService orderService;
	private CategoryService categoryService;
	private LoginUserInfoService loginUserInfoService;

	public InStockTool(Map<String, String> headers) {
		inStockService = new InStockServiceImpl(headers);
		stockService = new StockServiceImpl(headers);
		supplierService = new SupplierServiceImpl(headers);
		orderService = new OrderServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
	}

	/**
	 * 
	 * @param supplier_id 供应的ID
	 * @param texts       搜索入库商品关键字
	 * @return
	 * @throws Exception
	 */
	public String oneStepCreateInStockSheet(String supplier_id, String[] texts) throws Exception {
		List<SupplierDetailBean> suppliers = supplierService.getSettleSupplierList();
		Assert.assertNotEquals(suppliers, null, "拉取站点供应商列表失败");

		SupplierDetailBean supplier = suppliers.stream().filter(s -> s.getId().equals(supplier_id)).findAny()
				.orElse(null);

		Assert.assertNotEquals(supplier, null, "没有找到供应的ID为 " + supplier_id + " 的供应商");

		String supplier_name = supplier.getName();

		String sheet_id = inStockService.createInStockSheet(supplier_id, supplier_name);
		Assert.assertNotEquals(sheet_id, null, "创建成品入库单失败");

		LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
		Assert.assertNotEquals(loginUserInfo, null, "获取登录账户信息失败");

		InStockDetailInfoBean inStockDetail = new InStockDetailInfoBean();
		inStockDetail.setId(sheet_id);
		inStockDetail.setCreator("自动化");
		inStockDetail.setRemark("自动化创建");
		inStockDetail.setDate_time(TimeUtil.getCurrentTime("yyyy-MM-dd'T'HH:mm:ss.SSS"));
		inStockDetail.setSubmit_time(TimeUtil.getCurrentTime("yyyy-MM-dd"));
		inStockDetail.setSettle_supplier_id(supplier_id);
		inStockDetail.setSupplier_name(supplier_name);
		inStockDetail.setSupplier_customer_id(supplier.getCustomer_id());
		inStockDetail.setType(1);
		inStockDetail.setStatus(1);
		inStockDetail.setIs_submit(2);
		inStockDetail.setStation_id(loginUserInfo.getStation_id());
		inStockDetail.setSku_money(new BigDecimal("0"));
		inStockDetail.setDelta_money(new BigDecimal("0"));

		// 金额折让
		inStockDetail.setDiscounts(new ArrayList<InStockDetailInfoBean.Discount>());

		// 费用分摊
		inStockDetail.setShares(new ArrayList<InStockDetailInfoBean.Share>());

		List<InStockDetailInfoBean.Detail> details = new ArrayList<InStockDetailInfoBean.Detail>();
		NumberFormat nf = new DecimalFormat("00000");

		// 用来记录已添加的采购规格
		Set<String> purchase_ids = new HashSet<String>();
		int batch_number = 1;
		for (String text : texts) {
			List<SupplySkuBean> supplySkus = stockService.getSupplySkuList(text, sheet_id);
			Assert.assertEquals(supplySkus != null, true, "搜索过滤入库商品失败");
			for (SupplySkuBean supplySku : supplySkus) {
				if (purchase_ids.contains(supplySku.getSku_id())) {
					continue;
				}
				String parchase_std_unit = supplySku.getStd_unit_name();
				BigDecimal purchase_ratio = supplySku.getSale_ratio();
				String purchase_unit = supplySku.getSale_unit_name();

				InStockDetailInfoBean.Detail detail = inStockDetail.new Detail();
				detail.setBatch_number(sheet_id + "-" + nf.format(batch_number));
				detail.setName(supplySku.getSku_name());
				String displayName = supplySku.getSku_name() + "(" + purchase_ratio + parchase_std_unit + "/"
						+ purchase_unit + ")";
				detail.setDisplayName(displayName);
				detail.setId(supplySku.getSku_id());
				detail.setCategory(supplySku.getCategory_id_2_name());
				detail.setSpu_id(supplySku.getSpu_id());
				detail.setPurchase_unit(purchase_unit);
				detail.setStd_unit(parchase_std_unit);
				detail.setRatio(purchase_ratio);
				BigDecimal quantity = NumberUtil.getRandomNumber(30, 45, 0);
				detail.setQuantity(quantity);
				detail.setPurchase_unit_quantity(quantity.multiply(purchase_ratio));
				BigDecimal unit_price = NumberUtil.getRandomNumber(2, 6, 1);
				detail.setUnit_price(unit_price);
				detail.setPurchase_unit_price(unit_price.multiply(purchase_ratio));
				detail.setMoney(unit_price.multiply(quantity));
				detail.setOperator("自动化");
				details.add(detail);
				batch_number += 1;
				purchase_ids.add(supplySku.getSku_id());
				if (details.size() >= 30) {
					break;
				}
			}
		}

		Assert.assertEquals(details.size() > 0, true, "根据关键字搜索过滤,没有找到任何匹配的入库商品,无法进行入库操作");

		inStockDetail.setDetails(details);

		return inStockService.modifyInStockSheet(inStockDetail) ? sheet_id : null;
	}

	/**
	 * 为订单入库商品
	 * 
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	public boolean createInStockSheetForOrder(String order_id) throws Exception {
		OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
		Assert.assertNotEquals(orderDetail, null, "获取订单详细信息失败");

		List<OrderDetailBean.Detail> details = orderDetail.getDetails();

		// 找到订单中的商品对应的采购规格以及采购规格对应的供应商
		String sku_id = null;
		String spu_id = null;
		String purchase_spec_id = null;
		// 供应商ID为键
		List<PurchaseSpecBean> purchaseSpecList = new ArrayList<>();
		Set<String> purcahse_spec_ids = new HashSet<String>();
		SkuBean sku = null;
		for (OrderDetailBean.Detail detail : details) {
			sku_id = detail.getSku_id();
			spu_id = detail.getSpu_id();
			sku = categoryService.getSaleSkuById(spu_id, sku_id);
			Assert.assertNotEquals(sku, null, "获取销售SKU详细信息失败");
			purchase_spec_id = sku.getPurchase_spec_id();
			if (!purcahse_spec_ids.contains(purchase_spec_id)) {
				purcahse_spec_ids.add(purchase_spec_id);
				PurchaseSpecBean purchaseSpec = categoryService.getPurchaseSpecById(purchase_spec_id);
				Assert.assertNotEquals(purchaseSpec, null, "获取采购规格 " + purchase_spec_id + " 详细信息失败");
				purchaseSpecList.add(purchaseSpec);
			}
		}

		List<SupplierDetailBean> supplierList = supplierService.getSettleSupplierList();
		Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

		SupplierDetailBean supplierDetail = NumberUtil.roundNumberInList(supplierList);

		String supplier_id = supplierDetail.getId();
		String supplier_name = supplierDetail.getName();

		InStockCreateParam inStockCreateParam = new InStockCreateParam();

		List<InStockCreateParam.Detail> inStockDetails = new ArrayList<InStockCreateParam.Detail>();
		InStockCreateParam.Detail inStockDetail = null;
		BigDecimal sku_money = new BigDecimal("0");
		for (PurchaseSpecBean purchaseSpec : purchaseSpecList) {
			inStockDetail = inStockCreateParam.new Detail();

			purchase_spec_id = purchaseSpec.getId();
			// 获取指定供应商和指定采购规格的入库均价
			BigDecimal supplier_avg_price = inStockService.getSupplieraveragePrice(purchase_spec_id, supplier_id);

			Assert.assertNotEquals(supplier_avg_price, null,
					"获取采购规格 " + purchase_spec_id + " 在此供应商 " + supplier_id + " 对应的入库均价失败");

			String parchase_name = purchaseSpec.getName();
			String parchase_std_unit = purchaseSpec.getPurchase_unit();
			BigDecimal purchase_ratio = purchaseSpec.getRatio();
			String purchase_unit = purchaseSpec.getUnit_name();

			inStockDetail.setBatch_number(StringUtil.getRandomString(12).toUpperCase());
			inStockDetail.setName(parchase_name);
			String displayName = parchase_name + "(" + purchase_ratio + parchase_std_unit + "/" + purchase_unit + ")";
			inStockDetail.setDisplayName(displayName);
			inStockDetail.setId(purchase_spec_id);
			inStockDetail.setCategory(purchaseSpec.getCategory_2_name());
			inStockDetail.setSpu_id(purchaseSpec.getSpu_id());
			inStockDetail.setPurchase_unit(purchase_unit);
			inStockDetail.setStd_unit(parchase_std_unit);
			inStockDetail.setRatio(purchase_ratio);
			BigDecimal quantity = NumberUtil.getRandomNumber(100, 200, 1);
			inStockDetail.setQuantity(quantity);
			inStockDetail.setPurchase_unit_quantity(quantity.divide(purchase_ratio, 4, BigDecimal.ROUND_HALF_UP));

			// 有入库均价就取均价,没有就随机取
			BigDecimal unit_price = supplier_avg_price.compareTo(BigDecimal.ZERO) == 1 ? supplier_avg_price
					: NumberUtil.getRandomNumber(5, 10, 1);

			inStockDetail.setUnit_price(unit_price);
			inStockDetail.setPurchase_unit_price(unit_price.multiply(purchase_ratio));
			BigDecimal money = unit_price.multiply(quantity).setScale(2, BigDecimal.ROUND_HALF_UP);
			inStockDetail.setMoney(money);

			inStockDetail.setIs_arrival(NumberUtil.roundNumberInList(Arrays.asList(0, 1)));
			sku_money = sku_money.add(money);
			inStockDetails.add(inStockDetail);
		}

		inStockCreateParam.setDetails(inStockDetails);
		inStockCreateParam.setSku_money(sku_money);
		inStockCreateParam.setSettle_supplier_id(supplier_id);
		inStockCreateParam.setSupplier_name(supplier_name);
		inStockCreateParam.setSubmit_time_new(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm"));
		inStockCreateParam.setDiscount(new ArrayList<>());
		inStockCreateParam.setShare(new ArrayList<>());
		inStockCreateParam.setDelta_money(new BigDecimal("0"));
		inStockCreateParam.setIs_submit(2);
		inStockCreateParam.setRemark(StringUtil.getRandomString(6));
		String sheet_id = inStockService.createInStockSheet(inStockCreateParam);
		return sheet_id != null;
	}

}
