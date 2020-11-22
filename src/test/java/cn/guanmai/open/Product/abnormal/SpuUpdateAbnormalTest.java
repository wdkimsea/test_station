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
import cn.guanmai.open.bean.product.OpenSpuBean;
import cn.guanmai.open.bean.product.param.OpenSpuCreateParam;
import cn.guanmai.open.bean.product.param.OpenSpuUpdateParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Jun 6, 2019 5:38:27 PM 
* @des 更新SPU异常测试
* @version 1.0 
*/
public class SpuUpdateAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(CategoryTest.class);
	private OpenCategoryService categoryService;
	private OpenSpuUpdateParam spuUpdateParam;
	private OpenSpuBean spu;
	private String pinlei_id;
	private String spu_id;

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

			OpenSpuCreateParam createParam = new OpenSpuCreateParam();
			createParam.setName(StringUtil.getRandomString(6).toUpperCase());
			createParam.setDesc("");
			createParam.setDispatch_method("2");
			createParam.setPinlei_id(pinlei_id);
			createParam.setStd_unit_name("斤");
			spu_id = categoryService.createSpu(createParam);
			Assert.assertNotEquals(spu_id, null, "新建SPU失败");

			spu = categoryService.getSpuBean(spu_id);
			Assert.assertNotEquals(spu, null, "获取SPU详细信息失败");

		} catch (Exception e) {
			logger.error("获取品类分类列表遇到错误: ", e);
			Assert.fail("获取品类分类列表遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		spuUpdateParam = new OpenSpuUpdateParam();
		spuUpdateParam.setSpu_id(spu_id);
		spuUpdateParam.setName(spu.getName());
		spuUpdateParam.setPinlei_id(pinlei_id);
		spuUpdateParam.setDesc(spu.getDesc());
	}

	@Test
	public void spuUpdateAbnormalTestCase01() {
		Reporter.log("测试点: 修改SPU,SPU ID传入空的值,断言修改失败");
		try {
			spuUpdateParam.setSpu_id(null);
			boolean reuslt = categoryService.updateSpu(spuUpdateParam);
			Assert.assertEquals(reuslt, false, "修改SPU,传入空的SPU_ID值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改SPU信息遇到错误: ", e);
			Assert.fail("修改SPU信息遇到错误: ", e);
		}
	}

	@Test
	public void spuUpdateAbnormalTestCase02() {
		Reporter.log("测试点: 修改SPU,传入别的站点的SPU_ID值,断言修改失败");
		try {
			spuUpdateParam.setSpu_id("C369758");
			boolean reuslt = categoryService.updateSpu(spuUpdateParam);
			Assert.assertEquals(reuslt, false, "修改SPU,传入别的站点的SPU_ID值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改SPU信息遇到错误: ", e);
			Assert.fail("修改SPU信息遇到错误: ", e);
		}
	}

	@Test
	public void spuUpdateAbnormalTestCase03() {
		Reporter.log("测试点: 修改SPU,name传入空的值,断言修改失败");
		try {
			spuUpdateParam.setName("");
			boolean reuslt = categoryService.updateSpu(spuUpdateParam);
			Assert.assertEquals(reuslt, false, "修改SPU,传入空的name值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改SPU信息遇到错误: ", e);
			Assert.fail("修改SPU信息遇到错误: ", e);
		}
	}

	@Test
	public void spuUpdateAbnormalTestCase04() {
		Reporter.log("测试点: 修改SPU,name传入空格,断言修改失败");
		try {
			spuUpdateParam.setName(" ");
			boolean reuslt = categoryService.updateSpu(spuUpdateParam);
			Assert.assertEquals(reuslt, false, "修改SPU,name传入空格,断言修改失败");
		} catch (Exception e) {
			logger.error("修改SPU信息遇到错误: ", e);
			Assert.fail("修改SPU信息遇到错误: ", e);
		}
	}

	@Test
	public void spuUpdateAbnormalTestCase05() {
		Reporter.log("测试点: 修改SPU,传入过长的name值,断言修改失败");
		try {
			String name = StringUtil.getRandomString(33).toUpperCase();
			spuUpdateParam.setName(name);
			boolean reuslt = categoryService.updateSpu(spuUpdateParam);
			Assert.assertEquals(reuslt, false, "修改SPU,传入过长的name值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改SPU信息遇到错误: ", e);
			Assert.fail("修改SPU信息遇到错误: ", e);
		}
	}

	@Test
	public void spuUpdateAbnormalTestCase06() {
		Reporter.log("测试点: 修改SPU,传入空的pinlei_id值,断言修改失败");
		try {
			spuUpdateParam.setPinlei_id("");
			boolean reuslt = categoryService.updateSpu(spuUpdateParam);
			Assert.assertEquals(reuslt, false, "修改SPU,传入空的pinlei_id值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改SPU信息遇到错误: ", e);
			Assert.fail("修改SPU信息遇到错误: ", e);
		}
	}

	@Test
	public void spuUpdateAbnormalTestCase07() {
		Reporter.log("测试点: 修改SPU,传入别的站点下的pinlei_id值,断言修改失败");
		try {
			spuUpdateParam.setPinlei_id("P04400");
			boolean reuslt = categoryService.updateSpu(spuUpdateParam);
			Assert.assertEquals(reuslt, false, "修改SPU,传入别的站点下的pinlei_id值,断言修改失败");
		} catch (Exception e) {
			logger.error("修改SPU信息遇到错误: ", e);
			Assert.fail("修改SPU信息遇到错误: ", e);
		}
	}

}
