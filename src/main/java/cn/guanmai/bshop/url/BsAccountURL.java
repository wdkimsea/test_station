package cn.guanmai.bshop.url;

import cn.guanmai.util.ConfigureUtil;

/**
 * @author: liming
 * @Date: 2020年6月18日 下午4:39:34
 * @description: 商户相关接口
 * @version: 1.0
 */

public class BsAccountURL {
	public static String bshopUrl = ConfigureUtil.getValueByKey("bshopUrl");

	// 检测用户名是否存在的接口
	public static final String USER_NAME_CHECK_URL = bshopUrl + "/user/username/check";

	// 新用户注册接口
	public static final String REGISTER_URL = bshopUrl + "/register";

	// 注册区域接口
	public static final String REGISTER_AREA_URL = bshopUrl + "/register/area";

	// 添加店铺接口
	public static final String ADD_ADDRESS_URL = bshopUrl + "/user/address/add";

	// 登录账户信息接口
	public static final String USER_INFO_URL = bshopUrl + "/user/account";

	// 设置店铺接口
	public static final String SET_ADDRESS_URL = bshopUrl + "/user/address/set";

}
