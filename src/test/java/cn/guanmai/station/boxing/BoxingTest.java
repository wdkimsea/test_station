package cn.guanmai.station.boxing;

import cn.guanmai.station.bean.boxing.BoxBean;
import cn.guanmai.station.bean.boxing.BoxingManagePrintDetailBean;
import cn.guanmai.station.bean.boxing.BoxingOrderDetailBean;
import cn.guanmai.station.bean.boxing.BoxingOrderListBean;
import cn.guanmai.station.bean.boxing.param.BoxParam;
import cn.guanmai.station.bean.boxing.param.BoxingManageOrderParam;
import cn.guanmai.station.bean.boxing.param.BoxingManagePrintParam;
import cn.guanmai.station.bean.boxing.param.BoxingOrderParam;
import cn.guanmai.station.bean.delivery.RouteBean;
import cn.guanmai.station.bean.order.OrderBean;
import cn.guanmai.station.bean.order.OrderCycle;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.param.OrderFilterParam;
import cn.guanmai.station.impl.boxing.BoxingAppServiceImpl;
import cn.guanmai.station.impl.delivery.RouteServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.interfaces.boxing.BoxingAppService;
import cn.guanmai.station.interfaces.delivery.RouteService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.purchase.PurchaserCreateTest;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by abc on 2020/2/20.
 */
