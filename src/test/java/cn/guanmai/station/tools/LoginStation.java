package cn.guanmai.station.tools;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;

import cn.guanmai.station.MainTest;
import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.impl.base.InitDataServiceImpl;
import cn.guanmai.station.interfaces.base.InitDataService;
import cn.guanmai.util.LoginUtil;

/* 
* @author liming 
* @date Mar 1, 2019 10:17:25 AM 
* @des 执行测试用例前的登录ST
* @version 1.0 
*/
public class LoginStation {
	private static Logger logger = LoggerFactory.getLogger(LoginStation.class);
	private static Map<String, String> headers = MainTest.headers;
	private static InitDataBean initData = MainTest.initData;

	@BeforeTest
	public void loginStation() {
		try {
			if (headers == null) {
				headers = LoginUtil.loginStation();
				Assert.assertEquals(headers != null, true, "登录ST失败");
				logger.info("cookie: " + headers);
			}
			if (initData == null) {
				InitDataService initDataService = new InitDataServiceImpl(headers);
				initData = initDataService.getInitData();
				Assert.assertEquals(initData != null, true, "初始化站点数据失败");
			}
		} catch (Exception e) {
			logger.error("登录系统失败: ", e);
			Assert.fail("登录系统失败: ", e);
		}
	}

	public Map<String, String> getStationCookie() {
		return headers;
	}

	public InitDataBean getInitData() {
		return initData;
	}

}
