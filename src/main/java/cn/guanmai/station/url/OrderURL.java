package cn.guanmai.station.url;

import cn.guanmai.util.ConfigureUtil;

/* 
* @author liming 
* @date Nov 12, 2018 10:18:08 AM 
* @des 订单的相关接口
* @version 1.0 
*/
public class OrderURL {
	private static final String stationUrl = ConfigureUtil.getValueByKey("stationUrl");

	// 搜索过滤订单接口
	public static final String search_order_url = stationUrl + "/station/orders";

	// 拉取所有的商户接口
	public static final String customer_list_url = stationUrl + "/station/order/customer/search";

	// 商户服务时间接口
	public static final String customer_service_time_list_url = stationUrl + "/station/order/service_time";

	// 检测商户是否能下单的接口
	public static final String customer_check_unpay_url = stationUrl + "/station/check_unpay";

	// 下单搜索商品接口
	public static final String search_sku_url = stationUrl + "/station/skus/addr";

	// 下单智能识别商品接口
	public static final String recognize_sku_url = stationUrl + "/station/skus/recognize";

	// 创建订单接口
	public static final String create_order_url = stationUrl + "/station/order/create";

	// 补录订单接口
	public static final String create_old_order_url = stationUrl + "/station/order/create_old";

	// 修改订单接口
	public static final String edit_order_url = stationUrl + "/station/order/edit";

	// 删除订单接口
	public static final String delete_order_url = stationUrl + "/station/order/delete_old";

	// 按预设数修改订单状态接口
	public static final String preconfig_order_status_url = stationUrl + "/station/order/update/status/preconfig";

	// 订单详细接口
	public static final String order_detail_url = stationUrl + "/station/order/edit";

	// Station订单按商品查看,修改的出库数接口
	public static final String order_real_quantity_update_url = stationUrl + "/station/order/real_quantity/update";

	// 修改订单状态接口
	public static final String update_order_state_url = stationUrl + "/station/order/set_status";

	// 批量设置缺货接口
	public static final String batch_out_of_stock_url = stationUrl + "/station/order/batch_out_of_stock/update";

	// 订单按商品查看-批量修改单价-同步最新采购价接口
	public static final String update_sku_price_auto_url = stationUrl + "/station/order/update_sku_price_auto_new";

	// 订单按商品查看-批量修改单价-同步最新采购价-查看结果接口
	public static final String update_sku_price_result_url = stationUrl + "/station/order/update_sku_price_result";

	// 手动修改订单中的商品单价
	public static final String update_sku_price_url = stationUrl + "/station/order/update_sku_price";

	// 订单列表按商品查看
	public static final String order_sku_list_url = stationUrl + "/station/order/order_sku_list";

	// 获取分拣备注接口
	public static final String get_sorting_remark_url = stationUrl + "/station/order/remark/get";

	// 订单添加售后接口
	public static final String order_exception_url = stationUrl + "/station/order/exception";

	// 订单详细分析接口(订单列表页面导出按钮)
	public static final String order_detail_sales_analysis_url = stationUrl + "/station/sales_analysis/orderdetail";

	// 显示该商户此运营时间的近 10 条历史订单接口
	public static final String get_recent_order_url = stationUrl + "/station/order/recent_order/get";

	// 复制订单接口
	public static final String copy_order_url = stationUrl + "/station/order/copy";

	// 导出批量导入订单模板
	public static final String export_order_template_url = stationUrl + "/station/order/batch/export";

	// 订单批量订单接口
	public static final String order_batch_upload_url = stationUrl + "/station/order/batch/template/upload";

	// 订单导入接口
	public static final String order_import_url = stationUrl + "/station/order/import";

	// 订单批量创建接口
	public static final String order_batch_submit_url = stationUrl + "/station/order/batch/submit";

	// 订单批量导入异步任务执行结果
	public static final String order_batch_result_url = stationUrl + "/station/order/batch/result";

	// 订单价格同步到销售SKU接口
	public static final String order_price_sync_to_sku_url = stationUrl + "/station/order/price_sync_to_sku";

	// 订单价格批量同步到销售SKU接口
	public static final String batch_order_price_sync_to_sku_url = stationUrl
			+ "/station/order/batch_price_sync_to_sku";

	// 订单价格批量同步到销售SKU结果接口
	public static final String batch_order_price_sync_to_sku_result_url = stationUrl
			+ "/station/order/batch_price_sync_to_sku/result";

	// 获取商户标签列表的接口
	public static final String address_label_list_url = stationUrl + "/station/address_label/list";

	// 新建订单选择好商户和商品后,切换商户,这个时候请求的接口
	public static final String order_sku_check_url = stationUrl + "/station/order/sku/check";

	// 订单替换商品-查询商品接口
	public static final String order_change_sku_search_url = stationUrl + "/station/order/order_change_sku/search";

	// 批量替换订单商品接口
	public static final String order_change_sku_url = stationUrl + "/station/order/batch_change_skus";

	// 批量删除订单接口
	public static final String order_delete_sku_url = stationUrl + "/station/order/batch_delete_skus";

	// 批量删除订单接口结果
	public static final String order_delete_sku_result_url = stationUrl + "/station/order/batch_delete_skus_result";

	// 批量替换订单商品接口
	public static final String order_change_sku_result_url = stationUrl + "/station/order/batch_change_skus_result";

	// 订单流接口
	public static final String order_process_list_url = stationUrl + "/station/order_process/list";

}
