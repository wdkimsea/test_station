package cn.guanmai.manage.tools;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;

import cn.guanmai.manage.MainTest;
import cn.guanmai.util.LoginUtil;

/* 
* @author liming 
* @date Mar 26, 2019 7:21:57 PM 
* @des 登录MA
* @version 1.0 
*/
public class LoginManage {
	private Logger logger = LoggerFactory.getLogger(LoginManage.class);
	private Map<String, String> ma_cookie = MainTest.ma_cookie;

	@BeforeTest
	public void loginManage() {
		if (ma_cookie == null) {
			try {
				ma_cookie = LoginUtil.loginManage();
				Assert.assertNotEquals(ma_cookie, null, "登录MA失败");
			} catch (Exception e) {
				logger.error("登录MA系统报错 ", e);
				Assert.fail("登录MA系统报错 ", e);
			}
		}
	}

	public Map<String, String> getManageCookie() {
		return ma_cookie;
	}

}
