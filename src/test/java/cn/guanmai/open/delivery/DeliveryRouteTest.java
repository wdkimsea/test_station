package cn.guanmai.open.delivery;

import java.util.ArrayList;
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
import cn.guanmai.open.bean.delivery.OpenRouteDetailBean;
import cn.guanmai.open.impl.customer.OpenCustomerServiceImpl;
import cn.guanmai.open.impl.delivery.OpenDeliveryServiceImpl;
import cn.guanmai.open.interfaces.customer.OpenCustomerService;
import cn.guanmai.open.interfaces.delivery.OpenDeliveryService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Jun 13, 2019 5:49:16 PM 
* @des 配送线路测试
* @version 1.0 
*/
public class DeliveryRouteTest extends AccessToken {
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
	public void deliveryRouteTestCase01() {
		try {
			ReporterCSS.title("测试点: 搜索配线路");
			List<OpenRouteBean> openRouteList = deliveryService.searchRoute(null, "0", "20");
			Assert.assertNotEquals(openRouteList, null, "搜索配送线路失败");
		} catch (Exception e) {
			logger.error("过滤搜索配送线路遇到错误: ", e);
			Assert.fail("过滤搜索配送线路遇到错误: ", e);
		}
	}

	@Test
	public void deliveryRouteTestCase02() {
		OpenRouteBean openRoute = null;
		try {
			ReporterCSS.title("测试点: 新建配送线路");
			String route_name = StringUtil.getRandomString(6).toUpperCase();

			List<OpenCustomerBean> openCustomerList = openCustomerService.searchCustomer(null, null, 0, 20);
			Assert.assertNotEquals(openCustomerList, null, "获取商户列表失败");
			List<String> customer_ids = openCustomerList.stream().map(c -> c.getCustomer_id())
					.collect(Collectors.toList());

			boolean result = deliveryService.createRoute(route_name, customer_ids);
			Assert.assertEquals(result, true, "新建线路失败");

			List<OpenRouteBean> openRouteList = deliveryService.searchRoute(route_name, "0", "20");
			Assert.assertNotEquals(openRouteList, null, "搜索配送线路失败");

			openRoute = openRouteList.stream().filter(r -> r.getRoute_name().equals(route_name)).findAny().orElse(null);
			Assert.assertNotEquals(openRoute, null, "新建的配送线路在配送线路列表中没有搜索到");

			String route_id = openRoute.getRoute_id();
			OpenRouteDetailBean openRouteDetail = deliveryService.getRouteDetail(route_id);
			Assert.assertNotEquals(openRouteDetail, null, "获取配送线路详细信息失败");

			String msg = null;
			if (!openRouteDetail.getRoute_name().equals(route_name)) {
				msg = String.format("新建配送线路,输入的新的线路名称和查询到的不一致,预期:%s,实际:%s", route_name,
						openRouteDetail.getRoute_name());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			// List<String> temp_customer_ids =
			// openRouteDetail.getCustomers().stream().map(r ->
			// r.getCustomer_id())
			// .collect(Collectors.toList());
			// if (!temp_customer_ids.equals(customer_ids)) {
			// msg = String.format("新建配送线路,绑定的商户列表和和查询到的不一致,预期:%s,实际:%s",
			// customer_ids, temp_customer_ids);
			// Reporter.log(msg);
			// logger.warn(msg);
			// result = false;
			// }

			Assert.assertEquals(result, true, "新建配送线路,查询到的信息和输入的不一致");

		} catch (Exception e) {
			logger.error("新建配送线路遇到错误: ", e);
			Assert.fail("新建配送线路遇到错误: ", e);
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
	public void deliveryRouteTestCase03() {
		OpenRouteBean openRoute = null;
		try {
			ReporterCSS.title("测试点: 修改配送线路");
			String route_name = StringUtil.getRandomString(6).toUpperCase();
			boolean result = deliveryService.createRoute(route_name, new ArrayList<>());
			Assert.assertEquals(result, true, "新建线路失败");

			List<OpenRouteBean> openRouteList = deliveryService.searchRoute(route_name, "0", "20");
			Assert.assertNotEquals(openRouteList, null, "搜索配送线路失败");

			openRoute = openRouteList.stream().filter(r -> r.getRoute_name().equals(route_name)).findAny().orElse(null);
			Assert.assertNotEquals(openRoute, null, "新建的配送线路在配送线路列表中没有搜索到");

			String route_id = openRoute.getRoute_id();
			String new_route_name = StringUtil.getRandomString(6).toUpperCase();
			List<OpenCustomerBean> openCustomerList = openCustomerService.searchCustomer(null, null, 0, 20);
			Assert.assertNotEquals(openCustomerList, null, "获取商户列表失败");
			List<String> customer_ids = openCustomerList.stream().filter(c -> c.getCheck_out() == 1)
					.map(c -> c.getCustomer_id()).collect(Collectors.toList());

			result = deliveryService.updateRoute(route_id, new_route_name, customer_ids);
			Assert.assertEquals(result, true, "修改配送线路失败");

			OpenRouteDetailBean openRouteDetail = deliveryService.getRouteDetail(route_id);
			Assert.assertNotEquals(openRouteDetail, null, "获取配送线路详细信息失败");

			String msg = null;
			if (!openRouteDetail.getRoute_name().equals(new_route_name)) {
				msg = String.format("修改配送线路,输入的新的线路名称和查询到的不一致,预期:%s,实际:%s", new_route_name,
						openRouteDetail.getRoute_name());
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			List<String> temp_customer_ids = openRouteDetail.getCustomers().stream().map(r -> r.getCustomer_id())
					.collect(Collectors.toList());

			if (!(temp_customer_ids.containsAll(customer_ids) && customer_ids.containsAll(temp_customer_ids))) {
				msg = String.format("修改配送线路,绑定的商户列表和和查询到的不一致,预期:%s,实际:%s", customer_ids, temp_customer_ids);
				Reporter.log(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "修改配送线路,查询到的信息和输入的不一致");

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
	public void deliveryRouteTestCase04() {
		OpenRouteBean openRoute = null;
		try {
			ReporterCSS.title("测试点: 新建配送线路");
			String route_name = StringUtil.getRandomString(6).toUpperCase();

			boolean result = deliveryService.createRoute(route_name, new ArrayList<>());
			Assert.assertEquals(result, true, "新建线路失败");

			List<OpenRouteBean> openRouteList = deliveryService.searchRoute(route_name, "0", "20");
			Assert.assertNotEquals(openRouteList, null, "搜索配送线路失败");

			openRoute = openRouteList.stream().filter(r -> r.getRoute_name().equals(route_name)).findAny().orElse(null);
			Assert.assertNotEquals(openRoute, null, "新建的配送线路在配送线路列表中没有搜索到");

			String route_id = openRoute.getRoute_id();

			result = deliveryService.deleteRoute(route_id);
			Assert.assertEquals(result, true, "删除配送线路失败");

			openRouteList = deliveryService.searchRoute(route_name, "0", "20");
			Assert.assertNotEquals(openRouteList, null, "搜索配送线路失败");

			openRoute = openRouteList.stream().filter(r -> r.getRoute_name().equals(route_name)).findAny().orElse(null);
			Assert.assertEquals(openRoute, null, "删除的配送线路还显示在配送线路列表");

		} catch (Exception e) {
			logger.error("新建配送线路遇到错误: ", e);
			Assert.fail("新建配送线路遇到错误: ", e);
		}
	}
}
