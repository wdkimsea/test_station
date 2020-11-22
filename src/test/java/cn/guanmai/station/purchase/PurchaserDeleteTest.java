package cn.guanmai.station.purchase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import cn.guanmai.station.bean.purchase.PurchaserBean;
import cn.guanmai.station.bean.purchase.PurchaserResponseBean;
import cn.guanmai.station.bean.purchase.param.PurchaserParam;
import cn.guanmai.station.impl.purchase.PurchaserServiceImpl;
import cn.guanmai.station.interfaces.purchase.PurchaserService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Nov 27, 2018 3:22:06 PM 
* @des 删除采购员
* @version 1.0 
*/
public class PurchaserDeleteTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PurchaserDeleteTest.class);
	private PurchaserService purchaserService;
	private String id;
	private String username;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		purchaserService = new PurchaserServiceImpl(headers);

		String phone = "129" + StringUtil.getRandomNumber(8);
		username = "AT" + StringUtil.getRandomNumber(6);

		PurchaserParam purchaserParam = new PurchaserParam();
		purchaserParam.setUsername(username);
		purchaserParam.setName(username);
		purchaserParam.setPhone(phone);
		purchaserParam.setPassword("123456");
		purchaserParam.setSettle_suppliers(new ArrayList<>());
		purchaserParam.setIs_allow_login(0);
		purchaserParam.setStatus(1);

		PurchaserResponseBean purchaserResponse = null;
		try {
			purchaserResponse = purchaserService.createPurchaser(purchaserParam);
			String msg = purchaserResponse.getMsg();
			Assert.assertEquals(msg, "ok", "新建采购员失败");

			id = purchaserResponse.getData();

		} catch (Exception e) {
			logger.error("创建采购员遇到错误: ", e);
			Assert.fail("创建采购员遇到错误: ", e);
		}
	}

	/**
	 * 删除采购员
	 * 
	 */
	@Test
	public void purchaserDeleteTestCase01() {
		try {
			ReporterCSS.title("测试点: 删除采购员");
			boolean result = purchaserService.deletePurchaser(id);
			Assert.assertEquals(result, true, "删除采购员失败");

			List<PurchaserBean> purchasers = purchaserService.searchPurchaser(username);
			Assert.assertNotEquals(purchasers, null, "搜索过滤采购员失败");

			PurchaserBean purchaser = purchasers.stream().filter(p -> p.getId().equals(id)).findAny().orElse(null);
			Assert.assertEquals(purchaser, null, "删除的采购员没有实际删除,还可以查询到");
		} catch (Exception e) {
			logger.error("删除采购员遇到错误: ", e);
			Assert.fail("删除采购员遇到错误: ", e);
		}
	}

}
