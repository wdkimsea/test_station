package cn.guanmai.open.Product.abnormal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.Product.CategoryTest;
import cn.guanmai.open.bean.product.OpenCategory2Bean;
import cn.guanmai.open.bean.product.OpenPinleiBean;
import cn.guanmai.open.bean.product.OpenSpuBean;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.tool.AccessToken;

/* 
* @author liming 
* @date Jun 10, 2019 10:56:42 AM 
* @des 商户库异常测试
* @version 1.0 
*/
public class CategoryAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(CategoryTest.class);
	private OpenCategoryService categoryService;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		categoryService = new OpenCategoryServiceImpl(access_token);
	}

	@Test
	public void categoryAbnormalTestCase01() {
		Reporter.log("测试点: 获取商品库二级分类列表,传入非本GROUP下的一级分类ID,查看是否获取到别的GROUP下的二级分类列表");
		try {
			List<OpenCategory2Bean> category2List = categoryService.getCategory2List("A231", null, null);
			Assert.assertEquals(category2List, null, "获取商品库二级分类列表,传入非本GROUP下的一级分类ID,预期获取不到任何二级分类列表");
		} catch (Exception e) {
			logger.error("获取商品二级分类列表遇到错误: ", e);
			Assert.fail("获取商品二级分类列表遇到错误: ", e);
		}
	}

	@Test
	public void categoryAbnormalTestCase02() {
		Reporter.log("测试点: 获取商品库品类分类列表,传入非本GROUP下的一级分类ID,查看是否获取到别的GROUP下的品类分类列表");
		try {
			List<OpenPinleiBean> pinleiList = categoryService.getPinleiList("A231", null, null, null);
			Assert.assertEquals(pinleiList, null, "获取商品库品类分类列表,传入非本GROUP下的一级分类ID,预期获取不到任何品类分类列表");
		} catch (Exception e) {
			logger.error("获取品类分类列表遇到错误: ", e);
			Assert.fail("获取品类分类列表遇到错误: ", e);
		}
	}

	@Test
	public void categoryAbnormalTestCase03() {
		Reporter.log("测试点: 获取商品库品类分类列表,传入非本GROUP下的二级分类ID,查看是否获取到别的GROUP下的品类分类列表");
		try {
			List<OpenPinleiBean> pinleiList = categoryService.getPinleiList("B414", null, null, null);
			Assert.assertEquals(pinleiList, null, "获取商品库品类分类列表,传入非本GROUP下的二级分类ID,预期获取不到任何品类分类列表");
		} catch (Exception e) {
			logger.error("获取品类分类列表遇到错误: ", e);
			Assert.fail("获取品类分类列表遇到错误: ", e);
		}
	}

	@Test
	public void categoryAbnormalTestCase04() {
		Reporter.log("测试点: 获取商品库SPU分类列表,传入非本GROUP下的一级分类ID,查看是否获取到别的GROUP下的SPU分类列表");
		try {
			List<OpenSpuBean> spuList = categoryService.getSpuBeanList("A231", null, null, null, null);
			Assert.assertEquals(spuList, null, "获取商品库SPU分类列表,传入非本GROUP下的一级分类ID,预期获取不到任何SPU分类列表");
		} catch (Exception e) {
			logger.error("获取SPU分类列表遇到错误: ", e);
			Assert.fail("获取SPU分类列表遇到错误: ", e);
		}
	}

	@Test
	public void categoryAbnormalTestCase05() {
		Reporter.log("测试点: 获取商品库SPU分类列表,传入非本GROUP下的二级分类ID,查看是否获取到别的GROUP下的SPU分类列表");
		try {
			List<OpenSpuBean> spuList = categoryService.getSpuBeanList(null, "B414", null, null, null);
			Assert.assertEquals(spuList, null, "获取商品库SPU分类列表,传入非本GROUP下的二级分类ID,预期获取不到任何SPU分类列表");
		} catch (Exception e) {
			logger.error("获取SPU分类列表遇到错误: ", e);
			Assert.fail("获取SPU分类列表遇到错误: ", e);
		}
	}

	@Test
	public void categoryAbnormalTestCase06() {
		Reporter.log("测试点: 获取商品库SPU分类列表,传入非本GROUP下的品类分类ID,查看是否获取到别的GROUP下的SPU分类列表");
		try {
			List<OpenSpuBean> spuList = categoryService.getSpuBeanList(null, null, "P04400", null, null);
			Assert.assertEquals(spuList, null, "获取商品库SPU分类列表,传入非本GROUP下的品类分类ID,预期获取不到任何SPU分类列表");
		} catch (Exception e) {
			logger.error("获取SPU分类列表遇到错误: ", e);
			Assert.fail("获取SPU分类列表遇到错误: ", e);
		}
	}

	@Test
	public void categoryAbnormalTestCase07() {
		Reporter.log("测试点: 获取SPU详细信息,传入别的GROUP下的SPU ID,查看是否获取大别的GROUP下的SPU信息");
		try {
			OpenSpuBean spu = categoryService.getSpuBean("C115770");
			Assert.assertEquals(spu, null, "获取SPU详细信息,传入别的GROUP下的SPU ID,预期获取不到别的站点的SPU信息");
		} catch (Exception e) {
			logger.error("获取SPU信息遇到错误: ", e);
			Assert.fail("获取SPU信息遇到错误: ", e);
		}
	}

}
