package cn.guanmai.station.url;

import cn.guanmai.util.ConfigureUtil;

/* 
* @author liming 
* @date Jan 7, 2019 11:21:20 AM 
* @des 进销存Web相关接口
* @version 1.0 
*/
public class InvoicingURL {
	private static final String stationUrl = ConfigureUtil.getValueByKey("stationUrl");

	// 商户退货入库查询接口
	public static final String search_refund_url = stationUrl + "/stock/refund/search";

	// 商户退货入库数据导出接口
	public static final String export_refund_url = stationUrl + "/stock/refund/export";

	// 获取指定采购规格的供应商列表接口
	public static final String get_purchase_sku_settle_supplier_url = stationUrl + "/stock/settle_supplier/get";

	// 编辑商户退货处理接口
	public static final String edit_refund_url = stationUrl + "/stock/refund/edit";

	// 商户退货操作的一些候选项
	public static final String refund_base_url = stationUrl + "/stock/refund/base";

	/** ------------ 成品入库相关 Web 接口 ---------- **/

	// 创建入库单Web接口
	public static final String create_in_stock_sheet_url = stationUrl + "/stock/in_stock_sheet/material/create";

	// 创建入库单新接口
	public static final String new_create_in_stock_sheet_url = stationUrl + "/stock/in_stock_sheet/material/create/new";

	// 获取入库单详细信息接口
	public static final String get_in_stock_sheet_detail_url = stationUrl + "/stock/in_stock_sheet/material/new_detail";

	// 获取供应商能提供的采购SKU列表接口
	public static final String get_supply_sku_url = stationUrl + "/stock/in_stock_sku/supply_sku";

	// 新版采购入库搜索商品接口
	public static final String new_supply_sku_url = stationUrl + "/stock/in_stock_sku/supply_sku_new";

	// 获取指定供应商指定商品入库均价的接口
	public static final String supplier_avg_price_url = stationUrl + "/purchase/purchase_spec/avg_price/get";

	// 修改入库单接口
	public static final String modify_in_stock_sheet_url = stationUrl + "/stock/in_stock_sheet/material/modify";

	// 入库单编辑页面,查看采购规格近期价格接口
	public static final String purchase_spec_price_url = stationUrl + "/station/purchase_spec/price_statistics";

	// 入库单审核不通过接口
	public static final String review_in_stock_sheet_url = stationUrl + "/stock/in_stock_sheet/material/review";

	// 入库单审核不通过接口
	public static final String cancel_in_stock_sheet_url = stationUrl + "/stock/in_stock_sheet/material/cancel";

	// 入库单打印模板列表接口
	public static final String stock_in_template_list_url = stationUrl + "/fe/stock_in_tpl/list";
	// 打印入库单日志接口
	public static final String print_in_stock_sheet_log_url = stationUrl
			+ "/stock/in_stock_sheet/material/print_log/create";
	// 打印成品入库单拉取数据接口
	public static final String print_in_stock_sheet_url = stationUrl + "/stock/in_stock_sheet/material/print";

	// 搜索过滤成品入库单接口
	public static final String search_in_stock_sheet_url = stationUrl + "/stock/in_stock_sheet/material/list";

	// 下载批量导入入库单模板接口
	public static final String download_in_stock_sheet_template_url = stationUrl
			+ "/stock/in_stock_sheet/material/download_template";

	// 批量导入采购入库单接口
	public static final String import_in_stock_sheet_url = stationUrl + "/stock/in_stock_sheet/material/import";

	/** ------------ 销售出库相关 Web 接口 ---------- **/

	// 搜索成品出库单接口
	public static final String search_out_stock_sheet_url = stationUrl + "/stock/out_stock_sheet/list";

	// 手工创建成品出库单接口
	public static final String create_out_stock_sheet_url = stationUrl + "/stock/out_stock_sheet/create";

	// 获取成品出库单详细信息接口
	public static final String out_stock_sheet_detail_url = stationUrl + "/stock/out_stock_sheet/detail";

	// 出库单手工出库接口
	public static final String modify_out_stock_sheet_url = stationUrl + "/stock/out_stock_sheet/modify";

	// 冲销成品出库单接口
	public static final String cancel_out_stock_sheet_url = stationUrl + "/stock/out_stock_sheet/cancel";

