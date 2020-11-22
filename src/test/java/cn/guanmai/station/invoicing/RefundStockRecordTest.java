package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.invoicing.PurchaseSkuSettleSupplier;
import cn.guanmai.station.bean.invoicing.RefundStockRecordBean;
import cn.guanmai.station.bean.invoicing.RefundStockResultBean;
import cn.guanmai.station.bean.invoicing.StockAbandonGoodsRecordBean;
import cn.guanmai.station.bean.invoicing.param.RefundStockFilterParam;
import cn.guanmai.station.bean.invoicing.param.StockRecordFilterParam;
import cn.guanmai.station.bean.invoicing.param.RefundStockParam;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.param.OrderExceptionParam;
import cn.guanmai.station.bean.order.param.OrderRefundParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.invoicing.RefundStockServiceImpl;
import cn.guanmai.station.impl.invoicing.StockRecordServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.invoicing.RefundStockService;
import cn.guanmai.station.interfaces.invoicing.StockRecordService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/**
 * @author liming
 * @date 2020年1月6日
 * @time 下午5:23:48
 * @des 商户退货入库记录查询
 */

public class RefundStockRecordTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(RefundStockRecordTest.class);
	private Map<String, String> headers;
	private RefundStockService refundStockService;
	private StockRecordService stockRecordService;
	private LoginUserInfoService loginUserInfoService;
	private OrderService orderService;
	private OrderTool orderTool;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private String order_id;
	private OrderDetailBean orderDetail;
	private List<RefundStockResultBean> refundStockList;

	@BeforeClass
	public void initData() {
		headers = getStationCookie();
		orderService = new OrderServiceImpl(headers);
		refundStockService = new RefundStockServiceImpl(headers);
		stockRecordService = new StockRecordServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		orderTool = new OrderTool(headers);
		try {
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账户信息失败");

			String station_id = loginUserInfo.getStation_id();

			Assert.assertNotEquals(station_id, null, "获取站点ID失败");

			order_id = orderTool.oneStepCreateOrder(4);
			Assert.assertNotEquals(order_id, null, "创建订单失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + "详细信息失败");

			List<OrderRefundParam> orderRefundArray = new ArrayList<OrderRefundParam>();
			for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
				BigDecimal request_amount = detail.getStd_real_quantity().subtract(new BigDecimal("1"));
				String sku_id = detail.getSku_id();

				OrderRefundParam orderRund = new OrderRefundParam(request_amount, sku_id, station_id, 1, 1);

				orderRefundArray.add(orderRund);
			}

			boolean result = orderService.addOrderException(order_id, new ArrayList<OrderExceptionParam>(),
					orderRefundArray);
			Assert.assertEquals(result, true, "订单" + order_id + "添加退货处理失败");

			RefundStockFilterParam refundStockFilterParam = new RefundStockFilterParam();
			refundStockFilterParam.setDate_from(todayStr);
			refundStockFilterParam.setDate_end(todayStr);
			refundStockFilterParam.setOrder_id(order_id);
			refundStockFilterParam.setPage(0);
			refundStockFilterParam.setNum(20);

			refundStockList = refundStockService.searchRefundStock(refundStockFilterParam);
			Assert.assertNotEquals(refundStockList, null, "商户退货列表查询搜索失败");

			Assert.assertEquals(refundStockList.size(), orderRefundArray.size(), "订单 " + order_id + "退货商品数与预期不一致");

			RefundStockResultBean resultRefund = null;
			for (int i = 0; i < refundStockList.size(); i++) {
				resultRefund = refundStockList.get(i);
				if (i % 2 == 0) {
					List<PurchaseSkuSettleSupplier> purchaseSkuSettleSupplierList = refundStockService
							.getPurchaseSkuSettleSupplierList(resultRefund.getPurchase_sku_id());

					Assert.assertNotEquals(purchaseSkuSettleSupplierList, null, "获取采购规格对应的供应商列表失败");

					Assert.assertEquals(purchaseSkuSettleSupplierList.size() > 0, true,
							resultRefund.getPurchase_sku_id() + "此采购规格没有对应的供应商,无法进行后续操作");

					PurchaseSkuSettleSupplier purchaseSkuSettleSupplier = purchaseSkuSettleSupplierList.get(0);

					RefundStockParam refundStockParam = new RefundStockParam();
					refundStockParam.setRefund_id(resultRefund.getRefund_id());
					refundStockParam.setSku_name(resultRefund.getSku_name());
					refundStockParam.setSku_id(resultRefund.getSku_id());
					refundStockParam.setSolution(160);
					refundStockParam.setDriver_id(0);
					// 入库单价为分
					refundStockParam.setIn_stock_price(new BigDecimal("200"));
					refundStockParam.setDisabled_in_stock_price(true);
					refundStockParam.setReal_amount(resultRefund.getRequest_amount());
					refundStockParam.setStore_amount(resultRefund.getRequest_amount());
					refundStockParam.setRequest_amount(resultRefund.getRequest_amount());
					refundStockParam.setDescription("自动化测试");
					refundStockParam.setShelf_name(null);
					refundStockParam.setShelf_id(null);
					refundStockParam.setSupplier_id(purchaseSkuSettleSupplier.getSupplier_id());
					refundStockParam.setSupplier_name(purchaseSkuSettleSupplier.getSupplier_name());
					refundStockParam.setSale_ratio(resultRefund.getSale_ratio());
					refundStockParam.setPurchase_sku_id(resultRefund.getPurchase_sku_id());

					List<RefundStockParam> paramList = new ArrayList<RefundStockParam>();
					paramList.add(refundStockParam);
					result = refundStockService.editRefundStock(paramList);
					Assert.assertEquals(result, true, "商户退货入库处理失败");
				} else {
					RefundStockParam refundStockParam = new RefundStockParam();
					refundStockParam.setRefund_id(resultRefund.getRefund_id());
					refundStockParam.setSku_name(resultRefund.getSku_name());
					refundStockParam.setSku_id(resultRefund.getSku_id());
					refundStockParam.setSolution(157);
					refundStockParam.setDriver_id(0);
					refundStockParam.setIn_stock_price(new BigDecimal("0"));
					refundStockParam.setDisabled_in_stock_price(true);
					refundStockParam.setReal_amount(resultRefund.getRequest_amount());
					refundStockParam.setStore_amount(new BigDecimal("0"));
					refundStockParam.setRequest_amount(new BigDecimal("0"));
					refundStockParam.setDescription("自动化测试");
					refundStockParam.setShelf_name(null);
					refundStockParam.setShelf_id(null);
					refundStockParam.setSupplier_id(null);
					refundStockParam.setSupplier_name(null);
					refundStockParam.setSale_ratio(resultRefund.getSale_ratio());
					refundStockParam.setPurchase_sku_id(resultRefund.getPurchase_sku_id());

					List<RefundStockParam> paramList = new ArrayList<RefundStockParam>();
					paramList.add(refundStockParam);
					result = refundStockService.editRefundStock(paramList);
					Assert.assertEquals(result, true, "商户退货入库处理失败");
				}
			}

			refundStockList = refundStockService.searchRefundStock(refundStockFilterParam);
			Assert.assertNotEquals(refundStockList, null, "商户退货列表查询搜索失败");
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	private List<String> category_id_1 = new ArrayList<String>();
	private List<String> category_id_2 = new ArrayList<String>();

	@Test
	public void stockRefundRecordTestCase01() {
		ReporterCSS.title("测试点: 按退货入库时间查询退货入库记录");
		try {
			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);
			stockRecordParam.setOffset(0);
			stockRecordParam.setLimit(10);

			List<RefundStockRecordBean> refundRecordList = stockRecordService.refundRecords(stockRecordParam);
			Assert.assertNotEquals(refundRecordList, null, "获取商户退货入库记录失败");

			List<RefundStockRecordBean> targetRefundRecordList = refundRecordList.stream()
					.filter(r -> r.getOrder_id().equals(order_id)).collect(Collectors.toList());

			List<RefundStockResultBean> targetRefundList = refundStockList.stream()
					.filter(r -> r.getSolution_id() == 160 && r.getOrder_id().equals(order_id))
					.collect(Collectors.toList());
			boolean result = checkStockRefundRecord(targetRefundList, targetRefundRecordList);
			Assert.assertEquals(result, true, "按退货入库时间查询退货入库记录,查询结果与预期不符");

			String category1_id = null;
			String category2_id = null;
			for (RefundStockRecordBean refundRecord : targetRefundRecordList) {
				category1_id = refundRecord.getCategory_id_1();
				category2_id = refundRecord.getCategory_id_2();
				if (!category_id_1.contains(category1_id)) {
					category_id_1.add(category1_id);
				}

				if (!category_id_2.contains(category2_id)) {
					category_id_2.add(category2_id);
				}
			}
		} catch (Exception e) {
			logger.error("查询退货入库记录遇到错误: ", e);
			Assert.fail("查询退货入库记录遇到错误: ", e);
		}
	}

	@Test
	public void stockRefundRecordTestCase02() {
		ReporterCSS.title("测试点: 按[退货入库时间+商品ID]查询退货入库记录");
		try {
			List<RefundStockResultBean> targetRefundList = refundStockList.stream()
					.filter(r -> r.getSolution_id() == 160 && r.getOrder_id().equals(order_id))
					.collect(Collectors.toList());

			RefundStockResultBean refundResult = NumberUtil.roundNumberInList(targetRefundList);
			String spu_id = refundResult.getSpu_id();

			List<RefundStockResultBean> tempRefundList = Arrays.asList(refundResult);

			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);
			stockRecordParam.setOffset(0);
			stockRecordParam.setLimit(10);
			stockRecordParam.setText(spu_id);

			List<RefundStockRecordBean> refundRecordList = stockRecordService.refundRecords(stockRecordParam);
			Assert.assertNotEquals(refundRecordList, null, "获取商户退货入库记录失败");

			List<RefundStockRecordBean> targetRefundRecordList = refundRecordList.stream()
					.filter(r -> r.getOrder_id().equals(order_id)).collect(Collectors.toList());

			boolean result = checkStockRefundRecord(tempRefundList, targetRefundRecordList);
			Assert.assertEquals(result, true, "按退货入库时间查询退货入库记录,查询结果与预期不符");

			List<String> tempSpuIds = refundRecordList.stream().filter(s -> !s.getSpu_id().equals(spu_id))
					.map(s -> s.getSpu_id()).collect(Collectors.toList());
			Assert.assertEquals(tempSpuIds.size(), 0, "按[退货入库时间+商品ID]查询退货入库记录,查询出了如下不符合的商品" + tempSpuIds + "的退货入库记录");
		} catch (Exception e) {
			logger.error("查询退货入库记录遇到错误: ", e);
			Assert.fail("查询退货入库记录遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "stockRefundRecordTestCase01" })
	public void stockRefundRecordTestCase03() {
		ReporterCSS.title("测试点: 按[退货入库时间+商品分类]查询退货入库记录");
		try {
			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);
			stockRecordParam.setOffset(0);
			stockRecordParam.setLimit(10);
			stockRecordParam.setCategory_id_1(category_id_1);
			stockRecordParam.setCategory_id_2(category_id_2);

			List<RefundStockRecordBean> refundRecordList = stockRecordService.refundRecords(stockRecordParam);
			Assert.assertNotEquals(refundRecordList, null, "获取商户退货入库记录失败");

			List<RefundStockRecordBean> targetRefundRecordList = refundRecordList.stream()
					.filter(r -> r.getOrder_id().equals(order_id)).collect(Collectors.toList());

			List<RefundStockResultBean> targetRefundList = refundStockList.stream()
					.filter(r -> r.getSolution_id() == 160 && r.getOrder_id().equals(order_id))
					.collect(Collectors.toList());

			boolean result = checkStockRefundRecord(targetRefundList, targetRefundRecordList);
			Assert.assertEquals(result, true, "按退货入库时间查询退货入库记录,查询结果与预期不符");

			List<String> tempSpuIds = refundRecordList.stream().filter(
					s -> !category_id_1.contains(s.getCategory_id_1()) && !category_id_2.contains(s.getCategory_id_2()))
					.map(s -> s.getSpu_id()).collect(Collectors.toList());
			Assert.assertEquals(tempSpuIds.size(), 0, "按[退货入库时间+商品分类]查询退货入库记录,查询出了如下不符合的商品" + tempSpuIds + "的退货入库记录");
		} catch (Exception e) {
			logger.error("查询退货入库记录遇到错误: ", e);
			Assert.fail("查询退货入库记录遇到错误: ", e);
		}
	}

	public boolean checkStockRefundRecord(List<RefundStockResultBean> refundStockList,
			List<RefundStockRecordBean> targetRefundRecordList) {
		String msg = null;
		boolean result = true;
		for (RefundStockResultBean refundResult : refundStockList) {
			String spu_id = refundResult.getSpu_id();
			BigDecimal store_amount = refundResult.getStore_amount();
			RefundStockRecordBean refundRecord = targetRefundRecordList.stream().filter(r -> r.getSpu_id()
					.equals(spu_id)
					&& r.getIn_stock_amount().subtract(store_amount).abs().compareTo(new BigDecimal("0.1")) <= 0)
					.findAny().orElse(null);
			if (refundRecord == null) {
				msg = String.format("订单%s的退货商品%s,二次入库记录没有查询到", order_id, spu_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (refundRecord.getIn_stock_amount().compareTo(refundResult.getReal_amount()) != 0) {
				msg = String.format("订单%s的退货商品%s,商户退货入库记录的实退数与预期不符,预期:%s,实际:%s", order_id, spu_id,
						refundResult.getReal_amount(), refundRecord.getIn_stock_amount());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (refundRecord.getPrice().compareTo(refundResult.getIn_stock_price()) != 0) {
				msg = String.format("订单%s的退货商品%s,商户退货入库记录的入库单价与预期不符,预期:%s,实际:%s", order_id, spu_id,
						refundResult.getIn_stock_price().divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP),
						refundRecord.getPrice().divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
		}
		return result;
	}

	@Test
	public void stockRefundRecordTestCase04() {
		ReporterCSS.title("测试点: 按放弃时间查询放弃取货记录");
		try {
			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);

			int limit = 50;
			int offset = 0;
			stockRecordParam.setLimit(limit);

			List<StockAbandonGoodsRecordBean> stockAbandonGoodsRecordList = new ArrayList<>();
			List<StockAbandonGoodsRecordBean> tempStockAbandonGoodsRecords = null;
			while (true) {
				stockRecordParam.setOffset(offset);
				tempStockAbandonGoodsRecords = stockRecordService.abandonGoodsRecords(stockRecordParam);
				Assert.assertNotEquals(stockAbandonGoodsRecordList, null, "获取放弃取货记录失败");
				stockAbandonGoodsRecordList.addAll(tempStockAbandonGoodsRecords);
				if (tempStockAbandonGoodsRecords.size() < 50) {
					break;
				}
				offset += limit;
			}

			List<StockAbandonGoodsRecordBean> targetStockAbandonGoodsRecordList = stockAbandonGoodsRecordList.stream()
					.filter(r -> r.getOrder_id().equals(order_id)).collect(Collectors.toList());

			List<RefundStockResultBean> targetRefundList = refundStockList.stream()
					.filter(r -> r.getSolution_id() == 157 && r.getOrder_id().equals(order_id))
					.collect(Collectors.toList());

			boolean result = checkStockAbandonGoodsRecord(targetRefundList, targetStockAbandonGoodsRecordList);
			Assert.assertEquals(result, true, "查询到的放弃取货操作日志与预期不一致");
		} catch (Exception e) {
			logger.error("查询放弃取货记录遇到错误: ", e);
			Assert.fail("查询放弃取货记录遇到错误: ", e);
		}
	}

	@Test
	public void stockRefundRecordTestCase05() {
		ReporterCSS.title("测试点: 按[放弃时间+商品ID]查询放弃取货记录");
		try {
			List<RefundStockResultBean> targetRefundList = refundStockList.stream()
					.filter(r -> r.getSolution_id() == 157 && r.getOrder_id().equals(order_id))
					.collect(Collectors.toList());

			RefundStockResultBean refundResult = NumberUtil.roundNumberInList(targetRefundList);
			String spu_id = refundResult.getSpu_id();

			List<RefundStockResultBean> tempRefundResultList = Arrays.asList(refundResult);

			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);
			stockRecordParam.setQ(spu_id);

			int limit = 50;
			int offset = 0;
			stockRecordParam.setLimit(limit);

			List<StockAbandonGoodsRecordBean> stockAbandonGoodsRecordList = new ArrayList<>();
			List<StockAbandonGoodsRecordBean> tempStockAbandonGoodsRecords = null;
			while (true) {
				stockRecordParam.setOffset(offset);
				tempStockAbandonGoodsRecords = stockRecordService.abandonGoodsRecords(stockRecordParam);
				Assert.assertNotEquals(stockAbandonGoodsRecordList, null, "获取放弃取货记录失败");
				stockAbandonGoodsRecordList.addAll(tempStockAbandonGoodsRecords);
				if (tempStockAbandonGoodsRecords.size() < 50) {
					break;
				}
				offset += limit;
			}

			List<StockAbandonGoodsRecordBean> targetStockAbandonGoodsRecordList = stockAbandonGoodsRecordList.stream()
					.filter(r -> r.getOrder_id().equals(order_id)).collect(Collectors.toList());

			boolean result = checkStockAbandonGoodsRecord(tempRefundResultList, targetStockAbandonGoodsRecordList);
			Assert.assertEquals(result, true, "查询到的放弃取货操作日志与预期不一致");

			List<String> tempSpuIds = stockAbandonGoodsRecordList.stream().filter(r -> !r.getSpu_id().equals(spu_id))
					.map(r -> r.getSpu_id()).collect(Collectors.toList());
			Assert.assertEquals(tempSpuIds.size(), 0, "按[放弃时间+商品ID]查询放弃取货记录,查询出了如下不合符过滤条件的SPU " + tempSpuIds + "记录");
		} catch (Exception e) {
			logger.error("查询放弃取货记录遇到错误: ", e);
			Assert.fail("查询放弃取货记录遇到错误: ", e);
		}
	}

	@Test
	public void stockRefundRecordTestCase06() {
		ReporterCSS.title("测试点: 按[放弃时间+商品分类]查询放弃取货记录");
		try {
			StockRecordFilterParam stockRecordParam = new StockRecordFilterParam();
			stockRecordParam.setBegin(todayStr);
			stockRecordParam.setEnd(todayStr);

			int limit = 50;
			int offset = 0;
			stockRecordParam.setLimit(limit);

			List<StockAbandonGoodsRecordBean> stockAbandonGoodsRecordList = new ArrayList<>();
			List<StockAbandonGoodsRecordBean> tempStockAbandonGoodsRecords = null;
			while (true) {
				stockRecordParam.setOffset(offset);
				tempStockAbandonGoodsRecords = stockRecordService.abandonGoodsRecords(stockRecordParam);
				Assert.assertNotEquals(stockAbandonGoodsRecordList, null, "获取放弃取货记录失败");
				stockAbandonGoodsRecordList.addAll(tempStockAbandonGoodsRecords);
				if (tempStockAbandonGoodsRecords.size() < 50) {
					break;
				}
				offset += limit;
			}

			List<StockAbandonGoodsRecordBean> targetStockAbandonGoodsRecordList = stockAbandonGoodsRecordList.stream()
					.filter(r -> r.getOrder_id().equals(order_id)).collect(Collectors.toList());

			List<String> category_id_1 = new ArrayList<String>();
			List<String> category_id_2 = new ArrayList<String>();
			for (StockAbandonGoodsRecordBean stockAbandonGoodsRecord : targetStockAbandonGoodsRecordList) {
				String category1_id = stockAbandonGoodsRecord.getCategory_id_1();
				String category2_id = stockAbandonGoodsRecord.getCategory_id_2();
				if (!category_id_1.contains(category1_id)) {
					category_id_1.add(category1_id);
				}

				if (!category_id_2.contains(category2_id)) {
					category_id_2.add(category2_id);
				}
			}

			stockRecordParam.setCategory_id_1(category_id_1);
			stockRecordParam.setCategory_id_2(category_id_2);

			stockAbandonGoodsRecordList = stockRecordService.abandonGoodsRecords(stockRecordParam);
			Assert.assertNotEquals(stockAbandonGoodsRecordList, null, "获取放弃取货记录失败");

			targetStockAbandonGoodsRecordList = stockAbandonGoodsRecordList.stream()
					.filter(r -> r.getOrder_id().equals(order_id)).collect(Collectors.toList());

			List<RefundStockResultBean> targetRefundList = refundStockList.stream()
					.filter(r -> r.getSolution_id() == 157 && r.getOrder_id().equals(order_id))
					.collect(Collectors.toList());

			boolean result = checkStockAbandonGoodsRecord(targetRefundList, targetStockAbandonGoodsRecordList);
			Assert.assertEquals(result, true, "查询到的放弃取货操作日志与预期不一致");

			List<String> tempSpuIds = targetStockAbandonGoodsRecordList.stream().filter(
					r -> !category_id_1.contains(r.getCategory_id_1()) || !category_id_2.contains(r.getCategory_id_2()))
					.map(r -> r.getSpu_id()).collect(Collectors.toList());
			Assert.assertEquals(tempSpuIds.size(), 0, "按[放弃时间+商品分类]查询放弃取货记录,过滤出了如下不合符过滤条件的记录 " + tempSpuIds);
		} catch (Exception e) {
			logger.error("查询放弃取货记录遇到错误: ", e);
			Assert.fail("查询放弃取货记录遇到错误: ", e);
		}
	}

	public boolean checkStockAbandonGoodsRecord(List<RefundStockResultBean> refundStockList,
			List<StockAbandonGoodsRecordBean> targetStockAbandonGoodsRecordList) {
		String msg = null;
		boolean result = true;
		for (RefundStockResultBean refundResult : refundStockList) {
			String spu_id = refundResult.getSpu_id();
			BigDecimal request_amount = refundResult.getRequest_amount();
			StockAbandonGoodsRecordBean stockAbandonGoodsRecord = targetStockAbandonGoodsRecordList.stream()
					.filter(s -> s.getSpu_id().equals(spu_id) && s.getRequest_amount().compareTo(request_amount) == 0)
					.findAny().orElse(null);
			if (stockAbandonGoodsRecord == null) {
				msg = String.format("订单%s里的退货商品%s,放弃取货后没有记录放弃取货日志", order_id, spu_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
				continue;
			}

			if (refundResult.getRequest_refund_money()
					.compareTo(stockAbandonGoodsRecord.getRequest_refund_money()) != 0) {
				msg = String.format("订单%s里的退货商品%s,应退金额与预期不一致,预期:%s,实际:%s", order_id, spu_id,
						refundResult.getRequest_refund_money(), stockAbandonGoodsRecord.getRequest_refund_money());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
		}
		return result;
	}
}
