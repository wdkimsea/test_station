package cn.guanmai.station.marketing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.marketing.PromotionResultBean;
import cn.guanmai.station.bean.marketing.PromotionSkuBean;
import cn.guanmai.station.bean.marketing.param.PromotionBaseParam;
import cn.guanmai.station.bean.marketing.param.PromotionDefaultParam;
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
* @date Feb 21, 2019 3:44:47 PM 
* @des 创建营销活动测试
* @version 1.0 
*/
public class PromotionCreateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PromotionCreateTest.class);
	private PromotionService promotionService;
	private CategoryService categoryService;
	private AsyncService asyncService;
	private String promotion_id;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		promotionService = new PromotionServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
	}

	@BeforeMethod
	public void beforeMethod() {
		promotion_id = null;
	}

	@Test
	public void promotionCreateTestCase01() {
		ReporterCSS.title("测试点 : 创建默认营销活动(不添加商品)");

		PromotionDefaultParam param = new PromotionDefaultParam();
		param.setName(StringUtil.getRandomString(8));
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
			promotion_id = promotionService.createPromotionDefault(param);
			Assert.assertNotEquals(promotion_id, null, "创建默认营销活动失败");
		} catch (Exception e) {
			logger.error("创建默认营销活动的过程中出现错误: ", e);
			Assert.fail("创建默认营销活动的过程中出现错误: ", e);
		}
	}

	@Test
	public void promotionCreateTestCase02() {
		ReporterCSS.title("测试点 : 创建默认营销活动(添加商品)");

		PromotionDefaultParam param = new PromotionDefaultParam();
		param.setName(StringUtil.getRandomString(8));
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

		try {
			List<PromotionSkuBean> skuList = categoryService.searchPromotionSku("C", 20);
			Assert.assertEquals(skuList != null, true, "查询销售SKU失败,无法进行后续创建营销活动操作");

			Assert.assertEquals(skuList.size() > 0, true, "没有查询到销售SKU,无法进行后续创建营销活动操作");

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

			promotion_id = promotionService.createPromotionDefault(param);
			Assert.assertNotEquals(promotion_id, null, "创建默认营销活动失败");
		} catch (Exception e) {
			logger.error("创建默认营销活动的过程中出现错误: ", e);
			Assert.fail("创建默认营销活动的过程中出现错误: ", e);
		}
	}

	@Test
	public void promotionCreateTestCase03() {
		ReporterCSS.title("测试点 : 创建限购营销活动(异步)");
		PromotionLimitParam param = new PromotionLimitParam();
		param.setName("AT" + StringUtil.getRandomString(8));
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
			List<PromotionSkuBean> skuList = categoryService.searchPromotionSku("a", 20);
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
		} catch (Exception e) {
			logger.error("创建限购营销活动的过程中出现错误: ", e);
			Assert.fail("创建限购营销活动的过程中出现错误: ", e);
		}
	}

	@AfterMethod
	public void afteMethod() {
		if (promotion_id != null) {
			Reporter.log("恢复数据,把创建出来的营销活动删除");
			try {
				boolean result = promotionService.deletePromotion(promotion_id);
				Assert.assertEquals(result, true, "删除营销活动失败");
			} catch (Exception e) {
				logger.error("删除营销活动的过程中出现错误: ", e);
				Assert.fail("删除营销活动的过程中出现错误: ", e);
			}
		}
	}

}
