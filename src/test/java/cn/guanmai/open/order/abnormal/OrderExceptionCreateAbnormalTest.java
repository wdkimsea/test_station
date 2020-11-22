package cn.guanmai.open.order.abnormal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.customer.OpenCustomerBean;
import cn.guanmai.open.bean.order.param.OrderAbnormalCreateParam;
import cn.guanmai.open.bean.order.param.OrderCreateParam;
import cn.guanmai.open.bean.order.param.OrderProductParam;
import cn.guanmai.open.bean.order.param.OrderRefundCreateParam;
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

/* 
* @author liming 
* @date Jun 13, 2019 10:17:38 AM 
* @des 订单商品异常异常添加测试
* @version 1.0 
*/
public class OrderExceptionCreateAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OrderExceptionCreateAbnormalTest.class);
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

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openCustomerService = new OpenCustomerServiceImpl(access_token);
		openOrderService = new OpenOrderServiceImpl(access_token);
		openSalemenuService = new OpenSalemenuServiceImpl(access_token);
		openCategoryService = new OpenCategoryServiceImpl(access_token);
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

		} catch (Exception e) {
			logger.error("搜索过滤下单商户遇到错误: ", e);
			Assert.fail("搜索过滤下单商户遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertNotEquals(order_id, null, "通过开放平台下单失败");

		} catch (Exception e) {
			logger.error("商户下单遇到错误: ", e);
			Assert.fail("商户下单遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionCreateAbnormalTestCase01() {
		Reporter.log("测试点: 新建订单[商品异常]异常测试,不传入订单ID,断言失败");
		try {
			String sku_id = products.get(0).getSku_id();

			OrderAbnormalCreateParam orderAbnormalCreateParam = new OrderAbnormalCreateParam();
			orderAbnormalCreateParam.setSku_id(sku_id);
			orderAbnormalCreateParam.setException_reason("1");
			orderAbnormalCreateParam.setFinal_count("4");

			List<OrderAbnormalCreateParam> orderAbnormalCreateParamList = Arrays.asList(orderAbnormalCreateParam);

			boolean result = openOrderService.createOrderAbnormal("", orderAbnormalCreateParamList);
			Assert.assertEquals(result, false, "新建订单[商品异常]异常测试,不传入订单ID,断言失败");
		} catch (Exception e) {
			logger.error("新建订单商品异常遇到错误: ", e);
			Assert.fail("新建订单商品异常遇到错误: ", e);
		}

	}

	@Test
	public void orderExceptionCreateAbnormalTestCase02() {
		Reporter.log("测试点: 新建订单[商品异常]异常测试,传入错误的订单ID,断言失败");
		try {
			String sku_id = products.get(0).getSku_id();

			OrderAbnormalCreateParam orderAbnormalCreateParam = new OrderAbnormalCreateParam();
			orderAbnormalCreateParam.setSku_id(sku_id);
			orderAbnormalCreateParam.setException_reason("1");
			orderAbnormalCreateParam.setFinal_count("4");

			List<OrderAbnormalCreateParam> orderAbnormalCreateParamList = Arrays.asList(orderAbnormalCreateParam);

			boolean result = openOrderService.createOrderAbnormal(order_id + "0", orderAbnormalCreateParamList);
			Assert.assertEquals(result, false, "新建订单[商品异常]异常测试,传入错误的订单ID,断言失败");
		} catch (Exception e) {
			logger.error("新建订单商品异常遇到错误: ", e);
			Assert.fail("新建订单商品异常遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionCreateAbnormalTestCase03() {
		Reporter.log("测试点: 新建订单[商品异常]异常测试,不传入商品ID,断言失败");
		try {
			OrderAbnormalCreateParam orderAbnormalCreateParam = new OrderAbnormalCreateParam();
			orderAbnormalCreateParam.setSku_id("");
			orderAbnormalCreateParam.setException_reason("1");
			orderAbnormalCreateParam.setFinal_count("4");

			List<OrderAbnormalCreateParam> orderAbnormalCreateParamList = Arrays.asList(orderAbnormalCreateParam);

			boolean result = openOrderService.createOrderAbnormal(order_id, orderAbnormalCreateParamList);
			Assert.assertEquals(result, false, "新建订单[商品异常]异常测试,传入错误的订单ID,断言失败");
		} catch (Exception e) {
			logger.error("新建订单商品异常遇到错误: ", e);
			Assert.fail("新建订单商品异常遇到错误: ", e);
		}

	}

	@Test
	public void orderExceptionCreateAbnormalTestCase04() {
		Reporter.log("测试点: 新建订单[商品异常]异常测试,传入不在订单里的商品ID,断言失败");
		try {
			String sku_id = products.get(0).getSku_id();
			boolean result = openOrderService.deleteOrderSkus(order_id, Arrays.asList(sku_id));
			Assert.assertEquals(result, true, "订单删除商品失败");

			OrderAbnormalCreateParam orderAbnormalCreateParam = new OrderAbnormalCreateParam();
			orderAbnormalCreateParam.setSku_id(sku_id);
			orderAbnormalCreateParam.setException_reason("1");
			orderAbnormalCreateParam.setFinal_count("4");

			List<OrderAbnormalCreateParam> orderAbnormalCreateParamList = Arrays.asList(orderAbnormalCreateParam);

			result = openOrderService.createOrderAbnormal(order_id, orderAbnormalCreateParamList);
			Assert.assertEquals(result, false, "新建订单[商品异常]异常测试,传入错误的订单ID,断言失败");
		} catch (Exception e) {
			logger.error("新建订单商品异常遇到错误: ", e);
			Assert.fail("新建订单商品异常遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionCreateAbnormalTestCase05() {
		Reporter.log("测试点: 新建订单[商品异常]异常测试,不传入商品异常原因编号,断言失败");
		try {
			String sku_id = products.get(0).getSku_id();

			OrderAbnormalCreateParam orderAbnormalCreateParam = new OrderAbnormalCreateParam();
			orderAbnormalCreateParam.setSku_id(sku_id);
			orderAbnormalCreateParam.setException_reason("");
			orderAbnormalCreateParam.setFinal_count("4");

			List<OrderAbnormalCreateParam> orderAbnormalCreateParamList = Arrays.asList(orderAbnormalCreateParam);

			boolean result = openOrderService.createOrderAbnormal(order_id, orderAbnormalCreateParamList);
			Assert.assertEquals(result, false, "新建订单[商品异常]异常测试,不传入商品异常原因编号,断言失败");
		} catch (Exception e) {
			logger.error("新建订单商品异常遇到错误: ", e);
			Assert.fail("新建订单商品异常遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionCreateAbnormalTestCase06() {
		Reporter.log("测试点: 新建订单[商品异常]异常测试,传入非候选的商品异常原因编号,断言失败");
		try {
			String sku_id = products.get(0).getSku_id();

			OrderAbnormalCreateParam orderAbnormalCreateParam = new OrderAbnormalCreateParam();
			orderAbnormalCreateParam.setSku_id(sku_id);
			orderAbnormalCreateParam.setException_reason("18");
			orderAbnormalCreateParam.setFinal_count("4");

			List<OrderAbnormalCreateParam> orderAbnormalCreateParamList = Arrays.asList(orderAbnormalCreateParam);

			boolean result = openOrderService.createOrderAbnormal(order_id, orderAbnormalCreateParamList);
			Assert.assertEquals(result, false, "新建订单[商品异常]异常测试,传入非候选的商品异常原因编号,断言失败");
		} catch (Exception e) {
			logger.error("新建订单商品异常遇到错误: ", e);
			Assert.fail("新建订单商品异常遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionCreateAbnormalTestCase07() {
		Reporter.log("测试点: 新建订单[商品异常]异常测试,不传入商品异常数值,断言失败");
		try {
			String sku_id = products.get(0).getSku_id();

			OrderAbnormalCreateParam orderAbnormalCreateParam = new OrderAbnormalCreateParam();
			orderAbnormalCreateParam.setSku_id(sku_id);
			orderAbnormalCreateParam.setException_reason("1");
			orderAbnormalCreateParam.setFinal_count("");

			List<OrderAbnormalCreateParam> orderAbnormalCreateParamList = Arrays.asList(orderAbnormalCreateParam);

			boolean result = openOrderService.createOrderAbnormal(order_id, orderAbnormalCreateParamList);
			Assert.assertEquals(result, false, "新建订单[商品异常]异常测试,不传入商品异常数值,断言失败");
		} catch (Exception e) {
			logger.error("新建订单商品异常遇到错误: ", e);
			Assert.fail("新建订单商品异常遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionCreateAbnormalTestCase08() {
		Reporter.log("测试点: 新建订单[商品异常]异常测试,传入非数值的商品异常数值,断言失败");
		try {
			String sku_id = products.get(0).getSku_id();

			OrderAbnormalCreateParam orderAbnormalCreateParam = new OrderAbnormalCreateParam();
			orderAbnormalCreateParam.setSku_id(sku_id);
			orderAbnormalCreateParam.setException_reason("1");
			orderAbnormalCreateParam.setFinal_count("A");

			List<OrderAbnormalCreateParam> orderAbnormalCreateParamList = Arrays.asList(orderAbnormalCreateParam);

			boolean result = openOrderService.createOrderAbnormal(order_id, orderAbnormalCreateParamList);
			Assert.assertEquals(result, false, "新建订单[商品异常]异常测试,传入非数值的商品异常数值,断言失败");
		} catch (Exception e) {
			logger.error("新建订单商品异常遇到错误: ", e);
			Assert.fail("新建订单商品异常遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionCreateAbnormalTestCase09() {
		Reporter.log("测试点: 新建订单[商品异常]异常测试,传入负数的商品异常数值,断言失败");
		try {
			String sku_id = products.get(0).getSku_id();

			OrderAbnormalCreateParam orderAbnormalCreateParam = new OrderAbnormalCreateParam();
			orderAbnormalCreateParam.setSku_id(sku_id);
			orderAbnormalCreateParam.setException_reason("1");
			orderAbnormalCreateParam.setFinal_count("-2");

			List<OrderAbnormalCreateParam> orderAbnormalCreateParamList = Arrays.asList(orderAbnormalCreateParam);

			boolean result = openOrderService.createOrderAbnormal(order_id, orderAbnormalCreateParamList);
			Assert.assertEquals(result, false, "新建订单[商品异常]异常测试,传入负数的商品异常数值,断言失败");
		} catch (Exception e) {
			logger.error("新建订单商品异常遇到错误: ", e);
			Assert.fail("新建订单商品异常遇到错误: ", e);
		}
	}

	// @Test
	// public void orderExceptionCreateAbnormalTestCase10() {
	// Reporter.log("测试点: 新建订单[商品异常]异常测试,商品异常数值传入下单数,断言失败");
	// try {
	// OrderProductParam orderProduct = products.get(0);
	// String sku_id = orderProduct.getSku_id();
	//
	// OrderAbnormalCreateParam orderAbnormalCreateParam = new
	// OrderAbnormalCreateParam();
	// orderAbnormalCreateParam.setSku_id(sku_id);
	// orderAbnormalCreateParam.setException_reason("1");
	// orderAbnormalCreateParam.setFinal_count(orderProduct.getAmount());
	//
	// List<OrderAbnormalCreateParam> orderAbnormalCreateParamList =
	// Arrays.asList(orderAbnormalCreateParam);
	//
	// boolean result = openOrderService.createOrderAbnormal(order_id,
	// orderAbnormalCreateParamList);
	// Assert.assertEquals(result, false, "新建订单[商品异常]异常测试,商品异常数值传入下单数,断言失败");
	// } catch (Exception e) {
	// logger.error("新建订单商品异常遇到错误: ", e);
	// Assert.fail("新建订单商品异常遇到错误: ", e);
	// }
	// }

	@Test
	public void orderExceptionCreateAbnormalTestCase11() {
		Reporter.log("测试点: 新建订单[商品异常]异常测试,为已做了退货的商品添加商品异常,断言失败");
		try {
			OrderProductParam orderProduct = products.get(0);
			String sku_id = orderProduct.getSku_id();

			OrderRefundCreateParam orderRefundCreateParam = new OrderRefundCreateParam();
			orderRefundCreateParam.setException_reason("1");
			orderRefundCreateParam.setRequest_count("2");
			orderRefundCreateParam.setSku_id(sku_id);

			boolean result = openOrderService.createOrderRefund(order_id, Arrays.asList(orderRefundCreateParam));
			Assert.assertEquals(result, true, "正常添加订单商品退货,断言成功");

			OrderAbnormalCreateParam orderAbnormalCreateParam = new OrderAbnormalCreateParam();
			orderAbnormalCreateParam.setSku_id(sku_id);
			orderAbnormalCreateParam.setException_reason("1");
			orderAbnormalCreateParam.setFinal_count("2");

			List<OrderAbnormalCreateParam> orderAbnormalCreateParamList = Arrays.asList(orderAbnormalCreateParam);

			result = openOrderService.createOrderAbnormal(order_id, orderAbnormalCreateParamList);
			Assert.assertEquals(result, false, "新建订单[商品异常]异常测试,为已做了退货的商品添加商品异常,断言失败");
		} catch (Exception e) {
			logger.error("新建订单商品异常遇到错误: ", e);
			Assert.fail("新建订单商品异常遇到错误: ", e);
		}
	}
}
