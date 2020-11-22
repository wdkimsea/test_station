package cn.guanmai.station.category;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.async.AsyncTaskResultBean;
import cn.guanmai.station.bean.category.PurchaseSpecBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.bean.category.SpuIndexBean;
import cn.guanmai.station.bean.category.param.BatchDeleteSpuParam;
import cn.guanmai.station.bean.category.param.SpuIndexFilterParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.LocalRetry;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Nov 7, 2018 10:03:31 AM 
* @des 删除SPU商品
* @version 1.0 
*/
public class SpuDeleteTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SpuDeleteTest.class);
	private InitDataBean initData;
	private String category1_id;
	private String category2_id;
	private String pinlei_id;
	private String supplier_id;

	private LoginUserInfoService loginUserInfoService;
	private CategoryService categoryService;
	private AsyncService asyncService;

	@BeforeClass
	public void beforeTest() {
		Map<String, String> headers = getStationCookie();
		initData = getInitData();
		asyncService = new AsyncServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		try {
			category1_id = initData.getCategory1().getId();
			category2_id = initData.getCategory2().getId();
			pinlei_id = initData.getPinlei().getId();
			supplier_id = initData.getSupplier().getId();
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}

	}

	/**
	 * 删除SPU测试
	 * 
	 */
	@Test
	public void spuDeleteTestCase01() {
		try {
			ReporterCSS.title("测试点: 单个删除SPU测试");

			Reporter.log("步骤一: 新建SPU用于删除操作");
			String name = StringUtil.getRandomString(6);
			SpuBean spu = new SpuBean(name, pinlei_id, "case01", new JSONArray(), 0, "斤", new JSONArray(), 2, 1);
			String spu_id = categoryService.createSpu(spu);
			Assert.assertNotEquals(spu_id, null, "新建SPU失败");

			Reporter.log("步骤一: 删除新建的SPU");
			boolean result = categoryService.deleteSpu(spu_id);
			Assert.assertEquals(result, true, "删除SPU失败");

			SpuIndexFilterParam fiterParam = new SpuIndexFilterParam();
			fiterParam.setCategory1_ids(new JSONArray());
			fiterParam.setCategory2_ids(new JSONArray());
			fiterParam.setPinlei_ids(new JSONArray());
			fiterParam.setSalemenu_ids(new JSONArray());
			fiterParam.setQ(spu_id);

			List<SpuIndexBean> spuIndexs = categoryService.searchSpuIndex(fiterParam);
			Assert.assertEquals(spuIndexs != null, true, "商品库搜索过滤失败");

			Assert.assertEquals(spuIndexs.size(), 0, "批量商品SPU后,删除的商品还可以在商品库搜索查询到");
		} catch (Exception e) {
			logger.error("删除SPU商品遇到错误: ", e);
			Assert.fail("删除SPU商品遇到错误: ", e);
		}
	}

	@Test
	public void spuDeleteTestCase02() {
		try {
			ReporterCSS.title("测试点: 查看登录账户是否有批量删除SPU权限");
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			JSONArray user_permission = loginUserInfo.getUser_permission();
			Assert.assertEquals(user_permission.contains("delete_spu_batch"), true, "登录账号无批量删除SPU权限,无法进行批量删除SPU操作");
		} catch (Exception e) {
			logger.error("获取登录账号权限到错误: ", e);
			Assert.fail("获取登录账号权限到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "spuDeleteTestCase02" })
	public void spuDeleteTestCase03() {
		boolean result = true;
		String spu_id = null;
		try {
			ReporterCSS.title("测试点: 批量删除SPU测试(指定SPU ID列表删除)");

			Reporter.log("步骤一: 新建SPU用于删除操作");
			String name = StringUtil.getRandomString(6);
			SpuBean spu = new SpuBean(name, pinlei_id, "spuDeleteTestCase03", new JSONArray(), 0, "斤", new JSONArray(),
					2, 1);
			spu_id = categoryService.createSpu(spu);
			Assert.assertNotEquals(spu_id, null, "新建SPU失败");

			JSONArray spu_ids = new JSONArray();
			spu_ids.add(spu_id);

			BatchDeleteSpuParam param = new BatchDeleteSpuParam(spu_ids);
			result = categoryService.batchDeleteSpu(param);
			Assert.assertEquals(result, true, "批量删除SPU,创建异步任务失败");

			Thread.sleep(500);

			List<AsyncTaskResultBean> asyncTasks = asyncService.getAsyncTaskResultList();
			Assert.assertNotNull(asyncTasks, "获取异步任务列表失败");

			AsyncTaskResultBean asyncTask = asyncTasks.stream()
					.filter(at -> at.getType() == 12 && at.getTask_name().contains("批量删除商品")).findFirst().orElse(null);
			Assert.assertNotEquals(asyncTask, null, "批量删除SPU的异步任务没有找到");

			BigDecimal task_id = asyncTask.getTask_id();

			result = asyncService.getAsyncTaskResult(task_id, "失败0");
			Assert.assertEquals(result, true, "批量删除SPU的异步任务执行失败");

			SpuIndexFilterParam fiterParam = new SpuIndexFilterParam();
			fiterParam.setCategory1_ids(new JSONArray());
			fiterParam.setCategory2_ids(new JSONArray());
			fiterParam.setPinlei_ids(new JSONArray());
			fiterParam.setSalemenu_ids(new JSONArray());
			fiterParam.setQ(spu_id);

			List<SpuIndexBean> spuIndexs = categoryService.searchSpuIndex(fiterParam);
			Assert.assertEquals(spuIndexs != null, true, "商品库搜索过滤失败");

			Assert.assertEquals(spuIndexs.size(), 0, "批量商品SPU后,删除的商品还可以在商品库搜索查询到");
		} catch (Exception e) {
			logger.error("批量删除SPU商品遇到错误: ", e);
			Assert.fail("批量删除SPU商品遇到错误: ", e);
		} finally {
			try {
				if (!result) {
					ReporterCSS.title("后置处理: 如果批量删除没有成功,此处做单个商品删除");
					result = categoryService.deleteSpu(spu_id);
					Assert.assertEquals(result, true, "单个删除SPU失败");
				}
			} catch (Exception e) {
				logger.error("单个删除SPU商品遇到错误: ", e);
				Assert.fail("单个删除SPU商品遇到错误: ", e);
			}
		}
	}

	@Test(dependsOnMethods = { "spuDeleteTestCase02" })
	public void spuDeleteTestCase04() {
		boolean result = true;
		String spu_id = null;
		ReporterCSS.title("测试点: 批量删除SPU测试(搜索过滤后删除)");
		try {
			Reporter.log("步骤一: 新建SPU用于删除操作");
			String name = StringUtil.getRandomString(6);
			SpuBean spu = new SpuBean(name, pinlei_id, "spuDeleteTestCase04", new JSONArray(), 0, "斤", new JSONArray(),
					2, 1);
			spu_id = categoryService.createSpu(spu);
			Assert.assertNotEquals(spu_id, null, "新建SPU失败");

			JSONArray category1_ids = new JSONArray();
			category1_ids.add(category1_id);
			JSONArray category2_ids = new JSONArray();
			category2_ids.add(category2_id);
			JSONArray pinlei_ids = new JSONArray();
			pinlei_ids.add(pinlei_id);

			BatchDeleteSpuParam param = new BatchDeleteSpuParam(category1_ids, category2_ids, pinlei_ids,
					new JSONArray(), spu_id);
			result = categoryService.batchDeleteSpu(param);
			Assert.assertEquals(result, true, "批量删除SPU,创建异步任务失败");

			List<AsyncTaskResultBean> asyncTasks = asyncService.getAsyncTaskResultList();
			Assert.assertNotNull(asyncTasks, "获取异步任务列表失败");

			AsyncTaskResultBean asyncTask = asyncTasks.stream()
					.filter(at -> at.getType() == 12 && at.getTask_name().contains("批量删除商品")).findFirst().orElse(null);
			Assert.assertNotEquals(asyncTask, null, "批量删除SPU的异步任务没有找到");

			BigDecimal task_id = asyncTask.getTask_id();

			result = asyncService.getAsyncTaskResult(task_id, "失败0");
			Assert.assertEquals(result, true, "批量删除SPU的异步任务执行失败");

			Thread.sleep(1000);

			SpuIndexFilterParam fiterParam = new SpuIndexFilterParam();
			fiterParam.setCategory1_ids(category1_ids);
			fiterParam.setCategory2_ids(category2_ids);
			fiterParam.setPinlei_ids(pinlei_ids);
			fiterParam.setQ(spu_id);

			List<SpuIndexBean> spuIndexs = categoryService.searchSpuIndex(fiterParam);
			Assert.assertEquals(spuIndexs != null, true, "商品库搜索过滤失败");

			Assert.assertEquals(spuIndexs.size(), 0, "批量商品SPU后,删除的商品还可以在商品库搜索查询到");
		} catch (Exception e) {
			logger.error("批量删除SPU商品遇到错误: ", e);
			Assert.fail("批量删除SPU商品遇到错误: ", e);
		} finally {
			try {
				if (!result) {
					ReporterCSS.title("后置处理: 如果批量删除没有成功,此处做单个商品删除");
					result = categoryService.deleteSpu(spu_id);
					Assert.assertEquals(result, true, "单个删除SPU失败");
					Thread.sleep(1000);
				}
			} catch (Exception e) {
				logger.error("单个删除SPU商品遇到错误: ", e);
				Assert.fail("单个删除SPU商品遇到错误: ", e);
			}
		}
	}

	@Test(dependsOnMethods = { "spuDeleteTestCase02" }, retryAnalyzer = LocalRetry.class)
	public void spuDeleteTestCase05() {
		String spu_id = null;
		boolean result = true;
		ReporterCSS.title("测试点: 批量删除SPU(查看对应的销售SKU和采购规格是否删除)");
		try {
			String spu_name = "板叶茼蒿";
			SpuBean spu = categoryService.getSpuByName(pinlei_id, spu_name);
			if (spu == null) {
				SpuBean temp_spu = new SpuBean(spu_name, pinlei_id, "spuDeleteTestCase05", new JSONArray(), 0, "斤",
						new JSONArray(), 2, 1);
				spu_id = categoryService.createSpu(temp_spu);
				Assert.assertNotEquals(spu_id, null, "新建SPU失败");
			} else {
				spu_id = spu.getId();
			}

			String unit_name_1 = StringUtil.getRandomString(4).toString();
			PurchaseSpecBean purchaseSpec_1 = new PurchaseSpecBean(spu_name + "|" + unit_name_1,
					StringUtil.getRandomString(6), String.valueOf(TimeUtil.getLongTime()), unit_name_1,
					new BigDecimal(1), category1_id, category2_id, pinlei_id, spu_id);
			String purchase_spec_id_1 = categoryService.createPurchaseSpec(purchaseSpec_1);
			Assert.assertNotEquals(purchase_spec_id_1, null, "新建采购规格失败");

			String unit_name_2 = StringUtil.getRandomString(4).toString();
			PurchaseSpecBean purchaseSpec_2 = new PurchaseSpecBean(spu_name + "|" + unit_name_2,
					StringUtil.getRandomString(6), String.valueOf(TimeUtil.getLongTime()), unit_name_2,
					new BigDecimal(1), category1_id, category2_id, pinlei_id, spu_id);
			String purchase_spec_id_2 = categoryService.createPurchaseSpec(purchaseSpec_2);
			Assert.assertNotEquals(purchase_spec_id_2, null, "新建采购规格失败");

			JSONArray spu_ids = new JSONArray();
			spu_ids.add(spu_id);

			BatchDeleteSpuParam param = new BatchDeleteSpuParam(spu_ids);
			result = categoryService.batchDeleteSpu(param);
			Assert.assertEquals(result, true, "批量删除SPU,创建异步任务失败");

			List<AsyncTaskResultBean> asyncTasks = asyncService.getAsyncTaskResultList();
			Assert.assertNotNull(asyncTasks, "获取异步任务列表失败");

			AsyncTaskResultBean asyncTask = asyncTasks.stream()
					.filter(at -> at.getType() == 12 && at.getTask_name().contains("批量删除商品")).findFirst().orElse(null);
			Assert.assertNotEquals(asyncTask, null, "批量删除SPU的异步任务没有找到");

			BigDecimal task_id = asyncTask.getTask_id();

			result = asyncService.getAsyncTaskResult(task_id, "失败0");
			Assert.assertEquals(result, true, "批量删除SPU的异步任务执行失败");

			Thread.sleep(1500);

			List<PurchaseSpecBean> purchaseSpecList = categoryService.getPurchaseSpecArray(spu_id, supplier_id);
			Assert.assertNotEquals(purchaseSpecList, null, "获取指定SPU对应的采购规格列表失败");

			Assert.assertEquals(purchaseSpecList.size(), 0, "批量删除SPU后,其对应的采购规格没有删除");
		} catch (Exception e) {
			logger.error("批量删除SPU商品遇到错误: ", e);
			Assert.fail("批量删除SPU商品遇到错误: ", e);
		} finally {
			try {
				if (!result) {
					ReporterCSS.title("后置处理: 如果批量删除没有成功,此处做单个商品删除");
					result = categoryService.deleteSpu(spu_id);
					Assert.assertEquals(result, true, "单个删除SPU失败");
					Thread.sleep(1000);
				}
			} catch (Exception e) {
				logger.error("单个删除SPU商品遇到错误: ", e);
				Assert.fail("单个删除SPU商品遇到错误: ", e);
			}
		}
	}
}
