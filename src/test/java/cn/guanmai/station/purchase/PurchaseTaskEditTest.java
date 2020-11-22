package cn.guanmai.station.purchase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.purchase.PurchaseTaskBean.PurchaseTaskData;
import cn.guanmai.station.bean.purchase.PurchaseTaskCanChangeSupplierBean;
import cn.guanmai.station.bean.purchase.SupplyLimitBean;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskFilterParam;
import cn.guanmai.station.bean.purchase.param.SupplyLimitFilterParam;
import cn.guanmai.station.impl.purchase.PurchaseTaskServiceImpl;
import cn.guanmai.station.interfaces.purchase.PurchaseTaskService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.station.tools.PurchaseTaskTool;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/**
 * @author: liming
 * @Date: 2020年2月14日 下午3:24:10
 * @description: 采购任务修改相关操作
 * @version: 1.0
 */

public class PurchaseTaskEditTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PurchaseTaskEditTest.class);
	private PurchaseTaskService purchaseTaskService;
	private String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
	private PurchaseTaskFilterParam purchaseTaskFilterParam;
	private OrderTool orderTool;
	private PurchaseTaskTool purchaseTaskTool;
	private int limit = 50;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		purchaseTaskService = new PurchaseTaskServiceImpl(headers);
		orderTool = new OrderTool(headers);
		try {
			purchaseTaskFilterParam = new PurchaseTaskFilterParam();
			purchaseTaskFilterParam.setQ_type(1);
			purchaseTaskFilterParam.setStatus(1);
			purchaseTaskFilterParam.setBegin_time(begin_time);
			purchaseTaskFilterParam.setEnd_time(begin_time);
			purchaseTaskFilterParam.setLimit(20);
			purchaseTaskFilterParam.setOffset(0);

			List<PurchaseTaskData> purchaseTaskDataList = purchaseTaskService
					.newSearchPurchaseTask(purchaseTaskFilterParam);
			Assert.assertNotEquals(purchaseTaskDataList, null, "采购任务过滤搜索失败");

			if (purchaseTaskDataList.size() == 0) {
				// 创建订单
				String order_id = orderTool.oneStepCreateOrder(4);
				Assert.assertNotEquals(order_id, null, "创建订单失败");

				List<PurchaseTaskData> purchaseTaskDataArray = purchaseTaskTool.getOrderPurcahseTask(order_id);
				Assert.assertEquals(purchaseTaskDataArray != null && purchaseTaskDataArray.size() > 0, true,
						"订单" + order_id + "的采购任务在45秒内没有生成");
			}
		} catch (Exception e) {
			logger.error("初始化数据失败: ", e);
			Assert.fail("初始化数据失败: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		purchaseTaskFilterParam = new PurchaseTaskFilterParam();
		purchaseTaskFilterParam.setQ_type(1);
		purchaseTaskFilterParam.setStatus(1);
		purchaseTaskFilterParam.setBegin_time(begin_time);
		purchaseTaskFilterParam.setEnd_time(begin_time);
		purchaseTaskFilterParam.setLimit(50);
		purchaseTaskFilterParam.setOffset(0);
	}

	@Test
	public void purchaseTaskEditTestCase01() {
		ReporterCSS.title("测试点: 采购任务切换供应商");
		try {
			List<PurchaseTaskData> purchaseTaskDataList = purchaseTaskService
					.newSearchPurchaseTask(purchaseTaskFilterParam);
			Assert.assertNotEquals(purchaseTaskDataList, null, "采购任务过滤搜索失败");

			PurchaseTaskData purchaseTaskData = NumberUtil.roundNumberInList(purchaseTaskDataList);

			String old_settle_supplier_id = purchaseTaskData.getSettle_supplier_id();

			List<PurchaseTaskCanChangeSupplierBean> purchaseTaskCanChangeSuppliers = purchaseTaskService
					.searchPurchaseTaskCanChangeSuppliers(1, begin_time, begin_time, purchaseTaskData.getSpec_id());
			Assert.assertNotEquals(purchaseTaskCanChangeSuppliers, null, "获取可更改供应商列表失败");

			PurchaseTaskCanChangeSupplierBean purchaseTaskCanChangeSupplier = purchaseTaskCanChangeSuppliers.stream()
					.filter(p -> !p.getSettle_supplier_id().equals(old_settle_supplier_id)).findFirst().orElse(null);
			Assert.assertNotEquals(purchaseTaskCanChangeSupplier, null, "没有可用更换的供应商");

			String new_settle_supplier_id = purchaseTaskCanChangeSupplier.getSettle_supplier_id();

			List<String> task_ids = new ArrayList<String>();
			for (PurchaseTaskData.Task task : purchaseTaskData.getTasks()) {
				task_ids.add(task.getId());
			}

			boolean result = purchaseTaskService.purchaseTaskChangeSupplier(task_ids, new_settle_supplier_id);
			Assert.assertEquals(result, true, "采购任务修改供应商失败");

			int offset = 0;
			purchaseTaskFilterParam.setSettle_supplier_id(new_settle_supplier_id);
			purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDataList = null;
			while (true) {
				purchaseTaskFilterParam.setOffset(offset);
				tempPurchaseTaskDataList = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParam);
				Assert.assertNotEquals(tempPurchaseTaskDataList, null, "采购任务过滤搜索失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDataList);
				if (tempPurchaseTaskDataList.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> actual_task_ids = new ArrayList<String>();

			for (PurchaseTaskData pt : purchaseTaskDataList) {
				for (PurchaseTaskData.Task task : pt.getTasks()) {
					actual_task_ids.add(task.getId());
				}
			}

			if (!actual_task_ids.containsAll(task_ids)) {
				task_ids.removeAll(actual_task_ids);
				Assert.assertEquals(task_ids.size(), 0, "task_id:" + task_ids + "对应的采购任务没有修改成功供应商");
			}
		} catch (Exception e) {
			logger.error("采购任务切换供应商过程中遇到错误: ", e);
			Assert.fail("采购任务切换供应商过程中遇到错误: ", e);
		}
	}

	@Test
	public void purchaseTaskEditTestCase02() {
		ReporterCSS.title("测试点: 采购任务切换采购员");
		try {
			List<PurchaseTaskData> purchaseTaskDataList = purchaseTaskService
					.newSearchPurchaseTask(purchaseTaskFilterParam);
			Assert.assertNotEquals(purchaseTaskDataList, null, "采购任务过滤搜索失败");

			// 找一个采购任务,用来切换采购员
			PurchaseTaskData purchaseTaskData = purchaseTaskDataList.stream()
					.filter(t -> t.getSupplier_status() != null && t.getSupplier_status().equals("1")).findFirst()
					.orElse(null);
			Assert.assertNotEquals(purchaseTaskData, null, "没有符合可用于切换采购员的采购任务");

			List<PurchaseTaskCanChangeSupplierBean> purchaseTaskCanChangeSuppliers = purchaseTaskService
					.searchPurchaseTaskCanChangeSuppliers(1, begin_time, begin_time, purchaseTaskData.getSpec_id());
			Assert.assertNotEquals(purchaseTaskCanChangeSuppliers, null, "获取可更改供应商列表失败");

			PurchaseTaskCanChangeSupplierBean purchaseTaskCanChangeSupplier = purchaseTaskCanChangeSuppliers.stream()
					.filter(p -> p.getSettle_supplier_id().equals(purchaseTaskData.getSettle_supplier_id())).findFirst()
					.orElse(null);

			Assert.assertNotEquals(purchaseTaskCanChangeSupplier, null, "没有可供切换的采购员");

			String old_purchaser_id = purchaseTaskData.getPurchaser_id();

			Assert.assertEquals(purchaseTaskCanChangeSupplier.getPurchasers().size() > 0, true, "没有可供切换的采购员");
			PurchaseTaskCanChangeSupplierBean.Purchaser purchaser = null;
			if (old_purchaser_id == null) {
				purchaser = purchaseTaskCanChangeSupplier.getPurchasers().get(0);
			} else {
				purchaser = purchaseTaskCanChangeSupplier.getPurchasers().stream()
						.filter(p -> !p.getPurchaser_id().equals(old_purchaser_id)).findAny().orElse(null);
			}

			Assert.assertNotEquals(purchaser, null, "没有可供更换的采购员");

			String new_purchaser_id = purchaser.getPurchaser_id();

			List<String> task_ids = new ArrayList<String>();
			for (PurchaseTaskData.Task task : purchaseTaskData.getTasks()) {
				task_ids.add(task.getId());
			}

			boolean result = purchaseTaskService.purchaseTaskChangePurchaser(task_ids, new_purchaser_id);
			Assert.assertEquals(result, true, "采购任务修改供应商失败");

			int offset = 0;
			purchaseTaskFilterParam.setPurchaser_id(new_purchaser_id);

			purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
			List<PurchaseTaskData> tempPurchaseTaskDataList = null;
			while (true) {
				purchaseTaskFilterParam.setOffset(offset);
				tempPurchaseTaskDataList = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParam);
				Assert.assertNotEquals(tempPurchaseTaskDataList, null, "采购任务过滤搜索失败");
				purchaseTaskDataList.addAll(tempPurchaseTaskDataList);
				if (tempPurchaseTaskDataList.size() < limit) {
					break;
				}
				offset += limit;
			}

			List<String> actual_task_ids = new ArrayList<String>();

			for (PurchaseTaskData pt : purchaseTaskDataList) {
				for (PurchaseTaskData.Task task : pt.getTasks()) {
					actual_task_ids.add(task.getId());
				}
			}

			if (!actual_task_ids.containsAll(task_ids)) {
				task_ids.removeAll(actual_task_ids);
				Assert.assertEquals(task_ids.size(), 0, "task_id:" + task_ids + "对应的采购任务没有修改成功采购员");
			}
		} catch (Exception e) {
			logger.error("采购任务切换采购员过程中遇到错误: ", e);
			Assert.fail("采购任务切换采购员过程中遇到错误: ", e);
		}
	}

	@Test
	public void purchaseTaskEditTestCase03() {
		ReporterCSS.title("测试点: 采购任务设置单次可供上线");
		try {
			List<PurchaseTaskData> purchaseTaskDataList = purchaseTaskService
					.newSearchPurchaseTask(purchaseTaskFilterParam);
			Assert.assertNotEquals(purchaseTaskDataList, null, "采购任务过滤搜索失败");

			PurchaseTaskData purchaseTaskData = NumberUtil.roundNumberInList(purchaseTaskDataList);

			List<String> task_ids = new ArrayList<String>();
			for (PurchaseTaskData.Task task : purchaseTaskData.getTasks()) {
				task_ids.add(task.getId());
			}

			String settle_supplier_id = purchaseTaskData.getSettle_supplier_id();
			String spec_id = purchaseTaskData.getSpec_id();

			BigDecimal supply_limit = NumberUtil.getRandomNumber(50, 100, 2);

			boolean result = purchaseTaskService.purchaseTaskChangeSupplyLimit(settle_supplier_id, spec_id,
					supply_limit);
			Assert.assertEquals(result, true, "设置采购任务对应的供应商单次最大可供应数失败");

			SupplyLimitFilterParam supplyLimitFilterParam = new SupplyLimitFilterParam();
			supplyLimitFilterParam.setQ_type(1);
			supplyLimitFilterParam.setBegin_time(begin_time);
			supplyLimitFilterParam.setEnd_time(begin_time);

			SupplyLimitFilterParam.SupplierSpec supplierSpec = supplyLimitFilterParam.new SupplierSpec();
			supplierSpec.setSku_id(spec_id);
			supplierSpec.setSupplier_id(settle_supplier_id);

			List<SupplyLimitFilterParam.SupplierSpec> supplierSpecList = Arrays.asList(supplierSpec);
			supplyLimitFilterParam.setSupplier_spec(supplierSpecList);

			List<SupplyLimitBean> supplyLimitList = purchaseTaskService.searchSupplyLimit(supplyLimitFilterParam);
			Assert.assertNotEquals(supplyLimitList, null, "获取供应商最大可供应商数失败");

			Assert.assertEquals(supplyLimitList.size() > 0, true, "获取供应商最大可供应商数列表为空,与预期不符");

			SupplyLimitBean supplyLimit = supplyLimitList.get(0);

			String msg = null;
			if (supplyLimit.getSupply_limit().compareTo(supply_limit) != 0) {
				msg = String.format("供应商%s对应的商品%s单次可供最大数与预期不符,预期:%s,实际:%s", settle_supplier_id, spec_id, supply_limit,
						supplyLimit.getSupply_limit());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "供应商单次可供最大数结果与预期不符");
		} catch (Exception e) {
			logger.error("采购任务切换采购员过程中遇到错误: ", e);
			Assert.fail("采购任务切换采购员过程中遇到错误: ", e);
		}
	}

}
