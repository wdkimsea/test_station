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

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.category.Category1Bean;
import cn.guanmai.station.bean.category.Category2Bean;
import cn.guanmai.station.bean.category.PinleiBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Nov 6, 2018 2:21:32 PM 
* @des 创建SPU商品测试
* @version 1.0 
*/
public class SpuCreateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SpuCreateTest.class);
	private String category1_id;
	private String category2_id;
	private String pinlei_id;
	private String spu_id;
	private String tmp_spu_id;

	private static CategoryService categoryService;

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

			String pinlei_name = "茼蒿";
			PinleiBean pinlei = categoryService.getPinleiByName(category2_id, pinlei_name);
			if (pinlei == null) {
				pinlei = new PinleiBean(category2_id, pinlei_name);
				pinlei_id = categoryService.createPinlei(pinlei);
				Assert.assertNotEquals(pinlei_id, null, "新建品类分类失败");
			} else {
				pinlei_id = pinlei.getId();
			}
			Assert.assertNotEquals(pinlei_id, null, "获取品类ID失败");
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}

	}

	@BeforeMethod
	public void beforeMethod() {
		spu_id = null;
		tmp_spu_id = null;
	}

	/**
	 * 新建商品SPU
	 * 
	 */
	@Test
	public void spuCreateTestCase01() {
		try {
			Reporter.log("新建SPU测试");
			String name = StringUtil.getRandomString(6);
			SpuBean spu = new SpuBean(name, pinlei_id, "case01", new JSONArray(), 0, "斤", new JSONArray(), 2, 1);
			spu_id = categoryService.createSpu(spu);
			Assert.assertNotEquals(spu_id, null, "新建SPU失败");

			// 验证创建SPU填写的信息是否和输入的一致
			SpuBean tmp_spu = categoryService.getSpuById(spu_id);
			boolean result = tmp_spu != null && tmp_spu.getName().equals(name)
					&& tmp_spu.getDesc().equals(spu.getDesc()) && tmp_spu.getPinlei_id().equals(pinlei_id)
					&& tmp_spu.getStd_unit_name().equals(spu.getStd_unit_name())
					&& tmp_spu.getP_type() == spu.getP_type()
					&& tmp_spu.getDispatch_method() == spu.getDispatch_method()
					&& tmp_spu.getNeed_pesticide_detect() == 1 && tmp_spu.getCategory_id_1().equals(category1_id)
					&& tmp_spu.getCategory_id_2().equals(category2_id);
			Assert.assertEquals(result, true, "新建SPU成功后,验证创建SPU填写的信息是否和输入的一致,验证失败");
		} catch (Exception e) {
			logger.error("新建SPU商品遇到错误: ", e);
			Assert.fail("新建SPU商品遇到错误: ", e);
		}

	}

	/**
	 * 新建商品SPU
	 * 
	 */
	@Test
	public void spuCreateTestCase02() {
		try {
			Reporter.log("新建SPU测试");
			String name = StringUtil.getRandomString(6);
			SpuBean spu = new SpuBean(name, pinlei_id, "case02", new JSONArray(), 1, "斤", new JSONArray(), 1, 0);
			spu_id = categoryService.createSpu(spu);
			Assert.assertNotEquals(spu_id, null, "新建SPU失败");

			// 验证创建SPU填写的信息是否和输入的一致
			SpuBean tmp_spu = categoryService.getSpuById(spu_id);
			boolean result = tmp_spu != null && tmp_spu.getName().equals(name)
					&& tmp_spu.getDesc().equals(spu.getDesc()) && tmp_spu.getPinlei_id().equals(pinlei_id)
					&& tmp_spu.getStd_unit_name().equals(spu.getStd_unit_name())
					&& tmp_spu.getP_type() == spu.getP_type()
					&& tmp_spu.getDispatch_method() == spu.getDispatch_method()
					&& tmp_spu.getNeed_pesticide_detect() == 0 && tmp_spu.getCategory_id_1().equals(category1_id)
					&& tmp_spu.getCategory_id_2().equals(category2_id);
			Assert.assertEquals(result, true, "新建SPU成功后,验证创建SPU填写的信息是否和输入的一致,验证失败");
		} catch (Exception e) {
			logger.error("新建SPU商品遇到错误: ", e);
			Assert.fail("新建SPU商品遇到错误: ", e);
		}
	}

	/**
	 * 新建商品SPU
	 * 
	 */
	@Test
	public void spuCreateTestCase03() {
		try {
			Reporter.log("新建SPU,使用已经存在的SPU名称");
			String name = StringUtil.getRandomString(6);
			SpuBean spu = new SpuBean(name, pinlei_id, "case03", new JSONArray(), 0, "斤", new JSONArray(), 2, 1);
			spu_id = categoryService.createSpu(spu);
			Assert.assertNotEquals(spu_id, null, "新建SPU失败");

			tmp_spu_id = categoryService.createSpu(spu);
			Assert.assertEquals(tmp_spu_id, null, "新建SPU,使用已经存在的SPU名称,断言失败");
		} catch (Exception e) {
			logger.error("新建SPU商品遇到错误: ", e);
			Assert.fail("新建SPU商品遇到错误: ", e);
		}

	}

	/**
	 * 新建商品SPU
	 * 
	 */
	@Test
	public void spuCreateTestCase04() {
		try {
			Reporter.log("新建SPU,使用删除的SPU名称");
			String name = StringUtil.getRandomString(6);
			SpuBean spu = new SpuBean(name, pinlei_id, "case04", new JSONArray(), 0, "斤", new JSONArray(), 2, 1);
			String tmp_spu_id = categoryService.createSpu(spu);
			Assert.assertNotEquals(tmp_spu_id, null, "新建SPU失败");
			boolean result = categoryService.deleteSpu(tmp_spu_id);
			Assert.assertEquals(result, true, "删除商品SPU失败");

			spu_id = categoryService.createSpu(spu);
			Assert.assertNotEquals(spu_id, null, "新建SPU,使用删除的SPU名称,断言成功");
		} catch (Exception e) {
			logger.error("新建SPU商品遇到错误: ", e);
			Assert.fail("新建SPU商品遇到错误: ", e);
		}

	}

	/**
	 * 新建商品SPU
	 * 
	 */
	@Test
	public void spuCreateTestCase05() {
		try {
			Reporter.log("新建SPU,输入多个别名");
			JSONArray alias = new JSONArray();
			alias.add("别名One");
			alias.add("别名Two");

			String name = StringUtil.getRandomString(6);
			SpuBean spu = new SpuBean(name, pinlei_id, "case05", new JSONArray(), 0, "斤", alias, 2, 1);
			spu_id = categoryService.createSpu(spu);
			Assert.assertNotEquals(spu_id, null, "新建SPU失败");

			SpuBean tmp_spu = categoryService.getSpuById(spu_id);
			boolean result = tmp_spu.getAlias().toString().equals(alias.toString());

			Assert.assertEquals(result, true, "新建SPU成功后,验证填写的基本信息,验证通过");
		} catch (Exception e) {
			logger.error("新建SPU商品遇到错误: ", e);
			Assert.fail("新建SPU商品遇到错误: ", e);
		}

	}

	/**
	 * 上传SPU图片测试
	 * 
	 */
	@Test
	public void spuCreateTestCase06() {
		try {
			Reporter.log("上传SPU图片测试");
			String imagePath = System.getProperty("user.dir") + "/image/BoCai.jpg";
			String image_name = categoryService.uploadSpuImage(imagePath);
			Assert.assertNotEquals(image_name, null, "上传SPU图片失败");

			JSONArray images = new JSONArray();
			images.add(image_name);

			String name = StringUtil.getRandomString(6);
			SpuBean spu = new SpuBean(name, pinlei_id, "case06", images, 0, "斤", new JSONArray(), 2, 1);
			spu_id = categoryService.createSpu(spu);
			Assert.assertNotEquals(spu_id, null, "新建SPU,绑定图片信息,创建失败");

			SpuBean tmp_spu = categoryService.getSpuById(spu_id);
			JSONArray temp_images = tmp_spu.getImages();
			Assert.assertEquals(temp_images.toString().contains(image_name), true, "创建SPU的时候上传图片,但是新建成功后查看却没有");

		} catch (Exception e) {
			logger.error("上传SPU图片遇到错误: ", e);
			Assert.fail("上传SPU图片遇到错误: ", e);
		}

	}

	@AfterMethod
	public void afterMethod() {
		try {
			if (spu_id != null) {
				boolean result = categoryService.deleteSpu(spu_id);
				Assert.assertEquals(result, true, "删除商品SPU失败");
			}

			if (tmp_spu_id != null) {
				boolean result = categoryService.deleteSpu(tmp_spu_id);
				Assert.assertEquals(result, true, "删除商品SPU失败");
			}
		} catch (Exception e) {
			logger.error("删除SPU商品遇到错误: ", e);
			Assert.fail("删除SPU商品遇到错误: ", e);
		}

	}
}
