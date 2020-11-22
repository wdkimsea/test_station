package cn.guanmai.open.url;

import cn.guanmai.util.ConfigureUtil;

/**
 * @author liming
 * @date 2019年10月21日
 * @time 下午3:46:39
 * @des TODO
 */

public class OpenStockURL {
	private static final String openUrl = ConfigureUtil.getValueByKey("openUrl");

	// 查询供应商接口
	public static String query_supplier_url = openUrl + "/stock/supplier/list/1.0";

	// 获取供应商详情信息接口
	public static String get_supplier_detail_url = openUrl + "/stock/supplier/get/1.0";

	// 新建供应商
	public static String create_supplier_url = openUrl + "/stock/supplier/create/1.0";

	// 修改供应商
	public static String update_supplier_url = openUrl + "/stock/supplier/update/1.0";

	// 采购入库单搜索过滤接口
	public static String query_stock_in_sheet_url = openUrl + "/stock/in_stock_sheet/list/1.0";

	// 获取采购入库单详情接口
	public static String get_stock_in_sheet_detail_url = openUrl + "/stock/in_stock_sheet/get/1.0";

	// 创建采购入库单
	public static String create_stock_in_sheet_url = openUrl + "/stock/in_stock_sheet/create/1.0";

	// 修改采购入库单
	public static String update_stock_in_sheet_url = openUrl + "/stock/in_stock_sheet/update/1.0";

	// 添加采购入库单采购条目
	public static String add_stock_in_sheet_detail_url = openUrl + "/stock/in_stock_sheet/detail/create/1.0";

	// 修改采购入库单采购条目
	public static String update_stock_in_sheet_detail_url = openUrl + "/stock/in_stock_sheet/detail/update/1.0";

	// 删除采购入库单采购条目
	public static String delete_stock_in_sheet_detail_url = openUrl + "/stock/in_stock_sheet/detail/delete/1.0";

	// 提交采购入库单
	public static String submit_stock_in_sheet_url = openUrl + "/stock/in_stock_sheet/submit/1.0";

	// 冲销采购入库单
	public static String revert_stock_in_sheet_url = openUrl + "/stock/in_stock_sheet/revert/1.0";

	// 审核不通过采购入库单
	public static String reject_stock_in_sheet_url = openUrl + "/stock/in_stock_sheet/reject/1.0";

	// 搜索过滤出库单
	public static String query_stock_out_sheet_url = openUrl + "/stock/out_stock_sheet/list/1.0";

	// 获取出库单详情
	public static String get_stock_out_sheet_detail_url = openUrl + "/stock/out_stock_sheet/get/1.0";

	// 新建出库单
	public static String create_stock_out_sheet_url = openUrl + "/stock/out_stock_sheet/create/1.0";

	// 修改出库单
	public static String update_stock_out_sheet_url = openUrl + "/stock/out_stock_sheet/update/1.0";

	// 提交出库单
	public static String submit_stock_out_sheet_url = openUrl + "/stock/out_stock_sheet/submit/1.0";

	// 冲销出库单
	public static String revert_stock_out_sheet_url = openUrl + "/stock/out_stock_sheet/revert/1.0";

	// 搜索欧过滤采购退货单
	public static String query_stock_refund_sheet_url = openUrl + "/stock/supplier_refund_sheet/list/1.0";

	// 获取采购退货单详细信息
	public static String get_stock_refund_sheet_detail_url = openUrl + "/stock/supplier_refund_sheet/get/1.0";

	// 新建采购退货单
	public static String create_stock_refund_sheet_url = openUrl + "/stock/supplier_refund_sheet/create/1.0";

	// 修改采购退货单
	public static String update_stock_refund_sheet_url = openUrl + "/stock/supplier_refund_sheet/update/1.0";

	// 提交采购退货单
	public static String submit_stock_refund_sheet_url = openUrl + "/stock/supplier_refund_sheet/submit/1.0";

	// 冲销采购退货单
	public static String revert_stock_refund_sheet_url = openUrl + "/stock/supplier_refund_sheet/revert/1.0";

	// 采购退货单审核不通过
	public static String reject_stock_refund_sheet_url = openUrl + "/stock/supplier_refund_sheet/reject/1.0";

	// 查询总账列表
	public static String query_stock_ledger_url = openUrl + "/stock/ledger/list/1.0";

	// 查询总账详情
	public static String get_stock_ledger_detail_url = openUrl + "/stock/ledger/get/1.0";

	// 库存盘点列表
	public static String stock_list_url = openUrl + "/stock/list/1.0";

	// 库存盘点
	public static String stock_update_url = openUrl + "/stock/update/1.0";

	// 拉取商户退货列表
	public static String stock_sale_refund_list_url = openUrl + "/stock/sale_refund/list/1.0";

}
