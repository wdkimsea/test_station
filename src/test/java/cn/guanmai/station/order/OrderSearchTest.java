package cn.guanmai.station.order;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.delivery.DriverBean;
import cn.guanmai.station.bean.delivery.RouteBean;
import cn.guanmai.station.bean.order.OrderAnalysisBean;
import cn.guanmai.station.bean.order.OrderBean;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderDetailBean.Detail;
import cn.guanmai.station.bean.order.param.OrderCreateParam;
import cn.guanmai.station.bean.order.param.OrderEditParam;
import cn.guanmai.station.bean.order.param.OrderFilterParam;
import cn.guanmai.station.bean.order.param.OrderEditParam.OrderData;
import cn.guanmai.station.bean.system.AreaBean;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.bean.system.ServiceTimeBean;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.delivery.DistributeServiceImpl;
import cn.guanmai.station.impl.delivery.RouteServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.AreaServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.impl.system.ServiceTimeServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.delivery.DistributeService;
import cn.guanmai.station.interfaces.delivery.RouteService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.AreaService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.interfaces.system.ServiceTimeService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.JsonUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Jan 7, 2019 10:31:35 AM 
* @des 订单搜索测试
* @version 1.0 
*/
public class OrderSearchTest extends LoginStation {
	private static Logger logger = LoggerFactory.getLogger(OrderSearchTest.class);
	private OrderService orderService;
	private ServiceTimeService serviceTimeService;
	private AsyncService asyncService;
	private AreaService areaService;
	private RouteService routeService;
	private DistributeService distributeService;
	private LoginUserInfoService loginUserInfoService;
	private OrderTool orderTool;

	private OrderDetailBean orderDetail;
	private String today;

	private OrderFilterParam orderFilterParam;
	private String start_date_new;
	private String end_date_new;
	private String receive_start_date;
	private String receive_end_date;
	private String order_id;

	private final int limit = 50;

	private String station_id;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		orderService = new OrderServiceImpl(headers);
		orderTool = new OrderTool(headers);
		serviceTimeService = new ServiceTimeServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
		areaService = new AreaServiceImpl(headers);
		routeService = new RouteServiceImpl(headers);
		distributeService = new DistributeServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		try {
			today = TimeUtil.getCurrentTime("yyyy-MM-dd");
			start_date_new = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00");
			end_date_new = TimeUtil.calculateTime("yyyy-MM-dd 00:00", start_date_new, 1, Calendar.DATE);

			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账户信息失败");

			station_id = loginUserInfo.getStation_id();

			Assert.assertNotNull(station_id, "获取站点ID失败");
			order_id = orderTool.oneStepCreateOrder(8);
			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			receive_start_date = orderDetail.getCustomer().getReceive_begin_time();
			receive_end_date = orderDetail.getCustomer().getReceive_end_time();
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		orderFilterParam = new OrderFilterParam();
		orderFilterParam.setSearch_type(1);
		orderFilterParam.setQuery_type(1);
		orderFilterParam.setStart_date_new(start_date_new);
		orderFilterParam.setEnd_date_new(end_date_new);
		orderFilterParam.setLimit(limit);
		orderFilterParam.setOffset(0);
	}

	@Test
	public void orderSearchTestCase01() {
		ReporterCSS.title("测试点: 订单列表,导出信息");
		try {
			OrderAnalysisBean orderDetailAnalysis = orderService.orderDetailSalesAnalysis(orderFilterParam);
			Assert.assertNotEquals(orderDetailAnalysis, null, "调用订单列表详细接口失败");

			// 1 为后端异步导出
			if (orderDetailAnalysis.getAsync() == 1) {
				String task_url = orderDetailAnalysis.getTask_url();
				BigDecimal task_id = new BigDecimal(task_url.split("=")[1]);

				boolean result = asyncService.getAsyncTaskResult(task_id, "导出成功");
				Assert.assertEquals(result, true, "订单导出异步任务执行失败");
			}
		} catch (Exception e) {
			logger.error("调用订单列表详细接口遇到错误: ", e);
			Assert.fail("调用订单列表详细接口遇到错误: ", e);
		}
	}

