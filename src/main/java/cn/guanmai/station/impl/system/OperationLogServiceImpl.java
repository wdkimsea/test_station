package cn.guanmai.station.impl.system;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.system.OperationLogBean;
import cn.guanmai.station.bean.system.OperationLogDetailBean;
import cn.guanmai.station.bean.system.param.OperationLogFilterParam;
import cn.guanmai.station.interfaces.system.OperationLogService;
import cn.guanmai.station.url.SystemURL;
import cn.guanmai.util.JsonUtil;

/**
 * @author: liming
 * @Date: 2020年7月15日 下午3:00:42
 * @description:
 * @version: 1.0
 */

public class OperationLogServiceImpl implements OperationLogService {
	private BaseRequest baseRequest;

	public OperationLogServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<OperationLogBean> searchOperationLog(OperationLogFilterParam operationLogFilterParam) throws Exception {
		String url = SystemURL.operation_log_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, operationLogFilterParam);

		List<OperationLogBean> operationLogs = null;
		if (retObj.getInteger("code") == 0) {
			if (retObj.getJSONObject("data").containsKey("op_data")) {
				operationLogs = JsonUtil.strToClassList(retObj.getJSONObject("data").getJSONArray("op_data").toString(),
						OperationLogBean.class);
			} else {
				operationLogs = new ArrayList<OperationLogBean>();
			}
		}
		return operationLogs;
	}

	@Override
	public OperationLogDetailBean getOperationLogDetail(String id) throws Exception {
		String url = SystemURL.operation_log_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OperationLogDetailBean.class)
				: null;
	}

	@Override
	public BigDecimal exportOperationLog(OperationLogFilterParam operationLogFilterParam) throws Exception {
		String url = SystemURL.operation_log_export_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, operationLogFilterParam);

		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}

}
