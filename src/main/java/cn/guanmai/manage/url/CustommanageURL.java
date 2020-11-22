package cn.guanmai.manage.url;

import cn.guanmai.util.ConfigureUtil;

/**
 * @program: test_station
 * @description: 商户相关接口
 * @author: weird
 * @create: 2019-01-09 17:18
 **/
public class CustommanageURL {
	private static final String manage_url = ConfigureUtil.getValueByKey("manageUrl");

	// 商户基础信息
	public static final String customer_base_info = manage_url + "/custommanage/";

	// 商户信息编辑和获取，有get和post方法
	public static final String customer_edit = manage_url + "/custommanage/edit";

	// 商户添加
	public static final String customer_add = manage_url + "/custommanage/restaurant/add";

	// 商户查询列表
	public static final String customer_search = manage_url + "/custommanage/list";

	// 商户详细信息接口
	public static final String customer_detail_info = manage_url + "/custommanage/get";

	// 商户批量修改,模板文件导出
	public static final String export_eidt_customer_template_url = manage_url + "/custommanage/edit_customer/export";

	// 商户批量修改,模板文件导入
	public static final String customer_edit_import = manage_url + "/custommanage/edit_customer/import";

	// 商户批量添加
	public static final String customer_add_import = manage_url + "/custommanage/restaurant/import";

	// 商户删除
	public static final String customer_delete = manage_url + "/custommanage/restaurant/delete";

	// group 员工账号信息
	public static final String customer_employee_info = manage_url + "/custommanage/saleemployee/simple_info";

	// 商户标签
	public static final String customer_label = manage_url + "/custommanage/address_label/list";

	// 商户对账单查询接口
	public static final String customer_bill_search = manage_url + "/custommanage/bill/search";

	// 商户对账单详细信息接口
	public static final String customer_bill_detail = manage_url + "/custommanage/bill/detail";

	// 商户报表接口
	public static final String customer_report = manage_url + "/custommanage/report";

	// 商户报表详细接口
	public static final String customer_report_detail = manage_url + "/custommanage/report/detail";

	/**********************************************************************/
	/**************************** 商户报表相关接口 ********************************/
	/**********************************************************************/

	// 销售报表列表接口
	public static final String sales_report_search_url = manage_url + "/custommanage/sales_report/search";

	// 销售报表详情接口
	public static final String sales_report_detail_url = manage_url + "/custommanage/sales_report/detail";

	public static final String sales_report_baseinfo_url = manage_url + "/custommanage/report";

	public static final String sales_report_staion_detail_url = manage_url + "/custommanage/report/detail";

}
