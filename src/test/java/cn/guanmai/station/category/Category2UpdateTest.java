package cn.guanmai.station.category;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
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
* @date Nov 5, 2018 7:37:21 PM 
* @des 修改二级分类
* @version 1.0 
*/
public class Category2UpdateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(Category2UpdateTest.class);
	private String tmp_id;
	private String category1_id;
	private String category2_id;
	private Category2Bean category2Bean;

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

			Category2Bean category2 = new Category2Bean(category1_id, StringUtil.getRandomString(6));
			category2_id = categoryService.createCategory2(category2);
			Assert.assertNotEquals(category2_id, null, "二级分类创建失败");
		} catch (Exception e) {
			logger.error("初始化数据过程中遇到错误", e);
			Assert.fail("初始化数据过程中遇到错误", e);
		}

	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			category2Bean = categoryService.getCategory2ById(category2_id);
		} catch (Exception e) {
			logger.error("获取二级分类信息遇到错误", e);
			Assert.fail("获取二级分类信息遇到错误", e);
		}
		tmp_id = null;
	}

	@Test
	public void category2UpdateTestCase01() {
		try {
			Reporter.log("修改二级分类的名称");
			String name = StringUtil.getRandomString(6);
			category2Bean.setName(name);
			boolean result = categoryService.updateCategory2(category2Bean);
			Assert.assertEquals(result, true, "修改二级分类的名称,断言成功");
			category2Bean = categoryService.getCategory2ById(category2_id);
			Assert.assertEquals(category2Bean.getName(), name, "修改后的二级分类名称和输入的不一致");
		} catch (Exception e) {
			logger.error("更新二级分类信息遇到错误", e);
			Assert.fail("更新二级分类信息遇到错误", e);
		}

	}

	@Test
	public void category2UpdateTestCase02() {
		try {
			Reporter.log("修改二级分类的名称,使用在同一父级分类下已经存在的二级分类名称,断言失败");
			String name = StringUtil.getRandomString(6);

			Category2Bean tmp_category2 = new Category2Bean(category1_id, name);
			tmp_id = categoryService.createCategory2(tmp_category2);
			Assert.assertNotEquals(tmp_id, null, "创建二级分类失败");

			category2Bean.setName(name);
			boolean result = categoryService.updateCategory2(category2Bean);
			Assert.assertEquals(result, false, "修改二级分类的名称,使用在同一父级分类下已经存在的二级分类名称,断言失败");
		} catch (Exception e) {
			logger.error("更新二级分类信息遇到错误", e);
			Assert.fail("更新二级分类信息遇到错误", e);
		}

	}

	@Test
	public void category2UpdateTestCase03() {
		try {
			Reporter.log("修改二级分类的名称,使用已经删除的二级分类名称,断言成功");
			String name = StringUtil.getRandomString(6);

			Category2Bean tmp_category2 = new Category2Bean(category1_id, name);
			tmp_id = categoryService.createCategory2(tmp_category2);
			Assert.assertNotEquals(tmp_id, null, "创建二级分类失败");

			boolean result = categoryService.deleteCategory2ById(tmp_id);
			Assert.assertEquals(result, true, "删除二级分类失败");
			tmp_id = null;

			category2Bean.setName(name);
			result = categoryService.updateCategory2(category2Bean);
			Assert.assertEquals(result, true, "修改二级分类的名称,修改二级分类的名称,使用已经删除的二级分类名称,断言成功");
		} catch (Exception e) {
			logger.error("更新二级分类信息遇到错误", e);
			Assert.fail("更新二级分类信息遇到错误", e);
		}

	}

	@AfterMethod
	public void afterMethod() {
		try {
			if (tmp_id != null) {
				boolean result = categoryService.deleteCategory2ById(tmp_id);
				Assert.assertEquals(result, true, "删除二级分类失败");
			}
		} catch (Exception e) {
			logger.error("删除二级分类信息遇到错误", e);
			Assert.fail("删除二级分类信息遇到错误", e);
		}

	}

	@AfterClass
	public void afterClass() {
		try {
			boolean result = categoryService.deleteCategory2ById(category2_id);
			Assert.assertEquals(result, true, "删除二级分类失败");
		} catch (Exception e) {
			logger.error("删除二级分类信息遇到错误", e);
			Assert.fail("删除二级分类信息遇到错误", e);
		}

	}

}
