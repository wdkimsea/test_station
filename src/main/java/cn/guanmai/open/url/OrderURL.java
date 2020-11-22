package cn.guanmai.open.url;

import cn.guanmai.util.ConfigureUtil;

/* 
* @author liming 
* @date Jun 4, 2019 2:50:27 PM 
* @des 订单相关接口
* @version 1.0 
*/
public class OrderURL {
	private static final String openUrl = ConfigureUtil.getValueByKey("openUrl");

	// 创建订单接口
	public static final String order_create_url = openUrl + "/order/create/1.0";

	// 删除订单接口
	public static final String order_detele_url = openUrl + "/order/delete/1.0";

	// 修改订单接口
	public static final String order_update_url = openUrl + "/order/update/1.0";

	// 订单详细接口
	public static final String order_detail_url = openUrl + "/order/get/1.0";

	// 查询订单列表接口
	public static final String order_list_url = openUrl + "/order/list/1.0";

	// 订单添加商品接口
	public static final String order_sku_create_url = openUrl + "/order/sku/create/1.0";

	// 订单删除商品接口
	public static final String order_sku_delete_url = openUrl + "/order/sku/delete/1.0";

	// 订单修改商品接口
	public static final String order_sku_update_url = openUrl + "/order/sku/update/1.0";

	// 新建订单异常接口
	public static final String order_abnormal_create_url = openUrl + "/order/abnormal/create/1.0";

	// 新建订单退货接口
	public static final String order_refund_create_url = openUrl + "/order/refund/create/1.0";

	// 修改订单异常接口
	public static final String order_abnormal_update_url = openUrl + "/order/abnormal/update/1.0";

	// 修改订单退货接口
	public static final String order_refund_update_url = openUrl + "/order/refund/update/1.0";

	// 删除订单异常接口
	public static final String order_abnormal_delete_url = openUrl + "/order/abnormal/delete/1.0";

	// 删除订单退货接口
	public static final String order_refund_delete_url = openUrl + "/order/refund/delete/1.0";

	// 订单异常原因列表接口
	public static final String order_exception_reason_url = openUrl + "/order/exception_reason/list/1.0";

}
