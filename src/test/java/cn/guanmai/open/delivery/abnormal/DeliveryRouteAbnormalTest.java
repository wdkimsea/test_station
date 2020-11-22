package cn.guanmai.open.delivery.abnormal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.customer.OpenCustomerBean;
import cn.guanmai.open.bean.delivery.OpenRouteBean;
import cn.guanmai.open.delivery.DeliveryRouteTest;
import cn.guanmai.open.impl.customer.OpenCustomerServiceImpl;
import cn.guanmai.open.impl.delivery.OpenDeliveryServiceImpl;
import cn.guanmai.open.interfaces.customer.OpenCustomerService;
import cn.guanmai.open.interfaces.delivery.OpenDeliveryService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Jun 14, 2019 11:10:23 AM 
* @des 开放平台, 配送线路异常测试
* @version 1.0 
*/
public class DeliveryRouteAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(DeliveryRouteTest.class);
	private OpenDeliveryService deliveryService;
	private OpenCustomerService openCustomerService;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		deliveryService = new OpenDeliveryServiceImpl(access_token);
		openCustomerService = new OpenCustomerServiceImpl(access_token);
	}

	@Test
	public void deliveryRouteAbnormalTestCase01() {
		try {
			Reporter.log("测试点: 异常新建配送线路,配送路线名称输入为空,断言失败");

			String route_name = "";
			List<OpenCustomerBean> openCustomerList = openCustomerService.searchCustomer(null, null, 0, 20);
			Assert.assertNotEquals(openCustomerList, null, "获取商户列表失败");
			List<String> customer_ids = openCustomerList.stream().map(c -> c.getCustomer_id())
					.collect(Collectors.toList());

			boolean result = deliveryService.createRoute(route_name, customer_ids);
			Assert.assertEquals(result, false, "异常新建配送线路,配送路线名称输入为空,断言失败");

		} catch (Exception e) {
			logger.error("新建配送线路遇到错误: ", e);
			Assert.fail("新建配送线路遇到错误: ", e);
		}
	}

//	@Test
	public void deliveryRouteAbnormalTestCase02() {
		try {
			Reporter.log("测试点: 异常新建配送线路,绑定的商户ID为错误的商户ID,断言失败");

			String route_name = StringUtil.getRandomString(6).toUpperCase();

			List<String> customer_ids = Arrays.asList("S110");

			boolean result = deliveryService.createRoute(route_name, customer_ids);
			Assert.assertEquals(result, false, "异常新建配送线路,绑定的商户ID为错误的商户ID,断言失败");

		} catch (Exception e) {
			logger.error("新建配送线路遇到错误: ", e);
			Assert.fail("新建配送线路遇到错误: ", e);
		}
	}

