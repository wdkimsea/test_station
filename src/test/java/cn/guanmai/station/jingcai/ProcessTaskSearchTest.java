package cn.guanmai.station.jingcai;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.delivery.DriverBean;
import cn.guanmai.station.bean.delivery.RouteBean;
import cn.guanmai.station.bean.jingcai.IngredientBean;
import cn.guanmai.station.bean.jingcai.LabelBean;
import cn.guanmai.station.bean.jingcai.ProcessTaskBean;
import cn.guanmai.station.bean.jingcai.ProductBean;
import cn.guanmai.station.bean.jingcai.param.ProcessTaskFilterParam;
import cn.guanmai.station.bean.order.AddressLabelBean;
import cn.guanmai.station.bean.order.CustomerBean;
import cn.guanmai.station.bean.order.OrderCycle;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderReceiveTimeBean;
import cn.guanmai.station.bean.order.OrderResponseBean;
import cn.guanmai.station.bean.order.OrderResponseBean.Data.NotEnoughInventories;
import cn.guanmai.station.bean.order.param.OrderCreateParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.delivery.DistributeServiceImpl;
import cn.guanmai.station.impl.delivery.RouteServiceImpl;
import cn.guanmai.station.impl.jingcai.LabelServiceImpl;
import cn.guanmai.station.impl.jingcai.ProcessTaskServiceImpl;
import cn.guanmai.station.impl.jingcai.ProductServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.delivery.DistributeService;
import cn.guanmai.station.interfaces.delivery.RouteService;
import cn.guanmai.station.interfaces.jingcai.LabelService;
import cn.guanmai.station.interfaces.jingcai.ProcessTaskService;
import cn.guanmai.station.interfaces.jingcai.ProductService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author: liming
 * @Date: 2020年5月13日 上午10:24:19
 * @description:
 * @version: 1.0
 */