public class BoxingTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PurchaserCreateTest.class); // 这里得改下类
	private BoxingAppService boxingAppService;
	private OrderService orderService;
	private OrderDetailBean orderDetail;
	private OrderTool orderTool;
	private String order_id;
	private OrderCycle orderCycle;
	private RouteService routeService;
	private String time_config_id;
	private String date;
	private String begin_time;
	private String end_time;
	private String address_name;
	private String cycle_start_time;
	private String cycle_end_time;

	@BeforeClass
	public void init() {

		try {
			Map<String, String> headers = getStationCookie();
			orderTool = new OrderTool(headers);
			orderService = new OrderServiceImpl(headers);
			routeService = new RouteServiceImpl(headers);
			Assert.assertNotNull(headers, "装箱app登录失败");

			boxingAppService = new BoxingAppServiceImpl(headers);
		} catch (Exception e) {
			logger.error("装箱app登录遇到错误", e);
			Assert.fail("装箱app登录遇到错误", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			order_id = orderTool.oneStepCreateOrder(4);
			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 订单详细信息失败");
			address_name = orderDetail.getCustomer().getExtender().getResname();

			orderCycle = orderTool.getOrderOperationCycle(order_id);
			Assert.assertNotEquals(orderCycle, null, "获取订单运营配置信息发生错误");
			time_config_id = orderCycle.getTime_config_id();
			date = orderCycle.getCycle_start_time().substring(0, 10);
			cycle_start_time = orderCycle.getCycle_start_time();
			cycle_end_time = orderCycle.getCycle_end_time();
			begin_time = cycle_start_time + ":00";
			end_time = cycle_end_time + ":00";
		} catch (Exception e) {
			logger.error("创建订单遇到错误: ", e);
			Assert.fail("创建订单遇到错误: ", e);
		}
	}

	@Test(timeOut = 30000)
	public void boxingAppTestCase001() {
		ReporterCSS.title("测试点:装箱app订单列表");
		try {
			order_id = orderTool.oneStepCreateOrder(4);
			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 订单详细信息失败");

			orderCycle = orderTool.getOrderOperationCycle(order_id);
			Assert.assertNotEquals(orderCycle, null, "获取订单运营配置信息发生错误");
			time_config_id = orderCycle.getTime_config_id();
			date = orderCycle.getCycle_start_time().substring(0, 10);

			BoxingOrderParam bolParam = new BoxingOrderParam();
			bolParam.setTime_config_id(time_config_id);
			bolParam.setDate(date);
			bolParam.setSearch("");

			BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(bolParam);
			Assert.assertNotEquals(bolBean, null, "获取装箱app订单列表发生错误");
			Assert.assertEquals(bolBean.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			// 拉取station该运营时间下所有的订单
			int limit = 50;
			int offset = 0;
			OrderFilterParam paramBean = new OrderFilterParam();
			paramBean.setTime_config_id(time_config_id);
			paramBean.setCycle_start_time(cycle_start_time);
			paramBean.setCycle_end_time(cycle_end_time);
			paramBean.setQuery_type(2);
			paramBean.setLimit(limit);
			List<String> orderArray = new ArrayList<>();
			List<OrderBean> tempOrderArray = null;
			while (true) {
				paramBean.setOffset(offset);
				tempOrderArray = orderService.searchOrder(paramBean);
				Assert.assertEquals(tempOrderArray != null, true, "订单列表过滤搜索订单失败");

				// 对比每一个订单都在装箱app可以找到
				for (int i = 0; i < tempOrderArray.size(); i++) {
					String order_id = tempOrderArray.get(i).getId();
					bolParam.setSearch(order_id);
					bolBean = boxingAppService.getBoxingOrderList(bolParam);
					Assert.assertNotEquals(bolBean, null, "装箱app列表根据订单搜索失败");
					if (bolBean.getOrders().size() == 0) {
						orderArray.add(order_id);
					}
				}
				if (tempOrderArray.size() < limit) {
					break;
				} else {
					offset += limit;
				}
			}

			// 无法在装箱app列表找到的订单
			Assert.assertEquals(orderArray.size() == 0, true, "station订单列表没有在装箱app列表搜索到" + orderArray.toString());
		} catch (Exception e) {
			logger.error("查询装箱列表遇到错误", e);
			Assert.fail("获取订单装箱列表遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase002() {
		ReporterCSS.title("测试点:根据订单号查询装箱列表");
		try {
			BoxingOrderParam bolParam = new BoxingOrderParam();
			bolParam.setTime_config_id(time_config_id);
			bolParam.setDate(date);
			bolParam.setSearch(order_id);

			BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(bolParam);
			Assert.assertNotEquals(bolBean, null, "获取装箱app订单列表发生错误");
			Assert.assertEquals(bolBean.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			BoxingOrderListBean.Orders verify_order_id = bolBean.getOrders().stream()
					.filter(s -> s.getOrder_id().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(verify_order_id, null, "装箱app订单列表无法查找到订单" + order_id);
		} catch (Exception e) {
			logger.error("根据订单号查询装箱列表遇到错误", e);
			Assert.fail("根据订单号查询装箱列表遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase003() {
		ReporterCSS.title("测试点:根据商户名查询装箱列表");
		try {
			BoxingOrderParam bolParam = new BoxingOrderParam();
			bolParam.setTime_config_id(time_config_id);
			bolParam.setDate(date);
			bolParam.setSearch(address_name);

			BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(bolParam);
			Assert.assertNotEquals(bolBean, null, "根据商户名查询装箱列表遇到错误");
			Assert.assertEquals(bolBean.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			List<BoxingOrderListBean.Orders> orders = bolBean.getOrders();
			boolean verify_result = false;
			OK: for (BoxingOrderListBean.Orders order : orders) {
				verify_result = order.getAddress_name().equals(order.getAddress_name());
				if (verify_result == true) {
					break OK;
				}
			}
			Assert.assertEquals(verify_result, true, "装箱app订单列表按商户名搜索结果出现不一样的商户名" + order_id);
		} catch (Exception e) {
			logger.error("根据商户名查询装箱列表遇到错误", e);
			Assert.fail("根据商户名查询装箱列表遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase004() {
		ReporterCSS.title("测试点:搜索未集包的订单");
		try {
			BoxingOrderParam bolParam = new BoxingOrderParam(time_config_id, "0", date, "");
			BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(bolParam);
			Assert.assertNotEquals(bolBean, null, "搜索未集包的订单遇到错误");
			Assert.assertEquals(bolBean.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			List<BoxingOrderListBean.Orders> orders = bolBean.getOrders();
			List<String> temp_order_ids = orders.stream().filter(s -> s.getOrder_box_status().equals("1"))
					.map(o -> o.getOrder_id()).collect(Collectors.toList());

			Assert.assertEquals(temp_order_ids.size() == 0, true, "根据未集包状态搜索列表出现集包的订单" + temp_order_ids);
		} catch (Exception e) {
			logger.error("搜索未集包的订单遇到错误", e);
			Assert.fail("搜索未集包的订单遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase005() {
		ReporterCSS.title("测试点:搜索已集包的订单");
		try {
			BoxingOrderParam bolParam = new BoxingOrderParam(time_config_id, "0", date, "");
			BoxingOrderListBean boxOderList = boxingAppService.getBoxingOrderList(bolParam);
			Assert.assertNotEquals(boxOderList, null, "搜索未集包的订单遇到错误");
			Assert.assertEquals(boxOderList.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			// 标记已集包
			bolParam = new BoxingOrderParam();
			bolParam.setOrder_id(order_id);
			bolParam.setOrder_box_status("1");
			boolean result = boxingAppService.updateBoxingOrderStatus(bolParam);
			Assert.assertEquals(result, true, "修改订单为集包状态失败");

			bolParam = new BoxingOrderParam(time_config_id, "1", date, "");
			boxOderList = boxingAppService.getBoxingOrderList(bolParam);
			Assert.assertNotEquals(boxOderList, null, "搜索未集包的订单遇到错误");
			Assert.assertEquals(boxOderList.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			List<BoxingOrderListBean.Orders> orders = boxOderList.getOrders();

			List<String> temp_order_ids = orders.stream().filter(s -> s.getOrder_box_status().equals("0"))
					.map(o -> o.getOrder_id()).collect(Collectors.toList());

			Assert.assertEquals(temp_order_ids.size() == 0, true, "根据集包状态搜索列表出现未集包的订单" + temp_order_ids);
		} catch (Exception e) {
			logger.error("搜索已集包的订单遇到错误", e);
			Assert.fail("搜索已集包的订单遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase006() {
		ReporterCSS.title("测试点:按无线路搜索装箱app订单");
		try {
			BoxingOrderParam bolParam = new BoxingOrderParam();
			bolParam.setTime_config_id(time_config_id);
			bolParam.setDate(date);
			bolParam.setSearch("");
			bolParam.setRoute_id("-1");

			BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(bolParam);
			Assert.assertNotEquals(bolBean, null, "搜索未集包的订单遇到错误");

			List<String> temp_order_ids = bolBean.getOrders().stream()
					.filter(o -> o.getRoute_name().compareTo("无线路") != 0).map(o -> o.getOrder_id())
					.collect(Collectors.toList());
			Assert.assertEquals(temp_order_ids.size() == 0, true, "按无线路搜索结果是其他路线的订单列表" + temp_order_ids);
		} catch (Exception e) {
			logger.error("按线路搜索遇到错误", e);
			Assert.fail("按线路搜索遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase007() {
		ReporterCSS.title("测试点:按线路搜索app订单");
		try {
			BoxingOrderParam bolParam = new BoxingOrderParam();
			bolParam.setTime_config_id(time_config_id);
			bolParam.setDate(date);
			bolParam.setSearch("");

			routeService.initRouteData();
			List<RouteBean> routes = routeService.getAllRoutes();
			Assert.assertNotEquals(routes, null, "获取全部线路信息失败");

			// 根据route_id获取route_name
			String msg = null;
			boolean verify_result = true;
			for (RouteBean routeBean : routes) {
				BigDecimal route_id = routeBean.getId();
				String route_name = routeBean.getName();
				bolParam.setRoute_id(String.valueOf(route_id));

				BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(bolParam);
				Assert.assertNotEquals(bolBean, null, "根据路线搜索装箱app订单列表遇到错误");

				List<String> temp_order_ids = bolBean.getOrders().stream()
						.filter(o -> !o.getRoute_name().equals(route_name)).map(o -> o.getOrder_id())
						.collect(Collectors.toList());

				if (temp_order_ids.size() != 0) {
					msg = String.format("按线路搜索结果出现其他线路的订单,预期线路:%s,不是预期线路的订单列表:%s", route_name, temp_order_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					verify_result = false;
				}
			}

			Assert.assertEquals(verify_result, true, "按线路搜索结果列表和搜索条件不符合");
		} catch (Exception e) {
			logger.error("按线路搜索遇到错误", e);
			Assert.fail("按线路搜索遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase008() {
		ReporterCSS.title("测试点:装箱app订单详情接口");
		try {
			BoxingOrderParam bolParam = new BoxingOrderParam();
			bolParam.setTime_config_id(time_config_id);
			bolParam.setDate(date);
			bolParam.setSearch("");

			BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(bolParam);
			Assert.assertNotEquals(bolBean, null, "获取装箱app订单列表发生错误");
			Assert.assertEquals(bolBean.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			bolParam = new BoxingOrderParam();
			bolParam.setOrder_id(order_id);
			bolParam.setSearch("");

			BoxingOrderDetailBean bodBean = boxingAppService.getBoxingOrderDetail(bolParam);
			Assert.assertNotEquals(bodBean, null, "订单详情接口返回为空");
			Assert.assertEquals(bodBean.getDetails().size() != 0, true, "订单" + order_id + " 实际存在数据,订单返回数据为空");

			// 最好是和订单详情信息对比下，条目是否对的上，对应的下单数啥的
			boolean result = true;
			String msg = null;
			if (!bodBean.getOrder_id().equals(order_id)) {
				msg = String.format("station订单id和装箱app订单id不一致,装箱app订单:%s,station订单:%s", order_id,
						bodBean.getOrder_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			// 获取station订单详情列表
			List<String> stationDetail = new ArrayList<>();
			List<OrderDetailBean.Detail> stationOrderDetails = orderDetail.getDetails();
			for (OrderDetailBean.Detail detail : stationOrderDetails) {
				stationDetail.add(detail.getSku_id());
			}

			// 获取box订单详情列表
			List<String> boxDetail = new ArrayList<>();
			List<BoxingOrderDetailBean.Details> boxOrderDetails = bodBean.getDetails();
			for (BoxingOrderDetailBean.Details detail : boxOrderDetails) {
				boxDetail.add(detail.getSku_id());
			}

			String[] stationArray = stationDetail.toArray(new String[] {});
			String[] boxArray = boxDetail.toArray(new String[] {});
			Arrays.sort(stationArray);
			Arrays.sort(boxArray);

			if (!Arrays.equals(stationArray, boxArray)) {
				msg = String.format("station订单商品详情和装箱app订单商品详情不一致,装箱app订单:%s,station订单:%s", stationDetail, boxDetail);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "station订单详情和装箱订单详情不一致");
		} catch (Exception e) {
			logger.error("查询装箱订单详情遇到错误", e);
			Assert.fail("查询装箱订单详情遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase009() {
		ReporterCSS.title("测试点:装箱app订单详情根据商品名搜索");
		try {
			BoxingOrderParam bolParam = new BoxingOrderParam();
			bolParam.setTime_config_id(time_config_id);
			bolParam.setDate(date);
			bolParam.setSearch("");

			BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(bolParam);
			Assert.assertNotEquals(bolBean, null, "获取装箱app订单列表发生错误");
			Assert.assertEquals(bolBean.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			String sku_name = NumberUtil.roundNumberInList(orderDetail.getDetails()).getSku_name();

			bolParam = new BoxingOrderParam();
			bolParam.setOrder_id(order_id);
			bolParam.setSearch(URLEncoder.encode(sku_name, "utf-8"));

			BoxingOrderDetailBean bodBean = boxingAppService.getBoxingOrderDetail(bolParam);
			Assert.assertNotEquals(bodBean, null, "订单详情接口返回为空");
			Assert.assertEquals(bodBean.getDetails().size() != 0, true, "订单" + order_id + " 实际存在数据,订单返回数据为空");

			BoxingOrderDetailBean.Details boDetailBean = bodBean.getDetails().stream()
					.filter(s -> s.getSku_name().equals(sku_name)).findAny().orElse(null);
			Assert.assertNotEquals(boDetailBean, null, "订单没有找到该商品");

		} catch (Exception e) {
			logger.error("装箱app订单详情根据商品名搜索遇到错误", e);
			Assert.fail("装箱app订单详情根据商品名搜索遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase010() {
		ReporterCSS.title("测试点:装箱app订单详情根据未装箱搜索");
		try {
			BoxingOrderParam bolParam = new BoxingOrderParam();
			bolParam.setTime_config_id(time_config_id);
			bolParam.setDate(date);
			bolParam.setSearch("");

			BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(bolParam);
			Assert.assertNotEquals(bolBean, null, "获取装箱app订单列表发生错误");
			Assert.assertEquals(bolBean.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			bolParam = new BoxingOrderParam();
			bolParam.setOrder_id(order_id);
			bolParam.setSku_box_status("0");
			bolParam.setSearch("");

			BoxingOrderDetailBean bodBean = boxingAppService.getBoxingOrderDetail(bolParam);
			Assert.assertNotEquals(bodBean, null, "订单详情接口返回为空");
			Assert.assertEquals(bodBean.getDetails().size() != 0, true, "订单" + order_id + " 实际存在数据,订单返回数据为空");

			BoxingOrderDetailBean.Details boDetailBean = bodBean.getDetails().stream()
					.filter(s -> s.getSku_box_status().equals("0")).findAny().orElse(null);
			Assert.assertNotEquals(boDetailBean, null, order_id + "存在商品根据未装箱搜索结果不是未装箱");

		} catch (Exception e) {
			logger.error("装箱app订单详情根据商品名搜索遇到错误", e);
			Assert.fail("装箱app订单详情根据商品名搜索遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase011() {
		ReporterCSS.title("测试点:装箱app订单详情根据已装箱搜索");
		try {
			BoxingOrderParam bolParam = new BoxingOrderParam();
			bolParam.setTime_config_id(time_config_id);
			bolParam.setDate(date);
			bolParam.setSearch("");

			BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(bolParam);
			Assert.assertNotEquals(bolBean, null, "获取装箱app订单列表发生错误");
			Assert.assertEquals(bolBean.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			bolParam = new BoxingOrderParam();
			bolParam.setOrder_id(order_id);
			bolParam.setSku_box_status("0");
			bolParam.setSearch("");

			BoxingOrderDetailBean bodBean = boxingAppService.getBoxingOrderDetail(bolParam);
			Assert.assertNotEquals(bodBean, null, "订单详情接口返回为空");
			Assert.assertEquals(bodBean.getDetails().size() != 0, true, "订单" + order_id + " 实际存在数据,订单返回数据为空");

			if (bodBean.getOrder_box_status().equals("1")) {
				BoxingOrderParam boParam = new BoxingOrderParam();
				boParam.setOrder_id(order_id);
				boParam.setOrder_box_status("0");
				boolean result = boxingAppService.updateBoxingOrderStatus(boParam);
				Assert.assertEquals(result, true, "修改订单为未集包遇到错误");
			}

			BoxingOrderDetailBean.Details boDetailBean = bodBean.getDetails().stream()
					.filter(s -> s.getSku_box_status().equals("0")).findAny().orElse(null);
			Assert.assertNotEquals(boDetailBean, null, order_id + "存在商品根据未装箱搜索结果不是未装箱");
			String package_id = boDetailBean.getPackage_id();

			// 创建箱子
			String box_id = boxingAppService.createBox();
			Assert.assertNotEquals(box_id, null, "箱子创建过程中遇到错误");

			// 将商品装入箱子
			List<String> package_ids = new ArrayList<String>();
			package_ids.add(package_id);
			// 用 List<String> 存储这个参数,接口实现那边用 GsonUtil转下就行
			boolean result = boxingAppService.packageInBox(package_ids, box_id);
			Assert.assertEquals(result, true, "订单" + order_id + "商品装箱失败");

			// 根据已装箱进行搜索
			bolParam = new BoxingOrderParam();
			bolParam.setOrder_id(order_id);
			bolParam.setSku_box_status("1");
			bolParam.setSearch("");

			bodBean = boxingAppService.getBoxingOrderDetail(bolParam);
			Assert.assertNotEquals(bodBean, null, "订单详情接口返回为空");
			Assert.assertEquals(bodBean.getDetails().size() != 0, true,
					"订单" + order_id + " 实际存在已装箱商品数据,按照已装箱搜索订单返回数据为空");

			boDetailBean = bodBean.getDetails().stream().filter(s -> s.getSku_box_status().equals("1")).findAny()
					.orElse(null);
			Assert.assertNotEquals(boDetailBean, null, "按已装箱搜索搜索结果存在不是已装箱的商品");

		} catch (Exception e) {
			logger.error("装箱app订单详情根据已装箱搜索遇到错误", e);
			Assert.fail("装箱app订单详情根据已装箱搜索遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase012() {
		ReporterCSS.title("测试点:修改订单为集包状态");
		try {
			// 搜索为未集包状态的订单
			BoxingOrderParam boParam = new BoxingOrderParam();
			boParam.setTime_config_id(time_config_id);
			boParam.setOrder_box_status("0");
			boParam.setDate(date);
			boParam.setSearch("");

			BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(boParam);
			Assert.assertNotEquals(bolBean, null, "装箱app接口返回遇到错误");
			Assert.assertEquals(bolBean.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			BoxingOrderListBean.Orders orders = bolBean.getOrders().stream()
					.filter(s -> s.getOrder_box_status().equals("0")).findAny().orElse(null);
			Assert.assertNotEquals(orders, null, "没有找到未集包的订单");

			String order_id = orders.getOrder_id();
			boParam = new BoxingOrderParam();
			boParam.setOrder_id(order_id);
			boParam.setOrder_box_status("1");

			// 集包
			boolean result = boxingAppService.updateBoxingOrderStatus(boParam);
			Assert.assertEquals(result, true, "修改订单集包状态为已集包遇到错误");

			boParam = new BoxingOrderParam();
			boParam.setOrder_id(order_id);
			boParam.setSearch("");

			BoxingOrderDetailBean boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boParam);
			Assert.assertNotEquals(boxingOrderDetailBean, null, "装箱app详情接口返回错误");
			boolean result_status = boxingOrderDetailBean.getOrder_box_status().equals("1");
			Assert.assertEquals(result_status, true, "订单" + order_id + "不是已集包状态");

			// 取消集包
			boParam = new BoxingOrderParam();
			boParam.setOrder_id(order_id);
			boParam.setOrder_box_status("0");

			boolean result_ = boxingAppService.updateBoxingOrderStatus(boParam);
			Assert.assertEquals(result_, true, "取消订单集包状态为已集包遇到错误");

			boParam = new BoxingOrderParam();
			boParam.setOrder_id(order_id);
			boParam.setSearch("");

			boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boParam);
			Assert.assertNotEquals(boxingOrderDetailBean, null, "装箱app详情接口返回错误");
			boolean result_status_ = boxingOrderDetailBean.getOrder_box_status().equals("0");
			Assert.assertEquals(result_status_, true, "订单" + order_id + "不是取消集包状态");
		} catch (Exception e) {
			logger.error("查询装箱订单详情遇到错误", e);
			Assert.fail("查询装箱订单详情遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase013() {
		ReporterCSS.title("测试点:创建箱子并将商品装箱-单箱操作");
		try {
			// 搜索为未集包状态的订单
			BoxingOrderParam boParam = new BoxingOrderParam();
			boParam.setTime_config_id(time_config_id);
			boParam.setOrder_box_status("0");
			boParam.setDate(date);
			boParam.setSearch("");

			BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(boParam);
			Assert.assertNotEquals(bolBean, null, "装箱app接口返回遇到错误");
			Assert.assertEquals(bolBean.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			BoxingOrderListBean.Orders orders = bolBean.getOrders().stream()
					.filter(s -> s.getOrder_box_num().equals("0")).findAny().orElse(null);
			Assert.assertNotEquals(orders, null, "装箱app列表没有箱子数为0的订单");

			String order_id = orders.getOrder_id();
			boParam = new BoxingOrderParam();
			boParam.setOrder_id(order_id);
			boParam.setSearch("");

			BoxingOrderDetailBean boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boParam);
			Assert.assertNotEquals(boxingOrderDetailBean, null, "装箱app详情接口返回错误");
			boolean result_status = boxingOrderDetailBean.getOrder_box_status().equals("1");
			if (result_status == true) {
				// 取消集包
				boParam = new BoxingOrderParam();
				boParam.setOrder_id(order_id);
				boParam.setOrder_box_status("0");

				boolean result_ = boxingAppService.updateBoxingOrderStatus(boParam);
				Assert.assertEquals(result_, true, "取消订单集包状态为已集包遇到错误");
			}

			// 取商品编号
			List<String> package_ids = new ArrayList<>();
			List<BoxingOrderDetailBean.Details> details = boxingOrderDetailBean.getDetails();

			package_ids.add(NumberUtil.roundNumberInList(details).getPackage_id());

			// 创建箱子
			String box_id = boxingAppService.createBox();
			Assert.assertNotEquals(box_id, null, "箱子创建过程中遇到错误");

			// 将商品装入箱子
			boolean result = boxingAppService.packageInBox(package_ids.subList(0, 1), box_id);
			Assert.assertEquals(result, true, "订单" + order_id + "商品装箱失败");

			// 检查是否装箱
			String package_id = NumberUtil.roundNumberInList(package_ids);
			boParam = new BoxingOrderParam();
			boParam.setOrder_id(order_id);
			boParam.setSearch(package_id);

			// 验证装箱
			boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boParam);
			Assert.assertNotEquals(boxingOrderDetailBean, null, "装箱app详情接口返回错误");
			List<BoxingOrderDetailBean.Details> sku_details = boxingOrderDetailBean.getDetails();
			boolean result_sku_box_status = NumberUtil.roundNumberInList(sku_details).getSku_box_status().equals("1");
			Assert.assertEquals(result_sku_box_status, true, "商品已装箱返回结果不是已装箱状态");

			List<BoxingOrderDetailBean.Details.Box_list> box_lists = NumberUtil.roundNumberInList(sku_details)
					.getBox_list();
			String box_id_sku = NumberUtil.roundNumberInList(box_lists).getBox_id();

			// 取消装箱
			JSONArray box_ids = new JSONArray();
			box_ids.add(box_id_sku);

			BoxParam boxParam = new BoxParam();
			boxParam.setBox_ids(box_ids);
			boxParam.setPackage_id(package_id);

			boolean out_box_result = boxingAppService.packageOutBox(boxParam);
			Assert.assertEquals(out_box_result, true, "取消装箱接口遇到错误");

			boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boParam);
			Assert.assertNotEquals(boxingOrderDetailBean, null, "装箱app详情接口返回错误");
			sku_details = boxingOrderDetailBean.getDetails();

			boolean result_ = true;
			for (BoxingOrderDetailBean.Details details_list : sku_details) {
				BoxingOrderDetailBean.Details.Box_list box = details_list.getBox_list().stream()
						.filter(s -> s.getBox_id().equals(box_id)).findAny().orElse(null);

				if (box != null) {
					result_ = false;
				}
			}
			Assert.assertEquals(result_, true, "商品没有正确移除箱子");
		} catch (Exception e) {
			logger.error("查询装箱订单详情遇到错误", e);
			Assert.fail("查询装箱订单详情遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase014() {
		ReporterCSS.title("测试点:创建箱子并将商品装箱-多箱操作");
		try {
			// 存储箱子,验证多箱是否装箱成功
			List<String> box_list = new ArrayList<>();
			// 搜索为未集包状态的订单
			BoxingOrderParam boParam = new BoxingOrderParam();
			boParam.setTime_config_id(time_config_id);
			boParam.setOrder_box_status("0");
			boParam.setDate(date);
			boParam.setSearch("");

			BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(boParam);
			Assert.assertNotEquals(bolBean, null, "装箱app接口返回遇到错误");
			Assert.assertEquals(bolBean.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			BoxingOrderListBean.Orders orders = bolBean.getOrders().stream()
					.filter(s -> s.getOrder_box_num().equals("0")).findAny().orElse(null);
			Assert.assertNotEquals(orders, null, "装箱app列表没有箱子数为0的订单");

			String order_id = orders.getOrder_id();
			boParam = new BoxingOrderParam();
			boParam.setOrder_id(order_id);
			boParam.setSearch("");

			BoxingOrderDetailBean boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boParam);
			Assert.assertNotEquals(boxingOrderDetailBean, null, "装箱app订单详情接口返回错误");
			boolean result_status = boxingOrderDetailBean.getOrder_box_status().equals("1");
			if (result_status == true) {
				// 取消集包
				boParam = new BoxingOrderParam();
				boParam.setOrder_id(order_id);
				boParam.setOrder_box_status("0");

				boolean result_ = boxingAppService.updateBoxingOrderStatus(boParam);
				Assert.assertEquals(result_, true, "取消订单集包状态为已集包遇到错误");
			}

			// 取商品编号
			List<String> package_ids = new ArrayList<>();
			List<BoxingOrderDetailBean.Details> details = boxingOrderDetailBean.getDetails();
			for (int i = 0; i < details.size(); i++) {
				package_ids.add(details.get(i).getPackage_id());
			}

			String box_id_one = boxingAppService.createBox();
			Assert.assertNotEquals(box_id_one, null, "箱子创建过程中遇到错误");
			box_list.add(box_id_one);

			// 将商品装入箱子
			boolean result = boxingAppService.packageInBox(package_ids, box_id_one);
			Assert.assertEquals(result, true, "订单" + order_id + "商品装箱失败");

			// 验证全部商品都存在箱子
			boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boParam);
			Assert.assertNotEquals(boxingOrderDetailBean, null, "装箱app订单详情接口返回错误");

			List<BoxingOrderDetailBean.Details> sku_details = boxingOrderDetailBean.getDetails();

			for (BoxingOrderDetailBean.Details detail : sku_details) {
				BoxingOrderDetailBean.Details.Box_list box = detail.getBox_list().stream()
						.filter(s -> s.getBox_id().equals(box_id_one)).findAny().orElse(null);
				Assert.assertNotEquals(box, null, "sku没有装箱成功,无法找到存在的箱子" + box_id_one);
			}

			// 第二次装箱,生成新箱,一个商品装多箱
			Random random = new Random();
			int rand_number = random.nextInt(2) + 3;
			for (int i = 0; i < rand_number; i++) {
				String box_id_ = boxingAppService.createBox();
				Assert.assertNotEquals(box_id_, null, "箱子创建过程中遇到错误");
				box_list.add(box_id_);

				// 将商品装入箱子
				boolean result_ = boxingAppService.packageInBox(package_ids.subList(0, 1), box_id_);
				Assert.assertEquals(result_, true, "订单" + order_id + "商品装箱失败");
			}

			// 验证sku是否装入多个箱子
			String package_id = package_ids.get(0);
			boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boParam);
			BoxingOrderDetailBean.Details detailBean = boxingOrderDetailBean.getDetails().stream()
					.filter(s -> s.getPackage_id().equals(package_id)).findAny().orElse(null);
			Assert.assertNotEquals(detailBean, null, "订单：" + order_id + "中没有找到package_id为" + package_id + "商品");

			// 商品中多箱的个数和添加的个数一致
			List<String> sku_box_list = new ArrayList<>();
			List<BoxingOrderDetailBean.Details.Box_list> box_lists = detailBean.getBox_list();
			for (BoxingOrderDetailBean.Details.Box_list box : box_lists) {
				sku_box_list.add(box.getBox_id());
			}

			boolean result_ = true;
			for (String box_ : box_list) {
				if (!sku_box_list.contains(box_)) {
					result_ = false;
				}
			}
			Assert.assertEquals(result_, true);

			// 移出多箱
			JSONArray box_ids = new JSONArray();
			for (String box_id : sku_box_list) {
				box_ids.add(box_id);
			}

			BoxParam boxParam = new BoxParam();
			boxParam.setPackage_id(package_id);
			boxParam.setBox_ids(box_ids);

			boolean result_out_box = boxingAppService.packageOutBox(boxParam);
			Assert.assertEquals(result_out_box, true, "移除多箱发生错误");

			// 验证
			boParam.setSearch(package_id);
			BoxingOrderDetailBean boDetailBean = boxingAppService.getBoxingOrderDetail(boParam);
			int box_size = boDetailBean.getDetails().get(0).getBox_list().size();
			Assert.assertEquals(box_size == 0, true, order_id + " " + package_id + "商品箱子数不为0");
		} catch (Exception e) {
			logger.error("进行多箱操作遇到错误", e);
			Assert.fail("进行多箱操作遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase015() {
		ReporterCSS.title("测试点:商品更换箱子");
		try {
			// 搜索为未集包状态的订单
			BoxingOrderParam boParam = new BoxingOrderParam();
			boParam.setTime_config_id(time_config_id);
			boParam.setOrder_box_status("0");
			boParam.setDate(date);
			boParam.setSearch("");

			BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(boParam);
			Assert.assertNotEquals(bolBean, null, "装箱app接口返回遇到错误");
			Assert.assertEquals(bolBean.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			List<String> box_id_list = new ArrayList<>();

			BoxingOrderParam boxingOrderParam = new BoxingOrderParam();
			boxingOrderParam.setOrder_id(order_id);
			boxingOrderParam.setSearch("");
			BoxingOrderDetailBean boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boxingOrderParam);
			List<BoxingOrderDetailBean.Details> sku_details = boxingOrderDetailBean.getDetails();

			String package_id = NumberUtil.roundNumberInList(sku_details).getPackage_id();
			List<String> package_ids = new ArrayList<String>();
			package_ids.add(package_id);
			// 创建箱子
			Random random = new Random();
			int rand_number = random.nextInt(2) + 3;
			for (int i = 0; i < rand_number; i++) {
				String box_id_ = boxingAppService.createBox();
				Assert.assertNotEquals(box_id_, null, "箱子创建过程中遇到错误");
				box_id_list.add(box_id_);

				boolean result = boxingAppService.packageInBox(package_ids, box_id_);
				Assert.assertEquals(result, true, "商品装箱失败");
			}

			// 查看订单关联箱子
			List<BoxBean> boxBeans = boxingAppService.getBoxDetails(order_id);
			Assert.assertNotEquals(boxBeans, null, "订单关联箱子接口返回错误");

			// 移除一个箱子,为更换箱子坐准备
			String out_box_id = NumberUtil.roundNumberInList(box_id_list);
			JSONArray box_ids = new JSONArray();
			box_ids.add(out_box_id);
			BoxParam boxParam = new BoxParam();
			boxParam.setBox_ids(box_ids);
			boxParam.setPackage_id(package_id);
			boolean out_result = boxingAppService.packageOutBox(boxParam);
			Assert.assertEquals(out_result, true, "商品移除箱子失败");
			boxingOrderParam.setOrder_id(order_id);
			boxingOrderParam.setSearch("");
			boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boxingOrderParam);
			Assert.assertNotEquals(boxingOrderDetailBean, null, "获取装箱app订单商品详情遇到错误");
			BoxingOrderDetailBean.Details sku_detail = boxingOrderDetailBean.getDetails().stream()
					.filter(s -> s.getPackage_id().equals(package_id)).findAny().orElse(null);
			Assert.assertNotEquals(sku_detail, null, "没有找到移除箱子的商品");

			BoxingOrderDetailBean.Details.Box_list box = sku_detail.getBox_list().stream()
					.filter(s -> s.getBox_id().equals(out_box_id)).findAny().orElse(null);
			Assert.assertEquals(box, null, "箱子没有移除成功,还存在再商品箱子列表");

			List<String> new_box_ids = new ArrayList<>();// 存储商品的箱子id
			List<BoxingOrderDetailBean.Details.Box_list> box_lists = boxingOrderDetailBean.getDetails().get(rand_number)
					.getBox_list();
			Assert.assertEquals(box_id_list.size() != 0, true, "商品关联箱子数为0,与实际不符合");

			for (BoxingOrderDetailBean.Details.Box_list boxBean : box_lists) {
				new_box_ids.add(boxBean.getBox_id());
			}

			// 随机取商品的一个箱子id
			String change_box_id = NumberUtil.roundNumberInList(new_box_ids);

			BoxParam change_boxParam = new BoxParam();
			change_boxParam.setPackage_id(package_id);
			change_boxParam.setOld_box_id(change_box_id);
			change_boxParam.setBox_id(out_box_id);

			boolean change_result = boxingAppService.packageChangeBox(change_boxParam);
			Assert.assertEquals(change_result, true, "商品更换箱子失败");

			boxingOrderParam.setOrder_id(order_id);
			boxingOrderParam.setSearch(package_id);
			boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boxingOrderParam);
			Assert.assertNotEquals(boxingOrderDetailBean, null, "获取装箱app订单商品详情遇到错误");

			List<BoxingOrderDetailBean.Details.Box_list> box_list = boxingOrderDetailBean.getDetails().get(0)
					.getBox_list();

			BoxingOrderDetailBean.Details.Box_list box_id_change = box_list.stream()
					.filter(s -> s.getBox_id().equals(out_box_id)).findAny().orElse(null);
			Assert.assertNotEquals(box_id_change, null, "没有找到更换的箱子");

			BoxingOrderDetailBean.Details.Box_list box_id_out = box_list.stream()
					.filter(s -> s.getBox_id().equals(change_box_id)).findAny().orElse(null);
			Assert.assertEquals(box_id_out, null, "箱子没有移除成功");
		} catch (Exception e) {
			logger.error("商品更换箱子遇到错误");
			Assert.fail("商品更换箱子遇到错误");
		}
	}

	@Test
	public void boxingAppTestCase016() {
		ReporterCSS.title("测试点:订单列表扫码接口");
		try {
			BoxingOrderParam boParam = new BoxingOrderParam(time_config_id, "0", date, "");
			BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(boParam);
			Assert.assertNotEquals(bolBean, null, "装箱app接口返回遇到错误");
			Assert.assertEquals(bolBean.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			BoxingOrderListBean.Orders orders = bolBean.getOrders().stream()
					.filter(s -> s.getOrder_box_num().equals("0")).findAny().orElse(null);
			Assert.assertNotEquals(orders, null, "装箱app列表没有箱子数为0的订单");

			String order_id = orders.getOrder_id();
			boParam = new BoxingOrderParam();
			boParam.setOrder_id(order_id);
			boParam.setSearch("");

			BoxingOrderDetailBean boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boParam);
			Assert.assertNotEquals(boxingOrderDetailBean, null, "装箱app详情接口返回错误");
			boolean result_status = boxingOrderDetailBean.getOrder_box_status().equals("1");
			if (result_status == true) {
				// 取消集包
				boParam = new BoxingOrderParam();
				boParam.setOrder_id(order_id);
				boParam.setOrder_box_status("0");

				boolean result_ = boxingAppService.updateBoxingOrderStatus(boParam);
				Assert.assertEquals(result_, true, "取消订单集包状态为已集包遇到错误");
			}

			// 取商品编号
			List<String> package_ids = new ArrayList<>();
			List<BoxingOrderDetailBean.Details> details = boxingOrderDetailBean.getDetails();
			int rand_number = new Random().nextInt(details.size());
			String package_id = details.get(rand_number).getPackage_id();
			package_ids.add(package_id);

			// 创建箱子
			String box_id = boxingAppService.createBox();
			Assert.assertNotEquals(box_id, null, "箱子创建过程中遇到错误");

			// 将商品装入箱子
			boolean result = boxingAppService.packageInBox(package_ids.subList(0, 1), box_id);
			Assert.assertEquals(result, true, "订单" + order_id + "商品装箱失败");

			// 返回订单关联的箱子接口,获取箱签码box_code
			List<BoxBean> boxBeans = boxingAppService.getBoxDetails(order_id);
			Assert.assertNotEquals(boxBeans, null, "获取订单关联箱子返回错误");
			BoxBean boxBean = boxBeans.stream().filter(s -> s.getBox_id().equals(box_id)).findAny().orElse(null);
			String box_code = boxBean.getBox_code();

			// 根据箱签码搜索订单
			String verify_order_id = boxingAppService.searchBoxBarCode(box_code, time_config_id, date);
			Assert.assertNotEquals(verify_order_id, null, "根据箱签码搜索订单信息接口发生错误");
			Assert.assertEquals(verify_order_id.equals(order_id), true, "箱签码返回结果错误");

		} catch (Exception e) {
			logger.error("订单列表扫码接口遇到错误", e);
			Assert.fail("订单列表扫码接口遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase017() {
		ReporterCSS.title("测试点:订单详情商品码扫码接口");
		try {
			// 搜索为未集包状态的订单
			BoxingOrderParam boParam = new BoxingOrderParam(time_config_id, "0", date, "");
			BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(boParam);
			Assert.assertNotEquals(bolBean, null, "装箱app接口返回遇到错误");
			Assert.assertEquals(bolBean.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			BoxingOrderParam boxingOrderParam = new BoxingOrderParam();
			boxingOrderParam.setOrder_id(order_id);
			boxingOrderParam.setSearch("");
			BoxingOrderDetailBean boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boxingOrderParam);
			List<BoxingOrderDetailBean.Details> sku_details = boxingOrderDetailBean.getDetails();

			String package_id = NumberUtil.roundNumberInList(sku_details).getPackage_id();

			String package_id_search = boxingAppService.searchBoxPackageId(package_id);
			Assert.assertNotEquals(package_id_search, null, "订单详情扫码接口遇到错误");

			Assert.assertTrue(package_id.equals(package_id_search), "输入扫码商品id和扫码返回接口不一致");
		} catch (Exception e) {
			logger.error("订单详情商品码扫码接口遇到错误", e);
			Assert.fail("订单详情商品码扫码接口遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase018() {
		ReporterCSS.title("测试点:装箱管理获取订单列表");
		try {
			BoxingManageOrderParam bmOrderParam = new BoxingManageOrderParam();
			bmOrderParam.setTime_config_id(time_config_id);
			bmOrderParam.setBegin(begin_time);
			bmOrderParam.setEnd(end_time);
			bmOrderParam.setSearch("");
			bmOrderParam.setLimit("10");
			bmOrderParam.setOffset(0);

			List<BoxingOrderDetailBean> boxingOrderDetails = boxingAppService.getBoxingManageOrderList(bmOrderParam);
			Assert.assertNotEquals(boxingOrderDetails, null, "装箱管理接口数据返回为空");
			Assert.assertEquals(boxingOrderDetails.size() != 0, true, "装箱管理存在数据,后台没有返回");

		} catch (Exception e) {
			logger.error("装箱管理获取订单列表接口遇到错误", e);
			Assert.fail("装箱管理获取订单列表接口遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase019() {
		ReporterCSS.title("测试点:根据订单id搜索");
		try {
			BoxingManageOrderParam boParam = new BoxingManageOrderParam();
			boParam.setTime_config_id(time_config_id);
			boParam.setBegin(begin_time);
			boParam.setEnd(end_time);
			boParam.setSearch(order_id);
			boParam.setLimit("10");
			boParam.setOffset(0);

			List<BoxingOrderDetailBean> boxingOrderDetails = boxingAppService.getBoxingManageOrderList(boParam);
			Assert.assertNotEquals(boxingOrderDetails, null, "装箱管理接口数据返回为空");
			Assert.assertEquals(boxingOrderDetails.size() != 0, true, "装箱管理存在数据,后台没有返回");

			// 验证
			BoxingOrderDetailBean boxingOrderDetail = boxingOrderDetails.stream()
					.filter(s -> s.getOrder_id().equals(order_id)).findAny().orElse(null);

			Assert.assertNotEquals(boxingOrderDetail, null,
					"根据运营时间" + time_config_id + " " + begin_time + " " + end_time + "没有搜索到订单" + order_id);
		} catch (Exception e) {
			logger.error("", e);
			Assert.fail("", e);
		}
	}

	@Test
	public void boxingAppTestCase020() {
		ReporterCSS.title("测试点:装箱管理获取订单列表");
		try {
			BoxingOrderParam boParam = new BoxingOrderParam();
			boParam.setTime_config_id(time_config_id);
			boParam.setOrder_box_status("0");
			boParam.setDate(date);
			boParam.setSearch("");

			BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(boParam);
			Assert.assertNotEquals(bolBean, null, "装箱app接口返回遇到错误");
			Assert.assertEquals(bolBean.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			BoxingOrderListBean.Orders orders = bolBean.getOrders().stream()
					.filter(s -> s.getOrder_box_num().equals("0")).findAny().orElse(null);
			Assert.assertNotEquals(orders, null, "装箱app列表没有箱子数为0的订单");

			String order_id = orders.getOrder_id();
			boParam = new BoxingOrderParam();
			boParam.setOrder_id(order_id);
			boParam.setSearch("");

			BoxingOrderDetailBean boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boParam);
			Assert.assertNotEquals(boxingOrderDetailBean, null, "装箱app详情接口返回错误");
			boolean result_status = boxingOrderDetailBean.getOrder_box_status().equals("1");
			if (result_status == true) {
				// 取消集包
				boParam = new BoxingOrderParam();
				boParam.setOrder_id(order_id);
				boParam.setOrder_box_status("0");

				boolean result_ = boxingAppService.updateBoxingOrderStatus(boParam);
				Assert.assertEquals(result_, true, "取消订单集包状态为已集包遇到错误");
			}

			// 取商品编号
			List<String> package_ids = new ArrayList<>();
			List<BoxingOrderDetailBean.Details> details = boxingOrderDetailBean.getDetails();
			int rand_number = new Random().nextInt(details.size());
			String package_id = details.get(rand_number).getPackage_id();
			String sku_id = details.get(rand_number).getSku_id();
			package_ids.add(package_id);

			// 创建箱子
			String box_id = boxingAppService.createBox();
			Assert.assertNotEquals(box_id, null, "箱子创建过程中遇到错误");

			// 将商品装入箱子
			boolean result = boxingAppService.packageInBox(package_ids.subList(0, 1), box_id);
			Assert.assertEquals(result, true, "订单" + order_id + "商品装箱失败");

			List<BoxBean> boxBeans = boxingAppService.getBoxDetails(order_id);
			Assert.assertNotEquals(boxBeans, null, "获取订单关联箱子返回错误");

			BoxBean boxBean = boxBeans.stream().filter(s -> s.getBox_id().equals(box_id)).findAny().orElse(null);
			String box_code = boxBean.getBox_code();

			BoxingManageOrderParam bmoParam = new BoxingManageOrderParam();
			bmoParam.setTime_config_id(time_config_id);
			bmoParam.setBegin(begin_time);
			bmoParam.setEnd(end_time);
			bmoParam.setSearch(box_code);
			bmoParam.setLimit("10");
			bmoParam.setOffset(0);

			List<BoxingOrderDetailBean> boxingOrderDetails = boxingAppService.getBoxingManageOrderList(bmoParam);
			Assert.assertNotEquals(boxingOrderDetails, null, "装箱管理接口数据返回为空");
			Assert.assertEquals(boxingOrderDetails.size() != 0, true, "装箱管理存在数据,后台没有返回");

			// 验证
			BoxingOrderDetailBean boxingOrderDetail = boxingOrderDetails.stream()
					.filter(s -> s.getOrder_id().equals(order_id)).findAny().orElse(null);

			Assert.assertNotEquals(boxingOrderDetail, null,
					"根据运营时间" + time_config_id + " " + begin_time + " " + end_time + "没有搜索到订单" + order_id);

			BoxingOrderDetailBean.Details detail = boxingOrderDetail.getDetails().stream()
					.filter(s -> s.getSku_id().equals(sku_id)).findAny().orElse(null);
			Assert.assertNotEquals(detail, null, order_id + "没有找到对应的商品:" + sku_id);

			BoxingOrderDetailBean.Details.Box_list box = detail.getBox_list().stream()
					.filter(s -> s.getBox_id().equals(box_id)).findAny().orElse(null);

			Assert.assertNotEquals(box, null, sku_id + "已装箱" + box_code + "在订单中没有找到");
		} catch (Exception e) {
			logger.error("", e);
			Assert.fail("", e);
		}
	}

	@Test
	public void boxingAppTestCase021() {
		ReporterCSS.title("测试点:装箱管理导出接口");
		try {
			BoxingManageOrderParam bmoParam = new BoxingManageOrderParam();
			bmoParam.setTime_config_id(time_config_id);
			bmoParam.setBegin(begin_time);
			bmoParam.setEnd(end_time);
			bmoParam.setSearch(order_id);
			bmoParam.setLimit("10");
			bmoParam.setOffset(0);
			bmoParam.setExport(1);

			String result = boxingAppService.exportBoxingManageOrderList(bmoParam);
			Assert.assertNotEquals(result, null, "装箱管理同步导出结果发生错误");

		} catch (Exception e) {
			logger.error("装箱管理同步导出接口遇到错误", e);
			Assert.fail("装箱管理同步导出接口遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase022() {
		ReporterCSS.title("测试点:模拟装箱管理三个月导出接口");
		try {
			// 获取三个月内的装箱管理订单
			String begin = TimeUtil.calculateTime("yyyy-MM-dd HH:mm:ss", begin_time, -2, Calendar.MONTH);
			String end = TimeUtil.calculateTime("yyyy-MM-dd HH:mm:ss", begin_time, 1, Calendar.MONTH);

			BoxingManageOrderParam bmoParam = new BoxingManageOrderParam();
			bmoParam.setTime_config_id(time_config_id);
			bmoParam.setBegin(begin);
			bmoParam.setEnd(end);
			bmoParam.setSearch("");
			bmoParam.setLimit("10");
			bmoParam.setOffset(0);
			bmoParam.setExport(1);

			String result = boxingAppService.exportBoxingManageOrderList(bmoParam);
			Assert.assertNotEquals(result, null, "装箱管理异步导出结果发生错误,请检查异步导出任务");
		} catch (Exception e) {
			logger.error("装箱管理异步导出接口遇到错误", e);
			Assert.fail("装箱管理异步导出接口遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase023() {
		ReporterCSS.title("测试点:单个订单打印箱子");
		try {
			// 搜索为未集包状态的订单
			BoxingOrderParam boParam = new BoxingOrderParam();
			boParam.setTime_config_id(time_config_id);
			boParam.setOrder_box_status("0");
			boParam.setDate(date);
			boParam.setSearch("");

			BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(boParam);
			Assert.assertNotEquals(bolBean, null, "装箱app接口返回遇到错误");
			Assert.assertEquals(bolBean.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			BoxingOrderListBean.Orders orders = bolBean.getOrders().stream()
					.filter(s -> s.getOrder_box_num().equals("0")).findAny().orElse(null);
			Assert.assertNotEquals(orders, null, "装箱app列表没有箱子数为0的订单");

			String order_id = orders.getOrder_id();
			boParam = new BoxingOrderParam();
			boParam.setOrder_id(order_id);
			boParam.setSearch("");

			BoxingOrderDetailBean boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boParam);
			Assert.assertNotEquals(boxingOrderDetailBean, null, "装箱app订单详情接口返回错误");
			boolean result_status = boxingOrderDetailBean.getOrder_box_status().equals("1");
			if (result_status == true) {
				// 取消集包
				boParam = new BoxingOrderParam();
				boParam.setOrder_id(order_id);
				boParam.setOrder_box_status("0");

				boolean result_ = boxingAppService.updateBoxingOrderStatus(boParam);
				Assert.assertEquals(result_, true, "取消订单集包状态为已集包遇到错误");
			}

			// 取商品编号
			List<String> package_ids = new ArrayList<>();
			List<BoxingOrderDetailBean.Details> details = boxingOrderDetailBean.getDetails();
			for (int i = 0; i < details.size(); i++) {
				package_ids.add(details.get(i).getPackage_id());
			}

			String create_box_id = boxingAppService.createBox();
			Assert.assertNotEquals(create_box_id, null, "箱子创建过程中遇到错误");

			// 将商品装入箱子
			boolean result = boxingAppService.packageInBox(package_ids, create_box_id);
			Assert.assertEquals(result, true, "订单" + order_id + "商品装箱失败");

			// 验证全部商品都存在箱子
			boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boParam);
			Assert.assertNotEquals(boxingOrderDetailBean, null, "装箱app订单详情接口返回错误");

			List<BoxingOrderDetailBean.Details> sku_details = boxingOrderDetailBean.getDetails();

			for (BoxingOrderDetailBean.Details detail : sku_details) {
				BoxingOrderDetailBean.Details.Box_list box = detail.getBox_list().stream()
						.filter(s -> s.getBox_id().equals(create_box_id)).findAny().orElse(null);
				Assert.assertNotEquals(box, null, "sku没有装箱成功,无法找到存在的箱子" + create_box_id);
			}

			// 打印该订单的箱子
			JSONArray order_ids = new JSONArray();
			order_ids.add(order_id);
			BoxingManagePrintParam bmPrintParam = new BoxingManagePrintParam();
			bmPrintParam.setOrder_ids(order_ids);
			bmPrintParam.setForce_print("1");

			List<BoxingManagePrintDetailBean> bmPrintDetails = boxingAppService.printBoxingManageOrder(bmPrintParam);

			Assert.assertNotEquals(bmPrintDetails, null, "装箱管理操作单个订单打印发生错误");

			BoxingManagePrintDetailBean bmPrintDetail = bmPrintDetails.stream()
					.filter(s -> s.getBox_id().equals(create_box_id)).findAny().orElse(null);
			Assert.assertNotEquals(bmPrintDetail, null, "单个订单打印接口没有返回正确的箱子信息");

		} catch (Exception e) {
			logger.error("单个订单打印箱子遇到错误", e);
			Assert.fail("单个订单打印箱子遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase024() {
		ReporterCSS.title("测试点:装箱管理小规模批量打印");
		try {
			// 存储箱子,批量打印
			List<String> box_list = new ArrayList<>();
			// 搜索为未集包状态的订单
			BoxingOrderParam boParam = new BoxingOrderParam();
			boParam.setTime_config_id(time_config_id);
			boParam.setOrder_box_status("0");
			boParam.setDate(date);
			boParam.setSearch("");

			BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(boParam);
			Assert.assertNotEquals(bolBean, null, "装箱app接口返回遇到错误");
			Assert.assertEquals(bolBean.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			BoxingOrderListBean.Orders orders = bolBean.getOrders().stream()
					.filter(s -> s.getOrder_box_num().equals("0")).findAny().orElse(null);
			Assert.assertNotEquals(orders, null, "装箱app列表没有箱子数为0的订单");

			String order_id = orders.getOrder_id();
			boParam = new BoxingOrderParam();
			boParam.setOrder_id(order_id);
			boParam.setSearch("");

			BoxingOrderDetailBean boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boParam);
			Assert.assertNotEquals(boxingOrderDetailBean, null, "装箱app订单详情接口返回错误");
			boolean result_status = boxingOrderDetailBean.getOrder_box_status().equals("1");
			if (result_status == true) {
				// 取消集包
				boParam = new BoxingOrderParam();
				boParam.setOrder_id(order_id);
				boParam.setOrder_box_status("0");

				boolean result_ = boxingAppService.updateBoxingOrderStatus(boParam);
				Assert.assertEquals(result_, true, "取消订单集包状态为已集包遇到错误");
			}

			// 取商品编号
			List<String> package_ids = new ArrayList<>();
			List<BoxingOrderDetailBean.Details> details = boxingOrderDetailBean.getDetails();
			for (int i = 0; i < details.size(); i++) {
				package_ids.add(details.get(i).getPackage_id());
			}

			String box_id_one = boxingAppService.createBox();
			Assert.assertNotEquals(box_id_one, null, "箱子创建过程中遇到错误");
			box_list.add(box_id_one);

			// 将商品装入箱子
			boolean result = boxingAppService.packageInBox(package_ids, box_id_one);
			Assert.assertEquals(result, true, "订单" + order_id + "商品装箱失败");

			// 验证全部商品都存在箱子
			boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boParam);
			Assert.assertNotEquals(boxingOrderDetailBean, null, "装箱app订单详情接口返回错误");

			List<BoxingOrderDetailBean.Details> sku_details = boxingOrderDetailBean.getDetails();

			for (BoxingOrderDetailBean.Details detail : sku_details) {
				BoxingOrderDetailBean.Details.Box_list box = detail.getBox_list().stream()
						.filter(s -> s.getBox_id().equals(box_id_one)).findAny().orElse(null);
				Assert.assertNotEquals(box, null, "sku没有装箱成功,无法找到存在的箱子" + box_id_one);
			}

			// 第二次装箱,生成新箱,一个商品装多箱
			int rand_number = new Random().nextInt(2) + 3;
			for (int i = 0; i < rand_number; i++) {
				String box_id_ = boxingAppService.createBox();
				Assert.assertNotEquals(box_id_, null, "箱子创建过程中遇到错误");
				box_list.add(box_id_);

				// 将商品装入箱子
				boolean result_ = boxingAppService.packageInBox(package_ids.subList(0, 1), box_id_);
				Assert.assertEquals(result_, true, "订单" + order_id + "商品装箱失败");
			}

			// 验证sku是否装入多个箱子
			String package_id = package_ids.get(0);
			boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boParam);
			BoxingOrderDetailBean.Details detailBean = boxingOrderDetailBean.getDetails().stream()
					.filter(s -> s.getPackage_id().equals(package_id)).findAny().orElse(null);
			Assert.assertNotEquals(detailBean, null, "订单：" + order_id + "中没有找到package_id为" + package_id + "商品");

			JSONArray box_ids = new JSONArray();
			for (String box_id : box_list) {
				box_ids.add(box_id);
			}

			BoxingManagePrintParam bmPrintParam = new BoxingManagePrintParam();
			bmPrintParam.setOrder_ids(box_ids);
			bmPrintParam.setForce_print("1");

			List<BoxingManagePrintDetailBean> bmPrintDetails = boxingAppService.printBoxingManageOrder(bmPrintParam);
			Assert.assertNotEquals(bmPrintDetails, null, "小批量接口打印遇到错误");

			boolean verify_box_id_result = true;
			for (BoxingManagePrintDetailBean bmPrintDetail : bmPrintDetails) {
				if (!box_list.contains(bmPrintDetail.getBox_id())) {
					ReporterCSS.warn("接口返回箱子" + bmPrintDetail.getBox_id() + "跟请求参数不符合");
					verify_box_id_result = false;
				}
			}
			Assert.assertEquals(verify_box_id_result, true, "箱子返回与预期不一致");
		} catch (Exception e) {
			logger.error("装箱管理小规模批量打印遇到错误", e);
			Assert.fail("装箱管理小规模批量打印遇到错误", e);
		}
	}

	@Test
	public void boxingAppTestCase025() {
		ReporterCSS.title("测试点: 装箱管理批量打印");
		try {
			// 没有箱子打印返回信息为空,先进行装箱
			BoxingOrderParam boParam = new BoxingOrderParam();
			boParam.setTime_config_id(time_config_id);
			boParam.setOrder_box_status("0");
			boParam.setDate(date);
			boParam.setSearch("");

			BoxingOrderListBean bolBean = boxingAppService.getBoxingOrderList(boParam);
			Assert.assertNotEquals(bolBean, null, "装箱app接口返回遇到错误");
			Assert.assertEquals(bolBean.getOrders().size() != 0, true,
					"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

			BoxingOrderListBean.Orders orders = bolBean.getOrders().stream()
					.filter(s -> s.getOrder_box_num().equals("0")).findAny().orElse(null);
			Assert.assertNotEquals(orders, null, "装箱app列表没有箱子数为0的订单");

			String order_id = orders.getOrder_id();
			boParam = new BoxingOrderParam();
			boParam.setOrder_id(order_id);
			boParam.setSearch("");

			BoxingOrderDetailBean boxingOrderDetailBean = boxingAppService.getBoxingOrderDetail(boParam);
			Assert.assertNotEquals(boxingOrderDetailBean, null, "装箱app订单详情接口返回错误");
			boolean result_status = boxingOrderDetailBean.getOrder_box_status().equals("1");
			if (result_status == true) {
				// 取消集包
				boParam = new BoxingOrderParam();
				boParam.setOrder_id(order_id);
				boParam.setOrder_box_status("0");

				boolean result_ = boxingAppService.updateBoxingOrderStatus(boParam);
				Assert.assertEquals(result_, true, "取消订单集包状态为已集包遇到错误");
			}

			// 取商品编号
			List<String> package_ids = new ArrayList<>();
			List<BoxingOrderDetailBean.Details> details = boxingOrderDetailBean.getDetails();
			for (int i = 0; i < details.size(); i++) {
				package_ids.add(details.get(i).getPackage_id());
			}

			String create_box_id = boxingAppService.createBox();
			Assert.assertNotEquals(create_box_id, null, "箱子创建过程中遇到错误");

			// 将商品装入箱子
			boolean result = boxingAppService.packageInBox(package_ids, create_box_id);
			Assert.assertEquals(result, true, "订单" + order_id + "商品装箱失败");

			// 打印
			BoxingManagePrintParam bmPrintParam = new BoxingManagePrintParam();
			bmPrintParam.setBegin(begin_time);
			bmPrintParam.setEnd(end_time);
			bmPrintParam.setTime_config_id(time_config_id);
			bmPrintParam.setForce_print("1");
			bmPrintParam.setSearch("");

			List<BoxingManagePrintDetailBean> bmPrintDetails = boxingAppService.printBoxingManageOrder(bmPrintParam);
			Assert.assertNotEquals(bmPrintDetails, null, "批量打印接口遇到错误");
			Assert.assertEquals(bmPrintDetails.size() == 0, false, "批量打印存在装箱数据,批量接口返回数据为0");

			// 验证
			for (BoxingManagePrintDetailBean bmPrintDetail : bmPrintDetails) {
				String verify_order_id = bmPrintDetail.getOrder_id();
				String verify_box_id = bmPrintDetail.getBox_id();
				List<BoxingManagePrintDetailBean.Details> verify_details = bmPrintDetail.getDetails();

				for (BoxingManagePrintDetailBean.Details detail : verify_details) {
					String sku_name = detail.getSku_name();

					BoxingOrderParam verify_boParam = new BoxingOrderParam();
					verify_boParam.setTime_config_id(time_config_id);
					verify_boParam.setOrder_box_status("0");
					verify_boParam.setDate(date);
					verify_boParam.setSearch("");

					BoxingOrderListBean verify_bolBean = boxingAppService.getBoxingOrderList(verify_boParam);
					Assert.assertNotEquals(verify_bolBean, null, "装箱app接口返回遇到错误");
					Assert.assertEquals(verify_bolBean.getOrders().size() != 0, true,
							"运营周期" + time_config_id + " " + date + " 存在数据,返回列表数据为空");

					verify_boParam = new BoxingOrderParam();
					verify_boParam.setOrder_id(verify_order_id);
					verify_boParam.setSearch(URLEncoder.encode(sku_name, "utf-8"));

					BoxingOrderDetailBean verify_boxingOrderDetailBean = boxingAppService
							.getBoxingOrderDetail(verify_boParam);
					Assert.assertNotEquals(verify_boxingOrderDetailBean, null, "装箱app订单详情接口返回错误");

					BoxingOrderDetailBean.Details verify_oderDetail = verify_boxingOrderDetailBean.getDetails().stream()
							.filter(s -> s.getSku_name().equals(sku_name)).findAny().orElse(null);
					Assert.assertNotEquals(orderDetail, null, "装箱app中没有找到打印接口中的商品");

					BoxingOrderDetailBean.Details.Box_list box = verify_oderDetail.getBox_list().stream()
							.filter(s -> s.getBox_id().equals(verify_box_id)).findAny().orElse(null);
					Assert.assertNotEquals(box, null, "打印接口箱子返回的商品在订单商品中没有找到");
				}
			}
		} catch (Exception e) {
			logger.error("装箱管理批量打印遇到错误", e);
			Assert.fail("装箱管理批量打印遇到错误", e);
		}
	}
}
