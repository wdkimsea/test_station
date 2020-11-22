package cn.guanmai.station.marketing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.marketing.PromotionBean;
import cn.guanmai.station.bean.marketing.PromotionResultBean;
import cn.guanmai.station.bean.marketing.PromotionSkuBean;
import cn.guanmai.station.bean.marketing.param.PromotionBaseParam;
import cn.guanmai.station.bean.marketing.param.PromotionDefaultParam;
import cn.guanmai.station.bean.marketing.param.PromotionFilterParam;
import cn.guanmai.station.bean.marketing.param.PromotionLimitParam;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.marketing.PromotionServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.marketing.PromotionService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Feb 22, 2019 11:57:40 AM 
* @des 修改营销规则的测试用例
* @version 1.0 
*/
public class PromotionEidtTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PromotionEidtTest.class);
	private PromotionService promotionService;
	private CategoryService categoryService;
	private AsyncService asyncService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		promotionService = new PromotionServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
	}

	@Test
	public void promotionEidtTestCase01() {
		ReporterCSS.title("测试点: 修改默认营销规则(无商品修改)");
		PromotionDefaultParam param = new PromotionDefaultParam();
		param.setName("AT" + StringUtil.getRandomNumber(6));
		param.setActive(1);
		param.setShow_method(1);
		param.setSort(1);
		param.setEnable_label_2(1);
		param.setLabel_1_name(StringUtil.getRandomString(8));
		param.setPic_url("//img.guanmai.cn/icon/icon-discount.1ac6d012.png");
		List<PromotionBaseParam.Label_2> label_2_list = new ArrayList<PromotionBaseParam.Label_2>();
		param.setLabel_2(label_2_list);
		param.setType(1);
		param.setSkus(new ArrayList<PromotionDefaultParam.Sku>());

		try {
			String promotion_id = promotionService.createPromotionDefault(param);
			Assert.assertNotEquals(promotion_id, null, "创建默认营销活动失败");

			param.setId(promotion_id);
			boolean result = promotionService.updatePromotionDefault(param);
			Assert.assertEquals(result, true, "修改默认营销活动失败");

		} catch (Exception e) {
			logger.error("修改默认营销活动的过程中出现错误: ", e);
			Assert.fail("修改默认营销活动的过程中出现错误: ", e);
		}
	}

	@Test
	public void promotionEidtTestCase02() {
		ReporterCSS.title("测试点: 修改默认营销规则-添加修改商品");
		PromotionDefaultParam param = new PromotionDefaultParam();
		param.setName("AT" + StringUtil.getRandomNumber(6));
		param.setActive(1);
		param.setShow_method(1);
		param.setSort(1);
		param.setEnable_label_2(1);
		String label_1_name = StringUtil.getRandomString(8);
		param.setLabel_1_name(label_1_name);
		param.setPic_url("//img.guanmai.cn/icon/icon-discount.1ac6d012.png");
		List<PromotionBaseParam.Label_2> label_2_list = new ArrayList<PromotionBaseParam.Label_2>();
		param.setLabel_2(label_2_list);
		param.setType(1);
		param.setSkus(new ArrayList<PromotionDefaultParam.Sku>());
		try {
			String promotion_id = promotionService.createPromotionDefault(param);
			Assert.assertNotEquals(promotion_id, null, "创建默认营销活动失败");

			List<PromotionSkuBean> skuList = categoryService.searchPromotionSku("C", 20);
			Assert.assertEquals(skuList != null, true, "查询销售SKU失败,无法对营销活动添加销售SKU修改");

			Assert.assertEquals(skuList.size() > 0, true, "没有查询到销售SKU,无法对营销活动添加销售SKU修改");

			PromotionSkuBean sku = NumberUtil.roundNumberInList(skuList);

			PromotionDefaultParam.Sku skuParam = param.new Sku();
			skuParam.set_gm_select(false);
			skuParam.setId(sku.getId());
			skuParam.setLabel_1_name(label_1_name);
			skuParam.setLabel_2_name(null);
			skuParam.setValue(sku.getId());

			List<PromotionDefaultParam.Sku> skuParamList = new ArrayList<>();
			skuParamList.add(skuParam);
			param.setSkus(skuParamList);
			param.setId(promotion_id);

			boolean result = promotionService.updatePromotionDefault(param);
			Assert.assertEquals(result, true, "修改默认营销规则-添加修改商品失败");

		} catch (Exception e) {
			logger.error("修改默认营销规则-添加修改商品的过程中出现错误: ", e);
			Assert.fail("修改默认营销规则-添加修改商品的过程中出现错误: ", e);
		}
	}

	@Test
	public void promotionEidtTestCase03() {
		ReporterCSS.title("测试点: 修改限购营销活动-添加商品");
		PromotionLimitParam param = new PromotionLimitParam();
		param.setName("AT" + StringUtil.getRandomNumber(6));
		param.setActive(1);
		param.setShow_method(1);
		param.setSort(1);
		param.setEnable_label_2(0);
		String label_1_name = StringUtil.getRandomString(8);
		param.setLabel_1_name(label_1_name);
		param.setPic_url("//img.guanmai.cn/icon/icon-discount.1ac6d012.png");
		List<PromotionBaseParam.Label_2> label_2_list = new ArrayList<PromotionBaseParam.Label_2>();
		param.setLabel_2(label_2_list);
		param.setType(2);

		try {
			List<PromotionSkuBean> skuList = categoryService.searchPromotionSku("e", 10);
			Assert.assertNotEquals(skuList, null, "查询销售商品接口调用失败");

			for (PromotionSkuBean limitSku : skuList) {
				limitSku.setPrice(new BigDecimal("0"));
				limitSku.setLimit_number(new BigDecimal(1));
				limitSku.setLabel_1_name(label_1_name);
			}
			param.setSkus(skuList);

			PromotionResultBean promotionResult = promotionService.createPromotionLimit(param);
			Assert.assertNotEquals(promotionResult, null, "创建限购营销活动失败");

			int code = promotionResult.getCode();
			if (code != 0) {
				if (code == 1) {
					List<String> usedSkus = promotionResult.getUsedSkus();
					skuList = skuList.stream().filter(s -> !usedSkus.contains(s.getId())).collect(Collectors.toList());

				} else if (code == 4) {
					List<String> deleteSkus = promotionResult.getDeleteSkus();
					skuList = skuList.stream().filter(s -> !deleteSkus.contains(s.getId()))
							.collect(Collectors.toList());
				}
				Assert.assertEquals(skuList.size() > 0, true, "选中的商品都被其他限购活动所占用了");
				param.setSkus(skuList);
				promotionResult = promotionService.createPromotionLimit(param);

			}
			Assert.assertEquals(promotionResult != null && promotionResult.getCode() == 0, true, "创建限购营销活动失败");

			BigDecimal task_id = promotionResult.getTask_id();
			boolean result = asyncService.getAsyncTaskResult(task_id, "失败0个");
			Assert.assertEquals(result, true, "新建限购营销活动的异步任务执行失败");

			PromotionFilterParam filterParam = new PromotionFilterParam();
			filterParam.setOffset(0);
			filterParam.setSearch_text(param.getName());
			List<PromotionBean> promotionList = promotionService.searchPromotion(filterParam);
			Assert.assertNotEquals(promotionList, null, "查询过滤营销活动失败");

			PromotionBean promotion = promotionList.stream().filter(p -> p.getName().equals(param.getName())).findAny()
					.orElse(null);
			Assert.assertNotEquals(promotion, null, "新建的限购营销活动失败");

			String promotion_id = promotion.getId();

			skuList = categoryService.searchPromotionSku("a", 12);
			Assert.assertNotEquals(skuList, null, "查询销售商品接口调用失败");

			for (PromotionSkuBean limitSku : skuList) {
				limitSku.setPrice(new BigDecimal("0"));
				limitSku.setLimit_number(new BigDecimal(1));
				limitSku.setLabel_1_name(label_1_name);
			}
			param.setSkus(skuList);
			param.setId(promotion_id);

			promotionResult = promotionService.updatePromotionLimit(param);
			Assert.assertNotEquals(promotionResult, null, "修改限购营销活动-添加商品失败");

			code = promotionResult.getCode();
			if (code != 0) {
				if (code == 1) {
					List<String> usedSkus = promotionResult.getUsedSkus();
					skuList = skuList.stream().filter(s -> !usedSkus.contains(s.getId())).collect(Collectors.toList());

				} else if (code == 4) {
					List<String> deleteSkus = promotionResult.getDeleteSkus();
					skuList = skuList.stream().filter(s -> !deleteSkus.contains(s.getId()))
							.collect(Collectors.toList());
				}
				Assert.assertEquals(skuList.size() > 0, true, "选中的商品都被其他限购活动所占用了");
				param.setSkus(skuList);
				promotionResult = promotionService.updatePromotionLimit(param);
			}
			Assert.assertEquals(promotionResult != null && promotionResult.getCode() == 0, true, "修改限购营销活动-添加商品失败");
		} catch (Exception e) {
			logger.error("修改限购营销活动-添加商品的过程中出现错误: ", e);
			Assert.fail("修改限购营销活动-添加商品的过程中出现错误: ", e);
		}
	}

	@AfterMethod
	public void afteMethod() {
		// 恢复数据,把新建的营销活动删除
		try {
			PromotionFilterParam promotionFilterParam = new PromotionFilterParam();
			promotionFilterParam.setSearch_text("AT");

			List<PromotionBean> promotions = promotionService.searchPromotion(promotionFilterParam);
			Assert.assertNotEquals(promotions, null, "搜索过滤营销活动失败");

			for (PromotionBean promotion : promotions) {
				boolean result = promotionService.deletePromotion(promotion.getId());
				Assert.assertEquals(result, true, "删除营销活动失败");
			}
		} catch (Exception e) {
			logger.error("营销活动删除过程中出现错误: ", e);
			Assert.fail("营销活动删除过程中出现错误: ", e);
		}
	}

}
