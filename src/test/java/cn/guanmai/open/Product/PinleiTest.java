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

import cn.guanmai.open.bean.product.OpenPinleiBean;
import cn.guanmai.open.bean.product.param.OpenSpuCreateParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author liming
 * @date 2019年12月18日
 * @time 下午3:24:53
 * @des TODO
 */

public class PinleiTest extends AccessToken {
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
	public void pinleiTestCase01() {
		ReporterCSS.title("测试点: 新建品类分类");
		try {
			String pinlei_name = StringUtil.getRandomString(6);
			pinlei_id = categoryService.createPinlei(category2_id, pinlei_name);
			Assert.assertNotEquals(pinlei_id, null, "新建品类分类失败");

			List<OpenPinleiBean> openPinleiList = categoryService.getPinleiList(category1_id, category2_id, null, null);
			Assert.assertNotEquals(openPinleiList, null, "获取品类分类列表失败");

			OpenPinleiBean openPinlei = openPinleiList.stream().filter(p -> p.getId().equals(pinlei_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(openPinlei, null, "新建的品类分类在品类分类列表中没有找到");

			Assert.assertEquals(openPinlei.getName(), pinlei_name, "新建品类传的名称和查询到的不一致");
		} catch (Exception e) {
			logger.error("新建品类遇到错误: ", e);
			Assert.fail("新建品类遇到错误: ", e);
		}
	}

	@Test
	public void pinleiTestCase02() {
		ReporterCSS.title("测试点: 新建品类分类,使用已经存在的名称,断言失败");
		String temp_pinlei_id = null;
		try {
			String temp_pinlei_name = StringUtil.getRandomString(6);
			temp_pinlei_id = categoryService.createPinlei(category2_id, temp_pinlei_name);
			Assert.assertNotEquals(temp_pinlei_id, null, "新建品类分类失败");

			pinlei_id = categoryService.createPinlei(category2_id, temp_pinlei_name);
			Assert.assertEquals(pinlei_id, null, "新建品类分类,使用已经存在的名称,断言失败");
		} catch (Exception e) {
			logger.error("新建品类遇到错误: ", e);
			Assert.fail("新建品类遇到错误: ", e);
		} finally {
			if (temp_pinlei_id != null) {
				try {
					boolean result = categoryService.deletePinlei(temp_pinlei_id);
					Assert.assertEquals(result, true, "后置处理,删除品类遇到错误");
				} catch (Exception e) {
					logger.error("后置处理,删除品类遇到错误: ", e);
					Assert.fail("后置处理,删除品类遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void pinleiTestCase03() {
		ReporterCSS.title("测试点: 修改品类分类名称");
		try {
			String pinlei_name = StringUtil.getRandomString(6);
			pinlei_id = categoryService.createPinlei(category2_id, pinlei_name);
			Assert.assertNotEquals(pinlei_id, null, "新建品类分类失败");

			String new_pinlei_name = StringUtil.getRandomString(6);
			boolean result = categoryService.updatePinlei(pinlei_id, new_pinlei_name);
			Assert.assertEquals(result, true, "修改品类分类名称失败");

			List<OpenPinleiBean> openPinleiList = categoryService.getPinleiList(category1_id, category2_id, null, null);
			Assert.assertNotEquals(openPinleiList, null, "获取品类分类列表失败");

			OpenPinleiBean openPinlei = openPinleiList.stream().filter(p -> p.getId().equals(pinlei_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(openPinlei, null, "新建的品类分类在品类分类列表中没有找到");

			Assert.assertEquals(openPinlei.getName(), new_pinlei_name, "修改品类传的名称和查询到的不一致");
		} catch (Exception e) {
			logger.error("修改品类遇到错误: ", e);
			Assert.fail("修改品类遇到错误: ", e);
		}
	}

	@Test
	public void pinleiTestCase04() {
		ReporterCSS.title("测试点: 修改品类分类名称,使用已经存在的品类名称,断言修改失败");
		String temp_pinlei_id = null;
		try {
			String temp_pinlei_name = StringUtil.getRandomString(6);
			temp_pinlei_id = categoryService.createPinlei(category2_id, temp_pinlei_name);
			Assert.assertNotEquals(temp_pinlei_id, null, "新建品类分类失败");

			String pinlei_name = StringUtil.getRandomString(6);
			pinlei_id = categoryService.createPinlei(category2_id, pinlei_name);
			Assert.assertNotEquals(pinlei_id, null, "新建品类分类失败");

			boolean result = categoryService.updatePinlei(pinlei_id, temp_pinlei_name);
			Assert.assertEquals(result, false, "修改品类分类名称,使用已经存在的品类名称,断言修改失败");

		} catch (Exception e) {
			logger.error("修改品类遇到错误: ", e);
			Assert.fail("修改品类遇到错误: ", e);
		} finally {
			if (temp_pinlei_id != null) {
				try {
					boolean result = categoryService.deletePinlei(temp_pinlei_id);
					Assert.assertEquals(result, true, "后置处理,删除品类遇到错误");
				} catch (Exception e) {
					logger.error("后置处理,删除品类遇到错误: ", e);
					Assert.fail("后置处理,删除品类遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void pinleiTestCase05() {
		ReporterCSS.title("测试点: 删除品类分类");
		try {
			String pinlei_name = StringUtil.getRandomString(6);
			String pinlei_id = categoryService.createPinlei(category2_id, pinlei_name);
			Assert.assertNotEquals(pinlei_id, null, "新建品类分类失败");

			boolean result = categoryService.deletePinlei(pinlei_id);
			Assert.assertEquals(result, true, "删除品类分类失败");

			List<OpenPinleiBean> openPinleiList = categoryService.getPinleiList(category1_id, category2_id, null, null);
			Assert.assertNotEquals(openPinleiList, null, "获取品类分类列表失败");

			OpenPinleiBean openPinlei = openPinleiList.stream().filter(p -> p.getId().equals(pinlei_id)).findAny()
					.orElse(null);
			Assert.assertEquals(openPinlei, null, "品类分类没有真正的删除");
		} catch (Exception e) {
			logger.error("新建品类遇到错误: ", e);
			Assert.fail("新建品类遇到错误: ", e);
		}
	}

	@Test
	public void pinleiTestCase06() {
		ReporterCSS.title("测试点: 删除有子级的品类分类,断言删除失败");
		String spu_id = null;
		try {
			String pinlei_name = StringUtil.getRandomString(6);
			pinlei_id = categoryService.createPinlei(category2_id, pinlei_name);
			Assert.assertNotEquals(pinlei_id, null, "新建品类分类失败");

			OpenSpuCreateParam spuCreateParam = new OpenSpuCreateParam();
			spuCreateParam.setPinlei_id(pinlei_id);
			spuCreateParam.setName(StringUtil.getRandomString(6));
			spuCreateParam.setStd_unit_name("斤");
			spuCreateParam.setDispatch_method("1");
			spuCreateParam.setDesc("pinleiTestCase06");

			spu_id = categoryService.createSpu(spuCreateParam);
			Assert.assertNotEquals(spu_id, null, "新建SPU失败");

			boolean result = categoryService.deletePinlei(pinlei_id);
			Assert.assertEquals(result, false, "删除品类分类失败");

		} catch (Exception e) {
			logger.error("新建品类遇到错误: ", e);
			Assert.fail("新建品类遇到错误: ", e);
		} finally {
			if (spu_id != null) {
				try {
					boolean result = categoryService.deleteSpu(spu_id);
					Assert.assertEquals(result, true, "后置处理,删除SPU失败");
				} catch (Exception e) {
					logger.error("后置处理,删除SPU遇到错误: ", e);
					Assert.fail("后置处理,删除SPU遇到错误: ", e);
				}
			}
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
