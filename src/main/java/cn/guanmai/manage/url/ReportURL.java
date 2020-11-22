package cn.guanmai.manage.url;

import cn.guanmai.util.ConfigureUtil;

/**
 * @program: station
 * @description: 统计url
 * @author: weird
 * @create: 2019-01-18 11:22
 **/
public class ReportURL {
    private static final String manage_url = ConfigureUtil.getValueByKey("manageUrl");

    //订单分析
	public static final String report_customer_order = manage_url + "/report/customer_order";
    //商品分析
	public static final String report_orders = manage_url + "/report/orders";
}
