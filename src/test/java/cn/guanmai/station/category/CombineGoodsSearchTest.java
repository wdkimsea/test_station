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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.station.MainTest;
import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.category.CombineGoodsBean;
import cn.guanmai.station.bean.category.CombineGoodsDetailBean;
import cn.guanmai.station.bean.category.CombineGoodsPageBean;
import cn.guanmai.station.bean.category.SalemenuBean;
import cn.guanmai.station.bean.category.SalemenuSkuBean;
import cn.guanmai.station.bean.category.param.CombineGoodsBatchFilterParam;
import cn.guanmai.station.bean.category.param.CombineGoodsFilterParam;
import cn.guanmai.station.bean.category.param.CombineGoodsParam;
import cn.guanmai.station.bean.category.param.SalemenuFilterParam;
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
 * @Date: 2020年2月18日 下午7:45:19
 * @description:
 * @version: 1.0
 */

public class CombineGoodsSearchTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(CombineGoodsCreateTest.class);

	private CombineGoodsService combineGoodsService;
	private CategoryService categoryService;
	private SalemenuService salemenuService;
	private List<String> salemenu_ids;
	private String combine_goods_id;
	private InitDataBean initData = MainTest.initData;
	private String combine_goods_name;

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

			String salemenu_id = NumberUtil.roundNumberInList(salemenu_ids);
			SalemenuSkuFilterParam salemenuSkuFilterParam = new SalemenuSkuFilterParam();
			salemenuSkuFilterParam.setSalemenu_id(salemenu_id);

			List<SalemenuSkuBean> skuSalemenuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuSalemenuList, null, "报价单商品列表页面拉取销售SKU失败");

			SalemenuSkuBean skuSalemenu = NumberUtil.roundNumberInList(skuSalemenuList);
			String spu_id = skuSalemenu.getSpu_id();
			String sku_id = skuSalemenu.getSku_id();

			CombineGoodsParam combineGoodsCreateParam = new CombineGoodsParam();
			combine_goods_name = "AT_" + StringUtil.getRandomNumber(6);
			combineGoodsCreateParam.setName(combine_goods_name);
			String sale_unit_name = "包";
			combineGoodsCreateParam.setSale_unit_name(sale_unit_name);
			combineGoodsCreateParam.setSalemenu_ids(Arrays.asList(salemenu_id));
			combineGoodsCreateParam.setState(0);
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

		} catch (Exception e) {
			logger.error("初始化数据过程中遇到错误", e);
			Assert.fail("初始化数据过程中遇到错误", e);
		}
	}

	@Test
	public void combineGoodsSearchTestCase01() {
		ReporterCSS.title("测试点: 按组合商品名搜索组合商品");
		try {
			CombineGoodsFilterParam combineGoodsFilterParam = new CombineGoodsFilterParam();
			combineGoodsFilterParam.setSearch_text(combine_goods_name);

			List<CombineGoodsBean> combineGoodsList = combineGoodsService.searchCombineGoods(combineGoodsFilterParam);
			Assert.assertNotEquals(combineGoodsList, null, "按组合商品名搜索组合商品失败");

			CombineGoodsBean combineGoods = combineGoodsList.stream().filter(c -> c.getId().equals(combine_goods_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(combineGoods, null, "按组合商品名过滤,目标组合商品没有过滤出来");

			List<String> combine_goods_names = combineGoodsList.stream()
					.filter(c -> !c.getName().contains(combine_goods_name)).map(c -> c.getName())
					.collect(Collectors.toList());
			Assert.assertEquals(combine_goods_names.size(), 0,
					"按组合商品名" + combine_goods_name + "过滤,过滤出了不符合过滤条件的数据 " + combine_goods_names);

		} catch (Exception e) {
			logger.error("组合商品搜索过滤遇到错误", e);
			Assert.fail("组合商品搜索过滤遇到错误", e);
		}
	}

	@Test
	public void combineGoodsSearchTestCase02() {
		ReporterCSS.title("测试点: 按组合商品状态搜索组合商品");
		try {
			CombineGoodsFilterParam combineGoodsFilterParam = new CombineGoodsFilterParam();
			combineGoodsFilterParam.setState(0);

			List<CombineGoodsBean> combineGoodsList = combineGoodsService.searchCombineGoods(combineGoodsFilterParam);
			Assert.assertNotEquals(combineGoodsList, null, "按组合商品状态搜索组合商品失败");

			CombineGoodsBean combineGoods = combineGoodsList.stream().filter(c -> c.getId().equals(combine_goods_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(combineGoods, null, "按组合商品状态过滤,目标组合商品没有过滤出来");

			List<String> combine_goods_names = combineGoodsList.stream().filter(c -> c.getState() != 0)
					.map(c -> c.getName()).collect(Collectors.toList());
			Assert.assertEquals(combine_goods_names.size(), 0, "按组合商品状态(下架)过滤,过滤出了不符合过滤条件的数据 " + combine_goods_names);

		} catch (Exception e) {
			logger.error("组合商品搜索过滤遇到错误", e);
			Assert.fail("组合商品搜索过滤遇到错误", e);
		}
	}

	@Test
	public void combineGoodsSearchTestCase03() {
		ReporterCSS.title("测试点: 组合商品批量修改搜索商品,按关键词搜索");
		try {
			CombineGoodsBatchFilterParam combineGoodsBatchFilterParam = new CombineGoodsBatchFilterParam();
			combineGoodsBatchFilterParam.setSearch_text(combine_goods_id);
			combineGoodsBatchFilterParam.setAll(1);

			List<CombineGoodsDetailBean> combineGoodsDetails = combineGoodsService
					.batchSearchCombineGoods(combineGoodsBatchFilterParam);
			Assert.assertNotEquals(combineGoodsDetails, null, "组合商品批量修改搜索商品失败");

			CombineGoodsDetailBean combineGoodsDetail = combineGoodsDetails.stream()
					.filter(c -> c.getId().equals(combine_goods_id)).findAny().orElse(null);
			Assert.assertNotEquals(combineGoodsDetail, null, "目标组合商品" + combine_goods_id + "没有搜索出来");

			List<String> otherCombineGoodsIds = combineGoodsDetails.stream()
					.filter(c -> !c.getId().contains(combine_goods_id) && !c.getName().contains(combine_goods_id))
					.map(c -> c.getId()).collect(Collectors.toList());

			Assert.assertEquals(otherCombineGoodsIds.size(), 0,
					"组合商品批量修改搜索商品,按关键词" + combine_goods_id + "搜索,搜索出了如下不符合条件的数据 " + otherCombineGoodsIds);
		} catch (Exception e) {
			logger.error("组合商品批量修改搜索商品遇到错误", e);
			Assert.fail("组合商品批量修改搜索商品遇到错误", e);
		}
	}

	@Test
	public void combineGoodsSearchTestCase04() {
		ReporterCSS.title("测试点: 组合商品批量修改搜索商品,按ID搜索");
		try {
			CombineGoodsBatchFilterParam combineGoodsBatchFilterParam = new CombineGoodsBatchFilterParam();
			combineGoodsBatchFilterParam.setCombine_goods_ids(Arrays.asList(combine_goods_id));
			combineGoodsBatchFilterParam.setAll(0);

			List<CombineGoodsDetailBean> combineGoodsDetails = combineGoodsService
					.batchSearchCombineGoods(combineGoodsBatchFilterParam);
			Assert.assertNotEquals(combineGoodsDetails, null, "组合商品批量修改搜索商品失败");

			CombineGoodsDetailBean combineGoodsDetai = combineGoodsDetails.stream()
					.filter(c -> c.getId().equals(combine_goods_id)).findAny().orElse(null);
			Assert.assertNotEquals(combineGoodsDetai, null, "目标组合商品" + combine_goods_id + "没有搜索出来");
		} catch (Exception e) {
			logger.error("组合商品批量修改搜索商品遇到错误", e);
			Assert.fail("组合商品批量修改搜索商品遇到错误", e);
		}
	}

	@Test
	public void combineGoodsSearchTestCase05() {
		ReporterCSS.title("测试点: 组合商品批量修改搜索商品,拉取全部");
		try {
			CombineGoodsBatchFilterParam combineGoodsBatchFilterParam = new CombineGoodsBatchFilterParam();
			combineGoodsBatchFilterParam.setAll(1);

			List<CombineGoodsDetailBean> combineGoodsDetails = combineGoodsService
					.batchSearchCombineGoods(combineGoodsBatchFilterParam);
			Assert.assertNotEquals(combineGoodsDetails, null, "组合商品批量修改搜索商品失败");

			CombineGoodsDetailBean combineGoodsDetai = combineGoodsDetails.stream()
					.filter(c -> c.getId().equals(combine_goods_id)).findAny().orElse(null);
			Assert.assertNotEquals(combineGoodsDetai, null, "目标组合商品" + combine_goods_id + "没有搜索出来");
		} catch (Exception e) {
			logger.error("组合商品批量修改搜索商品遇到错误", e);
			Assert.fail("组合商品批量修改搜索商品遇到错误", e);
		}
	}

	@Test
	public void combineGoodsSearchTestCase06() {
		ReporterCSS.title("测试点: 组合商品翻页验证");
		try {
			// 先创建10多个组合商品
			String salemenu_id = NumberUtil.roundNumberInList(salemenu_ids);
			SalemenuSkuFilterParam salemenuSkuFilterParam = new SalemenuSkuFilterParam();
			salemenuSkuFilterParam.setSalemenu_id(salemenu_id);

			List<SalemenuSkuBean> skuSalemenuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuSalemenuList, null, "报价单商品列表页面拉取销售SKU失败");

			SalemenuSkuBean skuSalemenu = NumberUtil.roundNumberInList(skuSalemenuList);
			String spu_id = skuSalemenu.getSpu_id();
			String sku_id = skuSalemenu.getSku_id();

			CombineGoodsParam combineGoodsCreateParam = new CombineGoodsParam();
			String sale_unit_name = "包";
			combineGoodsCreateParam.setSale_unit_name(sale_unit_name);
			combineGoodsCreateParam.setSalemenu_ids(Arrays.asList(salemenu_id));
			combineGoodsCreateParam.setState(0);
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
			for (int i = 0; i < 12; i++) {
				combine_goods_name = "AT_" + StringUtil.getRandomNumber(6);
				combineGoodsCreateParam.setName(combine_goods_name);
				combine_goods_id = combineGoodsService.createCombineGoods(combineGoodsCreateParam);
				Assert.assertNotEquals(combine_goods_id, null, "新建组合商品失败");
			}

			CombineGoodsFilterParam combineGoodsFilterParam = new CombineGoodsFilterParam();
			combineGoodsFilterParam.setLimit(10);
			combineGoodsFilterParam.setOffset(0);

			CombineGoodsPageBean combineGoodsPage = combineGoodsService.searchCombineGoodsPage(combineGoodsFilterParam);
			Assert.assertNotEquals(combineGoodsPage, null, "搜索过滤净菜列表失败");

			CombineGoodsPageBean.Pagination pagination = combineGoodsPage.getPagination();
			JSONObject page_obj = pagination.getPage_obj();
			boolean more = pagination.isMore();
			Assert.assertEquals(more, true, "净菜列表搜索返回的分页信息中more为false,与预期不符");

			Assert.assertEquals(combineGoodsPage.getCombineGoodsList().size() > 0, true, "组合商品搜索结果列表为空");

			List<String> firstPageIds = combineGoodsPage.getCombineGoodsList().stream().map(c -> c.getId())
					.collect(Collectors.toList());

			combineGoodsFilterParam.setPage_obj(page_obj);
			combineGoodsPage = combineGoodsService.searchCombineGoodsPage(combineGoodsFilterParam);
			Assert.assertNotEquals(combineGoodsPage, null, "搜索过滤净菜列表失败");

			List<String> secondPageIds = combineGoodsPage.getCombineGoodsList().stream().map(c -> c.getId())
					.collect(Collectors.toList());

			ReporterCSS.step("第一页的组合商品ID列表: " + firstPageIds);
			logger.info("第一页的组合商品ID列表: " + firstPageIds);
			ReporterCSS.step("第二页的组合商品ID列表: " + secondPageIds);
			logger.info("第二页的组合商品ID列表: " + secondPageIds);

			firstPageIds.retainAll(secondPageIds);
			Assert.assertEquals(firstPageIds.size(), 0, "组合商品翻页显示的数据有问题,第一页和第二页存在重复数据" + firstPageIds);

		} catch (Exception e) {
			logger.error("组合商品列表翻页遇到错误", e);
			Assert.fail("组合商品列表翻页遇到错误", e);
		}
	}

	@Test
	public void combineGoodsSearchTestCase07() {
		ReporterCSS.title("测试点: 导出组合商品");
		try {
			CombineGoodsFilterParam combineGoodsFilterParam = new CombineGoodsFilterParam();
			combineGoodsFilterParam.setState(0);
			combineGoodsFilterParam.setSearch_text(combine_goods_name);

			String file_path = combineGoodsService.exportCombineGoods(combineGoodsFilterParam);
			Assert.assertNotEquals(file_path, null, "导出组合商品失败");
		} catch (Exception e) {
			logger.error("导出组合商品遇到错误", e);
			Assert.fail("导出组合商品遇到错误", e);
		}
	}

	@AfterClass(timeOut = 20000)
	public void afterClass() {
		try {
			CombineGoodsFilterParam combineGoodsFilterParam = new CombineGoodsFilterParam();
			combineGoodsFilterParam.setLimit(50);
			combineGoodsFilterParam.setOffset(0);
			combineGoodsFilterParam.setSearch_text("AT_");

			boolean reuslt = true;
			while (true) {
				List<CombineGoodsBean> combineGoodsList = combineGoodsService
						.searchCombineGoods(combineGoodsFilterParam);
				Assert.assertNotEquals(combineGoodsList, null, "按组合商品名搜索组合商品失败");
				for (CombineGoodsBean combineGoods : combineGoodsList) {
					reuslt = combineGoodsService.deleteCombineGoods(combineGoods.getId());
					Assert.assertEquals(reuslt, true, "删除组合商品失败");
				}
				if (combineGoodsList.size() < combineGoodsFilterParam.getLimit()) {
					break;
				}
			}
		} catch (Exception e) {
			logger.error("删除组合商品遇到错误", e);
			Assert.fail("删除组合商品遇到错误", e);
		}
	}

}
