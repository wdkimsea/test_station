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

import cn.guanmai.station.bean.category.CombineGoodsBean;
import cn.guanmai.station.bean.category.SalemenuBean;
import cn.guanmai.station.bean.category.SkuPromotionBean;
import cn.guanmai.station.bean.category.SalemenuSkuBean;
import cn.guanmai.station.bean.category.SmartMenuDetailBean;
import cn.guanmai.station.bean.category.param.CombineGoodsParam;
import cn.guanmai.station.bean.category.param.SalemenuFilterParam;
import cn.guanmai.station.bean.category.param.SalemenuSkuFilterParam;
import cn.guanmai.station.bean.category.param.SmartMenuParam;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.category.CombineGoodsServiceImpl;
import cn.guanmai.station.impl.category.SalemenuServiceImpl;
import cn.guanmai.station.impl.category.SmartMenuServiceImpl;
import cn.guanmai.station.impl.marketing.PromotionServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.category.CombineGoodsService;
import cn.guanmai.station.interfaces.category.SalemenuService;
import cn.guanmai.station.interfaces.category.SmartMenuService;
import cn.guanmai.station.interfaces.marketing.PromotionService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author: liming
 * @Date: 2020年3月3日 上午11:05:53
 * @description:
 * @version: 1.0
 */

