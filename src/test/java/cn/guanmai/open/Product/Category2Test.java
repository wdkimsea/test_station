package cn.guanmai.open.Product;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.product.OpenCategory2Bean;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author liming
 * @date 2019年12月18日
 * @time 下午2:44:51
 * @des TODO
 */

public class Category2Test extends AccessToken {
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
	public void category2TestCase01() {
		ReporterCSS.title("测试点: 新建二级分类");
		try {
			String category2_name = StringUtil.getRandomString(6);
			category2_id = categoryService.createCategory2(category1_id, category2_name);
			Assert.assertNotEquals(category2_id, null, "新建二级分类失败");

			List<OpenCategory2Bean> openCategory2List = categoryService.getCategory2List(category1_id, null, null);
			Assert.assertNotEquals(openCategory2List, null, "获取二级分类列表失败");

			OpenCategory2Bean openCategory2 = openCategory2List.stream().filter(c -> c.getId().equals(category2_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(openCategory2, null, "新建的二级分类在二级分类列表没有找到");

			Assert.assertEquals(openCategory2.getName(), category2_name, "新建二级分类填写的名称与查询到的不一致");
		} catch (Exception e) {
			logger.error("新建二级分类遇到错误: ", e);
			Assert.fail("新建二级分类遇到错误: ", e);
		}
	}

	@Test
	public void category2TestCase02() {
		ReporterCSS.title("测试点: 新建二级分类,使用已经存在的二级分类名称,断言创建失败");
		String temp_category2_id = null;
		try {
			String temp_category2_name = StringUtil.getRandomString(6);
			temp_category2_id = categoryService.createCategory2(category1_id, temp_category2_name);
			Assert.assertNotEquals(temp_category2_name, null, "新建二级分类失败");

			category2_id = categoryService.createCategory2(category1_id, temp_category2_name);
			Assert.assertEquals(category2_id, null, "新建二级分类,使用已经存在的二级分类名称,断言创建失败");
		} catch (Exception e) {
			logger.error("新建二级分类遇到错误: ", e);
			Assert.fail("新建二级分类遇到错误: ", e);
		} finally {
			if (temp_category2_id != null) {
				try {
					boolean result = categoryService.deleteCategory2(temp_category2_id);
					Assert.assertEquals(result, true, "后置处理,删除二级分类失败");
				} catch (Exception e) {
					logger.error("后置处理,删除二级分类遇到错误: ", e);
					Assert.fail("后置处理,删除二级分类遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void category2TestCase03() {
		ReporterCSS.title("测试点: 修改二级分类");
		try {
			String category2_name = StringUtil.getRandomString(6);
			category2_id = categoryService.createCategory2(category1_id, category2_name);
			Assert.assertNotEquals(category2_id, null, "新建二级分类失败");

			String new_category2_name = StringUtil.getRandomString(6);
			boolean reuslt = categoryService.updateCategory2(category2_id, new_category2_name);
			Assert.assertEquals(reuslt, true, "修改二级分类失败");

			List<OpenCategory2Bean> openCategory2List = categoryService.getCategory2List(category1_id, null, null);
			Assert.assertNotEquals(openCategory2List, null, "获取二级分类列表失败");

			OpenCategory2Bean openCategory2 = openCategory2List.stream().filter(c -> c.getId().equals(category2_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(openCategory2, null, "新建的二级分类在二级分类列表没有找到");

			Assert.assertEquals(openCategory2.getName(), new_category2_name, "修改二级分类填写的名称与查询到的不一致");
		} catch (Exception e) {
			logger.error("修改二级分类遇到错误: ", e);
			Assert.fail("修改二级分类遇到错误: ", e);
		}
	}

	@Test
	public void category2TestCase04() {
		ReporterCSS.title("测试点: 修改二级分类,使用存在的二级分类名称,断言修改失败");
		String temp_category2_id = null;
		try {
			String category2_name = StringUtil.getRandomString(6);
			category2_id = categoryService.createCategory2(category1_id, category2_name);
			Assert.assertNotEquals(category2_id, null, "新建二级分类失败");

			String temp_category2_name = StringUtil.getRandomString(6);
			temp_category2_id = categoryService.createCategory2(category1_id, temp_category2_name);
			Assert.assertNotEquals(temp_category2_id, null, "新建二级分类失败");

			boolean reuslt = categoryService.updateCategory2(category2_id, temp_category2_name);
			Assert.assertEquals(reuslt, false, "修改二级分类,使用存在的二级分类名称,断言修改失败");

		} catch (Exception e) {
			logger.error("修改二级分类遇到错误: ", e);
			Assert.fail("修改二级分类遇到错误: ", e);
		} finally {
			if (temp_category2_id != null) {
				try {
					boolean result = categoryService.deleteCategory2(temp_category2_id);
					Assert.assertEquals(result, true, "后置处理,删除二级分类失败");
				} catch (Exception e) {
					logger.error("后置处理,删除二级分类遇到错误: ", e);
					Assert.fail("后置处理,删除二级分类遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void category2TestCase05() {
		ReporterCSS.title("测试点: 删除二级分类");
		try {
			String category2_name = StringUtil.getRandomString(6);
			String category2_id = categoryService.createCategory2(category1_id, category2_name);
			Assert.assertNotEquals(category2_id, null, "新建二级分类失败");

			boolean result = categoryService.deleteCategory2(category2_id);
			Assert.assertEquals(result, true, "删除二级分类失败");

			List<OpenCategory2Bean> openCategory2List = categoryService.getCategory2List(category1_id, null, null);
			Assert.assertNotEquals(openCategory2List, null, "获取二级分类列表失败");

			OpenCategory2Bean openCategory2 = openCategory2List.stream().filter(c -> c.getId().equals(category2_id))
					.findAny().orElse(null);
			Assert.assertEquals(openCategory2, null, "删除的二级分类还存在二级分类列表");
		} catch (Exception e) {
			logger.error("删除二级分类遇到错误: ", e);
			Assert.fail("删除二级分类遇到错误: ", e);
		}
	}

	@Test
	public void category2TestCase06() {
		ReporterCSS.title("测试点: 删除有子级的二级分类,断言删除失败");
		String pinlei_id = null;
		try {
			String category2_name = StringUtil.getRandomString(6);
			category2_id = categoryService.createCategory2(category1_id, category2_name);
			Assert.assertNotEquals(category2_id, null, "新建二级分类失败");

			String pinlei_name = StringUtil.getRandomString(6);
			pinlei_id = categoryService.createPinlei(category2_id, pinlei_name);
			Assert.assertNotEquals(pinlei_id, null, "新建品类失败");

			boolean result = categoryService.deleteCategory2(category2_id);
			Assert.assertEquals(result, false, "删除有子级的二级分类,断言删除失败");
		} catch (Exception e) {
			logger.error("删除二级分类遇到错误: ", e);
			Assert.fail("删除二级分类遇到错误: ", e);
		} finally {
			try {
				boolean result = categoryService.deletePinlei(pinlei_id);
				Assert.assertEquals(result, true, "后置处理,删除品类分类失败");
			} catch (Exception e) {
				logger.error("后置处理,删除品类分类遇到错误: ", e);
				Assert.fail("后置处理,删除品类分类遇到错误: ", e);
			}
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
