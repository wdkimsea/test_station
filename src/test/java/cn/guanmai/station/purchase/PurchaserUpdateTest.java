package cn.guanmai.station.purchase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.purchase.PurchaserBean;
import cn.guanmai.station.bean.purchase.PurchaserResponseBean;
import cn.guanmai.station.bean.purchase.PurchaserBean.SettleSupplier;
import cn.guanmai.station.bean.purchase.param.PurchaserParam;
import cn.guanmai.station.impl.purchase.PurchaserServiceImpl;
import cn.guanmai.station.interfaces.purchase.PurchaserService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Nov 27, 2018 2:34:10 PM 
* @des 修改采购员测试
* @version 1.0 
*/
public class PurchaserUpdateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PurchaserUpdateTest.class);
	private PurchaserService purchaserService;
	private PurchaserParam purchaserParam;
	private String phone;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		purchaserService = new PurchaserServiceImpl(headers);

		phone = "129" + StringUtil.getRandomNumber(8);
		String username = "AT" + StringUtil.getRandomNumber(6);

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

			List<PurchaserBean> purchasers = purchaserService.searchPurchaser(username);
			for (PurchaserBean purchaser : purchasers) {
				if (purchaser.getUsername().equals(username)) {
					purchaserParam.setId(purchaser.getId());
					break;
				}
			}
		} catch (Exception e) {
			logger.error("创建采购员遇到错误: ", e);
			Assert.fail("创建采购员遇到错误: ", e);
		}
	}

	/*
	 * 修改采购员
	 * 
	 */
	@Test
	public void purchaserUpdateTestCase01() {
		ReporterCSS.title("测试点: 修改采购员信息");
		purchaserParam.setName(StringUtil.getRandomString(4).toUpperCase());
		purchaserParam.setStatus(0);
		String new_phone = "12" + StringUtil.getRandomNumber(9);
		purchaserParam.setPhone(new_phone);
		String new_name = StringUtil.getRandomString(4);
		purchaserParam.setName(new_name);

		try {
			PurchaserResponseBean purchaserResponse = purchaserService.updatePurchaser(purchaserParam);
			String msg = purchaserResponse.getMsg();
			Assert.assertEquals(msg, "ok", "修改采购员信息失败");
			if (msg.equals("ok")) {
				PurchaserBean purchaser = purchaserService.getPurchaserDetail(purchaserParam.getId());
				Assert.assertNotEquals(purchaser, null, "获取采购员详细信息失败");
				boolean result = true;
				if (!purchaser.getName().equals(new_name)) {
					msg = String.format("修改采购员信息,名称与预期不符,预期:%s,实际:%s", new_name, purchaser.getName());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (!purchaser.getPhone().equals(new_phone)) {
					msg = String.format("修改采购员信息,电话号码与预期不符,预期:%s,实际:%s", new_phone, purchaser.getPhone());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (purchaser.getStatus() != purchaserParam.getStatus()) {
					msg = String.format("修改采购员信息,状态值与预期不符,预期:%s,实际:%s", purchaserParam.getStatus(),
							purchaser.getStatus());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
				Assert.assertEquals(result, true, "修改采购员后,信息与预期不符");
			}
		} catch (Exception e) {
			logger.error("修改采购员遇到错误: ", e);
			Assert.fail("修改采购员遇到错误: ", e);
		}
	}

	@AfterTest
	public void afterTest() {
		try {
			List<PurchaserBean> purchaserArray = purchaserService.searchPurchaser("");
			for (PurchaserBean purchaser : purchaserArray) {
				if (purchaser.getUsername().equals(purchaser.getUsername())) {
					List<SettleSupplier> settleSuppliers = purchaser.getSettle_suppliers();
					if (settleSuppliers == null || settleSuppliers.size() == 0) {
						boolean result = purchaserService.deletePurchaser(purchaser.getId());
						Assert.assertEquals(result, true, "删除采购员失败");
					}
				}
			}
		} catch (Exception e) {
			logger.error("删除采购员遇到错误: ", e);
			Assert.fail("删除采购员遇到错误: ", e);
		}
	}

}
