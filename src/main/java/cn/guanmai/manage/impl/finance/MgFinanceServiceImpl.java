package cn.guanmai.manage.impl.finance;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.manage.bean.custommange.param.CustomerBillFilterParam;
import cn.guanmai.manage.bean.custommange.result.CustomerBillDetailBean;
import cn.guanmai.manage.bean.custommange.result.CustomerBillListBean;
import cn.guanmai.manage.bean.finance.param.FinanceOrderArrivalParamBean;
import cn.guanmai.manage.bean.finance.param.FinanceOrderParamBean;
import cn.guanmai.manage.bean.finance.param.StrikeBalanceParamBean;
import cn.guanmai.manage.bean.finance.result.FinanceOrderBean;
import cn.guanmai.manage.interfaces.finance.MgFinanceService;
import cn.guanmai.manage.url.CustommanageURL;
import cn.guanmai.manage.url.FinanceURL;
import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.util.JsonUtil;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Jan 16, 2019 11:00:57 AM 
* @des 财务相关业务实现类
* @version 1.0 
*/
public class MgFinanceServiceImpl implements MgFinanceService {
	private BaseRequest baseRequest;

	public MgFinanceServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public boolean rechargeMoney(String kid, BigDecimal money) throws Exception {
		String url = FinanceURL.finance_money_recharge_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("uid", kid);
		paramMap.put("money", String.valueOf(money));
		paramMap.put("comment", StringUtil.getRandomString(6));
		paramMap.put("dealcode", "AT" + TimeUtil.getLongTime());

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean addStrikeBalance(StrikeBalanceParamBean paramBean) throws Exception {
		String urlStr = FinanceURL.strike_balance_add;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramBean);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<FinanceOrderBean> searchFinanceOrder(FinanceOrderParamBean paramBean) throws Exception {
		String urlStr = FinanceURL.finance_order_search;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramBean);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassList(
				retObj.getJSONObject("data").getJSONArray("orders").toString(), FinanceOrderBean.class) : null;
	}

	@Override
	public boolean exportFinanceOrder(FinanceOrderParamBean paramBean) throws Exception {
		String urlStr = FinanceURL.finance_order_export;

		String file_name = baseRequest.baseExport(urlStr, RequestType.GET, paramBean, "temp.xlsx");

		return file_name != null;
	}

	@Override
	public boolean addFinanceOrderArrival(FinanceOrderArrivalParamBean paramBean) throws Exception {
		String urlStr = FinanceURL.finance_order_arrival;

		JSONObject retObj = baseRequest.baseRawRequest(urlStr, paramBean);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<CustomerBillListBean> searchCustomerBill(CustomerBillFilterParam paramBean) throws Exception {
		String urlStr = CustommanageURL.customer_bill_search;
		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramBean);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), CustomerBillListBean.class)
				: null;
	}

	@Override
	public CustomerBillDetailBean customerBillDetail(String date_from, String date_to, String sid, Integer search_type)
			throws Exception {
		String urlStr = CustommanageURL.customer_bill_detail;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("date_from", date_from);
		paramMap.put("date_to", date_to);
		paramMap.put("id", sid);
		paramMap.put("search_type", String.valueOf(search_type));

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), CustomerBillDetailBean.class)
				: null;
	}

	@Override
	public boolean customerReport(String beginTime, String endTime, BigDecimal saleEmployee) throws Exception {
		String urlStr = CustommanageURL.customer_report;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("beginTime", beginTime);
		paramMap.put("endTime", endTime);
		paramMap.put("saleEmployee", String.valueOf(saleEmployee));
		paramMap.put("type", "0");

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean customerReport(String beginTime, String endTime, String station_id) throws Exception {
		String urlStr = CustommanageURL.customer_report;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("beginTime", beginTime);
		paramMap.put("endTime", endTime);
		paramMap.put("station", station_id);
		paramMap.put("type", "1");

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0;

	}

	@Override
	public boolean customerReportDetail(String begin_time, String end_time, BigDecimal saleEmployee_id)
			throws Exception {
		String urlStr = CustommanageURL.customer_report_detail;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("bt", begin_time);
		paramMap.put("et", end_time);
		paramMap.put("saleEmployee", String.valueOf(saleEmployee_id));
		paramMap.put("kind", "0");
		paramMap.put("type", "0");

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean customerReportDetail(String begin_time, String end_time, String station_id) throws Exception {
		String urlStr = CustommanageURL.customer_report_detail;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("bt", begin_time);
		paramMap.put("et", end_time);
		paramMap.put("id", station_id);
		paramMap.put("kind", "1");
		paramMap.put("type", "0");

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateOrderLockStatus(List<String> order_ids, int status) throws Exception {
		String urlStr = FinanceURL.FINANCE_ORDER_LOCK_URL;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_ids", JSONArray.toJSONString(order_ids));
		paramMap.put("freeze", String.valueOf(status));

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean refundOrder(String order_id, int type) throws Exception {
		String urlStr = FinanceURL.FINANCE_ORDER_REFUND_URL;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);
		paramMap.put("type", String.valueOf(type));

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

}
