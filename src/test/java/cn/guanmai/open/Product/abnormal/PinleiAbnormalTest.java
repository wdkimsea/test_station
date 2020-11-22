package cn.guanmai.open.Product.abnormal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.open.Product.Category2Test;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author liming
 * @date 2019年12月18日
 * @time 下午5:09:02
 * @des TODO
 */

public class PinleiAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(Category2Test.class);
	private OpenCategoryService categoryService;
	private String category1_id;
	private String category2_id;
	private String pinlei_id;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		categoryService = new OpenCategoryServiceImpl(access_token);

		try {
			category1_id = categoryService.createCategory1(StringUtil.getRandomString(6));
			Assert.assertNotEquals(category1_id, null, "创建一级分类失败");

			category2_id = categoryService.createCategory2(category1_id, StringUtil.getRandomString(6));
			Assert.assertNotEquals(category1_id, null, "创建二级分类失败");
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		pinlei_id = null;
	}

	@Test
	public void pinleiAbnormalTestCase01() {
		ReporterCSS.title("测试点: 异常测试,新建品类分类,二级分类ID输入为空,断言失败");
		try {
			pinlei_id = categoryService.createPinlei("", StringUtil.getRandomString(6));
			Assert.assertEquals(pinlei_id, null, "异常测试,新建品类分类,二级分类ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("品类分类测试遇到错误: ", e);
			Assert.fail("品类分类测试遇到错误: ", e);
		}
	}

	@Test
	public void pinleiAbnormalTestCase02() {
		ReporterCSS.title("测试点: 异常测试,新建品类分类,二级分类ID输入为错误值,断言失败");
		try {
			pinlei_id = categoryService.createPinlei("B1", StringUtil.getRandomString(6));
			Assert.assertEquals(pinlei_id, null, "异常测试,新建品类分类,二级分类ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("品类分类测试遇到错误: ", e);
			Assert.fail("品类分类测试遇到错误: ", e);
		}
	}

	@Test
	public void pinleiAbnormalTestCase03() {
		ReporterCSS.title("测试点: 异常测试,新建品类分类,二级分类ID输入为别的Group下的值,断言失败");
		try {
			pinlei_id = categoryService.createPinlei("B436", StringUtil.getRandomString(6));
			Assert.assertEquals(pinlei_id, null, "异常测试,新建品类分类,二级分类ID输入为别的Group下的值,断言失败");
		} catch (Exception e) {
			logger.error("品类分类测试遇到错误: ", e);
			Assert.fail("品类分类测试遇到错误: ", e);
		}
	}

	@Test
	public void pinleiAbnormalTestCase04() {
		ReporterCSS.title("测试点: 异常测试,新建品类分类,品类名称输入为空,断言失败");
		try {
			pinlei_id = categoryService.createPinlei(category2_id, "");
			Assert.assertEquals(pinlei_id, null, "异常测试,新建品类分类,品类名称输入为空,断言失败");
		} catch (Exception e) {
			logger.error("品类分类测试遇到错误: ", e);
			Assert.fail("品类分类测试遇到错误: ", e);
		}
	}

	@Test
	public void pinleiAbnormalTestCase05() {
		ReporterCSS.title("测试点: 异常测试,新建品类分类,品类名称输入为过长字符,断言失败");
		try {
			pinlei_id = categoryService.createPinlei(category2_id, StringUtil.getRandomString(11));
			Assert.assertEquals(pinlei_id, null, "异常测试,新建品类分类,品类名称输入为过长字符,断言失败");
		} catch (Exception e) {
			logger.error("品类分类测试遇到错误: ", e);
			Assert.fail("品类分类测试遇到错误: ", e);
		}
	}

	@Test
	public void pinleiAbnormalTestCase06() {
		ReporterCSS.title("测试点: 异常测试,修改品类分类,品类ID输入为空,断言失败");
		try {
			boolean result = categoryService.updatePinlei("", StringUtil.getRandomString(6));
			Assert.assertEquals(result, false, "异常测试,修改品类分类,品类ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("品类分类测试遇到错误: ", e);
			Assert.fail("品类分类测试遇到错误: ", e);
		}
	}

	@Test
	public void pinleiAbnormalTestCase07() {
		ReporterCSS.title("测试点: 异常测试,修改品类分类,品类ID输入错误值,断言失败");
		try {
			boolean result = categoryService.updatePinlei("P1", StringUtil.getRandomString(6));
			Assert.assertEquals(result, false, "异常测试,修改品类分类,品类ID输入错误值,断言失败");
		} catch (Exception e) {
			logger.error("品类分类测试遇到错误: ", e);
			Assert.fail("品类分类测试遇到错误: ", e);
		}
	}

	@Test
	public void pinleiAbnormalTestCase08() {
		ReporterCSS.title("测试点: 异常测试,修改品类分类,品类ID输入别的Group下的品类ID,断言失败");
		try {
			boolean result = categoryService.updatePinlei("P04410", StringUtil.getRandomString(6));
			Assert.assertEquals(result, false, "异常测试,修改品类分类,品类ID输入别的Group下的品类ID,断言失败");
		} catch (Exception e) {
			logger.error("品类分类测试遇到错误: ", e);
			Assert.fail("品类分类测试遇到错误: ", e);
		}
	}

	@Test
	public void pinleiAbnormalTestCase09() {
		ReporterCSS.title("测试点: 异常测试,修改品类分类,新品类名称输入为空,断言失败");
		try {
			pinlei_id = categoryService.createPinlei(category2_id, StringUtil.getRandomString(6));
			Assert.assertNotEquals(pinlei_id, null, "新建品类失败");
			boolean result = categoryService.updatePinlei(pinlei_id, "");
			Assert.assertEquals(result, false, "异常测试,修改品类分类,新品类名称输入为空,断言失败");
		} catch (Exception e) {
			logger.error("品类分类测试遇到错误: ", e);
			Assert.fail("品类分类测试遇到错误: ", e);
		}
	}

	@Test
	public void pinleiAbnormalTestCase10() {
		ReporterCSS.title("测试点: 异常测试,修改品类分类,新品类名称输入过长字符,断言失败");
		try {
			pinlei_id = categoryService.createPinlei(category2_id, StringUtil.getRandomString(6));
			Assert.assertNotEquals(pinlei_id, null, "新建品类失败");
			boolean result = categoryService.updatePinlei(pinlei_id, StringUtil.getRandomString(11));
			Assert.assertEquals(result, false, "异常测试,修改品类分类,新品类名称输入过长字符,断言失败");
		} catch (Exception e) {
			logger.error("品类分类测试遇到错误: ", e);
			Assert.fail("品类分类测试遇到错误: ", e);
		}
	}

	@Test
	public void pinleiAbnormalTestCase11() {
		ReporterCSS.title("测试点: 异常测试,删除品类分类,品类ID输入为空,断言失败");
		try {
			boolean result = categoryService.deletePinlei("");
			Assert.assertEquals(result, false, "异常测试,删除品类分类,品类ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("品类分类测试遇到错误: ", e);
			Assert.fail("品类分类测试遇到错误: ", e);
		}
	}

	@Test
	public void pinleiAbnormalTestCase12() {
		ReporterCSS.title("测试点: 异常测试,删除品类分类,品类ID输入为错误值,断言失败");
		try {
			boolean result = categoryService.deletePinlei("P1");
			Assert.assertEquals(result, false, "异常测试,删除品类分类,品类ID输入为错误值,断言失败");
		} catch (Exception e) {
			logger.error("品类分类测试遇到错误: ", e);
			Assert.fail("品类分类测试遇到错误: ", e);
		}
	}

	@Test
	public void pinleiAbnormalTestCase13() {
		ReporterCSS.title("测试点: 异常测试,删除品类分类,品类ID输入别的Group下的品类ID,断言失败");
		try {
			boolean result = categoryService.deletePinlei("P04410");
			Assert.assertEquals(result, false, "异常测试,删除品类分类,品类ID输入别的Group下的品类ID,断言失败");
		} catch (Exception e) {
			logger.error("品类分类测试遇到错误: ", e);
			Assert.fail("品类分类测试遇到错误: ", e);
		}
	}

	@AfterMethod
	public void afterMethod() {
		if (pinlei_id != null) {
			try {
				boolean result = categoryService.deletePinlei(pinlei_id);
				Assert.assertEquals(result, true, "后置处理,删除品类遇到错误");
			} catch (Exception e) {
				logger.error("后置处理,删除品类遇到错误: ", e);
				Assert.fail("后置处理,删除品类遇到错误: ", e);
			}
		}
	}

	@AfterClass
	public void afterClass() {
		try {
			boolean result = categoryService.deleteCategory2(category2_id);
			Assert.assertEquals(result, true, "后置处理,删除二级分类遇到错误");

			result = categoryService.deleteCategory1(category1_id);
			Assert.assertEquals(result, true, "后置处理,删除一级分类遇到错误");
		} catch (Exception e) {
			logger.error("后置处理,删除一、二级分类遇到错误: ", e);
			Assert.fail("后置处理,删除一、二级分类遇到错误: ", e);
		}
	}

}
