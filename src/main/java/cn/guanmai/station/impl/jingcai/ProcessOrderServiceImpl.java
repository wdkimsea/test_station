package cn.guanmai.station.impl.jingcai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.jingcai.ProcessOrderBean;
import cn.guanmai.station.bean.jingcai.param.ProcessOrderFilterParam;
import cn.guanmai.station.interfaces.jingcai.ProcessOrderService;
import cn.guanmai.station.url.JingcaiURL;
import cn.guanmai.util.JsonUtil;

/**
 * @author: liming
 * @Date: 2020年5月18日 下午2:42:07
 * @description:
 * @version: 1.0
 */

public class ProcessOrderServiceImpl implements ProcessOrderService {
	private BaseRequest baseRequest;

	public ProcessOrderServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<ProcessOrderBean> searchProcessOrder(ProcessOrderFilterParam processOrderFilterParam) throws Exception {
		String url = JingcaiURL.process_order_search_url;

		List<ProcessOrderBean> processOrders = new ArrayList<ProcessOrderBean>();
		List<ProcessOrderBean> tempProcessOrders = null;
		JSONObject retObj = null;
		boolean more = true;
		while (more) {
			retObj = baseRequest.baseRequest(url, RequestType.GET, processOrderFilterParam);
			if (retObj.getInteger("code") == 0) {
				tempProcessOrders = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(),
						ProcessOrderBean.class);
				processOrders.addAll(tempProcessOrders);

				JSONObject pagination = retObj.getJSONObject("pagination");
				more = pagination.getBoolean("more");
				processOrderFilterParam.setPage_obj(pagination.getString("page_obj"));
			} else {
				more = false;
				processOrders = null;
			}
		}
		return processOrders;
	}

}
