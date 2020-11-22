package cn.guanmai.station.url;

import cn.guanmai.util.ConfigureUtil;

/* 
* @author liming 
* @date Nov 7, 2018 4:18:41 PM 
* @des 供应商相关接口
* @version 1.0 
*/
public class SupplierURL {
	private static final String stationUrl = ConfigureUtil.getValueByKey("stationUrl");

	// 创建供应商接口
	public static final String create_supplier_url = stationUrl + "/supplier/create";

	// 获取供应商详细信息接口
	public static final String get_supplier_info_url = stationUrl + "/supplier/detail";

	// 更新供应商接口
	public static final String update_supplier_url = stationUrl + "/supplier/update";

	// 删除供应商接口
	public static final String delete_supplier_url = stationUrl + "/supplier/delete";

	// 搜索供应商
	public static final String search_supplier_url = stationUrl + "/supplier/search";

	// 获取供应商列表接口
	public static final String get_supplier_list_url = stationUrl + "/stock/settle_supplier/get";

	// 获取供应商账户接口
	public static final String supplier_account_url = stationUrl + "/supplier/account";

	// 获取供应商结款单列表接口
	public static final String get_settle_sheet_list_url = stationUrl + "/stock/settle_sheet/list";

	// 待处理单据加入结款单
	public static final String add_settle_sheet_to_payment_url = stationUrl + "/stock/settle_sheet";

	// 结款单据列表接口
	public static final String search_settle_sheet_url = stationUrl + "/stock/settle_sheet";

	// 获取结款单据详细信息
	public static final String get_settle_sheet_detail_url = stationUrl + "/stock/settle_sheet/details";

	// 提交结款单据接口
	public static final String submit_settle_sheet_url = stationUrl + "/stock/settle_sheet/submit";

	// 标记借款单接口
	public static final String pay_settle_sheet_url = stationUrl + "/stock/settle_sheet/pay";

	// 红冲结款单据
	public static final String delete_settle_sheet_url = stationUrl + "/stock/settle_sheet";

	// 审核不通过结款单据
	public static final String review_settle_sheet_url = stationUrl + "/stock/settle_sheet/submit";

	// 打印结款单据接口
	public static final String print_settle_sheet_url = stationUrl + "/stock/settle_sheet/deal";

	// 导出结款单据接口
	public static final String export_settle_sheet_url = stationUrl + "/stock/settle_sheet/export";

	// 导出待结款单据列表
	public static final String export_pre_payment_list_url = stationUrl + "/stock/settle_sheet/list";

	// 导出结款单列表
	public static final String export_payment_list_url = stationUrl + "/stock/settle_sheet";

	/************************
	 ******** 应付总账&应付明细账***
	 ************************/
	// 应付总账列表接口
	public static final String settlement_report_url = stationUrl + "/stock/report/settlement/list";

	// 应付总账汇总接口
	public static final String settlement_report_collect_url = stationUrl + "/stock/report/settlement/collect";

	// 应付总账导出接口
	public static final String settlement_report_export_url = stationUrl + "/stock/report/settlement/export_list";

	// 应付明细-入库\退货-维度明细
	public static final String settlement_report_detail_url = stationUrl + "/stock/report/settlement/detail";

	// 导出应付明细账接口
	public static final String export_settlement_report_detail_url = stationUrl
			+ "/stock/report/settlement/export_detail";

	// 付款执行明细汇总
	public static final String settlement_pay_collect_url = stationUrl + "/stock/report/verification/collect";

	// 付款执行明细
	public static final String settlement_pay_url = stationUrl + "/stock/report/verification/list";

}
