package cn.guanmai.open.purchase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.product.OpenPurchaseSpecBean;
import cn.guanmai.open.bean.product.param.OpenPurchaseSpecFilterParam;
import cn.guanmai.open.bean.purchase.OpenPurcahserBean;
import cn.guanmai.open.bean.purchase.OpenPurchaseTaskBean;
import cn.guanmai.open.bean.purchase.param.OpenPurchaseTaskCreateParam;
import cn.guanmai.open.bean.purchase.param.OpenPurchaseTaskFilterParam;
import cn.guanmai.open.bean.purchase.param.OpenPurchaseTaskUpdateParam;
import cn.guanmai.open.bean.purchase.param.OpenPurchaserFilterParam;
import cn.guanmai.open.bean.stock.OpenSupplierBean;
import cn.guanmai.open.bean.stock.param.OpenSupplierFilterParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.impl.purchase.OpenPurcahseServiceImpl;
import cn.guanmai.open.impl.stock.OpenSupplierServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.interfaces.purchase.OpenPurcahseService;
import cn.guanmai.open.interfaces.stock.OpenSupplierService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean.PurchaseTaskData;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskFilterParam;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaseTaskServiceImpl;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.purchase.PurchaseTaskService;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/**
 * @author liming
 * @date 2019年11月13日
 * @time 下午5:42:46
 * @des TODO
 */

