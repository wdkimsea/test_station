package cn.guanmai.station.category;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.MainTest;
import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.category.CombineGoodsDetailBean;
import cn.guanmai.station.bean.category.SalemenuBean;
import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.category.SalemenuSkuBean;
import cn.guanmai.station.bean.category.SkuSimpleBean;
import cn.guanmai.station.bean.category.SpuSimpleBean;
import cn.guanmai.station.bean.category.param.CombineGoodsParam;
import cn.guanmai.station.bean.category.param.SalemenuFilterParam;
import cn.guanmai.station.bean.category.param.SkuFilterParam;
import cn.guanmai.station.bean.category.param.SalemenuSkuFilterParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.base.InitDataServiceImpl;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.category.CombineGoodsServiceImpl;
import cn.guanmai.station.impl.category.SalemenuServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.base.InitDataService;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.category.CombineGoodsService;
import cn.guanmai.station.interfaces.category.SalemenuService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author: liming
 * @Date: 2020年2月17日 下午7:20:03
 * @description:
 * @version: 1.0
 */

public class CombineGoodsCreateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(CombineGoodsCreateTest.class);

	private CombineGoodsService combineGoodsService;
	private CategoryService categoryService;
	private SalemenuService salemenuService;
	private List<String> salemenu_ids;
	private String combine_goods_id;
	private InitDataBean initData = MainTest.initData;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		combineGoodsService = new CombineGoodsServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		salemenuService = new SalemenuServiceImpl(headers);
		LoginUserInfoService loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		try {
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账号相关信息失败");

			JSONArray user_permission = loginUserInfo.getUser_permission();
			Assert.assertEquals(user_permission.contains("add_combine_goods"), true, "此登录账号无创建组合商品权限");

			if (initData == null) {
				InitDataService initDataService = new InitDataServiceImpl(headers);
				initData = initDataService.getInitData();
				Assert.assertNotEquals(initData, null, "初始化站点数据失败");
			}

			SalemenuFilterParam salemenuFilterParam = new SalemenuFilterParam();
			salemenuFilterParam.setType(1);
			salemenuFilterParam.setWith_sku_num(1);

			List<SalemenuBean> salemenus = salemenuService.searchSalemenu(salemenuFilterParam);
			Assert.assertNotEquals(salemenus, null, "搜索过滤报价单失败");

			salemenu_ids = salemenus.stream().filter(s -> s.getSku_num() >= 2).map(s -> s.getId())
					.collect(Collectors.toList());
			Assert.assertEquals(salemenu_ids.size() > 2, true, "此站点商品数目大于2的报价单不足两个");
		} catch (Exception e) {
			logger.error("初始化数据过程中遇到错误", e);
			Assert.fail("初始化数据过程中遇到错误", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		combine_goods_id = null;
	}

	@Test
	public void combineGoodsCreateTestCase01() {
		ReporterCSS.title("测试点: 新建组合商品,搜索SPU");
		try {
			List<SpuSimpleBean> spuSimples = categoryService.searchSimpleSpu("鱼");
			Assert.assertNotEquals(spuSimples, null, "组合商品,搜索SPU失败");
		} catch (Exception e) {
			logger.error("组合商品,搜索SPU遇到错误", e);
			Assert.fail("组合商品,搜索SPU遇到错误", e);
		}
	}

	@Test
	public void combineGoodsCreateTestCase02() {
		ReporterCSS.title("测试点: 新建组合商品,搜索销售SKU");
		try {
			String salemenu_id = NumberUtil.roundNumberInList(salemenu_ids);
			SalemenuSkuFilterParam salemenuSkuFilterParam = new SalemenuSkuFilterParam();
			salemenuSkuFilterParam.setSalemenu_id(salemenu_id);

			List<SalemenuSkuBean> skuSalemenuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuSalemenuList, null, "报价单商品列表页面拉取销售SKU失败");

			SalemenuSkuBean skuSalemenu = NumberUtil.roundNumberInList(skuSalemenuList);
			String spu_id = skuSalemenu.getSpu_id();
			String sku_id = skuSalemenu.getSku_id();

			SkuFilterParam skuFilterParam = new SkuFilterParam();
			skuFilterParam.setSalemenu_id(salemenu_id);
			skuFilterParam.setSpu_id(spu_id);
			skuFilterParam.setLimit(30);

			List<SkuSimpleBean> skus = categoryService.searchSaleSku(skuFilterParam);
			Assert.assertNotEquals(skus, null, "新建组合商品,搜索销售SKU遇到错误");

			SkuSimpleBean skuSimple = skus.stream().filter(s -> s.getId().equals(sku_id)).findAny().orElse(null);
			Assert.assertNotEquals(skuSimple, null, "新建组合商品,搜索销售SKU与预期不符");
		} catch (Exception e) {
			logger.error("新建组合商品,搜索销售SKU遇到错误", e);
			Assert.fail("新建组合商品,搜索销售SKU遇到错误", e);
		}
	}

	@Test
	public void combineGoodsCreateTestCase03() {
		ReporterCSS.title("测试点: 新建组合商品(单个SKU)");
		try {
			String salemenu_id = NumberUtil.roundNumberInList(salemenu_ids);
			SalemenuSkuFilterParam salemenuSkuFilterParam = new SalemenuSkuFilterParam();
			salemenuSkuFilterParam.setSalemenu_id(salemenu_id);

			List<SalemenuSkuBean> skuSalemenuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuSalemenuList, null, "报价单商品列表页面拉取销售SKU失败");

			SalemenuSkuBean skuSalemenu = NumberUtil.roundNumberInList(skuSalemenuList);
			String spu_id = skuSalemenu.getSpu_id();
			String sku_id = skuSalemenu.getSku_id();

			CombineGoodsParam combineGoodsCreateParam = new CombineGoodsParam();
			String name = "AT_" + StringUtil.getRandomNumber(6);
			combineGoodsCreateParam.setName(name);
			String sale_unit_name = "包";
			combineGoodsCreateParam.setSale_unit_name(sale_unit_name);
			combineGoodsCreateParam.setSalemenu_ids(Arrays.asList(salemenu_id));
			combineGoodsCreateParam.setState(1);
			String desc = StringUtil.getRandomNumber(6);
			combineGoodsCreateParam.setDesc(desc);

			List<CombineGoodsParam.Spu> spus = new ArrayList<CombineGoodsParam.Spu>();
			CombineGoodsParam.Spu spu = combineGoodsCreateParam.new Spu();
			spu.setSpu_id(spu_id);
			BigDecimal spu_quantity = NumberUtil.getRandomNumber(5, 10, 1);
			spu.setQuantity(spu_quantity);
			spus.add(spu);
			combineGoodsCreateParam.setSpus(spus);

			List<CombineGoodsParam.Sku> skus = new ArrayList<CombineGoodsParam.Sku>();
			CombineGoodsParam.Sku sku = combineGoodsCreateParam.new Sku();
			sku.setSalemenu_id(salemenu_id);
			sku.setSku_id(sku_id);
			sku.setSpu_id(spu_id);
			skus.add(sku);
			combineGoodsCreateParam.setSkus(skus);

			combineGoodsCreateParam.setImages(Arrays.asList("NB.jpg"));
			combine_goods_id = combineGoodsService.createCombineGoods(combineGoodsCreateParam);
			Assert.assertNotEquals(combine_goods_id, null, "新建组合商品失败");

			CombineGoodsDetailBean combineGoodsDetail = combineGoodsService.getCombineGoodsDetail(combine_goods_id);
			Assert.assertNotEquals(combineGoodsDetail, null, "获取组合商品" + combine_goods_id + "详细信息失败");

			boolean result = compareResult(combineGoodsCreateParam, combineGoodsDetail);
			Assert.assertEquals(result, true, "新建组合商品填写的商品信息与查询到的不一致");
		} catch (Exception e) {
			logger.error("新建组合商品遇到错误", e);
			Assert.fail("新建组合商品遇到错误", e);
		}
	}

	@Test
	public void combineGoodsCreateTestCase04() {
		ReporterCSS.title("测试点: 新建组合商品(多个SKU)");
		try {
			String salemenu_id = NumberUtil.roundNumberInList(salemenu_ids);
			SalemenuSkuFilterParam salemenuSkuFilterParam = new SalemenuSkuFilterParam();
			salemenuSkuFilterParam.setSalemenu_id(salemenu_id);

			List<SalemenuSkuBean> skuSalemenuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuSalemenuList, null, "报价单商品列表页面拉取销售SKU失败");

			CombineGoodsParam combineGoodsCreateParam = new CombineGoodsParam();
			String name = "AT_" + StringUtil.getRandomNumber(6);
			combineGoodsCreateParam.setName(name);
			String sale_unit_name = "包";
			combineGoodsCreateParam.setSale_unit_name(sale_unit_name);
			combineGoodsCreateParam.setSalemenu_ids(Arrays.asList(salemenu_id));
			combineGoodsCreateParam.setState(1);
			String desc = StringUtil.getRandomNumber(6);
			combineGoodsCreateParam.setDesc(desc);

			List<CombineGoodsParam.Spu> spus = new ArrayList<CombineGoodsParam.Spu>();
			List<CombineGoodsParam.Sku> skus = new ArrayList<CombineGoodsParam.Sku>();

			List<String> spu_ids = new ArrayList<String>();
			for (SalemenuSkuBean skuSalemenu : skuSalemenuList) {
				String spu_id = skuSalemenu.getSpu_id();
				String sku_id = skuSalemenu.getSku_id();
				if (spu_ids.contains(spu_id)) {
					continue;
				} else {
					spu_ids.add(spu_id);
				}

				CombineGoodsParam.Spu spu = combineGoodsCreateParam.new Spu();
				spu.setSpu_id(spu_id);
				BigDecimal spu_quantity = NumberUtil.getRandomNumber(5, 10, 1);
				spu.setQuantity(spu_quantity);
				spus.add(spu);

				CombineGoodsParam.Sku sku = combineGoodsCreateParam.new Sku();
				sku.setSalemenu_id(salemenu_id);
				sku.setSku_id(sku_id);
				sku.setSpu_id(spu_id);
				skus.add(sku);

				if (spus.size() >= 4) {
					break;
				}
			}
			combineGoodsCreateParam.setSpus(spus);
			combineGoodsCreateParam.setSkus(skus);

			combineGoodsCreateParam.setImages(new ArrayList<String>());
			combine_goods_id = combineGoodsService.createCombineGoods(combineGoodsCreateParam);
			Assert.assertNotEquals(combine_goods_id, null, "新建组合商品失败");

			CombineGoodsDetailBean combineGoodsDetail = combineGoodsService.getCombineGoodsDetail(combine_goods_id);
			Assert.assertNotEquals(combineGoodsDetail, null, "获取组合商品" + combine_goods_id + "详细信息失败");

			boolean result = compareResult(combineGoodsCreateParam, combineGoodsDetail);
			Assert.assertEquals(result, true, "新建组合商品填写的商品信息与查询到的不一致");
		} catch (Exception e) {
			logger.error("新建组合商品遇到错误", e);
			Assert.fail("新建组合商品遇到错误", e);
		}
	}

	@Test
	public void combineGoodsCreateTestCase05() {
		ReporterCSS.title("测试点: 新建组合商品(多报价单)");
		String other_sku_id = null;
		try {
			String salemenu_id = null;
			for (String id : salemenu_ids) {
				if (!id.equals(initData.getSalemenu().getId())) {
					salemenu_id = id;
					break;
				}
			}
			SalemenuSkuFilterParam salemenuSkuFilterParam = new SalemenuSkuFilterParam();
			salemenuSkuFilterParam.setSalemenu_id(salemenu_id);

			List<SalemenuSkuBean> skuSalemenuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuSalemenuList, null, "报价单商品列表页面拉取销售SKU失败");

			SalemenuSkuBean skuSalemenu = NumberUtil.roundNumberInList(skuSalemenuList);
			String spu_id = skuSalemenu.getSpu_id();
			String sku_id = skuSalemenu.getSku_id();

			// 创建同SPU下的SKU
			SkuBean other_sku = new SkuBean();
			other_sku.setSpu_id(spu_id);
			other_sku.setOuter_id("");
			BigDecimal std_sale_price = new BigDecimal("4");
			other_sku.setStd_sale_price(std_sale_price);
			other_sku.setPartframe(1);
			other_sku.setStd_unit_name(skuSalemenu.getStd_unit_name());
			other_sku.setSlitting(1);
			other_sku.setSale_num_least(new BigDecimal("1"));
			other_sku.setStocks("-99999");
			other_sku.setSale_ratio(new BigDecimal("1"));
			other_sku.setSale_unit_name(skuSalemenu.getStd_unit_name());
			other_sku.setDesc("LD");
			other_sku.setSupplier_id(initData.getSupplier().getId());
			other_sku.setIs_price_timing(0);
			other_sku.setIs_weigh(1);
			other_sku.setPurchase_spec_id(initData.getPurchaseSpec().getId());
			other_sku.setAttrition_rate(BigDecimal.ZERO);
			other_sku.setStock_type(1);
			other_sku.setName(skuSalemenu.getSpu_name() + "|" + skuSalemenu.getStd_unit_name());
			other_sku.setSalemenu_id(initData.getSalemenu().getId());
			other_sku.setState(1);
			other_sku.setSale_price(std_sale_price.multiply(other_sku.getSale_ratio()));
			other_sku.setRemark_type(7);

			other_sku_id = categoryService.createSaleSku(other_sku);
			Assert.assertEquals(other_sku_id != null, true, "新建销售SKU失败");

			CombineGoodsParam combineGoodsCreateParam = new CombineGoodsParam();
			String name = "AT_" + StringUtil.getRandomNumber(6);
			combineGoodsCreateParam.setName(name);
			String sale_unit_name = "包";
			combineGoodsCreateParam.setSale_unit_name(sale_unit_name);
			List<String> salemenu_ids = new ArrayList<String>();
			salemenu_ids.add(salemenu_id);
			salemenu_ids.add(initData.getSalemenu().getId());
			combineGoodsCreateParam.setSalemenu_ids(salemenu_ids);
			combineGoodsCreateParam.setState(1);
			String desc = StringUtil.getRandomNumber(6);
			combineGoodsCreateParam.setDesc(desc);

			List<CombineGoodsParam.Spu> spus = new ArrayList<CombineGoodsParam.Spu>();
			CombineGoodsParam.Spu spu = combineGoodsCreateParam.new Spu();
			spu.setSpu_id(spu_id);
			BigDecimal spu_quantity = NumberUtil.getRandomNumber(5, 10, 1);
			spu.setQuantity(spu_quantity);
			spus.add(spu);
			combineGoodsCreateParam.setSpus(spus);

			List<CombineGoodsParam.Sku> skus = new ArrayList<CombineGoodsParam.Sku>();
			CombineGoodsParam.Sku sku1 = combineGoodsCreateParam.new Sku();
			sku1.setSalemenu_id(salemenu_id);
			sku1.setSku_id(sku_id);
			sku1.setSpu_id(spu_id);
			skus.add(sku1);

			CombineGoodsParam.Sku sku2 = combineGoodsCreateParam.new Sku();
			sku2.setSalemenu_id(initData.getSalemenu().getId());
			sku2.setSku_id(other_sku_id);
			sku2.setSpu_id(spu_id);
			skus.add(sku2);
			combineGoodsCreateParam.setSkus(skus);

			combineGoodsCreateParam.setImages(new ArrayList<String>());
			combine_goods_id = combineGoodsService.createCombineGoods(combineGoodsCreateParam);
			Assert.assertNotEquals(combine_goods_id, null, "新建组合商品失败");

			CombineGoodsDetailBean combineGoodsDetail = combineGoodsService.getCombineGoodsDetail(combine_goods_id);
			Assert.assertNotEquals(combineGoodsDetail, null, "获取组合商品" + combine_goods_id + "详细信息失败");

			boolean result = compareResult(combineGoodsCreateParam, combineGoodsDetail);
			Assert.assertEquals(result, true, "新建组合商品填写的商品信息与查询到的不一致");
		} catch (Exception e) {
			logger.error("新建组合商品遇到错误", e);
			Assert.fail("新建组合商品遇到错误", e);
		} finally {
			try {
				if (other_sku_id != null) {
					boolean result = categoryService.deleteSaleSku(other_sku_id);
					Assert.assertEquals(result, true, "删除销售SKU失败");
				}
			} catch (Exception e) {
				logger.error("后置处理,删除销售SKU遇到错误", e);
				Assert.fail("后置处理,删除销售SKU遇到错误", e);
			}
		}
	}

	private boolean compareResult(CombineGoodsParam combineGoodsCreateParam,
			CombineGoodsDetailBean combineGoodsDetail) {
		String msg = null;
		boolean result = true;
		if (!combineGoodsCreateParam.getName().equals(combineGoodsDetail.getName())) {
			msg = String.format("新建组合商品填写的名称和查询到的不一致,预期:%s,实际:%s", combineGoodsCreateParam.getName(),
					combineGoodsDetail.getName());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!combineGoodsCreateParam.getSale_unit_name().equals(combineGoodsDetail.getSale_unit_name())) {
			msg = String.format("新建组合商品填写的单位和查询到的不一致,预期:%s,实际:%s", combineGoodsCreateParam.getSale_unit_name(),
					combineGoodsDetail.getSale_unit_name());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!combineGoodsCreateParam.getDesc().equals(combineGoodsDetail.getDesc())) {
			msg = String.format("新建组合商品填写的描述信息和查询到的不一致,预期:%s,实际:%s", combineGoodsCreateParam.getDesc(),
					combineGoodsDetail.getDesc());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (combineGoodsCreateParam.getState() != combineGoodsDetail.getState()) {
			msg = String.format("新建组合商品填写的状态信息和查询到的不一致,预期:%s,实际:%s", combineGoodsCreateParam.getState(),
					combineGoodsDetail.getState());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		for (String img : combineGoodsCreateParam.getImages()) {
			if (!combineGoodsDetail.getImages().toString().contains(img)) {
				msg = String.format("新建组合商品填写的图片信息 %s没有查询到", img);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
		}

		Map<String, CombineGoodsDetailBean.Spu> actual_spu_map = combineGoodsDetail.getSpus();
		for (CombineGoodsParam.Spu spu : combineGoodsCreateParam.getSpus()) {
			if (actual_spu_map.containsKey(spu.getSpu_id())) {
				CombineGoodsDetailBean.Spu actual_spu = actual_spu_map.get(spu.getSpu_id());
				if (actual_spu.getQuantity().compareTo(spu.getQuantity()) != 0) {
					msg = String.format("新建组合商品填写的SPU %s 对应的单位数量与预期的不一致,预期:%s,实际:%s", spu.getSpu_id(),
							spu.getQuantity(), actual_spu.getQuantity());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				msg = String.format("新建组合商品填写的SPU %s 在组合商品详情里没有找到", spu.getSpu_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
		}

		List<CombineGoodsDetailBean.Sku> actual_skus = combineGoodsDetail.getSkus();
		for (CombineGoodsParam.Sku sku : combineGoodsCreateParam.getSkus()) {
			CombineGoodsDetailBean.Sku actual_sku = actual_skus.stream().filter(s -> s.getId().equals(sku.getSku_id()))
					.findAny().orElse(null);
			if (actual_sku == null) {
				msg = String.format("新建组合商品填写的SKU %s 在组合商品详情里没有找到", sku.getSku_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else {
				if (!sku.getSalemenu_id().equals(actual_sku.getSalemenu_id())) {
					msg = String.format("新建组合商品填写的SKU %s 对应的报价单ID与预期不一致,预期:%s,实际:%s", sku.getSku_id(),
							sku.getSalemenu_id(), actual_sku.getSalemenu_id());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (!sku.getSpu_id().equals(actual_sku.getSpu_id())) {
					msg = String.format("新建组合商品填写的SKU %s 对应的SPU与预期不一致,预期:%s,实际:%s", sku.getSku_id(), sku.getSpu_id(),
							actual_sku.getSpu_id());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
		}

		return result;

	}

	@AfterMethod
	public void afterMethod() {
		ReporterCSS.title("后置处理,删除新建的组合商品");
		try {
			if (combine_goods_id != null) {
				boolean result = combineGoodsService.deleteCombineGoods(combine_goods_id);
				Assert.assertEquals(result, true, "删除组合商品 " + combine_goods_id + " 失败");
			}
		} catch (Exception e) {
			logger.error("后置处理,删除新建的组合商品遇到错误", e);
			Assert.fail("后置处理,删除新建的组合商品遇到错误", e);
		}
	}

}
