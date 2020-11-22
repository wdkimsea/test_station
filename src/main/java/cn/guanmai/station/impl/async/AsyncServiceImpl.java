package cn.guanmai.station.impl.async;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.async.AsyncTaskResultBean;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.url.SystemURL;
import cn.guanmai.util.JsonUtil;
import cn.guanmai.util.ReporterCSS;

/* 
* @author liming 
* @date Jan 2, 2019 4:39:50 PM 
* @todo TODO
* @version 1.0 
*/
public class AsyncServiceImpl implements AsyncService {
	private BaseRequest baseRequest;

	public AsyncServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public AsyncTaskResultBean getAsyncTaskResult(BigDecimal task_id) throws Exception {
		AsyncTaskResultBean asyncTaskResult = null;
		String urlStr = SystemURL.task_list_url;
		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET);
		if (retObj.getInteger("code") == 0) {
			String tasks = retObj.getJSONObject("data").getJSONArray("tasks").toString().replace("\"result\":\"\"",
					"\"result\":{}");
			List<AsyncTaskResultBean> asyncTaskResulList = JsonUtil.strToClassList(tasks, AsyncTaskResultBean.class);
			asyncTaskResult = asyncTaskResulList.stream().filter(a -> a.getTask_id().compareTo(task_id) == 0).findAny()
					.orElse(null);
			if (asyncTaskResult != null) {
				ReporterCSS.title("此异步任务执行情况: " + JsonUtil.objectToStr(asyncTaskResult));
			}
		}
		return asyncTaskResult;
	}

	@Override
	public List<AsyncTaskResultBean> getAsyncTaskResultList() throws Exception {
		List<AsyncTaskResultBean> asyncTaskResulList = null;
		String urlStr = SystemURL.task_list_url;
		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET);
		if (retObj.getInteger("code") == 0) {
			String tasks = retObj.getJSONObject("data").getJSONArray("tasks").toString().replace("\"result\":\"\"",
					"\"result\":{}");
			asyncTaskResulList = JsonUtil.strToClassList(tasks, AsyncTaskResultBean.class);
		}
		return asyncTaskResulList;
	}

	@Override
	public boolean getAsyncTaskResult(BigDecimal task_id, String msg) throws Exception {
		Thread.sleep(1000);
		boolean result = false;
		String urlStr = SystemURL.task_list_url;
		JSONObject retObj = null;
		List<AsyncTaskResultBean> asyncTaskResultList = null;
		AsyncTaskResultBean asyncTaskResult = null;
		int num = 0;
		while (num < 20) {
			retObj = baseRequest.baseRequest(urlStr, RequestType.GET);
			if (retObj.getInteger("code") == 0) {
				String tasks = retObj.getJSONObject("data").getJSONArray("tasks").toString().replace("\"result\":\"\"",
						"\"result\":{}");
				asyncTaskResultList = JsonUtil.strToClassList(tasks, AsyncTaskResultBean.class);
				asyncTaskResult = asyncTaskResultList.stream().filter(a -> a.getTask_id().compareTo(task_id) == 0)
						.findAny().orElse(null);
				if (asyncTaskResult == null) {
					throw new Exception("task_id为" + task_id + "异步任务在异步任务列表里没有找到");
				} else {
					ReporterCSS.title("此异步任务执行情况: " + JsonUtil.objectToStr(asyncTaskResult));
					if (asyncTaskResult.getProgress() == 100) {
						if (asyncTaskResult.getResult() != null) {
							String result_msg = asyncTaskResult.getResult().getMsg();
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
	public boolean getAsyncTaskResult(String user_task_id, String msg) throws Exception {
		Thread.sleep(1000);
		boolean result = false;
		String urlStr = SystemURL.task_list_url;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("task_id", String.valueOf(user_task_id));
		JSONObject retObj = null;
		List<AsyncTaskResultBean> asyncTaskResultList = null;
		AsyncTaskResultBean asyncTaskResult = null;
		int num = 0;
		while (num < 20) {
			retObj = baseRequest.baseRequest(urlStr, RequestType.GET);
			if (retObj.getInteger("code") == 0) {
				String tasks = retObj.getJSONObject("data").getJSONArray("tasks").toString().replace("\"result\":\"\"",
						"\"result\":{}");
				asyncTaskResultList = JsonUtil.strToClassList(tasks, AsyncTaskResultBean.class);
				asyncTaskResult = asyncTaskResultList.stream().filter(a -> a.getUser_task_id().equals(user_task_id))
						.findAny().orElse(null);
				if (asyncTaskResult == null) {
					throw new Exception("task_id为" + user_task_id + "异步任务在异步任务列表里没有找到");
				} else {
					ReporterCSS.title("此异步任务执行情况: " + JsonUtil.objectToStr(asyncTaskResult));
					if (asyncTaskResult.getProgress() == 100) {
						if (asyncTaskResult.getResult() != null) {
							String result_msg = asyncTaskResult.getResult().getMsg();
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
}
