package cn.guanmai.open.url;

import cn.guanmai.util.ConfigureUtil;

/* 
* @author liming 
* @date Jun 6, 2019 10:55:46 AM 
* @todo TODO
* @version 1.0 
*/
public class FinanceURL {
	private static final String openUrl = ConfigureUtil.getValueByKey("openUrl");

	// 冲账接口
	public static final String finance_strike_url = openUrl + "/finance/strike/1.0";

	// 退款接口
	public static final String finance_order_refund_url = openUrl + "/finance/order/refund/1.0";

	// 查询冲账流水
	public static final String finance_strike_flow_list_url = openUrl + "/finance/strike_flow/list/1.0";
	
	public static final String finance_cash_flow_list = openUrl + "/finance/cash_flow/list/1.0";

}
