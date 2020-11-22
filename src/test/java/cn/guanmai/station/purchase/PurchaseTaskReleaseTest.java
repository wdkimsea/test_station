package cn.guanmai.station.purchase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.async.AsyncTaskResultBean;
import cn.guanmai.station.bean.category.MerchandiseTreeBean;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.bean.purchase.PurcahseTaskSummaryBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskExpectedBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskHistoryBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskShareBean;
import cn.guanmai.station.bean.purchase.PurchaserBean;
import cn.guanmai.station.bean.purchase.PurchaserResponseBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean.PurchaseTaskData;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean.PurchaseTaskData.Task;
import cn.guanmai.station.bean.purchase.PurchaserBean.SettleSupplier;
import cn.guanmai.station.bean.purchase.SupplyLimitBean;
import cn.guanmai.station.bean.purchase.param.PurchaseSheetCreateParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskFilterParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskHistoryFilterParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskShareCreateParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskShareGetParam;
import cn.guanmai.station.bean.purchase.param.PurchaserParam;
import cn.guanmai.station.bean.purchase.param.ReleasePurchaseTaskParam;
import cn.guanmai.station.bean.purchase.param.SupplyLimitFilterParam;
import cn.guanmai.station.bean.purchase.param.SupplyLimitFilterParam.SupplierSpec;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.bean.system.param.OrderProfileParam;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.base.DownloadServiceImpl;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.invoicing.SupplierServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaseTaskServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaserServiceImpl;
import cn.guanmai.station.impl.system.ProfileServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.base.DownloadService;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.invoicing.SupplierService;
import cn.guanmai.station.interfaces.purchase.PurchaseTaskService;
import cn.guanmai.station.interfaces.purchase.PurchaserService;
import cn.guanmai.station.interfaces.system.ProfileService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
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
* @date Apr 17, 2019 2:46:26 PM 
* @des 验证采购任务
* @version 1.0 
*/
public class PurchaseTaskReleaseTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PurchaseTaskReleaseTest.class);
	private OrderTool orderTool;
	private AsyncService asyncService;
	private DownloadService downloadService;
	private PurchaseTaskTool purchaseTaskTool;
	private PurchaseTaskService purchaseTaskService;
	private PurchaserService purchaserService;
	private CategoryService categoryService;
	private SupplierService supplierService;
	private LoginUserInfoService loginUserInfoService;
	private String order_id;
	private InitDataBean initData;
	private String today = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
	private String tommrow;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		orderTool = new OrderTool(headers);
		downloadService = new DownloadServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
		purchaseTaskTool = new PurchaseTaskTool(headers);
		purchaseTaskService = new PurchaseTaskServiceImpl(headers);
		purchaserService = new PurchaserServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		supplierService = new SupplierServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		try {
			initData = getInitData();
			tommrow = TimeUtil.calculateTime("yyyy-MM-dd 00:00:00", today, 1, Calendar.DATE);
			ProfileService profileService = new ProfileServiceImpl(headers);
			OrderProfileParam orderProfileParam = new OrderProfileParam();
			orderProfileParam.setOrder_create_purchase_task(0);
			boolean result = profileService.updateOrderProfile(orderProfileParam);
			Assert.assertEquals(result, true, "设置订单商品手动进入采购任务失败");

			// 把没有绑定采购员的供应商都绑定采购员
			List<SettleSupplier> settleSupplierArray = purchaserService.getNoBindSettleSupplierArray();
			Assert.assertNotEquals(settleSupplierArray, null, "获取没有绑定采购员的供应商列表失败");

			String purchaser_id = null;
			List<String> settle_suppliers = new ArrayList<String>();
			if (settleSupplierArray.size() > 0) {
				settleSupplierArray.stream().forEach(s -> {
					settle_suppliers.add(s.getId());
				});
			}

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

			List<SupplierDetailBean> supplierList = supplierService.getSettleSupplierList();
			Assert.assertNotEquals(supplierList, null, "获取站点所有的供应商失败");

			String supplier_id = null;
			SupplierDetailBean supplierDetail = null;
			for (SupplierDetailBean supplier : supplierList) {
				supplier_id = supplier.getId();
				supplierDetail = supplierService.getSupplierById(supplier_id);
				Assert.assertNotEquals(supplierDetail, null, "获取" + supplier_id + "供应商详细信息失败");

				if (supplierDetail.getDefault_purchaser_id() == null
						|| supplierDetail.getDefault_purchaser_id().equals("null")) {
					supplierDetail.setDefault_purchaser_id(purchaser_id);
					result = supplierService.updateSupplier(supplierDetail);
					Assert.assertEquals(result, true, "为供应商 " + supplier_id + "绑定默认采购员失败");
				}
			}
		} catch (Exception e) {
			logger.error("初始化数据失败: ", e);
			Assert.fail("初始化数据失败: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			order_id = orderTool.oneStepCreateOrder(8);
			Assert.assertNotEquals(order_id, null, "创建订单失败");
		} catch (Exception e) {
			logger.error("创建订单的过程中遇到错误: ", e);
			Assert.fail("创建订单的过程中遇到错误: ", e);
		}
	}

	/**
	 * 验证订单是否生成采购任务
	 * 
	 */
	@Test
	public void purchaseTaskTestCase01() {
		try {
			ReporterCSS.title("测试点: 验证订单是否生成采购任务");

			// 获取预期生成的采购任务
			List<PurchaseTaskExpectedBean> purchaseTaskExpectedList = purchaseTaskTool
					.getOrderExpectedPurcahseTask(order_id);
			Assert.assertNotEquals(purchaseTaskExpectedList, null, "获取订单" + order_id + "预期的采购任务失败");

			List<PurchaseTaskData> purchaseTaskDataArray = purchaseTaskTool.getOrderPurcahseTask(order_id);
			Assert.assertEquals(purchaseTaskDataArray != null && purchaseTaskDataArray.size() > 0, true,
					"订单" + order_id + "的采购任务在1分钟内没有生成");

			boolean result = true;
			String msg = null;
			for (PurchaseTaskExpectedBean purchaseTaskExpected : purchaseTaskExpectedList) {
				String spec_id = purchaseTaskExpected.getSpec_id();
				String supplier_id = purchaseTaskExpected.getSupplier_id();
				PurchaseTaskData purchaseTaskData = purchaseTaskDataArray.stream()
						.filter(p -> p.getSpec_id().equals(spec_id) && p.getSettle_supplier_id().equals(supplier_id))
						.findAny().orElse(null);
				if (purchaseTaskData == null) {
					msg = String.format("订单%s里的商品%s没有生成对应的采购任务 ", order_id,
							purchaseTaskExpected.getSku_ids().toString());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (purchaseTaskData.getPlan_purchase_amount()
						.compareTo(purchaseTaskExpected.getPlan_purchase_amount()) != 0) {
					msg = String.format("订单%s里的商品%s生成的采购数不正确,预期:%s,实际:%s ", order_id,
							purchaseTaskExpected.getSku_ids().toString(),
							purchaseTaskExpected.getPlan_purchase_amount(), purchaseTaskData.getPlan_purchase_amount());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			msg = JsonUtil.objectToStr(purchaseTaskExpectedList);
			ReporterCSS.title("预期的采购任务为: " + msg);
			logger.info(msg);
			Assert.assertEquals(result, true, "订单" + order_id + "生成的采购任务与预期不符");

		} catch (Exception e) {
			logger.error("验证订单生成的采购任务遇到错误: ", e);
			Assert.fail("验证订单生成的采购任务遇到错误: ", e);
		}
	}

	@Test
	public void purchaseTaskTestCase02() {
		ReporterCSS.title("测试点: 采购任务总览查看");

		// 采购任务过滤对象参数
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setBegin_time(today);
		param.setEnd_time(tommrow);
		param.setQ_type(1);
		param.setLimit(10);
		try {
			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService.getPurcahseTaskSummary(param);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览查看失败");
		} catch (Exception e) {
			logger.error("采购任务总览查看遇到错误: ", e);
			Assert.fail("采购任务总览查看遇到错误: ", e);
		}
	}

	@Test
	public void purchaseTaskTestCase03() {
		ReporterCSS.title("测试点: 获取采购任务建议采购数");

		// 采购任务过滤对象参数
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setBegin_time(today);
		param.setEnd_time(tommrow);
		param.setQ_type(1);
		param.setLimit(10);
		try {
			PurchaseTaskBean purcahseTask = purchaseTaskService.searchPurchaseTask(param);
			Assert.assertEquals(purcahseTask.getCode() == 0, true, "获取采购任务失败");

			SupplyLimitFilterParam supplyLimitFilterParam = new SupplyLimitFilterParam();
			supplyLimitFilterParam.setQ_type(1);
			supplyLimitFilterParam.setBegin_time(today);
			supplyLimitFilterParam.setEnd_time(tommrow);

			List<SupplierSpec> supplierSpecList = new ArrayList<>();
			SupplierSpec supplierSpec = null;
			List<PurchaseTaskData> purchaseTaskDataArray = purcahseTask.getPurchaseTaskDataArray();
			if (purchaseTaskDataArray.size() > 0) {
				for (PurchaseTaskData purchaseTaskData : purchaseTaskDataArray) {
					supplierSpec = supplyLimitFilterParam.new SupplierSpec();
					supplierSpec.setSupplier_id(purchaseTaskData.getSettle_supplier_id());
					supplierSpec.setSku_id(purchaseTaskData.getSpec_id());
					supplierSpecList.add(supplierSpec);
				}
				supplyLimitFilterParam.setSupplier_spec(supplierSpecList);
				List<SupplyLimitBean> supplyLimitList = purchaseTaskService.searchSupplyLimit(supplyLimitFilterParam);
				Assert.assertNotEquals(supplyLimitList, null, "获取采购任务建议采购数失败");
			}
		} catch (Exception e) {
			logger.error("获取采购任务建议采购数遇到错误: ", e);
			Assert.fail("获取采购任务建议采购数遇到错误: ", e);
		}
	}

	/**
	 * 按订单过滤,在发布采购任务 按订单发布采购任务
	 * 
	 */
	@Test
	public void purchaseTaskTestCase04() {
		try {
			ReporterCSS.title("测试点: 按订单发布采购任务");

			List<PurchaseTaskData> purchaseTaskDataArray = purchaseTaskTool.getOrderPurcahseTask(order_id);
			Assert.assertEquals(purchaseTaskDataArray != null && purchaseTaskDataArray.size() > 0, true,
					"订单" + order_id + "的采购任务在1分钟内没有生成");

			List<String> no_supplier_task_ids = new ArrayList<String>();

			JSONArray taskids = new JSONArray();
			for (PurchaseTaskData purchaseTask : purchaseTaskDataArray) {
				List<Task> taskArry = purchaseTask.getTasks();

				if (purchaseTask.getSupplier_status() != null && purchaseTask.getSupplier_status().equals("0")) {
					for (Task task : taskArry) {
						no_supplier_task_ids.add(task.getId());
					}
				}
				for (Task task : taskArry) {
					taskids.add(task.getId());
				}
			}

			// 如果有采购任务对应的供应商删除的任务,则给切换下供应商
			if (no_supplier_task_ids.size() > 0) {
				boolean result = purchaseTaskService.purchaseTaskChangeSupplier(no_supplier_task_ids,
						initData.getSupplier().getId());
				Assert.assertEquals(result, true, "采购任务切换供应商删除");
				Thread.sleep(2000);
			}

			ReleasePurchaseTaskParam releasePurchaseTaskParam = new ReleasePurchaseTaskParam();
			releasePurchaseTaskParam.setBegin_time(today);
			releasePurchaseTaskParam.setEnd_time(tommrow);

			releasePurchaseTaskParam.setTask_ids(taskids);
			releasePurchaseTaskParam.setQ_type(1);
			releasePurchaseTaskParam.setTask_suggests(new JSONArray());

			boolean result = purchaseTaskService.releasePurchaseTask(releasePurchaseTaskParam);
			Assert.assertEquals(result, true, "发布采购任务失败");
		} catch (Exception e) {
			logger.error("验证订单生成的采购任务遇到错误: ", e);
			Assert.fail("验证订单生成的采购任务遇到错误: ", e);
		}
	}

	/**
	 * 发布全部采购任务
	 * 
	 */
	@Test
	public void purchaseTaskTestCase05() {
		try {
			ReporterCSS.title("测试点: 发布全部采购任务");

			ReleasePurchaseTaskParam fiterParam = new ReleasePurchaseTaskParam();
			fiterParam.setBegin_time(today);
			fiterParam.setEnd_time(tommrow);
			fiterParam.setTask_ids(new JSONArray());
			fiterParam.setQ_type(1);
			fiterParam.setTask_suggests(new JSONArray());

			boolean result = purchaseTaskService.releasePurchaseTask(fiterParam);
			Assert.assertEquals(result, true, "发布采购任务失败");
		} catch (Exception e) {
			logger.error("验证订单生成的采购任务遇到错误: ", e);
			Assert.fail("验证订单生成的采购任务遇到错误: ", e);
		}
	}

	/**
	 * 按订单过滤发布采购单
	 * 
	 */
	@Test
	public void purchaseTaskTestCase06() {
		try {
			ReporterCSS.title("测试点: 按订单过滤发布采购单");

			// 采购任务过滤对象参数
			PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
			param.setBegin_time(today);
			param.setEnd_time(tommrow);
			param.setQ(order_id);
			param.setQ_type(1);
			param.setLimit(10);

			List<PurchaseTaskData> purchaseTaskDataArray = purchaseTaskTool.getOrderPurcahseTask(order_id);
			Assert.assertEquals(purchaseTaskDataArray != null && purchaseTaskDataArray.size() > 0, true,
					"订单" + order_id + "的采购任务在1分钟内没有生成");

			ReleasePurchaseTaskParam fiterParam = new ReleasePurchaseTaskParam();
			fiterParam.setBegin_time(today);
			fiterParam.setEnd_time(tommrow);
			fiterParam.setTask_ids(new JSONArray());
			fiterParam.setQ_type(1);
			fiterParam.setQ(order_id);
			fiterParam.setTask_suggests(new JSONArray());

			boolean result = purchaseTaskService.releasePurchaseTask(fiterParam);
			Assert.assertEquals(result, true, "发布采购任务失败");

			// 重新查询一次采购任务,获得 release_id
			purchaseTaskDataArray = purchaseTaskService.searchPurchaseTask(param).getPurchaseTaskDataArray();

			// 构造发布采购单据参数
			List<BigDecimal> release_ids = new ArrayList<>();

			for (PurchaseTaskData purchaseTaskData : purchaseTaskDataArray) {
				release_ids.add(purchaseTaskData.getRelease_id());
			}
			PurchaseSheetCreateParam purchaseSheetParam = new PurchaseSheetCreateParam();
			purchaseSheetParam.setRelease_ids(release_ids);
			purchaseSheetParam.setBegin_time(today);
			purchaseSheetParam.setEnd_time(tommrow);
			purchaseSheetParam.setQ_type(1);

			JSONArray purchaseSheetIdArray = purchaseTaskService.createPruchaseSheet(purchaseSheetParam);
			Assert.assertNotEquals(purchaseSheetIdArray, null, "生成采购单失败");

			Assert.assertEquals(purchaseSheetIdArray.size() > 0, true, "生成采购单失败,单据没有生成");
		} catch (Exception e) {
			logger.error("生成采购单据的过程中遇到错误: ", e);
			Assert.fail("生成采购单据的过程中遇到错误: ", e);
		}
	}

	/**
	 * 发布全部采购任务
	 * 
	 */
	@Test
	public void purchaseTaskTestCase07() {
		try {
			ReporterCSS.title("测试点: 发布全部采购任务");

			List<PurchaseTaskData> purchaseTaskDataArray = purchaseTaskTool.getOrderPurcahseTask(order_id);
			Assert.assertEquals(purchaseTaskDataArray != null && purchaseTaskDataArray.size() > 0, true,
					"订单" + order_id + "的采购任务在1分钟内没有生成");

			ReleasePurchaseTaskParam fiterParam = new ReleasePurchaseTaskParam();
			fiterParam.setBegin_time(today);
			fiterParam.setEnd_time(tommrow);
			fiterParam.setTask_ids(new JSONArray());
			fiterParam.setQ_type(1);
			fiterParam.setTask_suggests(new JSONArray());

			boolean result = purchaseTaskService.releasePurchaseTask(fiterParam);
			Assert.assertEquals(result, true, "发布采购任务失败");

			Reporter.log("生成采购单(生成全部)");
			PurchaseSheetCreateParam purchaseSheetParam = new PurchaseSheetCreateParam();
			purchaseSheetParam.setRelease_ids(new ArrayList<>());
			purchaseSheetParam.setBegin_time(today);
			purchaseSheetParam.setEnd_time(tommrow);
			purchaseSheetParam.setQ_type(1);

			JSONArray purchaseSheetIdArray = purchaseTaskService.createPruchaseSheet(purchaseSheetParam);
			Assert.assertNotEquals(purchaseSheetIdArray, null, "生成采购单(生成全部)失败");

			Assert.assertEquals(purchaseSheetIdArray.size() > 0, true, "生成采购单(生成全部)失败,单据没有生成");

		} catch (Exception e) {
			logger.error("验证订单生成的采购任务遇到错误: ", e);
			Assert.fail("验证订单生成的采购任务遇到错误: ", e);
		}
	}

	@Test
	public void purchaseTaskTestCase08() {
		ReporterCSS.title("测试点: 导出采购任务");
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setBegin_time(today);
		param.setEnd_time(tommrow);
		param.setQ_type(1);
		param.setType(1);
		try {
			boolean result = purchaseTaskService.exportPurchaseTask(param);
			Assert.assertEquals(result, true, "导出采购任务失败");
		} catch (Exception e) {
			logger.error("导出采购任务遇到错误: ", e);
			Assert.fail("导出采购任务遇到错误: ", e);
		}
	}

	@Test
	public void purchaseTaskTestCase09() {
		ReporterCSS.title("测试点: 异步导出采购任务(新版)");
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setBegin_time(today);
		param.setEnd_time(tommrow);
		param.setQ_type(1);
		param.setType(1);
		try {
			BigDecimal task_id = purchaseTaskService.exportPurchaseTaskV2(param);
			Assert.assertNotEquals(task_id, null, "创建异步导出采购任务失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "导出成功");
			Assert.assertEquals(result, true, "异步导出采购任务执行失败");

			AsyncTaskResultBean asyncTaskResult = asyncService.getAsyncTaskResult(task_id);
			Assert.assertNotEquals(asyncTaskResult, null, "获取指定异步任务详细信息失败");

			String download_url = asyncTaskResult.getResult().getLink();

			String file_path = downloadService.downloadFile(download_url);
			Assert.assertNotEquals(file_path, null, "下载采购任务Excel文件失败");
		} catch (Exception e) {
			logger.error("导出采购任务遇到错误: ", e);
			Assert.fail("导出采购任务遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "purchaseTaskTestCase06" })
	public void purchaseTaskTestCase10() {
		ReporterCSS.title("测试点: 查看采购任务关联的订单和采购单");
		try {
			PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
			param.setQ_type(1);
			param.setBegin_time(today);
			param.setEnd_time(tommrow);
			param.setLimit(20);
			param.setHas_created_sheet(1);
			PurchaseTaskBean purcahseTask = purchaseTaskService.searchPurchaseTask(param);
			Assert.assertEquals(purcahseTask.getCode() == 0 && purcahseTask.getPurchaseTaskDataArray() != null, true,
					"搜索过滤采购任务失败");

			List<PurchaseTaskBean.PurchaseTaskData> purchaseTaskDatas = purcahseTask.getPurchaseTaskDataArray();

			PurchaseTaskBean.PurchaseTaskData purchaseTaskData = NumberUtil.roundNumberInList(purchaseTaskDatas);

			String spec_id = purchaseTaskData.getSpec_id();
			BigDecimal release_id = purchaseTaskData.getRelease_id();

			PurchaseTaskHistoryFilterParam filterParam = new PurchaseTaskHistoryFilterParam();
			filterParam.setQ_type(1);
			filterParam.setBegin_time(today);
			filterParam.setEnd_time(tommrow);
			filterParam.setRelease_id(release_id);
			filterParam.setSku_id(spec_id);

			List<PurchaseTaskHistoryBean> purchaseTaskHistorys = purchaseTaskService
					.getPurchaseTaskHistorys(filterParam);
			Assert.assertNotEquals(purchaseTaskHistorys, null, "查看采购任务关联的订单和采购单失败");
		} catch (Exception e) {
			logger.error("查看采购任务关联的订单和采购单遇到错误: ", e);
			Assert.fail("查看采购任务关联的订单和采购单遇到错误: ", e);
		}
	}

	@Test(timeOut = 60000)
	public void purchaseTaskTestCase11() {
		ReporterCSS.title("测试点: 按商品分类过滤发布采购任务");
		try {
			PurchaseTaskFilterParam purchaseTaskFilterParam = new PurchaseTaskFilterParam();
			purchaseTaskFilterParam.setQ_type(1);
			purchaseTaskFilterParam.setStatus(1);
			purchaseTaskFilterParam.setBegin_time(today);
			purchaseTaskFilterParam.setEnd_time(tommrow);
			purchaseTaskFilterParam.setLimit(20);
			PurchaseTaskBean purcahseTask = null;

			int times = 10;
			while (times-- > 0) {
				purcahseTask = purchaseTaskService.searchPurchaseTask(purchaseTaskFilterParam);
				Assert.assertEquals(purcahseTask.getCode() == 0, true, "搜索过滤采购任务失败");
				if (purcahseTask.getPurchaseTaskDataArray() == null) {
					Thread.sleep(3000);
					continue;
				} else {
					break;
				}
			}

			Assert.assertNotEquals(purcahseTask.getPurchaseTaskDataArray(), null, "订单的采购任务在30S内没有生成");

			List<PurchaseTaskBean.PurchaseTaskData> purchaseTaskDatas = purcahseTask.getPurchaseTaskDataArray();
			PurchaseTaskBean.PurchaseTaskData purchaseTaskData = NumberUtil.roundNumberInList(purchaseTaskDatas);

			String category1_name = purchaseTaskData.getCategory1_name();
			String category2_name = purchaseTaskData.getCategory2_name();
			String pinlei_name = purchaseTaskData.getPinlei_name();

			List<MerchandiseTreeBean> merchandiseTree = categoryService.getMerchandiseTree();
			Assert.assertNotEquals(merchandiseTree, null, "获取站点分类树失败");

			// 找到对应的ID
			String category1_id = null, category2_id = null, pinlei_id = null;
			OK: for (MerchandiseTreeBean merchandise : merchandiseTree) {
				if (merchandise.getCategory1_name().equals(category1_name)) {
					category1_id = merchandise.getCategory1_id();
					for (MerchandiseTreeBean.Category2 category2 : merchandise.getCategory2s()) {
						if (category2.getCategory2_name().equals(category2_name)) {
							category2_id = category2.getCategory2_id();
							for (MerchandiseTreeBean.Category2.Pinlei pinlei : category2.pinleis) {
								if (pinlei.getPinlei_name().equals(pinlei_name)) {
									pinlei_id = pinlei.getPinlei_id();
									break OK;
								}
							}
						}
					}
				}
			}

			List<String> category1_ids = Arrays.asList(category1_id);
			List<String> category2_ids = Arrays.asList(category2_id);
			List<String> pinlei_ids = Arrays.asList(pinlei_id);

			purchaseTaskFilterParam.setCategory1_ids(category1_ids);
			purchaseTaskFilterParam.setCategory2_ids(category2_ids);
			purchaseTaskFilterParam.setPinlei_ids(pinlei_ids);
			purchaseTaskFilterParam.setLimit(50);
			purcahseTask = purchaseTaskService.searchPurchaseTask(purchaseTaskFilterParam);
			Assert.assertEquals(purcahseTask.getCode() == 0, true, "搜索过滤采购任务失败");

			List<String> task_ids = new ArrayList<String>();
			for (PurchaseTaskBean.PurchaseTaskData ptd : purcahseTask.getPurchaseTaskDataArray()) {
				if (ptd.getSupplier_status() != null && ptd.getSupplier_status().equals("0")) {
					for (PurchaseTaskBean.PurchaseTaskData.Task task : ptd.getTasks()) {
						task_ids.add(task.getId());
					}
				}
			}
			// 如果有采购任务对应的供应商删除的任务,则给切换下供应商
			if (task_ids.size() > 0) {
				boolean result = purchaseTaskService.purchaseTaskChangeSupplier(task_ids,
						initData.getSupplier().getId());
				Assert.assertEquals(result, true, "采购任务切换供应商删除");
				Thread.sleep(2000);
			}

			ReleasePurchaseTaskParam releasePurchaseTaskParam = new ReleasePurchaseTaskParam();
			releasePurchaseTaskParam.setBegin_time(today);
			releasePurchaseTaskParam.setEnd_time(tommrow);
			releasePurchaseTaskParam.setTask_ids(new JSONArray());
			releasePurchaseTaskParam.setQ_type(1);
			releasePurchaseTaskParam.setTask_suggests(new JSONArray());
			releasePurchaseTaskParam.setCategory1_ids(category1_ids);
			releasePurchaseTaskParam.setCategory2_ids(category2_ids);
			releasePurchaseTaskParam.setPinlei_ids(pinlei_ids);

			boolean result = purchaseTaskService.releasePurchaseTask(releasePurchaseTaskParam);
			Assert.assertEquals(result, true, "发布采购任务失败");

			PurchaseSheetCreateParam purchaseSheetParam = new PurchaseSheetCreateParam();
			purchaseSheetParam.setRelease_ids(new ArrayList<>());
			purchaseSheetParam.setBegin_time(today);
			purchaseSheetParam.setEnd_time(tommrow);
			purchaseSheetParam.setQ_type(1);
			purchaseSheetParam.setCategory1_ids(category1_ids);
			purchaseSheetParam.setCategory2_ids(category2_ids);
			purchaseSheetParam.setPinlei_ids(pinlei_ids);

			JSONArray purchaseSheetIdArray = purchaseTaskService.createPruchaseSheet(purchaseSheetParam);
			Assert.assertNotEquals(purchaseSheetIdArray, null, "按分类过滤发布采购任务失败");

			Assert.assertEquals(purchaseSheetIdArray.size() > 0, true, "按分类过滤发布采购任务,单据没有生成");
		} catch (Exception e) {
			logger.error("发布采购任务的过程中遇到错误: ", e);
			Assert.fail("发布采购任务的过程中遇到错误: ", e);
		}
	}

	@Test
	public void purchaseTaskTestCase12() {
		ReporterCSS.title("测试点: 采购任务总览查看");
		// 采购任务过滤对象参数
		PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
		param.setBegin_time(today);
		param.setEnd_time(tommrow);
		param.setQ_type(1);
		param.setLimit(10);
		try {
			PurcahseTaskSummaryBean purcahseTaskSummary = purchaseTaskService.getPurcahseTaskSummary(param);
			Assert.assertNotEquals(purcahseTaskSummary, null, "采购任务总览查看失败");

			List<PurcahseTaskSummaryBean.SupplierSummary> supplierSummaryList = purcahseTaskSummary
					.getSupplierSummaries();

			String settle_supplier_id = NumberUtil.roundNumberInList(supplierSummaryList).getSettle_supplier_id();

			PurchaseTaskShareCreateParam purchaseTaskShareParam = new PurchaseTaskShareCreateParam();
			purchaseTaskShareParam.setQ_type(1);
			purchaseTaskShareParam.setSettle_supplier_id(settle_supplier_id);
			purchaseTaskShareParam.setBegin_time(today);
			purchaseTaskShareParam.setEnd_time(tommrow);

			String token = purchaseTaskService.createSharePurchaseTask(purchaseTaskShareParam);
			Assert.assertNotEquals(token, null, "创建分享采购任务二维码失败");

			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账号信息失败");

			PurchaseTaskShareGetParam purchaseTaskShareGetParam = new PurchaseTaskShareGetParam();
			purchaseTaskShareGetParam.setSettle_supplier_id(settle_supplier_id);
			purchaseTaskShareGetParam.set__trace_group_id("false");
			purchaseTaskShareGetParam.setQ_type(1);
			purchaseTaskShareGetParam.setBegin_time(today);
			purchaseTaskShareGetParam.setEnd_time(tommrow);
			purchaseTaskShareGetParam.setStation_id(loginUserInfo.getStation_id());
			purchaseTaskShareGetParam.setToken(token);

			List<PurchaseTaskShareBean> purchaseTaskShareList = purchaseTaskService
					.getSharePurchaseTask(purchaseTaskShareGetParam);
			Assert.assertNotEquals(purchaseTaskShareList, null, "获取分享的采购任务信息失败");

		} catch (Exception e) {
			logger.error("分享采购任务过长中遇到错误: ", e);
			Assert.fail("分享采购任务过长中遇到错误: ", e);
		}
	}

}