public class OpenPurchaseTaskTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenPurchaseTaskTest.class);
	private OpenPurcahseService openPurcahseService;
	private OpenSupplierService openSupplierService;
	private OpenCategoryService openCategoryService;

	private OpenPurchaseTaskCreateParam openPurchaseTaskCreateParam;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private OpenPurchaseSpecBean openPurchaseSpec;

	private OrderTool orderTool;
	private OrderService orderService;
	private PurchaseTaskService purchaseTaskService;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openPurcahseService = new OpenPurcahseServiceImpl(access_token);
		openSupplierService = new OpenSupplierServiceImpl(access_token);
		openCategoryService = new OpenCategoryServiceImpl(access_token);

		Map<String, String> st_headers = getSt_headers();

		orderTool = new OrderTool(st_headers);
		purchaseTaskService = new PurchaseTaskServiceImpl(st_headers);
		orderService = new OrderServiceImpl(st_headers);
	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			List<OpenSupplierBean> openSupplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(openSupplierList, null, "搜索过滤供应商失败");
			String supplier_id = null;
			String purchaser_id = null;
			OpenPurchaserFilterParam openPurchaserFilterParam = new OpenPurchaserFilterParam();
			List<OpenPurcahserBean> openPurcahserList = null;
			for (OpenSupplierBean openSupplier : openSupplierList) {
				supplier_id = openSupplier.getSupplier_id();
				openPurchaserFilterParam.setSupplier_id(supplier_id);
				openPurcahserList = openPurcahseService.queryPurchaser(openPurchaserFilterParam);
				Assert.assertNotEquals(openPurcahserList, null, "搜索过滤采购员失败");
				if (openPurcahserList.size() > 0) {
					purchaser_id = NumberUtil.roundNumberInList(openPurcahserList).getPurchaser_id().toString();
					break;
				}
			}

			Assert.assertNotEquals(purchaser_id, null, "供应商都没有对应的采购员");

			List<OpenPurchaseSpecBean> openPurchaseSpecList = openCategoryService
					.queryPurchaseSpec(new OpenPurchaseSpecFilterParam());
			Assert.assertNotEquals(openPurchaseSpecList, null, "搜索过滤采购规格失败");
			Assert.assertEquals(openPurchaseSpecList.size() > 0, true, "采购规格列表为空,无法进行后续操作");

			openPurchaseSpec = NumberUtil.roundNumberInList(openPurchaseSpecList);

			String spec_id = openPurchaseSpec.getSpec_id();

			openPurchaseTaskCreateParam = new OpenPurchaseTaskCreateParam();
			openPurchaseTaskCreateParam.setSpec_id(spec_id);
			openPurchaseTaskCreateParam.setSupplier_id(supplier_id);
			openPurchaseTaskCreateParam.setPurchaser_id(purchaser_id);
			openPurchaseTaskCreateParam.setPurchase_count(NumberUtil.getRandomNumber(5, 10, 2).toString());
		} catch (Exception e) {
			logger.error("初始化新建采购任务参数遇到错误: ", e);
			Assert.fail("初始化新建采购任务参数遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void openPurchaseTaskTestCase01() {
		ReporterCSS.title("测试点: 新建采购任务");
		try {
			boolean result = openPurcahseService.createPurchaseTask(openPurchaseTaskCreateParam);
			Assert.assertEquals(result, true, "新建采购任务失败");

			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setPinlei_id(openPurchaseSpec.getPinlei_id());

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertNotEquals(openPurchaseTasks, null, "搜索欧过滤采购任务失败");

			String spec_id = null;
			boolean find = false;
			OK: for (OpenPurchaseTaskBean openPurchaseTask : openPurchaseTasks) {
				spec_id = openPurchaseTask.getSpec_id();
				if (spec_id.equals(openPurchaseSpec.getSpec_id())) {
					List<OpenPurchaseTaskBean.PurchaseTask> purchase_tasks = openPurchaseTask.getPurchase_tasks();
					for (OpenPurchaseTaskBean.PurchaseTask purchase_task : purchase_tasks) {
						if (purchase_task.getPlan_purchase_count()
								.compareTo(new BigDecimal(openPurchaseTaskCreateParam.getPurchase_count())) == 0
								&& purchase_task.getSupplier_id()
										.equals(openPurchaseTaskCreateParam.getSupplier_id())) {
							find = true;
							break OK;
						}
					}
				}
			}
			Assert.assertEquals(find, true, "新建的采购任务没有在采购任务列表找到");
		} catch (Exception e) {
			logger.error("新建采购任务遇到错误: ", e);
			Assert.fail("新建采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void openPurchaseTaskTestCase02() {
		ReporterCSS.title("测试点: 修改采购任务");
		try {
			boolean result = openPurcahseService.createPurchaseTask(openPurchaseTaskCreateParam);
			Assert.assertEquals(result, true, "新建采购任务失败");

			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setPinlei_id(openPurchaseSpec.getPinlei_id());

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertNotEquals(openPurchaseTasks, null, "搜索欧过滤采购任务失败");

			String spec_id = null;
			boolean find = false;
			String task_id = null;
			OK: for (OpenPurchaseTaskBean openPurchaseTask : openPurchaseTasks) {
				spec_id = openPurchaseTask.getSpec_id();
				if (spec_id.equals(openPurchaseSpec.getSpec_id())) {
					List<OpenPurchaseTaskBean.PurchaseTask> purchase_tasks = openPurchaseTask.getPurchase_tasks();
					for (OpenPurchaseTaskBean.PurchaseTask purchase_task : purchase_tasks) {
						if (purchase_task.getPlan_purchase_count()
								.compareTo(new BigDecimal(openPurchaseTaskCreateParam.getPurchase_count())) == 0
								&& purchase_task.getSupplier_id()
										.equals(openPurchaseTaskCreateParam.getSupplier_id())) {
							task_id = purchase_task.getTask_id();
							find = true;
							break OK;
						}
					}
				}
			}
			Assert.assertEquals(find, true, "新建的采购任务没有在采购任务列表找到");

			List<OpenSupplierBean> openSupplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(openSupplierList, null, "搜索过滤供应商失败");
			String supplier_id = null;
			String purchaser_id = null;
			OpenPurchaserFilterParam openPurchaserFilterParam = new OpenPurchaserFilterParam();
			List<OpenPurcahserBean> openPurcahserList = null;
			for (OpenSupplierBean openSupplier : openSupplierList) {
				supplier_id = openSupplier.getSupplier_id();
				if (supplier_id.equals(openPurchaseTaskCreateParam.getSupplier_id())) {
					continue;
				}
				openPurchaserFilterParam.setSupplier_id(supplier_id);
				openPurcahserList = openPurcahseService.queryPurchaser(openPurchaserFilterParam);
				Assert.assertNotEquals(openPurcahserList, null, "搜索过滤采购员失败");
				if (openPurcahserList.size() > 0) {
					purchaser_id = NumberUtil.roundNumberInList(openPurcahserList).getPurchaser_id().toString();
					break;
				}
			}

			Assert.assertNotEquals(purchaser_id, null, "没有找到其他可以使用的采购员");

			OpenPurchaseTaskUpdateParam openPurchaseTaskUpdateParam = new OpenPurchaseTaskUpdateParam();
			openPurchaseTaskUpdateParam.setTask_id(task_id);
			// openPurchaseTaskUpdateParam.setPurchaser_id(purchaser_id);
			openPurchaseTaskUpdateParam.setSupplier_id(supplier_id);

			result = openPurcahseService.updatePurcahseTask(openPurchaseTaskUpdateParam);
			Assert.assertEquals(result, true, "修改采购任务失败");

			// 再次搜索验证
			openPurchaseTasks = openPurcahseService.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertNotEquals(openPurchaseTasks, null, "搜索欧过滤采购任务失败");

			result = false;
			find = false;
			OK: for (OpenPurchaseTaskBean openPurchaseTask : openPurchaseTasks) {
				spec_id = openPurchaseTask.getSpec_id();
				if (spec_id.equals(openPurchaseSpec.getSpec_id())) {
					List<OpenPurchaseTaskBean.PurchaseTask> purchase_tasks = openPurchaseTask.getPurchase_tasks();
					for (OpenPurchaseTaskBean.PurchaseTask purchase_task : purchase_tasks) {
						if (purchase_task.getTask_id().equals(task_id)) {
							find = true;
							result = purchase_task.getSupplier_id().equals(supplier_id);
							break OK;
						}
					}
				}
			}
			Assert.assertEquals(find, true, "采购任务修改,修改后的采购任务没有找到");
			Assert.assertEquals(result, true, "采购任务修改,采购员和供应商没有替换成功");
		} catch (Exception e) {
			logger.error("修改采购任务遇到错误: ", e);
			Assert.fail("修改采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void openPurchaseTaskTestCase03() {
		ReporterCSS.title("测试点: 修改采购任务");
		try {
			boolean result = openPurcahseService.createPurchaseTask(openPurchaseTaskCreateParam);
			Assert.assertEquals(result, true, "新建采购任务失败");

			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setPinlei_id(openPurchaseSpec.getPinlei_id());

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertNotEquals(openPurchaseTasks, null, "搜索欧过滤采购任务失败");

			String spec_id = null;
			boolean find = false;
			String task_id = null;
			OK: for (OpenPurchaseTaskBean openPurchaseTask : openPurchaseTasks) {
				spec_id = openPurchaseTask.getSpec_id();
				if (spec_id.equals(openPurchaseSpec.getSpec_id())) {
					List<OpenPurchaseTaskBean.PurchaseTask> purchase_tasks = openPurchaseTask.getPurchase_tasks();
					for (OpenPurchaseTaskBean.PurchaseTask purchase_task : purchase_tasks) {
						if (purchase_task.getPlan_purchase_count()
								.compareTo(new BigDecimal(openPurchaseTaskCreateParam.getPurchase_count())) == 0
								&& purchase_task.getSupplier_id()
										.equals(openPurchaseTaskCreateParam.getSupplier_id())) {
							task_id = purchase_task.getTask_id();
							find = true;
							break OK;
						}
					}
				}
			}
			Assert.assertEquals(find, true, "新建的采购任务没有在采购任务列表找到");

			List<OpenSupplierBean> openSupplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(openSupplierList, null, "搜索过滤供应商失败");
			String supplier_id = null;
			String purchaser_id = null;
			OpenPurchaserFilterParam openPurchaserFilterParam = new OpenPurchaserFilterParam();
			List<OpenPurcahserBean> openPurcahserList = null;
			for (OpenSupplierBean openSupplier : openSupplierList) {
				supplier_id = openSupplier.getSupplier_id();
				if (supplier_id.equals(openPurchaseTaskCreateParam.getSupplier_id())) {
					continue;
				}
				openPurchaserFilterParam.setSupplier_id(supplier_id);
				openPurcahserList = openPurcahseService.queryPurchaser(openPurchaserFilterParam);
				Assert.assertNotEquals(openPurcahserList, null, "搜索过滤采购员失败");
				if (openPurcahserList.size() > 0) {
					purchaser_id = NumberUtil.roundNumberInList(openPurcahserList).getPurchaser_id().toString();
					break;
				}
			}

			Assert.assertNotEquals(purchaser_id, null, "没有找到其他可以使用的采购员");

			OpenPurchaseTaskUpdateParam openPurchaseTaskUpdateParam = new OpenPurchaseTaskUpdateParam();
			openPurchaseTaskUpdateParam.setTask_id(task_id);
			openPurchaseTaskUpdateParam.setPurchaser_id(purchaser_id);
			// openPurchaseTaskUpdateParam.setSupplier_id(supplier_id);

			result = openPurcahseService.updatePurcahseTask(openPurchaseTaskUpdateParam);
			Assert.assertEquals(result, true, "修改采购任务失败");

			// 再次搜索验证
			openPurchaseTasks = openPurcahseService.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertNotEquals(openPurchaseTasks, null, "搜索欧过滤采购任务失败");

			result = false;
			find = false;
			OK: for (OpenPurchaseTaskBean openPurchaseTask : openPurchaseTasks) {
				spec_id = openPurchaseTask.getSpec_id();
				if (spec_id.equals(openPurchaseSpec.getSpec_id())) {
					List<OpenPurchaseTaskBean.PurchaseTask> purchase_tasks = openPurchaseTask.getPurchase_tasks();
					for (OpenPurchaseTaskBean.PurchaseTask purchase_task : purchase_tasks) {
						if (purchase_task.getTask_id().equals(task_id)) {
							find = true;
							result = openPurchaseTask.getPurchaser_id().equals(purchaser_id);
							break OK;
						}
					}
				}
			}
			Assert.assertEquals(find, true, "采购任务修改,修改后的采购任务没有找到");
			Assert.assertEquals(result, true, "采购任务修改,采购员和供应商没有替换成功");
		} catch (Exception e) {
			logger.error("修改采购任务遇到错误: ", e);
			Assert.fail("修改采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void openPurchaseTaskTestCase04() {
		ReporterCSS.title("测试点: 发布采购任务");
		try {
			boolean result = openPurcahseService.createPurchaseTask(openPurchaseTaskCreateParam);
			Assert.assertEquals(result, true, "新建采购任务失败");

			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setPinlei_id(openPurchaseSpec.getPinlei_id());

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertNotEquals(openPurchaseTasks, null, "搜索欧过滤采购任务失败");

			String spec_id = null;
			boolean find = false;
			String task_id = null;
			OK: for (OpenPurchaseTaskBean openPurchaseTask : openPurchaseTasks) {
				spec_id = openPurchaseTask.getSpec_id();
				if (spec_id.equals(openPurchaseSpec.getSpec_id())) {
					List<OpenPurchaseTaskBean.PurchaseTask> purchase_tasks = openPurchaseTask.getPurchase_tasks();
					for (OpenPurchaseTaskBean.PurchaseTask purchase_task : purchase_tasks) {
						if (purchase_task.getPlan_purchase_count()
								.compareTo(new BigDecimal(openPurchaseTaskCreateParam.getPurchase_count())) == 0
								&& purchase_task.getSupplier_id()
										.equals(openPurchaseTaskCreateParam.getSupplier_id())) {
							task_id = purchase_task.getTask_id();
							find = true;
							break OK;
						}
					}
				}
			}
			Assert.assertEquals(find, true, "新建的采购任务没有在采购任务列表找到");

			result = openPurcahseService.publishPurchaseTask(task_id);
			Assert.assertEquals(result, true, "发布采购任务失败");

			// 再次查找验证
			openPurchaseTasks = openPurcahseService.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertNotEquals(openPurchaseTasks, null, "搜索欧过滤采购任务失败");

			result = false;
			find = false;
			OK: for (OpenPurchaseTaskBean openPurchaseTask : openPurchaseTasks) {
				spec_id = openPurchaseTask.getSpec_id();
				if (spec_id.equals(openPurchaseSpec.getSpec_id())) {
					List<OpenPurchaseTaskBean.PurchaseTask> purchase_tasks = openPurchaseTask.getPurchase_tasks();
					for (OpenPurchaseTaskBean.PurchaseTask purchase_task : purchase_tasks) {
						if (purchase_task.getTask_id().equals(task_id)) {
							find = true;
							result = openPurchaseTask.getStatus() == 2;
							break OK;
						}
					}
				}
			}
			Assert.assertEquals(find, true, "发布的采购任务没有找到");

			Assert.assertEquals(result, true, "发布的采购任务状态值不正确");
		} catch (Exception e) {
			logger.error("发布采购任务遇到错误: ", e);
			Assert.fail("发布采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void openPurchaseTaskTestCase05() {
		ReporterCSS.title("测试点: 按一级分类搜索目标采购任务");
		try {
			boolean result = openPurcahseService.createPurchaseTask(openPurchaseTaskCreateParam);
			Assert.assertEquals(result, true, "新建采购任务失败");

			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setPinlei_id(openPurchaseSpec.getPinlei_id());

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertNotEquals(openPurchaseTasks, null, "搜索欧过滤采购任务失败");

			String task_id = null;
			String spec_id = null;
			boolean find = false;
			OK: for (OpenPurchaseTaskBean openPurchaseTask : openPurchaseTasks) {
				spec_id = openPurchaseTask.getSpec_id();
				if (spec_id.equals(openPurchaseSpec.getSpec_id())) {
					List<OpenPurchaseTaskBean.PurchaseTask> purchase_tasks = openPurchaseTask.getPurchase_tasks();
					for (OpenPurchaseTaskBean.PurchaseTask purchase_task : purchase_tasks) {
						if (purchase_task.getPlan_purchase_count()
								.compareTo(new BigDecimal(openPurchaseTaskCreateParam.getPurchase_count())) == 0
								&& purchase_task.getSupplier_id()
										.equals(openPurchaseTaskCreateParam.getSupplier_id())) {
							task_id = purchase_task.getTask_id();
							find = true;
							break OK;
						}
					}
				}
			}
			Assert.assertEquals(find, true, "新建的采购任务没有在采购任务列表找到");

			openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setCategory1_id(openPurchaseSpec.getCategory1_id());

			result = checkPurchaseTask(openPurchaseTaskFilterParam, task_id);
			Assert.assertEquals(result, true, "目标采购任务没有找到");
		} catch (Exception e) {
			logger.error("搜索采购任务遇到错误: ", e);
			Assert.fail("搜索采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void openPurchaseTaskTestCase06() {
		ReporterCSS.title("测试点: 按二级分类搜索目标采购任务");
		try {
			boolean result = openPurcahseService.createPurchaseTask(openPurchaseTaskCreateParam);
			Assert.assertEquals(result, true, "新建采购任务失败");

			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setPinlei_id(openPurchaseSpec.getPinlei_id());

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertNotEquals(openPurchaseTasks, null, "搜索欧过滤采购任务失败");

			String task_id = null;
			String spec_id = null;
			boolean find = false;
			OK: for (OpenPurchaseTaskBean openPurchaseTask : openPurchaseTasks) {
				spec_id = openPurchaseTask.getSpec_id();
				if (spec_id.equals(openPurchaseSpec.getSpec_id())) {
					List<OpenPurchaseTaskBean.PurchaseTask> purchase_tasks = openPurchaseTask.getPurchase_tasks();
					for (OpenPurchaseTaskBean.PurchaseTask purchase_task : purchase_tasks) {
						if (purchase_task.getPlan_purchase_count()
								.compareTo(new BigDecimal(openPurchaseTaskCreateParam.getPurchase_count())) == 0
								&& purchase_task.getSupplier_id()
										.equals(openPurchaseTaskCreateParam.getSupplier_id())) {
							task_id = purchase_task.getTask_id();
							find = true;
							break OK;
						}
					}
				}
			}
			Assert.assertEquals(find, true, "新建的采购任务没有在采购任务列表找到");

			openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setCategory2_id(openPurchaseSpec.getCategory2_id());

			result = checkPurchaseTask(openPurchaseTaskFilterParam, task_id);
			Assert.assertEquals(result, true, "目标采购任务没有找到");
		} catch (Exception e) {
			logger.error("搜索采购任务遇到错误: ", e);
			Assert.fail("搜索采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void openPurchaseTaskTestCase07() {
		ReporterCSS.title("测试点: 按采购员搜索目标采购任务");
		try {
			boolean result = openPurcahseService.createPurchaseTask(openPurchaseTaskCreateParam);
			Assert.assertEquals(result, true, "新建采购任务失败");

			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setPinlei_id(openPurchaseSpec.getPinlei_id());

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertNotEquals(openPurchaseTasks, null, "搜索欧过滤采购任务失败");

			String task_id = null;
			String spec_id = null;
			boolean find = false;
			OK: for (OpenPurchaseTaskBean openPurchaseTask : openPurchaseTasks) {
				spec_id = openPurchaseTask.getSpec_id();
				if (spec_id.equals(openPurchaseSpec.getSpec_id())) {
					List<OpenPurchaseTaskBean.PurchaseTask> purchase_tasks = openPurchaseTask.getPurchase_tasks();
					for (OpenPurchaseTaskBean.PurchaseTask purchase_task : purchase_tasks) {
						if (purchase_task.getPlan_purchase_count()
								.compareTo(new BigDecimal(openPurchaseTaskCreateParam.getPurchase_count())) == 0
								&& purchase_task.getSupplier_id()
										.equals(openPurchaseTaskCreateParam.getSupplier_id())) {
							task_id = purchase_task.getTask_id();
							find = true;
							break OK;
						}
					}
				}
			}
			Assert.assertEquals(find, true, "新建的采购任务没有在采购任务列表找到");

			openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setPurchaser_id(openPurchaseTaskCreateParam.getPurchaser_id());

			result = checkPurchaseTask(openPurchaseTaskFilterParam, task_id);
			Assert.assertEquals(result, true, "目标采购任务没有找到");
		} catch (Exception e) {
			logger.error("搜索采购任务遇到错误: ", e);
			Assert.fail("搜索采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void openPurchaseTaskTestCase08() {
		ReporterCSS.title("测试点: 按供应商搜索目标采购任务");
		try {
			boolean result = openPurcahseService.createPurchaseTask(openPurchaseTaskCreateParam);
			Assert.assertEquals(result, true, "新建采购任务失败");

			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setPinlei_id(openPurchaseSpec.getPinlei_id());

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertNotEquals(openPurchaseTasks, null, "搜索欧过滤采购任务失败");

			String task_id = null;
			String spec_id = null;
			boolean find = false;
			OK: for (OpenPurchaseTaskBean openPurchaseTask : openPurchaseTasks) {
				spec_id = openPurchaseTask.getSpec_id();
				if (spec_id.equals(openPurchaseSpec.getSpec_id())) {
					List<OpenPurchaseTaskBean.PurchaseTask> purchase_tasks = openPurchaseTask.getPurchase_tasks();
					for (OpenPurchaseTaskBean.PurchaseTask purchase_task : purchase_tasks) {
						if (purchase_task.getPlan_purchase_count()
								.compareTo(new BigDecimal(openPurchaseTaskCreateParam.getPurchase_count())) == 0
								&& purchase_task.getSupplier_id()
										.equals(openPurchaseTaskCreateParam.getSupplier_id())) {
							task_id = purchase_task.getTask_id();
							find = true;
							break OK;
						}
					}
				}
			}
			Assert.assertEquals(find, true, "新建的采购任务没有在采购任务列表找到");

			openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setSupplier_id(openPurchaseTaskCreateParam.getSupplier_id());

			result = checkPurchaseTask(openPurchaseTaskFilterParam, task_id);
			Assert.assertEquals(result, true, "目标采购任务没有找到");
		} catch (Exception e) {
			logger.error("搜索采购任务遇到错误: ", e);
			Assert.fail("搜索采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void openPurchaseTaskTestCase09() {
		ReporterCSS.title("测试点: 按采购任务状态索目标采购任务");
		try {
			boolean result = openPurcahseService.createPurchaseTask(openPurchaseTaskCreateParam);
			Assert.assertEquals(result, true, "新建采购任务失败");

			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setPinlei_id(openPurchaseSpec.getPinlei_id());

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertNotEquals(openPurchaseTasks, null, "搜索欧过滤采购任务失败");

			String task_id = null;
			String spec_id = null;
			boolean find = false;
			OK: for (OpenPurchaseTaskBean openPurchaseTask : openPurchaseTasks) {
				spec_id = openPurchaseTask.getSpec_id();
				if (spec_id.equals(openPurchaseSpec.getSpec_id())) {
					List<OpenPurchaseTaskBean.PurchaseTask> purchase_tasks = openPurchaseTask.getPurchase_tasks();
					for (OpenPurchaseTaskBean.PurchaseTask purchase_task : purchase_tasks) {
						if (purchase_task.getPlan_purchase_count()
								.compareTo(new BigDecimal(openPurchaseTaskCreateParam.getPurchase_count())) == 0
								&& purchase_task.getSupplier_id()
										.equals(openPurchaseTaskCreateParam.getSupplier_id())) {
							task_id = purchase_task.getTask_id();
							find = true;
							break OK;
						}
					}
				}
			}
			Assert.assertEquals(find, true, "新建的采购任务没有在采购任务列表找到");

			openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setStatus("1");

			result = checkPurchaseTask(openPurchaseTaskFilterParam, task_id);
			Assert.assertEquals(result, true, "目标采购任务没有找到");
		} catch (Exception e) {
			logger.error("搜索采购任务遇到错误: ", e);
			Assert.fail("搜索采购任务遇到错误: ", e);
		}
	}

	public boolean checkPurchaseTask(OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam, String task_id)
			throws Exception {
		List<OpenPurchaseTaskBean> openPurchaseTasks = new ArrayList<OpenPurchaseTaskBean>();

		List<OpenPurchaseTaskBean> tempOpenPurchaseTasks = null;

		int offset = Integer.valueOf(openPurchaseTaskFilterParam.getOffset());
		while (true) {
			tempOpenPurchaseTasks = openPurcahseService.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertNotEquals(openPurchaseTasks, null, "搜索欧过滤采购任务失败");
			openPurchaseTasks.addAll(tempOpenPurchaseTasks);
			if (tempOpenPurchaseTasks.size() < Integer.valueOf(openPurchaseTaskFilterParam.getLimit())) {
				break;
			}
			offset += Integer.valueOf(openPurchaseTaskFilterParam.getLimit());
			openPurchaseTaskFilterParam.setOffset(String.valueOf(offset));
		}

		boolean find = false;
		String msg = null;
		boolean result = true;
		for (OpenPurchaseTaskBean openPurchaseTask : openPurchaseTasks) {
			if (openPurchaseTaskFilterParam.getCategory1_id() != null) {
				if (!openPurchaseTask.getCategory1_name().equals(openPurchaseSpec.getCategory1_name())) {
					msg = String.format("采购过滤条件中包含了一级分类%s,但是过滤出了非一级分类%s的采购任务%s",
							openPurchaseTaskFilterParam.getCategory1_id(),
							openPurchaseTaskFilterParam.getCategory1_id(), openPurchaseTask.getCategory1_name());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			if (openPurchaseTaskFilterParam.getCategory2_id() != null) {
				if (!openPurchaseTask.getCategory2_name().equals(openPurchaseSpec.getCategory2_name())) {
					msg = String.format("采购过滤条件中包含了二级分类%s,但是过滤出了非二级分类%s的采购任务%s",
							openPurchaseTaskFilterParam.getCategory2_id(),
							openPurchaseTaskFilterParam.getCategory2_id(), openPurchaseTask.getCategory2_name());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			if (openPurchaseTaskFilterParam.getPinlei_id() != null) {
				if (!openPurchaseTask.getPinlei_name().equals(openPurchaseSpec.getPinlei_name())) {
					msg = String.format("采购过滤条件中包含了品类分类%s,但是过滤出了非品类分类%s的采购任务%s",
							openPurchaseTaskFilterParam.getPinlei_id(), openPurchaseTaskFilterParam.getPinlei_id(),
							openPurchaseTask.getPinlei_name());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			if (openPurchaseTaskFilterParam.getSupplier_id() != null) {
				if (!openPurchaseTask.getSupplier_id().equals(openPurchaseTaskFilterParam.getSupplier_id())) {
					msg = String.format("采购过滤条件中包含了供应商ID %s,但是过滤出了非供应商%s的采购任务%s",
							openPurchaseTaskFilterParam.getSupplier_id(), openPurchaseTaskFilterParam.getSupplier_id(),
							openPurchaseTask.getSupplier_id());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			if (openPurchaseTaskFilterParam.getStatus() != null) {
				if (!String.valueOf(openPurchaseTask.getStatus()).equals(openPurchaseTaskFilterParam.getStatus())) {
					msg = String.format("采购过滤条件中包含了状态值  %s,但是过滤出了状态值%s的采购任务%s", openPurchaseTaskFilterParam.getStatus(),
							openPurchaseTaskFilterParam.getStatus(), openPurchaseTask.getStatus());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			if (openPurchaseTaskFilterParam.getPurchaser_id() != null) {
				if (!openPurchaseTask.getPurchaser_id().equals(openPurchaseTaskFilterParam.getPurchaser_id())) {
					msg = String.format("采购过滤条件中包含了采购员  %s,但是过滤出了非采购员%s的采购任务%s",
							openPurchaseTaskFilterParam.getPurchaser_id(),
							openPurchaseTaskFilterParam.getPurchaser_id(), openPurchaseTask.getPurchaser_id());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			List<OpenPurchaseTaskBean.PurchaseTask> purchase_tasks = openPurchaseTask.getPurchase_tasks();
			for (OpenPurchaseTaskBean.PurchaseTask purchase_task : purchase_tasks) {
				if (purchase_task.getTask_id().equals(task_id)) {
					find = true;
				}
			}
		}
		if (!find) {
			msg = String.format("没有找到目标采购任务%s", task_id);
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}
		return result;
	}

	@Test(timeOut = 20000)
	public void openPurchaseTaskTestCase10() {
		ReporterCSS.title("测试点: 订单的采购任务,按照下单日期搜索");
		try {
			String order_id = orderTool.oneStepCreateOrder(8);
			Assert.assertNotEquals(order_id, null, "下单失败");

			// 采购任务过滤对象参数
			PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
			String today = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
			param.setBegin_time(today);
			param.setEnd_time(today);
			param.setQ(order_id);
			param.setQ_type(1);
			param.setLimit(10);

			List<PurchaseTaskData> purchaseTaskDataArray = new ArrayList<PurchaseTaskData>();
			PurchaseTaskBean purcahseTask = null;
			boolean more = true;
			String page_obj = null;
			// 采购任务异步生成,有延迟,设置等待次数,每次等待3s
			int waitTimes = 20;
			// 翻页获取
			while (more) {
				purcahseTask = purchaseTaskService.searchPurchaseTask(param);
				if (purcahseTask.getCode() == 0) {
					if (purcahseTask.getPurchaseTaskDataArray() != null) {
						purchaseTaskDataArray.addAll(purcahseTask.getPurchaseTaskDataArray());
						if (purcahseTask.getPagination() != null) {
							more = purcahseTask.getPagination().isMore();
							page_obj = purcahseTask.getPagination().getPage_obj();
							param.setPage_obj(page_obj);
						}
					} else {
						waitTimes -= 1;
						Thread.sleep(3000);
					}
				} else {
					more = false;
					Assert.assertEquals(purcahseTask.getMsg(), "ok", "搜索过滤采购任务失败");
				}
				if (waitTimes == 0) {
					break;
				}
			}
			Assert.assertEquals(purchaseTaskDataArray != null && purchaseTaskDataArray.size() > 0, true,
					"订单" + order_id + "的采购任务在60秒内没有生成");

			List<OpenPurchaseTaskBean> openPurchaseTasks = new ArrayList<OpenPurchaseTaskBean>();

			List<OpenPurchaseTaskBean> tempOpenPurchaseTasks = null;

			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			int offset = Integer.valueOf(openPurchaseTaskFilterParam.getOffset());
			while (true) {
				tempOpenPurchaseTasks = openPurcahseService.queryPurcahseTask(openPurchaseTaskFilterParam);
				Assert.assertNotEquals(openPurchaseTasks, null, "搜索欧过滤采购任务失败");
				openPurchaseTasks.addAll(tempOpenPurchaseTasks);
				if (tempOpenPurchaseTasks.size() < Integer.valueOf(openPurchaseTaskFilterParam.getLimit())) {
					break;
				}
				offset += Integer.valueOf(openPurchaseTaskFilterParam.getLimit());
				openPurchaseTaskFilterParam.setOffset(String.valueOf(offset));
			}

			boolean find = false;
			OK: for (OpenPurchaseTaskBean openPurchaseTask : openPurchaseTasks) {
				List<OpenPurchaseTaskBean.PurchaseTask> purchase_tasks = openPurchaseTask.getPurchase_tasks();
				for (OpenPurchaseTaskBean.PurchaseTask purchase_task : purchase_tasks) {
					if (purchase_task.getOrder_id().equals(order_id)) {
						find = true;
						break OK;
					}
				}
			}
			Assert.assertEquals(find, true, "按下单日期搜索,没有搜索到订单 " + order_id + " 的采购任务");
		} catch (Exception e) {
			logger.error("搜索采购任务遇到错误: ", e);
			Assert.fail("搜索采购任务遇到错误: ", e);
		}
	}

	@Test(timeOut = 20000)
	public void openPurchaseTaskTestCase11() {
		ReporterCSS.title("测试点: 订单的采购任务,按照收货日期搜索");
		try {
			String order_id = orderTool.oneStepCreateOrder(8);
			Assert.assertNotEquals(order_id, null, "下单失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");
			String receive_begin_time = orderDetail.getCustomer().getReceive_begin_time().substring(0, 10);
			String receive_end_time = orderDetail.getCustomer().getReceive_end_time().substring(0, 10);

			// 采购任务过滤对象参数
			PurchaseTaskFilterParam param = new PurchaseTaskFilterParam();
			String today = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
			param.setBegin_time(today);
			param.setEnd_time(today);
			param.setQ(order_id);
			param.setQ_type(1);
			param.setLimit(10);

			List<PurchaseTaskData> purchaseTaskDataArray = new ArrayList<PurchaseTaskData>();
			PurchaseTaskBean purcahseTask = null;
			boolean more = true;
			String page_obj = null;
			// 采购任务异步生成,有延迟,设置等待次数,每次等待3s
			int waitTimes = 20;
			// 翻页获取
			while (more) {
				purcahseTask = purchaseTaskService.searchPurchaseTask(param);
				if (purcahseTask.getCode() == 0) {
					if (purcahseTask.getPurchaseTaskDataArray() != null) {
						purchaseTaskDataArray.addAll(purcahseTask.getPurchaseTaskDataArray());
						if (purcahseTask.getPagination() != null) {
							more = purcahseTask.getPagination().isMore();
							page_obj = purcahseTask.getPagination().getPage_obj();
							param.setPage_obj(page_obj);
						}
					} else {
						waitTimes -= 1;
						Thread.sleep(3000);
					}
				} else {
					more = false;
					Assert.assertEquals(purcahseTask.getMsg(), "ok", "搜索过滤采购任务失败");
				}
				if (waitTimes == 0) {
					break;
				}
			}
			Assert.assertEquals(purchaseTaskDataArray != null && purchaseTaskDataArray.size() > 0, true,
					"订单" + order_id + "的采购任务在60秒内没有生成");

			List<OpenPurchaseTaskBean> openPurchaseTasks = new ArrayList<OpenPurchaseTaskBean>();

			List<OpenPurchaseTaskBean> tempOpenPurchaseTasks = null;

			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(receive_begin_time);
			openPurchaseTaskFilterParam.setEnd_date(receive_end_time);
			openPurchaseTaskFilterParam.setQuery_type("2");
			int offset = Integer.valueOf(openPurchaseTaskFilterParam.getOffset());
			while (true) {
				tempOpenPurchaseTasks = openPurcahseService.queryPurcahseTask(openPurchaseTaskFilterParam);
				Assert.assertNotEquals(openPurchaseTasks, null, "搜索欧过滤采购任务失败");
				openPurchaseTasks.addAll(tempOpenPurchaseTasks);
				if (tempOpenPurchaseTasks.size() < Integer.valueOf(openPurchaseTaskFilterParam.getLimit())) {
					break;
				}
				offset += Integer.valueOf(openPurchaseTaskFilterParam.getLimit());
				openPurchaseTaskFilterParam.setOffset(String.valueOf(offset));
			}

			boolean find = false;
			OK: for (OpenPurchaseTaskBean openPurchaseTask : openPurchaseTasks) {
				List<OpenPurchaseTaskBean.PurchaseTask> purchase_tasks = openPurchaseTask.getPurchase_tasks();
				for (OpenPurchaseTaskBean.PurchaseTask purchase_task : purchase_tasks) {
					if (purchase_task.getOrder_id().equals(order_id)) {
						find = true;
						break OK;
					}
				}
			}
			Assert.assertEquals(find, true, "按收货日期搜索,没有搜索到订单 " + order_id + " 的采购任务");
		} catch (Exception e) {
			logger.error("搜索采购任务遇到错误: ", e);
			Assert.fail("搜索采购任务遇到错误: ", e);
		}
	}
}
