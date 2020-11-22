package cn.guanmai.station;

import java.io.File;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.TestNG;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.system.LoginStationInfoBean;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.bean.system.param.OrderProfileParam;
import cn.guanmai.station.impl.base.InitDataServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.impl.system.ProfileServiceImpl;
import cn.guanmai.station.interfaces.base.InitDataService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.interfaces.system.ProfileService;
import cn.guanmai.util.LoginUtil;
import cn.guanmai.util.ReportBotUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Nov 5, 2018 3:05:08 PM 
* @des 主测试方入口
* @version 1.0 
*/
public class MainTest {
	public static Map<String, String> headers;
	public static InitDataBean initData;
	private static Logger logger = LoggerFactory.getLogger(MainTest.class);

	public static void main(String[] args) {
		logger.info(args.length > 0 ? args.toString() : "运行时没有传参数");
		TestNG testng = new TestNG();
		Integer stock_method = null;
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

			stock_method = loginUserInfo.getStock_method();
			logger.info("进销存类型为: " + (stock_method == 1 ? "加权平均" : "先进先出"));

			LoginStationInfoBean loginStationInfo = loginUserInfoService.getLoginStationInfo();
			Assert.assertNotEquals(loginStationInfo, null, "获取登录站点信息失败");
			clean_food = loginStationInfo.isClean_food();
			logger.info("是否为净菜站点: " + clean_food);

			InitDataService initDataService = new InitDataServiceImpl(headers);
			initData = initDataService.getInitData();
			Assert.assertEquals(initData != null, true, "初始化站点数据失败");

			ProfileService profileService = new ProfileServiceImpl(headers);
			OrderProfileParam orderProfileParam = new OrderProfileParam();
			orderProfileParam.setOrder_create_purchase_task(0);
			boolean result = profileService.updateOrderProfile(orderProfileParam);
			Assert.assertEquals(result, true, "设置订单商品自动进入采购任务失败");

		} catch (Exception e) {
			logger.error("获取登入信息出现错误: ", e);
			Assert.fail("获取登入信息出现错误: ", e);
		}

		// ***第一种方式***//
		// testng.setTestClasses(new Class[] { CategoryCreateNormalTest.class
		// });

		String dirPath = System.getProperty("user.dir");
		String testngxmlPath = dirPath + "/testngxml/station";
		File dirFile = new File(testngxmlPath);
		String[] fileList = dirFile.list();

		List<String> suits = new ArrayList<String>();

		String module = "all";
		int threadPoolSize = 1;
		String reportUrl = "本地执行";
		String desc = TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");

		if (args.length >= 1) {
			module = args[0];
		}
		if (args.length >= 2) {
			String size = args[1];
			Pattern pattern = Pattern.compile("[0-9]*");
			if (pattern.matcher(size).matches()) {
				threadPoolSize = Integer.valueOf(size);
				if (threadPoolSize > 1 && threadPoolSize <= 4) {
					logger.info("启用多线程: 线程数为 " + threadPoolSize);
					testng.setSuiteThreadPoolSize(threadPoolSize);
				}
			}
		}
		if (args.length >= 3) {
			reportUrl = args[2];
		}
		if (args.length >= 4) {
			desc = args[3];
		}

		for (String fileName : fileList) {
			if (module.equals("all")) {
				suits.add(testngxmlPath + "/" + fileName);
			} else {
				if (fileName.contains(module)) {
					suits.add(testngxmlPath + "/" + fileName);
				}
			}
		}

		// 先进先出和加权平均执行用例区分
		if (stock_method == 1) {
			suits = suits.stream().filter(s -> !s.contains("xjxc")).collect(Collectors.toList());
		} else {
			suits = suits.stream().filter(s -> !s.contains("jqpj")).collect(Collectors.toList());
		}

		// 把监听加入最前面
		suits.add(0, dirPath + "/testngxml/IReporterListener.xml");

		logger.info(suits.toString());
		testng.setTestSuites(suits);

		testng.run();
		ReportBotUtil.sendReportResult(reportUrl, desc);
	}
}
