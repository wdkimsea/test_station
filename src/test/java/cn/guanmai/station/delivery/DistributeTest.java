package cn.guanmai.station.delivery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.station.bean.category.Category1Bean;
import cn.guanmai.station.bean.category.SkuMeasurementBean;
import cn.guanmai.station.bean.delivery.DeliveryCategoryConfigBean;
import cn.guanmai.station.bean.delivery.DistributeOrderBean;
import cn.guanmai.station.bean.delivery.DistributeOrderDetailBean;
import cn.guanmai.station.bean.delivery.DriverBean;
import cn.guanmai.station.bean.delivery.DriverDistributeTaskBean;
import cn.guanmai.station.bean.delivery.DriverResultBean;
import cn.guanmai.station.bean.delivery.param.DistributeOrderFilterParam;
import cn.guanmai.station.bean.delivery.param.DriverDistributeTaskFilterParam;
import cn.guanmai.station.bean.invoicing.RefundStockResultBean;
import cn.guanmai.station.bean.invoicing.param.RefundStockFilterParam;
import cn.guanmai.station.bean.invoicing.param.RefundStockParam;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderDetailBean.Detail;
import cn.guanmai.station.bean.order.param.OrderExceptionParam;
import cn.guanmai.station.bean.order.param.OrderRefundParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.delivery.DistributeServiceImpl;
import cn.guanmai.station.impl.invoicing.RefundStockServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.delivery.DistributeService;
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
* @date Jan 4, 2019 10:42:26 AM 
* @des 配送相关业务测试用例
* @version 1.0 
*/
public class DistributeTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(DistributeTest.class);
	private DistributeService distributeService;
	private OrderService orderService;
	private OrderDetailBean orderDetail;
	private RefundStockService refundService;
	private CategoryService categoryService;
	private LoginUserInfoService loginUserInfoService;
	private String todayStr;
	private OrderTool orderTool;
	private String order_id;
	private String receive_begin_time;
	private String receive_end_time;
	private String time_config_id;
	private String address_id;
	private String station_store_id;

	@BeforeClass
	public void initData() {
		try {
			Map<String, String> headers = getStationCookie();
			orderTool = new OrderTool(headers);
			orderService = new OrderServiceImpl(headers);
			distributeService = new DistributeServiceImpl(headers);
			refundService = new RefundStockServiceImpl(headers);
			categoryService = new CategoryServiceImpl(headers);
			loginUserInfoService = new LoginUserInfoServiceImpl(headers);

			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账户信息失败");

			String station_id = loginUserInfo.getStation_id();

			DriverBean driver = distributeService.initDriverData(station_id);
			Assert.assertNotEquals(driver, null, "初始化司机管理数据失败");

			todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			station_store_id = station_id;
		} catch (Exception e) {
			logger.error("初始化配送数据遇到错误: ", e);
			Assert.fail("初始化配送数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			order_id = orderTool.oneStepCreateOrder(8);
			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 订单详细信息失败");

			receive_begin_time = orderDetail.getCustomer().getReceive_begin_time();
			receive_end_time = orderDetail.getCustomer().getReceive_end_time();
			time_config_id = orderDetail.getTime_config_id();
			address_id = orderDetail.getCustomer().getAddress_id();
		} catch (Exception e) {
			logger.error("创建订单遇到错误: ", e);
			Assert.fail("创建订单遇到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase01() {
		ReporterCSS.title("测试点: 订单智能智能规划");
		JSONArray order_ids = new JSONArray();
		order_ids.add(order_id);
		try {
			boolean result = distributeService.autoAssigndistributeTask(order_ids);
			Assert.assertEquals(result, true, "配送-智能规划订单失败");
		} catch (Exception e) {
			logger.error("配送-智能规划订单遇到错误: ", e);
			Assert.fail("配送-智能规划订单遇到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase02() {
		DistributeOrderFilterParam parmaBean = new DistributeOrderFilterParam(todayStr, todayStr, 0, 20, order_id);
		Reporter.log("配送-按下单日期方式搜索配送订单任务列表");

		try {
			List<DistributeOrderBean> distributeOrderArray = distributeService.getDistributeOrders(parmaBean);
			Assert.assertNotEquals(distributeOrderArray, null, "配送-按下单日期方式搜索配送订单任务列表失败");

			DistributeOrderBean distributeOrder = distributeOrderArray.stream()
					.filter(d -> d.getOrder_id().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(distributeOrder, null, "配送-按下单日期方式搜索配送单,没有查询到指定的配送单");
		} catch (Exception e) {
			logger.error("配送-按下单日期方式搜索配送订单任务列表遇到错误: ", e);
			Assert.fail("配送-按下单日期方式搜索配送订单任务列表遇到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase03() {
		String receive_start_time = receive_begin_time.split(" ")[0];
		String receive_end_time = receive_begin_time.split(" ")[0];
		DistributeOrderFilterParam parmaBean = new DistributeOrderFilterParam(0, 20, receive_start_time,
				receive_end_time, order_id);
		Reporter.log("配送-按收货日期方式搜索配送订单任务列表");

		try {
			List<DistributeOrderBean> distributeOrderArray = distributeService.getDistributeOrders(parmaBean);
			Assert.assertNotEquals(distributeOrderArray, null, "配送-按收货日期方式搜索配送订单任务列表失败");

			DistributeOrderBean distributeOrder = distributeOrderArray.stream()
					.filter(d -> d.getOrder_id().equals(order_id)).findAny().orElse(null);

			Assert.assertNotEquals(distributeOrder, null, "配送-按收货日期方式搜索配送单,没有查询到指定的配送单");
		} catch (Exception e) {
			logger.error("配送-按收货日期方式搜索配送订单任务列表遇到错误: ", e);
			Assert.fail("配送-按收货日期方式搜索配送订单任务列表遇到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase04() {
		DistributeOrderFilterParam parmaBean = new DistributeOrderFilterParam(receive_begin_time, receive_end_time,
				time_config_id, 0, 20, order_id);
		Reporter.log("配送-按运营时间方式搜索配送订单任务列表");

		try {
			List<DistributeOrderBean> distributeOrderArray = distributeService.getDistributeOrders(parmaBean);
			Assert.assertNotEquals(distributeOrderArray, null, "配送-按运营时间方式搜索配送订单任务列表失败");

			DistributeOrderBean distributeOrder = distributeOrderArray.stream()
					.filter(d -> d.getOrder_id().equals(order_id)).findAny().orElse(null);

			Assert.assertNotEquals(distributeOrder, null, "配送-按运营时间方式搜索配送单,没有查询到指定的配送单");
		} catch (Exception e) {
			logger.error("配送-按运营时间方式搜索配送订单任务列表遇到错误: ", e);
			Assert.fail("配送-按运营时间方式搜索配送订单任务列表遇到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase05() {

		Reporter.log("配送-司机任务列表查询,按下单周期查询");
		DriverDistributeTaskFilterParam paramBean = new DriverDistributeTaskFilterParam(0, todayStr, todayStr, 0, 20);
		try {
			List<DriverDistributeTaskBean> driverDistributeTaskArray = distributeService
					.getDriverDistributeTasks(paramBean);
			Assert.assertNotEquals(driverDistributeTaskArray, null, "配送-司机任务列表查询,按下单周期查询失败");

		} catch (Exception e) {
			logger.error("配送-司机任务列表查询,按下单周期查询遇到错误: ", e);
			Assert.fail("配送-司机任务列表查询,按下单周期查询到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase06() {
		Reporter.log("配送-司机任务列表查询,按运营周期方式查询");
		DriverDistributeTaskFilterParam paramBean = new DriverDistributeTaskFilterParam(0, receive_begin_time,
				receive_end_time, time_config_id, 0, 20);
		try {
			List<DriverDistributeTaskBean> driverDistributeTaskArray = distributeService
					.getDriverDistributeTasks(paramBean);
			Assert.assertNotEquals(driverDistributeTaskArray, null, "配送-司机任务列表查询,按运营周期方式查询失败");

		} catch (Exception e) {
			logger.error("配送-司机任务列表查询,按运营周期方式查询遇到错误: ", e);
			Assert.fail("配送-司机任务列表查询,按运营周期方式查询遇到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase07() {
		Reporter.log("配送-司机任务列表查询,按收货日期查询");
		String receive_start_time = receive_begin_time.split(" ")[0];
		String receive_end_time = receive_begin_time.split(" ")[0];

		DriverDistributeTaskFilterParam paramBean = new DriverDistributeTaskFilterParam(0, 0, 20, receive_start_time,
				receive_end_time);
		try {
			List<DriverDistributeTaskBean> driverDistributeTaskArray = distributeService
					.getDriverDistributeTasks(paramBean);
			Assert.assertNotEquals(driverDistributeTaskArray, null, "配送-司机任务列表查询,按收货日期查询失败");

		} catch (Exception e) {
			logger.error("配送-司机任务列表查询,按收货日期查询遇到错误: ", e);
			Assert.fail("配送-司机任务列表查询,按收货日期查询到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase08() {
		try {
			ReporterCSS.title("测试点: 订单分配司机");
			List<DriverResultBean> driverList = distributeService.getDriverList();
			Assert.assertNotEquals(driverList, null, "获取站点司机列表失败");

			DriverResultBean driver = driverList.stream().filter(d -> d.getState() == 1).findFirst().orElse(null);
			Assert.assertNotEquals(driver, null, "站点没有有效的司机,与预期不符");
			BigDecimal driver_id = driver.getId();

			boolean result = distributeService.editAssignDistributeTask(order_id, address_id, receive_begin_time,
					driver_id, 1);
			Assert.assertEquals(result, true, "订单分配任务司机失败");
		} catch (Exception e) {
			logger.error("订单任务分配司机失败遇到错误: ", e);
			Assert.fail("订单任务分配司机失败遇到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase09() {
		try {
			// 供应链-配送-配送任务-司机任务列表
			ReporterCSS.title("测试点: 获取司机配送订单列表");
			List<String> paramArray = new ArrayList<>();
			paramArray.add(order_id);
			List<DistributeOrderDetailBean> distributeOrderDetailList = distributeService
					.getDistributeOrderDetailArray(paramArray);
			Assert.assertEquals(
					distributeOrderDetailList != null && distributeOrderDetailList.size() == paramArray.size(), true,
					"获取配送订单列表失败");
		} catch (Exception e) {
			logger.error("获取配送订单列表遇到错误: ", e);
			Assert.fail("获取配送订单列表遇到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase10() {
		try {
			ReporterCSS.title("测试点: 获取司机配送单相信信息");
			List<DriverResultBean> driverList = distributeService.getDriverList();
			DriverResultBean driver = driverList.get(0);
			BigDecimal driver_id = driver.getId();

			boolean result = distributeService.editAssignDistributeTask(order_id, address_id, receive_begin_time,
					driver_id, 1);
			Assert.assertEquals(result, true, "订单分配任务司机失败");

			Reporter.log("获取司机配送单详细信息");
			JSONArray paramArray = new JSONArray();
			paramArray.add(order_id);
			result = distributeService.printDriverTasks(driver_id, paramArray);
			Assert.assertEquals(result, true, "获取司机配送单详细信息失败");
		} catch (Exception e) {
			logger.error("获取司机配送单详细信息遇到错误: ", e);
			Assert.fail("获取司机配送单详细信息遇到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase11() {
		try {
			ReporterCSS.title("测试点: 编辑配送单信息");
			List<String> paramArray = new ArrayList<>();
			paramArray.add(order_id);
			List<DistributeOrderDetailBean> distributeOrderDetailArray = distributeService
					.getDistributeOrderDetailArray(paramArray);
			Assert.assertEquals(
					distributeOrderDetailArray != null && distributeOrderDetailArray.size() == paramArray.size(), true,
					"获取配送订单详细信息失败");

			DistributeOrderDetailBean distributeOrderDetail = distributeOrderDetailArray.get(0);

			List<DistributeOrderDetailBean.Detail> details = distributeOrderDetail.getDetails();
			JSONArray skus = new JSONArray();
			JSONObject sku = null;
			for (DistributeOrderDetailBean.Detail detail : details) {
				sku = new JSONObject();
				sku.put("category_title_1", detail.getCategory_title_1());
				sku.put("category_title_2", detail.getCategory_title_2());
				sku.put("pinlei_title", detail.getPinlei_title());
				sku.put("id", detail.getSku_id());
				sku.put("name", detail.getSku_name());
				sku.put("std_unit_name", detail.getStd_unit_name());
				sku.put("sale_unit_name", detail.getSale_unit_name());
				sku.put("sale_ratio", detail.getSale_ratio());
				sku.put("quantity", detail.getQuantity());
				sku.put("real_weight", detail.getReal_weight());
				sku.put("std_sale_price", detail.getStd_sale_price());
				skus.add(sku);
			}

			boolean result = distributeService.submitDistributionOrder(order_id, new BigDecimal("0.00"), skus,
					new JSONArray());
			Assert.assertEquals(result, true, "编辑配送单信息失败");

		} catch (Exception e) {
			logger.error("编辑配送单信息过程中遇到错误: ", e);
			Assert.fail("编辑配送单信息过程中遇到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase12() {
		ReporterCSS.title("测试点: 配送单与订单比较,验证配送单基本信息");
		try {
			List<String> paramArray = new ArrayList<>();
			paramArray.add(order_id);
			List<DistributeOrderDetailBean> distributeOrderDetailArray = distributeService
					.getDistributeOrderDetailArray(paramArray);
			Assert.assertEquals(distributeOrderDetailArray != null, true, "获取配送订单详细信息失败");

			Assert.assertEquals(distributeOrderDetailArray.size(), 1, "获取到的配送单数量不对");

			List<OrderDetailBean.Detail> o_details = orderDetail.getDetails();

			DistributeOrderDetailBean distributeOrderDetail = distributeOrderDetailArray.get(0);
			List<DistributeOrderDetailBean.Detail> d_details = distributeOrderDetail.getDetails();

			// 比较订单与配送单信息
			boolean result = true;
			String msg = null;
			for (OrderDetailBean.Detail o_detail : o_details) {
				String sku_id = o_detail.getSku_id();
				DistributeOrderDetailBean.Detail d_detail = d_details.stream().filter(d -> d.getSku_id().equals(sku_id))
						.findAny().orElse(null);
				Assert.assertNotEquals(d_detail, null, "在配送单信息中没有找到下单商品 " + sku_id);

				if (!o_detail.getCategory_title_1().equals(d_detail.getCategory_title_1())) {
					msg = String.format("配送单中%s的商品%s一级分类名称与订单详细中记录的不一致,预期%s,实际%s", order_id, sku_id,
							o_detail.getCategory_title_1(), d_detail.getCategory_title_1());
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}

				if (!o_detail.getCategory_title_2().equals(d_detail.getCategory_title_2())) {
					msg = String.format("配送单中%s的商品%s二级分类名称与订单详细中记录的不一致,预期%s,实际%s", order_id, sku_id,
							o_detail.getCategory_title_2(), d_detail.getCategory_title_2());
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}
				if (!o_detail.getSku_name().equals(d_detail.getSku_name())) {
					msg = String.format("配送单中%s的商品%名类名称与订单详细中记录的不一致,预期%s,实际%s", order_id, sku_id,
							o_detail.getSku_name(), d_detail.getSku_name());
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}

				if (!o_detail.getSale_unit_name().equals(d_detail.getSale_unit_name())) {
					msg = String.format("配送单中%s的商品%销售单位与订单详细中记录的不一致,预期%s,实际%s", order_id, sku_id,
							o_detail.getSale_unit_name(), d_detail.getSale_unit_name());
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}

				if (!o_detail.getStd_unit_name().equals(d_detail.getStd_unit_name())) {
					msg = String.format("配送单中%s的商品%基本单位与订单详细中记录的不一致,预期%s,实际%s", order_id, sku_id,
							o_detail.getStd_unit_name(), d_detail.getStd_unit_name());
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}

				if (o_detail.getSale_ratio().compareTo(d_detail.getSale_ratio()) != 0) {
					msg = String.format("配送单中%s的商品%销售单位转化率与订单详细中记录的不一致,预期%s,实际%s", order_id, sku_id,
							o_detail.getSale_ratio(), d_detail.getSale_ratio());
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}

				if (o_detail.getQuantity().compareTo(d_detail.getQuantity()) != 0) {
					msg = String.format("配送单中%s的商品%下单数与订单详细中记录的不一致,预期%s,实际%s", order_id, sku_id, o_detail.getQuantity(),
							d_detail.getQuantity());
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}

				if (o_detail.getStd_sale_price_forsale().compareTo(d_detail.getStd_sale_price_forsale()) != 0) {
					msg = String.format("配送单中%s的商品%s基本单价与订单详细中记录的不一致,预期%s,实际%s", order_id, sku_id,
							o_detail.getStd_sale_price_forsale(), d_detail.getStd_sale_price_forsale());
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}
			}

			if (orderDetail.getTotal_price().compareTo(distributeOrderDetail.getTotal_price()) != 0) {
				msg = String.format("配送单记录的下单金额和订单记录的下单金额不一致,预期%s,实际%s", orderDetail.getTotal_price(),
						distributeOrderDetail.getTotal_price());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (orderDetail.getReal_price().compareTo(distributeOrderDetail.getReal_price()) != 0) {
				msg = String.format("配送单记录的下单金额和订单中记录的下单金额不一致,预期%s,实际%s", orderDetail.getReal_price(),
						distributeOrderDetail.getReal_price());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "配送单里记录的信息与订单详细里记录的信息不一致");
		} catch (Exception e) {
			logger.error("验证配送单信息的过程中遇到错误: ", e);
			Assert.fail("验证配送单信息的过程中遇到错误: ", e);
		}

	}

	@Test
	public void distributeTestCase13() {
		ReporterCSS.title("测试点: 拉取配送单模板列表");
		try {
			List<String> distributeConfigIds = distributeService.getDistributeConfigIds();
			Assert.assertNotNull(distributeConfigIds, "拉取配送单模板列表失败");
		} catch (Exception e) {
			logger.error("获取配送单信息过程中遇到错误: ", e);
			Assert.fail("获取配送单信息过程中遇到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase14() {
		ReporterCSS.title("测试点: 获取配送单模板选中状态值");
		try {
			boolean result = distributeService.getDistributeConfigSelected();
			Assert.assertEquals(result, true, "获取配送单模板选中状态值");
		} catch (Exception e) {
			logger.error("获取配送单信息过程中遇到错误: ", e);
			Assert.fail("获取配送单信息过程中遇到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase15() {
		ReporterCSS.title("测试点: 获取配送单模板详细信息");
		try {
			boolean result = distributeService.getDistributeConfigDetail();
			Assert.assertEquals(result, true, "获取配送单模板选中状态值");
		} catch (Exception e) {
			logger.error("获取配送单信息过程中遇到错误: ", e);
			Assert.fail("获取配送单信息过程中遇到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase16() {
		ReporterCSS.title("测试点: 新增配送单打印日志");
		try {
			List<String> order_ids = new ArrayList<>();
			order_ids.add(order_id);

			boolean result = distributeService.createPrintLog(order_ids);
			Assert.assertEquals(result, true, "新增配送单打印日志失败");
		} catch (Exception e) {
			logger.error("新增配送单打印日志遇到错误: ", e);
			Assert.fail("新增配送单打印日志遇到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase17() {
		ReporterCSS.title("测试点: 订单添加售后信息后,查看配送单详细信息");
		// 选取一个商品,添加商品异常
		Assert.assertEquals(orderDetail.getDetails().size() >= 2, true, "订单 " + order_id + " 中的商品数不足以同时添加退货和异常");

		// 商品退货
		Detail refundSku = orderDetail.getDetails().get(0);
		String std_unit_name_forsale = refundSku.getStd_unit_name_forsale();
		String std_unit_name = refundSku.getStd_unit_name();

		BigDecimal request_amount = NumberUtil.getRandomNumber(1, refundSku.getStd_unit_quantity().intValue(), 2);
		BigDecimal request_std_sale_price = refundSku.getStd_sale_price_forsale();
		String refund_sku_id = refundSku.getSku_id();

		// 商品异常
		Detail exceptionSku = orderDetail.getDetails().get(1);
		String exception_sku_id = exceptionSku.getSku_id();
		BigDecimal final_amount = NumberUtil.getRandomNumber(1, exceptionSku.getStd_unit_quantity().intValue(), 2);
		// 最终记账数减去出库数
		BigDecimal exception_amount = final_amount.subtract(exceptionSku.getStd_real_quantity());
		BigDecimal exception_money = exceptionSku.getStd_sale_price_forsale().multiply(exception_amount).setScale(2,
				BigDecimal.ROUND_HALF_UP);

		try {
			Map<String, List<SkuMeasurementBean>> skuMeasurementMap = categoryService.getSkuMeasurementMap();
			Assert.assertNotEquals(skuMeasurementMap, null, "获取单位对应关系列表失败");

			BigDecimal sale_ratio = new BigDecimal("1");
			BigDecimal real_amount = request_amount;
			if (skuMeasurementMap.containsKey(std_unit_name)) {
				List<SkuMeasurementBean> skuMeasurementList = skuMeasurementMap.get(std_unit_name);
				SkuMeasurementBean skuMeasurement = skuMeasurementList.stream()
						.filter(s -> s.getStd_unit_name_forsale().equals(std_unit_name_forsale)).findAny().orElse(null);
				sale_ratio = skuMeasurement.getStd_ratio();
				real_amount = request_amount.multiply(sale_ratio);
			}

			List<OrderRefundParam> orderRundArray = new ArrayList<OrderRefundParam>();
			orderRundArray.add(new OrderRefundParam(request_amount, refund_sku_id, station_store_id, 1, 1));

			List<OrderExceptionParam> orderExceptionArray = new ArrayList<OrderExceptionParam>();
			orderExceptionArray.add(new OrderExceptionParam(final_amount, exception_sku_id, 1, 1, 2));

			boolean result = orderService.addOrderException(order_id, orderExceptionArray, orderRundArray);
			Assert.assertEquals(result, true, "订单添加售后处理失败");

			// 对退货商品进行处理
			RefundStockFilterParam refundStockFilterParam = new RefundStockFilterParam();
			refundStockFilterParam.setDate_from(todayStr);
			refundStockFilterParam.setDate_end(todayStr);
			refundStockFilterParam.setOrder_id(order_id);
			refundStockFilterParam.setPage(0);
			refundStockFilterParam.setNum(20);

			List<RefundStockResultBean> refundResuls = refundService.searchRefundStock(refundStockFilterParam);
			Assert.assertNotEquals(refundResuls, null, "获取商户退货入库信息失败");

			RefundStockResultBean refundResult = refundResuls.stream().filter(r -> r.getSku_id().equals(refund_sku_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(refundResult, null, "商户退货入库列表信息中没有找到订单" + order_id + "的退货商品 " + refund_sku_id);

			// 放弃取货
			List<RefundStockParam> refundList = new ArrayList<>();
			RefundStockParam refundParam = new RefundStockParam();
			refundParam.setRefund_id(refundResult.getRefund_id());
			refundParam.setSku_name(refundResult.getSku_name());
			refundParam.setSku_id(refundResult.getSku_id());
			refundParam.setSolution(157);
			refundParam.setDriver_id(0);
			refundParam.setIn_stock_price(BigDecimal.ZERO); // 单位:分

			refundParam.setDisabled_in_stock_price(false);
			refundParam.setRequest_amount(BigDecimal.ZERO);
			refundParam.setReal_amount(real_amount);
			refundParam.setStore_amount(BigDecimal.ZERO);
			refundParam.setDescription("");
			refundParam.setSale_ratio(refundResult.getSale_ratio());
			refundParam.setPurchase_sku_id(refundResult.getPurchase_sku_id());
			refundList.add(refundParam);

			result = refundService.editRefundStock(refundList);
			Assert.assertEquals(result, true, "商户退货入库操作失败");

			List<String> order_ids = new ArrayList<>();
			order_ids.add(order_id);

			result = distributeService.createPrintLog(order_ids);
			Assert.assertEquals(result, true, "配送单创建打印日志失败");
			List<DistributeOrderDetailBean> distributeOrderDetailList = distributeService
					.getDistributeOrderDetailArray(order_ids);
			Assert.assertEquals(distributeOrderDetailList != null && distributeOrderDetailList.size() == 1, true,
					"获取配送单 " + order_id + " 详细信息失败");

			DistributeOrderDetailBean distributeOrderDetail = distributeOrderDetailList.get(0);

			List<DistributeOrderDetailBean.Abnormal> abnormals = distributeOrderDetail.getAbnormals();
			List<DistributeOrderDetailBean.Refund> refunds = distributeOrderDetail.getRefunds();
			Assert.assertEquals(abnormals.size(), 1, "配送单 " + order_id + " 显示的异常商品数量与预期不符");
			Assert.assertEquals(refunds.size(), 1, "配送单 " + order_id + " 显示的退货商品数量与预期不符");

			DistributeOrderDetailBean.Abnormal abnormal = abnormals.get(0);
			String msg = null;
			if (!abnormal.getDetail_id().equals(exception_sku_id)) {
				msg = String.format("配送单%s中记录的异常商品ID与预期不符,预期:%s,实际%s", order_id, exception_sku_id,
						abnormal.getDetail_id());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (abnormal.getAmount_delta().compareTo(exception_amount) != 0) {
				msg = String.format("配送单%s中记录的异常商品%s异常数与预期不符,预期:%s,实际%s", order_id, exception_sku_id, exception_amount,
						abnormal.getAmount_delta());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (abnormal.getMoney_delta().compareTo(exception_money) != 0) {
				msg = String.format("配送单%s中记录的异常商品%s异常金额与预期不符,预期:%s,实际%s", order_id, exception_sku_id,
						abnormal.getMoney_delta(), exception_money);
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			DistributeOrderDetailBean.Refund refund = refunds.get(0);
			if (!refund.getDetail_id().equals(refund_sku_id)) {
				msg = String.format("配送单%s中记录的退货商品ID与预期不符,预期:%s,实际%s", order_id, refund_sku_id, refund.getDetail_id());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (refund.getAmount_delta().multiply(new BigDecimal("-1")).compareTo(request_amount) != 0) {
				msg = String.format("配送单%s中记录的退货商品%s退货数与预期不符,预期:%s,实际%s", order_id, refund_sku_id, request_amount,
						refund.getAmount_delta().multiply(new BigDecimal("-1")));
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			BigDecimal expected_refund_money = request_amount.multiply(request_std_sale_price).setScale(2,
					BigDecimal.ROUND_HALF_UP);

			if (refund.getMoney_delta().multiply(new BigDecimal("-1")).compareTo(expected_refund_money) != 0) {
				msg = String.format("配送单%s中记录的退货商品%s退货金额与预期不符,预期:%s,实际%s", order_id, refund_sku_id,
						expected_refund_money, refund.getMoney_delta().multiply(new BigDecimal("-1")));
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + "详细信息失败");
			BigDecimal actual_real_price = distributeOrderDetail.getTotal_pay();
			BigDecimal expected_real_price = orderDetail.getTotal_pay();

			if (actual_real_price.compareTo(expected_real_price) != 0) {
				msg = String.format("配送单%s中记录的销售额与预期不符,预期:%s,实际%s", order_id, expected_real_price, actual_real_price);
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "订单" + order_id + "添加完商品售后信息后,配送单打印的信息与预期不符");
		} catch (Exception e) {
			logger.error("订单添加售后信息后,查看配送单详细信息过程中遇到错误: ", e);
			Assert.fail("订单添加售后信息后,查看配送单详细信息过程中遇到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase18() {
		ReporterCSS.title("测试点: 批量打印配送单");

		DistributeOrderFilterParam parmaBean = new DistributeOrderFilterParam(todayStr, todayStr, 0, 20, "");
		try {
			List<DistributeOrderBean> distributeOrderArray = distributeService.getDistributeOrders(parmaBean);
			Assert.assertNotEquals(distributeOrderArray, null, "配送-按下单日期方式搜索配送订单任务列表失败");

			List<String> order_ids = new ArrayList<>();
			for (DistributeOrderBean distributeOrder : distributeOrderArray) {
				order_ids.add(distributeOrder.getOrder_id());
				if (order_ids.size() >= 3)
					break;
			}
			if (order_ids.size() > 0) {
				boolean result = distributeService.createPrintLog(order_ids);
				Assert.assertEquals(result, true, "创建打印配送单日志失败");
				List<DistributeOrderDetailBean> distributeOrderDetailList = distributeService
						.getDistributeOrderDetailArray(order_ids);
				Assert.assertNotEquals(distributeOrderDetailList, null, "批量打印配送单失败");

				Assert.assertEquals(distributeOrderDetailList.size(), order_ids.size(), "批量打印配送单,打印的配送单数量与预期的不一致");
			}
		} catch (Exception e) {
			logger.error("批量打印配送单遇到错误: ", e);
			Assert.fail("批量打印配送单遇到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase19() {
		ReporterCSS.title("测试点: 获取配送模板详细信息");
		try {
			List<String> ids = distributeService.getDistributeConfigIds();
			Assert.assertNotNull(ids, "获取配送单模板列表失败");

			boolean resutl = distributeService.getDistributeConfigDetail(ids.get(0));
			Assert.assertEquals(resutl, true, "获取配送单模板详细信息失败");
		} catch (Exception e) {
			logger.error("获取配送模板详细信息遇到错误: ", e);
			Assert.fail("获取配送模板详细信息遇到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase20() {
		ReporterCSS.title("测试点: 获取配送打印自定义分类信息");
		try {
			DeliveryCategoryConfigBean deliveryCategoryConfig = distributeService.getDeliveryCategoryConfig();
			Assert.assertNotNull(deliveryCategoryConfig, "获取配送打印自定义分类信息失败");
		} catch (Exception e) {
			logger.error("获取配送打印自定义分类信息遇到错误: ", e);
			Assert.fail("获取配送打印自定义分类信息遇到错误: ", e);
		}
	}

	@Test
	public void distributeTestCase21() {
		ReporterCSS.title("测试点: 修改配送打印自定义分类信息");
		try {
			DeliveryCategoryConfigBean deliveryCategoryConfig = distributeService.getDeliveryCategoryConfig();
			Assert.assertNotNull(deliveryCategoryConfig, "获取配送打印自定义分类信息失败");

			String id = deliveryCategoryConfig.getId();

			List<Category1Bean> category1List = categoryService.getCategory1List();
			Assert.assertNotEquals(category1List, null, "获取一级分类列表失败");

			List<List<String>> category_config = new ArrayList<List<String>>();
			List<String> ids = null;
			for (int i = 0; i < category1List.size(); i += 2) {
				ids = new ArrayList<String>();
				ids.add(category1List.get(i).getId());
				if (i + 1 < category1List.size()) {
					ids.add(category1List.get(i + 1).getId());
				}
				category_config.add(ids);
			}

			boolean result = distributeService.updateDeliveryCategoryConfig(id, category_config);
			Assert.assertEquals(result, true, "修改配送打印自定义分类信息失败");

			deliveryCategoryConfig = distributeService.getDeliveryCategoryConfig();
			Assert.assertNotNull(deliveryCategoryConfig, "获取配送打印自定义分类信息失败");

			List<List<String>> category_config_result = deliveryCategoryConfig.getCategory_config();
			ReporterCSS.step("预期的配送打印分类信息: " + category_config);
			logger.info("预期的配送打印分类信息: " + category_config);
			ReporterCSS.step("实际的配送打印分类信息: " + category_config_result);
			logger.info("实际的配送打印分类信息: " + category_config_result);

			Assert.assertEquals(category_config_result, category_config);
		} catch (Exception e) {
			logger.error("获取配送打印自定义分类信息遇到错误: ", e);
			Assert.fail("获取配送打印自定义分类信息遇到错误: ", e);
		}
	}

}
