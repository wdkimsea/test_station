package cn.guanmai.station.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.station.bean.order.CustomerBean;
import cn.guanmai.station.bean.order.OrderBean;
import cn.guanmai.station.bean.order.OrderCycle;
import cn.guanmai.station.bean.async.AsyncTaskBean;
import cn.guanmai.station.bean.category.SalemenuSkuBean;
import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.delivery.DistributeOrderDetailBean;
import cn.guanmai.station.bean.invoicing.OutStockDetailBean;
import cn.guanmai.station.bean.order.OrderResponseBean;
import cn.guanmai.station.bean.order.OrderResponseBean.Data.NotEnoughInventories;
import cn.guanmai.station.bean.order.OrderSkuFilterResultBean;
import cn.guanmai.station.bean.order.param.OrderCreateParam;
import cn.guanmai.station.bean.order.param.OrderEditParam;
import cn.guanmai.station.bean.order.param.OrderFilterParam;
import cn.guanmai.station.bean.order.param.OrderSkuPriceAutoUpdateParam;
import cn.guanmai.station.bean.order.param.OrderStatusPreconfigParam;
import cn.guanmai.station.bean.order.param.OrderSkuFilterParam;
import cn.guanmai.station.bean.order.param.OrderEditParam.OrderData;
import cn.guanmai.station.bean.share.OrderAndSkuBean;
import cn.guanmai.station.bean.order.SaleSkuPriceUpdateResultBean;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderReceiveTimeBean;
import cn.guanmai.station.bean.order.OrderDetailBean.Detail;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.delivery.DistributeServiceImpl;
import cn.guanmai.station.impl.invoicing.OutStockServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.delivery.DistributeService;
import cn.guanmai.station.interfaces.invoicing.OutStockService;
import cn.guanmai.station.interfaces.order.OrderService;
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
public class OrderEditTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(OrderEditTest.class);
	private OrderService orderService;
	private OrderTool orderTool;
	private String todayStr;
	private OrderCreateParam orderCreateParam;
	private String order_id;
	private AsyncService asyncService;
	private CategoryService categoryService;
	private DistributeService distributeService;
	private OutStockService stockOutService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		orderService = new OrderServiceImpl(headers);
		orderTool = new OrderTool(headers);
		asyncService = new AsyncServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		distributeService = new DistributeServiceImpl(headers);
		stockOutService = new OutStockServiceImpl(headers);
		todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
	}

	@BeforeMethod
	public void beforeMethod() {
		try {
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
		} catch (Exception e) {
			logger.error("修改订单前的创建订单遇到错误: ", e);
			Assert.fail("修改订单前的创建订单遇到错误: ", e);
		}
	}

	public boolean compareResult(List<OrderCreateParam.OrderSku> orderSkus, List<OrderDetailBean.Detail> details)
			throws Exception {
		Map<String, BigDecimal> priceMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> quantityMap = new HashMap<String, BigDecimal>();

		for (OrderCreateParam.OrderSku orderSku : orderSkus) {
			String sku_id = orderSku.getSku_id();
			if (quantityMap.containsKey(sku_id)) {
				BigDecimal quantity = quantityMap.get(sku_id);
				quantity = quantity.add(orderSku.getAmount());
				quantityMap.put(sku_id, quantity);
			} else {
				quantityMap.put(sku_id, orderSku.getAmount());
				priceMap.put(sku_id, orderSku.getUnit_price());
			}
		}

		String msg = null;
		boolean result = true;
		for (String sku_id : quantityMap.keySet()) {
			Detail temp_detail = details.stream().filter(d -> d.getSku_id().equals(sku_id)).findAny().orElse(null);
			if (temp_detail == null) {
				msg = String.format("商品%在订单%s详细里没有出现", sku_id, order_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
				continue;
			}

			BigDecimal expectedPrice = priceMap.get(sku_id);
			BigDecimal expectedQuantity = quantityMap.get(sku_id);
			if (expectedPrice.compareTo(temp_detail.getSale_price()) != 0) {
				msg = String.format("订单%s详细里商品%s价格与预期不一致,预期:%s,实际:%s", order_id, sku_id, expectedPrice,
						temp_detail.getSale_price());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (expectedQuantity.compareTo(temp_detail.getQuantity()) != 0) {
				String spu_id = temp_detail.getSpu_id();
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
						expectedQuantity = expectedQuantity.setScale(0, BigDecimal.ROUND_FLOOR);
					} else if (sku.getRounding() == 2) {
						expectedQuantity = expectedQuantity.setScale(0, BigDecimal.ROUND_DOWN);
					}
					if (expectedQuantity.compareTo(temp_detail.getQuantity()) != 0) {
						msg = String.format("订单%s详细里商品%s设置了下单取整,但是下单数与预期不一致,预期:%s,实际:%s", order_id, sku_id,
								expectedQuantity, temp_detail.getQuantity());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				} else {
					msg = String.format("订单%s详细里商品%s下单数与预期不一致,预期:%s,实际:%s", order_id, sku_id, expectedQuantity,
							temp_detail.getQuantity());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * 创建订单测试
	 * 
	 */
	@Test
	public void orderEditTestCase01() {
		try {
			ReporterCSS.title("测试点: 修改订单,修改订单备注");
			// 封装修改订单对象
			OrderEditParam editOrder = new OrderEditParam();
			editOrder.setOrder_id(order_id);
			editOrder.setDetails(orderCreateParam.getDetails());
			editOrder.setCombine_goods_map(orderCreateParam.getCombine_goods_map());

			// 设置收货时间
			OrderData orderData = editOrder.new OrderData();
			orderData.setReceive_begin_time(orderCreateParam.getReceive_begin_time());
			orderData.setReceive_end_time(orderCreateParam.getReceive_end_time());
			String remark = StringUtil.getRandomString(6);
			orderData.setRemark(remark);
			editOrder.setOrder_data(orderData);

			boolean result = orderService.editOrder(editOrder);
			Assert.assertEquals(result, true, "修改订单失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + "的详情信息失败");

			Assert.assertEquals(orderDetail.getRemark(), remark, "订单" + order_id + "的备注信息修改后与预期的不一致");
		} catch (Exception e) {
			logger.error("修改订单过程中遇到错误: ", e);
			Assert.fail("修改订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderEditTestCase02() {
		try {
			ReporterCSS.title("测试点: 修改订单,修改商品的下单数和金额");
			// 封装修改订单对象
			OrderEditParam editOrder = new OrderEditParam();
			editOrder.setOrder_id(order_id);

			// 先把组合商品的下单数调整
			Map<String, OrderCreateParam.CombineGoods> combineGoodsMap = orderCreateParam.getCombine_goods_map();
			if (combineGoodsMap != null && combineGoodsMap.size() > 0) {
				for (String combineGoodsId : combineGoodsMap.keySet()) {
					OrderCreateParam.CombineGoods combineGoods = combineGoodsMap.get(combineGoodsId);
					BigDecimal quantity = combineGoods.getQuantity().add(NumberUtil.getRandomNumber(1, 3, 1));
					combineGoods.setQuantity(quantity);
					combineGoods.setFake_quantity(quantity);

					Map<String, BigDecimal> skusRatioMap = combineGoods.getSkus_ratio();
					for (OrderCreateParam.OrderSku orderSku : orderCreateParam.getDetails()) {
						if (orderSku.isIs_combine_goods() && orderSku.getCombine_goods_id().equals(combineGoodsId)) {
							BigDecimal skusRatio = skusRatioMap.get(orderSku.getSku_id());
							BigDecimal skuQuantity = quantity.multiply(skusRatio);
							orderSku.setAmount(skuQuantity);
							orderSku.setFake_quantity(skuQuantity);
						}
					}
				}
			}

			Map<String, BigDecimal> priceMap = new HashMap<String, BigDecimal>();
			Map<String, BigDecimal> quantityMap = new HashMap<String, BigDecimal>();

			for (OrderCreateParam.OrderSku orderSku : orderCreateParam.getDetails()) {
				String sku_id = orderSku.getSku_id();
				BigDecimal unit_price = orderSku.getUnit_price().add(NumberUtil.getRandomNumber(1, 3, 1));
				if (!orderSku.isIs_combine_goods()) {
					BigDecimal quantity = orderSku.getAmount().add(NumberUtil.getRandomNumber(1, 2, 1));
					orderSku.setAmount(quantity);
					orderSku.setFake_quantity(quantity);
				}
				if (priceMap.containsKey(sku_id)) {
					unit_price = priceMap.get(sku_id);
				} else {
					priceMap.put(sku_id, unit_price);
				}

				if (quantityMap.containsKey(sku_id)) {
					BigDecimal quantity = quantityMap.get(sku_id).add(orderSku.getAmount());
					quantityMap.put(sku_id, quantity);
				} else {
					quantityMap.put(sku_id, orderSku.getAmount());
				}

				orderSku.setUnit_price(unit_price);
				orderSku.setIs_price_timing(0);

			}
			editOrder.setDetails(orderCreateParam.getDetails());
			editOrder.setCombine_goods_map(orderCreateParam.getCombine_goods_map());

			// 设置收货时间
			OrderData orderData = editOrder.new OrderData();
			orderData.setReceive_begin_time(orderCreateParam.getReceive_begin_time());
			orderData.setReceive_end_time(orderCreateParam.getReceive_end_time());
			orderData.setRemark(StringUtil.getRandomString(6));
			editOrder.setOrder_data(orderData);

			boolean result = orderService.editOrder(editOrder);
			Assert.assertEquals(result, true, "修改订单失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单详情失败");

			// 验证传入的参数是否修改成功
			String msg = null;
			BigDecimal expectedPrice = null;
			BigDecimal expectedQuantity = null;
			for (String sku_id : priceMap.keySet()) {
				Detail temp_detail = orderDetail.getDetails().stream().filter(d -> d.getSku_id().equals(sku_id))
						.findAny().orElse(null);
				if (temp_detail == null) {
					msg = String.format("商品%在订单%s详细里没有出现", sku_id, order_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				expectedPrice = priceMap.get(sku_id);
				expectedQuantity = quantityMap.get(sku_id);
				if (expectedPrice.compareTo(temp_detail.getSale_price()) != 0) {
					msg = String.format("订单%s详细里商品%s价格与预期不一致,预期:%s,实际:%s", order_id, sku_id, expectedPrice,
							temp_detail.getSale_price());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (expectedQuantity.compareTo(temp_detail.getQuantity()) != 0) {
					msg = String.format("订单%s详细里商品%s下单数与预期不一致,预期:%s,实际:%s", order_id, sku_id, expectedQuantity,
							temp_detail.getQuantity());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单详细里的信息与预期的不一致");
		} catch (Exception e) {
			logger.error("修改订单过程中遇到错误: ", e);
			Assert.fail("修改订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderEditTestCase03() {
		try {
			ReporterCSS.title("测试点: 修改订单,移除个别下单商品");

			List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();
			Assert.assertEquals(orderCreateParam.getDetails().size() >= 2, true, "下单集合只有一个商品,无法再移除商品");

			OrderCreateParam.OrderSku orderSku = orderSkus.remove(new Random().nextInt(orderSkus.size()));
			List<OrderCreateParam.OrderSku> editOrderSkus = new ArrayList<OrderCreateParam.OrderSku>();
			if (orderSku.isIs_combine_goods()) {
				String combine_goods_id = orderSku.getCombine_goods_id();
				orderCreateParam.getCombine_goods_map().remove(combine_goods_id);
				for (OrderCreateParam.OrderSku sku : orderSkus) {
					if (sku.isIs_combine_goods() && sku.getCombine_goods_id().equals(combine_goods_id)) {
						OrderCreateParam.OrderSku editOrderSku = editOrderSkus.stream()
								.filter(s -> !s.isIs_combine_goods() && s.getSku_id().equals(sku.getSku_id())).findAny()
								.orElse(null);
						if (editOrderSku != null) {
							BigDecimal quantity = editOrderSku.getAmount();
							quantity = quantity.add(sku.getAmount());
							editOrderSku.setAmount(quantity);
						} else {
							sku.setCombine_goods_id(null);
							sku.setIs_combine_goods(false);
							editOrderSkus.add(sku);
						}
					} else {
						editOrderSkus.add(sku);
					}
				}
			} else {
				editOrderSkus = orderSkus;
			}
			// 封装修改订单对象
			OrderEditParam editOrder = new OrderEditParam();
			editOrder.setOrder_id(order_id);

			editOrder.setCombine_goods_map(orderCreateParam.getCombine_goods_map());
			editOrder.setDetails(editOrderSkus);

			// 设置收货时间
			OrderData orderData = editOrder.new OrderData();
			orderData.setReceive_begin_time(orderCreateParam.getReceive_begin_time());
			orderData.setReceive_end_time(orderCreateParam.getReceive_end_time());
			orderData.setRemark(StringUtil.getRandomString(6));
			editOrder.setOrder_data(orderData);

			boolean result = orderService.editOrder(editOrder);
			Assert.assertEquals(result, true, "修改订单失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			result = compareResult(editOrderSkus, orderDetail.getDetails());
			Assert.assertEquals(result, true, "订单的详细详细与预期的不一致");
		} catch (Exception e) {
			logger.error("修改订单过程中遇到错误: ", e);
			Assert.fail("修改订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderEditTestCase04() {
		try {
			ReporterCSS.title("测试点: 修改订单,移除下单商品后再添加回来");
			// 封装修改订单对象
			OrderEditParam editOrder = new OrderEditParam();
			editOrder.setOrder_id(order_id);

			List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();

			Assert.assertEquals(orderSkus.size() >= 2, true, "下单集合只有一个商品,无法再移除商品");
			// 随机移除一个下单商品
			OrderCreateParam.OrderSku removeOrderSku = orderSkus.stream().filter(s -> !s.isIs_combine_goods())
					.findFirst().orElse(null);
			Assert.assertNotEquals(removeOrderSku, null, "没有非组合商品可供移除");

			orderSkus.remove(removeOrderSku);

			editOrder.setDetails(orderSkus);
			editOrder.setCombine_goods_map(orderCreateParam.getCombine_goods_map());

			// 设置收货时间
			OrderData orderData = editOrder.new OrderData();
			orderData.setReceive_begin_time(orderCreateParam.getReceive_begin_time());
			orderData.setReceive_end_time(orderCreateParam.getReceive_end_time());
			orderData.setRemark(StringUtil.getRandomString(6));
			editOrder.setOrder_data(orderData);

			boolean result = orderService.editOrder(editOrder);
			Assert.assertEquals(result, true, "修改订单失败");

			result = orderSkus.add(removeOrderSku);
			Assert.assertEquals(result, true, "下单商品集合新增商品失败");

			editOrder.setDetails(orderSkus);
			result = orderService.editOrder(editOrder);
			Assert.assertEquals(result, true, "修改订单失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			result = compareResult(orderSkus, orderDetail.getDetails());
			Assert.assertEquals(result, true, "订单的详细详细与预期的不一致");

		} catch (Exception e) {
			logger.error("修改订单过程中遇到错误: ", e);
			Assert.fail("修改订单过程中遇到错误: ", e);
		}
	}

	/**
	 * Station订单按商品查看,修改的出库数（基本单位）
	 */
	@Test
	public void orderEditTestCase05() {
		ReporterCSS.title("测试点: 订单列表-按商品查看-修改出库数");
		try {
			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单详细信息失败");

			Detail detail = NumberUtil.roundNumberInList(orderDetail.getDetails());
			String sku_id = detail.getSku_id();
			BigDecimal std_real_quantity = detail.getReal_quantity().add(new BigDecimal("0.1"));

			boolean result = orderService.updateOrderState(order_id, 5);
			Assert.assertEquals(result, true, "修改订单" + order_id + "状态,由待分拣改为分拣中,断言成功");
			result = orderService.orderRealQuantityUpdate(order_id, sku_id, std_real_quantity);
			Assert.assertEquals(result, true, "订单列表-按商品查看-修改出库数,断言成功");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单详细信息失败");

			detail = orderDetail.getDetails().stream().filter(d -> d.getSku_id().equals(sku_id)).findAny().orElse(null);
			Assert.assertNotEquals(detail, null, "订单" + order_id + "详细信息中没有找到商品 " + sku_id);

			boolean compare = detail.getStd_real_quantity().compareTo(std_real_quantity) == 0;
			Assert.assertEquals(compare, result, "订单 " + order_id + " 的商品" + sku_id + " 出库数没有更新成功, 预期: "
					+ std_real_quantity + ",实际: " + detail.getStd_real_quantity());
		} catch (Exception e) {
			logger.error("修改订单过程中遇到错误: ", e);
			Assert.fail("修改订单过程中遇到错误: ", e);
		}
	}

	/**
	 * 按商品查看,批量设置商品缺货
	 * 
	 */
	@Test
	public void orderEditTestCase06() {
		ReporterCSS.title("测试点: 按商品查看,批量设置商品缺货");
		List<OrderAndSkuBean> batchOutOfStockArray = new ArrayList<OrderAndSkuBean>();
		OrderAndSkuBean batchOutOfStockBean = null;
		for (OrderCreateParam.OrderSku orderSku : orderCreateParam.getDetails()) {
			batchOutOfStockBean = new OrderAndSkuBean(order_id, orderSku.getSku_id());
			batchOutOfStockArray.add(batchOutOfStockBean);
		}
		try {
			List<String> order_ids = Arrays.asList(order_id);
			boolean result = orderService.updateOrderState(order_ids, 5, "orderEditTestCase03");
			Assert.assertEquals(result, true, "修改订单" + order_id + "状态,由待分拣改为分拣中,断言成功");
			result = orderService.updateBatchOutOfStock(batchOutOfStockArray);
			Assert.assertEquals(result, true, "按商品查看,批量设置商品缺货,断言成功");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单详细信息失败");

			boolean success = true;
			for (Detail detail : orderDetail.getDetails()) {
				result = detail.getStd_real_quantity().compareTo(BigDecimal.ZERO) == 0;
				if (!result) {
					success = false;
					ReporterCSS.warn("订单 " + order_id + " 中的商品" + detail.getSku_id() + "预期称重数为0,实际为 "
							+ detail.getStd_real_quantity());
				}
			}
			Assert.assertEquals(success, true, "订单 " + order_id + " 中存在实际称重数与预期不符的商品");
		} catch (Exception e) {
			logger.error("修改订单过程中遇到错误: ", e);
			Assert.fail("修改订单过程中遇到错误: ", e);
		}
	}

	/**
	 * 按商品查看,订单商品同步最新单价
	 * 
	 */
	@Test
	public void orderEditTestCase07() {
		ReporterCSS.title("测试点: 按商品查看,同步最新销售单价");
		try {
			List<OrderAndSkuBean> orderSkuPriceAutoArray = new ArrayList<OrderAndSkuBean>();
			OrderAndSkuBean orderAndSku = null;

			List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();

			// 修改商品价格,然后再同步
			for (OrderCreateParam.OrderSku orderSku : orderSkus) {
				orderSku.setUnit_price(orderSku.getUnit_price().add(new BigDecimal("0.2")));

				orderAndSku = new OrderAndSkuBean(order_id, orderSku.getSku_id());
				orderSkuPriceAutoArray.add(orderAndSku);
			}

			OrderEditParam orderEditParam = new OrderEditParam();
			orderEditParam.setDetails(orderSkus);
			orderEditParam.setCombine_goods_map(orderCreateParam.getCombine_goods_map());

			OrderData orderData = orderEditParam.new OrderData();
			orderData.setReceive_begin_time(orderCreateParam.getReceive_begin_time());
			orderData.setReceive_end_time(orderCreateParam.getReceive_end_time());
			orderData.setRemark(StringUtil.getRandomString(6));
			orderEditParam.setOrder_data(orderData);

			orderEditParam.setOrder_id(order_id);

			boolean result = orderService.editOrder(orderEditParam);
			Assert.assertEquals(result, true, "修改订单失败");

			AsyncTaskBean aysncTask = orderService.updateOrderSkuPriceAuto(orderSkuPriceAutoArray, 2);
			Assert.assertNotEquals(aysncTask, null, "按商品查看,同步最新单价,断言成功");
			String task_url = aysncTask.getTask_url();
			BigDecimal task_id = new BigDecimal(task_url.split("=")[1]);
			boolean task_result = asyncService.getAsyncTaskResult(task_id, "失败0");
			String msg = null;
			SkuBean sku = null;
			List<String> remove_skus = new ArrayList<String>();
			if (!task_result) {
				// 查看失败结果
				List<SaleSkuPriceUpdateResultBean> updateSkuPriceResults = orderService.updateSkuPriceResult(task_id);
				Assert.assertNotEquals(updateSkuPriceResults, null, "获取按商品查看,同步最新单价异步任务失败结果失败");

				String sku_id = null;
				String salemenu_id = null;
				for (SaleSkuPriceUpdateResultBean updateSkuPriceResult : updateSkuPriceResults) {
					sku_id = updateSkuPriceResult.getSku_id();
					salemenu_id = updateSkuPriceResult.getSalemenu_id();
					SalemenuSkuBean salemenuSku = categoryService.getSkuInSalemenu(salemenu_id, sku_id);
					Assert.assertNotEquals(salemenuSku, null, "报价单里搜索过滤销售商品失败");
					if (updateSkuPriceResult.getReason().equals("时价商品")) {
						if (!salemenuSku.isIs_price_timing()) {
							msg = String.format("销售商品%s价格为非时价,但是执行失败结果显示失败原因为时价商品", sku_id);
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
						remove_skus.add(sku_id);
					}

				}
			}

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			String spu_id = null;
			BigDecimal sale_price = null;

			for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
				if (detail.getLock_price() != null) {
					if (detail.getLock_price().compareTo(detail.getSale_price()) != 0) {
						msg = String.format("订单%s中的商品%为锁价商品,但是锁价价格和售卖价格不一致,锁价:%s,售卖价格:%s", order_id, detail.getSku_id(),
								detail.getLock_price(), detail.getSale_price());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
					continue;
				}

				if (!remove_skus.contains(detail.getSku_id())) {
					spu_id = detail.getSpu_id();
					String sku_id = detail.getSku_id();
					orderAndSku = orderSkuPriceAutoArray.stream().filter(s -> s.getSku_id().equals(sku_id)).findAny()
							.orElse(null);
					sale_price = detail.getSale_price();
					sku = categoryService.getSaleSkuById(spu_id, sku_id);
					Assert.assertNotEquals(sku, null, "获取销售SKU " + sku_id + " 详细信息失败");
					if (sku.getSale_price().compareTo(sale_price) != 0) {
						msg = String.format("订单%s里的商品%s没有同步成功最近销售价格,预期%s,实际:%s", order_id, sku_id, sku.getSale_price(),
								sale_price);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "订单 " + order_id + " 里的商品没有同步成功最新单价");
		} catch (Exception e) {
			logger.error("修改订单过程中遇到错误: ", e);
			Assert.fail("修改订单过程中遇到错误: ", e);
		}
	}

	/**
	 * 按商品查看,订单商品同步最新单价
	 * 
	 */
	@Test
	public void orderEditTestCase08() {
		ReporterCSS.title("测试点: 按商品查看,同步最新单价,把商品改为时价后再同步,预期同步失败");
		List<OrderAndSkuBean> orderSkuPriceAutoArray = new ArrayList<OrderAndSkuBean>();
		OrderAndSkuBean orderAndSkuBean = null;

		SkuBean sku = null;
		String spu_id = null;
		String sku_id = null;
		try {
			List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();
			for (OrderCreateParam.OrderSku orderSku : orderSkus) {
				orderAndSkuBean = new OrderAndSkuBean(order_id, orderSku.getSku_id());
				orderSkuPriceAutoArray.add(orderAndSkuBean);
				spu_id = orderSku.getSpu_id();
				sku_id = orderSku.getSku_id();
				break;
			}
			sku = categoryService.getSaleSkuById(spu_id, sku_id);
			Assert.assertNotEquals(sku, null, "获取销售SKU " + sku_id + " 详细信息失败");

			sku.setIs_price_timing(1);

			boolean result = categoryService.updateSaleSku(sku);
			Assert.assertEquals(result, true, "修改销售SKU: " + sku_id + " 详细信息失败");

			AsyncTaskBean aysncTask = orderService.updateOrderSkuPriceAuto(orderSkuPriceAutoArray, 1);
			Assert.assertNotEquals(aysncTask, null, "按商品查看,同步最新单价,断言成功");
			BigDecimal task_id = new BigDecimal(aysncTask.getTask_url().split("=")[1]);
			result = asyncService.getAsyncTaskResult(task_id, "失败1");

			List<SaleSkuPriceUpdateResultBean> updateSkuPriceResultArray = orderService.updateSkuPriceResult(task_id);
			Assert.assertNotEquals(updateSkuPriceResultArray, null, "按商品查看,查看同步最新单价具体结果失败");
			for (SaleSkuPriceUpdateResultBean saleSkuPriceUpdateResult : updateSkuPriceResultArray) {
				String temp_sku_id = saleSkuPriceUpdateResult.getSku_id();

				Assert.assertEquals(saleSkuPriceUpdateResult.getReason(), "时价商品",
						"商品 " + temp_sku_id + "为时价商品,订单同步最新单价失败");
			}
		} catch (Exception e) {
			logger.error("修改订单过程中遇到错误: ", e);
			Assert.fail("修改订单过程中遇到错误: ", e);
		} finally {
			if (sku != null) {
				try {
					sku.setIs_price_timing(0);
					boolean result = categoryService.updateSaleSku(sku);
					Assert.assertEquals(result, true, "修改销售SKU: " + sku_id + " 信息失败");
				} catch (Exception e) {
					logger.error("后置处理,修改销售商品遇到错误: ", e);
					Assert.fail("后置处理,修改销售商品遇到错误: ", e);
				}

			}
		}
	}

	@Test
	public void orderEditTestCase09() {
		ReporterCSS.title("测试点: 按商品查看,同步最新销售单价,搜索过滤后再同步(老版UI)");
		try {
			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OrderSkuPriceAutoUpdateParam orderSkuPriceAutoUpdateParam = new OrderSkuPriceAutoUpdateParam();
			orderSkuPriceAutoUpdateParam.setStart_date(todayStr);
			orderSkuPriceAutoUpdateParam.setEnd_date(todayStr);
			orderSkuPriceAutoUpdateParam.setQuery_type(1);
			orderSkuPriceAutoUpdateParam.setSearch_text(order_id);
			orderSkuPriceAutoUpdateParam.setPrice_unit_type(1);

			List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();
			// 修改商品价格,然后再同步
			for (OrderCreateParam.OrderSku orderSku : orderSkus) {
				orderSku.setUnit_price(orderSku.getUnit_price().add(new BigDecimal("1")));
			}
			OrderEditParam orderEditParam = new OrderEditParam();
			orderEditParam.setDetails(orderSkus);
			orderEditParam.setCombine_goods_map(orderCreateParam.getCombine_goods_map());

			OrderData orderData = orderEditParam.new OrderData();
			orderData.setReceive_begin_time(orderCreateParam.getReceive_begin_time());
			orderData.setReceive_end_time(orderCreateParam.getReceive_end_time());
			orderData.setRemark(StringUtil.getRandomString(6));
			orderEditParam.setOrder_data(orderData);

			orderEditParam.setOrder_id(order_id);

			boolean result = orderService.editOrder(orderEditParam);
			Assert.assertEquals(result, true, "修改订单失败");

			AsyncTaskBean aysncTask = orderService.updateOrderSkuPriceAuto(orderSkuPriceAutoUpdateParam);
			Assert.assertNotEquals(aysncTask, null, "按商品查看,同步最新单价,断言成功");

			String task_url = aysncTask.getTask_url();
			BigDecimal task_id = new BigDecimal(task_url.split("=")[1]);
			boolean task_result = asyncService.getAsyncTaskResult(task_id, "失败0");
			String msg = null;
			SkuBean sku = null;
			List<String> remove_skus = new ArrayList<String>();
			if (!task_result) {
				// 查看失败结果
				List<SaleSkuPriceUpdateResultBean> updateSkuPriceResults = orderService.updateSkuPriceResult(task_id);
				Assert.assertNotEquals(updateSkuPriceResults, null, "获取按商品查看,同步最新单价异步任务失败结果失败");

				String sku_id = null;
				String salemenu_id = null;
				for (SaleSkuPriceUpdateResultBean updateSkuPriceResult : updateSkuPriceResults) {
					sku_id = updateSkuPriceResult.getSku_id();
					salemenu_id = updateSkuPriceResult.getSalemenu_id();
					SalemenuSkuBean salemenuSku = categoryService.getSkuInSalemenu(salemenu_id, sku_id);
					Assert.assertNotEquals(salemenuSku, null, "报价单里搜索过滤销售商品失败");
					if (updateSkuPriceResult.getReason().equals("时价商品")) {
						if (!salemenuSku.isIs_price_timing()) {
							msg = String.format("销售商品%s价格为非时价,但是执行失败结果显示失败原因为时价商品", sku_id);
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
						remove_skus.add(sku_id);
					}
				}
			}

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			String spu_id = null;
			String sku_id = null;
			BigDecimal sale_price = null;
			for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
				// 可能商品为锁价,锁价商品不同步销售最新单价
				if (detail.getLock_price() != null) {
					if (detail.getLock_price().compareTo(detail.getSale_price()) != 0) {
						msg = String.format("订单%s中的商品%为锁价商品,但是锁价价格和售卖价格不一致,锁价:%s,售卖价格:%s", order_id, detail.getSku_id(),
								detail.getLock_price(), detail.getSale_price());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
					continue;
				}

				if (!remove_skus.contains(detail.getSku_id())) {
					spu_id = detail.getSpu_id();
					sku_id = detail.getSku_id();
					sale_price = detail.getSale_price();
					sku = categoryService.getSaleSkuById(spu_id, sku_id);
					Assert.assertNotEquals(sku, null, "获取销售SKU " + sku_id + " 详细信息失败");
					if (sku.getSale_price().compareTo(sale_price) != 0) {
						msg = String.format("订单%s里的商品%s没有同步成功最近销售价格,预期%s,实际:%s", order_id, sku_id, sku.getSale_price(),
								sale_price);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "订单 " + order_id + " 里的商品没有同步成功最新单价");
		} catch (Exception e) {
			logger.error("修改订单过程中遇到错误: ", e);
			Assert.fail("修改订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderEditTestCase10() {
		ReporterCSS.title("测试点: 按商品查看,手工修改订单中的单价");

		OrderSkuFilterParam param = new OrderSkuFilterParam(1, todayStr, todayStr, order_id, 0, 20);
		List<OrderSkuFilterResultBean> resultArray = null;
		try {
			resultArray = orderService.searchOrderSku(param);
			Assert.assertNotEquals(resultArray, null, "订单列表,按商品查看,查询下单商品失败");
		} catch (Exception e) {
			logger.error("订单列表,按商品查看查询遇到错误: ", e);
			Assert.fail("订单列表,按商品查看查询遇到错误: ", e);
		}

		OrderDetailBean orderDetail_1 = null;
		try {
			orderDetail_1 = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail_1, null, "获取订单详情信息失败");
		} catch (Exception e) {
			logger.error("查看订单详情信息遇到错误: ", e);
			Assert.fail("查看订单详情信息遇到错误: ", e);
		}

		// 这里的参数结果过于复杂,就没有对参数进行封装,直接用JSONObject进行的组装
		JSONObject price_data = new JSONObject();
		String key = null;
		JSONObject valueObj = null;

		JSONArray orders = new JSONArray();
		orders.add(order_id);

		for (OrderSkuFilterResultBean resulBean : resultArray) {
			key = resulBean.getSku_id();
			valueObj = new JSONObject();
			valueObj.put("std_sale_price", resulBean.getStd_sale_price().add(new BigDecimal("1")));
			valueObj.put("sale_price",
					resulBean.getStd_sale_price().add(new BigDecimal("1")).multiply(resulBean.getSale_ratio()));
			valueObj.put("orders", orders);
			price_data.put(key, valueObj);
		}

		try {
			BigDecimal task_id = orderService.updateOrderSkuPrice(price_data);
			Assert.assertNotEquals(task_id, null, "订单列表,按商品查看,手工修改商品单价异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "失败0");
			Assert.assertEquals(result, true, "订单列表,按商品查看,手工修改商品单价异步任务执行存在失败的价格更新");

			OrderDetailBean orderDetail_2 = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail_2, null, "获取订单详情信息失败");

			boolean seccess = true;

			for (Object obj : price_data.keySet()) {
				String sku_id = String.valueOf(obj);
				Detail temp_detail = orderDetail_2.getDetails().stream().filter(d -> d.getSku_id().equals(sku_id))
						.findAny().orElse(null);
				BigDecimal expected_sale_price = new BigDecimal(
						price_data.getJSONObject(sku_id).getString("sale_price"));
				result = expected_sale_price.compareTo(temp_detail.getSale_price()) == 0;
				if (!result) {
					seccess = false;
					ReporterCSS.warn("订单" + order_id + "中的商品" + sku_id + " 预期价格是 " + expected_sale_price + ", 实际价格是"
							+ temp_detail.getSale_price());
					logger.warn("订单" + order_id + "中的商品" + sku_id + " 预期价格是 " + expected_sale_price + ", 实际价格是"
							+ temp_detail.getSale_price());

				}
			}
			Assert.assertEquals(seccess, true, "订单中存在销售价格和预期价格不一致的商品");
		} catch (Exception e) {
			logger.error("订单列表,按商品查看,手工修改商品单价遇到错误: ", e);
			Assert.fail("订单列表,按商品查看,手工修改商品单价遇到错误: ", e);
		}
	}

	@Test
	public void orderEditTestCase11() {
		ReporterCSS.title("测试点: 修改订单,对下单商品进行重新排序");
		try {
			List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();
			orderSkus = orderSkus.stream()
					.filter(s -> s.getCombine_goods_id() == null || s.getCombine_goods_id().equals(""))
					.collect(Collectors.toList());
			Assert.assertEquals(orderSkus.size() > 1, true, "下单商品列表数不大于2,无法对下单商品列表进行重新排序");

			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);
			Assert.assertEquals(orderResponse.getData().getNew_order_ids().size() > 0, true, "正常下单,断言成功");

			String order_id = orderResponse.getData().getNew_order_ids().getString(0);
			// 封装修改订单对象
			OrderEditParam editOrder = new OrderEditParam();
			editOrder.setOrder_id(order_id);

			// 下单商品列表逆转
			OrderCreateParam.OrderSku temp = null;
			for (int i = 0; i < orderSkus.size(); i++) {
				if (i < orderSkus.size() / 2) {
					temp = orderSkus.get(orderSkus.size() - 1 - i);
					orderSkus.set(orderSkus.size() - 1 - i, orderSkus.get(i));
					orderSkus.set(i, temp);
				}
			}
			List<String> expected_sku_list = orderSkus.stream().map(OrderCreateParam.OrderSku::getSku_id)
					.collect(Collectors.toList());
			editOrder.setDetails(orderSkus);

			// 设置收货时间
			OrderData orderData = editOrder.new OrderData();
			orderData.setReceive_begin_time(orderCreateParam.getReceive_begin_time());
			orderData.setReceive_end_time(orderCreateParam.getReceive_end_time());
			orderData.setRemark(StringUtil.getRandomString(6));
			editOrder.setOrder_data(orderData);

			boolean result = orderService.editOrder(editOrder);
			Assert.assertEquals(result, true, "修改订单失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			List<String> actual_sku_list = orderDetail.getDetails().stream().map(Detail::getSku_id)
					.collect(Collectors.toList());
			Assert.assertEquals(actual_sku_list.equals(expected_sku_list), true,
					"修改订单" + order_id + ",商品重新排序,修改的后商品排序与预期不符,预期: " + expected_sku_list + ",实际: " + actual_sku_list);
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderEditTestCase12() {
		ReporterCSS.title("测试点: 修改订单状态,由 待分拣->分拣中->配送中->签收中");
		try {
			List<Integer> statuses = Arrays.asList(5, 10, 15);
			for (Integer status : statuses) {
				boolean result = orderService.updateOrderState(order_id, status);
				Assert.assertEquals(result, true, "订单" + order_id + "状态值改为 " + status + "失败");

				OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
				Assert.assertNotEquals(orderDetail, null, "获取订单详情信息失败");
				Assert.assertEquals(orderDetail.getStatus(), status,
						"订单" + order_id + "状态值与预期不一致,预期:" + status + ",实际: " + orderDetail.getStatus());
			}
		} catch (Exception e) {
			logger.error("修改订单状态遇到错误: ", e);
			Assert.fail("修改订单状态遇到错误: ", e);
		}
	}

	@Test
	public void orderEditTestCase13() {
		ReporterCSS.title("测试点: 修改订单状态,由 待分拣直接修改为 签收中");
		try {
			Integer status = 15;
			boolean result = orderService.updateOrderState(order_id, status);
			Assert.assertEquals(result, true, "订单" + order_id + "状态值改为 " + status + "失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单详情信息失败");
			Assert.assertEquals(orderDetail.getStatus(), status,
					"订单" + order_id + "状态值与预期不一致,预期:" + status + ",实际: " + orderDetail.getStatus());

		} catch (Exception e) {
			logger.error("修改订单状态遇到错误: ", e);
			Assert.fail("修改订单状态遇到错误: ", e);
		}
	}

	@Test
	public void orderEditTestCase14() {
		ReporterCSS.title("测试点: 修改订单状态(批量修改),由 待分拣->分拣中->配送中->签收中");
		try {
			List<Integer> statuses = Arrays.asList(5, 10, 15);
			List<String> order_ids = Arrays.asList(order_id);
			for (Integer status : statuses) {
				boolean result = orderService.updateOrderState(order_ids, status, "批量修改");
				Assert.assertEquals(result, true, "订单" + order_id + "状态值改为 " + status + "失败");

				OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
				Assert.assertNotEquals(orderDetail, null, "获取订单详情信息失败");
				Assert.assertEquals(orderDetail.getStatus(), status,
						"订单" + order_id + "状态值与预期不一致,预期:" + status + ",实际: " + orderDetail.getStatus());
			}
		} catch (Exception e) {
			logger.error("修改订单状态遇到错误: ", e);
			Assert.fail("修改订单状态遇到错误: ", e);
		}
	}

	@Test
	public void orderEditTestCase15() {
		ReporterCSS.title("测试点: 修改订单状态(批量),由 待分拣直接修改为 签收中");
		try {
			Integer status = 15;
			List<String> order_ids = Arrays.asList(order_id);
			boolean result = orderService.updateOrderState(order_ids, status, "ASF");
			Assert.assertEquals(result, true, "订单" + order_id + "状态值改为 " + status + "失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单详情信息失败");
			Assert.assertEquals(orderDetail.getStatus(), status,
					"订单" + order_id + "状态值与预期不一致,预期:" + status + ",实际: " + orderDetail.getStatus());
		} catch (Exception e) {
			logger.error("修改订单状态遇到错误: ", e);
			Assert.fail("修改订单状态遇到错误: ", e);
		}
	}

	@Test
	public void orderEditTestCase16() {
		ReporterCSS.title("测试点: 修改订单,查看修改数据是否同步到配送单");
		try {
			List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();
			if (orderSkus.size() >= 2) {
				OrderCreateParam.OrderSku orderSku = orderSkus.stream().filter(s -> s.getCombine_goods_id() == null)
						.findFirst().orElse(null);
				if (orderSku != null) {
					boolean result = orderSkus.remove(orderSku);
					Assert.assertEquals(result, true, "移除商品操作失败");
				}
			}

			for (OrderCreateParam.OrderSku orderSku : orderSkus) {
				if (orderSku.getCombine_goods_id() == null) {
					orderSku.setAmount(NumberUtil.getRandomNumber(20, 30, 2));
				}
			}

			// 封装修改订单对象
			OrderEditParam editOrder = new OrderEditParam();
			editOrder.setOrder_id(order_id);
			editOrder.setDetails(orderSkus);
			editOrder.setCombine_goods_map(orderCreateParam.getCombine_goods_map());

			// 设置收货时间
			OrderData orderData = editOrder.new OrderData();
			orderData.setReceive_begin_time(orderCreateParam.getReceive_begin_time());
			orderData.setReceive_end_time(orderCreateParam.getReceive_end_time());
			orderData.setRemark(StringUtil.getRandomString(6));
			editOrder.setOrder_data(orderData);

			boolean result = orderService.editOrder(editOrder);
			Assert.assertEquals(result, true, "修改订单失败");

			List<DistributeOrderDetailBean> distributeOrderDetailList = distributeService
					.getDistributeOrderDetailArray(Arrays.asList(order_id));
			Assert.assertNotEquals(distributeOrderDetailList, null, "获取配送单" + order_id + " 详细信息失败");

			DistributeOrderDetailBean distributeOrderDetail = distributeOrderDetailList.stream()
					.filter(d -> d.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(distributeOrderDetail, null, "没有找到订单 " + order_id + " 对应的配送单");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单详细信息失败");

			String msg = null;
			for (OrderDetailBean.Detail o_detail : orderDetail.getDetails()) {
				String sku_id = o_detail.getSku_id();
				DistributeOrderDetailBean.Detail d_detail = distributeOrderDetail.getDetails().stream()
						.filter(d -> d.getSku_id().equals(sku_id)).findAny().orElse(null);
				if (d_detail == null) {
					msg = String.format("订单%s中的商品%s没有在配送单中找到", order_id, sku_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (o_detail.getQuantity().compareTo(d_detail.getQuantity()) != 0) {
					msg = String.format("订单%s中的商品%s下单数没有同步到配送单,预期:%s,实际:%s", order_id, sku_id, o_detail.getQuantity(),
							o_detail.getQuantity());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (o_detail.getSale_price().compareTo(d_detail.getSale_price()) != 0) {
					msg = String.format("订单%s中的商品%s单价没有同步到配送单,预期:%s,实际:%s", order_id, sku_id, o_detail.getSale_price(),
							d_detail.getSale_price());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			if (orderSkus.size() < distributeOrderDetail.getDetails().size()) {
				msg = String.format("订单中删除的商品在配送单中没有删除", order_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "订单 " + order_id + " 修改后,相关数据没有同步到配送单");

		} catch (Exception e) {
			logger.error("修改订单状态遇到错误: ", e);
			Assert.fail("修改订单状态遇到错误: ", e);
		}
	}

	@Test
	public void orderEditTestCase17() {
		ReporterCSS.title("测试点: 修改订单,查看修改数据是否同步到出库单");
		try {
			List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();
			if (orderSkus.size() >= 2) {
				for (OrderCreateParam.OrderSku orderSku : orderSkus) {
					if (orderSku.getCombine_goods_id() == null || orderSku.getCombine_goods_id().equals("")) {
						orderSkus.remove(orderSku);
						break;
					}
				}
			}

			for (OrderCreateParam.OrderSku orderSku : orderSkus) {
				orderSku.setAmount(NumberUtil.getRandomNumber(20, 30, 2));
				orderSku.setUnit_price(NumberUtil.getRandomNumber(6, 10, 2));
			}

			// 封装修改订单对象
			OrderEditParam editOrder = new OrderEditParam();
			editOrder.setOrder_id(order_id);
			editOrder.setDetails(orderSkus);
			editOrder.setCombine_goods_map(orderCreateParam.getCombine_goods_map());

			// 设置收货时间
			OrderData orderData = editOrder.new OrderData();
			orderData.setReceive_begin_time(orderCreateParam.getReceive_begin_time());
			orderData.setReceive_end_time(orderCreateParam.getReceive_end_time());
			orderData.setRemark(StringUtil.getRandomString(6));
			editOrder.setOrder_data(orderData);

			boolean result = orderService.editOrder(editOrder);
			Assert.assertEquals(result, true, "修改订单失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + "详细信息失败");

			result = stockOutService.asyncOrderToOutStockSheet(order_id);
			Assert.assertEquals(result, true, "订单 " + order_id + " 在一定的时间内没有生成对应的出库单");

			String msg = null;
			OutStockDetailBean stockOutDetail = stockOutService.getOutStockDetailInfo(order_id);
			Assert.assertEquals(result, true, "获取出库单 " + order_id + " 详细信息失败");

			for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
				String sku_id = detail.getSku_id();
				OutStockDetailBean.Detail s_detail = stockOutDetail.getDetails().stream()
						.filter(d -> d.getId().equals(sku_id)).findAny().orElse(null);
				if (s_detail == null) {
					msg = String.format("订单 %s 中的商品 [%s,%s] 没有同步到出库单中 ", order_id, sku_id, detail.getSku_name());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				} else {
					if (detail.getQuantity().compareTo(s_detail.getQuantity()) != 0) {
						msg = String.format("订单%s中的商品[%s,%s]修改的下单数没有同步到出库单,预期:%s,实际:%s", order_id, sku_id,
								s_detail.getName(), detail.getQuantity(), s_detail.getQuantity());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "订单订单,信息没有同步到出库单");
		} catch (Exception e) {
			logger.error("修改订单状态遇到错误: ", e);
			Assert.fail("修改订单状态遇到错误: ", e);
		}
	}

	@Test
	public void orderEditTestCase18() {
		ReporterCSS.title("测试点: 修改订单,添加商品,往商品列表中间添加商品");
		try {
			List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();
			for (OrderCreateParam.OrderSku orderSku : orderSkus) {
				if (orderSku.getCombine_goods_id() == null || orderSku.getCombine_goods_id().equals("")) {
					orderSkus.remove(orderSku);
					break;
				}
			}

			Assert.assertEquals(orderSkus.size() >= 3, true, "下单商品列表不足3个商品,此用例无法执行");
			OrderCreateParam.OrderSku orderSku = NumberUtil.roundNumberInList(orderSkus);
			orderSkus.remove(orderSku);

			orderCreateParam.setCombine_goods_map(new HashMap<>());
			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);

			Assert.assertEquals(orderResponse.getData().getNew_order_ids().size() > 0, true, "正常下单,断言成功");
			order_id = orderResponse.getData().getNew_order_ids().getString(0);

			// 把商品加在中间
			orderSkus.add(1, orderSku);

			// 封装修改订单对象
			OrderEditParam editOrder = new OrderEditParam();
			editOrder.setOrder_id(order_id);
			editOrder.setDetails(orderSkus);
			editOrder.setCombine_goods_map(new HashMap<>());

			// 设置收货时间
			OrderData orderData = editOrder.new OrderData();
			orderData.setReceive_begin_time(orderCreateParam.getReceive_begin_time());
			orderData.setReceive_end_time(orderCreateParam.getReceive_end_time());
			orderData.setRemark(StringUtil.getRandomString(6));
			editOrder.setOrder_data(orderData);

			List<String> order_sku_ids = orderSkus.stream().map(s -> s.getSku_id()).collect(Collectors.toList());

			boolean result = orderService.editOrder(editOrder);
			Assert.assertEquals(result, true, "修改订单失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + "详细信息失败");

			String msg = null;
			msg = String.format("订单:%s中商品预期的排列顺序从上至下依次为:%s", order_id, order_sku_ids);
			ReporterCSS.step(msg);
			logger.info(msg);

			List<String> actul_order_sku_ids = orderDetail.getDetails().stream().map(d -> d.getSku_id())
					.collect(Collectors.toList());

			msg = String.format("订单:%s中商品实际的排列顺序从上至下依次为:%s", order_id, actul_order_sku_ids);
			ReporterCSS.step(msg);
			logger.info(msg);

			Assert.assertEquals(actul_order_sku_ids, order_sku_ids, "编辑订单,往订单商品列表中间添加商品,订单商品排列顺序与预期不符");
		} catch (Exception e) {
			logger.error("修改订单状态遇到错误: ", e);
			Assert.fail("修改订单状态遇到错误: ", e);
		}
	}

	@Test
	public void orderEditTestCase19() {
		ReporterCSS.title("测试点: 按预设数修改订单状态(按下单时间)");
		try {
			OrderStatusPreconfigParam orderStatusPreconfigParam = new OrderStatusPreconfigParam();
			orderStatusPreconfigParam.setQuery_type(1);
			orderStatusPreconfigParam.setStart_date_new(todayStr + " 00:00");
			String tommrow = TimeUtil.calculateTime("yyyy-MM-dd", todayStr, 1, Calendar.DATE);
			orderStatusPreconfigParam.setEnd_date_new(tommrow + " 00:00");
			orderStatusPreconfigParam.setTo_status(5);
			orderStatusPreconfigParam.setCount("10");

			boolean result = orderService.preconfigUpdateOrderStatus(orderStatusPreconfigParam);
			Assert.assertEquals(result, true, "按预设数修改订单操作失败");

			OrderFilterParam orderFilterParam = new OrderFilterParam();
			orderFilterParam.setSearch_type(1);
			orderFilterParam.setQuery_type(1);
			orderFilterParam.setStart_date_new(todayStr + " 00:00");
			orderFilterParam.setEnd_date_new(tommrow + " 00:00");
			orderFilterParam.setLimit(10);
			orderFilterParam.setOffset(0);
			orderFilterParam.setSort_type("date_asc");

			List<OrderBean> orders = orderService.searchOrder(orderFilterParam);
			Assert.assertNotEquals(orders, null, "订单搜索过滤失败");

			List<String> order_ids = orders.stream().filter(o -> o.getStatus() < 5).map(o -> o.getId())
					.collect(Collectors.toList());

			ReporterCSS.warn("注意: 订单列表排序是按下单时间反序排列的,按预设数修改订单状态(下单时间)是正序的)");
			Assert.assertEquals(order_ids.size(), 0, "如下订单没有变为分拣中" + order_ids);
		} catch (Exception e) {
			logger.error("按预设数修改订单状态遇到错误: ", e);
			Assert.fail("按预设数修改订单状态遇到错误: ", e);
		}
	}

	@Test
	public void orderEditTestCase20() {
		ReporterCSS.title("测试点: 按预设数修改订单状态(按运营时间)");
		try {
			OrderCycle orderCycle = orderTool.getOrderOperationCycle(order_id);
			String time_config_id = orderCycle.getTime_config_id();
			String cycle_start_time = orderCycle.getCycle_start_time();
			String cycle_end_time = orderCycle.getCycle_end_time();
			// 按运营时间的方式 时间格式为 2020-07-07-06-00-00
			String time_format = cycle_start_time.replace(":", "-").replace(" ", "-") + "-00";

			OrderStatusPreconfigParam orderStatusPreconfigParam = new OrderStatusPreconfigParam();
			orderStatusPreconfigParam.setQuery_type(2);
			orderStatusPreconfigParam.setTime_config_id(time_config_id);
			orderStatusPreconfigParam.setStart_cycle_time(time_format);
			orderStatusPreconfigParam.setEnd_cycle_time(time_format);
			orderStatusPreconfigParam.setQuery_type(2);
			orderStatusPreconfigParam.setTo_status(5);
			orderStatusPreconfigParam.setCount("20");

			boolean result = orderService.preconfigUpdateOrderStatus(orderStatusPreconfigParam);
			Assert.assertEquals(result, true, "按预设数修改订单操作失败");

			OrderFilterParam orderFilterParam = new OrderFilterParam();
			orderFilterParam.setTime_config_id(time_config_id);
			orderFilterParam.setSearch_type(1);
			orderFilterParam.setQuery_type(2);
			orderFilterParam.setCycle_start_time(cycle_start_time);
			orderFilterParam.setCycle_end_time(cycle_end_time);
			orderFilterParam.setLimit(20);
			orderFilterParam.setOffset(0);

			List<OrderBean> orders = orderService.searchOrder(orderFilterParam);
			Assert.assertNotEquals(orders, null, "订单搜索过滤失败");

			List<String> order_ids = orders.stream().filter(o -> o.getStatus() < 5).map(o -> o.getId())
					.collect(Collectors.toList());

			Assert.assertEquals(order_ids.size(), 0, "如下订单没有变为分拣中" + order_ids);
		} catch (Exception e) {
			logger.error("按预设数修改订单状态遇到错误: ", e);
			Assert.fail("按预设数修改订单状态遇到错误: ", e);
		}
	}

	@Test
	public void orderEditTestCase21() {
		ReporterCSS.title("测试点: 按预设数修改订单状态(按收货时间)");
		try {
			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + "详细信息失败");
			String receive_start_date = orderDetail.getCustomer().getReceive_begin_time().substring(0, 10) + " 00:00";
			String receive_end_date = TimeUtil.calculateTime("yyyy-MM-dd 00:00", receive_start_date, 1, Calendar.DATE);

			OrderStatusPreconfigParam orderStatusPreconfigParam = new OrderStatusPreconfigParam();
			orderStatusPreconfigParam.setQuery_type(3);
			orderStatusPreconfigParam.setReceive_start_date_new(receive_start_date);
			orderStatusPreconfigParam.setReceive_end_date_new(receive_end_date);
			orderStatusPreconfigParam.setTo_status(5);
			orderStatusPreconfigParam.setCount("50");

			boolean result = orderService.preconfigUpdateOrderStatus(orderStatusPreconfigParam);
			Assert.assertEquals(result, true, "按预设数修改订单操作失败");

			OrderFilterParam orderFilterParam = new OrderFilterParam();
			orderFilterParam.setSearch_type(1);
			orderFilterParam.setQuery_type(3);
			orderFilterParam.setReceive_start_date_new(receive_start_date);
			orderFilterParam.setReceive_end_date_new(receive_end_date);
			orderFilterParam.setLimit(50);
			orderFilterParam.setOffset(0);

			List<OrderBean> orders = orderService.searchOrder(orderFilterParam);
			Assert.assertNotEquals(orders, null, "订单搜索过滤失败");

			List<String> order_ids = orders.stream().filter(o -> o.getStatus() < 5).map(o -> o.getId())
					.collect(Collectors.toList());

			Assert.assertEquals(order_ids.size(), 0, "如下订单没有变为分拣中" + order_ids);
		} catch (Exception e) {
			logger.error("按预设数修改订单状态遇到错误: ", e);
			Assert.fail("按预设数修改订单状态遇到错误: ", e);
		}
	}
}
