package cn.guanmai.open.order;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import cn.guanmai.open.bean.order.param.OrderCreateParam;
import cn.guanmai.open.bean.order.param.OrderProductParam;
import cn.guanmai.open.bean.order.param.OrderRefundCreateParam;
import cn.guanmai.open.bean.order.param.OrderRefundDeleteParam;
import cn.guanmai.open.bean.order.param.OrderRefundUpdateParam;
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

/* 
* @author liming 
* @date Jun 12, 2019 9:57:35 AM 
* @des 开放平台商品退货测试
* @version 1.0 
*/
public class OrderRefundTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OrderRefundTest.class);
	private OpenOrderService openOrderService;
	private OpenCustomerService openCustomerService;
	private OpenSalemenuService openSalemenuService;
	private OpenCustomerBean openCustomer;
	private OpenCategoryService openCategoryService;
	private OrderCreateParam orderCreateParam;
	private List<OrderProductParam> products;
	private String order_id;

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
			List<OpenCustomerBean> order_customer_list = new ArrayList<>();
			boolean result = false;
			String customer_id = null;
			for (OpenCustomerBean customer : openCustomers) {
				customer_id = customer.getCustomer_id();
				result = openCustomerService.checkCustomerOrderStatus(customer_id);
				if (result) {
					openSalemenus = openSalemenuService.searchSalemenu(customer_id, 1);
					Assert.assertNotNull(openSalemenus, "根据商户查询报价单集合失败");
					if (openSalemenus.size() > 0) {
						order_customer_list.add(customer);
						if (order_customer_list.size() >= 6) {
							break;
						}
					}
				}

			}

			openCustomer = NumberUtil.roundNumberInList(order_customer_list);

			openSalemenus = openSalemenuService.searchSalemenu(openCustomer.getCustomer_id(), 1);
			Assert.assertNotNull(openSalemenus, "根据商户查询报价单集合失败");

			Map<String, List<OpenSalemenuBean>> openSalemenuGroup = openSalemenus.stream()
					.collect(Collectors.groupingBy(OpenSalemenuBean::getTime_config_id));

			// 取第一个运营时间的所有的报价单
			String time_config_id = openSalemenuGroup.keySet().iterator().next();
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
			String receive_time_start = receive_time_list.get(0);
			String receive_time_end = receive_time_list.get(1);

			orderCreateParam = new OrderCreateParam();
			orderCreateParam.setProducts(products);
			orderCreateParam.setReceive_begin_time(receive_time_start);
			orderCreateParam.setReceive_end_time(receive_time_end);
			orderCreateParam.setCustomer_id(openCustomer.getCustomer_id());
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
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderRefundTestCase01() {
		Reporter.log("测试点: 新建订单商品退货(单个)");
		try {
			String sku_id = NumberUtil.roundNumberInList(products).getSku_id();

			List<OrderRefundCreateParam> orderRefundCreateParamList = new ArrayList<>();
			OrderRefundCreateParam orderRefundCreateParam = new OrderRefundCreateParam();
			orderRefundCreateParam.setException_reason("1");
			orderRefundCreateParam.setSku_id(sku_id);
			orderRefundCreateParam.setRequest_count("2");
			orderRefundCreateParamList.add(orderRefundCreateParam);

			boolean result = openOrderService.createOrderRefund(order_id, orderRefundCreateParamList);
			Assert.assertEquals(result, true, "新建订单商品退货失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			List<OpenOrderDetailBean.Refund> refunds = openOrderDetail.getRefunds();
			Assert.assertEquals(refunds.size(), 1, "新建订单商品退货后,在订单详情中没有查询到退货信息");

			OpenOrderDetailBean.Refund refund = refunds.get(0);
			String msg = null;
			if (!refund.getSku_id().equals(sku_id)) {
				msg = String.format("新建订单商品退货所填商品ID和查询到的商品ID不一致,预期:%s,实际:%s", sku_id, refund.getSku_id());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			if (refund.getRequest_count().compareTo(new BigDecimal("2")) != 0) {
				msg = String.format("新建订单商品退货所填商品退货数和查询到的商品退货数不一致,预期:%s,实际:%s", "2", refund.getRequest_count());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			if (refund.getException_reason() != 1) {
				msg = String.format("新建订单商品退货退货原因和查询到的商品退货原因不一致,预期:%s,实际:%s", "1", refund.getException_reason());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "新建的订单商品退货信息和查询到的订单商品退货信息不一致");
		} catch (Exception e) {
			logger.error("新建订单商品退货遇到错误: ", e);
			Assert.fail("新建订单商品退货遇到错误: ", e);
		}
	}

	@Test
	public void orderRefundTestCase02() {
		Reporter.log("测试点: 新建订单商品退货(批量)");
		try {

			List<OrderRefundCreateParam> orderRefundCreateParamList = new ArrayList<>();
			OrderRefundCreateParam orderRefundCreateParam = null;
			for (OrderProductParam orderProduct : products) {
				orderRefundCreateParam = new OrderRefundCreateParam();
				orderRefundCreateParam.setException_reason("1");
				orderRefundCreateParam.setSku_id(orderProduct.getSku_id());
				orderRefundCreateParam.setRequest_count(String.valueOf(NumberUtil.getRandomNumber(2, 5, 1)));
				orderRefundCreateParamList.add(orderRefundCreateParam);

			}

			boolean result = openOrderService.createOrderRefund(order_id, orderRefundCreateParamList);
			Assert.assertEquals(result, true, "新建订单商品退货失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			List<OpenOrderDetailBean.Refund> refunds = openOrderDetail.getRefunds();
			Assert.assertEquals(refunds.size(), products.size(), "查询到的订单商品退货条目数和预期的不一致");

			String msg = null;
			for (OrderRefundCreateParam orderRefund : orderRefundCreateParamList) {
				String sku_id = orderRefund.getSku_id();
				OpenOrderDetailBean.Refund refund = refunds.stream().filter(r -> r.getSku_id().equals(sku_id)).findAny()
						.orElse(null);
				if (refund == null) {
					msg = String.format("订单%s中的商品%s的商品退货在订单详情里没有找到", order_id, sku_id);
					Reporter.log(msg);
					logger.warn(msg);
					result = false;
				}

				if (refund.getRequest_count().compareTo(new BigDecimal(orderRefund.getRequest_count())) != 0) {
					msg = String.format("新建订单商品退货所填商品退货数和查询到的商品退货数不一致,预期:%s,实际:%s", "2", refund.getRequest_count());
					Reporter.log(msg);
					logger.warn(msg);
					result = false;
				}

				if (refund.getException_reason() != 1) {
					msg = String.format("新建订单商品退货退货原因和查询到的商品退货原因不一致,预期:%s,实际:%s", "1", refund.getException_reason());
					Reporter.log(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "新建的订单商品退货信息和查询到的订单商品退货信息不一致");
		} catch (Exception e) {
			logger.error("新建订单商品退货遇到错误: ", e);
			Assert.fail("新建订单商品退货遇到错误: ", e);
		}
	}

	@Test
	public void orderRefundTestCase03() {
		Reporter.log("测试点: 修改订单商品退货(单个)");
		try {
			String sku_id = NumberUtil.roundNumberInList(products).getSku_id();

			List<OrderRefundCreateParam> orderRefundCreateParamList = new ArrayList<>();
			OrderRefundCreateParam orderRefundCreateParam = new OrderRefundCreateParam();
			orderRefundCreateParam.setException_reason("1");
			orderRefundCreateParam.setSku_id(sku_id);
			orderRefundCreateParam.setRequest_count("2");
			orderRefundCreateParamList.add(orderRefundCreateParam);

			boolean result = openOrderService.createOrderRefund(order_id, orderRefundCreateParamList);
			Assert.assertEquals(result, true, "新建订单商品退货失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			List<OpenOrderDetailBean.Refund> refunds = openOrderDetail.getRefunds();
			Assert.assertEquals(refunds.size(), 1, "新建订单商品退货后,在订单详情中没有查询到退货信息");

			OpenOrderDetailBean.Refund refund = refunds.get(0);

			List<OrderRefundUpdateParam> orderRefundUpdateParamList = new ArrayList<>();
			OrderRefundUpdateParam orderRefundUpdateParam = new OrderRefundUpdateParam();
			orderRefundUpdateParam.setId(refund.getId());
			orderRefundUpdateParam.setSku_id(sku_id);
			orderRefundUpdateParam.setException_reason("5");
			orderRefundUpdateParam.setRequest_count("2.1");
			orderRefundUpdateParamList.add(orderRefundUpdateParam);

			result = openOrderService.updateOrderRefund(order_id, orderRefundUpdateParamList);
			Assert.assertEquals(result, true, "修改订单商品退货失败");

			openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			refunds = openOrderDetail.getRefunds();
			Assert.assertEquals(refunds.size(), 1, "修改订单商品退货后,在订单详情中没有查询到退货信息");

			refund = refunds.get(0);
			String msg = null;
			if (!refund.getSku_id().equals(sku_id)) {
				msg = String.format("修改订单商品退货所填商品ID和查询到的商品ID不一致,预期:%s,实际:%s", sku_id, refund.getSku_id());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			if (refund.getRequest_count().compareTo(new BigDecimal(orderRefundUpdateParam.getRequest_count())) != 0) {
				msg = String.format("修改订单商品退货所填商品退货数和查询到的商品退货数不一致,预期:%s,实际:%s",
						orderRefundUpdateParam.getRequest_count(), refund.getRequest_count());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			if (refund.getException_reason() != 5) {
				msg = String.format("修改订单商品退货退货原因和查询到的商品退货原因不一致,预期:%s,实际:%s", "5", refund.getException_reason());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "修改的订单商品退货信息和查询到的订单商品退货信息不一致");
		} catch (Exception e) {
			logger.error("修改订单商品退货遇到错误: ", e);
			Assert.fail("修改订单商品退货遇到错误: ", e);
		}
	}

	@Test
	public void orderRefundTestCase04() {
		Reporter.log("测试点: 修改订单商品退货(批量)");
		try {

			List<OrderRefundCreateParam> orderRefundCreateParamList = new ArrayList<>();
			OrderRefundCreateParam orderRefundCreateParam = null;
			for (OrderProductParam orderProduct : products) {
				orderRefundCreateParam = new OrderRefundCreateParam();
				orderRefundCreateParam.setException_reason("1");
				orderRefundCreateParam.setSku_id(orderProduct.getSku_id());
				orderRefundCreateParam.setRequest_count(String.valueOf(NumberUtil.getRandomNumber(2, 5, 1)));
				orderRefundCreateParamList.add(orderRefundCreateParam);

			}

			boolean result = openOrderService.createOrderRefund(order_id, orderRefundCreateParamList);
			Assert.assertEquals(result, true, "新建订单商品退货失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			List<OpenOrderDetailBean.Refund> refunds = openOrderDetail.getRefunds();
			Assert.assertEquals(refunds.size(), products.size(), "查询到的订单商品退货条目数和预期的不一致");

			List<OrderRefundUpdateParam> orderRefundUpdateParamList = new ArrayList<>();
			OrderRefundUpdateParam orderRefundUpdateParam = null;
			for (OpenOrderDetailBean.Refund refund : refunds) {
				orderRefundUpdateParam = new OrderRefundUpdateParam();
				orderRefundUpdateParam.setId(refund.getId());
				orderRefundUpdateParam.setSku_id(refund.getSku_id());
				orderRefundUpdateParam.setException_reason("5");
				orderRefundUpdateParam.setRequest_count(String.valueOf(NumberUtil.getRandomNumber(2, 5, 1)));
				orderRefundUpdateParamList.add(orderRefundUpdateParam);

			}

			result = openOrderService.updateOrderRefund(order_id, orderRefundUpdateParamList);
			Assert.assertEquals(result, true, "修改订单商品退货失败");

			openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			refunds = openOrderDetail.getRefunds();
			Assert.assertEquals(refunds.size(), orderRefundUpdateParamList.size(),
					"修改订单商品退货后,在订单详情中没有查询到退货信息条目数和预期不一致");

			String msg = null;
			for (OrderRefundUpdateParam orderRefund : orderRefundUpdateParamList) {
				String sku_id = orderRefund.getSku_id();
				OpenOrderDetailBean.Refund refund = refunds.stream().filter(r -> r.getSku_id().equals(sku_id)).findAny()
						.orElse(null);
				if (refund == null) {
					msg = String.format("订单%s中的商品%s的商品退货在订单详情里没有找到", order_id, sku_id);
					Reporter.log(msg);
					logger.warn(msg);
					result = false;
				}

				if (refund.getRequest_count().compareTo(new BigDecimal(orderRefund.getRequest_count())) != 0) {
					msg = String.format("修改订单商品退货所填商品退货数和查询到的商品退货数不一致,预期:%s,实际:%s", "2", refund.getRequest_count());
					Reporter.log(msg);
					logger.warn(msg);
					result = false;
				}

				if (refund.getException_reason() != 5) {
					msg = String.format("修改订单商品退货退货原因和查询到的商品退货原因不一致,预期:%s,实际:%s", "5", refund.getException_reason());
					Reporter.log(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "修改的订单商品退货信息和查询到的订单商品退货信息不一致");
		} catch (Exception e) {
			logger.error("修改订单商品退货遇到错误: ", e);
			Assert.fail("修改订单商品退货遇到错误: ", e);
		}
	}

	@Test
	public void orderRefundTestCase05() {
		Reporter.log("测试点: 删除订单商品退货(单个)");
		try {
			String sku_id = NumberUtil.roundNumberInList(products).getSku_id();

			List<OrderRefundCreateParam> orderRefundCreateParamList = new ArrayList<>();
			OrderRefundCreateParam orderRefundCreateParam = new OrderRefundCreateParam();
			orderRefundCreateParam.setException_reason("1");
			orderRefundCreateParam.setSku_id(sku_id);
			orderRefundCreateParam.setRequest_count("2");
			orderRefundCreateParamList.add(orderRefundCreateParam);

			boolean result = openOrderService.createOrderRefund(order_id, orderRefundCreateParamList);
			Assert.assertEquals(result, true, "新建订单商品退货失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			List<OpenOrderDetailBean.Refund> refunds = openOrderDetail.getRefunds();
			Assert.assertEquals(refunds.size(), 1, "新建订单商品退货后,在订单详情中没有查询到退货信息");

			OpenOrderDetailBean.Refund refund = refunds.get(0);

			List<OrderRefundDeleteParam> orderRefundDeleteParamList = new ArrayList<>();
			OrderRefundDeleteParam orderRefundDeleteParam = new OrderRefundDeleteParam();
			orderRefundDeleteParam.setId(refund.getId());
			orderRefundDeleteParam.setSku_id(refund.getSku_id());
			orderRefundDeleteParamList.add(orderRefundDeleteParam);

			result = openOrderService.deleteOrderRefund(order_id, orderRefundDeleteParamList);
			Assert.assertEquals(result, true, "删除订单商品退货失败");

			openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			refunds = openOrderDetail.getRefunds();
			Assert.assertEquals(refunds == null || refunds.size() == 0, true, "在删除订单商品退货后,在订单详情里还可以查询到商品退货信息");
		} catch (Exception e) {
			logger.error("删除订单商品退货遇到错误: ", e);
			Assert.fail("删除订单商品退货遇到错误: ", e);
		}
	}

	@Test
	public void orderRefundTestCase06() {
		Reporter.log("测试点: 删除订单商品退货(批量)");
		try {

			List<OrderRefundCreateParam> orderRefundCreateParamList = new ArrayList<>();
			OrderRefundCreateParam orderRefundCreateParam = null;
			for (OrderProductParam orderProduct : products) {
				orderRefundCreateParam = new OrderRefundCreateParam();
				orderRefundCreateParam.setException_reason("1");
				orderRefundCreateParam.setSku_id(orderProduct.getSku_id());
				orderRefundCreateParam.setRequest_count(String.valueOf(NumberUtil.getRandomNumber(2, 5, 1)));
				orderRefundCreateParamList.add(orderRefundCreateParam);

			}

			boolean result = openOrderService.createOrderRefund(order_id, orderRefundCreateParamList);
			Assert.assertEquals(result, true, "新建订单商品退货失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			List<OpenOrderDetailBean.Refund> refunds = openOrderDetail.getRefunds();
			Assert.assertEquals(refunds.size(), products.size(), "查询到的订单商品退货条目数和预期的不一致");

			List<OrderRefundDeleteParam> orderRefundDeleteParamList = new ArrayList<>();
			OrderRefundDeleteParam orderRefundDeleteParam = null;
			for (OpenOrderDetailBean.Refund refund : refunds) {
				orderRefundDeleteParam = new OrderRefundDeleteParam();
				orderRefundDeleteParam.setId(refund.getId());
				orderRefundDeleteParam.setSku_id(refund.getSku_id());
				orderRefundDeleteParamList.add(orderRefundDeleteParam);

			}

			result = openOrderService.deleteOrderRefund(order_id, orderRefundDeleteParamList);
			Assert.assertEquals(result, true, "删除订单商品退货失败");

			openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			refunds = openOrderDetail.getRefunds();
			Assert.assertEquals(refunds == null || refunds.size() == 0, true, "在删除订单商品退货后,在订单详情里还可以查询到商品退货信息");
		} catch (Exception e) {
			logger.error("删除订单商品退货遇到错误: ", e);
			Assert.fail("删除订单商品退货遇到错误: ", e);
		}
	}

}
