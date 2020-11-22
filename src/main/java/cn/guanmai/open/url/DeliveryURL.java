package cn.guanmai.open.url;

import cn.guanmai.util.ConfigureUtil;

/* 
* @author liming 
* @date Jun 5, 2019 2:51:14 PM 
* @des 配送相关接口
* @version 1.0 
*/
public class DeliveryURL {
	private static final String openUrl = ConfigureUtil.getValueByKey("openUrl");

	// 查询配送任务接口
	public static final String delivery_task_search_url = openUrl + "/delivery/task/list/1.0";

	// 订单分配司机
	public static final String delivery_driver_assign_url = openUrl + "/delivery/driver/assign/1.0";

	// 订单取消分配司机
	public static final String delivery_driver_cancel_url = openUrl + "/delivery/driver/cancel_assign/1.0";

	// 创建司机接口
	public static final String delivery_driver_create_url = openUrl + "/delivery/driver/create/1.0";

	// 修改司机接口
	public static final String delivery_driver_update_url = openUrl + "/delivery/driver/update/1.0";

	// 查询司机接口
	public static final String delivery_driver_search_url = openUrl + "/delivery/driver/list/1.0";

	// 查询车型列表接口
	public static final String delivery_car_model_search_url = openUrl + "/delivery/car_model/list/1.0";

	// 查询承运商列表接口
	public static final String delivery_carrier_search_url = openUrl + "/delivery/carrier/list/1.0";

	// 新建线路接口
	public static final String delivery_route_create_url = openUrl + "/delivery/route/create/1.0";

	// 修改线路接口
	public static final String delivery_route_update_url = openUrl + "/delivery/route/update/1.0";

	// 删除线路接口
	public static final String delivery_route_delete_url = openUrl + "/delivery/route/delete/1.0";

	// 查询路线列表
	public static final String delivery_route_search_url = openUrl + "/delivery/route/list/1.0";

	// 获取线路详细信息
	public static final String delivery_route_detail_url = openUrl + "/delivery/route/get/1.0";

}
