package cn.guanmai.station.impl.jingcai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.jingcai.ProcessTaskBean;
import cn.guanmai.station.bean.jingcai.param.ProcessTaskFilterParam;
import cn.guanmai.station.interfaces.jingcai.ProcessTaskService;
import cn.guanmai.station.url.JingcaiURL;
import cn.guanmai.util.JsonUtil;

/**
 * @author: liming
 * @Date: 2020年5月12日 下午7:37:41
 * @description:
 * @version: 1.0
 */

public class ProcessTaskServiceImpl implements ProcessTaskService {
	private BaseRequest baseRequest;

	public ProcessTaskServiceImpl(Map<String, String> cookie) {
		baseRequest = new BaseRequestImpl(cookie);
	}

	@Override
	public List<ProcessTaskBean> searchProcessTask(ProcessTaskFilterParam processTaskFilterParam) throws Exception {
		String url = JingcaiURL.process_task_search_url;

		JSONObject retObj = null;

		List<ProcessTaskBean> processTasks = new ArrayList<ProcessTaskBean>();
		List<ProcessTaskBean> tempProcessTasks = null;
		while (true) {
			retObj = baseRequest.baseRequest(url, RequestType.GET, processTaskFilterParam);
			if (retObj.getInteger("code") == 0) {
				tempProcessTasks = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(),
						ProcessTaskBean.class);
				if (tempProcessTasks.size() > 0) {
					processTasks.addAll(tempProcessTasks);
					boolean more = retObj.getJSONObject("pagination").getBoolean("more");
					if (more) {
						String page_obj = retObj.getJSONObject("pagination").getString("page_obj");
						processTaskFilterParam.setPage_obj(page_obj);
						int offset = processTaskFilterParam.getOffset();
						processTaskFilterParam.setOffset(offset + processTaskFilterParam.getLimit());
					} else {
						break;
					}
				}
			} else {
				processTasks = null;
				break;
			}
		}
		return processTasks;
	}

	@Override
	public List<ProcessTaskBean> waitingProcessTask(ProcessTaskFilterParam processTaskFilterParam) throws Exception {
		String url = JingcaiURL.process_task_search_url;

		JSONObject retObj = null;
		int loop_times = 20;
		List<ProcessTaskBean> processTasks = new ArrayList<ProcessTaskBean>();
		List<ProcessTaskBean> tempProcessTasks = null;
		while (loop_times > 0) {
			retObj = baseRequest.baseRequest(url, RequestType.GET, processTaskFilterParam);
			if (retObj.getInteger("code") == 0) {
				tempProcessTasks = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(),
						ProcessTaskBean.class);
				if (tempProcessTasks.size() > 0) {
					processTasks.addAll(tempProcessTasks);
					boolean more = retObj.getJSONObject("pagination").getBoolean("more");
					if (more) {
						String page_obj = retObj.getJSONObject("pagination").getString("page_obj");
						processTaskFilterParam.setPage_obj(page_obj);
						int offset = processTaskFilterParam.getOffset();
						processTaskFilterParam.setOffset(offset + processTaskFilterParam.getLimit());
					} else {
						break;
					}
				}
			} else {
				processTasks = null;
				break;
			}
			Thread.sleep(3000);
			loop_times--;
		}
		return processTasks;
	}

	@Override
	public boolean releaseProcessTask(ProcessTaskFilterParam processTaskFilterParam) throws Exception {
		String url = JingcaiURL.process_task_release_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, processTaskFilterParam);

		return retObj.getInteger("code") == 0;
	}

}
