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
 * @time 下午4:26:10
 * @des TODO
 */

public class Category2AbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(Category2Test.class);
	private OpenCategoryService categoryService;
	private String category1_id;
	private String category2_id;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		categoryService = new OpenCategoryServiceImpl(access_token);

		try {
			category1_id = categoryService.createCategory1(StringUtil.getRandomString(6));
			Assert.assertNotEquals(category1_id, null, "创建一级分类失败");
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		category2_id = null;
	}

	@Test
	public void category2AbnormalTestCase01() {
		ReporterCSS.title("测试点: 异常测试,新建二级分类,一级分类ID传入空值,断言失败");
		try {
			category2_id = categoryService.createCategory2("", StringUtil.getRandomString(6));
			Assert.assertEquals(category2_id, null, "异常测试,新建二级分类,一级分类ID传入空值,断言失败");
		} catch (Exception e) {
			logger.error("二级分类测试遇到错误: ", e);
			Assert.fail("二级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void category2AbnormalTestCase02() {
		ReporterCSS.title("测试点: 异常测试,新建二级分类,一级分类ID传入错误值,断言失败");
		try {
			category2_id = categoryService.createCategory2("A", StringUtil.getRandomString(6));
			Assert.assertEquals(category2_id, null, "异常测试,新建二级分类,一级分类ID传入错误值,断言失败");
		} catch (Exception e) {
			logger.error("二级分类测试遇到错误: ", e);
			Assert.fail("二级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void category2AbnormalTestCase03() {
		ReporterCSS.title("测试点: 异常测试,新建二级分类,二级分类名称输入为空,断言失败");
		try {
			category2_id = categoryService.createCategory2(category1_id, "");
			Assert.assertEquals(category2_id, null, "异常测试,新建二级分类,二级分类名称输入为空,断言失败");
		} catch (Exception e) {
			logger.error("二级分类测试遇到错误: ", e);
			Assert.fail("二级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void category2AbnormalTestCase04() {
		ReporterCSS.title("测试点: 异常测试,新建二级分类,二级分类名称输入过长字符,断言失败");
		try {
			category2_id = categoryService.createCategory2(category1_id, StringUtil.getRandomString(11));
			Assert.assertEquals(category2_id, null, "异常测试,新建二级分类,二级分类名称输入过长字符,断言失败");
		} catch (Exception e) {
			logger.error("二级分类测试遇到错误: ", e);
			Assert.fail("二级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void category2AbnormalTestCase05() {
		ReporterCSS.title("测试点: 异常测试,修改二级分类,二级分类ID输入为空,断言失败");
		try {
			boolean result = categoryService.updateCategory2("", StringUtil.getRandomString(6));
			Assert.assertEquals(result, false, "异常测试,修改二级分类,二级分类ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("二级分类测试遇到错误: ", e);
			Assert.fail("二级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void category2AbnormalTestCase06() {
		ReporterCSS.title("测试点: 异常测试,修改二级分类,二级分类ID输入错误值,断言失败");
		try {
			boolean result = categoryService.updateCategory2("B1", StringUtil.getRandomString(6));
			Assert.assertEquals(result, false, "异常测试,修改二级分类,二级分类ID输入错误值,断言失败");
		} catch (Exception e) {
			logger.error("二级分类测试遇到错误: ", e);
			Assert.fail("二级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void category2AbnormalTestCase07() {
		ReporterCSS.title("测试点: 异常测试,修改二级分类,二级分类ID输入别的Group下的二级分类ID,断言失败");
		try {
			boolean result = categoryService.updateCategory2("B436", StringUtil.getRandomString(6));
			Assert.assertEquals(result, false, "异常测试,修改二级分类,二级分类ID输入别的Group下的二级分类ID,断言失败");
		} catch (Exception e) {
			logger.error("二级分类测试遇到错误: ", e);
			Assert.fail("二级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void category2AbnormalTestCase08() {
		ReporterCSS.title("测试点: 异常测试,修改二级分类,新的二级名称输入为空,断言失败");
		try {
			category2_id = categoryService.createCategory2(category1_id, StringUtil.getRandomString(6));
			Assert.assertNotEquals(category2_id, null, "新建二级分类失败");

			boolean result = categoryService.updateCategory2(category2_id, "");
			Assert.assertEquals(result, false, "异常测试,修改二级分类,新的二级名称输入为空,断言失败");
		} catch (Exception e) {
			logger.error("二级分类测试遇到错误: ", e);
			Assert.fail("二级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void category2AbnormalTestCase09() {
		ReporterCSS.title("测试点: 异常测试,修改二级分类,新的二级名称输入过长值,断言失败");
		try {
			category2_id = categoryService.createCategory2(category1_id, StringUtil.getRandomString(6));
			Assert.assertNotEquals(category2_id, null, "新建二级分类失败");

			boolean result = categoryService.updateCategory2(category2_id, StringUtil.getRandomString(11));
			Assert.assertEquals(result, false, "异常测试,修改二级分类,新的二级名称输入过长值,断言失败");
		} catch (Exception e) {
			logger.error("二级分类测试遇到错误: ", e);
			Assert.fail("二级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void category2AbnormalTestCase11() {
		ReporterCSS.title("测试点: 异常测试,删除二级分类,二级分类ID传入空值,断言失败");
		try {
			boolean result = categoryService.deleteCategory2("");
			Assert.assertEquals(result, false, "异常测试,删除二级分类,二级分类ID传入空值,断言失败");
		} catch (Exception e) {
			logger.error("二级分类测试遇到错误: ", e);
			Assert.fail("二级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void category2AbnormalTestCase12() {
		ReporterCSS.title("测试点: 异常测试,删除二级分类,二级分类ID传入错误值,断言失败");
		try {
			boolean result = categoryService.deleteCategory2("B1");
			Assert.assertEquals(result, false, "异常测试,删除二级分类,二级分类ID传入错误值,断言失败");
		} catch (Exception e) {
			logger.error("二级分类测试遇到错误: ", e);
			Assert.fail("二级分类测试遇到错误: ", e);
		}
	}

	@Test
	public void category2AbnormalTestCase13() {
		ReporterCSS.title("测试点: 异常测试,删除二级分类,二级分类ID传入别的Group的二级分类ID,断言失败");
		try {
			boolean result = categoryService.deleteCategory2("B436");
			Assert.assertEquals(result, false, "异常测试,删除二级分类,二级分类ID传入别的Group的二级分类ID,断言失败");
		} catch (Exception e) {
			logger.error("二级分类测试遇到错误: ", e);
			Assert.fail("二级分类测试遇到错误: ", e);
		}
	}

	@AfterMethod
	public void afterMethod() {
		if (category2_id != null) {
			try {
				boolean result = categoryService.deleteCategory2(category2_id);
				Assert.assertEquals(result, true, "后置处理,删除二级分类失败");
			} catch (Exception e) {
				logger.error("后置处理,删除二级分类遇到错误: ", e);
				Assert.fail("后置处理,删除二级分类遇到错误: ", e);
			}
		}
	}

	@AfterClass
	public void afterTest() {
		try {
			boolean result = categoryService.deleteCategory1(category1_id);
			Assert.assertEquals(result, true, "后置处理,删除一级分类失败");
		} catch (Exception e) {
			logger.error("后置处理,删除一级分类遇到错误: ", e);
			Assert.fail("后置处理,删除一级分类遇到错误: ", e);
		}
	}

}
