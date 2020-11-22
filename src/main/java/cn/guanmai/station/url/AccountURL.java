package cn.guanmai.station.url;

import cn.guanmai.util.ConfigureUtil;

/* 
* @author liming 
* @date Apr 23, 2019 11:20:05 AM 
* @des 用户管理相关接口
* @version 1.0 
*/
public class AccountURL {
	private static final String stationUrl = ConfigureUtil.getValueByKey("stationUrl");

	// 获取全部权限
	public static final String get_permissions_url = stationUrl + "/gm_account/station/meta_info";
	// 查询角色接口
	public static final String search_role_url = stationUrl + "/gm_account/station/role/search";

	// 新增角色接口
	public static final String add_role_url = stationUrl + "/gm_account/station/role/create";

	// 获取角色详细信息
	public static final String get_role_detail_url = stationUrl + "/gm_account/station/role/detail";

	// 修改角色接口
	public static final String update_role_url = stationUrl + "/gm_account/station/role/update";

	// 删除角色接口
	public static final String delete_role_url = stationUrl + "/gm_account/station/role/delete";

	// 搜索查询用户接口
	public static final String search_user_url = stationUrl + "/gm_account/station/user/search";

	// 获取用户的详细信息接口
	public static final String get_user_detail_url = stationUrl + "/gm_account/station/user/detail";

	// 新建用户接口
	public static final String create_user_url = stationUrl + "/gm_account/station/user/create";

	// 更新用户信息接口
	public static final String update_user_url = stationUrl + "/gm_account/station/user/update";

	// 删除用户接口
	public static final String delete_user_url = stationUrl + "/gm_account/station/user/delete";

}
