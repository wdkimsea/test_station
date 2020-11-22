package cn.guanmai.open.Product;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.product.OpenCategory1Bean;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author liming
 * @date 2019年12月18日
 * @time 下午2:43:09
 * @des TODO
 */

public class Category1Test extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(Category1Test.class);
	private OpenCategoryService categoryService;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		categoryService = new OpenCategoryServiceImpl(access_token);
	}

	@Test
	public void category1TestCase01() {
		ReporterCSS.title("测试点: 新建一级分类");
		String category1_id = null;
		try {
			String category1_name = StringUtil.getRandomString(6);
			String temp_category1_id = categoryService.createCategory1(category1_name);
			Assert.assertNotEquals(temp_category1_id, null, "新建一级分类失败");
			category1_id = temp_category1_id;

			List<OpenCategory1Bean> category1List = categoryService.getCategory1List();
			Assert.assertNotEquals(category1List, null, "获取一级分类失败");

			OpenCategory1Bean openCategory1 = category1List.stream().filter(c -> c.getId().equals(temp_category1_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(openCategory1, null, "新建的一级分类在一级分类列表没有找到");

			Assert.assertEquals(openCategory1.getName(), category1_name, "新建一级分类填写的名称和查询到的名称不一致");
		} catch (Exception e) {
			logger.error("新建一级分类遇到错误: ", e);
			Assert.fail("新建一级分类遇到错误: ", e);
		} finally {
			if (category1_id != null) {
				try {
					boolean result = categoryService.deleteCategory1(category1_id);
					Assert.assertEquals(result, true, "后置处理,删除一级分类失败");
				} catch (Exception e) {
					logger.error("后置处理,删除一级分类遇到错误: ", e);
					Assert.fail("后置处理,删除一级分类遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void category1TestCase02() {
		ReporterCSS.title("测试点: 新建一级分类,使用已经存在的名称,断言失败");
		String category1_id = null;
		try {
			List<OpenCategory1Bean> category1List = categoryService.getCategory1List();
			Assert.assertNotEquals(category1List, null, "获取一级分类失败");

			OpenCategory1Bean openCategory1 = NumberUtil.roundNumberInList(category1List);

			String category1_name = openCategory1.getName();
			category1_id = categoryService.createCategory1(category1_name);
			Assert.assertEquals(category1_id, null, "新建一级分类,使用已经存在的名称,断言失败");
		} catch (Exception e) {
			logger.error("新建一级分类遇到错误: ", e);
			Assert.fail("新建一级分类遇到错误: ", e);
		} finally {
			if (category1_id != null) {
				try {
					boolean result = categoryService.deleteCategory1(category1_id);
					Assert.assertEquals(result, true, "后置处理,删除一级分类失败");
				} catch (Exception e) {
					logger.error("后置处理,删除一级分类遇到错误: ", e);
					Assert.fail("后置处理,删除一级分类遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void category1TestCase03() {
		ReporterCSS.title("测试点: 新建一级分类");
		String category1_id = null;
		try {
			String category1_name = StringUtil.getRandomString(6);
			String temp_category1_id = categoryService.createCategory1(category1_name);
			Assert.assertNotEquals(temp_category1_id, null, "新建一级分类失败");
			category1_id = temp_category1_id;

			String new_category1_name = StringUtil.getRandomString(6);
			boolean result = categoryService.updateCategory1(temp_category1_id, new_category1_name);
			Assert.assertEquals(result, true, "修改一级分类名称失败");

			List<OpenCategory1Bean> category1List = categoryService.getCategory1List();
			Assert.assertNotEquals(category1List, null, "获取一级分类失败");

			OpenCategory1Bean openCategory1 = category1List.stream().filter(c -> c.getId().equals(temp_category1_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(openCategory1, null, "新建的一级分类在一级分类列表没有找到");

			Assert.assertEquals(openCategory1.getName(), new_category1_name, "修改一级分类填写的名称和查询到的名称不一致");
		} catch (Exception e) {
			logger.error("新建一级分类遇到错误: ", e);
			Assert.fail("新建一级分类遇到错误: ", e);
		} finally {
			if (category1_id != null) {
				try {
					boolean result = categoryService.deleteCategory1(category1_id);
					Assert.assertEquals(result, true, "后置处理,删除一级分类失败");
				} catch (Exception e) {
					logger.error("后置处理,删除一级分类遇到错误: ", e);
					Assert.fail("后置处理,删除一级分类遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void category1TestCase04() {
		ReporterCSS.title("测试点: 修改一级分类,使用已经存在的名称,断言失败");
		String category1_id = null;
		try {
			List<OpenCategory1Bean> category1List = categoryService.getCategory1List();
			Assert.assertNotEquals(category1List, null, "获取一级分类失败");

			OpenCategory1Bean openCategory1 = NumberUtil.roundNumberInList(category1List);

			String category1_name = StringUtil.getRandomString(6);
			String temp_category1_id = categoryService.createCategory1(category1_name);
			Assert.assertNotEquals(temp_category1_id, null, "新建一级分类失败");
			category1_id = temp_category1_id;

			boolean result = categoryService.updateCategory1(temp_category1_id, openCategory1.getName());
			Assert.assertEquals(result, false, "修改一级分类,使用已经存在的名称,断言失败");

		} catch (Exception e) {
			logger.error("新建一级分类遇到错误: ", e);
			Assert.fail("新建一级分类遇到错误: ", e);
		} finally {
			if (category1_id != null) {
				try {
					boolean result = categoryService.deleteCategory1(category1_id);
					Assert.assertEquals(result, true, "后置处理,删除一级分类失败");
				} catch (Exception e) {
					logger.error("后置处理,删除一级分类遇到错误: ", e);
					Assert.fail("后置处理,删除一级分类遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void category1TestCase05() {
		ReporterCSS.title("测试点: 删除一级分类");
		try {
			String category1_name = StringUtil.getRandomString(6);
			String category1_id = categoryService.createCategory1(category1_name);
			Assert.assertNotEquals(category1_id, null, "新建一级分类失败");

			boolean result = categoryService.deleteCategory1(category1_id);
			Assert.assertEquals(result, true, "删除一级分类失败");

			List<OpenCategory1Bean> category1List = categoryService.getCategory1List();
			Assert.assertNotEquals(category1List, null, "获取一级分类失败");

			OpenCategory1Bean openCategory1 = category1List.stream().filter(c -> c.getId().equals(category1_id))
					.findAny().orElse(null);
			Assert.assertEquals(openCategory1, null, "删除的一级分类实际没有删除成功");
		} catch (Exception e) {
			logger.error("删除一级分类遇到错误: ", e);
			Assert.fail("删除一级分类遇到错误: ", e);
		}
	}

	@Test
	public void category1TestCase06() {
		ReporterCSS.title("测试点: 删除有子级的一级分类,断言删除失败");
		String category1_id = null;
		String category2_id = null;
		try {
			String category1_name = StringUtil.getRandomString(6);
			category1_id = categoryService.createCategory1(category1_name);
			Assert.assertNotEquals(category1_id, null, "新建一级分类失败");

			String category2_name = StringUtil.getRandomString(6);
			category2_id = categoryService.createCategory2(category1_id, category2_name);
			Assert.assertNotEquals(category2_id, null, "新建二级分类失败");

			boolean result = categoryService.deleteCategory1(category1_id);
			Assert.assertEquals(result, false, "删除有子级的一级分类,断言删除失败");

		} catch (Exception e) {
			logger.error("删除一级分类遇到错误: ", e);
			Assert.fail("删除一级分类遇到错误: ", e);
		} finally {
			try {
				boolean result = true;
				if (category2_id != null) {
					result = categoryService.deleteCategory2(category2_id);
					Assert.assertEquals(result, true, "后置处理,删除二级分类失败");
				}

				if (category1_id != null) {
					result = categoryService.deleteCategory1(category1_id);
					Assert.assertEquals(result, true, "后置处理,删除一级分类失败");
				}
			} catch (Exception e) {
				logger.error("后置处理,恢复环境遇到错误: ", e);
				Assert.fail("后置处理,恢复环境遇到错误: ", e);
			}
		}
	}

}
