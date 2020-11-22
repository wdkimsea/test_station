package cn.guanmai.open.order.abnormal;

import java.math.BigDecimal;
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
import cn.guanmai.open.bean.order.OpenOrderDetailBean;
import cn.guanmai.open.bean.order.param.OrderAbnormalCreateParam;
import cn.guanmai.open.bean.order.param.OrderAbnormalDeleteParam;
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

/* 
* @author liming 
* @date Jun 13, 2019 11:27:19 AM 
* @des 开放平台-订单商品异常删除异常测试
* @version 1.0 
*/
public class OrderExceptionDeleteAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OrderExceptionDeleteAbnormalTest.class);
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
	private OrderProductParam orderProduct;
	private OpenOrderDetailBean.Abnormal abnormal;

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

			orderProduct = products.get(0);

			OrderAbnormalCreateParam orderAbnormalCreateParam = new OrderAbnormalCreateParam();
			orderAbnormalCreateParam.setException_reason("1");
			orderAbnormalCreateParam.setSku_id(orderProduct.getSku_id());
			orderAbnormalCreateParam.setFinal_count("2");

			boolean reuslt = openOrderService.createOrderAbnormal(order_id, Arrays.asList(orderAbnormalCreateParam));
			Assert.assertEquals(reuslt, true, "订单添加商品异常失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotNull(openOrderDetail, "获取订单详细信息失败");

			List<OpenOrderDetailBean.Abnormal> abnomals = openOrderDetail.getAbnormals();
			Assert.assertEquals(abnomals.size(), 1, "新建的订单商品异常在订单详细中没有找到");

			abnormal = abnomals.get(0);
		} catch (Exception e) {
			logger.error("商户下单遇到错误: ", e);
			Assert.fail("商户下单遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionDeleteAbnormalTestCase01() {
		Reporter.log("测试点: 删除订单商品异常异常测试,不传入订单ID,断言失败");
		try {
			OrderAbnormalDeleteParam orderAbnormalDeleteParam = new OrderAbnormalDeleteParam();
			orderAbnormalDeleteParam.setId(abnormal.getId());
			orderAbnormalDeleteParam.setSku_id(abnormal.getSku_id());

			boolean result = openOrderService.deleteOrderAbnormal("", Arrays.asList(orderAbnormalDeleteParam));
			Assert.assertEquals(result, false, "删除订单商品异常异常测试,不传入订单ID,断言失败");
		} catch (Exception e) {
			logger.error("删除订单商品异常到错误: ", e);
			Assert.fail("删除订单商品异常遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionDeleteAbnormalTestCase02() {
		Reporter.log("测试点: 删除订单商品异常异常测试,传入错误的订单ID,断言失败");
		try {
			OrderAbnormalDeleteParam orderAbnormalDeleteParam = new OrderAbnormalDeleteParam();
			orderAbnormalDeleteParam.setId(abnormal.getId());
			orderAbnormalDeleteParam.setSku_id(abnormal.getSku_id());

			boolean result = openOrderService.deleteOrderAbnormal(order_id + "0",
					Arrays.asList(orderAbnormalDeleteParam));
			Assert.assertEquals(result, false, "删除订单商品异常异常测试,传入错误的订单ID,断言失败");
		} catch (Exception e) {
			logger.error("删除订单商品异常到错误: ", e);
			Assert.fail("删除订单商品异常遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionDeleteAbnormalTestCase03() {
		Reporter.log("测试点: 删除订单商品异常异常测试,传入的订单ID与商品异常ID不匹配,断言失败");
		try {

			String temm_order_id = "PL"
					+ new BigDecimal(order_id.substring(2)).subtract(new BigDecimal("1")).toString();

			OrderAbnormalDeleteParam orderAbnormalDeleteParam = new OrderAbnormalDeleteParam();
			orderAbnormalDeleteParam.setId(abnormal.getId());
			orderAbnormalDeleteParam.setSku_id(abnormal.getSku_id());

			boolean result = openOrderService.deleteOrderAbnormal(temm_order_id,
					Arrays.asList(orderAbnormalDeleteParam));
			Assert.assertEquals(result, false, "删除订单商品异常异常测试,传入的订单ID与商品异常ID不匹配,断言失败");
		} catch (Exception e) {
			logger.error("删除订单商品异常到错误: ", e);
			Assert.fail("删除订单商品异常遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionDeleteAbnormalTestCase04() {
		Reporter.log("测试点: 删除订单商品异常异常测试,传入空的商品异常列表,断言失败");
		try {
			boolean result = openOrderService.deleteOrderAbnormal(order_id, new ArrayList<>());
			Assert.assertEquals(result, false, "删除订单商品异常异常测试,传入空的商品异常列表,断言失败");
		} catch (Exception e) {
			logger.error("删除订单商品异常到错误: ", e);
			Assert.fail("删除订单商品异常遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionDeleteAbnormalTestCase05() {
		Reporter.log("测试点: 删除订单商品异常异常测试,不传入商品异常ID,断言失败");
		try {
			OrderAbnormalDeleteParam orderAbnormalDeleteParam = new OrderAbnormalDeleteParam();
			orderAbnormalDeleteParam.setId("");
			orderAbnormalDeleteParam.setSku_id(abnormal.getSku_id());

			boolean result = openOrderService.deleteOrderAbnormal(order_id, Arrays.asList(orderAbnormalDeleteParam));
			Assert.assertEquals(result, false, "删除订单商品异常异常测试,传入错误的订单ID,断言失败");
		} catch (Exception e) {
			logger.error("删除订单商品异常到错误: ", e);
			Assert.fail("删除订单商品异常遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionDeleteAbnormalTestCase06() {
		Reporter.log("测试点: 删除订单商品异常异常测试,不传入商品ID,断言失败");
		try {
			OrderAbnormalDeleteParam orderAbnormalDeleteParam = new OrderAbnormalDeleteParam();
			orderAbnormalDeleteParam.setId(abnormal.getId());
			orderAbnormalDeleteParam.setSku_id("");

			boolean result = openOrderService.deleteOrderAbnormal(order_id, Arrays.asList(orderAbnormalDeleteParam));
			Assert.assertEquals(result, false, "删除订单商品异常异常测试,不传入商品ID,断言失败");
		} catch (Exception e) {
			logger.error("删除订单商品异常到错误: ", e);
			Assert.fail("删除订单商品异常遇到错误: ", e);
		}
	}

	@Test
	public void orderExceptionDeleteAbnormalTestCase07() {
		Reporter.log("测试点: 删除订单商品异常异常测试,传入商品异常ID和商品ID不匹配,断言失败");
		try {
			OrderAbnormalDeleteParam orderAbnormalDeleteParam = new OrderAbnormalDeleteParam();
			orderAbnormalDeleteParam.setId(abnormal.getId());
			String temp_sku_id = products.get(products.size() - 1).getSku_id();
			orderAbnormalDeleteParam.setSku_id(temp_sku_id);

			boolean result = openOrderService.deleteOrderAbnormal(order_id, Arrays.asList(orderAbnormalDeleteParam));
			Assert.assertEquals(result, false, "删除订单商品异常异常测试,传入商品异常ID和商品ID不匹配,断言失败");
		} catch (Exception e) {
			logger.error("删除订单商品异常到错误: ", e);
			Assert.fail("删除订单商品异常遇到错误: ", e);
		}
	}

}
