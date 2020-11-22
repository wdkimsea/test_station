package cn.guanmai.station.url;

import cn.guanmai.util.ConfigureUtil;

/* 
* @author liming 
* @date Feb 15, 2019 5:01:26 PM 
* @des 报价单相关接口
* @version 1.0 
*/
public class SalemenuURL {
	private static final String stationUrl = ConfigureUtil.getValueByKey("stationUrl");

	// 获取报价单详细信息的接口
	public static final String salemenu_info_url = stationUrl + "/salemenu/sale/detail";

	// 搜索过滤报价单接口
	public static final String search_salemenu_url = stationUrl + "/salemenu/sale/list";

	// 新建报价单接口
	public static final String create_salemenu_url = stationUrl + "/salemenu/sale/create";

	// 修改报价单接口
	public static final String update_salemenu_url = stationUrl + "/salemenu/sale/update";

	// 删除报价单接口
	public static final String delete_salemenu_url = stationUrl + "/salemenu/sale/delete";

	// 创建分享报价单接口
	public static final String create_share_salemenu_url = stationUrl + "/station/salemenu/share/create";

	// 获取分享报价单信息接口
	public static final String get_share_salemenu_url = stationUrl + "/station/salemenu/share/get";

	// 获取报价单里的销售SKU接口
	public static final String get_salemenu_skus_url = stationUrl + "/product/sku_salemenu/list";

	// 显示锁价页面,获取所有报价单的接口
	public static final String get_all_salemenu_url = stationUrl + "/station/salemenu/";

	// 批量新建销售SKU,选择报价单时调用的接口
	public static final String get_salemenu_list_url = stationUrl + "/salemenu/list";
}
