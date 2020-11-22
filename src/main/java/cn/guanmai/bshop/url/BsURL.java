package cn.guanmai.bshop.url;

import cn.guanmai.util.ConfigureUtil;

/* 
* @author liming 
* @date Jan 12, 2019 11:20:12 AM 
* @des bshop相关Web接口
* @version 1.0 
*/
public class BsURL {
	private static final String bshopUrl = ConfigureUtil.getValueByKey("bshopUrl");

	// 登录接口
	public static final String login_url = bshopUrl + "/login";

	// 获取账号信息接口
	public static final String user_account_url = bshopUrl + "/user/account";

	// 选择分店接口
	public static final String set_address_url = bshopUrl + "/user/address/set";

	// 获取报价单列表接口
	public static final String get_salemenu_url = bshopUrl + "/util/salemenu";

	// 获取首页定制信息
	public static final String homepage_customized = bshopUrl + "/product/homepage/customized";

	// 获取商品分类接口
	public static final String get_category_url = bshopUrl + "/product/category/get";

	// 获取销售SKU接口
	public static final String get_sku_url = bshopUrl + "/product/sku/get";

	// 更新购物车接口
	public static final String update_cart_url = bshopUrl + "/cart/update";

	// 设置收货地址
	public static final String set_receive_address_url = bshopUrl + "/user/address/set";

	// 确认购物车接口
	public static final String confirm_order_url = bshopUrl + "/order/confirm";

	// 设置付款方式接口
	public static final String paymethod_url = bshopUrl + "/order/paymethod";

	// 查找销售SKU接口
	public static final String search_sku_url = bshopUrl + "/product/sku/search";

	// 设置收货时间接口
	public static final String set_receive_time_url = bshopUrl + "/order/receive_time";

	// 提交订单接口
	public static final String submit_order_url = bshopUrl + "/order/submit";

	// 获取购物车接口
	public static final String get_cart_url = bshopUrl + "/cart/get";

	// 获取自定义列表接口
	public static final String get_favorite_list_url = bshopUrl + "/favorite/list/get";

	// 获取营销列表接口
	public static final String get_promotion_list_url = bshopUrl + "/product/promotion/list";

	// 搜索热词接口
	public static final String hot_search = bshopUrl + "/product/hot_search";

	// 订单列表接口
	public static final String order_list_url = bshopUrl + "/order/list";

	// 订单总数接口
	public static final String order_count_url = bshopUrl + "/order/count";

	// 订单详细信息接口
	public static final String order_detail_url = bshopUrl + "/order/detail";

	// 把商品加入常用列表接口
	public static final String update_sku_to_favorite_url = bshopUrl + "/favorite/detail/update";

	// 修改用户信息接口
	public static final String edit_user_address_url = bshopUrl + "/user/address/edit";

	// 添加子账号接口
	public static final String add_subaccount_url = bshopUrl + "/user/subaccount";

	// 修改账号密码接口
	public static final String change_pwd_url = bshopUrl + "/user/change_pwd";

	// 首页自定义配置接口
	public static final String homepage_customized_url = bshopUrl + "/product/homepage/customized";

	public static final String promotion_sku_url = bshopUrl + "/product/sku/promotion";

	// 获取已领优惠券列表
	public static final String avail_coupon_list_url = bshopUrl + "/coupon/avail_coupon/list";

	// 获取可见的优惠券列表
	public static final String visible_coupon_list_url = bshopUrl + "/coupon/visible_coupon";

	/*********** 自提点相关接口 ********/
	// 获取账户注册类型,收货类型,司机是否展示接口
	public static final String get_optional_info = bshopUrl + "/user/optional_info";

	// 获取bshop自提点接口
	public static final String get_pick_up_station_list = bshopUrl + "/order/pick_up_station/list";

}
