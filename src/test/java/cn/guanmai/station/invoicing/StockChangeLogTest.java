package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.category.PurchaseSpecBean;
import cn.guanmai.station.bean.invoicing.SpuStockBean;
import cn.guanmai.station.bean.invoicing.StockChangeLogBean;
import cn.guanmai.station.bean.invoicing.InStockDetailInfoBean;
import cn.guanmai.station.bean.invoicing.OutStockDetailBean;
import cn.guanmai.station.bean.invoicing.OutStockSheetBean;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.bean.invoicing.SupplySkuBean;
import cn.guanmai.station.bean.invoicing.param.StockChangeLogFilterParam;
import cn.guanmai.station.bean.invoicing.param.StockCheckFilterParam;
import cn.guanmai.station.bean.invoicing.param.OutStockModifyParam;
import cn.guanmai.station.bean.invoicing.param.OutStockSheetFilterParam;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.invoicing.StockCheckServiceImpl;
import cn.guanmai.station.impl.invoicing.InStockServiceImpl;
import cn.guanmai.station.impl.invoicing.OutStockServiceImpl;
import cn.guanmai.station.impl.invoicing.StockServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.invoicing.StockCheckService;
import cn.guanmai.station.interfaces.invoicing.InStockService;
import cn.guanmai.station.interfaces.invoicing.OutStockService;
import cn.guanmai.station.interfaces.invoicing.StockService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Mar 6, 2019 7:15:35 PM 
* @des SPU库存变动记录测试
* @version 1.0 
*/
public class StockChangeLogTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(StockChangeLogTest.class);
	private StockCheckService stockCheckService;
	private LoginUserInfoService loginUserInfoService;
	private InStockService stockInService;
	private StockService stockService;
	private InitDataBean initData;
	private OrderTool orderTool;
	private OrderService orderService;
	private OutStockService stockOutService;
	private StockChangeLogFilterParam filterParam;
	private LoginUserInfoBean loginUserInfo;
	private Map<String, String> headers;
	private String spu_id;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");

	@BeforeClass
	public void initData() {
		headers = getStationCookie();
		stockCheckService = new StockCheckServiceImpl(headers);
		stockInService = new InStockServiceImpl(headers);
		stockService = new StockServiceImpl(headers);
		orderService = new OrderServiceImpl(headers);
		stockOutService = new OutStockServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		orderTool = new OrderTool(headers);
		try {
			initData = getInitData();
			Assert.assertNotEquals(initData, null, "初始化站点数据失败");
			spu_id = initData.getSpu().getSpu_id();

			filterParam = new StockChangeLogFilterParam(spu_id, todayStr, todayStr);

			loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账户信息失败");

		} catch (Exception e) {
			logger.error("初始化站点数据过程中遇到错误: ", e);
			Assert.fail("初始化站点数据过程中遇到错误: ", e);
		}
	}

	@Test
	public void stockChangeLogTestCase01() {
		Reporter.log("测试点: 查询库存变动历史记录");
		try {
			List<StockChangeLogBean> stockChangeLogArray = stockCheckService.getSpuStockChangeLogList(filterParam);
			Assert.assertNotEquals(stockChangeLogArray, null, "获取SPU库存变动历史记录失败");
		} catch (Exception e) {
			logger.error("获取SPU库存变动历史记录遇到错误: ", e);
			Assert.fail("获取SPU库存变动历史记录遇到错误: ", e);
		}
	}

	@Test
	public void stockChangeLogTestCase02() {
		try {
			Reporter.log("测试点: 比对最新的一条库存变动记录和库存盘点页面展示的数据是否一致");
			String beforeMonthDay = TimeUtil.calculateTime("yyyy-MM-dd", todayStr, -1, Calendar.MONTH);
			filterParam.setBegin(beforeMonthDay);
			List<StockChangeLogBean> stockChangeLogArray = stockCheckService.getSpuStockChangeLogList(filterParam);
			Assert.assertNotEquals(stockChangeLogArray, null, "获取SPU库存变动历史记录失败");
			if (stockChangeLogArray.size() > 0) {
				StockCheckFilterParam filterParam = new StockCheckFilterParam();
				filterParam.setOffset(0);
				filterParam.setLimit(10);
				filterParam.setText(spu_id);

				List<SpuStockBean> stockCheckList = stockCheckService.searchStockCheck(filterParam);
				Assert.assertNotEquals(stockCheckList, null, "获取商品盘点列表失败");

				// 获取SPU库存盘点记录
				SpuStockBean spuStock = stockCheckList.stream().filter(s -> s.getSpu_id().equals(spu_id)).findAny()
						.orElse(null);

				Assert.assertNotEquals(spuStock, null, "条件查找没有找到目标SPU " + spu_id);

				// 获取最新的库存变动记录
				StockChangeLogBean stockChangeLog = stockChangeLogArray.get(0);

				boolean result = spuStock.getMaterial().getAmount().compareTo(stockChangeLog.getStock()) == 0
						&& spuStock.getMaterial().getAvg_price().divide(new BigDecimal("100"))
								.compareTo(stockChangeLog.getAvg_price()) == 0;

				Assert.assertEquals(result, true, "库存盘点页面看到的数据和库存变动页面最新一条数据不匹配");
			}
		} catch (Exception e) {
			logger.error("获取SPU库存变动历史记录遇到错误: ", e);
			Assert.fail("获取SPU库存变动历史记录遇到错误: ", e);
		}
	}

	@Test
	public void stockChangeLogTestCase03() {
		try {
			Reporter.log("测试点: 成品入库后查看商品库存变动记录");

			// 先在入库前查询当前的库存和库存均价
			StockCheckFilterParam stockCheckfilterParam = new StockCheckFilterParam();
			stockCheckfilterParam.setOffset(0);
			stockCheckfilterParam.setLimit(10);
			stockCheckfilterParam.setText(spu_id);

			Reporter.log("步骤一: 先查询商品的库存和库存均价");
			List<SpuStockBean> stockCheckList = stockCheckService.searchStockCheck(stockCheckfilterParam);
			Assert.assertNotEquals(stockCheckList, null, "获取商品盘点列表失败");

			// 获取SPU库存盘点记录
			SpuStockBean spuStock = stockCheckList.stream().filter(s -> s.getSpu_id().equals(spu_id)).findAny()
					.orElse(null);

			Reporter.log("步骤二: 进行商品成品入库操作");
			// 创建入库单
			SupplierDetailBean supplier = initData.getSupplier();
			String supplier_id = supplier.getId();
			String supplier_name = supplier.getName();

			String sheet_id = stockInService.createInStockSheet(supplier_id, supplier_name);
			Assert.assertNotEquals(sheet_id, null, "创建成品入库单失败");

			InStockDetailInfoBean stockIn = new InStockDetailInfoBean();
			stockIn.setId(sheet_id);
			stockIn.setCreator("自动化");
			stockIn.setRemark("自动化创建");
			stockIn.setDate_time(TimeUtil.getCurrentTime("yyyy-MM-dd'T'HH:mm:ss.SSS"));
			stockIn.setSubmit_time(TimeUtil.getCurrentTime("yyyy-MM-dd"));
			stockIn.setSettle_supplier_id(supplier_id);
			stockIn.setSupplier_name(supplier_name);
			stockIn.setSupplier_customer_id(supplier.getCustomer_id());
			stockIn.setType(1);
			stockIn.setStatus(1);
			stockIn.setIs_submit(2);
			stockIn.setStation_id(loginUserInfo.getStation_id());
			stockIn.setSku_money(new BigDecimal("0"));
			stockIn.setDelta_money(new BigDecimal("0"));

			// 金额折让
			List<InStockDetailInfoBean.Discount> discounts = new ArrayList<InStockDetailInfoBean.Discount>();
			stockIn.setDiscounts(discounts);

			// 费用分摊
			List<InStockDetailInfoBean.Share> shares = new ArrayList<InStockDetailInfoBean.Share>();
			stockIn.setShares(shares);

			PurchaseSpecBean purchaseSpec = initData.getPurchaseSpec();
			String purchaseSpec_id = purchaseSpec.getId();
			String parchase_name = purchaseSpec.getName();

			List<SupplySkuBean> supplySkus = stockService.getSupplySkuList("大叶茼蒿", sheet_id);
			Assert.assertEquals(supplySkus != null && supplySkus.size() >= 1, true, "入库单搜索入库商品无结果,与预期不符");

			SupplySkuBean supplySku = supplySkus.get(0);

			String parchase_std_unit = supplySku.getStd_unit_name();
			BigDecimal purchase_ratio = supplySku.getSale_ratio();
			String purchase_unit = supplySku.getSale_unit_name();
			InStockDetailInfoBean.Detail detail = stockIn.new Detail();

			NumberFormat nf = new DecimalFormat("00000");

			detail.setBatch_number(sheet_id + "-" + nf.format(1));
			detail.setName(purchaseSpec.getName());
			String displayName = parchase_name + "(" + purchase_ratio + parchase_std_unit + "/" + purchase_unit + ")";
			detail.setDisplayName(displayName);
			detail.setId(purchaseSpec_id);
			detail.setCategory(supplySku.getCategory_id_2_name());
			detail.setSpu_id(supplySku.getSpu_id());
			detail.setPurchase_unit(purchase_unit);
			detail.setStd_unit(parchase_std_unit);
			detail.setRatio(purchase_ratio);
			BigDecimal quantity = new BigDecimal("2");
			detail.setQuantity(quantity);
			detail.setPurchase_unit_quantity(quantity.multiply(purchase_ratio));
			BigDecimal unit_price = new BigDecimal("5");
			detail.setUnit_price(unit_price);
			detail.setPurchase_unit_price(unit_price.multiply(purchase_ratio));
			detail.setMoney(unit_price.multiply(quantity));
			detail.setOperator("自动化");

			List<InStockDetailInfoBean.Detail> details = new ArrayList<InStockDetailInfoBean.Detail>();
			details.add(detail);
			stockIn.setDetails(details);

			boolean result = stockInService.modifyInStockSheet(stockIn);
			Assert.assertEquals(result, true, "提交成品入库单失败");

			Reporter.log("步骤三: 查看库存变动记录的数据是否正确");
			List<StockChangeLogBean> stockChangeLogArray = stockCheckService.getSpuStockChangeLogList(filterParam);
			Assert.assertNotEquals(stockChangeLogArray, null, "查询商品库存变动记录失败");
			Assert.assertEquals(stockChangeLogArray.size() > 0, true, "商品库存变动记录为空");

			StockChangeLogBean targetStockChangeLog = stockChangeLogArray.get(0);
			boolean success = false;
			if (spuStock == null) {
				success = targetStockChangeLog.getAmount().compareTo(quantity) == 0
						&& targetStockChangeLog.getAvg_price().compareTo(unit_price) == 0
						&& targetStockChangeLog.getOld_avg_price().compareTo(BigDecimal.ZERO) == 0
						&& targetStockChangeLog.getOld_stock().compareTo(BigDecimal.ZERO) == 0
						&& targetStockChangeLog.getStock().compareTo(quantity) == 0
						&& targetStockChangeLog.getSheet_number().equals(sheet_id)
						&& targetStockChangeLog.getChange_type().equals("入库");
			} else {
				// 预期现库存
				BigDecimal stock_now = spuStock.getMaterial().getAmount().add(quantity);
				// 预期现库存价值
				BigDecimal stock_value = spuStock.getMaterial().getAmount()
						.multiply(spuStock.getMaterial().getAvg_price().divide(new BigDecimal("100")))
						.add(quantity.multiply(unit_price));

				// 预期现库存均价
				BigDecimal stock_avg_price = stock_value.divide(stock_now, 2, RoundingMode.CEILING);

				success = targetStockChangeLog.getAmount().compareTo(quantity) == 0
						&& targetStockChangeLog.getOld_avg_price()
								.compareTo(spuStock.getMaterial().getAvg_price().divide(new BigDecimal("100"))) == 0
						&& targetStockChangeLog.getOld_stock().compareTo(spuStock.getMaterial().getAmount()) == 0
						&& targetStockChangeLog.getAvg_price().compareTo(stock_avg_price) == 0
						&& targetStockChangeLog.getStock().compareTo(stock_now) == 0
						&& targetStockChangeLog.getSheet_number().equals(sheet_id)
						&& targetStockChangeLog.getChange_type().equals("入库");

			}

			Assert.assertEquals(success, true, "SPU商品 " + spu_id + " 入库后记录的库存变动记录不正确");

		} catch (Exception e) {
			logger.error("成品入库后查看库存变动记录遇到错误: ", e);
			Assert.fail("成品入库后查看库存变动记录遇到错误: ", e);
		}
	}

	@Test
	public void stockChangeLogTestCase04() {
		Reporter.log("测试点: 出库单出库后查询库存变动记录");

		Reporter.log("步骤一: 创建订单");

		String spu_id = null;
		String order_id = null;
		OrderDetailBean.Detail detail = null;
		try {
			order_id = orderTool.oneStepCreateOrder(4);
			Assert.assertNotEquals(order_id, null, "创建订单失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单详情信息失败");

			detail = orderDetail.getDetails().get(0);
			spu_id = detail.getSpu_id();
			boolean result = orderService.updateOrderState(order_id, 10);
			Assert.assertEquals(result, true, "修改订单状态为配送中,修改失败");
		} catch (Exception e) {
			logger.error("创建订单的过程中遇到错误: ", e);
			Assert.fail("创建订单的过程中遇到错误: ", e);
		}

		SpuStockBean spuStock = null;
		try {
			Reporter.log("步骤二: 获取出库商品的库存信息");
			// 先在入库前查询当前的库存和库存均价
			StockCheckFilterParam stockCheckfilterParam = new StockCheckFilterParam();
			stockCheckfilterParam.setOffset(0);
			stockCheckfilterParam.setLimit(10);
			stockCheckfilterParam.setText(spu_id);
			List<SpuStockBean> stockCheckList = stockCheckService.searchStockCheck(stockCheckfilterParam);
			Assert.assertNotEquals(stockCheckList, null, "获取商品盘点列表失败");

			// 获取SPU库存盘点记录
			final String temp_spu_id = spu_id;
			spuStock = stockCheckList.stream().filter(s -> s.getSpu_id().equals(temp_spu_id)).findAny().orElse(null);

		} catch (Exception e) {
			logger.error("商品盘点信息查询遇到错误: ", e);
			Assert.fail("商品盘点信息查询遇到错误: ", e);
		}

		Reporter.log("步骤三: 出库单进行出库操作");
		OutStockSheetFilterParam fiterParam = new OutStockSheetFilterParam(2, 0, order_id, 0, 10, todayStr, todayStr);
		try {
			Thread.sleep(2000);
			List<OutStockSheetBean> stockOutSheetList = stockOutService.searchOutStockSheet(fiterParam);
			Assert.assertEquals(stockOutSheetList != null && stockOutSheetList.size() > 0, true, "搜索成品出库单失败");

			String temp_order_id = order_id;
			OutStockSheetBean stockOutSheet = stockOutSheetList.stream().filter(s -> s.getId().equals(temp_order_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(stockOutSheet, null, "订单" + order_id + "没有生成对应的出库单");

			OutStockDetailBean stockOutDetail = stockOutService.getOutStockDetailInfo(order_id);
			Assert.assertNotEquals(stockOutDetail, null, "获取成品出库单详细信息失败");

			OutStockModifyParam outStockModifyParam = new OutStockModifyParam();
			outStockModifyParam.setId(stockOutDetail.getId());
			outStockModifyParam.setCreator(stockOutDetail.getCreator());
			outStockModifyParam.setIs_bind_order(false);
			outStockModifyParam.setIs_submit(2);
			outStockModifyParam.setOut_stock_customer_id(stockOutDetail.getOut_stock_customer_id());
			outStockModifyParam.setOut_stock_time(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm"));
			outStockModifyParam.setStatus(1);
			List<OutStockModifyParam.Detail> param_details = new ArrayList<OutStockModifyParam.Detail>();
			OutStockModifyParam.Detail param_detail = null;
			for (OutStockDetailBean.Detail d : stockOutDetail.getDetails()) {
				param_detail = outStockModifyParam.new Detail();
				param_detail.setCategory(d.getCategory());
				param_detail.setClean_food(false);
				param_detail.setId(d.getId());
				param_detail.setName(d.getName());
				param_detail.setOut_of_stock(false);
				param_detail.setQuantity(d.getQuantity());
				param_detail.setReal_std_count(d.getReal_std_count());
				param_detail.setSale_ratio(d.getSale_ratio());
				param_detail.setSale_unit_name(d.getSale_unit_name());
				param_detail.setSpu_id(d.getSpu_id());
				param_detail.setStd_unit_name(d.getStd_unit_name());
				param_detail.setStd_unit_name_forsale(d.getStd_unit_name_forsale());
				param_details.add(param_detail);
			}
			outStockModifyParam.setDetails(param_details);

			boolean result = stockOutService.modifyOutStockSheet(outStockModifyParam);
			Assert.assertEquals(result, true, "出库单手工出库失败");
		} catch (Exception e) {
			logger.error("查询出库单并对出库单进行出库的过程中遇到错误: ", e);
			Assert.fail("查询出库单并对出库单进行出库的过程中遇到错误: ", e);
		}

		try {
			Reporter.log("步骤四: 查看库存变动记录的数据是否正确");
			filterParam.setSpu_id(spu_id);
			List<StockChangeLogBean> stockChangeLogArray = stockCheckService.getSpuStockChangeLogList(filterParam);
			Assert.assertNotEquals(stockChangeLogArray, null, "查询商品库存变动记录失败");
			Assert.assertEquals(stockChangeLogArray.size() > 0, true, "商品库存变动记录为空");

			StockChangeLogBean targetStockChangeLog = stockChangeLogArray.get(0);
			boolean success = false;
			BigDecimal amount = detail.getReal_quantity();
			if (spuStock == null) {
				success = targetStockChangeLog.getAmount().compareTo(amount.negate()) == 0
						&& targetStockChangeLog.getOld_avg_price().compareTo(BigDecimal.ZERO) == 0
						&& targetStockChangeLog.getOld_stock().compareTo(BigDecimal.ZERO) == 0
						&& targetStockChangeLog.getStock().compareTo(amount.negate()) == 0
						&& targetStockChangeLog.getSheet_number().equals(order_id)
						&& targetStockChangeLog.getChange_type().equals("出库");
			} else {
				// 预期现库存
				BigDecimal stock_now = spuStock.getMaterial().getAmount().subtract(amount);

				// 预期现库存均价
				BigDecimal stock_avg_price = spuStock.getMaterial().getAvg_price();

				success = targetStockChangeLog.getAmount().compareTo(amount.negate()) == 0
						&& targetStockChangeLog.getOld_avg_price()
								.compareTo(spuStock.getMaterial().getAvg_price().divide(new BigDecimal("100"))) == 0
						&& targetStockChangeLog.getOld_stock().compareTo(spuStock.getMaterial().getAmount()) == 0
						&& targetStockChangeLog.getAvg_price().compareTo(stock_avg_price) == 0
						&& targetStockChangeLog.getStock().compareTo(stock_now) == 0
						&& targetStockChangeLog.getSheet_number().equals(order_id)
						&& targetStockChangeLog.getChange_type().equals("出库");

			}
			Assert.assertEquals(success, true, "SPU商品 " + spu_id + " 出库后记录的库存变动记录不正确");
		} catch (Exception e) {
			logger.error("成品出库后查看库存变动记录遇到错误: ", e);
			Assert.fail("成品出库后查看库存变动记录遇到错误: ", e);
		}
	}

}
