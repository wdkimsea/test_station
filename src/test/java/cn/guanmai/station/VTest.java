package cn.guanmai.station;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.TestNG;

import cn.guanmai.station.bean.system.LoginStationInfoBean;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.util.LoginUtil;
import cn.guanmai.util.ReportBotUtil;

/**
 * @author liming
 * @date 2019年8月21日
 * @time 下午2:14:08
 * @des TODO
 */

public class VTest {
	public static Map<String, String> headers;
	private static Logger logger = LoggerFactory.getLogger(MainTest.class);

	public static void main(String[] args) {
		TestNG testng = new TestNG();
		Integer stockMethod = null;
		boolean clean_food = false;

		try {
			if (headers == null) {
				headers = LoginUtil.loginStation();
				if (headers == null) {
					logger.error("登录失败");
					return;
				}
			}
			LoginUserInfoService loginUserInfoService = new LoginUserInfoServiceImpl(headers);
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取站点进销存类型失败");

			stockMethod = loginUserInfo.getStock_method();
			logger.info("进销存类型为: " + (stockMethod == 1 ? "加权平均" : "先进先出"));

			LoginStationInfoBean loginStationInfo = loginUserInfoService.getLoginStationInfo();
			Assert.assertNotEquals(loginStationInfo, null, "获取登录站点信息失败");
			clean_food = loginStationInfo.isClean_food();
			logger.info("是否为净菜站点: " + clean_food);
		} catch (Exception e) {
			logger.error("获取登入信息出现错误: ", e);
		}

		// ***第一种方式***//
		// testng.setTestClasses(new Class[] { CategoryCreateNormalTest.class
		// });

		List<String> suits = new ArrayList<String>();
		// 先加入监听
		suits.add("./testngxml/IReporterListener.xml");
		suits.add("./testngxml/station/test_common.xml");
		testng.setTestSuites(suits);

		String reportDir = "本地执行";
		String desc = "";
		if (args.length >= 3) {
			reportDir = args[2];
		}

		if (args.length >= 4) {
			desc = args[3];
		}
		testng.run();
		ReportBotUtil.sendReportResult(reportDir, desc);
	}

}
