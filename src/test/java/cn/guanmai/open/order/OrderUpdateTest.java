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
import org.testng.annotations.Test;

import cn.guanmai.open.bean.customer.OpenCustomerBean;
import cn.guanmai.open.bean.order.OpenOrderDetailBean;
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
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Jun 11, 2019 2:26:21 PM 
* @des 开放平台修改订单测试
* @version 1.0 
*/
public class OrderUpdateTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OrderUpdateTest.class);
	private OpenOrderService openOrderService;
	private OpenCustomerService openCustomerService;
	private OpenSalemenuService openSalemenuService;
	private OpenCustomerBean openCustomer;
	private OpenCategoryService openCategoryService;
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
			Assert.assertEquals(order_customer_list.size() > 0, true, "没有可用的下单商户");

			openCustomer = NumberUtil.roundNumberInList(order_customer_list);

			openSalemenus = openSalemenuService.searchSalemenu(openCustomer.getCustomer_id(), 1);
			Assert.assertNotNull(openSalemenus, "根据商户查询报价单集合失败");

			openSalemenuGroup = openSalemenus.stream()
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
		} catch (Exception e) {
			logger.error("搜索过滤下单商户遇到错误: ", e);
			Assert.fail("搜索过滤下单商户遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateTestCase01() {
		ReporterCSS.title("测试点: 修改订单,为订单新增商品");
		// 取第一个运营时间的所有的报价单
		String time_config_id = openSalemenuGroup.keySet().iterator().next();
		List<OpenSalemenuBean> openSalemenus = openSalemenuGroup.get(time_config_id);
		List<OpenSaleSkuBean> openSaleSkus = new ArrayList<>();

		OpenSaleSkuFilterParam filterParam = new OpenSaleSkuFilterParam();
		try {
			for (OpenSalemenuBean openSalemenu : openSalemenus) {
				filterParam.setSalemenu_id(openSalemenu.getId());
				List<OpenSaleSkuBean> tempOpenSaleSkus = openCategoryService.seachSaleSku(filterParam);
				Assert.assertNotEquals(tempOpenSaleSkus, null, "以报价单ID搜索过滤商品遇到错误");
				openSaleSkus
						.addAll(tempOpenSaleSkus.stream().filter(s -> s.getState() == 1).collect(Collectors.toList()));
			}

			// 创建订单的商品集合
			List<OrderProductParam> c_products = new ArrayList<>();
			// 修改订单的商品集合
			List<OrderProductParam> u_products = new ArrayList<>();
			OrderProductParam product = null;
			for (OpenSaleSkuBean openSaleSku : openSaleSkus) {
				product = new OrderProductParam();
				product.setSku_id(openSaleSku.getId());
				product.setCount("10");
				product.setIs_price_timing("0");
				product.setPrice("5");
				if (c_products.size() <= 6) {
					c_products.add(product);
				} else {
					if (u_products.size() <= 2) {
						product.setPrice("4");
						u_products.add(product);
					} else {
						break;
					}
				}

			}

			OpenReceiveTimeBean openReceiveTime = openCategoryService.getReceiveTime(time_config_id);
			Assert.assertNotEquals(openReceiveTime, null, "获取运营时间的收货时间信息失败");

			String receive_time_day = openReceiveTime.getReceive_time().keySet().iterator().next();
			List<String> receive_time_list = openReceiveTime.getReceive_time().get(receive_time_day);
			String receive_time_start = receive_time_list.get(0);
			String receive_time_end = receive_time_list.get(1);

			OrderCreateParam orderCreateParam = new OrderCreateParam();
			orderCreateParam.setProducts(c_products);
			orderCreateParam.setReceive_begin_time(receive_time_start);
			orderCreateParam.setReceive_end_time(receive_time_end);
			orderCreateParam.setCustomer_id(openCustomer.getCustomer_id());
			orderCreateParam.setTime_config_id(time_config_id);

			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertNotEquals(order_id, null, "通过开放平台下单失败");

			Assert.assertEquals(u_products.size() > 0, true, "无可用商品进行订单修改");
			boolean result = openOrderService.addOrderSkus(order_id, u_products);
			Assert.assertEquals(result, true, "为订单添加新增商品失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotNull(openOrderDetail, "获取订单的详细信息失败");

			for (OrderProductParam orderProduct : u_products) {
				String sku_id = orderProduct.getSku_id();
				OpenOrderDetailBean.Detail detail = openOrderDetail.getDetails().stream()
						.filter(d -> d.getSku_id().equals(sku_id)).findAny().orElse(null);
				if (detail == null) {
					Reporter.log("修改订单,新增商品 " + sku_id + "在订单中 " + order_id + " 没有查找到");
					result = false;
				}
			}
			Assert.assertEquals(result, true, "修改订单,存在新增的商品在订单中没有查找到");
		} catch (Exception e) {
			logger.error("商户下单-修改订单过程中遇到错误: ", e);
			Assert.fail("商户下单-修改订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateTestCase02() {
		ReporterCSS.title("测试点: 修改订单,修改订单的收货时间");
		// 取第一个运营时间的所有的报价单
		String time_config_id = openSalemenuGroup.keySet().iterator().next();
		List<OpenSalemenuBean> openSalemenus = openSalemenuGroup.get(time_config_id);
		List<OpenSaleSkuBean> openSaleSkus = new ArrayList<>();

		OpenSaleSkuFilterParam filterParam = new OpenSaleSkuFilterParam();
		try {
			for (OpenSalemenuBean openSalemenu : openSalemenus) {
				filterParam.setSalemenu_id(openSalemenu.getId());
				List<OpenSaleSkuBean> tempOpenSaleSkus = openCategoryService.seachSaleSku(filterParam);
				Assert.assertNotEquals(tempOpenSaleSkus, null, "以报价单ID搜索过滤商品遇到错误");
				openSaleSkus
						.addAll(tempOpenSaleSkus.stream().filter(s -> s.getState() == 1).collect(Collectors.toList()));
			}

			// 创建订单的商品集合
			List<OrderProductParam> c_products = new ArrayList<>();

			OrderProductParam product = null;
			for (OpenSaleSkuBean openSaleSku : openSaleSkus) {
				product = new OrderProductParam();
				product.setSku_id(openSaleSku.getId());
				product.setCount("10");
				product.setIs_price_timing("0");
				product.setPrice("5");
				if (c_products.size() <= 6) {
					c_products.add(product);
				} else {
					break;
				}

			}

			OpenReceiveTimeBean openReceiveTime = openCategoryService.getReceiveTime(time_config_id);
			Assert.assertNotEquals(openReceiveTime, null, "获取运营时间的收货时间信息失败");

			String receive_time_day = openReceiveTime.getReceive_time().keySet().iterator().next();
			List<String> receive_time_list = openReceiveTime.getReceive_time().get(receive_time_day);
			String receive_time_start = receive_time_list.get(0);
			String receive_time_end = receive_time_list.get(1);

			OrderCreateParam orderCreateParam = new OrderCreateParam();
			orderCreateParam.setProducts(c_products);
			orderCreateParam.setReceive_begin_time(receive_time_start);
			orderCreateParam.setReceive_end_time(receive_time_end);
			orderCreateParam.setCustomer_id(openCustomer.getCustomer_id());
			orderCreateParam.setTime_config_id(time_config_id);

			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertNotEquals(order_id, null, "通过开放平台下单失败");

			String u_receive_time_start = receive_time_list.get(receive_time_list.size() - 2);
			String u_receive_time_end = receive_time_list.get(receive_time_list.size() - 1);

			OrderUpdateParam orderUpdateParam = new OrderUpdateParam();
			orderUpdateParam.setOrder_id(order_id);
			orderUpdateParam.setReceive_begin_time(u_receive_time_start);
			orderUpdateParam.setReceive_end_time(u_receive_time_end);

			boolean reuslt = openOrderService.updateOrder(orderUpdateParam);
			Assert.assertEquals(reuslt, true, "修改订单,修改订单的收货时间失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotNull(openOrderDetail, "获取订单" + order_id + "的详细信息失败");

			if (!openOrderDetail.getReceive_begin_time().equals(u_receive_time_start)) {
				Reporter.log("修改订单,修改的新的收货起始时间没有生效");
				reuslt = false;
			}

			if (!openOrderDetail.getReceive_end_time().equals(u_receive_time_end)) {
				Reporter.log("修改订单,修改的新的收货结束时间没有生效");
				reuslt = false;
			}
			Assert.assertEquals(reuslt, true, "修改订单,修改的收货时间没有生效");
		} catch (Exception e) {
			logger.error("商户下单-修改订单过程中遇到错误: ", e);
			Assert.fail("商户下单-修改订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateTestCase03() {
		ReporterCSS.title("测试点: 修改订单,修改订单的备注");
		// 取第一个运营时间的所有的报价单
		String time_config_id = openSalemenuGroup.keySet().iterator().next();
		List<OpenSalemenuBean> openSalemenus = openSalemenuGroup.get(time_config_id);
		List<OpenSaleSkuBean> openSaleSkus = new ArrayList<>();

		OpenSaleSkuFilterParam filterParam = new OpenSaleSkuFilterParam();
		try {
			for (OpenSalemenuBean openSalemenu : openSalemenus) {
				filterParam.setSalemenu_id(openSalemenu.getId());
				List<OpenSaleSkuBean> tempOpenSaleSkus = openCategoryService.seachSaleSku(filterParam);
				Assert.assertNotEquals(tempOpenSaleSkus, null, "以报价单ID搜索过滤商品遇到错误");
				openSaleSkus
						.addAll(tempOpenSaleSkus.stream().filter(s -> s.getState() == 1).collect(Collectors.toList()));
			}

			// 创建订单的商品集合
			List<OrderProductParam> c_products = new ArrayList<>();

			OrderProductParam product = null;
			for (OpenSaleSkuBean openSaleSku : openSaleSkus) {
				product = new OrderProductParam();
				product.setSku_id(openSaleSku.getId());
				product.setCount("10");
				product.setIs_price_timing("0");
				product.setPrice("5");
				if (c_products.size() <= 6) {
					c_products.add(product);
				} else {
					break;
				}

			}

			OpenReceiveTimeBean openReceiveTime = openCategoryService.getReceiveTime(time_config_id);
			Assert.assertNotEquals(openReceiveTime, null, "获取运营时间的收货时间信息失败");

			String receive_time_day = openReceiveTime.getReceive_time().keySet().iterator().next();
			List<String> receive_time_list = openReceiveTime.getReceive_time().get(receive_time_day);
			String receive_time_start = receive_time_list.get(0);
			String receive_time_end = receive_time_list.get(1);

			OrderCreateParam orderCreateParam = new OrderCreateParam();
			orderCreateParam.setProducts(c_products);
			orderCreateParam.setReceive_begin_time(receive_time_start);
			orderCreateParam.setReceive_end_time(receive_time_end);
			orderCreateParam.setCustomer_id(openCustomer.getCustomer_id());
			orderCreateParam.setTime_config_id(time_config_id);

			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertNotEquals(order_id, null, "通过开放平台下单失败");

			OrderUpdateParam orderUpdateParam = new OrderUpdateParam();
			orderUpdateParam.setOrder_id(order_id);
			String remark = "修改备注信息";
			orderUpdateParam.setRemark(remark);

			boolean result = openOrderService.updateOrder(orderUpdateParam);
			Assert.assertEquals(result, true, "修改订单,修改订单备注失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + "详细信息失败");

			Assert.assertEquals(openOrderDetail.getRemark(), remark, "修改订单备注,查询到的备注信息与预期的不一致");
		} catch (Exception e) {
			logger.error("商户下单-修改订单过程中遇到错误: ", e);
			Assert.fail("商户下单-修改订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateTestCase04() {
		ReporterCSS.title("测试点: 修改订单,修改订单的状态");
		// 取第一个运营时间的所有的报价单
		String time_config_id = openSalemenuGroup.keySet().iterator().next();
		List<OpenSalemenuBean> openSalemenus = openSalemenuGroup.get(time_config_id);
		List<OpenSaleSkuBean> openSaleSkus = new ArrayList<>();

		OpenSaleSkuFilterParam filterParam = new OpenSaleSkuFilterParam();
		try {
			for (OpenSalemenuBean openSalemenu : openSalemenus) {
				filterParam.setSalemenu_id(openSalemenu.getId());
				List<OpenSaleSkuBean> tempOpenSaleSkus = openCategoryService.seachSaleSku(filterParam);
				Assert.assertNotEquals(tempOpenSaleSkus, null, "以报价单ID搜索过滤商品遇到错误");
				openSaleSkus
						.addAll(tempOpenSaleSkus.stream().filter(s -> s.getState() == 1).collect(Collectors.toList()));
			}

			// 创建订单的商品集合
			List<OrderProductParam> c_products = new ArrayList<>();

			OrderProductParam product = null;
			for (OpenSaleSkuBean openSaleSku : openSaleSkus) {
				product = new OrderProductParam();
				product.setSku_id(openSaleSku.getId());
				product.setCount("10");
				product.setIs_price_timing("0");
				product.setPrice("5");
				if (c_products.size() <= 6) {
					c_products.add(product);
				} else {
					break;
				}

			}

			OpenReceiveTimeBean openReceiveTime = openCategoryService.getReceiveTime(time_config_id);
			Assert.assertNotEquals(openReceiveTime, null, "获取运营时间的收货时间信息失败");

			String receive_time_day = openReceiveTime.getReceive_time().keySet().iterator().next();
			List<String> receive_time_list = openReceiveTime.getReceive_time().get(receive_time_day);

			String receive_time_start = receive_time_list.get(0);
			String receive_time_end = receive_time_list.get(1);

			OrderCreateParam orderCreateParam = new OrderCreateParam();
			orderCreateParam.setProducts(c_products);
			orderCreateParam.setReceive_begin_time(receive_time_start);
			orderCreateParam.setReceive_end_time(receive_time_end);
			orderCreateParam.setCustomer_id(openCustomer.getCustomer_id());
			orderCreateParam.setTime_config_id(time_config_id);

			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertNotEquals(order_id, null, "通过开放平台下单失败");

			OrderUpdateParam orderUpdateParam = new OrderUpdateParam();
			orderUpdateParam.setOrder_id(order_id);
			orderUpdateParam.setStatus(5);

			boolean result = openOrderService.updateOrder(orderUpdateParam);
			Assert.assertEquals(result, true, "修改订单,修改订单状态值失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + "详细信息失败");

			Assert.assertEquals(openOrderDetail.getStatus(), 5, "修改订单状态,查询到的状态值与预期的不一致");
		} catch (Exception e) {
			logger.error("商户下单-修改订单过程中遇到错误: ", e);
			Assert.fail("商户下单-修改订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateTestCase05() {
		ReporterCSS.title("测试点: 修改订单,删除订单中的商品(单个)");
		// 取第一个运营时间的所有的报价单
		String time_config_id = openSalemenuGroup.keySet().iterator().next();
		List<OpenSalemenuBean> openSalemenus = openSalemenuGroup.get(time_config_id);
		List<OpenSaleSkuBean> openSaleSkus = new ArrayList<>();

		OpenSaleSkuFilterParam filterParam = new OpenSaleSkuFilterParam();
		try {
			for (OpenSalemenuBean openSalemenu : openSalemenus) {
				filterParam.setSalemenu_id(openSalemenu.getId());
				List<OpenSaleSkuBean> tempOpenSaleSkus = openCategoryService.seachSaleSku(filterParam);
				Assert.assertNotEquals(tempOpenSaleSkus, null, "以报价单ID搜索过滤商品遇到错误");
				openSaleSkus
						.addAll(tempOpenSaleSkus.stream().filter(s -> s.getState() == 1).collect(Collectors.toList()));
			}

			// 创建订单的商品集合
			List<OrderProductParam> c_products = new ArrayList<>();

			OrderProductParam product = null;
			for (OpenSaleSkuBean openSaleSku : openSaleSkus) {
				product = new OrderProductParam();
				product.setSku_id(openSaleSku.getId());
				product.setCount("10");
				product.setIs_price_timing("0");
				product.setPrice("5");
				if (c_products.size() <= 6) {
					c_products.add(product);
				} else {
					break;
				}

			}

			OpenReceiveTimeBean openReceiveTime = openCategoryService.getReceiveTime(time_config_id);
			Assert.assertNotEquals(openReceiveTime, null, "获取运营时间的收货时间信息失败");

			String receive_time_day = openReceiveTime.getReceive_time().keySet().iterator().next();
			List<String> receive_time_list = openReceiveTime.getReceive_time().get(receive_time_day);

			String receive_time_start = receive_time_list.get(0);
			String receive_time_end = receive_time_list.get(1);

			OrderCreateParam orderCreateParam = new OrderCreateParam();
			orderCreateParam.setProducts(c_products);
			orderCreateParam.setReceive_begin_time(receive_time_start);
			orderCreateParam.setReceive_end_time(receive_time_end);
			orderCreateParam.setCustomer_id(openCustomer.getCustomer_id());
			orderCreateParam.setTime_config_id(time_config_id);

			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertNotEquals(order_id, null, "通过开放平台下单失败");

			List<String> sku_ids = new ArrayList<>();
			// 从下单列表里随机取一个SKU进行删除
			String sku_id = NumberUtil.roundNumberInList(c_products).getSku_id();
			sku_ids.add(sku_id);

			boolean reuslt = openOrderService.deleteOrderSkus(order_id, sku_ids);
			Assert.assertEquals(reuslt, true, "修改订单,删除订单中的商品失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + "详细信息失败");

			OpenOrderDetailBean.Detail detail = openOrderDetail.getDetails().stream()
					.filter(d -> d.getSku_id().equals(sku_id)).findAny().orElse(null);
			Assert.assertEquals(detail, null, "修改订单,删除订单中的商品,实际订单中" + order_id + "的商品" + sku_id + "没有删除");
		} catch (Exception e) {
			logger.error("商户下单-修改订单过程中遇到错误: ", e);
			Assert.fail("商户下单-修改订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateTestCase06() {
		ReporterCSS.title("测试点: 修改订单,删除订单中的商品(多个)");
		// 取第一个运营时间的所有的报价单
		String time_config_id = openSalemenuGroup.keySet().iterator().next();
		List<OpenSalemenuBean> openSalemenus = openSalemenuGroup.get(time_config_id);
		List<OpenSaleSkuBean> openSaleSkus = new ArrayList<>();

		OpenSaleSkuFilterParam filterParam = new OpenSaleSkuFilterParam();
		try {
			for (OpenSalemenuBean openSalemenu : openSalemenus) {
				filterParam.setSalemenu_id(openSalemenu.getId());
				List<OpenSaleSkuBean> tempOpenSaleSkus = openCategoryService.seachSaleSku(filterParam);
				Assert.assertNotEquals(tempOpenSaleSkus, null, "以报价单ID搜索过滤商品遇到错误");
				openSaleSkus
						.addAll(tempOpenSaleSkus.stream().filter(s -> s.getState() == 1).collect(Collectors.toList()));
			}

			// 创建订单的商品集合
			List<OrderProductParam> c_products = new ArrayList<>();

			OrderProductParam product = null;
			for (OpenSaleSkuBean openSaleSku : openSaleSkus) {
				product = new OrderProductParam();
				product.setSku_id(openSaleSku.getId());
				product.setCount("10");
				product.setIs_price_timing("0");
				product.setPrice("5");
				if (c_products.size() <= 6) {
					c_products.add(product);
				} else {
					break;
				}

			}

			Assert.assertEquals(c_products.size() >= 3, true, "下单商品个数不足三个,无法完成订单批量删除商品");

			OpenReceiveTimeBean openReceiveTime = openCategoryService.getReceiveTime(time_config_id);
			Assert.assertNotEquals(openReceiveTime, null, "获取运营时间的收货时间信息失败");

			String receive_time_day = openReceiveTime.getReceive_time().keySet().iterator().next();
			List<String> receive_time_list = openReceiveTime.getReceive_time().get(receive_time_day);

			String receive_time_start = receive_time_list.get(0);
			String receive_time_end = receive_time_list.get(1);

			OrderCreateParam orderCreateParam = new OrderCreateParam();
			orderCreateParam.setProducts(c_products);
			orderCreateParam.setReceive_begin_time(receive_time_start);
			orderCreateParam.setReceive_end_time(receive_time_end);
			orderCreateParam.setCustomer_id(openCustomer.getCustomer_id());
			orderCreateParam.setTime_config_id(time_config_id);

			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertNotEquals(order_id, null, "通过开放平台下单失败");

			List<String> sku_ids = new ArrayList<>();
			sku_ids.add(c_products.get(0).getSku_id());
			sku_ids.add(c_products.get(1).getSku_id());

			boolean reuslt = openOrderService.deleteOrderSkus(order_id, sku_ids);
			Assert.assertEquals(reuslt, true, "修改订单,删除订单中的商品失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + "详细信息失败");

			for (String sku_id : sku_ids) {
				OpenOrderDetailBean.Detail detail = openOrderDetail.getDetails().stream()
						.filter(d -> d.getSku_id().equals(sku_id)).findAny().orElse(null);
				Assert.assertEquals(detail, null, "修改订单,删除订单中的商品,实际订单中" + order_id + "的商品" + sku_id + "没有删除");
			}
		} catch (Exception e) {
			logger.error("商户下单-修改订单过程中遇到错误: ", e);
			Assert.fail("商户下单-修改订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateTestCase07() {
		ReporterCSS.title("测试点: 修改订单,修改订单商品");
		// 取第一个运营时间的所有的报价单
		String time_config_id = openSalemenuGroup.keySet().iterator().next();
		List<OpenSalemenuBean> openSalemenus = openSalemenuGroup.get(time_config_id);
		List<OpenSaleSkuBean> openSaleSkus = new ArrayList<>();

		OpenSaleSkuFilterParam filterParam = new OpenSaleSkuFilterParam();
		try {
			for (OpenSalemenuBean openSalemenu : openSalemenus) {
				filterParam.setSalemenu_id(openSalemenu.getId());
				List<OpenSaleSkuBean> tempOpenSaleSkus = openCategoryService.seachSaleSku(filterParam);
				Assert.assertNotEquals(tempOpenSaleSkus, null, "以报价单ID搜索过滤商品遇到错误");
				openSaleSkus
						.addAll(tempOpenSaleSkus.stream().filter(s -> s.getState() == 1).collect(Collectors.toList()));
			}

			// 创建订单的商品集合
			List<OrderProductParam> c_products = new ArrayList<>();

			List<OrderProductParam> u_products = new ArrayList<>();
			OrderProductParam product = null;
			for (OpenSaleSkuBean openSaleSku : openSaleSkus) {
				product = new OrderProductParam();
				product.setSku_id(openSaleSku.getId());
				product.setCount("10");
				product.setIs_price_timing("0");
				product.setPrice("5");
				if (c_products.size() <= 6) {
					c_products.add(product);
					product.setCount("8");
					product.setPrice("6");
					product.setRemark(StringUtil.getRandomString(6));
					u_products.add(product);
				} else {
					break;
				}

			}

			Assert.assertEquals(c_products.size() >= 3, true, "下单商品个数不足三个,无法完成订单批量删除商品");

			OpenReceiveTimeBean openReceiveTime = openCategoryService.getReceiveTime(time_config_id);
			Assert.assertNotEquals(openReceiveTime, null, "获取运营时间的收货时间信息失败");

			String receive_time_day = openReceiveTime.getReceive_time().keySet().iterator().next();
			List<String> receive_time_list = openReceiveTime.getReceive_time().get(receive_time_day);

			String receive_time_start = receive_time_list.get(0);
			String receive_time_end = receive_time_list.get(1);

			OrderCreateParam orderCreateParam = new OrderCreateParam();
			orderCreateParam.setProducts(c_products);
			orderCreateParam.setReceive_begin_time(receive_time_start);
			orderCreateParam.setReceive_end_time(receive_time_end);
			orderCreateParam.setCustomer_id(openCustomer.getCustomer_id());
			orderCreateParam.setTime_config_id(time_config_id);

			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertNotEquals(order_id, null, "通过开放平台下单失败");

			boolean result = openOrderService.updateOrderSkus(order_id, u_products);
			Assert.assertEquals(result, true, "修改订单,修改订单商品失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + "详细信息失败");

			String msg = null;
			for (OrderProductParam orderProduct : u_products) {
				String sku_id = orderProduct.getSku_id();
				OpenOrderDetailBean.Detail detail = openOrderDetail.getDetails().stream()
						.filter(d -> d.getSku_id().equals(sku_id)).findAny().orElse(null);
				if (detail.getQuantity().compareTo(new BigDecimal(orderProduct.getCount())) != 0) {
					msg = String.format("订单%s里的商品%s,下单数和预期的不一致,预期%s,实际%s", order_id, sku_id, orderProduct.getCount(),
							detail.getQuantity());
					Reporter.log(msg);
					logger.warn(msg);
					result = false;
				}

				if (detail.getSale_price().compareTo(new BigDecimal(orderProduct.getPrice())) != 0) {
					msg = String.format("订单%s里的商品%s,下单价格和预期的不一致,预期%s,实际%s", order_id, sku_id, orderProduct.getPrice(),
							detail.getSale_price());
					Reporter.log(msg);
					logger.warn(msg);
					result = false;
				}

				if (!detail.getSpu_remark().equals(orderProduct.getRemark())) {
					msg = String.format("订单%s里的商品%s,下单备注和预期的不一致,预期%s,实际%s", order_id, sku_id, orderProduct.getRemark(),
							detail.getSpu_remark());
					Reporter.log(msg);
					logger.warn(msg);
					result = false;
				}
			}

			Assert.assertEquals(result, true, "订单修改商品,修改结果和预期不一致");
		} catch (Exception e) {
			logger.error("商户下单-修改订单过程中遇到错误: ", e);
			Assert.fail("商户下单-修改订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateTestCase08() {
		ReporterCSS.title("测试点: 修改订单,修改订单商品,不修改下单商品数量,修改价格");
		// 取第一个运营时间的所有的报价单
		String time_config_id = openSalemenuGroup.keySet().iterator().next();
		List<OpenSalemenuBean> openSalemenus = openSalemenuGroup.get(time_config_id);
		List<OpenSaleSkuBean> openSaleSkus = new ArrayList<>();

		OpenSaleSkuFilterParam filterParam = new OpenSaleSkuFilterParam();
		try {
			for (OpenSalemenuBean openSalemenu : openSalemenus) {
				filterParam.setSalemenu_id(openSalemenu.getId());
				List<OpenSaleSkuBean> tempOpenSaleSkus = openCategoryService.seachSaleSku(filterParam);
				Assert.assertNotEquals(tempOpenSaleSkus, null, "以报价单ID搜索过滤商品遇到错误");
				openSaleSkus
						.addAll(tempOpenSaleSkus.stream().filter(s -> s.getState() == 1).collect(Collectors.toList()));
			}

			// 创建订单的商品集合
			List<OrderProductParam> c_products = new ArrayList<>();

			List<OrderProductParam> u_products = new ArrayList<>();
			OrderProductParam product = null;
			for (OpenSaleSkuBean openSaleSku : openSaleSkus) {
				product = new OrderProductParam();
				product.setSku_id(openSaleSku.getId());
				product.setCount("10");
				product.setIs_price_timing("0");
				product.setPrice("5");
				if (c_products.size() <= 6) {
					c_products.add(product);
					product.setCount("8");
					product.setRemark(StringUtil.getRandomString(6));
					u_products.add(product);
				} else {
					break;
				}

			}

			Assert.assertEquals(c_products.size() >= 3, true, "下单商品个数不足三个,无法完成订单批量删除商品");

			OpenReceiveTimeBean openReceiveTime = openCategoryService.getReceiveTime(time_config_id);
			Assert.assertNotEquals(openReceiveTime, null, "获取运营时间的收货时间信息失败");

			String receive_time_day = openReceiveTime.getReceive_time().keySet().iterator().next();
			List<String> receive_time_list = openReceiveTime.getReceive_time().get(receive_time_day);

			String receive_time_start = receive_time_list.get(0);
			String receive_time_end = receive_time_list.get(1);

			OrderCreateParam orderCreateParam = new OrderCreateParam();
			orderCreateParam.setProducts(c_products);
			orderCreateParam.setReceive_begin_time(receive_time_start);
			orderCreateParam.setReceive_end_time(receive_time_end);
			orderCreateParam.setCustomer_id(openCustomer.getCustomer_id());
			orderCreateParam.setTime_config_id(time_config_id);

			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertNotEquals(order_id, null, "通过开放平台下单失败");

			boolean result = openOrderService.updateOrderSkus(order_id, u_products);
			Assert.assertEquals(result, true, "修改订单,修改订单商品失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + "详细信息失败");

			String msg = null;
			for (OrderProductParam orderProduct : u_products) {
				String sku_id = orderProduct.getSku_id();
				OpenOrderDetailBean.Detail detail = openOrderDetail.getDetails().stream()
						.filter(d -> d.getSku_id().equals(sku_id)).findAny().orElse(null);

				if (detail.getSale_price().compareTo(new BigDecimal(orderProduct.getPrice())) != 0) {
					msg = String.format("订单%s里的商品%s,下单价格和预期的不一致,预期%s,实际%s", order_id, sku_id, orderProduct.getPrice(),
							detail.getSale_price());
					Reporter.log(msg);
					logger.warn(msg);
					result = false;
				}

				if (!detail.getSpu_remark().equals(orderProduct.getRemark())) {
					msg = String.format("订单%s里的商品%s,下单备注和预期的不一致,预期%s,实际%s", order_id, sku_id, orderProduct.getRemark(),
							detail.getSpu_remark());
					Reporter.log(msg);
					logger.warn(msg);
					result = false;
				}
			}

			Assert.assertEquals(result, true, "订单修改商品,修改结果和预期不一致");
		} catch (Exception e) {
			logger.error("商户下单-修改订单过程中遇到错误: ", e);
			Assert.fail("商户下单-修改订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateTestCase09() {
		ReporterCSS.title("测试点: 修改订单,修改订单商品,不修改商品价格,只修改商品数量");
		// 取第一个运营时间的所有的报价单
		String time_config_id = openSalemenuGroup.keySet().iterator().next();
		List<OpenSalemenuBean> openSalemenus = openSalemenuGroup.get(time_config_id);
		List<OpenSaleSkuBean> openSaleSkus = new ArrayList<>();

		OpenSaleSkuFilterParam filterParam = new OpenSaleSkuFilterParam();
		try {
			for (OpenSalemenuBean openSalemenu : openSalemenus) {
				filterParam.setSalemenu_id(openSalemenu.getId());
				List<OpenSaleSkuBean> tempOpenSaleSkus = openCategoryService.seachSaleSku(filterParam);
				Assert.assertNotEquals(tempOpenSaleSkus, null, "以报价单ID搜索过滤商品遇到错误");
				openSaleSkus
						.addAll(tempOpenSaleSkus.stream().filter(s -> s.getState() == 1).collect(Collectors.toList()));
			}

			// 创建订单的商品集合
			List<OrderProductParam> c_products = new ArrayList<>();

			List<OrderProductParam> u_products = new ArrayList<>();
			OrderProductParam product = null;
			for (OpenSaleSkuBean openSaleSku : openSaleSkus) {
				product = new OrderProductParam();
				product.setSku_id(openSaleSku.getId());
				product.setCount("10");
				product.setIs_price_timing("0");
				product.setPrice("5");
				if (c_products.size() <= 6) {
					c_products.add(product);
					product.setCount("8");
					product.setRemark(StringUtil.getRandomString(6));
					u_products.add(product);
				} else {
					break;
				}

			}

			Assert.assertEquals(c_products.size() >= 3, true, "下单商品个数不足三个,无法完成订单批量删除商品");

			OpenReceiveTimeBean openReceiveTime = openCategoryService.getReceiveTime(time_config_id);
			Assert.assertNotEquals(openReceiveTime, null, "获取运营时间的收货时间信息失败");

			String receive_time_day = openReceiveTime.getReceive_time().keySet().iterator().next();
			List<String> receive_time_list = openReceiveTime.getReceive_time().get(receive_time_day);

			String receive_time_start = receive_time_list.get(0);
			String receive_time_end = receive_time_list.get(1);

			OrderCreateParam orderCreateParam = new OrderCreateParam();
			orderCreateParam.setProducts(c_products);
			orderCreateParam.setReceive_begin_time(receive_time_start);
			orderCreateParam.setReceive_end_time(receive_time_end);
			orderCreateParam.setCustomer_id(openCustomer.getCustomer_id());
			orderCreateParam.setTime_config_id(time_config_id);

			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertNotEquals(order_id, null, "通过开放平台下单失败");

			boolean result = openOrderService.updateOrderSkus(order_id, u_products);
			Assert.assertEquals(result, true, "修改订单,修改订单商品失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + "详细信息失败");

			String msg = null;
			for (OrderProductParam orderProduct : u_products) {
				String sku_id = orderProduct.getSku_id();
				OpenOrderDetailBean.Detail detail = openOrderDetail.getDetails().stream()
						.filter(d -> d.getSku_id().equals(sku_id)).findAny().orElse(null);
				if (detail.getQuantity().compareTo(new BigDecimal(orderProduct.getCount())) != 0) {
					msg = String.format("订单%s里的商品%s,下单数和预期的不一致,预期%s,实际%s", order_id, sku_id, orderProduct.getCount(),
							detail.getQuantity());
					Reporter.log(msg);
					logger.warn(msg);
					result = false;
				}

				if (!detail.getSpu_remark().equals(orderProduct.getRemark())) {
					msg = String.format("订单%s里的商品%s,下单备注和预期的不一致,预期%s,实际%s", order_id, sku_id, orderProduct.getRemark(),
							detail.getSpu_remark());
					Reporter.log(msg);
					logger.warn(msg);
					result = false;
				}
			}

			Assert.assertEquals(result, true, "订单修改商品,修改结果和预期不一致");
		} catch (Exception e) {
			logger.error("商户下单-修改订单过程中遇到错误: ", e);
			Assert.fail("商户下单-修改订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateTestCase10() {
		ReporterCSS.title("测试点: 修改订单,修改订单的出库数");
		// 取第一个运营时间的所有的报价单
		String time_config_id = openSalemenuGroup.keySet().iterator().next();
		List<OpenSalemenuBean> openSalemenus = openSalemenuGroup.get(time_config_id);
		List<OpenSaleSkuBean> openSaleSkus = new ArrayList<>();

		OpenSaleSkuFilterParam filterParam = new OpenSaleSkuFilterParam();
		try {
			for (OpenSalemenuBean openSalemenu : openSalemenus) {
				filterParam.setSalemenu_id(openSalemenu.getId());
				List<OpenSaleSkuBean> tempOpenSaleSkus = openCategoryService.seachSaleSku(filterParam);
				Assert.assertNotEquals(tempOpenSaleSkus, null, "以报价单ID搜索过滤商品遇到错误");
				openSaleSkus
						.addAll(tempOpenSaleSkus.stream().filter(s -> s.getState() == 1).collect(Collectors.toList()));
			}

			// 创建订单的商品集合
			List<OrderProductParam> c_products = new ArrayList<>();

			OrderProductParam product = null;
			for (OpenSaleSkuBean openSaleSku : openSaleSkus) {
				product = new OrderProductParam();
				product.setSku_id(openSaleSku.getId());
				product.setCount("10");
				product.setIs_price_timing("0");
				product.setPrice("5");
				if (c_products.size() <= 6) {
					c_products.add(product);
				} else {
					break;
				}

			}

			OpenReceiveTimeBean openReceiveTime = openCategoryService.getReceiveTime(time_config_id);
			Assert.assertNotEquals(openReceiveTime, null, "获取运营时间的收货时间信息失败");

			String receive_time_day = openReceiveTime.getReceive_time().keySet().iterator().next();
			List<String> receive_time_list = openReceiveTime.getReceive_time().get(receive_time_day);
			String receive_time_start = receive_time_list.get(0);
			String receive_time_end = receive_time_list.get(1);

			OrderCreateParam orderCreateParam = new OrderCreateParam();
			orderCreateParam.setProducts(c_products);
			orderCreateParam.setReceive_begin_time(receive_time_start);
			orderCreateParam.setReceive_end_time(receive_time_end);
			orderCreateParam.setCustomer_id(openCustomer.getCustomer_id());
			orderCreateParam.setTime_config_id(time_config_id);

			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertNotEquals(order_id, null, "通过开放平台下单失败");

			OrderUpdateParam orderUpdateParam = new OrderUpdateParam();
			orderUpdateParam.setOrder_id(order_id);
			orderUpdateParam.setStatus(5);

			boolean result = openOrderService.updateOrder(orderUpdateParam);
			Assert.assertEquals(result, true, "修改订单,修改订单状态值失败");

			OrderProductParam orderProductParam = NumberUtil.roundNumberInList(c_products);
			String count = orderProductParam.getCount();
			String sku_id = orderProductParam.getSku_id();

			String real_count = new BigDecimal(count).add(new BigDecimal("1")).toString();
			orderProductParam.setReal_count(real_count);

			List<OrderProductParam> u_products = Arrays.asList(orderProductParam);
			result = openOrderService.updateOrderSkus(order_id, u_products);
			Assert.assertEquals(result, true, "修改订单" + order_id + "出库数失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + "详细信息失败");

			List<OpenOrderDetailBean.Detail> details = openOrderDetail.getDetails();
			OpenOrderDetailBean.Detail detail = details.stream().filter(s -> s.getSku_id().equals(sku_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(detail, null, "订单" + order_id + "里的商品" + sku_id + "没有找到");

			Assert.assertEquals(detail.getReal_quantity().toString(), real_count,
					"修改订单" + order_id + ",修改商品" + sku_id + "的出库单,出库数与预期不符");

		} catch (Exception e) {
			logger.error("商户下单-修改订单过程中遇到错误: ", e);
			Assert.fail("商户下单-修改订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateTestCase11() {
		ReporterCSS.title("测试点: 修改订单,修改订单的出库数,出库数设置为缺货");
		// 取第一个运营时间的所有的报价单
		String time_config_id = openSalemenuGroup.keySet().iterator().next();
		List<OpenSalemenuBean> openSalemenus = openSalemenuGroup.get(time_config_id);
		List<OpenSaleSkuBean> openSaleSkus = new ArrayList<>();

		OpenSaleSkuFilterParam filterParam = new OpenSaleSkuFilterParam();
		try {
			for (OpenSalemenuBean openSalemenu : openSalemenus) {
				filterParam.setSalemenu_id(openSalemenu.getId());
				List<OpenSaleSkuBean> tempOpenSaleSkus = openCategoryService.seachSaleSku(filterParam);
				Assert.assertNotEquals(tempOpenSaleSkus, null, "以报价单ID搜索过滤商品遇到错误");
				openSaleSkus
						.addAll(tempOpenSaleSkus.stream().filter(s -> s.getState() == 1).collect(Collectors.toList()));
			}

			// 创建订单的商品集合
			List<OrderProductParam> c_products = new ArrayList<>();

			OrderProductParam product = null;
			for (OpenSaleSkuBean openSaleSku : openSaleSkus) {
				product = new OrderProductParam();
				product.setSku_id(openSaleSku.getId());
				product.setCount("10");
				product.setIs_price_timing("0");
				product.setPrice("5");
				if (c_products.size() <= 6) {
					c_products.add(product);
				} else {
					break;
				}

			}

			OpenReceiveTimeBean openReceiveTime = openCategoryService.getReceiveTime(time_config_id);
			Assert.assertNotEquals(openReceiveTime, null, "获取运营时间的收货时间信息失败");

			String receive_time_day = openReceiveTime.getReceive_time().keySet().iterator().next();
			List<String> receive_time_list = openReceiveTime.getReceive_time().get(receive_time_day);
			String receive_time_start = receive_time_list.get(0);
			String receive_time_end = receive_time_list.get(1);

			OrderCreateParam orderCreateParam = new OrderCreateParam();
			orderCreateParam.setProducts(c_products);
			orderCreateParam.setReceive_begin_time(receive_time_start);
			orderCreateParam.setReceive_end_time(receive_time_end);
			orderCreateParam.setCustomer_id(openCustomer.getCustomer_id());
			orderCreateParam.setTime_config_id(time_config_id);

			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertNotEquals(order_id, null, "通过开放平台下单失败");

			OrderUpdateParam orderUpdateParam = new OrderUpdateParam();
			orderUpdateParam.setOrder_id(order_id);
			orderUpdateParam.setStatus(5);

			boolean result = openOrderService.updateOrder(orderUpdateParam);
			Assert.assertEquals(result, true, "修改订单,修改订单状态值失败");

			OrderProductParam orderProductParam = NumberUtil.roundNumberInList(c_products);
			String sku_id = orderProductParam.getSku_id();
			String real_count = "0";
			orderProductParam.setReal_count(real_count);

			List<OrderProductParam> u_products = Arrays.asList(orderProductParam);

			result = openOrderService.updateOrderSkus(order_id, u_products);
			Assert.assertEquals(result, true, "修改订单" + order_id + "出库数失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + "详细信息失败");

			List<OpenOrderDetailBean.Detail> details = openOrderDetail.getDetails();
			OpenOrderDetailBean.Detail detail = details.stream().filter(s -> s.getSku_id().equals(sku_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(detail, null, "订单" + order_id + "里的商品" + sku_id + "没有找到");

			Assert.assertEquals(detail.getReal_quantity().toString(), real_count,
					"修改订单" + order_id + ",修改商品" + sku_id + "的出库单,出库数与预期不符");

			Assert.assertEquals(detail.isOut_of_stock(), true, "订单" + order_id + "里的商品" + sku_id + "没有标记成缺货");

		} catch (Exception e) {
			logger.error("商户下单-修改订单过程中遇到错误: ", e);
			Assert.fail("商户下单-修改订单过程中遇到错误: ", e);
		}
	}

	@Test
	public void orderUpdateTestCase12() {
		ReporterCSS.title("测试点: 修改订单,修改订单的出库数(sale_retio非1)");
		// 取第一个运营时间的所有的报价单
		String time_config_id = openSalemenuGroup.keySet().iterator().next();
		List<OpenSalemenuBean> openSalemenus = openSalemenuGroup.get(time_config_id);
		List<OpenSaleSkuBean> openSaleSkus = new ArrayList<>();

		OpenSaleSkuFilterParam filterParam = new OpenSaleSkuFilterParam();
		try {
			for (OpenSalemenuBean openSalemenu : openSalemenus) {
				filterParam.setSalemenu_id(openSalemenu.getId());
				List<OpenSaleSkuBean> tempOpenSaleSkus = openCategoryService.seachSaleSku(filterParam);
				Assert.assertNotEquals(tempOpenSaleSkus, null, "以报价单ID搜索过滤商品遇到错误");

				for (OpenSaleSkuBean openSaleSku : tempOpenSaleSkus) {
					if (openSaleSku.getState() == 1
							&& openSaleSku.getSale_ratio().compareTo(new BigDecimal("1")) == 1) {
						openSaleSkus.add(openSaleSku);
					}
				}
			}

			Assert.assertEquals(openSaleSkus.size() > 0, true, "站点没有基本单位和销售单位不一致的商品");

			// 创建订单的商品集合
			List<OrderProductParam> c_products = new ArrayList<>();

			OrderProductParam product = null;
			for (OpenSaleSkuBean openSaleSku : openSaleSkus) {
				product = new OrderProductParam();
				product.setSku_id(openSaleSku.getId());
				product.setCount("10");
				product.setIs_price_timing("0");
				product.setPrice("5");
				if (c_products.size() <= 6) {
					c_products.add(product);
				} else {
					break;
				}

			}

			OpenReceiveTimeBean openReceiveTime = openCategoryService.getReceiveTime(time_config_id);
			Assert.assertNotEquals(openReceiveTime, null, "获取运营时间的收货时间信息失败");

			String receive_time_day = openReceiveTime.getReceive_time().keySet().iterator().next();
			List<String> receive_time_list = openReceiveTime.getReceive_time().get(receive_time_day);
			String receive_time_start = receive_time_list.get(0);
			String receive_time_end = receive_time_list.get(1);

			OrderCreateParam orderCreateParam = new OrderCreateParam();
			orderCreateParam.setProducts(c_products);
			orderCreateParam.setReceive_begin_time(receive_time_start);
			orderCreateParam.setReceive_end_time(receive_time_end);
			orderCreateParam.setCustomer_id(openCustomer.getCustomer_id());
			orderCreateParam.setTime_config_id(time_config_id);

			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertNotEquals(order_id, null, "通过开放平台下单失败");

			OrderUpdateParam orderUpdateParam = new OrderUpdateParam();
			orderUpdateParam.setOrder_id(order_id);
			orderUpdateParam.setStatus(5);

			boolean result = openOrderService.updateOrder(orderUpdateParam);
			Assert.assertEquals(result, true, "修改订单,修改订单状态值失败");

			OrderProductParam orderProductParam = NumberUtil.roundNumberInList(c_products);
			String sku_id = orderProductParam.getSku_id();
			String real_count = new BigDecimal(orderProductParam.getCount()).multiply(new BigDecimal("2")).toString();
			orderProductParam.setReal_count(real_count);

			List<OrderProductParam> u_products = Arrays.asList(orderProductParam);

			result = openOrderService.updateOrderSkus(order_id, u_products);
			Assert.assertEquals(result, true, "修改订单" + order_id + "出库数失败");

			OpenOrderDetailBean openOrderDetail = openOrderService.getOrderDetail(order_id);
			Assert.assertNotEquals(openOrderDetail, null, "获取订单 " + order_id + "详细信息失败");

			List<OpenOrderDetailBean.Detail> details = openOrderDetail.getDetails();
			OpenOrderDetailBean.Detail detail = details.stream().filter(s -> s.getSku_id().equals(sku_id)).findAny()
					.orElse(null);

			Assert.assertNotEquals(detail, null, "订单" + order_id + "里的商品" + sku_id + "没有找到");

			Assert.assertEquals(detail.getReal_quantity().toString(), real_count,
					"修改订单" + order_id + ",修改商品" + sku_id + "的出库单,出库数与预期不符");

		} catch (Exception e) {
			logger.error("商户下单-修改订单过程中遇到错误: ", e);
			Assert.fail("商户下单-修改订单过程中遇到错误: ", e);
		}
	}

}
