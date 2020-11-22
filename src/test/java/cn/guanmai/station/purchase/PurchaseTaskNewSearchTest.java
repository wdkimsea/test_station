package cn.guanmai.station.purchase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.category.PurchaseSpecBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.bean.order.OrderBean;
import cn.guanmai.station.bean.order.OrderCycle;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderDetailBean.Detail;
import cn.guanmai.station.bean.order.param.OrderFilterParam;
import cn.guanmai.station.bean.purchase.PurchaseTaskExpectedBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskHistoryBean;
import cn.guanmai.station.bean.purchase.PurchaserBean;
import cn.guanmai.station.bean.purchase.PurchaserResponseBean;
import cn.guanmai.station.bean.purchase.PurcahseTaskSummaryBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean.PurchaseTaskData;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean.PurchaseTaskData.Task;
import cn.guanmai.station.bean.purchase.PurchaserBean.SettleSupplier;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskFilterParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskHistoryFilterParam;
import cn.guanmai.station.bean.purchase.param.PurchaserParam;
import cn.guanmai.station.bean.system.param.OrderProfileParam;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.delivery.RouteServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaseTaskServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaserServiceImpl;
import cn.guanmai.station.impl.system.ProfileServiceImpl;
import cn.guanmai.station.impl.weight.WeightServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.delivery.RouteService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.purchase.PurchaseTaskService;
import cn.guanmai.station.interfaces.purchase.PurchaserService;
import cn.guanmai.station.interfaces.system.ProfileService;
import cn.guanmai.station.interfaces.weight.WeightService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.station.tools.PurchaseTaskTool;
import cn.guanmai.util.JsonUtil;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/**
 * @author: liming
 * @Date: 2020年2月12日 上午10:01:08
 * @description: 采购任务从ES拉取验证
 * @version: 1.0
 */

public class PurchaseTaskNewSearchTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PurchaseTaskNewSearchTest.class);
	private OrderTool orderTool;
	private OrderService orderService;
	private PurchaseTaskService purchaseTaskService;
	private PurchaseTaskTool purchaseTaskTool;
	private PurchaserService purchaserService;
	private CategoryService categoryService;
	private List<PurchaseTaskExpectedBean> purchaseTaskExpectedList;
	private OrderCycle orderCycle;
	private RouteService routeService;
	private WeightService weightService;
	private OrderDetailBean orderDetail;
	private String order_id;
	private int limit = 50;
	private int offset = 0;
	private PurchaseTaskFilterParam purchaseTaskFilterParamType1; // 按下单日期
	private PurchaseTaskFilterParam purchaseTaskFilterParamType2; // 按运营时间
	private PurchaseTaskFilterParam purchaseTaskFilterParamType3; // 按收货日期

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		orderTool = new OrderTool(headers);
		routeService = new RouteServiceImpl(headers);
		purchaseTaskService = new PurchaseTaskServiceImpl(headers);
		purchaserService = new PurchaserServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		purchaseTaskTool = new PurchaseTaskTool(headers);
		orderService = new OrderServiceImpl(headers);
		weightService = new WeightServiceImpl(headers);
		try {
			ProfileService profileService = new ProfileServiceImpl(headers);
			OrderProfileParam orderProfileParam = new OrderProfileParam();
			orderProfileParam.setOrder_create_purchase_task(0);
			boolean result = profileService.updateOrderProfile(orderProfileParam);
			Assert.assertEquals(result, true, "设置订单商品自动进入采购任务失败");

			// 把没有绑定采购员的供应商都绑定采购员
			List<SettleSupplier> settleSupplierArray = purchaserService.getNoBindSettleSupplierArray();
			Assert.assertNotEquals(settleSupplierArray, null, "获取没有绑定采购员的供应商列表失败");
			if (settleSupplierArray.size() > 0) {
				List<String> settle_suppliers = new ArrayList<String>();
				settleSupplierArray.stream().forEach(s -> {
					settle_suppliers.add(s.getId());
				});

				List<PurchaserBean> purchasers = purchaserService.searchPurchaser("");
				Assert.assertNotEquals(purchasers, null, "搜索过滤采购员失败");
				if (purchasers.size() > 0) {
					PurchaserBean purchaser = NumberUtil.roundNumberInList(purchasers);
					// 原有绑定的供应商加上没绑定的供应商
					for (SettleSupplier settleSupplier : purchaser.getSettle_suppliers()) {
						settle_suppliers.add(settleSupplier.getId());
					}

					PurchaserParam purchaserParam = new PurchaserParam();
					purchaserParam.setId(purchaser.getId());
					purchaserParam.setName(purchaser.getName());
					purchaserParam.setUsername(purchaser.getUsername());
					purchaserParam.setPhone(purchaser.getPhone());
					purchaserParam.setStatus(purchaser.getStatus());
					purchaserParam.setIs_allow_login(purchaser.getIs_allow_login());
					purchaserParam.setSettle_suppliers(settle_suppliers);

					PurchaserResponseBean purchaserResponse = purchaserService.updatePurchaser(purchaserParam);
					Assert.assertEquals(purchaserResponse.getCode(), 0, "修改采购员信息失败");
				} else {
					String phone = "12" + StringUtil.getRandomNumber(9);
					String username = "AT" + StringUtil.getRandomNumber(6);

					PurchaserParam purchaserParam = new PurchaserParam();
					purchaserParam.setUsername(username);
					purchaserParam.setName(username);
					purchaserParam.setPhone(phone);
					purchaserParam.setPassword("123456");
					purchaserParam.setSettle_suppliers(new ArrayList<>());
					purchaserParam.setIs_allow_login(0);
					purchaserParam.setStatus(1);

					PurchaserResponseBean purchaserResponse = purchaserService.createPurchaser(purchaserParam);
					Assert.assertEquals(purchaserResponse.getMsg(), "ok", "创建采购员失败");
				}
			}

			// 创建订单
			order_id = orderTool.oneStepCreateOrder(10);
			Assert.assertNotEquals(order_id, null, "创建订单失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + "详细信息失败");

			orderCycle = orderTool.getOrderOperationCycle(order_id);

			purchaseTaskExpectedList = purchaseTaskTool.getOrderExpectedPurcahseTask(order_id);
			Assert.assertNotEquals(purchaseTaskExpectedList, null, "获取订单 " + order_id + " 预期的采购任务失败");

			List<PurchaseTaskData> purchaseTaskDataArray = purchaseTaskTool.getOrderPurcahseTask(order_id);
			Assert.assertEquals(purchaseTaskDataArray != null && purchaseTaskDataArray.size() > 0, true,
					"订单" + order_id + "的采购任务在45秒内没有生成");

		} catch (Exception e) {
			logger.error("初始化数据失败: ", e);
			Assert.fail("初始化数据失败: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		// 按下单日期
		purchaseTaskFilterParamType1 = new PurchaseTaskFilterParam();
		String begin_time1 = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
		purchaseTaskFilterParamType1.setQ_type(1);
		purchaseTaskFilterParamType1.setBegin_time(begin_time1);
		purchaseTaskFilterParamType1.setEnd_time(begin_time1);
		purchaseTaskFilterParamType1.setLimit(limit);

		// 按运营时间
		purchaseTaskFilterParamType2 = new PurchaseTaskFilterParam();
		String begin_time2 = orderCycle.getCycle_start_time() + ":00";
		String end_time2 = orderCycle.getCycle_end_time() + ":00";
		purchaseTaskFilterParamType2.setQ_type(2);
		purchaseTaskFilterParamType2.setTime_config_id(orderCycle.getTime_config_id());
		purchaseTaskFilterParamType2.setBegin_time(begin_time2);
		purchaseTaskFilterParamType2.setEnd_time(end_time2);
		purchaseTaskFilterParamType2.setLimit(limit);

		// 按收货日期
		purchaseTaskFilterParamType3 = new PurchaseTaskFilterParam();
		String begin_time3 = orderDetail.getCustomer().getReceive_begin_time().substring(0, 10) + " 00:00:00";
		purchaseTaskFilterParamType3.setQ_type(3);
		purchaseTaskFilterParamType3.setBegin_time(begin_time3);
		purchaseTaskFilterParamType3.setEnd_time(begin_time3);
		purchaseTaskFilterParamType3.setLimit(limit);

		offset = 0;
	}

	private boolean compareResult(List<PurchaseTaskExpectedBean> purchaseTaskExpectedList,
			Map<String, BigDecimal> purchaseActualMap) {
		boolean result = true;
		String msg = null;
		String spec_id = null;
		String supplier_id = null;
		String key = null;
		for (PurchaseTaskExpectedBean purchaseTaskExpected : purchaseTaskExpectedList) {
			spec_id = purchaseTaskExpected.getSpec_id();
			supplier_id = purchaseTaskExpected.getSupplier_id();
			key = spec_id + "-" + supplier_id;
			msg = JsonUtil.objectToStr(purchaseTaskExpected);
			if (purchaseActualMap.containsKey(key)) {
				if (purchaseActualMap.get(key).setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(
						purchaseTaskExpected.getPlan_purchase_amount().setScale(2, BigDecimal.ROUND_HALF_UP)) != 0) {
					msg = msg + "生成的采购数不正确,实际生成的采购数: " + purchaseActualMap.get(key);
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}
			} else {
				msg = msg + "采购任务没有生成";
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
		}
		ReporterCSS.title("预期的采购任务为 " + JsonUtil.objectToStr(purchaseTaskExpectedList));
		return result;

	}

	private Map<String, BigDecimal> getActualPurcahseTaskMap(List<PurchaseTaskData> purchaseTaskDataList) {
		Map<String, BigDecimal> actualPurchaseMap = new HashMap<>();
		String spec_id = null;
		String supplier_id = null;
		String actual_key = null;
		for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
			spec_id = purchaseTaskData.getSpec_id();
			supplier_id = purchaseTaskData.getSettle_supplier_id();
			actual_key = spec_id + "-" + supplier_id;
			for (Task task : purchaseTaskData.getTasks()) {
				if (task.getOrder_id() != null && task.getOrder_id().equals(order_id)) {
					BigDecimal plan_purchase_amount = task.getPlan_purchase_amount();
					if (actualPurchaseMap.containsKey(actual_key)) {
						actualPurchaseMap.put(actual_key,
								actualPurchaseMap.get(actual_key).add(plan_purchase_amount).stripTrailingZeros());
					} else {
						actualPurchaseMap.put(actual_key, plan_purchase_amount.stripTrailingZeros());
					}
				}
			}
		}
		return actualPurchaseMap;
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase01() {
		ReporterCSS.title("测试点: 采购任务ES,按下单日期搜索过滤");
		try {
			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单日期搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			Map<String, BigDecimal> actualPurchaseMap = getActualPurcahseTaskMap(purchaseTaskDataList);

			boolean result = compareResult(purchaseTaskExpectedList, actualPurchaseMap);
			Assert.assertEquals(result, true, "订单 " + order_id + " 预期查到的采购任务和实际从ES查到的采购任务不一致");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType1);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase02() {
		ReporterCSS.title("测试点: 采购任务ES,按运营时间搜索过滤");
		try {
			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType2.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按运营时间搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			Map<String, BigDecimal> actualPurchaseMap = getActualPurcahseTaskMap(purchaseTaskDataList);

			boolean result = compareResult(purchaseTaskExpectedList, actualPurchaseMap);
			Assert.assertEquals(result, true, "订单 " + order_id + " 预期查到的采购任务和实际从ES查到的采购任务不一致");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType2);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase03() {
		ReporterCSS.title("测试点: 采购任务ES,按收货时间搜索过滤");
		try {
			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType3.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货时间搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			Map<String, BigDecimal> actualPurchaseMap = getActualPurcahseTaskMap(purchaseTaskDataList);

			boolean result = compareResult(purchaseTaskExpectedList, actualPurchaseMap);
			Assert.assertEquals(result, true, "订单 " + order_id + " 预期查到的采购任务和实际从ES查到的采购任务不一致");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType3);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase04() {
		ReporterCSS.title("测试点: 采购任务ES,按下单时间+订单号搜索过滤采购任务");
		try {
			purchaseTaskFilterParamType1.setQ(order_id);
			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单时间+订单号搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			boolean result = true;
			String msg = null;
			for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				for (PurchaseTaskData.Task task : purchaseTaskData.getTasks()) {
					if (!task.getOrder_id().equals(order_id)) {
						msg = String.format("按下单时间+订单 %s 过滤采购任务,过滤出了其他的订单%s的采购任务", order_id, task.getOrder_id());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "按下单时间+订单号过滤采购任务,过滤出了不符合过滤条件的采购任务");

			Map<String, BigDecimal> actualPurchaseMap = getActualPurcahseTaskMap(purchaseTaskDataList);

			result = compareResult(purchaseTaskExpectedList, actualPurchaseMap);
			Assert.assertEquals(result, true, "订单 " + order_id + " 预期查到的采购任务和实际从ES查到的采购任务不一致");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType1);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase05() {
		ReporterCSS.title("测试点: 采购任务ES,按运营时间+订单号搜索过滤");
		try {
			purchaseTaskFilterParamType2.setQ(order_id);
			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType2.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单时间+订单号搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			boolean result = true;
			String msg = null;
			for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				for (PurchaseTaskData.Task task : purchaseTaskData.getTasks()) {
					if (!task.getOrder_id().equals(order_id)) {
						msg = String.format("按运营时间+订单 %s 过滤采购任务,过滤出了其他的订单%s的采购任务", order_id, task.getOrder_id());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "按运营时间+订单号过滤采购任务,过滤出了不符合过滤条件的采购任务");

			Map<String, BigDecimal> actualPurchaseMap = getActualPurcahseTaskMap(purchaseTaskDataList);

			result = compareResult(purchaseTaskExpectedList, actualPurchaseMap);
			Assert.assertEquals(result, true, "订单 " + order_id + " 预期查到的采购任务和实际从ES查到的采购任务不一致");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType2);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase06() {
		ReporterCSS.title("测试点: 采购任务ES,按收货时间+订单号搜索过滤");
		try {
			purchaseTaskFilterParamType3.setQ(order_id);
			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType3.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货时间+订单号搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			boolean result = true;
			String msg = null;
			for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				for (PurchaseTaskData.Task task : purchaseTaskData.getTasks()) {
					if (!task.getOrder_id().equals(order_id)) {
						msg = String.format("按收货时间+订单 %s 过滤采购任务,过滤出了其他的订单%s的采购任务", order_id, task.getOrder_id());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "按收货时间+订单号过滤采购任务,过滤出了不符合过滤条件的采购任务");

			Map<String, BigDecimal> actualPurchaseMap = getActualPurcahseTaskMap(purchaseTaskDataList);

			result = compareResult(purchaseTaskExpectedList, actualPurchaseMap);
			Assert.assertEquals(result, true, "订单 " + order_id + " 预期查到的采购任务和实际从ES查到的采购任务不一致");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType3);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase07() {
		ReporterCSS.title("测试点: 采购任务ES,按下单日期+商品名称过滤");
		try {
			PurchaseTaskExpectedBean purchaseTaskExpected = NumberUtil.roundNumberInList(purchaseTaskExpectedList);
			String spec_id = purchaseTaskExpected.getSpec_id();
			// 取一个采购规格的名称
			PurchaseSpecBean purchaseSpec = categoryService.getPurchaseSpecById(spec_id);
			Assert.assertNotEquals(purchaseSpec, null, "获取采购规格" + spec_id + "详细信息失败");
			String purchase_name = purchaseSpec.getName();
			purchaseTaskFilterParamType1.setQ(purchase_name);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单时间+商品名称搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> spce_names = purchaseTaskDataList.stream().filter(p -> !p.getName().contains(purchase_name))
					.map(p -> p.getName()).collect(Collectors.toList());
			if (spce_names.size() > 0) {
				String msg = String.format("按下单日期+商品名称[%s]过滤采购任务,过滤出了如下%s不符合的采购任务", purchase_name, spce_names);
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}
			Assert.assertEquals(spce_names.size(), 0, "按下单日期+商品名称过滤采购任务,过滤出了不符合过滤条件的采购任务");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType1);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase08() {
		ReporterCSS.title("测试点: 采购任务ES,按运营时间+商品名称搜索过滤");
		try {
			PurchaseTaskExpectedBean purchaseTaskExpected = NumberUtil.roundNumberInList(purchaseTaskExpectedList);
			String spec_id = purchaseTaskExpected.getSpec_id();
			// 取一个采购规格的名称
			PurchaseSpecBean purchaseSpec = categoryService.getPurchaseSpecById(spec_id);
			Assert.assertNotEquals(purchaseSpec, null, "获取采购规格" + spec_id + "详细信息失败");
			String purchase_name = purchaseSpec.getName();
			purchaseTaskFilterParamType2.setQ(purchase_name);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType2.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按运营时间+商品名称搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> spce_names = purchaseTaskDataList.stream().filter(p -> !p.getName().contains(purchase_name))
					.map(p -> p.getName()).collect(Collectors.toList());
			if (spce_names.size() > 0) {
				String msg = String.format("按运营时间+商品名称[%s]过滤采购任务,过滤出了如下%s不符合的采购任务", purchase_name, spce_names);
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}
			Assert.assertEquals(spce_names.size(), 0, "按运营时间+商品名称过滤采购任务,过滤出了不符合过滤条件的采购任务");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType2);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase09() {
		ReporterCSS.title("测试点: 采购任务ES,按收货时间+商品名称搜索过滤");
		try {
			PurchaseTaskExpectedBean purchaseTaskExpected = NumberUtil.roundNumberInList(purchaseTaskExpectedList);
			String spec_id = purchaseTaskExpected.getSpec_id();
			// 取一个采购规格的名称
			PurchaseSpecBean purchaseSpec = categoryService.getPurchaseSpecById(spec_id);
			Assert.assertNotEquals(purchaseSpec, null, "获取采购规格" + spec_id + "详细信息失败");
			String purchase_name = purchaseSpec.getName();
			purchaseTaskFilterParamType3.setQ(purchase_name);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType3.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货时间+商品名称搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> spce_names = purchaseTaskDataList.stream().filter(p -> !p.getName().contains(purchase_name))
					.map(p -> p.getName()).collect(Collectors.toList());
			if (spce_names.size() > 0) {
				String msg = String.format("按收货时间+商品名称[%s]过滤采购任务,过滤出了如下%s不符合的采购任务", purchase_name, spce_names);
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}
			Assert.assertEquals(spce_names.size(), 0, "按收货时间+商品名称过滤采购任务,过滤出了不符合过滤条件的采购任务");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType3);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase10() {
		ReporterCSS.title("测试点: 采购任务ES,按下单日期+一级分类过滤采购任务");
		try {
			purchaseTaskFilterParamType1.setOffset(offset);
			// 取一个下单商品,获取对应的一级分类信息
			int index = new Random().nextInt(orderDetail.getDetails().size());
			Detail order_sku = orderDetail.getDetails().get(index);
			String spu_id = order_sku.getSpu_id();
			SpuBean spu = categoryService.getSpuById(spu_id);
			Assert.assertNotEquals(spu, null, "获取商品 " + spu_id + " 详细信息失败");
			String category1_id = spu.getCategory_id_1();
			String category1_name = spu.getCategory_name_1();
			List<String> category1_ids = new ArrayList<String>();
			category1_ids.add(category1_id);
			purchaseTaskFilterParamType1.setCategory1_ids(category1_ids);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单时间+一级分类搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> category1_names = purchaseTaskDataList.stream()
					.filter(p -> !p.getCategory1_name().equals(category1_name)).map(p -> p.getCategory1_name())
					.collect(Collectors.toList());
			Assert.assertEquals(category1_names.size(), 0,
					"按下单日期+一级分类[" + category1_name + "]过滤采购任务,过滤出了如下不符合的一级分类" + category1_names + "的采购任务");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType1);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase11() {
		ReporterCSS.title("测试点: 采购任务ES,按运营周期+一级分类过滤采购任务");
		try {
			purchaseTaskFilterParamType2.setOffset(offset);
			// 取一个下单商品,获取对应的一级分类信息
			int index = new Random().nextInt(orderDetail.getDetails().size());
			Detail order_sku = orderDetail.getDetails().get(index);
			String spu_id = order_sku.getSpu_id();
			SpuBean spu = categoryService.getSpuById(spu_id);
			Assert.assertNotEquals(spu, null, "获取商品 " + spu_id + " 详细信息失败");
			String category1_id = spu.getCategory_id_1();
			String category1_name = spu.getCategory_name_1();
			List<String> category1_ids = new ArrayList<String>();
			category1_ids.add(category1_id);
			purchaseTaskFilterParamType2.setCategory1_ids(category1_ids);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType2.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按运营周期+一级分类搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> category1_names = purchaseTaskDataList.stream()
					.filter(p -> !p.getCategory1_name().equals(category1_name)).map(p -> p.getCategory1_name())
					.collect(Collectors.toList());
			Assert.assertEquals(category1_names.size(), 0,
					"按运营周期+一级分类[" + category1_name + "]过滤采购任务,过滤出了如下不符合的一级分类" + category1_names + "的采购任务");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType2);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase12() {
		ReporterCSS.title("测试点: 采购任务ES,按收货日期+一级分类过滤采购任务");
		try {
			purchaseTaskFilterParamType3.setOffset(offset);
			// 取一个下单商品,获取对应的一级分类信息
			int index = new Random().nextInt(orderDetail.getDetails().size());
			Detail order_sku = orderDetail.getDetails().get(index);
			String spu_id = order_sku.getSpu_id();
			SpuBean spu = categoryService.getSpuById(spu_id);
			Assert.assertNotEquals(spu, null, "获取商品 " + spu_id + " 详细信息失败");
			String category1_id = spu.getCategory_id_1();
			String category1_name = spu.getCategory_name_1();
			List<String> category1_ids = new ArrayList<String>();
			category1_ids.add(category1_id);
			purchaseTaskFilterParamType3.setCategory1_ids(category1_ids);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType3.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货日期+一级分类搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> category1_names = purchaseTaskDataList.stream()
					.filter(p -> !p.getCategory1_name().equals(category1_name)).map(p -> p.getCategory1_name())
					.collect(Collectors.toList());
			Assert.assertEquals(category1_names.size(), 0,
					"收货日期+一级分类[" + category1_name + "]过滤采购任务,过滤出了如下不符合的一级分类" + category1_names + "的采购任务");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType3);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase13() {
		ReporterCSS.title("测试点: 采购任务ES,按下单日期+二级分类过滤采购任务");
		try {
			purchaseTaskFilterParamType1.setOffset(offset);
			// 取一个下单商品,获取对应的一级分类信息
			int index = new Random().nextInt(orderDetail.getDetails().size());
			Detail order_sku = orderDetail.getDetails().get(index);
			String spu_id = order_sku.getSpu_id();
			SpuBean spu = categoryService.getSpuById(spu_id);
			Assert.assertNotEquals(spu, null, "获取商品 " + spu_id + " 详细信息失败");
			String category1_id = spu.getCategory_id_1();
			// String category1_name = spu.getCategory_name_1();

			String category2_id = spu.getCategory_id_2();
			String category2_name = spu.getCategory_name_2();

			List<String> category1_ids = new ArrayList<String>();
			category1_ids.add(category1_id);
			purchaseTaskFilterParamType1.setCategory1_ids(category1_ids);

			List<String> category2_ids = new ArrayList<String>();
			category2_ids.add(category2_id);
			purchaseTaskFilterParamType1.setCategory1_ids(category1_ids);
			purchaseTaskFilterParamType1.setCategory2_ids(category2_ids);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单时间+二级分类搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> category2_names = purchaseTaskDataList.stream()
					.filter(p -> !p.getCategory2_name().equals(category2_name)).map(p -> p.getCategory2_name())
					.collect(Collectors.toList());
			Assert.assertEquals(category2_names.size(), 0,
					"按下单日期+二级分类[" + category2_name + "]过滤采购任务,过滤出了如下不符合的二级分类" + category2_names + "的采购任务");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType1);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase14() {
		ReporterCSS.title("测试点: 采购任务ES,按运营周期+二级分类过滤采购任务");
		try {
			purchaseTaskFilterParamType2.setOffset(offset);
			// 取一个下单商品,获取对应的一级分类信息
			int index = new Random().nextInt(orderDetail.getDetails().size());
			Detail order_sku = orderDetail.getDetails().get(index);
			String spu_id = order_sku.getSpu_id();
			SpuBean spu = categoryService.getSpuById(spu_id);
			Assert.assertNotEquals(spu, null, "获取商品 " + spu_id + " 详细信息失败");
			String category1_id = spu.getCategory_id_1();
			// String category1_name = spu.getCategory_name_1();

			String category2_id = spu.getCategory_id_2();
			String category2_name = spu.getCategory_name_2();
			List<String> category1_ids = new ArrayList<String>();
			category1_ids.add(category1_id);
			purchaseTaskFilterParamType2.setCategory1_ids(category1_ids);

			List<String> category2_ids = new ArrayList<String>();
			category2_ids.add(category2_id);
			purchaseTaskFilterParamType2.setCategory2_ids(category2_ids);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType2.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按运营周期+二级分类搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> category2_names = purchaseTaskDataList.stream()
					.filter(p -> !p.getCategory2_name().equals(category2_name)).map(p -> p.getCategory2_name())
					.collect(Collectors.toList());
			Assert.assertEquals(category2_names.size(), 0,
					"按运营周期+二级分类[" + category2_name + "]过滤采购任务,过滤出了如下不符合的二级分类" + category2_names + "的采购任务");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType2);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase15() {
		ReporterCSS.title("测试点: 采购任务ES,按收货日期+二级分类过滤采购任务");
		try {
			purchaseTaskFilterParamType3.setOffset(offset);
			// 取一个下单商品,获取对应的一级分类信息
			int index = new Random().nextInt(orderDetail.getDetails().size());
			Detail order_sku = orderDetail.getDetails().get(index);
			String spu_id = order_sku.getSpu_id();
			SpuBean spu = categoryService.getSpuById(spu_id);
			Assert.assertNotEquals(spu, null, "获取商品 " + spu_id + " 详细信息失败");
			String category1_id = spu.getCategory_id_1();
			// String category1_name = spu.getCategory_name_1();
			List<String> category1_ids = new ArrayList<String>();
			category1_ids.add(category1_id);
			purchaseTaskFilterParamType3.setCategory1_ids(category1_ids);

			String category2_id = spu.getCategory_id_1();
			String category2_name = spu.getCategory_name_1();
			List<String> category2_ids = new ArrayList<String>();
			category2_ids.add(category2_id);
			purchaseTaskFilterParamType3.setCategory2_ids(category2_ids);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType3.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货日期+二级分类搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> category2_names = purchaseTaskDataList.stream()
					.filter(p -> !p.getCategory2_name().equals(category2_name)).map(p -> p.getCategory1_name())
					.collect(Collectors.toList());
			Assert.assertEquals(category2_names.size(), 0,
					"收货日期+二级分类[" + category2_name + "]过滤采购任务,过滤出了如下不符合的二级分类" + category2_names + "的采购任务");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType3);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase16() {
		ReporterCSS.title("测试点: 采购任务ES,按下单日期+品类分类过滤采购任务");
		try {
			purchaseTaskFilterParamType1.setOffset(offset);
			// 取一个下单商品,获取对应的一级分类信息
			int index = new Random().nextInt(orderDetail.getDetails().size());
			Detail order_sku = orderDetail.getDetails().get(index);
			String spu_id = order_sku.getSpu_id();
			SpuBean spu = categoryService.getSpuById(spu_id);
			Assert.assertNotEquals(spu, null, "获取商品 " + spu_id + " 详细信息失败");
			String category1_id = spu.getCategory_id_1();
			// String category1_name = spu.getCategory_name_1();

			String category2_id = spu.getCategory_id_2();
			// String category2_name = spu.getCategory_name_2();

			String pinlei_id = spu.getPinlei_id();
			String pinlei_name = spu.getPinlei_name();

			List<String> category1_ids = new ArrayList<String>();
			category1_ids.add(category1_id);

			List<String> category2_ids = new ArrayList<String>();
			category2_ids.add(category2_id);

			List<String> pinlei_ids = new ArrayList<String>();
			pinlei_ids.add(pinlei_id);

			purchaseTaskFilterParamType1.setCategory1_ids(category1_ids);
			purchaseTaskFilterParamType1.setCategory2_ids(category2_ids);
			purchaseTaskFilterParamType1.setPinlei_ids(pinlei_ids);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单时间+品类分类搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> pinlei_names = purchaseTaskDataList.stream()
					.filter(p -> !p.getPinlei_name().equals(pinlei_name)).map(p -> p.getPinlei_name())
					.collect(Collectors.toList());
			Assert.assertEquals(pinlei_names.size(), 0,
					"按下单日期+品类分类[" + pinlei_name + "]过滤采购任务,过滤出了如下不符合的品类分类" + pinlei_names + "的采购任务");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType1);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase17() {
		ReporterCSS.title("测试点: 采购任务ES,按运营周期+二级分类过滤采购任务");
		try {
			purchaseTaskFilterParamType2.setOffset(offset);
			// 取一个下单商品,获取对应的一级分类信息
			int index = new Random().nextInt(orderDetail.getDetails().size());
			Detail order_sku = orderDetail.getDetails().get(index);
			String spu_id = order_sku.getSpu_id();
			SpuBean spu = categoryService.getSpuById(spu_id);
			Assert.assertNotEquals(spu, null, "获取商品 " + spu_id + " 详细信息失败");
			String category1_id = spu.getCategory_id_1();
			// String category1_name = spu.getCategory_name_1();

			String category2_id = spu.getCategory_id_2();
			// String category2_name = spu.getCategory_name_2();

			String pinlei_id = spu.getPinlei_id();
			String pinlei_name = spu.getPinlei_name();

			List<String> category1_ids = new ArrayList<String>();
			category1_ids.add(category1_id);

			List<String> category2_ids = new ArrayList<String>();
			category2_ids.add(category2_id);

			List<String> pinlei_ids = new ArrayList<String>();
			pinlei_ids.add(pinlei_id);

			purchaseTaskFilterParamType2.setCategory1_ids(category1_ids);
			purchaseTaskFilterParamType2.setCategory2_ids(category2_ids);
			purchaseTaskFilterParamType2.setPinlei_ids(pinlei_ids);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType2.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按运营周期+品类分类搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> pinlei_names = purchaseTaskDataList.stream()
					.filter(p -> !p.getPinlei_name().equals(pinlei_name)).map(p -> p.getPinlei_name())
					.collect(Collectors.toList());
			Assert.assertEquals(pinlei_names.size(), 0,
					"按运营周期+品类分类[" + pinlei_name + "]过滤采购任务,过滤出了如下不符合的品类分类" + pinlei_names + "的采购任务");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType2);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase18() {
		ReporterCSS.title("测试点: 采购任务ES,按收货日期+二级分类过滤采购任务");
		try {
			purchaseTaskFilterParamType3.setOffset(offset);
			// 取一个下单商品,获取对应的一级分类信息
			int index = new Random().nextInt(orderDetail.getDetails().size());
			Detail order_sku = orderDetail.getDetails().get(index);
			String spu_id = order_sku.getSpu_id();
			SpuBean spu = categoryService.getSpuById(spu_id);
			Assert.assertNotEquals(spu, null, "获取商品 " + spu_id + " 详细信息失败");
			String category1_id = spu.getCategory_id_1();
			// String category1_name = spu.getCategory_name_1();

			String category2_id = spu.getCategory_id_2();
			// String category2_name = spu.getCategory_name_2();

			String pinlei_id = spu.getPinlei_id();
			String pinlei_name = spu.getPinlei_name();

			List<String> category1_ids = new ArrayList<String>();
			category1_ids.add(category1_id);

			List<String> category2_ids = new ArrayList<String>();
			category2_ids.add(category2_id);

			List<String> pinlei_ids = new ArrayList<String>();
			pinlei_ids.add(pinlei_id);

			purchaseTaskFilterParamType3.setCategory1_ids(category1_ids);
			purchaseTaskFilterParamType3.setCategory2_ids(category2_ids);
			purchaseTaskFilterParamType3.setPinlei_ids(pinlei_ids);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType3.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货日期+品类分类搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> pinlei_names = purchaseTaskDataList.stream()
					.filter(p -> !p.getPinlei_name().equals(pinlei_name)).map(p -> p.getPinlei_name())
					.collect(Collectors.toList());
			Assert.assertEquals(pinlei_names.size(), 0,
					"收货日期+品类分类[" + pinlei_name + "]过滤采购任务,过滤出了如下不符合的品类分类" + pinlei_names + "的采购任务");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType3);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase19() {
		ReporterCSS.title("测试点: 采购任务ES,按下单日期+任务状态搜索过滤");
		try {
			for (int i = 1; i <= 3; i++) {
				int fiter_status = i;
				offset = 0;
				purchaseTaskFilterParamType1.setStatus(fiter_status);
				List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
				List<PurchaseTaskData> tempPurchaseTaskDate = null;
				while (true) {
					purchaseTaskFilterParamType1.setOffset(offset);
					tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
					Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单日期+任务状态搜索过滤失败");
					purchaseTaskDataList.addAll(tempPurchaseTaskDate);
					if (tempPurchaseTaskDate.size() < limit) {
						break;
					}
					offset += limit;
				}
				List<PurchaseTaskData> tempList = purchaseTaskDataList.stream()
						.filter(p -> p.getStatus() != fiter_status).collect(Collectors.toList());
				Assert.assertEquals(tempList.size(), 0,
						"按下单时间+任务状态搜索查询采购任务,过滤出不符合条件的采购任务 " + JsonUtil.objectToStr(tempList));

				PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
						.getPurcahseTaskSummary(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
			}
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);

		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase20() {
		ReporterCSS.title("测试点: 采购任务ES,按运营周期+任务状态搜索过滤");
		try {
			for (int i = 1; i <= 3; i++) {
				int fiter_status = i;
				offset = 0;
				purchaseTaskFilterParamType2.setStatus(fiter_status);
				List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
				List<PurchaseTaskData> tempPurchaseTaskDate = null;
				while (true) {
					purchaseTaskFilterParamType2.setOffset(offset);
					tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
					Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按运营周期+任务状态搜索过滤失败");
					purchaseTaskDataList.addAll(tempPurchaseTaskDate);
					if (tempPurchaseTaskDate.size() < limit) {
						break;
					}
					offset += limit;
				}
				List<PurchaseTaskData> tempList = purchaseTaskDataList.stream()
						.filter(p -> p.getStatus() != fiter_status).collect(Collectors.toList());
				Assert.assertEquals(tempList.size(), 0,
						"按运营周期+任务状态搜索查询采购任务,过滤出不符合条件的采购任务 " + JsonUtil.objectToStr(tempList));

				PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
						.getPurcahseTaskSummary(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
			}
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);

		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase21() {
		ReporterCSS.title("测试点: 采购任务ES,按收货日期+任务状态搜索过滤");
		try {
			for (int i = 1; i <= 3; i++) {
				int fiter_status = i;
				offset = 0;
				purchaseTaskFilterParamType3.setStatus(fiter_status);
				List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
				List<PurchaseTaskData> tempPurchaseTaskDate = null;
				while (true) {
					purchaseTaskFilterParamType3.setOffset(offset);
					tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
					Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货日期+任务状态搜索过滤失败");
					purchaseTaskDataList.addAll(tempPurchaseTaskDate);
					if (tempPurchaseTaskDate.size() < limit) {
						break;
					}
					offset += limit;
				}
				List<PurchaseTaskData> tempList = purchaseTaskDataList.stream()
						.filter(p -> p.getStatus() != fiter_status).collect(Collectors.toList());
				Assert.assertEquals(tempList.size(), 0,
						"按收货日期+任务状态搜索查询采购任务,过滤出不符合条件的采购任务 " + JsonUtil.objectToStr(tempList));

				PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
						.getPurcahseTaskSummary(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
			}
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);

		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase22() {
		ReporterCSS.title("测试点: 采购任务ES,按下单时间+订单状态搜索过滤");
		try {
			List<Integer> order_status_array = new ArrayList<Integer>();
			int order_status = 5;
			order_status_array.add(order_status);
			purchaseTaskFilterParamType1.setOrder_status(order_status_array);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单日期+订单状态搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> actual_order_ids = new ArrayList<String>();
			for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				List<Task> tempList = purchaseTaskData.getTasks().stream()
						.filter(t -> t.getOrder_status() != order_status).collect(Collectors.toList());
				Assert.assertEquals(tempList.size(), 0,
						"按下单时间+订单状态[" + order_status + "]搜索查询采购任务,过滤出不符合条件的采购任务 " + JsonUtil.objectToStr(tempList));

				// 把订单号过滤出来
				for (PurchaseTaskData.Task task : purchaseTaskData.getTasks()) {
					if (!actual_order_ids.contains(task.getOrder_id())) {
						actual_order_ids.add(task.getOrder_id());
					}
				}
			}

			offset = 0;
			String today = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OrderFilterParam orderFilterParam = new OrderFilterParam();
			orderFilterParam.setQuery_type(1);
			orderFilterParam.setStart_date(today);
			orderFilterParam.setEnd_date(today);
			orderFilterParam.setStatus(order_status);
			orderFilterParam.setLimit(limit);
			List<OrderBean> tempOrders = null;
			List<OrderBean> orderList = new ArrayList<OrderBean>();
			while (true) {
				orderFilterParam.setOffset(offset);
				tempOrders = orderService.searchOrder(orderFilterParam);
				Assert.assertNotEquals(tempOrders, null, "搜索过滤订单列表失败");
				orderList.addAll(tempOrders);
				if (tempOrders.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> expected_order_ids = orderList.stream().map(s -> s.getId()).collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			ReporterCSS.step("预期的订单列表: " + expected_order_ids);
			ReporterCSS.step("实际的订单列表: " + actual_order_ids);
			if (expected_order_ids.size() > actual_order_ids.size()) {
				expected_order_ids.removeAll(actual_order_ids);
				msg = String.format("如下订单:%s 没有出现在预期列表中", expected_order_ids);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else if (expected_order_ids.size() < actual_order_ids.size()) {
				actual_order_ids.removeAll(expected_order_ids);
				msg = String.format("如下订单:%s 不应该出现在预期列表中", actual_order_ids);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else {
				if (!expected_order_ids.containsAll(actual_order_ids)) {
					expected_order_ids.removeAll(actual_order_ids);
					msg = String.format("如下订单:%s 没有出现在预期列表中", expected_order_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			Assert.assertEquals(result, true, "以订单状态过滤出的采购任务结果与预期不一致");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType1);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase23() {
		ReporterCSS.title("测试点: 采购任务ES,按运营时间+订单状态搜索过滤");
		try {
			List<Integer> order_status_array = new ArrayList<Integer>();
			int order_status = 1;
			order_status_array.add(order_status);
			purchaseTaskFilterParamType2.setOrder_status(order_status_array);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType2.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货日期+任务状态搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> actual_order_ids = new ArrayList<String>();
			for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				List<Task> tempList = purchaseTaskData.getTasks().stream()
						.filter(t -> t.getOrder_status() != order_status).collect(Collectors.toList());
				Assert.assertEquals(tempList.size(), 0,
						"按下单时间+订单状态[" + order_status + "]搜索查询采购任务,过滤出不符合条件的采购任务 " + JsonUtil.objectToStr(tempList));

				// 把订单号过滤出来
				for (PurchaseTaskData.Task task : purchaseTaskData.getTasks()) {
					if (!actual_order_ids.contains(task.getOrder_id())) {
						actual_order_ids.add(task.getOrder_id());
					}
				}
			}

			offset = 0;
			String cycle_start_time = orderCycle.getCycle_start_time();
			String cycle_end_time = orderCycle.getCycle_end_time();
			String time_config_id = orderCycle.getTime_config_id();

			OrderFilterParam orderFilterParam = new OrderFilterParam();
			orderFilterParam.setQuery_type(2);
			orderFilterParam.setCycle_start_time(cycle_start_time);
			orderFilterParam.setCycle_end_time(cycle_end_time);
			orderFilterParam.setTime_config_id(time_config_id);
			orderFilterParam.setStatus(order_status);
			orderFilterParam.setLimit(limit);
			List<OrderBean> tempOrders = null;
			List<OrderBean> orderList = new ArrayList<OrderBean>();
			while (true) {
				orderFilterParam.setOffset(offset);
				tempOrders = orderService.searchOrder(orderFilterParam);
				Assert.assertNotEquals(tempOrders, null, "搜索过滤订单列表失败");
				orderList.addAll(tempOrders);
				if (tempOrders.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> expected_order_ids = orderList.stream().map(s -> s.getId()).collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			ReporterCSS.step("预期的订单列表: " + expected_order_ids);
			ReporterCSS.step("实际的订单列表: " + actual_order_ids);
			if (expected_order_ids.size() > actual_order_ids.size()) {
				expected_order_ids.removeAll(actual_order_ids);
				msg = String.format("如下订单:%s 没有出现在预期列表中", expected_order_ids);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else if (expected_order_ids.size() < actual_order_ids.size()) {
				actual_order_ids.removeAll(expected_order_ids);
				msg = String.format("如下订单:%s 不应该出现在预期列表中", actual_order_ids);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else {
				if (!expected_order_ids.containsAll(actual_order_ids)) {
					expected_order_ids.removeAll(actual_order_ids);
					msg = String.format("如下订单:%s 没有出现在预期列表中", expected_order_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			Assert.assertEquals(result, true, "以订单状态过滤出的采购任务结果与预期不一致");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType2);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseTaskNewSearchTestCase24() {
		ReporterCSS.title("测试点: 采购任务ES,按收货时间+订单状态搜索过滤");
		try {
			List<Integer> order_status_array = new ArrayList<Integer>();
			int order_status = 10;
			order_status_array.add(order_status);
			purchaseTaskFilterParamType3.setOrder_status(order_status_array);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType3.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货日期+订单状态搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> actual_order_ids = new ArrayList<String>();
			for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				List<Task> tempList = purchaseTaskData.getTasks().stream()
						.filter(t -> t.getOrder_status() != order_status).collect(Collectors.toList());
				Assert.assertEquals(tempList.size(), 0,
						"按收货时间+订单状态[" + order_status + "]搜索查询采购任务,过滤出不符合条件的采购任务 " + JsonUtil.objectToStr(tempList));

				// 把订单号过滤出来
				for (PurchaseTaskData.Task task : purchaseTaskData.getTasks()) {
					if (!actual_order_ids.contains(task.getOrder_id())) {
						actual_order_ids.add(task.getOrder_id());
					}
				}
			}

			offset = 0;
			String receive_start_date = orderDetail.getCustomer().getReceive_begin_time().substring(0, 10);
			String receive_end_date = receive_start_date;
			OrderFilterParam orderFilterParam = new OrderFilterParam();
			orderFilterParam.setQuery_type(3);
			orderFilterParam.setReceive_start_date(receive_start_date);
			orderFilterParam.setReceive_end_date(receive_end_date);
			orderFilterParam.setStatus(order_status);
			orderFilterParam.setLimit(limit);
			List<OrderBean> tempOrders = null;
			List<OrderBean> orderList = new ArrayList<OrderBean>();
			while (true) {
				orderFilterParam.setOffset(offset);
				tempOrders = orderService.searchOrder(orderFilterParam);
				Assert.assertNotEquals(tempOrders, null, "搜索过滤订单列表失败");
				orderList.addAll(tempOrders);
				if (tempOrders.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> expected_order_ids = orderList.stream().map(s -> s.getId()).collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			ReporterCSS.step("预期的订单列表: " + expected_order_ids);
			ReporterCSS.step("实际的订单列表: " + actual_order_ids);
			if (expected_order_ids.size() > actual_order_ids.size()) {
				expected_order_ids.removeAll(actual_order_ids);
				msg = String.format("如下订单:%s 没有出现在预期列表中", expected_order_ids);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else if (expected_order_ids.size() < actual_order_ids.size()) {
				actual_order_ids.removeAll(expected_order_ids);
				msg = String.format("如下订单:%s 不应该出现在预期列表中", actual_order_ids);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else {
				if (!expected_order_ids.containsAll(actual_order_ids)) {
					expected_order_ids.removeAll(actual_order_ids);
					msg = String.format("如下订单:%s 没有出现在预期列表中", expected_order_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "以订单状态过滤出的采购任务结果与预期不一致");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType3);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	private List<String> settle_supplier_ids;
	private List<String> purchaser_ids;

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase25() {
		ReporterCSS.title("测试点: 采购任务ES,过滤采购任务绑定的个供应商,用作后面用例的参数");
		try {
			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单日期+供应商搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			settle_supplier_ids = purchaseTaskDataList.stream().map(p -> p.getSettle_supplier_id())
					.filter(p -> p != null && !p.equals("")).distinct().collect(Collectors.toList());
			Assert.assertEquals(settle_supplier_ids.size() > 0, true, "采购任务都没有绑定供应商");

			purchaser_ids = purchaseTaskDataList.stream().map(p -> p.getPurchaser_id())
					.filter(p -> p != null && !p.equals("")).distinct().collect(Collectors.toList());
			Assert.assertEquals(purchaser_ids.size() > 0, true, "采购任务没有绑定采购员");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseTaskNewSearchTestCase25" }, timeOut = 120000)
	public void purchaseTaskNewSearchTestCase26() {
		ReporterCSS.title("测试点: 采购任务ES,按下单日期+供应商过滤采购任务");
		try {
			String settle_supplier_id = NumberUtil.roundNumberInList(settle_supplier_ids);
			purchaseTaskFilterParamType1.setSettle_supplier_id(settle_supplier_id);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单日期+供应商搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			List<String> settle_supplier_ids = purchaseTaskDataList.stream()
					.filter(p -> !p.getSettle_supplier_id().equals(settle_supplier_id))
					.map(p -> p.getSettle_supplier_id()).distinct().collect(Collectors.toList());

			Assert.assertEquals(settle_supplier_ids.size(), 0,
					"采购任务ES,按下单日期+供应商[" + settle_supplier_id + "]过滤采购任务,过滤出了如下供应商" + settle_supplier_ids + "绑定的采购任务");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseTaskNewSearchTestCase25" }, timeOut = 120000)
	public void purchaseTaskNewSearchTestCase27() {
		ReporterCSS.title("测试点: 采购任务ES,按运营时间+供应商过滤采购任务");
		try {
			String settle_supplier_id = NumberUtil.roundNumberInList(settle_supplier_ids);
			purchaseTaskFilterParamType2.setSettle_supplier_id(settle_supplier_id);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType2.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按运营时间+供应商搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			List<String> settle_supplier_ids = purchaseTaskDataList.stream()
					.filter(p -> !p.getSettle_supplier_id().equals(settle_supplier_id))
					.map(p -> p.getSettle_supplier_id()).distinct().collect(Collectors.toList());

			Assert.assertEquals(settle_supplier_ids.size(), 0,
					"采购任务ES,按运营时间+供应商[" + settle_supplier_id + "]过滤采购任务,过滤出了如下供应商" + settle_supplier_ids + "绑定的采购任务");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseTaskNewSearchTestCase25" }, timeOut = 120000)
	public void purchaseTaskNewSearchTestCase28() {
		ReporterCSS.title("测试点: 采购任务ES,按收货日期+供应商过滤采购任务");
		try {
			String settle_supplier_id = NumberUtil.roundNumberInList(settle_supplier_ids);
			purchaseTaskFilterParamType3.setSettle_supplier_id(settle_supplier_id);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType3.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货日期+供应商搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			List<String> settle_supplier_ids = purchaseTaskDataList.stream()
					.filter(p -> !p.getSettle_supplier_id().equals(settle_supplier_id))
					.map(p -> p.getSettle_supplier_id()).distinct().collect(Collectors.toList());

			Assert.assertEquals(settle_supplier_ids.size(), 0,
					"采购任务ES,按收货日期+供应商[" + settle_supplier_id + "]过滤采购任务,过滤出了如下供应商" + settle_supplier_ids + "绑定的采购任务");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseTaskNewSearchTestCase25" }, timeOut = 120000)
	public void purchaseTaskNewSearchTestCase29() {
		ReporterCSS.title("测试点: 采购任务ES,按下单日期+采购员过滤采购任务");
		try {
			String purchaser_id = NumberUtil.roundNumberInList(purchaser_ids);
			purchaseTaskFilterParamType1.setPurchaser_id(purchaser_id);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单日期+供应商搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			List<String> purchaser_ids = purchaseTaskDataList.stream()
					.filter(p -> !p.getPurchaser_id().equals(purchaser_id)).map(p -> p.getPurchaser_id()).distinct()
					.collect(Collectors.toList());

			Assert.assertEquals(purchaser_ids.size(), 0,
					"采购任务ES,按下单日期+采购员[" + purchaser_id + "]过滤采购任务,过滤出了如下采购员" + purchaser_ids + "绑定的采购任务");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType1);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseTaskNewSearchTestCase25" }, timeOut = 120000)
	public void purchaseTaskNewSearchTestCase30() {
		ReporterCSS.title("测试点: 采购任务ES,按运营时间+采购员过滤采购任务");
		try {
			String purchaser_id = NumberUtil.roundNumberInList(purchaser_ids);
			purchaseTaskFilterParamType2.setPurchaser_id(purchaser_id);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType2.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按运营时间+采购员搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			List<String> purchaser_ids = purchaseTaskDataList.stream()
					.filter(p -> p.getPurchaser_id() != null && !p.getPurchaser_id().equals(purchaser_id))
					.map(p -> p.getPurchaser_id()).distinct().collect(Collectors.toList());

			Assert.assertEquals(purchaser_ids.size(), 0,
					"采购任务ES,按运营时间+采购员[" + purchaser_id + "]过滤采购任务,过滤出了如下采购员" + purchaser_ids + "绑定的采购任务");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType2);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseTaskNewSearchTestCase25" }, timeOut = 120000)
	public void purchaseTaskNewSearchTestCase31() {
		ReporterCSS.title("测试点: 采购任务ES,按收货日期+采购员过滤采购任务");
		try {
			String purchaser_id = NumberUtil.roundNumberInList(purchaser_ids);
			purchaseTaskFilterParamType3.setPurchaser_id(purchaser_id);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType3.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货日期+采购员搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			List<String> purchaser_ids = purchaseTaskDataList.stream()
					.filter(p -> !p.getPurchaser_id().equals(purchaser_id)).map(p -> p.getPurchaser_id()).distinct()
					.collect(Collectors.toList());

			Assert.assertEquals(purchaser_ids.size(), 0,
					"采购任务ES,按收货日期+采购员[" + purchaser_id + "]过滤采购任务,过滤出了如下采购员" + purchaser_ids + "绑定的采购任务");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType3);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	private BigDecimal route_id;
	private String route_name = "人民路";

	@Test
	public void purchaseTaskNewSearchTestCase32() {
		ReporterCSS.title("测试点: 初始化线路数据,为后面的搜索过滤做准备");
		try {
			route_id = routeService.initRouteData();
		} catch (Exception e) {
			logger.error("初始化线路数据失败: ", e);
			Assert.fail("初始化线路数据失败: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseTaskNewSearchTestCase32" })
	public void purchaseTaskNewSearchTestCase33() {
		ReporterCSS.title("测试点: 采购任务ES,按下单日期+路线过滤采购任务");
		try {
			purchaseTaskFilterParamType1.setRoute_id(route_id);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单日期+路线搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			String msg = null;
			boolean result = true;
			for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				for (PurchaseTaskData.Task task : purchaseTaskData.getTasks()) {
					if (!task.getRoute_name().contains(route_name)) {
						msg = String.format("采购任务ES,按下单日期+路线[%s]过滤采购任务,过滤出了路线[%s]", route_name, task.getRoute_name());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "按下单日期+路线过滤采购任务,过滤出不符合的采购任务");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType1);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseTaskNewSearchTestCase32" }, timeOut = 120000)
	public void purchaseTaskNewSearchTestCase34() {
		ReporterCSS.title("测试点: 采购任务ES,按运营时间+路线过滤采购任务");
		try {
			purchaseTaskFilterParamType2.setRoute_id(route_id);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType2.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按运营时间+路线搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			String msg = null;
			boolean result = true;
			for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				for (PurchaseTaskData.Task task : purchaseTaskData.getTasks()) {
					if (!task.getRoute_name().contains(route_name)) {
						msg = String.format("采购任务ES,按运营时间+路线[%s]过滤采购任务,过滤出了路线[%s]", route_name, task.getRoute_name());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "按运营时间+路线过滤采购任务,过滤出不符合的采购任务");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType2);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseTaskNewSearchTestCase32" }, timeOut = 120000)
	public void purchaseTaskNewSearchTestCase35() {
		ReporterCSS.title("测试点: 采购任务ES,按收货日期+路线过滤采购任务");
		try {
			purchaseTaskFilterParamType3.setRoute_id(route_id);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType3.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货日期+路线搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			String msg = null;
			boolean result = true;
			for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				for (PurchaseTaskData.Task task : purchaseTaskData.getTasks()) {
					if (!task.getRoute_name().contains(route_name)) {
						msg = String.format("采购任务ES,按收货日期+路线[%s]过滤采购任务,过滤出了路线[%s]", route_name, task.getRoute_name());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "按收货日期+路线过滤采购任务,过滤出不符合的采购任务");

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType3);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test
	public void purchaseTaskNewSearchTestCase36() {
		ReporterCSS.title("测试点: 分拣订单,为后面的测试准备数据");
		try {
			// 修改订单状态
			boolean result = orderService.updateOrderState(order_id, 5);
			Assert.assertEquals(result, true, "修改订单 " + order_id + " 状态为分拣中失败");

			result = weightService.oneStepWeightOrder(order_id);
			Assert.assertEquals(result, true, "订单 " + order_id + " 分拣失败");
		} catch (Exception e) {
			logger.error("订单分拣操作失败: ", e);
			Assert.fail("订单分拣操作失败: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseTaskNewSearchTestCase36" }, timeOut = 120000)
	public void purchaseTaskNewSearchTestCase37() {
		ReporterCSS.title("测试点: 采购任务ES,按下单日期+分拣状态搜索过滤采购任务");
		try {
			purchaseTaskFilterParamType1.setWeight_status(1);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单日期+分拣状态搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			boolean find = false;
			OK: for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				for (PurchaseTaskData.Task task : purchaseTaskData.getTasks()) {
					if (task.getOrder_id() != null && task.getOrder_id().equals(order_id)) {
						find = true;
						break OK;
					}
				}
			}
			Assert.assertEquals(find, true, "按下单时间+分拣状态[已完成]搜索过滤采购任务,没有过滤出目标订单 " + order_id);

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType1);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseTaskNewSearchTestCase36" }, timeOut = 120000)
	public void purchaseTaskNewSearchTestCase38() {
		ReporterCSS.title("测试点: 采购任务ES,按运营时间+分拣状态搜索过滤采购任务");
		try {
			purchaseTaskFilterParamType2.setWeight_status(1);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType2.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按运营时间+分拣状态搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			boolean find = false;
			OK: for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				for (PurchaseTaskData.Task task : purchaseTaskData.getTasks()) {
					if (task.getOrder_id() != null && task.getOrder_id().equals(order_id)) {
						find = true;
						break OK;
					}
				}
			}
			Assert.assertEquals(find, true, "按运营时间+分拣状态[已完成]搜索过滤采购任务,没有过滤出目标订单 " + order_id);

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType2);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseTaskNewSearchTestCase36" }, timeOut = 120000)
	public void purchaseTaskNewSearchTestCase39() {
		ReporterCSS.title("测试点: 采购任务ES,按收货日期+分拣状态搜索过滤采购任务");
		try {
			purchaseTaskFilterParamType3.setWeight_status(1);

			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType3.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货日期+分拣状态搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			boolean find = false;
			OK: for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				for (PurchaseTaskData.Task task : purchaseTaskData.getTasks()) {
					if (task.getOrder_id() != null && task.getOrder_id().equals(order_id)) {
						find = true;
						break OK;
					}
				}
			}
			Assert.assertEquals(find, true, "按收货时间+分拣状态[已完成]搜索过滤采购任务,没有过滤出目标订单 " + order_id);

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType3);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test
	public void purchaseTaskNewSearchTestCase40() {
		ReporterCSS.title("测试点: 采购任务ES,按下单日期+采购单状态搜索过滤采购任务");
		try {
			purchaseTaskFilterParamType1.setHas_created_sheet(1);
			List<PurchaseTaskData> purchaseTaskDataList = purchaseTaskService
					.newSearchPurchaseTask(purchaseTaskFilterParamType1);
			Assert.assertNotEquals(purchaseTaskDataList, null, "按下单日期+采购单状态搜索过滤采购任务失败");
			if (purchaseTaskDataList.size() > 0) {
				PurchaseTaskData purchaseTaskData = NumberUtil.roundNumberInList(purchaseTaskDataList);
				String spec_id = purchaseTaskData.getSpec_id();
				BigDecimal release_id = purchaseTaskData.getRelease_id();

				String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
				PurchaseTaskHistoryFilterParam filterParam = new PurchaseTaskHistoryFilterParam();
				filterParam.setQ_type(1);
				filterParam.setBegin_time(begin_time);
				filterParam.setEnd_time(begin_time);
				filterParam.setRelease_id(release_id);
				filterParam.setSku_id(spec_id);

				List<PurchaseTaskHistoryBean> purchaseTaskHistorys = purchaseTaskService
						.getPurchaseTaskHistorys(filterParam);
				Assert.assertNotEquals(purchaseTaskHistorys, null, "查看采购任务关联的订单和采购单失败");

				Assert.assertEquals(purchaseTaskHistorys.size() > 0, true, "按下单时间+采购单[已生成]搜索过滤采购任务,过滤出的采购任务没有对应的采购单");
			}

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType1);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test
	public void purchaseTaskNewSearchTestCase41() {
		ReporterCSS.title("测试点: 采购任务ES,按运营时间+采购单状态搜索过滤采购任务");
		try {
			purchaseTaskFilterParamType2.setHas_created_sheet(1);
			List<PurchaseTaskData> purchaseTaskDataList = purchaseTaskService
					.newSearchPurchaseTask(purchaseTaskFilterParamType2);
			Assert.assertNotEquals(purchaseTaskDataList, null, "按运营时间+采购单状态搜索过滤采购任务失败");
			if (purchaseTaskDataList.size() > 0) {
				PurchaseTaskData purchaseTaskData = NumberUtil.roundNumberInList(purchaseTaskDataList);
				String spec_id = purchaseTaskData.getSpec_id();
				BigDecimal release_id = purchaseTaskData.getRelease_id();

				PurchaseTaskHistoryFilterParam filterParam = new PurchaseTaskHistoryFilterParam();
				filterParam.setQ_type(2);
				filterParam.setBegin_time(purchaseTaskFilterParamType2.getBegin_time());
				filterParam.setEnd_time(purchaseTaskFilterParamType2.getEnd_time());
				filterParam.setRelease_id(release_id);
				filterParam.setSku_id(spec_id);

				List<PurchaseTaskHistoryBean> purchaseTaskHistorys = purchaseTaskService
						.getPurchaseTaskHistorys(filterParam);
				Assert.assertNotEquals(purchaseTaskHistorys, null, "查看采购任务关联的订单和采购单失败");

				Assert.assertEquals(purchaseTaskHistorys.size() > 0, true, "按运营时间+采购单[已生成]搜索过滤采购任务,过滤出的采购任务没有对应的采购单");
			}

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType2);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test
	public void purchaseTaskNewSearchTestCase42() {
		ReporterCSS.title("测试点: 采购任务ES,按收货日期+采购单状态搜索过滤采购任务");
		try {
			purchaseTaskFilterParamType3.setHas_created_sheet(1);
			List<PurchaseTaskData> purchaseTaskDataList = purchaseTaskService
					.newSearchPurchaseTask(purchaseTaskFilterParamType3);
			Assert.assertNotEquals(purchaseTaskDataList, null, "按收货日期+采购单状态搜索过滤采购任务失败");
			if (purchaseTaskDataList.size() > 0) {
				PurchaseTaskData purchaseTaskData = NumberUtil.roundNumberInList(purchaseTaskDataList);
				String spec_id = purchaseTaskData.getSpec_id();
				BigDecimal release_id = purchaseTaskData.getRelease_id();

				PurchaseTaskHistoryFilterParam filterParam = new PurchaseTaskHistoryFilterParam();
				filterParam.setQ_type(3);
				filterParam.setBegin_time(purchaseTaskFilterParamType3.getBegin_time());
				filterParam.setEnd_time(purchaseTaskFilterParamType3.getEnd_time());
				filterParam.setRelease_id(release_id);
				filterParam.setSku_id(spec_id);

				List<PurchaseTaskHistoryBean> purchaseTaskHistorys = purchaseTaskService
						.getPurchaseTaskHistorys(filterParam);
				Assert.assertNotEquals(purchaseTaskHistorys, null, "查看采购任务关联的订单和采购单失败");

				Assert.assertEquals(purchaseTaskHistorys.size() > 0, true, "按收货日期+采购单[已生成]搜索过滤采购任务,过滤出的采购任务没有对应的采购单");
			}

			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService
					.getPurcahseTaskSummary(purchaseTaskFilterParamType3);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览搜索报错");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase43() {
		ReporterCSS.title("测试点: 采购任务ES,按下单日期+商品名排序");
		try {
			// 先正序
			PurchaseTaskFilterParam.Sort sort = purchaseTaskFilterParamType1.new Sort();
			sort.setOpt("asc");
			sort.setFileds(4);
			purchaseTaskFilterParamType1.setSort(sort);

			List<PurchaseTaskData> purchaseTaskDataAscList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单日期+商品名正序排序失败");
				purchaseTaskDataAscList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			offset = 0;

			// 再反序
			sort.setOpt("desc");
			List<PurchaseTaskData> purchaseTaskDataDescList = new ArrayList<PurchaseTaskData>();
			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单日期+商品名反序排序失败");
				purchaseTaskDataDescList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> purchaseNameAscList = purchaseTaskDataAscList.stream().map(p -> p.getName())
					.collect(Collectors.toList());

			List<String> purchaseNameDescList = purchaseTaskDataDescList.stream().map(p -> p.getName())
					.collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			if (purchaseNameAscList.size() != purchaseNameDescList.size()) {
				msg = String.format("采购任务ES,按下单日期+商品名正序排序商品总数%s", purchaseNameAscList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				msg = String.format("采购任务ES,按下单日期+商品名反序排序商品总数%s", purchaseNameDescList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}
			Assert.assertEquals(purchaseNameAscList.size(), purchaseNameDescList.size(),
					"采购任务ES,按下单日期,商品名正序排序和反序排序查询到的商品数不一致");

			int size = purchaseNameAscList.size();
			for (int i = 0; i < size; i++) {
				if (!purchaseNameAscList.get(i).equals(purchaseNameDescList.get(size - 1 - i))) {
					msg = String.format("正序的第%s个商品名和反序的第%s商品名不一致,正序:%s,反序:%s", i, size - 1 - i,
							purchaseNameAscList.get(i), purchaseNameDescList.get(size - 1 - i));
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "采购任务ES,按下单日期,商品名正序排序和反序排序没有对应起来");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase44() {
		ReporterCSS.title("测试点: 采购任务ES,按运营周期+商品名排序");
		try {
			// 先正序
			PurchaseTaskFilterParam.Sort sort = purchaseTaskFilterParamType2.new Sort();
			sort.setOpt("asc");
			sort.setFileds(4);
			purchaseTaskFilterParamType2.setSort(sort);

			List<PurchaseTaskData> purchaseTaskDataAscList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType2.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按运营周期+商品名正序排序失败");
				purchaseTaskDataAscList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			offset = 0;

			// 再反序
			sort.setOpt("desc");
			List<PurchaseTaskData> purchaseTaskDataDescList = new ArrayList<PurchaseTaskData>();
			while (true) {
				purchaseTaskFilterParamType2.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按运营周期+商品名反序排序失败");
				purchaseTaskDataDescList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> purchaseNameAscList = purchaseTaskDataAscList.stream().map(p -> p.getName())
					.collect(Collectors.toList());

			List<String> purchaseNameDescList = purchaseTaskDataDescList.stream().map(p -> p.getName())
					.collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			if (purchaseNameAscList.size() != purchaseNameDescList.size()) {
				msg = String.format("采购任务ES,按运营周期+商品名正序排序商品总数%s", purchaseNameAscList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				msg = String.format("采购任务ES,按运营周期+商品名反序排序商品总数%s", purchaseNameDescList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}
			Assert.assertEquals(purchaseNameAscList.size(), purchaseNameDescList.size(),
					"采购任务ES,按运营周期,商品名正序排序和反序排序查询到的商品数不一致");

			int size = purchaseNameAscList.size();
			for (int i = 0; i < size; i++) {
				if (!purchaseNameAscList.get(i).equals(purchaseNameDescList.get(size - 1 - i))) {
					msg = String.format("正序的第%s个商品名和反序的第%s商品名不一致,正序:%s,反序:%s", i, size - 1 - i,
							purchaseNameAscList.get(i), purchaseNameDescList.get(size - 1 - i));
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "采购任务ES,按运营周期,商品名正序排序和反序排序没有对应起来");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase45() {
		ReporterCSS.title("测试点: 采购任务ES,按收货日期+商品名排序");
		try {
			// 先正序
			PurchaseTaskFilterParam.Sort sort = purchaseTaskFilterParamType3.new Sort();
			sort.setOpt("asc");
			sort.setFileds(4);
			purchaseTaskFilterParamType3.setSort(sort);

			List<PurchaseTaskData> purchaseTaskDataAscList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType3.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,收货日期+商品名正序排序失败");
				purchaseTaskDataAscList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			offset = 0;

			// 再反序
			sort.setOpt("desc");
			List<PurchaseTaskData> purchaseTaskDataDescList = new ArrayList<PurchaseTaskData>();
			while (true) {
				purchaseTaskFilterParamType3.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货日期+商品名反序排序失败");
				purchaseTaskDataDescList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> purchaseNameAscList = purchaseTaskDataAscList.stream().map(p -> p.getName())
					.collect(Collectors.toList());

			List<String> purchaseNameDescList = purchaseTaskDataDescList.stream().map(p -> p.getName())
					.collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			if (purchaseNameAscList.size() != purchaseNameDescList.size()) {
				msg = String.format("采购任务ES,按收货日期+商品名正序排序商品总数%s", purchaseNameAscList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				msg = String.format("采购任务ES,按收货日期+商品名反序排序商品总数%s", purchaseNameDescList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}
			Assert.assertEquals(purchaseNameAscList.size(), purchaseNameDescList.size(),
					"采购任务ES,按收货日期,商品名正序排序和反序排序查询到的商品数不一致");

			int size = purchaseNameAscList.size();
			for (int i = 0; i < size; i++) {
				if (!purchaseNameAscList.get(i).equals(purchaseNameDescList.get(size - 1 - i))) {
					msg = String.format("正序的第%s个商品名和反序的第%s商品名不一致,正序:%s,反序:%s", i, size - 1 - i,
							purchaseNameAscList.get(i), purchaseNameDescList.get(size - 1 - i));
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "采购任务ES,按收货日期,商品名正序排序和反序排序没有对应起来");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase46() {
		ReporterCSS.title("测试点: 采购任务ES,按下单日期+商品分类排序");
		try {
			// 先正序
			PurchaseTaskFilterParam.Sort sort = purchaseTaskFilterParamType1.new Sort();
			sort.setOpt("asc");
			sort.setFileds(1);
			purchaseTaskFilterParamType1.setSort(sort);

			List<PurchaseTaskData> purchaseTaskDataAscList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单日期+商品名正序排序失败");
				purchaseTaskDataAscList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			offset = 0;

			// 再反序
			sort.setOpt("desc");
			List<PurchaseTaskData> purchaseTaskDataDescList = new ArrayList<PurchaseTaskData>();
			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单日期+商品名反序排序失败");
				purchaseTaskDataDescList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> purchaseCategoryAscList = purchaseTaskDataAscList.stream()
					.map(p -> p.getCategory1_name() + "/" + p.getCategory2_name() + "/" + p.getPinlei_name())
					.collect(Collectors.toList());

			List<String> purchaseCategoryDescList = purchaseTaskDataDescList.stream()
					.map(p -> p.getCategory1_name() + "/" + p.getCategory2_name() + "/" + p.getPinlei_name())
					.collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			if (purchaseCategoryAscList.size() != purchaseCategoryAscList.size()) {
				msg = String.format("采购任务ES,按下单日期+商品分类正序排序商品总数%s", purchaseCategoryAscList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				msg = String.format("采购任务ES,按下单日期+商品分类反序排序商品总数%s", purchaseCategoryDescList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}
			Assert.assertEquals(purchaseCategoryAscList.size(), purchaseCategoryDescList.size(),
					"采购任务ES,按下单日期,商品分类正序排序和反序排序查询到的商品数不一致");

			int size = purchaseCategoryAscList.size();
			for (int i = 0; i < size; i++) {
				if (!purchaseCategoryAscList.get(i).equals(purchaseCategoryDescList.get(size - 1 - i))) {
					msg = String.format("正序的第%s个商品分类和反序的第%s商品分类不一致,正序:%s,反序:%s", i, size - 1 - i,
							purchaseCategoryAscList.get(i), purchaseCategoryDescList.get(size - 1 - i));
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "采购任务ES,按下单日期,商品分类正序排序和反序排序没有对应起来");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase47() {
		ReporterCSS.title("测试点: 采购任务ES,按运营周期+商品分类排序");
		try {
			// 先正序
			PurchaseTaskFilterParam.Sort sort = purchaseTaskFilterParamType2.new Sort();
			sort.setOpt("asc");
			sort.setFileds(1);
			purchaseTaskFilterParamType2.setSort(sort);

			List<PurchaseTaskData> purchaseTaskDataAscList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType2.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按运营周期+商品名正序排序失败");
				purchaseTaskDataAscList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			offset = 0;

			// 再反序
			sort.setOpt("desc");
			List<PurchaseTaskData> purchaseTaskDataDescList = new ArrayList<PurchaseTaskData>();
			while (true) {
				purchaseTaskFilterParamType2.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按运营周期+商品名反序排序失败");
				purchaseTaskDataDescList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> purchaseCategoryAscList = purchaseTaskDataAscList.stream()
					.map(p -> p.getCategory1_name() + "/" + p.getCategory2_name() + "/" + p.getPinlei_name())
					.collect(Collectors.toList());

			List<String> purchaseCategoryDescList = purchaseTaskDataDescList.stream()
					.map(p -> p.getCategory1_name() + "/" + p.getCategory2_name() + "/" + p.getPinlei_name())
					.collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			if (purchaseCategoryAscList.size() != purchaseCategoryAscList.size()) {
				msg = String.format("采购任务ES,按运营周期+商品分类正序排序商品总数%s", purchaseCategoryAscList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				msg = String.format("采购任务ES,按运营周期+商品分类反序排序商品总数%s", purchaseCategoryDescList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}
			Assert.assertEquals(purchaseCategoryAscList.size(), purchaseCategoryDescList.size(),
					"采购任务ES,按运营周期,商品分类正序排序和反序排序查询到的商品数不一致");

			int size = purchaseCategoryAscList.size();
			for (int i = 0; i < size; i++) {
				if (!purchaseCategoryAscList.get(i).equals(purchaseCategoryDescList.get(size - 1 - i))) {
					msg = String.format("正序的第%s个商品分类和反序的第%s商品分类不一致,正序:%s,反序:%s", i, size - 1 - i,
							purchaseCategoryAscList.get(i), purchaseCategoryDescList.get(size - 1 - i));
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "采购任务ES,按运营周期,商品分类正序排序和反序排序没有对应起来");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase48() {
		ReporterCSS.title("测试点: 采购任务ES,按收货日期+商品分类排序");
		try {
			// 先正序
			PurchaseTaskFilterParam.Sort sort = purchaseTaskFilterParamType3.new Sort();
			sort.setOpt("asc");
			sort.setFileds(1);
			purchaseTaskFilterParamType3.setSort(sort);

			List<PurchaseTaskData> purchaseTaskDataAscList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType3.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货日期+商品名正序排序失败");
				purchaseTaskDataAscList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			offset = 0;

			// 再反序
			sort.setOpt("desc");
			List<PurchaseTaskData> purchaseTaskDataDescList = new ArrayList<PurchaseTaskData>();
			while (true) {
				purchaseTaskFilterParamType3.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货日期+商品名反序排序失败");
				purchaseTaskDataDescList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> purchaseCategoryAscList = purchaseTaskDataAscList.stream()
					.map(p -> p.getCategory1_name() + "/" + p.getCategory2_name() + "/" + p.getPinlei_name())
					.collect(Collectors.toList());

			List<String> purchaseCategoryDescList = purchaseTaskDataDescList.stream()
					.map(p -> p.getCategory1_name() + "/" + p.getCategory2_name() + "/" + p.getPinlei_name())
					.collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			if (purchaseCategoryAscList.size() != purchaseCategoryAscList.size()) {
				msg = String.format("采购任务ES,按收货日期+商品分类正序排序商品总数%s", purchaseCategoryAscList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				msg = String.format("采购任务ES,按收货日期+商品分类反序排序商品总数%s", purchaseCategoryDescList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}
			Assert.assertEquals(purchaseCategoryAscList.size(), purchaseCategoryDescList.size(),
					"采购任务ES,按收货日期,商品分类正序排序和反序排序查询到的商品数不一致");

			int size = purchaseCategoryAscList.size();
			for (int i = 0; i < size; i++) {
				if (!purchaseCategoryAscList.get(i).equals(purchaseCategoryDescList.get(size - 1 - i))) {
					msg = String.format("正序的第%s个商品分类和反序的第%s商品分类不一致,正序:%s,反序:%s", i, size - 1 - i,
							purchaseCategoryAscList.get(i), purchaseCategoryDescList.get(size - 1 - i));
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "采购任务ES,按收货日期,商品分类正序排序和反序排序没有对应起来");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase49() {
		ReporterCSS.title("测试点: 采购任务ES,按下单日期+供应商排序");
		try {
			// 先正序
			PurchaseTaskFilterParam.Sort sort = purchaseTaskFilterParamType1.new Sort();
			sort.setOpt("asc");
			sort.setFileds(2);
			purchaseTaskFilterParamType1.setSort(sort);

			List<PurchaseTaskData> purchaseTaskDataAscList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单日期+供应商正序排序失败");
				purchaseTaskDataAscList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			offset = 0;

			// 再反序
			sort.setOpt("desc");
			List<PurchaseTaskData> purchaseTaskDataDescList = new ArrayList<PurchaseTaskData>();
			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单日期+供应商反序排序失败");
				purchaseTaskDataDescList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> purchaseSupplierAscList = purchaseTaskDataAscList.stream()
					.map(p -> p.getSettle_supplier_name()).collect(Collectors.toList());

			List<String> purchaseSupplierDescList = purchaseTaskDataDescList.stream()
					.map(p -> p.getSettle_supplier_name()).collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			if (purchaseSupplierAscList.size() != purchaseSupplierDescList.size()) {
				msg = String.format("采购任务ES,按下单日期+供应商正序排序商品总数%s", purchaseSupplierAscList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				msg = String.format("采购任务ES,按下单日期+供应商反序排序商品总数%s", purchaseSupplierDescList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}
			Assert.assertEquals(purchaseSupplierAscList.size(), purchaseSupplierDescList.size(),
					"采购任务ES,按下单日期,供应商正序排序和反序排序查询到的商品数不一致");

			int size = purchaseSupplierAscList.size();
			for (int i = 0; i < size; i++) {
				if (!purchaseSupplierAscList.get(i).equals(purchaseSupplierDescList.get(size - 1 - i))) {
					msg = String.format("正序的第%s个供应商和反序的第%s供应商不一致,正序:%s,反序:%s", i, size - 1 - i,
							purchaseSupplierAscList.get(i), purchaseSupplierDescList.get(size - 1 - i));
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "采购任务ES,按下单日期,供应商正序排序和反序排序没有对应起来");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase50() {
		ReporterCSS.title("测试点: 采购任务ES,按运营周期+供应商排序");
		try {
			// 先正序
			PurchaseTaskFilterParam.Sort sort = purchaseTaskFilterParamType2.new Sort();
			sort.setOpt("asc");
			sort.setFileds(2);
			purchaseTaskFilterParamType2.setSort(sort);

			List<PurchaseTaskData> purchaseTaskDataAscList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType2.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按运营周期+供应商正序排序失败");
				purchaseTaskDataAscList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			offset = 0;

			// 再反序
			sort.setOpt("desc");
			List<PurchaseTaskData> purchaseTaskDataDescList = new ArrayList<PurchaseTaskData>();
			while (true) {
				purchaseTaskFilterParamType2.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按运营周期+供应商反序排序失败");
				purchaseTaskDataDescList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> purchaseSupplierAscList = purchaseTaskDataAscList.stream()
					.map(p -> p.getSettle_supplier_name()).collect(Collectors.toList());

			List<String> purchaseSupplierDescList = purchaseTaskDataDescList.stream()
					.map(p -> p.getSettle_supplier_name()).collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			if (purchaseSupplierAscList.size() != purchaseSupplierDescList.size()) {
				msg = String.format("采购任务ES,按运营周期+供应商正序排序商品总数%s", purchaseSupplierAscList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				msg = String.format("采购任务ES,按运营周期+供应商反序排序商品总数%s", purchaseSupplierDescList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}
			Assert.assertEquals(purchaseSupplierAscList.size(), purchaseSupplierDescList.size(),
					"采购任务ES,按运营周期,供应商正序排序和反序排序查询到的商品数不一致");

			int size = purchaseSupplierAscList.size();
			for (int i = 0; i < size; i++) {
				if (!purchaseSupplierAscList.get(i).equals(purchaseSupplierDescList.get(size - 1 - i))) {
					msg = String.format("正序的第%s个供应商和反序的第%s供应商不一致,正序:%s,反序:%s", i, size - 1 - i,
							purchaseSupplierAscList.get(i), purchaseSupplierDescList.get(size - 1 - i));
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "采购任务ES,按运营周期,供应商正序排序和反序排序没有对应起来");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase51() {
		ReporterCSS.title("测试点: 采购任务ES,按收货日期+供应商排序");
		try {
			// 先正序
			PurchaseTaskFilterParam.Sort sort = purchaseTaskFilterParamType3.new Sort();
			sort.setOpt("asc");
			sort.setFileds(2);
			purchaseTaskFilterParamType3.setSort(sort);

			List<PurchaseTaskData> purchaseTaskDataAscList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType3.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货日期+供应商正序排序失败");
				purchaseTaskDataAscList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			offset = 0;

			// 再反序
			sort.setOpt("desc");
			List<PurchaseTaskData> purchaseTaskDataDescList = new ArrayList<PurchaseTaskData>();
			while (true) {
				purchaseTaskFilterParamType3.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货日期+供应商反序排序失败");
				purchaseTaskDataDescList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> purchaseSupplierAscList = purchaseTaskDataAscList.stream()
					.map(p -> p.getSettle_supplier_name()).collect(Collectors.toList());

			List<String> purchaseSupplierDescList = purchaseTaskDataDescList.stream()
					.map(p -> p.getSettle_supplier_name()).collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			if (purchaseSupplierAscList.size() != purchaseSupplierDescList.size()) {
				msg = String.format("采购任务ES,按收货日期+供应商正序排序商品总数%s", purchaseSupplierAscList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				msg = String.format("采购任务ES,按收货日期+供应商反序排序商品总数%s", purchaseSupplierDescList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}
			Assert.assertEquals(purchaseSupplierAscList.size(), purchaseSupplierDescList.size(),
					"采购任务ES,按收货日期,供应商正序排序和反序排序查询到的商品数不一致");

			int size = purchaseSupplierAscList.size();
			for (int i = 0; i < size; i++) {
				if (!purchaseSupplierAscList.get(i).equals(purchaseSupplierDescList.get(size - 1 - i))) {
					msg = String.format("正序的第%s个供应商和反序的第%s供应商不一致,正序:%s,反序:%s", i, size - 1 - i,
							purchaseSupplierAscList.get(i), purchaseSupplierDescList.get(size - 1 - i));
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "采购任务ES,按收货日期,供应商正序排序和反序排序没有对应起来");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase52() {
		ReporterCSS.title("测试点: 采购任务ES,按下单日期+采购员排序");
		try {
			// 先正序
			PurchaseTaskFilterParam.Sort sort = purchaseTaskFilterParamType1.new Sort();
			sort.setOpt("asc");
			sort.setFileds(3);
			purchaseTaskFilterParamType1.setSort(sort);

			List<PurchaseTaskData> purchaseTaskDataAscList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单日期+采购员正序排序失败");
				purchaseTaskDataAscList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			offset = 0;

			// 再反序
			sort.setOpt("desc");
			List<PurchaseTaskData> purchaseTaskDataDescList = new ArrayList<PurchaseTaskData>();
			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单日期+采购员反序排序失败");
				purchaseTaskDataDescList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> purchasePurchaserAscList = purchaseTaskDataAscList.stream()
					.map(p -> p.getSettle_supplier_name()).collect(Collectors.toList());

			List<String> purchasePurchaserDescList = purchaseTaskDataDescList.stream()
					.map(p -> p.getSettle_supplier_name()).collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			if (purchasePurchaserAscList.size() != purchasePurchaserDescList.size()) {
				msg = String.format("采购任务ES,按下单日期+采购员正序排序商品总数%s", purchasePurchaserAscList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				msg = String.format("采购任务ES,按下单日期+采购员反序排序商品总数%s", purchasePurchaserDescList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}
			Assert.assertEquals(purchasePurchaserAscList.size(), purchasePurchaserDescList.size(),
					"采购任务ES,按下单日期,采购员正序排序和反序排序查询到的商品数不一致");

			int size = purchasePurchaserAscList.size();
			for (int i = 0; i < size; i++) {
				if (!purchasePurchaserAscList.get(i).equals(purchasePurchaserDescList.get(size - 1 - i))) {
					msg = String.format("正序的第%s个采购员和反序的第%s采购员不一致,正序:%s,反序:%s", i, size - 1 - i,
							purchasePurchaserAscList.get(i), purchasePurchaserDescList.get(size - 1 - i));
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "采购任务ES,按下单日期,采购员正序排序和反序排序没有对应起来");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase53() {
		ReporterCSS.title("测试点: 采购任务ES,按运营周期+采购员排序");
		try {
			// 先正序
			PurchaseTaskFilterParam.Sort sort = purchaseTaskFilterParamType2.new Sort();
			sort.setOpt("asc");
			sort.setFileds(3);
			purchaseTaskFilterParamType2.setSort(sort);

			List<PurchaseTaskData> purchaseTaskDataAscList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType2.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按运营周期+采购员正序排序失败");
				purchaseTaskDataAscList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			offset = 0;

			// 再反序
			sort.setOpt("desc");
			List<PurchaseTaskData> purchaseTaskDataDescList = new ArrayList<PurchaseTaskData>();
			while (true) {
				purchaseTaskFilterParamType2.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType2);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单日期+采购员反序排序失败");
				purchaseTaskDataDescList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> purchasePurchaserAscList = purchaseTaskDataAscList.stream()
					.map(p -> p.getSettle_supplier_name()).collect(Collectors.toList());

			List<String> purchasePurchaserDescList = purchaseTaskDataDescList.stream()
					.map(p -> p.getSettle_supplier_name()).collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			if (purchasePurchaserAscList.size() != purchasePurchaserDescList.size()) {
				msg = String.format("采购任务ES,按运营周期+采购员正序排序商品总数%s", purchasePurchaserAscList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				msg = String.format("采购任务ES,按运营周期+采购员反序排序商品总数%s", purchasePurchaserDescList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}
			Assert.assertEquals(purchasePurchaserAscList.size(), purchasePurchaserDescList.size(),
					"采购任务ES,按运营周期,采购员正序排序和反序排序查询到的商品数不一致");

			int size = purchasePurchaserAscList.size();
			for (int i = 0; i < size; i++) {
				if (!purchasePurchaserAscList.get(i).equals(purchasePurchaserDescList.get(size - 1 - i))) {
					msg = String.format("正序的第%s个采购员和反序的第%s采购员不一致,正序:%s,反序:%s", i, size - 1 - i,
							purchasePurchaserAscList.get(i), purchasePurchaserDescList.get(size - 1 - i));
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "采购任务ES,按运营周期,采购员正序排序和反序排序没有对应起来");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase54() {
		ReporterCSS.title("测试点: 采购任务ES,按收货日期+采购员排序");
		try {
			// 先正序
			PurchaseTaskFilterParam.Sort sort = purchaseTaskFilterParamType3.new Sort();
			sort.setOpt("asc");
			sort.setFileds(3);
			purchaseTaskFilterParamType3.setSort(sort);

			List<PurchaseTaskData> purchaseTaskDataAscList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;

			while (true) {
				purchaseTaskFilterParamType3.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货日期+采购员正序排序失败");
				purchaseTaskDataAscList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}
			offset = 0;

			// 再反序
			sort.setOpt("desc");
			List<PurchaseTaskData> purchaseTaskDataDescList = new ArrayList<PurchaseTaskData>();
			while (true) {
				purchaseTaskFilterParamType3.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType3);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按收货日期+采购员反序排序失败");
				purchaseTaskDataDescList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> purchasePurchaserAscList = purchaseTaskDataAscList.stream()
					.map(p -> p.getSettle_supplier_name()).collect(Collectors.toList());

			List<String> purchasePurchaserDescList = purchaseTaskDataDescList.stream()
					.map(p -> p.getSettle_supplier_name()).collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			if (purchasePurchaserAscList.size() != purchasePurchaserDescList.size()) {
				msg = String.format("采购任务ES,按收货日期+采购员正序排序商品总数%s", purchasePurchaserAscList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				msg = String.format("采购任务ES,按收货日期+采购员反序排序商品总数%s", purchasePurchaserDescList.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}
			Assert.assertEquals(purchasePurchaserAscList.size(), purchasePurchaserDescList.size(),
					"采购任务ES,按收货日期,采购员正序排序和反序排序查询到的商品数不一致");

			int size = purchasePurchaserAscList.size();
			for (int i = 0; i < size; i++) {
				if (!purchasePurchaserAscList.get(i).equals(purchasePurchaserDescList.get(size - 1 - i))) {
					msg = String.format("正序的第%s个采购员和反序的第%s采购员不一致,正序:%s,反序:%s", i, size - 1 - i,
							purchasePurchaserAscList.get(i), purchasePurchaserDescList.get(size - 1 - i));
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "采购任务ES,按收货日期,采购员正序排序和反序排序没有对应起来");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}

	@Test(timeOut = 120000)
	public void purchaseTaskNewSearchTestCase55() {
		ReporterCSS.title("测试点: 采购任务ES,验证当天的所有订单是否都生成采购任务了");
		try {
			List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDate = null;
			while (true) {
				purchaseTaskFilterParamType1.setOffset(offset);
				tempPurchaseTaskDate = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParamType1);
				Assert.assertNotEquals(tempPurchaseTaskDate, null, "采购任务ES,按下单日期+订单状态搜索过滤失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDate);
				if (tempPurchaseTaskDate.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> actual_order_ids = new ArrayList<String>();
			for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				// 把订单号过滤出来
				for (PurchaseTaskData.Task task : purchaseTaskData.getTasks()) {
					if (task.getOrder_id() != null && !task.getOrder_id().equals("")
							&& !actual_order_ids.contains(task.getOrder_id())) {
						actual_order_ids.add(task.getOrder_id());
					}
				}
			}

			offset = 0;
			String today = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OrderFilterParam orderFilterParam = new OrderFilterParam();
			orderFilterParam.setQuery_type(1);
			orderFilterParam.setStart_date(today);
			orderFilterParam.setEnd_date(today);
			orderFilterParam.setLimit(limit);
			List<OrderBean> tempOrders = null;
			List<OrderBean> orderList = new ArrayList<OrderBean>();
			while (true) {
				orderFilterParam.setOffset(offset);
				tempOrders = orderService.searchOrder(orderFilterParam);
				Assert.assertNotEquals(tempOrders, null, "搜索过滤订单列表失败");
				orderList.addAll(tempOrders);
				if (tempOrders.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> expected_order_ids = orderList.stream().map(s -> s.getId()).collect(Collectors.toList());
			logger.info("订单列表中的订单号: " + expected_order_ids);
			logger.info("采购任务中的订单号: " + actual_order_ids);
			ReporterCSS.step("订单列表中的订单号: " + expected_order_ids);
			ReporterCSS.step("采购任务中的订单号: " + actual_order_ids);
			String msg = null;
			boolean result = true;
			if (expected_order_ids.size() > actual_order_ids.size()) {
				expected_order_ids.removeAll(actual_order_ids);
				msg = String.format("如下订单:%s 还没有生成采购任务", expected_order_ids);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else if (expected_order_ids.size() < actual_order_ids.size()) {
				actual_order_ids.removeAll(expected_order_ids);
				msg = String.format("如下订单:%s 的采购任务应该是没有同步删除", actual_order_ids);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else {
				if (!expected_order_ids.containsAll(actual_order_ids)) {
					expected_order_ids.removeAll(actual_order_ids);
					msg = String.format("如下订单:%s 还没有生成采购任务", expected_order_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单生成的采购任务与预期的不一致,请确认");
		} catch (Exception e) {
			logger.error("从ES中拉取采购任务失败: ", e);
			Assert.fail("从ES中拉取采购任务失败: ", e);
		}
	}
}
