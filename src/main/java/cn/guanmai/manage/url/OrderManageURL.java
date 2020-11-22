package cn.guanmai.manage.url;

import cn.guanmai.util.ConfigureUtil;

/* 
* @author liming 
* @date Jan 18, 2019 6:56:46 PM 
* @des 订单管理相关接口
* @version 1.0 
*/
public class OrderManageURL {
	private static final String manage_url = ConfigureUtil.getValueByKey("manageUrl");

	// 售后-异常-每日订单接口
	public static final String ordermanage_daily_search = manage_url + "/ordermanage/daily/search";

	public static final String ordermanage_daily_info = manage_url + "/ordermanage/daily/info";

	// 每日订单导出接口
	public static final String ordermanage_daily_export = manage_url + "/ordermanage/daily/export";

	// 用户订单异常搜索
	public static final String ordermanage_order_search = manage_url + "/ordermanage/order/search";

	// 站点订单搜索
	public static final String ordermanage_lkorder_search = manage_url + "/ordermanage/lkorder/search";

	// 站点信息，上面接口需要用到的数据
	public static final String ordermanage_lkorder_info = manage_url + "/ordermanage/order/search/lkinfo";

	// 商户订单异常-获取订单详情接口
	public static final String ordermanage_order_info = manage_url + "/ordermanage/get";

	// 添加售后信息接口
	public static final String ordermanage_exception = manage_url + "/ordermanage/exception";

}
