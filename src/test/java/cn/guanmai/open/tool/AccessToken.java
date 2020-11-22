package cn.guanmai.open.tool;

import cn.guanmai.open.MainTest;
import cn.guanmai.open.bean.auth.OpenStationInfoBean;
import cn.guanmai.open.impl.auth.OpenAuthServiceImpl;
import cn.guanmai.open.interfaces.auth.OpenAuthService;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.util.LoginUtil;
import cn.guanmai.util.OpenApiUtil;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;

public class AccessToken {
	private String access_token = MainTest.access_token;
	private Map<String, String> st_headers = MainTest.st_headers;
	private Map<String, String> ma_headers = MainTest.ma_headers;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public Map<String, String> getSt_headers() {
		return st_headers;
	}

	public void setSt_headers(Map<String, String> st_headers) {
		this.st_headers = st_headers;
	}

	public Map<String, String> getMa_headers() {
		return ma_headers;
	}

	public void setMa_headers(Map<String, String> ma_headers) {
		this.ma_headers = ma_headers;
	}

	@BeforeTest
	public void getAccess() throws Exception {
		if (ma_headers == null) {
			String access_token = OpenApiUtil.getAccess_token();
			Assert.assertNotNull(access_token, "获取access_token失败");

			Map<String, String> st_headers = LoginUtil.loginStation();
			Assert.assertNotNull(st_headers, "登录Station失败");

			LoginUserInfoService loginUserInfoService = new LoginUserInfoServiceImpl(st_headers);
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取ST的登录信息失败");

			String station_id = loginUserInfo.getStation_id();
			Assert.assertNotNull(station_id, "获取Staiton ID失败");

			OpenAuthService openAuthService = new OpenAuthServiceImpl(access_token);

			OpenStationInfoBean openStationInfo = openAuthService.stationInfo();
			Assert.assertNotEquals(openStationInfo, null, "开放平台获取登录账号信息失败");

			Assert.assertEquals(openStationInfo.getStation_id(), station_id, "登录配置的Station账号和OpenAPI登录信息不是同一个站点");

			Map<String, String> ma_headers = LoginUtil.loginManage();
			Assert.assertNotNull(ma_headers, "登录Manage失败");

			this.access_token = access_token;
			this.st_headers = st_headers;
			this.ma_headers = ma_headers;
		}
	}
}
