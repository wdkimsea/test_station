package cn.guanmai.station.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.invoicing.PurchaseSkuSettleSupplier;
import cn.guanmai.station.bean.invoicing.RefundStockResultBean;
import cn.guanmai.station.bean.invoicing.param.RefundStockFilterParam;
import cn.guanmai.station.bean.invoicing.param.RefundStockParam;
import cn.guanmai.station.bean.order.OrderBean;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderDetailBean.Abnormal;
import cn.guanmai.station.bean.order.OrderDetailBean.Detail;
import cn.guanmai.station.bean.order.OrderDetailBean.Refund;
import cn.guanmai.station.bean.order.param.OrderExceptionParam;
import cn.guanmai.station.bean.order.param.OrderRefundParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.invoicing.RefundStockServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.invoicing.RefundStockService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Jan 3, 2019 11:48:49 AM 
* @des 订单售后测试
* @version 1.0 
*/
public class OrderExceptionTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(OrderExceptionTest.class);
	private OrderService orderService;
	private RefundStockService refundService;
	private LoginUserInfoService loginUserInfoService;
	private OrderTool orderTool;
	private String order_id;
	private String station_id;
	private String todayStr;
	private OrderDetailBean orderDetail;
	private String station_store_id;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		orderTool = new OrderTool(headers);
		orderService = new OrderServiceImpl(headers);
		refundService = new RefundStockServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		try {
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账户信息失败");

			station_id = loginUserInfo.getStation_id();

			todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			station_store_id = station_id;
		} catch (Exception e) {
			logger.error("获取站点ID遇到错误: ", e);
			Assert.fail("获取站点ID遇到错误: ", e);
		}

	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			order_id = orderTool.oneStepCreateOrder(8);
			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 的详细信息失败");
		} catch (Exception e) {
			logger.error("修改订单前的创建订单遇到错误: ", e);
			Assert.fail("修改订单前的创建订单遇到错误: ", e);
		}
	}

	/**
	 * 添加订单售后
	 * 
	 */
	@Test
	public void orderExceptionTestCase01() {
		ReporterCSS.title("测试点) 订单添加异常商品信息");
		// 选取一个商品,添加商品异常
		Detail detail = NumberUtil.roundNumberInList(orderDetail.getDetails());

		String sku_id = detail.getSku_id();
		// 都以基本单位计算

		BigDecimal std_sale_price_forsale = detail.getStd_sale_price_forsale();

		BigDecimal std_unit_quantity = detail.getStd_unit_quantity(); // 商品下单数(基本单位)
		int max = std_unit_quantity.intValue() + 10;
		// 最终值
		BigDecimal final_amount = NumberUtil.getRandomNumber(1, max, 2);
		// 异常数值
		BigDecimal abnormal_quantity = final_amount.subtract(std_unit_quantity);

		BigDecimal expected_abnormal_money = std_sale_price_forsale.multiply(abnormal_quantity);

		List<OrderExceptionParam> orderExceptionArray = new ArrayList<OrderExceptionParam>();
		orderExceptionArray.add(new OrderExceptionParam(final_amount, sku_id, 1, 1, 2));

		try {
			boolean result = orderService.addOrderException(order_id, orderExceptionArray,
					new ArrayList<OrderRefundParam>());
			Assert.assertEquals(result, true, "订单添加售后处理失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			BigDecimal abnormal_money = orderDetail.getAbnormal_money();
			expected_abnormal_money = expected_abnormal_money.setScale(2, BigDecimal.ROUND_HALF_UP);
			Assert.assertEquals(expected_abnormal_money.compareTo(abnormal_money) == 0, true, "订单详细信息中,订单 " + order_id
					+ " 展示的异常金额和预期的不一致,预期: " + expected_abnormal_money + ",实际: " + abnormal_money);

			List<Abnormal> abnormals = orderDetail.getAbnormals();
			Assert.assertNotEquals(abnormals, null, "订单 " + order_id + " 没有售后异常相关信息");

			Assert.assertEquals(abnormals.size(), 1, "订单 " + order_id + " 售后异常信息应该只有一条");

			Abnormal abnormal = abnormals.get(0);
			result = abnormal.getDetail_id().equals(sku_id)
					&& abnormal.getFinal_amount_forsale().compareTo(final_amount) == 0
					&& abnormal.getMoney_delta().compareTo(expected_abnormal_money) == 0;

			String msg = null;
			if (!result) {
				msg = "预期SKU_ID:" + sku_id + ",实际SKU_ID:" + abnormal.getDetail_id();
				ReporterCSS.warn(msg);
				logger.warn(msg);
				msg = "预期记账数:" + final_amount + ",实际记账数:" + abnormal.getFinal_amount_forsale();
				ReporterCSS.warn(msg);
				logger.warn(msg);
				msg = "预期异常金额:" + expected_abnormal_money + ",实际异常金额:" + abnormal.getMoney_delta();
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}
			Assert.assertEquals(result, true, "录入保存的售后信息与预期的售后信息不一致");

			// 验证订单列表展示的异常金额
			OrderBean order = orderService.getOrderBeanById(order_id);
			Assert.assertNotEquals(order, null, "订单列表没有找到订单 " + order_id);

			abnormal_money = order.getAbnormal_money();
			Assert.assertEquals(expected_abnormal_money.compareTo(abnormal_money) == 0, true, "订单列表中订单 " + order_id
					+ " 展示的异常金额与预期异常金额不一致,预期: " + expected_abnormal_money + ",实际: " + abnormal_money);
		} catch (Exception e) {
			logger.error("订单添加售后处理遇到错误: ", e);
			Assert.fail("订单添加售后处理遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionTestCase02() {
		ReporterCSS.title("测试点: 订单添加退货商品信息");
		// 选取一个商品,添加商品退货
		Detail detail = NumberUtil.roundNumberInList(orderDetail.getDetails());

		String sku_id = detail.getSku_id();
		// 退货数(基本单位)
		int max = detail.getStd_real_quantity().intValue();
		BigDecimal request_amount = NumberUtil.getRandomNumber(1, max, 2);

		try {
			OrderRefundParam orderRefund = new OrderRefundParam(request_amount, sku_id, station_id, 1, 1);
			List<OrderRefundParam> orderRefundArray = new ArrayList<OrderRefundParam>();
			orderRefundArray.add(orderRefund);

			boolean result = orderService.addOrderException(order_id, new ArrayList<OrderExceptionParam>(),
					orderRefundArray);
			Assert.assertEquals(result, true, "订单添加售后处理失败");

			// 验证售后信息
			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + "详细信息失败");

			List<Refund> refunds = orderDetail.getRefunds();
			Assert.assertNotEquals(refunds, null, "订单 " + order_id + " 详细信息中没有退货相关信息");

			Assert.assertEquals(refunds.size(), 1, "订单 " + order_id + " 详细信息中应该只有一条退货信息");

			Refund refund = refunds.get(0);
			result = refund.getDetail_id().equals(sku_id)
					&& refund.getRequest_amount_forsale().compareTo(request_amount) == 0;
			if (!result) {
				ReporterCSS.warn("预期SKU_ID:" + sku_id + ",实际SKU_ID:" + refund.getDetail_id());
				ReporterCSS.warn("预期退货数:" + request_amount + ",实际退货数:" + refund.getRequest_amount_forsale());
			}
			Assert.assertEquals(result, true, "录入保存的退货信息与预期的售后信息不一致");

		} catch (Exception e) {
			logger.error("订单添加售后处理遇到错误: ", e);
			Assert.fail("订单添加售后处理遇到错误: ", e);
		}
	}

	/**
	 * 同时添加退货和异常
	 */
	@Test
	public void orderExceptionTestCase03() {
		ReporterCSS.title("测试点) 订单同时添加退货商品信息和异常商品信息");
		// 选取一个商品,添加商品异常
		Assert.assertEquals(orderDetail.getDetails().size() >= 2, true, "订单 " + order_id + " 中的商品数不足以同时添加退货和异常");

		// 商品退货
		Detail refundSku = orderDetail.getDetails().get(0);
		BigDecimal request_amount = NumberUtil.getRandomNumber(1, refundSku.getStd_unit_quantity().intValue(), 2);
		String refund_sku_id = refundSku.getSku_id();

		// 商品异常
		Detail exceptionSku = orderDetail.getDetails().get(1);
		String exception_sku_id = exceptionSku.getSku_id();
		BigDecimal final_amount = NumberUtil.getRandomNumber(1, exceptionSku.getStd_unit_quantity().intValue(), 2);
		BigDecimal exception_amount = exceptionSku.getStd_unit_quantity().subtract(final_amount);
		BigDecimal exception_money = exceptionSku.getStd_sale_price_forsale().multiply(exception_amount).setScale(2,
				BigDecimal.ROUND_HALF_UP);

		try {
			List<OrderRefundParam> orderRundArray = new ArrayList<OrderRefundParam>();
			orderRundArray.add(new OrderRefundParam(request_amount, refund_sku_id, station_store_id, 1, 1));

			List<OrderExceptionParam> orderExceptionArray = new ArrayList<OrderExceptionParam>();
			orderExceptionArray.add(new OrderExceptionParam(final_amount, exception_sku_id, 1, 1, 2));

			boolean result = orderService.addOrderException(order_id, orderExceptionArray, orderRundArray);
			Assert.assertEquals(result, true, "订单添加售后处理失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			// 验证商品异常信息
			List<Abnormal> abnormals = orderDetail.getAbnormals();
			Assert.assertNotEquals(abnormals, null, "订单详细 " + order_id + " 没有商品异常相关信息");

			Assert.assertEquals(abnormals.size(), 1, "订单详细 " + order_id + " 中的商品异常相关信息应该只有一条");

			Abnormal abnormal = abnormals.get(0);
			result = abnormal.getDetail_id().equals(exception_sku_id)
					&& abnormal.getFinal_amount_forsale().compareTo(final_amount) == 0
					&& abnormal.getMoney_delta().multiply(new BigDecimal("-1")).compareTo(exception_money) == 0;

			String msg = null;
			if (!result) {
				msg = "预期SKU_ID:" + exception_sku_id + ",实际SKU_ID:" + abnormal.getDetail_id();
				ReporterCSS.warn(msg);
				logger.warn(msg);
				msg = "预期记账数:" + final_amount + ",实际记账数:" + abnormal.getFinal_amount_forsale();
				ReporterCSS.warn(msg);
				logger.warn(msg);
				msg = "预期异常金额:" + exception_money + ",实际异常金额:"
						+ abnormal.getMoney_delta().multiply(new BigDecimal("-1"));
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}
			Assert.assertEquals(result, true, "录入保存的售后信息与预期的售后信息不一致");

			// 验证商品退货信息
			List<Refund> refunds = orderDetail.getRefunds();
			Assert.assertNotEquals(refunds, null, "订单 " + order_id + " 详细信息中没有退货相关信息");

			Assert.assertEquals(refunds.size(), 1, "订单 " + order_id + " 详细信息中应该只有一条退货信息");

			Refund refund = refunds.get(0);
			result = refund.getDetail_id().equals(refund_sku_id)
					&& refund.getRequest_amount_forsale().compareTo(request_amount) == 0;
			if (!result) {
				msg = "预期SKU_ID:" + refund_sku_id + ",实际SKU_ID:" + refund.getDetail_id();
				logger.warn(msg);
				ReporterCSS.warn(msg);
				msg = "预期退货数:" + request_amount + ",实际退货数:" + refund.getRequest_amount_forsale();
				logger.warn(msg);
				ReporterCSS.warn(msg);
			}
			Assert.assertEquals(result, true, "订单" + order_id + "录入保存的退货信息与预期的售后信息不一致");

			// 验证订单列表展示的异常金额
			OrderBean order = orderService.getOrderBeanById(order_id);
			Assert.assertNotEquals(order, null, "订单列表没有找到订单 " + order_id);

			BigDecimal abnormal_money = order.getAbnormal_money();
			Assert.assertEquals(exception_money.compareTo(abnormal_money.multiply(new BigDecimal("-1"))) == 0, true,
					"订单列表中订单 " + order_id + " 展示的异常金额与预期异常金额不一致,预期: " + exception_money + ",实际: "
							+ abnormal_money.multiply(new BigDecimal("-1")));
		} catch (Exception e) {
			logger.error("订单添加售后处理遇到错误: ", e);
			Assert.fail("订单添加售后处理遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionTestCase04() {
		ReporterCSS.title("测试点) 订单商品中全部商品都添加异常信息");
		try {
			List<Detail> details = orderDetail.getDetails();
			List<OrderExceptionParam> orderExceptionArray = new ArrayList<OrderExceptionParam>();
			BigDecimal exception_money = BigDecimal.ZERO;
			for (Detail detail : details) {
				String sku_id = detail.getSku_id();
				// 都以基本单位计算
				BigDecimal std_sale_price_forsale = detail.getStd_sale_price_forsale(); // 商品单价(基本单位)
				BigDecimal std_unit_quantity = detail.getStd_unit_quantity(); // 商品下单数(基本单位)
				int max = std_unit_quantity.intValue();
				// 异常数随机值
				BigDecimal abnormal_quantity = NumberUtil.getRandomNumber(1, max, 2);
				exception_money = exception_money.add(std_sale_price_forsale.multiply(abnormal_quantity));

				BigDecimal final_amount = std_unit_quantity.subtract(abnormal_quantity);
				orderExceptionArray.add(new OrderExceptionParam(final_amount, sku_id, 1, 1, 2));
			}

			boolean result = orderService.addOrderException(order_id, orderExceptionArray,
					new ArrayList<OrderRefundParam>());
			Assert.assertEquals(result, true, "订单添加售后处理失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			List<Abnormal> abnormals = orderDetail.getAbnormals();
			Assert.assertNotEquals(abnormals, null, "订单详细 " + order_id + " 没有商品异常相关信息");

			Assert.assertEquals(abnormals.size(), orderDetail.getDetails().size(),
					"订单详细 " + order_id + " 中的商品异常相关信息条目数与预期不符");

			BigDecimal abnormal_money = orderDetail.getAbnormal_money().multiply(new BigDecimal("-1"));
			exception_money = exception_money.setScale(2, BigDecimal.ROUND_HALF_UP);
			Assert.assertEquals(abnormal_money.compareTo(exception_money) == 0, true,
					"订单" + order_id + "异常金额与预期异常金额不一致,预期: " + exception_money + ",实际:" + abnormal_money);

			// 验证订单列表展示的异常金额
			OrderBean order = orderService.getOrderBeanById(order_id);
			Assert.assertNotEquals(order, null, "订单列表没有找到订单 " + order_id);

			abnormal_money = order.getAbnormal_money().multiply(new BigDecimal("-1"));
			Assert.assertEquals(exception_money.compareTo(abnormal_money) == 0, true,
					"订单列表中订单 " + order_id + " 展示的异常金额与预期异常金额不一致,预期: " + exception_money + ",实际: " + abnormal_money);
		} catch (Exception e) {
			logger.error("订单添加售后处理遇到错误: ", e);
			Assert.fail("订单添加售后处理遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionTestCase05() {
		ReporterCSS.title("测试点) 订单商品中全部商品都添加退货信息");
		try {
			List<Detail> details = orderDetail.getDetails();
			List<OrderRefundParam> orderRefundArray = new ArrayList<OrderRefundParam>();
			for (Detail detail : details) {
				String sku_id = detail.getSku_id();
				// 退货数(基本单位)
				int max = detail.getStd_real_quantity().intValue();
				BigDecimal request_amount = NumberUtil.getRandomNumber(1, max, 2);
				OrderRefundParam orderRefund = new OrderRefundParam(request_amount, sku_id, station_id, 1, 1);
				orderRefundArray.add(orderRefund);
			}
			boolean result = orderService.addOrderException(order_id, new ArrayList<OrderExceptionParam>(),
					orderRefundArray);
			Assert.assertEquals(result, true, "订单添加售后处理失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取 " + order_id + " 订单详细信息失败");

			List<Refund> refunds = orderDetail.getRefunds();
			Assert.assertNotEquals(refunds, null, "订单 " + order_id + " 详细信息中没有退货相关信息");

			Assert.assertEquals(refunds.size(), orderDetail.getDetails().size(), "订单" + order_id + "详细中的退货信息条目数与预期不一致");
		} catch (Exception e) {
			logger.error("订单添加售后处理遇到错误: ", e);
			Assert.fail("订单添加售后处理遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionTestCase06() {
		ReporterCSS.title("测试点) 订单添加商品异常售后信息后再删除");
		try {
			List<Detail> details = orderDetail.getDetails();
			List<OrderExceptionParam> orderExceptionArray = new ArrayList<OrderExceptionParam>();
			for (Detail detail : details) {
				String sku_id = detail.getSku_id();
				// 都以基本单位计算

				BigDecimal std_unit_quantity = detail.getStd_unit_quantity(); // 商品下单数(基本单位)
				int max = std_unit_quantity.intValue();
				// 异常数随机值
				BigDecimal abnormal_quantity = NumberUtil.getRandomNumber(1, max, 2);

				BigDecimal final_amount = std_unit_quantity.subtract(abnormal_quantity);
				orderExceptionArray.add(new OrderExceptionParam(final_amount, sku_id, 1, 1, 2));
			}

			boolean result = orderService.addOrderException(order_id, orderExceptionArray,
					new ArrayList<OrderRefundParam>());
			Assert.assertEquals(result, true, "订单添加售后处理失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			List<Abnormal> abnormals = orderDetail.getAbnormals();
			Assert.assertNotEquals(abnormals, null, "订单详细 " + order_id + " 没有商品异常相关信息");

			Assert.assertEquals(abnormals.size(), orderDetail.getDetails().size(),
					"订单详细 " + order_id + " 中的商品异常相关信息条目数与预期不符");

			result = orderService.addOrderException(order_id, new ArrayList<OrderExceptionParam>(),
					new ArrayList<OrderRefundParam>());
			Assert.assertEquals(result, true, "订单删除售后处理失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			abnormals = orderDetail.getAbnormals();
			Assert.assertEquals(abnormals == null || abnormals.size() == 0, true,
					"订单详细 " + order_id + " 中的商品异常相关信息没有删除");

			Assert.assertEquals(orderDetail.getAbnormal_money().compareTo(BigDecimal.ZERO) == 0, true,
					"订单 " + order_id + "商品异常已经删除,却还存在异常金额 " + orderDetail.getAbnormal_money());

		} catch (Exception e) {
			logger.error("订单添加售后处理遇到错误: ", e);
			Assert.fail("订单添加售后处理遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionTestCase07() {
		ReporterCSS.title("测试点) 订单中的商品添加退货信息后再删除");
		try {
			List<Detail> details = orderDetail.getDetails();
			List<OrderRefundParam> orderRefundArray = new ArrayList<OrderRefundParam>();
			for (Detail detail : details) {
				String sku_id = detail.getSku_id();
				// 退货数(基本单位)
				int max = detail.getStd_real_quantity().intValue();
				BigDecimal request_amount = NumberUtil.getRandomNumber(1, max, 2);
				OrderRefundParam orderRefund = new OrderRefundParam(request_amount, sku_id, station_id, 1, 1);
				orderRefundArray.add(orderRefund);
			}
			boolean result = orderService.addOrderException(order_id, new ArrayList<OrderExceptionParam>(),
					orderRefundArray);
			Assert.assertEquals(result, true, "订单添加售后处理失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取 " + order_id + " 订单详细信息失败");

			List<Refund> refunds = orderDetail.getRefunds();
			Assert.assertNotEquals(refunds, null, "订单 " + order_id + " 详细信息中没有退货相关信息");

			Assert.assertEquals(refunds.size(), orderDetail.getDetails().size(), "订单" + order_id + "详细中的退货信息条目数与预期不一致");

			result = orderService.addOrderException(order_id, new ArrayList<OrderExceptionParam>(),
					new ArrayList<OrderRefundParam>());
			Assert.assertEquals(result, true, "订单 " + order_id + " 删除商品退货信息失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取 " + order_id + " 订单详细信息失败");

			refunds = orderDetail.getRefunds();
			Assert.assertEquals(refunds == null || refunds.size() == 0, true, "订单 " + order_id + " 中的商品退货信息没有删除");
		} catch (Exception e) {
			logger.error("订单添加售后处理遇到错误: ", e);
			Assert.fail("订单添加售后处理遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionTestCase08() {
		ReporterCSS.title("测试点) 订单添加商品异常和商品退货信息后再删除");
		// 选取一个商品,添加商品异常
		Assert.assertEquals(orderDetail.getDetails().size() >= 2, true, "订单 " + order_id + " 中的商品数不足以同时添加退货和异常");

		// 商品退货
		Detail refundSku = orderDetail.getDetails().get(0);
		BigDecimal request_amount = NumberUtil.getRandomNumber(1, refundSku.getStd_unit_quantity().intValue(), 2);
		String refund_sku_id = refundSku.getSku_id();

		// 商品异常
		Detail exceptionSku = orderDetail.getDetails().get(1);
		String exception_sku_id = exceptionSku.getSku_id();
		BigDecimal final_amount = NumberUtil.getRandomNumber(1, exceptionSku.getStd_unit_quantity().intValue(), 2);

		try {
			List<OrderRefundParam> orderRundArray = new ArrayList<OrderRefundParam>();
			orderRundArray.add(new OrderRefundParam(request_amount, refund_sku_id, station_store_id, 1, 1));

			List<OrderExceptionParam> orderExceptionArray = new ArrayList<OrderExceptionParam>();
			orderExceptionArray.add(new OrderExceptionParam(final_amount, exception_sku_id, 1, 1, 2));

			boolean result = orderService.addOrderException(order_id, orderExceptionArray, orderRundArray);
			Assert.assertEquals(result, true, "订单添加售后处理失败");

			result = orderService.addOrderException(order_id, new ArrayList<OrderExceptionParam>(),
					new ArrayList<OrderRefundParam>());
			Assert.assertEquals(result, true, "订单 " + order_id + " 删除商品退货和商品异常信息失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			List<Refund> refunds = orderDetail.getRefunds();
			Assert.assertEquals(refunds == null || refunds.size() == 0, true, "订单 " + order_id + " 中的商品退货信息没有删除");

			List<Abnormal> abnormals = orderDetail.getAbnormals();
			Assert.assertEquals(abnormals == null || abnormals.size() == 0, true,
					"订单详细 " + order_id + " 中的商品异常相关信息没有删除");

			Assert.assertEquals(orderDetail.getAbnormal_money().compareTo(BigDecimal.ZERO) == 0, true,
					"订单 " + order_id + "商品异常已经删除,却还存在异常金额 " + orderDetail.getAbnormal_money());
		} catch (Exception e) {
			logger.error("订单添加售后处理遇到错误: ", e);
			Assert.fail("订单添加售后处理遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionTestCase09() {
		ReporterCSS.title("测试点) 尝试删除存在商品异常的订单,断言删除失败");
		// 选取一个商品,添加商品异常
		Detail detail = NumberUtil.roundNumberInList(orderDetail.getDetails());

		String sku_id = detail.getSku_id();
		// 都以基本单位计算
		BigDecimal std_unit_quantity = detail.getStd_unit_quantity(); // 商品下单数(基本单位)
		int max = std_unit_quantity.intValue();
		// 异常数随机值
		BigDecimal abnormal_quantity = NumberUtil.getRandomNumber(1, max, 2);

		BigDecimal final_amount = std_unit_quantity.subtract(abnormal_quantity);

		List<OrderExceptionParam> orderExceptionArray = new ArrayList<OrderExceptionParam>();
		orderExceptionArray.add(new OrderExceptionParam(final_amount, sku_id, 1, 1, 2));

		try {
			boolean result = orderService.addOrderException(order_id, orderExceptionArray,
					new ArrayList<OrderRefundParam>());
			Assert.assertEquals(result, true, "订单添加售后处理失败");

			result = orderService.deleteOrder(order_id);
			Assert.assertEquals(result, false, "尝试删除存在商品异常的订单,断言删除失败");

			OrderBean order = orderService.getOrderBeanById(order_id);
			Assert.assertNotEquals(order, null, "删除订单提示删除失败,实际却删除成功了");
		} catch (Exception e) {
			logger.error("订单添加售后处理遇到错误: ", e);
			Assert.fail("订单添加售后处理遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionTestCase10() {
		ReporterCSS.title("测试点) 删除添加了商品退货的订单,断言删除失败");
		// 选取一个商品,添加商品退货
		Detail detail = NumberUtil.roundNumberInList(orderDetail.getDetails());

		String sku_id = detail.getSku_id();
		// 退货数(基本单位)
		int max = detail.getStd_real_quantity().intValue();
		BigDecimal request_amount = NumberUtil.getRandomNumber(1, max, 2);

		try {
			OrderRefundParam orderRefund = new OrderRefundParam(request_amount, sku_id, station_id, 1, 1);
			List<OrderRefundParam> orderRefundArray = new ArrayList<OrderRefundParam>();
			orderRefundArray.add(orderRefund);

			boolean result = orderService.addOrderException(order_id, new ArrayList<OrderExceptionParam>(),
					orderRefundArray);
			Assert.assertEquals(result, true, "订单添加售后处理失败");

			result = orderService.deleteOrder(order_id);
			Assert.assertEquals(result, false, "尝试删除存在商品异常的订单,断言删除失败");

			OrderBean order = orderService.getOrderBeanById(order_id);
			Assert.assertNotEquals(order, null, "删除订单提示删除失败,实际却删除成功了");
		} catch (Exception e) {
			logger.error("订单添加售后处理遇到错误: ", e);
			Assert.fail("订单添加售后处理遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionTestCase11() {
		ReporterCSS.title("测试点) 订单添加商品异常售后信息后再删除,再删除订单");
		try {
			List<Detail> details = orderDetail.getDetails();
			List<OrderExceptionParam> orderExceptionArray = new ArrayList<OrderExceptionParam>();
			for (Detail detail : details) {
				String sku_id = detail.getSku_id();
				// 都以基本单位计算

				BigDecimal std_unit_quantity = detail.getStd_unit_quantity(); // 商品下单数(基本单位)
				int max = std_unit_quantity.intValue();
				// 异常数随机值
				BigDecimal abnormal_quantity = NumberUtil.getRandomNumber(1, max, 2);

				BigDecimal final_amount = std_unit_quantity.subtract(abnormal_quantity);
				orderExceptionArray.add(new OrderExceptionParam(final_amount, sku_id, 1, 1, 2));
			}

			boolean result = orderService.addOrderException(order_id, orderExceptionArray,
					new ArrayList<OrderRefundParam>());
			Assert.assertEquals(result, true, "订单添加售后处理失败");

			result = orderService.addOrderException(order_id, new ArrayList<OrderExceptionParam>(),
					new ArrayList<OrderRefundParam>());
			Assert.assertEquals(result, true, "订单删除售后处理失败");

			result = orderService.deleteOrder(order_id);
			Assert.assertEquals(result, true, "订单添加商品异常售后信息后再删除,再删除订单,断言删除成功");

			OrderBean order = orderService.getOrderBeanById(order_id);
			Assert.assertEquals(order, null, "订单 " + order_id + " 没有真正的删除成功,在订单列表还可以查询到");
		} catch (Exception e) {
			logger.error("订单添加售后处理遇到错误: ", e);
			Assert.fail("订单添加售后处理遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionTestCase12() {
		ReporterCSS.title("测试点) 订单中的商品添加退货信息后再删除,再删除订单");
		try {
			List<Detail> details = orderDetail.getDetails();
			List<OrderRefundParam> orderRefundArray = new ArrayList<OrderRefundParam>();
			for (Detail detail : details) {
				String sku_id = detail.getSku_id();
				// 退货数(基本单位)
				int max = detail.getStd_real_quantity().intValue();
				BigDecimal request_amount = NumberUtil.getRandomNumber(1, max, 2);
				OrderRefundParam orderRefund = new OrderRefundParam(request_amount, sku_id, station_id, 1, 1);
				orderRefundArray.add(orderRefund);
			}
			boolean result = orderService.addOrderException(order_id, new ArrayList<OrderExceptionParam>(),
					orderRefundArray);
			Assert.assertEquals(result, true, "订单添加售后处理失败");

			result = orderService.addOrderException(order_id, new ArrayList<OrderExceptionParam>(),
					new ArrayList<OrderRefundParam>());
			Assert.assertEquals(result, true, "订单 " + order_id + " 删除商品退货信息失败");

			result = orderService.deleteOrder(order_id);
			Assert.assertEquals(result, true, "订单中的商品添加退货信息后再删除,再删除订单,断言删除成功");

			OrderBean order = orderService.getOrderBeanById(order_id);
			Assert.assertEquals(order, null, "订单 " + order_id + " 没有真正的删除成功,在订单列表还可以查询到");
		} catch (Exception e) {
			logger.error("订单添加售后处理遇到错误: ", e);
			Assert.fail("订单添加售后处理遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionTestCase13() {
		ReporterCSS.title("测试点) 订单添加退货售后信息后,在进销存的商户退货入库查询退货信息");
		try {
			List<Detail> details = orderDetail.getDetails();
			List<OrderRefundParam> orderRefundArray = new ArrayList<OrderRefundParam>();
			for (Detail detail : details) {
				String sku_id = detail.getSku_id();
				// 退货数(基本单位)
				int max = detail.getStd_real_quantity().intValue();
				BigDecimal request_amount = NumberUtil.getRandomNumber(1, max, 2);
				OrderRefundParam orderRefund = new OrderRefundParam(request_amount, sku_id, station_id, 1, 1);
				orderRefundArray.add(orderRefund);
			}
			boolean result = orderService.addOrderException(order_id, new ArrayList<OrderExceptionParam>(),
					orderRefundArray);
			Assert.assertEquals(result, true, "订单添加售后处理失败");

			RefundStockFilterParam refundStockFilterParam = new RefundStockFilterParam();
			refundStockFilterParam.setDate_from(todayStr);
			refundStockFilterParam.setDate_end(todayStr);
			refundStockFilterParam.setOrder_id(order_id);
			refundStockFilterParam.setPage(0);
			refundStockFilterParam.setNum(20);

			List<RefundStockResultBean> refundResuls = refundService.searchRefundStock(refundStockFilterParam);
			Assert.assertNotEquals(refundResuls, null, "获取商户退货入库信息失败");

			Assert.assertEquals(refundResuls.size(), orderDetail.getDetails().size(),
					"查询到的订单" + order_id + "商户退货入库条目数与预期不符");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + "详细信息失败");
			List<Refund> refunds = orderDetail.getRefunds();
			BigDecimal refund_amount = null;
			RefundStockResultBean refundResult = null;
			BigDecimal std_ratio = null;
			String msg = null;
			for (Refund refund : refunds) {
				String sku_id = refund.getDetail_id();
				refund_amount = refund.getRequest_amount_forsale();
				refundResult = refundResuls.stream().filter(r -> r.getSku_id().equals(sku_id)).findAny().orElse(null);
				Assert.assertNotEquals(refundResult, null, "录入的退货商品 " + sku_id + " 在商户退货页面没有查询到");
				std_ratio = refundResult.getStd_ratio();
				if (refund_amount.compareTo(refundResult.getRequest_amount().divide(std_ratio)) != 0) {
					msg = String.format("订单%s录入的退货商品%s退货数和商户退货入库页面展示的退货数不一致,预期%s,实际:%s", order_id, sku_id,
							refund_amount.multiply(std_ratio), refundResult.getRequest_amount());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单 " + order_id + " 录入的退货信息与在商户退货入库页面查看到的数据不一致");
		} catch (Exception e) {
			logger.error("订单添加售后处理遇到错误: ", e);
			Assert.fail("订单添加售后处理遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionTestCase14() {
		ReporterCSS.title("测试点) 订单退货售后信息处理后,查看订单金额");
		try {
			List<Detail> details = orderDetail.getDetails();
			List<OrderRefundParam> orderRefundArray = new ArrayList<OrderRefundParam>();
			for (Detail detail : details) {
				String sku_id = detail.getSku_id();
				// 退货数(基本单位)
				int max = detail.getStd_real_quantity().intValue();
				BigDecimal request_amount = NumberUtil.getRandomNumber(1, max, 2);
				OrderRefundParam orderRefund = new OrderRefundParam(request_amount, sku_id, station_id, 1, 1);
				orderRefundArray.add(orderRefund);
			}
			boolean result = orderService.addOrderException(order_id, new ArrayList<OrderExceptionParam>(),
					orderRefundArray);
			Assert.assertEquals(result, true, "订单添加售后处理失败");

			RefundStockFilterParam refundStockFilterParam = new RefundStockFilterParam();
			refundStockFilterParam.setDate_from(todayStr);
			refundStockFilterParam.setDate_end(todayStr);
			refundStockFilterParam.setOrder_id(order_id);
			refundStockFilterParam.setPage(0);
			refundStockFilterParam.setNum(20);

			List<RefundStockResultBean> refundResuls = refundService.searchRefundStock(refundStockFilterParam);
			Assert.assertNotEquals(refundResuls, null, "获取商户退货入库信息失败");

			Assert.assertEquals(refundResuls.size(), orderDetail.getDetails().size(),
					"查询到的订单" + order_id + "商户退货入库条目数与预期不符");

			BigDecimal expected_refund_money = BigDecimal.ZERO;
			Detail detail = null; // 订单详情里的商品信息

			BigDecimal std_ratio = null;
			for (RefundStockResultBean refundResult : refundResuls) {
				List<RefundStockParam> refundList = new ArrayList<>();

				String sku_id = refundResult.getSku_id();
				detail = orderDetail.getDetails().stream().filter(d -> d.getSku_id().equals(sku_id)).findAny()
						.orElse(null);
				Assert.assertNotEquals(detail, null, "订单 " + order_id + " 详情中没有找到退货商品 " + refundResult.getSku_id());

				BigDecimal std_sale_price_forsale = detail.getStd_sale_price_forsale();

				String purchase_sku_id = refundResult.getPurchase_sku_id();
				List<PurchaseSkuSettleSupplier> suppliers = refundService
						.getPurchaseSkuSettleSupplierList(purchase_sku_id);
				Assert.assertNotEquals(suppliers, null, "获取采购规格" + purchase_sku_id + "对应的供应商列表失败");
				Assert.assertEquals(suppliers.size() > 0, true, "采购规格" + purchase_sku_id + "没有对应的供应商");
				PurchaseSkuSettleSupplier purchaseSkuSettleSupplier = suppliers.get(0);

				BigDecimal request_amount = refundResult.getRequest_amount();
				std_ratio = refundResult.getStd_ratio();
				RefundStockParam refundParam = new RefundStockParam();
				refundParam.setRefund_id(refundResult.getRefund_id());
				refundParam.setSku_name(refundResult.getSku_name());
				refundParam.setSku_id(refundResult.getSku_id());
				refundParam.setSolution(160);
				refundParam.setDriver_id(0);
				// 入库单价为分
				refundParam.setIn_stock_price(std_sale_price_forsale.multiply(new BigDecimal("100"))); // 单位:分

				refundParam.setDisabled_in_stock_price(true);

				// 入库数取退货数整数位
				BigDecimal real_amount = null;
				if (request_amount.compareTo(new BigDecimal("1")) == 1) {
					real_amount = request_amount.setScale(0, BigDecimal.ROUND_FLOOR);
				} else {
					real_amount = request_amount;
				}

				expected_refund_money = expected_refund_money
						.add(real_amount.divide(std_ratio).multiply(std_sale_price_forsale))
						.setScale(2, BigDecimal.ROUND_HALF_UP);

				refundParam.setRequest_amount(request_amount);
				refundParam.setReal_amount(real_amount);
				refundParam.setStore_amount(real_amount);
				refundParam.setDescription("");
				refundParam.setSupplier_id(purchaseSkuSettleSupplier.getSupplier_id());
				refundParam.setSupplier_name(purchaseSkuSettleSupplier.getSupplier_name());
				refundParam.setSale_ratio(refundResult.getSale_ratio());
				refundParam.setPurchase_sku_id(purchase_sku_id);
				refundList.add(refundParam);

				result = refundService.editRefundStock(refundList);
				Assert.assertEquals(result, true, "商户退货入库操作失败");
			}

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			BigDecimal refund_money = orderDetail.getRefund_money().multiply(new BigDecimal("-1"));
			Assert.assertEquals(refund_money.compareTo(expected_refund_money) == 0, true,
					"订单退货售后信息处理后,订单退款金额和预期的不一致,预期:" + expected_refund_money + ",实际" + refund_money);

		} catch (Exception e) {
			logger.error("订单添加售后处理遇到错误: ", e);
			Assert.fail("订单添加售后处理遇到错误: ", e);
		}
	}

}
