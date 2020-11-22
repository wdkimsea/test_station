package cn.guanmai.manage.impl.ordermanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.manage.bean.ordermanage.param.DailyOrderParamBean;
import cn.guanmai.manage.bean.ordermanage.param.OrderExceptionParamBean;
import cn.guanmai.manage.bean.ordermanage.result.DailyOrderBean;
import cn.guanmai.manage.bean.ordermanage.result.OrderDetailInfoBean;
import cn.guanmai.manage.interfaces.ordermanange.OrderManangeService;
import cn.guanmai.manage.url.OrderManageURL;
import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Jan 18, 2019 6:55:33 PM 
* @des 订单管理相关接口实现类
* @version 1.0 
*/
public class OrderManageServiceImpl implements OrderManangeService {
	private BaseRequest baseRequest;

	public OrderManageServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<DailyOrderBean> searchDailyOrder(DailyOrderParamBean paramBean) throws Exception {
		String url = OrderManageURL.ordermanage_daily_search;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramBean);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), DailyOrderBean.class)
				: null;
	}

	@Override
	public List<String> districtCodeList() throws Exception {
		String url = OrderManageURL.ordermanage_daily_info;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("get_district", "");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		if (retObj.getInteger("code") == 0) {
			List<String> districtCodeList = new ArrayList<String>();
			JSONArray district = retObj.getJSONObject("data").getJSONArray("district");
			for (Object obj : district) {
				districtCodeList.add(JSONObject.parseObject(obj.toString()).getString("code"));
			}
			return districtCodeList;
		} else {
			return null;
		}

	}

	@Override
	public boolean exportDailyOrder(DailyOrderParamBean paramBean) throws Exception {
		String url = OrderManageURL.ordermanage_daily_export;

		String file_name = baseRequest.baseExport(url, RequestType.GET, paramBean, "temp.xlsx");

		return file_name != null;
	}

	@Override
	public boolean searchOrder(String order_id) throws Exception {
		String url = OrderManageURL.ordermanage_order_search;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", order_id.replace("PL", ""));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0 ? retObj.getJSONArray("data").size() > 0 : false;
	}

	@Override
	public OrderDetailInfoBean getOrderDetailInfo(String order_id) throws Exception {
		String url = OrderManageURL.ordermanage_order_info;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", order_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassObject(
				retObj.getJSONObject("data").getJSONObject("order_info").toString(), OrderDetailInfoBean.class) : null;

	}

	@Override
	public String getToken(String order_id) throws Exception {
		String url = OrderManageURL.ordermanage_order_info;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", order_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("token") : null;
	}

	@Override
	public boolean addOrderException(OrderExceptionParamBean paramBean) throws Exception {
		String url = OrderManageURL.ordermanage_exception;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("token", paramBean.getToken());
		paramMap.put("id", paramBean.getId());
		paramMap.put("exception", JsonUtil.objectToStr(paramBean.getExceptions()));
		paramMap.put("refund", JsonUtil.objectToStr(paramBean.getRefunds()));
		paramMap.put("order_remark", JsonUtil.objectToStr(paramBean.getRemarks()));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}
}
