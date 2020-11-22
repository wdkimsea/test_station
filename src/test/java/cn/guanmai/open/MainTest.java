package cn.guanmai.open;

import cn.guanmai.open.bean.auth.OpenStationInfoBean;
import cn.guanmai.open.impl.auth.OpenAuthServiceImpl;
import cn.guanmai.open.interfaces.auth.OpenAuthService;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.util.LoginUtil;
import cn.guanmai.util.OpenApiUtil;
import cn.guanmai.util.ReportBotUtil;
import cn.guanmai.util.TimeUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.TestNG;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainTest {
	public static String access_token;
	public static Map<String, String> st_headers;
	public static Map<String, String> ma_headers;

	private static Logger logger = LoggerFactory.getLogger(MainTest.class);

	public static void main(String[] args) {
		TestNG testng = new TestNG();
		try {
			access_token = OpenApiUtil.getAccess_token();
			Assert.assertNotNull(access_token, "获取access_token失败");

			st_headers = LoginUtil.loginStation();
			Assert.assertNotNull(st_headers, "登录Station失败");

			// 获取st登录账号信息
			LoginUserInfoService loginUserInfoService = new LoginUserInfoServiceImpl(st_headers);
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取ST的登录信息失败");

			String station_id = loginUserInfo.getStation_id();
			Assert.assertNotNull(station_id, "获取Staiton ID失败");

			OpenAuthService openAuthService = new OpenAuthServiceImpl(access_token);

			OpenStationInfoBean openStationInfo = openAuthService.stationInfo();
			Assert.assertNotEquals(openStationInfo, null, "开放平台获取登录账号信息失败");

			Assert.assertEquals(openStationInfo.getStation_id(), station_id, "登录配置的Station账号和OpenAPI登录信息不是同一个站点");

			ma_headers = LoginUtil.loginManage();
			Assert.assertNotNull(ma_headers, "登录Manage失败");
		} catch (Exception e) {
			logger.error("获取登录信息出现错误: ", e);
			Assert.fail("获取登录信息出现错误", e);
		}

		String reportDir = "本地执行";
		String module = "all";
		String method = "normal";
		String desc = "执行开始时间" + TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");

		if (args.length == 1) {
			reportDir = args[0];
		} else if (args.length == 2) {
			reportDir = args[0];
			module = args[1];
		} else if (args.length == 3) {
			reportDir = args[0];
			module = args[1];
			method = args[2];
		} else if (args.length == 4) {
			reportDir = args[0];
			module = args[1];
			method = args[2];
			desc = args[3];
		}

		String dirPath = System.getProperty("user.dir") + "/testngxml/openapi";
		File dirFile = new File(dirPath);
		String[] fileList = dirFile.list();

		List<String> suits = new ArrayList<String>();

		for (String fileName : fileList) {
			if (module.equals("all")) {
				suits.add("./testngxml/openapi/" + fileName);
			} else {
				if (fileName.contains(module)) {
					suits.add("./testngxml/openapi/" + fileName);
				}
			}
		}

		if (method.equals("normal")) {
			suits = suits.stream().filter(s -> s.contains("_normal")).collect(Collectors.toList());
		} else if (method.equals("abnormal")) {
			suits = suits.stream().filter(s -> s.contains("_abnormal")).collect(Collectors.toList());
		}

		// 监听加入到最前面
		suits.add(0, "./testngxml/IReporterListener.xml");

		logger.info(suits.toString());
		testng.setTestSuites(suits);
		testng.run();

		ReportBotUtil.sendReportResult(reportDir, desc);
	}
}
