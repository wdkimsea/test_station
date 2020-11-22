package cn.guanmai.station.marketing;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.category.SalemenuSkuBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.bean.category.param.SalemenuSkuFilterParam;
import cn.guanmai.station.bean.marketing.SmartPricingSkuBean;
import cn.guanmai.station.bean.marketing.param.SmartPricingSkuFilterParam;
import cn.guanmai.station.bean.marketing.param.SmartPricingUpdateParam;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.marketing.PricingServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.marketing.PricingService;
import cn.guanmai.station.tools.LoginStation;

/* 
* @author liming 
* @date May 30, 2019 2:44:06 PM 
* @des 智能定价测试
* @version 1.0 
*/
public class SmartPricingTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SmartPricingTest.class);
	private PricingService pricingService;
	private CategoryService categoryService;
	private AsyncService asyncService;

	private InitDataBean initData;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		pricingService = new PricingServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
		try {
			initData = getInitData();
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	@Test
	public void smartPricingTestCase01() {
		Reporter.log("测试点: 选择销售SKU,进行商品智能定价");
		try {
			String salemenu_id = initData.getSalemenu().getId();
			SalemenuSkuFilterParam filterParam = new SalemenuSkuFilterParam();
			filterParam.setSalemenu_id(salemenu_id);

			List<SalemenuSkuBean> skuSalemenuList = categoryService.searchSkuInSalemenu(filterParam);
			Assert.assertNotEquals(skuSalemenuList, null, "报价单 " + salemenu_id + " 展示销售SKU数据失败");

			JSONArray sku_ids = new JSONArray();
			SkuBean sku = null;
			if (skuSalemenuList.size() == 0) {
				sku = new SkuBean();
				SpuBean spu = initData.getSpu();
				// 填写SKU相关属性值,创建SKU必填
				sku = new SkuBean();
				sku.setSpu_id(spu.getId());
				sku.setOuter_id("");
				sku.setStd_sale_price(new BigDecimal("400"));
				sku.setPartframe(1);
				sku.setStd_unit_name(spu.getStd_unit_name());
				sku.setSlitting(1);
				sku.setSale_num_least(new BigDecimal("1"));
				sku.setStocks("-99999");
				sku.setSale_ratio(new BigDecimal("1"));
				sku.setSale_unit_name(spu.getStd_unit_name());
				sku.setDesc("");
				sku.setSupplier_id(initData.getSupplier().getId());
				sku.setIs_price_timing(0);
				sku.setIs_weigh(1);
				sku.setPurchase_spec_id(initData.getPurchaseSpec().getId());
				sku.setAttrition_rate(BigDecimal.ZERO);
				sku.setStock_type(1);
				sku.setName(spu.getName() + "|" + spu.getStd_unit_name());
				sku.setSalemenu_id(salemenu_id);
				sku.setState(1);
				sku.setSale_price(new BigDecimal("400"));
				sku.setRemark_type(7);

				String sku_id = categoryService.createSaleSku(sku);
				Assert.assertNotNull(sku_id, "新建SKU失败");

				sku_ids.add(sku_id);
			} else {
				List<String> temp_ids = skuSalemenuList.stream().map(s -> s.getSku_id()).collect(Collectors.toList());
				sku_ids = JSONArray.parseArray(JSON.toJSONString(temp_ids));
			}

			SmartPricingSkuFilterParam smartPricingSkuFilterParam = new SmartPricingSkuFilterParam();
			smartPricingSkuFilterParam.setSku_list(sku_ids);
			smartPricingSkuFilterParam.setAll(0);
			smartPricingSkuFilterParam.setFormula_type(2);
			smartPricingSkuFilterParam.setCal_num(100);

			List<SmartPricingSkuBean> smartPricingSkuList = pricingService
					.getSmartPricingSkuList(smartPricingSkuFilterParam);
			Assert.assertNotEquals(smartPricingSkuList, null, "智能定价,选取SKU后,展示的定价列表页面显示数据失败");

			SmartPricingUpdateParam updateParam = new SmartPricingUpdateParam();
			updateParam.setAll(0);
			updateParam.setSku_list(sku_ids);
			updateParam.setFormula_type(2);
			updateParam.setCal_num(new BigDecimal("100"));

			String task_id = pricingService.updateSmartPricing(updateParam);
			Assert.assertNotNull(task_id, "智能定价异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(new BigDecimal(task_id), "失败0");
			Assert.assertEquals(result, true, "智能定价异步任务执行失败");

		} catch (Exception e) {
			logger.error("商品智能定价过程中遇到错误: ", e);
			Assert.fail("商品智能定价过程中遇到错误: ", e);
		}
	}
}
