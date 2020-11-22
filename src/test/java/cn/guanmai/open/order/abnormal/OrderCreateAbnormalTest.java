package cn.guanmai.open.order.abnormal;

import java.util.ArrayList;
import java.util.Calendar;
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
import cn.guanmai.open.bean.order.param.OrderCreateParam;
import cn.guanmai.open.bean.order.param.OrderProductParam;
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
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Jun 12, 2019 11:32:52 AM 
* @des 开放平台新建订单异常测试
* @version 1.0 
*/
public class OrderCreateAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OrderCreateAbnormalTest.class);
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

		} catch (Exception e) {
			logger.error("搜索过滤下单商户遇到错误: ", e);
			Assert.fail("搜索过滤下单商户遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		orderCreateParam = new OrderCreateParam();
		orderCreateParam.setProducts(products);
		orderCreateParam.setReceive_begin_time(receive_time_start);
		orderCreateParam.setReceive_end_time(receive_time_end);
		orderCreateParam.setCustomer_id(customer_id);
		orderCreateParam.setTime_config_id(time_config_id);
	}

	@Test
	public void orderCreateAbnormalTestCase01() {
		Reporter.log("测试点: 创建订单异常测试,传入别的GROUP下的商户ID,预期下单失败");
		try {
			orderCreateParam.setCustomer_id("S025156");
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,传入别的GROUP下的商户ID,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase02() {
		Reporter.log("测试点: 创建订单异常测试,不传入下单商户ID,预期下单失败");
		try {
			orderCreateParam.setCustomer_id("");
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,传入别的GROUP下的商户ID,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase03() {
		Reporter.log("测试点: 创建订单异常测试,传入不能正常下单的商户ID,预期下单失败");
		try {
			List<OpenCustomerBean> openCustomers = openCustomerService.searchCustomer(null, null, 0, 100);
			Assert.assertNotEquals(openCustomers, null, "搜索查询商户失败");
			String temp_customer_id = null;
			for (OpenCustomerBean customer : openCustomers) {
				temp_customer_id = customer.getCustomer_id();
				boolean result = openCustomerService.checkCustomerOrderStatus(temp_customer_id);
				if (!result) {
					break;
				}
			}
			orderCreateParam.setCustomer_id(temp_customer_id);
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,传入不能正常下单的商户ID,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase04() {
		Reporter.log("测试点: 创建订单异常测试,不传入运营时间ID,预期下单失败");
		try {
			orderCreateParam.setTime_config_id("");
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,不传入运营时间ID,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase05() {
		Reporter.log("测试点: 创建订单异常测试,传入非本站点的运营时间ID,预期下单失败");
		try {
			orderCreateParam.setTime_config_id("ST001");
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,传入非本站点的运营时间ID,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase06() {
		Reporter.log("测试点: 创建订单异常测试,传入本站点另一个运营时间ID,预期下单失败");
		try {
			Assert.assertEquals(openSalemenuGroup.size() >= 2, true, "本站点只有一个运营时间,无法执行此用例");
			String temp_time_config_id = null;
			for (String key : openSalemenuGroup.keySet()) {
				if (!key.equals(time_config_id)) {
					temp_time_config_id = key;
					break;
				}
			}
			orderCreateParam.setTime_config_id(temp_time_config_id);
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,传入本站点另一个运营时间ID,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase07() {
		Reporter.log("测试点: 创建订单异常测试,不传入收货时间的起始时间,预期下单失败");
		try {
			orderCreateParam.setReceive_begin_time("");
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,不传入收货时间的起始时间,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase08() {
		Reporter.log("测试点: 创建订单异常测试,传入不符合格式要求的收货起始时间,预期下单失败");
		try {
			String receive_begin_time = orderCreateParam.getReceive_begin_time();
			receive_begin_time = receive_begin_time.replaceAll("-", ":");
			orderCreateParam.setReceive_begin_time(receive_begin_time);
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,传入不符合格式要求的收货起始时间,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase09() {
		Reporter.log("测试点: 创建订单异常测试,传入非候选值的收货时间,预期下单失败");
		try {
			String receive_begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm");
			String receive_end_time = TimeUtil.calculateTime("yyyy-MM-dd HH:mm", receive_begin_time, 30,
					Calendar.MINUTE);
			orderCreateParam.setReceive_begin_time(receive_begin_time);
			orderCreateParam.setReceive_end_time(receive_end_time);

			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,传入非候选值的收货时间,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase10() {
		Reporter.log("测试点: 创建订单异常测试,设置收货起始时间晚于收货结束时间,预期下单失败");
		try {
			orderCreateParam.setReceive_begin_time(receive_time_end);
			orderCreateParam.setReceive_end_time(receive_time_start);

			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,设置收货起始时间晚于收货结束时间,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase11() {
		Reporter.log("测试点: 创建订单异常测试,设置商品价格为空,预期下单失败");
		try {
			List<OrderProductParam> temp_products = new ArrayList<>();
			for (OrderProductParam orderProduct : products) {
				orderProduct.setPrice("");
				temp_products.add(orderProduct);
			}
			orderCreateParam.setProducts(temp_products);
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,设置商品价格为空,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase12() {
		Reporter.log("测试点: 创建订单异常测试,设置商品价格为非数字,预期下单失败");
		try {
			List<OrderProductParam> temp_products = new ArrayList<>();
			for (OrderProductParam orderProduct : products) {
				orderProduct.setPrice("A");
				temp_products.add(orderProduct);
			}
			orderCreateParam.setProducts(temp_products);
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,设置商品价格为非数字,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase13() {
		Reporter.log("测试点: 创建订单异常测试,设置商品价格为负数,预期下单失败");
		try {
			List<OrderProductParam> temp_products = new ArrayList<>();
			for (OrderProductParam orderProduct : products) {
				orderProduct.setPrice("-5");
				temp_products.add(orderProduct);
			}
			orderCreateParam.setProducts(temp_products);
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,设置商品价格为负数,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase14() {
		Reporter.log("测试点: 创建订单异常测试,设置商品价格多位小数,预期下单失败");
		try {
			List<OrderProductParam> temp_products = new ArrayList<>();
			for (OrderProductParam orderProduct : products) {
				orderProduct.setPrice("3.1415");
				temp_products.add(orderProduct);
			}
			orderCreateParam.setProducts(temp_products);
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,设置商品价格为负数,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase15() {
		Reporter.log("测试点: 创建订单异常测试,不设置下单商品数量,预期下单失败");
		try {
			List<OrderProductParam> temp_products = new ArrayList<>();
			for (OrderProductParam orderProduct : products) {
				orderProduct.setCount("");
				temp_products.add(orderProduct);
			}
			orderCreateParam.setProducts(temp_products);
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,不设置下单商品数量,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase16() {
		Reporter.log("测试点: 创建订单异常测试,设置下单商品数量为非数字,预期下单失败");
		try {
			List<OrderProductParam> temp_products = new ArrayList<>();
			for (OrderProductParam orderProduct : products) {
				orderProduct.setCount("A");
				temp_products.add(orderProduct);
			}
			orderCreateParam.setProducts(temp_products);
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,设置下单商品数量为非数字,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase17() {
		Reporter.log("测试点: 创建订单异常测试,设置下单商品数量为负数,预期下单失败");
		try {
			List<OrderProductParam> temp_products = new ArrayList<>();
			for (OrderProductParam orderProduct : products) {
				orderProduct.setCount("-5");
				temp_products.add(orderProduct);
			}
			orderCreateParam.setProducts(temp_products);
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,设置下单商品数量为负数,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase18() {
		Reporter.log("测试点: 创建订单异常测试,设置下单商品数量数值为多位小数,预期下单失败");
		try {
			List<OrderProductParam> temp_products = new ArrayList<>();
			for (OrderProductParam orderProduct : products) {
				orderProduct.setCount("6.182");
				temp_products.add(orderProduct);
			}
			orderCreateParam.setProducts(temp_products);
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,设置下单商品数量为负数,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase19() {
		Reporter.log("测试点: 创建订单异常测试,设置下单商品是否时价值为非布尔值,预期下单失败");
		try {
			List<OrderProductParam> temp_products = new ArrayList<>();
			for (OrderProductParam orderProduct : products) {
				orderProduct.setIs_price_timing("2");
				temp_products.add(orderProduct);
			}
			orderCreateParam.setProducts(temp_products);
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,设置下单商品数量为负数,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase20() {
		Reporter.log("测试点: 创建订单异常测试,传入空的SKU ID,预期下单失败");
		try {
			List<OrderProductParam> temp_products = new ArrayList<>();
			for (OrderProductParam orderProduct : products) {
				orderProduct.setSku_id("");
				temp_products.add(orderProduct);
			}
			orderCreateParam.setProducts(temp_products);
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,传入空的SKU ID,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase21() {
		Reporter.log("测试点: 创建订单异常测试,传入错误的SKU ID值,预期下单失败");
		try {
			List<OrderProductParam> temp_products = new ArrayList<>();
			for (OrderProductParam orderProduct : products) {
				orderProduct.setSku_id(orderProduct.getSku_id() + "0");
				temp_products.add(orderProduct);
			}
			orderCreateParam.setProducts(temp_products);
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,传入错误的SKU ID值,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderCreateAbnormalTestCase22() {
		Reporter.log("测试点: 创建订单异常测试,传入其他站点的SKU ID值,预期下单失败");
		try {
			List<OrderProductParam> temp_products = new ArrayList<>();
			OrderProductParam orderProductParam = new OrderProductParam();
			orderProductParam.setSku_id("D3242914");
			orderProductParam.setCount("100");
			orderProductParam.setPrice("2");
			orderProductParam.setIs_price_timing("0");

			orderCreateParam.setProducts(temp_products);
			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertEquals(order_id, null, "创建订单异常测试,传入错误的SKU ID值,预期下单失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}
}
