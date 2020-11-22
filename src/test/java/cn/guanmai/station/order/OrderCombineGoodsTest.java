package cn.guanmai.station.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.category.SalemenuBean;
import cn.guanmai.station.bean.category.param.CombineGoodsParam;
import cn.guanmai.station.bean.category.param.SalemenuFilterParam;
import cn.guanmai.station.bean.order.CustomerBean;
import cn.guanmai.station.bean.order.OrderReceiveTimeBean;
import cn.guanmai.station.bean.order.OrderResponseBean;
import cn.guanmai.station.bean.order.OrderResponseBean.Data.NotEnoughInventories;
import cn.guanmai.station.bean.order.param.OrderCreateParam;
import cn.guanmai.station.bean.order.param.OrderEditParam;
import cn.guanmai.station.bean.order.param.OrderEditParam.OrderData;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.category.CombineGoodsServiceImpl;
import cn.guanmai.station.impl.category.SalemenuServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.category.CombineGoodsService;
import cn.guanmai.station.interfaces.category.SalemenuService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author: liming
 * @Date: 2020年7月9日 下午4:07:53
 * @description: 下单下组合商品
 * @version: 1.0
 */

public class OrderCombineGoodsTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(OrderCombineGoodsTest.class);

	private CombineGoodsService combineGoodsService;
	private SalemenuService salemenuService;
	private OrderService orderService;
	private List<String> salemenu_ids;
	private String order_id;
	private OrderCreateParam orderCreateParam;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		combineGoodsService = new CombineGoodsServiceImpl(headers);
		salemenuService = new SalemenuServiceImpl(headers);
		orderService = new OrderServiceImpl(headers);

		LoginUserInfoService loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		try {
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账号相关信息失败");

			JSONArray user_permission = loginUserInfo.getUser_permission();
			Assert.assertEquals(user_permission.contains("add_combine_goods"), true, "此登录账号无创建组合商品权限");

			SalemenuFilterParam salemenuFilterParam = new SalemenuFilterParam();
			salemenuFilterParam.setType(1);
			salemenuFilterParam.setWith_sku_num(1);

			List<SalemenuBean> salemenus = salemenuService.searchSalemenu(salemenuFilterParam);
			Assert.assertNotEquals(salemenus, null, "搜索过滤报价单失败");

			salemenu_ids = salemenus.stream().filter(s -> s.getSku_num() >= 2).map(s -> s.getId())
					.collect(Collectors.toList());
			Assert.assertEquals(salemenu_ids.size() > 2, true, "此站点商品数目大于2的报价单不足两个");
		} catch (Exception e) {
			logger.error("初始化数据过程中遇到错误", e);
			Assert.fail("初始化数据过程中遇到错误", e);
		}
	}

	@Test
	public void orderCombineGoodsTestCase01() {
		ReporterCSS.step("测试点: 下单组合商品");
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

			List<OrderReceiveTimeBean.ReceiveTime> receiveTimes = orderReceiveTime.getReceive_times().stream()
					.filter(r -> r.getTimes().size() >= 2).collect(Collectors.toList());
			Assert.assertEquals(receiveTimes.size() > 0, true, "运营时间的收货时间无法取值了(当前时间过了收货时间结束时间或者收货起始和收货结束时间一致)");

			OrderReceiveTimeBean.ReceiveTime receiveTime = NumberUtil.roundNumberInList(receiveTimes);
			List<String> receive_times = receiveTime.getTimes();
			int index = new Random().nextInt(receive_times.size() - 1);
			String receive_begin_time = receive_times.get(index);
			String receive_end_time = receive_times.get(index + 1);

			// 下单商品集合
			String[] search_texts = new String[] { "A", "B", "C", "J", "Y" };
			orderCreateParam = orderService.searchOrderSkus(address_id, time_config_id, search_texts, 20);
			Assert.assertEquals(orderCreateParam != null && orderCreateParam.getDetails().size() > 0, true,
					"下单搜索搜商品列表为空");

			// 如果下单商品中有组合商品,那直接下单,没有则要新建
			if (orderCreateParam.getCombine_goods_map().size() == 0) {
				// 按报价单分组
				List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();
				Map<String, List<OrderCreateParam.OrderSku>> orderSkusMap = orderSkus.stream()
						.collect(Collectors.groupingBy(OrderCreateParam.OrderSku::getSalemenu_id));
				List<OrderCreateParam.OrderSku> combineOrderSkus = null;
				String salemenu_id = null;
				for (String key : orderSkusMap.keySet()) {
					if (orderSkusMap.get(key).size() >= 2) {
						combineOrderSkus = orderSkusMap.get(key);
						salemenu_id = key;
						break;
					}
				}

				Assert.assertNotEquals(combineOrderSkus, null, "没有找到新建组合商品可用的销售SKU");

				CombineGoodsParam combineGoodsCreateParam = new CombineGoodsParam();
				String combine_goods_name = "AT_" + StringUtil.getRandomNumber(6);
				combineGoodsCreateParam.setName(combine_goods_name);
				String sale_unit_name = "包";
				combineGoodsCreateParam.setSale_unit_name(sale_unit_name);
				combineGoodsCreateParam.setSalemenu_ids(Arrays.asList(salemenu_id));
				combineGoodsCreateParam.setState(1);
				String desc = StringUtil.getRandomNumber(6);
				combineGoodsCreateParam.setDesc(desc);

				List<CombineGoodsParam.Spu> spus = new ArrayList<CombineGoodsParam.Spu>();
				List<CombineGoodsParam.Sku> skus = new ArrayList<CombineGoodsParam.Sku>();

				List<String> spu_ids = new ArrayList<String>();
				for (OrderCreateParam.OrderSku orderSku : combineOrderSkus) {
					String spu_id = orderSku.getSpu_id();
					String sku_id = orderSku.getSku_id();
					if (spu_ids.contains(spu_id)) {
						continue;
					} else {
						spu_ids.add(spu_id);
					}

					CombineGoodsParam.Spu spu = combineGoodsCreateParam.new Spu();
					spu.setSpu_id(spu_id);
					BigDecimal spu_quantity = NumberUtil.getRandomNumber(5, 10, 1);
					spu.setQuantity(spu_quantity);
					spus.add(spu);

					CombineGoodsParam.Sku sku = combineGoodsCreateParam.new Sku();
					sku.setSalemenu_id(salemenu_id);
					sku.setSku_id(sku_id);
					sku.setSpu_id(spu_id);
					skus.add(sku);

					if (spus.size() >= 4) {
						break;
					}
				}

				combineGoodsCreateParam.setSpus(spus);
				combineGoodsCreateParam.setSkus(skus);

				combineGoodsCreateParam.setImages(new ArrayList<String>());
				String combine_goods_id = combineGoodsService.createCombineGoods(combineGoodsCreateParam);
				Assert.assertNotEquals(combine_goods_id, null, "新建组合商品失败");

				search_texts = new String[] { combine_goods_name, "A", "B", "C", "J" };
				orderCreateParam = orderService.searchOrderSkus(address_id, time_config_id, search_texts, 20);
				Assert.assertEquals(orderCreateParam.getCombine_goods_map().size() > 0, true, "下单商品中组合商品为空,与预期不符");
			}

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
			logger.error("下单组合商品遇到错误", e);
			Assert.fail("下单组合商品遇到错误", e);
		}
	}

	@Test(dependsOnMethods = { "orderCombineGoodsTestCase01" })
	public void orderCombineGoodsTestCase02() {
		ReporterCSS.title("测试点: 订单修改,里面包含组合商品");
		try {
			// 封装修改订单对象
			OrderEditParam editOrder = new OrderEditParam();
			editOrder.setOrder_id(order_id);
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
		} catch (Exception e) {
			logger.error("下单组合商品遇到错误", e);
			Assert.fail("下单组合商品遇到错误", e);
		}
	}

	@Test(dependsOnMethods = { "orderCombineGoodsTestCase02" })
	public void orderCombineGoodsTestCase03() {
		ReporterCSS.title("测试点: 订单修改,移除组合商品");
		try {
			// 封装修改订单对象
			OrderEditParam editOrder = new OrderEditParam();
			editOrder.setOrder_id(order_id);
			List<OrderCreateParam.OrderSku> editOrderSkus = new ArrayList<>();
			List<String> sku_ids = new ArrayList<String>();
			for (OrderCreateParam.OrderSku orderSku : orderCreateParam.getDetails()) {
				if (!sku_ids.contains(orderSku.getSku_id())) {
					orderSku.setCombine_goods_id("");
					orderSku.setIs_combine_goods(false);
					editOrderSkus.add(orderSku);
				}
				sku_ids.add(orderSku.getSku_id());

			}
			editOrder.setDetails(editOrderSkus);
			editOrder.setCombine_goods_map(new HashMap<>());
			// 设置收货时间
			OrderData orderData = editOrder.new OrderData();
			orderData.setReceive_begin_time(orderCreateParam.getReceive_begin_time());
			orderData.setReceive_end_time(orderCreateParam.getReceive_end_time());
			orderData.setRemark(StringUtil.getRandomString(6));
			editOrder.setOrder_data(orderData);

			boolean result = orderService.editOrder(editOrder);
			Assert.assertEquals(result, true, "修改订单失败");
		} catch (Exception e) {
			logger.error("下单组合商品遇到错误", e);
			Assert.fail("下单组合商品遇到错误", e);
		}
	}

}
