package cn.guanmai.station.category;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.async.AsyncTaskResultBean;
import cn.guanmai.station.bean.category.PurchaseSpecBean;
import cn.guanmai.station.bean.category.SalemenuBean;
import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.category.SalemenuSkuBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.bean.category.param.BatchDeleteSkuParam;
import cn.guanmai.station.bean.category.param.SalemenuSkuFilterParam;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.ReporterCSS;

/* 
* @author liming 
* @date Nov 9, 2018 4:14:23 PM 
* @des 新建销售SKU测试
* @version 1.0 
*/
public class SkuDeleteTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SkuDeleteTest.class);
	private InitDataBean initData;
	private SpuBean spu;
	private String sku_id;
	private SkuBean sku;
	private PurchaseSpecBean purchaseSpec;
	private SupplierDetailBean supplier;
	private SalemenuBean salemenu;

	private CategoryService categoryService;
	private LoginUserInfoService loginUserInfoService;
	private AsyncService asyncService;

	@BeforeClass
	public void beforeTest() {
		try {
			Map<String, String> headers = getStationCookie();
			initData = getInitData();
			categoryService = new CategoryServiceImpl(headers);
			asyncService = new AsyncServiceImpl(headers);
			loginUserInfoService = new LoginUserInfoServiceImpl(headers);

			spu = initData.getSpu();
			purchaseSpec = initData.getPurchaseSpec();
			supplier = initData.getSupplier();
			salemenu = initData.getSalemenu();

		} catch (Exception e) {
			logger.error("初始化站点数据遇到错误: ", e);
			Assert.fail("初始化站点数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		sku_id = null;

		// 填写SKU相关属性值,创建SKU必填
		sku = new SkuBean();
		sku.setSpu_id(spu.getId());
		sku.setOuter_id("");
		sku.setStd_sale_price(new BigDecimal("400"));
		sku.setPartframe(1);
		sku.setStd_unit_name(spu.getStd_unit_name());
		sku.setSlitting(1);
		sku.setSale_num_least(new BigDecimal("1"));
		sku.setStocks("-99999");
		sku.setSale_ratio(new BigDecimal("1"));
		sku.setSale_unit_name(spu.getStd_unit_name());
		sku.setDesc("SkuCreateTestCase01");
		sku.setSupplier_id(supplier.getId());
		sku.setIs_price_timing(0);
		sku.setIs_weigh(1);
		sku.setPurchase_spec_id(purchaseSpec.getId());
		sku.setAttrition_rate(BigDecimal.ZERO);
		sku.setStock_type(1);
		sku.setName(spu.getName() + "|" + spu.getStd_unit_name());
		sku.setSalemenu_id(salemenu.getId());
		sku.setState(1);
		sku.setSale_price(new BigDecimal("400"));
		sku.setRemark_type(7);

		try {
			sku_id = categoryService.createSaleSku(sku);
			Assert.assertEquals(sku_id != null, true, "新建销售SKU失败");
		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}

	}

	/**
	 * 删除SKU测试
	 * 
	 */
	@Test
	public void skuDeleteTestCase01() {
		ReporterCSS.title("测试点: 单个删除销售SKU");
		try {
			boolean result = categoryService.deleteSaleSku(sku_id);
			Assert.assertEquals(result, true, "删除销售规格,断言成功");
		} catch (Exception e) {
			logger.error("删除销售SKU遇到错误: ", e);
			Assert.fail("删除销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void skuDeleteTestCase02() {
		ReporterCSS.title("测试点: 查看站点是否有批量删除销售SKU的权限");
		try {
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			JSONArray user_permission = loginUserInfo.getUser_permission();
			Assert.assertEquals(user_permission.contains("delete_sku_batch"), true, "站点无批量删除销售SKU权限,无法进行批量删除销售SKU操作");
		} catch (Exception e) {
			logger.error("查看站点是否有批量删除销售SKU的权限遇到错误: ", e);
			Assert.fail("查看站点是否有批量删除销售SKU的权限遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "skuDeleteTestCase02" })
	public void skuDeleteTestCase03() {
		ReporterCSS.title("测试点: 批量删除销售SKU");
		try {
			JSONArray sku_ids = new JSONArray();
			sku_ids.add(sku_id);

			BatchDeleteSkuParam param = new BatchDeleteSkuParam(sku_ids, 1);

			boolean result = categoryService.batchDeleteSaleSku(param);
			Assert.assertEquals(result, true, "批量删除销售规格,异步任务创建成功");

			Thread.sleep(1000);

			List<AsyncTaskResultBean> asyncTasks = asyncService.getAsyncTaskResultList();
			Assert.assertNotNull(asyncTasks, "获取异步任务列表失败");

			AsyncTaskResultBean asyncTask = asyncTasks.stream().filter(at -> at.getType() == 12).findFirst()
					.orElse(null);
			Assert.assertNotEquals(asyncTask, null, "没有找到对应的异步任务");

			BigDecimal task_id = asyncTask.getTask_id();

			result = asyncService.getAsyncTaskResult(task_id, "失败0");
			Assert.assertEquals(result, true, "批量删除销售规格的异步任务执行失败");

			Thread.sleep(1000);

			SkuBean sku = categoryService.getSaleSkuById(spu.getId(), sku_id);
			Assert.assertNull(sku, "删除的销售SKU,再次查询应该获取不到对应信息");
		} catch (Exception e) {
			logger.error("批量删除销售SKU遇到错误: ", e);
			Assert.fail("批量删除销售SKU遇到错误: ", e);
		} finally {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				logger.error("线程睡眠1秒遇到错误: ", e);
				Assert.fail("线程睡眠1秒遇到错误: ", e);
			}
		}
	}

	@Test(dependsOnMethods = { "skuDeleteTestCase02" })
	public void skuDeleteTestCase04() {
		ReporterCSS.title("测试点: 批量删除销售SKU,过滤后删除");
		try {
			String category1_id = initData.getCategory1().getId();
			JSONArray category1_ids = new JSONArray();
			category1_ids.add(category1_id);

			String category2_id = initData.getCategory2().getId();
			JSONArray category2_ids = new JSONArray();
			category2_ids.add(category2_id);

			String pinlei_id = initData.getPinlei().getId();
			JSONArray pinlei_ids = new JSONArray();
			pinlei_ids.add(pinlei_id);

			String salemenu_id = initData.getSalemenu().getId();
			JSONArray salemenu_ids = new JSONArray();
			salemenu_ids.add(salemenu_id);

			SalemenuSkuFilterParam filterParam = new SalemenuSkuFilterParam();
			filterParam.setCategory1_ids(category1_ids);
			filterParam.setCategory2_ids(category2_ids);
			filterParam.setPinlei_ids(pinlei_ids);
			filterParam.setSalemenu_id(salemenu_id);

			List<SalemenuSkuBean> skuSalemenuList = categoryService.searchSkuInSalemenu(filterParam);
			Assert.assertNotNull(skuSalemenuList, "报价单里搜索过滤销售商品失败");

			BatchDeleteSkuParam param = new BatchDeleteSkuParam(category1_ids, category2_ids, pinlei_ids, salemenu_ids,
					null, null, 1);
			boolean result = categoryService.batchDeleteSaleSku(param);
			Assert.assertEquals(result, true, "批量删除销售规格,异步任务创建成功");

			Thread.sleep(1000);

			List<AsyncTaskResultBean> asyncTasks = asyncService.getAsyncTaskResultList();
			Assert.assertNotNull(asyncTasks, "获取异步任务列表失败");

			AsyncTaskResultBean asyncTask = asyncTasks.stream().filter(at -> at.getType() == 12).findFirst()
					.orElse(null);

			BigDecimal task_id = asyncTask.getTask_id();

			result = asyncService.getAsyncTaskResult(task_id, "失败0");
			Assert.assertEquals(result, true, "批量删除销售规格的异步任务执行失败");

			Thread.sleep(1000);

			skuSalemenuList = categoryService.searchSkuInSalemenu(filterParam);
			Assert.assertNotNull(skuSalemenuList, "报价单里搜索过滤销售商品失败");

			Assert.assertEquals(skuSalemenuList.size() == 0, true, "搜素过滤批量删除后,再次查询销售SKU还存在,与预期不符");

		} catch (Exception e) {
			logger.error("批量删除销售SKU遇到错误: ", e);
			Assert.fail("批量删除销售SKU遇到错误: ", e);
		}
	}

}
