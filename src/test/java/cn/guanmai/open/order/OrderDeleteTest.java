package cn.guanmai.open.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.customer.OpenCustomerBean;
import cn.guanmai.open.bean.order.OpenOrderBean;
import cn.guanmai.open.bean.order.param.OrderCreateParam;
import cn.guanmai.open.bean.order.param.OrderProductParam;
import cn.guanmai.open.bean.order.param.OrderSearchParam;
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
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Jun 11, 2019 3:48:12 PM 
* @des 开放平台删除订单测试
* @version 1.0 
*/
public class OrderDeleteTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OrderCreateTest.class);
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

			openCustomer = NumberUtil.roundNumberInList(order_customer_list);

			openSalemenus = openSalemenuService.searchSalemenu(openCustomer.getCustomer_id(), 1);
			Assert.assertNotNull(openSalemenus, "根据商户查询报价单集合失败");

			openSalemenuGroup = openSalemenus.stream()
					.collect(Collectors.groupingBy(OpenSalemenuBean::getTime_config_id));
		} catch (Exception e) {
			logger.error("搜索过滤下单商户遇到错误: ", e);
			Assert.fail("搜索过滤下单商户遇到错误: ", e);
		}
	}

	@Test
	public void orderDeleteTestCase01() {
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

			OrderCreateParam orderCreateParam = new OrderCreateParam();

			List<OrderProductParam> products = new ArrayList<>();

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

			orderCreateParam.setProducts(products);
			orderCreateParam.setReceive_begin_time(receive_time_start);
			orderCreateParam.setReceive_end_time(receive_time_end);
			orderCreateParam.setCustomer_id(openCustomer.getCustomer_id());
			orderCreateParam.setTime_config_id(time_config_id);

			String order_id = openOrderService.createOrder(orderCreateParam);
			Assert.assertNotEquals(order_id, null, "通过开放平台下单失败");

			boolean result = openOrderService.deleteOrder(order_id);
			Assert.assertEquals(result, true, "删除订单失败");

			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OrderSearchParam orderSearchParam = new OrderSearchParam();
			orderSearchParam.setQuery_type("1");
			orderSearchParam.setStart_date(todayStr);
			orderSearchParam.setEnd_date(todayStr);
			List<OpenOrderBean> openOrders = openOrderService.searchOrder(orderSearchParam);
			Assert.assertNotNull(openOrders, "搜索过滤订单失败");

			OpenOrderBean openOrder = openOrders.stream().filter(o -> o.getOrder_id().equals(order_id)).findAny()
					.orElse(null);
			Assert.assertEquals(openOrder, null, "删除的订单 " + order_id + " 在还在订单列表展示");

		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

}
