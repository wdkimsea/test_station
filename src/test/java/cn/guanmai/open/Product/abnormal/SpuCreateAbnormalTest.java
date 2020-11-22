package cn.guanmai.open.Product.abnormal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.open.Product.CategoryTest;
import cn.guanmai.open.bean.product.OpenCategory1Bean;
import cn.guanmai.open.bean.product.OpenPinleiBean;
import cn.guanmai.open.bean.product.param.OpenSpuCreateParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Jun 6, 2019 4:55:41 PM 
* @des 商品库异常测试
* @version 1.0 
*/
public class SpuCreateAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(CategoryTest.class);
	private OpenCategoryService categoryService;
	private OpenSpuCreateParam spuCreateParam;
	private String pinlei_id;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		categoryService = new OpenCategoryServiceImpl(access_token);
		try {
			List<OpenCategory1Bean> category1List = categoryService.getCategory1List();
			Assert.assertNotEquals(category1List, null, "获取本GROUP下的一级分类列表失败");
			// 取第一个一级分类ID
			String category1_id = category1List.get(0).getId();
			// 从品类列表里随机取一个品类,取其ID
			List<OpenPinleiBean> pinleis = categoryService.getPinleiList(category1_id, null, null, null);
			Assert.assertNotNull(pinleis, "获取本GROUP下的品类分类列表失败");
			pinlei_id = NumberUtil.roundNumberInList(pinleis).getId();
		} catch (Exception e) {
			logger.error("获取品类分类列表遇到错误: ", e);
			Assert.fail("获取品类分类列表遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		spuCreateParam = new OpenSpuCreateParam();
		String name = StringUtil.getRandomString(6).toUpperCase();
		spuCreateParam.setName(name);
		spuCreateParam.setDesc("");
		spuCreateParam.setDispatch_method("1");
		spuCreateParam.setPinlei_id(pinlei_id);
		spuCreateParam.setStd_unit_name("斤");
	}

	@Test
	public void categoryAbnormalTestCase01() {
		Reporter.log("测试点: 新建SPU,不传入name值,断言创建失败");
		try {
			spuCreateParam.setName(null);
			String id = categoryService.createSpu(spuCreateParam);
			Assert.assertEquals(id, null, "新建SPU,不传入name值,断言创建失败");
		} catch (Exception e) {
			logger.error("新建商品SPU分类遇到错误: ", e);
			Assert.fail("新建商品SPU分类遇到错误: ", e);
		}
	}

	@Test
	public void categoryAbnormalTestCase02() {
		Reporter.log("测试点: 新建SPU,传入空的name值,断言创建失败");
		try {
			spuCreateParam.setName("");
			String id = categoryService.createSpu(spuCreateParam);
			Assert.assertEquals(id, null, "新建SPU,传入空的name值,断言创建失败");
		} catch (Exception e) {
			logger.error("新建商品SPU分类遇到错误: ", e);
			Assert.fail("新建商品SPU分类遇到错误: ", e);
		}
	}

	@Test
	public void categoryAbnormalTestCase03() {
		Reporter.log("测试点: 新建SPU,name值传入空格,断言创建失败");
		try {
			spuCreateParam.setName(" ");
			String id = categoryService.createSpu(spuCreateParam);
			Assert.assertEquals(id, null, "新建SPU,name值传入空格,断言创建失败");
		} catch (Exception e) {
			logger.error("新建商品SPU分类遇到错误: ", e);
			Assert.fail("新建商品SPU分类遇到错误: ", e);
		}
	}

	@Test
	public void categoryAbnormalTestCase04() {
		Reporter.log("测试点: 新建SPU,传入过长的name值,断言创建失败");
		try {
			String name = StringUtil.getRandomString(33);
			spuCreateParam.setName(name);
			String id = categoryService.createSpu(spuCreateParam);
			Assert.assertEquals(id, null, "新建SPU,传入过长的name值,断言创建失败");
		} catch (Exception e) {
			logger.error("新建商品SPU分类遇到错误: ", e);
			Assert.fail("新建商品SPU分类遇到错误: ", e);
		}
	}

//	@Test
//	public void categoryAbnormalTestCase05() {
//		Reporter.log("测试点: 新建SPU,不传入dispatch_method值,断言创建失败");
//		try {
//			spuCreateParam.setDispatch_method(null);
//			String id = categoryService.createSpu(spuCreateParam);
//			Assert.assertEquals(id, null, "新建SPU,不传入dispatch_method值,断言创建失败");
//		} catch (Exception e) {
//			logger.error("新建商品SPU分类遇到错误: ", e);
//			Assert.fail("新建商品SPU分类遇到错误: ", e);
//		}
//	}
//
//	@Test
//	public void categoryAbnormalTestCase06() {
//		Reporter.log("测试点: 新建SPU,传入非法dispatch_method值,断言创建失败");
//		try {
//			spuCreateParam.setDispatch_method("A");
//			String id = categoryService.createSpu(spuCreateParam);
//			Assert.assertEquals(id, null, "新建SPU,传入非法dispatch_method值,断言创建失败");
//		} catch (Exception e) {
//			logger.error("新建商品SPU分类遇到错误: ", e);
//			Assert.fail("新建商品SPU分类遇到错误: ", e);
//		}
//	}
//
//	@Test
//	public void categoryAbnormalTestCase07() {
//		Reporter.log("测试点: 新建SPU,传入非区间的dispatch_method值,断言创建失败");
//		try {
//			spuCreateParam.setDispatch_method("3");
//			String id = categoryService.createSpu(spuCreateParam);
//			Assert.assertEquals(id, null, "新建SPU,传入非区间的dispatch_method值,断言创建失败");
//		} catch (Exception e) {
//			logger.error("新建商品SPU分类遇到错误: ", e);
//			Assert.fail("新建商品SPU分类遇到错误: ", e);
//		}
//	}

	@Test
	public void categoryAbnormalTestCase08() {
		Reporter.log("测试点: 新建SPU,不传入std_unit_name的值,断言创建失败");
		try {
			spuCreateParam.setStd_unit_name(null);
			String id = categoryService.createSpu(spuCreateParam);
			Assert.assertEquals(id, null, "新建SPU,不传入std_unit_name的值,断言创建失败");
		} catch (Exception e) {
			logger.error("新建商品SPU分类遇到错误: ", e);
			Assert.fail("新建商品SPU分类遇到错误: ", e);
		}
	}

	@Test
	public void categoryAbnormalTestCase09() {
		Reporter.log("测试点: 新建SPU,std_unit_name传入空格,断言创建失败");
		try {
			spuCreateParam.setStd_unit_name(" ");
			String id = categoryService.createSpu(spuCreateParam);
			Assert.assertEquals(id, null, "新建SPU,std_unit_name传入空格,断言创建失败");
		} catch (Exception e) {
			logger.error("新建商品SPU分类遇到错误: ", e);
			Assert.fail("新建商品SPU分类遇到错误: ", e);
		}
	}

	@Test
	public void categoryAbnormalTestCase10() {
		Reporter.log("测试点: 新建SPU,传入非区间的std_unit_name值,断言创建失败");
		try {
			spuCreateParam.setStd_unit_name("N");
			String id = categoryService.createSpu(spuCreateParam);
			Assert.assertEquals(id, null, "新建SPU,传入非区间的std_unit_name值,断言创建失败");
		} catch (Exception e) {
			logger.error("新建商品SPU分类遇到错误: ", e);
			Assert.fail("新建商品SPU分类遇到错误: ", e);
		}
	}

	@Test
	public void categoryAbnormalTestCase11() {
		Reporter.log("测试点: 新建SPU,不传入pinlei_id的值,断言创建失败");
		try {
			spuCreateParam.setPinlei_id(null);
			String id = categoryService.createSpu(spuCreateParam);
			Assert.assertEquals(id, null, "新建SPU,不传入pinlei_id的值,断言创建失败");
		} catch (Exception e) {
			logger.error("新建商品SPU分类遇到错误: ", e);
			Assert.fail("新建商品SPU分类遇到错误: ", e);
		}
	}

	@Test
	public void categoryAbnormalTestCase12() {
		Reporter.log("测试点: 新建SPU,传入别的Group下pinlei_id的值,断言创建失败");
		try {
			spuCreateParam.setPinlei_id("P04400");
			String id = categoryService.createSpu(spuCreateParam);
			Assert.assertEquals(id, null, "新建SPU,传入别的Group下pinlei_id的值,断言创建失败");
		} catch (Exception e) {
			logger.error("新建商品SPU分类遇到错误: ", e);
			Assert.fail("新建商品SPU分类遇到错误: ", e);
		}
	}

	@Test
	public void categoryAbnormalTestCase13() {
		Reporter.log("测试点: 新建SPU,pinlei_id不传入,断言创建失败");
		try {
			spuCreateParam.setPinlei_id("");
			String id = categoryService.createSpu(spuCreateParam);
			Assert.assertEquals(id, null, "新建SPU,传入别的Group下pinlei_id的值,断言创建失败");
		} catch (Exception e) {
			logger.error("新建商品SPU分类遇到错误: ", e);
			Assert.fail("新建商品SPU分类遇到错误: ", e);
		}
	}

	@Test
	public void categoryAbnormalTestCase14() {
		Reporter.log("测试点: 新建SPU,pinlei_id传入空格,断言创建失败");
		try {
			spuCreateParam.setPinlei_id(" ");
			String id = categoryService.createSpu(spuCreateParam);
			Assert.assertEquals(id, null, "新建SPU,pinlei_id传入空格,断言创建失败");
		} catch (Exception e) {
			logger.error("新建商品SPU分类遇到错误: ", e);
			Assert.fail("新建商品SPU分类遇到错误: ", e);
		}
	}
}
