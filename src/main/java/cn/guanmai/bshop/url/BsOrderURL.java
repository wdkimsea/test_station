package cn.guanmai.bshop.url;

import cn.guanmai.util.ConfigureUtil;

/**
 * @author: liming
 * @Date: 2020年6月16日 下午3:03:39
 * @description:
 * @version: 1.0
 */

public class BsOrderURL {
	public static final String bshopUrl = ConfigureUtil.getValueByKey("bshopUrl");

	// 销售商品搜索接口
	public static final String SEARCH_SKU_URL = bshopUrl + "/product/sku/search";

	// 更新购物车接口
	public static final String UPDATE_CART_URL = bshopUrl + "/cart/update";

	// 获取购物车接口
	public static final String GET_CART_URL = bshopUrl + "/cart/get";

	// 设置付款方式接口
	public static final String ORDER_PATMETHOD_URL = bshopUrl + "/order/paymethod";

	// 购物车确认接口
	public static final String ORDER_CONFIRM_URL = bshopUrl + "/order/confirm";

	// 设置收货时间接口
	public static final String ORDER_RECEIVE_TIME_URL = bshopUrl + "/order/receive_time";

	// 提交订单
	public static final String ORDER_SUBMIT_URL = bshopUrl + "/order/submit";

	// 获取订单详情
	public static final String ORDER_DETAIL_URL = bshopUrl + "/order/detail";

	// 订单支付接口
	public static final String ORDER_PAY_URL = bshopUrl + "/pay/order";

	// 部分支付订单列表
	public static final String ORDER_PART_PAY_LIST_URL = bshopUrl + "/order/partpay_list";

}
