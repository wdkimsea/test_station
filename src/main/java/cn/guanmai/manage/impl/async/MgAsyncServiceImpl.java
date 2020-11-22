package cn.guanmai.manage.impl.async;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.manage.bean.async.MgAsyncTaskBean;
import cn.guanmai.manage.interfaces.async.MgAsyncService;
import cn.guanmai.manage.url.AsyncURL;
import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.util.JsonUtil;
import cn.guanmai.util.ReporterCSS;

/**
 * @author liming
 * @date 2019年8月20日
 * @time 上午9:59:35
 * @des TODO
 */

public class MgAsyncServiceImpl implements MgAsyncService {
	private Logger logger = LoggerFactory.getLogger(MgAsyncServiceImpl.class);

	private BaseRequest baseRequest;

	public MgAsyncServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public boolean getAsyncTaskResult(BigDecimal task_id, String msg) throws Exception {
		String url = AsyncURL.async_task_list;

		JSONObject retObj = null;
		List<MgAsyncTaskBean> asyncTasks = null;
		MgAsyncTaskBean asyncTask = null;
		int num = 0;
		boolean result = true;
		while (num < 20) {
			retObj = baseRequest.baseRequest(url, RequestType.GET);
			if (retObj.getInteger("code") == 0) {
				String tasks = retObj.getJSONObject("data").getJSONArray("tasks").toString().replace("\"result\":\"\"",
						"\"result\":{}");
				asyncTasks = JsonUtil.strToClassList(tasks, MgAsyncTaskBean.class);
				asyncTask = asyncTasks.stream().filter(a -> a.getTask_id().compareTo(task_id) == 0).findAny()
						.orElse(null);
				if (asyncTask == null) {
					result = false;
					break;
				} else {
					ReporterCSS.title("此异步任务执行情况: " + JsonUtil.objectToStr(asyncTask));
					if (asyncTask.getProgress() == 100) {
						if (asyncTask.getResult() != null) {
							String result_msg = asyncTask.getResult().getMsg();
							if (result_msg.contains("请联系客服解决")) {
								throw new Exception("异步任务系统出错了!!!");
							} else if (result_msg.contains("系统繁忙, 请重试")) {
								throw new Exception("异步任务系统出错了!!!");
							} else {
								result = result_msg.contains(msg == null ? "失败(0" : msg);
							}
						}
						break;
					}
				}
			}
			num += 1;
			Thread.sleep(2000);
		}
		return result;
	}

	@Override
	public MgAsyncTaskBean getAsyncTask(BigDecimal task_id) throws Exception {
		String url = AsyncURL.async_task_list;

		JSONObject retObj = null;
		List<MgAsyncTaskBean> asyncTasks = null;
		MgAsyncTaskBean asyncTask = null;

		retObj = baseRequest.baseRequest(url, RequestType.GET);
		if (retObj.getInteger("code") == 0) {
			String tasks = retObj.getJSONObject("data").getJSONArray("tasks").toString().replace("\"result\":\"\"",
					"\"result\":{}");
			asyncTasks = JsonUtil.strToClassList(tasks, MgAsyncTaskBean.class);
			asyncTask = asyncTasks.stream().filter(a -> a.getTask_id().compareTo(task_id) == 0).findAny().orElse(null);
			if (asyncTask == null) {
				throw new Exception("task_id为" + task_id + "异步任务在异步任务列表里没有找到");
			}
			logger.info("异步信息: " + JsonUtil.objectToStr(asyncTask));
		}
		return asyncTask;
	}

}
