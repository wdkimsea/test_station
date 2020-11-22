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
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.category.PurchaseSpecBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.bean.delivery.RouteBean;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.bean.order.OrderBean;
import cn.guanmai.station.bean.order.OrderCycle;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderDetailBean.Detail;
import cn.guanmai.station.bean.order.param.OrderFilterParam;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskExpectedBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskHistoryBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean.PurchaseTaskData;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean.PurchaseTaskData.Task;
import cn.guanmai.station.bean.purchase.PurchaseTaskPrintBean;
import cn.guanmai.station.bean.purchase.PurchaserBean;
import cn.guanmai.station.bean.purchase.PurchaserResponseBean;
import cn.guanmai.station.bean.purchase.PurchaserBean.SettleSupplier;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskFilterParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskHistoryFilterParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskPrintParam;
import cn.guanmai.station.bean.purchase.param.PurchaserParam;
import cn.guanmai.station.bean.system.param.OrderProfileParam;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.delivery.RouteServiceImpl;
import cn.guanmai.station.impl.invoicing.SupplierServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaseTaskServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaserServiceImpl;
import cn.guanmai.station.impl.system.ProfileServiceImpl;
import cn.guanmai.station.impl.weight.WeightServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.delivery.RouteService;
import cn.guanmai.station.interfaces.invoicing.SupplierService;
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