	// 手工出库库存不足提醒
	public static final String negative_stock_remind_single_url = stationUrl
			+ "/stock/out_stock_sheet/negative_stock_remind_single";

	public static final String negative_stock_remind_batch_url = stationUrl
			+ "/stock/out_stock_sheet/negative_stock_remind_batch";

	// 新的批量出库负库存提醒接口
	public static final String negative_stock_remind_batch_new_url = stationUrl
			+ "/stock/out_stock_sheet/negative_stock_remind_batch_new";

	// 成品出库批量出库接口
	public static final String batch_out_stock_url = stationUrl + "/stock/out_stock_sheet/submit/batch";

	// 修复出库单价接口
	public static final String update_out_stock_price_url = stationUrl + "/stock/out_stock_price/update";

	// 出库搜索销售SKU接口
	public static final String search_stock_sale_sku_url = stationUrl + "/stock/search_sale_sku";

	/**** 成品出库单相关接口 *****/

	// 新建成品退货单接口
	public static final String create_return_stock_sheet_url = stationUrl + "/stock/return_stock_sheet/create";

	// 新建采购退货单接口(新)
	public static final String new_create_return_stock_sheet_url = stationUrl + "/stock/return_stock_sheet/new_create";

	// 获取成品退货单详细信息接口
	public static final String return_stock_sheet_detail_url = stationUrl + "/stock/return_stock_sheet/detail";

	// 修改提交成品退货单接口
	public static final String edit_return_stock_sheet_url = stationUrl + "/stock/return_stock_sheet/modify";

	// 冲销成品退货单接口
	public static final String cancel_return_stock_sheet_url = stationUrl + "/stock/return_stock_sheet/cancel";

	// 搜索过滤成品退货单接口
	public static final String search_return_stock_sheet_url = stationUrl + "/stock/return_stock_sheet/list";

	// 下载成品退货批量导入模板
	public static final String down_return_stock_sheet_template_url = stationUrl
			+ "/stock/return_stock_sheet/download_template";

	// 采购退货批量导入接口
	public static final String import_return_stock_sheet_url = stationUrl + "/stock/return_stock_sheet/import";

	/**** 商品盘点相关接口 ****/
	// 进销存获取一二级分类列表,用于分类搜索接口
	public static final String categories_url = stationUrl + "/station/skucategories";

	// 商品盘点列表接口
	public static final String search_stock_check_url = stationUrl + "/stock/list";

	// 修改SPU库存接口
	public static final String edit_spu_stock_url = stationUrl + "/stock/edit";

	// 下载批量盘点模板接口
	public static final String download_stock_check_template_url = stationUrl + "/stock/check/template";

	// 批次盘点接口
	public static final String check_batch_stock_url = stationUrl + "/stock/check/batch";

	// 上传批次批量盘点模板文件
	public static final String upload_stock_check_template_url = stationUrl + "/stock/check/upload";

	// 安全库存接口
	public static final String stock_warning_url = stationUrl + "/stock/warning";

	// 库存变动日志接口
	public static final String spu_stock_change_log_url = stationUrl + "/stock/change_log/list";

	// 批量盘点接口
	public static final String batch_stock_check_url = stationUrl + "/stock/check/batch";

	// 修复库存均价接口
	public static final String stock_avg_price_update_url = stationUrl + "/stock/avg_price/update";

	/**** 出入库明细接口 ****/

	// 成品入库日志接口
	public static final String in_stock_record_url = stationUrl + "/stock/in_stock_sku";

	// 商户退货入库记录接口
	public static final String refund_stock_record_url = stationUrl + "/stock/refund_stock_sku";

	// 商户退货放弃取货记录接口
	public static final String abandon_goods_record_url = stationUrl + "/stock/abandon_goods/log/list";

	// 成品出库记录接口
	public static final String out_stock_record_url = stationUrl + "/stock/out_stock_sku";

	// 成品退货记录接口
	public static final String return_stock_record_url = stationUrl + "/stock/return_supply_sku";

	// 成品报溢记录接口
	public static final String increase_stock_record_url = stationUrl + "/stock/increase";

	// 成品报损记录接口
	public static final String loss_stock_record_url = stationUrl + "/stock/loss";

	// 分割入库接口
	public static final String split_in_stock_record_url = stationUrl + "/stock/split/in_stock/list";

