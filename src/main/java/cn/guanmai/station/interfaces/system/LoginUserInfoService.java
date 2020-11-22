package cn.guanmai.station.interfaces.system;

import cn.guanmai.station.bean.system.LoginStationInfoBean;
import cn.guanmai.station.bean.system.LoginUserInfoBean;

/* 
* @author liming 
* @date Apr 11, 2019 10:49:03 AM 
* @des 站点用户相关信息接口
* @version 1.0 
*/
public interface LoginUserInfoService {
	/**
	 * 获取登录账户信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public LoginUserInfoBean getLoginUserInfo() throws Exception;

	/**
	 * 登录站点相关信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public LoginStationInfoBean getLoginStationInfo() throws Exception;

}
