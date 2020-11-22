package cn.guanmai.station.url;

import cn.guanmai.util.ConfigureUtil;

/* 
* @author liming 
* @date Feb 21, 2019 10:58:18 AM 
* @des 营销相关接口
* @version 1.0 
*/
public class MarketingURL {
	private static final String stationUrl = ConfigureUtil.getValueByKey("stationUrl");

	// 限时锁价搜索接口
	public static final String search_price_rule_url = stationUrl + "/station/price_rule/search";

	// 限时锁价修改接口
	public static final String edit_price_rule_url = stationUrl + "/station/price_rule/edit";

	// 限时锁价查询商户接口
	public static final String search_customer_url = stationUrl + "/station/customer/search";

	// 限时锁价查询商品接口
	public static final String search_sku_url = stationUrl + "/station/skus";

	// 限时锁价创建接口
	public static final String create_price_rule_url = stationUrl + "/station/price_rule/create";

	// 限时锁价,按商户商品查看接口
	public static final String search_price_rule_sku_url = stationUrl + "/station/price_rule/sku_search";

	// 营销活动,搜索过滤营销活动接口
	public static final String search_promotion_url = stationUrl + "/station/promotion/list";

	// 营销活动,获取营销活动详细信息接口
	public static final String get_promotion_detail_url = stationUrl + "/station/promotion/get";

	// 营销活动,删除营销活动接口
	public static final String delete_promotion_url = stationUrl + "/station/promotion/delete";

	// 营销活动,拉取所有销售SKU接口
	public static final String promotion_sku_list_url = stationUrl + "/station/promotion/sku/list";

	// 创建营销活动接口
	public static final String create_promotion_url = stationUrl + "/station/promotion/create";

	// 异步创建营销活动
	public static final String async_create_promotion_url = stationUrl + "/station/promotion/async_create";

	// 修改营销活动接口
	public static final String update_promotion_url = stationUrl + "/station/promotion/update";

	// 智能定价拉取商品详情接口
	public static final String get_smart_pricing_sku_list = stationUrl + "/product/sku/smart_pricing/list";

	// 智能定价更新接口
	public static final String update_smart_pricing_url = stationUrl + "/product/sku/smart_pricing/update";

	// 定价公司修改接口
	public static final String update_smart_formula_pricint_url = stationUrl
			+ "/product/sku/smart_formula_pricing/update";

	// 优惠券列表接口
	public static final String coupon_list_url = stationUrl + "/coupon/list";

	// 优惠券使用概况接口
	public static final String coupon_usage_list_url = stationUrl + "/coupon/usage/list";

	// 优惠券使用概况导出接口
	public static final String coupon_usage_export_url = stationUrl + "/coupon/usage/export";

	// 获取优惠券详细信息接口
	public static final String coupon_get_url = stationUrl + "/coupon/get";

	// 优惠券更新接口
	public static final String coupon_update_url = stationUrl + "/coupon/edit";

	// 创建优惠券接口
	public static final String coupon_create_url = stationUrl + "/coupon/create";

	// 优惠券商户接口
	public static final String coupon_address_list_url = stationUrl + "/coupon/address/list";

}
