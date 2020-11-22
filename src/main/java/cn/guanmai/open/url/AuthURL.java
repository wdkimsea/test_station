package cn.guanmai.open.url;

import cn.guanmai.util.ConfigureUtil;

public class AuthURL {

	private static final String openUrl = ConfigureUtil.getValueByKey("openUrl");

	// 获取站点信息接口
	public static final String station_info_url = openUrl + "/auth/user/station_info/get/1.0";
}
