package cn.guanmai.manage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.TestNG;

import cn.guanmai.util.LoginUtil;

/**
 * @program: test_station
 * @description: ma主入口
 * @author: weird
 * @create: 2019-01-09 16:09
 **/
public class MainTest {
	private static Logger logger = LoggerFactory.getLogger(MainTest.class);
	public static Map<String, String> ma_cookie;

	public static void main(String[] args) {
		TestNG testng = new TestNG();
		try {
			if (ma_cookie == null) {
				ma_cookie = LoginUtil.loginManage();
				Assert.assertNotEquals(ma_cookie, null, "登录MA失败");
			}

			String dirPath = System.getProperty("user.dir");
			String tesngxmlPath = dirPath + "/testngxml/manage";
			List<String> suits = new ArrayList<String>();
			suits.add(0, dirPath + "testngxml/IReporterListener.xml");

			suits.add(tesngxmlPath + "/custommanage.xml");
			suits.add(tesngxmlPath + "/finance.xml");
			suits.add(tesngxmlPath + "/ordermanage.xml");
			suits.add(tesngxmlPath + "/report.xml");

			testng.setTestSuites(suits);
			testng.run();

		} catch (Exception e) {
			logger.error("登录MA系统报错 ", e);
			Assert.fail("登录MA报错 ", e);
		}
	}
}
