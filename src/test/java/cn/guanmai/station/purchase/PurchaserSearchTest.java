package cn.guanmai.station.purchase;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.purchase.PurchaserBean;
import cn.guanmai.station.impl.purchase.PurchaserServiceImpl;
import cn.guanmai.station.interfaces.purchase.PurchaserService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;

/* 
* @author liming 
* @date Nov 27, 2018 11:16:03 AM 
* @des 采购员搜索查询
* @version 1.0 
*/
public class PurchaserSearchTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PurchaserSearchTest.class);
	private PurchaserService purchaserService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		purchaserService = new PurchaserServiceImpl(headers);
	}

	/**
	 * 搜索查询采购员
	 * 
	 */
	@Test
	public void purchaserSearchTestCase01() {
		try {
			ReporterCSS.title("测试单: 搜索查询采购员");
			List<PurchaserBean> purchasers = purchaserService.searchPurchaser("");
			Assert.assertNotEquals(purchasers, null, "搜索查询采购员失败");

			if (purchasers.size() > 0) {
				PurchaserBean purchaser = NumberUtil.roundNumberInList(purchasers);
				String userName = purchaser.getUsername();

				purchasers = purchaserService.searchPurchaser(userName);
				Assert.assertNotEquals(purchasers, null, "搜索查询采购员失败");

				purchaser = purchasers.stream().filter(p -> p.getUsername().equals(userName)).findAny().orElse(null);
				Assert.assertNotEquals(purchaser, null, "搜索过滤采购员,没有搜索出目标采购员");

				List<PurchaserBean> otherPurchasers = purchasers.stream()
						.filter(p -> !p.getUsername().contains(userName) && p.getName().contains(userName))
						.collect(Collectors.toList());
				Assert.assertEquals(otherPurchasers.size(), 0, "搜索过滤采购员,查询过滤出不符合输入条件的数据");
			}
		} catch (Exception e) {
			logger.error("查询采购员遇到错误: ", e);
			Assert.fail("查询采购员遇到错误: ", e);
		}
	}
}
