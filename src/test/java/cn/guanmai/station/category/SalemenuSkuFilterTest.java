package cn.guanmai.station.category;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.category.PinleiBean;
import cn.guanmai.station.bean.category.SalemenuBean;
import cn.guanmai.station.bean.category.SalemenuSkuBean;
import cn.guanmai.station.bean.category.param.SalemenuFilterParam;
import cn.guanmai.station.bean.category.param.SalemenuSkuFilterParam;
import cn.guanmai.station.bean.marketing.param.SmartFormulaPricingParam;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.category.SalemenuServiceImpl;
import cn.guanmai.station.impl.marketing.PricingServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.category.SalemenuService;
import cn.guanmai.station.interfaces.marketing.PricingService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;

/**
 * @author: liming
 * @Date: 2020年3月16日 下午3:33:35
 * @description: 报价单里的销售SKU搜索过滤
 * @version: 1.0
 */

public class SalemenuSkuFilterTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SalemenuSkuFilterTest.class);

	private SalemenuService salemenuService;
	private CategoryService categoryService;
	private SalemenuSkuFilterParam salemenuSkuFilterParam;
	private PricingService pricingService;
	private List<SalemenuSkuBean> salemenuSkus;
	private String salemenu_id;
	private int limit = 20;
	private int offset = 0;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		salemenuService = new SalemenuServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		pricingService = new PricingServiceImpl(headers);
		try {
			SalemenuFilterParam filterParam = new SalemenuFilterParam();
			filterParam.setWith_sku_num(1);
			filterParam.setType(-1);
			List<SalemenuBean> salemenus = salemenuService.searchSalemenu(filterParam);
			Assert.assertNotEquals(salemenus, null, "搜索过滤报价单列表失败");

			Optional<SalemenuBean> optionalSalemenu = salemenus.stream()
					.max(Comparator.comparingInt(SalemenuBean::getSku_num));

			SalemenuBean salemenu = optionalSalemenu.get();
			Assert.assertEquals(salemenu.getSku_num() > 1, true, "此站点商品数最大的报价单商品数都不足 2 个");

			salemenu_id = salemenu.getId();
			salemenuSkuFilterParam = new SalemenuSkuFilterParam();
			salemenuSkuFilterParam.setSalemenu_id(salemenu_id);

			salemenuSkus = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(salemenuSkus, null, "报价单商品列表页面搜索销售商品失败");
		} catch (Exception e) {
			logger.error("搜索过滤报价单遇到错误: ", e);
			Assert.fail("搜索过滤报价单遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		offset = 0;
		salemenuSkuFilterParam = new SalemenuSkuFilterParam();
		salemenuSkuFilterParam.setSalemenu_id(salemenu_id);
		salemenuSkuFilterParam.setLimit(limit);
		salemenuSkuFilterParam.setOffset(offset);
	}

	@Test
	public void salemenuSkuFilterTestCase01() {
		ReporterCSS.title("测试点: 报价单里销售SKU搜索过滤,使用商品一级分类进行搜索过滤");
		try {
			SalemenuSkuBean sku = NumberUtil.roundNumberInList(salemenuSkus);
			String category1_id = sku.getCategory1_id();

			JSONArray category1_ids = new JSONArray();
			category1_ids.add(category1_id);

			salemenuSkuFilterParam.setCategory1_ids(category1_ids);
			List<SalemenuSkuBean> skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

			Assert.assertEquals(skuList.size() > 0, true,
					"报价单里销售SKU搜索过滤,使用商品一级分类" + category1_ids + "进行搜索过滤,没有过滤出数据,与预期不符");

			List<String> temp_category1_ids = skuList.stream().filter(c -> !c.getCategory1_id().equals(category1_id))
					.map(c -> c.getCategory1_id()).collect(Collectors.toList());
			Assert.assertEquals(temp_category1_ids.size() == 0, true,
					"报价单里销售SKU搜索过滤,使用商品一级分类" + category1_ids + "进行搜索过滤,搜索出了其他一级分类 " + temp_category1_ids + "的销售商品");

			// 翻页获取,翻一页验证就行了
			if (skuList.size() == limit) {
				salemenuSkuFilterParam.setOffset(offset += limit);
				skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
				Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");
				temp_category1_ids = skuList.stream().filter(c -> !c.getCategory1_id().equals(category1_id))
						.map(c -> c.getCategory1_id()).collect(Collectors.toList());
				Assert.assertEquals(temp_category1_ids.size() == 0, true,
						"报价单里销售SKU搜索过滤,使用商品一级分类" + category1_ids + "进行搜索过滤,搜索出了其他一级分类 " + temp_category1_ids + "的销售商品");
			}
		} catch (Exception e) {
			logger.error("报价单里搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("报价单里搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void salemenuSkuFilterTestCase02() {
		ReporterCSS.title("测试点: 报价单里销售SKU搜索过滤,使用商品一级分类[多个一级分类]进行搜索过滤");
		try {

			List<String> category1_id_list = salemenuSkus.stream().map(s -> s.getCategory1_id()).distinct()
					.collect(Collectors.toList());
			if (category1_id_list.size() >= 2) {
				JSONArray category1_ids = new JSONArray();
				category1_ids.addAll(NumberUtil.roundNumberInList(category1_id_list, 2));

				salemenuSkuFilterParam.setCategory1_ids(category1_ids);
				List<SalemenuSkuBean> skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
				Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

				Assert.assertEquals(skuList.size() >= 2, true,
						"报价单里销售SKU搜索过滤,使用商品一级分类" + category1_ids + "进行搜索过滤,没有过滤出数据,与预期不符");

				List<String> temp_category1_ids = skuList.stream()
						.filter(c -> !category1_ids.contains(c.getCategory1_id())).map(c -> c.getCategory1_id())
						.collect(Collectors.toList());

				Assert.assertEquals(temp_category1_ids.size() == 0, true,
						"报价单里销售SKU搜索过滤,使用商品一级分类" + category1_ids + "进行搜索过滤,搜索出了其他一级分类 " + temp_category1_ids + "的销售商品");

				// 翻页获取,翻一页验证就行了
				if (skuList.size() == limit) {
					salemenuSkuFilterParam.setOffset(offset += limit);
					skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
					Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");
					temp_category1_ids = skuList.stream().filter(c -> !category1_ids.contains(c.getCategory1_id()))
							.map(c -> c.getCategory1_id()).collect(Collectors.toList());

					Assert.assertEquals(temp_category1_ids.size() == 0, true, "报价单里销售SKU搜索过滤,使用商品一级分类" + category1_ids
							+ "进行搜索过滤,搜索出了其他一级分类 " + temp_category1_ids + "的销售商品");
				}
			}
		} catch (Exception e) {
			logger.error("报价单里搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("报价单里搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void salemenuSkuFilterTestCase03() {
		ReporterCSS.title("测试点: 报价单里销售SKU搜索过滤,使用商品二级分类进行搜索过滤");
		try {

			SalemenuSkuBean sku = NumberUtil.roundNumberInList(salemenuSkus);
			String category1_id = sku.getCategory1_id();
			String category2_id = sku.getCategory2_id();

			JSONArray category1_ids = new JSONArray();
			category1_ids.add(category1_id);

			JSONArray category2_ids = new JSONArray();
			category2_ids.add(category2_id);

			salemenuSkuFilterParam.setCategory1_ids(category1_ids);
			salemenuSkuFilterParam.setCategory2_ids(category2_ids);

			List<SalemenuSkuBean> skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

			Assert.assertEquals(skuList.size() > 0, true,
					"报价单里销售SKU搜索过滤,使用商品二级分类" + category2_ids + "进行搜索过滤,没有过滤出数据,与预期不符");

			List<String> temp_category2_ids = skuList.stream().filter(c -> !c.getCategory2_id().equals(category2_id))
					.map(c -> c.getCategory2_id()).collect(Collectors.toList());
			Assert.assertEquals(temp_category2_ids.size() == 0, true, "报价单里销售SKU搜索过滤,使用商品二级分类" + temp_category2_ids
					+ "进行搜索过滤,搜索出了其他二级分类 " + temp_category2_ids + "的销售商品");

			// 翻页获取,翻一页验证就行了
			if (skuList.size() == limit) {
				salemenuSkuFilterParam.setOffset(offset += limit);
				skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
				Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");
				temp_category2_ids = skuList.stream().filter(c -> !c.getCategory2_id().equals(category2_id))
						.map(c -> c.getCategory2_id()).collect(Collectors.toList());
				Assert.assertEquals(temp_category2_ids.size() == 0, true, "报价单里销售SKU搜索过滤,使用商品二级分类" + temp_category2_ids
						+ "进行搜索过滤,搜索出了其他二级分类 " + temp_category2_ids + "的销售商品");
			}
		} catch (Exception e) {
			logger.error("报价单里搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("报价单里搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void salemenuSkuFilterTestCase04() {
		ReporterCSS.title("测试点: 报价单里销售SKU搜索过滤,使用商品二级分类[多个二级分类]进行搜索过滤");
		try {
			JSONArray category1_ids = new JSONArray();
			JSONArray category2_ids = new JSONArray();
			for (SalemenuSkuBean salemenuSku : salemenuSkus) {
				if (!category2_ids.contains(salemenuSku.getCategory2_id())) {
					category1_ids.add(salemenuSku.getCategory1_id());
					category2_ids.add(salemenuSku.getCategory2_id());
					if (category2_ids.size() > 2) {
						break;
					}
				}
			}
			if (category2_ids.size() >= 2) {
				salemenuSkuFilterParam.setCategory1_ids(category1_ids);
				salemenuSkuFilterParam.setCategory2_ids(category2_ids);

				List<SalemenuSkuBean> skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
				Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

				Assert.assertEquals(skuList.size() >= 2, true,
						"报价单里销售SKU搜索过滤,使用商品二级分类" + category2_ids + "进行搜索过滤,没有过滤出数据,与预期不符");

				List<String> temp_category2_ids = skuList.stream()
						.filter(c -> !category2_ids.contains(c.getCategory2_id())).map(c -> c.getCategory2_id())
						.collect(Collectors.toList());

				Assert.assertEquals(temp_category2_ids.size() == 0, true,
						"报价单里销售SKU搜索过滤,使用商品二级分类" + category2_ids + "进行搜索过滤,搜索出了其他二级分类 " + temp_category2_ids + "的销售商品");

				// 翻页获取,翻一页验证就行了
				if (skuList.size() == limit) {
					salemenuSkuFilterParam.setOffset(offset += limit);
					skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
					Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");
					temp_category2_ids = skuList.stream().filter(c -> !category2_ids.contains(c.getCategory2_id()))
							.map(c -> c.getCategory2_id()).collect(Collectors.toList());

					Assert.assertEquals(temp_category2_ids.size() == 0, true, "报价单里销售SKU搜索过滤,使用商品二级分类" + category2_ids
							+ "进行搜索过滤,搜索出了其他二级分类 " + temp_category2_ids + "的销售商品");
				}
			}
		} catch (Exception e) {
			logger.error("报价单里搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("报价单里搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void salemenuSkuFilterTestCase05() {
		ReporterCSS.title("测试点: 报价单里销售SKU搜索过滤,使用商品品类分类进行搜索过滤");
		try {
			JSONArray category1_ids = new JSONArray();
			JSONArray category2_ids = new JSONArray();
			JSONArray pinlei_ids = new JSONArray();
			Set<String> pinlei_names = new HashSet<String>();
			for (SalemenuSkuBean salemenuSku : salemenuSkus) {
				if (!category2_ids.contains(salemenuSku.getCategory2_id())) {
					String pinlei_name = salemenuSku.getPinlei_name();
					if (!pinlei_names.contains(pinlei_name)) {
						PinleiBean pinlei = categoryService.getPinleiByName(salemenuSku.getCategory2_id(), pinlei_name);
						Assert.assertNotEquals(pinlei, null, "获取品类 " + pinlei_name + " 信息失败");
						category1_ids.add(salemenuSku.getCategory1_id());
						category2_ids.add(salemenuSku.getCategory2_id());
						pinlei_ids.add(pinlei.getId());
						pinlei_names.add(pinlei_name);
						if (pinlei_ids.size() > 2) {
							break;
						}
					}
				}
			}

			salemenuSkuFilterParam.setCategory1_ids(category1_ids);
			salemenuSkuFilterParam.setCategory2_ids(category2_ids);
			salemenuSkuFilterParam.setPinlei_ids(pinlei_ids);

			List<SalemenuSkuBean> skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

			Assert.assertEquals(skuList.size() >= 0, true,
					"报价单里销售SKU搜索过滤,使用商品品类分类" + category2_ids + "进行搜索过滤,没有过滤出数据,与预期不符");

			List<String> temp_pinlei_ids = skuList.stream().filter(
					c -> !category2_ids.contains(c.getCategory2_id()) || !pinlei_names.contains(c.getPinlei_name()))
					.map(c -> c.getPinlei_name()).collect(Collectors.toList());

			Assert.assertEquals(temp_pinlei_ids.size() == 0, true,
					"报价单里销售SKU搜索过滤,使用商品品类分类" + pinlei_ids + "进行搜索过滤,搜索出了其他品类分类 " + temp_pinlei_ids + "的销售商品");

			// 翻页获取,翻一页验证就行了
			if (skuList.size() == limit) {
				salemenuSkuFilterParam.setOffset(offset += limit);
				skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
				Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

				temp_pinlei_ids = skuList.stream().filter(
						c -> !category2_ids.contains(c.getCategory2_id()) || !pinlei_names.contains(c.getPinlei_name()))
						.map(c -> c.getPinlei_name()).collect(Collectors.toList());

				Assert.assertEquals(temp_pinlei_ids.size() == 0, true,
						"报价单里销售SKU搜索过滤,使用商品品类分类" + pinlei_ids + "进行搜索过滤,搜索出了其他品类分类 " + temp_pinlei_ids + "的销售商品");
			}
		} catch (Exception e) {
			logger.error("报价单里搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("报价单里搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void salemenuSkuFilterTestCase06() {
		ReporterCSS.title("测试点: 报价单里销售SKU搜索过滤,使用商品名称搜索过滤");
		try {
			SalemenuSkuBean sku = NumberUtil.roundNumberInList(salemenuSkus);
			String spu_name = sku.getSpu_name();
			salemenuSkuFilterParam.setText(spu_name);

			List<SalemenuSkuBean> skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

			Assert.assertEquals(skuList.size() >= 0, true, "报价单里销售SKU搜索过滤,使用商品名称" + spu_name + "进行搜索过滤,没有过滤出数据,与预期不符");

			List<String> temp_spu_names = skuList.stream()
					.filter(c -> !c.getSpu_name().contains(spu_name) && !c.getSku_name().contains(spu_name)
							&& !c.getSku_id().contains(spu_name))
					.map(c -> c.getSpu_name()).collect(Collectors.toList());

			Assert.assertEquals(temp_spu_names.size() == 0, true,
					"报价单里销售SKU搜索过滤,使用商品名称" + spu_name + "进行搜索过滤,搜索出名称为 " + temp_spu_names + "的销售商品");

			// 翻页获取,翻一页验证就行了
			if (skuList.size() == limit) {
				salemenuSkuFilterParam.setOffset(offset += limit);
				skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
				Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

				temp_spu_names = skuList.stream()
						.filter(c -> !c.getSpu_name().contains(spu_name) && !c.getSku_name().contains(spu_name)
								&& !c.getSku_id().contains(spu_name))
						.map(c -> c.getSpu_name()).collect(Collectors.toList());

				Assert.assertEquals(temp_spu_names.size() == 0, true,
						"报价单里销售SKU搜索过滤,使用商品名称" + spu_name + "进行搜索过滤,搜索出名称为 " + temp_spu_names + "的销售商品");

			}
		} catch (Exception e) {
			logger.error("报价单里搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("报价单里搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void salemenuSkuFilterTestCase07() {
		ReporterCSS.title("测试点: 报价单里销售SKU搜索过滤,使用规格名称搜索过滤");
		try {
			SalemenuSkuBean sku = NumberUtil.roundNumberInList(salemenuSkus);
			String sku_name = sku.getSku_name();
			salemenuSkuFilterParam.setText(sku_name);

			List<SalemenuSkuBean> skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

			Assert.assertEquals(skuList.size() >= 0, true, "报价单里销售SKU搜索过滤,使用规格名称" + sku_name + "进行搜索过滤,没有过滤出数据,与预期不符");

			List<String> temp_sku_names = skuList.stream()
					.filter(c -> !c.getSpu_name().contains(sku_name) && !c.getSku_name().contains(sku_name)
							&& !c.getSku_id().contains(sku_name))
					.map(c -> c.getSku_name()).collect(Collectors.toList());

			Assert.assertEquals(temp_sku_names.size() == 0, true,
					"报价单里销售SKU搜索过滤,使用规格名称" + sku_name + "进行搜索过滤,搜索出名称为 " + temp_sku_names + "的销售商品");

			// 翻页获取,翻一页验证就行了
			if (skuList.size() == limit) {
				salemenuSkuFilterParam.setOffset(offset += limit);
				skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
				Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

				temp_sku_names = skuList.stream()
						.filter(c -> !c.getSpu_name().contains(sku_name) && !c.getSku_name().contains(sku_name)
								&& !c.getSku_id().contains(sku_name))
						.map(c -> c.getSku_name()).collect(Collectors.toList());

				Assert.assertEquals(temp_sku_names.size() == 0, true,
						"报价单里销售SKU搜索过滤,使用商品名称" + sku_name + "进行搜索过滤,搜索出名称为 " + temp_sku_names + "的销售商品");
			}
		} catch (Exception e) {
			logger.error("报价单里搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("报价单里搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void salemenuSkuFilterTestCase08() {
		ReporterCSS.title("测试点: 报价单里销售SKU搜索过滤,使用规格ID搜索过滤");
		try {
			SalemenuSkuBean sku = NumberUtil.roundNumberInList(salemenuSkus);
			String sku_id = sku.getSku_id();
			salemenuSkuFilterParam.setText(sku_id);

			List<SalemenuSkuBean> skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

			Assert.assertEquals(skuList.size() >= 0, true, "报价单里销售SKU搜索过滤,使用规格ID" + sku_id + "进行搜索过滤,没有过滤出数据,与预期不符");

			List<String> temp_sku_ids = skuList.stream().filter(c -> !c.getSpu_name().contains(sku_id)
					&& !c.getSku_name().contains(sku_id) && !c.getSku_id().contains(sku_id)).map(c -> c.getSku_id())
					.collect(Collectors.toList());

			Assert.assertEquals(temp_sku_ids.size() == 0, true,
					"报价单里销售SKU搜索过滤,使用规格ID" + sku_id + "进行搜索过滤,搜索出规格ID为 " + temp_sku_ids + "的销售商品");

			// 翻页获取,翻一页验证就行了
			if (skuList.size() == limit) {
				salemenuSkuFilterParam.setOffset(offset += limit);
				skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
				Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

				temp_sku_ids = skuList.stream()
						.filter(c -> !c.getSpu_name().contains(sku_id) && !c.getSku_name().contains(sku_id)
								&& !c.getSku_id().contains(sku_id))
						.map(c -> c.getSku_id()).collect(Collectors.toList());

				Assert.assertEquals(temp_sku_ids.size() == 0, true,
						"报价单里销售SKU搜索过滤,使用规格ID" + sku_id + "进行搜索过滤,搜索出规格ID为 " + temp_sku_ids + "的销售商品");

			}
		} catch (Exception e) {
			logger.error("报价单里搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("报价单里搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void salemenuSkuFilterTestCase09() {
		ReporterCSS.title("测试点: 报价单里销售SKU搜索过滤,使用销售状态[上架]搜索过滤");
		try {
			salemenuSkuFilterParam.setState(1);
			List<SalemenuSkuBean> skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

			Assert.assertEquals(skuList.size() > 0, true, "报价单里销售SKU搜索过滤,使用销售状态搜索过滤,没有过滤出数据,与预期不符");

			List<String> sku_ids = skuList.stream().filter(s -> s.getState() != 1).map(s -> s.getSku_id())
					.collect(Collectors.toList());

			Assert.assertEquals(sku_ids.size(), 0, "报价单里销售SKU搜索过滤,使用销售状态[上架]搜索过滤,过滤出了下架的商品 " + sku_ids);

			if (skuList.size() == limit) {
				salemenuSkuFilterParam.setOffset(offset += limit);
				skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
				Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

				sku_ids = skuList.stream().filter(s -> s.getState() != 1).map(s -> s.getSku_id())
						.collect(Collectors.toList());

				Assert.assertEquals(sku_ids.size(), 0, "报价单里销售SKU搜索过滤,使用销售状态[上架]搜索过滤,过滤出了下架的商品 " + sku_ids);
			}
		} catch (Exception e) {
			logger.error("报价单里搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("报价单里搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void salemenuSkuFilterTestCase10() {
		ReporterCSS.title("测试点: 报价单里销售SKU搜索过滤,使用销售状态[下架]搜索过滤");
		String sku_id = null;
		try {
			SalemenuSkuBean sku = NumberUtil.roundNumberInList(salemenuSkus);
			sku_id = sku.getSku_id();
			boolean result = categoryService.updateSaleSkuStatus(sku_id, false);
			Assert.assertEquals(result, true, "修改销售规格" + sku_id + " 的销售状态失败");

			salemenuSkuFilterParam.setState(0);

			List<SalemenuSkuBean> skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

			Assert.assertEquals(skuList.size() > 0, true, "报价单里销售SKU搜索过滤,使用销售状态搜索过滤,没有过滤出数据,与预期不符");

			List<String> sku_ids = skuList.stream().filter(s -> s.getState() != 0).map(s -> s.getSku_id())
					.collect(Collectors.toList());

			Assert.assertEquals(sku_ids.size(), 0, "报价单里销售SKU搜索过滤,使用销售状态[下架]搜索过滤,过滤出了上架的商品 " + sku_ids);
		} catch (Exception e) {
			logger.error("报价单里搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("报价单里搜索过滤销售SKU遇到错误: ", e);
		} finally {
			try {
				boolean result = categoryService.updateSaleSkuStatus(sku_id, true);
				Assert.assertEquals(result, true, "修改销售规格" + sku_id + " 的销售状态失败");
			} catch (Exception e) {
				logger.error("修改销售规格的销售状态遇到错误: ", e);
				Assert.fail("修改销售规格的销售状态遇到错误: ", e);
			}
		}
	}

	@Test
	public void salemenuSkuFilterTestCase11() {
		ReporterCSS.title("测试点: 报价单里销售SKU搜索过滤,使用商品图片[有图]搜索过滤");
		try {
			salemenuSkuFilterParam.setHas_images(1);
			List<SalemenuSkuBean> skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

			List<String> sku_ids = skuList.stream()
					.filter(s -> s.getSku_image() == null || s.getSku_image().trim().equals("")).map(s -> s.getSku_id())
					.collect(Collectors.toList());

			Assert.assertEquals(sku_ids.size(), 0, "报价单里销售SKU搜索过滤,使用商品图片[有图]搜索过滤,过滤出了无图的商品 " + sku_ids);

			if (skuList.size() == limit) {
				salemenuSkuFilterParam.setOffset(offset += limit);

				skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
				Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

				sku_ids = skuList.stream().filter(s -> s.getSku_image() == null || s.getSku_image().trim().equals(""))
						.map(s -> s.getSku_id()).collect(Collectors.toList());

				Assert.assertEquals(sku_ids.size(), 0, "报价单里销售SKU搜索过滤,使用商品图片[有图]搜索过滤,过滤出了无图的商品 " + sku_ids);
			}
		} catch (Exception e) {
			logger.error("报价单里搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("报价单里搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void salemenuSkuFilterTestCase12() {
		ReporterCSS.title("测试点: 报价单里销售SKU搜索过滤,使用商品图片[无图]搜索过滤");
		try {
			salemenuSkuFilterParam.setHas_images(0);
			List<SalemenuSkuBean> skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

			List<String> sku_ids = skuList.stream()
					.filter(s -> s.getSku_image() != null && !s.getSku_image().trim().equals(""))
					.map(s -> s.getSku_id()).collect(Collectors.toList());

			Assert.assertEquals(sku_ids.size(), 0, "报价单里销售SKU搜索过滤,使用商品图片[无图]搜索过滤,过滤出了有图的商品 " + sku_ids);

			if (skuList.size() == limit) {
				salemenuSkuFilterParam.setOffset(offset += limit);

				skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
				Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

				sku_ids = skuList.stream().filter(s -> s.getSku_image() != null && !s.getSku_image().trim().equals(""))
						.map(s -> s.getSku_id()).collect(Collectors.toList());

				Assert.assertEquals(sku_ids.size(), 0, "报价单里销售SKU搜索过滤,使用商品图片[无图]搜索过滤,过滤出了有图的商品 " + sku_ids);
			}
		} catch (Exception e) {
			logger.error("报价单里搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("报价单里搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void salemenuSkuFilterTestCase13() {
		ReporterCSS.title("测试点: 报价单里销售SKU搜索过滤,使用时价状态[时价]搜索过滤");
		try {
			salemenuSkuFilterParam.setIs_price_timing(1);
			List<SalemenuSkuBean> skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

			List<String> sku_ids = skuList.stream().filter(s -> !s.isIs_price_timing()).map(s -> s.getSku_id())
					.collect(Collectors.toList());

			Assert.assertEquals(sku_ids.size(), 0, "报价单里销售SKU搜索过滤,使用时价状态[时价]搜索过滤,过滤出了非时价的商品 " + sku_ids);

			if (skuList.size() == limit) {
				salemenuSkuFilterParam.setOffset(offset += limit);

				skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
				Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

				sku_ids = skuList.stream().filter(s -> !s.isIs_price_timing()).map(s -> s.getSku_id())
						.collect(Collectors.toList());

				Assert.assertEquals(sku_ids.size(), 0, "报价单里销售SKU搜索过滤,使用时价状态[时价]搜索过滤,过滤出了非时价的商品 " + sku_ids);

			}
		} catch (Exception e) {
			logger.error("报价单里搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("报价单里搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void salemenuSkuFilterTestCase14() {
		ReporterCSS.title("测试点: 报价单里销售SKU搜索过滤,使用时价状态[非时价]搜索过滤");
		try {
			salemenuSkuFilterParam.setIs_price_timing(0);
			List<SalemenuSkuBean> skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

			List<String> sku_ids = skuList.stream().filter(s -> s.isIs_price_timing()).map(s -> s.getSku_id())
					.collect(Collectors.toList());

			Assert.assertEquals(sku_ids.size(), 0, "报价单里销售SKU搜索过滤,使用时价状态[非时价]搜索过滤,过滤出了时价的商品 " + sku_ids);

			if (skuList.size() == limit) {
				salemenuSkuFilterParam.setOffset(offset += limit);

				skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
				Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

				sku_ids = skuList.stream().filter(s -> s.isIs_price_timing()).map(s -> s.getSku_id())
						.collect(Collectors.toList());

				Assert.assertEquals(sku_ids.size(), 0, "报价单里销售SKU搜索过滤,使用时价状态[非时价]搜索过滤,过滤出了时价的商品 " + sku_ids);
			}
		} catch (Exception e) {
			logger.error("报价单里搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("报价单里搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void salemenuSkuFilterTestCase15() {
		ReporterCSS.title("测试点: 报价单里销售SKU搜索过滤,使用定价公式状态[关闭]搜索过滤");
		try {
			salemenuSkuFilterParam.setFormula(0);
			List<SalemenuSkuBean> skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

			List<String> sku_ids = skuList.stream().filter(s -> s.getFormula_status() == 1).map(s -> s.getSku_id())
					.collect(Collectors.toList());

			Assert.assertEquals(sku_ids.size(), 0, "报价单里销售SKU搜索过滤,使用定价公式状态[关闭]搜索过滤,过滤出了定价公式状态[开启]的商品 " + sku_ids);

			if (skuList.size() == limit) {
				salemenuSkuFilterParam.setOffset(offset += limit);

				sku_ids = skuList.stream().filter(s -> s.getFormula_status() == 1).map(s -> s.getSku_id())
						.collect(Collectors.toList());

				Assert.assertEquals(sku_ids.size(), 0, "报价单里销售SKU搜索过滤,使用定价公式状态[关闭]搜索过滤,过滤出了定价公式状态[开启]的商品 " + sku_ids);
			}
		} catch (Exception e) {
			logger.error("报价单里搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("报价单里搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void salemenuSkuFilterTestCase16() {
		ReporterCSS.title("测试点: 报价单里销售SKU搜索过滤,使用定价公式状态[开启]搜索过滤");
		try {
			SalemenuSkuBean sku = NumberUtil.roundNumberInList(salemenuSkus);
			String sku_id = sku.getSku_id();
			SmartFormulaPricingParam smartFormulaPricingParam = new SmartFormulaPricingParam();
			smartFormulaPricingParam.setFormula_status(1);
			smartFormulaPricingParam.setPrice_type(5);
			smartFormulaPricingParam.setCal_type(0);
			smartFormulaPricingParam.setCal_num(new BigDecimal("10"));
			smartFormulaPricingParam.setAll(0);
			smartFormulaPricingParam.setSku_list(Arrays.asList(sku_id));

			boolean result = pricingService.updateSmartFormulaPricing(smartFormulaPricingParam);
			Assert.assertEquals(result, true, "设置商品启用定价公式失败");

			salemenuSkuFilterParam.setFormula(1);
			List<SalemenuSkuBean> skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

			Assert.assertEquals(skuList.size() > 0, true, "报价单里销售SKU搜索过滤,使用定价公式状态[开启]搜索过滤,搜索过滤无数据,与预期不符");

			List<String> sku_ids = skuList.stream().filter(s -> s.getFormula_status() == 0).map(s -> s.getSku_id())
					.collect(Collectors.toList());

			Assert.assertEquals(sku_ids.size(), 0, "报价单里销售SKU搜索过滤,使用定价公式状态[开启]搜索过滤,过滤出了定价公式状态[关闭]的商品 " + sku_ids);

			if (skuList.size() == limit) {
				salemenuSkuFilterParam.setOffset(offset += limit);

				sku_ids = skuList.stream().filter(s -> s.getFormula_status() == 0).map(s -> s.getSku_id())
						.collect(Collectors.toList());

				Assert.assertEquals(sku_ids.size(), 0, "报价单里销售SKU搜索过滤,使用定价公式状态[开启]搜索过滤,过滤出了定价公式状态[关闭]的商品 " + sku_ids);
			}
		} catch (Exception e) {
			logger.error("报价单里搜索过滤销售SKU遇到错误: ", e);
			Assert.fail("报价单里搜索过滤销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void salemenuSkuFilterTestCase17() {
		ReporterCSS.title("测试点: 报价单里的商品翻页");
		try {
			salemenuSkuFilterParam = new SalemenuSkuFilterParam();
			salemenuSkuFilterParam.setSalemenu_id(salemenu_id);
			salemenuSkuFilterParam.setLimit(10);
			salemenuSkuFilterParam.setOffset(0);

			List<SalemenuSkuBean> skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

			List<String> sku_ids_1 = skuList.stream().map(s -> s.getSku_id()).collect(Collectors.toList());

			salemenuSkuFilterParam.setOffset(10);
			skuList = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(skuList, null, "报价单商品列表页面搜索销售商品失败");

			List<String> sku_ids_2 = skuList.stream().map(s -> s.getSku_id()).collect(Collectors.toList());

			sku_ids_1.retainAll(sku_ids_2);

			Assert.assertEquals(sku_ids_1.size(), 0, "报价单里的商品翻页,出现了重复数据" + sku_ids_1);
		} catch (Exception e) {
			logger.error("报价单里商品翻页遇到错误: ", e);
			Assert.fail("报价单里商品翻页遇到错误: ", e);
		}
	}

	@Test
	public void salemenuSkuFilterTestCase18() {
		ReporterCSS.title("测试点: 导出报价单里的商品列表");
		try {
			String filePath = categoryService.exportSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(filePath, null, "导出报价单里的商品列表失败");
		} catch (Exception e) {
			logger.error("导出报价单里的商品列表遇到错误: ", e);
			Assert.fail("导出报价单里的商品列表遇到错误: ", e);
		}
	}

}
