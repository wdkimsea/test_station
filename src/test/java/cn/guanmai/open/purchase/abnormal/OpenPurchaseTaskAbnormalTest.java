package cn.guanmai.open.purchase.abnormal;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
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
import cn.guanmai.open.purchase.OpenPurchaseTaskTest;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.DeepCloneUtil;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/**
 * @author liming
 * @date 2019年11月14日
 * @time 下午5:56:38
 * @des TODO
 */

public class OpenPurchaseTaskAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenPurchaseTaskTest.class);
	private OpenPurcahseService openPurcahseService;
	private OpenSupplierService openSupplierService;
	private OpenCategoryService openCategoryService;

	private OpenPurchaseTaskCreateParam openPurchaseTaskCreateParam;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private OpenPurchaseSpecBean openPurchaseSpec;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openPurcahseService = new OpenPurcahseServiceImpl(access_token);
		openSupplierService = new OpenSupplierServiceImpl(access_token);
		openCategoryService = new OpenCategoryServiceImpl(access_token);

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
			logger.error("初始化新建采购任务遇到错误: ", e);
			Assert.fail("初始化新建采购任务遇到错误: ", e);
		}

	}

	@Test
	public void openPurchaseTaskAbnormalTestCase01() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,不指定搜索方式,断言失败");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks, null, "异常测试,搜索过滤采购任务,不指定搜索方式,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase02() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,搜索方式指定空值,断言失败");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type(" ");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks, null, "异常测试,搜索过滤采购任务,搜索方式指定空值,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase03() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,搜索方式指定非后选值,断言失败");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("3");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks, null, "异常测试,搜索过滤采购任务,搜索方式指定空值,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase04() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,搜索日期输入非指定格式,断言失败");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			String todayStr = TimeUtil.getCurrentTime("yyyy.MM.dd");
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks, null, "异常测试,搜索过滤采购任务,搜索日期输入非指定格式,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase05() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,输入起始时间晚于结束时间,断言失败");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			String end_time = TimeUtil.calculateTime("yyyy-MM-dd", todayStr, -2, Calendar.DATE);
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(end_time);
			openPurchaseTaskFilterParam.setQuery_type("1");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks, null, "异常测试,搜索过滤采购任务,输入起始时间晚于结束时间,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase06() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,结束时间输入为空,断言失败");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date("");
			openPurchaseTaskFilterParam.setQuery_type("1");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks, null, "异常测试,搜索过滤采购任务,结束时间输入为空,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase07() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,一级分类输入非法值,断言搜索结果为空");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setCategory1_id("A");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks == null, true, "异常测试,搜索过滤采购任务,一级分类输入非法值,断言搜索结果为空");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase08() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,二级分类输入非法值,断言搜索结果为空");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setCategory2_id("B");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks == null, true, "异常测试,搜索过滤采购任务,二级分类输入非法值,断言搜索结果为空");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase09() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,品类分类输入非法值,断言搜索结果为空");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setPinlei_id("P");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks == null, true, "异常测试,搜索过滤采购任务,品类分类输入非法值,断言搜索结果为空");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase10() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,一级分类输入非本站一级分类ID,断言搜索结果为空");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setCategory1_id("A3772");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks == null, true, "异常测试,搜索过滤采购任务,一级分类输入非本站一级分类ID,断言搜索结果为空");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase11() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,二级分类输入非本站一级分类ID,断言搜索结果为空");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setCategory2_id("B18495");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks == null, true, "异常测试,搜索过滤采购任务,二级分类输入非本站一级分类ID,断言搜索结果为空");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase12() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,品类分类输入非本站品类分类ID,断言搜索结果为空");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setPinlei_id("P345690");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks == null, true, "异常测试,搜索过滤采购任务,品类分类输入非本站品类分类ID,断言搜索结果为空");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase13() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,供应商输入错误值,断言搜索结果为空");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setSupplier_id("T7936");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks == null, true, "异常测试,搜索过滤采购任务,供应商输入错误值,断言搜索结果为空");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase14() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,状态值输入非数值,断言失败");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setStatus("A");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks, null, "异常测试,搜索过滤采购任务,状态值输入非数值,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase15() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,状态值输入非候选值,断言失败");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setStatus("0");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks, null, "异常测试,搜索过滤采购任务,状态值输入非候选值,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase16() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,采购员ID输入非数值,断言失败");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setPurchaser_id("A");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks, null, "异常测试,搜索过滤采购任务,采购员ID输入非数值,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase17() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,采购员ID输入错误值,断言失败");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setPurchaser_id("13");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks, null, "异常测试,搜索过滤采购任务,采购员ID输入错误值,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase18() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,分页Offset输入非数值,断言失败");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setOffset("a");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks, null, "异常测试,搜索过滤采购任务,分页Offset输入非数值,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase19() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,分页Offset输入负数,断言失败");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setOffset("-10");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks, null, "异常测试,搜索过滤采购任务,分页Offset输入负数,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase20() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,分页limit输入非数值,断言失败");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setOffset("a");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks, null, "异常测试,搜索过滤采购任务,分页limit输入非数值,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase21() {
		ReporterCSS.title("测试点: 异常测试,搜索过滤采购任务,分页limit输入负数,断言失败");
		try {
			OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam = new OpenPurchaseTaskFilterParam();
			openPurchaseTaskFilterParam.setStart_date(todayStr);
			openPurchaseTaskFilterParam.setEnd_date(todayStr);
			openPurchaseTaskFilterParam.setQuery_type("1");
			openPurchaseTaskFilterParam.setOffset("-10");

			List<OpenPurchaseTaskBean> openPurchaseTasks = openPurcahseService
					.queryPurcahseTask(openPurchaseTaskFilterParam);
			Assert.assertEquals(openPurchaseTasks, null, "异常测试,搜索过滤采购任务,分页limit输入负数,断言失败");
		} catch (Exception e) {
			logger.error("搜索过滤采购任务遇到错误: ", e);
			Assert.fail("搜索过滤采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase22() {
		ReporterCSS.title("测试点: 异常测试,新建采购任务,采购规格输入空值,断言失败");
		try {
			OpenPurchaseTaskCreateParam tempPurchaseTaskCreateParam = DeepCloneUtil
					.deepClone(openPurchaseTaskCreateParam);
			tempPurchaseTaskCreateParam.setSpec_id("");
			boolean result = openPurcahseService.createPurchaseTask(tempPurchaseTaskCreateParam);
			Assert.assertEquals(result, false, "异常测试,新建采购任务,采购规格输入空值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购任务遇到错误: ", e);
			Assert.fail("新建采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase23() {
		ReporterCSS.title("测试点: 异常测试,新建采购任务,采购规格输入错误值,断言失败");
		try {
			OpenPurchaseTaskCreateParam tempPurchaseTaskCreateParam = DeepCloneUtil
					.deepClone(openPurchaseTaskCreateParam);
			tempPurchaseTaskCreateParam.setSpec_id("D17507101");
			boolean result = openPurcahseService.createPurchaseTask(tempPurchaseTaskCreateParam);
			Assert.assertEquals(result, false, "异常测试,新建采购任务,采购规格输入空值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购任务遇到错误: ", e);
			Assert.fail("新建采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase24() {
		ReporterCSS.title("测试点: 异常测试,新建采购任务,采购数量输入空值,断言失败");
		try {
			OpenPurchaseTaskCreateParam tempPurchaseTaskCreateParam = DeepCloneUtil
					.deepClone(openPurchaseTaskCreateParam);
			tempPurchaseTaskCreateParam.setPurchase_count("");
			boolean result = openPurcahseService.createPurchaseTask(tempPurchaseTaskCreateParam);
			Assert.assertEquals(result, false, "异常测试,新建采购任务,采购数量输入空值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购任务遇到错误: ", e);
			Assert.fail("新建采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase25() {
		ReporterCSS.title("测试点: 异常测试,新建采购任务,采购数量输入非数值,断言失败");
		try {
			OpenPurchaseTaskCreateParam tempPurchaseTaskCreateParam = DeepCloneUtil
					.deepClone(openPurchaseTaskCreateParam);
			tempPurchaseTaskCreateParam.setPurchase_count("a");
			boolean result = openPurcahseService.createPurchaseTask(tempPurchaseTaskCreateParam);
			Assert.assertEquals(result, false, "异常测试,新建采购任务,采购数量输入非数值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购任务遇到错误: ", e);
			Assert.fail("新建采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase26() {
		ReporterCSS.title("测试点: 异常测试,新建采购任务,采购数量输入负数,断言失败");
		try {
			OpenPurchaseTaskCreateParam tempPurchaseTaskCreateParam = DeepCloneUtil
					.deepClone(openPurchaseTaskCreateParam);
			tempPurchaseTaskCreateParam.setPurchase_count("-10");
			boolean result = openPurcahseService.createPurchaseTask(tempPurchaseTaskCreateParam);
			Assert.assertEquals(result, false, "异常测试,新建采购任务,采购数量输入负数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购任务遇到错误: ", e);
			Assert.fail("新建采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase27() {
		ReporterCSS.title("测试点: 异常测试,新建采购任务,采购数量输入0,断言失败");
		try {
			OpenPurchaseTaskCreateParam tempPurchaseTaskCreateParam = DeepCloneUtil
					.deepClone(openPurchaseTaskCreateParam);
			tempPurchaseTaskCreateParam.setPurchase_count("0");
			boolean result = openPurcahseService.createPurchaseTask(tempPurchaseTaskCreateParam);
			Assert.assertEquals(result, false, "异常测试,新建采购任务,采购数量输入0,断言失败");
		} catch (Exception e) {
			logger.error("新建采购任务遇到错误: ", e);
			Assert.fail("新建采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase28() {
		ReporterCSS.title("测试点: 异常测试,新建采购任务,采购数量输入多位小数,断言失败");
		try {
			OpenPurchaseTaskCreateParam tempPurchaseTaskCreateParam = DeepCloneUtil
					.deepClone(openPurchaseTaskCreateParam);
			tempPurchaseTaskCreateParam.setPurchase_count("11.234");
			boolean result = openPurcahseService.createPurchaseTask(tempPurchaseTaskCreateParam);
			Assert.assertEquals(result, false, "异常测试,新建采购任务,采购数量输入多位小数,断言失败");
		} catch (Exception e) {
			logger.error("新建采购任务遇到错误: ", e);
			Assert.fail("新建采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase29() {
		ReporterCSS.title("测试点: 异常测试,新建采购任务,采购数量输入过大值,断言失败");
		try {
			OpenPurchaseTaskCreateParam tempPurchaseTaskCreateParam = DeepCloneUtil
					.deepClone(openPurchaseTaskCreateParam);
			tempPurchaseTaskCreateParam.setPurchase_count("10000000");
			boolean result = openPurcahseService.createPurchaseTask(tempPurchaseTaskCreateParam);
			Assert.assertEquals(result, false, "异常测试,新建采购任务,采购数量输入过大值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购任务遇到错误: ", e);
			Assert.fail("新建采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase30() {
		ReporterCSS.title("测试点: 异常测试,新建采购任务,采购员ID输入非数值,断言失败");
		try {
			OpenPurchaseTaskCreateParam tempPurchaseTaskCreateParam = DeepCloneUtil
					.deepClone(openPurchaseTaskCreateParam);
			tempPurchaseTaskCreateParam.setPurchaser_id("D");
			boolean result = openPurcahseService.createPurchaseTask(tempPurchaseTaskCreateParam);
			Assert.assertEquals(result, false, "异常测试,新建采购任务,采购员ID输入非数值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购任务遇到错误: ", e);
			Assert.fail("新建采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase31() {
		ReporterCSS.title("测试点: 异常测试,新建采购任务,采购员ID输错误值,断言失败");
		try {
			OpenPurchaseTaskCreateParam tempPurchaseTaskCreateParam = DeepCloneUtil
					.deepClone(openPurchaseTaskCreateParam);
			tempPurchaseTaskCreateParam.setPurchaser_id("12");
			boolean result = openPurcahseService.createPurchaseTask(tempPurchaseTaskCreateParam);
			Assert.assertEquals(result, false, "异常测试,新建采购任务,采购员ID输错误值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购任务遇到错误: ", e);
			Assert.fail("新建采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase32() {
		ReporterCSS.title("测试点: 异常测试,新建采购任务,采购供应商输入空值,断言失败");
		try {
			OpenPurchaseTaskCreateParam tempPurchaseTaskCreateParam = DeepCloneUtil
					.deepClone(openPurchaseTaskCreateParam);
			tempPurchaseTaskCreateParam.setSupplier_id("");
			boolean result = openPurcahseService.createPurchaseTask(tempPurchaseTaskCreateParam);
			Assert.assertEquals(result, false, "异常测试,新建采购任务,采购供应商输入空值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购任务遇到错误: ", e);
			Assert.fail("新建采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase33() {
		ReporterCSS.title("测试点: 异常测试,新建采购任务,采购供应商输入错误值,断言失败");
		try {
			OpenPurchaseTaskCreateParam tempPurchaseTaskCreateParam = DeepCloneUtil
					.deepClone(openPurchaseTaskCreateParam);
			tempPurchaseTaskCreateParam.setSupplier_id("T7936");
			boolean result = openPurcahseService.createPurchaseTask(tempPurchaseTaskCreateParam);
			Assert.assertEquals(result, false, "异常测试,新建采购任务,采购供应商输入错误值,断言失败");
		} catch (Exception e) {
			logger.error("新建采购任务遇到错误: ", e);
			Assert.fail("新建采购任务遇到错误: ", e);
		}
	}

	private String task_id;

	@Test
	public void openPurchaseTaskAbnormalTestCase34() {
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
							task_id = purchase_task.getTask_id();
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

	@Test(dependsOnMethods = { "openPurchaseTaskAbnormalTestCase34" })
	public void openPurchaseTaskAbnormalTestCase35() {
		ReporterCSS.title("测试点: 异常测试,修改采购任务,供应商传入空格,断言失败");
		try {
			OpenPurchaseTaskUpdateParam openPurchaseTaskUpdateParam = new OpenPurchaseTaskUpdateParam();
			openPurchaseTaskUpdateParam.setTask_id(task_id);
			openPurchaseTaskUpdateParam.setSupplier_id("  ");
			boolean result = openPurcahseService.updatePurcahseTask(openPurchaseTaskUpdateParam);
			Assert.assertEquals(result, false, "异常测试,修改采购任务,供应商传入空格,断言失败");
		} catch (Exception e) {
			logger.error("修改采购任务遇到错误: ", e);
			Assert.fail("修改采购任务遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "openPurchaseTaskAbnormalTestCase34" })
	public void openPurchaseTaskAbnormalTestCase36() {
		ReporterCSS.title("测试点: 异常测试,修改采购任务,供应商传入错误值,断言失败");
		try {
			OpenPurchaseTaskUpdateParam openPurchaseTaskUpdateParam = new OpenPurchaseTaskUpdateParam();
			openPurchaseTaskUpdateParam.setTask_id(task_id);
			openPurchaseTaskUpdateParam.setSupplier_id("T7936");
			boolean result = openPurcahseService.updatePurcahseTask(openPurchaseTaskUpdateParam);
			Assert.assertEquals(result, false, "异常测试,修改采购任务,供应商传入错误值,断言失败");
		} catch (Exception e) {
			logger.error("修改采购任务遇到错误: ", e);
			Assert.fail("修改采购任务遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "openPurchaseTaskAbnormalTestCase34" })
	public void openPurchaseTaskAbnormalTestCase37() {
		ReporterCSS.title("测试点: 异常测试,修改采购任务,采购员传入空格,断言失败");
		try {
			OpenPurchaseTaskUpdateParam openPurchaseTaskUpdateParam = new OpenPurchaseTaskUpdateParam();
			openPurchaseTaskUpdateParam.setTask_id(task_id);
			openPurchaseTaskUpdateParam.setPurchaser_id("  ");
			boolean result = openPurcahseService.updatePurcahseTask(openPurchaseTaskUpdateParam);
			Assert.assertEquals(result, false, "异常测试,修改采购任务,采购员传入空格,断言失败");
		} catch (Exception e) {
			logger.error("修改采购任务遇到错误: ", e);
			Assert.fail("修改采购任务遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "openPurchaseTaskAbnormalTestCase34" })
	public void openPurchaseTaskAbnormalTestCase38() {
		ReporterCSS.title("测试点: 异常测试,修改采购任务,采购员传入错误值,断言失败");
		try {
			OpenPurchaseTaskUpdateParam openPurchaseTaskUpdateParam = new OpenPurchaseTaskUpdateParam();
			openPurchaseTaskUpdateParam.setTask_id(task_id);
			openPurchaseTaskUpdateParam.setPurchaser_id("3581");
			boolean result = openPurcahseService.updatePurcahseTask(openPurchaseTaskUpdateParam);
			Assert.assertEquals(result, false, "异常测试,修改采购任务,采购员传入错误值,断言失败");
		} catch (Exception e) {
			logger.error("修改采购任务遇到错误: ", e);
			Assert.fail("修改采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase39() {
		ReporterCSS.title("测试点: 异常测试,修改采购任务,采购任务ID传入为空,断言失败");
		try {
			OpenPurchaseTaskUpdateParam openPurchaseTaskUpdateParam = new OpenPurchaseTaskUpdateParam();
			openPurchaseTaskUpdateParam.setTask_id("");
			boolean result = openPurcahseService.updatePurcahseTask(openPurchaseTaskUpdateParam);
			Assert.assertEquals(result, false, "异常测试,修改采购任务,采购任务ID传入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改采购任务遇到错误: ", e);
			Assert.fail("修改采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase40() {
		ReporterCSS.title("测试点: 异常测试,修改采购任务,采购任务ID传入错误值,断言失败");
		try {
			OpenPurchaseTaskUpdateParam openPurchaseTaskUpdateParam = new OpenPurchaseTaskUpdateParam();
			openPurchaseTaskUpdateParam.setTask_id("2392");
			openPurchaseTaskUpdateParam.setPurchaser_id(openPurchaseTaskCreateParam.getPurchaser_id());
			boolean result = openPurcahseService.updatePurcahseTask(openPurchaseTaskUpdateParam);
			Assert.assertEquals(result, false, "异常测试,修改采购任务,采购任务ID传入错误值,断言失败");
		} catch (Exception e) {
			logger.error("修改采购任务遇到错误: ", e);
			Assert.fail("修改采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase41() {
		ReporterCSS.title("测试点: 异常测试,修改已经发布的采购任务供应商,断言失败");
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
							find = true;
							task_id = purchase_task.getTask_id();
							break OK;
						}
					}
				}
			}
			Assert.assertEquals(find, true, "新建的采购任务没有在采购任务列表找到");

			result = openPurcahseService.publishPurchaseTask(task_id);
			Assert.assertEquals(result, true, "发布采购任务失败");

			List<OpenSupplierBean> openSuppliers = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			Assert.assertNotEquals(openSuppliers, null, "搜索过滤供应商失败");

			OpenSupplierBean openSupplier = openSuppliers.stream()
					.filter(s -> !s.getSupplier_id().equals(openPurchaseTaskCreateParam.getSupplier_id())).findFirst()
					.orElse(null);

			OpenPurchaseTaskUpdateParam openPurchaseTaskUpdateParam = new OpenPurchaseTaskUpdateParam();
			openPurchaseTaskUpdateParam.setTask_id(task_id);
			openPurchaseTaskUpdateParam.setSupplier_id(openSupplier.getSupplier_id());
			result = openPurcahseService.updatePurcahseTask(openPurchaseTaskUpdateParam);

			Assert.assertEquals(result, false, "异常测试,修改已经发布的采购任务供应商,断言失败");
		} catch (Exception e) {
			logger.error("修改采购任务遇到错误: ", e);
			Assert.fail("修改采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase42() {
		ReporterCSS.title("测试点: 异常测试,发布采购任务,采购任务ID传入空值,断言失败");
		try {
			boolean result = openPurcahseService.publishPurchaseTask("");
			Assert.assertEquals(result, false, "异常测试,发布采购任务,采购任务ID传入空值,断言失败");
		} catch (Exception e) {
			logger.error("修改采购任务遇到错误: ", e);
			Assert.fail("修改采购任务遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaseTaskAbnormalTestCase43() {
		ReporterCSS.title("测试点: 异常测试,发布采购任务,采购任务ID传入错误值,断言失败");
		try {
			boolean result = openPurcahseService.publishPurchaseTask("132986800");
			Assert.assertEquals(result, false, "异常测试,发布采购任务,采购任务ID传入错误值,断言失败");
		} catch (Exception e) {
			logger.error("修改采购任务遇到错误: ", e);
			Assert.fail("修改采购任务遇到错误: ", e);
		}
	}

}
