package cn.guanmai.open.impl.finance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.open.bean.finance.CashFlowBean;
import cn.guanmai.open.bean.finance.StrikeFlowBean;
import cn.guanmai.open.bean.finance.param.FinanceStrikeParam;
import cn.guanmai.open.interfaces.finance.OpenFinanceService;
import cn.guanmai.open.url.FinanceURL;
import cn.guanmai.request.impl.OpenRequestImpl;
import cn.guanmai.request.intefaces.OpenRequest;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Jun 6, 2019 10:55:23 AM 
* @todo TODO
* @version 1.0 
*/
public class OpenFinanceServiceImpl implements OpenFinanceService {
	private JSONObject retObj;
	private OpenRequest openRequest;

	public OpenFinanceServiceImpl(String access_token) {
		openRequest = new OpenRequestImpl(access_token);
	}

	@Override
	public boolean strikeFinance(FinanceStrikeParam financeStrikeParam) throws Exception {
		String url = FinanceURL.finance_strike_url;

		retObj = openRequest.baseRequest(url, RequestType.POST, financeStrikeParam);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean refundOrderFinance(String order_id, String type) throws Exception {
		String url = FinanceURL.finance_order_refund_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);
		paramMap.put("type", type);
		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<StrikeFlowBean> searchStrikeFlow(String start_time, String end_time, String customer_id)
			throws Exception {
		String url = FinanceURL.finance_strike_flow_list_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("start_date", start_time);
		paramMap.put("end_date", end_time);
		paramMap.put("customer_id", customer_id);
		retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), StrikeFlowBean.class)
				: null;
	}

	@Override
	public List<CashFlowBean> searchCashFlow(String start_time, String end_time, String customer_id, Integer offset,
			Integer limit) throws Exception {
		String url = FinanceURL.finance_cash_flow_list;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("start_date", start_time);
		paramMap.put("end_date", end_time);
		paramMap.put("customer_id", customer_id);
		paramMap.put("offset", offset.toString());
		paramMap.put("limit", limit.toString());
		retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), CashFlowBean.class)
				: null;
	}

}
