package cn.guanmai.open.impl.delivery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.open.bean.delivery.OpenCarModelBean;
import cn.guanmai.open.bean.delivery.OpenCarrierBean;
import cn.guanmai.open.bean.delivery.OpenDeliveryTaskBean;
import cn.guanmai.open.bean.delivery.OpenDriverBean;
import cn.guanmai.open.bean.delivery.OpenRouteBean;
import cn.guanmai.open.bean.delivery.OpenRouteDetailBean;
import cn.guanmai.open.bean.delivery.param.DeliveryTaskFilterParam;
import cn.guanmai.open.bean.delivery.param.DrivcerCreateParam;
import cn.guanmai.open.bean.delivery.param.DrivcerUpdateParam;
import cn.guanmai.open.interfaces.delivery.OpenDeliveryService;
import cn.guanmai.open.url.DeliveryURL;
import cn.guanmai.request.impl.OpenRequestImpl;
import cn.guanmai.request.intefaces.OpenRequest;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Jun 5, 2019 2:48:45 PM 
* @des 配送相关业务接口实现
* @version 1.0 
*/
public class OpenDeliveryServiceImpl implements OpenDeliveryService {
	private JSONObject retObj;
	private OpenRequest openRequest;

	public OpenDeliveryServiceImpl(String access_token) {
		openRequest = new OpenRequestImpl(access_token);
	}

	@Override
	public List<OpenDeliveryTaskBean> searchDeliveryTasks(DeliveryTaskFilterParam filterParam) throws Exception {
		String url = DeliveryURL.delivery_task_search_url;

		retObj = openRequest.baseRequest(url, RequestType.GET, filterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenDeliveryTaskBean.class)
				: null;
	}

	@Override
	public boolean assignDriver(List<String> order_ids, String driver_id) throws Exception {
		String url = DeliveryURL.delivery_driver_assign_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_ids", JSONArray.parseArray(order_ids.toString()).toString());
		paramMap.put("driver_id", driver_id);

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean cancelDriver(List<String> order_ids) throws Exception {
		String url = DeliveryURL.delivery_driver_cancel_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_ids", JSONArray.parseArray(order_ids.toString()).toString());

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public String createDriver(DrivcerCreateParam createParam) throws Exception {
		String url = DeliveryURL.delivery_driver_create_url;

		retObj = openRequest.baseRequest(url, RequestType.POST, createParam);
		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("driver_id") : null;
	}

	@Override
	public boolean updateDriver(DrivcerUpdateParam updateParam) throws Exception {
		String url = DeliveryURL.delivery_driver_update_url;

		retObj = openRequest.baseRequest(url, RequestType.POST, updateParam);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<OpenDriverBean> searchDriver(String search_text, String offset, String limit) throws Exception {
		String url = DeliveryURL.delivery_driver_search_url;

		Map<String, String> paramMap = new HashMap<>();
		if (search_text != null) {
			paramMap.put("search_text", search_text);
		}

		if (offset != null) {
			paramMap.put("offset", offset);
		}

		if (limit != null) {
			paramMap.put("limit", limit);
		}

		retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenDriverBean.class)
				: null;
	}

	@Override
	public List<OpenCarModelBean> searchCarModel(String search_text, String offset, String limit) throws Exception {
		String url = DeliveryURL.delivery_car_model_search_url;

		Map<String, String> paramMap = new HashMap<>();
		if (search_text != null) {
			paramMap.put("search_text", search_text);
		}

		if (offset != null) {
			paramMap.put("offset", offset);
		}

		if (limit != null) {
			paramMap.put("limit", limit);
		}

		retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenCarModelBean.class)
				: null;

	}

	@Override
	public List<OpenCarrierBean> searchCarrier(String search_text, String offset, String limit) throws Exception {
		String url = DeliveryURL.delivery_carrier_search_url;

		Map<String, String> paramMap = new HashMap<>();
		if (search_text != null) {
			paramMap.put("search_text", search_text);
		}

		if (offset != null) {
			paramMap.put("offset", offset);
		}

		if (limit != null) {
			paramMap.put("limit", limit);
		}
		retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenCarrierBean.class)
				: null;
	}

	@Override
	public boolean createRoute(String route_name, List<String> customer_ids) throws Exception {
		String url = DeliveryURL.delivery_route_create_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("route_name", route_name);
		paramMap.put("customer_ids", JSONArray.toJSONString(customer_ids));

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateRoute(String route_id, String route_name, List<String> customer_ids) throws Exception {
		String url = DeliveryURL.delivery_route_update_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("route_id", route_id);
		paramMap.put("route_name", route_name);
		paramMap.put("customer_ids", JSONArray.toJSONString(customer_ids));

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteRoute(String route_id) throws Exception {
		String url = DeliveryURL.delivery_route_delete_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("route_id", route_id);

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<OpenRouteBean> searchRoute(String search_text, String offset, String limit) throws Exception {
		String url = DeliveryURL.delivery_route_search_url;

		Map<String, String> paramMap = new HashMap<>();
		if (search_text != null) {
			paramMap.put("search_text", search_text);
		}

		if (offset != null) {
			paramMap.put("offset", offset);
		}

		if (limit != null) {
			paramMap.put("limit", limit);
		}

		retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenRouteBean.class)
				: null;
	}

	@Override
	public OpenRouteDetailBean getRouteDetail(String route_id) throws Exception {
		String url = DeliveryURL.delivery_route_detail_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("route_id", route_id);

		retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OpenRouteDetailBean.class)
				: null;
	}

}
