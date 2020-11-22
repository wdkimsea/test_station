package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.station.bean.invoicing.PurchaseSkuSettleSupplier;
import cn.guanmai.station.bean.invoicing.RefundStockResultBean;
import cn.guanmai.station.bean.invoicing.SpuStockBean;
import cn.guanmai.station.bean.invoicing.StockBatchBean;
import cn.guanmai.station.bean.invoicing.param.RefundStockFilterParam;
import cn.guanmai.station.bean.invoicing.param.StockCheckFilterParam;
import cn.guanmai.station.bean.invoicing.param.RefundStockParam;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.param.OrderExceptionParam;
import cn.guanmai.station.bean.order.param.OrderRefundParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.invoicing.RefundStockServiceImpl;
import cn.guanmai.station.impl.invoicing.StockCheckServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.invoicing.RefundStockService;
import cn.guanmai.station.interfaces.invoicing.StockCheckService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Jan 7, 2019 11:38:24 AM 
* @des 商户退货相关测试
* @version 1.0 
*/
public class RefundStockTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(RefundStockTest.class);
	private Map<String, String> headers;
	private OrderService orderService;
	private OrderTool orderTool;
	private OrderDetailBean orderDetail;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private RefundStockService refundStockService;
	private StockCheckService stockCheckService;
	private LoginUserInfoService loginUserInfoService;
	private String order_id;
	private String station_id;

	@BeforeClass
	public void initData() {
		headers = getStationCookie();
		orderService = new OrderServiceImpl(headers);
		refundStockService = new RefundStockServiceImpl(headers);
		orderTool = new OrderTool(headers);
		stockCheckService = new StockCheckServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);

		try {
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账户信息失败");
			station_id = loginUserInfo.getStation_id();
		} catch (Exception e) {
			logger.error("获取站点ID遇到错误: ", e);
			Assert.fail("获取站点ID遇到错误: ", e);
		}
	}

	@Test
	public void refundStockTestCase01() {
		try {
			order_id = orderTool.oneStepCreateOrder(6);
			Assert.assertNotEquals(order_id, null, "新建订单失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + "详细信息失败");

		} catch (Exception e) {
			logger.error("创建订单过程中遇到错误: ", e);
			Assert.fail("创建订单过程中遇到错误: ", e);
		}

		// 选取一个商品,添加商品退货
		List<OrderDetailBean.Detail> details = orderDetail.getDetails();

		OrderDetailBean.Detail orderSku = NumberUtil.roundNumberInList(details);
		BigDecimal std_real_quantity = orderSku.getStd_real_quantity();
		BigDecimal request_amount = std_real_quantity
				.subtract(NumberUtil.getRandomNumber(1, std_real_quantity.intValue(), 1));
		String sku_id = orderSku.getSku_id();

		try {
			OrderRefundParam orderRund = new OrderRefundParam(request_amount, sku_id, station_id, 1, 1);
			List<OrderRefundParam> orderRundArray = new ArrayList<OrderRefundParam>();
			orderRundArray.add(orderRund);

			boolean result = orderService.addOrderException(order_id, new ArrayList<OrderExceptionParam>(),
					orderRundArray);
			Assert.assertEquals(result, true, "订单添加退货处理失败");
		} catch (Exception e) {
			logger.error("订单添加退货处理遇到错误: ", e);
			Assert.fail("订单添加退货处理遇到错误: ", e);
		}

		RefundStockFilterParam refundStockFilterParam = new RefundStockFilterParam();
		refundStockFilterParam.setDate_from(todayStr);
		refundStockFilterParam.setDate_end(todayStr);
		refundStockFilterParam.setOrder_id(order_id);
		refundStockFilterParam.setPage(0);
		refundStockFilterParam.setNum(20);

		try {
			List<RefundStockResultBean> refundList = refundStockService.searchRefundStock(refundStockFilterParam);
			Assert.assertNotEquals(refundList, null, "商户退货入库列表查询失败");

			RefundStockResultBean refundStockResult = refundList.stream().filter(r -> r.getOrder_id().equals(order_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(refundStockResult, null, "订单 " + order_id + " 添加的退货请求在商户退货列表没有找到");

			String msg = null;
			boolean result = true;
			if (!refundStockResult.getSku_id().equals(sku_id)) {
				msg = String.format("订单%S添加商户退货的商品ID与添加后查看到的不一致,预期:%s,实际:%s", order_id, sku_id,
						refundStockResult.getSku_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			if (refundStockResult.getRequest_amount().compareTo(request_amount) != 0) {
				msg = String.format("订单%S中的商品%s填写的退货数与查询到的不一致,预期:%s,实际:%s", order_id, sku_id, request_amount,
						refundStockResult.getRequest_amount());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (refundStockResult.getState() != 1) {
				msg = String.format("订单%S中的商品%s退货状态与预期的不一致,预期:%s,实际:%s", order_id, sku_id, 1,
						refundStockResult.getState());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "新增商户退货信息与查询到的信息不一致");
		} catch (Exception e) {
			logger.error("获取商户退货列表遇到错误: ", e);
			Assert.fail("获取商户退货列表遇到错误: ", e);
		}
	}

	@Test
	public void refundStockTestCase02() {
		try {
			order_id = orderTool.oneStepCreateOrder(6);
			Assert.assertNotEquals(order_id, null, "新建订单失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + "详细信息失败");

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

			boolean result = orderService.addOrderException(order_id, new ArrayList<OrderExceptionParam>(),
					orderRundArray);
			Assert.assertEquals(result, true, "订单添加退货处理失败");
		} catch (Exception e) {
			logger.error("订单添加退货处理遇到错误: ", e);
			Assert.fail("订单添加退货处理遇到错误: ", e);
		}

		RefundStockFilterParam refundStockFilterParam = new RefundStockFilterParam();
		refundStockFilterParam.setDate_from(todayStr);
		refundStockFilterParam.setDate_end(todayStr);
		refundStockFilterParam.setOrder_id(order_id);
		refundStockFilterParam.setPage(0);
		refundStockFilterParam.setNum(20);

		try {
			List<RefundStockResultBean> refundStockList = refundStockService.searchRefundStock(refundStockFilterParam);
			RefundStockResultBean refundStockResult = refundStockList.stream()
					.filter(r -> r.getOrder_id().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(refundStockResult, null, "订单 " + order_id + " 添加的退货请求在商户退货列表没有找到");

			List<PurchaseSkuSettleSupplier> purchaseSkuSettleSupplierList = refundStockService
					.getPurchaseSkuSettleSupplierList(refundStockResult.getPurchase_sku_id());

			Assert.assertNotEquals(purchaseSkuSettleSupplierList, null, "获取采购规格对应的供应商列表失败");

			Assert.assertEquals(purchaseSkuSettleSupplierList.size() > 0, true,
					refundStockResult.getPurchase_sku_id() + "此采购规格没有对应的供应商,无法进行后续操作");

			PurchaseSkuSettleSupplier purchaseSkuSettleSupplier = purchaseSkuSettleSupplierList.get(0);
			String supplier_name = purchaseSkuSettleSupplier.getSupplier_name();

			RefundStockParam refundStockParam = new RefundStockParam();
			refundStockParam.setRefund_id(refundStockResult.getRefund_id());
			refundStockParam.setSku_name(refundStockResult.getSku_name());
			refundStockParam.setSku_id(refundStockResult.getSku_id());
			refundStockParam.setSolution(160);
			refundStockParam.setDriver_id(0);
			// 入库单价为分
			refundStockParam.setIn_stock_price(NumberUtil.getRandomNumber(200, 400, 2));
			refundStockParam.setDisabled_in_stock_price(true);
			refundStockParam.setReal_amount(refundStockResult.getRequest_amount());
			refundStockParam.setStore_amount(refundStockResult.getRequest_amount());
			refundStockParam.setRequest_amount(refundStockResult.getRequest_amount());
			refundStockParam.setDescription("自动化测试");
			refundStockParam.setShelf_name(null);
			refundStockParam.setShelf_id(null);
			refundStockParam.setSupplier_id(purchaseSkuSettleSupplier.getSupplier_id());
			refundStockParam.setSupplier_name(purchaseSkuSettleSupplier.getSupplier_name());
			refundStockParam.setSale_ratio(refundStockResult.getSale_ratio());
			refundStockParam.setPurchase_sku_id(refundStockResult.getPurchase_sku_id());

			List<RefundStockParam> paramList = new ArrayList<RefundStockParam>();
			paramList.add(refundStockParam);
			boolean result = refundStockService.editRefundStock(paramList);
			Assert.assertEquals(result, true, "商户退货入库处理失败");

			refundStockList = refundStockService.searchRefundStock(refundStockFilterParam);
			refundStockResult = refundStockList.stream().filter(r -> r.getOrder_id().equals(order_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(refundStockResult, null, "订单" + order_id + "的二次入库的商户退货处理在商户退货列表没有查询到");

			String msg = null;
			if (!refundStockResult.getSku_id().equals(sku_id)) {
				msg = String.format("订单%S添加商户退货的商品ID与添加后查看到的不一致,预期:%s,实际:%s", order_id, sku_id,
						refundStockResult.getSku_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (refundStockResult.getState() != 4) {
				msg = String.format("订单%S中的商品%s退货状态与预期的不一致,预期:%s,实际:%s", order_id, sku_id, 4,
						refundStockResult.getState());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (refundStockResult.getSolution_id() != 160) {
				msg = String.format("订单%S中的商品%s退货处理方式与预期的不一致,预期:%s,实际:%s", order_id, sku_id, 160,
						refundStockResult.getSolution_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (refundStockResult.getReal_amount().compareTo(refundStockParam.getReal_amount()) != 0) {
				msg = String.format("订单%S中的商品%s实退数与预期的不一致,预期:%s,实际:%s", order_id, sku_id,
						refundStockParam.getReal_amount(), refundStockResult.getReal_amount());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (refundStockResult.getStore_amount().compareTo(refundStockParam.getStore_amount()) != 0) {
				msg = String.format("订单%S中的商品%s二次入库数与预期的不一致,预期:%s,实际:%s", order_id, sku_id,
						refundStockParam.getStore_amount(), refundStockResult.getStore_amount());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (refundStockResult.getIn_stock_price().compareTo(refundStockParam.getIn_stock_price()) != 0) {
				msg = String.format("订单%S中的商品%s二次入库单价与预期的不一致,预期:%s,实际:%s", order_id, sku_id,
						refundStockParam.getIn_stock_price().divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP),
						refundStockResult.getIn_stock_price().divide(new BigDecimal("100"), 2,
								BigDecimal.ROUND_HALF_UP));
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!refundStockResult.getSupplier_name().equals(supplier_name)) {
				msg = String.format("订单%S中的商品%s二次入库绑定的供应商与预期的不一致,预期:%s,实际:%s", order_id, sku_id, supplier_name,
						refundStockResult.getSupplier_name());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "处理完的商户退货信息,查询到的与预期不一致");

		} catch (Exception e) {
			logger.error("提交商户退货处理遇到错误: ", e);
			Assert.fail("提交商户退货处理遇到错误: ", e);
		}
	}

	@Test
	public void refundStockTestCase03() {
		Reporter.log("测试点: 商户退货处理,进行放弃取货操作");
		try {
			order_id = orderTool.oneStepCreateOrder(6);
			Assert.assertNotEquals(order_id, null, "新建订单失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + "详细信息失败");

		} catch (Exception e) {
			logger.error("创建订单过程中遇到错误: ", e);
			Assert.fail("创建订单过程中遇到错误: ", e);
		}

		List<OrderDetailBean.Detail> details = orderDetail.getDetails();

		OrderDetailBean.Detail orderSku = NumberUtil.roundNumberInList(details);
		BigDecimal request_amount = orderSku.getStd_real_quantity().subtract(new BigDecimal("1"));
		String sku_id = orderSku.getSku_id();

		try {
			OrderRefundParam orderRund = new OrderRefundParam(request_amount, sku_id, station_id, 1, 1);
			List<OrderRefundParam> orderRundArray = new ArrayList<OrderRefundParam>();
			orderRundArray.add(orderRund);

			boolean result = orderService.addOrderException(order_id, new ArrayList<OrderExceptionParam>(),
					orderRundArray);
			Assert.assertEquals(result, true, "订单添加退货处理失败");
		} catch (Exception e) {
			logger.error("订单添加退货处理遇到错误: ", e);
			Assert.fail("订单添加退货处理遇到错误: ", e);
		}

		RefundStockFilterParam refundFilterParam = new RefundStockFilterParam();
		refundFilterParam.setDate_from(todayStr);
		refundFilterParam.setDate_end(todayStr);
		refundFilterParam.setOrder_id(order_id);
		refundFilterParam.setPage(0);
		refundFilterParam.setNum(20);

		try {
			List<RefundStockResultBean> refundList = refundStockService.searchRefundStock(refundFilterParam);
			Assert.assertNotEquals(refundList, null, "获取商户退货入库列表信息失败");

			RefundStockResultBean resultRefund = refundList.stream().filter(r -> r.getOrder_id().equals(order_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(resultRefund, null, "订单 " + order_id + " 添加的退货请求在商户退货列表没有找到");

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
			boolean result = refundStockService.editRefundStock(paramList);
			Assert.assertEquals(result, true, "商户退货入库处理失败");

			refundList = refundStockService.searchRefundStock(refundFilterParam);
			Assert.assertNotEquals(refundList, null, "获取商户退货入库列表信息失败");

			resultRefund = refundList.stream().filter(r -> r.getOrder_id().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(resultRefund, null, "订单 " + order_id + " 处理的退货请求在商户退货列表没有找到");

			String msg = null;
			if (resultRefund.getState() != 4) {
				msg = String.format("订单%S中的商品%s商户退货处理状态与预期的不一致,预期:%s,实际:%s", order_id, sku_id, 4,
						resultRefund.getState());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (resultRefund.getSolution_id() != 157) {
				msg = String.format("订单%S中的商品%s商户退货处理方式与预期的不一致,预期:%s,实际:%s", order_id, sku_id, 157,
						resultRefund.getSolution_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "订单" + order_id + "中的商品" + sku_id + "商户退货,放弃获取后查询到的信息与预期的不一致");
		} catch (Exception e) {
			logger.error("提交商户退货处理遇到错误: ", e);
			Assert.fail("提交商户退货处理遇到错误: ", e);
		}
	}

	@Test
	public void refundStockTestCase04() {
		Reporter.log("测试点: 导出商户退货信息");
		RefundStockFilterParam refundFilterParam = new RefundStockFilterParam();
		refundFilterParam.setDate_from(todayStr);
		refundFilterParam.setDate_end(todayStr);
		refundFilterParam.setPage(0);
		refundFilterParam.setNum(20);
		try {
			boolean reuslt = refundStockService.exportRefundStockData(refundFilterParam);
			Assert.assertEquals(reuslt, true, "导出商户退货列表数据失败");
		} catch (Exception e) {
			logger.error("导出商户退货列表遇到错误: ", e);
			Assert.fail("导出商户退货列表遇到错误: ", e);
		}
	}

	@Test
	public void refundStockTestCase05() {
		Reporter.log("测试点: 按处理方式(二次入库)搜索过滤商户退货信息");
		RefundStockFilterParam refundFilterParam = new RefundStockFilterParam();
		refundFilterParam.setDate_from(todayStr);
		refundFilterParam.setDate_end(todayStr);
		refundFilterParam.setPage(0);
		refundFilterParam.setNum(20);
		refundFilterParam.setState(4);
		try {
			List<RefundStockResultBean> refundStockResultList = refundStockService.searchRefundStock(refundFilterParam);
			Assert.assertNotEquals(refundStockResultList, null, "按处理方式搜索过滤商户退货信息失败");

			List<RefundStockResultBean> tempList = refundStockResultList.stream()
					.filter(r -> r.getState() != 4 || r.getSolution_id() != 160).collect(Collectors.toList());
			Assert.assertEquals(tempList.size(), 0, "按处理方式(二次入库)搜索过滤商户退货信息,把不符合过滤条件的给搜索出来了");
		} catch (Exception e) {
			logger.error("导出商户退货列表遇到错误: ", e);
			Assert.fail("导出商户退货列表遇到错误: ", e);
		}
	}

	@Test
	public void refundStockTestCase06() {
		Reporter.log("测试点: 按处理方式(放弃取货)搜索过滤商户退货信息");
		RefundStockFilterParam refundFilterParam = new RefundStockFilterParam();
		refundFilterParam.setDate_from(todayStr);
		refundFilterParam.setDate_end(todayStr);
		refundFilterParam.setPage(0);
		refundFilterParam.setNum(20);
		refundFilterParam.setState(5);
		try {
			List<RefundStockResultBean> refundStockResultList = refundStockService.searchRefundStock(refundFilterParam);
			Assert.assertNotEquals(refundStockResultList, null, "按处理方式搜索过滤商户退货信息失败");

			List<RefundStockResultBean> tempList = refundStockResultList.stream().filter(r -> r.getSolution_id() != 157)
					.collect(Collectors.toList());
			Assert.assertEquals(tempList.size(), 0, "按处理方式(放弃取货)搜索过滤商户退货信息,把不符合过滤条件的给搜索出来了");
		} catch (Exception e) {
			logger.error("导出商户退货列表遇到错误: ", e);
			Assert.fail("导出商户退货列表遇到错误: ", e);
		}
	}

	@Test
	public void refundStockTestCase07() {
		Reporter.log("测试点: 按商户ID过滤商户退货入库信息");
		RefundStockFilterParam refundFilterParam = new RefundStockFilterParam();
		refundFilterParam.setDate_from(todayStr);
		refundFilterParam.setDate_end(todayStr);
		refundFilterParam.setPage(0);
		refundFilterParam.setNum(20);

		try {
			List<RefundStockResultBean> refundStockResultList = refundStockService.searchRefundStock(refundFilterParam);
			Assert.assertNotEquals(refundStockResultList, null, "搜索过滤商户退货信息失败");

			if (refundStockResultList.size() > 0) {
				String address_id = refundStockResultList.get(0).getAddress_id();
				refundFilterParam.setSid(address_id);
				refundStockResultList = refundStockService.searchRefundStock(refundFilterParam);
				Assert.assertNotEquals(refundStockResultList, null, "按商户ID搜索过滤商户退货信息失败");

				List<RefundStockResultBean> tempList = refundStockResultList.stream()
						.filter(r -> !r.getAddress_id().equals(address_id)).collect(Collectors.toList());
				Assert.assertNotEquals(tempList, null, "按商户ID搜索过滤商户退货信息,搜索出了不符合过滤条件的信息");
			}
		} catch (Exception e) {
			logger.error("搜索过滤商户退货信息遇到错误: ", e);
			Assert.fail("搜索过滤商户退货信息遇到错误: ", e);
		}
	}

	@Test
	public void refundStockTestCase08() {
		Reporter.log("测试点: 按商户名称过滤商户退货入库信息");
		RefundStockFilterParam refundFilterParam = new RefundStockFilterParam();
		refundFilterParam.setDate_from(todayStr);
		refundFilterParam.setDate_end(todayStr);
		refundFilterParam.setPage(0);
		refundFilterParam.setNum(20);

		try {
			List<RefundStockResultBean> refundStockResultList = refundStockService.searchRefundStock(refundFilterParam);
			Assert.assertNotEquals(refundStockResultList, null, "搜索过滤商户退货信息失败");

			if (refundStockResultList.size() > 0) {
				String resname = refundStockResultList.get(0).getResname();
				refundFilterParam.setResname(resname);
				refundStockResultList = refundStockService.searchRefundStock(refundFilterParam);
				Assert.assertNotEquals(refundStockResultList, null, "按商户名称搜索过滤商户退货信息失败");

				List<RefundStockResultBean> tempList = refundStockResultList.stream()
						.filter(r -> !r.getResname().equals(resname)).collect(Collectors.toList());
				Assert.assertNotEquals(tempList, null, "按商户名称搜索过滤商户退货信息,搜索出了不符合过滤条件的信息");
			}
		} catch (Exception e) {
			logger.error("搜索过滤商户退货信息遇到错误: ", e);
			Assert.fail("搜索过滤商户退货信息遇到错误: ", e);
		}
	}

	@Test
	public void refundStockTestCase09() {
		Reporter.log("测试点: 拉取商户退货处理操作的一些候选值");
		try {
			JSONObject retObj = refundStockService.refundBaseInfo();
			Assert.assertEquals(retObj.getInteger("code") == 0, true, "拉取商户退货处理操作的一些候选值失败");
		} catch (Exception e) {
			logger.error("拉取商户退货处理操作的一些候选值遇到错误: ", e);
			Assert.fail("拉取商户退货处理操作的一些候选值遇到错误: ", e);
		}
	}

	@Test
	public void refundStockTestCase10() {
		Reporter.log("测试点: 商户退货入库,查询对应的商品库存数、均价信息");
		try {
			order_id = orderTool.oneStepCreateOrder(6);
			Assert.assertNotEquals(order_id, null, "新建订单失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + "详细信息失败");

		} catch (Exception e) {
			logger.error("创建订单过程中遇到错误: ", e);
			Assert.fail("创建订单过程中遇到错误: ", e);
		}

		// 选取一个商品,添加商品退货
		List<OrderDetailBean.Detail> details = orderDetail.getDetails();

		OrderDetailBean.Detail orderSku = NumberUtil.roundNumberInList(details);
		BigDecimal request_amount = NumberUtil.getRandomNumber(1, orderSku.getStd_real_quantity().intValue(), 2);
		String sku_id = orderSku.getSku_id();
		String spu_id = orderSku.getSpu_id();
		try {
			OrderRefundParam orderRund = new OrderRefundParam(request_amount, sku_id, station_id, 1, 1);
			List<OrderRefundParam> orderRundArray = new ArrayList<OrderRefundParam>();
			orderRundArray.add(orderRund);

			boolean result = orderService.addOrderException(order_id, new ArrayList<OrderExceptionParam>(),
					orderRundArray);
			Assert.assertEquals(result, true, "订单添加退货处理失败");
		} catch (Exception e) {
			logger.error("订单添加退货处理遇到错误: ", e);
			Assert.fail("订单添加退货处理遇到错误: ", e);
		}

		RefundStockFilterParam refundFilterParam = new RefundStockFilterParam();
		refundFilterParam.setDate_from(todayStr);
		refundFilterParam.setDate_end(todayStr);
		refundFilterParam.setOrder_id(order_id);
		refundFilterParam.setPage(0);
		refundFilterParam.setNum(20);

		try {
			StockCheckFilterParam stockCheckFilterParam = new StockCheckFilterParam();
			stockCheckFilterParam.setText(spu_id);
			stockCheckFilterParam.setOffset(0);
			stockCheckFilterParam.setLimit(10);
			List<SpuStockBean> spuStockList = stockCheckService.searchStockCheck(stockCheckFilterParam);
			Assert.assertNotEquals(spuStockList, null, "获取商品盘点列表信息失败");
			SpuStockBean spuStock = spuStockList.stream().filter(s -> s.getSpu_id().equals(spu_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(spuStock, null, "商品盘点列表,没有找到 " + spu_id + "对应的商品信息,无法进行后续验证操作");

			BigDecimal spu_stock_remain = spuStock.getRemain();
			BigDecimal spu_stock_avg_price = spuStock.getAvg_price();

			BigDecimal stock_value = spu_stock_remain.multiply(spu_stock_avg_price).setScale(2,
					BigDecimal.ROUND_HALF_UP);

			List<RefundStockResultBean> refundList = refundStockService.searchRefundStock(refundFilterParam);
			RefundStockResultBean refundStockResult = refundList.stream().filter(r -> r.getOrder_id().equals(order_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(refundStockResult, null, "订单 " + order_id + " 添加的退货请求在商户退货列表没有找到");

			List<PurchaseSkuSettleSupplier> purchaseSkuSettleSupplierList = refundStockService
					.getPurchaseSkuSettleSupplierList(refundStockResult.getPurchase_sku_id());

			Assert.assertNotEquals(purchaseSkuSettleSupplierList, null, "获取采购规格对应的供应商列表失败");

			Assert.assertEquals(purchaseSkuSettleSupplierList.size() > 0, true,
					refundStockResult.getPurchase_sku_id() + "此采购规格所属分类没有绑定供应商,无法进行后续操作");

			PurchaseSkuSettleSupplier purchaseSkuSettleSupplier = purchaseSkuSettleSupplierList.get(0);

			RefundStockParam refundStockParam = new RefundStockParam();
			refundStockParam.setRefund_id(refundStockResult.getRefund_id());
			refundStockParam.setSku_name(refundStockResult.getSku_name());
			refundStockParam.setSku_id(refundStockResult.getSku_id());
			refundStockParam.setSolution(160);
			refundStockParam.setDriver_id(0);
			BigDecimal in_stock_price = NumberUtil.getRandomNumber(2, 4, 2);
			// 入库单价为分
			refundStockParam.setIn_stock_price(in_stock_price.multiply(new BigDecimal("100")));
			refundStockParam.setDisabled_in_stock_price(true);
			refundStockParam.setReal_amount(request_amount);
			refundStockParam.setStore_amount(request_amount);
			refundStockParam.setRequest_amount(request_amount);
			refundStockParam.setDescription("自动化测试");
			refundStockParam.setShelf_name(null);
			refundStockParam.setShelf_id(null);
			refundStockParam.setSupplier_id(purchaseSkuSettleSupplier.getSupplier_id());
			refundStockParam.setSupplier_name(purchaseSkuSettleSupplier.getSupplier_name());
			refundStockParam.setSale_ratio(refundStockResult.getSale_ratio());
			refundStockParam.setPurchase_sku_id(refundStockResult.getPurchase_sku_id());

			List<RefundStockParam> paramList = new ArrayList<RefundStockParam>();
			paramList.add(refundStockParam);
			boolean result = refundStockService.editRefundStock(paramList);
			Assert.assertEquals(result, true, "商户退货入库处理失败");

			LoginUserInfoService loginUserInfoService = new LoginUserInfoServiceImpl(headers);
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取站点进销存类型失败");

			int stockMethod = loginUserInfo.getStock_method();
			logger.info("进销存类型为: " + (stockMethod == 1 ? "加权平均" : "先进先出"));

			BigDecimal expected_stock_remain = spu_stock_remain.add(request_amount);
			BigDecimal expected_stock_avg_price = null;
			// 特殊逻辑,当库存货值为负时,此时入库就把这一次的入库单价改为SPU库存均价
			if (stock_value.compareTo(BigDecimal.ZERO) < 0) {
				ReporterCSS.step("注意:此商品 " + spu_id + " 目前货值为负,此次退货入库,均价直接设置为退货入库价格");
				expected_stock_avg_price = in_stock_price;
			} else {
				expected_stock_avg_price = (stock_value.add(request_amount.multiply(in_stock_price)))
						.divide(spu_stock_remain.add(request_amount), 2, BigDecimal.ROUND_HALF_UP);
			}

			spuStockList = stockCheckService.searchStockCheck(stockCheckFilterParam);
			Assert.assertNotEquals(spuStockList, null, "获取商品盘点列表信息失败");
			spuStock = spuStockList.stream().filter(s -> s.getSpu_id().equals(spu_id)).findAny().orElse(null);
			Assert.assertNotEquals(spuStock, null, "商品盘点列表,没有找到 " + spu_id + "对应的商品信息,无法进行后续验证操作");

			String msg = null;
			if (spuStock.getAvg_price().compareTo(expected_stock_avg_price) != 0) {
				msg = String.format("商户退货二次入库后,商品%s新的库存均价与预期不符,预期:%s,实际:%s", spu_id, expected_stock_avg_price,
						spuStock.getAvg_price());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (spuStock.getRemain().compareTo(expected_stock_remain) != 0) {
				msg = String.format("商户退货二次入库后,商品%s新的库存数与预期不符,预期:%s,实际:%s", spu_id, expected_stock_remain,
						spuStock.getRemain());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (stockMethod == 2) {
				refundList = refundStockService.searchRefundStock(refundFilterParam);
				refundStockResult = refundList.stream().filter(r -> r.getOrder_id().equals(order_id)).findAny()
						.orElse(null);
				Assert.assertNotEquals(refundStockResult, null, "订单 " + order_id + " 添加的退货请求在商户退货列表没有找到");

				String batch_number = refundStockResult.getBatch_number();
				List<StockBatchBean> stockBatchList = stockCheckService.searchStockBatch(batch_number, 0, 10);
				if (stockBatchList == null) {
					msg = String.format("搜索过滤批次信息失败");
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				} else {
					StockBatchBean stockBatch = stockBatchList.stream()
							.filter(s -> s.getBatch_number().equals(batch_number)).findAny().orElse(null);
					Assert.assertNotEquals(stockBatch, null, "商户退货,二次入库生成的批次" + batch_number + "在批次列表没有搜索到");
					if (stockBatch == null) {
						msg = String.format("商户退货,二次入库生成的批次%s在批次列表没有搜索到", batch_number);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					} else {
						if (stockBatch.getPrice().compareTo(in_stock_price) != 0) {
							msg = String.format("商户退货,二次入库生成的批次%s入库单价与预期不一致,预期:%s,实际:%s", in_stock_price,
									stockBatch.getPrice());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}

						if (stockBatch.getRemain().compareTo(request_amount) != 0) {
							msg = String.format("商户退货,二次入库生成的批次%s库存数与预期不一致,预期:%s,实际:%s", request_amount,
									stockBatch.getRemain());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					}
				}
			}
			Assert.assertEquals(result, true, "商户退货,二次入库后查询到的信息与预期的不一致");
		} catch (Exception e) {
			logger.error("验证商户退货二次入库信息遇到错误: ", e);
			Assert.fail("验证商户退货二次入库信息遇到错误: ", e);
		}
	}

}
