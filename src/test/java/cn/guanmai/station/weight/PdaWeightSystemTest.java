package cn.guanmai.station.weight;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.delivery.DistributeOrderDetailBean;
import cn.guanmai.station.bean.delivery.RouteBean;
import cn.guanmai.station.bean.invoicing.OutStockDetailBean;
import cn.guanmai.station.bean.order.OrderBean;
import cn.guanmai.station.bean.order.OrderCycle;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.param.OrderFilterParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.bean.weight.PdaOrderBean;
import cn.guanmai.station.bean.weight.PdaOrderDetailBean;
import cn.guanmai.station.bean.weight.PdaPatckageInfoBean;
import cn.guanmai.station.bean.weight.PdaWeightSkuBean;
import cn.guanmai.station.bean.weight.PdaWeightSkuDetailBean;
import cn.guanmai.station.bean.weight.PdaWeightSortDetailBean;
import cn.guanmai.station.bean.weight.PreSortingSkuBean;
import cn.guanmai.station.bean.weight.PreSortingSkuPackageBean;
import cn.guanmai.station.bean.weight.param.PdaOrderDetailParam;
import cn.guanmai.station.bean.weight.param.PdaOrderFilterParam;
import cn.guanmai.station.bean.weight.param.PdaSetWeightParam;
import cn.guanmai.station.bean.weight.param.PdaWeightSkuDetailFilterParam;
import cn.guanmai.station.impl.delivery.DistributeServiceImpl;
import cn.guanmai.station.impl.delivery.RouteServiceImpl;
import cn.guanmai.station.impl.invoicing.OutStockServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.impl.weight.WeightServiceImpl;
import cn.guanmai.station.interfaces.delivery.DistributeService;
import cn.guanmai.station.interfaces.delivery.RouteService;
import cn.guanmai.station.interfaces.invoicing.OutStockService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.interfaces.weight.WeightService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/**
 * @author liming
 * @date 2019年7月31日 下午7:22:15
 * @des PDA 分拣测试
 * @version 1.0
 */
