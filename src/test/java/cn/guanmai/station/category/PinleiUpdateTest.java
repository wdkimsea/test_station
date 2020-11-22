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
import cn.guanmai.station.bean.category.PinleiBean;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Nov 6, 2018 10:46:29 AM 
* @des 品类修改测试用例
* @version 1.0 
*/
public class PinleiUpdateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PinleiCreateTest.class);
	private String category1_id;
	private String category2_id;
	private String pinlei_id;
	private String tmp_pinlei_id;
	private PinleiBean pinlei;

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
			String category2_name = "叶菜";
			Category2Bean category2 = categoryService.getCategory2ByName(category1_id, "叶菜");
			if (category2 == null) {
				category2 = new Category2Bean(category1_id, category2_name);
				category2_id = categoryService.createCategory2(category2);
				Assert.assertNotEquals(category2_id, null, "新建二级分类失败");
			} else {
				category2_id = category2.getId();
			}

			/**
			 * 创建品类
			 */
			PinleiBean pinlei = new PinleiBean(category2_id, StringUtil.getRandomString(6));
			pinlei_id = categoryService.createPinlei(pinlei);
			Assert.assertNotEquals(pinlei_id, null, "新建品类失败");
		} catch (Exception e) {
			logger.error("初始化数据失败: ", e);
			Assert.fail("初始化数据失败: ", e);
		}

	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			pinlei = categoryService.getPinleiById(pinlei_id);
			Assert.assertNotEquals(pinlei, null, "获取品类失败");
		} catch (Exception e) {
			logger.error("获取品类信息遇到错误: ", e);
			Assert.fail("获取品类信息遇到错误: ", e);
		}

	}

	@Test
	public void pinleiUpdateTestCase01() {
		try {
			Reporter.log("修改品类名称");
			String name = StringUtil.getRandomString(6);
			pinlei.setName(name);

			boolean result = categoryService.updatePinlei(pinlei);
			Assert.assertEquals(result, true, "修改品类名称,修改失败");
		} catch (Exception e) {
			logger.error("修改品类信息遇到错误: ", e);
			Assert.fail("修改品类信息遇到错误: ", e);
		}

	}

	@Test
	public void pinleiUpdateTestCase02() {
		try {
			Reporter.log("修改品类名称,使用已经被占用的品类分类名称");
			String name = StringUtil.getRandomString(6);
			PinleiBean tmp_pinlei = new PinleiBean(category2_id, name);
			tmp_pinlei_id = categoryService.createPinlei(tmp_pinlei);
			Assert.assertNotEquals(tmp_pinlei_id, null, "新建品类失败");

			pinlei.setName(name);
			boolean result = categoryService.updatePinlei(pinlei);
			Assert.assertEquals(result, false, "修改品类名称,使用已经被占用的二级分类名称,断言失败");
		} catch (Exception e) {
			logger.error("修改品类信息遇到错误: ", e);
			Assert.fail("修改品类信息遇到错误: ", e);
		}

	}

	@Test
	public void pinleiUpdateTestCase03() {
		try {
			Reporter.log("修改品类名称,使用已经删除的品类分类名称");
			String name = StringUtil.getRandomString(6);
			PinleiBean tmp_pinlei = new PinleiBean(category2_id, name);
			String tmp_pinlei_id = categoryService.createPinlei(tmp_pinlei);
			Assert.assertNotEquals(tmp_pinlei_id, null, "新建品类失败");
			boolean result = categoryService.deletePinlei(tmp_pinlei_id);
			Assert.assertEquals(result, true, "删除品类分类失败");

			pinlei.setName(name);
			result = categoryService.updatePinlei(pinlei);
			Assert.assertEquals(result, true, "修改品类名称,使用已经删除的品类分类名称,断言成功");
		} catch (Exception e) {
			logger.error("修改品类信息遇到错误: ", e);
			Assert.fail("修改品类信息遇到错误: ", e);
		}

	}

	@AfterMethod
	public void afterMethod() {
		try {
			if (tmp_pinlei_id != null) {
				boolean result = categoryService.deletePinlei(tmp_pinlei_id);
				Assert.assertEquals(result, true, "删除品类分类失败");
			}
		} catch (Exception e) {
			logger.error("删除品类信息遇到错误: ", e);
			Assert.fail("删除品类信息遇到错误: ", e);
		}

	}

	@AfterClass
	public void afterClass() {
		try {
			boolean result = categoryService.deletePinlei(pinlei_id);
			Assert.assertEquals(result, true, "删除品类失败");
		} catch (Exception e) {
			logger.error("后置处理数据遇到错误: ", e);
			Assert.fail("后置处理数据遇到错误: ", e);
		}
	}

}
