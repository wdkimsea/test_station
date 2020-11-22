package cn.guanmai.station.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.order.CustomerBean;
import cn.guanmai.station.bean.order.OrderBean;
import cn.guanmai.station.bean.order.OrderCycle;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderDetailBean.Detail;
import cn.guanmai.station.bean.order.OrderReceiveTimeBean;
import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.invoicing.SpuStockBean;
import cn.guanmai.station.bean.order.OrderResponseBean;
import cn.guanmai.station.bean.order.OrderSkuCopyBean;
import cn.guanmai.station.bean.order.OrderResponseBean.Data.NotEnoughInventories;
import cn.guanmai.station.bean.order.OrderSkuRecognizeBean;
import cn.guanmai.station.bean.order.RecentOrderBean;
import cn.guanmai.station.bean.order.param.OrderCreateParam;
import cn.guanmai.station.bean.order.param.OrderFilterParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.bean.system.ServiceTimeLimitBean;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.invoicing.StockCheckServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.impl.system.ServiceTimeServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.invoicing.StockCheckService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.interfaces.system.ServiceTimeService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Nov 12, 2018 11:08:55 AM 
* @des 创建订单测试
* @version 1.0 
*/
public class OrderCreateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(OrderCreateTest.class);
	private OrderService orderService;
	private OrderTool orderTool;
	private OrderCreateParam orderCreateParam;
	private CategoryService categoryService;
	private StockCheckService stockCheckService;
	private ServiceTimeService serviceTimeService;
	private LoginUserInfoService loginUserInfoService;
	private String address_id;
	private String time_config_id;
	private String todayStr;

	@BeforeClass
	public void initData() {
		todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
		Map<String, String> headers = getStationCookie();
		orderService = new OrderServiceImpl(headers);
		orderTool = new OrderTool(headers);
		categoryService = new CategoryServiceImpl(headers);
		stockCheckService = new StockCheckServiceImpl(headers);
		serviceTimeService = new ServiceTimeServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			List<CustomerBean> customerArray = orderService.getOrderCustomerArray(6);
			Assert.assertNotEquals(customerArray, null, "获取下单商户列表信息失败");

			Assert.assertEquals(customerArray.size() > 0, true, "无可用下单商户");

			// 随机选取一个正常商户进行下单
			CustomerBean customer = NumberUtil.roundNumberInList(customerArray);
			address_id = customer.getAddress_id();
			String uid = customer.getId();

			List<OrderReceiveTimeBean> orderReceiveTimes = orderService.getCustomerServiceTimeArray(address_id);
			Assert.assertNotEquals(orderReceiveTimes, null, "获取下单商户对应的运营时间失败");

			Assert.assertEquals(orderReceiveTimes.size() > 0, true, "商户" + address_id + "没有绑定运营时间,无法进行下单操作");
			// 随机取一个绑定的运营时间
			OrderReceiveTimeBean orderReceiveTime = NumberUtil.roundNumberInList(orderReceiveTimes);
			time_config_id = orderReceiveTime.getTime_config_id();

			Assert.assertEquals(orderReceiveTime.getReceive_times().size() > 0, true,
					"受收货自然日限制,运营时间" + time_config_id + "无可用收货日期可选");

			// 下单商品集合
			String[] search_texts = new String[] { "A", "B", "C"};
			orderCreateParam = orderService.searchOrderSkus(address_id, time_config_id, search_texts, 10);
			Assert.assertEquals(orderCreateParam != null && orderCreateParam.getDetails().size() > 0, true,
					"下单搜索搜商品列表为空");

			List<OrderReceiveTimeBean.ReceiveTime> receiveTimes = orderReceiveTime.getReceive_times().stream()
					.filter(r -> r.getTimes().size() >= 2).collect(Collectors.toList());
			Assert.assertEquals(receiveTimes.size() > 0, true, "运营时间的收货时间无法取值了(当前时间过了收货时间结束时间或者收货起始和收货结束时间一致)");

			OrderReceiveTimeBean.ReceiveTime receiveTime = NumberUtil.roundNumberInList(receiveTimes);
			List<String> receive_times = receiveTime.getTimes();
			int index = new Random().nextInt(receive_times.size() - 1);
			String receive_begin_time = receive_times.get(index);
			String receive_end_time = receive_times.get(index + 1);

			// 下单对象
			orderCreateParam.setAddress_id(address_id);
			orderCreateParam.setUid(uid);
			orderCreateParam.setReceive_begin_time(receive_begin_time);
			orderCreateParam.setReceive_end_time(receive_end_time);
			orderCreateParam.setTime_config_id(time_config_id);
			orderCreateParam.setRemark(StringUtil.getRandomString(6));
			orderCreateParam.setForce(1);
		} catch (Exception e) {
			logger.error("下单前的准备工作遇到错误: ", e);
			Assert.fail("下单前的准备工作遇到错误: ", e);
		}

	}

	/**
	 * 
	 * 创建订单测试
	 * 
	 */
	@Test
	public void orderCreateTestCase01() {
		try {
			ReporterCSS.title("测试点: 正常下单,并验证下单商品是否有漏记");
			// 开始下单
			String order_id = null;
			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);
			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				if (notEnoughInventoriesList.size() > 0) {
					Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam
							.getCombine_goods_map();
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
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}

			OrderDetailBean orderDetailInfo = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetailInfo, null, "获取订单详细信息失败");

			List<OrderDetailBean.Detail> details = orderDetailInfo.getDetails();
			boolean success = true;
			for (OrderCreateParam.OrderSku orderSku : orderCreateParam.getDetails()) {
				OrderDetailBean.Detail tempDetail = details.stream()
						.filter(d -> d.getSku_id().equals(orderSku.getSku_id())).findAny().orElse(null);
				if (tempDetail == null) {
					ReporterCSS.warn("输入的下单商品 " + orderSku.getSku_id() + " 在订单中没有找到");
					success = false;
				}
			}
			Assert.assertEquals(success, true, "下单存在漏记商品");
		} catch (Exception e) {
			logger.error("下单过程中遇到错误: ", e);
			Assert.fail("下单过程中遇到错误: ", e);
		}
	}

	/**
	 * 正常下单,修改商品单价,不采用商品默认价格
	 * 
	 */
	@Test
	public void orderCreateTestCase02() {
		try {
			String order_id = null;
			ReporterCSS.title("测试点: 正常下单,修改商品单价,不采用商品默认价格");
			List<OrderCreateParam.OrderSku> temp_sku_array = new ArrayList<OrderCreateParam.OrderSku>();

			Map<String, BigDecimal> newPriceMap = new HashMap<String, BigDecimal>();
			for (OrderCreateParam.OrderSku orderSku : orderCreateParam.getDetails()) {
				String sku_id = orderSku.getSku_id();
				BigDecimal newPrice = null;
				if (newPriceMap.containsKey(sku_id)) {
					newPrice = newPriceMap.get(sku_id);
				} else {
					newPrice = orderSku.getUnit_price().add(NumberUtil.getRandomNumber(1, 3, 1));
					newPriceMap.put(sku_id, newPrice);
				}
				orderSku.setUnit_price(newPrice);
				orderSku.setIs_price_timing(0);
				temp_sku_array.add(orderSku);
			}
			orderCreateParam.setDetails(temp_sku_array);

			// 开始下单
			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);
			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				if (notEnoughInventoriesList.size() > 0) {
					Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam
							.getCombine_goods_map();
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
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}

			OrderDetailBean orderDetailInfo = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetailInfo, null, "获取订单详细信息失败");

			List<OrderDetailBean.Detail> details = orderDetailInfo.getDetails();

			boolean result = true;
			String msg = null;

			for (OrderCreateParam.OrderSku orderSku : orderCreateParam.getDetails()) {
				BigDecimal amount = new BigDecimal("0");
				for (OrderCreateParam.OrderSku tempOrderSku : orderCreateParam.getDetails()) {
					if (orderSku.getSku_id().equals(tempOrderSku.getSku_id())) {
						amount = amount.add(tempOrderSku.getAmount());
					}
				}
				OrderDetailBean.Detail tempDetail = details.stream()
						.filter(d -> d.getSku_id().equals(orderSku.getSku_id())).findAny().orElse(null);
				if (tempDetail == null) {
					ReporterCSS.warn("输入的下单商品 " + orderSku.getSku_id() + " 在订单中没有找到");
					result = false;
				} else {
					String spu_id = tempDetail.getSpu_id();
					String sku_id = tempDetail.getSku_id();
					SkuBean sku = categoryService.getSaleSkuById(spu_id, sku_id);
					if (sku == null) {
						msg = String.format("获取商品销售SKU %s详细信息失败", sku_id);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}
					// 可能商品设置了下单取整
					if (sku.getRounding() != null) {
						if (sku.getRounding() == 1) {
							amount = amount.setScale(0, BigDecimal.ROUND_FLOOR);
						} else if (sku.getRounding() == 2) {
							amount = amount.setScale(0, BigDecimal.ROUND_DOWN);
						}
						if (amount.compareTo(tempDetail.getQuantity()) != 0) {
							msg = String.format("订单%s详细里商品%s设置了下单取整,但是下单数与预期不一致,预期:%s,实际:%s", order_id, sku_id, amount,
									tempDetail.getQuantity());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					} else {
						msg = String.format("订单%s详细里商品%s下单数与预期不一致,预期:%s,实际:%s", order_id, sku_id, amount,
								tempDetail.getQuantity());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "下单时刻填入的信息与订单创建后显示的信息不一致");
		} catch (Exception e) {
			logger.error("下单过程中遇到错误: ", e);
			Assert.fail("下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateTestCase03() {
		try {
			ReporterCSS.title("测试点: 正常下单,并进行合单操作");
			// 开始下单
			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);
			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				if (notEnoughInventoriesList.size() > 0) {
					Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam
							.getCombine_goods_map();
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

				String order_id = orderResponse.getData().getNew_order_ids().getString(0);

				OrderDetailBean beforeOrderDetail = orderService.getOrderDetailById(order_id);
				Assert.assertNotEquals(beforeOrderDetail, null, "获取订单 " + order_id + " 详细信息失败");

				// 再一次提交订单请求,进行合单操作
				orderCreateParam.setForce(2);
				orderResponse = orderService.createOrder(orderCreateParam);
				if (orderResponse.getCode() == 0) {
					Assert.assertEquals(orderResponse.getData().getNew_order_ids().size() > 0, true, "正常合单,断言成功");
					String temp_order_id = orderResponse.getData().getNew_order_ids().getString(0);
					Assert.assertEquals(temp_order_id, order_id, "合单失败,系统生成了新的订单");
				} else {
					Assert.assertEquals(orderResponse.getMsg(), "ok", "合单失败");
				}

				OrderDetailBean afterOrderDetail = orderService.getOrderDetailById(order_id);
				Assert.assertNotEquals(afterOrderDetail, null, "获取订单 " + order_id + " 详细信息失败");

				LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
				Assert.assertNotEquals(loginUserInfo, null, "获取订单用户信息失败");
				int order_can_have_duplicate_sku = loginUserInfo.getProfile().getOrder_can_have_duplicate_sku();
				String msg = null;
				boolean result = true;
				List<OrderDetailBean.Detail> beforeDetails = beforeOrderDetail.getDetails();
				if (order_can_have_duplicate_sku == 0) {
					for (OrderDetailBean.Detail afterDetail : afterOrderDetail.getDetails()) {
						String sku_id = afterDetail.getSku_id();
						OrderDetailBean.Detail beforeDetail = beforeDetails.stream()
								.filter(d -> d.getSku_id().equals(sku_id)).findAny().orElse(null);

						if (afterDetail.getQuantity()
								.compareTo(beforeDetail.getQuantity().multiply(new BigDecimal("2"))) != 0) {
							msg = String.format("订单 %s 合单后,商品 [%s,%s] 下单数与预期不符,预期:%s,实际:%s", order_id, sku_id,
									beforeDetail.getSku_name(),
									beforeDetail.getQuantity().multiply(new BigDecimal("2")),
									afterDetail.getQuantity());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					}
				} else {
					ReporterCSS.warn("注意: 站点开启了多SKU");
					List<OrderDetailBean.Detail> afterDetails = afterOrderDetail.getDetails();
					for (OrderDetailBean.Detail beforeDetail : beforeDetails) {
						String sku_id = beforeDetail.getSku_id();
						List<OrderDetailBean.Detail> tempAfterDetails = afterDetails.stream()
								.filter(d -> d.getSku_id().equals(sku_id)).collect(Collectors.toList());
						if (tempAfterDetails.size() != 2) {
							msg = String.format("订单%s里的商品%s在合单后,应该出现两次,实际出现了%s次", order_id, sku_id,
									tempAfterDetails.size());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
							continue;
						}

						for (OrderDetailBean.Detail tempAfterDetail : tempAfterDetails) {
							if (beforeDetail.getQuantity().compareTo(tempAfterDetail.getQuantity()) != 0) {
								msg = String.format("订单%s里的商品[%s,%s],下单数与预期的不一致,预期:%s,实际:%s", order_id, sku_id,
										tempAfterDetail.getDetail_id(), beforeDetail.getQuantity(),
										tempAfterDetail.getQuantity());
								ReporterCSS.warn(msg);
								logger.warn(msg);
								result = false;
							}
						}
					}
				}

				Assert.assertEquals(result, true, "订单 " + order_id + " 合单后,信息与预期不一致");
			} else {
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}
		} catch (Exception e) {
			logger.error("合单过程中遇到错误: ", e);
			Assert.fail("合单过程中遇到错误: ", e);
		}
	}

	/**
	 * 把订单状态都改为待分拣状态,再次下单,应该不存在合单操作
	 * 
	 */
	@Test
	public void orderCreateTestCase04() {
		try {
			ReporterCSS.title("测试点: 把订单状态都改为待分拣状态,再次下单,应该不存在合单操作");
			// 开始下单
			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);
			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				if (notEnoughInventoriesList.size() > 0) {
					Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam
							.getCombine_goods_map();
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
				String order_id = orderResponse.getData().getNew_order_ids().getString(0);
				OrderCycle orderCycle = orderTool.getOrderOperationCycle(order_id);

				OrderFilterParam orderFilterParam = new OrderFilterParam();
				orderFilterParam.setTime_config_id(orderCycle.getTime_config_id());
				orderFilterParam.setCycle_start_time(orderCycle.getCycle_start_time());
				orderFilterParam.setCycle_end_time(orderCycle.getCycle_end_time());
				orderFilterParam.setStatus(1);
				orderFilterParam.setQuery_type(2);
				orderFilterParam.setLimit(50);
				orderFilterParam.setOffset(0);
				address_id = String.format("S%6s", address_id).replace(" ", "0");
				orderFilterParam.setSearch_text(address_id);

				List<OrderBean> orderList = orderService.searchOrder(orderFilterParam);
				Assert.assertNotEquals(orderList, null, "搜索过滤订单列表失败");

				List<String> order_ids = orderList.stream().map(o -> o.getId()).collect(Collectors.toList());
				if (order_ids.size() > 0) {
					boolean result = orderService.updateOrderState(order_ids, 5, "批量更新");
					Assert.assertEquals(result, true, "更新选中的订单状态失败");
				}
				Thread.sleep(500);

				// 再一次提交订单请求,这个时候没有待分拣状态的订单,应该不存在合单操作
				orderCreateParam.setForce(null);
				orderResponse = orderService.createOrder(orderCreateParam);
				if (orderResponse.getCode() == 0) {
					Assert.assertEquals(orderResponse.getData().getNew_order_ids().size() > 0, true,
							"创建订单,之前下的订单状态已经改为分拣状态,不存在合单操作");
				} else {
					Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
				}
			} else {
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}
		} catch (Exception e) {
			logger.error("合单过程中遇到错误: ", e);
			Assert.fail("合单过程中遇到错误: ", e);
		}
	}

	/**
	 * 
	 * 创建订单,验证订单总额
	 * 
	 */
	@Test
	public void orderCreateTestCase05() {
		try {
			ReporterCSS.title("测试点: 创建订单后验证订单总额四舍五入是否正确");
			List<OrderCreateParam.OrderSku> temp_sku_array = new ArrayList<OrderCreateParam.OrderSku>();
			for (OrderCreateParam.OrderSku orderSku : orderCreateParam.getDetails()) {
				orderSku.setIs_price_timing(0);
				orderSku.setUnit_price(NumberUtil.getRandomNumber(7, 15, 2));
				orderSku.setAmount(NumberUtil.getRandomNumber(6, 10, 2));
				temp_sku_array.add(orderSku);
			}
			orderCreateParam.setDetails(temp_sku_array);

			// 开始下单
			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);

			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				if (notEnoughInventoriesList.size() > 0) {
					Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam
							.getCombine_goods_map();
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

				// 新建订单ID
				String order_id = orderResponse.getData().getNew_order_ids().getString(0);

				// 订单详细信息
				OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);

				// 后端计算的订单总额
				BigDecimal total_price = orderDetail.getTotal_price();

				// 计算下单商品总额
				BigDecimal actual_money = BigDecimal.ZERO;
				for (Detail detail : orderDetail.getDetails()) {
					actual_money = actual_money.add(detail.getReal_quantity().multiply(detail.getSale_price()));
				}
				// 计算的实际订单总额
				actual_money = actual_money.setScale(2, BigDecimal.ROUND_HALF_UP);
				boolean compare = actual_money.compareTo(total_price) == 0;
				Assert.assertEquals(compare, true,
						"订单 " + order_id + " 总金额和预期不符,预期:" + actual_money + ",实际: " + total_price);

			} else {
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}
		} catch (Exception e) {
			logger.error("下单过程中遇到错误: ", e);
			Assert.fail("下单过程中遇到错误: ", e);
		}
	}

	/**
	 * 状态为下架的商品,下单的时候搜索不出来
	 * 
	 */
	@Test
	public void orderCreateTestCase06() {
		String temp_sku_id = null;
		try {
			ReporterCSS.title("测试点: 状态为下架的商品,下单搜索商品的时候应该搜索不出来");

			// 把下单商品中的一个商品改为下架
			OrderCreateParam.OrderSku tempObj = orderCreateParam.getDetails().get(0);
			temp_sku_id = tempObj.getSku_id();
			boolean result = categoryService.updateSaleSkuStatus(temp_sku_id, false);
			Assert.assertEquals(result, true, "商品状态改为下架,断言成功");

			String[] search_texts = new String[] { temp_sku_id };
			OrderCreateParam tempOrderCreateParam = orderService.searchOrderSkus(address_id, time_config_id,
					search_texts, 10);
			boolean exist = false;
			for (OrderCreateParam.OrderSku skuObj : tempOrderCreateParam.getDetails()) {
				String sku_id = skuObj.getSku_id();
				if (sku_id.equals(temp_sku_id)) {
					exist = true;
					break;
				}
			}

			Assert.assertEquals(exist, false, "状态为下架的商品,下单搜索商品的时候应该搜索不出来");

		} catch (Exception e) {
			logger.error("下单过程中遇到错误: ", e);
			Assert.fail("下单过程中遇到错误: ", e);
		} finally {
			if (temp_sku_id != null) {
				try {
					boolean result = categoryService.updateSaleSkuStatus(temp_sku_id, true);
					Assert.assertEquals(result, true, "商品状态改为上架,断言成功");
				} catch (Exception e) {
					logger.error("修改销售商品的状态遇到错误: ", e);
					Assert.fail("修改销售商品的状态遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void orderCreateTestCase07() {
		ReporterCSS.title("测试点: 销售商品设置为固定库存,下单数大于固定库存,验证下单提醒");

		OrderCreateParam.OrderSku orderSku = orderCreateParam.getDetails().get(0);
		String spu_id = orderSku.getSpu_id();
		String sku_id = orderSku.getSku_id();
		SkuBean saleSku = null;
		try {
			saleSku = categoryService.getSaleSkuById(spu_id, sku_id);
			Assert.assertNotEquals(saleSku, null, "获取销售SKU详细信息失败");

			saleSku.setSalemenu_id(null);
			saleSku.setStock_type(2);
			saleSku.setStocks("1");

			boolean result = categoryService.updateSaleSku(saleSku);
			Assert.assertEquals(result, true, "销售SKU库存设置为固定库存设置失败");

			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);

			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				Assert.assertEquals(notEnoughInventoriesList.size() > 0, true, "下单操作应该存在商品库存不足提示");
			} else {
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}

		} catch (Exception e) {
			logger.error("下单过程中遇到错误: ", e);
			Assert.fail("下单过程中遇到错误: ", e);
		} finally {
			try {
				saleSku.setStock_type(1);
				saleSku.setStocks("-99999");
				boolean result = categoryService.updateSaleSku(saleSku);
				Assert.assertEquals(result, true, "销售SKU库存设置为不限制库存失败");
			} catch (Exception e) {
				logger.error("修改销售SKU信息遇到错误: ", e);
				Assert.fail("修改销售SKU信息遇到错误: ", e);
			}
		}

	}

	@Test
	public void orderCreateTestCase08() {
		ReporterCSS.title("测试点: 设置下单商品库存设置为读取库存数,且库存数小于下单数,验证下单提醒");

		OrderCreateParam.OrderSku orderSku = orderCreateParam.getDetails().get(0);
		String spu_id = orderSku.getSpu_id();
		String sku_id = orderSku.getSku_id();
		SkuBean saleSku = null;
		try {

			saleSku = categoryService.getSaleSkuById(spu_id, sku_id);
			Assert.assertNotEquals(saleSku, null, "获取销售SKU详细信息失败");

			// 限制库存,读取库存数
			saleSku.setSalemenu_id(null);
			saleSku.setStock_type(3);
			saleSku.setStocks("-99999");

			boolean result = categoryService.updateSaleSku(saleSku);
			Assert.assertEquals(result, true, "销售SKU库存设置为读取现库存失败");

			SpuStockBean spuStock = stockCheckService.getSpuStock(spu_id);
			Assert.assertNotEquals(spuStock, null, "库存盘点,获取SPU " + spu_id + " 库存相关信息失败");
			BigDecimal new_stock = spuStock.getRemain();
			if (new_stock.compareTo(new BigDecimal("0")) > 0) {
				orderSku.setAmount(new_stock.divide(saleSku.getSale_ratio(), 2, BigDecimal.ROUND_HALF_UP)
						.add(new BigDecimal("2")));
			}

			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);

			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				Assert.assertEquals(notEnoughInventoriesList.size() > 0, true, "下单操作应该存在商品库存不足提示");

			} else {
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}

		} catch (Exception e) {
			logger.error("下单过程中遇到错误: ", e);
			Assert.fail("下单过程中遇到错误: ", e);
		} finally {
			try {
				saleSku.setStock_type(1);
				saleSku.setStocks("-99999");
				boolean result = categoryService.updateSaleSku(saleSku);
				Assert.assertEquals(result, true, "销售SKU库存设置为不限制库存失败");
			} catch (Exception e) {
				logger.error("修改销售SKU信息遇到错误: ", e);
				Assert.fail("修改销售SKU信息遇到错误: ", e);
			}
		}
	}

	@Test
	public void orderCreateTestCase09() {
		ReporterCSS.title("测试点: 下单验证商品排序是否按照自定义排序");
		try {
			String order_id = null;
			// 下单SKU排序,按照SKU_ID反序
			List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails().stream()
					.sorted((s1, s2) -> s2.getSku_id().compareTo(s1.getSku_id())).collect(Collectors.toList());
			orderCreateParam.setDetails(orderSkus);
			// 开始下单
			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);
			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				if (notEnoughInventoriesList.size() > 0) {
					Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam
							.getCombine_goods_map();
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
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}

			OrderDetailBean orderDetailInfo = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetailInfo, null, "获取订单详细信息失败");

			List<String> expected_sort_sku = new ArrayList<String>();
			for (OrderCreateParam.OrderSku orderSku : orderSkus) {
				if (orderSku.isIs_combine_goods()) {
					String combine_goods_id = orderSku.getCombine_goods_id();
					if (!expected_sort_sku.contains(combine_goods_id)) {
						expected_sort_sku.add(combine_goods_id);
					}
				} else {
					expected_sort_sku.add(orderSku.getSku_id());
				}
			}

			// 订单中没有组合商品没有sort_skus
			List<String> actual_sort_sku = null;
			if (orderDetailInfo.getSort_skus() != null) {
				actual_sort_sku = orderDetailInfo.getSort_skus();
			} else {
				actual_sort_sku = orderDetailInfo.getDetails().stream().map(s -> s.getSku_id())
						.collect(Collectors.toList());
			}

			Assert.assertEquals(expected_sort_sku.toString().equals(actual_sort_sku.toString()), true,
					"订单 " + order_id + " 里的商品排序与预期不符,预期:" + expected_sort_sku + ",实际:" + actual_sort_sku);
		} catch (Exception e) {
			logger.error("下单过程中遇到错误: ", e);
			Assert.fail("下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateTestCase10() {
		ReporterCSS.title("测试点: 下单智能识别商品");
		try {
			String order_id = null;
			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);
			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				if (notEnoughInventoriesList.size() > 0) {
					Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam
							.getCombine_goods_map();
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
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详情信息失败");

			// 把SKU名称唯一的SKU商品进行下单智能识别
			StringJoiner recognition_text = new StringJoiner(",");
			for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
				recognition_text.add(detail.getSku_name() + " " + "10");
			}

			OrderSkuRecognizeBean orderSkuRecognize = orderService.recognizeSaleSku(address_id, time_config_id,
					recognition_text.toString());

			List<OrderSkuRecognizeBean.Vaild> vaildSkus = orderSkuRecognize.getVailds().stream().map(v -> v.get(0))
					.collect(Collectors.toList());

			boolean result = true;
			OrderSkuRecognizeBean.Vaild vaildSku = null;
			for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
				vaildSku = vaildSkus.stream().filter(v -> v.getSku_id().equals(detail.getSku_id())).findAny()
						.orElse(null);
				if (vaildSku == null) {
					ReporterCSS.warn("下单智能识别商品,输入 " + detail.getSku_name() + " 没有找到销售规格 " + detail.getSku_id());
					result = false;
				}
			}
			Assert.assertEquals(result, true, "下单智能识别商品结果与预期不符");
		} catch (Exception e) {
			logger.error("下单过程中遇到错误: ", e);
			Assert.fail("下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateTestCase11() {
		ReporterCSS.title("测试点: 复制订单,拉取指定运营时间下的近10条订单");
		try {
			List<RecentOrderBean> recentOrders = orderService.getRecentOrders(address_id, time_config_id);
			Assert.assertNotEquals(recentOrders, null, "复制订单,拉取指定运营时间下的近10条订单失败");

			String end_date = TimeUtil.getCurrentTime("yyyy-MM-dd");
			String start_date = TimeUtil.calculateTime("yyyy-MM-dd", end_date, -90, Calendar.DATE);

			// 订单列表查询订单
			OrderFilterParam paramBean = new OrderFilterParam();
			paramBean.setStart_date(start_date);
			paramBean.setEnd_date(end_date);
			paramBean.setQuery_type(1);
			paramBean.setOffset(0);
			paramBean.setLimit(50);
			paramBean.setSearch_text(address_id);

			List<OrderBean> orders = orderService.searchOrder(paramBean);
			Assert.assertNotEquals(orders, null, "订单列表过滤查询订单失败");

			orders = orders.stream().filter(o -> o.getTime_config_info().getId().equals(time_config_id))
					.collect(Collectors.toList());

			int order_length = orders.size() > recentOrders.size() ? recentOrders.size() : orders.size();
			List<String> expected_order_ids = orders.stream().map(o -> o.getId()).limit(order_length)
					.collect(Collectors.toList());

			List<String> actual_order_ids = recentOrders.stream().map(ro -> ro.getId()).limit(order_length)
					.collect(Collectors.toList());

			logger.info(expected_order_ids.toString());
			logger.info(actual_order_ids.toString());
			Assert.assertEquals(actual_order_ids, expected_order_ids, "复制订单,拉取指定运营时间下的近10条订单数据与预期不符");
		} catch (Exception e) {
			logger.error("下单过程中遇到错误: ", e);
			Assert.fail("下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateTestCase12() {
		ReporterCSS.title("测试点: 复制订单,复制指定订单");
		try {
			String order_id = null;
			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);
			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				if (notEnoughInventoriesList.size() > 0) {
					Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam
							.getCombine_goods_map();
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
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}

			List<OrderSkuCopyBean> orderSkuCopyList = orderService.copyOrder(order_id);
			Assert.assertNotEquals(orderSkuCopyList, null, "复制订单商品数据失败");

		} catch (Exception e) {
			logger.error("下单过程中遇到错误: ", e);
			Assert.fail("下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateTestCase13() {
		try {
			ReporterCSS.title("测试点: 正常下单,自定义收货地址");
			// 开始下单
			String order_id = null;
			String address = "深圳市-南山区-高新园-高新南四道";
			orderCreateParam.setAddress(address);
			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);
			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				if (notEnoughInventoriesList.size() > 0) {
					Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam
							.getCombine_goods_map();
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
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}

			OrderDetailBean orderDetailInfo = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetailInfo, null, "获取订单详细信息失败");

			Assert.assertEquals(orderDetailInfo.getCustomer().getAddress(), address, "创建订单,自定义收货地址,订单创建后收货地址与预期不符");

		} catch (Exception e) {
			logger.error("下单过程中遇到错误: ", e);
			Assert.fail("下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateTestCase14() {
		ReporterCSS.title("测试点: 订单商品价格同步销售SKU");
		try {
			// 价格有加有减
			BigDecimal unit_price = null;
			for (OrderCreateParam.OrderSku orderSku : orderCreateParam.getDetails()) {
				unit_price = orderSku.getUnit_price();
				if (unit_price.compareTo(new BigDecimal("4")) > 0) {
					unit_price = unit_price.subtract(new BigDecimal("1"));
				} else {
					unit_price = unit_price.add(new BigDecimal("1"));
				}
				orderSku.setIs_price_timing(0);
				orderSku.setUnit_price(unit_price);
			}

			String order_id = null;
			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);
			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				if (notEnoughInventoriesList.size() > 0) {
					Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam
							.getCombine_goods_map();
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
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}

			boolean result = orderService.orderPriceSyncToSku(order_id);
			Assert.assertEquals(result, true, "订单 " + order_id + "商品价格同步报价单失败");

			OrderCreateParam.OrderSku orderSku = null;
			String spu_id = null;
			String sku_id = null;
			BigDecimal sale_unit_price = null;
			SkuBean sku = null;
			String msg = null;
			List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();
			for (int i = 0; i < orderSkus.size(); i++) {
				orderSku = orderSkus.get(i);
				spu_id = orderSku.getSpu_id();
				sku_id = orderSku.getSku_id();
				sale_unit_price = orderSku.getUnit_price();
				sku = categoryService.getSaleSkuById(spu_id, sku_id);
				Assert.assertNotEquals(sku, null, "获取销售SKU " + sku_id + " 详细信息失败");
				if (sku.getSale_price().compareTo(sale_unit_price) != 0) {
					msg = String.format("订单%s里的商品%s价格没有同步成功到销售SKU,预期:%s,实际:%s", order_id, sku_id, sale_unit_price,
							sku.getSale_price());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单" + order_id + "中的商品价格没有同步到销售SKU");
		} catch (Exception e) {
			logger.error("订单商品价格同步销售SKU遇到错误: ", e);
			Assert.fail("订单商品价格同步销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateTestCase16() {
		try {
			ReporterCSS.title("测试点: 补录订单");
			// 开始下单
			String order_id = null;
			String time_config_id = orderCreateParam.getTime_config_id();
			ServiceTimeLimitBean serviceTimeLimit = serviceTimeService.getServiceTimeLimit(time_config_id);
			Assert.assertNotEquals(serviceTimeLimit, null, "获取运营时间 " + time_config_id + " 详细信息失败");
			String order_start_time = serviceTimeLimit.getOrder_start_time();
			String date_time = todayStr + " " + order_start_time + ":00";
			orderCreateParam.setDate_time(date_time);
			OrderResponseBean orderResponse = orderService.createOldOrder(orderCreateParam);
			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				if (notEnoughInventoriesList.size() > 0) {
					Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam
							.getCombine_goods_map();
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
					orderResponse = orderService.createOldOrder(orderCreateParam);
				}
				Assert.assertEquals(orderResponse.getData().getNew_order_ids().size() > 0, true, "正常下单,断言成功");
				order_id = orderResponse.getData().getNew_order_ids().getString(0);
			} else {
				Assert.assertEquals(orderResponse.getMsg(), "ok", "补录订单失败");
			}

			OrderDetailBean orderDetailInfo = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetailInfo, null, "获取订单详细信息失败");

			List<OrderDetailBean.Detail> details = orderDetailInfo.getDetails();
			boolean success = true;
			for (OrderCreateParam.OrderSku orderSku : orderCreateParam.getDetails()) {
				OrderDetailBean.Detail tempDetail = details.stream()
						.filter(d -> d.getSku_id().equals(orderSku.getSku_id())).findAny().orElse(null);
				if (tempDetail == null) {
					ReporterCSS.warn("输入的下单商品 " + orderSku.getSku_id() + " 在订单中没有找到");
					success = false;
				}
			}
			Assert.assertEquals(success, true, "下单存在漏记商品");
		} catch (Exception e) {
			logger.error("下单过程中遇到错误: ", e);
			Assert.fail("下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateTestCase17() {
		ReporterCSS.title("测试点: 随机输入几个商品ID,搜索过滤不应该报错");
		try {
			String id_1 = "D" + StringUtil.getRandomNumber(6);
			String id_2 = "D" + StringUtil.getRandomNumber(7);
			String[] search_texts = new String[] { id_1, id_2 };

			orderCreateParam = orderService.searchOrderSkus(address_id, time_config_id, search_texts, 10);
			Assert.assertEquals(orderCreateParam != null, true, "下单搜索搜商品列表报错");
		} catch (Exception e) {
			logger.error("下单过程中遇到错误: ", e);
			Assert.fail("下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateTestCase18() {
		ReporterCSS.title("测试点: 下单输入预下单数与实际下单数不一致");
		try {
			Map<String, BigDecimal> fake_quntity_map = new HashMap<String, BigDecimal>();

			List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();
			// 改变下单参数中的 fake_quantity 对应的值(排除组合商品)
			BigDecimal fake_quantity = null;
			for (OrderCreateParam.OrderSku orderSku : orderSkus) {
				String sku_id = orderSku.getSku_id();
				if (orderSku.isIs_combine_goods()) {
					fake_quantity = orderSku.getAmount();
					orderSku.setFake_quantity(fake_quantity);
				} else {
					fake_quantity = orderSku.getAmount().multiply(new BigDecimal("2"));
					orderSku.setFake_quantity(fake_quantity);
				}
				if (fake_quntity_map.containsKey(sku_id)) {
					BigDecimal temp_fake_quntity = fake_quntity_map.get(sku_id);
					fake_quntity_map.put(sku_id, temp_fake_quntity.add(fake_quantity));
				} else {
					fake_quntity_map.put(sku_id, fake_quantity);
				}
			}

			String order_id = null;
			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);
			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				if (notEnoughInventoriesList.size() > 0) {
					Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam
							.getCombine_goods_map();
					orderSkus = orderCreateParam.getDetails();
					for (NotEnoughInventories skuObj : notEnoughInventoriesList) {
						String sku_id = skuObj.getSku_id();
						fake_quntity_map.remove(sku_id);

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
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}

			OrderDetailBean orderDetailInfo = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetailInfo, null, "获取订单详细信息失败");

			List<OrderDetailBean.Detail> details = orderDetailInfo.getDetails();
			boolean result = true;

			String msg = null;
			BigDecimal expected_fake_quantity = null;
			BigDecimal actual_fake_quantity = null;
			for (OrderDetailBean.Detail detail : details) {
				String sku_id = detail.getSku_id();
				expected_fake_quantity = fake_quntity_map.get(sku_id);
				actual_fake_quantity = detail.getFake_quantity();
				if (expected_fake_quantity.compareTo(actual_fake_quantity) != 0) {
					msg = String.format("订单%s里的商品%s预下单数与预期不一致,预期:%s,实际:%s", order_id, sku_id, expected_fake_quantity,
							actual_fake_quantity);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单" + order_id + "里的商品预下单数与预期不一致");
		} catch (Exception e) {
			logger.error("下单过程中遇到错误: ", e);
			Assert.fail("下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateTestCase19() {
		ReporterCSS.title("测试点: 下单添加订单备注");
		try {
			String order_id = null;
			orderCreateParam.setRemark("这个是订单备注");
			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);
			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				if (notEnoughInventoriesList.size() > 0) {
					Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam
							.getCombine_goods_map();
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
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + "详细信息失败");

			Assert.assertEquals(orderDetail.getRemark(), orderCreateParam.getRemark(), "订单 " + order_id + "备注与期望的不一致");
		} catch (Exception e) {
			logger.error("下单过程中遇到错误: ", e);
			Assert.fail("下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateTestCase20() {
		ReporterCSS.title("测试点: 新建订单,选择后商户和商品,切换商户,发送检测商品请求");
		try {
			String error_sku_id = "D1" + StringUtil.getRandomNumber(5);
			List<OrderCreateParam.OrderSku> skus = orderCreateParam.getDetails();
			List<String> skus_ids = skus.stream().map(s -> s.getSku_id()).collect(Collectors.toList());
			skus_ids.add(error_sku_id);

			List<String> valid_sku_ids = orderService.checkOrderSku(time_config_id, address_id, skus_ids);
			Assert.assertNotEquals(valid_sku_ids, null, "新建订单,选择后商户和商品,切换商户,发送检测商品请求失败");

			skus_ids.remove(error_sku_id);

			Assert.assertEquals(valid_sku_ids, skus_ids, "预期返回的可下单商品列表和实际返回的不一致");

		} catch (Exception e) {
			logger.error("下单过程中遇到错误: ", e);
			Assert.fail("下单过程中遇到错误: ", e);
		}
	}

}
