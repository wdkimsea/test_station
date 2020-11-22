package cn.guanmai.bshop.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.bshop.bean.order.BsCartBean;
import cn.guanmai.bshop.bean.order.BsOrderDetailBean;
import cn.guanmai.bshop.bean.order.BsOrderResultBean;
import cn.guanmai.bshop.bean.order.PayMethod;
import cn.guanmai.bshop.bean.product.BsProductBean;
import cn.guanmai.bshop.service.BsOrderService;
import cn.guanmai.bshop.url.BsOrderURL;
import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.util.JsonUtil;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.TimeUtil;

/**
 * @author: liming
 * @Date: 2020年6月16日 下午3:02:55
 * @description:
 * @version: 1.0
 */

public class BsOrderServiceImpl implements BsOrderService {
	private BaseRequest baseRequest;

	public BsOrderServiceImpl(Map<String, String> cookie) {
		baseRequest = new BaseRequestImpl(cookie);
	}

	@Override
	public List<BsProductBean> searchProducts(String search_text) throws Exception {
		String url = BsOrderURL.SEARCH_SKU_URL;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("text", search_text);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), BsProductBean.class)
				: null;
	}

	@Override
	public boolean updateCart(List<BsProductBean> orderProducts) throws Exception {
		String url = BsOrderURL.UPDATE_CART_URL;

		JSONArray skus = new JSONArray();
		JSONObject combine_map = new JSONObject();
		BigDecimal quantity = null;
		for (BsProductBean orderProduct : orderProducts) {
			quantity = NumberUtil.getRandomNumber(2, 8, 0);
			if (orderProduct.isIs_combine_goods()) {
				JSONObject combineObj = new JSONObject();
				combineObj.put("quantity", quantity);
				JSONObject real = new JSONObject();

				Map<String, BigDecimal> realMap = orderProduct.getSkus_ratio();
				for (String sku : realMap.keySet()) {
					real.put(sku, realMap.get(sku).multiply(quantity));
				}
				combineObj.put("real", real);
				combine_map.put(orderProduct.getCombine_good_id(), combineObj);
			} else {
				List<BsProductBean.Sku> skuList = orderProduct.getSkus();
				for (BsProductBean.Sku sku : skuList) {
					JSONObject skuObj = new JSONObject();
					skuObj.put(sku.getSku_id(), quantity);
					skus.add(skuObj);
				}
			}
		}

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("skus", skus.toString());
		paramMap.put("combine_map", combine_map.toString());

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean setPaymethod(PayMethod paymethod) throws Exception {
		String url = BsOrderURL.ORDER_CONFIRM_URL;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		JSONArray orders = retObj.getJSONObject("data").getJSONArray("orders");

		Map<String, String> paramMap = null;
		url = BsOrderURL.ORDER_PATMETHOD_URL;

		for (int i = 0; i < orders.size(); i++) {
			JSONObject orderObj = orders.getJSONObject(i);
			String station_id = orderObj.getString("station_id");
			JSONObject receive_time_limit = orderObj.getJSONObject("receive_time").getJSONObject("receive_time_limit");
			String time_config_id = receive_time_limit.getString("time_config_id");

			paramMap = new HashMap<>();
			paramMap.put("type", paymethod == PayMethod.One ? "1" : "2");
			paramMap.put("time_config_id", time_config_id);
			paramMap.put("station_id", station_id);

			retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

			if (retObj.getInteger("code") != 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean setReceiveTime() throws Exception {
		String url = BsOrderURL.ORDER_CONFIRM_URL;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		JSONArray orders = retObj.getJSONObject("data").getJSONArray("orders");

		String current_time = TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm");
		String today = current_time.substring(0, 10);

		for (Object obj : orders) {
			JSONObject orderObj = JSONObject.parseObject(obj.toString());
			JSONObject receiveTimeObj = orderObj.getJSONObject("receive_time");

			url = BsOrderURL.ORDER_RECEIVE_TIME_URL;

			JSONObject receiveTimeLimit = receiveTimeObj.getJSONObject("receive_time_limit");
			int timeConfigType = receiveTimeLimit.getInteger("time_config_type");
			if (timeConfigType == 1) {
				int endSpanTime = receiveTimeLimit.getInteger("e_span_time");
				int receiveTimeSpan = Integer.valueOf(receiveTimeLimit.getString("receiveTimeSpan"));
				String endTime = receiveTimeLimit.getString("r_end");
				String startTime = TimeUtil.calculateTime("HH:mm", endTime, -receiveTimeSpan, Calendar.MINUTE);

				String endDay = TimeUtil.calculateTime("yyyy-MM-dd", today, endSpanTime, Calendar.DATE);
				String receiveStartTime = endDay + " " + startTime;

				// 已经没有可用的收货时间
				if (TimeUtil.compareDate("yyyy-MM-dd HH:mm", current_time, receiveStartTime) >= 0) {
					throw new Exception("没有可用的收货时间");
				}

				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("defaultSpanStartFlag", String.valueOf(endSpanTime));
				paramMap.put("defaultSpanEndFlag", String.valueOf(endSpanTime));

				paramMap.put("defaultStart", startTime);
				paramMap.put("defaultEnd", endTime);

				paramMap.put("time_config_id", receiveTimeLimit.getString("time_config_id"));
				paramMap.put("station_id", orderObj.getString("station_id"));

				retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

				if (retObj.getInteger("code") != 0) {
					return false;
				}
			} else {
				int weekdays = receiveTimeLimit.getInteger("weekdays");
				String weekdaysBinary = Integer.toBinaryString(weekdays);
				int length = weekdaysBinary.length();
				int startSpanTime = receiveTimeLimit.getInteger("s_span_time");
				int endSpanTime = receiveTimeLimit.getInteger("e_span_time");
				String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
				String startDay = TimeUtil.calculateTime("yyyy-MM-dd", todayStr, startSpanTime, Calendar.DATE);
				String endDay = TimeUtil.calculateTime("yyyy-MM-dd", todayStr, endSpanTime, Calendar.DATE);

				while (TimeUtil.compareDate("yyyy-MM-dd", startDay, endDay) <= 0) {
					int weekday = TimeUtil.getDateOfWeek(startDay);
					if (weekday <= length) {
						char temp = weekdaysBinary.charAt(length - weekday);
						if (String.valueOf(temp).equals("1")) {
							break;
						}
					}
					startSpanTime += 1;
					startDay = TimeUtil.calculateTime("yyyy-MM-dd", todayStr, startSpanTime, Calendar.DATE);
				}

				int receiveTimeSpan = Integer.valueOf(receiveTimeLimit.getString("receiveTimeSpan"));
				String receiveStartTime = receiveTimeLimit.getString("r_start");
				String receiveEndTime = TimeUtil.calculateTime("HH:mm", receiveStartTime, receiveTimeSpan,
						Calendar.MINUTE);

				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("defaultSpanStartFlag", String.valueOf(startSpanTime));
				paramMap.put("defaultSpanEndFlag", String.valueOf(startSpanTime));

				paramMap.put("defaultStart", receiveStartTime);
				paramMap.put("defaultEnd", receiveEndTime);

				paramMap.put("time_config_id", receiveTimeLimit.getString("time_config_id"));
				paramMap.put("station_id", orderObj.getString("station_id"));

				retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

				if (retObj.getInteger("code") != 0) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public BsCartBean getCart() throws Exception {
		String url = BsOrderURL.GET_CART_URL;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), BsCartBean.class)
				: null;
	}

	@Override
	public List<BsOrderResultBean> submitCart(boolean isCombineOrder) throws Exception {
		String url = BsOrderURL.ORDER_CONFIRM_URL;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);
		if (retObj.getInteger("code") == 0) {
			JSONArray orders = retObj.getJSONObject("data").getJSONArray("orders");
			JSONArray skuArray = new JSONArray();
			for (int i = 0; i < orders.size(); i++) {
				JSONObject orderObj = orders.getJSONObject(i);
				JSONObject skuObj = new JSONObject();
				skuObj.put("salemenu_ids", orderObj.getJSONArray("salemenu_ids"));
				skuObj.put("sku_ids", orderObj.getJSONArray("sku_ids"));
				skuObj.put("combine_good_ids", orderObj.getJSONArray("combine_good_ids"));
				skuObj.put("station_id", orderObj.getString("station_id"));
				skuObj.put("spu_remark", new JSONObject());
				if (isCombineOrder) {
					if (orderObj.get("order_id") != null) {
						skuObj.put("order_id", orderObj.getString("order_id"));
					}
				}
				skuArray.add(skuObj);
			}

			url = BsOrderURL.ORDER_SUBMIT_URL;
			Map<String, String> paramMap = new HashMap<>();
			paramMap.put("orders", skuArray.toString());

			retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

			return retObj.getInteger("code") == 0
					? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), BsOrderResultBean.class)
					: null;
		} else {
			throw new Exception("接口 :" + url + " 没有正确返回结果");
		}
	}

	@Override
	public BsOrderResultBean submitCart(String avail_coupon_id) throws Exception {
		String url = BsOrderURL.ORDER_CONFIRM_URL;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		BsOrderResultBean bsOrderResult = null;
		if (retObj.getInteger("code") == 0) {
			JSONArray orders = retObj.getJSONObject("data").getJSONArray("orders");
			Random random = new Random();

			JSONObject orderObj = orders.getJSONObject(random.nextInt(orders.size()));
			JSONArray skuArray = new JSONArray();

			JSONObject skuObj = new JSONObject();
			skuObj.put("salemenu_ids", orderObj.getJSONArray("salemenu_ids"));
			skuObj.put("sku_ids", orderObj.getJSONArray("sku_ids"));
			skuObj.put("combine_good_ids", orderObj.getJSONArray("combine_good_ids"));
			skuObj.put("station_id", orderObj.getString("station_id"));
			skuObj.put("spu_remark", new JSONObject());
			skuObj.put("coupon_id", new BigDecimal(avail_coupon_id));

			skuArray.add(skuObj);

			url = BsOrderURL.ORDER_SUBMIT_URL;
			Map<String, String> paramMap = new HashMap<>();
			paramMap.put("orders", skuArray.toString());

			retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

			if (retObj.getInteger("code") == 0) {
				bsOrderResult = JsonUtil.strToClassObject(retObj.getJSONArray("data").getJSONObject(0).toString(),
						BsOrderResultBean.class);
			}
		} else {
			throw new Exception("接口 :" + url + " 没有正确返回结果");
		}
		return bsOrderResult;
	}

	@Override
	public BsOrderDetailBean getOrderDetail(String order_id) throws Exception {
		String url = BsOrderURL.ORDER_DETAIL_URL;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", order_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), BsOrderDetailBean.class)
				: null;
	}

	@Override
	public boolean payOrder(List<String> order_ids) throws Exception {
		String url = BsOrderURL.ORDER_PAY_URL;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_ids", JSONArray.toJSONString(order_ids));
		paramMap.put("pay_method", "1");
		paramMap.put("page", "order");

		StringBuffer attach = new StringBuffer("orderIdsGMEQ");
		StringBuffer state = new StringBuffer("orderIdsGMEQP");

		for (String order_id : order_ids) {
			attach.append(order_id).append("GMDOT");
			state.append(order_id).append("GMDOT");
		}
		attach = attach.delete(attach.lastIndexOf("GMDOT"), attach.length());
		state = state.delete(state.lastIndexOf("GMDOT"), state.length());

		attach.append("GMANDpayMethodGMEQ1GMANDisChargeGMEQ0GMANDselected_conponsGMEQ{}");
		state.append("GMANDpayMethodGMEQ1GMANDisChargeGMEQ0GMANDselected_conponsGMEQ{}GMANDpageGMEQorder");

		paramMap.put("attach", attach.toString());
		paramMap.put("state", state.toString());

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<String> getPartPayOrders() throws Exception {
		String url = BsOrderURL.ORDER_PART_PAY_LIST_URL;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		List<String> order_ids = null;
		if (retObj.getInteger("code") == 0) {
			order_ids = new ArrayList<String>();
			JSONArray order_list = retObj.getJSONObject("data").getJSONArray("order_list");
			for (Object obj : order_list) {
				JSONObject orderObj = JSONObject.parseObject(obj.toString());
				order_ids.add(orderObj.getString("order_id"));
			}
		}
		return order_ids;
	}

}
