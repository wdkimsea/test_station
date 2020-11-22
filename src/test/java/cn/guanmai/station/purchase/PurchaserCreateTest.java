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
* @date Nov 27, 2018 12:02:00 PM 
* @des 创建采购员测试
* @version 1.0 
*/
public class PurchaserCreateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PurchaserCreateTest.class);
	private PurchaserService purchaserService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		purchaserService = new PurchaserServiceImpl(headers);
	}

	/**
	 * 新建采购员
	 * 
	 */
	@Test
	public void purchaserCreateTestCase01() {
		ReporterCSS.title("测试点: 新建采购员");
		String phone = "129" + StringUtil.getRandomNumber(8);
		String username = "AT" + StringUtil.getRandomNumber(6);

		PurchaserParam purchaserParam = new PurchaserParam();
		purchaserParam = new PurchaserParam();
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
			Assert.assertEquals(msg.equals("ok") || msg.equals("账号 " + username + " 已存在"), true, "新建采购员失败");
		} catch (Exception e) {
			logger.error("创建采购员遇到错误: ", e);
			Assert.fail("创建采购员遇到错误: ", e);
		} finally {
			// 后置处理,新建成功后删除
			if (purchaserResponse != null && purchaserResponse.getCode() == 0) {
				try {
					List<PurchaserBean> purchaserArray = purchaserService.searchPurchaser(username);
					for (PurchaserBean purchaserBean : purchaserArray) {
						if (purchaserBean.getUsername().equals(username)) {
							String id = purchaserBean.getId();
							boolean result = purchaserService.deletePurchaser(id);
							Assert.assertEquals(result, true, "删除采购员失败");
							break;
						}
					}
				} catch (Exception e) {
					logger.error("删除采购员遇到错误: ", e);
					Assert.fail("删除采购员遇到错误: ", e);
				}
			}
		}
	}

	/**
	 * 新建采购员,用已存在的账号号去注册采购员
	 * 
	 */
	@Test
	public void purchaserCreateTestCase02() {
		String username = null;
		try {
			ReporterCSS.title("测试点: 新建采购员,用已存在账号去注册采购员,注册断言失败");
			List<PurchaserBean> purchasers = purchaserService.searchPurchaser("");
			if (purchasers.size() > 0) {
				username = purchasers.get(0).getUsername();
				if (username != null && !username.trim().equals("")) {
					PurchaserParam purchaserParam = new PurchaserParam();
					purchaserParam = new PurchaserParam();
					purchaserParam.setUsername(username);
					purchaserParam.setName(username);
					purchaserParam.setPhone("");
					purchaserParam.setPassword("123456");
					purchaserParam.setSettle_suppliers(new ArrayList<>());
					purchaserParam.setIs_allow_login(0);
					purchaserParam.setStatus(1);

					PurchaserResponseBean purchaserResponse = purchaserService.createPurchaser(purchaserParam);
					String msg = purchaserResponse.getMsg();
					Assert.assertEquals(msg, "账号 " + username + " 已存在", "用已存在的账号去注册采购员,提示与预期不符");
				}
			}
		} catch (Exception e) {
			logger.error("创建采购员遇到错误: ", e);
			Assert.fail("创建采购员遇到错误: ", e);
		}
	}
}
