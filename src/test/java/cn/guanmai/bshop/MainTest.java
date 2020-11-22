package cn.guanmai.bshop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.TestNG;

import cn.guanmai.bshop.test.BshopBusinessTest;
import cn.guanmai.util.LoginUtil;

/* 
* @author liming 
* @date Apr 23, 2019 10:12:08 AM 
* @des bshop测试主方法入口
* @version 1.0 
*/
public class MainTest {
	private static Logger logger = LoggerFactory.getLogger(BshopBusinessTest.class);
	public static Map<String, String> bs_headers;

	public static void main(String[] args) {
		try {
			bs_headers = LoginUtil.loginBshop();
			if (bs_headers == null) {
				logger.error("登录BSHOP失败");
				return;
			}

			TestNG testng = new TestNG();
			String dirPath = System.getProperty("user.dir");
			String testngxmlPath = dirPath + "/testngxml/bshop";
			List<String> suits = new ArrayList<String>();
			suits.add(dirPath + "/testngxml/IReporterListener.xml");
			suits.add(testngxmlPath + "/coupon.xml");

			testng.setTestSuites(suits);
			testng.run();
		} catch (Exception e) {
			logger.error("登录bshop遇到错误", e);
			Assert.fail("登录bshop遇到错误", e);
		}
	}

}
