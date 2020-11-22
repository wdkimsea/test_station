package cn.guanmai.station.url;

import cn.guanmai.util.ConfigureUtil;

/**
 * @author liming
 * @date 2019年8月7日 上午10:36:21
 * @des 净菜业务相关接口
 * @version 1.0
 */
public class JingcaiURL {
	private static final String stationUrl = ConfigureUtil.getValueByKey("stationUrl");

	// ******************************************************************//
	// *************************以下是工艺相关接口*************************//
	// ******************************************************************//

	// 添加工艺类型
	public static final String technic_category_create_url = stationUrl + "/process/technic_category/create";

	// 删除工艺类型
	public static final String technic_category_delete_url = stationUrl + "/process/technic_category/delete";

	// 拉取工艺类型列表
	public static final String technic_category_list_url = stationUrl + "/process/technic_category/list";

	// 添加工艺接口
	public static final String technic_create_url = stationUrl + "/process/technic/create";

	// 修改工艺接口
	public static final String technic_update_url = stationUrl + "/process/technic/update";

	// 拉取工艺列表接口
	public static final String technic_list_url = stationUrl + "/process/technic/list";

	// 获取指定的工艺信息
	public static final String technic_get_url = stationUrl + "/process/technic/get";

	// 删除指定工艺信息接口
	public static final String technic_delete_url = stationUrl + "/process/technic/delete";

	// 批量导入工艺接口
	public static final String technic_import_url = stationUrl + "/process/technic/import";

	// 新建车间接口
	public static final String workshop_create_url = stationUrl + "/process/workshop/create";

	// 修改车间接口
	public static final String workshop_update_url = stationUrl + "/process/workshop/update";

	// 删除车间接口
	public static final String workshop_delete_url = stationUrl + "/process/workshop/delete";

	// 搜索车间接口
	public static final String workshop_list_url = stationUrl + "/process/workshop/list";

	// 获取车间信息
	public static final String workshop_get_url = stationUrl + "/process/workshop/get";

	// 净菜原料对应工艺信息
	public static final String process_technic_flow_get_url = stationUrl + "/process/technic_flow/get";

	// 净菜原料添加工艺信息
	public static final String process_technic_flow_create_url = stationUrl + "/process/technic_flow/create_technic";

	// 净菜原料修改工艺信息
	public static final String process_technic_flow_update_url = stationUrl + "/process/technic_flow/update_technic";

	// 净菜原料删除工艺信息
	public static final String process_technic_flow_delete_url = stationUrl + "/process/technic_flow/delete_technic";

	// 净菜原料工艺切换顺序
	public static final String process_technic_flow_change_url = stationUrl
			+ "/process/technic_flow/change_technic_order";

	// ******************************************************************//
	// *************************以下是商品相关接口*************************//
	// ******************************************************************//

	// 新建商品标签
	public static final String label_create_url = stationUrl + "/process/label/create";

	// 删除商品标签
	public static final String label_delete_url = stationUrl + "/process/label/delete";

	// 商品标签列表
	public static final String label_list_url = stationUrl + "/process/label/list";

	// 创建净菜销售产品
	public static final String product_create_url = stationUrl + "/product/sku_sale/create";

	// 修改净菜销售产品
	public static final String product_update_url = stationUrl + "/product/sku/update";

	// 删除净菜销售产品
	public static final String product_delete_url = stationUrl + "/product/sku/delete";

	// 获取净菜商品
	public static final String product_list_url = stationUrl + "/product/sku_sale/list";

	// 获取商品成分
	public static final String product_ingredient_list_url = stationUrl + "/product/sku/ingredient/list";

	// 净菜搜索原料接口
	public static final String product_sku_ingredient_list_url = stationUrl + "/product/sku/ingredient/list";

	// 净菜原料百分比搜索接口
	public static final String product_sku_percentage_get_url = stationUrl + "/product/sku/percentage/get";

	// 净菜加工计划搜索接口
	public static final String process_task_search_url = stationUrl + "/stock/process/order_request/list";

	// 净菜加工计划发布接口
	public static final String process_task_release_url = stationUrl + "/stock/process/order_request/release_v2";

	// 净菜加工单据搜索接口
	public static final String process_order_search_url = stationUrl + "/stock/process/process_order/list";

}
