package cn.guanmai.station.url;

import cn.guanmai.util.ConfigureUtil;

/* 
* @author liming 
* @date Jan 8, 2019 11:39:49 AM 
* @des 称重相关Web接口链接
* @version 1.0 
*/
public class WeightURL {
	private static final String stationUrl = ConfigureUtil.getValueByKey("stationUrl");

	// 分拣任务-分拣明细-批量缺货上报接口
	public static final String batch_out_of_stock_url = stationUrl + "/weight/batch_out_of_stock/update";

	// 新版称重软件-设置称重员工工号
	public static final String set_employee_url = stationUrl + "/weight/employee/set";

	// 新版称重软件-获取称重框列表
	public static final String get_weight_basket_list_url = stationUrl + "/weight/baskets/list";

	// 新版称重软件-新建称重框
	public static final String create_weight_basket_url = stationUrl + "/weight/baskets/create";

	// 新版称重软件-修改称重框
	public static final String update_weight_basket_url = stationUrl + "/weight/baskets/update";

	// 新版称重软件-删除称重框
	public static final String delete_weight_basket_url = stationUrl + "/weight/baskets/delete";

	// 新版称重软件-拉取称重商品的接口
	public static final String get_weight_skus_url = stationUrl + "/weight/skus";

	// 新版称重软件-批量缺货上报接口
	public static final String out_of_stock_url = stationUrl + "/weight/sku/out_of_stock";

	// 新版称重软件-取消称重操作接口
	public static final String un_out_of_stock_url = stationUrl + "/weight/sku/un_out_of_stock";

	// 新版称重软件-称重接口
	public static final String set_weight_url = stationUrl + "/weight/sku/set_weight";

	// 新版称重软件-获取打印标签的信息接口
	public static final String get_weight_sku_print_info_url = stationUrl + "/weight/sku/print/infos";

	// 新版称重软件-打印标签接口
	public static final String print_sku_weitht_url = stationUrl + "/weight/sku/print";

	// 新版称重软件-按计重商品分拣-获取商品分类组列表
	public static final String get_weight_group_list_url = stationUrl + "/weight/group/list";

	// 新版称重软件-创建称重分组
	public static final String create_weight_group_url = stationUrl + "/weight/group/create";

	// 新版称重软件-获取称重分组详细
	public static final String get_weight_group_detail_url = stationUrl + "/weight/group/detail";

	// 新版称重软件-更新称重分组详细
	public static final String update_weight_group_url = stationUrl + "/weight/group/update";

	// 新版称重软件-删除称重分组
	public static final String delete_weight_group_url = stationUrl + "/weight/group/remove";

	// 新版称重软件-按计重商品分拣-获取称重商品结果树
	public static final String get_weight_category_tree_url = stationUrl + "/weight/category/tree";

	// 新版称重软件-获取为分组商品列表
	public static final String get_weight_category_ungroup_tree_url = stationUrl + "/weight/category/ungroup_tree";

	// 老版本的称重软件(PC称重) 分拣单核查接口
	public static final String weigh_checklist_url = stationUrl + "/station/weigh/checklist/get";

	// 老版本的称重软件(PC称重) 统配称重快速打印接口
	public static final String old_union_dispath_fast_task_url = stationUrl + "/station/weigh/union_dispatch/fast_task";

	// 老版本的称重软件(PC称重) 获取所有的称重数据接口
	public static final String old_get_weigh_all_data_url = stationUrl + "/station/weigh/get_all_data";

	// 老版本的称重软件(PC称重) 差异对比接口
	public static final String old_get_diff_order_weight_url = stationUrl + "/station/weigh/get_diff_order_weight";

	// 老版本的称重软件(PC称重) 获取称重数据接口
	public static final String old_get_weigh_task_url = stationUrl + "/station/weigh/get_task";

	// 老版本的称重软件(PC称重) 生成称重数据接口
	public static final String old_pack_weigh_data_url = stationUrl + "/station/weigh/pack_data";

	// 老版本的称重软件(PC称重) 称重接口
	public static final String old_set_weight_url = stationUrl + "/station/weigh/set_weight";

	// 老版本的称重软件(PC称重) 拉取一二级分类
	public static final String old_what_can_i_do_url = stationUrl + "/station/weigh/what_can_i_do";

	// ST-供应链-分拣-分拣任务-分拣进度数据接口
	public static final String get_station_weigh_info_url = stationUrl + "/weight/weight_collect/weight_info/get";

	// ST-供应链-分拣-分拣任务
	public static final String weight_collect_randomorder_list_url = stationUrl
			+ "/weight/weight_collect/randomorder/list";

	// ST-供应链-分拣-分拣任务-商品分拣进度
	public static final String weight_collect_randomsku_list_url = stationUrl + "/weight/weight_collect/randomsku/list";

	// ST-供应链-分拣-分拣明细-按订单分拣
	public static final String weight_collect_order_list_url = stationUrl + "/weight/weight_collect/order/list";

	// ST-供应链-分拣-分拣明细-按商品分拣
	public static final String weight_collect_sku_list_url = stationUrl + "/weight/weight_collect/sku/list";

	// ST-供应链-分拣-分拣任务-分拣明细-批量修改缺货接口
	public static final String st_batch_out_of_stock_url = stationUrl + "/weight/batch_out_of_stock/update";

	/**********************************************************************************/
	/******************************* 以下是预分拣&PDA相关接口 ***************************/
	/**********************************************************************************/

	// 新版称重软件-预分拣-获取要进行预分拣sku列表
	public static final String weight_package_sku_list_url = stationUrl + "/weight/package/sku/list";

	// 获取指定预分拣包装
	public static final String get_weight_package_sku_url = stationUrl + "/weight/package/sku/package/get";

	// 新版称重软件-预分拣-获取要进行预分拣sku的包装详情
	public static final String weight_package_sku_package_list_url = stationUrl + "/weight/package/sku/package/list";

	// 预分拣/批量打印标签
	public static final String create_weight_package_url = stationUrl + "/weight/package/create";

	// 删除包装
	public static final String delete_weight_package_url = stationUrl + "/weight/package/delete";

	// pda 包装编码查询
	public static final String search_pda_package_url = stationUrl + "/weight/pda/package/get";

	// pda按商品拣货-商品列表
	public static final String search_pda_weight_sku_url = stationUrl + "/weight/pda/sku/list";

	// PDA 按商品拣货-商品详情
	public static final String pda_weight_sku_detail_url = stationUrl + "/weight/pda/sku/detail";

	// PDA 分拣详情页面
	public static final String pda_weight_sort_detail_url = stationUrl + "/weight/pda/sort/detail";

	// PDA 分拣
	public static final String pda_sort_set_weight_url = stationUrl + "/weight/pda/sort/set_weight";

	// PDA 设置缺货
	public static final String pda_out_of_stock_url = stationUrl + "/weight/pda/sort/out_of_stock";

	// PDA 按订单分拣,拉取订单列表
	public static final String pda_order_list_url = stationUrl + "/weight/pda/order/list";

	// PDA 按订单分拣,拉取订单详细
	public static final String pda_order_detail_url = stationUrl + "/weight/pda/order/detail";
}
