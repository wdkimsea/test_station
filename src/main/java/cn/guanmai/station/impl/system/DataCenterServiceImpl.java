package cn.guanmai.station.impl.system;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.interfaces.system.DataCenterService;
import cn.guanmai.station.url.SystemURL;

/* 
* @author liming 
* @date Jan 8, 2019 5:43:48 PM 
* @des 首页数据接口
* @version 1.0 
*/
public class DataCenterServiceImpl implements DataCenterService {
	private BaseRequest baseRequest;

	public DataCenterServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public boolean dailyProfit(String start_date, String end_date, int query_type) throws Exception {
		String urlStr = SystemURL.daily_profit_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("start_date", start_date);
		paramMap.put("end_date", end_date);
		paramMap.put("query_type", String.valueOf(query_type));

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public Integer unReadMessageCount() throws Exception {
		String urlStr = SystemURL.message_unread_count_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getInteger("unread_count") : null;
	}

}
