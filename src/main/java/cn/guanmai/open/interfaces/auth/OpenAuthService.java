package cn.guanmai.open.interfaces.auth;

import cn.guanmai.open.bean.auth.OpenStationInfoBean;

public interface OpenAuthService {
	/**
	 * 通过access_token获取站点信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public OpenStationInfoBean stationInfo() throws Exception;
}
