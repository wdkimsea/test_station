package cn.guanmai.station.delivery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.delivery.DeliveryConfirmInfoBean;
import cn.guanmai.station.bean.delivery.DeliveryOrderDetail;
import cn.guanmai.station.bean.delivery.DeliveryOrderException;
import cn.guanmai.station.bean.delivery.DeliveryTaskBean;
import cn.guanmai.station.bean.delivery.DriverBean;
import cn.guanmai.station.bean.delivery.HomePageBean;
import cn.guanmai.station.bean.delivery.param.DeliveryOrderExceptionParam;
import cn.guanmai.station.bean.delivery.param.DriverTracePointParam;
import cn.guanmai.station.bean.delivery.param.DriverUpdateParam;
import cn.guanmai.station.bean.order.CustomerBean;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderDetailBean.Detail;
import cn.guanmai.station.bean.share.OrderAndSkuBean;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.bean.weight.param.PrintInfoBean;
import cn.guanmai.station.bean.weight.param.SetWeightParam;
import cn.guanmai.station.impl.delivery.DistributeServiceImpl;
import cn.guanmai.station.impl.delivery.DriverServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.impl.weight.WeightServiceImpl;
import cn.guanmai.station.interfaces.delivery.DistributeService;
import cn.guanmai.station.interfaces.delivery.DriverService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.interfaces.weight.WeightService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.LoginUtil;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;