//	@Test
//	public void deliveryRouteAbnormalTestCase03() {
//		try {
//			Reporter.log("测试点: 异常新建配送线路,绑定的商户ID传入别的站点的商户ID,断言失败");
//
//			String route_name = StringUtil.getRandomString(6).toUpperCase();
//
//			List<String> customer_ids = Arrays.asList("S027729");
//
//			boolean result = deliveryService.createRoute(route_name, customer_ids);
//			Assert.assertEquals(result, false, "异常新建配送线路,绑定的商户ID传入别的站点的商户ID,断言失败");
//
//		} catch (Exception e) {
//			logger.error("新建配送线路遇到错误: ", e);
//			Assert.fail("新建配送线路遇到错误: ", e);
//		}
//	}

	@Test
	public void deliveryRouteAbnormalTestCase04() {
		try {
			Reporter.log("测试点: 异常修改配送线路,传入空的配送线路ID,断言失败");
			String route_name = StringUtil.getRandomString(6).toUpperCase();
			boolean result = deliveryService.updateRoute("", route_name, new ArrayList<>());
			Assert.assertEquals(result, false, "异常修改配送线路,传入空的配送线路ID,断言失败");

		} catch (Exception e) {
			logger.error("修改配送线路遇到错误: ", e);
			Assert.fail("修改配送线路遇到错误: ", e);
		}
	}

	@Test
	public void deliveryRouteAbnormalTestCase05() {
		OpenRouteBean openRoute = null;
		try {
			Reporter.log("测试点: 异常修改配送线路,传入错误的商户ID,断言失败");
			String route_name = StringUtil.getRandomString(6).toUpperCase();
			boolean result = deliveryService.createRoute(route_name, new ArrayList<>());
			Assert.assertEquals(result, true, "新建线路失败");

			List<OpenRouteBean> openRouteList = deliveryService.searchRoute(route_name, "0", "20");
			Assert.assertNotEquals(openRouteList, null, "搜索配送线路失败");

			openRoute = openRouteList.stream().filter(r -> r.getRoute_name().equals(route_name)).findAny().orElse(null);
			Assert.assertNotEquals(openRoute, null, "新建的配送线路在配送线路列表中没有搜索到");

			String route_id = openRoute.getRoute_id();

			List<String> customer_ids = Arrays.asList("S110");

			result = deliveryService.updateRoute(route_id, route_name, customer_ids);
			Assert.assertEquals(result, false, "异常修改配送线路,传入错误的商户ID,断言失败");

		} catch (Exception e) {
			logger.error("过滤搜索配送线路遇到错误: ", e);
			Assert.fail("过滤搜索配送线路遇到错误: ", e);
		} finally {
			if (openRoute != null) {
				try {
					String route_id = openRoute.getRoute_id();
					boolean result = deliveryService.deleteRoute(route_id);
					Assert.assertEquals(result, true, "删除配送线路失败");
				} catch (Exception e) {
					logger.error("删除配送线路遇到错误: ", e);
					Assert.fail("删除配送线路遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void deliveryRouteAbnormalTestCase06() {
		OpenRouteBean openRoute = null;
		try {
			Reporter.log("测试点: 异常修改配送线路,传入错误的商户ID,断言失败");
			String route_name = StringUtil.getRandomString(6).toUpperCase();
			boolean result = deliveryService.createRoute(route_name, new ArrayList<>());
			Assert.assertEquals(result, true, "新建线路失败");

			List<OpenRouteBean> openRouteList = deliveryService.searchRoute(route_name, "0", "20");
			Assert.assertNotEquals(openRouteList, null, "搜索配送线路失败");

			openRoute = openRouteList.stream().filter(r -> r.getRoute_name().equals(route_name)).findAny().orElse(null);
			Assert.assertNotEquals(openRoute, null, "新建的配送线路在配送线路列表中没有搜索到");

			String route_id = openRoute.getRoute_id();

			List<String> customer_ids = Arrays.asList("S027729");

			result = deliveryService.updateRoute(route_id, route_name, customer_ids);
			Assert.assertEquals(result, false, "异常修改配送线路,绑定的商户ID传入别的站点的商户ID,断言失败");

		} catch (Exception e) {
			logger.error("过滤搜索配送线路遇到错误: ", e);
			Assert.fail("过滤搜索配送线路遇到错误: ", e);
		} finally {
			if (openRoute != null) {
				try {
					String route_id = openRoute.getRoute_id();
					boolean result = deliveryService.deleteRoute(route_id);
					Assert.assertEquals(result, true, "删除配送线路失败");
				} catch (Exception e) {
					logger.error("删除配送线路遇到错误: ", e);
					Assert.fail("删除配送线路遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void deliveryRouteAbnormalTestCase07() {
		try {
			Reporter.log("测试点: 异常删除配送线路,传入别的站点的配送线路ID,断言失败");
			String route_id = "110";
			boolean result = deliveryService.deleteRoute(route_id);
			Assert.assertEquals(result, false, "异常删除配送线路,传入别的站点的配送线路ID,断言失败");
		} catch (Exception e) {
			logger.error("删除配送线路遇到错误: ", e);
			Assert.fail("删除配送线路遇到错误: ", e);
		}
	}

}
