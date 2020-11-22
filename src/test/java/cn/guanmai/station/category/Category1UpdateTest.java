package cn.guanmai.station.category;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.category.Category1Bean;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Nov 5, 2018 3:56:51 PM 
* @des 修改一级分类
* @version 1.0 
*/
public class Category1UpdateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(Category1CreateTest.class);
	private String id;
	private String id_2;

	private CategoryService categoryService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		categoryService = new CategoryServiceImpl(headers);
	}

	@BeforeMethod
	public void beforeMethod() {
		id = null;
		id_2 = null;
	}

	/**
	 * 修改一级分类名称
	 * 
	 */
	@Test
	public void category1UpdateTestCase01() {
		try {
			Reporter.log("修改商品一级分类名称测试", 2);
			String name = StringUtil.getRandomString(6);
			Category1Bean category = categoryService.getCategory1ByName(name);
			if (category == null) {
				category = new Category1Bean(name, 1);
				id = categoryService.createCategory1(category);
				Assert.assertNotEquals(id, null, "新建一级分类失败");

				name = StringUtil.getRandomString(6);
				category = new Category1Bean(id, name, 1, 0);
				boolean result = categoryService.updateCategory1(category);
				Assert.assertEquals(result, true, "修改一级分类失败");

				Category1Bean category_2 = categoryService.getCategory1ById(id);
				Assert.assertEquals(name.equals(category_2.getName()), true, "验证一级分类名称是否修改成功,验证失败");
			}
		} catch (Exception e) {
			logger.error("修改一级分类过程中遇到错误", e);
			Assert.fail("修改一级分类过程中遇到错误", e);
		}
	}

	/**
	 * 修改一级分类icon
	 * 
	 */
	@Test
	public void category1UpdateTestCase02() {
		try {
			Reporter.log("修改商品一级分类icon测试", 2);
			String name = StringUtil.getRandomString(6);
			Category1Bean category = categoryService.getCategory1ByName(name);
			if (category == null) {
				category = new Category1Bean(name, 1);
				id = categoryService.createCategory1(category);
				Assert.assertNotEquals(id, null, "新建一级分类失败");

				category = new Category1Bean(id, name, 2, 0);
				boolean result = categoryService.updateCategory1(category);
				Assert.assertEquals(result, true, "修改一级分类失败");

				Category1Bean category_2 = categoryService.getCategory1ById(id);
				Assert.assertEquals(2 == category_2.getIcon(), true, "验证一级分类icon是否修改成功,验证失败");
			}
		} catch (Exception e) {
			logger.error("修改一级分类过程中遇到错误", e);
			Assert.fail("修改一级分类过程中遇到错误", e);
		}

	}

	/**
	 * 修改一级分类名称,使用已经存在的其他分类名称
	 * 
	 */
	@Test
	public void category1UpdateTestCase03() {
		try {
			Reporter.log("修改一级分类名称,使用已经存在的其他分类名称", 2);
			String name = StringUtil.getRandomString(6);
			Category1Bean category = categoryService.getCategory1ByName(name);
			if (category == null) {
				category = new Category1Bean(name, 1);
				id = categoryService.createCategory1(category);
				Assert.assertNotEquals(id, null, "新建一级分类失败");

				String name_2 = StringUtil.getRandomString(6);
				Category1Bean category_2 = new Category1Bean(name_2, 1);
				id_2 = categoryService.createCategory1(category_2);
				Assert.assertNotEquals(id_2, null, "新建一级分类失败");

				category = new Category1Bean(id, name_2, 1, 0);
				boolean result = categoryService.updateCategory1(category);
				Assert.assertEquals(result, false, "修改一级分类名称,使用已经存在的其他分类名称,断言失败");
			}
		} catch (Exception e) {
			logger.error("修改一级分类过程中遇到错误", e);
			Assert.fail("修改一级分类过程中遇到错误", e);
		}

	}

	/**
	 * 修改一级分类名称,使用已经删除的其他分类名称
	 * 
	 */
	@Test
	public void categoryUpdateTestCase04() {
		try {
			Reporter.log("修改一级分类名称,使用已经删除的其他分类名称");
			String name = StringUtil.getRandomString(6);
			Category1Bean category = categoryService.getCategory1ByName(name);
			if (category == null) {
				category = new Category1Bean(name, 1);
				id = categoryService.createCategory1(category);
				Assert.assertNotEquals(id, null, "新建一级分类失败");

				String name_2 = StringUtil.getRandomString(6);
				Category1Bean category_2 = new Category1Bean(name_2, 1);
				String id_2 = categoryService.createCategory1(category_2);
				Assert.assertNotEquals(id_2, null, "新建一级分类失败");

				boolean result = categoryService.deleteCategory1ById(id_2);

				category = new Category1Bean(id, name_2, 1, 0);
				result = categoryService.updateCategory1(category);
				Assert.assertEquals(result, true, "修改一级分类名称,使用已经删除的其他分类名称,断言成功");
			}
		} catch (Exception e) {
			logger.error("修改一级分类过程中遇到错误", e);
			Assert.fail("修改一级分类过程中遇到错误", e);
		}

	}

	@AfterMethod
	public void afterMethod() {
		try {
			if (id != null) {
				boolean result = categoryService.deleteCategory1ById(id);
				Assert.assertEquals(result, true, "删除商品一级分类失败");
			}

			if (id_2 != null) {
				boolean result = categoryService.deleteCategory1ById(id_2);
				Assert.assertEquals(result, true, "删除商品一级分类失败");
			}
		} catch (Exception e) {
			logger.error("删除一级分类过程中遇到错误", e);
			Assert.fail("删除一级分类过程中遇到错误", e);
		}

	}

}
