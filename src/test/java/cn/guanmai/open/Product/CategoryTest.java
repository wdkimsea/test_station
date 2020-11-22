package cn.guanmai.open.Product;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.product.OpenCategory1Bean;
import cn.guanmai.open.bean.product.OpenCategory2Bean;
import cn.guanmai.open.bean.product.OpenPinleiBean;
import cn.guanmai.open.bean.product.OpenSpuBean;
import cn.guanmai.open.bean.product.param.OpenSpuCreateParam;
import cn.guanmai.open.bean.product.param.OpenSpuUpdateParam;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Jun 3, 2019 4:52:22 PM 
* @des 商品相关测试
* @version 1.0 
*/
public class CategoryTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(CategoryTest.class);
	private OpenCategoryService categoryService;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		categoryService = new OpenCategoryServiceImpl(access_token);
	}

	@Test
	public void categoryTestCase01() {
		Reporter.log("测试点: 获取商品一级分类列表");
		try {
			List<OpenCategory1Bean> category1List = categoryService.getCategory1List();
			Assert.assertNotNull(category1List, "获取商品一级分类失败");
		} catch (Exception e) {
			logger.error("获取商品一级分类遇到错误: ", e);
			Assert.fail("获取商品一级分类遇到错误: ", e);
		}
	}

	@Test
	public void categoryTestCase02() {
		Reporter.log("测试点: 获取商品二级分类列表");
		try {
			List<OpenCategory2Bean> category2List = categoryService.getCategory2List(null, null, null);
			Assert.assertNotNull(category2List, "获取商品二级分类失败");
		} catch (Exception e) {
			logger.error("获取商品二级分类遇到错误: ", e);
			Assert.fail("获取商品二级分类遇到错误: ", e);
		}
	}

	@Test
	public void categoryTestCase03() {
		Reporter.log("测试点: 获取商品品类分类列表");
		try {
			List<OpenCategory1Bean> category1List = categoryService.getCategory1List();
			Assert.assertNotNull(category1List, "获取商品一级分类失败");

			String category1_id = category1List.get(0).getId();
			List<OpenPinleiBean> pinleiList = categoryService.getPinleiList(category1_id, null, null, null);
			Assert.assertNotNull(pinleiList, "获取商品品类分类失败");
		} catch (Exception e) {
			logger.error("获取商品品类分类遇到错误: ", e);
			Assert.fail("获取商品品类分类遇到错误: ", e);
		}
	}

	@Test
	public void categoryTestCase04() {
		Reporter.log("测试点: 获取商品SPU分类列表");
		try {
			List<OpenCategory1Bean> category1List = categoryService.getCategory1List();
			Assert.assertNotNull(category1List, "获取商品一级分类失败");

			String category1_id = category1List.get(0).getId();
			List<OpenSpuBean> spuList = categoryService.getSpuBeanList(category1_id, null, null, null, null);
			Assert.assertNotNull(spuList, "获取商品品类分类失败");
		} catch (Exception e) {
			logger.error("获取商品SPU分类遇到错误: ", e);
			Assert.fail("获取商品SPU分类遇到错误: ", e);
		}
	}

	@Test
	public void categoryTestCase05() {
		Reporter.log("测试点: 新建商品SPU");
		String spu_id = null;
		try {
			List<OpenPinleiBean> pinleiList = categoryService.getPinleiList(null, null, null, null);
			Assert.assertNotNull(pinleiList, "获取商品品类分类失败");

			Assert.assertEquals(pinleiList.size() > 0, true, "此站点无商品品类分类,无法进行商品SPU创建");

			String pinlei_id = pinleiList.get(0).getId();
			OpenSpuCreateParam createParam = new OpenSpuCreateParam();
			createParam.setPinlei_id(pinlei_id);

			String spu_name = StringUtil.getRandomString(4).toUpperCase();
			createParam.setName(spu_name);

			createParam.setDispatch_method("1");

			String std_unit_name = "斤";
			createParam.setStd_unit_name(std_unit_name);

			String desc = StringUtil.getRandomNumber(12);
			createParam.setDesc(desc);

			spu_id = categoryService.createSpu(createParam);
			Assert.assertNotEquals(spu_id, null, "创建商品SPU失败");

			OpenSpuBean spuDetail = categoryService.getSpuBean(spu_id);
			Assert.assertNotNull(spuDetail, "获取刚刚创建的SPU详细信息失败");

			boolean result = true;
			String msg = null;
			if (!spuDetail.getName().equals(spu_name)) {
				msg = String.format("创建SPU输入的名称和查询到的名称不一致,预期:%s,实际:%s", spu_name, spuDetail.getName());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			if (!spuDetail.getDesc().equals(desc)) {
				msg = String.format("创建SPU输入的描述信息和查询到的描述信息名称不一致,预期:%s,实际:%s", desc, spuDetail.getDesc());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			if (spuDetail.getP_type() != 0) { // 判断p_type类型(0-通用, 1-本站可见)
				msg = String.format("创建SPU输入的分拣类型和查询到的分拣类型值不一致,预期:%s,实际:%s", 0, spuDetail.getP_type());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			if (!spuDetail.getStd_unit_name().equals(std_unit_name)) {
				msg = String.format("创建SPU输入的基本单位和查询到的基本单位值不一致,预期:%s,实际:%s", std_unit_name,
						spuDetail.getStd_unit_name());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "创建SPU输入的信息和创建后查询到的SPU基本信息不一致 " + msg);
		} catch (Exception e) {
			logger.error("新建商品SPU遇到错误: ", e);
			Assert.fail("新建商品SPU遇到错误: ", e);
		} finally {
			if (spu_id != null) {
				try {
					boolean reuslt = categoryService.deleteSpu(spu_id);
					Assert.assertEquals(reuslt, true, "删除SPU遇到错误");
				} catch (Exception e) {
					logger.error("后置处理,删除商品SPU遇到错误: ", e);
					Assert.fail("后置处理,删除商品SPU遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void categoryTestCase06() {
		Reporter.log("测试点: 修改商品SPU");
		try {
			List<OpenCategory1Bean> category1List = categoryService.getCategory1List();
			Assert.assertNotNull(category1List, "获取商品一级分类失败");

			String category1_id = category1List.get(0).getId();
			List<OpenSpuBean> spuList = categoryService.getSpuBeanList(category1_id, null, null, null, null);
			Assert.assertNotNull(spuList, "获取商品品类分类失败");

			Assert.assertEquals(spuList.size() > 0, true, "本站点没有一个SPU");

			String spu_id = spuList.get(0).getId();
			OpenSpuBean spu = categoryService.getSpuBean(spu_id);
			Assert.assertNotEquals(spu, null, "获取指定SPU的详细信息失败");

			OpenSpuUpdateParam updateParam = new OpenSpuUpdateParam();
			updateParam.setSpu_id(spu_id);

			String new_desc = StringUtil.getRandomNumber(16);
			updateParam.setDesc(new_desc);

			String new_name = spu.getName() + StringUtil.getRandomString(2).toUpperCase();
			updateParam.setName(new_name);

			boolean result = categoryService.updateSpu(updateParam);
			Assert.assertEquals(result, true, "更新商品SPU信息失败");

			OpenSpuBean temp_spu = categoryService.getSpuBean(spu_id);

			String msg = null;
			if (!temp_spu.getName().equals(new_name)) {
				msg = String.format("修改SPU输入的名称和查询到的名称不一致,预期:%s,实际:%s", new_name, temp_spu.getName());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			if (!temp_spu.getDesc().equals(new_desc)) {
				msg = String.format("修改SPU输入的描述信息和查询到的描述信息不一致,预期:%s,实际:%s", new_desc, temp_spu.getDesc());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}
		} catch (Exception e) {
			logger.error("修改商品SPU详细信息遇到错误: ", e);
			Assert.fail("修改商品SPU详细信息遇到错误: ", e);
		}
	}

	@Test
	public void categoryTestCase07() {
		Reporter.log("测试点: 删除商品SPU");
		try {
			List<OpenPinleiBean> pinleiList = categoryService.getPinleiList(null, null, null, null);
			Assert.assertNotNull(pinleiList, "获取商品品类分类失败");

			Assert.assertEquals(pinleiList.size() > 0, true, "此站点无商品品类分类,无法进行商品SPU创建");

			String pinlei_id = pinleiList.get(0).getId();
			OpenSpuCreateParam createParam = new OpenSpuCreateParam();
			createParam.setPinlei_id(pinlei_id);

			String spu_name = StringUtil.getRandomString(4).toUpperCase();
			createParam.setName(spu_name);

			createParam.setDispatch_method("2");

			String std_unit_name = "斤";
			createParam.setStd_unit_name(std_unit_name);

			String desc = StringUtil.getRandomNumber(12);
			createParam.setDesc(desc);

			String spu_id = categoryService.createSpu(createParam);
			Assert.assertNotEquals(spu_id, null, "创建商品SPU失败");

			boolean result = categoryService.deleteSpu(spu_id);
			Assert.assertEquals(result, true, "删除商品SPU失败");

			List<OpenSpuBean> spuList = categoryService.getSpuBeanList(null, null, pinlei_id, null, null);
			Assert.assertNotEquals(spuList, null, "获取SPU列表失败");

			OpenSpuBean temp_spu = spuList.stream().filter(s -> s.getId().equals(spu_id)).findAny().orElse(null);
			Assert.assertEquals(temp_spu, null, "删除的SPU商品,还可以在商品列表获取到其信息");

			temp_spu = categoryService.getSpuBean(spu_id);
			Assert.assertEquals(temp_spu, null, "删除的SPU商品,还可以获取到其详细信息");
		} catch (Exception e) {
			logger.error("删除商品SPU遇到错误: ", e);
			Assert.fail("删除商品SPU遇到错误: ", e);
		}
	}

	@Test
	public void categoryTestCase08() {
		Reporter.log("测试点: 获取商品SPU基本单位集合");
		try {
			List<String> std_unit_name_list = categoryService.get_std_unit_name_map();
			Assert.assertNotEquals(std_unit_name_list, null, "获取商品SPU基本单位集合失败");
		} catch (Exception e) {
			logger.error("获取商品SPU基本单位集合遇到错误: ", e);
			Assert.fail("获取商品SPU基本单位集合遇到错误: ", e);
		}
	}

}
