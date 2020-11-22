package cn.guanmai.manage.url;

import cn.guanmai.util.ConfigureUtil;

/**
 * @author liming
 * @date 2019年8月22日
 * @time 下午7:47:32
 * @des TODO
 */

public class ManageAccountURL {
	private static final String manage_url = ConfigureUtil.getValueByKey("manageUrl");

	// manage 业务平台角色列表接口
	public static final String gm_account_station_role_search_url = manage_url + "/gm_account/station/role/search";

	// station 一些基本信息
	public static final String gm_account_station_meta_info_url = manage_url + "/gm_account/station/meta_info";

	// 创建station 角色接口
	public static final String gm_account_station_role_create_url = manage_url + "/gm_account/station/role/create";

	// station用户搜索接口
	public static final String gm_account_station_user_search_url = manage_url + "/gm_account/station/user/search";

	// station用户创建接口
	public static final String gm_account_station_user_create_url = manage_url + "/gm_account/station/user/create";

	// staiton用户删除接口
	public static final String gm_account_station_user_delete_url = manage_url + "/gm_account/station/user/delete";

	// station角色删除接口
	public static final String gm_account_station_role_delete_url = manage_url + "/gm_account/station/role/delete";

	// station账号信息修改接口
	public static final String gm_account_station_user_update_url = manage_url + "/gm_account/station/user/update";

	// manage角色搜索接口
	public static final String gm_account_manage_role_search_url = manage_url + "/gm_account/ma/role/search";

	// manage获取权限列表
	public static final String gm_account_manage_meta_info_url = manage_url + "/gm_account/ma/meta_info";

	// manage角色新建接口
	public static final String gm_account_manage_role_create_url = manage_url + "/gm_account/ma/role/create";

	// manage账号搜索接口
	public static final String gm_account_manager_user_search_url = manage_url + "/gm_account/ma/user/search";

	// manage创建用户接口
	public static final String gm_account_manager_user_create_url = manage_url + "/gm_account/ma/user/create";
}