public class ProcessTaskSearchTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(ProcessTaskSearchTest.class);
	private Map<String, String> cookie;

	private OrderService orderService;
	private OrderTool orderTool;
	private LabelService labelService;
	private RouteService routeService;
	private OrderCreateParam orderCreateParam;
	private ProcessTaskService processTaskService;
	private ProductService productService;
	private DistributeService distributeService;
	private LoginUserInfoService loginUserInfoService;
	private ProcessTaskFilterParam processTaskFilterParam;
	private InitDataBean initData;
	private OrderCycle orderCycle;
	private OrderDetailBean orderDetail;
	private BigDecimal label_id;
	private String order_id;
	private String address_id;
	private String time_config_id;

	@BeforeClass
	public void initData() {
		cookie = getStationCookie();
		processTaskService = new ProcessTaskServiceImpl(cookie);

		orderTool = new OrderTool(cookie);

		routeService = new RouteServiceImpl(cookie);
		labelService = new LabelServiceImpl(cookie);
		orderService = new OrderServiceImpl(cookie);
		productService = new ProductServiceImpl(cookie);
		distributeService = new DistributeServiceImpl(cookie);
		loginUserInfoService = new LoginUserInfoServiceImpl(cookie);

		try {
			initData = getInitData();
			String supplier_id = initData.getSupplier().getId();

			label_id = labelService.initLabel();

			List<CustomerBean> customerArray = orderService.getOrderCustomerArray(6);
			Assert.assertNotEquals(customerArray, null, "获取下单商户列表信息失败");

			Assert.assertEquals(customerArray.size() > 0, true, "无可用下单商户");

			// 随机选取一个正常商户进行下单
			CustomerBean customer = NumberUtil.roundNumberInList(customerArray);
			address_id = customer.getAddress_id();
			String uid = customer.getId();

			List<OrderReceiveTimeBean> orderReceiveTimes = orderService.getCustomerServiceTimeArray(address_id);
			Assert.assertNotEquals(orderReceiveTimes, null, "获取下单商户对应的运营时间失败");

			Assert.assertEquals(orderReceiveTimes.size() > 0, true, "商户" + address_id + "没有绑定运营时间,无法进行下单操作");
			// 随机取一个绑定的运营时间
			OrderReceiveTimeBean orderReceiveTime = NumberUtil.roundNumberInList(orderReceiveTimes);
			time_config_id = orderReceiveTime.getTime_config_id();

			Assert.assertEquals(orderReceiveTime.getReceive_times().size() > 0, true,
					"受收货自然日限制,运营时间" + time_config_id + "无可用收货日期可选");

			// 下单商品集合
			String[] search_texts = new String[] { "A", "B", "C" };
			orderCreateParam = orderService.searchOrderSkus(address_id, time_config_id, search_texts, 10);
			Assert.assertEquals(orderCreateParam != null && orderCreateParam.getDetails().size() > 0, true,
					"下单搜索搜商品列表为空");

			List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();
			OrderCreateParam.OrderSku orderSku = orderSkus.get(0);

			String sku_id = orderSku.getSku_id();
			String spu_id = orderSku.getSpu_id();

			ProductBean product = productService.getProduct(spu_id, sku_id);
			Assert.assertNotEquals(product, null, "获取销售SKU: " + sku_id + "失败");

			product.setStock_type(1);
			product.setStocks("-99999");

			ProductBean.CleanFoodInfo cleanFoodInfo = product.new CleanFoodInfo();
			cleanFoodInfo.setLicense("IOS123123");
			cleanFoodInfo.setStorage_condition("冷藏");
			cleanFoodInfo.setProduct_performance_standards("卫*12233");
			cleanFoodInfo.setNutrition("蛋白质、水");
			cleanFoodInfo.setMaterial_description("新鲜");
			cleanFoodInfo.setProcess_label_id(label_id);
			cleanFoodInfo.setOrigin_place("广东深圳");
			cleanFoodInfo.setCombine_technic_status("0");
			cleanFoodInfo.setCut_specification("分切");
			cleanFoodInfo.setRecommended_method("煮熟后食用");
			cleanFoodInfo.setShelf_life(90);
			cleanFoodInfo.setProcess_label_id(label_id);
			product.setClean_food_info(cleanFoodInfo);

			if (product.getClean_food() == 0) {
				product.setClean_food(1);
				List<IngredientBean> ingredients = productService.searchIngredient("菜");
				Assert.assertNotEquals(ingredients, null, "新建净菜,搜索物料信息失败");

				List<ProductBean.Ingredient> ingredientParams = new ArrayList<>();
				for (IngredientBean ingredient : ingredients) {
					ProductBean.Ingredient ingredientParam = product.new Ingredient();
					if (ingredient.getRemark_type() == 1) {
						ingredientParam.setSupplier_id(supplier_id);
					}
					ingredientParam.setAttrition_rate(new BigDecimal("0"));
					ingredientParam.setStd_unit_name(ingredient.getStd_unit_name());
					ingredientParam.setSale_unit_name(ingredient.getSale_unit_name());
					ingredientParam.setId(ingredient.getId());
					ingredientParam.setRatio(ingredient.getRatio());
					ingredientParam.setName(ingredient.getName());
					ingredientParam.setTechnic_flow_len(0);
					ingredientParam.setRemark_type(ingredient.getRemark_type());
					ingredientParam.setSale_proportion(new BigDecimal("1").multiply(ingredient.getRatio()));
					ingredientParam.setProportion(new BigDecimal("1"));
					ingredientParam.setCategory_id_2(ingredient.getCategory_id_2());
					ingredientParam.setVersion(1);
					ingredientParams.add(ingredientParam);
					if (ingredientParams.size() >= 2) {
						break;
					}
				}
			}

			boolean result = productService.updateProduct(product);
			Assert.assertEquals(result, true, "修改销售SKU " + sku_id + " 转化为净菜商品失败");

			List<OrderReceiveTimeBean.ReceiveTime> receiveTimes = orderReceiveTime.getReceive_times().stream()
					.filter(r -> r.getTimes().size() >= 2).collect(Collectors.toList());
			Assert.assertEquals(receiveTimes.size() > 0, true, "运营时间的收货时间无法取值了(当前时间过了收货时间结束时间或者收货起始和收货结束时间一致)");

			OrderReceiveTimeBean.ReceiveTime receiveTime = NumberUtil.roundNumberInList(receiveTimes);
			String receive_begin_time = receiveTime.getTimes().get(0);
			String receive_end_time = receiveTime.getTimes().get(1);

			// 下单对象
			orderCreateParam.setAddress_id(address_id);
			orderCreateParam.setUid(uid);
			orderCreateParam.setReceive_begin_time(receive_begin_time);
			orderCreateParam.setReceive_end_time(receive_end_time);
			orderCreateParam.setTime_config_id(time_config_id);
			orderCreateParam.setRemark(StringUtil.getRandomString(6));
			orderCreateParam.setForce(1);

			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);
			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				if (notEnoughInventoriesList.size() > 0) {
					Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam
							.getCombine_goods_map();
					orderSkus = orderCreateParam.getDetails();
					for (NotEnoughInventories skuObj : notEnoughInventoriesList) {
						String temp_sku_id = skuObj.getSku_id();

						// 首先找出这个SKU有没有在组合商品里
						List<String> combine_goods_ids = orderSkus.stream()
								.filter(s -> s.getSku_id().equals(temp_sku_id) && s.isIs_combine_goods())
								.map(s -> s.getCombine_goods_id()).distinct().collect(Collectors.toList());

						// 先把这个商品过滤掉
						orderSkus = orderSkus.stream().filter(s -> !s.getSku_id().equals(temp_sku_id))
								.collect(Collectors.toList());

						// 过滤掉组合商品里的其他商品
						for (String combine_goods_id : combine_goods_ids) {
							combine_goods_map.remove(combine_goods_id);
							orderSkus = orderSkus.stream().filter(
									s -> s.isIs_combine_goods() && !s.getCombine_goods_id().equals(combine_goods_id))
									.collect(Collectors.toList());
						}
					}
					Assert.assertEquals(orderSkus.size() > 0, true, "下单商品列表为空,无法进行下单");
					orderCreateParam.setDetails(orderSkus);
					orderResponse = orderService.createOrder(orderCreateParam);
				}
				Assert.assertEquals(orderResponse.getData().getNew_order_ids().size() > 0, true, "正常下单,断言成功");
				order_id = orderResponse.getData().getNew_order_ids().getString(0);
			} else {
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}

			orderCycle = orderTool.getOrderOperationCycle(order_id);

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

		} catch (Exception e) {
			logger.error("下单过程中遇到错误: ", e);
			Assert.fail("下单过程中遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		processTaskFilterParam = new ProcessTaskFilterParam();
		processTaskFilterParam.setTime_config_id(orderCycle.getTime_config_id());
		processTaskFilterParam.setBegin_time(orderCycle.getCycle_start_time());
		processTaskFilterParam.setEnd_time(orderCycle.getCycle_end_time());
		processTaskFilterParam.setQ_type(1);
		processTaskFilterParam.setLimit(10);
		processTaskFilterParam.setOffset(0);
		processTaskFilterParam.setStatus(0);
	}

	@Test
	public void processTaskTestCase01() {
		ReporterCSS.title("测试点: 搜索过滤查询加工计划");
		try {
			List<ProcessTaskBean> processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "搜索查询加工计划失败");
		} catch (Exception e) {
			logger.error("查询加工计划遇到错误: ", e);
			Assert.fail("查询加工计划遇到错误: ", e);
		}
	}

	@Test
	public void processTaskTestCase02() {
		ReporterCSS.title("测试点: 验证订单里的净菜是否生成对应的加工计划");
		try {
			processTaskFilterParam.setQ_type(2);
			processTaskFilterParam.setQ(order_id);
			List<ProcessTaskBean> processTasks = processTaskService.waitingProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "搜索查询加工计划失败");

			Assert.assertEquals(processTasks.size() > 0, true, "订单" + order_id + "创建后1分钟,对应的净菜的加工计划没有生成");
		} catch (Exception e) {
			logger.error("查询加工计划遇到错误: ", e);
			Assert.fail("查询加工计划遇到错误: ", e);
		}
	}

	@Test
	public void processTaskTestCase03() {
		ReporterCSS.title("测试点: 按订单搜索过滤查询加工计划");
		try {

			processTaskFilterParam.setQ_type(2);
			processTaskFilterParam.setQ(order_id);
			List<ProcessTaskBean> processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "搜索查询加工计划失败");

			Assert.assertEquals(processTasks.size() > 0, true, "订单" + order_id + "创建后1分钟,对应的净菜的加工计划没有生成");

			String msg = null;
			boolean result = true;
			List<OrderDetailBean.Detail> details = orderDetail.getDetails();
			for (OrderDetailBean.Detail detail : details) {
				if (detail.isClean_food()) {
					String sku_id = detail.getSku_id();
					ProcessTaskBean processTask = processTasks.stream().filter(p -> p.getSku_id().equals(sku_id))
							.findAny().orElse(null);
					if (processTask == null) {
						msg = String.format("订单%s中的净菜商品%s没有生成对应的加工计划", order_id, sku_id);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}

					if (detail.getQuantity().compareTo(processTask.getPlan_amount()) != 0) {
						msg = String.format("订单%s中的净菜商品%s生成对应的加工计划数与预期不一致,预期:%s,实际:%s", order_id, sku_id,
								detail.getQuantity(), processTask.getPlan_amount());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}

			for (ProcessTaskBean processTask : processTasks) {
				String sku_id = processTask.getSku_id();
				OrderDetailBean.Detail detail = details.stream().filter(d -> d.getSku_id().equals(sku_id)).findAny()
						.orElse(null);
				if (detail == null) {
					msg = String.format("加工计划按订单%s过滤,过滤出了非订单中的净菜商品:%s", order_id, sku_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				List<ProcessTaskBean.Task> tasks = processTask.getTasks();
				List<String> order_ids = tasks.stream().filter(t -> !t.getOrder_id().equals(order_id))
						.map(t -> t.getOrder_id()).collect(Collectors.toList());
				if (order_ids.size() > 0) {
					msg = String.format("加工计划按订单%s过滤,过滤出了其他订单的相关加工信息:%s", order_id, order_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单 " + order_id + " 的加工计划数据与预期不一致");
		} catch (Exception e) {
			logger.error("按订单搜索过滤查询加工计划遇到错误: ", e);
			Assert.fail("按订单搜索过滤查询加工计划遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = "processTaskTestCase02")
	public void processTaskTestCase04() {
		ReporterCSS.title("测试点: 不输入关键词查询加工计划");
		try {
			processTaskFilterParam.setQ_type(1);

			List<ProcessTaskBean> processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "搜索查询加工计划失败");

			Map<String, BigDecimal> expected_tasks = new HashMap<String, BigDecimal>();
			for (ProcessTaskBean processTask : processTasks) {
				for (ProcessTaskBean.Task task : processTask.getTasks()) {
					if (task.getOrder_id().equals(order_id)) {
						expected_tasks.put(processTask.getSku_id(), task.getOrder_amount());
					}
				}
			}

			String msg = null;
			boolean result = true;
			List<OrderDetailBean.Detail> details = orderDetail.getDetails();
			for (OrderDetailBean.Detail detail : details) {
				if (detail.isClean_food()) {
					String sku_id = detail.getSku_id();
					if (expected_tasks.containsKey(sku_id)) {
						if (expected_tasks.get(sku_id).compareTo(detail.getQuantity()) != 0) {
							msg = String.format("订单%s中的净菜商品%s对应的加工数量与预期不一致,预期:%s,实际:%s", order_id, sku_id,
									detail.getQuantity(), expected_tasks.get(sku_id));
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					} else {
						msg = String.format("订单%s中的净菜商品%s没有找到对应的加工计划", order_id, sku_id);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "订单 " + order_id + " 的加工计划数据与预期不一致");
		} catch (Exception e) {
			logger.error("不输入关键词查询加工计划遇到错误: ", e);
			Assert.fail("不输入关键词查询加工计划遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = "processTaskTestCase02")
	public void processTaskTestCase05() {
		ReporterCSS.title("测试点: 按商品名称关键词查找加工计划");
		try {
			processTaskFilterParam.setQ_type(1);

			OrderDetailBean.Detail detail = orderDetail.getDetails().stream().filter(s -> s.isClean_food()).findFirst()
					.orElse(null);
			String sku_name = detail.getSku_name();
			processTaskFilterParam.setQ(sku_name);

			List<ProcessTaskBean> processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "搜索查询加工计划失败");

			Assert.assertEquals(processTasks.size() > 0, true, "按商品名称搜索过滤加工计划,结果为空,与预期不符");

			List<String> sku_names = processTasks.stream().filter(p -> !p.getName().contains(sku_name))
					.map(p -> p.getName()).collect(Collectors.toList());
			boolean result = true;
			if (sku_names.size() > 0) {
				String msg = String.format("按商品名称关键词:%s 查找加工计划,过滤出了不符合过滤条件的加工计划", sku_name, sku_names);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "按商品名称过滤加工计划,过滤出了不符合过滤条件的数据");
		} catch (Exception e) {
			logger.error("按商品名称关键词查找加工计划遇到错误: ", e);
			Assert.fail("按商品名称关键词查找加工计划遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = "processTaskTestCase02")
	public void processTaskTestCase06() {
		ReporterCSS.title("测试点: 按商户信息搜索过滤加工计划");
		try {
			String resname = orderDetail.getCustomer().getExtender().getResname();
			processTaskFilterParam.setQ_type(3);
			processTaskFilterParam.setQ(resname);

			List<ProcessTaskBean> processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "按商户信息搜索过滤加工计划失败");

			Assert.assertEquals(processTasks.size() > 0, true, "按商户信息搜索过滤加工计划,结果为空,与预期不符");

			String msg = null;
			boolean result = true;
			for (ProcessTaskBean processTask : processTasks) {
				for (ProcessTaskBean.Task task : processTask.getTasks()) {
					if (!task.getResname().contains(resname)) {
						msg = String.format("按商户信息:%s 搜索过滤加工计划,搜索出了不符合条件:%s的信息", resname, task.getResname());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "按商户信息搜索过滤加工计划,搜索出了不符合条件的数据");
		} catch (Exception e) {
			logger.error("按商户信息搜索过滤加工计划遇到错误: ", e);
			Assert.fail("按商户信息搜索过滤加工计划遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = "processTaskTestCase02")
	public void processTaskTestCase07() {
		ReporterCSS.title("测试点: 按线路搜索过滤加工计划");
		try {
			routeService.initRouteData();
			List<ProcessTaskBean> processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "搜索过滤加工计划失败");

			int count1 = 0;
			for (ProcessTaskBean processTask : processTasks) {
				count1 = count1 + processTask.getTasks().size();
			}

			List<RouteBean> routes = routeService.getAllRoutes();
			Assert.assertNotEquals(routes, null, "获取线路数据失败");

			processTaskFilterParam.setQ_type(1);

			String msg = null;
			boolean result = true;
			int count2 = 0;
			for (RouteBean route : routes) {
				processTaskFilterParam.setRoute_id(route.getId());
				processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
				Assert.assertNotEquals(processTasks, null, "按线路搜索过滤加工计划失败");

				for (ProcessTaskBean processTask : processTasks) {
					count2 = count2 + processTask.getTasks().size();
					for (ProcessTaskBean.Task task : processTask.getTasks()) {
						if (!task.getRoute_name().equals(route.getName())) {
							msg = String.format("按线路:%s搜索过滤加工计划,搜索出了其他路线:%s的加工数据", route.getName(),
									task.getRoute_name());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					}
				}
			}

			if (count1 != count2) {
				msg = String.format("净菜加工计划按线路筛选,所有的线路筛选的条目数总和与预期不一致,预期:%s,实际:%s", count1, count2);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "按线路搜索过滤加工计划,搜索结果与预期不符");
		} catch (Exception e) {
			logger.error("按线路搜索过滤遇到错误: ", e);
			Assert.fail("按线路搜索过滤遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = "processTaskTestCase02")
	public void processTaskTestCase08() {
		ReporterCSS.title("测试点: 按司机筛选净菜加工计划");
		try {
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录用户信息失败");

			String station_id = loginUserInfo.getStation_id();

			DriverBean driver = distributeService.initDriverData(station_id);
			Assert.assertNotEquals(driver, null, "初始化司机管理数据失败");

			String receive_begin_time = orderDetail.getCustomer().getReceive_begin_time();

			BigDecimal driver_id = driver.getId();
			String driver_name = driver.getName();

			boolean result = distributeService.editAssignDistributeTask(order_id, address_id, receive_begin_time,
					driver_id, 1);
			Assert.assertEquals(result, true, "订单分配任务司机失败");

			List<BigDecimal> driver_ids = new ArrayList<BigDecimal>();
			driver_ids.add(driver_id);

			processTaskFilterParam.setDriver_ids(driver_ids);

			List<ProcessTaskBean> processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "按线路搜索过滤加工计划失败");

			String msg = null;
			Map<String, BigDecimal> expected_tasks = new HashMap<String, BigDecimal>();

			for (ProcessTaskBean processTask : processTasks) {
				for (ProcessTaskBean.Task task : processTask.getTasks()) {
					if (task.getOrder_id().equals(order_id)) {
						expected_tasks.put(processTask.getSku_id(), task.getOrder_amount());
					}

					if (!task.getDriver_name().equals(driver_name)) {
						msg = String.format("按司机:%s搜索过滤加工计划,搜索出了别的司机:%s 对应的数据", driver_name, task.getDriver_name());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}

			for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
				if (detail.isClean_food()) {
					String sku_id = detail.getSku_id();
					if (expected_tasks.containsKey(sku_id)) {
						if (expected_tasks.get(sku_id).compareTo(detail.getQuantity()) != 0) {
							msg = String.format("订单%s中的净菜商品%s对应的加工数量与预期不一致,预期:%s,实际:%s", order_id, sku_id,
									detail.getQuantity(), expected_tasks.get(sku_id));
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					} else {
						msg = String.format("订单%s中的净菜商品%s没有找到对应的加工计划", order_id, sku_id);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "按司机筛选加工计划,筛选过滤结果与预期不符");
		} catch (Exception e) {
			logger.error("按司机筛选净菜加工计划遇到错误: ", e);
			Assert.fail("按司机筛选净菜加工计划遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = "processTaskTestCase02")
	public void processTaskTestCase09() {
		ReporterCSS.title("测试点: 按商户标签筛选净菜加工计划");
		try {
			List<AddressLabelBean> addressLabels = orderService.getAddressLabels();
			Assert.assertNotEquals(addressLabels, null, "获取商户标签列表失败");

			if (addressLabels.size() > 0) {
				AddressLabelBean addressLabel = NumberUtil.roundNumberInList(addressLabels);
				BigDecimal address_label_id = addressLabel.getId();
				String address_label_name = addressLabel.getName();
				List<BigDecimal> address_label_ids = new ArrayList<BigDecimal>();
				address_label_ids.add(address_label_id);
				processTaskFilterParam.setAddress_labels(address_label_ids);

				List<ProcessTaskBean> processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
				Assert.assertNotEquals(processTasks, null, "按商户标签搜索过滤加工计划失败");

				String msg = null;
				boolean result = true;
				for (ProcessTaskBean processTask : processTasks) {
					for (ProcessTaskBean.Task task : processTask.getTasks()) {
						if (!task.getAddress_label().equals(address_label_name)) {
							msg = String.format("按商户标签:%s搜索过滤加工计划,搜索出了其他商户标签:%s的数据", address_label_name,
									task.getAddress_label());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					}
				}
				Assert.assertEquals(result, true, "按商户标签搜索过滤加工计划,搜索结果与预期不符");
			}
		} catch (Exception e) {
			logger.error("按商户标签筛选净菜加工计划遇到错误: ", e);
			Assert.fail("按商户标签筛选净菜加工计划遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = "processTaskTestCase02")
	public void processTaskTestCase10() {
		ReporterCSS.title("测试点: 按发布状态搜索过滤加工计划");
		try {
			List<ProcessTaskBean> processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "按商户标签搜索过滤加工计划失败");

			int count1 = processTasks.size();

			int count2 = 0;
			int status = 1;
			String msg = null;
			boolean result = true;
			processTaskFilterParam.setStatus(status);
			processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "按发布状态搜索过滤加工计划失败");
			for (ProcessTaskBean processTask : processTasks) {
				if (processTask.getStatus() != status) {
					msg = String.format("加工计划按状态值:%s过滤,过滤出了其他状态值:%s的数据", status, processTask.getStatus());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			count2 = count2 + processTasks.size();

			status = 2;
			processTaskFilterParam.setStatus(status);
			processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "按发布状态搜索过滤加工计划失败");
			for (ProcessTaskBean processTask : processTasks) {
				if (processTask.getStatus() != status) {
					msg = String.format("加工计划按状态值:%s过滤,过滤出了其他状态值:%s的数据", status, processTask.getStatus());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			count2 = count2 + processTasks.size();

			if (count1 != count2) {
				msg = String.format("加工计划按状态值过滤,未发布+已发布的总条目数与预期不一致,预期:%s,实际:%s", count1, count2);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "加工计划按状态值过滤,过滤出的结果与预期不符");
		} catch (Exception e) {
			logger.error("按发布状态搜索过滤加工计划遇到错误: ", e);
			Assert.fail("按发布状态搜索过滤加工计划遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = "processTaskTestCase02")
	public void processTaskTestCase11() {
		ReporterCSS.title("测试点: 按商品标签搜索过滤加工计划");
		try {
			List<BigDecimal> process_labels = new ArrayList<BigDecimal>();
			process_labels.add(label_id);

			processTaskFilterParam.setProcess_labels(process_labels);

			List<ProcessTaskBean> processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "按商户标签搜索过滤加工计划失败");

			Assert.assertEquals(processTasks.size() > 0, true, "按商户标签搜索过滤加工计划,结果列表为空,与预期不符");

			LabelBean label = labelService.getLabelByID(label_id);
			String label_name = label.getName();

			String msg = null;
			boolean result = true;
			for (ProcessTaskBean processTask : processTasks) {
				if (!label_name.equals(processTask.getProcess_label())) {
					msg = String.format("按商品标签:%s搜索过滤加工计划,搜索出了其他标签:%s的数据", label_name, processTask.getProcess_label());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "按商品标签搜索过滤加工计划,搜索出了不符合过滤条件的数据");
		} catch (Exception e) {
			logger.error("按商品标签搜索过滤加工计划遇到错误: ", e);
			Assert.fail("按商品标签搜索过滤加工计划遇到错误: ", e);
		}
	}
}
