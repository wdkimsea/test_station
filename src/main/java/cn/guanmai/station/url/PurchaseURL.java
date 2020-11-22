package cn.guanmai.station.url;

import cn.guanmai.util.ConfigureUtil;

/* 
* @author liming 
* @date Nov 16, 2018 10:31:58 AM 
* @des 采购相关接口
* @version 1.0 
*/
public class PurchaseURL {
	private static final String stationUrl = ConfigureUtil.getValueByKey("stationUrl");

	// 搜索采购任务接口
	public static final String purchase_task_search_url = stationUrl + "/purchase/task/search";

	// 采购任务新版搜索接口,从ES中拉取
	public static final String purchase_task_search_new_url = stationUrl + "/purchase/task/new_search";

	// 查看采购任务关联的订单和采购单
	public static final String purchase_task_history_url = stationUrl + "/purchase/task/history";

	// 获取站点所有的供应商接口
	public static final String purcahse_task_settle_suppliers_url = stationUrl + "/purchase/task/settle_suppliers";

	// 采购任务供应商列表
	public static final String purcahse_task_suppliers_url = stationUrl + "/purchase/task/suppliers";

	// 获取供应商绑定的采购员接口
	public static final String optional_suppliers_purchasers_url = stationUrl
			+ "/purchase/task/optional_suppliers_purchasers";

	// 采购任务总览接口
	public static final String purchase_task_summary_url = stationUrl + "/purchase/task/suppliers/summary";

	// 创建分享采购任务
	public static final String create_share_purcahse_task_url = stationUrl + "/purchase/task/share_token";

	// 获取分享的采购任务
	public static final String get_share_purcahse_task_url = stationUrl + "/purchase/task/print_no_login";

	// 手工进入生成采购任务
	public static final String create_purchase_task_by_order_url = stationUrl + "/purchase/task/create_by_order";

	// 建议采购任务接口
	public static final String purchase_supply_limit_url = stationUrl + "/purchase/task/search/supply_limit";

	// 导出采购任务接口
	public static final String export_purcahse_task_url = stationUrl + "/purchase/task/export";

	// 异步导出采购采购接口
	public static final String export_purcahse_task_v2_url = stationUrl + "/purchase/task/export_v2";

	// 发布采购任务接口
	public static final String release_purchase_task_url = stationUrl + "/purchase/task/release";

	// 生成采购单据
	public static final String create_purchase_sheet_url = stationUrl + "/purchase/task/create_sheet";

	// 新建采购条目用来搜索采购规格的接口
	public static final String purchaseSpec_suppliers_url = stationUrl + "/purchase/task/get_create_source";

	// 采购任务打印接口
	public static final String purchase_print_task_url = stationUrl + "/purchase/task/print";

	// 手工创建采购任务
	public static final String create_purchase_task_url = stationUrl + "/purchase/task/create_many";

	// 采购任务切换采购员接口
	public static final String purcahse_task_change_parchaser_url = stationUrl
			+ "/purchase/task/change_settle_supplier";

	// 采购任务可修改供应商列表以及采购员
	public static final String purchase_task_can_change_settle_suppliers_url = stationUrl
			+ "/purchase/task/settle_suppliers_can_change";

	// 采购任务切换供应商接口
	public static final String purcahse_task_change_supplier_url = stationUrl + "/purchase/task/change_settle_supplier";

	// 采购任务修改单次可供应量
	public static final String purchase_task_change_supply_limit_url = stationUrl
			+ "/purchase/task/change_supply_limit";

	// 优先供应商列表接口
	public static final String priority_supplier_list_url = stationUrl + "/supplier/priority_supplier/all_type/list";

	// 采购最新入库价格接口
	public static final String purchase_spec_ref_price_url = stationUrl + "/purchase/purchase_spec/ref_price";

	// 搜索采购员列表接口
	public static final String search_purchaser_url = stationUrl + "/purchase/purchaser/search";

	// 获取采购员详细信息接口
	public static final String get_purchaser_detail_url = stationUrl + "/purchase/purchaser/detail";

	// 创建采购员接口
	public static final String create_purchaser_url = stationUrl + "/purchase/purchaser/create";

	// 删除采购员接口
	public static final String delete_purchaser_url = stationUrl + "/purchase/purchaser/delete";

