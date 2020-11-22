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
import cn.guanmai.open.bean.order.param.OrderCreateParam;
import cn.guanmai.open.bean.order.param.OrderProductParam;
import cn.guanmai.open.bean.order.param.OrderRefundCreateParam;
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

/* 
* @author liming 
* @date Jun 12, 2019 7:28:43 PM 
* @des 开放平台订单退货修改异常测试
* @version 1.0 
*/
public class OrderRefundUpdateAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OrderRefundUpdateAbnormalTest.class);
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
	private OpenOrderDetailBean openOrderDetail;
	private OpenOrderDetailBean.Refund refund;

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

			OrderProductParam orderProductParam = products.get(0);

			OrderRefundCreateParam orderRefundCreateParam = new OrderRefundCreateParam();
			orderRefundCreateParam.setSku_id(orderProductParam.getSku_id());
			orderRefundCreateParam.setRequest_count("2");
			orderRefundCreateParam.setException_reason("1");
			List<OrderRefundCreateParam> orderRefundCreateParamList = Arrays.asList(orderRefundCreateParam);

			boolean result = openOrderService.createOrderRefund(order_id, orderRefundCreateParamList);
			Assert.assertEquals(result, true, "新建订单退货,创建失败");

			openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			List<OpenOrderDetailBean.Refund> refunds = openOrderDetail.getRefunds();
			Assert.assertEquals(refunds.size(), 1, "新建的订单异常在订单详细中没有找到");

			refund = refunds.get(0);
		} catch (Exception e) {
			logger.error("商户下单遇到错误: ", e);
			Assert.fail("商户下单遇到错误: ", e);
		}
	}

	@Test
	public void orderRefundUpdateAbnormalTestCase01() {
		Reporter.log("测试点: 修改订单退货异常测试,不传入订单ID,断言失败");
		try {
			List<OrderRefundUpdateParam> orderRefundUpdateParamList = new ArrayList<>();

			OrderRefundUpdateParam orderRefundUpdateParam = new OrderRefundUpdateParam();
			orderRefundUpdateParam.setId(refund.getId());
			orderRefundUpdateParam.setException_reason("1");
			orderRefundUpdateParam.setRequest_count("2.1");
			orderRefundUpdateParam.setSku_id(refund.getSku_id());

			orderRefundUpdateParamList.add(orderRefundUpdateParam);

			boolean result = openOrderService.updateOrderRefund("", orderRefundUpdateParamList);
			Assert.assertEquals(result, false, "修改订单退货异常测试,不传入订单ID,断言失败");

		} catch (Exception e) {
			logger.error("修改订单退货遇到错误: ", e);
			Assert.fail("修改订单退货遇到错误: ", e);
		}
	}

	@Test
	public void orderRefundUpdateAbnormalTestCase02() {
		Reporter.log("测试点: 修改订单退货异常测试,传入错误的订单ID,断言失败");
		try {
			List<OrderRefundUpdateParam> orderRefundUpdateParamList = new ArrayList<>();

			OrderRefundUpdateParam orderRefundUpdateParam = new OrderRefundUpdateParam();
			orderRefundUpdateParam.setId(refund.getId());
			orderRefundUpdateParam.setException_reason("1");
			orderRefundUpdateParam.setRequest_count("2.1");
			orderRefundUpdateParam.setSku_id(refund.getSku_id());

			orderRefundUpdateParamList.add(orderRefundUpdateParam);

			boolean result = openOrderService.updateOrderRefund(order_id + "0", orderRefundUpdateParamList);
			Assert.assertEquals(result, false, "修改订单退货异常测试,传入错误的订单ID,断言失败");

		} catch (Exception e) {
			logger.error("修改订单退货遇到错误: ", e);
			Assert.fail("修改订单退货遇到错误: ", e);
		}
	}

	@Test
	public void orderRefundUpdateAbnormalTestCase03() {
		Reporter.log("测试点: 修改订单退货异常测试,不传入退货申请ID,断言失败");
		try {
			List<OrderRefundUpdateParam> orderRefundUpdateParamList = new ArrayList<>();

			OrderRefundUpdateParam orderRefundUpdateParam = new OrderRefundUpdateParam();
			orderRefundUpdateParam.setId("");
			orderRefundUpdateParam.setException_reason("1");
			orderRefundUpdateParam.setRequest_count("2.1");
			orderRefundUpdateParam.setSku_id(refund.getSku_id());

			orderRefundUpdateParamList.add(orderRefundUpdateParam);

			boolean result = openOrderService.updateOrderRefund(order_id, orderRefundUpdateParamList);
			Assert.assertEquals(result, false, "修改订单退货异常测试,不传入退货申请ID,断言失败");

		} catch (Exception e) {
			logger.error("修改订单退货遇到错误: ", e);
			Assert.fail("修改订单退货遇到错误: ", e);
		}
	}

	@Test
	public void orderRefundUpdateAbnormalTestCase04() {
		Reporter.log("测试点: 修改订单退货异常测试,传入非数字退货ID,断言失败");
		try {
			List<OrderRefundUpdateParam> orderRefundUpdateParamList = new ArrayList<>();

			OrderRefundUpdateParam orderRefundUpdateParam = new OrderRefundUpdateParam();
			orderRefundUpdateParam.setId("A");
			orderRefundUpdateParam.setException_reason("1");
			orderRefundUpdateParam.setRequest_count("2.1");
			orderRefundUpdateParam.setSku_id(refund.getSku_id());

			orderRefundUpdateParamList.add(orderRefundUpdateParam);

			boolean result = openOrderService.updateOrderRefund(order_id, orderRefundUpdateParamList);
			Assert.assertEquals(result, false, "修改订单退货异常测试,传入非数字退货ID,断言失败");

		} catch (Exception e) {
			logger.error("修改订单退货遇到错误: ", e);
			Assert.fail("修改订单退货遇到错误: ", e);
		}
	}

	@Test
	public void orderRefundUpdateAbnormalTestCase05() {
		Reporter.log("测试点: 修改订单退货异常测试,传入非本订单中的退货ID,断言失败");
		try {
			List<OrderRefundUpdateParam> orderRefundUpdateParamList = new ArrayList<>();

			OrderRefundUpdateParam orderRefundUpdateParam = new OrderRefundUpdateParam();

			String id = new BigDecimal(refund.getId()).subtract(new BigDecimal("1")).toString();
			orderRefundUpdateParam.setId(id);
			orderRefundUpdateParam.setException_reason("1");
			orderRefundUpdateParam.setRequest_count("2.1");
			orderRefundUpdateParam.setSku_id(refund.getSku_id());

			orderRefundUpdateParamList.add(orderRefundUpdateParam);

			boolean result = openOrderService.updateOrderRefund(order_id, orderRefundUpdateParamList);
			Assert.assertEquals(result, false, "修改订单退货异常测试,传入非本订单中的退货ID,断言失败");

		} catch (Exception e) {
			logger.error("修改订单退货遇到错误: ", e);
			Assert.fail("修改订单退货遇到错误: ", e);
		}
	}

	@Test
	public void orderRefundUpdateAbnormalTestCase06() {
		Reporter.log("测试点: 修改订单退货异常测试,不传入退货商品ID,断言失败");
		try {
			List<OrderRefundUpdateParam> orderRefundUpdateParamList = new ArrayList<>();

			OrderRefundUpdateParam orderRefundUpdateParam = new OrderRefundUpdateParam();
			orderRefundUpdateParam.setId(refund.getId());
			orderRefundUpdateParam.setException_reason("1");
			orderRefundUpdateParam.setRequest_count("2.1");
			orderRefundUpdateParam.setSku_id("");

			orderRefundUpdateParamList.add(orderRefundUpdateParam);

			boolean result = openOrderService.updateOrderRefund(order_id, orderRefundUpdateParamList);
			Assert.assertEquals(result, false, "修改订单退货异常测试,不传入退货商品ID,断言失败");

		} catch (Exception e) {
			logger.error("修改订单退货遇到错误: ", e);
			Assert.fail("修改订单退货遇到错误: ", e);
		}
	}

	@Test
	public void orderRefundUpdateAbnormalTestCase07() {
		Reporter.log("测试点: 修改订单退货异常测试,传入非对应的商品ID,断言失败");
		try {
			List<OrderRefundUpdateParam> orderRefundUpdateParamList = new ArrayList<>();

			String sku_id = products.get(products.size() - 1).getSku_id();
			OrderRefundUpdateParam orderRefundUpdateParam = new OrderRefundUpdateParam();
			orderRefundUpdateParam.setId(refund.getId());
			orderRefundUpdateParam.setException_reason("1");
			orderRefundUpdateParam.setRequest_count("2.1");
			orderRefundUpdateParam.setSku_id(sku_id);

			orderRefundUpdateParamList.add(orderRefundUpdateParam);

			boolean result = openOrderService.updateOrderRefund(order_id, orderRefundUpdateParamList);
			Assert.assertEquals(result, false, "修改订单退货异常测试,传入非对应的商品ID,断言失败");

		} catch (Exception e) {
			logger.error("修改订单退货遇到错误: ", e);
			Assert.fail("修改订单退货遇到错误: ", e);
		}
	}

	@Test
	public void orderRefundUpdateAbnormalTestCase08() {
		Reporter.log("测试点: 修改订单退货异常测试,不传入异常原因编号,断言失败");
		try {
			List<OrderRefundUpdateParam> orderRefundUpdateParamList = new ArrayList<>();

			OrderRefundUpdateParam orderRefundUpdateParam = new OrderRefundUpdateParam();
			orderRefundUpdateParam.setId(refund.getId());
			orderRefundUpdateParam.setException_reason("");
			orderRefundUpdateParam.setRequest_count("2.1");
			orderRefundUpdateParam.setSku_id(refund.getSku_id());

			orderRefundUpdateParamList.add(orderRefundUpdateParam);

			boolean result = openOrderService.updateOrderRefund(order_id, orderRefundUpdateParamList);
			Assert.assertEquals(result, false, "修改订单退货异常测试,传入非对应的商品ID,断言失败");

		} catch (Exception e) {
			logger.error("修改订单退货遇到错误: ", e);
			Assert.fail("修改订单退货遇到错误: ", e);
		}
	}

	@Test
	public void orderRefundUpdateAbnormalTestCase09() {
		Reporter.log("测试点: 修改订单退货异常测试,传入非候选值的异常原因编号,断言失败");
		try {
			List<OrderRefundUpdateParam> orderRefundUpdateParamList = new ArrayList<>();

			OrderRefundUpdateParam orderRefundUpdateParam = new OrderRefundUpdateParam();
			orderRefundUpdateParam.setId(refund.getId());
			orderRefundUpdateParam.setException_reason("20");
			orderRefundUpdateParam.setRequest_count("2.1");
			orderRefundUpdateParam.setSku_id(refund.getSku_id());

			orderRefundUpdateParamList.add(orderRefundUpdateParam);

			boolean result = openOrderService.updateOrderRefund(order_id, orderRefundUpdateParamList);
			Assert.assertEquals(result, false, "修改订单退货异常测试,传入非对应的商品ID,断言失败");

		} catch (Exception e) {
			logger.error("修改订单退货遇到错误: ", e);
			Assert.fail("修改订单退货遇到错误: ", e);
		}
	}

	@Test
	public void orderRefundUpdateAbnormalTestCase10() {
		Reporter.log("测试点: 修改订单退货异常测试,不传入退货数,断言失败");
		try {
			List<OrderRefundUpdateParam> orderRefundUpdateParamList = new ArrayList<>();

			OrderRefundUpdateParam orderRefundUpdateParam = new OrderRefundUpdateParam();
			orderRefundUpdateParam.setId(refund.getId());
			orderRefundUpdateParam.setException_reason("1");
			orderRefundUpdateParam.setRequest_count("");
			orderRefundUpdateParam.setSku_id(refund.getSku_id());

			orderRefundUpdateParamList.add(orderRefundUpdateParam);

			boolean result = openOrderService.updateOrderRefund(order_id, orderRefundUpdateParamList);
			Assert.assertEquals(result, false, "修改订单退货异常测试,不传入退货数,断言失败");

		} catch (Exception e) {
			logger.error("修改订单退货遇到错误: ", e);
			Assert.fail("修改订单退货遇到错误: ", e);
		}
	}

	@Test
	public void orderRefundUpdateAbnormalTestCase11() {
		Reporter.log("测试点: 修改订单退货异常测试,传入非数字退货数,断言失败");
		try {
			List<OrderRefundUpdateParam> orderRefundUpdateParamList = new ArrayList<>();

			OrderRefundUpdateParam orderRefundUpdateParam = new OrderRefundUpdateParam();
			orderRefundUpdateParam.setId(refund.getId());
			orderRefundUpdateParam.setException_reason("1");
			orderRefundUpdateParam.setRequest_count("A");
			orderRefundUpdateParam.setSku_id(refund.getSku_id());

			orderRefundUpdateParamList.add(orderRefundUpdateParam);

			boolean result = openOrderService.updateOrderRefund(order_id, orderRefundUpdateParamList);
			Assert.assertEquals(result, false, "修改订单退货异常测试,传入非数字退货数,断言失败");

		} catch (Exception e) {
			logger.error("修改订单退货遇到错误: ", e);
			Assert.fail("修改订单退货遇到错误: ", e);
		}
	}

	@Test
	public void orderRefundUpdateAbnormalTestCase12() {
		Reporter.log("测试点: 修改订单退货异常测试,传入负数退货数,断言失败");
		try {
			List<OrderRefundUpdateParam> orderRefundUpdateParamList = new ArrayList<>();

			OrderRefundUpdateParam orderRefundUpdateParam = new OrderRefundUpdateParam();
			orderRefundUpdateParam.setId(refund.getId());
			orderRefundUpdateParam.setException_reason("1");
			orderRefundUpdateParam.setRequest_count("-2");
			orderRefundUpdateParam.setSku_id(refund.getSku_id());

			orderRefundUpdateParamList.add(orderRefundUpdateParam);

			boolean result = openOrderService.updateOrderRefund(order_id, orderRefundUpdateParamList);
			Assert.assertEquals(result, false, "修改订单退货异常测试,传入非数字退货数,断言失败");

		} catch (Exception e) {
			logger.error("修改订单退货遇到错误: ", e);
			Assert.fail("修改订单退货遇到错误: ", e);
		}
	}

	@Test
	public void orderRefundUpdateAbnormalTestCase13() {
		Reporter.log("测试点: 修改订单退货异常测试,传入退货数为0,断言失败");
		try {
			List<OrderRefundUpdateParam> orderRefundUpdateParamList = new ArrayList<>();

			OrderRefundUpdateParam orderRefundUpdateParam = new OrderRefundUpdateParam();
			orderRefundUpdateParam.setId(refund.getId());
			orderRefundUpdateParam.setException_reason("1");
			orderRefundUpdateParam.setRequest_count("0");
			orderRefundUpdateParam.setSku_id(refund.getSku_id());

			orderRefundUpdateParamList.add(orderRefundUpdateParam);

			boolean result = openOrderService.updateOrderRefund(order_id, orderRefundUpdateParamList);
			Assert.assertEquals(result, false, "修改订单退货异常测试,传入退货数为0,断言失败");

		} catch (Exception e) {
			logger.error("修改订单退货遇到错误: ", e);
			Assert.fail("修改订单退货遇到错误: ", e);
		}
	}

//	@Test
//	public void orderRefundUpdateAbnormalTestCase14() {
//		Reporter.log("测试点: 修改订单退货异常测试,传入退货数大于出库数,断言失败");
//		try {
//			List<OrderRefundUpdateParam> orderRefundUpdateParamList = new ArrayList<>();
//
//			OrderRefundUpdateParam orderRefundUpdateParam = new OrderRefundUpdateParam();
//			orderRefundUpdateParam.setId(refund.getId());
//			orderRefundUpdateParam.setException_reason("1");
//			orderRefundUpdateParam.setRequest_count("20");
//			orderRefundUpdateParam.setSku_id(refund.getSku_id());
//
//			orderRefundUpdateParamList.add(orderRefundUpdateParam);
//
//			boolean result = openOrderService.updateOrderRefund(order_id, orderRefundUpdateParamList);
//			Assert.assertEquals(result, false, "修改订单退货异常测试,传入退货数大于出库数,断言失败");
//
//		} catch (Exception e) {
//			logger.error("修改订单退货遇到错误: ", e);
//			Assert.fail("修改订单退货遇到错误: ", e);
//		}
//	}

}
