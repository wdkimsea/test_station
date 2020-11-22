package cn.guanmai.open.delivery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.open.Product.CategoryTest;
import cn.guanmai.open.bean.delivery.OpenDeliveryTaskBean;
import cn.guanmai.open.bean.delivery.param.DeliveryTaskFilterParam;
import cn.guanmai.open.impl.delivery.OpenDeliveryServiceImpl;
import cn.guanmai.open.interfaces.delivery.OpenDeliveryService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.station.bean.delivery.DriverBean;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.delivery.DistributeServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.delivery.DistributeService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Jun 11, 2019 9:53:00 AM 
* @des 开放平台 配送任务测试
* @version 1.0 
*/
public class DeliveryTaskTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(CategoryTest.class);
	private OpenDeliveryService deliveryService;
	private DeliveryTaskFilterParam filterParam;
	private DistributeService distributeService;
	private OrderService orderService;
	private OrderTool orderTool;
	private String order_id;
	private String driver_id;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		deliveryService = new OpenDeliveryServiceImpl(access_token);

		Map<String, String> st_headers = getSt_headers();

		distributeService = new DistributeServiceImpl(st_headers);
		orderTool = new OrderTool(st_headers);
		orderService = new OrderServiceImpl(st_headers);

		try {
			LoginUserInfoService loginUserInfoService = new LoginUserInfoServiceImpl(st_headers);
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取ST的登录信息失败");

			String station_id = loginUserInfo.getStation_id();
			Assert.assertNotEquals(station_id, null, "获取站点ID失败");
			DriverBean driver = distributeService.initDriverData(station_id);
			Assert.assertNotEquals(driver, null, "初始化配送线路失败");

			driver_id = String.valueOf(driver.getId());
		} catch (Exception e) {
			logger.error("初始化配送数据遇到错误: ", e);
			Assert.fail("初始化配送数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		filterParam = new DeliveryTaskFilterParam();
		filterParam.setQuery_type("1");
		filterParam.setStart_date(todayStr);
		filterParam.setEnd_date(todayStr);
		filterParam.setOffset(0);
		filterParam.setLimit(50);
		try {
			order_id = orderTool.oneStepCreateOrder(6);
			Assert.assertNotEquals(order_id, null, "ST创建订单失败");
		} catch (Exception e) {
			logger.error("新建订单遇到错误: ", e);
			Assert.fail("新建订单遇到错误: ", e);
		}

	}

	@Test(timeOut = 20000)
	public void deliveryTaskTestCase01() {
		try {
			Reporter.log("测试点: 订单分配司机");
			List<String> order_ids = Arrays.asList(order_id);
			boolean result = deliveryService.assignDriver(order_ids, driver_id);
			Assert.assertEquals(result, true, "订单分配司机失败");

			int limit = 50;
			int offset = 0;
			List<OpenDeliveryTaskBean> deliveryTasks = new ArrayList<OpenDeliveryTaskBean>();
			List<OpenDeliveryTaskBean> tempDeliveryTasks = null;
			while (true) {
				filterParam.setOffset(offset);
				tempDeliveryTasks = deliveryService.searchDeliveryTasks(filterParam);
				Assert.assertNotEquals(tempDeliveryTasks, null, "过滤搜索配送任务失败");
				deliveryTasks.addAll(tempDeliveryTasks);
				if (tempDeliveryTasks.size() < limit) {
					break;
				}
				offset += limit;
			}

			OpenDeliveryTaskBean openDeliveryTask = deliveryTasks.stream().filter(s -> s.getOrder_id().equals(order_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(openDeliveryTask, null, "按下单日期搜索,订单 " + order_id + " 对应的配送任务没有搜索到");

			result = deliveryService.cancelDriver(order_ids);
			Assert.assertEquals(result, true, "订单取消分配司机失败");
		} catch (Exception e) {
			logger.error("过滤搜索配送任务遇到错误: ", e);
			Assert.fail("过滤搜索配送任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void deliveryTaskTestCase02() {
		try {
			ReporterCSS.title("测试点: 按收货时间搜索配送单");
			filterParam.setQuery_type("2");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + " 详细信息失败");

			String receive_begin_time = orderDetail.getCustomer().getReceive_begin_time().substring(0, 10);
			String receive_end_time = orderDetail.getCustomer().getReceive_end_time().substring(0, 10);

			filterParam.setStart_date(receive_begin_time);
			filterParam.setEnd_date(receive_end_time);
			int limit = 50;
			int offset = 0;
			List<OpenDeliveryTaskBean> deliveryTasks = new ArrayList<OpenDeliveryTaskBean>();
			List<OpenDeliveryTaskBean> tempDeliveryTasks = null;
			while (true) {
				filterParam.setOffset(offset);
				tempDeliveryTasks = deliveryService.searchDeliveryTasks(filterParam);
				Assert.assertNotEquals(tempDeliveryTasks, null, "过滤搜索配送任务失败");
				deliveryTasks.addAll(tempDeliveryTasks);
				if (tempDeliveryTasks.size() < limit) {
					break;
				}
				offset += limit;
			}

			OpenDeliveryTaskBean openDeliveryTask = deliveryTasks.stream().filter(s -> s.getOrder_id().equals(order_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(openDeliveryTask, null, "按收货日期查询,订单 " + order_id + " 对应的配送任务没有搜索到");
		} catch (Exception e) {
			logger.error("过滤搜索配送任务遇到错误: ", e);
			Assert.fail("过滤搜索配送任务遇到错误: ", e);
		}
	}
}