	// 修改采购员接口
	public static final String update_purchaser_url = stationUrl + "/purchase/purchaser/edit";

	// 重置采购员密码
	public static final String restore_purchaser_pwd_url = stationUrl + "/purchase/purchaser/restore_pwd";

	// 采购员登录Station
	public static final String purchaser_login_st_url = stationUrl + "/station/login";

	// 拉取没有绑定采购员的供应商列表接口
	public static final String no_bind_settle_suppliers_url = stationUrl
			+ "/purchase/purchaser/settle_suppliers_can_bind";

	// 拉取采购单据列表接口
	public static final String purchase_sheet_list_url = stationUrl + "/stock/purchase_sheet/get";

	// 删除采购单据接口
	public static final String delete_purchase_sheet_url = stationUrl + "/stock/purchase_sheet/delete";

	// 采购单据详情接口
	public static final String purchase_sheet_detail_url = stationUrl + "/stock/purchase_sheet/details";

	// 提交采购单据接口
	public static final String submit_purchase_sheet_url = stationUrl + "/stock/purchase_sheet/submit";

	// 创建分享采购单据接口
	public static final String create_share_purchase_sheet_url = stationUrl + "/stock/purchase_sheet/share_token";

	// 获取分享采购单据接口
	public static final String get_share_purchase_sheet_url = stationUrl + "/stock/purchase_sheet/details_no_login";

	/********************************** 采购助手APP相关接口 *******************************/
	// 采购助手APP登录账号信息接口
	public static final String get_purchase_assistant_info_url = stationUrl + "/purchase_assistant/info";

	// 获取登录账号绑定的供应商列表接口
	public static final String quoted_settle_suppliers_url = stationUrl + "/purchase_assistant/quoted_settle_suppliers";

	// 采购助手获取运营时间接口
	public static final String purchase_assistant_service_time_url = stationUrl + "/purchase_assistant/service_time";

	// 采购助手获取今日任务
	public static final String daily_work_url = stationUrl + "/purchase_assistant/daily_work";

	// 采购助手采购趋势汇总接口
	public static final String daily_count_url = stationUrl + "/purchase_assistant/task/daily_count/get";

	// 采购金额分布统计(供应商维度)接口
	public static final String supplier_count_url = stationUrl + "/purchase_assistant/task/supplier_count/get";

	// 供应商对应采购规格列表接口
	public static final String supplier_spec_url = stationUrl + "/purchase_assistant/supplier/get_specs";

	// 更新采购规格询价接口
	public static final String update_spec_quoted_price_url = stationUrl + "/purchase_assistant/edit_quoted_price";

	// 采购助手过滤采购任务接口
	public static final String search_purchase_assistant_task_url = stationUrl + "/purchase_assistant/task/search";

	// 采购助手新建采购任务接口
	public static final String create_purcahse_assistant_sheet_url = stationUrl
			+ "/purchase_assistant/purchase_sheet/task_create";

	// 采购助手指定供应商提供的采购规格接口
	public static final String settle_supplier_supply_skus_url = stationUrl
			+ "/purchase_assistant/settle_supplier/supply_sku";

	// 采购助手采购单据页面数据汇总接口
	public static final String get_purchase_sheet_count_url = stationUrl
			+ "/purchase_assistant/purchase_sheet_count/get";

	// 采购助手-采购单据页面获取采购单据列表接口
	public static final String get_purchase_sheets_url = stationUrl + "/purchase_assistant/purchase_sheets";

	// 采购助手-采购单据页面-获取采购单据详情接口
	public static final String get_purchase_sheet_detail_url = stationUrl
			+ "/purchase_assistant/purchase_sheet/details";

	// 采购助手-编辑采购单据接口
	public static final String update_purchase_assistant_sheet_url = stationUrl
			+ "/purchase_assistant/purchase_sheet/modify";

	// 采购助手-删除采购单据接口
	public static final String delete_purchase_assistant_sheet_url = stationUrl
			+ "/purchase_assistant/purchase_sheet/delete";

	// 采购助手-提交采购单据接口
	public static final String submit_purchase_assistant_sheet_url = stationUrl
			+ "/purchase_assistant/purchase_sheet/submit";

	// 采购助手-采购单据标记完成接口
	public static final String finish_task_url = stationUrl + "/purchase_assistant/finish_task";
}
