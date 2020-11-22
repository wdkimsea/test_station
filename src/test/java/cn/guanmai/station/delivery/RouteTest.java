package cn.guanmai.station.delivery;

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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.delivery.RouteBean;
import cn.guanmai.station.bean.delivery.RouteBindCustomerBean;
import cn.guanmai.station.bean.delivery.RouteTask;
import cn.guanmai.station.bean.delivery.param.RouteTaskFilterParam;
import cn.guanmai.station.bean.order.OrderBean;
import cn.guanmai.station.bean.order.OrderCycle;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.param.OrderFilterParam;
import cn.guanmai.station.impl.delivery.RouteServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.interfaces.delivery.RouteService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Apr 1, 2019 2:46:38 PM 
* @des 线路测试
* @version 1.0 
*/
public class RouteTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(RouteTest.class);
	private RouteService routeService;
	private OrderService orderService;
	private OrderTool orderTool;
	private String route_name = "人民路";
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private BigDecimal route_id;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		routeService = new RouteServiceImpl(headers);
		orderTool = new OrderTool(headers);
		orderService = new OrderServiceImpl(headers);
	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			List<RouteBean> routeList = routeService.getRouteList(route_name, 0, 10);
			Assert.assertNotEquals(routeList, null, "获取配送线路列表失败");

			if (routeList.size() == 0) {
				boolean result = routeService.createRoute(route_name);
				Assert.assertEquals(result, true, "创建配送线路失败");
				routeList = routeService.getRouteList(route_name, 0, 10);
			}
			RouteBean Route = routeList.stream().filter(a -> a.getName().contains(route_name)).findAny().orElse(null);
			Assert.assertNotEquals(Route, null, "没有找到名称为 " + route_name + " 的配送线路");

			route_id = Route.getId();

			List<RouteBindCustomerBean> routeBindCustomerList = routeService.getRouteBindCustomer(route_id);

			// 过滤出没有绑定线路的商户
			List<RouteBindCustomerBean> routeNoBindCustomerList = routeBindCustomerList.stream()
					.filter(a -> a.getRoute_id() == null).collect(Collectors.toList());

			// 把过滤出的商户绑定到 "人民路" 线路上
			if (routeNoBindCustomerList.size() > 0) {
				List<BigDecimal> address_ids = new ArrayList<>();
				for (RouteBindCustomerBean a : routeBindCustomerList) {
					if (a.getRoute_id() == null || a.getRoute_id().compareTo(route_id) == 0) {
						address_ids.add(a.getAddress_id());
					}
				}

				boolean result = routeService.updateRouteBindCustomer(route_id, address_ids);
				Assert.assertEquals(result, true, "更新配送线路绑定的商户失败");
			}
		} catch (Exception e) {
			logger.error("更新配送线路绑定的商户遇到错误: ", e);
			Assert.fail("更新配送线路绑定的商户遇到错误: ", e);
		}

	}

	@Test
	public void routeTestCase01() {
		Reporter.log("测试点: 获取配送线路列表");
		try {
			List<RouteBean> routeList = routeService.getRouteList("", 0, 10);
			Assert.assertNotEquals(routeList, null, "获取配送线路列表失败");
		} catch (Exception e) {
			logger.error("获取配送线路列表遇到错误: ", e);
			Assert.fail("获取配送线路列表遇到错误: ", e);
		}
	}

	@Test
	public void routeTestCase02() {
		Reporter.log("测试点: 创建配送线路");
		String name = StringUtil.getRandomString(6).toUpperCase();
		boolean result = false;
		try {
			result = routeService.createRoute(name);
			Assert.assertEquals(result, true, "创建配送线路失败");
		} catch (Exception e) {
			logger.error("创建配送线路遇到错误: ", e);
			Assert.fail("创建配送线路遇到错误: ", e);
		} finally {
			if (result) {
				try {
					Reporter.log("后置处理,删除刚刚创建的线路,恢复环境数据");
					List<RouteBean> routeList = routeService.getRouteList(name, 0, 10);
					Assert.assertNotEquals(routeList, null, "获取配送路线列表失败");
					RouteBean Route = routeList.stream().filter(a -> a.getName().equals(name)).findAny().orElse(null);
					Assert.assertNotEquals(Route, null, "刚刚创建的配送线路 " + name + "没有找到");

					result = routeService.deleteRoute(Route.getId());
					Assert.assertEquals(result, true, "删除配送路线 " + name + "失败");
					Reporter.log("后置处理完毕");
				} catch (Exception e) {
					logger.error("删除配送线路遇到错误: ", e);
					Assert.fail("删除配送线路遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void routeTestCase03() {
		Reporter.log("测试点: 获取配送路线绑定的商户");
		try {
			List<RouteBindCustomerBean> routeBindCustomerList = routeService.getRouteBindCustomer(route_id);
			Assert.assertNotEquals(routeBindCustomerList, null, "获取配送路线绑定的商户列表失败");
		} catch (Exception e) {
			logger.error("获取配送路线绑定的商户遇到错误: ", e);
			Assert.fail("获取配送路线绑定的商户遇到错误: ", e);
		}
	}

	@Test
	public void routeTestCase04() {
		Reporter.log("测试点: 给未分配配送路线的商户分配路线");
		try {
			List<RouteBindCustomerBean> routeBindCustomerList = routeService.getRouteBindCustomer(route_id);
			Assert.assertNotEquals(routeBindCustomerList, null, "获取配送路线绑定的商户列表失败");

			// 没有绑定线路的商户
			List<RouteBindCustomerBean> noBindCustomerList = routeBindCustomerList.stream()
					.filter(a -> a.getRoute_id() == null).collect(Collectors.toList());

			List<BigDecimal> noBind_address_ids = new ArrayList<>();
			for (RouteBindCustomerBean routeBindCustomer : noBindCustomerList) {
				noBind_address_ids.add(routeBindCustomer.getAddress_id());
			}

			// 绑定了此线路的商户
			List<RouteBindCustomerBean> bindCustomerList = routeBindCustomerList.stream()
					.filter(a -> a.getRoute_id() != null && a.getRoute_id().compareTo(route_id) == 0)
					.collect(Collectors.toList());
			List<BigDecimal> bind_address_ids = new ArrayList<>();
			for (RouteBindCustomerBean RouteBindCustomer : bindCustomerList) {
				bind_address_ids.add(RouteBindCustomer.getAddress_id());
			}

			bind_address_ids.addAll(noBind_address_ids);

			boolean result = routeService.updateRouteBindCustomer(route_id, bind_address_ids);
			Assert.assertEquals(result, true, "把未分配线路的商户绑定到 " + route_name + " 线路上失败");

		} catch (Exception e) {
			logger.error("给未分配配送路线的商户分配路线遇到错误: ", e);
			Assert.fail("给未分配配送路线的商户分配路线遇到错误: ", e);
		}
	}

	@Test
	public void routeTestCase05() {
		final String temp_route_name = StringUtil.getRandomString(6).toUpperCase();
		ReporterCSS.title("测试点: 为绑定了配送路线的商户重新绑定配送路线");
		try {
			List<RouteBindCustomerBean> routeBindCustomerList = routeService.getRouteBindCustomer(route_id);
			Assert.assertNotEquals(routeBindCustomerList, null, "获取配送路线绑定商户列表信息失败");

			RouteBindCustomerBean routeBindCustomer = routeBindCustomerList.stream()
					.filter(a -> a.getRoute_id() != null).findFirst().orElse(null);

			Assert.assertNotEquals(routeBindCustomer, null, "商户全部没有绑定配送路线,与预期结果不符,无法进行后续测试");

			boolean result = routeService.createRoute(temp_route_name);
			Assert.assertEquals(result, true, "新建配送线路失败");

			List<RouteBean> RouteList = routeService.getRouteList(temp_route_name, 0, 10);
			Assert.assertNotEquals(RouteList, null, "获取配送线路列表失败");

			RouteBean temp_route = RouteList.stream().filter(a -> a.getName().equals(temp_route_name)).findAny()
					.orElse(null);
			Assert.assertNotEquals(temp_route, null, "没有找到刚刚新建名称为 " + temp_route_name + "的配送路线");

			List<BigDecimal> address_ids = new ArrayList<>();
			address_ids.add(routeBindCustomer.getAddress_id());

			result = routeService.updateRouteBindCustomer(temp_route.getId(), address_ids);
			Assert.assertEquals(result, true, "更新配送路线绑定的商户列表失败");

		} catch (Exception e) {
			logger.error("为绑定了配送路线的商户重新绑定配送路线遇到错误: ", e);
			Assert.fail("为绑定了配送路线的商户重新绑定配送路线遇到错误: ", e);
		} finally {
			try {
				Reporter.log("后置处理,删除刚刚添加的配送线路");
				List<RouteBean> RouteList = routeService.getRouteList(temp_route_name, 0, 10);
				Assert.assertNotEquals(RouteList, null, "获取配送线路列表失败");

				RouteBean Route = RouteList.stream().filter(a -> a.getName().equals(temp_route_name)).findAny()
						.orElse(null);
				if (Route != null) {
					Thread.sleep(1000);
					boolean result = routeService.deleteRoute(Route.getId());
					Assert.assertEquals(result, true, "删除配送线路遇到错误");
				}
			} catch (Exception e) {
				logger.error("后置处理,删除刚刚添加的配送线路遇到错误: ", e);
				Assert.fail("后置处理,删除刚刚添加的配送线路遇到错误: ", e);
			}
		}
	}

	@Test
	public void routeTestCase06() {
		Reporter.log("测试点: 导出配送线路");
		try {
			boolean result = routeService.exportRoute("");
			Assert.assertEquals(result, true, "导出配送路线失败");
		} catch (Exception e) {
			logger.error("导出配送线遇到错误: ", e);
			Assert.fail("导出配送线路遇到错误: ", e);
		}
	}

	@Test
	public void routeTestCase07() {
		Reporter.log("测试点: 配送任务-线路任务列表-按下单时间过滤");
		try {
			String order_id = orderTool.oneStepCreateOrder(10);
			Assert.assertNotEquals(order_id, null, "创建订单失败");

			// 在订单列表搜索过滤刚刚下的订单,获得线路名称
			OrderFilterParam param = new OrderFilterParam();
			param.setStart_date(todayStr);
			param.setEnd_date(todayStr);
			param.setQuery_type(1);
			param.setSearch_text(order_id);
			param.setOffset(0);
			param.setLimit(20);

			List<OrderBean> orders = orderService.searchOrder(param);
			Assert.assertNotEquals(orders, null, "订单列表搜索过滤订单失败");

			OrderBean order = orders.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(order, null, "订单 " + order_id + "没有过滤搜索到");
			String route_name_in_order = order.getRoute_name();

			// 线路任务列表过滤
			RouteTaskFilterParam filterParam = new RouteTaskFilterParam();
			filterParam.setDate_type(1);
			filterParam.setStart_date(todayStr + " " + "00:00:00");
			filterParam.setEnd_date(todayStr + " " + "23:59:59");
			filterParam.setQ(route_name_in_order);
			List<RouteTask> routeTaskList = routeService.searchRouteTasks(filterParam);
			Assert.assertNotEquals(routeTaskList, null, "获取线路任务列表失败");

			RouteTask routeTask = routeTaskList.stream().filter(r -> r.getRoute_name().equals(route_name_in_order))
					.findAny().orElse(null);
			Assert.assertNotEquals(routeTask, null, "线路 " + route_name_in_order + " 没有过滤搜索到");

			boolean result = routeTask.getOrder_ids().contains(order_id);
			Assert.assertEquals(result, true, "配送线路 " + route_name_in_order + " 中没有订单 " + order_id);
		} catch (Exception e) {
			logger.error("配送任务-线路任务列表-按下单时间过滤遇到错误: ", e);
			Assert.fail("配送任务-线路任务列表-按下单时间过滤遇到错误: ", e);
		}
	}

	@Test
	public void routeTestCase08() {
		Reporter.log("测试点: 配送任务-线路任务列表-按收货时间过滤");
		try {
			String order_id = orderTool.oneStepCreateOrder(10);

			// 在订单列表搜索过滤刚刚下的订单,获得线路名称
			OrderFilterParam param = new OrderFilterParam();
			param.setStart_date(todayStr);
			param.setEnd_date(todayStr);
			param.setQuery_type(1);
			param.setSearch_text(order_id);
			param.setOffset(0);
			param.setLimit(20);

			List<OrderBean> orders = orderService.searchOrder(param);
			Assert.assertNotEquals(orders, null, "订单列表搜索过滤订单失败");

			OrderBean order = orders.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(order, null, "订单 " + order_id + "没有过滤搜索到");
			String route_name_in_order = order.getRoute_name();

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			String receive_begin_time = orderDetail.getCustomer().getReceive_begin_time();
			String receive_begin_date = receive_begin_time.split(" ")[0];

			String start_date = receive_begin_date + " " + "00:00:00";
			String end_date = receive_begin_date + " " + "23:59:59";

			// 线路任务列表过滤
			RouteTaskFilterParam filterParam = new RouteTaskFilterParam();
			filterParam.setDate_type(3);
			filterParam.setStart_date(start_date);
			filterParam.setEnd_date(end_date);
			filterParam.setQ(route_name_in_order);
			List<RouteTask> routeTaskList = routeService.searchRouteTasks(filterParam);
			Assert.assertNotEquals(routeTaskList, null, "获取线路任务列表失败");

			RouteTask routeTask = routeTaskList.stream().filter(r -> r.getRoute_name().equals(route_name_in_order))
					.findAny().orElse(null);
			Assert.assertNotEquals(routeTask, null, "线路 " + route_name_in_order + " 没有过滤搜索到");

			boolean result = routeTask.getOrder_ids().contains(order_id);
			Assert.assertEquals(result, true, "配送线路 " + route_name_in_order + " 中没有订单 " + order_id);
		} catch (Exception e) {
			logger.error("配送任务-线路任务列表-按收货时间过滤遇到错误: ", e);
			Assert.fail("配送任务-线路任务列表-按收货时间过滤遇到错误: ", e);
		}
	}

	@Test
	public void routeTestCase09() {
		Reporter.log("测试点: 配送任务-线路任务列表-按运营时间过滤");
		try {
			String order_id = orderTool.oneStepCreateOrder(10);

			// 在订单列表搜索过滤刚刚下的订单,获得线路名称
			OrderFilterParam param = new OrderFilterParam();
			param.setStart_date(todayStr);
			param.setEnd_date(todayStr);
			param.setQuery_type(1);
			param.setSearch_text(order_id);
			param.setOffset(0);
			param.setLimit(20);

			List<OrderBean> orders = orderService.searchOrder(param);
			Assert.assertNotEquals(orders, null, "订单列表搜索过滤订单失败");

			OrderBean order = orders.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(order, null, "订单 " + order_id + "没有过滤搜索到");
			String route_name_in_order = order.getRoute_name();

			OrderCycle cycle = orderTool.getOrderOperationCycle(order_id);

			RouteTaskFilterParam filterParam = new RouteTaskFilterParam();
			filterParam.setDate_type(2);
			filterParam.setStart_date(cycle.getCycle_start_time() + ":00");
			filterParam.setEnd_date(cycle.getCycle_end_time() + ":00");
			filterParam.setTime_config_id(cycle.getTime_config_id());
			filterParam.setQ(route_name_in_order);

			List<RouteTask> routeTaskList = routeService.searchRouteTasks(filterParam);
			Assert.assertNotEquals(routeTaskList, null, "获取线路任务列表失败");

			RouteTask routeTask = routeTaskList.stream().filter(r -> r.getRoute_name().equals(route_name_in_order))
					.findAny().orElse(null);
			Assert.assertNotEquals(routeTask, null, "线路 " + route_name_in_order + " 没有过滤搜索到");

			boolean result = routeTask.getOrder_ids().contains(order_id);
			Assert.assertEquals(result, true, "配送线路 " + route_name_in_order + " 中没有订单 " + order_id);
		} catch (Exception e) {
			logger.error("配送任务-线路任务列表-按运营时间过滤遇到错误: ", e);
			Assert.fail("配送任务-线路任务列表-按运营时间过滤遇到错误: ", e);
		}
	}
}
