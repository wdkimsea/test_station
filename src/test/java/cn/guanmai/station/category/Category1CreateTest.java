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
* @date Oct 31, 2018 4:11:08 PM 
* @todo TODO
* @version 1.0 
*/
public class Category1CreateTest extends LoginStation {
	private static Logger logger = LoggerFactory.getLogger(Category1CreateTest.class);
	private static Map<String, String> headers;
	private static String id;

	private CategoryService categoryService;

	@BeforeClass
	public void InitData() {
		headers = getStationCookie();
		categoryService = new CategoryServiceImpl(headers);
	}

	@BeforeMethod
	public void beforeMethod() {
		id = null;
	}

	/**
	 * 新建商品一级分类测试
	 * 
	 */
	@Test
	public void category1CreateTestCase01() {
		try {
			Reporter.log("新建商品一级分类测试", 2);
			String name = StringUtil.getRandomString(6);
			Category1Bean category = categoryService.getCategory1ByName(name);
			if (category == null) {
				category = new Category1Bean(name, 1);
				id = categoryService.createCategory1(category);
				Assert.assertNotEquals(id, null, "新建一级分类失败");
			}
		} catch (Exception e) {
			logger.error("新建商品一级分类遇到错误", e);
			Assert.fail("新建商品一级分类遇到错误", e);
		}

	}

	/**
	 * 新建商品一级分类,使用存在的一级分类名称
	 * 
	 */
	@Test
	public void category1CreateTestCase02() {
		try {
			Reporter.log("新建商品一级分类,使用存在的一级分类名称");
			String name = StringUtil.getRandomString(6);
			Category1Bean category = categoryService.getCategory1ByName(name);
			if (category == null) {
				category = new Category1Bean(name, 1);
				id = categoryService.createCategory1(category);
				Assert.assertNotEquals(id, null, "新建一级分类失败");

				String id_2 = categoryService.createCategory1(category);
				Assert.assertEquals(id_2, null, "新建商品一级分类,使用存在的一级分类名称,断言失败");
			}
		} catch (Exception e) {
			logger.error("新建商品一级分类遇到错误", e);
			Assert.fail("新建商品一级分类遇到错误", e);
		}

	}

	/**
	 * 新建商品一级分类,使用删除的一级分类名称
	 * 
	 */
	@Test
	public void category1CreateTestCase03() {
		try {
			Reporter.log("新建商品一级分类,使用删除的一级分类名称");
			String name = StringUtil.getRandomString(6);
			Category1Bean category = categoryService.getCategory1ByName(name);
			if (category == null) {
				category = new Category1Bean(name, 1);
				id = categoryService.createCategory1(category);
				Assert.assertNotEquals(id, null, "新建一级分类失败");
				boolean result = categoryService.deleteCategory1ById(id);
				Assert.assertEquals(result, true, "删除一级分类失败");

				id = categoryService.createCategory1(category);
				Assert.assertNotEquals(id, null, "新建商品一级分类,使用删除的一级分类名称,断言成功");
			}
		} catch (Exception e) {
			logger.error("新建商品一级分类遇到错误", e);
			Assert.fail("新建商品一级分类遇到错误", e);
		}
	}

	@Test
	public void category1CreateTestCase04() {
		Reporter.log("测试点: 拉取一级分类的icon测试");
		try {
			boolean result = categoryService.getCategory1Icon();
			Assert.assertEquals(result, true, "拉取一级分类的icon失败");
		} catch (Exception e) {
			logger.error("拉取一级分类的icon遇到错误", e);
			Assert.fail("拉取一级分类的icon遇到错误", e);
		}
	}

	@AfterMethod
	public void afterMethod() {
		if (id != null) {
			try {
				boolean result = categoryService.deleteCategory1ById(id);
				Assert.assertEquals(result, true, "删除商品一级分类失败");
			} catch (Exception e) {
				logger.error("删除商品一级分类遇到错误", e);
				Assert.fail("删除商品一级分类遇到错误", e);
			}
		}
	}
}
