package cn.guanmai.open.stock.abnormal;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.stock.OpenStockBean;
import cn.guanmai.open.bean.stock.param.OpenStockFilterParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.impl.stock.OpenStockServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.interfaces.stock.OpenStockService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author liming
 * @date 2019年12月18日
 * @time 下午6:01:29
 * @des TODO
 */

public class OpenStockAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenStockAbnormalTest.class);
	private OpenStockService openStockService;
	private OpenCategoryService openCategoryService;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openStockService = new OpenStockServiceImpl(access_token);
		openCategoryService = new OpenCategoryServiceImpl(access_token);

	}

	@Test
	public void openStockAbnormalTestCase01() {
		ReporterCSS.title("测试点: 异常测试,商品盘点,搜索过滤,一级分类ID输入非法值");
		try {
			OpenStockFilterParam openStockFilterParam = new OpenStockFilterParam();
			openStockFilterParam.setCategory1_id("A");

			List<OpenStockBean> openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertEquals(openStockList, null, "异常测试,商品盘点,搜索过滤,一级分类ID输入非法值,断言失败");
		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockAbnormalTestCase02() {
		ReporterCSS.title("测试点: 异常测试,商品盘点,搜索过滤,一级分类ID输入别的Group的一级分类值");
		try {
			OpenStockFilterParam openStockFilterParam = new OpenStockFilterParam();
			openStockFilterParam.setCategory1_id("A238");

			List<OpenStockBean> openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertEquals(openStockList, null, "异常测试,商品盘点,搜索过滤,一级分类ID输入别的Group的一级分类值,断言失败");
		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockAbnormalTestCase03() {
		ReporterCSS.title("测试点: 异常测试,商品盘点,搜索过滤,一级分类ID输入删除的一级分类ID");
		try {
			String category1_id = openCategoryService.createCategory1(StringUtil.getRandomString(6));
			Assert.assertNotEquals(category1_id, null);
			boolean result = openCategoryService.deleteCategory1(category1_id);
			Assert.assertEquals(result, true, "删除一级分类ID失败");

			OpenStockFilterParam openStockFilterParam = new OpenStockFilterParam();
			openStockFilterParam.setCategory1_id(category1_id);

			List<OpenStockBean> openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertEquals(openStockList, null, "异常测试,商品盘点,搜索过滤,一级分类ID输入删除的一级分类ID");
		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockAbnormalTestCase04() {
		ReporterCSS.title("测试点: 异常测试,商品盘点,搜索过滤,二级分类ID输入非法值");
		try {
			OpenStockFilterParam openStockFilterParam = new OpenStockFilterParam();
			openStockFilterParam.setCategory2_id("B");

			List<OpenStockBean> openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertEquals(openStockList, null, "异常测试,商品盘点,搜索过滤,二级分类ID输入非法值,断言失败");
		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockAbnormalTestCase05() {
		ReporterCSS.title("测试点: 异常测试,商品盘点,搜索过滤,二级分类ID输入别的Group的二级分类值");
		try {
			OpenStockFilterParam openStockFilterParam = new OpenStockFilterParam();
			openStockFilterParam.setCategory2_id("B436");

			List<OpenStockBean> openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertEquals(openStockList, null, "异常测试,商品盘点,搜索过滤,二级分类ID输入别的Group的二级分类值,断言失败");
		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockAbnormalTestCase06() {
		ReporterCSS.title("测试点: 异常测试,商品盘点,搜索过滤,二级分类ID输入删除的二级分类ID");
		try {
			String category1_id = openCategoryService.createCategory1(StringUtil.getRandomString(6));
			Assert.assertNotEquals(category1_id, null, "新建一级分类失败");

			String category2_id = openCategoryService.createCategory2(category1_id, StringUtil.getRandomString(6));
			Assert.assertNotEquals(category2_id, null, "新建二级分类失败");

			boolean result = openCategoryService.deleteCategory2(category2_id);
			Assert.assertEquals(result, true, "删除二级分类ID失败");

			result = openCategoryService.deleteCategory1(category1_id);
			Assert.assertEquals(result, true, "删除一级分类ID失败");

			OpenStockFilterParam openStockFilterParam = new OpenStockFilterParam();
			openStockFilterParam.setCategory2_id(category2_id);

			List<OpenStockBean> openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertEquals(openStockList, null, "异常测试,商品盘点,搜索过滤,二级分类ID输入删除的二级分类ID,断言失败");
		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockAbnormalTestCase07() {
		ReporterCSS.title("测试点: 异常测试,商品盘点,搜索过滤,状态值输入非法值");
		try {
			OpenStockFilterParam openStockFilterParam = new OpenStockFilterParam();
			openStockFilterParam.setStatus("a");

			List<OpenStockBean> openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertEquals(openStockList, null, "异常测试,商品盘点,搜索过滤,状态值输入非法值,断言失败");
		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockAbnormalTestCase08() {
		ReporterCSS.title("测试点: 异常测试,商品盘点,搜索过滤,状态值输入非候选值");
		try {
			OpenStockFilterParam openStockFilterParam = new OpenStockFilterParam();
			openStockFilterParam.setStatus("4");

			List<OpenStockBean> openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertEquals(openStockList, null, "异常测试,商品盘点,搜索过滤,状态值输入非候选值,断言失败");
		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockAbnormalTestCase09() {
		ReporterCSS.title("测试点: 异常测试,商品盘点,搜索过滤,limit值输入非候选值");
		try {
			OpenStockFilterParam openStockFilterParam = new OpenStockFilterParam();
			openStockFilterParam.setLimit("a");

			List<OpenStockBean> openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertEquals(openStockList, null, "异常测试,商品盘点,搜索过滤,limit输入非候选值,断言失败");
		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockAbnormalTestCase10() {
		ReporterCSS.title("测试点: 异常测试,商品盘点,搜索过滤,limit值输入非候选值");
		try {
			OpenStockFilterParam openStockFilterParam = new OpenStockFilterParam();
			openStockFilterParam.setLimit("-10");

			List<OpenStockBean> openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertEquals(openStockList, null, "异常测试,商品盘点,搜索过滤,limit输入非候选值,断言失败");
		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockAbnormalTestCase11() {
		ReporterCSS.title("测试点: 异常测试,商品盘点,搜索过滤,offset值输入非候选值");
		try {
			OpenStockFilterParam openStockFilterParam = new OpenStockFilterParam();
			openStockFilterParam.setOffset("a");

			List<OpenStockBean> openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertEquals(openStockList, null, "异常测试,商品盘点,搜索过滤,offset输入非候选值,断言失败");
		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockAbnormalTestCase12() {
		ReporterCSS.title("测试点: 异常测试,商品盘点,搜索过滤,offset值输入非候选值");
		try {
			OpenStockFilterParam openStockFilterParam = new OpenStockFilterParam();
			openStockFilterParam.setOffset("-10");

			List<OpenStockBean> openStockList = openStockService.queryStock(openStockFilterParam);
			Assert.assertEquals(openStockList, null, "异常测试,商品盘点,搜索过滤,offset输入非候选值,断言失败");
		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockAbnormalTestCase13() {
		ReporterCSS.title("测试点: 异常测试,商品盘点,修改库存,SPU id 输入为空");
		try {
			boolean result = openStockService.updateStock("", "0", "");
			Assert.assertEquals(result, false, "异常测试,商品盘点,修改库存,SPU id 输入为空,断言失败");
		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockAbnormalTestCase14() {
		ReporterCSS.title("测试点: 异常测试,商品盘点,修改库存,SPU id 输入别的站点的SPU ID");
		try {
			boolean result = openStockService.updateStock("C115348", "0", "");
			Assert.assertEquals(result, false, "异常测试,商品盘点,修改库存,SPU id 输入别的站点的SPU ID,断言失败");
		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockAbnormalTestCase15() {
		ReporterCSS.title("测试点: 异常测试,商品盘点,修改库存,库存数输入非法值,断言失败");
		try {
			List<OpenStockBean> openStockList = openStockService.queryStock(new OpenStockFilterParam());
			Assert.assertNotEquals(openStockList, null, "商品盘点,搜索过滤失败");

			String spu_id = openStockList.get(0).getSpu_id();

			boolean result = openStockService.updateStock(spu_id, "A", "");
			Assert.assertEquals(result, false, "异常测试,商品盘点,修改库存,库存数数非法值,断言失败,断言失败");

		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockAbnormalTestCase16() {
		ReporterCSS.title("测试点: 异常测试,商品盘点,修改库存,库存数输入负数,断言失败");
		try {
			List<OpenStockBean> openStockList = openStockService.queryStock(new OpenStockFilterParam());
			Assert.assertNotEquals(openStockList, null, "商品盘点,搜索过滤失败");

			String spu_id = openStockList.get(0).getSpu_id();

			boolean result = openStockService.updateStock(spu_id, "-10", "");
			Assert.assertEquals(result, false, "异常测试,商品盘点,修改库存,库存数输入负数,断言失败");

		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void openStockAbnormalTestCase17() {
		ReporterCSS.title("测试点: 异常测试,商品盘点,修改库存,库存数输入过长小数,断言失败");
		try {
			List<OpenStockBean> openStockList = openStockService.queryStock(new OpenStockFilterParam());
			Assert.assertNotEquals(openStockList, null, "商品盘点,搜索过滤失败");

			String spu_id = openStockList.get(0).getSpu_id();

			boolean result = openStockService.updateStock(spu_id, "1.234", "");
			Assert.assertEquals(result, false, "异常测试,商品盘点,修改库存,库存数输入过长小数,断言失败");

		} catch (Exception e) {
			logger.error("商品盘点,搜索过滤遇到错误: ", e);
			Assert.fail("商品盘点,搜索过滤遇到错误: ", e);
		}
	}

}
