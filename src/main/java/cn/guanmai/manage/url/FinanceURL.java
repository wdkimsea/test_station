package cn.guanmai.manage.url;

import cn.guanmai.util.ConfigureUtil;

/* 
* @author liming 
* @date Jan 16, 2019 11:01:26 AM 
* @des 财务相关Web接口
* @version 1.0 
*/
public class FinanceURL {
	private static final String manage_url = ConfigureUtil.getValueByKey("manageUrl");

	// 为商户账号进行充值
	public static final String finance_money_recharge_url = manage_url + "/finance/money/recharge";

	// 添加到账凭证接口
	public static final String strike_balance_add = manage_url + "/finance/strike/balance/add";

	// 商户结算搜索过滤
	public static final String finance_order_search = manage_url + "/finance/order/search";

	// 商户结算单导出
	public static final String finance_order_export = manage_url + "/finance/order/export";

	public static final String finance_order_arrival = manage_url + "/finance/order/update/arrival";

	// 订单锁定\解锁接口
	public static final String FINANCE_ORDER_LOCK_URL = manage_url + "/finance/order/update/lock";

	// 退款接口
	public static final String FINANCE_ORDER_REFUND_URL = manage_url + "/finance/order/refund";

}