	// 分割出库接口
	public static final String split_out_stock_record_url = stationUrl + "/stock/split/out_stock/list";

	/**** 出入库汇总接口 ****/
	// 入库汇总按分类统计接口
	public static final String in_stock_summary_by_category_list_url = stationUrl
			+ "/stock/in_stock_summary_by_category/list";

	// 入库汇总按SPU统计接口
	public static final String in_stock_summary_by_spu_list_url = stationUrl + "/stock/in_stock_summary_by_spu/list";

	// 入库汇总按SPU统计总计接口
	public static final String in_stock_summary_by_spu_url = stationUrl + "/stock/in_stock_summary_by_spu/get";

	// 入库汇总按分类统计总计接口
	public static final String in_stock_summary_by_category_url = stationUrl
			+ "/stock/in_stock_summary_by_category/get";

	// 出库汇总按分类统计接口
	public static final String out_stock_summary_by_category_lsit_url = stationUrl
			+ "/stock/out_stock_summary_by_category/list";

	// 出库汇总按SPU统计接口
	public static final String out_stock_summary_by_spu_list_url = stationUrl + "/stock/out_stock_summary_by_spu/list";

	// 出库汇总按SPU统计总计接口
	public static final String out_stock_summary_by_spu_url = stationUrl + "/stock/out_stock_summary_by_spu/get";

	// 出库汇总按分类统计总计接口
	public static final String out_stock_summary_by_category_url = stationUrl
			+ "/stock/out_stock_summary_by_category/get";

	// 导出出库按SPU统计接口
	public static final String export_out_stock_summary_by_spu_url = stationUrl
			+ "/stock/out_stock_summary_by_spu/export";

	// 导出出库按分类统计接口
	public static final String export_out_stock_summary_by_category_url = stationUrl
			+ "/stock/out_stock_summary_by_category/export";

	// 导出入库按SPU统计接口
	public static final String export_in_stock_summary_by_spu_url = stationUrl
			+ "/stock/in_stock_summary_by_spu/export";

	// 导出入库按分类统计接口
	public static final String export_in_stock_summary_by_category_url = stationUrl
			+ "/stock/in_stock_summary_by_category/export";

	/**** 货位管理相关接口 ****/
	// 新增货位接口
	public static final String add_shelf_location_url = stationUrl + "/stock/shelf_location/add";

	// 删除货位接口
	public static final String del_shelf_location_url = stationUrl + "/stock/shelf_location/delete";

	// 修改货位名称接口
	public static final String edit_shelf_location_url = stationUrl + "/stock/shelf_location/edit";

	// 获取货位信息接口
	public static final String get_shelf_url = stationUrl + "/stock/shelf/get";

	// 指定货位SPU库存总计接口
	public static final String shelf_spu_summary_url = stationUrl + "/stock/shelf/spu/summary";

	// 获取指定货位上的SPU相关信息接口
	public static final String shelf_spu_list_url = stationUrl + "/stock/shelf/spu/list";

	// 获取指定货位上的负库存的SPU相关信息接口
	public static final String shelf_spu_negative_list_url = stationUrl + "/stock/shelf/spu/list";

	// 获取指定SPU在指定货位上的批次库存信息接口
	public static final String shelf_stock_batch_list_url = stationUrl + "/stock/check/batch_number/list";

	// 指定SPU在各个货位上的库存情况接口
	public static final String shelf_spu_stock_list_url = stationUrl + "/stock/shelf/list";

	/** 应付总账与应付明细账相关接口 **/

	// 应付总账统计接口
	public static final String stock_settlement_collect_url = stationUrl + "/stock/report/settlement/collect";

	// 应付总账条目信息
	public static final String stock_settlement_list_url = stationUrl + "/stock/report/settlement/list";

	/**** 自提点相关接口 ****/

	/**********************************************************************************/
	/****************************** 以下是先进先出站点的接口 *****************************/
	/**********************************************************************************/

	/**** 成品出库相关接口 ****/
	public static final String get_batch_stock_url = stationUrl + "/stock/get_batch_out";

	/**** 成品退货相关接口 *****/
	// 成品退货搜索商品退货批次接口
	public static final String get_batch_return_url = stationUrl + "/stock/get_batch_return";

	/**** 批次盘点相关接口 *****/
	// 获取指定SPU的批次列表接口
	public static final String search_stock_batch_url = stationUrl + "/stock/check/batch_number";

