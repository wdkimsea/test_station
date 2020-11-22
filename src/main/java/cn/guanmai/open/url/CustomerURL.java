package cn.guanmai.open.url;

import cn.guanmai.util.ConfigureUtil;

/* 
* @author liming 
* @date Jun 6, 2019 9:58:13 AM 
* @des 商户相关接口
* @version 1.0 
*/
public class CustomerURL {
	private static final String openUrl = ConfigureUtil.getValueByKey("openUrl");

	// 新建商户接口
	public static final String customer_create_url = openUrl + "/customer/create/1.0";

	// 修改商户接口
	public static final String customer_update_url = openUrl + "/customer/update/1.0";

	// 查询商户接口
	public static final String customer_search_url = openUrl + "/customer/list/1.0";

	// 查询商户是否可以下单接口
	public static final String customer_order_status_check_url = openUrl + "/customer/check_order_status/1.0";

	// 商户绑定的地理标签
	public static final String customer_area_list_url = openUrl + "/customer/area/list/1.0";
}
