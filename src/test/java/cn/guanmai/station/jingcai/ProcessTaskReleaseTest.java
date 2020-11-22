package cn.guanmai.station.jingcai;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import cn.guanmai.station.bean.delivery.RouteBindCustomerBean;
import cn.guanmai.station.bean.jingcai.IngredientBean;
import cn.guanmai.station.bean.jingcai.ProcessOrderBean;
import cn.guanmai.station.bean.jingcai.ProcessTaskBean;
import cn.guanmai.station.bean.jingcai.ProductBean;
import cn.guanmai.station.bean.jingcai.param.ProcessOrderFilterParam;
import cn.guanmai.station.bean.jingcai.param.ProcessTaskFilterParam;
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
import cn.guanmai.station.impl.jingcai.ProcessOrderServiceImpl;
import cn.guanmai.station.impl.jingcai.ProcessTaskServiceImpl;
import cn.guanmai.station.impl.jingcai.ProductServiceImpl;
import cn.guanmai.station.impl.jingcai.TechnicServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.delivery.DistributeService;
import cn.guanmai.station.interfaces.delivery.RouteService;
import cn.guanmai.station.interfaces.jingcai.LabelService;
import cn.guanmai.station.interfaces.jingcai.ProcessOrderService;
import cn.guanmai.station.interfaces.jingcai.ProcessTaskService;
import cn.guanmai.station.interfaces.jingcai.ProductService;
import cn.guanmai.station.interfaces.jingcai.TechnicService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/**
 * @author: liming
 * @Date: 2020年5月13日 上午10:24:19
 * @description:
 * @version: 1.0
 */

