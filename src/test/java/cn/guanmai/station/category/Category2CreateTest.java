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
import cn.guanmai.station.bean.category.Category2Bean;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Nov 5, 2018 7:08:35 PM 
* @des 新建二级分类测试
* @version 1.0 
*/
public class Category2CreateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(Category2CreateTest.class);
	private String category1_id;
	private String category2_id;

	private CategoryService categoryService;

	@BeforeClass
	public void beforeTest() {
		Map<String, String> headers = getStationCookie();
		categoryService = new CategoryServiceImpl(headers);
		try {
			String name = "蔬菜";
			Category1Bean category = categoryService.getCategory1ByName(name);
			if (category == null) {
				category = new Category1Bean(name, 1);
				category1_id = categoryService.createCategory1(category);
				Assert.assertNotEquals(category1_id, null, "新建一级分类失败");
			} else {
				category1_id = category.getId();
			}
		} catch (Exception e) {
			logger.error("新建一级分类遇到错误", e);
			Assert.fail("新建一级分类遇到错误", e);
		}

	}

	@BeforeMethod
	public void beforeMethod() {
		category2_id = null;
	}

	/**
	 * 创建二级分类
	 * 
	 */
	@Test
	public void caterory2CreateTestCase01() {
		try {
			Reporter.log("新建商品二级分类测试");
			String name = StringUtil.getRandomString(6);
			Category2Bean category = categoryService.getCategory2ByName(category1_id, name);
			if (category == null) {
				category = new Category2Bean(category1_id, name);
				category2_id = categoryService.createCategory2(category);
				Assert.assertNotEquals(category2_id, null, "新建二级分类失败");

				category = categoryService.getCategory2ById(category2_id);
				Assert.assertEquals(category.getName().equals(name) && category.getUpstream_id().equals(category1_id),
						true, "新建的二级分类与创建时输入的信息不一致");
			}
		} catch (Exception e) {
			logger.error("新建二级分类过程中遇到错误", e);
			Assert.fail("新建二级分类过程中遇到错误", e);
		}

	}

	/**
	 * 创建二级分类,使用已经存在的名称
	 * 
	 */
	@Test
	public void caterory2CreateTestCase02() {
		try {
			Reporter.log("创建二级分类,使用已经存在的二级分类名称");
			String name = StringUtil.getRandomString(6);
			Category2Bean category = categoryService.getCategory2ByName(category1_id, name);
			if (category == null) {
				category = new Category2Bean(category1_id, name);
				category2_id = categoryService.createCategory2(category);
				Assert.assertNotEquals(category2_id, null, "新建一级分类失败");

				String category2_id_2 = categoryService.createCategory2(category);
				Assert.assertEquals(category2_id_2, null, "创建二级分类,使用已经存在的二级分类名称,断言失败");
			}
		} catch (Exception e) {
			logger.error("新建二级分类过程中遇到错误", e);
			Assert.fail("新建二级分类过程中遇到错误", e);
		}

	}

	/**
	 * 创建二级分类,使用已经删除的二级分类名称
	 * 
	 */
	@Test
	public void caterory2CreateTestCase03() {
		try {
			Reporter.log("创建二级分类,使用已经删除的二级分类名称");
			String name = StringUtil.getRandomString(6);
			Category2Bean category = categoryService.getCategory2ByName(category1_id, name);
			if (category == null) {
				category = new Category2Bean(category1_id, name);
				String tmp_id = categoryService.createCategory2(category);
				Assert.assertNotEquals(tmp_id, null, "新建二级分类失败");

				boolean result = categoryService.deleteCategory2ById(tmp_id);
				Assert.assertEquals(result, true, "删除二级分类失败");

				category2_id = categoryService.createCategory2(category);
				Assert.assertNotEquals(category2_id, null, "创建二级分类,使用已经删除的二级分类名称,断言成功");
			}
		} catch (Exception e) {
			logger.error("新建二级分类过程中遇到错误", e);
			Assert.fail("新建二级分类过程中遇到错误", e);
		}

	}

	@AfterMethod
	public void afterMethod() {
		try {
			if (category2_id != null) {
				boolean result = categoryService.deleteCategory2ById(category2_id);
				Assert.assertEquals(result, true, "删除二级分类失败");
			}
		} catch (Exception e) {
			logger.error("删除二级分类过程中遇到错误", e);
			Assert.fail("删除二级分类过程中遇到错误", e);
		}

	}

}