public class SmartMenuCreateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SmartMenuCreateTest.class);

	private SmartMenuService smartMenuService;
	private PromotionService promotionService;
	private CombineGoodsService combineGoodsService;
	private CategoryService categoryService;
	private SalemenuService salemenuService;
	private String id;

	@BeforeClass
	private void initData() {
		Map<String, String> headers = getStationCookie();
		smartMenuService = new SmartMenuServiceImpl(headers);
		promotionService = new PromotionServiceImpl(headers);
		combineGoodsService = new CombineGoodsServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		salemenuService = new SalemenuServiceImpl(headers);
	}

	@BeforeMethod
	public void beforeMethod() {
		id = null;
	}

	@Test
	public void smartMenuCreateTestCase01() {
		ReporterCSS.title("测试点: 新建智能菜单(添加普通商品)");
		try {
			List<SkuPromotionBean> skuPromotions = promotionService.promotionSkus();
			Assert.assertNotEquals(skuPromotions, null, "拉取站点所有的销售SKU失败");
			Assert.assertEquals(skuPromotions.size() > 0, true, "此站点无销售SKU,无法新建智能菜单");

			List<String> sku_ids = null;
			if (skuPromotions.size() > 2) {
				List<SkuPromotionBean> selectedSkuPromotions = NumberUtil.roundNumberInList(skuPromotions, 2);
				sku_ids = selectedSkuPromotions.stream().map(s -> s.getId()).collect(Collectors.toList());
			} else {
				SkuPromotionBean skuPromotion = NumberUtil.roundNumberInList(skuPromotions);
				sku_ids = Arrays.asList(skuPromotion.getId());
			}

			String name = StringUtil.getRandomString(6).toUpperCase();
			SmartMenuParam smartMenuParam = new SmartMenuParam();
			smartMenuParam.setName(name);
			smartMenuParam.setSku_ids(sku_ids);

			id = smartMenuService.createSmartMenu(smartMenuParam);
			Assert.assertNotEquals(id, null, "新建智能菜单失败");

			SmartMenuDetailBean smartMenuDetail = smartMenuService.getSmartMenuDetail(id);
			Assert.assertNotEquals(smartMenuDetail, null, "获取智能菜单 " + id + " 详细信息失败");

			String msg = null;
			boolean result = true;
			if (!smartMenuDetail.getName().equals(name)) {
				msg = String.format("新建智能菜单填写的名称和新建完查看到的不一致,预期:%s,实际:%s", name, smartMenuDetail.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<SmartMenuDetailBean.Sku> skus = smartMenuDetail.getSkus();
			List<String> actual_sku_ids = skus.stream().map(s -> s.getId()).collect(Collectors.toList());
			msg = String.format("新建智能菜单填写的商品列表和新建完查看到的不一致,预期:%s,实际:%s", sku_ids, actual_sku_ids);
			if (actual_sku_ids.size() == sku_ids.size()) {
				if (!actual_sku_ids.containsAll(sku_ids)) {
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "新建智能菜单填写的信息与查看到的不一致");
		} catch (Exception e) {
			logger.error("拉取站点所有的组合商品遇到错误: ", e);
			Assert.fail("拉取站点所有的组合商品遇到错误: ", e);
		}
	}

	@Test
	public void smartMenuCreateTestCase02() {
		ReporterCSS.title("测试点: 新建智能菜单(添加组合商品)");
		try {
			List<CombineGoodsBean> combineGoodsList = combineGoodsService.promotionCombineGoods();
			Assert.assertNotEquals(combineGoodsList, null, "拉取站点所有的组合商品失败");

			List<String> combine_good_ids = new ArrayList<String>();
			if (combineGoodsList.size() > 0) {
				CombineGoodsBean combineGood = NumberUtil.roundNumberInList(combineGoodsList);
				String id = combineGood.getId();
				combine_good_ids.add(id);
			} else {
				SalemenuFilterParam salemenuFilterParam = new SalemenuFilterParam();
				salemenuFilterParam.setType(1);
				salemenuFilterParam.setWith_sku_num(1);

				List<SalemenuBean> salemenus = salemenuService.searchSalemenu(salemenuFilterParam);
				Assert.assertNotEquals(salemenus, null, "搜索过滤报价单失败");

				List<String> salemenu_ids = salemenus.stream().filter(s -> s.getSku_num() >= 2).map(s -> s.getId())
						.collect(Collectors.toList());
				Assert.assertEquals(salemenu_ids.size() > 1, true, "此站点没有商品数目大于2的报价单");

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
				String combine_good_id = combineGoodsService.createCombineGoods(combineGoodsCreateParam);
				Assert.assertNotEquals(combine_good_id, null, "新建组合商品失败");

				combineGoodsList = combineGoodsService.promotionCombineGoods();
				Assert.assertNotEquals(combineGoodsList, null, "拉取站点所有的组合商品失败");

				CombineGoodsBean combineGoods = combineGoodsList.stream().filter(s -> s.getId().equals(combine_good_id))
						.findAny().orElse(null);
				Assert.assertNotEquals(combineGoods, null, "新建的组合商品没有在新建智能菜单页面展示");
				combine_good_ids.add(combine_good_id);
			}

			String name = StringUtil.getRandomString(6).toUpperCase();
			SmartMenuParam smartMenuParam = new SmartMenuParam();
			smartMenuParam.setName(name);
			smartMenuParam.setCombine_good_ids(combine_good_ids);

			id = smartMenuService.createSmartMenu(smartMenuParam);
			Assert.assertNotEquals(id, null, "新建智能菜单失败");

			SmartMenuDetailBean smartMenuDetail = smartMenuService.getSmartMenuDetail(id);
			Assert.assertNotEquals(smartMenuDetail, null, "获取智能菜单 " + id + " 详细信息失败");

			String msg = null;
			boolean result = true;
			if (!smartMenuDetail.getName().equals(name)) {
				msg = String.format("新建智能菜单填写的名称和新建完查看到的不一致,预期:%s,实际:%s", name, smartMenuDetail.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<SmartMenuDetailBean.CombineGoods> combine_goods = smartMenuDetail.getCombine_goods();
			List<String> actual_combine_good_ids = combine_goods.stream().map(s -> s.getId())
					.collect(Collectors.toList());
			msg = String.format("新建智能菜单填写的商品列表和新建完查看到的不一致,预期:%s,实际:%s", combine_good_ids, actual_combine_good_ids);
			if (combine_good_ids.size() == actual_combine_good_ids.size()) {
				if (!actual_combine_good_ids.containsAll(combine_good_ids)) {
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "新建智能菜单填写的信息与查看到的不一致");
		} catch (Exception e) {
			logger.error("拉取站点所有的组合商品遇到错误: ", e);
			Assert.fail("拉取站点所有的组合商品遇到错误: ", e);
		}
	}

	@Test
	public void smartMenuCreateTestCase03() {
		ReporterCSS.title("测试点: 新建智能菜单(添加普通商品+组合商品)");
		try {
			List<SkuPromotionBean> skuPromotions = promotionService.promotionSkus();
			Assert.assertNotEquals(skuPromotions, null, "拉取站点所有的销售SKU失败");
			Assert.assertEquals(skuPromotions.size() > 0, true, "此站点无销售SKU,无法新建智能菜单");

			SkuPromotionBean skuPromotion = NumberUtil.roundNumberInList(skuPromotions);
			List<String> sku_ids = Arrays.asList(skuPromotion.getId());

			List<CombineGoodsBean> combineGoodsList = combineGoodsService.promotionCombineGoods();
			Assert.assertNotEquals(combineGoodsList, null, "拉取站点所有的组合商品失败");

			List<String> combine_good_ids = new ArrayList<String>();
			if (combineGoodsList.size() > 0) {
				CombineGoodsBean combineGood = NumberUtil.roundNumberInList(combineGoodsList);
				String id = combineGood.getId();
				combine_good_ids.add(id);
			} else {
				SalemenuFilterParam salemenuFilterParam = new SalemenuFilterParam();
				salemenuFilterParam.setType(1);
				salemenuFilterParam.setWith_sku_num(1);

				List<SalemenuBean> salemenus = salemenuService.searchSalemenu(salemenuFilterParam);
				Assert.assertNotEquals(salemenus, null, "搜索过滤报价单失败");

				List<String> salemenu_ids = salemenus.stream().filter(s -> s.getSku_num() >= 2).map(s -> s.getId())
						.collect(Collectors.toList());
				Assert.assertEquals(salemenu_ids.size() > 1, true, "此站点没有商品数目大于2的报价单");

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
				String combine_good_id = combineGoodsService.createCombineGoods(combineGoodsCreateParam);
				Assert.assertNotEquals(combine_good_id, null, "新建组合商品失败");

				combineGoodsList = combineGoodsService.promotionCombineGoods();
				Assert.assertNotEquals(combineGoodsList, null, "拉取站点所有的组合商品失败");

				CombineGoodsBean combineGoods = combineGoodsList.stream().filter(s -> s.getId().equals(combine_good_id))
						.findAny().orElse(null);
				Assert.assertNotEquals(combineGoods, null, "新建的组合商品没有在新建智能菜单页面展示");
				combine_good_ids.add(combine_good_id);
			}

			String name = StringUtil.getRandomString(6).toUpperCase();
			SmartMenuParam smartMenuParam = new SmartMenuParam();
			smartMenuParam.setName(name);
			smartMenuParam.setSku_ids(sku_ids);
			smartMenuParam.setCombine_good_ids(combine_good_ids);

			id = smartMenuService.createSmartMenu(smartMenuParam);
			Assert.assertNotEquals(id, null, "新建智能菜单失败");

			SmartMenuDetailBean smartMenuDetail = smartMenuService.getSmartMenuDetail(id);
			Assert.assertNotEquals(smartMenuDetail, null, "获取智能菜单 " + id + " 详细信息失败");

			String msg = null;
			boolean result = true;
			if (!smartMenuDetail.getName().equals(name)) {
				msg = String.format("新建智能菜单填写的名称和新建完查看到的不一致,预期:%s,实际:%s", name, smartMenuDetail.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<SmartMenuDetailBean.Sku> skus = smartMenuDetail.getSkus();
			List<String> actual_sku_ids = skus.stream().map(s -> s.getId()).collect(Collectors.toList());
			msg = String.format("新建智能菜单填写的商品列表和新建完查看到的不一致,预期:%s,实际:%s", sku_ids, actual_sku_ids);
			if (actual_sku_ids.size() == sku_ids.size()) {
				if (!actual_sku_ids.containsAll(sku_ids)) {
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<SmartMenuDetailBean.CombineGoods> combine_goods = smartMenuDetail.getCombine_goods();
			List<String> actual_combine_good_ids = combine_goods.stream().map(s -> s.getId())
					.collect(Collectors.toList());
			msg = String.format("新建智能菜单填写的商品列表和新建完查看到的不一致,预期:%s,实际:%s", combine_good_ids, actual_combine_good_ids);
			if (combine_good_ids.size() == actual_combine_good_ids.size()) {
				if (!actual_combine_good_ids.containsAll(combine_good_ids)) {
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "新建智能菜单填写的信息与查看到的不一致");
		} catch (Exception e) {
			logger.error("拉取站点所有的组合商品遇到错误: ", e);
			Assert.fail("拉取站点所有的组合商品遇到错误: ", e);
		}
	}

	@AfterMethod
	public void afterMethod() {
		if (id != null) {
			try {
				boolean result = smartMenuService.deleteSmartMenu(id);
				Assert.assertEquals(result, true, "删除智能菜单失败");
			} catch (Exception e) {
				logger.error("删除智能菜单遇到错误: ", e);
				Assert.fail("删除智能菜单遇到错误: ", e);
			}
		}
	}
}