public class ProcessTaskReleaseTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(ProcessTaskReleaseTest.class);
	private Map<String, String> cookie;

	private OrderService orderService;
	private OrderTool orderTool;
	private LabelService labelService;
	private RouteService routeService;
	private OrderCreateParam orderCreateParam;
	private ProcessTaskService processTaskService;
	private ProcessOrderService processOrderService;
	private ProductService productService;
	private DistributeService distributeService;
	private LoginUserInfoService loginUserInfoService;
	private TechnicService technicService;
	private ProcessTaskFilterParam processTaskFilterParam;
	private InitDataBean initData;
	private OrderCycle orderCycle;
	private OrderDetailBean orderDetail;
	private BigDecimal label_id;
	private String order_id;
	private String address_id;
	private String time_config_id;
	private String supplier_id;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");

	@BeforeClass
	public void initData() {
		cookie = getStationCookie();

		orderTool = new OrderTool(cookie);
		routeService = new RouteServiceImpl(cookie);
		labelService = new LabelServiceImpl(cookie);
		orderService = new OrderServiceImpl(cookie);
		productService = new ProductServiceImpl(cookie);
		technicService = new TechnicServiceImpl(cookie);
		distributeService = new DistributeServiceImpl(cookie);
		processTaskService = new ProcessTaskServiceImpl(cookie);
		processOrderService = new ProcessOrderServiceImpl(cookie);
		loginUserInfoService = new LoginUserInfoServiceImpl(cookie);

		try {
			initData = getInitData();
			supplier_id = initData.getSupplier().getId();

			technicService.initTechnic();

			label_id = labelService.initLabel();
		} catch (Exception e) {
			logger.error("初始化净菜相关数据遇到错误: ", e);
			Assert.fail("初始化净菜相关数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		try {
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
		processTaskFilterParam = new ProcessTaskFilterParam();
		processTaskFilterParam.setTime_config_id(orderCycle.getTime_config_id());
		processTaskFilterParam.setBegin_time(orderCycle.getCycle_start_time());
		processTaskFilterParam.setEnd_time(orderCycle.getCycle_end_time());
		processTaskFilterParam.setQ_type(1);
		processTaskFilterParam.setLimit(10);
		processTaskFilterParam.setOffset(0);
		processTaskFilterParam.setStatus(0);
	}

	public boolean compareData(List<ProcessOrderBean> processOrders, List<ProcessTaskBean> processTasks) {
		// 验证加工单据生成的是否正确
		String msg = null;
		boolean result = true;
		for (ProcessTaskBean processTask : processTasks) {
			if (processTask.getStatus() == 2) {
				ProcessOrderBean processOrder = null;
				String expected_custom_id = null;
				for (ProcessTaskBean.Task task : processTask.getTasks()) {
					String custom_id = processTask.getSku_id() + "-" + task.getId();
					processOrder = processOrders.stream().filter(p -> p.getCustom_id().equals(custom_id)).findAny()
							.orElse(null);
					expected_custom_id = custom_id;
					if (processOrder != null) {
						break;
					}
				}
				if (processOrder == null) {
					msg = String.format("净菜加工任务发布后,预期的计划编号:%s 加工单据没有出现", expected_custom_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (processOrder.getPlan_amount().compareTo(processTask.getPlan_amount()) != 0) {
					msg = String.format("净菜加工任务发布后,计划编号:%s 加工单据计划生成数与预期不符,预期:%s,实际:%s", expected_custom_id,
							processTask.getPlan_amount(), processOrder.getPlan_amount());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

		}
		return result;
	}

	@Test
	public void processTaskReleaseTestCase01() {
		ReporterCSS.title("测试点: 按订单过滤发布净菜加工计划");
		try {
			processTaskFilterParam.setQ_type(2);
			processTaskFilterParam.setQ(order_id);
			List<ProcessTaskBean> processTasks = processTaskService.waitingProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "搜索查询加工计划失败");

			Assert.assertEquals(processTasks.size() > 0, true, "订单" + order_id + "创建后1分钟,对应的净菜的加工计划没有生成");

			processTaskFilterParam.setAll(1);

			boolean result = processTaskService.releaseProcessTask(processTaskFilterParam);
			Assert.assertEquals(result, true, "按订单过滤发布净菜加工计划失败");

			processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "搜索查询加工计划失败");

			String msg = null;
			for (ProcessTaskBean processTask : processTasks) {
				if (processTask.getStatus() != 2) {
					msg = String.format("订单%s里的净菜商品:%s,加工计划对应的状态值与预期不符,预期:%s,实际:%s", order_id, processTask.getSku_id(),
							2, processTask.getStatus());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			ProcessOrderFilterParam processOrderFilterParam = new ProcessOrderFilterParam();
			processOrderFilterParam.setBegin(todayStr);
			processOrderFilterParam.setEnd(todayStr);
			processOrderFilterParam.setDate_type(5);
			processOrderFilterParam.setNeed_unrelease(1);

			List<ProcessOrderBean> processOrders = processOrderService.searchProcessOrder(processOrderFilterParam);
			Assert.assertNotEquals(processOrders, null, "按创建日期搜索过滤加工单据失败");

			result = compareData(processOrders, processTasks);

			Assert.assertEquals(result, true, "按订单过滤发布净菜加工计划后,净菜加工任务信息与预期不符");
		} catch (Exception e) {
			logger.error("按订单过滤发布净菜加工计划遇到错误: ", e);
			Assert.fail("按订单过滤发布净菜加工计划遇到错误: ", e);
		}
	}

	@Test
	public void processTaskReleaseTestCase02() {
		ReporterCSS.title("测试点: 选择个别商品发布净菜加工计划");
		try {
			processTaskFilterParam.setQ_type(2);
			processTaskFilterParam.setQ(order_id);
			List<ProcessTaskBean> processTasks = processTaskService.waitingProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "搜索查询加工计划失败");

			Assert.assertEquals(processTasks.size() > 0, true, "订单" + order_id + "创建后1分钟,对应的净菜的加工计划没有生成");

			List<ProcessTaskFilterParam.Release> releases = new ArrayList<ProcessTaskFilterParam.Release>();
			ProcessTaskFilterParam.Release release = processTaskFilterParam.new Release();
			List<BigDecimal> ids = new ArrayList<BigDecimal>();
			ProcessTaskBean processTask = processTasks.get(0);
			for (ProcessTaskBean.Task task : processTask.getTasks()) {
				ids.add(task.getId());
			}
			release.setIds(ids);
			release.setAmount(processTask.getPlan_amount());
			releases.add(release);

			processTaskFilterParam.setAll(0);
			processTaskFilterParam.setRelease_list(releases);
			processTaskFilterParam.setQ(null);

			boolean result = processTaskService.releaseProcessTask(processTaskFilterParam);
			Assert.assertEquals(result, true, "按订单过滤发布净菜加工计划失败");

			// 再次查询,修改查询参数
			processTaskFilterParam.setRelease_list(null);
			processTaskFilterParam.setQ(order_id);

			processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "搜索查询加工计划失败");

			String msg = null;
			for (ProcessTaskBean pt : processTasks) {
				if (pt.getSku_id().equals(processTask.getSku_id())) {
					if (pt.getStatus() != 2) {
						msg = String.format("订单:%s里的净菜商品:%s对应的加工计划的状态与预期不符,预期:2,实际:%s", order_id, pt.getName(),
								pt.getStatus());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				} else {
					if (pt.getStatus() != 1) {
						msg = String.format("订单:%s里的净菜商品:%s对应的加工计划的状态与预期不符,预期:1,实际:%s", order_id, pt.getName(),
								pt.getStatus());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}

			ProcessOrderFilterParam processOrderFilterParam = new ProcessOrderFilterParam();
			processOrderFilterParam.setBegin(todayStr);
			processOrderFilterParam.setEnd(todayStr);
			processOrderFilterParam.setDate_type(5);
			processOrderFilterParam.setNeed_unrelease(1);

			List<ProcessOrderBean> processOrders = processOrderService.searchProcessOrder(processOrderFilterParam);
			Assert.assertNotEquals(processOrders, null, "按创建日期搜索过滤加工单据失败");

			// 验证加工单据生成的是否正确
			for (ProcessTaskBean pt : processTasks) {
				if (pt.getSku_id().equals(processTask.getSku_id())) {
					for (ProcessTaskBean.Task task : pt.getTasks()) {
						String expected_custom_id = pt.getSku_id() + "-" + task.getId();
						ProcessOrderBean processOrder = processOrders.stream()
								.filter(p -> p.getCustom_id().equals(expected_custom_id)).findAny().orElse(null);
						if (processOrder == null) {
							msg = String.format("净菜加工任务发布后,预期的计划编号:%s 加工单据没有出现", expected_custom_id);
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
							continue;
						}

						if (processOrder.getPlan_amount().compareTo(task.getOrder_amount()) != 0) {
							msg = String.format("净菜加工任务发布后,计划编号:%s 加工单据计划生成数与预期不符,预期:%s,实际:%s", expected_custom_id,
									task.getOrder_amount(), processOrder.getPlan_amount());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					}
				}
			}
			Assert.assertEquals(result, true, "选择个别商品发布净菜加工计划后,净菜加工任务信息与预期不符");
		} catch (Exception e) {
			logger.error("选择个别商品发布净菜加工计划遇到错误: ", e);
			Assert.fail("选择个别商品发布净菜加工计划遇到错误: ", e);
		}
	}

	@Test
	public void processTaskReleaseTestCase03() {
		ReporterCSS.title("测试点: 全选加工计划进行任务发布");
		try {
			processTaskFilterParam.setQ_type(2);
			processTaskFilterParam.setQ(order_id);
			List<ProcessTaskBean> processTasks = processTaskService.waitingProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "净菜加工计划搜索查询失败");

			Assert.assertEquals(processTasks.size() > 0, true, "订单" + order_id + "创建后1分钟,对应的净菜的加工计划没有生成");

			processTaskFilterParam.setIs_submit(0);
			processTaskFilterParam.setAll(1);
			processTaskFilterParam.setQ(null);

			processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "净菜加工计划搜索查询失败");

			processTasks = processTasks.stream().filter(p -> p.getStatus() == 1).collect(Collectors.toList());

			boolean result = processTaskService.releaseProcessTask(processTaskFilterParam);
			Assert.assertEquals(result, true, "全选加工计划进行任务发布失败");

			String msg = null;
			for (ProcessTaskBean processTask : processTasks) {
				if (processTask.getStatus() != 2) {
					msg = String.format("净菜商品:%s对应的任务状态与预期不符,预期:2,实际:%s", processTask.getName(),
							processTask.getStatus());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			ProcessOrderFilterParam processOrderFilterParam = new ProcessOrderFilterParam();
			processOrderFilterParam.setBegin(todayStr);
			processOrderFilterParam.setEnd(todayStr);
			processOrderFilterParam.setDate_type(5);
			processOrderFilterParam.setSource_type(0);
			processOrderFilterParam.setNeed_unrelease(1);

			List<ProcessOrderBean> processOrders = processOrderService.searchProcessOrder(processOrderFilterParam);
			Assert.assertNotEquals(processOrders, null, "按创建日期搜索过滤加工单据失败");

			result = compareData(processOrders, processTasks);

			Assert.assertEquals(result, true, "全选加工计划进行任务发布,净菜加工任务信息与预期不符");
		} catch (Exception e) {
			logger.error("全选加工计划进行任务发布遇到错误: ", e);
			Assert.fail("全选加工计划进行任务发布遇到错误: ", e);
		}
	}

	@Test
	public void processTaskReleaseTestCase04() {
		ReporterCSS.title("测试点: 按商品名过滤发布净菜加工任务");
		try {
			processTaskFilterParam.setQ_type(2);
			processTaskFilterParam.setQ(order_id);
			List<ProcessTaskBean> processTasks = processTaskService.waitingProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "净菜加工计划搜索查询失败");

			Assert.assertEquals(processTasks.size() > 0, true, "订单" + order_id + "创建后1分钟,对应的净菜的加工计划没有生成");

			// 选择一个商品
			ProcessTaskBean processTask = NumberUtil.roundNumberInList(processTasks);
			String sku_name = processTask.getName();

			processTaskFilterParam.setQ_type(1);
			processTaskFilterParam.setIs_submit(0);
			processTaskFilterParam.setAll(1);
			processTaskFilterParam.setQ(sku_name);

			boolean result = processTaskService.releaseProcessTask(processTaskFilterParam);
			Assert.assertEquals(result, true, "按商品名过滤发布净菜加工任务失败");

			processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "按商品名过滤搜索净菜加工任务失败");

			String msg = null;
			for (ProcessTaskBean pt : processTasks) {
				if (pt.getStatus() != 2) {
					msg = String.format("按商品名:%s过滤发布净菜加工任务,对应的商品:%s加工任务状态与预期不符,预期:2,实际:%s", sku_name, pt.getName(),
							pt.getStatus());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			ProcessOrderFilterParam processOrderFilterParam = new ProcessOrderFilterParam();
			processOrderFilterParam.setBegin(todayStr);
			processOrderFilterParam.setEnd(todayStr);
			processOrderFilterParam.setDate_type(5);
			processOrderFilterParam.setNeed_unrelease(1);

			List<ProcessOrderBean> processOrders = processOrderService.searchProcessOrder(processOrderFilterParam);
			Assert.assertNotEquals(processOrders, null, "按创建日期搜索过滤加工单据失败");

			result = compareData(processOrders, processTasks);

			Assert.assertEquals(result, true, "按商品名过滤发布净菜加工任务,净菜加工任务信息与预期不符");
		} catch (Exception e) {
			logger.error("全选加工计划进行任务发布遇到错误: ", e);
			Assert.fail("全选加工计划进行任务发布遇到错误: ", e);
		}
	}

	@Test
	public void processTaskReleaseTestCase05() {
		ReporterCSS.title("测试点: 按商户信息过滤发布净菜加工任务");
		try {
			String resname = orderDetail.getCustomer().getExtender().getResname();
			processTaskFilterParam.setQ_type(2);
			processTaskFilterParam.setQ(order_id);
			List<ProcessTaskBean> processTasks = processTaskService.waitingProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "净菜加工计划搜索查询失败");

			Assert.assertEquals(processTasks.size() > 0, true, "订单" + order_id + "创建后1分钟,对应的净菜的加工计划没有生成");

			processTaskFilterParam.setQ_type(3);
			processTaskFilterParam.setIs_submit(0);
			processTaskFilterParam.setAll(1);
			processTaskFilterParam.setQ(resname);

			processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "按商户信息搜索过滤净菜加工任务失败");

			processTasks = processTasks.stream().filter(p -> p.getStatus() == 1).collect(Collectors.toList());

			boolean result = processTaskService.releaseProcessTask(processTaskFilterParam);
			Assert.assertEquals(result, true, "按商户信息过滤发布净菜加工任务失败");

			String msg = null;
			for (ProcessTaskBean pt : processTasks) {
				if (pt.getStatus() != 2) {
					msg = String.format("按商户名:%s过滤发布净菜加工任务,对应的商品:%s加工任务状态与预期不符,预期:2,实际:%s", resname, pt.getName(),
							pt.getStatus());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			ProcessOrderFilterParam processOrderFilterParam = new ProcessOrderFilterParam();
			processOrderFilterParam.setBegin(todayStr);
			processOrderFilterParam.setEnd(todayStr);
			processOrderFilterParam.setDate_type(5);
			processOrderFilterParam.setNeed_unrelease(1);

			List<ProcessOrderBean> processOrders = processOrderService.searchProcessOrder(processOrderFilterParam);
			Assert.assertNotEquals(processOrders, null, "按创建日期搜索过滤加工单据失败");

			result = compareData(processOrders, processTasks);

			Assert.assertEquals(result, true, "按商户信息过滤发布净菜加工任务,净菜加工任务信息与预期不符");
		} catch (Exception e) {
			logger.error("按商户信息过滤发布净菜加工任务遇到错误: ", e);
			Assert.fail("按商户信息过滤发布净菜加工任务遇到错误: ", e);
		}
	}

	@Test
	public void processTaskReleaseTestCase06() {
		ReporterCSS.title("测试点: 按线路筛选过滤发布加工任务");
		try {
			String sid = orderDetail.getCustomer().getAddress_id();

			BigDecimal route_id = routeService.initRouteData();
			List<RouteBindCustomerBean> routeBindCustomers = routeService.getRouteBindCustomer(route_id);
			Assert.assertNotEquals(routeBindCustomers, null, "获取指定的线路详细信息失败");

			RouteBindCustomerBean routeBindCustomer = routeBindCustomers.stream()
					.filter(r -> String.valueOf(r.getAddress_id()).equals(sid)).findAny().orElse(null);

			route_id = routeBindCustomer.getRoute_id();
			String route_name = routeBindCustomer.getRoute_name();

			processTaskFilterParam.setQ_type(2);
			processTaskFilterParam.setQ(order_id);

			List<ProcessTaskBean> processTasks = processTaskService.waitingProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "净菜加工计划搜索查询失败");

			Assert.assertEquals(processTasks.size() > 0, true, "订单" + order_id + "创建后1分钟,对应的净菜的加工计划没有生成");

			processTaskFilterParam.setQ_type(2);
			processTaskFilterParam.setQ(null);
			processTaskFilterParam.setIs_submit(0);
			processTaskFilterParam.setAll(1);
			processTaskFilterParam.setRoute_id(route_id);

			boolean result = processTaskService.releaseProcessTask(processTaskFilterParam);
			Assert.assertEquals(result, true, "按线路筛选过滤发布净菜加工任务失败");

			processTaskFilterParam.setAll(null);
			processTaskFilterParam.setIs_submit(null);
			processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "按商户信息搜索过滤净菜加工任务失败");

			String msg = null;
			for (ProcessTaskBean pt : processTasks) {
				for (ProcessTaskBean.Task task : pt.getTasks())
					if (task.getRoute_name().equals(route_name)) {
						if (pt.getStatus() != 2) {
							msg = String.format("按线路:%s过滤发布净菜加工任务,对应商户:%s的商品:%s加工任务状态与预期不符,预期:2,实际:%s", route_name,
									task.getResname(), pt.getName(), pt.getStatus());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					}
			}

			ProcessOrderFilterParam processOrderFilterParam = new ProcessOrderFilterParam();
			processOrderFilterParam.setBegin(todayStr);
			processOrderFilterParam.setEnd(todayStr);
			processOrderFilterParam.setDate_type(5);
			processOrderFilterParam.setNeed_unrelease(1);

			List<ProcessOrderBean> processOrders = processOrderService.searchProcessOrder(processOrderFilterParam);
			Assert.assertNotEquals(processOrders, null, "按创建日期搜索过滤加工单据失败");

			result = compareData(processOrders, processTasks);

			Assert.assertEquals(result, true, "按线路筛选过滤发布净菜加工任务,净菜加工任务信息与预期不符");
		} catch (Exception e) {
			logger.error("按线路筛选过滤发布加工任务遇到错误: ", e);
			Assert.fail("按线路筛选过滤发布加工任务遇到错误: ", e);
		}
	}

	@Test
	public void processTaskReleaseTestCase07() {
		ReporterCSS.title("测试点: 按司机筛选过滤发布加工任务");
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

			processTaskFilterParam.setQ_type(2);
			processTaskFilterParam.setQ(order_id);

			List<ProcessTaskBean> processTasks = processTaskService.waitingProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "净菜加工计划搜索查询失败");

			Assert.assertEquals(processTasks.size() > 0, true, "订单" + order_id + "创建后1分钟,对应的净菜的加工计划没有生成");

			List<BigDecimal> driver_ids = new ArrayList<BigDecimal>();
			driver_ids.add(driver_id);
			processTaskFilterParam.setQ_type(2);
			processTaskFilterParam.setQ(null);
			processTaskFilterParam.setIs_submit(0);
			processTaskFilterParam.setAll(1);
			processTaskFilterParam.setDriver_ids(driver_ids);

			processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "按司机搜索过滤净菜加工任务失败");
			processTasks = processTasks.stream().filter(p -> p.getStatus() == 1).collect(Collectors.toList());

			result = processTaskService.releaseProcessTask(processTaskFilterParam);
			Assert.assertEquals(result, true, "按司机筛选过滤发布净菜加工任务失败");

			Assert.assertEquals(processTasks.size() > 0, true, "按司机搜索过滤净菜加工任务,结果为0,与预期不符");

			String msg = null;
			for (ProcessTaskBean processTask : processTasks) {
				if (processTask.getStatus() != 2) {
					msg = String.format("按司机:%s 发布净菜加工任务后,商品:%s 的加工任务状态值与预期不符,预期:2,实际:%s", driver_name,
							processTask.getName(), processTask.getStatus());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			ProcessOrderFilterParam processOrderFilterParam = new ProcessOrderFilterParam();
			processOrderFilterParam.setBegin(todayStr);
			processOrderFilterParam.setEnd(todayStr);
			processOrderFilterParam.setDate_type(5);
			processOrderFilterParam.setNeed_unrelease(1);

			List<ProcessOrderBean> processOrders = processOrderService.searchProcessOrder(processOrderFilterParam);
			Assert.assertNotEquals(processOrders, null, "按创建日期搜索过滤加工单据失败");

			result = compareData(processOrders, processTasks);

			Assert.assertEquals(result, true, "按司机搜索过滤发布净菜加工任务,净菜加工任务信息与预期不符");
		} catch (Exception e) {
			logger.error("按线路筛选过滤发布加工任务遇到错误: ", e);
			Assert.fail("按线路筛选过滤发布加工任务遇到错误: ", e);
		}
	}

	@Test
	public void processTaskReleaseTestCase08() {
		ReporterCSS.title("测试点: 按商品标签过滤发布加工任务");
		try {
			processTaskFilterParam.setQ_type(2);
			processTaskFilterParam.setQ(order_id);

			List<ProcessTaskBean> processTasks = processTaskService.waitingProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "净菜加工计划搜索查询失败");

			Assert.assertEquals(processTasks.size() > 0, true, "订单" + order_id + "创建后1分钟,对应的净菜的加工计划没有生成");

			List<BigDecimal> label_ids = new ArrayList<BigDecimal>();
			label_ids.add(label_id);
			processTaskFilterParam.setQ_type(2);
			processTaskFilterParam.setQ(null);
			processTaskFilterParam.setIs_submit(0);
			processTaskFilterParam.setAll(1);
			processTaskFilterParam.setProcess_labels(label_ids);

			boolean result = processTaskService.releaseProcessTask(processTaskFilterParam);
			Assert.assertEquals(result, true, "按商品标签过滤发布加工任务失败");

			processTaskFilterParam.setAll(null);
			processTaskFilterParam.setIs_submit(null);
			processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "按商品标签搜索过滤净菜加工任务失败");

			Assert.assertEquals(processTasks.size() > 0, true, "按商品标签搜索过滤净菜加工任务,结果为0,与预期不符");

			String msg = null;
			for (ProcessTaskBean processTask : processTasks) {
				if (processTask.getStatus() != 2) {
					msg = String.format("按商品标签:%s 发布净菜加工任务后,商品:%s 的加工任务状态值与预期不符,预期:2,实际:%s", label_id,
							processTask.getName(), processTask.getStatus());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			ProcessOrderFilterParam processOrderFilterParam = new ProcessOrderFilterParam();
			processOrderFilterParam.setBegin(todayStr);
			processOrderFilterParam.setEnd(todayStr);
			processOrderFilterParam.setDate_type(5);
			processOrderFilterParam.setNeed_unrelease(1);

			List<ProcessOrderBean> processOrders = processOrderService.searchProcessOrder(processOrderFilterParam);
			Assert.assertNotEquals(processOrders, null, "按创建日期搜索过滤加工单据失败");

			result = compareData(processOrders, processTasks);

			Assert.assertEquals(result, true, "按商品标签搜索过滤发布净菜加工任务,净菜加工任务信息与预期不符");
		} catch (Exception e) {
			logger.error("按商品标签筛选过滤发布加工任务遇到错误: ", e);
			Assert.fail("按商品标签筛选过滤发布加工任务遇到错误: ", e);
		}
	}

	@Test
	public void processTaskReleaseTestCase09() {
		ReporterCSS.title("测试点: 按订单过滤发布并下达净菜加工单据");
		try {
			processTaskFilterParam.setQ_type(2);
			processTaskFilterParam.setQ(order_id);
			List<ProcessTaskBean> processTasks = processTaskService.waitingProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "搜索查询加工计划失败");

			Assert.assertEquals(processTasks.size() > 0, true, "订单" + order_id + "创建后1分钟,对应的净菜的加工计划没有生成");

			processTaskFilterParam.setAll(1);
			processTaskFilterParam.setIs_submit(1);

			boolean result = processTaskService.releaseProcessTask(processTaskFilterParam);
			Assert.assertEquals(result, true, "按订单过滤发布并下达净菜加工单据失败");

			processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "搜索查询加工计划失败");

			String msg = null;
			for (ProcessTaskBean processTask : processTasks) {
				if (processTask.getStatus() != 2) {
					msg = String.format("订单%s里的净菜商品:%s,加工计划对应的状态值与预期不符,预期:%s,实际:%s", order_id, processTask.getSku_id(),
							2, processTask.getStatus());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			ProcessOrderFilterParam processOrderFilterParam = new ProcessOrderFilterParam();
			processOrderFilterParam.setBegin(todayStr);
			processOrderFilterParam.setEnd(todayStr);
			processOrderFilterParam.setDate_type(5);
			processOrderFilterParam.setNeed_unrelease(1);

			List<ProcessOrderBean> processOrders = processOrderService.searchProcessOrder(processOrderFilterParam);
			Assert.assertNotEquals(processOrders, null, "按创建日期搜索过滤加工单据失败");

			result = compareData(processOrders, processTasks);

			Assert.assertEquals(result, true, "按订单过滤发布并下达净菜加工单据后,净菜加工任务信息与预期不符");
		} catch (Exception e) {
			logger.error("按订单过滤发布并下达净菜加工单据遇到错误: ", e);
			Assert.fail("按订单过滤发布并下达净菜加工单据遇到错误: ", e);
		}
	}

	@Test
	public void processTaskReleaseTestCase10() {
		ReporterCSS.title("测试点: 选择个别净菜加工任务发布并下达加工单据");
		try {
			processTaskFilterParam.setQ_type(2);
			processTaskFilterParam.setQ(order_id);
			List<ProcessTaskBean> processTasks = processTaskService.waitingProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "搜索查询加工计划失败");

			Assert.assertEquals(processTasks.size() > 0, true, "订单" + order_id + "创建后1分钟,对应的净菜的加工计划没有生成");

			processTaskFilterParam.setAll(0);
			processTaskFilterParam.setIs_submit(1);
			processTaskFilterParam.setQ(null);

			List<ProcessTaskFilterParam.Release> release_list = new ArrayList<ProcessTaskFilterParam.Release>();

			ProcessTaskFilterParam.Release release = null;
			for (ProcessTaskBean processTask : processTasks) {
				release = processTaskFilterParam.new Release();
				List<BigDecimal> ids = new ArrayList<BigDecimal>();
				for (ProcessTaskBean.Task task : processTask.getTasks()) {
					ids.add(task.getId());
				}
				release.setIds(ids);
				release.setAmount(processTask.getPlan_amount());
				release_list.add(release);
			}
			processTaskFilterParam.setRelease_list(release_list);

			boolean result = processTaskService.releaseProcessTask(processTaskFilterParam);
			Assert.assertEquals(result, true, "选择个别净菜加工任务发布并下达加工单据失败");

			// 再次搜索查找
			processTaskFilterParam.setQ(order_id);

			processTasks = processTaskService.searchProcessTask(processTaskFilterParam);
			Assert.assertNotEquals(processTasks, null, "搜索查询加工计划失败");

			String msg = null;
			for (ProcessTaskBean processTask : processTasks) {
				if (processTask.getStatus() != 2) {
					msg = String.format("订单%s里的净菜商品:%s,加工计划对应的状态值与预期不符,预期:%s,实际:%s", order_id, processTask.getSku_id(),
							2, processTask.getStatus());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			ProcessOrderFilterParam processOrderFilterParam = new ProcessOrderFilterParam();
			processOrderFilterParam.setBegin(todayStr);
			processOrderFilterParam.setEnd(todayStr);
			processOrderFilterParam.setDate_type(5);
			processOrderFilterParam.setNeed_unrelease(1);

			List<ProcessOrderBean> processOrders = processOrderService.searchProcessOrder(processOrderFilterParam);
			Assert.assertNotEquals(processOrders, null, "按创建日期搜索过滤加工单据失败");

			result = compareData(processOrders, processTasks);

			Assert.assertEquals(result, true, "选择个别净菜加工任务发布并下达加工单据后,净菜加工任务信息与预期不符");
		} catch (Exception e) {
			logger.error("选择个别净菜加工任务发布并下达加工单据遇到错误: ", e);
			Assert.fail("选择个别净菜加工任务发布并下达加工单据遇到错误: ", e);
		}
	}

}
