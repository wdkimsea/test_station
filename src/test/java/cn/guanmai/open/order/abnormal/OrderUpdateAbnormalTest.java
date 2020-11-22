package cn.guanmai.open.order.abnormal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.customer.OpenCustomerBean;
import cn.guanmai.open.bean.order.param.OrderCreateParam;
import cn.guanmai.open.bean.order.param.OrderProductParam;
import cn.guanmai.open.bean.order.param.OrderUpdateParam;
import cn.guanmai.open.bean.product.OpenReceiveTimeBean;
import cn.guanmai.open.bean.product.OpenSaleSkuBean;
import cn.guanmai.open.bean.product.OpenSalemenuBean;
import cn.guanmai.open.bean.product.param.OpenSaleSkuFilterParam;
import cn.guanmai.open.impl.customer.OpenCustomerServiceImpl;
import cn.guanmai.open.impl.order.OpenOrderServiceImpl;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.impl.product.OpenSalemenuServiceImpl;
import cn.guanmai.open.interfaces.customer.OpenCustomerService;
import cn.guanmai.open.interfaces.order.OpenOrderService;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.interfaces.product.OpenSalemenuService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Jun 12, 2019 3:56:38 PM 
* @des 开放平台修改订单异常测试
* @version 1.0 
*/
public class OrderUpdateAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OrderUpdateAbnormalTest.class);
	private OpenOrderService openOrderService;
	private OpenCustomerService openCustomerService;
	private OpenSalemenuService openSalemenuService;
	private OpenCategoryService openCategoryService;
	private OrderCreateParam orderCreateParam;
	private List<OrderProductParam> products;
	private String receive_time_start;
	private String receive_time_end;
	private String customer_id;
	private String time_config_id;
	private String order_id;
	private Map<String, List<OpenSalemenuBean>> openSalemenuGroup;
	private OrderUpdateParam orderUpdateParam;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openCustomerService = new OpenCustomerServiceImpl(access_token);
		openOrderService = new OpenOrderServiceImpl(access_token);
		openSalemenuService = new OpenSalemenuServiceImpl(access_token);
		openCategoryService = new OpenCategoryServiceImpl(access_token);

	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			List<OpenSalemenuBean> openSalemenus = null;
			List<OpenCustomerBean> openCustomers = openCustomerService.searchCustomer(null, null, 0, 100);
			Assert.assertNotEquals(openCustomers, null, "搜索查询商户失败");
			boolean result = false;
			for (OpenCustomerBean customer : openCustomers) {

				result = openCustomerService.checkCustomerOrderStatus(customer.getCustomer_id());
				if (!result) {
					continue;
				}
				customer_id = customer.getCustomer_id();
				openSalemenus = openSalemenuService.searchSalemenu(customer_id, 1);
				Assert.assertNotNull(openSalemenus, "根据商户查询报价单集合失败");
				if (openSalemenus.size() == 0) {
					continue;
				} else {
					openSalemenuGroup = openSalemenus.stream()
							.collect(Collectors.groupingBy(OpenSalemenuBean::getTime_config_id));
					break;
				}
			}

			// 取第一个运营时间的所有的报价单
			time_config_id = openSalemenuGroup.keySet().iterator().next();
			openSalemenus = openSalemenuGroup.get(time_config_id);
			List<OpenSaleSkuBean> openSaleSkus = new ArrayList<>();

			OpenSaleSkuFilterParam filterParam = new OpenSaleSkuFilterParam();
			for (OpenSalemenuBean openSalemenu : openSalemenus) {
				filterParam.setSalemenu_id(openSalemenu.getId());
				List<OpenSaleSkuBean> tempOpenSaleSkus = openCategoryService.seachSaleSku(filterParam);
				Assert.assertNotEquals(tempOpenSaleSkus, null, "以报价单ID搜索过滤商品遇到错误");
				openSaleSkus
						.addAll(tempOpenSaleSkus.stream().filter(s -> s.getState() == 1).collect(Collectors.toList()));
			}
			products = new ArrayList<>();

			OrderProductParam product = null;
			for (OpenSaleSkuBean openSaleSku : openSaleSkus) {
				product = new OrderProductParam();
				product.setSku_id(openSaleSku.getId());
				product.setCount("10");
				product.setIs_price_timing("0");
				product.setPrice("4");
				products.add(product);
				if (products.size() >= 8) {
					break;
				}
			}

			OpenReceiveTimeBean openReceiveTime = openCategoryService.getReceiveTime(time_config_id);
			Assert.assertNotEquals(openReceiveTime, null, "获取运营时间的收货时间信息失败");

			String receive_time_day = openReceiveTime.getReceive_time().keySet().iterator().next();
			List<String> receive_time_list = openReceiveTime.getReceive_time().get(receive_time_day);
			receive_time_start = receive_time_list.get(0);
			receive_time_end = receive_time_list.get(1);

			orderCreateParam = new OrderCreateParam();
			orderCreateParam.setProducts(products);
			orderCreateParam.setReceive_begin_time(receive_time_start);
			orderCreateParam.setReceive_end_time(receive_time_end);
			orderCreateParam.setCustomer_id(customer_id);
			orderCreateParam.setTime_config_id(time_config_id);

			order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertNotEquals(order_id, null, "通过开放平台下单失败");

			orderUpdateParam = new OrderUpdateParam();
			orderUpdateParam.setOrder_id(order_id);
			orderUpdateParam.setReceive_begin_time(receive_time_start);
			orderUpdateParam.setReceive_end_time(receive_time_end);
		} catch (Exception e) {
			logger.error("商户下单遇到错误: ", e);
			Assert.fail("商户下单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase01() {
		ReporterCSS.title("测试点: 异常修改订单,不传入订单号,预期修改失败");
		try {
			orderUpdateParam.setOrder_id("");
			boolean result = openOrderService.updateOrder(orderUpdateParam);
			Assert.assertEquals(result, false, "异常修改订单,不传入订单号,预期修改失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase02() {
		ReporterCSS.title("测试点: 异常修改订单,传入错误的订单号,预期修改失败");
		try {
			orderUpdateParam.setOrder_id(order_id + "0");
			boolean result = openOrderService.updateOrder(orderUpdateParam);
			Assert.assertEquals(result, false, "异常修改订单,传入错误的订单号,预期修改失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase03() {
		ReporterCSS.title("测试点: 异常修改订单,传入别的站点的订单号,预期修改失败");
		try {
			orderUpdateParam.setOrder_id("PL234566");
			boolean result = openOrderService.updateOrder(orderUpdateParam);
			Assert.assertEquals(result, false, "异常修改订单,传入错误的订单号,预期修改失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase04() {
		ReporterCSS.title("测试点: 异常修改订单,收货时间传入空格,预期修改失败");
		try {
			orderUpdateParam.setReceive_begin_time(" ");
			boolean result = openOrderService.updateOrder(orderUpdateParam);
			Assert.assertEquals(result, false, "异常修改订单,收货时间传入空格,预期修改失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase05() {
		ReporterCSS.title("测试点: 异常修改订单,收货时间传入非候选值,预期修改失败");
		try {
			String receive_begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm");
			orderUpdateParam.setReceive_begin_time(receive_begin_time);
			boolean result = openOrderService.updateOrder(orderUpdateParam);
			Assert.assertEquals(result, false, "异常修改订单,收货时间传入非候选值,预期修改失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase06() {
		ReporterCSS.title("测试点: 异常修改订单,收货起始时间晚于收货结束时间,预期修改失败");
		try {
			orderUpdateParam.setReceive_begin_time(receive_time_end);
			orderUpdateParam.setReceive_end_time(receive_time_start);
			boolean result = openOrderService.updateOrder(orderUpdateParam);
			Assert.assertEquals(result, false, "异常修改订单,收货时间传入非候选值,预期修改失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase07() {
		ReporterCSS.title("测试点: 异常修改订单,状态值传入非候选值,预期修改失败");
		try {
			orderUpdateParam.setStatus(8);
			boolean result = openOrderService.updateOrder(orderUpdateParam);
			Assert.assertEquals(result, false, "异常修改订单,状态值传入非候选值,预期修改失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase08() {
		ReporterCSS.title("测试点: 异常修改订单,删除订单里的商品,不传入订单号,断言失败");
		try {
			List<String> sku_ids = Arrays.asList(products.get(0).getSku_id());
			boolean result = openOrderService.deleteOrderSkus("", sku_ids);
			Assert.assertEquals(result, false, "异常修改订单,删除订单里的商品,不传入订单号,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase09() {
		ReporterCSS.title("测试点: 异常修改订单,删除订单里的商品,传入错误的订单号,断言失败");
		try {
			List<String> sku_ids = Arrays.asList(products.get(0).getSku_id());
			boolean result = openOrderService.deleteOrderSkus(order_id + "1", sku_ids);
			Assert.assertEquals(result, false, "异常修改订单,删除订单里的商品,不传入订单号,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase10() {
		ReporterCSS.title("测试点: 异常修改订单,删除订单里的商品,传入空的商品ID列表,断言失败");
		try {
			boolean result = openOrderService.deleteOrderSkus(order_id, new ArrayList<>());
			Assert.assertEquals(result, false, "异常修改订单,删除订单里的商品,传入空的商品ID列表,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase11() {
		ReporterCSS.title("测试点: 异常修改订单,删除订单里的商品,传入订单中所有的商品ID,断言失败");
		try {
			List<String> sku_ids = products.stream().map(p -> p.getSku_id()).collect(Collectors.toList());
			boolean result = openOrderService.deleteOrderSkus(order_id, sku_ids);
			Assert.assertEquals(result, false, "异常修改订单,删除订单里的商品,传入订单中所有的商品ID,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase12() {
		ReporterCSS.title("测试点: 异常修改订单,删除订单里的商品,传入订单中不存在的商品ID,断言失败");
		try {
			List<String> sku_ids = Arrays.asList("D9504604");
			boolean result = openOrderService.deleteOrderSkus(order_id, sku_ids);
			Assert.assertEquals(result, false, "异常修改订单,删除订单里的商品,传入订单中不存在的商品ID,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase13() {
		ReporterCSS.title("测试点: 异常修改订单,修改已经删除的订单,断言失败");
		try {
			boolean result = openOrderService.deleteOrder(order_id);
			Assert.assertEquals(result, true, "删除订单失败");
			List<String> sku_ids = Arrays.asList(products.get(0).getSku_id());
			result = openOrderService.deleteOrderSkus(order_id, sku_ids);
			Assert.assertEquals(result, false, "异常修改订单,修改已经删除的订单,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase14() {
		ReporterCSS.title("测试点: 异常修改订单,订单添加商品,商品ID输入空值,断言失败");
		try {

			OrderProductParam orderProductParam = products.get(0);

			List<String> sku_ids = Arrays.asList(orderProductParam.getSku_id());
			boolean result = openOrderService.deleteOrderSkus(order_id, sku_ids);
			Assert.assertEquals(result, true, "修改订单,删除商品,断言成功");

			orderProductParam.setSku_id("");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			result = openOrderService.addOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,订单添加商品,商品ID输入空值,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase15() {
		ReporterCSS.title("测试点: 异常修改订单,订单添加商品,商品数量输入空值,断言失败");
		try {

			OrderProductParam orderProductParam = products.get(0);

			List<String> sku_ids = Arrays.asList(orderProductParam.getSku_id());
			boolean result = openOrderService.deleteOrderSkus(order_id, sku_ids);
			Assert.assertEquals(result, true, "修改订单,删除商品,断言成功");

			orderProductParam.setCount("");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			result = openOrderService.addOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,订单添加商品,商品数量输入空值,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase16() {
		ReporterCSS.title("测试点: 异常修改订单,订单添加商品,商品数量输入空值,断言失败");
		try {

			OrderProductParam orderProductParam = products.get(0);

			List<String> sku_ids = Arrays.asList(orderProductParam.getSku_id());
			boolean result = openOrderService.deleteOrderSkus(order_id, sku_ids);
			Assert.assertEquals(result, true, "修改订单,删除商品,断言成功");

			orderProductParam.setCount("");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			result = openOrderService.addOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,订单添加商品,商品数量输入空值,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase17() {
		ReporterCSS.title("测试点: 异常修改订单,订单添加商品,商品数量输入非数值,断言失败");
		try {

			OrderProductParam orderProductParam = products.get(0);

			List<String> sku_ids = Arrays.asList(orderProductParam.getSku_id());
			boolean result = openOrderService.deleteOrderSkus(order_id, sku_ids);
			Assert.assertEquals(result, true, "修改订单,删除商品,断言成功");

			orderProductParam.setCount("A");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			result = openOrderService.addOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,订单添加商品,商品数量输入非数值,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase18() {
		ReporterCSS.title("测试点: 异常修改订单,订单添加商品,商品数量输入负数,断言失败");
		try {

			OrderProductParam orderProductParam = products.get(0);

			List<String> sku_ids = Arrays.asList(orderProductParam.getSku_id());
			boolean result = openOrderService.deleteOrderSkus(order_id, sku_ids);
			Assert.assertEquals(result, true, "修改订单,删除商品,断言成功");

			orderProductParam.setCount("-8");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			result = openOrderService.addOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,订单添加商品,商品数量输入负数,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase19() {
		ReporterCSS.title("测试点: 异常修改订单,订单添加商品,商品价格输入为空,断言失败");
		try {

			OrderProductParam orderProductParam = products.get(0);

			List<String> sku_ids = Arrays.asList(orderProductParam.getSku_id());
			boolean result = openOrderService.deleteOrderSkus(order_id, sku_ids);
			Assert.assertEquals(result, true, "修改订单,删除商品,断言成功");

			orderProductParam.setPrice("");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			result = openOrderService.addOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,订单添加商品,商品价格输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase20() {
		ReporterCSS.title("测试点: 异常修改订单,订单添加商品,商品价格输入非数字,断言失败");
		try {

			OrderProductParam orderProductParam = products.get(0);

			List<String> sku_ids = Arrays.asList(orderProductParam.getSku_id());
			boolean result = openOrderService.deleteOrderSkus(order_id, sku_ids);
			Assert.assertEquals(result, true, "修改订单,删除商品,断言成功");

			orderProductParam.setPrice("A");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			result = openOrderService.addOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,订单添加商品,商品价格输入非数字,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase21() {
		ReporterCSS.title("测试点: 异常修改订单,订单添加商品,商品价格输入负数,断言失败");
		try {

			OrderProductParam orderProductParam = products.get(0);

			List<String> sku_ids = Arrays.asList(orderProductParam.getSku_id());
			boolean result = openOrderService.deleteOrderSkus(order_id, sku_ids);
			Assert.assertEquals(result, true, "修改订单,删除商品,断言成功");

			orderProductParam.setPrice("-5");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			result = openOrderService.addOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,订单添加商品,商品价格输入负数,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase22() {
		ReporterCSS.title("测试点: 异常修改订单,订单添加商品,商品价格输入多位小数,断言失败");
		try {

			OrderProductParam orderProductParam = products.get(0);

			List<String> sku_ids = Arrays.asList(orderProductParam.getSku_id());
			boolean result = openOrderService.deleteOrderSkus(order_id, sku_ids);
			Assert.assertEquals(result, true, "修改订单,删除商品,断言成功");

			orderProductParam.setPrice("3.145");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			result = openOrderService.addOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,订单添加商品,商品价格输入多位小数,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase23() {
		ReporterCSS.title("测试点: 异常修改订单,订单添加商品,添加订单中已经存在的商品,断言失败");
		try {

			OrderProductParam orderProductParam = products.get(0);

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			boolean result = openOrderService.addOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,订单添加商品,添加订单中已经存在的商品,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase24() {
		ReporterCSS.title("测试点: 异常修改订单,修改订单商品,商品ID输入为空,断言失败");
		try {

			OrderProductParam orderProductParam = products.get(0);
			orderProductParam.setSku_id("");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			boolean result = openOrderService.updateOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,订单添加商品,商品价格输入多位小数,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase25() {
		ReporterCSS.title("测试点: 异常修改订单,修改订单商品,商品ID输入为订单中不存在的商品ID,断言失败");
		try {

			OrderProductParam orderProductParam = products.get(0);

			List<String> sku_ids = Arrays.asList(orderProductParam.getSku_id());
			boolean result = openOrderService.deleteOrderSkus(order_id, sku_ids);
			Assert.assertEquals(result, true, "修改订单,删除商品,断言成功");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			result = openOrderService.updateOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,修改订单商品,商品ID输入为订单中不存在的商品ID,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase26() {
		ReporterCSS.title("测试点: 异常修改订单,修改订单商品,商品下单数输入为空,断言失败");
		try {
			OrderProductParam orderProductParam = products.get(0);
			orderProductParam.setCount("");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			boolean result = openOrderService.updateOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,修改订单商品,商品ID输入为订单中不存在的商品ID,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase27() {
		ReporterCSS.title("测试点: 异常修改订单,修改订单商品,商品下单数输入为非数字,断言失败");
		try {
			OrderProductParam orderProductParam = products.get(0);
			orderProductParam.setCount("A");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			boolean result = openOrderService.updateOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,修改订单商品,商品下单数输入为非数字,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase28() {
		ReporterCSS.title("测试点: 异常修改订单,修改订单商品,商品下单数输入负数,断言失败");
		try {
			OrderProductParam orderProductParam = products.get(0);
			orderProductParam.setCount("-9");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			boolean result = openOrderService.updateOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,修改订单商品,商品下单数输入负数,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase29() {
		ReporterCSS.title("测试点: 异常修改订单,修改订单商品,商品下单数输入多位小数,断言失败");
		try {
			OrderProductParam orderProductParam = products.get(0);
			orderProductParam.setCount("5.678");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			boolean result = openOrderService.updateOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,修改订单商品,商品下单数输入多位小数,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase30() {
		ReporterCSS.title("测试点: 异常修改订单,修改订单商品,商品金额输入为空,断言失败");
		try {
			OrderProductParam orderProductParam = products.get(0);
			orderProductParam.setPrice("");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			boolean result = openOrderService.updateOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,修改订单商品,商品金额输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase31() {
		ReporterCSS.title("测试点: 异常修改订单,修改订单商品,商品金额输入为空,断言失败");
		try {
			OrderProductParam orderProductParam = products.get(0);
			orderProductParam.setPrice("");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			boolean result = openOrderService.updateOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,修改订单商品,商品金额输入为空,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase32() {
		ReporterCSS.title("测试点: 异常修改订单,修改订单商品,商品金额输入非数字,断言失败");
		try {
			OrderProductParam orderProductParam = products.get(0);
			orderProductParam.setPrice("A");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			boolean result = openOrderService.updateOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,修改订单商品,商品金额输入非数字,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase33() {
		ReporterCSS.title("测试点: 异常修改订单,修改订单商品,商品金额输入负数,断言失败");
		try {
			OrderProductParam orderProductParam = products.get(0);
			orderProductParam.setPrice("-5");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			boolean result = openOrderService.updateOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,修改订单商品,商品金额输入负数,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase34() {
		ReporterCSS.title("测试点: 异常修改订单,修改订单商品,商品金额输入多位小数,断言失败");
		try {
			OrderProductParam orderProductParam = products.get(0);
			orderProductParam.setPrice("5.126");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			boolean result = openOrderService.updateOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,修改订单商品,商品金额输入多位小数,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateAbnormalTestCase35() {
		ReporterCSS.title("测试点: 异常修改订单,修改处于待分拣订单的商品的出库数,断言失败");
		try {
			OrderProductParam orderProductParam = NumberUtil.roundNumberInList(products);
			orderProductParam.setReal_count("8");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			boolean result = openOrderService.updateOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,修改订单商品,修改商品出库数,订单处于待分拣中,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "orderUpdateAbnormalTestCase35" })
	public void orderUpdateAbnormalTestCase36() {
		ReporterCSS.title("测试点: 修改订单状态,断言成功");
		try {
			OrderUpdateParam orderUpdateParam = new OrderUpdateParam();
			orderUpdateParam.setOrder_id(order_id);
			orderUpdateParam.setStatus(5);

			boolean result = openOrderService.updateOrder(orderUpdateParam);
			Assert.assertEquals(result, true, "修改订单状态,断言成功");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "orderUpdateAbnormalTestCase36" })
	public void orderUpdateAbnormalTestCase37() {
		ReporterCSS.title("测试点: 异常修改订单,修改订单商品,修改商品出库数,出库数输入负数,断言失败");
		try {
			OrderProductParam orderProductParam = NumberUtil.roundNumberInList(products);
			orderProductParam.setReal_count("-10");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			boolean result = openOrderService.updateOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,修改订单商品,修改商品出库数,出库数输入负数,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "orderUpdateAbnormalTestCase36" })
	public void orderUpdateAbnormalTestCase38() {
		ReporterCSS.title("测试点: 异常修改订单,修改订单商品,修改商品出库数,出库数输入字符,断言失败");
		try {
			OrderProductParam orderProductParam = NumberUtil.roundNumberInList(products);
			orderProductParam.setReal_count("a");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			boolean result = openOrderService.updateOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,修改订单商品,修改商品出库数,出库数输入字符,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "orderUpdateAbnormalTestCase36" })
	public void orderUpdateAbnormalTestCase39() {
		ReporterCSS.title("测试点: 异常修改订单,修改订单商品,修改商品出库数,出库数输入多位小数,断言失败");
		try {
			OrderProductParam orderProductParam = NumberUtil.roundNumberInList(products);
			orderProductParam.setReal_count("4.123");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			boolean result = openOrderService.updateOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,修改订单商品,修改商品出库数,出库数输入字符,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "orderUpdateAbnormalTestCase36" })
	public void orderUpdateAbnormalTestCase40() {
		ReporterCSS.title("测试点: 异常修改订单,修改订单商品,同时修改商品下单数和出库数,断言失败");
		try {
			OrderProductParam orderProductParam = NumberUtil.roundNumberInList(products);
			orderProductParam.setReal_count("9");
			orderProductParam.setCount("8.12");

			List<OrderProductParam> orderProductParamList = Arrays.asList(orderProductParam);
			boolean result = openOrderService.updateOrderSkus(order_id, orderProductParamList);
			Assert.assertEquals(result, false, "异常修改订单,修改订单商品,修改商品出库数,出库数输入字符,断言失败");
		} catch (Exception e) {
			logger.error("修改订单遇到错误: ", e);
			Assert.fail("修改订单遇到错误: ", e);
		}
	}

}
