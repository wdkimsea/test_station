package cn.guanmai.open.impl.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.open.bean.order.OpenOrderBean;
import cn.guanmai.open.bean.order.OpenOrderDetailBean;
import cn.guanmai.open.bean.order.param.OrderAbnormalCreateParam;
import cn.guanmai.open.bean.order.param.OrderAbnormalDeleteParam;
import cn.guanmai.open.bean.order.param.OrderAbnormalUpdateParam;
import cn.guanmai.open.bean.order.param.OrderCreateParam;
import cn.guanmai.open.bean.order.param.OrderProductParam;
import cn.guanmai.open.bean.order.param.OrderRefundCreateParam;
import cn.guanmai.open.bean.order.param.OrderRefundDeleteParam;
import cn.guanmai.open.bean.order.param.OrderRefundUpdateParam;
import cn.guanmai.open.bean.order.param.OrderSearchParam;
import cn.guanmai.open.bean.order.param.OrderUpdateParam;
import cn.guanmai.open.interfaces.order.OpenOrderService;
import cn.guanmai.open.url.OrderURL;
import cn.guanmai.request.impl.OpenRequestImpl;
import cn.guanmai.request.intefaces.OpenRequest;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Jun 4, 2019 2:49:32 PM 
* @todo TODO
* @version 1.0 
*/
public class OpenOrderServiceImpl implements OpenOrderService {
	private JSONObject retObj;
	private OpenRequest openRequest;

	public OpenOrderServiceImpl(String access_token) {
		openRequest = new OpenRequestImpl(access_token);
	}

	@Override
	public String createOrder(OrderCreateParam createParam) throws Exception {
		String url = OrderURL.order_create_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("customer_id", createParam.getCustomer_id());
		if (createParam.getCustomer_address() != null) {
			paramMap.put("customer_address", createParam.getCustomer_address());
		}
		paramMap.put("time_config_id", createParam.getTime_config_id());
		paramMap.put("receive_begin_time", createParam.getReceive_begin_time());
		paramMap.put("receive_end_time", createParam.getReceive_end_time());
		paramMap.put("remark", createParam.getRemark() == null ? "" : createParam.getRemark());
		paramMap.put("products", JsonUtil.objectToStr(createParam.getProducts()));
		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("order_id") : null;
	}

	@Override
	public boolean deleteOrder(String order_id) throws Exception {
		String url = OrderURL.order_detele_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateOrder(OrderUpdateParam updateParam) throws Exception {
		String url = OrderURL.order_update_url;

		retObj = openRequest.baseRequest(url, RequestType.POST, updateParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public OpenOrderDetailBean getOrderDetail(String order_id) throws Exception {
		String url = OrderURL.order_detail_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);

		retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OpenOrderDetailBean.class)
				: null;
	}

	@Override
	public List<OpenOrderBean> searchOrder(OrderSearchParam searchParam) throws Exception {
		String url = OrderURL.order_list_url;

		retObj = openRequest.baseRequest(url, RequestType.GET, searchParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenOrderBean.class)
				: null;
	}

	@Override
	public boolean addOrderSkus(String order_id, List<OrderProductParam> products) throws Exception {
		String url = OrderURL.order_sku_create_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);
		paramMap.put("products", JsonUtil.objectToStr(products));

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteOrderSkus(String order_id, List<String> sku_ids) throws Exception {
		String url = OrderURL.order_sku_delete_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);
		paramMap.put("sku_ids", JSONArray.toJSONString(sku_ids));

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateOrderSkus(String order_id, List<OrderProductParam> products) throws Exception {
		String url = OrderURL.order_sku_update_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);
		paramMap.put("products", JsonUtil.objectToStr(products));

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean createOrderAbnormal(String order_id, List<OrderAbnormalCreateParam> abnormals) throws Exception {
		String url = OrderURL.order_abnormal_create_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);
		paramMap.put("abnormals", JsonUtil.objectToStr(abnormals));

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean createOrderRefund(String order_id, List<OrderRefundCreateParam> refunds) throws Exception {
		String url = OrderURL.order_refund_create_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);
		paramMap.put("refunds", JsonUtil.objectToStr(refunds));

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateOrderAbnormal(String order_id, List<OrderAbnormalUpdateParam> abnormals) throws Exception {
		String url = OrderURL.order_abnormal_update_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);
		paramMap.put("abnormals", JsonUtil.objectToStr(abnormals));

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateOrderRefund(String order_id, List<OrderRefundUpdateParam> refunds) throws Exception {
		String url = OrderURL.order_refund_update_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);
		paramMap.put("refunds", JsonUtil.objectToStr(refunds));

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteOrderAbnormal(String order_id, List<OrderAbnormalDeleteParam> abnormals) throws Exception {
		String url = OrderURL.order_abnormal_delete_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);
		paramMap.put("abnormals", JsonUtil.objectToStr(abnormals));

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteOrderRefund(String order_id, List<OrderRefundDeleteParam> refunds) throws Exception {
		String url = OrderURL.order_refund_delete_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);
		paramMap.put("refunds", JsonUtil.objectToStr(refunds));

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public Map<String, String> getExceptionReasons() throws Exception {
		String url = OrderURL.order_exception_reason_url;

		retObj = openRequest.baseRequest(url, RequestType.GET);

		Map<String, String> exceptionReasons = null;
		if (retObj.getInteger("code") == 0) {
			exceptionReasons = new HashMap<>();
			JSONObject dataObj = retObj.getJSONObject("data");
			String key = null;
			for (Object obj : dataObj.keySet()) {
				key = String.valueOf(obj);
				exceptionReasons.put(key, dataObj.getString(key));
			}
		}
		return exceptionReasons;
	}

}
