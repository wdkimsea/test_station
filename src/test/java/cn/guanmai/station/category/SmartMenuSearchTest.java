package cn.guanmai.station.category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.category.SkuPromotionBean;
import cn.guanmai.station.bean.category.SmartMenuBean;
import cn.guanmai.station.bean.category.SmartMenuDetailBean;
import cn.guanmai.station.bean.category.param.SmartMenuParam;
import cn.guanmai.station.impl.category.SmartMenuServiceImpl;
import cn.guanmai.station.impl.marketing.PromotionServiceImpl;
import cn.guanmai.station.interfaces.category.SmartMenuService;
import cn.guanmai.station.interfaces.marketing.PromotionService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author: liming
 * @Date: 2020年3月11日 下午3:43:36
 * @description:
 * @version: 1.0
 */

public class SmartMenuSearchTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SmartMenuSearchTest.class);

	private SmartMenuService smartMenuService;
	private PromotionService promotionService;
	private SmartMenuParam smartMenuParam;
	private String id;

	@BeforeClass
	private void initData() {
		Map<String, String> headers = getStationCookie();
		smartMenuService = new SmartMenuServiceImpl(headers);
		promotionService = new PromotionServiceImpl(headers);
		try {
			List<SmartMenuBean> smartMenus = smartMenuService.searchSmartMenu("", 10, 0);
			Assert.assertNotEquals(smartMenus, null, "搜索智能菜单失败");

			if (smartMenus.size() > 0) {
				SmartMenuBean smartMenu = NumberUtil.roundNumberInList(smartMenus);
				id = smartMenu.getId();
			} else {
				List<SkuPromotionBean> skuPromotions = promotionService.promotionSkus();
				Assert.assertNotEquals(skuPromotions, null, "拉取站点所有的销售SKU失败");
				Assert.assertEquals(skuPromotions.size() > 0, true, "此站点无销售SKU,无法新建智能菜单");

				SkuPromotionBean skuPromotion = NumberUtil.roundNumberInList(skuPromotions);
				List<String> sku_ids = Arrays.asList(skuPromotion.getId());

				String name = StringUtil.getRandomString(6).toUpperCase();
				smartMenuParam = new SmartMenuParam();
				smartMenuParam.setName(name);
				smartMenuParam.setSku_ids(sku_ids);
				smartMenuParam.setCombine_good_ids(new ArrayList<String>());
				id = smartMenuService.createSmartMenu(smartMenuParam);
				Assert.assertNotEquals(id, null, "新建智能菜单失败");
			}
		} catch (Exception e) {
			logger.error("新建智能菜单遇到错误: ", e);
			Assert.fail("新建智能菜单遇到错误: ", e);
		}
	}

	@Test
	public void smartMenuSearchTestCase01() {
		ReporterCSS.title("测试点: 按名称搜索智能菜单");
		try {
			SmartMenuDetailBean smartMenuDetail = smartMenuService.getSmartMenuDetail(id);
			Assert.assertNotEquals(smartMenuDetail, null, "获取智能菜单 " + id + " 详细信息失败");

			String name = smartMenuDetail.getName();

			List<SmartMenuBean> smartMenus = smartMenuService.searchSmartMenu(name, 10, 0);
			Assert.assertNotEquals(smartMenus, null, "搜索智能菜单失败");

			SmartMenuBean smartMenu = smartMenus.stream().filter(s -> s.getId().equals(id)).findAny().orElse(null);
			Assert.assertNotEquals(smartMenu, null, "按名称搜索没有搜索到目标智能菜单 " + id);

			List<String> names = smartMenus.stream().filter(s -> !s.getName().contains(name)).map(s -> s.getName())
					.collect(Collectors.toList());
			Assert.assertEquals(names.size(), 0, "按智能菜单名称 " + name + " 搜索,搜索出了如下不符合条件的智能菜单 " + name);
		} catch (Exception e) {
			logger.error("搜索智能菜单遇到错误: ", e);
			Assert.fail("搜索智能菜单遇到错误: ", e);
		}
	}

	@Test
	public void smartMenuSearchTestCase02() {
		ReporterCSS.title("测试点: 打印智能菜单");
		try {
			SmartMenuDetailBean smartMenuDetail = smartMenuService.printSmartMenu(id);
			Assert.assertNotEquals(smartMenuDetail, null, "打印智能菜单失败");
		} catch (Exception e) {
			logger.error("打印智能菜单遇到错误: ", e);
			Assert.fail("打印智能菜单遇到错误: ", e);
		}
	}
}
