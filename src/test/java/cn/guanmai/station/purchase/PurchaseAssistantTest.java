package cn.guanmai.station.purchase;

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
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.invoicing.SupplierBean;
import cn.guanmai.station.bean.order.OrderCycle;
import cn.guanmai.station.bean.purchase.PurchaseTaskCanChangeSupplierBean;
import cn.guanmai.station.bean.purchase.PurchaserBean;
import cn.guanmai.station.bean.purchase.PurchaserResponseBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean.PurchaseTaskData;
import cn.guanmai.station.bean.purchase.assistant.DailyCountBean;
import cn.guanmai.station.bean.purchase.assistant.DailyWorkBean;
import cn.guanmai.station.bean.purchase.assistant.PurchaseAssistantTaskBean;
import cn.guanmai.station.bean.purchase.assistant.PurchaseSheetBean;
import cn.guanmai.station.bean.purchase.assistant.PurchaseSheetCountBean;
import cn.guanmai.station.bean.purchase.assistant.PurchaseSheetDetailBean;
import cn.guanmai.station.bean.purchase.assistant.SupplierCountBean;
import cn.guanmai.station.bean.purchase.assistant.SupplierSpecBean;
import cn.guanmai.station.bean.purchase.assistant.SupplySkuBean;
import cn.guanmai.station.bean.purchase.param.PurchaserParam;
import cn.guanmai.station.bean.purchase.param.ReleasePurchaseTaskParam;
import cn.guanmai.station.bean.purchase.param.assistant.SheetCreateParam;
import cn.guanmai.station.bean.purchase.param.assistant.TaskFiterParam;
import cn.guanmai.station.bean.system.ServiceTimeBean;
import cn.guanmai.station.bean.system.param.OrderProfileParam;
import cn.guanmai.station.impl.invoicing.SupplierServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaseAssistantServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaseTaskServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaserServiceImpl;
import cn.guanmai.station.impl.system.ProfileServiceImpl;
import cn.guanmai.station.interfaces.invoicing.SupplierService;
import cn.guanmai.station.interfaces.purchase.PurchaseAssistantService;
import cn.guanmai.station.interfaces.purchase.PurchaseTaskService;
import cn.guanmai.station.interfaces.purchase.PurchaserService;
import cn.guanmai.station.interfaces.system.ProfileService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.station.tools.PurchaseTaskTool;
import cn.guanmai.util.LoginUtil;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date May 22, 2019 3:08:59 PM 
* @des 采购助手APP相关测试用例
* @version 1.0 
*/
public class PurchaseAssistantTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PurchaserCreateTest.class);
	private SupplierService supplierService;
	private PurchaseAssistantService purchaseAssistantService;
	private PurchaserService purchaserService;
	private PurchaseTaskService purchaseTaskService;
	// 采购员ID
	private String purchaser_id;
	private List<SupplierBean> suppliers;
	private OrderTool orderTool;
	private PurchaseTaskTool purchaseTaskTool;

	private String today = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
	private String time_config_id;
	private String cycle_start_time;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		purchaserService = new PurchaserServiceImpl(headers);
		supplierService = new SupplierServiceImpl(headers);
		purchaseTaskService = new PurchaseTaskServiceImpl(headers);
		orderTool = new OrderTool(headers);
		purchaseTaskTool = new PurchaseTaskTool(headers);
		try {
			ProfileService profileService = new ProfileServiceImpl(headers);
			OrderProfileParam orderProfileParam = new OrderProfileParam();
			orderProfileParam.setOrder_create_purchase_task(0);
			boolean result = profileService.updateOrderProfile(orderProfileParam);
			Assert.assertEquals(result, true, "设置订单商品手动进入采购任务失败");

			suppliers = supplierService.searchSupplier("");
			Assert.assertNotNull(suppliers, "获取站点的供应商列表失败");

			List<String> settle_suppliers = suppliers.stream().map(s -> s.getSupplier_id())
					.collect(Collectors.toList());

			String user_name = null;
			String password = "123456";
			List<PurchaserBean> purchasers = purchaserService.searchPurchaser("");
			Assert.assertNotEquals(purchasers, null, "搜索过滤采购员失败");
			if (purchasers.size() > 0) {
				PurchaserBean purchaser = purchasers.stream().filter(p -> p.getIs_allow_login() == 1).findFirst()
						.orElse(purchasers.get(0));
				// 原有绑定的供应商加上没绑定的供应商
				purchaser_id = purchaser.getId();
				user_name = purchaser.getUsername();
				PurchaserParam purchaserParam = new PurchaserParam();
				purchaserParam.setId(purchaser.getId());
				purchaserParam.setName(purchaser.getName());
				purchaserParam.setUsername(purchaser.getUsername());
				purchaserParam.setPassword(password);
				purchaserParam.setPhone(purchaser.getPhone());
				purchaserParam.setStatus(purchaser.getStatus());
				purchaserParam.setIs_allow_login(1);
				purchaserParam.setSettle_suppliers(settle_suppliers);

				PurchaserResponseBean purchaserResponse = purchaserService.updatePurchaser(purchaserParam);
				Assert.assertEquals(purchaserResponse.getCode(), 0, "修改采购员信息失败");
			} else {
				String phone = "12" + StringUtil.getRandomNumber(9);
				user_name = "AT" + StringUtil.getRandomNumber(6);

				PurchaserParam purchaserParam = new PurchaserParam();
				purchaserParam.setUsername(user_name);
				purchaserParam.setName(user_name);
				purchaserParam.setPhone(phone);
				purchaserParam.setPassword(password);
				purchaserParam.setSettle_suppliers(settle_suppliers);
				purchaserParam.setIs_allow_login(1);
				purchaserParam.setStatus(1);

				PurchaserResponseBean purchaserResponse = purchaserService.createPurchaser(purchaserParam);
				Assert.assertEquals(purchaserResponse.getMsg(), "ok", "创建采购员失败");

				purchaser_id = purchaserResponse.getData();
			}

			Map<String, String> pa_cookie = LoginUtil.loginPurchaseAssistant(user_name, password);
			Assert.assertNotNull(pa_cookie, "采购助手登录失败");
			purchaseAssistantService = new PurchaseAssistantServiceImpl(pa_cookie);
		} catch (Exception e) {
			logger.error("采购助手登录失败: ", e);
			Assert.fail("采购助手登录失败: ", e);
		}
	}

	@Test
	public void purchaseAssistantTestCase01() {
		ReporterCSS.title("测试点: 获取采购助手登录账号信息");
		try {
			PurchaserBean purchaser = purchaseAssistantService.getPurchaserInfo();
			Assert.assertNotNull(purchaser, "获取采购助手登录账号信息遇失败");
		} catch (Exception e) {
			logger.error("获取采购助手登录账号信息遇到错误: ", e);
			Assert.fail("获取采购助手登录账号信息遇到错误: ", e);
		}
	}

	@Test
	public void purchaseAssistantTestCase02() {
		ReporterCSS.title("测试点: 获取采购助手登录账号绑定的供应商列表");
		try {
			List<String> temp_suppliers = purchaseAssistantService.getQuotedSettleSuppliers();
			Assert.assertNotNull(temp_suppliers, "获取采购助手登录账号绑定的供应商列表失败");

			boolean result = true;
			for (SupplierBean supplier : suppliers) {
				String supplier_id = supplier.getSupplier_id();
				if (!temp_suppliers.contains(supplier_id)) {
					ReporterCSS.warn("获取采购助手登录账号绑定的供应商列表中缺少了供应商 " + supplier_id);
					logger.warn("获取采购助手登录账号绑定的供应商列表中缺少了供应商 " + supplier_id);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "获取采购助手登录账号绑定的供应商列表与设置的不一致");
		} catch (Exception e) {
			logger.error("获取采购助手登录账号绑定的供应商列表遇到错误: ", e);
			Assert.fail("获取采购助手登录账号绑定的供应商列表遇到错误: ", e);
		}
	}

	@Test
	public void purchaseAssistantTestCase03() {
		ReporterCSS.title("测试点: 采购助手APP获取运营时间");
		try {
			List<ServiceTimeBean> serviceTimes = purchaseAssistantService.getPurchaseAssistantServiceTime();
			Assert.assertNotNull(serviceTimes, "采购助手APP获取运营时间失败");
		} catch (Exception e) {
			logger.error("采购助手APP获取运营时间遇到错误: ", e);
			Assert.fail("采购助手APP获取运营时间遇到错误: ", e);
		}
	}

	@Test
	public void purchaseAssistantTestCase04() {
		ReporterCSS.title("测试点: 采购助手APP获取今日任务");
		try {
			DailyWorkBean dailyWork = purchaseAssistantService.getPurchaseAssistantDailyWork();
			Assert.assertNotNull(dailyWork, "采购助手APP获取今日任务失败");
		} catch (Exception e) {
			logger.error("采购助手APP获取今日任务遇到错误: ", e);
			Assert.fail("采购助手APP获取今日任务遇到错误: ", e);
		}
	}

	@Test
	public void purchaseAssistantTestCase05() {
		ReporterCSS.title("测试点: 采购助手APP获取采购金额趋势统计");
		try {
			DailyCountBean dailyCount = purchaseAssistantService.getPurchaseAssistantDailyCount(7);
			Assert.assertNotNull(dailyCount, "采购助手APP获取采购金额趋势统计失败");
		} catch (Exception e) {
			logger.error("采购助手APP获取采购金额趋势统计遇到错误: ", e);
			Assert.fail("采购助手APP获取采购金额趋势统计遇到错误: ", e);
		}
	}

	@Test
	public void purchaseAssistantTestCase06() {
		ReporterCSS.title("测试点: 采购助手APP采购金额分布统计(供应商维度)");
		try {
			SupplierCountBean supplierCount = purchaseAssistantService.getPurchaseAssistantSupplierCount(7);
			Assert.assertNotNull(supplierCount, "采购助手APP采购金额分布统计(供应商维度)失败");
		} catch (Exception e) {
			logger.error("采购助手APP采购金额分布统计(供应商维度)遇到错误: ", e);
			Assert.fail("采购助手APP采购金额分布统计(供应商维度)遇到错误: ", e);
		}
	}

	@Test
	public void purchaseAssistantTestCase07() {
		ReporterCSS.title("测试点: 采购助手APP进行询价");
		try {
			List<String> quated_suppliers = purchaseAssistantService.getQuotedSettleSuppliers();
			Assert.assertNotNull(quated_suppliers, "采购助手APP获取登录账号绑定的供应商列表失败");

			Assert.assertEquals(quated_suppliers.size() > 0, true, "采购助手APP获取登录账号没有绑定供应商,无法进行后续操作");

			String supplier_id = quated_suppliers.get(0);

			List<SupplierSpecBean> supplierSpecs = purchaseAssistantService.getSupplierSpecs(supplier_id, 0, "");
			Assert.assertNotNull(supplierSpecs, "采购助手APP拉取指定供应商能提供的采购规格列表失败");

			Assert.assertEquals(supplierSpecs.size() > 0, true, "此供应商下没有任何采购规格可以提供,无法进行后续操作");

			SupplierSpecBean supplierSpec = supplierSpecs.get(0);
			String spec_id = supplierSpec.getSpec_id();
			String origin_place = "深圳南山";
			String remark = StringUtil.getRandomString(6);
			BigDecimal price = new BigDecimal("2");
			boolean result = purchaseAssistantService.updateSpecQuotedPrice(spec_id, price, remark, origin_place,
					supplier_id);
			Assert.assertEquals(result, true, "更新采购规格的询价操作失败");

			supplierSpecs = purchaseAssistantService.getSupplierSpecs(supplier_id, 0, "");
			Assert.assertNotNull(supplierSpecs, "采购助手APP拉取指定供应商能提供的采购规格列表失败");

			supplierSpec = supplierSpecs.stream().filter(s -> s.getSpec_id().equals(spec_id)).findAny().orElse(null);

			String msg = null;
			if (!supplierSpec.getOrigin_place().equals(origin_place)) {
				msg = String.format("采购规格询价更新的原产地值与输入的不符,预期:%s,实际:%s", origin_place, supplierSpec.getOrigin_place());
				ReporterCSS.warn(msg);
				result = false;
			}

			if (supplierSpec.getPrice().compareTo(price) != 0) {
				msg = String.format("采购规格询价更新的询价值与输入的不符,预期:%s,实际:%s", price, supplierSpec.getPrice());
				ReporterCSS.warn(msg);
				result = false;
			}

			if (!supplierSpec.getRemark().equals(remark)) {
				msg = String.format("采购规格询价更新的备注与输入的不符,预期:%s,实际:%s", remark, supplierSpec.getRemark());
				ReporterCSS.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "采购规格询价更新的信息与输入的不符");
		} catch (Exception e) {
			logger.error("采购助手APP进行询价遇到错误: ", e);
			Assert.fail("采购助手APP进行询价遇到错误: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseAssistantTestCase08() {
		ReporterCSS.title("测试点: 采购任务发布给采购员,采购员在采购助手查看数据");
		try {
			String order_id = orderTool.oneStepCreateOrder(6);
			Assert.assertNotNull(order_id, "新建订单失败");

			OrderCycle orderCycle = orderTool.getOrderOperationCycle(order_id);
			time_config_id = orderCycle.getTime_config_id();
			cycle_start_time = orderCycle.getCycle_start_time();

			// 获取订单对应的采购任务
			List<PurchaseTaskData> purchaseTasks = purchaseTaskTool.getOrderPurcahseTask(order_id);
			Assert.assertEquals(purchaseTasks.size() > 0, true, "订单 " + order_id + " 的采购任务在订单创建后60秒内没有生成");

			// 如果采购任务没有对应的供应商或者供应商已经被删除，需要重新分配供应商
			String supplier_status = null;
			String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
			for (PurchaseTaskData purchaseTask : purchaseTasks) {
				supplier_status = purchaseTask.getSupplier_status();
				if (supplier_status.equals("") || supplier_status.equals("0")) {
					List<PurchaseTaskCanChangeSupplierBean> purchaseTaskCanChangeSuppliers = purchaseTaskService
							.searchPurchaseTaskCanChangeSuppliers(1, begin_time, begin_time, purchaseTask.getSpec_id());
					Assert.assertNotEquals(purchaseTaskCanChangeSuppliers, null, "获取可更改供应商列表失败");

					PurchaseTaskCanChangeSupplierBean purchaseTaskCanChangeSupplier = NumberUtil
							.roundNumberInList(purchaseTaskCanChangeSuppliers);

					String new_settle_supplier_id = purchaseTaskCanChangeSupplier.getSettle_supplier_id();

					List<String> task_ids = new ArrayList<String>();
					for (PurchaseTaskData.Task task : purchaseTask.getTasks()) {
						task_ids.add(task.getId());
					}

					boolean result = purchaseTaskService.purchaseTaskChangeSupplier(task_ids, new_settle_supplier_id);
					Assert.assertEquals(result, true, "采购任务修改供应商失败");
				}
			}

			Map<String, BigDecimal> expected_purcahse_task = new HashMap<>();
			List<String> task_ids = new ArrayList<>();
			for (PurchaseTaskData purchaseTask : purchaseTasks) {
				for (PurchaseTaskData.Task task : purchaseTask.getTasks()) {
					task_ids.add(task.getId());
					if (task.getOrder_id().equals(order_id)) {
						String spec_id = purchaseTask.getSpec_id();
						if (expected_purcahse_task.containsKey(spec_id)) {
							expected_purcahse_task.put(spec_id,
									expected_purcahse_task.get(spec_id).add(task.getPlan_purchase_amount()));
						} else {
							expected_purcahse_task.put(spec_id, task.getPlan_purchase_amount());
						}
					}
				}
			}

			boolean result = purchaseTaskService.purchaseTaskChangePurchaser(task_ids, purchaser_id);
			Assert.assertEquals(result, true, "采购任务切换采购员失败");

			// 发布采购任务操作

			ReleasePurchaseTaskParam fiterParam = new ReleasePurchaseTaskParam();
			fiterParam.setBegin_time(today);
			fiterParam.setEnd_time(today);
			fiterParam.setTask_ids(JSONArray.parseArray(task_ids.toString()));
			fiterParam.setQ_type(1);
			fiterParam.setTask_suggests(new JSONArray());

			result = purchaseTaskService.releasePurchaseTask(fiterParam);
			Assert.assertEquals(result, true, "发布采购任务失败");

			TaskFiterParam taskFiterParam = new TaskFiterParam();
			taskFiterParam.setQ_type(1);
			taskFiterParam.setBegin_time(today);
			taskFiterParam.setEnd_time(today);
			taskFiterParam.setStatus(0);
			taskFiterParam.setSort_type(1);
			taskFiterParam.setTime_config_id(time_config_id);

			List<PurchaseAssistantTaskBean> purchaseAssistantTasks = purchaseAssistantService
					.searchPurchaseAssistantTask(taskFiterParam);

			Assert.assertNotNull(purchaseAssistantTasks, "采购助手APP获取采购任务失败");
		} catch (Exception e) {
			logger.error("采购任务发布给采购员,采购员在采购助手查看数据过程中遇到错误: ", e);
			Assert.fail("采购任务发布给采购员,采购员在采购助手查看数据过程中遇到错误: ", e);
		}
	}

	@Test(timeOut = 90000)
	public void purchaseAssistantTestCase09() {
		ReporterCSS.title("测试点: 采购任务发布给采购员,验证订单数据是否一致");
		try {
			String order_id = orderTool.oneStepCreateOrder(6);
			Assert.assertNotNull(order_id, "新建订单失败");

			OrderCycle orderCycle = orderTool.getOrderOperationCycle(order_id);
			time_config_id = orderCycle.getTime_config_id();
			cycle_start_time = orderCycle.getCycle_start_time();

			// 获取订单对应的采购任务
			List<PurchaseTaskData> purchaseTasks = purchaseTaskTool.getOrderPurcahseTask(order_id);
			Assert.assertEquals(purchaseTasks.size() > 0, true, "订单 " + order_id + " 的采购任务在订单创建后60秒内没有生成");

			Map<String, BigDecimal> expected_purcahse_task = new HashMap<>();
			List<String> task_ids = new ArrayList<>();
			// 如果采购任务没有对应的供应商或者供应商已经被删除，需要重新分配供应商
			String supplier_status = null;
			String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
			for (PurchaseTaskData purchaseTask : purchaseTasks) {
				supplier_status = purchaseTask.getSupplier_status();
				if (supplier_status.equals("") || supplier_status.equals("0")) {
					List<PurchaseTaskCanChangeSupplierBean> purchaseTaskCanChangeSuppliers = purchaseTaskService
							.searchPurchaseTaskCanChangeSuppliers(1, begin_time, begin_time, purchaseTask.getSpec_id());
					Assert.assertNotEquals(purchaseTaskCanChangeSuppliers, null, "获取可更改供应商列表失败");

					PurchaseTaskCanChangeSupplierBean purchaseTaskCanChangeSupplier = NumberUtil
							.roundNumberInList(purchaseTaskCanChangeSuppliers);

					String new_settle_supplier_id = purchaseTaskCanChangeSupplier.getSettle_supplier_id();

					List<String> temp_task_ids = new ArrayList<String>();
					for (PurchaseTaskData.Task task : purchaseTask.getTasks()) {
						temp_task_ids.add(task.getId());
					}

					boolean result = purchaseTaskService.purchaseTaskChangeSupplier(temp_task_ids,
							new_settle_supplier_id);
					Assert.assertEquals(result, true, "采购任务修改供应商失败");
				}

				for (PurchaseTaskData.Task task : purchaseTask.getTasks()) {
					task_ids.add(task.getId());
					if (task.getOrder_id().equals(order_id)) {
						String spec_id = purchaseTask.getSpec_id();
						if (expected_purcahse_task.containsKey(spec_id)) {
							expected_purcahse_task.put(spec_id,
									expected_purcahse_task.get(spec_id).add(task.getPlan_purchase_amount()));
						} else {
							expected_purcahse_task.put(spec_id, task.getPlan_purchase_amount());
						}
					}
				}
			}

			boolean result = purchaseTaskService.purchaseTaskChangePurchaser(task_ids, purchaser_id);
			Assert.assertEquals(result, true, "采购任务切换采购员失败");

			// 发布采购任务操作

			ReleasePurchaseTaskParam fiterParam = new ReleasePurchaseTaskParam();
			fiterParam.setBegin_time(today);
			fiterParam.setEnd_time(today);
			fiterParam.setTask_ids(JSONArray.parseArray(task_ids.toString()));
			fiterParam.setQ_type(1);
			fiterParam.setTask_suggests(new JSONArray());

			result = purchaseTaskService.releasePurchaseTask(fiterParam);
			Assert.assertEquals(result, true, "发布采购任务失败");

			TaskFiterParam taskFiterParam = new TaskFiterParam();
			taskFiterParam.setQ_type(1);
			taskFiterParam.setBegin_time(today);
			taskFiterParam.setEnd_time(today);
			taskFiterParam.setStatus(0);
			taskFiterParam.setSort_type(1);
			taskFiterParam.setTime_config_id(time_config_id);

			List<PurchaseAssistantTaskBean> purchaseAssistantTasks = purchaseAssistantService
					.searchPurchaseAssistantTask(taskFiterParam);

			Assert.assertNotNull(purchaseAssistantTasks, "采购助手APP获取采购任务失败");

			Map<String, BigDecimal> actual_purchase_task = new HashMap<>();

			for (PurchaseAssistantTaskBean purchaseAssistantTask : purchaseAssistantTasks) {
				for (PurchaseAssistantTaskBean.Detail detail : purchaseAssistantTask.getDetails()) {
					if (detail.getOrder_id().equals(order_id)) {
						String spec_id = purchaseAssistantTask.getSpec_id();
						if (actual_purchase_task.containsKey(spec_id)) {
							actual_purchase_task.put(spec_id,
									actual_purchase_task.get(spec_id).add(detail.getPlan_purchase_amount()));
						} else {
							actual_purchase_task.put(spec_id, detail.getPlan_purchase_amount());
						}
					}
				}
			}

			Assert.assertEquals(actual_purchase_task.equals(expected_purcahse_task), true,
					"采购助手APP收到的采购任务与ST发布的采购任务不一致,预期: " + expected_purcahse_task + ",实际: " + actual_purchase_task);
		} catch (Exception e) {
			logger.error("采购任务发布给采购员,采购员在采购助手查看数据过程中遇到错误: ", e);
			Assert.fail("采购任务发布给采购员,采购员在采购助手查看数据过程中遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseAssistantTestCase08" }, timeOut = 60000)
	public void purchaseAssistantTestCase10() {
		ReporterCSS.title("测试点: 采购助手APP生成采购单并提交");
		try {
			TaskFiterParam taskFiterParam = new TaskFiterParam();
			taskFiterParam.setQ_type(1);
			taskFiterParam.setBegin_time(today);
			taskFiterParam.setEnd_time(today);
			taskFiterParam.setStatus(0);
			taskFiterParam.setSort_type(1);

			List<PurchaseAssistantTaskBean> purchaseAssistantTasks = purchaseAssistantService
					.searchPurchaseAssistantTask(taskFiterParam);

			Assert.assertNotNull(purchaseAssistantTasks, "采购助手APP获取采购任务失败");

			List<String> release_ids = new ArrayList<>();
			for (PurchaseAssistantTaskBean purchaseAssistantTask : purchaseAssistantTasks) {
				release_ids.add(purchaseAssistantTask.getRelease_id());
			}

			SheetCreateParam param = new SheetCreateParam();
			param.setQ_type(1);
			param.setRelease_ids(release_ids);
			param.setBegin_time(today);
			param.setEnd_time(today);
			param.setCycle_start_time(cycle_start_time + ":00");
			param.setTime_config_id(time_config_id);

			List<String> sheet_nos = purchaseAssistantService.createPurchaseAssistantSheet(param);
			Assert.assertNotNull(sheet_nos, "采购助手APP生成采购单失败");

		} catch (Exception e) {
			logger.error("采购助手APP生成采购单并提交的过程中遇到错误: ", e);
			Assert.fail("采购助手APP生成采购单并提交的过程中遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseAssistantTestCase10" })
	public void purchaseAssistantTestCase11() {
		ReporterCSS.title("测试点: 采购助手APP采购单据列表");
		try {
			String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
			String end_time = TimeUtil.getCurrentTime("yyyy-MM-dd 23:59:59");

			PurchaseSheetCountBean purchaseSheetCoun = purchaseAssistantService.getPurchaseSheetCountInfo(begin_time,
					end_time, 0);
			Assert.assertNotNull(purchaseSheetCoun, "采购助手APP采购单据页面获取采购单据统计信息失败");

		} catch (Exception e) {
			logger.error("采购助手APP采购单据列表页面报错: ", e);
			Assert.fail("采购助手APP采购单据列表页面报错: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseAssistantTestCase10" })
	public void purchaseAssistantTestCase12() {
		ReporterCSS.title("测试点: 采购助手APP采购单据列表");
		try {
			String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
			String end_time = TimeUtil.getCurrentTime("yyyy-MM-dd 23:59:59");

			List<PurchaseSheetBean> purchaseSheets = purchaseAssistantService.getPurchaseSheets(begin_time, end_time, 1,
					0);
			Assert.assertNotNull(purchaseSheets, "采购助手APP采购单据页面获取采购单据列表失败");

		} catch (Exception e) {
			logger.error("采购助手APP采购单据列表页面报错: ", e);
			Assert.fail("采购助手APP采购单据列表页面报错: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseAssistantTestCase10" })
	public void purchaseAssistantTestCase13() {
		ReporterCSS.title("测试点: 采购助手APP-获取采购单据详情");
		try {
			String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
			String end_time = TimeUtil.getCurrentTime("yyyy-MM-dd 23:59:59");

			List<PurchaseSheetBean> purchaseSheets = purchaseAssistantService.getPurchaseSheets(begin_time, end_time, 1,
					0);
			Assert.assertNotNull(purchaseSheets, "采购助手APP采购单据页面获取采购单据列表失败");

			Assert.assertEquals(purchaseSheets.size() > 0, true, "采购助手APP-采购单据列表页面无数据,与预想不一致");

			String sheet_no = purchaseSheets.get(0).getId();

			PurchaseSheetDetailBean purchaseSheetDetail = purchaseAssistantService.getPurchaseSheetDetail(sheet_no);

			Assert.assertNotNull(purchaseSheetDetail, "采购助手APP-采购单据页面-获取采购单据的详细信息失败");

		} catch (Exception e) {
			logger.error("采购助手APP-采购单据页面-获取采购单据的详细信息遇到错误: ", e);
			Assert.fail("采购助手APP-采购单据页面-获取采购单据的详细信息遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseAssistantTestCase10" })
	public void purchaseAssistantTestCase14() {
		ReporterCSS.title("测试点: 采购助手APP-编辑采购单据");
		try {
			String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
			String end_time = TimeUtil.getCurrentTime("yyyy-MM-dd 23:59:59");

			List<PurchaseSheetBean> purchaseSheets = purchaseAssistantService.getPurchaseSheets(begin_time, end_time, 1,
					0);
			Assert.assertNotNull(purchaseSheets, "采购助手APP采购单据页面获取采购单据列表失败");

			Assert.assertEquals(purchaseSheets.size() > 0, true, "采购助手APP-采购单据列表页面无数据,与预想不一致");

			String sheet_no = purchaseSheets.get(0).getId();

			PurchaseSheetDetailBean purchaseSheetDetail = purchaseAssistantService.getPurchaseSheetDetail(sheet_no);

			Assert.assertNotNull(purchaseSheetDetail, "采购助手APP-采购单据页面-获取采购单据的详细信息失败");

			boolean result = purchaseAssistantService.modifyPurchaseSheet(sheet_no, purchaseSheetDetail);
			Assert.assertEquals(result, true, "采购助手APP-编辑采购单据失败");

		} catch (Exception e) {
			logger.error("采购助手APP-编辑采购单据遇到错误: ", e);
			Assert.fail("采购助手APP-编辑采购单据遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseAssistantTestCase08" }, timeOut = 60000)
	public void purchaseAssistantTestCase15() {
		ReporterCSS.title("测试点: 采购助手APP删除采购单据");
		try {
			TaskFiterParam taskFiterParam = new TaskFiterParam();
			taskFiterParam.setQ_type(1);
			taskFiterParam.setBegin_time(today);
			taskFiterParam.setEnd_time(today);
			taskFiterParam.setStatus(0);
			taskFiterParam.setSort_type(1);

			List<PurchaseAssistantTaskBean> purchaseAssistantTasks = purchaseAssistantService
					.searchPurchaseAssistantTask(taskFiterParam);

			Assert.assertNotNull(purchaseAssistantTasks, "采购助手APP获取采购任务失败");

			List<String> release_ids = new ArrayList<>();
			for (PurchaseAssistantTaskBean purchaseAssistantTask : purchaseAssistantTasks) {
				release_ids.add(purchaseAssistantTask.getRelease_id());
			}

			SheetCreateParam param = new SheetCreateParam();
			param.setQ_type(1);
			param.setRelease_ids(release_ids);
			param.setBegin_time(today);
			param.setEnd_time(today);
			param.setCycle_start_time(cycle_start_time + ":00");
			param.setTime_config_id(time_config_id);

			List<String> sheet_nos = purchaseAssistantService.createPurchaseAssistantSheet(param);
			Assert.assertNotNull(sheet_nos, "采购助手APP生成采购单失败");

			boolean result = purchaseAssistantService.deletePurchaseSheet(sheet_nos.get(0));
			Assert.assertEquals(result, true, "采购助手-删除采购单据失败");

		} catch (Exception e) {
			logger.error("采购助手-删除采购单据失败遇到错误: ", e);
			Assert.fail("采购助手-删除采购单据失败遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseAssistantTestCase10" })
	public void purchaseAssistantTestCase16() {
		ReporterCSS.title("测试点: 采购助手APP-提交采购单据");
		try {
			String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
			String end_time = TimeUtil.getCurrentTime("yyyy-MM-dd 23:59:59");

			List<PurchaseSheetBean> purchaseSheets = purchaseAssistantService.getPurchaseSheets(begin_time, end_time, 1,
					0);
			Assert.assertNotNull(purchaseSheets, "采购助手APP采购单据页面获取采购单据列表失败");

			Assert.assertEquals(purchaseSheets.size() > 0, true, "采购助手APP-采购单据列表页面无数据,与预想不一致");

			String sheet_no = purchaseSheets.get(0).getId();

			boolean result = purchaseAssistantService.submitPurchaseAssistantSheet(sheet_no);
			Assert.assertEquals(result, true, "采购APP-提交采购单据失败");

			PurchaseSheetDetailBean purchaseSheetDetail = purchaseAssistantService.getPurchaseSheetDetail(sheet_no);
			Assert.assertNotNull(purchaseSheetDetail, "采购助手APP获取采购单据详情失败");

			List<BigDecimal> release_ids = purchaseSheetDetail.getTasks().stream().map(t -> t.getRelease_id())
					.collect(Collectors.toList());

			result = purchaseAssistantService.finishTask(release_ids);
			Assert.assertEquals(result, true, "采购单据标记完成失败");

		} catch (Exception e) {
			logger.error("采购助手APP-提交采购单据遇到错误: ", e);
			Assert.fail("采购助手APP-提交采购单据遇到错误: ", e);
		}
	}

	@Test
	public void purchaseAssistantTestCase17() {
		ReporterCSS.title("测试点: 采购助手采购单新增采购条目,指定供应商拉取它对应的采购规格列表");
		try {
			List<String> settle_supplier_ids = purchaseAssistantService.getQuotedSettleSuppliers();

			String settle_supplier_id = settle_supplier_ids.get(0);

			List<SupplySkuBean> supplySkus = purchaseAssistantService.getSettleSupplierSupplySkus(settle_supplier_id);
			Assert.assertNotNull(supplySkus, "采购助手采购单新增采购条目,指定供应商拉取它对应的采购规格列表失败");
		} catch (Exception e) {
			logger.error("采购助手采购单新增采购条目,指定供应商拉取它对应的采购规格列表遇到错误: ", e);
			Assert.fail("采购助手采购单新增采购条目,指定供应商拉取它对应的采购规格列表遇到错误: ", e);
		}
	}
}