	// 先进先出站点批次盘点接口
	public static final String edit_batch_stock_url = stationUrl + "/stock/check/batch_edit";

	// 先进先出站点获取指定批次库存历史记录接口
	public static final String get_batch_log_url = stationUrl + "/stock/check/batch_log";

	/**** 调拨管理相关接口 仓内移库和移库记录 ****/

	// 获取货位管理信息接口
	public static final String get_shelf_tree = stationUrl + "/stock/shelf/tree";

	// 根据Spu名称搜索spu信息
	public static final String simple_search_spu_url = stationUrl + "/merchandise/spu/simple_search";

	// 仓内移库搜索批次接口
	public static final String search_stock_batch_number_url = stationUrl + "/station/stock/check/batch_number/list";

	// 创建仓内移库
	public static final String create_stock_inner_transfer = stationUrl + "/stock/inner_transfer_sheet/create";

	// 搜索仓内移库列表
	public static final String search_stock_inner_transfer_sheet_url = stationUrl + "/stock/inner_transfer_sheet/list";

	// 根据移库单单号查看详情
	public static final String get_stock_inner_transfer_detail = stationUrl + "/stock/inner_transfer_sheet/detail";

	// 移库记录搜索接口
	public static final String get_stock_inner_transfer_log_url = stationUrl + "/stock/inner_transfer_sheet/log/list";

	// 编辑,修改,确认移库单状态接口
	public static final String update_stock_inner_transfer_url = stationUrl + "/stock/inner_transfer_sheet/update";

	// 导出仓内移库列表
	public static final String export_stock_inner_transfer_url = stationUrl + "/stock/inner_transfer_sheet/export";

	// 移库日志导出接口
	public static final String export_stock_inner_transfer_log_url = stationUrl
			+ "/stock/inner_transfer_sheet/log/export";

	/**** 商户货值相关接口 ****/
	// 商户货值总计接口
	public static final String customer_stock_value_count_url = stationUrl + "/stock/address/stock_val/count";

	// 商户货值指定商户商品列表
	public static final String customer_spu_stock_list_url = stationUrl + "/stock/address/spu_stock/list";

	// 商户货值页面搜索查询接口
	public static final String customer_stock_value_list_url = stationUrl + "/stock/address/stock_val/list";

	// 商户货值库存变动记录接口
	public static final String customer_spu_stock_log_list_url = stationUrl + "/stock/address/spu_stock/log/list";

	/******** 分割相关接口 **********/

	// 分割方案查询接口
	public static final String split_plan_list_url = stationUrl + "/stock/split/plan/list";

	// 分割方案新建接口
	public static final String split_plan_create_url = stationUrl + "/stock/split/plan/create";

	// 分割方案详情接口
	public static final String split_plan_detail_url = stationUrl + "/stock/split/plan/detail";

	// 分割方案修改接口
	public static final String split_plan_update_url = stationUrl + "/stock/split/plan/update";

	// 分割方案删除接口
	public static final String split_plan_delete_url = stationUrl + "/stock/split/plan/delete";

	// 分割单据搜索接口
	public static final String split_sheet_list_url = stationUrl + "/stock/split/sheet/list";

	// 分割单据新建接口
	public static final String split_sheet_create_url = stationUrl + "/stock/split/sheet/create";

	// 分割单据修改接口
	public static final String split_sheet_update_url = stationUrl + "/stock/split/sheet/update";

	// 分割单据详细接口
	public static final String split_sheet_detail_url = stationUrl + "/stock/split/sheet/detail";

	// 分割单据删除接口
	public static final String split_sheet_delete_url = stationUrl + "/stock/split/sheet/delete";

	// 损耗统计总计接口
	public static final String split_loss_count_url = stationUrl + "/stock/split/loss/count";

	// 损耗统计列表接口
	public static final String split_loss_list_url = stationUrl + "/stock/split/loss/list";

	/**********************************************************************************/
	/********************************** 以下是共有的接口 ********************************/
	/**********************************************************************************/

	// 获取进销存供应商列表
	public static final String stock_settle_supplier_url = stationUrl + "/stock/settle_supplier/get";

	// 获取成本表接口
	public static final String stock_cost_report_url = stationUrl + "/station/report/value";

}
