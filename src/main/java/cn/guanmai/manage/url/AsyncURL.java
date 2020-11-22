package cn.guanmai.manage.url;

import cn.guanmai.util.ConfigureUtil;

/**
 * @author liming
 * @date 2019年8月20日
 * @time 上午10:00:36
 * @des TODO
 */

public class AsyncURL {
	private static final String manage_url = ConfigureUtil.getValueByKey("manageUrl");

	// 异步任务列表接口
	public static final String async_task_list = manage_url + "/task/list";

}
