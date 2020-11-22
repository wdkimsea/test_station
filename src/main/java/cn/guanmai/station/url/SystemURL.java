package cn.guanmai.station.url;

import cn.guanmai.util.ConfigureUtil;

/* 
* @author liming 
* @date Nov 8, 2018 11:18:26 AM 
* @des 系统相关Web接口链接
* @version 1.0 
*/
public class SystemURL {
	private static final String stationUrl = ConfigureUtil.getValueByKey("stationUrl");

	// 服务时间相关接口
	public static final String service_time_old_url = stationUrl + "/station/service_time";

	// 服务时间列表接口
	public static final String service_time_list_url = stationUrl + "/service_time/list";

	// 获取指定运营时间接口
	public static final String service_time_get_url = stationUrl + "/service_time/get";

	// 运营时间新建、更新共用接口
	public static final String service_time_save_url = stationUrl + "/service_time/save";

	// 运营时间删除接口
	public static final String service_time_delete_url = stationUrl + "/station/service_time/delete";

	// 首页数据统计相关接口
	public static final String daily_profit_url = stationUrl + "/data_center/profit/daily/";

	// 异步任务列表接口
	public static final String task_list_url = stationUrl + "/task/list";

	// 获取异步任务详细信息接口
	public static final String task_detail_url = stationUrl + "/task/get";

	// 地理便签列表
	public static final String area_dict_url = stationUrl + "/station/area_dict";

	// 登录用户相关信息
	public static final String station_user_info_url = stationUrl + "/station/user";

	// 登录站点相关信息
	public static final String station_info_url = stationUrl + "/station/info";

	// 未读消息数量统计接口
	public static final String message_unread_count_url = stationUrl + "/message/unread/count";

	// 获取采购模板列表接口
	public static final String purchase_template_list_url = stationUrl + "/fe/purchase_tpl/list";

	// 获取采购模板详细接口
	public static final String purchase_template_detail_url = stationUrl + "/fe/purchase_tpl/get";

	// 获取入库模板接口
	public static final String stock_in_template_list_url = stationUrl + "/fe/stock_in_tpl/list";

	// 获取出库模板接口
	public static final String stock_out_template_list_url = stationUrl + "/fe/stock_out_tpl/list";

	// 获取入库模板详细接口
	public static final String stock_in_template_detail_url = stationUrl + "/fe/stock_in_tpl/get";

	// 获取出库单模板详情接口
	public static final String stock_out_template_detail_url = stationUrl + "/fe/stock_out_tpl/get";

	// 获取打印标签模板接口
	public static final String print_tag_template_list_url = stationUrl + "/station/print_tag/list";

	// 获取打印标签模板详细接口
	public static final String print_tag_template_detail_url = stationUrl + "/station/print_tag/tag_content";

	// 获取装箱标签列表接口
	public static final String box_template_list_url = stationUrl + "/box_template/list";

	// 获取装箱标签详情接口
	public static final String box_tempalte_detail_url = stationUrl + "/box_template/detail";

	// 创建订单批量导入模板接口
	public static final String order_import_template_create_url = stationUrl + "/station/order/batch/template/create";

	// 获取订单批量导入模板列表
	public static final String order_import_template_list_url = stationUrl + "/station/order/batch/template/list";

	// 获取订单批量导入模板详细信息接口
	public static final String order_import_template_detail_url = stationUrl + "/station/order/batch/template/detail";

	// 获取配送模板列表接口
	public static final String distribute_template_list_url = stationUrl + "/station/distribute_config/list";

	// 配送模板详细信息接口
	public static final String distribute_template_detail_info_url = stationUrl + "/station/distribute_config/get_new";

	// 获取配送模板列表接口
	public static final String settle_template_list_url = stationUrl + "/fe/settle_tpl/list";

	// 获取配送模板详细接口
	public static final String settle_template_detail_info_url = stationUrl + "/fe/settle_tpl/get";

	// 店铺运营设置相关信息
	public static final String customized_url = stationUrl + "/station/customized";

	// 短信配置相关信息接口
	public static final String sms_customized_info_url = stationUrl + "/sms/customized_info/get";

	// 店铺运营自定义设置接口
	public static final String customized_update_url = stationUrl + "/station/customized/update";

	// 订单自定义相关配置接口
	public static final String order_profile_update_url = stationUrl + "/station/profile_order/update";

	// 分拣配置接口
	public static final String sorting_profile_update_url = stationUrl + "/station/profile_sorting/update";

	// 系统设置-商品设置接口
	public static final String merchandise_profile_update_url = stationUrl + "/station/profile_merchandise/update";

	// 运费模板列表接口
	public static final String freight_list_url = stationUrl + "/station/freight/list";

	// 运费模板创建接口
	public static final String freight_create_url = stationUrl + "/station/freight/create";

	// 运费模板详细接口
	public static final String freight_detail_url = stationUrl + "/station/freight/detail";

	// 运费模板修改接口
	public static final String freight_update_url = stationUrl + "/station/freight/edit_new";

	// 删除运费模板接口
	public static final String freight_delete_url = stationUrl + "/station/freight/delete";

	// 设置默认运费模板接口
	public static final String freight_default_set_url = stationUrl + "/station/freight/setdefault";

	// 默认生效报价单接口
	public static final String freight_sale_menu_list_url = stationUrl + "/station/freight/sale_menu/list";

	// 设置生效报价单接口
	public static final String freight_sale_menu_update_url = stationUrl + "/station/freight/sale_menu/update";

	// 运费模板商户列表接口
	public static final String freight_address_list_url = stationUrl + "/station/freight/address/list";

	// 操作日志接口
	public static final String operation_log_list_url = stationUrl + "/station/op_log/list";

	// 获取操作日志详情接口
	public static final String operation_log_detail_url = stationUrl + "/station/op_log/get";

	// 导出操作日志接口
	public static final String operation_log_export_url = stationUrl + "/station/station/op_log/export";
}
