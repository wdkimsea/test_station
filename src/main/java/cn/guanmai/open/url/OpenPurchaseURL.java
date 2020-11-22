package cn.guanmai.open.url;

import cn.guanmai.util.ConfigureUtil;

/**
 * @author liming
 * @date 2019年11月11日
 * @time 下午7:53:40
 * @des TODO
 */

public class OpenPurchaseURL {
	private static final String openUrl = ConfigureUtil.getValueByKey("openUrl");

	// 根据供应商查询采购员接口
	public static final String query_purchaser_by_suppliers_url = openUrl + "/purchase/optional_purchaser/list/1.0";

	// 搜索过滤采购员接口
	public static final String query_purcahser_url = openUrl + "/purchase/purchaser/list/1.0";

	// 查询采购单列表接口
	public static final String query_purchase_sheet_url = openUrl + "/purchase/purchase_sheet/list/1.0";

	// 获取采购单详情接口
	public static final String get_purchase_sheet_detail_url = openUrl + "/purchase/purchase_sheet/get/1.0";

	// 新建采购单据接口
	public static final String create_purchase_sheet_url = openUrl + "/purchase/purchase_sheet/create/1.0";

	// 修改采购单据接口
	public static final String update_purchase_sheet_url = openUrl + "/purchase/purchase_sheet/update/1.0";

	// 提交采购单据
	public static final String submit_purchase_sheet_url = openUrl + "/purchase/purchase_sheet/submit/1.0";

	// 删除采购单据
	public static final String delete_purchase_sheet_url = openUrl + "/purchase/purchase_sheet/delete/1.0";

	// 获取运营时间
	public static final String get_time_config_url = openUrl + "/purchase/service_time/get/1.0";

	// 过滤采购任务
	public static final String query_purchase_task_url = openUrl + "/purchase/purchase_task/list/1.0";

	// 新建采购任务
	public static final String create_purchase_task_url = openUrl + "/purchase/purchase_task/create/1.0";

	// 修改采购任务
	public static final String update_purcahse_task_url = openUrl + "/purchase/purchase_task/update/1.0";

	// 发布采购任务
	public static final String publish_purchase_task_url = openUrl + "/purchase/purchase_task/publish/1.0";

}
