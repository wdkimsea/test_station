package cn.guanmai.station.url;

import cn.guanmai.util.ConfigureUtil;

/* 
* @author liming 
* @date Jan 4, 2019 10:35:36 AM 
* @des 配送相关接口
* @version 1.0 
*/
public class DistributeURL {
	private static final String stationUrl = ConfigureUtil.getValueByKey("stationUrl");

	// 搜索查询车型接口
	public static final String search_car_model_url = stationUrl + "/station/car_model/list";

	// 添加车型接口
	public static final String add_car_model_url = stationUrl + "/station/car_model/create";

	// 修改车型接口
	public static final String update_car_model_url = stationUrl + "/station/car_model/update";

	// 删除车型接口
	public static final String delete_car_model_url = stationUrl + "/station/car_model/delete";

	// 搜索查询车型接口
	public static final String search_carrier_url = stationUrl + "/station/carrier/list";

	// 添加承运商接口
	public static final String add_carrier_url = stationUrl + "/station/carrier/create";

	// 修改承运商接口
	public static final String update_carrier_url = stationUrl + "/station/carrier/update";

	// 删除承运商接口
	public static final String delete_carrier_url = stationUrl + "/station/carrier/delete";

	// 查询司机接口
	public static final String search_driver_url = stationUrl + "/station/driver/list";

	// 添加司机接口
	public static final String add_driver_url = stationUrl + "/station/driver/create";

	// 修改司机接口
	public static final String update_driver_url = stationUrl + "/station/driver/update";

	// 删除司机接口
	public static final String delete_driver_url = stationUrl + "/station/driver/delete";

	// 获取司机列表接口
	public static final String get_driver_list_url = stationUrl + "/station/task/distribute/get_drivers";

	// 重置司机APP密码接口
	public static final String reset_driver_password_url = stationUrl + "/station/driver_manage/reset_password/";

	// 拉取配送单模板列表接口
	public static final String get_distribute_config_url = stationUrl + "/station/distribute_config/get";

	// 配送单模板选中值接口
	public static final String get_distribute_config_selected_url = stationUrl
			+ "/station/distribute_config/old_or_new/get";

	// 获取配送打印分类自定义信息接口
	public static final String delivery_category_config_get_url = stationUrl + "/delivery/category_config/get";

	// 更新配送打印分类自定义信息接口
	public static final String delivery_category_config_update_url = stationUrl + "/delivery/category_config/update";

	// 拉取配送单模板列表接口
	public static final String distribute_config_list_url = stationUrl + "/station/distribute_config/list";

	// 获取配送单模板详细信息接口
	public static final String distribute_config_detail_url = stationUrl + "/station/distribute_config/get_new";

	// 智能规划接口
	public static final String auto_assign_distribute_task_url = stationUrl + "/station/task/distribute/auto_assign";

	// 配送-搜索配送订单任务列表接口
	public static final String search_distribute_orders_url = stationUrl + "/station/task/distribute/orders/get";

	// 配送-司机任务列表查询接口
	public static final String driver_distribute_task_url = stationUrl + "/station/task/distribute/driver_tasks/get";

	// 订单分配司机接口
	public static final String edit_assign_distribute_task_url = stationUrl + "/station/task/distribute/edit_assign";

	// 获取配送订单详细信息列表接口
	public static final String get_distribute_order_detail_url = stationUrl + "/station/distribute/get_order_by_id";

	// 获取司机配送单信息
	public static final String print_driver_tasks_url = stationUrl + "/station/transport/driver_tasks/print";

	// 编辑配送单
	public static final String submit_distribution_order_url = stationUrl
			+ "/station/transport/distribution_order/submit";

	// 新增打印日志接口
	public static final String create_print_log_url = stationUrl + "/station/print_log/create";

	// 获取订单同步配送单同步配置接口
	public static final String get_delivery_setting_url = stationUrl + "/delivery/get";

	// 提交订单同步配送单同步配置
	public static final String submit_delivery_setting_url = stationUrl + "/delivery/create";

	// 更新套账单接口
	public static final String update_leger_url = stationUrl + "/delivery/update";

	/*************************** 线路相关接口 ****************************/
	// 新建路线
	public static final String create_route_url = stationUrl + "/station/address_route/create";

	// 删除路线
	public static final String delete_route_url = stationUrl + "/station/address_route/delete";

	// 获取路线列表
	public static final String get_route_list_url = stationUrl + "/station/address_route/list";

	// 获取路线详细信息
	public static final String get_route_bind_customer_url = stationUrl + "/station/address_route/get";

	// 更新路线绑定的商户
	public static final String update_route_bind_customer_url = stationUrl + "/station/address_route/update";

	// 搜索线路任务列表接口
	public static final String search_route_task_url = stationUrl + "/station/task/distribute/route_task";

	/*************************** 司机APP相关接口 ****************************/

	// 获取司机APP首页相关数据接口
	public static final String get_homepage_data_url = stationUrl + "/driver/homepage";

	// 获取司机APP配送任务接口
	public static final String get_delivery_task_url = stationUrl + "/driver/delivery/list";

	// 司机APP,进行完成配送操作接口
	public static final String finish_delivery_url = stationUrl + "/driver/delivery/finish";

	// 获取配送订单详细信息接口
	public static final String get_delivery_order_url = stationUrl + "/driver/delivery/detail";

	// 装车验货信息接口
	public static final String get_delivery_confirm_info_url = stationUrl + "/driver/delivery/confirm_info";

	// 商品装车验证接口
	public static final String product_confirm_url = stationUrl + "/driver/product/confirm";

	// 订单完成装车
	public static final String order_delivery_confirm_url = stationUrl + "/driver/delivery/confirm";

	// 开始配送
	public static final String start_delivery_url = stationUrl + "/driver_performance/delivery/start";

	// 获取司机状态接口
	public static final String get_driver_status_url = stationUrl + "/driver_performance/delivery/status";

	// 司机APP上传坐标
	public static final String upload_driver_trace_point_url = stationUrl + "/driver_performance/trace_point/create";

	// 更新商户收货地址接口
	public static final String update_customer_address_url = stationUrl + "/driver/address/update";

	// 售后选项接口
	public static final String exception_options_url = stationUrl + "/driver/delivery/exception/options";

	// 获取订单售后信息
	public static final String get_delivery_order_exception_url = stationUrl + "/driver/delivery/order/exception/get";

	// 发起售后前的检测接口
	public static final String check_return_delivery_order_url = stationUrl + "/driver/delivery/order/check_return";

	// 为订单添加售后信息接口
	public static final String update_delivery_order_url = stationUrl + "/driver/delivery/order/exception/update";

	/*************************** 自提点相关接口 ****************************/
	// 创建自提点
	public static final String create_pick_up_station = stationUrl + "/station/pick_up_station/create";

	// 查询单个自提点
	public static final String query_pick_up_station_get = stationUrl + "/station/pick_up_station/get";

	// 查询多个自提点
	public static final String query_pick_up_station_list = stationUrl + "/station/pick_up_station/list";

	// 修改自提点
	public static final String update_pick_up_station = stationUrl + "/station/pick_up_station/update";

	// 删除自提点
	public static final String delete_pick_up_station = stationUrl + "/station/pick_up_station/delete";
}
