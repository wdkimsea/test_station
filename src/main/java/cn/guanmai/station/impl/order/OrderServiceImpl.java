package cn.guanmai.station.impl.order;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.order.AddressLabelBean;
import cn.guanmai.station.bean.order.CustomerBean;
import cn.guanmai.station.bean.order.OrderAnalysisBean;
import cn.guanmai.station.bean.order.OrderBatchResultBean;
import cn.guanmai.station.bean.order.OrderBatchUploadResultBean;
import cn.guanmai.station.bean.order.OrderBean;
import cn.guanmai.station.bean.order.OrderChangeSkuResultBean;
import cn.guanmai.station.bean.order.OrderDeleteSkuResultBean;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderImportResultBean;
import cn.guanmai.station.bean.order.OrderPriceSyncToSkuResultBean;
import cn.guanmai.station.bean.order.OrderProcessBean;
import cn.guanmai.station.bean.order.OrderReceiveTimeBean;
import cn.guanmai.station.bean.async.AsyncTaskBean;
import cn.guanmai.station.bean.order.OrderResponseBean;
import cn.guanmai.station.bean.order.OrderSkuCopyBean;
import cn.guanmai.station.bean.order.OrderSkuFilterResultBean;
import cn.guanmai.station.bean.order.OrderSkuRecognizeBean;
import cn.guanmai.station.bean.order.RecentOrderBean;
import cn.guanmai.station.bean.order.OrderSkuBean;
import cn.guanmai.station.bean.order.SaleSkuPriceUpdateResultBean;
import cn.guanmai.station.bean.order.param.OrderBatchCreateParam;
import cn.guanmai.station.bean.order.param.OrderChangeSkuParam;
import cn.guanmai.station.bean.order.param.OrderCreateParam;
import cn.guanmai.station.bean.order.param.OrderDeleteSkuParam;
import cn.guanmai.station.bean.order.param.OrderEditParam;
import cn.guanmai.station.bean.order.param.OrderExceptionParam;
import cn.guanmai.station.bean.order.param.OrderRefundParam;
import cn.guanmai.station.bean.order.param.OrderFilterParam;
import cn.guanmai.station.bean.order.param.OrderSkuParam;
import cn.guanmai.station.bean.order.param.OrderSkuPriceAutoUpdateParam;
import cn.guanmai.station.bean.order.param.OrderStatusPreconfigParam;
import cn.guanmai.station.bean.order.param.WeightRemarkFilterParam;
import cn.guanmai.station.bean.order.param.OrderSkuFilterParam;
import cn.guanmai.station.bean.share.OrderAndSkuBean;
import cn.guanmai.station.bean.system.ServiceTimeBean;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.url.OrderURL;
import cn.guanmai.util.JsonUtil;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/* 
* @author liming 
* @date Nov 12, 2018 10:33:36 AM 
* @des 订单相关业务
* @version 1.0 
*/
public class OrderServiceImpl implements OrderService {
	private BaseRequest baseRequest;

	public OrderServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<OrderBean> searchOrder(OrderFilterParam orderFilterParam) throws Exception {
		String url = OrderURL.search_order_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, orderFilterParam);

		List<OrderBean> OrderDetailList = null;

