package cn.guanmai.station.tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.Reporter;

import cn.guanmai.station.bean.order.CustomerBean;
import cn.guanmai.station.bean.order.OrderCycle;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderReceiveTimeBean;
import cn.guanmai.station.bean.order.OrderResponseBean;
import cn.guanmai.station.bean.order.OrderResponseBean.Data.NotEnoughInventories;
import cn.guanmai.station.bean.order.param.OrderCreateParam;
import cn.guanmai.station.bean.order.param.OrderEditParam;
import cn.guanmai.station.bean.order.param.OrderEditParam.OrderData;
import cn.guanmai.station.bean.system.ServiceTimeBean;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.ServiceTimeServiceImpl;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.ServiceTimeService;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Mar 1, 2019 10:28:51 AM 
* @des 订单工具类
* @version 1.0 
*/
public class OrderTool {
	private OrderService orderService;
	private OrderCreateParam orderCreateParam;
	private ServiceTimeService serviceTimeService;
	private String order_id;

	public OrderTool(Map<String, String> headers) {
		orderService = new OrderServiceImpl(headers);
		serviceTimeService = new ServiceTimeServiceImpl(headers);
	}

	/**
	 * 创建订单,使用指定商户下单
	 * 
	 * @param skuCount 最大下单商品数
	 * @return
	 * @throws Exception
	 */
	public String oneStepCreateOrder(String bshop_user_name, int skuCount) throws Exception {
		Reporter.log("开始进行下单操作");
		List<CustomerBean> customerArray = orderService.getOrderCustomerArray(6);
		Assert.assertNotEquals(customerArray, null, "获取下单商户列表信息失败");

		Assert.assertEquals(customerArray.size() > 0, true, "无可用下单商户");

		// 随机选取一个正常商户进行下单
		CustomerBean customer = customerArray.stream().filter(c -> c.getUsername().equals(bshop_user_name)).findAny()
				.orElse(null);
		Assert.assertNotEquals(customer, null, "商户名为 " + bshop_user_name + " 商户没有找到");

		String address_id = customer.getAddress_id();
		String uid = customer.getId();

		List<OrderReceiveTimeBean> orderReceiveTimes = orderService.getCustomerServiceTimeArray(address_id);
		Assert.assertNotEquals(orderReceiveTimes, null, "获取下单商户对应的运营时间失败");

		Assert.assertEquals(orderReceiveTimes.size() > 0, true, "商户" + address_id + "没有绑定运营时间,无法进行下单操作");
		// 随机取一个绑定的运营时间
		OrderReceiveTimeBean orderReceiveTime = NumberUtil.roundNumberInList(orderReceiveTimes);
		String time_config_id = orderReceiveTime.getTime_config_id();

		Assert.assertEquals(orderReceiveTime.getReceive_times().size() > 0, true,
				"受收货自然日限制,运营时间" + time_config_id + "无可用收货日期可选");

		// 下单商品集合
		String[] search_texts = new String[] { "A", "B", "C" };
		orderCreateParam = orderService.searchOrderSkus(address_id, time_config_id, search_texts, 8);
		Assert.assertEquals(orderCreateParam != null && orderCreateParam.getDetails().size() > 0, true, "下单搜索搜商品列表为空");

		List<OrderReceiveTimeBean.ReceiveTime> receiveTimes = orderReceiveTime.getReceive_times().stream()
				.filter(r -> r.getTimes().size() >= 2).collect(Collectors.toList());
		Assert.assertEquals(receiveTimes.size() > 0, true, "运营时间的收货时间无法取值了(当前时间过了收货时间结束时间或者收货起始和收货结束时间一致)");

		OrderReceiveTimeBean.ReceiveTime receiveTime = NumberUtil.roundNumberInList(receiveTimes);
		List<String> receive_times = receiveTime.getTimes();
		int index = new Random().nextInt(receive_times.size() - 1);
		String receive_begin_time = receive_times.get(index);
		String receive_end_time = receive_times.get(index + 1);

		// 构造下单对象
		orderCreateParam.setAddress_id(address_id);
		orderCreateParam.setUid(uid);
		orderCreateParam.setReceive_begin_time(receive_begin_time);
		orderCreateParam.setReceive_end_time(receive_end_time);
		orderCreateParam.setTime_config_id(time_config_id);
		orderCreateParam.setRemark(StringUtil.getRandomString(6));
		orderCreateParam.setForce(1);

		// 开始下单
		OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);
		if (orderResponse.getCode() == 0) {
			// 判断下单商品中有无库存不足的商品,有就移除后再下单
			List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData().getNot_enough_inventories();
			if (notEnoughInventoriesList.size() > 0) {
				Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam.getCombine_goods_map();
				List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();
				for (NotEnoughInventories skuObj : notEnoughInventoriesList) {
					String sku_id = skuObj.getSku_id();
					// 首先找出这个SKU有没有在组合商品里
					List<String> combine_goods_ids = orderSkus.stream()
							.filter(s -> s.getSku_id().equals(sku_id) && s.isIs_combine_goods())
							.map(s -> s.getCombine_goods_id()).distinct().collect(Collectors.toList());

					// 先把这个商品过滤掉
					orderSkus = orderSkus.stream().filter(s -> !s.getSku_id().equals(sku_id))
							.collect(Collectors.toList());

					// 过滤掉组合商品里的其他商品
					for (String combine_goods_id : combine_goods_ids) {
						combine_goods_map.remove(combine_goods_id);
						orderSkus = orderSkus.stream().filter(
								s -> s.isIs_combine_goods() && !s.getCombine_goods_id().equals(combine_goods_id))
								.collect(Collectors.toList());
					}
				}
				Assert.assertEquals(orderSkus.size() > 0, true, "下单商品列表为空,无法进行下单");
				orderCreateParam.setDetails(orderSkus);
				orderResponse = orderService.createOrder(orderCreateParam);
			}
			Assert.assertEquals(orderResponse.getData().getNew_order_ids().size() > 0, true, "正常下单,断言成功");
			order_id = orderResponse.getData().getNew_order_ids().getString(0);
		}
		return order_id;
	}

	/**
	 * 创建订单,随机取一个商户下单
	 * 
	 * @param skuCount 最大下单商品数
	 * @return
	 * @throws Exception
	 */
	public String oneStepCreateOrder(int skuCount) throws Exception {
		Reporter.log("开始进行下单操作");
		List<CustomerBean> customerArray = orderService.getOrderCustomerArray(6);
		Assert.assertNotEquals(customerArray, null, "获取下单商户列表信息失败");

		Assert.assertEquals(customerArray.size() > 0, true, "无可用下单商户");

		// 随机选取一个正常商户进行下单
		CustomerBean customer = NumberUtil.roundNumberInList(customerArray);

		String address_id = customer.getAddress_id();
		String uid = customer.getId();

		List<OrderReceiveTimeBean> orderReceiveTimes = orderService.getCustomerServiceTimeArray(address_id);
		Assert.assertNotEquals(orderReceiveTimes, null, "获取下单商户对应的运营时间失败");

		Assert.assertEquals(orderReceiveTimes.size() > 0, true, "商户" + address_id + "没有绑定运营时间,无法进行下单操作");
		// 随机取一个绑定的运营时间
		OrderReceiveTimeBean orderReceiveTime = NumberUtil.roundNumberInList(orderReceiveTimes);
		String time_config_id = orderReceiveTime.getTime_config_id();

		Assert.assertEquals(orderReceiveTime.getReceive_times().size() > 0, true,
				"受收货自然日限制,运营时间" + time_config_id + "无可用收货日期可选");

		// 下单商品集合
		String[] search_texts = new String[] { "A", "B", "C" };
		orderCreateParam = orderService.searchOrderSkus(address_id, time_config_id, search_texts, skuCount);
		Assert.assertEquals(orderCreateParam != null && orderCreateParam.getDetails().size() > 0, true, "下单搜索搜商品列表为空");

		List<OrderReceiveTimeBean.ReceiveTime> receiveTimes = orderReceiveTime.getReceive_times().stream()
				.filter(r -> r.getTimes().size() >= 2).collect(Collectors.toList());
		Assert.assertEquals(receiveTimes.size() > 0, true, "运营时间的收货时间无法取值了(当前时间过了收货时间结束时间或者收货起始和收货结束时间一致)");

		OrderReceiveTimeBean.ReceiveTime receiveTime = NumberUtil.roundNumberInList(receiveTimes);
		List<String> receive_times = receiveTime.getTimes();
		int index = new Random().nextInt(receive_times.size() - 1);
		String receive_begin_time = receive_times.get(index);
		String receive_end_time = receive_times.get(index + 1);

		// 构造下单对象
		orderCreateParam.setAddress_id(address_id);
		orderCreateParam.setUid(uid);
		orderCreateParam.setReceive_begin_time(receive_begin_time);
		orderCreateParam.setReceive_end_time(receive_end_time);
		orderCreateParam.setTime_config_id(time_config_id);
		orderCreateParam.setRemark(StringUtil.getRandomString(6));
		orderCreateParam.setForce(1);

		// 开始下单
		OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);
		if (orderResponse.getCode() == 0) {
			// 判断下单商品中有无库存不足的商品,有就移除后再下单
			List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData().getNot_enough_inventories();
			if (notEnoughInventoriesList.size() > 0) {
				Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam.getCombine_goods_map();
				List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();
				for (NotEnoughInventories skuObj : notEnoughInventoriesList) {
					String sku_id = skuObj.getSku_id();
					// 首先找出这个SKU有没有在组合商品里
					List<String> combine_goods_ids = orderSkus.stream()
							.filter(s -> s.getSku_id().equals(sku_id) && s.isIs_combine_goods())
							.map(s -> s.getCombine_goods_id()).distinct().collect(Collectors.toList());

					// 先把这个商品过滤掉
					orderSkus = orderSkus.stream().filter(s -> !s.getSku_id().equals(sku_id))
							.collect(Collectors.toList());

					// 过滤掉组合商品里的其他商品
					for (String combine_goods_id : combine_goods_ids) {
						combine_goods_map.remove(combine_goods_id);
						orderSkus = orderSkus.stream().filter(
								s -> s.isIs_combine_goods() && !s.getCombine_goods_id().equals(combine_goods_id))
								.collect(Collectors.toList());
					}
				}
				Assert.assertEquals(orderSkus.size() > 0, true, "下单商品列表为空,无法进行下单");
				orderCreateParam.setDetails(orderSkus);
				orderResponse = orderService.createOrder(orderCreateParam);
			}
			Assert.assertEquals(orderResponse.getData().getNew_order_ids().size() > 0, true, "正常下单,断言成功");
			order_id = orderResponse.getData().getNew_order_ids().getString(0);
		} else {
			Assert.fail("下单失败了, " + orderResponse.getMsg());
		}
		return order_id;
	}

	/**
	 * 获取订单所处运营周期
	 * 
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	public OrderCycle getOrderOperationCycle(String order_id) throws Exception {
		String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
		OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
		Assert.assertNotEquals(orderDetail, null, "获取订单详细信息失败");

		// 获取订单的收货时间 年-月-日
		String receive_begin_time = orderDetail.getCustomer().getReceive_begin_time();
		receive_begin_time = receive_begin_time.split(" ")[0];

		String time_config_id = orderDetail.getTime_config_id();
		ServiceTimeBean serviceTime = serviceTimeService.getServiceTimeById(time_config_id);
		Assert.assertNotEquals(serviceTime, null, "获取运营时间详细信息失败");

		String cycle_start_time = null;
		String cycle_end_time = null;
		String start = null;
		String end = null;
		// 普通运营时间按下单周期搜索,预售运营时间按收货周期搜索
		if (serviceTime.getType() == 1) {
			String current_time_str = TimeUtil.getCurrentTime("HH:mm");
			Date current_time = new SimpleDateFormat("HH:mm").parse(current_time_str);
			start = serviceTime.getOrder_time_limit().getStart();
			Date start_time = new SimpleDateFormat("HH:mm").parse(start);

			String start_day = todayStr;
			if (current_time.compareTo(start_time) < 0) {
				start_day = TimeUtil.calculateTime("yyyy-MM-dd", todayStr, -1, Calendar.DATE);
			}
			cycle_start_time = start_day + " " + start;
			end = serviceTime.getOrder_time_limit().getEnd();
			int span = serviceTime.getOrder_time_limit().getE_span_time();

			cycle_end_time = TimeUtil.calculateTime("yyyy-MM-dd", start_day, span, Calendar.DAY_OF_YEAR) + " " + end;
		} else {
			// 收货开始时间 时:分
			start = serviceTime.getReceive_time_limit().getStart();

			// 收货结束时间 时:分
			end = serviceTime.getReceive_time_limit().getEnd();

			int span = serviceTime.getReceive_time_limit().getReceiveEndSpan();

			String receive_end_time = TimeUtil.calculateTime("yyyy-MM-dd", receive_begin_time, span,
					Calendar.DAY_OF_YEAR);

			cycle_start_time = receive_begin_time + " " + start;
			cycle_end_time = receive_end_time + " " + end;
		}

		OrderCycle orderCycle = new OrderCycle();
		orderCycle.setTime_config_id(time_config_id);
		orderCycle.setCycle_start_time(cycle_start_time);
		orderCycle.setCycle_end_time(cycle_end_time);
		orderCycle.setType(serviceTime.getType());
		return orderCycle;
	}

	/**
	 * 修改订单
	 * 
	 * @param orderDetail
	 * @return
	 * @throws Exception
	 */
	public boolean updateOrder(OrderDetailBean orderDetail) throws Exception {
		OrderEditParam editOrder = new OrderEditParam();
		editOrder.setOrder_id(orderDetail.getId());

		// 封装商品
		List<OrderCreateParam.OrderSku> orderSkus = new ArrayList<>();
		List<OrderDetailBean.Detail> details = orderDetail.getDetails();
		OrderCreateParam.OrderSku orderSku = null;
		for (OrderDetailBean.Detail detail : details) {
			orderSku = new OrderCreateParam().new OrderSku();
			orderSku.setIs_price_timing(detail.isIs_price_timing() ? 1 : 0);
			orderSku.setAmount(detail.getQuantity());
			orderSku.setSku_id(detail.getSku_id());
			orderSku.setSpu_id(detail.getSpu_id());
			orderSku.setSpu_remark(detail.getSpu_remark());
			orderSku.setUnit_price(detail.getSale_price());
			orderSkus.add(orderSku);
		}
		editOrder.setDetails(orderSkus);

		// 设置收货时间
		OrderData orderData = editOrder.new OrderData();
		orderData.setReceive_begin_time(orderDetail.getCustomer().getReceive_begin_time());
		orderData.setReceive_end_time(orderDetail.getCustomer().getReceive_end_time());
		orderData.setRemark(orderDetail.getRemark());
		editOrder.setOrder_data(orderData);

		return orderService.editOrder(editOrder);
	}

}
