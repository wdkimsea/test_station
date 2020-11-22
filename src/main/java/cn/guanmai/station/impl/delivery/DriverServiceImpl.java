package cn.guanmai.station.impl.delivery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.delivery.DeliveryConfirmInfoBean;
import cn.guanmai.station.bean.delivery.DeliveryOrderDetail;
import cn.guanmai.station.bean.delivery.DeliveryOrderException;
import cn.guanmai.station.bean.delivery.DeliveryTaskBean;
import cn.guanmai.station.bean.delivery.HomePageBean;
import cn.guanmai.station.bean.delivery.param.DeliveryOrderExceptionParam;
import cn.guanmai.station.bean.delivery.param.DriverTracePointParam;
import cn.guanmai.station.interfaces.delivery.DriverService;
import cn.guanmai.station.url.DistributeURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date May 7, 2019 7:16:54 PM 
* @des 司机APP相关接口实现
* @version 1.0 
*/
public class DriverServiceImpl implements DriverService {
	private BaseRequest baseRequest;

	public DriverServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public HomePageBean getHomePageData() throws Exception {
		String url = DistributeURL.get_homepage_data_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), HomePageBean.class)
				: null;
	}

	@Override
	public List<DeliveryTaskBean> getDeliveryTask(boolean delivered) throws Exception {
		String url = DistributeURL.get_delivery_task_url;

		String undelivered = delivered ? "0" : "1";

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("undelivered", undelivered);

		JSONObject retObj = null;

		List<DeliveryTaskBean> deliveryTaskList = new ArrayList<DeliveryTaskBean>();

		List<DeliveryTaskBean> tempDeliveryTaskList = null;
		String pagination = null;
		while (true) {
			retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
			if (retObj.getInteger("code") == 0) {
				tempDeliveryTaskList = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(),
						DeliveryTaskBean.class);
				deliveryTaskList.addAll(tempDeliveryTaskList);
				if (tempDeliveryTaskList.size() < 20) {
					break;
				}
				pagination = tempDeliveryTaskList.get(19).getPagination();
				paramMap.put("pagination", pagination);
			} else {
				deliveryTaskList = null;
				break;
			}
		}
		return deliveryTaskList;

	}

	@Override
	public boolean finishDelivery(String order_id) throws Exception {
		String url = DistributeURL.finish_delivery_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_no", order_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public DeliveryOrderDetail getDeliveryOrderDetail(String order_id, String search_text) throws Exception {
		String url = DistributeURL.get_delivery_order_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_no", order_id);
		if (search_text != null && !search_text.trim().equals("")) {
			paramMap.put("search_text", search_text);
		}

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), DeliveryOrderDetail.class)
				: null;
	}

	@Override
	public DeliveryConfirmInfoBean getDeliveryConfirmInfo(String order_id, String delivery_id) throws Exception {
		String url = DistributeURL.get_delivery_confirm_info_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);
		paramMap.put("delivery_id", delivery_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), DeliveryConfirmInfoBean.class)
				: null;
	}

	@Override
	public boolean productConfirm(String order_id, JSONArray sku_ids, String delivery_id, int status) throws Exception {
		String url = DistributeURL.product_confirm_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);
		paramMap.put("sku_ids", sku_ids.toString());
		paramMap.put("delivery_id", delivery_id);
		paramMap.put("status", String.valueOf(status));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deliveryConfirm(String order_id, String delivery_id) throws Exception {
		String url = DistributeURL.order_delivery_confirm_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);
		paramMap.put("delivery_id", delivery_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean udpdateCustomerAddress(String sid, String lat, String lng, String address) throws Exception {
		String url = DistributeURL.update_customer_address_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("sid", sid);
		paramMap.put("lat", lat);
		paramMap.put("lng", lng);
		paramMap.put("address", address);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean exceptionOptions(String order_id) throws Exception {
		String url = DistributeURL.exception_options_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_no", order_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public DeliveryOrderException getDeliveryOrderException(String order_id) throws Exception {
		String url = DistributeURL.get_delivery_order_exception_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_no", order_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), DeliveryOrderException.class)
				: null;
	}

	@Override
	public boolean checkReturnDeliveryOrder(DeliveryOrderExceptionParam param) throws Exception {
		String url = DistributeURL.check_return_delivery_order_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_no", param.getOrder_id());
		paramMap.put("refund", JsonUtil.objectToStr(param.getRefunds()));
		paramMap.put("exception", JsonUtil.objectToStr(param.getExceptions()));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateDeliveryOrderException(DeliveryOrderExceptionParam param) throws Exception {
		String url = DistributeURL.update_delivery_order_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_no", param.getOrder_id());
		paramMap.put("refund", JsonUtil.objectToStr(param.getRefunds()));
		paramMap.put("exception", JsonUtil.objectToStr(param.getExceptions()));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean startDelivery() throws Exception {
		String url = DistributeURL.start_delivery_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, new HashMap<String, String>());
		return retObj.getInteger("code") == 0;
	}

	@Override
	public Integer getDriverStatus() throws Exception {
		String url = DistributeURL.get_driver_status_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, new HashMap<String, String>());
		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getInteger("delivery_status") : null;
	}

	@Override
	public boolean uploadDriverTracePoint(DriverTracePointParam driverTracePointParam) throws Exception {
		String url = DistributeURL.upload_driver_trace_point_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, driverTracePointParam);

		return retObj.getInteger("code") == 0;
	}

}