public class PdaWeightSystemTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PdaWeightSystemTest.class);
	private LoginUserInfoService loginUserInfoService;
	private OrderService orderService;
	private OrderTool orderTool;
	private OrderDetailBean orderDetail;
	private WeightService weightService;
	private DistributeService distributeService;
	private OutStockService stockOutService;
	private RouteService routeService;

	private OrderCycle orderCycle;
	private String order_id;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		orderService = new OrderServiceImpl(headers);
		orderTool = new OrderTool(headers);
		weightService = new WeightServiceImpl(headers);
		distributeService = new DistributeServiceImpl(headers);
		stockOutService = new OutStockServiceImpl(headers);
		routeService = new RouteServiceImpl(headers);
		try {
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			JSONArray user_permission = loginUserInfo.getUser_permission();
			Assert.assertEquals(user_permission.contains("get_package"), true, "登录账号无预分拣权限,无法进行预分拣操作");

			order_id = orderTool.oneStepCreateOrder(6);
			Assert.assertNotEquals(order_id, null, "创建订单失败");
			boolean result = orderService.updateOrderState(order_id, 5);
			Assert.assertEquals(result, true, "订单 " + order_id + "状态改为分拣中,更新失败");
			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + "详细信息失败");

			orderCycle = orderTool.getOrderOperationCycle(order_id);
			Assert.assertNotEquals(orderCycle, null, "获取订单" + order_id + "所处运营周期信息失败");
		} catch (Exception e) {
			logger.error("前置操作,初始化数据遇到错误: ", e);
			Assert.fail("前置操作,初始化数据遇到错误: ", e);
		}
	}

	@Test
	public void pdaWeightSystemTestCase01() {
		ReporterCSS.title("测试点: 获取预分拣sku列表");
		List<String> spu_ids = orderDetail.getDetails().stream().map(d -> d.getSpu_id()).collect(Collectors.toList());
		try {
			List<PreSortingSkuBean> preSortingSkuList = weightService.getPreSortingSkuList(spu_ids, todayStr, todayStr);
			Assert.assertNotEquals(preSortingSkuList, null, "获取预分拣sku列表失败");

			Assert.assertEquals(preSortingSkuList.size() > 0, true, "拉取的预分拣sku列表大小为0,与预期不符");

			PreSortingSkuBean preSortingSku = preSortingSkuList.get(0);
			String sku_id = preSortingSku.getSku_id();
			List<String> sku_ids = preSortingSku.getSku_ids();

			PreSortingSkuPackageBean preSortingSkuPackage = weightService.getPreSortingSkuPackage(sku_id, sku_ids,
					todayStr, todayStr);
			Assert.assertNotEquals(preSortingSkuPackage, null, "获取指定sku " + sku_id + " 的预包装列表失败");
		} catch (Exception e) {
			logger.error("获取预分拣sku列表遇到错误: ", e);
			Assert.fail("获取预分拣sku列表遇到错误: ", e);
		}
	}

	@Test
	public void pdaWeightSystemTestCase02() {
		ReporterCSS.title("测试点: 生成预分拣包装");
		try {
			String sku_id = null;
			BigDecimal std_unit_quantity = null;
			List<String> package_ids = null;
			OrderDetailBean.Detail detail = null;
			List<OrderDetailBean.Detail> details = orderDetail.getDetails();
			Integer count = null;
			for (int i = 0; i < details.size(); i++) {
				detail = details.get(i);
				sku_id = detail.getSku_id();
				std_unit_quantity = detail.getStd_unit_quantity();
				if (i % 2 == 0) {
					count = null;
				} else {
					count = 1;
				}
				package_ids = weightService.createWeightPackage(sku_id, std_unit_quantity, count);
				Assert.assertNotEquals(package_ids, null, "生成预分拣包装失败");
			}
		} catch (Exception e) {
			logger.error("生成预分拣包装遇到错误: ", e);
			Assert.fail("生成预分拣包装遇到错误: ", e);
		}
	}

	@Test
	public void pdaWeightSystemTestCase03() {
		ReporterCSS.title("测试点: 搜索预分拣包装");
		try {
			OrderDetailBean.Detail detail = NumberUtil.roundNumberInList(orderDetail.getDetails());
			String spu_id = detail.getSpu_id();
			String sku_id = detail.getSku_id();
			BigDecimal std_unit_quantity = detail.getStd_unit_quantity();

			List<String> package_ids = weightService.createWeightPackage(sku_id, std_unit_quantity, null);
			Assert.assertEquals(package_ids != null && package_ids.size() > 0, true, "生成预分拣包装失败");

			PreSortingSkuPackageBean preSortingSkuPackage = weightService.searchPreSortingSkuPackage(package_ids.get(0),
					Arrays.asList(spu_id), todayStr, todayStr);

			Assert.assertNotEquals(preSortingSkuPackage, null, "搜索预分拣包装失败");

			List<String> temp_package_ids = preSortingSkuPackage.getPackages().stream().map(p -> p.getPackage_id())
					.collect(Collectors.toList());

			Assert.assertEquals(temp_package_ids.containsAll(package_ids), true, "搜索显示的预分拣包装列表中没有输入的预分拣包装");
		} catch (Exception e) {
			logger.error("生成预分拣包装遇到错误: ", e);
			Assert.fail("生成预分拣包装遇到错误: ", e);
		}
	}

	@Test
	public void pdaWeightSystemTestCase04() {
		ReporterCSS.title("测试点: 删除预分拣包装");
		try {
			OrderDetailBean.Detail detail = orderDetail.getDetails().get(0);
			String sku_id = detail.getSku_id();
			BigDecimal std_unit_quantity = detail.getStd_unit_quantity();

			List<String> package_ids = weightService.createWeightPackage(sku_id, std_unit_quantity, null);
			Assert.assertNotEquals(package_ids, null, "生成预分拣包装失败");

			boolean result = weightService.deleteWeightPackage(package_ids.get(0));
			Assert.assertEquals(result, true, "删除与分拣包装失败");
		} catch (Exception e) {
			logger.error("生成预分拣包装遇到错误: ", e);
			Assert.fail("生成预分拣包装遇到错误: ", e);
		}
	}

	@Test
	public void pdaWeightSystemTestCase05() {
		ReporterCSS.title("测试点: PDA分拣,输入包装编码查询包装信息");
		try {
			List<String> spu_ids = orderDetail.getDetails().stream().map(d -> d.getSpu_id())
					.collect(Collectors.toList());
			List<PreSortingSkuBean> preSortingSkuList = weightService.getPreSortingSkuList(spu_ids, todayStr, todayStr);
			Assert.assertNotEquals(preSortingSkuList, null, "获取预分拣sku列表失败");

			Assert.assertEquals(preSortingSkuList.size() > 0, true, "拉取的预分拣sku列表大小为0,与预期不符");

			PreSortingSkuBean preSortingSku = preSortingSkuList.get(0);
			String sku_id = preSortingSku.getSku_id();
			String sku_name = preSortingSku.getSku_name();
			List<String> sku_ids = preSortingSku.getSku_ids();

			List<String> package_ids = weightService.createWeightPackage(sku_id, new BigDecimal("2.1"), null);
			Assert.assertNotEquals(package_ids, null, "生成预分拣包装失败");

			Assert.assertEquals(package_ids.size() > 0, true, "生成与预分拣结果为空,请查验");

			String package_id = package_ids.get(0);

			PreSortingSkuPackageBean preSortingSkuPackage = weightService.getPreSortingSkuPackage(sku_id, sku_ids,
					todayStr, todayStr);
			Assert.assertNotEquals(preSortingSkuPackage, null, "获取指定sku " + sku_id + " 的预包装列表失败");

			Assert.assertEquals(preSortingSkuPackage.getPackages().size() > 0, true,
					"商品 [" + sku_id + "/" + sku_name + "] 无预分拣包装,与预期不符");

			PreSortingSkuPackageBean.PackageDetail packageDetail = preSortingSkuPackage.getPackages().stream()
					.filter(p -> p.getPackage_id().equals(package_id)).findAny().orElse(null);
			Assert.assertNotEquals(packageDetail, null, "上一步生成的预分拣包装在已打包列表中没有查询到");

			PdaPatckageInfoBean pdaPatckageInfo = weightService.searchPackageInPda(package_id);
			Assert.assertNotEquals(pdaPatckageInfo, null, "PDA分拣,输入包装编码查询包装信息失败");

			String msg = null;
			boolean result = true;
			if (!pdaPatckageInfo.getSku_id().equals(sku_id)) {
				msg = String.format("预分拣包装%s对应的sku id与预期不符,预期:%s,实际:%s", package_id, sku_id,
						pdaPatckageInfo.getSku_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (pdaPatckageInfo.getQuantity().compareTo(packageDetail.getQuantity()) != 0) {
				msg = String.format("预分拣包装%s对应的实称数量与预期不符,预期:%s,实际:%s", package_id, packageDetail.getQuantity(),
						pdaPatckageInfo.getQuantity());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "查询到的预分拣包装信息与预期不符");
		} catch (Exception e) {
			logger.error("PDA分拣,输入包装编码查询包装信息遇到错误: ", e);
			Assert.fail("PDA分拣,输入包装编码查询包装信息遇到错误: ", e);
		}
	}

	@Test
	public void pdaWeightSystemTestCase06() {
		ReporterCSS.title("测试点: PDA分拣,按商品分拣,搜索过滤");
		try {
			String date = orderCycle.getCycle_start_time().substring(0, 10);
			String time_config_id = orderCycle.getTime_config_id();

			List<PdaWeightSkuBean> pdaWeightSkuList = weightService.searchPdaWeightSkus("", date, time_config_id);
			Assert.assertNotEquals(pdaWeightSkuList, null, "PDA分拣,按商品分拣,拉取待分拣商品列表失败");

			PdaWeightSkuBean pdaWeightSku = NumberUtil.roundNumberInList(pdaWeightSkuList);
			List<String> sku_ids = pdaWeightSku.getSku_ids();

			PdaWeightSkuDetailFilterParam filterParam = new PdaWeightSkuDetailFilterParam();
			filterParam.setDate(date);
			filterParam.setSku_ids(JSONArray.parseArray(JSON.toJSONString(sku_ids)));
			filterParam.setTime_config_id(time_config_id);
			filterParam.setStatus(5);
			filterParam.setSort_status(1);

			PdaWeightSkuDetailBean pdaWeightSkuDetail = weightService.getPdaWeightSkuDetail(filterParam);
			Assert.assertNotEquals(pdaWeightSkuDetail, null, "PDA分拣,按商品分拣,搜索过滤拉取指定商品分拣信息失败");

			List<PdaWeightSkuDetailBean.Order> temp_ordres = pdaWeightSkuDetail.getOrders().stream().filter(
					o -> o.getStatus() != filterParam.getStatus() || o.getSort_status() != filterParam.getSort_status())
					.collect(Collectors.toList());
			String msg = null;
			boolean result = true;
			if (temp_ordres.size() > 0) {
				msg = String.format("PDA分拣,按商品分拣,条件条件搜索过滤,搜索出了不符合过滤条件的信息");
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<RouteBean> routeList = routeService.getAllRoutes();
			Assert.assertNotEquals(routeList, null, "获取站点的所有的配送线路信息失败");

			BigDecimal route_id = null;
			if (routeList.size() > 0) {
				route_id = routeList.get(0).getId();
			} else {
				route_id = new BigDecimal("-1");
			}

			filterParam.setRoute_id(route_id);
			pdaWeightSkuDetail = weightService.getPdaWeightSkuDetail(filterParam);
			Assert.assertNotEquals(pdaWeightSkuDetail, null, "PDA分拣,按商品分拣,搜索过滤拉取指定商品分拣信息失败");

			temp_ordres = pdaWeightSkuDetail.getOrders().stream()
					.filter(o -> o.getStatus() != filterParam.getStatus()
							|| o.getSort_status() != filterParam.getSort_status()
							|| o.getRoute_id().compareTo(filterParam.getRoute_id()) != 0)
					.collect(Collectors.toList());
			if (temp_ordres.size() > 0) {
				msg = String.format("PDA分拣,按商品分拣,条件条件搜索过滤,搜索出了不符合过滤条件的信息");
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "PDA分拣,按商品分拣,条件条件搜索过滤,搜索出了不符合过滤条件的信息");

		} catch (Exception e) {
			logger.error("PDA分拣,按商品分拣,搜索过滤遇到错误: ", e);
			Assert.fail("PDA分拣,按商品分拣,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void pdaWeightSystemTestCase07() {
		ReporterCSS.title("测试点: PDA分拣,按订单分拣,搜索过滤");
		try {
			String date = orderCycle.getCycle_start_time().substring(0, 10);
			String time_config_id = orderCycle.getTime_config_id();

			PdaOrderFilterParam filterParam = new PdaOrderFilterParam();
			filterParam.setTime_config_id(time_config_id);
			filterParam.setDate(date);

			List<PdaOrderBean> pdaOrderList = weightService.searchPdaOrders(filterParam);
			Assert.assertNotEquals(pdaOrderList, null, "PDA分拣,按订单分拣拉取数据失败");

			OrderFilterParam orderFilterParam = new OrderFilterParam();
			int limit = 50;
			int offset = 0;
			orderFilterParam.setQuery_type(2);
			orderFilterParam.setTime_config_id(time_config_id);
			orderFilterParam.setCycle_start_time(orderCycle.getCycle_start_time());
			orderFilterParam.setCycle_end_time(orderCycle.getCycle_end_time());
			orderFilterParam.setLimit(limit);
			List<OrderBean> orders = new ArrayList<>();
			while (true) {
				orderFilterParam.setOffset(offset);
				List<OrderBean> temp_orders = orderService.searchOrder(orderFilterParam);
				Assert.assertNotEquals(temp_orders, null, "搜索过滤订单列表失败");
				orders.addAll(temp_orders);
				if (temp_orders.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<OrderBean> target_orders = orders.stream().filter(o -> o.getStatus() >= 5)
					.collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			if (pdaOrderList.size() != target_orders.size()) {
				msg = String.format("PDA按订单分拣拉取到的订单总数和预期的不一致,预期:%s,实际:%s", target_orders.size(), pdaOrderList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			filterParam.setStatus(10);

			pdaOrderList = weightService.searchPdaOrders(filterParam);
			Assert.assertNotEquals(pdaOrderList, null, "PDA分拣,按订单分拣拉取数据失败");
			target_orders = orders.stream().filter(o -> o.getStatus() == filterParam.getStatus())
					.collect(Collectors.toList());

			if (pdaOrderList.size() != target_orders.size()) {
				msg = String.format("PDA按订单分拣拉取到的订单总数和预期的不一致,预期:%s,实际:%s", target_orders.size(), pdaOrderList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "PDA分拣,按订单分拣,条件条件搜索过滤,搜索出了不符合过滤条件的信息");
		} catch (Exception e) {
			logger.error("PDA分拣,按商品分拣,搜索过滤遇到错误: ", e);
			Assert.fail("PDA分拣,按商品分拣,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void pdaWeightSystemTestCase08() {
		ReporterCSS.title("测试点: PDA分拣,按商品分拣");
		try {
			// 先新建一个订单
			String order_id = orderTool.oneStepCreateOrder(4);
			Assert.assertNotEquals(order_id, null, "创建订单失败");
			boolean result = orderService.updateOrderState(order_id, 5);
			Assert.assertEquals(result, true, "订单 " + order_id + "状态改为分拣中,更新失败");
			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + "详细信息失败");

			String sku_id = null;
			BigDecimal std_unit_quantity = null;
			List<String> package_ids = null;
			List<OrderDetailBean.Detail> details = orderDetail.getDetails();
			Integer count = null;
			// 真实称重数Map
			Map<String, BigDecimal> expected_real_weight_map = new HashMap<>();
			// 预分拣包装Map
			Map<String, String> packageMap = new HashMap<String, String>();
			for (int i = 0; i < details.size(); i++) {
				OrderDetailBean.Detail detail = details.get(i);
				sku_id = detail.getSku_id();
				std_unit_quantity = detail.getStd_unit_quantity().add(new BigDecimal("0.1"));
				expected_real_weight_map.put(sku_id, std_unit_quantity);
				// 控制非必传值
				if (i % 2 == 0) {
					count = null;
				} else {
					count = 1;
				}
				package_ids = weightService.createWeightPackage(sku_id, std_unit_quantity, count);
				Assert.assertNotEquals(package_ids, null, "生成预分拣包装失败");
				packageMap.put(sku_id, package_ids.get(0));
			}

			// 在PDA中找分拣数据
			OrderCycle orderCycle = orderTool.getOrderOperationCycle(order_id);
			Assert.assertNotEquals(orderCycle, null, "获取订单 " + order_id + " 所处运营周期失败");

			String time_config_id = orderCycle.getTime_config_id();
			String start_day = orderCycle.getCycle_start_time().substring(0, 10);

			List<PdaWeightSkuBean> pdaWeightSkuList = weightService.searchPdaWeightSkus("", start_day, time_config_id);
			Assert.assertNotEquals(pdaWeightSkuList, null, "PDA分拣,按商品分拣,拉取待分拣商品列表失败");

			// PDA中实际看到的分拣SKU
			List<String> actual_sorting_sku_ids = new ArrayList<String>();
			for (PdaWeightSkuBean pdaWeightSku : pdaWeightSkuList) {
				List<String> skus = pdaWeightSku.getSku_ids();
				for (String sku : skus) {
					actual_sorting_sku_ids.add(sku.split("#")[0]);
				}
			}

			List<String> order_sku_ids = orderDetail.getDetails().stream().filter(d -> d.getIs_weigh() == 1)
					.map(d -> d.getSku_id()).collect(Collectors.toList());

			Assert.assertEquals(actual_sorting_sku_ids.containsAll(order_sku_ids), true,
					"PDA分拣,按商品分拣,订单" + order_id + "中的部分称重商品没有出现在待分拣商品列表");

			PdaWeightSkuDetailFilterParam pdaWeightSkuDetailFilterParam = new PdaWeightSkuDetailFilterParam();
			pdaWeightSkuDetailFilterParam.setTime_config_id(time_config_id);
			pdaWeightSkuDetailFilterParam.setDate(start_day);

			PdaSetWeightParam pdaSetWeightParam = new PdaSetWeightParam();
			pdaSetWeightParam.setOrder_id(order_id);
			pdaSetWeightParam.setSort_way(0);
			pdaSetWeightParam.setWeighting_quantity(BigDecimal.ZERO);
			BigDecimal detail_id = null;
			for (OrderDetailBean.Detail detail : details) {
				String temp_sku_id = detail.getSku_id();
				PdaWeightSkuBean pdaWeightSku = pdaWeightSkuList.stream()
						.filter(ws -> ws.getSku_ids().toString().contains(temp_sku_id)).findAny().orElse(null);
				List<String> sku_ids = pdaWeightSku.getSku_ids();
				pdaWeightSkuDetailFilterParam.setSku_ids(JSONArray.parseArray(JSON.toJSONString(sku_ids)));
				PdaWeightSkuDetailBean pdaWeightSkuDetail = weightService
						.getPdaWeightSkuDetail(pdaWeightSkuDetailFilterParam);
				Assert.assertNotEquals(pdaWeightSkuDetail, null, "获取指定的SKU分拣信息列表失败");

				PdaWeightSkuDetailBean.Order order = pdaWeightSkuDetail.getOrders().stream()
						.filter(s -> s.getOrder_id().equals(order_id)).findAny().orElse(null);
				Assert.assertNotEquals(order, null, "商品" + sku_ids + "的分拣列表中没有订单 " + order_id + "相关分拣信息");

				detail_id = detail.getDetail_id();

				PdaWeightSortDetailBean pdaWeightSortDetail = weightService.getPdaWeightSortDetail(order_id,
						temp_sku_id, detail_id);
				Assert.assertNotEquals(pdaWeightSortDetail, null,
						"获取订单 " + order_id + "中的商品 " + order.getSku_id() + "分拣详细新失败");

				String package_id = packageMap.get(detail.getSku_id());

				PdaPatckageInfoBean pdaPatckageInfo = weightService.searchPackageInPda(package_id);
				Assert.assertNotEquals(pdaPatckageInfo, null, "获取 " + package_id + " 预分拣包装失败");

				pdaSetWeightParam.setSku_id(order.getSku_id());
				JSONArray param_package_ids = new JSONArray();
				param_package_ids.add(package_id);
				pdaSetWeightParam.setPackage_ids(param_package_ids);
				if (detail.getDetail_id() != null) {
					pdaSetWeightParam.setDetail_id(detail.getDetail_id());
				}
				result = weightService.setWeightOfPda(pdaSetWeightParam);
				Assert.assertEquals(result, true, "PDA 进行商品分拣失败");
			}

			Reporter.log("PDA 称重后,验证相关信息是否同步到订单");
			// 再次获取订单,查看称重数据是否同步
			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			String msg = null;
			for (OrderDetailBean.Detail temp_detail : orderDetail.getDetails()) {
				if (temp_detail.getIs_weigh() == 1) {
					if (temp_detail.getWeighted() != 1) {
						msg = String.format("订单%s商品%s应该显示已经称重", order_id, temp_detail.getSku_id());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}

					BigDecimal expected_std_real_quantity = temp_detail.getStd_unit_quantity()
							.add(new BigDecimal("0.1"));
					if (expected_std_real_quantity.compareTo(temp_detail.getStd_real_quantity()) != 0) {
						msg = String.format("订单%s商品%s称重数与预期不符,预期:%s,实际:%s", order_id, temp_detail.getSku_id(),
								expected_std_real_quantity, temp_detail.getStd_real_quantity());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}

			List<String> order_ids = Arrays.asList(order_id);
			List<DistributeOrderDetailBean> distributeOrderDetailList = distributeService
					.getDistributeOrderDetailArray(order_ids);
			Assert.assertNotEquals(distributeOrderDetailList, null, "获取配送单" + order_id + " 详细信息失败");

			DistributeOrderDetailBean distributeOrderDetail = distributeOrderDetailList.stream()
					.filter(d -> d.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(distributeOrderDetail, null, "没有找到订单 " + order_id + " 对应的配送单");

			BigDecimal actual_real_weight = null;
			BigDecimal expected_real_weight = null;
			for (DistributeOrderDetailBean.Detail d_detail : distributeOrderDetail.getDetails()) {
				actual_real_weight = d_detail.getReal_weight();
				expected_real_weight = expected_real_weight_map.get(d_detail.getSku_id());
				sku_id = d_detail.getSku_id();
				if (actual_real_weight.compareTo(expected_real_weight) != 0) {
					msg = String.format("配送单%s里的商品%s显示的出库数(基本单位)与预期不符,预期:%s,实际:%s", order_id, sku_id,
							expected_real_weight, actual_real_weight);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			Assert.assertEquals(result, true, "订单" + order_id + " PDA分拣后,称重数据没有同步到(订单、出库单)");
		} catch (Exception e) {
			logger.error("PDA分拣,按商品分拣过程中遇到错误: ", e);
			Assert.fail("PDA分拣,按商品分拣过程中遇到错误: ", e);
		}
	}

	@Test
	public void pdaWeightSystemTestCase09() {
		ReporterCSS.title("测试点: PDA分拣,按订单分拣");
		try {
			String order_id = orderTool.oneStepCreateOrder(6);
			Assert.assertNotEquals(order_id, null, "创建订单失败");
			boolean result = orderService.updateOrderState(order_id, 5);
			Assert.assertEquals(result, true, "订单 " + order_id + "状态改为分拣中,更新失败");
			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + "详细信息失败");

			OrderCycle orderCycle = orderTool.getOrderOperationCycle(order_id);
			Assert.assertNotEquals(orderCycle, null, "获取订单 " + order_id + " 所处运营周期时间失败");
			String time_config_id = orderCycle.getTime_config_id();
			String start_date = orderCycle.getCycle_start_time().substring(0, 10);

			PdaOrderFilterParam filterParam = new PdaOrderFilterParam();
			filterParam.setTime_config_id(time_config_id);
			filterParam.setDate(start_date);

			List<PdaOrderBean> pdaOrderList = weightService.searchPdaOrders(filterParam);
			Assert.assertNotEquals(pdaOrderList, null, "PDA分拣,按订单分拣拉取数据失败");

			PdaOrderBean pdaOrder = pdaOrderList.stream().filter(o -> o.getOrder_id().equals(order_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(pdaOrder, null, "PDA分拣,按订单分拣,没有找到目标订单 " + order_id);

			PdaOrderDetailParam pdaOrderDetailParam = new PdaOrderDetailParam();
			pdaOrderDetailParam.setOrder_id(order_id);
			pdaOrderDetailParam.setSort_status(0);
			PdaOrderDetailBean pdaOrderDetail = weightService.getPdaOrderDetailBean(pdaOrderDetailParam);
			Assert.assertNotEquals(pdaOrderDetail, null, "PDA分拣,按订单分拣,获取订单 " + order_id + " 详细信息失败");

			PdaOrderDetailBean.Detail p_detail = null;
			String msg = null;
			for (OrderDetailBean.Detail o_detail : orderDetail.getDetails()) {
				String sku_id = o_detail.getSku_id();
				p_detail = pdaOrderDetail.getDetails().stream().filter(d -> d.getSku_id().equals(sku_id)).findAny()
						.orElse(null);
				if (p_detail == null) {
					msg = String.format("订单%s中的商品%s,没有出现PDA按订单分拣详情页面中", order_id, sku_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (p_detail.getStd_quantity().compareTo(o_detail.getStd_unit_quantity()) != 0) {
					msg = String.format("订单%s中的商品%s,在PDA按订单分拣详情页面显示的下单数与预期不符,预期:%s,实际:%s", order_id, sku_id,
							o_detail.getStd_unit_quantity(), p_detail.getStd_quantity());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单" + order_id + "在PDA按订单分拣详情页面显示的信息与预期不符");

			// 随机取订单中的一个商品进行PDA分拣
			OrderDetailBean.Detail detail = NumberUtil.roundNumberInList(orderDetail.getDetails());
			String sku_id = detail.getSku_id();
			BigDecimal std_real_quantity = detail.getStd_unit_quantity().add(new BigDecimal("0.2"));

			List<String> package_ids = weightService.createWeightPackage(sku_id, std_real_quantity, 1);
			Assert.assertNotEquals(package_ids, null, "生成预分拣包装失败");

			PdaSetWeightParam pdaSetWeightParam = new PdaSetWeightParam();
			pdaSetWeightParam.setOrder_id(order_id);
			pdaSetWeightParam.setPackage_ids(JSONArray.parseArray(JSON.toJSONString(package_ids)));
			pdaSetWeightParam.setSku_id(sku_id);
			if (detail.getDetail_id() != null) {
				pdaSetWeightParam.setDetail_id(detail.getDetail_id());
			}

			pdaSetWeightParam.setSort_way(0);
			pdaSetWeightParam.setWeighting_quantity(BigDecimal.ZERO);
			result = weightService.setWeightOfPda(pdaSetWeightParam);
			Assert.assertEquals(result, true, "PDA分拣失败");

			ReporterCSS.log("验证PDA数据是否刷新");
			pdaOrderList = weightService.searchPdaOrders(filterParam);
			Assert.assertNotEquals(pdaOrderList, null, "PDA分拣,按订单分拣拉取数据失败");

			pdaOrder = pdaOrderList.stream().filter(o -> o.getOrder_id().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(pdaOrder, null, "PDA分拣,按订单分拣,没有找到目标订单 " + order_id);

			if (pdaOrder.getFinish_count() != 1) {
				msg = String.format("PDA分拣,按订单分拣,分拣完商品,订单%s显示的分拣进度与预期不符,预期:1,实际:%s", order_id,
						pdaOrder.getFinish_count());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			pdaOrderDetail = weightService.getPdaOrderDetailBean(pdaOrderDetailParam);
			Assert.assertNotEquals(pdaOrderDetail, null, "PDA分拣,按订单分拣,获取订单 " + order_id + " 详细信息失败");

			PdaOrderDetailBean.Detail temp_detail = pdaOrderDetail.getDetails().stream()
					.filter(d -> d.getSku_id().equals(sku_id)).findAny().orElse(null);
			if (temp_detail.getWeighting_quantity().compareTo(std_real_quantity) != 0) {
				msg = String.format("PDA分拣,按订单分拣,分拣完商品%s,PDA中订单%s显示的称重数与预期不符,预期:%s,实际:%s", sku_id, order_id,
						std_real_quantity, temp_detail.getWeighting_quantity());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			ReporterCSS.log("验证PDA称重数据是否同步ST");
			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 的详细信息失败");

			detail = orderDetail.getDetails().stream().filter(o -> o.getSku_id().equals(sku_id)).findAny().orElse(null);
			Assert.assertNotEquals(detail, null, "订单 " + order_id + " 详细信息中没有找到商品 " + sku_id);

			if (detail.getWeighted() != 1) {
				msg = String.format("订单%s中的商品%s还是显示的未称重状态", order_id, sku_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (detail.getStd_real_quantity().compareTo(std_real_quantity) != 0) {
				msg = String.format("订单%s中的商品%s称重数与预期不符,预期:%s,实际:%s", order_id, sku_id, std_real_quantity,
						detail.getStd_real_quantity());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<String> order_ids = Arrays.asList(order_id);
			List<DistributeOrderDetailBean> distributeOrderDetailList = distributeService
					.getDistributeOrderDetailArray(order_ids);
			Assert.assertNotEquals(distributeOrderDetailList, null, "获取配送单" + order_id + " 详细信息失败");

			DistributeOrderDetailBean distributeOrderDetail = distributeOrderDetailList.stream()
					.filter(d -> d.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(distributeOrderDetail, null, "没有找到订单 " + order_id + " 对应的配送单");

			DistributeOrderDetailBean.Detail d_detail = distributeOrderDetail.getDetails().stream()
					.filter(d -> d.getSku_id().equals(sku_id)).findAny().orElse(null);

			Assert.assertNotEquals(d_detail, null, "配送单 " + order_id + " 中没有找到商品 " + sku_id);

			if (d_detail.getReal_weight().compareTo(std_real_quantity) != 0) {
				msg = String.format("配送单%s中的商品%s的出库数与预期不符,预期:%s,实际:%s", order_id, sku_id, std_real_quantity,
						d_detail.getReal_weight());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "PDA分拣,按订单分拣,分拣完毕,称重数据没有同步到订单、配送单");

			boolean is_async = stockOutService.asyncOrderToOutStockSheet(order_id);
			Assert.assertEquals(is_async, true, "订单" + order_id + " 在20秒内没有同步生成出库单");

			int loop_times = 10;
			while (loop_times > 0) {
				OutStockDetailBean stockOutDetail = stockOutService.getOutStockDetailInfo(order_id);
				Assert.assertNotEquals(stockOutDetail, null, "获取出库单 " + order_id + " 的详细信息失败");
				Thread.sleep(2000);

				OutStockDetailBean.Detail s_detail = stockOutDetail.getDetails().stream()
						.filter(d -> d.getId().equals(sku_id)).findAny().orElse(null);
				BigDecimal actual_std_unit_quantity = s_detail.getQuantity().multiply(s_detail.getSale_ratio())
						.setScale(2, BigDecimal.ROUND_HALF_UP);
				if (actual_std_unit_quantity.compareTo(std_real_quantity) == 0) {
					break;
				} else {
					if (loop_times == 1) {
						msg = String.format("订单%s中的商品%s的称重数没有同步到出库单,预期:%s,实际:%s", order_id, sku_id, std_real_quantity,
								actual_std_unit_quantity);
						ReporterCSS.title(msg);
						logger.warn(msg);
						result = false;
					}
					Thread.sleep(2000);
				}
				loop_times--;
			}
			Assert.assertEquals(result, true, "在一定得时间内,订单 " + order_id + " 的称重数没有同步到出库单");
		} catch (Exception e) {
			logger.error("PDA分拣,按订单分拣过程中遇到错误: ", e);
			Assert.fail("PDA分拣,按订单分拣过程中遇到错误: ", e);
		}
	}
}
