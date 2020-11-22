package cn.guanmai.station.category;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.category.Category1Bean;
import cn.guanmai.station.bean.category.Category2Bean;
import cn.guanmai.station.bean.category.PinleiBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.bean.category.SpuIndexBean;
import cn.guanmai.station.bean.category.param.SpuIndexFilterParam;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Nov 6, 2018 5:02:25 PM 
* @des 修改SPU 商品
* @version 1.0 
*/
public class SpuUpdateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SpuUpdateTest.class);
	private String category1_id;
	private String category2_id;
	private String pinlei_id;
	private String spu_id;
	private String tmp_spu_id;
	private SpuBean spu;

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

			// 新建SPU
			String spu_name = StringUtil.getRandomString(6);
			SpuBean spu = new SpuBean(spu_name, pinlei_id, "Init", new JSONArray(), 0, "斤", new JSONArray(), 2, 0);
			spu_id = categoryService.createSpu(spu);
			Assert.assertNotEquals(spu_id, null, "新建SPU失败");
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}

	}

	@BeforeMethod
	public void beforeMethod() {
		tmp_spu_id = null;
		try {
			spu = categoryService.getSpuById(spu_id);
			Assert.assertNotEquals(spu, null, "获取SPU " + spu_id + " 详细信息失败");
		} catch (Exception e) {
			logger.error("获取SPU商品遇到错误: ", e);
			Assert.fail("获取SPU商品遇到错误: ", e);
		}

	}

	@Test
	public void spuUpdateTestCase01() {
		try {
			Reporter.log("修改SPU信息");
			String new_name = StringUtil.getRandomString(6);
			spu.setName(new_name);
			spu.setDispatch_method(1);
			spu.setNeed_pesticide_detect(1);

			boolean result = categoryService.updateSpu(spu);
			Assert.assertEquals(result, true, "修改SPU信息失败");

			SpuBean tmp_spu = categoryService.getSpuById(spu_id);
			Assert.assertNotEquals(tmp_spu, null, "获取SPU商品 " + spu_id + "详细信息失败");

			String msg = null;
			if (!spu.getName().equals(tmp_spu.getName())) {
				msg = String.format("修改后的SPU的名称和预期的不一致,预期:%s,实际:%s", new_name, tmp_spu.getName());
				Reporter.log(msg);
				result = false;
			}

			if (spu.getDispatch_method() != tmp_spu.getDispatch_method()) {
				msg = String.format("修改后的SPU的分拣方式和预期的不一致,预期:%s,实际:%s", spu.getDispatch_method(),
						tmp_spu.getDispatch_method());
				Reporter.log(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "修改SPU信息后再次查询与输入值验证,验证失败");
		} catch (Exception e) {
			logger.error("修改SPU商品遇到错误: ", e);
			Assert.fail("修改SPU商品遇到错误: ", e);
		}

	}

	@Test
	public void spuUpdateTestCase02() {
		try {
			Reporter.log("修改SPU信息,名称使用其他的SPU名称");
			String tmp_spu_name = StringUtil.getRandomString(6);
			SpuBean tmp_spu = new SpuBean(tmp_spu_name, pinlei_id, "Init", new JSONArray(), 0, "斤", new JSONArray(), 2,
					0);
			tmp_spu_id = categoryService.createSpu(tmp_spu);
			Assert.assertNotEquals(tmp_spu_id, null, "新建SPU失败");

			SpuBean spu = categoryService.getSpuById(spu_id);
			spu.setName(tmp_spu_name);
			spu.setNeed_pesticide_detect(1);
			boolean result = categoryService.updateSpu(spu);
			Assert.assertEquals(result, false, "修改SPU信息,名称使用其他的SPU名称,断言失败");
		} catch (Exception e) {
			logger.error("修改SPU商品遇到错误: ", e);
			Assert.fail("修改SPU商品遇到错误: ", e);
		}

	}

	@Test
	public void spuUpdateTestCase03() {
		try {
			Reporter.log("测试点: 修改SPU信息,名称使用已经删除的SPU名称");
			String tmp_spu_name = StringUtil.getRandomString(6);
			SpuBean tmp_spu = new SpuBean(tmp_spu_name, pinlei_id, "Init", new JSONArray(), 0, "斤", new JSONArray(), 2,
					0);
			String tmp_spu_id = categoryService.createSpu(tmp_spu);
			Assert.assertNotEquals(tmp_spu_id, null, "新建SPU失败");
			boolean result = categoryService.deleteSpu(tmp_spu_id);
			Assert.assertEquals(result, true, "删除SPU商品,断言成功");

			SpuBean spu = categoryService.getSpuById(spu_id);
			spu.setName(tmp_spu_name);
			spu.setNeed_pesticide_detect(1);
			result = categoryService.updateSpu(spu);
			Assert.assertEquals(result, true, "修改SPU信息,名称使用已经删除的SPU名称,断言成功");
		} catch (Exception e) {
			logger.error("修改SPU商品遇到错误: ", e);
			Assert.fail("修改SPU商品遇到错误: ", e);
		}

	}

	/**
	 * 修改SPU所属分类
	 */
	@Test
	public void spuUpdateTestCase04() {
		try {
			Reporter.log("测试点:修改SPU信息,修改所属分类");
			String category1_name = "蔬菜";
			Category1Bean category1 = categoryService.getCategory1ByName(category1_name);
			// 查询指定的一级分类是否存在,不存在就新建
			String tmp_category1_id = null;
			if (category1 == null) {
				category1 = new Category1Bean(category1_name, 1);
				tmp_category1_id = categoryService.createCategory1(category1);
				Assert.assertNotEquals(category1_id, null, "新建一级分类失败");
			} else {
				tmp_category1_id = category1.getId();

			}

			String category2_name = "叶菜";
			Category2Bean category2 = categoryService.getCategory2ByName(tmp_category1_id, category2_name);
			String tmp_category2_id = null;
			if (category2 == null) {
				category2 = new Category2Bean(category1_id, category2_name);
				tmp_category2_id = categoryService.createCategory2(category2);
				Assert.assertNotEquals(tmp_category2_id, null, "新建二级分类失败");
			} else {
				tmp_category2_id = category2.getId();
			}

			String pinlei_name = "菠菜";
			PinleiBean pinlei = categoryService.getPinleiByName(tmp_category2_id, pinlei_name);
			String tmp_pinlei_id = null;
			if (pinlei == null) {
				pinlei = new PinleiBean(tmp_category2_id, pinlei_name);
				tmp_pinlei_id = categoryService.createPinlei(pinlei);
				Assert.assertNotEquals(tmp_pinlei_id, null, "新建品类分类失败");
			} else {
				tmp_pinlei_id = pinlei.getId();
			}

			spu.setCategory_id_1(tmp_category1_id);
			spu.setCategory_name_1(category1_name);
			spu.setCategory_id_2(tmp_category2_id);
			spu.setCategory_name_2(category2_name);
			spu.setPinlei_id(tmp_pinlei_id);
			spu.setPinlei_name(pinlei_name);
			spu.setNeed_pesticide_detect(1);

			boolean result = categoryService.updateSpu(spu);
			Assert.assertEquals(result, true, "修改SPU信息,修改所属分类,断言成功");

			SpuBean tmp_spu = categoryService.getSpuById(spu_id);
			result = tmp_spu.getCategory_id_1().equals(spu.getCategory_id_1())
					&& tmp_spu.getCategory_id_2().equals(spu.getCategory_id_2())
					&& tmp_spu.getPinlei_id().equals(spu.getPinlei_id())
					&& tmp_spu.getCategory_name_1().equals(spu.getCategory_name_1())
					&& tmp_spu.getCategory_name_2().equals(spu.getCategory_name_2())
					&& tmp_spu.getPinlei_name().equals(spu.getPinlei_name());
			Assert.assertEquals(result, true, "修改SPU信息,修改所属分类后再次验证,验证失败");
		} catch (Exception e) {
			logger.error("修改SPU商品遇到错误: ", e);
			Assert.fail("修改SPU商品遇到错误: ", e);
		}

	}

	@Test
	public void spuUpdateTestCase05() {
		Reporter.log("测试点: 修改站点之前存在的SPU商品信息");
		try {
			SpuIndexFilterParam filterParam = new SpuIndexFilterParam();
			filterParam.setOffset(0);
			filterParam.setLimit(10);

			List<SpuIndexBean> spuIndexList = categoryService.searchSpuIndex(filterParam);
			Assert.assertNotEquals(spuIndexList, null, "商品库页面拉取数据失败");

			if (spuIndexList.size() > 0) {
				SpuIndexBean spuIndex = spuIndexList.get(0);
				String spu_id = spuIndex.getSpu_id();
				SpuBean spuBean = categoryService.getSpuById(spu_id);
				Assert.assertNotEquals(spuBean, null, "获取商品SPU " + spu_id + " 详细信息失败");

				boolean result = categoryService.updateSpu(spuBean);
				Assert.assertEquals(result, true, "修改商品SPU " + spu_id + " 详细信息失败");
			}
		} catch (Exception e) {
			logger.error("修改SPU商品遇到错误: ", e);
			Assert.fail("修改SPU商品遇到错误: ", e);
		}
	}

	@AfterMethod
	public void afterMethod() {
		try {
			if (tmp_spu_id != null) {
				boolean result = categoryService.deleteSpu(tmp_spu_id);
				Assert.assertEquals(result, true, "删除商品SPU失败");
			}
		} catch (Exception e) {
			logger.error("删除SPU商品遇到错误: ", e);
			Assert.fail("删除SPU商品遇到错误: ", e);
		}
	}

	@AfterTest
	public void afterTest() {
		try {
			if (spu_id != null) {
				boolean result = categoryService.deleteSpu(spu_id);
				Assert.assertEquals(result, true, "删除商品SPU失败");
			}
		} catch (Exception e) {
			logger.error("删除SPU商品遇到错误: ", e);
			Assert.fail("删除SPU商品遇到错误: ", e);
		}
	}
}
