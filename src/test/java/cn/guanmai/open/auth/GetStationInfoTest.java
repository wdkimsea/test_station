package cn.guanmai.open.auth;

import cn.guanmai.open.bean.auth.OpenStationInfoBean;
import cn.guanmai.open.impl.auth.OpenAuthServiceImpl;
import cn.guanmai.open.interfaces.auth.OpenAuthService;
import cn.guanmai.open.tool.AccessToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GetStationInfoTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(GetStationInfoTest.class);
	private OpenAuthService authService;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		authService = new OpenAuthServiceImpl(access_token);
	}

	@Test
	public void getStationInfoTestCase01() {
		try {
			OpenStationInfoBean station_info = authService.stationInfo();
			Assert.assertNotEquals(station_info, null, "获取站点信息失败");
		} catch (Exception e) {
			logger.error("获取站点信息过程中遇到错误: ", e);
			Assert.fail("获取站点信息过程中遇到错误: ", e);
		}
	}
}
