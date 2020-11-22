package cn.guanmai.station.marketing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.marketing.PromotionBean;
import cn.guanmai.station.bean.marketing.PromotionDetailBean;
import cn.guanmai.station.bean.marketing.param.PromotionBaseParam;
import cn.guanmai.station.bean.marketing.param.PromotionDefaultParam;
import cn.guanmai.station.bean.marketing.param.PromotionFilterParam;
import cn.guanmai.station.impl.marketing.PromotionServiceImpl;
import cn.guanmai.station.interfaces.marketing.PromotionService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Feb 22, 2019 2:42:49 PM 
* @des 营销活动搜索查询测试类
* @version 1.0 
*/
public class PromotionSearchTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PromotionSearchTest.class);
	private PromotionService promotionService;
	private String promotion_id;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		promotionService = new PromotionServiceImpl(headers);

	}

	@BeforeMethod
	public void beforeMethod() {
		promotion_id = null;
	}

	@Test
	public void promotionSearchTestCase01() {
		Reporter.log("测试点: 查询过滤营销活动");
		PromotionFilterParam filterParam = new PromotionFilterParam();
		filterParam.setOffset(0);
		try {
			List<PromotionBean> promotionList = promotionService.searchPromotion(filterParam);
			Assert.assertNotEquals(promotionList, null, "查询过滤营销活动失败");
		} catch (Exception e) {
			logger.error("查询过滤营销活动遇到错误: ", e);
			Assert.fail("查询过滤营销活动遇到错误: ", e);
		}
	}

	@Test
	public void promotionSearchTestCase02() {
		Reporter.log("测试点: 查看营销活动详细信息");
		PromotionFilterParam filterParam = new PromotionFilterParam();
		filterParam.setOffset(0);
		String temp_promotion_id = null;
		try {
			List<PromotionBean> promotionList = promotionService.searchPromotion(filterParam);
			Assert.assertNotEquals(promotionList, null, "查询过滤营销活动失败");

			if (promotionList.size() > 0) {
				promotion_id = promotionList.get(0).getId();
				PromotionDetailBean promotionDetailInfo = promotionService.getPromotionDetailById(promotion_id);
				Assert.assertNotEquals(promotionDetailInfo, null, "获取营销活动的具体详细信息失败");
			} else {

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
					temp_promotion_id = promotionService.createPromotionDefault(param);
					Assert.assertNotEquals(temp_promotion_id, null, "创建默认营销活动失败");

					PromotionDetailBean promotionDetailInfo = promotionService
							.getPromotionDetailById(temp_promotion_id);
					Assert.assertNotEquals(promotionDetailInfo, null, "获取营销活动的具体详细信息失败");
				} catch (Exception e) {
					logger.error("创建默认营销活动的过程中出现错误: ", e);
					Assert.fail("创建默认营销活动的过程中出现错误: ", e);
				}
			}
		} catch (Exception e) {
			logger.error("查询过滤营销活动遇到错误: ", e);
			Assert.fail("查询过滤营销活动遇到错误: ", e);
		} finally {
			if (temp_promotion_id != null) {
				try {
					boolean result = promotionService.deletePromotion(temp_promotion_id);
					Assert.assertEquals(result, true, "删除营销活动失败");
				} catch (Exception e) {
					logger.error("删除营销活动遇到错误: ", e);
					Assert.fail("删除营销活动遇到错误: ", e);
				}
			}
		}
	}

}
