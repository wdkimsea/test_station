package cn.guanmai.open.url;

import cn.guanmai.util.ConfigureUtil;

/* 
* @author liming 
* @date Jun 6, 2019 10:45:45 AM 
* @todo TODO
* @version 1.0 
*/
public class AreaURL {
	private static final String openUrl = ConfigureUtil.getValueByKey("openUrl");
	
	// 
	public static final String area_list_url = openUrl + "/area/list";

}
