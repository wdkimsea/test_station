package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.category.Category2Bean;
import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.invoicing.PurchaseSkuSettleSupplier;
import cn.guanmai.station.bean.invoicing.RefundStockRecordBean;
import cn.guanmai.station.bean.invoicing.RefundStockResultBean;
import cn.guanmai.station.bean.invoicing.SettleSheetBean;
import cn.guanmai.station.bean.invoicing.SettleSheetDetailBean;
import cn.guanmai.station.bean.invoicing.StockBatchBean;
import cn.guanmai.station.bean.invoicing.InStockDetailInfoBean;
import cn.guanmai.station.bean.invoicing.InStockRecordBean;
import cn.guanmai.station.bean.invoicing.InStockSheetBean;
import cn.guanmai.station.bean.invoicing.InStockSummarySpuDetailBean;
import cn.guanmai.station.bean.invoicing.ReturnStockBatchBean;
import cn.guanmai.station.bean.invoicing.ReturnStockDetailBean;
import cn.guanmai.station.bean.invoicing.ReturnStockSheetBean;
import cn.guanmai.station.bean.invoicing.StockSummaryBean;
import cn.guanmai.station.bean.invoicing.StockSummaryCategoryDetailBean;
import cn.guanmai.station.bean.invoicing.SupplierBean;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.bean.invoicing.SupplySkuBean;
import cn.guanmai.station.bean.invoicing.param.RefundStockFilterParam;
import cn.guanmai.station.bean.invoicing.param.SettleSheetDetailFilterParam;
import cn.guanmai.station.bean.invoicing.param.SettleSheetDetailSubmitParam;
import cn.guanmai.station.bean.invoicing.param.SettleSheetFilterParam;
import cn.guanmai.station.bean.invoicing.param.InStockCreateParam;
import cn.guanmai.station.bean.invoicing.param.InStockRecordFilterParam;
import cn.guanmai.station.bean.invoicing.param.InStockSheetFilterParam;
import cn.guanmai.station.bean.invoicing.param.StockRecordFilterParam;
import cn.guanmai.station.bean.invoicing.param.RefundStockParam;
import cn.guanmai.station.bean.invoicing.param.ReturnStockSheetFilterParam;
import cn.guanmai.station.bean.invoicing.param.StockSummaryFilterParam;
import cn.guanmai.station.bean.order.CustomerBean;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderReceiveTimeBean;
import cn.guanmai.station.bean.order.OrderResponseBean;
import cn.guanmai.station.bean.order.OrderResponseBean.Data.NotEnoughInventories;
import cn.guanmai.station.bean.order.param.OrderCreateParam;
import cn.guanmai.station.bean.order.param.OrderExceptionParam;
import cn.guanmai.station.bean.order.param.OrderRefundParam;
import cn.guanmai.station.bean.purchase.PurchaserBean;
import cn.guanmai.station.bean.purchase.PurchaserResponseBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean.PurchaseTaskData;
import cn.guanmai.station.bean.purchase.param.PurchaserParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.invoicing.RefundStockServiceImpl;
import cn.guanmai.station.impl.invoicing.StockCheckServiceImpl;
import cn.guanmai.station.impl.invoicing.InStockServiceImpl;
import cn.guanmai.station.impl.invoicing.StockRecordServiceImpl;
import cn.guanmai.station.impl.invoicing.ReturnStockServiceImpl;
import cn.guanmai.station.impl.invoicing.StockServiceImpl;
import cn.guanmai.station.impl.invoicing.StockSummaryServiceImpl;
import cn.guanmai.station.impl.invoicing.SupplierFinanceServiceImpl;
import cn.guanmai.station.impl.invoicing.SupplierServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaserServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.invoicing.RefundStockService;
import cn.guanmai.station.interfaces.invoicing.StockCheckService;
import cn.guanmai.station.interfaces.invoicing.InStockService;
import cn.guanmai.station.interfaces.invoicing.StockRecordService;
import cn.guanmai.station.interfaces.invoicing.ReturnStockService;
import cn.guanmai.station.interfaces.invoicing.StockService;
import cn.guanmai.station.interfaces.invoicing.StockSummaryService;
import cn.guanmai.station.interfaces.invoicing.SupplierFinanceService;
import cn.guanmai.station.interfaces.invoicing.SupplierService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.purchase.PurchaserService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.station.tools.PurchaseTaskTool;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Nov 7, 2018 4:42:02 PM 
* @todo 删除供应商
* @version 1.0 
*/
public class SupplierDeleteTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SupplierDeleteTest.class);
	private int stock_method;
	private JSONArray permissionArray;
	private List<Category2Bean> category2Array;
	private JSONArray merchandise;
	private String supplier_id;
	private String supplier_name;
	private String customer_id;
	private String in_stock_sheet_id;
	private String return_stock_sheet_id;
	private String purchaser_id;
	private SkuBean sku;
	private String refund_order_id;
	private String purchase_order_id;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");

	private String station_id;

	private SupplierService supplierService;
	private CategoryService categoryService;
	private PurchaserService purchaserService;
	private StockService stockService;
	private OrderService orderService;
	private InStockService inStockService;
	private StockCheckService stockCheckService;
	private ReturnStockService returnStockService;
	private RefundStockService refundStockService;
	private OrderTool orderTool;
	private StockSummaryService stockSummaryService;
	private PurchaseTaskTool purchaseTaskTool;
	private StockRecordService stockRecordService;
	private SupplierFinanceService supplierFinanceService;

	private LoginUserInfoService loginUserInfoService;

	@BeforeClass
	public void initData() {
		try {
			Map<String, String> headers = getStationCookie();
			supplierService = new SupplierServiceImpl(headers);
			categoryService = new CategoryServiceImpl(headers);
			purchaserService = new PurchaserServiceImpl(headers);
			stockService = new StockServiceImpl(headers);
			orderService = new OrderServiceImpl(headers);
			orderTool = new OrderTool(headers);
			inStockService = new InStockServiceImpl(headers);
			refundStockService = new RefundStockServiceImpl(headers);
			returnStockService = new ReturnStockServiceImpl(headers);
			stockCheckService = new StockCheckServiceImpl(headers);
			purchaseTaskTool = new PurchaseTaskTool(headers);
			loginUserInfoService = new LoginUserInfoServiceImpl(headers);
			stockSummaryService = new StockSummaryServiceImpl(headers);
			stockRecordService = new StockRecordServiceImpl(headers);
			supplierFinanceService = new SupplierFinanceServiceImpl(headers);

			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录用户相关信息失败");
			permissionArray = loginUserInfo.getUser_permission();
			boolean result = permissionArray != null && permissionArray.contains("delete_settle_supplier");
			Assert.assertEquals(result, true, "此站点没有删除供应商的权限,所以测试用例不执行");

			stock_method = loginUserInfo.getStock_method();
			station_id = loginUserInfo.getStation_id();

			category2Array = categoryService.getCategory2List();
			merchandise = new JSONArray();
			for (Category2Bean category2 : category2Array) {
				merchandise.add(category2.getId());
			}

			String customer_id = "No" + StringUtil.getRandomNumber(6);
			supplier_name = customer_id;
			SupplierDetailBean supplier = new SupplierDetailBean(customer_id, supplier_name, merchandise, 1);
			supplier_id = supplierService.createSupplier(supplier);
			Assert.assertNotEquals(supplier_id, null, "新建供应商,断言成功");

		} catch (Exception e) {
			logger.error("新建供应商操作遇到错误: ", e);
			Assert.fail("新建供应商操作遇到错误: ", e);
		}
	}

	@Test(priority = 1)
	public void supplierDeleteTestCase01() {
		try {
			ReporterCSS.title("测试点:使用新建的供应商进行采购入库和采购退货");
			Map<String, List<SupplySkuBean>> supplySkusMap = stockService.newSearchSupplySku("d", supplier_id);
			Assert.assertNotEquals(supplySkusMap, null, "搜索入库商品失败");

			List<SupplySkuBean> targetSupplySkus = supplySkusMap.get("target");
			List<SupplySkuBean> otherSupplySkus = supplySkusMap.get("other");

			targetSupplySkus.addAll(otherSupplySkus);

			Assert.assertEquals(targetSupplySkus.size() >= 1, true, "搜索入库商品结果为空,无法进行入库操作");

			List<SupplySkuBean> selectedSupplySkus = NumberUtil.roundNumberInList(targetSupplySkus, 8);

			InStockCreateParam inStockCreateParam = new InStockCreateParam();

			List<InStockCreateParam.Detail> details = new ArrayList<InStockCreateParam.Detail>();
			InStockCreateParam.Detail detail = null;
			BigDecimal sku_money = new BigDecimal("0");
			List<String> purcahseSpec_ids = new ArrayList<String>();
			for (SupplySkuBean supplySku : selectedSupplySkus) {
				detail = inStockCreateParam.new Detail();
				String purchaseSpec_id = supplySku.getSku_id();
				purcahseSpec_ids.add(purchaseSpec_id);

				// 获取指定供应商和指定采购规格的入库均价
				BigDecimal supplier_avg_price = inStockService.getSupplieraveragePrice(purchaseSpec_id, supplier_id);
				Assert.assertNotEquals(supplier_avg_price, null,
						"获取采购规格 " + purchaseSpec_id + " 在此供应商 " + supplier_id + " 对应的入库均价失败");

				String parchase_name = supplySku.getSku_name();
				String parchase_std_unit = supplySku.getStd_unit_name();
				BigDecimal purchase_ratio = supplySku.getSale_ratio();
				String purchase_unit = supplySku.getSale_unit_name();

				detail.setBatch_number(StringUtil.getRandomString(12).toUpperCase());
				detail.setName(parchase_name);
				String displayName = parchase_name + "(" + purchase_ratio + parchase_std_unit + "/" + purchase_unit
						+ ")";
				detail.setDisplayName(displayName);
				detail.setId(purchaseSpec_id);
				detail.setCategory(supplySku.getCategory_id_2_name());
				detail.setSpu_id(supplySku.getSpu_id());
				detail.setPurchase_unit(purchase_unit);
				detail.setStd_unit(parchase_std_unit);
				detail.setRatio(purchase_ratio);
				BigDecimal quantity = NumberUtil.getRandomNumber(10, 20, 1);
				detail.setQuantity(quantity);
				detail.setPurchase_unit_quantity(quantity.divide(purchase_ratio, 4, BigDecimal.ROUND_HALF_UP));

				// 有入库均价就取均价,没有就随机取
				BigDecimal unit_price = supplier_avg_price.compareTo(BigDecimal.ZERO) == 1 ? supplier_avg_price
						: NumberUtil.getRandomNumber(5, 10, 1);

				detail.setUnit_price(unit_price);
				detail.setPurchase_unit_price(unit_price.multiply(purchase_ratio));
				BigDecimal money = unit_price.multiply(quantity).setScale(2, BigDecimal.ROUND_HALF_UP);
				detail.setMoney(money);

				detail.setIs_arrival(NumberUtil.roundNumberInList(Arrays.asList(0, 1)));
				sku_money = sku_money.add(money);
				details.add(detail);
				if (details.size() >= 10) {
					break;
				}
			}

			inStockCreateParam.setDetails(details);
			inStockCreateParam.setSku_money(sku_money);
			inStockCreateParam.setSettle_supplier_id(supplier_id);
			inStockCreateParam.setSupplier_name(supplier_name);
			inStockCreateParam.setSubmit_time_new(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm"));
			inStockCreateParam.setDiscount(new ArrayList<>());
			inStockCreateParam.setShare(new ArrayList<>());
			inStockCreateParam.setDelta_money(new BigDecimal("0"));
			inStockCreateParam.setIs_submit(2);
			inStockCreateParam.setRemark(StringUtil.getRandomString(6));

			// 金额折让
			inStockCreateParam.setDiscount(new ArrayList<>());
			inStockCreateParam.setDelta_money(new BigDecimal("0"));

			in_stock_sheet_id = inStockService.createInStockSheet(inStockCreateParam);
			Assert.assertNotEquals(in_stock_sheet_id, null, "新版UI,新建采购入库单失败");

			// 新建采购退货单
			return_stock_sheet_id = returnStockService.createReturnStockSheet(supplier_id, supplier_name);
			Assert.assertNotEquals(return_stock_sheet_id, null, "新建采购退货单失败");

			// 加权平均和先进先出退货方式不同
			ReturnStockDetailBean returnStock = returnStockService.getRetrunStockDetail(return_stock_sheet_id);
			Assert.assertNotEquals(returnStock, null, "获取采购退货单" + return_stock_sheet_id + "详情失败");
			List<ReturnStockDetailBean.Detail> returnStockDetails = new ArrayList<ReturnStockDetailBean.Detail>();
			ReturnStockDetailBean.Detail returnStockDetail = null;
			sku_money = new BigDecimal("0");
			BigDecimal money = null;
			BigDecimal unit_price = null;
			BigDecimal quantity = null;
			if (stock_method == 1) {
				for (SupplySkuBean supplySku : selectedSupplySkus) {
					returnStockDetail = returnStock.new Detail();
					returnStockDetail.setCategory(supplySku.getCategory_id_2_name());
					returnStockDetail.setId(supplySku.getSku_id());
					returnStockDetail.setName(supplySku.getSku_name());
					returnStockDetail.setSpu_id(supplySku.getSpu_id());
					returnStockDetail.setStd_unit(supplySku.getStd_unit_name());
					unit_price = NumberUtil.getRandomNumber(4, 8, 1);
					returnStockDetail.setUnit_price(unit_price);
					quantity = NumberUtil.getRandomNumber(4, 8, 0);
					returnStockDetail.setQuantity(quantity);
					money = unit_price.multiply(quantity);
					returnStockDetail.setMoney(money);
					sku_money = sku_money.add(money);
					returnStockDetails.add(returnStockDetail);
					if (returnStockDetails.size() >= 6) {
						break;
					}
				}
			} else {
				List<ReturnStockBatchBean> returnStockBatchList = null;
				String purchase_spec__id = null;
				for (SupplySkuBean supplySku : selectedSupplySkus) {
					returnStockDetail = returnStock.new Detail();
					purchase_spec__id = supplySku.getSku_id();
					returnStockBatchList = returnStockService.searchReturnStockBatch(purchase_spec__id, supplier_id);

					Assert.assertNotEquals(returnStockBatchList, null, "先进先出,搜索过滤成品退货商品批次信息失败");

					Assert.assertEquals(returnStockBatchList.size() > 0, true,
							"采购规格" + purchase_spec__id + "无对应批次,与预期结果不符");

					// 有商户退货的批次就使用商户退货的批次进行退货
					ReturnStockBatchBean returnStockBatch = returnStockBatchList.stream()
							.filter(s -> s.getBatch_number().startsWith("THRKP")).findFirst().orElse(null);
					if (returnStockBatch == null) {
						returnStockBatch = NumberUtil.roundNumberInList(returnStockBatchList);
					}

					String batch_number = returnStockBatch.getBatch_number();
					returnStockDetail.setName(supplySku.getSku_name());
					returnStockDetail.setId(purchase_spec__id);
					returnStockDetail.setSpu_id(supplySku.getSpu_id());
					returnStockDetail.setCategory(supplySku.getCategory_id_2_name());

					StockBatchBean stockBatch = stockCheckService.getStockBatch(batch_number);
					Assert.assertNotEquals(stockBatch, null, "获取批次  " + batch_number + " 详细信息失败");
					unit_price = stockBatch.getPrice();
					returnStockDetail.setUnit_price(unit_price);
					returnStockDetail.setStd_unit(supplySku.getStd_unit_name());
					BigDecimal remain = returnStockBatch.getRemain();
					returnStockDetail.setQuantity(remain);
					returnStockDetail.setMoney(unit_price.multiply(remain));
					returnStockDetail.setBatch_number(batch_number);
					returnStockDetail.setOperator("自动化");
					returnStockDetails.add(returnStockDetail);
					money = unit_price.multiply(remain);
					sku_money = sku_money.add(money);
					if (returnStockDetails.size() >= 6) {
						break;
					}
				}
			}

			returnStock.setSku_money(sku_money);
			String submit_time = TimeUtil.getCurrentTime("yyyy-MM-dd"); // 提交时间
			String return_stock_time = TimeUtil.calculateTime("yyyy-MM-dd", submit_time, -1, Calendar.DATE);
			returnStock.setSubmit_time(return_stock_time);
			returnStock.setDetails(returnStockDetails);
			returnStock.setDiscounts(new ArrayList<>());
			returnStock.setReturn_sheet_remark("");

			boolean result = returnStockService.modifyReturnStockSheet(returnStock);
			Assert.assertEquals(result, true, "提交成品退货单" + return_stock_sheet_id + "失败");

			returnStock = returnStockService.getRetrunStockDetail(return_stock_sheet_id);
			Assert.assertNotEquals(returnStock, null, "获取采购退货单 " + return_stock_sheet_id + " 失败");
		} catch (Exception e) {
			logger.error("新建采购入库、采购入库单遇到错误: ", e);
			Assert.fail("新建采购入库、采购入库单遇到错误: ", e);
		}
	}

	@Test(priority = 2)
	public void supplierDeleteTestCase02() {
		try {
			ReporterCSS.title("测试点: 采购员绑定新建的供应商");
			List<PurchaserBean> purchasers = purchaserService.searchPurchaser("");
			Assert.assertNotEquals(purchasers, null, "搜索查询采购员失败");

			if (purchasers.size() == 0) {
				String username = "AT" + StringUtil.getRandomNumber(6);
				String password = "123456";
				String phone = "911" + StringUtil.getRandomNumber(8);
				List<String> settle_suppliers = new ArrayList<String>();
				settle_suppliers.add(supplier_id);

				PurchaserParam purchaserParam = new PurchaserParam();
				purchaserParam = new PurchaserParam();
				purchaserParam.setUsername(username);
				purchaserParam.setName(username);
				purchaserParam.setPhone(phone);
				purchaserParam.setPassword(password);
				purchaserParam.setSettle_suppliers(settle_suppliers);
				purchaserParam.setIs_allow_login(0);
				purchaserParam.setStatus(1);

				PurchaserResponseBean purchaserResponse = purchaserService.createPurchaser(purchaserParam);
				Assert.assertEquals(purchaserResponse.getMsg(), "ok", "新建采购员失败");
				purchaser_id = purchaserResponse.getData();
			} else {
				PurchaserBean purchaser = NumberUtil.roundNumberInList(purchasers);
				purchaser_id = purchaser.getId();
				List<String> settle_suppliers = new ArrayList<String>();
				purchaser.getSettle_suppliers().stream().forEach(s -> settle_suppliers.add(s.getId()));
				settle_suppliers.add(supplier_id);

				PurchaserParam purchaserParam = new PurchaserParam();
				purchaserParam.setId(purchaser.getId());
				purchaserParam.setName(purchaser.getName());
				purchaserParam.setUsername(purchaser.getUsername());
				purchaserParam.setPassword("123456");
				purchaserParam.setPhone(purchaser.getPhone());
				purchaserParam.setStatus(purchaser.getStatus());
				purchaserParam.setIs_allow_login(purchaser.getIs_allow_login());
				purchaserParam.setSettle_suppliers(settle_suppliers);

				PurchaserResponseBean purchaserResponse = purchaserService.updatePurchaser(purchaserParam);
				Assert.assertEquals(purchaserResponse.getMsg(), "ok", "修改采购员信息失败");
			}
		} catch (Exception e) {
			logger.error("采购员绑定供应商遇到错误: ", e);
			Assert.fail("采购员绑定供应商遇到错误: ", e);
		}
	}

	@Test(priority = 3)
	public void supplierDeleteTestCase03() {
		ReporterCSS.title("测试点: 新建商户退货,二次入库使用新建的供应商进行退换入库操作");
		OrderDetailBean orderDetail = null;
		try {
			refund_order_id = orderTool.oneStepCreateOrder(6);
			Assert.assertNotEquals(refund_order_id, null, "新建订单失败");

			orderDetail = orderService.getOrderDetailById(refund_order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + refund_order_id + "详细信息失败");

		} catch (Exception e) {
			logger.error("创建订单过程中遇到错误: ", e);
			Assert.fail("创建订单过程中遇到错误: ", e);
		}

		// 选取一个商品,添加商品退货
		List<OrderDetailBean.Detail> details = orderDetail.getDetails();

		OrderDetailBean.Detail orderSku = NumberUtil.roundNumberInList(details);
		BigDecimal request_amount = orderSku.getStd_real_quantity().subtract(new BigDecimal("1"));
		String sku_id = orderSku.getSku_id();

		try {
			OrderRefundParam orderRund = new OrderRefundParam(request_amount, sku_id, station_id, 1, 1);
			List<OrderRefundParam> orderRundArray = new ArrayList<OrderRefundParam>();
			orderRundArray.add(orderRund);

			boolean result = orderService.addOrderException(refund_order_id, new ArrayList<OrderExceptionParam>(),
					orderRundArray);
			Assert.assertEquals(result, true, "订单添加退货处理失败");
		} catch (Exception e) {
			logger.error("订单添加退货处理遇到错误: ", e);
			Assert.fail("订单添加退货处理遇到错误: ", e);
		}

		RefundStockFilterParam refundFilterParam = new RefundStockFilterParam();
		refundFilterParam.setDate_from(todayStr);
		refundFilterParam.setDate_end(todayStr);
		refundFilterParam.setOrder_id(refund_order_id);
		refundFilterParam.setPage(0);
		refundFilterParam.setNum(20);

		try {
			List<RefundStockResultBean> refundList = refundStockService.searchRefundStock(refundFilterParam);
			RefundStockResultBean refundResult = refundList.stream()
					.filter(r -> r.getOrder_id().equals(refund_order_id)).findAny().orElse(null);
			Assert.assertNotEquals(refundResult, null, "订单 " + refund_order_id + " 添加的退货请求在商户退货列表没有找到");

			List<PurchaseSkuSettleSupplier> purchaseSkuSettleSupplierList = refundStockService
					.getPurchaseSkuSettleSupplierList(refundResult.getPurchase_sku_id());

			Assert.assertNotEquals(purchaseSkuSettleSupplierList, null, "获取采购规格对应的供应商列表失败");

			Assert.assertEquals(purchaseSkuSettleSupplierList.size() > 0, true,
					refundResult.getPurchase_sku_id() + "此采购规格没有对应的供应商,无法进行后续操作");

			RefundStockParam refundStockParam = new RefundStockParam();
			refundStockParam.setRefund_id(refundResult.getRefund_id());
			refundStockParam.setSku_name(refundResult.getSku_name());
			refundStockParam.setSku_id(refundResult.getSku_id());
			refundStockParam.setSolution(160);
			refundStockParam.setDriver_id(0);
			refundStockParam.setIn_stock_price(NumberUtil.getRandomNumber(200, 400, 2));
			refundStockParam.setDisabled_in_stock_price(true);
			refundStockParam.setReal_amount(refundResult.getRequest_amount());
			refundStockParam.setStore_amount(refundResult.getRequest_amount());
			refundStockParam.setRequest_amount(refundResult.getRequest_amount());
			refundStockParam.setDescription("自动化测试");
			refundStockParam.setShelf_name(null);
			refundStockParam.setShelf_id(null);
			refundStockParam.setSupplier_id(supplier_id);
			refundStockParam.setSupplier_name(supplier_name);
			refundStockParam.setSale_ratio(refundResult.getSale_ratio());
			refundStockParam.setPurchase_sku_id(refundResult.getPurchase_sku_id());

			List<RefundStockParam> refundStockParams = new ArrayList<RefundStockParam>();
			refundStockParams.add(refundStockParam);
			boolean result = refundStockService.editRefundStock(refundStockParams);
			Assert.assertEquals(result, true, "商户退货入库处理失败");
		} catch (Exception e) {
			logger.error("商户退货处理遇到错误: ", e);
			Assert.fail("商户退货处理遇到错误: ", e);
		}
	}

	@Test(priority = 4)
	public void supplierDeleteTestCase04() {
		try {
			ReporterCSS.title("测试点: 销售SKU绑定新的供应商,并对其下单");
			List<CustomerBean> customerArray = orderService.getOrderCustomerArray(6);
			Assert.assertNotEquals(customerArray, null, "获取下单商户列表信息失败");

			Assert.assertEquals(customerArray.size() > 0, true, "无可用下单商户");

			// 随机选取一个正常商户进行下单
			CustomerBean customer = NumberUtil.roundNumberInList(customerArray);
			String address_id = customer.getAddress_id();
			String uid = customer.getId();

			List<OrderReceiveTimeBean> orderReceiveTimes = orderService.getCustomerServiceTimeArray(address_id);
			Assert.assertNotEquals(orderReceiveTimes, null, "获取下单商户对应的运营时间失败");

			Assert.assertEquals(orderReceiveTimes.size() > 0, true, "商户" + address_id + "没有绑定运营时间,无法进行下单操作");
			// 随机取一个绑定的运营时间
			OrderReceiveTimeBean orderReceiveTime = NumberUtil.roundNumberInList(orderReceiveTimes);
			String time_config_id = orderReceiveTime.getTime_config_id();

			Assert.assertEquals(orderReceiveTime.getReceive_times().size() > 0, true,
					"受收货自然日限制,运营时间" + time_config_id + "无可用收货日期可选");

			// 下单商品集合
			String[] search_texts = new String[] { "A", "B", "C" };
			OrderCreateParam orderCreateParam = orderService.searchOrderSkus(address_id, time_config_id, search_texts,
					10);
			Assert.assertEquals(orderCreateParam != null && orderCreateParam.getDetails().size() > 0, true,
					"下单搜索搜商品列表为空");

			List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();
			OrderCreateParam.OrderSku orderSku = NumberUtil.roundNumberInList(orderSkus);
			String spu_id = orderSku.getSpu_id();
			String sku_id = orderSku.getSku_id();
			sku = categoryService.getSaleSkuById(spu_id, sku_id);
			Assert.assertNotEquals(sku, null, "获取销售SKU详细信息失败");
			sku.setSupplier_id(supplier_id);

			boolean result = categoryService.updateSaleSku(sku);
			Assert.assertEquals(result, true, "修改销售SKU信息失败");

			List<OrderReceiveTimeBean.ReceiveTime> receiveTimes = orderReceiveTime.getReceive_times().stream()
					.filter(r -> r.getTimes().size() >= 2).collect(Collectors.toList());
			Assert.assertEquals(receiveTimes.size() > 0, true, "运营时间的收货时间无法取值了(当前时间过了收货时间结束时间或者收货起始和收货结束时间一致)");

			OrderReceiveTimeBean.ReceiveTime receiveTime = NumberUtil.roundNumberInList(receiveTimes);
			List<String> receive_times = receiveTime.getTimes();
			int index = new Random().nextInt(receive_times.size() - 1);
			String receive_begin_time = receive_times.get(index);
			String receive_end_time = receive_times.get(index + 1);

			// 下单对象
			orderCreateParam.setAddress_id(address_id);
			orderCreateParam.setUid(uid);
			orderCreateParam.setReceive_begin_time(receive_begin_time);
			orderCreateParam.setReceive_end_time(receive_end_time);
			orderCreateParam.setTime_config_id(time_config_id);
			orderCreateParam.setRemark(StringUtil.getRandomString(6));
			orderCreateParam.setForce(1);

			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);
			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				if (notEnoughInventoriesList.size() > 0) {
					Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam
							.getCombine_goods_map();
					orderSkus = orderCreateParam.getDetails();
					for (NotEnoughInventories skuObj : notEnoughInventoriesList) {
						String temp_sku_id = skuObj.getSku_id();

						// 首先找出这个SKU有没有在组合商品里
						List<String> combine_goods_ids = orderSkus.stream()
								.filter(s -> s.getSku_id().equals(temp_sku_id) && s.isIs_combine_goods())
								.map(s -> s.getCombine_goods_id()).distinct().collect(Collectors.toList());

						// 先把这个商品过滤掉
						orderSkus = orderSkus.stream().filter(s -> !s.getSku_id().equals(temp_sku_id))
								.collect(Collectors.toList());

						// 过滤掉组合商品里的其他商品
						for (String combine_goods_id : combine_goods_ids) {
							combine_goods_map.remove(combine_goods_id);
							orderSkus = orderSkus.stream().filter(
									s -> s.isIs_combine_goods() && !s.getCombine_goods_id().equals(combine_goods_id))
									.collect(Collectors.toList());
						}
					}
					Assert.assertEquals(orderSkus.size() > 0, true, "下单商品列表为空,无法进行下单");
					orderCreateParam.setDetails(orderSkus);
					orderResponse = orderService.createOrder(orderCreateParam);
				}
				Assert.assertEquals(orderResponse.getData().getNew_order_ids().size() > 0, true, "正常下单,断言成功");
				purchase_order_id = orderResponse.getData().getNew_order_ids().getString(0);
			} else {
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}
		} catch (Exception e) {
			logger.error("销售SKU绑定新的供应商,并对其下单过程中遇到错误 : ", e);
			Assert.fail("销售SKU绑定新的供应商,并对其下单过程中遇到错误 : ", e);
		}
	}

	@Test(dependsOnMethods = { "supplierDeleteTestCase01", "supplierDeleteTestCase02", "supplierDeleteTestCase03",
			"supplierDeleteTestCase04" }, alwaysRun = true)
	public void supplierDeleteTestCase99() {
		try {
			ReporterCSS.title("测试点: 删除供应商");
			boolean result = supplierService.deleteSupplier(supplier_id);
			Assert.assertEquals(result, true, "删除供应商,断言成功");

			List<SupplierBean> suppliers = supplierService.searchSupplier(customer_id);
			Assert.assertNotEquals(suppliers, null, "搜索查询供应商失败");

			SupplierBean supplier = suppliers.stream().filter(s -> s.getSupplier_id().equals(supplier_id)).findAny()
					.orElse(null);
			Assert.assertEquals(supplier, null, "删除的供应商还显示在供应商列表");
		} catch (Exception e) {
			logger.error("删除供应商遇到错误: ", e);
			Assert.fail("删除供应商遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "supplierDeleteTestCase01", "supplierDeleteTestCase99" })
	public void supplierDeleteTestCase11() {
		try {
			ReporterCSS.title("测试点: 删除供应商后,搜索查询采购入库单");
			String start_date_new = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00");
			String end_date_new = TimeUtil.calculateTime("yyyy-MM-dd 00:00", start_date_new, 1, Calendar.DATE);

			InStockSheetFilterParam stockInSheetFilterParam = new InStockSheetFilterParam();
			stockInSheetFilterParam.setSearch_type(1);
			stockInSheetFilterParam.setType(2);
			stockInSheetFilterParam.setStatus(5);
			stockInSheetFilterParam.setSearch_text(in_stock_sheet_id);
			stockInSheetFilterParam.setStart_date_new(start_date_new);
			stockInSheetFilterParam.setEnd_date_new(end_date_new);

			List<InStockSheetBean> stockInSheets = inStockService.searchInStockSheet(stockInSheetFilterParam);
			Assert.assertNotEquals(stockInSheets, null, "删除供应商后,搜索查询采购入库单失败");

			InStockSheetBean stockInSheet = stockInSheets.stream().filter(s -> s.getId().equals(in_stock_sheet_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(stockInSheet, null, "删除供应商后,之前的采购入库单搜索不到了");

			Assert.assertEquals(stockInSheet.getSupplier_status() != null && stockInSheet.getSupplier_status() == 0,
					true, "供应商删除后,采购入库单列表对应的采购入库单里的供应商状态没有标记成删除状态");
		} catch (Exception e) {
			logger.error("删除供应商后,搜索查询采购入库单遇到错误: ", e);
			Assert.fail("删除供应商后,搜索查询采购入库单遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "supplierDeleteTestCase01", "supplierDeleteTestCase99" })
	public void supplierDeleteTestCase12() {
		try {
			ReporterCSS.title("测试点: 删除供应商后,获取采购入库单详情");
			InStockDetailInfoBean stockInDetailInfo = inStockService.getInStockSheetDetail(in_stock_sheet_id);
			Assert.assertNotEquals(stockInDetailInfo, null, "删除供应商后,采购入库单详情获取失败");

			Assert.assertEquals(
					stockInDetailInfo.getSupplier_status() != null && stockInDetailInfo.getSupplier_status() == 0, true,
					"删除供应商后,对应的采购入库单里的供应商状态没有标记成删除状态");
		} catch (Exception e) {
			logger.error("删除供应商后,采购入库单详情获取遇到错误: ", e);
			Assert.fail("删除供应商后,采购入库单详情获取遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "supplierDeleteTestCase01", "supplierDeleteTestCase99" })
	public void supplierDeleteTestCase13() {
		try {
			ReporterCSS.title("测试点: 删除供应商后,查询采购退换单");
			ReturnStockSheetFilterParam filterParam = new ReturnStockSheetFilterParam();
			filterParam.setType(1);
			filterParam.setStatus(5);
			filterParam.setStart(todayStr);
			filterParam.setEnd(todayStr);
			filterParam.setSearch_text(return_stock_sheet_id);
			filterParam.setOffset(0);
			filterParam.setLimit(10);

			List<ReturnStockSheetBean> returnStocks = returnStockService.searchReturnStockSheet(filterParam);
			Assert.assertNotEquals(returnStocks, null, "删除供应商后,查询采购退货单列表失败");

			ReturnStockSheetBean returnStock = returnStocks.stream()
					.filter(s -> s.getId().equals(return_stock_sheet_id)).findAny().orElse(null);
			Assert.assertNotEquals(returnStock, null, "删除供应商后,此供应商对应的采购退货单没有查询到");

			Assert.assertEquals(returnStock.getSupplier_status() != null && returnStock.getSupplier_status() == 0, true,
					"删除供应商后,采购退货单列表里的对应的退货单没有标记供应商删除");

		} catch (Exception e) {
			logger.error("删除供应商后,采购退货单详情获取遇到错误: ", e);
			Assert.fail("删除供应商后,采购退货单详情获取遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "supplierDeleteTestCase01", "supplierDeleteTestCase99" })
	public void supplierDeleteTestCase14() {
		try {
			ReporterCSS.title("测试点: 删除供应商后,获取采购退货单详情");
			ReturnStockDetailBean returnStock = returnStockService.getRetrunStockDetail(return_stock_sheet_id);
			Assert.assertNotEquals(returnStock, null, "删除供应商后,获取采购退货单详情失败");

			Assert.assertEquals(returnStock.getSupplier_status() != null && returnStock.getSupplier_status() == 0, true,
					"删除供应商后,获取采购退货单详情里的供应商状态没有标记成删除状态");
		} catch (Exception e) {
			logger.error("删除供应商后,获取采购退货单详情遇到错误: ", e);
			Assert.fail("删除供应商后,获取采购退货单详情遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "supplierDeleteTestCase02", "supplierDeleteTestCase99" })
	public void supplierDeleteTestCase16() {
		try {
			ReporterCSS.title("测试点: 删除供应商后,查询采购员列表信息");
			List<PurchaserBean> purchasers = purchaserService.searchPurchaser("");
			Assert.assertNotEquals(purchasers, null, "搜索查询采购员失败");

			PurchaserBean purchaser = purchasers.stream().filter(p -> p.getId().equals(purchaser_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(purchaser, null, "删除供应商后,之前绑定此供应商的采购员没有查询到");

			PurchaserBean.SettleSupplier settleSupplier = purchaser.getSettle_suppliers().stream()
					.filter(s -> s.getId().equals(supplier_id)).findAny().orElse(null);
			Assert.assertEquals(settleSupplier, null, "删除供应商后,之前绑定此供应商的采购员没有进行解绑");
		} catch (Exception e) {
			logger.error("删除供应商后,查询采购员列表信息遇到错误: ", e);
			Assert.fail("删除供应商后,查询采购员列表信息遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "supplierDeleteTestCase02", "supplierDeleteTestCase99" })
	public void supplierDeleteTestCase17() {
		try {
			ReporterCSS.title("测试点: 删除供应商后,获取采购员详细信息");
			PurchaserBean purchaser = purchaserService.getPurchaserDetail(purchaser_id);
			Assert.assertNotEquals(purchaser, null, "删除供应商后,获取采购员详细信息失败");

			PurchaserBean.SettleSupplier settleSupplier = purchaser.getSettle_suppliers().stream()
					.filter(s -> s.getId().equals(supplier_id)).findAny().orElse(null);
			Assert.assertEquals(settleSupplier, null, "删除供应商后,之前绑定此供应商的采购员没有进行解绑");
		} catch (Exception e) {
			logger.error("删除供应商后,获取采购员详细信息遇到错误: ", e);
			Assert.fail("删除供应商后,获取采购员详细信息遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "supplierDeleteTestCase03", "supplierDeleteTestCase99" })
	public void supplierDeleteTestCase18() {
		try {
			ReporterCSS.title("测试点: 删除供应商后,查询商户退货入库信息");
			RefundStockFilterParam refundFilterParam = new RefundStockFilterParam();
			refundFilterParam.setDate_from(todayStr);
			refundFilterParam.setDate_end(todayStr);
			refundFilterParam.setOrder_id(refund_order_id);
			refundFilterParam.setPage(0);
			refundFilterParam.setNum(20);

			List<RefundStockResultBean> refundList = refundStockService.searchRefundStock(refundFilterParam);
			Assert.assertNotEquals(refundList, null, "删除供应商后,查询商户退货入库信息失败");
		} catch (Exception e) {
			logger.error("删除供应商后,查询商户退货入库信息遇到错误: ", e);
			Assert.fail("删除供应商后,查询商户退货入库信息遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "supplierDeleteTestCase04", "supplierDeleteTestCase99" })
	public void supplierDeleteTestCase19() {
		try {
			ReporterCSS.title("测试点: 删除供应商后,查询采购任务");
			List<PurchaseTaskData> purchaseTaskDataList = purchaseTaskTool.getOrderPurcahseTask(purchase_order_id);
			Assert.assertEquals(purchaseTaskDataList != null && purchaseTaskDataList.size() > 0, true,
					"订单" + purchase_order_id + "的采购任务在1分钟内没有生成");

			String purchase_spec_id = sku.getPurchase_spec_id();
			PurchaseTaskData purchaseTaskData = purchaseTaskDataList.stream()
					.filter(p -> p.getSpec_id().equals(purchase_spec_id)).findAny().orElse(null);

			String msg = null;
			if (purchaseTaskData == null) {
				msg = String.format("采购任务中没有找到订单%s中商品[%s,%s]对应的采购任务", purchase_order_id, sku.getId(), sku.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				Assert.assertNotEquals(purchaseTaskData, null, msg);
			}

			if (!purchaseTaskData.getSettle_supplier_id().equals(supplier_id)) {
				msg = String.format("采购任务中没有找到订单%s中商品[%s,%s]对应的采购任务的供应商不是%s,与预期不符,猜测是设置了优先供应商", purchase_order_id,
						sku.getId(), sku.getName(), supplier_name);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				Assert.assertEquals(purchaseTaskData.getSettle_supplier_id(), supplier_id, msg);
			}

			String supplier_status = purchaseTaskData.getSupplier_status();
			if (supplier_status == null) {
				msg = String.format("采购任务中没有找到订单%s中商品[%s,%s]对应的采购任务的供应商%s的状态没有标记为已删除", purchase_order_id, sku.getId(),
						sku.getName(), supplier_name);
				ReporterCSS.warn(msg);
				logger.warn(msg);
			} else {
				if (!purchaseTaskData.getSupplier_status().equals("0")) {
					msg = String.format("采购任务中没有找到订单%s中商品[%s,%s]对应的采购任务的供应商%s的状态没有标记为已删除", purchase_order_id,
							sku.getId(), sku.getName(), supplier_name);
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}
			}
			Assert.assertEquals(supplier_status != null && purchaseTaskData.getSupplier_status().equals("0"), true,
					msg);
		} catch (Exception e) {
			logger.error("删除供应商后,查询商户退货入库信息遇到错误: ", e);
			Assert.fail("删除供应商后,查询商户退货入库信息遇到错误: ", e);
		} finally {
			try {
				boolean result = categoryService.updateSaleSku(sku);
				Assert.assertEquals(result, true, "后置处理,修改销售SKU信息失败");
			} catch (Exception e) {
				logger.error("修改采购规格信息遇到错误: ", e);
				Assert.fail("修改采购规格信息遇到错误: ", e);
			}
		}
	}

	@Test(dependsOnMethods = { "supplierDeleteTestCase01", "supplierDeleteTestCase99" })
	public void supplierDeleteTestCase20() {
		ReporterCSS.title("测试点: 供应商删除后,查看出入库汇总信息");
		try {
			StockSummaryFilterParam stockSummaryFilterParam = new StockSummaryFilterParam();
			stockSummaryFilterParam.setBegin(todayStr);
			stockSummaryFilterParam.setEnd(todayStr);
			stockSummaryFilterParam.setLimit(50);
			stockSummaryFilterParam.setOffset(0);

			StockSummaryBean stockSummary = stockSummaryService.inStockSummaryBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummary, null, "入库汇总,按SPU查看统计数据失败");

			List<InStockSummarySpuDetailBean> stockSummaryDetailList = stockSummaryService
					.inStockSummaryDetailBySpu(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummaryDetailList, null, "入库汇总,按SPU查看详细数据失败");

			stockSummary = stockSummaryService.inStockSummaryByCategory(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummary, null, "入库汇总,按分类查看统计数据失败");

			List<StockSummaryCategoryDetailBean> stockSummaryCategoryDetailList = stockSummaryService
					.inStockSummaryDetailByCategory(stockSummaryFilterParam);
			Assert.assertNotEquals(stockSummaryCategoryDetailList, null, "入库汇总,按分类查看详细数据失败");
		} catch (Exception e) {
			logger.error("供应商删除后,查看出入库汇总信息遇到错误: ", e);
			Assert.fail("供应商删除后,查看出入库汇总信息遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "supplierDeleteTestCase01", "supplierDeleteTestCase99" })
	public void supplierDeleteTestCase21() {
		ReporterCSS.title("测试点: 供应商删除后,查询采购入库记录");
		try {
			InStockRecordFilterParam stockInRecordFilterParam = new InStockRecordFilterParam();
			stockInRecordFilterParam.setTime_type(1);
			stockInRecordFilterParam.setBegin(todayStr);
			stockInRecordFilterParam.setEnd(todayStr);
			stockInRecordFilterParam.setSettle_supplier_id(supplier_id);

			List<InStockRecordBean> stockInRecords = stockRecordService.inStockRecords(stockInRecordFilterParam);
			Assert.assertNotEquals(stockInRecords, null, "供应商删除后,查询采购入库记录失败");

			String msg = null;
			if (stockInRecords.size() == 0) {
				msg = String.format("供应商删除后,查询采购入库记录,没有查询到供应商%s的对应的入库记录", supplier_name);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				Assert.assertEquals(stockInRecords.size() > 0, true, msg);
			}

			Integer supplier_status = null;
			boolean result = true;
			for (InStockRecordBean stockInRecord : stockInRecords) {
				supplier_status = stockInRecord.getSupplier_status();
				if (supplier_status == null) {
					msg = String.format("供应商删除后,对应的采购入库记录中的供应商状态没有标记成已删除");
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				} else {
					if (supplier_status != 0) {
						msg = String.format("供应商删除后,对应的采购入库记录中的供应商状态没有标记成已删除");
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, msg);
		} catch (Exception e) {
			logger.error("供应商删除后,查看入库记录遇到错误: ", e);
			Assert.fail("供应商删除后,查看入库记录遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "supplierDeleteTestCase03", "supplierDeleteTestCase99" })
	public void supplierDeleteTestCase22() {
		ReporterCSS.title("测试点: 供应商删除后,查询退货入库记录");
		try {
			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);
			stockRecordParam.setOffset(0);
			stockRecordParam.setLimit(50);

			List<RefundStockRecordBean> refundRecordList = stockRecordService.refundRecords(stockRecordParam);
			Assert.assertNotEquals(refundRecordList, null, "获取商户退货入库记录失败");
		} catch (Exception e) {
			logger.error("供应商删除后,查询退货入库记录遇到错误: ", e);
			Assert.fail("供应商删除后,查询退货入库记录遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "supplierDeleteTestCase01", "supplierDeleteTestCase99" })
	public void supplierDeleteTestCase23() {
		ReporterCSS.title("测试点: 供应商删除后,查询供应商结算信息");
		try {
			SettleSheetFilterParam settleSheetFilterParam = new SettleSheetFilterParam();
			settleSheetFilterParam.setType(2);
			settleSheetFilterParam.setStart(todayStr);
			settleSheetFilterParam.setEnd(todayStr);
			settleSheetFilterParam.setReceipt_type(5);
			List<SettleSheetBean> stockSettleSheets = supplierFinanceService.searchSettleSheet(settleSheetFilterParam);
			Assert.assertNotEquals(stockSettleSheets, null, "搜索过滤结款单据失败");

			stockSettleSheets = stockSettleSheets.stream().filter(s -> s.getSettle_supplier_id().equals(supplier_id))
					.collect(Collectors.toList());

			String msg = null;
			if (stockSettleSheets.size() == 0) {
				msg = String.format("供应商删除后,查询供应商结算信息,没有找到之前的采购入库、采购退货单据信息");
				ReporterCSS.warn(msg);
				logger.warn(msg);
				Assert.assertEquals(stockSettleSheets.size() > 0, true, msg);
			}

			Integer supplier_status = null;
			boolean result = true;
			for (SettleSheetBean stockSettleSheet : stockSettleSheets) {
				supplier_status = stockSettleSheet.getSupplier_status();
				if (supplier_status == null) {
					msg = String.format("供应商删除后,查询供应商结算信息,单据%s对应的供应商状态没有打上已删除的标签", stockSettleSheet.getId());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				} else {
					if (supplier_status != 0) {
						msg = String.format("供应商删除后,查询供应商结算信息,单据%s对应的供应商状态没有打上已删除的标签", stockSettleSheet.getId());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, msg);
		} catch (Exception e) {
			logger.error("供应商删除后,查询供应商结算信息遇到错误: ", e);
			Assert.fail("供应商删除后,查询供应商结算信息遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "supplierDeleteTestCase01", "supplierDeleteTestCase99" })
	public void supplierDeleteTestCase24() {
		ReporterCSS.title("测试点: 供应商删除后,待处理单据加入结款单");
		try {
			List<String> sheet_nos = new ArrayList<String>();
			sheet_nos.add(in_stock_sheet_id);
			sheet_nos.add(return_stock_sheet_id);
			String sheet_no = supplierFinanceService.addSettleSheet(supplier_id, sheet_nos);
			Assert.assertNotEquals(sheet_no, null, "供应商删除后,待处理单据加入结款单失败");

			SettleSheetDetailFilterParam settleSheetDetailFilterParam = new SettleSheetDetailFilterParam();
			settleSheetDetailFilterParam.setStart(todayStr);
			settleSheetDetailFilterParam.setEnd(todayStr);
			settleSheetDetailFilterParam.setReceipt_type(5);

			List<SettleSheetDetailBean> settleSheetDetails = supplierFinanceService
					.searchSettleSheetDetail(settleSheetDetailFilterParam);
			Assert.assertNotEquals(settleSheetDetails, null, "搜索过滤结款单据失败");

			SettleSheetDetailBean settleSheetDetail = settleSheetDetails.stream()
					.filter(s -> s.getId().equals(sheet_no)).findAny().orElse(null);
			Assert.assertNotEquals(settleSheetDetail, null, "没有找到刚刚生成的结款单据 " + sheet_no);

			String msg = null;
			boolean result = true;
			Integer supplier_status = settleSheetDetail.getSupplier_status();
			if (supplier_status == null) {
				msg = String.format("结款单据%s供应商对应的状态没有标记成已删除", sheet_no);
				ReporterCSS.title(msg);
				logger.warn(msg);
				result = false;
			} else {
				if (supplier_status != 0) {
					msg = String.format("结款单据%s供应商对应的状态没有标记成已删除", sheet_no);
					ReporterCSS.title(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, msg);

			settleSheetDetail = supplierFinanceService.getSettleSheetDetail(sheet_no);
			Assert.assertNotEquals(settleSheetDetail, null, "获取结款单据 " + sheet_no + "详细信息失败");

			supplier_status = settleSheetDetail.getSupplier_status();
			if (supplier_status == null) {
				msg = String.format("结款单据%s详细供应商对应的状态没有标记成已删除", sheet_no);
				ReporterCSS.title(msg);
				logger.warn(msg);
				result = false;
			} else {
				if (supplier_status != 0) {
					msg = String.format("结款单据%s详细供应商对应的状态没有标记成已删除", sheet_no);
					ReporterCSS.title(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, msg);

			SettleSheetDetailSubmitParam submitParam = new SettleSheetDetailSubmitParam();
			submitParam.setDate_time(TimeUtil.getCurrentTime("yyyy-MM-dd hh:mm:ss"));
			submitParam.setDelta_money(new BigDecimal("0"));
			submitParam.setDiscount(new ArrayList<SettleSheetDetailSubmitParam.Discount>());
			submitParam.setSettle_supplier_id(settleSheetDetail.getSettle_supplier_id());
			submitParam.setSettle_supplier_name(settleSheetDetail.getSettle_supplier_name());
			submitParam.setSheet_nos(settleSheetDetail.getSheet_nos());
			submitParam.setStatus(0);
			submitParam.setTotal_price(settleSheetDetail.getTotal_price());
			submitParam.setRemark("自动化");
			submitParam.setId(settleSheetDetail.getId());
			submitParam.setStation_id(station_id);

			result = supplierFinanceService.submitSettleSheetDetail(submitParam);
			Assert.assertEquals(result, true, "提交采购单据失败");

			result = supplierFinanceService.markPayment(settleSheetDetail.getId(),
					StringUtil.getRandomString(8).toUpperCase(), settleSheetDetail.getTotal_price());

			Assert.assertEquals(result, true, "结款单据标记结款失败");
		} catch (Exception e) {
			logger.error("供应商删除后,待处理单据加入结款单遇到错误: ", e);
			Assert.fail("供应商删除后,待处理单据加入结款单遇到错误: ", e);
		}
	}

	@AfterTest
	public void afterTest() {
		try {
			List<SupplierBean> suppliers = supplierService.searchSupplier(customer_id);
			Assert.assertNotEquals(suppliers, null, "搜索查询供应商失败");

			SupplierBean supplier = suppliers.stream().filter(s -> s.getSupplier_id().equals(supplier_id)).findAny()
					.orElse(null);
			if (supplier != null) {
				boolean result = supplierService.deleteSupplier(supplier_id);
				Assert.assertEquals(result, true, "删除供应商失败");
			}
		} catch (Exception e) {
			logger.error("后置处理,删除供应商遇到错误: ", e);
			Assert.fail("后置处理,删除供应商遇到错误: ", e);
		}
	}

}