		if (retObj.getInteger("code") == 0) {
			JSONArray list = retObj.getJSONObject("data").getJSONArray("list");
			OrderDetailList = JsonUtil.strToClassList(list.toString(), OrderBean.class);
		}
		return OrderDetailList;
	}

	@Override
	public List<CustomerBean> getCustomers() throws Exception {
		String urlStr = OrderURL.customer_list_url;
		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET);

		List<CustomerBean> customers = null;
		if (retObj.getInteger("code") == 0) {
			customers = JsonUtil.strToClassList(retObj.getJSONObject("data").getJSONArray("list").toString(),
					CustomerBean.class);

		}
		return customers;
	}

	@Override
	public List<CustomerBean> getOrderCustomerArray(int count) throws Exception {
		List<CustomerBean> customer_array = null;
		String urlStr = OrderURL.customer_list_url;
		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET);

		if (retObj.getInteger("code") == 0) {
			customer_array = new ArrayList<CustomerBean>();
			List<CustomerBean> temp_customer_array = JsonUtil
					.strToClassList(retObj.getJSONObject("data").getJSONArray("list").toString(), CustomerBean.class);

			// 判断商户是否可以正常下单
			urlStr = OrderURL.customer_check_unpay_url;
			for (CustomerBean customer : temp_customer_array) {
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("address_id", customer.getAddress_id());

				retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

				if (retObj.getInteger("code") == 0) {
					int type = retObj.getJSONObject("data").getInteger("type");
					if (type == 0 || type == 11) {
						customer_array.add(customer);
						if (customer_array.size() >= count) {
							break;
						}
					}
				} else {
					customer_array = null;
					break;
				}
			}
		}
		return customer_array;
	}

	public OrderReceiveTimeBean getOrderReceiveTime(ServiceTimeBean serviceTime) throws ParseException {
		ServiceTimeBean.ReceiveTimeLimit receiveTimeLimit = serviceTime.getReceive_time_limit();
		String current_time = TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm");
		String today = current_time.substring(0, 10);

		int s_span_time = receiveTimeLimit.getS_span_time();
		int e_span_time = receiveTimeLimit.getE_span_time();
		String start = receiveTimeLimit.getStart();
		String end = receiveTimeLimit.getEnd();

		// 收货起始时间 ,精确到分
		String start_receive_time = TimeUtil.calculateTime("yyyy-MM-dd", today, s_span_time, Calendar.DATE) + " "
				+ start;
		int receiveTimeSpan = receiveTimeLimit.getReceiveTimeSpan();

		OrderReceiveTimeBean orderReceiveTime = new OrderReceiveTimeBean();
		List<OrderReceiveTimeBean.ReceiveTime> receiveTimes = new ArrayList<OrderReceiveTimeBean.ReceiveTime>();
		// 默认收货时间
		if (serviceTime.getType() == 1) {
			// 收货结束时间,精确到分
			String end_receive_time = TimeUtil.calculateTime("yyyy-MM-dd", today, e_span_time, Calendar.DATE) + " "
					+ end;
			OrderReceiveTimeBean.ReceiveTime receiveTime = orderReceiveTime.new ReceiveTime();
			List<String> receive_times = new ArrayList<String>();

			// 一直循环,直到收货结束时间等于或大于收货起始时间
			while (TimeUtil.compareDate("yyyy-MM-dd HH:mm", start_receive_time, end_receive_time) <= 0) {
				// 去掉已经过了的收货时间(收货起始时间小于当前时间)
				if (TimeUtil.compareDate("yyyy-MM-dd HH:mm", start_receive_time, current_time) > 0) {
					receive_times.add(start_receive_time);
					// 收货的日期
					String receive_day = start_receive_time.substring(0, 10);
					// 存储收货天数
					String day = receiveTime.getDay();
					if (day != null) {
						if (!day.contains(receive_day)) {
							receive_day = day + "~" + receive_day;
							receiveTime.setDay(receive_day);
						}
					} else {
						receiveTime.setDay(receive_day);
					}
				}
				// 收货起始时间自增
				start_receive_time = TimeUtil.calculateTime("yyyy-MM-dd HH:mm", start_receive_time, receiveTimeSpan,
						Calendar.MINUTE);
			}
			receiveTime.setTimes(receive_times);
			receiveTimes.add(receiveTime);
		} else {
			// 是否跨天的标志
			int receiveEndSpan = receiveTimeLimit.getReceiveEndSpan();

			int weekdays = receiveTimeLimit.getWeekdays();
			// 收货自然日转化的二进制字符串,对应一个星期的七天
			String weekdaysBinary = Integer.toBinaryString(weekdays);
			int length = weekdaysBinary.length();

			// 收货起始日期
			String receive_start_date = TimeUtil.calculateTime("yyyy-MM-dd", today, s_span_time, Calendar.DATE);
			// 收货结束日期
			String receive_end_date = TimeUtil.calculateTime("yyyy-MM-dd", today, e_span_time, Calendar.DATE);

			OrderReceiveTimeBean.ReceiveTime receiveTime = null;
			// 一直循环,直到收货起始日期大于收货日期
			while (TimeUtil.compareDate("yyyy-MM-dd", receive_start_date, receive_end_date) <= 0) {
				receiveTime = orderReceiveTime.new ReceiveTime();
				List<String> receive_times = new ArrayList<String>();
				// 获取指定日期是星期几
				int weekday = TimeUtil.getDateOfWeek(receive_start_date);
				// 如果星期几大于收货自然日的二进制,那就肯定那天不能收货
				if (length >= weekday) {
					// 获取那天对应的收货自然日字节,如果是1才代表可以收货
					char temp = weekdaysBinary.charAt(length - weekday);
					start_receive_time = receive_start_date + " " + start;
					if (String.valueOf(temp).equals("1")) {
						// 跨天了
						if (receiveEndSpan == 1) {
							// 当前收货周期的最晚收货时间
							String end_receive_time = TimeUtil.calculateTime("yyyy-MM-dd", receive_start_date, 1,
									Calendar.DATE) + " " + end;
							while (TimeUtil.compareDate("yyyy-MM-dd HH:mm", start_receive_time,
									end_receive_time) <= 0) {
								if (TimeUtil.compareDate("yyyy-MM-dd HH:mm", start_receive_time, current_time) > 0) {
									receive_times.add(start_receive_time);
									String receive_day = start_receive_time.substring(0, 10);
									String day = receiveTime.getDay();
									if (day != null) {
										if (!day.contains(receive_day)) {
											receive_day = day + "~" + receive_day;
											receiveTime.setDay(receive_day);
										}
									} else {
										receiveTime.setDay(receive_day);
									}
								}
								start_receive_time = TimeUtil.calculateTime("yyyy-MM-dd HH:mm", start_receive_time,
										receiveTimeSpan, Calendar.MINUTE);
							}
							receiveTime.setTimes(receive_times);
						} else {
							// 没有跨天的当前收货周期的最晚收货时间
							String end_receive_time = receive_start_date + " " + end;
							while (TimeUtil.compareDate("yyyy-MM-dd HH:mm", start_receive_time,
									end_receive_time) <= 0) {
								if (TimeUtil.compareDate("yyyy-MM-dd HH:mm", start_receive_time, current_time) > 0) {
									receive_times.add(start_receive_time);
									String receive_day = start_receive_time.substring(0, 10);
									String day = receiveTime.getDay();
									if (day != null) {
										if (!day.contains(receive_day)) {
											receive_day = day + "~" + receive_day;
											receiveTime.setDay(receive_day);
										}
									} else {
										receiveTime.setDay(receive_day);
									}
								}
								start_receive_time = TimeUtil.calculateTime("yyyy-MM-dd HH:mm", start_receive_time,
										receiveTimeSpan, Calendar.MINUTE);
							}
							receiveTime.setTimes(receive_times);
						}
					}
				}
				if (receiveTime != null && receiveTime.getDay() != null) {
					receiveTimes.add(receiveTime);
				}
				receive_start_date = TimeUtil.calculateTime("yyyy-MM-dd", receive_start_date, 1, Calendar.DATE);
			}
		}

		orderReceiveTime.setTime_config_id(serviceTime.getId());
		orderReceiveTime.setReceive_times(receiveTimes);

		return orderReceiveTime;
	}

	@Override
	public List<OrderReceiveTimeBean> getCustomerServiceTimeArray(String address_id) throws Exception {
		List<ServiceTimeBean> serviceTimeArray = null;
		String urlStr = OrderURL.customer_service_time_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("address_id", address_id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		List<OrderReceiveTimeBean> orderReceiveTimes = null;
		if (retObj.getInteger("code") == 0) {
			String serticeTimeStr = retObj.getJSONObject("data").getJSONArray("service_time").toString();
			serviceTimeArray = JsonUtil.strToClassList(serticeTimeStr, ServiceTimeBean.class);
			orderReceiveTimes = new ArrayList<OrderReceiveTimeBean>();
			OrderReceiveTimeBean orderReceiveTime = null;
			for (ServiceTimeBean serviceTime : serviceTimeArray) {
				orderReceiveTime = getOrderReceiveTime(serviceTime);
				orderReceiveTimes.add(orderReceiveTime);
			}
		}
		return orderReceiveTimes;
	}

	@Override
	public List<OrderSkuParam> orderSkus(String address_id, String time_config_id, String[] search_texts, int max_count)
			throws Exception {
		List<OrderSkuParam> orderSkuArray = new ArrayList<OrderSkuParam>();
		String urlStr = OrderURL.search_sku_url;
		JSONArray sku_id_array = new JSONArray();
		OK: for (String search_text : search_texts) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("address_id", address_id);
			paramMap.put("time_config_id", time_config_id);
			paramMap.put("search_text", search_text);
			paramMap.put("offset", "0");
			paramMap.put("limit", "20");
			paramMap.put("fetch_category", "1");
			paramMap.put("usual_type", "2");
			paramMap.put("active", "1");

			JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

			if (retObj.getInteger("code") == 0) {
				List<OrderSkuBean> tempArray = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(),
						OrderSkuBean.class);
				// 因为属性字符串的不同,所以到这里要重新封装一下
				for (OrderSkuBean sku : tempArray) {
					if (orderSkuArray.size() < max_count) {
						if (!sku_id_array.contains(sku.getId())) {
							orderSkuArray.add(new OrderSkuParam(sku.getId(), sku.getSpu_id(), sku.getSale_price(),
									NumberUtil.getRandomNumber(5, 15, 2), StringUtil.getRandomString(6),
									sku.isIs_price_timing()));
							sku_id_array.add(sku.getId());
						}
					} else {
						break OK;
					}
				}
			} else {
				throw new Exception("下单搜索商品报错,报错信息: " + retObj.getString("msg"));
			}
		}
		return orderSkuArray;
	}

	@Override
	public OrderCreateParam searchOrderSkus(String address_id, String time_config_id, String[] search_texts,
			int max_count) throws Exception {
		String urlStr = OrderURL.search_sku_url;
		JSONArray sku_ids = new JSONArray();

		OrderCreateParam orderCreateParam = new OrderCreateParam();
		List<OrderCreateParam.OrderSku> orderSkuArray = new ArrayList<OrderCreateParam.OrderSku>();
		Map<String, OrderCreateParam.CombineGoods> combine_goods_map = new HashMap<String, OrderCreateParam.CombineGoods>();

		List<OrderSkuBean> orderSkus = new ArrayList<OrderSkuBean>(); // 用来存放搜索的下单商品
		OK: for (String search_text : search_texts) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("address_id", address_id);
			paramMap.put("time_config_id", time_config_id);
			paramMap.put("search_text", search_text);
			paramMap.put("offset", "0");
			paramMap.put("limit", "20");
			paramMap.put("fetch_category", "1");
			paramMap.put("usual_type", "2");
			paramMap.put("active", "1");
			paramMap.put("search_combine_goods", "1");

			JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

			if (retObj.getInteger("code") == 0) {
				List<OrderSkuBean> tempOrderSkus = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(),
						OrderSkuBean.class);
				for (OrderSkuBean tempOrderSku : tempOrderSkus) {
					if (!sku_ids.contains(tempOrderSku.getId())) {
						orderSkus.add(tempOrderSku);
					}
					sku_ids.add(tempOrderSku.getId());
					if (orderSkus.size() >= 40) {
						break OK;
					}
				}
			} else {
				throw new Exception("下单搜索商品报错,报错信息: " + retObj.getString("msg"));
			}
		}

		// 随机取下单商品
		orderSkus = NumberUtil.roundNumberInList(orderSkus, max_count);
		for (OrderSkuBean sku : orderSkus) {
			OrderCreateParam.OrderSku orderSku = null;
			if (sku.isIs_combine_goods()) {
				Map<String, BigDecimal> skus_ratio = sku.getSkus_ratio();
				List<OrderSkuBean> childSkus = sku.getSkus();
				BigDecimal quantity = NumberUtil.getRandomNumber(5, 15, 1);

				for (OrderSkuBean childSku : childSkus) {
					orderSku = orderCreateParam.new OrderSku();
					String sku_id = childSku.getId();
					BigDecimal ratio = skus_ratio.get(sku_id);
					orderSku.setSpu_id(childSku.getSpu_id());
					orderSku.setSku_id(sku_id);
					orderSku.setUnit_price(childSku.getSale_price());
					orderSku.setAmount(quantity.multiply(ratio));
					orderSku.setFake_quantity(quantity.multiply(ratio));
					orderSku.setSpu_remark(StringUtil.getRandomString(6));
					orderSku.setSalemenu_id(childSku.getSalemenu_id());
					orderSku.setIs_combine_goods(true);
					orderSku.setCombine_goods_id(sku.getId());
					orderSkuArray.add(orderSku);
				}

				OrderCreateParam.CombineGoods combineGoods = orderCreateParam.new CombineGoods();
				combineGoods.setFake_quantity(quantity);
				combineGoods.setQuantity(quantity);
				combineGoods.setImgs("");
				combineGoods.setName(sku.getName());
				combineGoods.setSale_unit_name(sku.getStd_unit_name_forsale());
				combineGoods.setSkus_ratio(skus_ratio);
				combine_goods_map.put(sku.getId(), combineGoods);
			} else {
				orderSku = orderCreateParam.new OrderSku();
				orderSku.setSpu_id(sku.getSpu_id());
				orderSku.setSku_id(sku.getId());
				orderSku.setUnit_price(sku.getSale_price());
				BigDecimal quantity = NumberUtil.getRandomNumber(5, 15, 1);
				orderSku.setAmount(quantity);
				orderSku.setFake_quantity(quantity);
				orderSku.setSpu_remark(StringUtil.getRandomNumber(6));
				orderSku.setSalemenu_id(sku.getSalemenu_id());
				orderSku.setIs_combine_goods(false);
				orderSkuArray.add(orderSku);

			}
		}
		orderCreateParam.setCombine_goods_map(combine_goods_map);
		orderCreateParam.setDetails(orderSkuArray);
		return orderCreateParam;

	}

	@Override
	public OrderSkuRecognizeBean recognizeSaleSku(String address_id, String time_config_id, String recognition_text)
			throws Exception {
		String urlStr = OrderURL.recognize_sku_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("address_id", address_id);
		paramMap.put("time_config_id", time_config_id);
		paramMap.put("recognition_text", recognition_text);
		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		OrderSkuRecognizeBean orderSkuRecognize = null;
		if (retObj.getInteger("code") == 0) {
			orderSkuRecognize = JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(),
					OrderSkuRecognizeBean.class);
		} else {
			throw new Exception("下单智能识别商品接口调用失败");
		}
		return orderSkuRecognize;
	}

	@Override
	public OrderResponseBean createOrder(OrderCreateParam order) throws Exception {
		String urlStr = OrderURL.create_order_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, order);

		return JsonUtil.strToClassObject(retObj.toJSONString(), OrderResponseBean.class);
	}

	@Override
	public OrderResponseBean createOldOrder(OrderCreateParam order) throws Exception {
		String url = OrderURL.create_old_order_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, order);

		return JsonUtil.strToClassObject(retObj.toString(), OrderResponseBean.class);
	}

	@Override
	public boolean editOrder(OrderEditParam editOrder) throws Exception {
		String urlStr = OrderURL.edit_order_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, editOrder);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteOrder(String order_id) throws Exception {
		String urlStr = OrderURL.delete_order_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", order_id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean preconfigUpdateOrderStatus(OrderStatusPreconfigParam orderStatusPreconfigParam) throws Exception {
		String urlStr = OrderURL.preconfig_order_status_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, orderStatusPreconfigParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public OrderDetailBean getOrderDetailById(String order_id) throws Exception {
		OrderDetailBean orderDetail = null;
		String urlStr = OrderURL.order_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", order_id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		if (retObj.getInteger("code") == 0) {
			orderDetail = JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OrderDetailBean.class);
		}
		return orderDetail;
	}

	@Override
	public OrderBean getOrderBeanById(String order_id) throws Exception {
		String url = OrderURL.search_order_url;

		String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
		OrderFilterParam param = new OrderFilterParam();
		param.setQuery_type(1);
		param.setStart_date(todayStr);
		param.setEnd_date(todayStr);
		param.setSearch_text(order_id);
		param.setOffset(0);
		param.setLimit(20);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, param);

		if (retObj.getInteger("code") == 0) {
			List<OrderBean> orders = JsonUtil
					.strToClassList(retObj.getJSONObject("data").getJSONArray("list").toString(), OrderBean.class);
			return orders.stream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
		} else {
			throw new Exception("订单列表搜索查询报错 " + url);
		}
	}

	@Override
	public boolean orderRealQuantityUpdate(String order_id, String sku_id, BigDecimal std_real_quantity)
			throws Exception {
		String urlStr = OrderURL.order_real_quantity_update_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", order_id);
		paramMap.put("sku_id", sku_id);
		paramMap.put("std_real_quantity", String.valueOf(std_real_quantity));

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateOrderState(List<String> order_ids, int status, String batch_remark) throws Exception {
		String urlStr = OrderURL.update_order_state_url;

		// 后端接收的为非数组形式的参数,这里需要转化一下
		// 参数格式: PL3941335,PL3941334,PL3941333
		StringJoiner ids = new StringJoiner(",");
		for (String id : order_ids) {
			ids.add(id);
		}
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_ids", ids.toString());
		paramMap.put("status", String.valueOf(status));
		paramMap.put("batch_remark", batch_remark);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateOrderState(String order_id, int status) throws Exception {
		String urlStr = OrderURL.update_order_state_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_ids", order_id);
		paramMap.put("status", String.valueOf(status));

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateBatchOutOfStock(List<OrderAndSkuBean> batchOutOfStockArray) throws Exception {
		String urlStr = OrderURL.batch_out_of_stock_url;

		String sku_info = JsonUtil.objectToStr(batchOutOfStockArray);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("sku_info", sku_info);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;

	}

	@Override
	public AsyncTaskBean updateOrderSkuPriceAuto(List<OrderAndSkuBean> orderSkuPriceAutoArray, int price_unit_type)
			throws Exception {
		String urlStr = OrderURL.update_sku_price_auto_url;

		String update_list = JsonUtil.objectToStr(orderSkuPriceAutoArray);

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("update_list", update_list);
		paramMap.put("price_unit_type", String.valueOf(price_unit_type));

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), AsyncTaskBean.class)
				: null;
	}

	@Override
	public AsyncTaskBean updateOrderSkuPriceAuto(OrderSkuPriceAutoUpdateParam orderSkuPriceAutoUpdateParam)
			throws Exception {
		String urlStr = OrderURL.update_sku_price_auto_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, orderSkuPriceAutoUpdateParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), AsyncTaskBean.class)
				: null;
	}

	@Override
	public BigDecimal updateOrderSkuPrice(JSONObject price_data) throws Exception {
		String urlStr = OrderURL.update_sku_price_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("price_data", price_data.toString());

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}

	@Override
	public List<OrderSkuFilterResultBean> searchOrderSku(OrderSkuFilterParam orderSkuFilterParam) throws Exception {
		String urlStr = OrderURL.order_sku_list_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, orderSkuFilterParam);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassList(
				retObj.getJSONObject("data").getJSONArray("list").toString(), OrderSkuFilterResultBean.class) : null;
	}

	@Override
	public boolean addOrderException(String order_id, List<OrderExceptionParam> orderExceptionArray,
			List<OrderRefundParam> orderRundArray) throws Exception {
		String urlStr = OrderURL.order_exception_url;

		String exception = JsonUtil.objectToStr(orderExceptionArray);
		String refund = JsonUtil.objectToStr(orderRundArray);

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", order_id);
		paramMap.put("exception", exception);
		paramMap.put("refund", refund);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<SaleSkuPriceUpdateResultBean> updateSkuPriceResult(BigDecimal task_id) throws Exception {
		String urlStr = OrderURL.update_sku_price_result_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("task_id", String.valueOf(task_id));

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SaleSkuPriceUpdateResultBean.class)
				: null;
	}

	@Override
	public OrderAnalysisBean orderDetailSalesAnalysis(OrderFilterParam paramBean) throws Exception {
		String urlStr = OrderURL.order_detail_sales_analysis_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramBean);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OrderAnalysisBean.class)
				: null;
	}

	@Override
	public List<String> getWeightRemarks(WeightRemarkFilterParam weightRemarkFilterParam) throws Exception {
		String url = OrderURL.get_sorting_remark_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, weightRemarkFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), String.class)
				: null;
	}

	@Override
	public List<String> getSortingRemarks(String time_config_id, String cycle_start_time, String cycle_end_time)
			throws Exception {
		String url = OrderURL.get_sorting_remark_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("query_type", "2");
		paramMap.put("time_config_id", time_config_id);
		paramMap.put("cycle_start_time", cycle_start_time);
		paramMap.put("cycle_end_time", cycle_end_time);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), String.class)
				: null;
	}

	@Override
	public List<RecentOrderBean> getRecentOrders(String address_id, String time_config_id) throws Exception {
		String url = OrderURL.get_recent_order_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("address_id", address_id);
		paramMap.put("time_config_id", time_config_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), RecentOrderBean.class)
				: null;
	}

	@Override
	public List<OrderSkuCopyBean> copyOrder(String order_id) throws Exception {
		String url = OrderURL.copy_order_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OrderSkuCopyBean.class)
				: null;
	}

	@Override
	public String exportOrderTemplate(String time_config_id, String file_name) throws Exception {
		String url = OrderURL.export_order_template_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("time_config_id", time_config_id);

		String file_path = baseRequest.baseExport(url, RequestType.GET, paramMap, file_name);
		return file_path;
	}

	@Override
	public OrderBatchUploadResultBean orderBatchUpload(String time_config_id, String file_path, String template_id)
			throws Exception {
		String url = OrderURL.order_batch_upload_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("time_config_id", time_config_id);
		paramMap.put("id", template_id);

		JSONObject retObj = baseRequest.baseUploadRequest(url, paramMap, "file", file_path);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OrderBatchUploadResultBean.class)
				: null;
	}

	@Override
	public String downloadOrderTemplate(String sid, String time_config_id) throws Exception {
		String url = OrderURL.order_import_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("sid", sid);
		paramMap.put("time_config_id", time_config_id);

		String file_path = baseRequest.baseExport(url, RequestType.GET, paramMap, "temp.xlsx");
		return file_path;
	}

	@Override
	public List<OrderImportResultBean> importOrder(String sid, String time_config_id, String file_path)
			throws Exception {
		String url = OrderURL.order_import_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("sid", sid);
		paramMap.put("time_config_id", time_config_id);

		JSONObject retObj = baseRequest.baseUploadRequest(url, paramMap, "file", file_path);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OrderImportResultBean.class)
				: null;
	}

	@Override
	public String orderBatchSubmite(OrderBatchCreateParam orderBatchCreateParam) throws Exception {
		String url = OrderURL.order_batch_submit_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, orderBatchCreateParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("task_url").split("=")[1] : null;
	}

	@Override
	public OrderBatchResultBean getOrderBatchResult(String task_id) throws Exception {
		String url = OrderURL.order_batch_result_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("task_id", task_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OrderBatchResultBean.class)
				: null;
	}

	@Override
	public boolean orderPriceSyncToSku(String order_id) throws Exception {
		String url = OrderURL.order_price_sync_to_sku_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public BigDecimal batchOrderPriceSyncToSku(List<OrderAndSkuBean> skuArray) throws Exception {
		String url = OrderURL.batch_order_price_sync_to_sku_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("sku_data", JsonUtil.objectToStr(skuArray));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}

	@Override
	public BigDecimal batchOrderPriceSyncToSku(OrderSkuFilterParam orderSkuFilterParam) throws Exception {
		String url = OrderURL.batch_order_price_sync_to_sku_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, orderSkuFilterParam);

		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}

	@Override
	public List<OrderPriceSyncToSkuResultBean> batchOrderPriceSyncToSkuResult(BigDecimal task_id) throws Exception {
		String url = OrderURL.batch_order_price_sync_to_sku_result_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("task_id", task_id.toString());

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OrderPriceSyncToSkuResultBean.class)
				: null;
	}

	@Override
	public List<AddressLabelBean> getAddressLabels() throws Exception {
		String url = OrderURL.address_label_list_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("limit", "1000");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), AddressLabelBean.class)
				: null;
	}

	@Override
	public List<String> checkOrderSku(String time_config_id, String address_id, List<String> sku_ids) throws Exception {
		String url = OrderURL.order_sku_check_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("time_config_id", time_config_id);
		paramMap.put("address_id", address_id.replaceAll("S0*", ""));
		paramMap.put("sku_ids", JSONArray.toJSONString(sku_ids));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		List<String> valid_sku_ids = null;
		if (retObj.getInteger("code") == 0) {
			valid_sku_ids = new ArrayList<String>();
			JSONArray temp_sku_ids = retObj.getJSONObject("data").getJSONArray("valid_sku_ids");
			for (Object obj : temp_sku_ids) {
				valid_sku_ids.add(String.valueOf(obj));
			}
		}
		return valid_sku_ids;
	}

	@Override
	public List<String> searchOrderChangeSku(String salemenu_id, String search_text) throws Exception {
		String url = OrderURL.order_change_sku_search_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("salemenu_id", salemenu_id);
		paramMap.put("search_text", search_text);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		List<String> sku_ids = null;
		if (retObj.getInteger("code") == 0) {
			sku_ids = new ArrayList<String>();
			JSONArray dataArry = retObj.getJSONArray("data");
			for (Object obj : dataArry) {
				sku_ids.add(JSONObject.parseObject(obj.toString()).getString("sku_id"));
			}
		}

		return sku_ids;

	}

	@Override
	public BigDecimal orderChangeSkus(List<OrderChangeSkuParam> orderChangeSkuParams) throws Exception {
		String url = OrderURL.order_change_sku_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("change_details", JsonUtil.objectToStr(orderChangeSkuParams));
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}

	@Override
	public BigDecimal orderDeleteSkus(List<OrderDeleteSkuParam> orderDeleteSkuParams) throws Exception {
		String url = OrderURL.order_delete_sku_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("delete_details", JsonUtil.objectToStr(orderDeleteSkuParams));
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}

	@Override
	public BigDecimal orderDeleteSkus(OrderSkuFilterParam orderSkuFilterParam) throws Exception {
		String url = OrderURL.order_delete_sku_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, orderSkuFilterParam);

		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}

	@Override
	public List<OrderDeleteSkuResultBean> getOrderDeleteSkuResults(BigDecimal task_id) throws Exception {
		String url = OrderURL.order_delete_sku_result_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("task_id", String.valueOf(task_id));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OrderDeleteSkuResultBean.class)
				: null;
	}

	@Override
	public List<OrderChangeSkuResultBean> getOrderChangeSkuResults(BigDecimal task_id) throws Exception {
		String url = OrderURL.order_change_sku_result_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("task_id", String.valueOf(task_id));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OrderChangeSkuResultBean.class)
				: null;
	}

	@Override
	public List<OrderProcessBean> getOrderProcessList() throws Exception {
		String url = OrderURL.order_process_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OrderProcessBean.class)
				: null;
	}

}
