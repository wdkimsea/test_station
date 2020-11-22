package cn.guanmai.station.category;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
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
* @date Nov 5, 2018 5:51:24 PM 
* @todo TODO
* @version 1.0 
*/
public class Category1DeleteTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(Category1CreateTest.class);
	private String id;

	private CategoryService categoryService;

	@BeforeClass
	public void InitData() {
		Map<String, String> headers = getStationCookie();
		categoryService = new CategoryServiceImpl(headers);
	}

	@BeforeMethod
	public void beforeMethod() {

		try {
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

	@Test
	public void categoryDeleteTest01() {
		try {
			Reporter.log("删除商品一级分类测试");
			boolean result = categoryService.deleteCategory1ById(id);
			Assert.assertEquals(result, true, "删除商品一级分类失败");
		} catch (Exception e) {
			logger.error("删除一级分类遇到错误", e);
			Assert.fail("删除一级分类遇到错误", e);
		}

	}

}