	@Test
	public void orderSearchTestCase02() {
		ReporterCSS.title("测试点: 订单列表,按下单日期搜索订单");
		try {
			List<OrderBean> orderArray = orderService.searchOrder(orderFilterParam);

			Assert.assertEquals(orderArray != null, true, "订单列表过滤搜索订单失败");

			OrderBean orderDetail = orderArray.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(orderDetail, null, "按下单日期过滤,没有搜索到订单 " + order_id);

		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test
	public void orderSearchTestCase03() {
		ReporterCSS.title("测试点: 按收货日期过滤订单");
		try {
			orderFilterParam = new OrderFilterParam();
			orderFilterParam.setSearch_type(1);
			orderFilterParam.setQuery_type(3);
			orderFilterParam.setReceive_start_date_new(receive_start_date);
			orderFilterParam.setReceive_end_date_new(receive_end_date);
			orderFilterParam.setLimit(limit);
			orderFilterParam.setOffset(0);

			List<OrderBean> orderArray = orderService.searchOrder(orderFilterParam);

			Assert.assertEquals(orderArray != null, true, "订单列表过滤搜索订单失败");

			OrderBean order = orderArray.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);

			Assert.assertNotEquals(order, null, "按收货时间过滤,没有搜索到订单 " + order_id);
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test
	public void orderSearchTestCase04() {
		ReporterCSS.title("测试点: 按运营时间过滤搜索订单");
		try {
			// 获取订单的收货时间 年-月-日
			String receive_begin_time = orderDetail.getCustomer().getReceive_begin_time();
			receive_begin_time = receive_begin_time.split(" ")[0];

			String time_config_id = orderDetail.getTime_config_id();
			ServiceTimeBean serviceTime = serviceTimeService.getServiceTimeById(time_config_id);
			Assert.assertNotEquals(serviceTime, null, "获取运营时间详细信息失败");

			String cycle_start_time = null;
			String cycle_end_time = null;
			String start = null;
			String end = null;
			// 普通运营时间按下单周期搜索,预售运营时间按收货周期搜索
			if (serviceTime.getType() == 1) {
				start = serviceTime.getOrder_time_limit().getStart();
				end = serviceTime.getOrder_time_limit().getEnd();
				int span = serviceTime.getOrder_time_limit().getE_span_time();

				cycle_start_time = today + " " + start;
				cycle_end_time = TimeUtil.calculateTime("yyyy-MM-dd", today, span, Calendar.DAY_OF_YEAR) + " " + end;
			} else {
				// 收货开始时间 时:分
				start = serviceTime.getReceive_time_limit().getStart();

				// 收货结束时间 时:分
				end = serviceTime.getReceive_time_limit().getEnd();

				int span = serviceTime.getReceive_time_limit().getReceiveEndSpan();

				String receive_end_time = TimeUtil.calculateTime("yyyy-MM-dd", receive_begin_time, span,
						Calendar.DAY_OF_YEAR);

				cycle_start_time = receive_begin_time + " " + start;
				cycle_end_time = receive_end_time + " " + end;
			}

			int limit = 50;
			int offset = 0;

			orderFilterParam = new OrderFilterParam();
			orderFilterParam.setSearch_type(1);
			orderFilterParam.setQuery_type(2);
			orderFilterParam.setTime_config_id(time_config_id);
			orderFilterParam.setCycle_start_time(cycle_start_time);
			orderFilterParam.setCycle_end_time(cycle_end_time);

			orderFilterParam.setLimit(limit);
			List<OrderBean> orderArray = new ArrayList<>();
			List<OrderBean> tempOrderArray = null;
			while (true) {
				orderFilterParam.setOffset(offset);
				tempOrderArray = orderService.searchOrder(orderFilterParam);
				Assert.assertEquals(tempOrderArray != null, true, "订单列表过滤搜索订单失败");
				orderArray.addAll(tempOrderArray);
				if (tempOrderArray.size() < limit) {
					break;
				} else {
					offset += limit;
				}
			}

			OrderBean order = orderArray.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(order, null, "按运营时间过滤,没有搜索到订单 " + order_id);
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test
	public void orderSearchTestCase05() {
		ReporterCSS.title("测试点: 订单搜索-按下单日期-以商户名称进行搜索");
		try {
			String customer_name = orderDetail.getCustomer().getExtender().getResname();

			orderFilterParam.setSearch_text(customer_name);

			List<OrderBean> orderArray = orderService.searchOrder(orderFilterParam);
			Assert.assertEquals(orderArray != null, true, "订单列表过滤搜索订单失败");

			OrderBean order = orderArray.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(order, null, "订单搜索-按下单日期-以商户名称进行搜索,没有搜索到订单 " + order_id);

		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test
	public void orderSearchTestCase06() {
		ReporterCSS.title("测试点: 订单搜索-按下单日期-以商户ID进行搜索");
		try {
			String address_id = orderDetail.getCustomer().getAddress_id();
			NumberFormat nf = new DecimalFormat("000000");
			address_id = "S" + nf.format(Double.valueOf(address_id));

			orderFilterParam.setSearch_text(address_id);

			List<OrderBean> orderArray = orderService.searchOrder(orderFilterParam);
			Assert.assertEquals(orderArray != null, true, "订单列表过滤搜索订单失败");

			OrderBean order = orderArray.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(order, null, "订单搜索-按下单日期-以商户ID进行搜索,没有搜索到订单 " + order_id);

		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test
	public void orderSearchTestCase07() {
		ReporterCSS.title("测试点: 订单搜索-按下单日期-以订单状态进行过滤(等待分拣)");
		try {
			orderFilterParam.setStatus(1);

			List<OrderBean> orderArray = orderService.searchOrder(orderFilterParam);
			Assert.assertEquals(orderArray != null, true, "订单列表过滤搜索订单失败");

			OrderBean order = orderArray.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(order, null, "订单搜索-按下单日期-以订单状态进行过滤,没有搜索到订单 " + order_id);
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test
	public void orderSearchTestCase08() {
		ReporterCSS.title("测试点: 订单搜索-按下单日期-以订单状态进行过滤(分拣中)");
		try {
			boolean result = orderService.updateOrderState(order_id, 5);
			Assert.assertEquals(result, true, "把订单 " + order_id + " 状态改为分拣中失败");

			orderFilterParam.setStatus(5);

			List<OrderBean> orderArray = orderService.searchOrder(orderFilterParam);
			Assert.assertEquals(orderArray != null, true, "订单列表过滤搜索订单失败");

			OrderBean order = orderArray.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(order, null, "订单搜索-按下单日期-以订单状态进行过滤,没有搜索到订单 " + order_id);

		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "orderSearchTestCase08" })
	public void orderSearchTestCase09() {
		ReporterCSS.title("测试点: 订单搜索-按下单日期-以订单状态进行过滤(配送中)");
		try {
			boolean result = orderService.updateOrderState(order_id, 10);
			Assert.assertEquals(result, true, "把订单 " + order_id + " 状态改为配送中失败");

			orderFilterParam.setStatus(10);

			List<OrderBean> orderArray = orderService.searchOrder(orderFilterParam);
			Assert.assertEquals(orderArray != null, true, "订单列表过滤搜索订单失败");

			OrderBean order = orderArray.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(order, null, "订单搜索-按下单日期-以订单状态进行过滤,没有搜索到订单 " + order_id);

		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void orderSearchTestCase10() {
		ReporterCSS.title("测试点: 订单搜索-按下单日期-以订单状态进行过滤(已签收)");
		try {
			int offset = 0;

			orderFilterParam.setStatus(15);
			orderFilterParam.setOffset(offset);
			orderFilterParam.setLimit(limit);
			List<OrderBean> orderArray = new ArrayList<>();
			while (true) {
				List<OrderBean> tempArray = orderService.searchOrder(orderFilterParam);
				Assert.assertEquals(tempArray != null, true, "订单列表过滤搜索订单失败");
				orderArray.addAll(tempArray);
				if (tempArray.size() >= limit) {
					offset += limit;
					orderFilterParam.setOffset(offset);
				} else {
					break;
				}
			}

			List<OrderBean> searchOrderArray = orderArray.stream().filter(o -> o.getStatus() != 15)
					.collect(Collectors.toList());
			Assert.assertEquals(searchOrderArray.size(), 0, "过滤搜索状态为已签收的订单,搜索的结果含有状态为非已签收的订单");

		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void orderSearchTestCase11() {
		ReporterCSS.title("测试点: 订单搜索-按下单日期-以订单支付状态进行过滤(未支付)");
		try {
			int offset = 0;
			orderFilterParam.setPay_status(1);
			orderFilterParam.setOffset(offset);
			orderFilterParam.setLimit(limit);
			List<OrderBean> orderArray = new ArrayList<>();
			while (true) {
				List<OrderBean> tempArray = orderService.searchOrder(orderFilterParam);
				Assert.assertEquals(tempArray != null, true, "订单列表过滤搜索订单失败");
				orderArray.addAll(tempArray);
				if (tempArray.size() >= limit) {
					offset += limit;
					orderFilterParam.setOffset(offset);
				} else {
					break;
				}
			}

			List<OrderBean> searchOrderArray = orderArray.stream().filter(o -> o.getPay_status() != 1)
					.collect(Collectors.toList());
			Assert.assertEquals(searchOrderArray.size(), 0, "过滤搜索支付状态为(未支付)的订单,搜索的结果含有支付状态为非(未支付)的订单");

		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void orderSearchTestCase12() {
		ReporterCSS.title("测试点: 订单搜索-按下单日期-以订单状态进行过滤(部分支付)");
		try {
			int offset = 0;
			orderFilterParam.setPay_status(5);
			orderFilterParam.setOffset(offset);
			orderFilterParam.setLimit(limit);
			List<OrderBean> orderArray = new ArrayList<>();
			while (true) {
				List<OrderBean> tempArray = orderService.searchOrder(orderFilterParam);
				Assert.assertEquals(tempArray != null, true, "订单列表过滤搜索订单失败");
				orderArray.addAll(tempArray);
				if (tempArray.size() >= limit) {
					offset += limit;
					orderFilterParam.setOffset(offset);
				} else {
					break;
				}
			}

			List<OrderBean> searchOrderArray = orderArray.stream().filter(o -> o.getPay_status() != 5)
					.collect(Collectors.toList());
			Assert.assertEquals(searchOrderArray.size(), 0, "过滤搜索支付状态为(部分支付)的订单,搜索的结果含有支付状态为非(部分支付)的订单");

		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void orderSearchTestCase13() {
		ReporterCSS.title("测试点: 订单搜索-按下单日期-以订单状态进行过滤(已支付)");
		try {
			int offset = 0;
			orderFilterParam.setPay_status(10);
			orderFilterParam.setOffset(offset);
			orderFilterParam.setLimit(limit);
			List<OrderBean> orderArray = new ArrayList<>();
			while (true) {
				List<OrderBean> tempArray = orderService.searchOrder(orderFilterParam);
				Assert.assertEquals(tempArray != null, true, "订单列表过滤搜索订单失败");
				orderArray.addAll(tempArray);
				if (tempArray.size() >= limit) {
					offset += limit;
					orderFilterParam.setOffset(offset);
				} else {
					break;
				}
			}

			List<String> other_order_ids = orderArray.stream().filter(o -> o.getPay_status() != 10).map(o -> o.getId())
					.collect(Collectors.toList());
			Assert.assertEquals(other_order_ids.size(), 0, "过滤搜索支付状态为(已支付)的订单,搜索的结果含有支付状态为非(已支付)的订单" + other_order_ids);

		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test
	public void orderSearchTestCase14() {
		ReporterCSS.title("测试点: 订单搜索-按下单日期-以地理标签过滤订单");
		try {
			List<AreaBean> areas = areaService.getAreaDict();
			Assert.assertNotEquals(areas, null, "获取订单编码信息失败");

			AreaBean city = areas.get(0);
			String city_id = city.getCity_id();

			String district_id = null;

			List<AreaBean.District> districts = city.getDistricts().stream().filter(d -> d.getAreas().size() > 0)
					.collect(Collectors.toList());

			String area_id = null;
			if (districts.size() > 0) {
				AreaBean.District district = districts.get(0);
				district_id = district.getDistrict_id();
				area_id = district.getAreas().get(0).getArea_id();
			} else {
				district_id = city.getDistricts().get(0).getDistrict_id();
			}

			ReporterCSS.step("测试一 : 以城市地区编码进行过滤搜索订单");
			orderFilterParam.setSearch_area(city_id + "__");
			List<OrderBean> orderArray = orderService.searchOrder(orderFilterParam);
			Assert.assertNotEquals(orderArray, null, "以城市地区编码进行过滤搜索订单失败");

			ReporterCSS.step("测试二: 以区级编码进行过滤搜索订单");
			orderFilterParam.setSearch_area(city_id + "_" + district_id + "_");
			orderArray = orderService.searchOrder(orderFilterParam);
			Assert.assertNotEquals(orderArray, null, "以区级编码进行过滤搜索订单失败");

			if (area_id != null) {
				Reporter.log("测试三: 以街道编码进行过滤搜索订单");
				orderFilterParam.setSearch_area(city_id + "_" + district_id + "_" + area_id);
				orderArray = orderService.searchOrder(orderFilterParam);
				Assert.assertNotEquals(orderArray, null, "以区级编码进行过滤搜索订单失败");
			}
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void orderSearchTestCase15() {
		ReporterCSS.title("测试点: 订单搜索-按下单日期-以线路筛选订单");
		try {
			routeService.initRouteData();
			List<RouteBean> routes = routeService.getAllRoutes();
			Assert.assertNotEquals(routes, null, "获取全部线路信息失败");

			BigDecimal route_id = null;
			for (RouteBean route : routes) {
				List<OrderBean> orderArray = new ArrayList<>();
				int offset = 0;
				route_id = route.getId();
				orderFilterParam.setRoute_id(route_id);
				while (true) {
					orderFilterParam.setOffset(offset);

					List<OrderBean> tempArray = orderService.searchOrder(orderFilterParam);
					Assert.assertEquals(tempArray != null, true, "订单列表过滤搜索订单失败");
					orderArray.addAll(tempArray);
					if (tempArray.size() >= limit) {
						offset += limit;
						orderFilterParam.setOffset(offset);
					} else {
						break;
					}
				}
				List<OrderBean> fiterOrders = orderArray.stream()
						.filter(o -> !o.getRoute_name().equals(route.getName())).collect(Collectors.toList());
				Assert.assertEquals(fiterOrders.size(), 0, "以线路名称为 [" + route.getName() + "] 的线路进行筛选订单,筛选出不符合过滤条件的订单");
			}

			// 搜索无路线的订单
			int offset = 0;
			List<OrderBean> orderArray = new ArrayList<>();
			while (true) {
				orderFilterParam.setOffset(offset);
				orderFilterParam.setRoute_id(new BigDecimal("-1"));

				List<OrderBean> tempArray = orderService.searchOrder(orderFilterParam);
				Assert.assertEquals(tempArray != null, true, "订单列表过滤搜索订单失败");
				orderArray.addAll(tempArray);
				if (tempArray.size() >= limit) {
					offset += limit;
					orderFilterParam.setOffset(offset);
				} else {
					break;
				}
			}

			List<OrderBean> fiterOrders = orderArray.stream().filter(o -> !o.getRoute_name().equals("无线路"))
					.collect(Collectors.toList());
			Assert.assertEquals(fiterOrders.size(), 0, "以线路名称为 [无线路] 的线路进行筛选订单,筛选出不符合过滤条件的订单");

		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test
	public void orderSearchTestCase16() {
		ReporterCSS.title("测试点: 订单搜索-按下单日期-以报价单筛选订单");
		try {
			Set<String> salemenu_ids = new HashSet<>();
			for (Detail detail : orderDetail.getDetails()) {
				salemenu_ids.add(detail.getSalemenu_id());
			}

			for (String salamenu_id : salemenu_ids) {
				orderFilterParam.setSalemenu_id(salamenu_id);
				List<OrderBean> orderArray = orderService.searchOrder(orderFilterParam);
				Assert.assertNotEquals(orderArray, null, "搜索过滤订单失败");
				OrderBean order = orderArray.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
				Assert.assertNotEquals(order, null, "订单搜索-按下单日期-以报价单筛选订单,没有搜索到订单 " + order_id);
			}
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test
	public void orderSearchTestCase17() {
		ReporterCSS.title("测试点: 订单搜索-按下单日期-以配送司机筛选订单");
		try {

			DriverBean driver = distributeService.initDriverData(station_id);
			Assert.assertNotEquals(driver, null, "初始化司机管理数据失败");

			String receive_address_id = orderDetail.getCustomer().getAddress_id();
			String receive_begin_time = orderDetail.getCustomer().getReceive_begin_time();
			BigDecimal driver_id = driver.getId();
			BigDecimal carrier_id = driver.getCarrier_id();

			boolean result = distributeService.editAssignDistributeTask(order_id, receive_address_id,
					receive_begin_time, driver_id, 1);

			Assert.assertEquals(result, true, "订单 " + order_id + " 分配配送司机失败");

			orderFilterParam.setCarrier_id(carrier_id);
			orderFilterParam.setDriver_id(driver_id);

			List<OrderBean> orderArray = orderService.searchOrder(orderFilterParam);
			Assert.assertNotEquals(orderArray, null, "搜索过滤订单失败");
			OrderBean order = orderArray.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(order, null, "订单搜索-按下单日期-以配送司机筛选订单,没有筛选出订单 " + order_id);

			ReporterCSS.title("测试点: 搜索过滤订单,以装车状态过滤订单");
			orderFilterParam.setInspect_status(1);
			orderArray = orderService.searchOrder(orderFilterParam);
			Assert.assertNotEquals(orderArray, null, "搜索过滤订单失败");

			List<String> tempOrderIds = orderArray.stream()
					.filter(o -> o.getInspect_status() != orderFilterParam.getInspect_status()).map(o -> o.getId())
					.collect(Collectors.toList());
			Assert.assertEquals(tempOrderIds.size(), 0, "搜索过滤订单,搜索未装车的订单,把已装车的订单" + tempOrderIds + "给搜索出来了");

			orderFilterParam.setInspect_status(2);
			orderArray = orderService.searchOrder(orderFilterParam);
			Assert.assertNotEquals(orderArray, null, "搜索过滤订单失败");
			tempOrderIds = orderArray.stream()
					.filter(o -> o.getInspect_status() != orderFilterParam.getInspect_status()).map(o -> o.getId())
					.collect(Collectors.toList());
			Assert.assertEquals(tempOrderIds.size(), 0, "搜索过滤订单,搜索已装车的订单,把未装车的订单" + tempOrderIds + "给搜索出来了");

		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test
	public void orderSearchTestCase18() {
		ReporterCSS.title("测试点: 订单搜索-按下单日期-以打印状态筛选订单");
		try {

			orderFilterParam.setIs_print(0);

			List<OrderBean> orderArray = orderService.searchOrder(orderFilterParam);
			Assert.assertNotEquals(orderArray, null, "搜索过滤订单失败");
			OrderBean order = orderArray.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(order, null, "订单搜索-按下单日期-以订单打印状态筛选订单,没有筛选出订单 " + order_id);

			// 打印后再次搜索

			boolean result = distributeService.createPrintLog(Arrays.asList(order_id));
			Assert.assertEquals(result, true, "新建配送单打印日志失败");

			orderFilterParam.setIs_print(1);
			orderArray = orderService.searchOrder(orderFilterParam);
			Assert.assertNotEquals(orderArray, null, "搜索过滤订单失败");
			order = orderArray.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(order, null, "订单搜索-按下单日期-以订单打印状态[已打印]筛选订单,没有筛选出订单 " + order_id);
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test
	public void orderSearchTestCase19() {
		ReporterCSS.title("测试点: 订单搜索-按下单日期-以订单备注筛选订单");
		try {
			orderFilterParam.setHas_remark(0);
			// 过滤无备注的订单
			List<OrderBean> orderArray = orderService.searchOrder(orderFilterParam);
			Assert.assertNotEquals(orderArray, null, "搜索过滤订单失败");

			List<OrderBean> search_orders = orderArray.stream()
					.filter(o -> o.getRemark() != null && !o.getRemark().equals("")).collect(Collectors.toList());

			Assert.assertEquals(search_orders.size(), 0, "订单搜索-按下单日期-以订单备注筛选订单,搜索[无备注]的订单把[有备注]的订单搜索出来了");

			OrderBean order = orderArray.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertEquals(order, null, "订单搜索-按下单日期-以订单备注筛选订单,搜索无备注的订单,把有备注的订单 " + order_id + " 搜索出来了");

			// 修改订单,订单增加备注信息
			OrderEditParam editOrderParam = new OrderEditParam();
			editOrderParam.setOrder_id(order_id);

			List<OrderCreateParam.OrderSku> orderSkus = new ArrayList<>();
			OrderCreateParam orderCreateParam = new OrderCreateParam();
			OrderCreateParam.OrderSku orderSku = null;
			for (Detail detail : orderDetail.getDetails()) {
				orderSku = orderCreateParam.new OrderSku();
				orderSku.setSpu_id(detail.getSpu_id());
				orderSku.setSku_id(detail.getSku_id());
				orderSku.setIs_price_timing(0);
				orderSku.setAmount(detail.getQuantity());
				orderSku.setSpu_remark("");
				orderSku.setUnit_price(detail.getSale_price());
				orderSkus.add(orderSku);
			}
			editOrderParam.setDetails(orderSkus);

			// 设置收货时间
			OrderData orderData = editOrderParam.new OrderData();
			orderData.setReceive_begin_time(orderDetail.getCustomer().getReceive_begin_time());
			orderData.setReceive_end_time(orderDetail.getCustomer().getReceive_end_time());
			orderData.setRemark("");
			editOrderParam.setOrder_data(orderData);

			boolean result = orderService.editOrder(editOrderParam);
			Assert.assertEquals(result, true, "修改订单 " + order_id + " 操作失败");

			orderFilterParam.setHas_remark(1);
			orderArray = orderService.searchOrder(orderFilterParam);
			Assert.assertNotEquals(orderArray, null, "搜索过滤订单失败");

			List<String> tempOrderIds = orderArray.stream()
					.filter(o -> o.getRemark() == null || o.getRemark().equals("")).map(o -> o.getId())
					.collect(Collectors.toList());
			Assert.assertEquals(tempOrderIds.size(), 0,
					"订单搜索-按下单日期-以订单备注筛选订单,搜索[有备注]的订单把[无备注]的订单" + tempOrderIds + "搜索出来了");

			order = orderArray.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			if (order != null) {
				System.out.println(JsonUtil.objectToStr(order));
				Assert.assertEquals(order.getRemark() == null || order.getRemark().equals(""), true,
						"修改订单" + order_id + ",订单的备注信息没有删除成功");
			}

			Assert.assertEquals(order, null, "订单搜索-按下单日期-以订单备注筛选订单,搜索有备注的订单,把没有备注的订单 " + order_id + " 给搜索出来了");
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void orderSearchTestCase20() {
		ReporterCSS.title("测试点: 订单列表按商户名升序排序");
		try {
			orderFilterParam.setSort_type("addr_asc");

			int offset = 0;
			List<OrderBean> orderArray = new ArrayList<>();
			while (true) {
				orderFilterParam.setOffset(offset);
				List<OrderBean> tempArray = orderService.searchOrder(orderFilterParam);
				orderArray.addAll(tempArray);
				if (tempArray.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> order_ids = orderArray.stream().map(o -> o.getId()).collect(Collectors.toList());

			List<String> expected_order_ids = orderArray.stream().sorted((o1, o2) -> {
				return o1.getCustomer().getAddress_id().compareTo(o2.getCustomer().getAddress_id());
			}).map(o -> o.getId()).collect(Collectors.toList());

			List<String> repeat_order_ids = order_ids.stream().collect(Collectors.toMap(e -> e, e -> 1, Integer::sum))
					.entrySet().stream().filter(entry -> entry.getValue() > 1).map(entry -> entry.getKey())
					.collect(Collectors.toList());

			JSONArray order_ids_array = JSONArray.parseArray(JSON.toJSONString(order_ids));
			JSONArray expected_order_ids_array = JSONArray.parseArray(JSON.toJSONString(expected_order_ids));
			Reporter.log("订单列表按商户名升序排序");
			Reporter.log("排序结果: " + order_ids_array);
			Reporter.log("预期结果: " + expected_order_ids_array);
			logger.info(order_ids_array.toString());
			logger.info(expected_order_ids_array.toString());

			Assert.assertEquals(repeat_order_ids.size() == 0, true, "订单列表按商户名升序排序,如下订单重复出现 " + repeat_order_ids);

			Assert.assertEquals(order_ids_array.toString(), order_ids_array.toString(), "订单列表按商户名升序排序,排序结果与预期不同");
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void orderSearchTestCase21() {
		ReporterCSS.title("测试点: 订单列表按商户ID降序排序");
		try {
			orderFilterParam.setSort_type("addr_desc");

			int offset = 0;
			List<OrderBean> orderArray = new ArrayList<>();
			while (true) {
				orderFilterParam.setOffset(offset);
				List<OrderBean> tempArray = orderService.searchOrder(orderFilterParam);
				orderArray.addAll(tempArray);
				if (tempArray.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> order_ids = orderArray.stream().map(o -> o.getId()).collect(Collectors.toList());

			List<String> expected_order_ids = orderArray.stream().sorted((o1, o2) -> {
				return o2.getCustomer().getAddress_id().compareTo(o1.getCustomer().getAddress_id());
			}).map(o -> o.getId()).collect(Collectors.toList());

			List<String> repeat_order_ids = order_ids.stream().collect(Collectors.toMap(e -> e, e -> 1, Integer::sum))
					.entrySet().stream().filter(entry -> entry.getValue() > 1).map(entry -> entry.getKey())
					.collect(Collectors.toList());

			JSONArray order_ids_array = JSONArray.parseArray(JSON.toJSONString(order_ids));
			JSONArray expected_order_ids_array = JSONArray.parseArray(JSON.toJSONString(expected_order_ids));
			Reporter.log("订单列表按商户名升序排序");
			Reporter.log("排序结果: " + order_ids_array);
			Reporter.log("预期结果: " + expected_order_ids_array);
			logger.info(order_ids_array.toString());
			logger.info(expected_order_ids_array.toString());

			Assert.assertEquals(repeat_order_ids.size() == 0, true, "订单列表按商户名升序排序,如下订单重复出现 " + repeat_order_ids);

			Assert.assertEquals(order_ids_array.toString(), expected_order_ids_array.toString(),
					"订单列表按商户名降序排序,排序结果与预期不同");
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void orderSearchTestCase22() {
		ReporterCSS.title("测试点: 订单列表按订单金额升序排序");
		try {
			orderFilterParam.setSort_type("price_asc");

			int offset = 0;
			List<OrderBean> orderArray = new ArrayList<>();
			while (true) {
				orderFilterParam.setOffset(offset);
				List<OrderBean> tempArray = orderService.searchOrder(orderFilterParam);
				orderArray.addAll(tempArray);
				if (tempArray.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> order_ids = orderArray.stream().map(o -> o.getId()).collect(Collectors.toList());

			List<String> expected_order_ids = orderArray.stream().sorted((o1, o2) -> {
				return o1.getTotal_price().compareTo(o2.getTotal_price());
			}).map(o -> o.getId()).collect(Collectors.toList());

			List<String> repeat_order_ids = order_ids.stream().collect(Collectors.toMap(e -> e, e -> 1, Integer::sum))
					.entrySet().stream().filter(entry -> entry.getValue() > 1).map(entry -> entry.getKey())
					.collect(Collectors.toList());

			JSONArray order_ids_array = JSONArray.parseArray(JSON.toJSONString(order_ids));
			JSONArray expected_order_ids_array = JSONArray.parseArray(JSON.toJSONString(expected_order_ids));
			ReporterCSS.step("订单列表按订单金额升序排序");
			ReporterCSS.step("排序结果: " + order_ids_array);
			ReporterCSS.step("预期结果: " + expected_order_ids_array);
			logger.info(order_ids_array.toString());
			logger.info(expected_order_ids_array.toString());

			Assert.assertEquals(repeat_order_ids.size() == 0, true, "订单列表按商户名升序排序,如下订单重复出现 " + repeat_order_ids);

			Assert.assertEquals(order_ids_array.toString(), expected_order_ids_array.toString(),
					"订单列表按订单金额升序排序,排序结果与预期不同");
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void orderSearchTestCase23() {
		ReporterCSS.title("测试点: 订单列表按订单金额降序排序");
		try {
			orderFilterParam.setSort_type("price_desc");
			int offset = 0;
			List<OrderBean> orderArray = new ArrayList<>();
			while (true) {
				orderFilterParam.setOffset(offset);
				List<OrderBean> tempArray = orderService.searchOrder(orderFilterParam);
				orderArray.addAll(tempArray);
				if (tempArray.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> order_ids = orderArray.stream().map(o -> o.getId()).collect(Collectors.toList());

			List<String> expected_order_ids = orderArray.stream().sorted((o1, o2) -> {
				return o2.getTotal_price().compareTo(o1.getTotal_price());
			}).map(o -> o.getId()).collect(Collectors.toList());

			List<String> repeat_order_ids = order_ids.stream().collect(Collectors.toMap(e -> e, e -> 1, Integer::sum))
					.entrySet().stream().filter(entry -> entry.getValue() > 1).map(entry -> entry.getKey())
					.collect(Collectors.toList());

			JSONArray order_ids_array = JSONArray.parseArray(JSON.toJSONString(order_ids));
			JSONArray expected_order_ids_array = JSONArray.parseArray(JSON.toJSONString(expected_order_ids));
			Reporter.log("订单列表按订单金额降序排序");
			Reporter.log("排序结果: " + order_ids_array);
			Reporter.log("预期结果: " + expected_order_ids_array);
			logger.info(order_ids_array.toString());
			logger.info(expected_order_ids_array.toString());

			Assert.assertEquals(repeat_order_ids.size() == 0, true, "订单列表按商户名升序排序,如下订单重复出现 " + repeat_order_ids);

			Assert.assertEquals(order_ids_array.toString(), expected_order_ids_array.toString(),
					"订单列表按订单金额降序排序,排序结果与预期不同");
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void orderSearchTestCase24() {
		ReporterCSS.title("测试点: 订单列表按订单状态升序排序");
		try {
			orderFilterParam.setSort_type("status_asc");

			int offset = 0;
			List<OrderBean> orderArray = new ArrayList<>();
			while (true) {
				orderFilterParam.setOffset(offset);
				List<OrderBean> tempArray = orderService.searchOrder(orderFilterParam);
				orderArray.addAll(tempArray);
				if (tempArray.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> order_ids = orderArray.stream().map(o -> o.getId()).collect(Collectors.toList());

			List<String> expected_order_ids = orderArray.stream().sorted(Comparator.comparing(OrderBean::getStatus))
					.map(o -> o.getId()).collect(Collectors.toList());

			List<String> repeat_order_ids = order_ids.stream().collect(Collectors.toMap(e -> e, e -> 1, Integer::sum))
					.entrySet().stream().filter(entry -> entry.getValue() > 1).map(entry -> entry.getKey())
					.collect(Collectors.toList());

			JSONArray order_ids_array = JSONArray.parseArray(JSON.toJSONString(order_ids));
			JSONArray expected_order_ids_array = JSONArray.parseArray(JSON.toJSONString(expected_order_ids));
			Reporter.log("订单列表按订单状态升序排序");
			Reporter.log("排序结果: " + order_ids_array);
			Reporter.log("预期结果: " + expected_order_ids_array);
			logger.info(order_ids_array.toString());
			logger.info(expected_order_ids_array.toString());

			Assert.assertEquals(repeat_order_ids.size() == 0, true, "订单列表按商户名升序排序,如下订单重复出现 " + repeat_order_ids);

			Assert.assertEquals(order_ids_array.toString(), expected_order_ids_array.toString(),
					"订单列表按订单状态升序排序,排序结果与预期不同");
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void orderSearchTestCase25() {
		ReporterCSS.title("测试点: 订单列表按订单状态降序排序");
		try {
			orderFilterParam.setSort_type("status_desc");
			int offset = 0;
			List<OrderBean> orderArray = new ArrayList<>();
			while (true) {
				orderFilterParam.setOffset(offset);
				List<OrderBean> tempArray = orderService.searchOrder(orderFilterParam);
				orderArray.addAll(tempArray);
				if (tempArray.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> order_ids = orderArray.stream().map(o -> o.getId()).collect(Collectors.toList());

			List<String> expected_order_ids = orderArray.stream()
					.sorted(Comparator.comparing(OrderBean::getStatus).reversed()).map(o -> o.getId())
					.collect(Collectors.toList());

			List<String> repeat_order_ids = order_ids.stream().collect(Collectors.toMap(e -> e, e -> 1, Integer::sum))
					.entrySet().stream().filter(entry -> entry.getValue() > 1).map(entry -> entry.getKey())
					.collect(Collectors.toList());

			JSONArray order_ids_array = JSONArray.parseArray(JSON.toJSONString(order_ids));
			JSONArray expected_order_ids_array = JSONArray.parseArray(JSON.toJSONString(expected_order_ids));
			Reporter.log("订单列表按订单状态降序排序");
			Reporter.log("排序结果: " + order_ids_array);
			Reporter.log("预期结果: " + expected_order_ids_array);
			logger.info(order_ids_array.toString());
			logger.info(expected_order_ids_array.toString());

			Assert.assertEquals(repeat_order_ids.size() == 0, true, "订单列表按商户名升序排序,如下订单重复出现 " + repeat_order_ids);

			Assert.assertEquals(order_ids_array.toString(), expected_order_ids_array.toString(),
					"订单列表按订单状态降序排序,排序结果与预期不同");

		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void orderSearchTestCase26() {
		ReporterCSS.title("测试点: 订单列表按订单运费升序排序");
		try {
			orderFilterParam.setSort_type("freight_asc");

			int offset = 0;
			List<OrderBean> orderArray = new ArrayList<>();
			while (true) {
				orderFilterParam.setOffset(offset);
				List<OrderBean> tempArray = orderService.searchOrder(orderFilterParam);
				orderArray.addAll(tempArray);
				if (tempArray.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> order_ids = orderArray.stream().map(o -> o.getId()).collect(Collectors.toList());

			List<String> expected_order_ids = orderArray.stream().sorted((o1, o2) -> {
				return o1.getFreight().compareTo(o2.getFreight());
			}).map(o -> o.getId()).collect(Collectors.toList());

			List<String> repeat_order_ids = order_ids.stream().collect(Collectors.toMap(e -> e, e -> 1, Integer::sum))
					.entrySet().stream().filter(entry -> entry.getValue() > 1).map(entry -> entry.getKey())
					.collect(Collectors.toList());

			JSONArray order_ids_array = JSONArray.parseArray(JSON.toJSONString(order_ids));
			JSONArray expected_order_ids_array = JSONArray.parseArray(JSON.toJSONString(expected_order_ids));
			Reporter.log("订单列表按订单运费升序排序");
			Reporter.log("排序结果: " + order_ids_array);
			Reporter.log("预期结果: " + expected_order_ids_array);
			logger.info(order_ids_array.toString());
			logger.info(expected_order_ids_array.toString());

			Assert.assertEquals(repeat_order_ids.size() == 0, true, "订单列表按商户名升序排序,如下订单重复出现 " + repeat_order_ids);

			Assert.assertEquals(order_ids_array.toString(), expected_order_ids_array.toString(),
					"订单列表按订单运费升序排序,排序结果与预期不同");
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void orderSearchTestCase27() {
		ReporterCSS.title("测试点: 订单列表按订单运费降序排序");
		try {
			orderFilterParam.setSort_type("freight_desc");
			int offset = 0;
			List<OrderBean> orderArray = new ArrayList<>();
			while (true) {
				orderFilterParam.setOffset(offset);
				List<OrderBean> tempArray = orderService.searchOrder(orderFilterParam);
				orderArray.addAll(tempArray);
				if (tempArray.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> order_ids = orderArray.stream().map(o -> o.getId()).collect(Collectors.toList());

			List<String> expected_order_ids = orderArray.stream().sorted((o1, o2) -> {
				return o2.getFreight().compareTo(o1.getFreight());
			}).map(o -> o.getId()).collect(Collectors.toList());

			List<String> repeat_order_ids = order_ids.stream().collect(Collectors.toMap(e -> e, e -> 1, Integer::sum))
					.entrySet().stream().filter(entry -> entry.getValue() > 1).map(entry -> entry.getKey())
					.collect(Collectors.toList());

			Assert.assertEquals(repeat_order_ids.size() == 0, true, "订单列表按商户名升序排序,如下订单重复出现 " + repeat_order_ids);

			JSONArray order_ids_array = JSONArray.parseArray(JSON.toJSONString(order_ids));
			JSONArray expected_order_ids_array = JSONArray.parseArray(JSON.toJSONString(expected_order_ids));
			ReporterCSS.log("订单列表按订单运费降序排序");
			ReporterCSS.log("排序结果: " + order_ids_array);
			ReporterCSS.log("预期结果: " + expected_order_ids_array);
			logger.info(order_ids_array.toString());
			logger.info(expected_order_ids_array.toString());

			Assert.assertEquals(order_ids_array.toString(), expected_order_ids_array.toString(),
					"订单列表按订单运费降序排序,排序结果与预期不同");
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void orderSearchTestCase28() {
		ReporterCSS.title("测试点: 搜索过滤订单,按订单来源过滤订单");
		try {
			orderFilterParam.setClient(1);
			List<OrderBean> orderList = orderService.searchOrder(orderFilterParam);
			Assert.assertNotEquals(orderList, null, "搜索过滤订单列表失败");

			List<String> tempOrderIds = orderList.stream().filter(o -> o.getClient() != orderFilterParam.getClient())
					.map(o -> o.getId()).collect(Collectors.toList());
			Assert.assertEquals(tempOrderIds.size(), 0, "搜索过滤订单,按订单来源过滤订单,过滤后台下的订单,过滤出了非后台下的订单 " + tempOrderIds);
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void orderSearchTestCase29() {
		ReporterCSS.title("测试点: 订单搜索-按收货日期-以商户名称进行搜索");
		try {
			String customer_name = orderDetail.getCustomer().getExtender().getResname();
			orderFilterParam = new OrderFilterParam();
			orderFilterParam.setQuery_type(3);
			orderFilterParam.setSearch_type(1);
			orderFilterParam.setReceive_start_date_new(receive_start_date);
			orderFilterParam.setReceive_end_date_new(receive_end_date);
			orderFilterParam.setSearch_text(customer_name);
			orderFilterParam.setLimit(limit);

			int offset = 0;
			List<OrderBean> orderList = new ArrayList<OrderBean>();
			List<OrderBean> tempOrderList = null;
			while (true) {
				orderFilterParam.setOffset(offset);
				tempOrderList = orderService.searchOrder(orderFilterParam);
				Assert.assertNotEquals(tempOrderList, null, "搜索过滤订单列表失败");
				orderList.addAll(tempOrderList);
				if (tempOrderList.size() < limit) {
					break;
				}
				offset += limit;
			}

			OrderBean order = orderList.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(order, null, "订单搜索-按收货日期-以商户名称进行搜索,没有过滤出目标订单 " + order_id);
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test
	public void orderSearchTestCase30() {
		ReporterCSS.title("测试点: 订单搜索-按下单日期-以订单号称进行搜索");
		try {
			orderFilterParam.setSearch_text(order_id);

			List<OrderBean> orderList = orderService.searchOrder(orderFilterParam);
			Assert.assertNotEquals(orderList, null, "搜索过滤订单列表失败");

			OrderBean order = orderList.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(order, null, "订单搜索-按下单日期-以订单号称进行搜索,没有过滤出目标订单 " + order_id);
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test
	public void orderSearchTestCase31() {
		ReporterCSS.title("测试点: 订单搜索-按收货日期-以订单号称进行搜索");
		try {
			orderFilterParam = new OrderFilterParam();
			orderFilterParam.setQuery_type(3);
			orderFilterParam.setSearch_type(1);
			orderFilterParam.setReceive_start_date_new(receive_start_date);
			orderFilterParam.setReceive_end_date_new(receive_end_date);
			orderFilterParam.setSearch_text(order_id);
			orderFilterParam.setOffset(0);
			orderFilterParam.setLimit(limit);

			List<OrderBean> orderList = orderService.searchOrder(orderFilterParam);
			Assert.assertNotEquals(orderList, null, "搜索过滤订单列表失败");

			OrderBean order = orderList.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(order, null, "订单搜索-按收货日期-以订单号称进行搜索,没有过滤出目标订单 " + order_id);
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test
	public void orderSearchTestCase32() {
		ReporterCSS.title("测试点: 订单搜索-按收货日期-以商户名+报价单筛选过滤");
		try {
			Set<String> salemenu_ids = new HashSet<>();
			for (Detail detail : orderDetail.getDetails()) {
				salemenu_ids.add(detail.getSalemenu_id());
			}

			String customer_name = orderDetail.getCustomer().getExtender().getResname();

			orderFilterParam = new OrderFilterParam();
			orderFilterParam.setQuery_type(3);
			orderFilterParam.setSearch_type(1);
			orderFilterParam.setReceive_start_date_new(receive_start_date);
			orderFilterParam.setReceive_end_date_new(receive_end_date);
			orderFilterParam.setLimit(limit);
			orderFilterParam.setOffset(0);
			orderFilterParam.setSearch_text(customer_name);

			for (String salamenu_id : salemenu_ids) {
				orderFilterParam.setSalemenu_id(salamenu_id);
				List<OrderBean> orderArray = orderService.searchOrder(orderFilterParam);
				Assert.assertNotEquals(orderArray, null, "搜索过滤订单失败");
				OrderBean order = orderArray.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
				Assert.assertNotEquals(order, null, "订单搜索-按收货日期-以商户名+报价单筛选过滤,没有搜索到订单 " + order_id);
			}
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void orderSearchTestCase33() {
		ReporterCSS.title("测试点: 订单搜索-按下单日期-以出库状态进行搜索");
		try {
			orderFilterParam.setStock_type(1);

			int offset = 0;
			List<OrderBean> orderList = new ArrayList<OrderBean>();
			List<OrderBean> tempOrderList = null;
			while (true) {
				orderFilterParam.setOffset(offset);
				tempOrderList = orderService.searchOrder(orderFilterParam);
				Assert.assertNotEquals(tempOrderList, null, "搜索过滤订单列表失败");
				orderList.addAll(tempOrderList);
				if (tempOrderList.size() < limit) {
					break;
				}
				offset += limit;
			}
			OrderBean order = orderList.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(order, null, "订单搜索-按下单日期-以出库状态进行搜索,没有过滤出目标订单 " + order_id);
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test
	public void orderSearchTestCase34() {
		ReporterCSS.title("测试点: 订单搜索-按收货日期-以出库状态进行搜索");
		try {
			orderFilterParam = new OrderFilterParam();
			orderFilterParam.setQuery_type(3);
			orderFilterParam.setSearch_type(1);
			orderFilterParam.setReceive_start_date_new(receive_start_date);
			orderFilterParam.setReceive_end_date_new(receive_end_date);
			orderFilterParam.setStock_type(1);
			orderFilterParam.setLimit(limit);

			int offset = 0;
			List<OrderBean> orderList = new ArrayList<OrderBean>();
			List<OrderBean> tempOrderList = null;
			while (true) {
				orderFilterParam.setOffset(offset);
				tempOrderList = orderService.searchOrder(orderFilterParam);
				Assert.assertNotEquals(tempOrderList, null, "搜索过滤订单列表失败");
				orderList.addAll(tempOrderList);
				if (tempOrderList.size() < limit) {
					break;
				}
				offset += limit;
			}
			OrderBean order = orderList.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(order, null, "订单搜索-按收货日期-以出库状态进行搜索,没有过滤出目标订单 " + order_id);
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test
	public void orderSearchTestCase35() {
		ReporterCSS.title("测试点: 老版本UI,订单列表,按下单日期搜索订单");
		try {
			orderFilterParam = new OrderFilterParam();
			orderFilterParam.setQuery_type(1);
			orderFilterParam.setStart_date(today);
			orderFilterParam.setEnd_date(today);
			orderFilterParam.setOffset(0);
			orderFilterParam.setLimit(limit);
			List<OrderBean> orderArray = orderService.searchOrder(orderFilterParam);

			Assert.assertEquals(orderArray != null, true, "老版本UI,订单列表过滤搜索订单失败");

			OrderBean orderDetail = orderArray.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(orderDetail, null, "老版本UI,按下单日期过滤,没有搜索到订单 " + order_id);

		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test
	public void orderSearchTestCase36() {
		ReporterCSS.title("测试点: 老版本UI,按收货日期过滤订单");
		try {
			orderFilterParam = new OrderFilterParam();
			orderFilterParam.setQuery_type(3);
			orderFilterParam.setReceive_start_date(receive_start_date.substring(0, 10));
			orderFilterParam.setReceive_end_date(receive_end_date.substring(0, 10));
			orderFilterParam.setLimit(limit);
			orderFilterParam.setOffset(0);

			List<OrderBean> orderArray = orderService.searchOrder(orderFilterParam);

			Assert.assertEquals(orderArray != null, true, "老版本UI,订单列表过滤搜索订单失败");

			OrderBean order = orderArray.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);

			Assert.assertNotEquals(order, null, "老版本UI,按收货时间过滤,没有搜索到订单 " + order_id);
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}

	@Test
	public void orderSearchTestCase37() {
		ReporterCSS.title("测试点: 老版本UI,按运营时间过滤搜索订单");
		try {
			// 获取订单的收货时间 年-月-日
			String receive_begin_time = orderDetail.getCustomer().getReceive_begin_time();
			receive_begin_time = receive_begin_time.split(" ")[0];

			String time_config_id = orderDetail.getTime_config_id();
			ServiceTimeBean serviceTime = serviceTimeService.getServiceTimeById(time_config_id);
			Assert.assertNotEquals(serviceTime, null, "获取运营时间详细信息失败");

			String cycle_start_time = null;
			String cycle_end_time = null;
			String start = null;
			String end = null;
			// 普通运营时间按下单周期搜索,预售运营时间按收货周期搜索
			if (serviceTime.getType() == 1) {
				start = serviceTime.getOrder_time_limit().getStart();
				end = serviceTime.getOrder_time_limit().getEnd();
				int span = serviceTime.getOrder_time_limit().getE_span_time();

				cycle_start_time = today + " " + start;
				cycle_end_time = TimeUtil.calculateTime("yyyy-MM-dd", today, span, Calendar.DAY_OF_YEAR) + " " + end;
			} else {
				// 收货开始时间 时:分
				start = serviceTime.getReceive_time_limit().getStart();

				// 收货结束时间 时:分
				end = serviceTime.getReceive_time_limit().getEnd();

				int span = serviceTime.getReceive_time_limit().getReceiveEndSpan();

				String receive_end_time = TimeUtil.calculateTime("yyyy-MM-dd", receive_begin_time, span,
						Calendar.DAY_OF_YEAR);

				cycle_start_time = receive_begin_time + " " + start;
				cycle_end_time = receive_end_time + " " + end;
			}

			int limit = 50;
			int offset = 0;

			orderFilterParam = new OrderFilterParam();
			orderFilterParam.setQuery_type(2);
			orderFilterParam.setTime_config_id(time_config_id);
			orderFilterParam.setCycle_start_time(cycle_start_time);
			orderFilterParam.setCycle_end_time(cycle_end_time);

			orderFilterParam.setLimit(limit);
			List<OrderBean> orderArray = new ArrayList<>();
			List<OrderBean> tempOrderArray = null;
			while (true) {
				orderFilterParam.setOffset(offset);
				tempOrderArray = orderService.searchOrder(orderFilterParam);
				Assert.assertEquals(tempOrderArray != null, true, "老版本UI,订单列表过滤搜索订单失败");
				orderArray.addAll(tempOrderArray);
				if (tempOrderArray.size() < limit) {
					break;
				} else {
					offset += limit;
				}
			}

			OrderBean order = orderArray.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(order, null, "老版本UI,按运营时间过滤,没有搜索到订单 " + order_id);
		} catch (Exception e) {
			logger.error("搜索过滤订单遇到错误: ", e);
			Assert.fail("搜索过滤订单遇到错误: ", e);
		}
	}
}