/* 
* @author liming 
* @date May 7, 2019 7:24:58 PM 
* @des 司机APP测试相关用例
* @version 1.0 
*/
public class DriverAppTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(DriverAppTest.class);
	private LoginUserInfoService loginUserInfoService;
	private DistributeService distributeService;
	private OrderService orderService;
	private WeightService weightService;
	private OrderTool orderTool;
	private DriverService driverService;
	private BigDecimal driver_id;
	private String token;

	@BeforeClass
	public void loginDriverApp() {
		Map<String, String> headers = getStationCookie();
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		distributeService = new DistributeServiceImpl(headers);
		orderService = new OrderServiceImpl(headers);
		weightService = new WeightServiceImpl(headers);
		orderTool = new OrderTool(headers);
		try {
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账户信息失败");

			String station_id = loginUserInfo.getStation_id();

			Assert.assertNotNull(station_id, "获取站点ID失败");
			DriverBean driver = distributeService.initDriverData(station_id);
			Assert.assertNotEquals(driver, null, "初始化司机管理数据失败");

			String password = "Test1234_";
			String account = driver.getAccount();
			driver_id = driver.getId();

			// 重置密码
			DriverUpdateParam driverUpdateParam = new DriverUpdateParam();
			driverUpdateParam.setAccount(driver.getAccount());
			driverUpdateParam.setAllow_login(1);
			driverUpdateParam.setCar_model_id(driver.getCar_model_id());
			driverUpdateParam.setCarrier_id(driver.getCarrier_id());
			driverUpdateParam.setDriver_id(driver_id);
			driverUpdateParam.setMax_load(driver.getMax_load());
			driverUpdateParam.setName(driver.getName());
			driverUpdateParam.setPassword(password);
			driverUpdateParam.setPassword_check(password);
			driverUpdateParam.setPhone(driver.getPhone());
			driverUpdateParam.setPlate_number(driver.getPlate_number());
			driverUpdateParam.setShare(1);
			driverUpdateParam.setState(1);
			boolean result = distributeService.updateDriver(driverUpdateParam);
			Assert.assertEquals(result, true, "修改司机密码失败");

			Map<String, String> driver_info_map = LoginUtil.loginDriverApp(account, password);

			Assert.assertNotNull(driver_info_map, "司机APP登录失败");

			Map<String, String> driver_cookie = new HashMap<String, String>();
			driver_cookie.put("cookie", driver_info_map.get("cookie"));
			driverService = new DriverServiceImpl(driver_cookie);

			token = driver_info_map.get("token");
		} catch (Exception e) {
			logger.error("初始化配送数据遇到错误: ", e);
			Assert.fail("初始化配送数据遇到错误: ", e);
		}
	}

	@Test
	public void driverAppTestCase01() {
		ReporterCSS.title("测试点: 获取配送APP首页数据");
		try {
			HomePageBean homePage = driverService.getHomePageData();
			Assert.assertNotNull(homePage, "获取配送APP首页数据失败");
		} catch (Exception e) {
			logger.error("获取司机APP首页数据遇到错误: ", e);
			Assert.fail("获取司机APP首页数据遇到错误: ", e);
		}
	}

	@Test
	public void driverAppTestCase02() {
		ReporterCSS.title("测试点: 为司机分配配送任务,司机APP验证任务是否收到");
		try {
			String order_id = orderTool.oneStepCreateOrder(8);
			Assert.assertNotNull(order_id, "创建订单失败");

			boolean result = orderService.updateOrderState(order_id, 10);
			Assert.assertEquals(result, true, "修改订单" + order_id + "状态为配送中失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotNull(orderDetail, "获取订单 " + order_id + " 详细信息失败");

			String receive_address_id = orderDetail.getCustomer().getAddress_id();

			String receive_begin_time = orderDetail.getCustomer().getReceive_begin_time();

			result = distributeService.editAssignDistributeTask(order_id, receive_address_id, receive_begin_time,
					driver_id, 1);
			Assert.assertEquals(result, true, "订单分配配送司机失败");

			List<DeliveryTaskBean> deliveryTasks = driverService.getDeliveryTask(false);

			DeliveryTaskBean deliveryTask = deliveryTasks.stream().filter(d -> d.getOrder_id().equals(order_id))
					.findAny().orElse(null);
			Assert.assertNotNull(deliveryTask, "司机APP没有收到系统分配的配送任务单 " + order_id);

			String msg = null;
			if (!orderDetail.getCustomer().getAddress().equals(deliveryTask.getAddress())) {
				msg = String.format("配送APP中配送任务%s显示的配送任务地址和订单详细中显示的配送任务地址不一致,预期:%s,实际:%s", order_id,
						orderDetail.getCustomer().getAddress(), deliveryTask.getAddress());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!orderDetail.getCustomer().getExtender().getResname().equals(deliveryTask.getResname())) {
				msg = String.format("配送APP中配送任务%s显示的商户名称和订单详细中显示的商户名称不一致,预期:%s,实际:%s", order_id,
						orderDetail.getCustomer().getExtender().getResname(), deliveryTask.getResname());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!orderDetail.getCustomer().getReceiver_phone().equals(deliveryTask.getReceiver_phone())) {
				msg = String.format("配送APP中配送任务%s显示的收货电话和订单详细中显示的商户电话不一致,预期:%s,实际:%s", order_id,
						orderDetail.getCustomer().getReceiver_phone(), deliveryTask.getReceiver_phone());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!orderDetail.getCustomer().getReceive_begin_time().equals(deliveryTask.getReceive_begin_time())) {
				msg = String.format("配送APP中配送任务%s显示的收货起始时间和订单详细中显示的收货起始时间不一致,预期:%s,实际:%s", order_id,
						orderDetail.getCustomer().getReceive_begin_time(), deliveryTask.getReceive_begin_time());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!orderDetail.getCustomer().getReceive_end_time().equals(deliveryTask.getReceive_end_time())) {
				msg = String.format("配送APP中配送任务%s显示的收货结束时间和订单详细中显示的收货结束时间不一致,预期:%s,实际:%s", order_id,
						orderDetail.getCustomer().getReceive_end_time(), deliveryTask.getReceive_end_time());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (orderDetail.getTotal_pay().compareTo(deliveryTask.getTotal_pay()) != 0) {
				msg = String.format("配送APP中配送任务%s显示的应付金额和订单详细中显示的应付金额不一致,预期:%s,实际:%s", order_id,
						orderDetail.getTotal_pay(), deliveryTask.getTotal_pay());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (orderDetail.getPay_status() != deliveryTask.getPay_status()) {
				msg = String.format("配送APP中配送任务%s显示的支付状态和订单详细中显示的支付状态不一致,预期:%s,实际:%s", order_id,
						orderDetail.getPay_status(), deliveryTask.getPay_status());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "配送APP中配送任务显示的信息和ST订单详细中显示的信息不一致");
		} catch (Exception e) {
			logger.error("验证司机APP是否收到配送任务遇到错误: ", e);
			Assert.fail("验证司机APP是否收到配送任务遇到错误: ", e);
		}
	}

	@Test
	public void driverAppTestCase03() {
		ReporterCSS.title("测试点: 司机APP对配送订单做完成配送操作");
		try {
			String order_id = orderTool.oneStepCreateOrder(8);
			Assert.assertNotNull(order_id, "创建订单失败");

			boolean result = orderService.updateOrderState(order_id, 10);
			Assert.assertEquals(result, true, "修改订单 " + order_id + " 状态为配送中失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotNull(orderDetail, "获取订单 " + order_id + " 详细信息失败");

			String receive_address_id = orderDetail.getCustomer().getAddress_id();

			String receive_begin_time = orderDetail.getCustomer().getReceive_begin_time();

			result = distributeService.editAssignDistributeTask(order_id, receive_address_id, receive_begin_time,
					driver_id, 1);
			Assert.assertEquals(result, true, "订单分配配送司机失败");

			List<DeliveryTaskBean> deliveryTasks = driverService.getDeliveryTask(false);

			DeliveryTaskBean deliveryTask = deliveryTasks.stream().filter(d -> d.getOrder_id().equals(order_id))
					.findAny().orElse(null);
			Assert.assertNotNull(deliveryTask, "司机APP没有收到系统分配的配送任务单 " + order_id);

			result = driverService.finishDelivery(order_id);
			Assert.assertEquals(result, true, "司机APP,对配送订单进行完成配送操作失败");
		} catch (Exception e) {
			logger.error("司机APP对配送订单做完成配送操作遇到错误: ", e);
			Assert.fail("司机APP对配送订单做完成配送操作遇到错误: ", e);
		}
	}

	@Test
	public void driverAppTestCase04() {
		ReporterCSS.title("测试点: 配送司机APP,获取配送订单详细信息");
		try {
			String order_id = orderTool.oneStepCreateOrder(8);
			Assert.assertNotNull(order_id, "创建订单失败");

			boolean result = orderService.updateOrderState(order_id, 10);
			Assert.assertEquals(result, true, "修改订单 " + order_id + " 状态为配送中失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotNull(orderDetail, "获取订单 " + order_id + " 详细信息失败");

			String receive_address_id = orderDetail.getCustomer().getAddress_id();

			String receive_begin_time = orderDetail.getCustomer().getReceive_begin_time();

			result = distributeService.editAssignDistributeTask(order_id, receive_address_id, receive_begin_time,
					driver_id, 1);
			Assert.assertEquals(result, true, "订单分配配送司机失败");

			List<DeliveryTaskBean> deliveryTasks = driverService.getDeliveryTask(false);

			DeliveryTaskBean deliveryTask = deliveryTasks.stream().filter(d -> d.getOrder_id().equals(order_id))
					.findAny().orElse(null);
			Assert.assertNotNull(deliveryTask, "司机APP没有收到系统分配的配送任务单 " + order_id);

			DeliveryOrderDetail deliveryOrderDetail = driverService.getDeliveryOrderDetail(order_id, null);
			Assert.assertNotNull(deliveryOrderDetail, "司机APP,获取配送单详细信息失败");

			Reporter.log("司机APP中配送单详细信息与订单列表中的订单详细信息对比");
			String msg = null;
			if (orderDetail.getReal_price().compareTo(deliveryOrderDetail.getReal_price()) != 0) {
				result = false;
				msg = String.format("配送任务单%s显示的应付金额%s和订单列表显示的应付金额%s不一致", order_id, orderDetail.getReal_price(),
						deliveryOrderDetail.getReal_price());
				ReporterCSS.warn(msg);
			}

			DeliveryOrderDetail.Detail d_detail = null;
			for (OrderDetailBean.Detail o_detail : orderDetail.getDetails()) {
				String sku_id = o_detail.getSku_id();
				d_detail = deliveryOrderDetail.getDetails().stream().filter(d -> d.getSku_id().equals(sku_id)).findAny()
						.orElse(null);
				if (d_detail == null) {
					msg = String.format("配送任务单%s详细信息缺少商品%s", order_id, sku_id);
					ReporterCSS.warn(msg);
					result = false;
				} else {
					if (!o_detail.getSku_name().equals(d_detail.getSku_name())) {
						msg = String.format("配送任务单%s中的商品%s显示的商品名称不正确,预期:%s,实际:%s", order_id, o_detail.getSku_name(),
								d_detail.getSku_name());
						ReporterCSS.warn(msg);
						result = false;
					}

					if (!o_detail.getSale_unit_name().equals(d_detail.getSale_unit_name())) {
						msg = String.format("配送任务单%s中的商品%s显示的销售单位不正确,预期:%s,实际:%s", order_id,
								o_detail.getSale_unit_name(), d_detail.getSale_unit_name());
						ReporterCSS.warn(msg);
						result = false;
					}

					if (o_detail.getQuantity().compareTo(d_detail.getQuantity()) != 0) {
						msg = String.format("配送任务单%s中的商品%s显示的下单数不正确,预期:%s,实际:%s", order_id, o_detail.getQuantity(),
								d_detail.getQuantity());
						ReporterCSS.warn(msg);
						result = false;
					}

					if (o_detail.getReal_quantity().compareTo(d_detail.getReal_quantity()) != 0) {
						msg = String.format("配送任务单%s中的商品%s显示的出库数不正确,预期:%s,实际:%s", order_id, o_detail.getReal_quantity(),
								d_detail.getReal_quantity());
						ReporterCSS.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "司机APP中显示的配送任务信息和订单列表看到的订单信息不一致");
		} catch (Exception e) {
			logger.error("验证司机APP是否收到配送任务遇到错误: ", e);
			Assert.fail("验证司机APP是否收到配送任务遇到错误: ", e);
		}
	}

	@Test
	public void driverAppTestCase05() {
		ReporterCSS.title("测试点: 司机APP,获取订单的装车验货信息");
		try {
			String order_id = orderTool.oneStepCreateOrder(8);
			Assert.assertNotNull(order_id, "创建订单失败");

			boolean result = orderService.updateOrderState(order_id, 10);
			Assert.assertEquals(result, true, "修改订单 " + order_id + " 状态为配送中失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotNull(orderDetail, "获取订单 " + order_id + " 详细信息失败");

			String receive_address_id = orderDetail.getCustomer().getAddress_id();

			String receive_begin_time = orderDetail.getCustomer().getReceive_begin_time();

			result = distributeService.editAssignDistributeTask(order_id, receive_address_id, receive_begin_time,
					driver_id, 1);
			Assert.assertEquals(result, true, "订单分配配送司机失败");

			List<DeliveryTaskBean> deliveryTasks = driverService.getDeliveryTask(false);

			DeliveryTaskBean deliveryTask = deliveryTasks.stream().filter(d -> d.getOrder_id().equals(order_id))
					.findAny().orElse(null);
			Assert.assertNotNull(deliveryTask, "司机APP没有收到系统分配的配送任务单 " + order_id);

			DeliveryConfirmInfoBean deliveryConfirmInfo = driverService.getDeliveryConfirmInfo(order_id,
					deliveryTask.getDelivery_id());
			Assert.assertNotNull(deliveryConfirmInfo, "司机APP获取订单的装车验货信息失败");

			Assert.assertEquals(deliveryConfirmInfo.getSku_count() == orderDetail.getDetails().size(), true,
					"司机APP,获取到的订单装车商品数与预期不符,预期: " + orderDetail.getDetails().size() + ",实际: "
							+ deliveryConfirmInfo.getSku_count());
		} catch (Exception e) {
			logger.error("司机APP,获取订单的装车验货信息遇到错误: ", e);
			Assert.fail("司机APP,获取订单的装车验货信息遇到错误: ", e);
		}
	}

	@Test
	public void driverAppTestCase06() {
		ReporterCSS.title("测试点: 司机APP,对订单进行商品验证装车");
		try {
			String order_id = orderTool.oneStepCreateOrder(8);
			Assert.assertNotNull(order_id, "创建订单失败");

			boolean result = orderService.updateOrderState(order_id, 10);
			Assert.assertEquals(result, true, "修改订单 " + order_id + " 状态为配送中失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotNull(orderDetail, "获取订单 " + order_id + " 详细信息失败");

			String receive_address_id = orderDetail.getCustomer().getAddress_id();

			String receive_begin_time = orderDetail.getCustomer().getReceive_begin_time();

			result = distributeService.editAssignDistributeTask(order_id, receive_address_id, receive_begin_time,
					driver_id, 1);
			Assert.assertEquals(result, true, "订单分配配送司机失败");

			List<DeliveryTaskBean> deliveryTasks = driverService.getDeliveryTask(false);

			DeliveryTaskBean deliveryTask = deliveryTasks.stream().filter(d -> d.getOrder_id().equals(order_id))
					.findAny().orElse(null);
			Assert.assertNotNull(deliveryTask, "司机APP没有收到系统分配的配送任务单 " + order_id);

			JSONArray sku_ids = new JSONArray();
			for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
				sku_ids.add(detail.getSku_id());
			}

			String delivery_id = deliveryTask.getDelivery_id();
			result = driverService.productConfirm(order_id, sku_ids, delivery_id, 2);
			Assert.assertEquals(result, true, "司机APP,对订单进行商品验证装车操作失败");

			result = driverService.deliveryConfirm(order_id, delivery_id);
			Assert.assertEquals(result, true, "订单完成装车验证操作失败");
		} catch (Exception e) {
			logger.error("司机APP,对订单进行商品验证装车遇到错误: ", e);
			Assert.fail("司机APP,对订单进行商品验证装车遇到错误: ", e);
		}
	}

	@Test
	public void driverAppTestCase07() {
		ReporterCSS.title("测试点: 司机APP,更新商户收货地址");
		try {
			String lat = "22.535121022367814";
			String lng = "113.94142037867356";
			String address = "广东省深圳市南山区科技南一路1号";

			List<CustomerBean> customerArray = orderService.getOrderCustomerArray(6);
			Assert.assertNotEquals(customerArray, null, "获取下单商户列表信息失败");

			Assert.assertEquals(customerArray.size() > 0, true, "无可用下单商户");

			// 随机选取一个正常商户进行下单
			CustomerBean customer = NumberUtil.roundNumberInList(customerArray);
			String address_id = customer.getAddress_id();
			boolean result = driverService.udpdateCustomerAddress(address_id, lat, lng, address);
			Assert.assertEquals(result, true, "司机APP更新商户收货地址失败");
		} catch (Exception e) {
			logger.error("司机APP,更新商户收货地址遇到错误: ", e);
			Assert.fail("司机APP,更新商户收货地址遇到错误: ", e);
		}
	}

	@Test
	public void driverAppTestCase08() {
		ReporterCSS.title("测试点: 司机APP代客户发起订单售后(退货)");
		try {
			String order_id = orderTool.oneStepCreateOrder(8);
			Assert.assertNotNull(order_id, "创建订单失败");

			boolean result = orderService.updateOrderState(order_id, 10);
			Assert.assertEquals(result, true, "修改订单 " + order_id + " 状态为配送中失败");

			result = driverService.exceptionOptions(order_id);
			Assert.assertEquals(result, true, "司机APP,获取订单售后选项失败");

			DeliveryOrderDetail deliveryOrderDetail = driverService.getDeliveryOrderDetail(order_id, null);
			Assert.assertNotNull(deliveryOrderDetail, "司机APP获取订单详情信息失败");

			// 选取第一个商品做退货处理
			DeliveryOrderDetail.Detail detail = deliveryOrderDetail.getDetails().get(0);

			DeliveryOrderExceptionParam param = new DeliveryOrderExceptionParam();
			param.setOrder_id(order_id);

			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotNull(loginUserInfo, "获取ST登录账号信息失败");
			String station_id = loginUserInfo.getStation_id();

			List<DeliveryOrderExceptionParam.RefundParam> refundParamList = new ArrayList<>();

			DeliveryOrderExceptionParam.RefundParam refundParam = param.new RefundParam();
			refundParam.setSku_id(detail.getSku_id());
			refundParam.setState(2);
			refundParam.setException_reason(1);
			refundParam.setStation_blame_id(station_id);
			refundParam.setStation_store_id(station_id);
			refundParam.setStation_to_id(station_id);
			BigDecimal request_amount_forsale = new BigDecimal("2");
			refundParam.setRequest_amount_forsale(request_amount_forsale);
			refundParam.setLose_money(new BigDecimal("0"));

			if (detail.getDetail_id() != null) {
				BigDecimal detail_id = detail.getDetail_id();
				refundParam.setDetail_id(detail_id);
				refundParam.setOrder_detail_id(detail_id);
			}

			refundParamList.add(refundParam);

			param.setRefunds(refundParamList);
			param.setExceptions(new ArrayList<DeliveryOrderExceptionParam.ExceptionParam>());

			result = driverService.checkReturnDeliveryOrder(param);
			Assert.assertEquals(result, true, "司机APP添加订单售后的前置检查操作失败");

			// 这里其实算一个BUG,订单没有分配司机,司机就可以直接带客户发起售后
			result = driverService.updateDeliveryOrderException(param);
			Assert.assertEquals(result, true, "司机APP代客户添加订单退货请求操作失败");

			Reporter.log("司机APP添加完售后,再次验证售后是否真正添加成功");
			DeliveryOrderException deliveryOrderException = driverService.getDeliveryOrderException(order_id);
			Assert.assertNotNull(deliveryOrderException, "司机APP获取订单售后信息失败");

			Assert.assertEquals(deliveryOrderException.getRefunds().size() == 1, true, "司机APP获取到的订单售后信息为空,与预期不符");

			DeliveryOrderException.SaleRefund saleRefund = deliveryOrderException.getRefunds().get(0);

			String msg = null;
			if (!saleRefund.getSku_id().equals(detail.getSku_id())) {
				msg = String.format("司机APP显示的售后商品ID与预期不符,预期:%s,实际:%s", detail.getSku_id(), saleRefund.getSku_id());
				ReporterCSS.warn(msg);
				result = false;
			}

			if (!saleRefund.getSku_name().equals(detail.getSku_name())) {
				msg = String.format("司机APP显示的售后商品名称与预期不符,预期:%s,实际:%s", detail.getSku_name(), saleRefund.getSku_name());
				ReporterCSS.warn(msg);
				result = false;
			}

			if (saleRefund.getRequest_amount_forsale().compareTo(request_amount_forsale) != 0) {
				msg = String.format("司机APP显示的售后商品退货数与预期不符,预期:%s,实际:%s", request_amount_forsale,
						saleRefund.getRequest_amount_forsale());
				ReporterCSS.warn(msg);
				result = false;
			}

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotNull(orderDetail, "ST获取订单详情失败");

			if (orderDetail.getRefunds().size() != 1) {
				msg = String.format("ST获取的订单%s详情中没有商户退货信息,与预期不符", order_id);
				ReporterCSS.warn(msg);
				result = false;
			} else {
				OrderDetailBean.Refund refund = orderDetail.getRefunds().get(0);
				if (!refund.getDetail_id().equals(saleRefund.getSku_id())) {
					msg = String.format("ST获取的订单%s详情中退货商品ID与预期不符,预期:%s,实际:%s", order_id, detail.getSku_id(),
							refund.getDetail_id());
					ReporterCSS.warn(msg);
					result = false;
				}

				if (refund.getRequest_amount_forsale().compareTo(request_amount_forsale) != 0) {
					msg = String.format("ST获取的订单%s详情中退货商品%s退货数与预期不符,预期:%s,实际:%s", order_id, detail.getSku_id(),
							request_amount_forsale, refund.getRequest_amount_forsale());
					ReporterCSS.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "司机APP录入的售后信息和录入后查询到的信息不一致");
		} catch (Exception e) {
			logger.error("司机APP代客户发起订单售后遇到错误: ", e);
			Assert.fail("司机APP代客户发起订单售后遇到错误: ", e);
		}
	}

	@Test
	public void driverAppTestCase09() {
		ReporterCSS.title("测试点: 司机APP代客户发起订单售后(异常)");
		try {
			String order_id = orderTool.oneStepCreateOrder(8);
			Assert.assertNotNull(order_id, "创建订单失败");

			boolean result = orderService.updateOrderState(order_id, 10);
			Assert.assertEquals(result, true, "修改订单 " + order_id + " 状态失败");

			result = driverService.exceptionOptions(order_id);
			Assert.assertEquals(result, true, "司机APP,获取订单售后选项失败");

			DeliveryOrderDetail deliveryOrderDetail = driverService.getDeliveryOrderDetail(order_id, null);
			Assert.assertNotNull(deliveryOrderDetail, "司机APP获取订单详情信息失败");

			// 选取第一个商品做异常处理
			DeliveryOrderDetail.Detail detail = deliveryOrderDetail.getDetails().get(0);

			DeliveryOrderExceptionParam param = new DeliveryOrderExceptionParam();

			List<DeliveryOrderExceptionParam.ExceptionParam> exceptionParamList = new ArrayList<>();
			DeliveryOrderExceptionParam.ExceptionParam exceptionParam = param.new ExceptionParam();
			exceptionParam.setSku_id(detail.getSku_id());
			BigDecimal final_amount_forsale = detail.getReal_quantity().subtract(new BigDecimal("1"));
			exceptionParam.setFinal_amount_forsale(final_amount_forsale);
			exceptionParam.setException_reason(1);
			exceptionParam.setLose_money(new BigDecimal("0"));

			BigDecimal money_delta = detail.getTotal_item_price()
					.divide(detail.getQuantity(), 4, BigDecimal.ROUND_HALF_UP)
					.divide(detail.getSale_ratio(), 2, BigDecimal.ROUND_HALF_UP);

			exceptionParam.setMoney_delta(money_delta);
			exceptionParam.setSolution(2);
			exceptionParam.setDescription("");
			if (detail.getDetail_id() != null) {
				exceptionParam.setOrder_detail_id(detail.getDetail_id());
			}

			exceptionParamList.add(exceptionParam);

			param.setOrder_id(order_id);
			param.setExceptions(exceptionParamList);
			param.setRefunds(new ArrayList<>());

			result = driverService.checkReturnDeliveryOrder(param);
			Assert.assertEquals(result, true, "司机APP添加订单售后的前置检查操作失败");

			// 这里其实算一个BUG,订单没有分配司机,司机就可以直接代替客户发起售后
			result = driverService.updateDeliveryOrderException(param);
			Assert.assertEquals(result, true, "司机APP代客户添加订单异常请求操作失败");

			Reporter.log("司机APP添加完售后,再次验证售后是否真正添加成功");
			DeliveryOrderException deliveryOrderException = driverService.getDeliveryOrderException(order_id);
			Assert.assertNotNull(deliveryOrderException, "司机APP获取订单售后信息失败");

			Assert.assertEquals(deliveryOrderException.getExceptions().size() == 1, true, "司机APP获取到的订单售后信息为空,与预期不符");

			DeliveryOrderException.SaleException saleException = deliveryOrderException.getExceptions().get(0);
			String msg = null;
			String sku_id = detail.getSku_id();
			if (!saleException.getSku_id().equals(sku_id)) {
				msg = String.format("司机APP显示的售后商品ID与预期不符,预期:%s,实际:%s", sku_id, saleException.getSku_id());
				ReporterCSS.warn(msg);
				result = false;
			}

			if (!saleException.getSku_name().equals(detail.getSku_name())) {
				msg = String.format("司机APP显示的售后商品%s名称与预期不符,预期:%s,实际:%s", sku_id, detail.getSku_name(),
						saleException.getSku_name());
				ReporterCSS.warn(msg);
				result = false;
			}

			if (saleException.getFinal_amount_forsale().compareTo(final_amount_forsale) != 0) {
				msg = String.format("司机APP显示的售后商品%s记账数与预期不符,预期:%s,实际:%s", sku_id, final_amount_forsale,
						saleException.getFinal_amount_forsale());
				ReporterCSS.warn(msg);
				result = false;
			}

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotNull(orderDetail, "ST获取订单详情失败");

			if (orderDetail.getAbnormals().size() != 1) {
				msg = String.format("ST获取的订单%s详情中没有商品异常信息,与预期不符", order_id);
				ReporterCSS.warn(msg);
				result = false;
			} else {
				OrderDetailBean.Abnormal abnormal = orderDetail.getAbnormals().get(0);
				sku_id = detail.getSku_id();
				if (!abnormal.getDetail_id().equals(sku_id)) {
					msg = String.format("ST获取的订单%s详情中退货商品ID与预期不符,预期:%s,实际:%s", order_id, sku_id,
							abnormal.getDetail_id());
					ReporterCSS.warn(msg);
					result = false;
				}

				if (abnormal.getFinal_amount_forsale().compareTo(final_amount_forsale) != 0) {
					msg = String.format("ST获取的订单%s详情中退货商品%s退货数与预期不符,预期:%s,实际:%s", order_id, sku_id,
							final_amount_forsale, abnormal.getFinal_amount_forsale());
					ReporterCSS.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "司机APP录入的售后信息和录入后查询到的信息不一致");
		} catch (Exception e) {
			logger.error("司机APP代客户发起订单售后遇到错误: ", e);
			Assert.fail("司机APP代客户发起订单售后遇到错误: ", e);
		}
	}

	@Test
	public void driverAppTestCase10() {
		ReporterCSS.title("测试点: 司机APP扫码验货");
		try {
			String order_id = orderTool.oneStepCreateOrder(8);
			Assert.assertNotNull(order_id, "创建订单失败");

			boolean result = orderService.updateOrderState(order_id, 10);
			Assert.assertEquals(result, true, "修改订单 " + order_id + " 状态失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotNull(orderDetail, "获取订单 " + order_id + " 详细信息失败");

			String receive_address_id = orderDetail.getCustomer().getAddress_id();

			String receive_begin_time = orderDetail.getCustomer().getReceive_begin_time();

			result = distributeService.editAssignDistributeTask(order_id, receive_address_id, receive_begin_time,
					driver_id, 1);
			Assert.assertEquals(result, true, "订单分配配送司机失败");

			List<DeliveryTaskBean> deliveryTasks = driverService.getDeliveryTask(false);

			DeliveryTaskBean deliveryTask = deliveryTasks.stream().filter(d -> d.getOrder_id().equals(order_id))
					.findAny().orElse(null);
			Assert.assertNotNull(deliveryTask, "司机APP没有收到系统分配的配送任务单 " + order_id);

			SetWeightParam setWeightParam = null;
			List<SetWeightParam.Weight> weights = null;
			SetWeightParam.Weight weight = null;
			String sku_id = null;
			BigDecimal set_weigh = null;
			List<OrderAndSkuBean> printParams = new ArrayList<OrderAndSkuBean>();
			for (Detail detail : orderDetail.getDetails()) {
				setWeightParam = new SetWeightParam();
				weights = new ArrayList<SetWeightParam.Weight>();

				sku_id = detail.getSku_id();
				OrderAndSkuBean orderAndSku = new OrderAndSkuBean(order_id, sku_id);
				printParams.add(orderAndSku);
				set_weigh = detail.getStd_unit_quantity().add(NumberUtil.getRandomNumber(0, 3, 1));
				weight = setWeightParam.new Weight(order_id, sku_id, new BigDecimal("0"), set_weigh, false,
						detail.getSort_way());
				weights.add(weight);
				setWeightParam.setWeights(weights);
				result = weightService.setWeight(setWeightParam);
				break;
			}
			Assert.assertEquals(result, true, "订单 " + order_id + " 称重失败");

			List<PrintInfoBean> printInfos = weightService.getWeightSkuPrintInfo(printParams);
			Assert.assertNotEquals(printInfos, null, "获取订单" + order_id + "商品称重打印信息失败");

			Assert.assertEquals(printInfos.size() > 0, true, "获取订单" + order_id + "商品称重打印信息为空");

			PrintInfoBean printInfo = printInfos.get(0);
			String package_id = printInfo.getPackage_id();

			DeliveryOrderDetail deliveryOrderDetail = driverService.getDeliveryOrderDetail(order_id, package_id);
			Assert.assertNotEquals(deliveryOrderDetail, null, "司机APP扫码验货失败");

		} catch (Exception e) {
			logger.error("司机APP扫码验货遇到错误: ", e);
			Assert.fail("司机APP扫码验货遇到错误: ", e);
		}
	}

	@Test
	public void driverAppTestCase11() {
		ReporterCSS.title("测试点: 司机APP配送发送坐标");
		try {
			boolean result = driverService.startDelivery();
			Assert.assertEquals(result, true, "司机APP点击开始配送按钮返回失败");

			Integer driver_status = driverService.getDriverStatus();
			Assert.assertNotEquals(driver_status, null, "获取司机状态失败");

			DriverTracePointParam driverTracePointParam = new DriverTracePointParam();
			driverTracePointParam.setDriver_id(driver_id);
			driverTracePointParam.setToken(token);
			driverTracePointParam.setDirection(93.57);
			driverTracePointParam.setLocate_type(1);
			driverTracePointParam.setSpeed(10);
			driverTracePointParam.setLatitude(22.535539);
			driverTracePointParam.setLongitude(113.943142);
			driverTracePointParam.setLocatetime(String.valueOf(System.currentTimeMillis()));

			result = driverService.uploadDriverTracePoint(driverTracePointParam);
			Assert.assertEquals(result, true, "司机APP上传定位信息失败");

		} catch (Exception e) {
			logger.error("司机APP配送发送坐标遇到错误: ", e);
			Assert.fail("司机APP配送发送坐标遇到错误: ", e);
		}
	}
}