/* 
* @author liming 
* @date Nov 16, 2018 10:42:00 AM 
* @des 查询、发布采购任务
* @version 1.0 
*/
public class PurchaseTaskSearchTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PurchaseTaskSearchTest.class);
	private OrderTool orderTool;
	private OrderService orderService;
	private PurchaseTaskService purchaseTaskService;
	private PurchaseTaskTool purchaseTaskTool;
	private PurchaserService purchaserService;
	private CategoryService categoryService;
	private List<PurchaseTaskExpectedBean> purchaseTaskExpectedList;
	private OrderCycle orderCycle;
	private WeightService weightService;
	private OrderDetailBean orderDetail;
	private SupplierService supplierService;
	private RouteService routeService;
	private String order_id;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		orderTool = new OrderTool(headers);
		purchaseTaskService = new PurchaseTaskServiceImpl(headers);
		purchaserService = new PurchaserServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		supplierService = new SupplierServiceImpl(headers);
		routeService = new RouteServiceImpl(headers);
		purchaseTaskTool = new PurchaseTaskTool(headers);
		orderService = new OrderServiceImpl(headers);
		weightService = new WeightServiceImpl(headers);
		try {
			ProfileService profileService = new ProfileServiceImpl(headers);
			OrderProfileParam orderProfileParam = new OrderProfileParam();
			orderProfileParam.setOrder_create_purchase_task(0);
			boolean result = profileService.updateOrderProfile(orderProfileParam);
			Assert.assertEquals(result, true, "设置订单商品手动进入采购任务失败");

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
					"订单" + order_id + "的采购任务在60秒内没有生成");
		} catch (Exception e) {
			logger.error("初始化数据失败: ", e);
			Assert.fail("初始化数据失败: ", e);
		}
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

	@Test(timeOut = 90000)
	public void purchaseTaskSearchTestCase01() {
		ReporterCSS.title("测试点: 按下单时间搜索查询采购任务");
		String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setQ_type(1);
		param.setBegin_time(begin_time);
		param.setEnd_time(begin_time);

		int limit = 50;
		int offset = 0;
		param.setLimit(limit);
		param.setOffset(offset);

		List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<>();
		PurchaseTaskBean purcahseTask = null;
		boolean more = true;
		String page_obj = null;
		try {
			while (more) {
				purcahseTask = purchaseTaskService.searchPurchaseTask(param);
				if (purcahseTask.getCode() == 0 && purcahseTask.getPurchaseTaskDataArray() != null) {
					purchaseTaskDataList.addAll(purcahseTask.getPurchaseTaskDataArray());
					offset += limit;
					more = purcahseTask.getPagination().isMore();
					page_obj = purcahseTask.getPagination().getPage_obj();
					if (page_obj != null && !page_obj.equals("")) {
						param.setPage_obj(page_obj);
					} else {
						if (purcahseTask.getPurchaseTaskDataArray().size() < limit) {
							break;
						}
						param.setOffset(offset);
					}
				} else {
					more = false;
					Assert.assertEquals(purcahseTask.getMsg(), "ok", "搜索过滤采购任务失败");
				}
			}

			Map<String, BigDecimal> purchaseActualMap = new HashMap<>();
			String spec_id = null;
			String supplier_id = null;
			String actual_key = null;
			for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				spec_id = purchaseTaskData.getSpec_id();
				String temp_supplier_id = purchaseTaskData.getSettle_supplier_id();
				supplier_id = temp_supplier_id == null || temp_supplier_id.trim().equals("") ? "" : temp_supplier_id;
				actual_key = spec_id + "-" + supplier_id;
				for (Task task : purchaseTaskData.getTasks()) {
					if (task.getOrder_id() != null && task.getOrder_id().equals(order_id)) {
						BigDecimal plan_purchase_amount = task.getPlan_purchase_amount();
						if (purchaseActualMap.containsKey(actual_key)) {
							purchaseActualMap.put(actual_key,
									purchaseActualMap.get(actual_key).add(plan_purchase_amount).stripTrailingZeros());
						} else {
							purchaseActualMap.put(actual_key, plan_purchase_amount.stripTrailingZeros());
						}
					}
				}
			}

			boolean result = compareResult(purchaseTaskExpectedList, purchaseActualMap);
			Assert.assertEquals(result, true, "订单 " + order_id + " 预期查到的采购任务和实际查到的采购任务不一致");
		} catch (Exception e) {
			logger.error("搜索查询采购任务遇到错误: ", e);
			Assert.fail("搜索查询采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseTaskSearchTestCase02() {
		ReporterCSS.title("测试点: 按运营时间搜索查询采购任务");
		String begin_time = orderCycle.getCycle_start_time() + ":00";
		String end_time = orderCycle.getCycle_end_time() + ":00";
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setQ_type(2);
		param.setTime_config_id(orderCycle.getTime_config_id());
		param.setBegin_time(begin_time);
		param.setEnd_time(end_time);

		int limit = 50;
		int offset = 0;
		param.setLimit(limit);
		param.setOffset(offset);
		String page_obj = null;

		List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<>();
		PurchaseTaskBean purcahseTask = null;
		boolean more = true;
		try {
			while (more) {
				purcahseTask = purchaseTaskService.searchPurchaseTask(param);
				if (purcahseTask.getCode() == 0 && purcahseTask.getPurchaseTaskDataArray() != null) {
					purchaseTaskDataList.addAll(purcahseTask.getPurchaseTaskDataArray());
					offset += limit;
					more = purcahseTask.getPagination().isMore();
					page_obj = purcahseTask.getPagination().getPage_obj();
					if (page_obj != null && !page_obj.equals("")) {
						param.setPage_obj(page_obj);
					} else {
						if (purcahseTask.getPurchaseTaskDataArray().size() < limit) {
							break;
						}
						param.setOffset(offset);
					}
				} else {
					more = false;
					Assert.assertEquals(purcahseTask.getMsg(), "ok", "搜索过滤采购任务失败");
				}
			}

			Map<String, BigDecimal> purchaseActualMap = new HashMap<>();
			String spec_id = null;
			String supplier_id = null;
			String actual_key = null;
			for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				spec_id = purchaseTaskData.getSpec_id();
				String temp_supplier_id = purchaseTaskData.getSettle_supplier_id();
				supplier_id = temp_supplier_id == null || temp_supplier_id.trim().equals("") ? "" : temp_supplier_id;
				actual_key = spec_id + "-" + supplier_id;
				for (Task task : purchaseTaskData.getTasks()) {
					if (task.getOrder_id() != null && task.getOrder_id().equals(order_id)) {
						BigDecimal plan_purchase_amount = task.getPlan_purchase_amount();
						if (purchaseActualMap.containsKey(actual_key)) {
							purchaseActualMap.put(actual_key,
									purchaseActualMap.get(actual_key).add(plan_purchase_amount).stripTrailingZeros());
						} else {
							purchaseActualMap.put(actual_key, plan_purchase_amount.stripTrailingZeros());
						}
					}
				}
			}

			// 验证查询到的订单采购任务是否正确
			boolean result = compareResult(purchaseTaskExpectedList, purchaseActualMap);
			Assert.assertEquals(result, true, "订单 " + order_id + " 预期查到的采购任务和时价查到的采购任务不一致");
		} catch (Exception e) {
			logger.error("搜索查询采购任务遇到错误: ", e);
			Assert.fail("搜索查询采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseTaskSearchTestCase03() {
		ReporterCSS.title("测试点: 按收货时间搜索查询采购任务");

		String begin_time = orderDetail.getCustomer().getReceive_begin_time().substring(0, 10) + " 00:00:00";

		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setQ_type(3);
		param.setBegin_time(begin_time);
		param.setEnd_time(begin_time);

		int limit = 50;
		int offset = 0;
		param.setLimit(limit);
		param.setOffset(offset);
		String page_obj = null;

		List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<>();
		PurchaseTaskBean purcahseTask = null;
		boolean more = true;
		try {
			while (more) {
				purcahseTask = purchaseTaskService.searchPurchaseTask(param);
				if (purcahseTask.getCode() == 0 && purcahseTask.getPurchaseTaskDataArray() != null) {
					purchaseTaskDataList.addAll(purcahseTask.getPurchaseTaskDataArray());
					offset += limit;
					more = purcahseTask.getPagination().isMore();
					page_obj = purcahseTask.getPagination().getPage_obj();
					if (page_obj != null && !page_obj.equals("")) {
						param.setPage_obj(page_obj);
					} else {
						if (purcahseTask.getPurchaseTaskDataArray().size() < limit) {
							break;
						}
						param.setOffset(offset);
					}
				} else {
					more = false;
					Assert.assertEquals(purcahseTask.getMsg(), "ok", "搜索过滤采购任务失败");
				}
			}

			Map<String, BigDecimal> purchaseActualMap = new HashMap<>();
			String spec_id = null;
			String supplier_id = null;
			String actual_key = null;
			for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				spec_id = purchaseTaskData.getSpec_id();
				String temp_supplier_id = purchaseTaskData.getSettle_supplier_id();
				supplier_id = temp_supplier_id == null || temp_supplier_id.trim().equals("") ? "" : temp_supplier_id;
				actual_key = spec_id + "-" + supplier_id;
				for (Task task : purchaseTaskData.getTasks()) {
					if (task.getOrder_id() != null && task.getOrder_id().equals(order_id)) {
						BigDecimal plan_purchase_amount = task.getPlan_purchase_amount();
						if (purchaseActualMap.containsKey(actual_key)) {
							purchaseActualMap.put(actual_key,
									purchaseActualMap.get(actual_key).add(plan_purchase_amount).stripTrailingZeros());
						} else {
							purchaseActualMap.put(actual_key, plan_purchase_amount.stripTrailingZeros());
						}
					}
				}
			}

			// 验证查询到的订单采购任务是否正确
			boolean result = compareResult(purchaseTaskExpectedList, purchaseActualMap);
			Assert.assertEquals(result, true, "订单 " + order_id + " 预期查到的采购任务和实际查到的采购任务不一致");
		} catch (Exception e) {
			logger.error("搜索查询采购任务遇到错误: ", e);
			Assert.fail("搜索查询采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseTaskSearchTestCase04() {
		ReporterCSS.title("测试点: 按下单时间+订单号搜索查询采购任务");
		String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setQ_type(1);
		param.setBegin_time(begin_time);
		param.setEnd_time(begin_time);
		param.setQ(order_id);

		int limit = 50;
		int offset = 0;
		param.setLimit(limit);
		param.setOffset(offset);
		String page_obj = null;

		List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<>();
		PurchaseTaskBean purcahseTask = null;
		boolean more = true;
		try {
			while (more) {
				purcahseTask = purchaseTaskService.searchPurchaseTask(param);
				if (purcahseTask.getCode() == 0 && purcahseTask.getPurchaseTaskDataArray() != null) {
					purchaseTaskDataList.addAll(purcahseTask.getPurchaseTaskDataArray());

					offset += limit;
					more = purcahseTask.getPagination().isMore();
					page_obj = purcahseTask.getPagination().getPage_obj();
					if (page_obj != null && !page_obj.equals("")) {
						param.setPage_obj(page_obj);
					} else {
						if (purcahseTask.getPurchaseTaskDataArray().size() < limit) {
							break;
						}
						param.setOffset(offset);
					}
				} else {
					more = false;
					Assert.assertEquals(purcahseTask.getMsg(), "ok", "搜索过滤采购任务失败");
				}
			}

			boolean result = true;
			Map<String, BigDecimal> purchaseActualMap = new HashMap<>();
			String spec_id = null;
			String supplier_id = null;
			String actual_key = null;
			for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				spec_id = purchaseTaskData.getSpec_id();
				String temp_supplier_id = purchaseTaskData.getSettle_supplier_id();
				supplier_id = temp_supplier_id == null || temp_supplier_id.trim().equals("") ? "" : temp_supplier_id;
				actual_key = spec_id + "-" + supplier_id;
				for (Task task : purchaseTaskData.getTasks()) {
					if (task.getOrder_id() == null || !task.getOrder_id().equals(order_id)) {
						ReporterCSS.warn("按下单时间+订单号搜索查询采购任务,查询出了不符合过滤条件的数据,不符合的数据如下");
						ReporterCSS.warn(JsonUtil.objectToStr(task));
						result = false;
						continue;
					}

					BigDecimal plan_purchase_amount = task.getPlan_purchase_amount();
					if (purchaseActualMap.containsKey(actual_key)) {
						purchaseActualMap.put(actual_key,
								purchaseActualMap.get(actual_key).add(plan_purchase_amount).stripTrailingZeros());
					} else {
						purchaseActualMap.put(actual_key, plan_purchase_amount.stripTrailingZeros());
					}
				}
			}

			Assert.assertEquals(result, true, "按下单时间+订单号搜索查询采购任务,查询出了不符合过滤条件的数据");
			// 验证查询到的订单采购任务是否正确
			result = compareResult(purchaseTaskExpectedList, purchaseActualMap);
			Assert.assertEquals(result, true, "订单 " + order_id + " 预期查到的采购任务和时价查到的采购任务不一致");
		} catch (Exception e) {
			logger.error("搜索查询采购任务遇到错误: ", e);
			Assert.fail("搜索查询采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseTaskSearchTestCase05() {
		ReporterCSS.title("测试点: 按运营时间+订单号搜索查询采购任务");
		String begin_time = orderCycle.getCycle_start_time() + ":00";
		String end_time = orderCycle.getCycle_end_time() + ":00";
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setQ_type(2);
		param.setTime_config_id(orderCycle.getTime_config_id());
		param.setBegin_time(begin_time);
		param.setEnd_time(end_time);
		param.setQ(order_id);

		int limit = 50;
		int offset = 0;
		param.setLimit(limit);
		param.setOffset(offset);
		String page_obj = null;

		List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<>();
		PurchaseTaskBean purcahseTask = null;
		boolean more = true;
		try {
			while (more) {
				purcahseTask = purchaseTaskService.searchPurchaseTask(param);
				if (purcahseTask.getCode() == 0 && purcahseTask.getPurchaseTaskDataArray() != null) {
					purchaseTaskDataList.addAll(purcahseTask.getPurchaseTaskDataArray());

					offset += limit;
					more = purcahseTask.getPagination().isMore();
					page_obj = purcahseTask.getPagination().getPage_obj();
					if (page_obj != null && !page_obj.equals("")) {
						param.setPage_obj(page_obj);
					} else {
						if (purcahseTask.getPurchaseTaskDataArray().size() < limit) {
							break;
						}
						param.setOffset(offset);
					}

				} else {
					more = false;
					Assert.assertEquals(purcahseTask.getMsg(), "ok", "搜索过滤采购任务失败");
				}
			}

			boolean result = true;
			Map<String, BigDecimal> purchaseActualMap = new HashMap<>();
			String spec_id = null;
			String supplier_id = null;
			String actual_key = null;
			for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				spec_id = purchaseTaskData.getSpec_id();
				String temp_supplier_id = purchaseTaskData.getSettle_supplier_id();
				supplier_id = temp_supplier_id == null || temp_supplier_id.trim().equals("") ? "" : temp_supplier_id;
				actual_key = spec_id + "-" + supplier_id;
				for (Task task : purchaseTaskData.getTasks()) {
					if (task.getOrder_id() == null || !task.getOrder_id().equals(order_id)) {
						Reporter.log("按下单时间+订单号搜索查询采购任务,查询出了不符合过滤条件的数据,不符合的数据如下");
						ReporterCSS.warn(JsonUtil.objectToStr(task));
						result = false;
						continue;
					}

					BigDecimal plan_purchase_amount = task.getPlan_purchase_amount();
					if (purchaseActualMap.containsKey(actual_key)) {
						purchaseActualMap.put(actual_key,
								purchaseActualMap.get(actual_key).add(plan_purchase_amount).stripTrailingZeros());
					} else {
						purchaseActualMap.put(actual_key, plan_purchase_amount.stripTrailingZeros());
					}
				}
			}

			Assert.assertEquals(result, true, "按运营时间+订单号搜索查询采购任务,查询出了不符合过滤条件的数据");
			// 验证查询到的订单采购任务是否正确
			result = compareResult(purchaseTaskExpectedList, purchaseActualMap);
			Assert.assertEquals(result, true, "订单 " + order_id + " 预期查到的采购任务和时价查到的采购任务不一致");
		} catch (Exception e) {
			logger.error("搜索查询采购任务遇到错误: ", e);
			Assert.fail("搜索查询采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseTaskSearchTestCase06() {
		ReporterCSS.title("测试点: 按收货时间+订单号搜索查询采购任务");

		String begin_time = orderDetail.getCustomer().getReceive_begin_time().substring(0, 10) + " 00:00:00";

		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setQ_type(3);
		param.setBegin_time(begin_time);
		param.setEnd_time(begin_time);
		param.setQ(order_id);

		int limit = 50;
		int offset = 0;
		param.setLimit(limit);
		param.setOffset(offset);
		String page_obj = null;

		List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<>();
		PurchaseTaskBean purcahseTask = null;
		boolean more = true;
		try {
			while (more) {
				purcahseTask = purchaseTaskService.searchPurchaseTask(param);
				if (purcahseTask.getCode() == 0 && purcahseTask.getPurchaseTaskDataArray() != null) {
					purchaseTaskDataList.addAll(purcahseTask.getPurchaseTaskDataArray());

					offset += limit;
					more = purcahseTask.getPagination().isMore();
					page_obj = purcahseTask.getPagination().getPage_obj();
					if (page_obj != null && !page_obj.equals("")) {
						param.setPage_obj(page_obj);
					} else {
						if (purcahseTask.getPurchaseTaskDataArray().size() < limit) {
							break;
						}
						param.setOffset(offset);
					}
				} else {
					more = false;
					Assert.assertEquals(purcahseTask.getMsg(), "ok", "搜索过滤采购任务失败");
				}
			}

			boolean result = true;
			Map<String, BigDecimal> purchaseActualMap = new HashMap<>();
			String spec_id = null;
			String supplier_id = null;
			String actual_key = null;
			for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				spec_id = purchaseTaskData.getSpec_id();
				String temp_supplier_id = purchaseTaskData.getSettle_supplier_id();
				supplier_id = temp_supplier_id == null || temp_supplier_id.trim().equals("") ? "" : temp_supplier_id;
				actual_key = spec_id + "-" + supplier_id;
				for (Task task : purchaseTaskData.getTasks()) {
					if (task.getOrder_id() == null || !task.getOrder_id().equals(order_id)) {
						ReporterCSS.warn("按下单时间+订单号搜索查询采购任务,查询出了不符合过滤条件的数据,不符合的数据如下");
						ReporterCSS.warn(JsonUtil.objectToStr(task));
						result = false;
						continue;
					}

					BigDecimal plan_purchase_amount = task.getPlan_purchase_amount();
					if (purchaseActualMap.containsKey(actual_key)) {
						purchaseActualMap.put(actual_key,
								purchaseActualMap.get(actual_key).add(plan_purchase_amount).stripTrailingZeros());
					} else {
						purchaseActualMap.put(actual_key, plan_purchase_amount.stripTrailingZeros());
					}

				}
			}

			Assert.assertEquals(result, true, "按收货时间+订单号搜索查询采购任务,查询出了不符合过滤条件的数据");

			// 验证查询到的订单采购任务是否正确
			result = compareResult(purchaseTaskExpectedList, purchaseActualMap);
			Assert.assertEquals(result, true, "订单 " + order_id + " 预期查到的采购任务和时价查到的采购任务不一致");
		} catch (Exception e) {
			logger.error("搜索查询采购任务遇到错误: ", e);
			Assert.fail("搜索查询采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseTaskSearchTestCase07() {
		ReporterCSS.title("测试点: 按下单时间+商品名称搜索查询采购任务");
		String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setQ_type(1);
		param.setBegin_time(begin_time);
		param.setEnd_time(begin_time);

		int limit = 50;
		int offset = 0;
		param.setLimit(limit);
		param.setOffset(offset);
		String page_obj = null;

		PurchaseTaskExpectedBean purchaseTaskExpected = NumberUtil.roundNumberInList(purchaseTaskExpectedList);
		String spec_id = purchaseTaskExpected.getSpec_id();
		BigDecimal expected_amount = purchaseTaskExpected.getPlan_purchase_amount();

		BigDecimal actual_amount = BigDecimal.ZERO;

		List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<>();
		PurchaseTaskBean purcahseTask = null;
		boolean more = true;
		try {
			// 取一个采购规格的名称
			PurchaseSpecBean purchaseSpec = categoryService.getPurchaseSpecById(spec_id);
			Assert.assertNotEquals(purchaseSpec, null, "获取采购规格" + spec_id + "详细信息失败");
			String purchase_name = purchaseSpec.getName();
			param.setQ(purchase_name);

			while (more) {
				purcahseTask = purchaseTaskService.searchPurchaseTask(param);
				if (purcahseTask.getCode() == 0 && purcahseTask.getPurchaseTaskDataArray() != null) {
					purchaseTaskDataList.addAll(purcahseTask.getPurchaseTaskDataArray());

					offset += limit;
					more = purcahseTask.getPagination().isMore();
					page_obj = purcahseTask.getPagination().getPage_obj();
					if (page_obj != null && !page_obj.equals("")) {
						param.setPage_obj(page_obj);
					} else {
						if (purcahseTask.getPurchaseTaskDataArray().size() < limit) {
							break;
						}
						param.setOffset(offset);
					}

				} else {
					more = false;
					Assert.assertEquals(purcahseTask.getMsg(), "ok", "搜索过滤采购任务失败");
				}
			}

			boolean exist = false;
			for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
				for (Task task : purchaseTaskData.getTasks()) {
					if (task.getOrder_id() != null && task.getOrder_id().equals(order_id)
							&& purchaseTaskData.getSettle_supplier_id().equals(purchaseTaskExpected.getSupplier_id())) {
						actual_amount = actual_amount.add(task.getPlan_purchase_amount());
						exist = true;
					}
				}
			}

			Assert.assertEquals(exist, true, "按下单时间+商品名称搜索查询采购任务,没有找到对应的采购任务商品" + purchase_name);

			actual_amount = actual_amount.setScale(2, BigDecimal.ROUND_HALF_UP);
			expected_amount = expected_amount.setScale(2, BigDecimal.ROUND_HALF_UP);

			Assert.assertEquals(actual_amount.compareTo(expected_amount) == 0, true, "采购商品 " + spec_id + " 在订单 "
					+ order_id + " 中的实际采购数与预期采购数不一致,预期: " + expected_amount + ",实际:" + actual_amount);
		} catch (Exception e) {
			logger.error("搜索查询采购任务遇到错误: ", e);
			Assert.fail("搜索查询采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseTaskSearchTestCase08() {
		ReporterCSS.title("测试点: 按下单时间+商品一级分类搜索查询采购任务");
		String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setQ_type(1);
		param.setBegin_time(begin_time);
		param.setEnd_time(begin_time);

		int limit = 50;
		int offset = 0;
		param.setLimit(limit);
		param.setOffset(offset);
		String page_obj = null;

		List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<>();
		PurchaseTaskBean purcahseTask = null;
		boolean more = true;
		try {
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

			param.setCategory1_ids(category1_ids);

			while (more) {
				purcahseTask = purchaseTaskService.searchPurchaseTask(param);
				if (purcahseTask.getCode() == 0 && purcahseTask.getPurchaseTaskDataArray() != null) {
					purchaseTaskDataList.addAll(purcahseTask.getPurchaseTaskDataArray());

					offset += limit;
					more = purcahseTask.getPagination().isMore();
					page_obj = purcahseTask.getPagination().getPage_obj();
					if (page_obj != null && !page_obj.equals("")) {
						param.setPage_obj(page_obj);
					} else {
						if (purcahseTask.getPurchaseTaskDataArray().size() < limit) {
							break;
						}
						param.setOffset(offset);
					}

				} else {
					more = false;
					Assert.assertEquals(purcahseTask.getMsg(), "ok", "搜索过滤采购任务失败");
				}
			}

			// 过滤看有没有不符合过滤一级分类的结果
			List<PurchaseTaskData> tempList = purchaseTaskDataList.stream()
					.filter(p -> !p.getCategory1_name().equals(category1_name)).collect(Collectors.toList());

			Assert.assertEquals(tempList.size(), 0,
					"按下单时间+商品一级分类搜索查询采购任务,过滤出了出了不符合条件的采购任务 " + JsonUtil.objectToStr(tempList));
		} catch (Exception e) {
			logger.error("搜索查询采购任务遇到错误: ", e);
			Assert.fail("搜索查询采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseTaskSearchTestCase09() {
		ReporterCSS.title("测试点: 按下单时间+商品二级分类搜索查询采购任务");
		String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setQ_type(1);
		param.setBegin_time(begin_time);
		param.setEnd_time(begin_time);

		int limit = 50;
		int offset = 0;
		param.setLimit(limit);
		param.setOffset(offset);
		String page_obj = null;

		List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<>();
		PurchaseTaskBean purcahseTask = null;
		boolean more = true;
		try {
			// 取一个下单商品,获取对应的一级分类信息
			int index = new Random().nextInt(orderDetail.getDetails().size());
			Detail order_sku = orderDetail.getDetails().get(index);
			String spu_id = order_sku.getSpu_id();
			SpuBean spu = categoryService.getSpuById(spu_id);
			Assert.assertNotEquals(spu, null, "获取商品 " + spu_id + " 详细信息失败");
			String category1_id = spu.getCategory_id_1();
			String category1_name = spu.getCategory_name_1();

			String category2_id = spu.getCategory_id_2();
			String category2_name = spu.getCategory_name_2();

			List<String> category1_ids = new ArrayList<String>();
			category1_ids.add(category1_id);

			List<String> category2_ids = new ArrayList<String>();
			category2_ids.add(category2_id);

			param.setCategory1_ids(category1_ids);
			param.setCategory2_ids(category2_ids);

			while (more) {
				purcahseTask = purchaseTaskService.searchPurchaseTask(param);
				if (purcahseTask.getCode() == 0 && purcahseTask.getPurchaseTaskDataArray() != null) {
					purchaseTaskDataList.addAll(purcahseTask.getPurchaseTaskDataArray());

					offset += limit;
					more = purcahseTask.getPagination().isMore();
					page_obj = purcahseTask.getPagination().getPage_obj();
					if (page_obj != null && !page_obj.equals("")) {
						param.setPage_obj(page_obj);
					} else {
						if (purcahseTask.getPurchaseTaskDataArray().size() < limit) {
							break;
						}
						param.setOffset(offset);
					}

				} else {
					more = false;
					Assert.assertEquals(purcahseTask.getMsg(), "ok", "搜索过滤采购任务失败");
				}
			}

			// 过滤看有没有不符合过滤条件的结果
			List<PurchaseTaskData> tempList = purchaseTaskDataList.stream().filter(
					p -> !p.getCategory1_name().equals(category1_name) || !p.getCategory2_name().equals(category2_name))
					.collect(Collectors.toList());

			Assert.assertEquals(tempList.size(), 0,
					"按下单时间+商品二级分类搜索查询采购任务,过滤出了出了不符合条件的采购任务 " + JsonUtil.objectToStr(tempList));
		} catch (Exception e) {
			logger.error("搜索查询采购任务遇到错误: ", e);
			Assert.fail("搜索查询采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseTaskSearchTestCase10() {
		ReporterCSS.title("测试点: 按下单时间+商品品类分类搜索查询采购任务");
		String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setQ_type(1);
		param.setBegin_time(begin_time);
		param.setEnd_time(begin_time);

		int limit = 50;
		int offset = 0;
		param.setLimit(limit);
		param.setOffset(offset);
		String page_obj = null;

		List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<>();
		PurchaseTaskBean purcahseTask = null;
		boolean more = true;
		try {
			// 取一个下单商品,获取对应的一级分类信息
			int index = new Random().nextInt(orderDetail.getDetails().size());
			Detail order_sku = orderDetail.getDetails().get(index);
			String spu_id = order_sku.getSpu_id();
			SpuBean spu = categoryService.getSpuById(spu_id);
			Assert.assertNotEquals(spu, null, "获取商品 " + spu_id + " 详细信息失败");

			String category1_id = spu.getCategory_id_1();
			String category1_name = spu.getCategory_name_1();

			String category2_id = spu.getCategory_id_2();
			String category2_name = spu.getCategory_name_2();

			String pinlei_id = spu.getPinlei_id();
			String pinlei_name = spu.getPinlei_name();

			List<String> category1_ids = new ArrayList<String>();
			category1_ids.add(category1_id);

			List<String> category2_ids = new ArrayList<String>();
			category2_ids.add(category2_id);

			List<String> pinlei_ids = new ArrayList<String>();
			pinlei_ids.add(pinlei_id);

			param.setCategory1_ids(category1_ids);
			param.setCategory2_ids(category2_ids);
			param.setPinlei_ids(pinlei_ids);

			while (more) {
				purcahseTask = purchaseTaskService.searchPurchaseTask(param);
				if (purcahseTask.getCode() == 0 && purcahseTask.getPurchaseTaskDataArray() != null) {
					purchaseTaskDataList.addAll(purcahseTask.getPurchaseTaskDataArray());

					offset += limit;
					more = purcahseTask.getPagination().isMore();
					page_obj = purcahseTask.getPagination().getPage_obj();
					if (page_obj != null && !page_obj.equals("")) {
						param.setPage_obj(page_obj);
					} else {
						if (purcahseTask.getPurchaseTaskDataArray().size() < limit) {
							break;
						}
						param.setOffset(offset);
					}
				} else {
					more = false;
					Assert.assertEquals(purcahseTask.getMsg(), "ok", "搜索过滤采购任务失败");
				}
			}

			// 过滤看有没有不符合过滤条件的结果
			List<PurchaseTaskData> tempList = purchaseTaskDataList.stream()
					.filter(p -> !p.getCategory1_name().equals(category1_name)
							|| !p.getCategory2_name().equals(category2_name) || !p.getPinlei_name().equals(pinlei_name))
					.collect(Collectors.toList());

			Assert.assertEquals(tempList.size(), 0,
					"按下单时间+商品品类分类搜索查询采购任务,过滤出了出了不符合条件的采购任务 " + JsonUtil.objectToStr(tempList));
		} catch (Exception e) {
			logger.error("搜索查询采购任务遇到错误: ", e);
			Assert.fail("搜索查询采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseTaskSearchTestCase11() {
		ReporterCSS.title("测试点: 按下单时间+任务状态搜索查询采购任务");
		String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setQ_type(1);
		param.setBegin_time(begin_time);
		param.setEnd_time(begin_time);

		int limit = 50;

		param.setLimit(limit);

		String page_obj = null;

		List<PurchaseTaskData> purchaseTaskDataList = null;
		PurchaseTaskBean purcahseTask = null;
		boolean more = true;
		try {
			for (int status = 1; status <= 3; status++) {
				int offset = 0;
				purchaseTaskDataList = new ArrayList<>();
				param.setStatus(status);
				int fiter_status = status;
				while (more) {
					param.setOffset(offset);
					purcahseTask = purchaseTaskService.searchPurchaseTask(param);
					if (purcahseTask.getCode() == 0 && purcahseTask.getPurchaseTaskDataArray() != null) {
						purchaseTaskDataList.addAll(purcahseTask.getPurchaseTaskDataArray());

						offset += limit;
						more = purcahseTask.getPagination().isMore();
						page_obj = purcahseTask.getPagination().getPage_obj();
						if (page_obj != null && !page_obj.equals("")) {
							param.setPage_obj(page_obj);
						} else {
							if (purcahseTask.getPurchaseTaskDataArray().size() < limit) {
								break;
							}
						}
					} else {
						more = false;
						Assert.assertEquals(purcahseTask.getMsg(), "ok", "搜索过滤采购任务失败");
					}
				}
				List<PurchaseTaskData> tempList = purchaseTaskDataList.stream()
						.filter(p -> p.getStatus() != fiter_status).collect(Collectors.toList());
				Assert.assertEquals(tempList.size(), 0,
						"按下单时间+任务状态搜索查询采购任务,过滤出不符合条件的采购任务 " + JsonUtil.objectToStr(tempList));
			}
		} catch (Exception e) {
			logger.error("搜索查询采购任务遇到错误: ", e);
			Assert.fail("搜索查询采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseTaskSearchTestCase12() {
		ReporterCSS.title("测试点: 按下单时间+订单状态搜索查询采购任务");
		String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setQ_type(1);
		param.setBegin_time(begin_time);
		param.setEnd_time(begin_time);

		int limit = 50;
		int offset = 0;
		param.setLimit(limit);
		param.setOffset(offset);
		String page_obj = null;

		List<PurchaseTaskData> purchaseTaskDataList = null;
		PurchaseTaskBean purcahseTask = null;
		boolean more = true;
		try {
			List<Integer> order_status_param = null;
			int[] order_status_array = new int[] { 1, 5, 10, 15 };
			for (int i = 0; i < order_status_array.length; i++) {
				purchaseTaskDataList = new ArrayList<>();
				order_status_param = new ArrayList<>();
				int order_status = order_status_array[i];
				order_status_param.add(order_status);
				offset = 0;
				page_obj = null;
				more = true;
				param.setOrder_status(order_status_param);
				param.setPage_obj(null);
				param.setOffset(0);
				while (more) {
					purcahseTask = purchaseTaskService.searchPurchaseTask(param);
					if (purcahseTask.getCode() == 0 && purcahseTask.getPurchaseTaskDataArray() != null) {
						purchaseTaskDataList.addAll(purcahseTask.getPurchaseTaskDataArray());
						offset += limit;
						more = purcahseTask.getPagination().isMore();
						page_obj = purcahseTask.getPagination().getPage_obj();
						if (page_obj != null && !page_obj.equals("")) {
							param.setPage_obj(page_obj);
						} else {
							if (purcahseTask.getPurchaseTaskDataArray().size() < limit) {
								break;
							}
							param.setOffset(offset);
						}
					} else {
						more = false;
						Assert.assertEquals(purcahseTask.getMsg(), "ok", "搜索过滤采购任务失败");
					}
				}

				List<String> actual_order_ids = new ArrayList<String>();
				for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
					List<Task> tempList = purchaseTaskData.getTasks().stream()
							.filter(t -> t.getOrder_status() != order_status).collect(Collectors.toList());
					Assert.assertEquals(tempList.size(), 0,
							"按下单时间+订单状态搜索查询采购任务,过滤出不符合条件的采购任务 " + JsonUtil.objectToStr(tempList));

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
						expected_order_ids.removeAll(expected_order_ids);
						msg = String.format("如下订单:%s 还没有生成采购任务", expected_order_ids);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
				Assert.assertEquals(result, true, "采购任务生成与预期不一致,请确认!");
			}
		} catch (Exception e) {
			logger.error("搜索查询采购任务遇到错误: ", e);
			Assert.fail("搜索查询采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseTaskSearchTestCase13() {
		ReporterCSS.title("测试点: 按下单时间+供应商搜索查询采购任务");
		String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setQ_type(1);
		param.setBegin_time(begin_time);
		param.setEnd_time(begin_time);

		int limit = 50;
		int offset = 0;
		param.setLimit(limit);
		param.setOffset(offset);
		String page_obj = null;

		List<PurchaseTaskData> purchaseTaskDataList = null;
		PurchaseTaskBean purcahseTask = null;
		boolean more = true;
		try {
			List<SupplierDetailBean> suppliers = supplierService.getSettleSupplierList();
			Assert.assertNotEquals(suppliers, null, "获取供应商列表失败");
			for (SupplierDetailBean supplier : suppliers) {
				offset = 0;
				purchaseTaskDataList = new ArrayList<>();
				String supplier_id = supplier.getId();
				param.setSettle_supplier_id(supplier_id);
				while (more) {
					purcahseTask = purchaseTaskService.searchPurchaseTask(param);
					if (purcahseTask.getCode() == 0 && purcahseTask.getPurchaseTaskDataArray() != null
							&& purcahseTask.getPurchaseTaskDataArray() != null) {
						purchaseTaskDataList.addAll(purcahseTask.getPurchaseTaskDataArray());

						offset += limit;
						more = purcahseTask.getPagination().isMore();
						page_obj = purcahseTask.getPagination().getPage_obj();
						if (page_obj != null && !page_obj.equals("")) {
							param.setPage_obj(page_obj);
						} else {
							if (purcahseTask.getPurchaseTaskDataArray().size() < limit) {
								break;
							}
							param.setOffset(offset);
						}
					} else {
						more = false;
						Assert.assertEquals(purcahseTask.getMsg(), "ok", "搜索过滤采购任务失败");
					}
				}

				List<PurchaseTaskData> tempList = purchaseTaskDataList.stream()
						.filter(p -> !p.getSettle_supplier_id().equals(supplier_id)).collect(Collectors.toList());
				Assert.assertEquals(tempList.size(), 0,
						"按下单时间+供应商搜索查询采购任务,过滤出了不符合条件的采购任务 " + JsonUtil.objectToStr(tempList));
			}
		} catch (Exception e) {
			logger.error("搜索查询采购任务遇到错误: ", e);
			Assert.fail("搜索查询采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseTaskSearchTestCase14() {
		ReporterCSS.title("测试点: 按下单时间+配送路线搜索查询采购任务");
		String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setQ_type(1);
		param.setBegin_time(begin_time);
		param.setEnd_time(begin_time);

		int limit = 50;
		int offset = 0;
		param.setLimit(limit);
		param.setOffset(offset);
		String page_obj = null;

		List<PurchaseTaskData> purchaseTaskDataList = null;
		PurchaseTaskBean purcahseTask = null;
		boolean more = true;
		try {
			List<RouteBean> routes = routeService.getAllRoutes();
			Assert.assertNotEquals(routes, null, "获取路线列表失败");
			for (RouteBean route : routes) {
				purchaseTaskDataList = new ArrayList<>();
				BigDecimal route_id = route.getId();
				String route_name = route.getName();
				param.setPage_obj(null);
				param.setOffset(0);
				param.setRoute_id(route_id);
				while (more) {
					purcahseTask = purchaseTaskService.searchPurchaseTask(param);
					if (purcahseTask.getCode() == 0 && purcahseTask.getPurchaseTaskDataArray() != null) {
						purchaseTaskDataList.addAll(purcahseTask.getPurchaseTaskDataArray());
						offset += limit;
						more = purcahseTask.getPagination().isMore();
						page_obj = purcahseTask.getPagination().getPage_obj();
						if (page_obj != null && !page_obj.equals("")) {
							param.setPage_obj(page_obj);
						} else {
							if (purcahseTask.getPurchaseTaskDataArray().size() < limit) {
								break;
							}
							param.setOffset(offset);
						}
					} else {
						more = false;
						Assert.assertEquals(purcahseTask.getMsg(), "ok", "搜索过滤采购任务失败");
					}
				}

				for (PurchaseTaskData purchaseTaskData : purchaseTaskDataList) {
					List<Task> tempList = purchaseTaskData.getTasks().stream()
							.filter(t -> !t.getRoute_name().equals(route_name)).collect(Collectors.toList());
					Assert.assertEquals(tempList.size(), 0, "按下单时间+配送路线[" + route_name + "]搜索查询采购任务,过滤出了不符合过滤条件的采购任务 "
							+ JsonUtil.objectToStr(tempList));
				}
			}
		} catch (Exception e) {
			logger.error("搜索查询采购任务遇到错误: ", e);
			Assert.fail("搜索查询采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseTaskSearchTestCase15() {
		ReporterCSS.title("测试点: 按下单时间+采购员搜索查询采购任务");
		String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setQ_type(1);
		param.setBegin_time(begin_time);
		param.setEnd_time(begin_time);

		int limit = 50;
		int offset = 0;
		param.setLimit(limit);
		param.setOffset(offset);
		String page_obj = null;

		List<PurchaseTaskData> purchaseTaskDataList = null;
		PurchaseTaskBean purcahseTask = null;
		boolean more = true;
		try {
			purcahseTask = purchaseTaskService.searchPurchaseTask(param);
			Assert.assertEquals(purcahseTask.getCode() == 0 && purcahseTask.getPurchaseTaskDataArray() != null, true,
					"搜索过滤采购任务失败");
			purchaseTaskDataList = purcahseTask.getPurchaseTaskDataArray();
			PurchaseTaskData purchaseTaskData = purchaseTaskDataList.stream()
					.filter(p -> p.getPurchaser_id() != null && !p.getPurchaser_id().trim().equals("")).findFirst()
					.orElse(null);
			Assert.assertNotEquals(purchaseTaskData, null, "采购任务都没有绑定采购员,与预期 不符");

			String purchaser_id = purchaseTaskData.getPurchaser_id();
			param.setPurchaser_id(purchaser_id);
			purchaseTaskDataList = new ArrayList<PurchaseTaskBean.PurchaseTaskData>();
			while (more) {
				purcahseTask = purchaseTaskService.searchPurchaseTask(param);
				if (purcahseTask.getCode() == 0 && purcahseTask.getPurchaseTaskDataArray() != null) {
					purchaseTaskDataList.addAll(purcahseTask.getPurchaseTaskDataArray());
					offset += limit;
					more = purcahseTask.getPagination().isMore();
					page_obj = purcahseTask.getPagination().getPage_obj();
					if (page_obj != null && !page_obj.equals("")) {
						param.setPage_obj(page_obj);
					} else {
						if (purcahseTask.getPurchaseTaskDataArray().size() < limit) {
							break;
						}
						param.setOffset(offset);
					}
				} else {
					more = false;
					Assert.assertEquals(purcahseTask.getMsg(), "ok", "搜索过滤采购任务失败");
				}
			}
			List<String> purchaser_ids = purchaseTaskDataList.stream()
					.filter(p -> p.getPurchaser_id() == null || !p.getPurchaser_id().equals(purchaser_id))
					.map(p -> p.getPurchaser_id()).collect(Collectors.toList());
			Assert.assertEquals(purchaser_ids.size(), 0,
					"按下单时间+采购员[" + purchaser_id + "]搜索查询采购任务,搜索出了其他采购员" + purchaser_ids + "绑定的采购任务");
		} catch (Exception e) {
			logger.error("搜索查询采购任务遇到错误: ", e);
			Assert.fail("搜索查询采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseTaskSearchTestCase16() {
		ReporterCSS.title("测试点: 按下单时间+分拣状态搜索过滤采购任务");
		String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setQ_type(1);
		param.setBegin_time(begin_time);
		param.setEnd_time(begin_time);

		int limit = 50;
		int offset = 0;
		param.setLimit(limit);
		param.setOffset(offset);
		String page_obj = null;

		List<PurchaseTaskData> purchaseTaskDataList = null;
		PurchaseTaskBean purcahseTask = null;
		boolean more = true;
		try {
			// 修改订单状态
			boolean result = orderService.updateOrderState(order_id, 5);
			Assert.assertEquals(result, true, "修改订单 " + order_id + " 状态为分拣中失败");

			result = weightService.oneStepWeightOrder(order_id);
			Assert.assertEquals(result, true, "订单 " + order_id + " 分拣失败");

			param.setWeight_status(1);

			purchaseTaskDataList = new ArrayList<PurchaseTaskBean.PurchaseTaskData>();
			while (more) {
				purcahseTask = purchaseTaskService.searchPurchaseTask(param);
				if (purcahseTask.getCode() == 0 && purcahseTask.getPurchaseTaskDataArray() != null) {
					purchaseTaskDataList.addAll(purcahseTask.getPurchaseTaskDataArray());
					offset += limit;
					more = purcahseTask.getPagination().isMore();
					page_obj = purcahseTask.getPagination().getPage_obj();
					if (page_obj != null && !page_obj.equals("")) {
						param.setPage_obj(page_obj);
					} else {
						if (purcahseTask.getPurchaseTaskDataArray().size() < limit) {
							break;
						}
						param.setOffset(offset);
					}
				} else {
					more = false;
					Assert.assertEquals(purcahseTask.getMsg(), "ok", "搜索过滤采购任务失败");
				}
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
			Assert.assertEquals(find, true, "按下单时间+分拣状态[已分拣]搜索过滤采购任务,没有过滤出目标订单 " + order_id);
		} catch (Exception e) {
			logger.error("搜索查询采购任务遇到错误: ", e);
			Assert.fail("搜索查询采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseTaskSearchTestCase17() {
		ReporterCSS.title("测试点: 按下单时间+采购单搜索过滤采购任务");
		String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setQ_type(1);
		param.setBegin_time(begin_time);
		param.setEnd_time(begin_time);

		int limit = 50;
		int offset = 0;
		param.setLimit(limit);
		param.setOffset(offset);

		List<PurchaseTaskData> purchaseTaskDataList = null;
		PurchaseTaskBean purcahseTask = null;

		try {
			param.setHas_created_sheet(1);

			purcahseTask = purchaseTaskService.searchPurchaseTask(param);
			if (purcahseTask.getCode() == 0 && purcahseTask.getPurchaseTaskDataArray() != null) {
				purchaseTaskDataList = purcahseTask.getPurchaseTaskDataArray();
			} else {
				Assert.assertEquals(purcahseTask.getMsg(), "ok", "搜索过滤采购任务失败");
			}
			if (purchaseTaskDataList != null && purchaseTaskDataList.size() > 0) {
				PurchaseTaskData purchaseTaskData = NumberUtil.roundNumberInList(purchaseTaskDataList);
				String spec_id = purchaseTaskData.getSpec_id();
				BigDecimal release_id = purchaseTaskData.getRelease_id();

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
		} catch (Exception e) {
			logger.error("搜索查询采购任务遇到错误: ", e);
			Assert.fail("搜索查询采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseTaskSearchTestCase18() {
		ReporterCSS.title("测试点: 采购任务单据打印");
		try {
			String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
			PurchaseTaskPrintParam purchaseTaskPrintParam = new PurchaseTaskPrintParam();
			purchaseTaskPrintParam.setBegin_time(begin_time);
			purchaseTaskPrintParam.setEnd_time(begin_time);
			purchaseTaskPrintParam.setPrint_what("task");
			purchaseTaskPrintParam.setQ_type(1);
			purchaseTaskPrintParam.setIs_print(1);

			List<PurchaseTaskPrintBean> purchaseTaskPrintList = purchaseTaskService
					.purchaseTaskPrint(purchaseTaskPrintParam);
			Assert.assertNotEquals(purchaseTaskPrintList, null, "采购任务单据打印失败");

		} catch (Exception e) {
			logger.error("采购任务单据打印遇到错误: ", e);
			Assert.fail("采购任务单据打印遇到错误: ", e);
		}
	}
}
