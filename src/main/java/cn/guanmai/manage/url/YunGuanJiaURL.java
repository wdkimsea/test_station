package cn.guanmai.manage.url;

import cn.guanmai.util.ConfigureUtil;

/**
 * @program: station
 * @description: 运管家相关接口
 * @author: weird
 * @create: 2019-01-17 14:41
 **/
public class YunGuanJiaURL {
    private static final String manage_url = ConfigureUtil.getValueByKey("manageUrl");

    // 统计最近订单数
	public static final String lastest_orders_count = manage_url + "/applets/data_analyse/lastest_orders_count";
}
