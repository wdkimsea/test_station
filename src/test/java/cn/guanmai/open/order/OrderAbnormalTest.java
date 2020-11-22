package cn.guanmai.open.order;

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
import cn.guanmai.open.bean.order.param.OrderAbnormalUpdateParam;
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
import cn.guanmai.util.NumberUtil;

/* 
* @author liming 
* @date Jun 11, 2019 6:59:03 PM 
* @des 开放平台-订单异常测试
* @version 1.0 
*/
public class OrderAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OrderAbnormalTest.class);
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
	public void orderAbnormalTestCase01() {
		Reporter.log("测试点: 订单新建商品异常(单个)");
		try {

			String sku_id = NumberUtil.roundNumberInList(products).getSku_id();

			OrderAbnormalCreateParam orderAbnormalCreateParam = new OrderAbnormalCreateParam();
			orderAbnormalCreateParam.setSku_id(sku_id);
			orderAbnormalCreateParam.setException_reason("1");
			orderAbnormalCreateParam.setFinal_count("4");

			List<OrderAbnormalCreateParam> orderAbnormalList = Arrays.asList(orderAbnormalCreateParam);

			boolean result = openOrderService.createOrderAbnormal(order_id, orderAbnormalList);
			Assert.assertEquals(result, true, "订单 " + order_id + " 新建商品异常失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			List<OpenOrderDetailBean.Abnormal> abnormals = openOrderDetail.getAbnormals();

			OpenOrderDetailBean.Abnormal abnomal = abnormals.stream().filter(a -> a.getSku_id().equals(sku_id))
					.findAny().orElse(null);
			Assert.assertNotEquals(abnomal, null, "新建的订单商品异常在订单详情里没有找到");

			String msg = null;
			if (abnomal.getFinal_count().compareTo(new BigDecimal("4")) != 0) {
				msg = String.format("订单%s新增的商品%s异常,记账数和预期不一致,预期%s,实际%s", order_id, sku_id, "4",
						abnomal.getFinal_count());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			if (abnomal.getException_reason() != 1) {
				msg = String.format("订单%s新增的商品%s异常,异常原因编号和预期不一致,预期%s,实际%s", order_id, sku_id, "1",
						abnomal.getException_reason());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "订单 " + order_id + " 新增的商品异常和订单详情里展示的不一致");
		} catch (Exception e) {
			logger.error("新建订单异常过程中遇到错误: ", e);
			Assert.fail("新建订单异常过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderAbnormalTestCase02() {
		Reporter.log("测试点: 订单新建商品异常(批量)");
		try {
			List<OrderAbnormalCreateParam> orderAbnormalList = new ArrayList<>();
			OrderAbnormalCreateParam orderAbnormalCreateParam = null;
			BigDecimal final_amount = NumberUtil.getRandomNumber(2, 6, 0);
			for (OrderProductParam orderProduct : products) {
				orderAbnormalCreateParam = new OrderAbnormalCreateParam();
				orderAbnormalCreateParam.setSku_id(orderProduct.getSku_id());
				orderAbnormalCreateParam.setException_reason("1");
				orderAbnormalCreateParam.setFinal_count(String.valueOf(final_amount));
				orderAbnormalList.add(orderAbnormalCreateParam);
			}
			boolean result = openOrderService.createOrderAbnormal(order_id, orderAbnormalList);
			Assert.assertEquals(result, true, "订单 " + order_id + " 新建商品异常失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			String msg = null;
			for (OrderAbnormalCreateParam orderAbnormal : orderAbnormalList) {
				String sku_id = orderAbnormal.getSku_id();
				OpenOrderDetailBean.Abnormal abnomal = openOrderDetail.getAbnormals().stream()
						.filter(a -> a.getSku_id().equals(sku_id)).findAny().orElse(null);
				Assert.assertNotEquals(abnomal, null, "新建的订单商品异常在订单详情里没有找到");
				if (abnomal == null) {
					msg = String.format("新建的订单%s商品%s异常在订单详情里没有找到", order_id, sku_id);
					Reporter.log(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (abnomal.getFinal_count().compareTo(final_amount) != 0) {
					msg = String.format("订单%s新增的商品%s异常,记账数和预期不一致,预期%s,实际%s", order_id, sku_id, final_amount,
							abnomal.getFinal_count());
					Reporter.log(msg);
					logger.warn(msg);
					result = false;
				}

				if (abnomal.getException_reason() != 1) {
					msg = String.format("订单%s新增的商品%s异常,异常原因编号和预期不一致,预期%s,实际%s", order_id, sku_id, "1",
							abnomal.getException_reason());
					Reporter.log(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单 " + order_id + " 新增的商品异常和订单详情里展示的不一致");
		} catch (Exception e) {
			logger.error("新建订单异常过程中遇到错误: ", e);
			Assert.fail("新建订单异常过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderAbnormalTestCase03() {
		Reporter.log("测试点: 修改订单商品异常");
		try {
			String sku_id = NumberUtil.roundNumberInList(products).getSku_id();

			OrderAbnormalCreateParam orderAbnormalCreateParam = new OrderAbnormalCreateParam();
			orderAbnormalCreateParam.setSku_id(sku_id);
			orderAbnormalCreateParam.setException_reason("1");
			orderAbnormalCreateParam.setFinal_count("4");

			List<OrderAbnormalCreateParam> orderAbnormalList = Arrays.asList(orderAbnormalCreateParam);

			boolean result = openOrderService.createOrderAbnormal(order_id, orderAbnormalList);
			Assert.assertEquals(result, true, "订单 " + order_id + " 新建商品异常失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			List<OpenOrderDetailBean.Abnormal> abnomals = openOrderDetail.getAbnormals();

			OpenOrderDetailBean.Abnormal abnomal = abnomals.stream().filter(a -> a.getSku_id().equals(sku_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(abnomal, null, "新建的订单商品异常在订单详情里没有找到");

			String abnormal_id = abnomal.getId();

			OrderAbnormalUpdateParam orderAbnormalUpdateParam = new OrderAbnormalUpdateParam();
			orderAbnormalUpdateParam.setId(abnormal_id);
			orderAbnormalUpdateParam.setSku_id(sku_id);
			orderAbnormalUpdateParam.setException_reason("5");
			String update_request_amount = String.valueOf(NumberUtil.getRandomNumber(2, 6, 2));
			orderAbnormalUpdateParam.setFinal_count(update_request_amount);

			List<OrderAbnormalUpdateParam> orderAbnormalUpdateParamList = new ArrayList<>();
			orderAbnormalUpdateParamList.add(orderAbnormalUpdateParam);

			result = openOrderService.updateOrderAbnormal(order_id, orderAbnormalUpdateParamList);
			Assert.assertEquals(result, true, "修改订单" + order_id + "的商品异常失败");

			openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			abnomals = openOrderDetail.getAbnormals();

			abnomal = abnomals.stream().filter(a -> a.getId().equals(abnormal_id)).findAny().orElse(null);
			Assert.assertNotEquals(abnomal, null, "修改的订单商品异常在订单详情里没有找到");

			String msg = null;
			if (abnomal.getFinal_count().compareTo(new BigDecimal(update_request_amount)) != 0) {
				msg = String.format("订单%s新增的商品%s异常,记账数和预期不一致,预期%s,实际%s", order_id, sku_id, update_request_amount,
						abnomal.getFinal_count());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			if (abnomal.getException_reason() != 5) {
				msg = String.format("订单%s新增的商品%s异常,异常原因编号和预期不一致,预期%s,实际%s", order_id, sku_id, "5",
						abnomal.getException_reason());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "订单 " + order_id + " 修改的商品异常和订单详情里展示的不一致");
		} catch (Exception e) {
			logger.error("订单修改商品异常过程中遇到错误: ", e);
			Assert.fail("订单修改商品异常过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderAbnormalTestCase04() {
		Reporter.log("测试点: 修改订单商品异常(批量)");
		try {
			List<OrderAbnormalCreateParam> orderAbnormalList = new ArrayList<>();
			OrderAbnormalCreateParam orderAbnormalCreateParam = null;
			for (OrderProductParam orderProduct : products) {
				orderAbnormalCreateParam = new OrderAbnormalCreateParam();
				orderAbnormalCreateParam.setSku_id(orderProduct.getSku_id());
				orderAbnormalCreateParam.setException_reason("1");
				orderAbnormalCreateParam.setFinal_count(String.valueOf(NumberUtil.getRandomNumber(2, 6, 0)));
				orderAbnormalList.add(orderAbnormalCreateParam);
			}
			boolean result = openOrderService.createOrderAbnormal(order_id, orderAbnormalList);
			Assert.assertEquals(result, true, "订单 " + order_id + " 新建商品异常失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			List<OrderAbnormalUpdateParam> orderAbnormalUpdateParamList = new ArrayList<>();

			OrderAbnormalUpdateParam orderAbnormalUpdateParam = null;
			List<OpenOrderDetailBean.Abnormal> abnomals = openOrderDetail.getAbnormals();
			for (OpenOrderDetailBean.Abnormal abnormal : abnomals) {
				orderAbnormalUpdateParam = new OrderAbnormalUpdateParam();
				orderAbnormalUpdateParam.setException_reason("5");
				orderAbnormalUpdateParam.setId(abnormal.getId());
				orderAbnormalUpdateParam.setFinal_count(String.valueOf(NumberUtil.getRandomNumber(2, 6, 2)));
				orderAbnormalUpdateParam.setSku_id(abnormal.getSku_id());
				orderAbnormalUpdateParamList.add(orderAbnormalUpdateParam);
			}

			result = openOrderService.updateOrderAbnormal(order_id, orderAbnormalUpdateParamList);
			Assert.assertEquals(result, true, "修改订单" + order_id + "的商品异常失败");

			openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			abnomals = openOrderDetail.getAbnormals();
			String msg = null;
			for (OrderAbnormalUpdateParam orderAbnormalUpdate : orderAbnormalUpdateParamList) {
				String abnormal_id = orderAbnormalUpdate.getId();
				OpenOrderDetailBean.Abnormal abnomal = abnomals.stream().filter(a -> a.getId().equals(abnormal_id))
						.findAny().orElse(null);
				if (abnomal == null) {
					msg = String.format("订单%s里的异常编号为%s的异常数据没有找到", order_id, abnormal_id);
					result = false;
					Reporter.log(msg);
					logger.warn(msg);
					continue;
				}

				if (!abnomal.getSku_id().equals(orderAbnormalUpdate.getSku_id())) {
					msg = String.format("订单%s里的异常编号为%s的异常数据,商品ID和预期的不一致,预期%s,实际:%s", order_id, abnormal_id,
							orderAbnormalUpdate.getSku_id(), abnomal.getSku_id());
					result = false;
					Reporter.log(msg);
					logger.warn(msg);
				}

				if (abnomal.getFinal_count().compareTo(new BigDecimal(orderAbnormalUpdate.getFinal_count())) != 0) {
					msg = String.format("订单%s里的异常编号为%s的异常数据,异常数和预期的不一致,预期%s,实际:%s", order_id, abnormal_id,
							orderAbnormalUpdate.getException_reason(), abnomal.getFinal_count());
					result = false;
					Reporter.log(msg);
					logger.warn(msg);
				}

				if (abnomal.getException_reason() != 5) {
					msg = String.format("订单%s里的异常编号为%s的异常数据,商品异常原因和预期的不一致,预期%s,实际:%s", order_id, abnormal_id, "5",
							abnomal.getException_reason());
					result = false;
					Reporter.log(msg);
					logger.warn(msg);
				}
			}
			Assert.assertEquals(result, true, "订单 " + order_id + " 修改的商品异常和订单详情里展示的不一致");
		} catch (Exception e) {
			logger.error("修改订单异常过程中遇到错误: ", e);
			Assert.fail("修改订单异常过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderAbnormalTestCase05() {
		Reporter.log("测试点: 删除订单商品异常(单个)");
		try {
			String sku_id = NumberUtil.roundNumberInList(products).getSku_id();
			OrderAbnormalCreateParam orderAbnormalCreateParam = new OrderAbnormalCreateParam();
			orderAbnormalCreateParam.setSku_id(sku_id);
			orderAbnormalCreateParam.setException_reason("1");
			orderAbnormalCreateParam.setFinal_count("4");

			List<OrderAbnormalCreateParam> orderAbnormalList = Arrays.asList(orderAbnormalCreateParam);

			boolean result = openOrderService.createOrderAbnormal(order_id, orderAbnormalList);
			Assert.assertEquals(result, true, "订单 " + order_id + " 新建商品异常失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			List<OpenOrderDetailBean.Abnormal> abnomals = openOrderDetail.getAbnormals();

			OpenOrderDetailBean.Abnormal abnomal = abnomals.stream().filter(a -> a.getSku_id().equals(sku_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(abnomal, null, "新建的订单商品异常在订单详情里没有找到");

			// 删除订单商品异常
			List<OrderAbnormalDeleteParam> orderAbnormalDeleteParamList = new ArrayList<>();
			OrderAbnormalDeleteParam orderAbnormalDeleteParam = new OrderAbnormalDeleteParam();
			orderAbnormalDeleteParam.setId(abnomal.getId());
			orderAbnormalDeleteParam.setSku_id(sku_id);
			orderAbnormalDeleteParamList.add(orderAbnormalDeleteParam);
			result = openOrderService.deleteOrderAbnormal(order_id, orderAbnormalDeleteParamList);
			Assert.assertEquals(result, true, "删除订单商品异常失败");

			openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			abnomals = openOrderDetail.getAbnormals();
			Assert.assertEquals(abnomals == null || abnomals.size() == 0, true, "订单" + order_id + "的商品异常实际没有删除成功");

		} catch (Exception e) {
			logger.error("删除订单异常过程中遇到错误: ", e);
			Assert.fail("删除订单异常过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderAbnormalTestCase06() {
		Reporter.log("测试点: 删除订单商品异常(批量)");
		try {
			List<OrderAbnormalCreateParam> orderAbnormalList = new ArrayList<>();
			OrderAbnormalCreateParam orderAbnormalCreateParam = null;
			for (OrderProductParam orderProduct : products) {
				orderAbnormalCreateParam = new OrderAbnormalCreateParam();
				orderAbnormalCreateParam.setSku_id(orderProduct.getSku_id());
				orderAbnormalCreateParam.setException_reason("1");
				orderAbnormalCreateParam.setFinal_count(String.valueOf(NumberUtil.getRandomNumber(2, 6, 0)));
				orderAbnormalList.add(orderAbnormalCreateParam);
			}
			boolean result = openOrderService.createOrderAbnormal(order_id, orderAbnormalList);
			Assert.assertEquals(result, true, "订单 " + order_id + " 新建商品异常失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			// 删除订单商品异常
			List<OrderAbnormalDeleteParam> orderAbnormalDeleteParamList = new ArrayList<>();
			OrderAbnormalDeleteParam orderAbnormalDeleteParam = null;
			for (OpenOrderDetailBean.Abnormal abnomal : openOrderDetail.getAbnormals()) {
				orderAbnormalDeleteParam = new OrderAbnormalDeleteParam();
				orderAbnormalDeleteParam.setId(abnomal.getId());
				orderAbnormalDeleteParam.setSku_id(abnomal.getSku_id());
				orderAbnormalDeleteParamList.add(orderAbnormalDeleteParam);
			}

			result = openOrderService.deleteOrderAbnormal(order_id, orderAbnormalDeleteParamList);
			Assert.assertEquals(result, true, "删除订单商品异常失败");

			openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + " 详情失败");

			List<OpenOrderDetailBean.Abnormal> abnomals = openOrderDetail.getAbnormals();
			Assert.assertEquals(abnomals == null || abnomals.size() == 0, true, "订单" + order_id + "的商品异常实际没有删除成功");
		} catch (Exception e) {
			logger.error("新建订单异常过程中遇到错误: ", e);
			Assert.fail("新建订单异常过程中遇到错误: ", e);
		}
	}

}
