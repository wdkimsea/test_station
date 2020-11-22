package cn.guanmai.open.Product.abnormal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author liming
 * @date 2019年12月18日
 * @time 下午4:07:35
 * @des TODO
 */

public class Category1AbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(Category1AbnormalTest.class);
	private OpenCategoryService categoryService;
	private String category1_id;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		categoryService = new OpenCategoryServiceImpl(access_token);
	}

	@BeforeMethod
	public void beforeMethod() {
		category1_id = null;
	}

	@Test
	public void category1AbnormalTestCase01() {
		ReporterCSS.title("测试点: 异常测试,新建一级分类,不传入名称,断言失败");
		try {
			category1_id = categoryService.createCategory1("");
			Assert.assertEquals(category1_id, null, "异常测试,新建一级分类,不传入名称,断言失败");
		} catch (Exception e) {
			logger.error("一级分类测试遇到错误: ", e);
			Assert.fail("一级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void category1AbnormalTestCase02() {
		ReporterCSS.title("测试点: 异常测试,新建一级分类,传入过长字符,断言失败");
		try {
			category1_id = categoryService.createCategory1(StringUtil.getRandomString(11));
			Assert.assertEquals(category1_id, null, "异常测试,新建一级分类,传入过长字符,断言失败");
		} catch (Exception e) {
			logger.error("一级分类测试遇到错误: ", e);
			Assert.fail("一级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void category1AbnormalTestCase03() {
		ReporterCSS.title("测试点: 异常测试,修改一级分类,ID传入为空,断言失败");
		try {
			boolean result = categoryService.updateCategory1("", StringUtil.getRandomString(6));
			Assert.assertEquals(result, false, "异常测试,修改一级分类,ID传入为空,断言失败");
		} catch (Exception e) {
			logger.error("一级分类测试遇到错误: ", e);
			Assert.fail("一级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void category1AbnormalTestCase04() {
		ReporterCSS.title("测试点: 异常测试,修改一级分类,ID传入为错误值,断言失败");
		try {
			boolean result = categoryService.updateCategory1("A1", StringUtil.getRandomString(6));
			Assert.assertEquals(result, false, "异常测试,修改一级分类,ID传入为空,断言失败");
		} catch (Exception e) {
			logger.error("一级分类测试遇到错误: ", e);
			Assert.fail("一级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void category1AbnormalTestCase05() {
		ReporterCSS.title("测试点: 异常测试,修改一级分类,ID传入别的Group的一级分类ID值,断言失败");
		try {
			boolean result = categoryService.updateCategory1("A238", StringUtil.getRandomString(6));
			Assert.assertEquals(result, false, "异常测试,修改一级分类,ID传入别的Group的一级分类ID值,断言失败");
		} catch (Exception e) {
			logger.error("一级分类测试遇到错误: ", e);
			Assert.fail("一级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void category1AbnormalTestCase06() {
		ReporterCSS.title("测试点: 异常测试,修改一级分类,新名称输入为空,断言失败");
		try {
			category1_id = categoryService.createCategory1(StringUtil.getRandomString(6));
			Assert.assertNotEquals(category1_id, null, "新建一级分类失败");
			boolean result = categoryService.updateCategory1(category1_id, "");
			Assert.assertEquals(result, false, "异常测试,修改一级分类,新名称输入为空,断言失败");
		} catch (Exception e) {
			logger.error("一级分类测试遇到错误: ", e);
			Assert.fail("一级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void category1AbnormalTestCase07() {
		ReporterCSS.title("测试点: 异常测试,修改一级分类,新名称输入过长字符,断言失败");
		try {
			category1_id = categoryService.createCategory1(StringUtil.getRandomString(6));
			Assert.assertNotEquals(category1_id, null, "新建一级分类失败");
			boolean result = categoryService.updateCategory1(category1_id, StringUtil.getRandomString(11));
			Assert.assertEquals(result, false, "异常测试,修改一级分类,新名称输入过长字符,断言失败");
		} catch (Exception e) {
			logger.error("一级分类测试遇到错误: ", e);
			Assert.fail("一级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void category1AbnormalTestCase08() {
		ReporterCSS.title("测试点: 异常测试,删除一级分类,ID输入为空,断言失败");
		try {
			boolean result = categoryService.deleteCategory1("");
			Assert.assertEquals(result, false, "异常测试,删除一级分类,ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("一级分类测试遇到错误: ", e);
			Assert.fail("一级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void category1AbnormalTestCase09() {
		ReporterCSS.title("测试点: 异常测试,删除一级分类,ID输入为别的站点的ID,断言失败");
		try {
			boolean result = categoryService.deleteCategory1("A238");
			Assert.assertEquals(result, false, "异常测试,删除一级分类,ID输入为别的站点的ID,断言失败");
		} catch (Exception e) {
			logger.error("一级分类测试遇到错误: ", e);
			Assert.fail("一级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void afterMethod() {
		try {
			if (category1_id != null) {
				boolean result = categoryService.deleteCategory1(category1_id);
				Assert.assertEquals(result, true, "后置处理,删除一级分类失败");
			}
		} catch (Exception e) {
			logger.error("后置处理,恢复环境遇到错误: ", e);
			Assert.fail("后置处理,恢复环境遇到错误: ", e);
		}
	}

}
