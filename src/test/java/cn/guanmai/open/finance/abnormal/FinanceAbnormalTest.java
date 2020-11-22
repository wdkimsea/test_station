package cn.guanmai.open.finance.abnormal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.guanmai.open.bean.product.OpenReceiveTimeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.customer.OpenCustomerBean;
import cn.guanmai.open.bean.finance.param.FinanceStrikeParam;
import cn.guanmai.open.bean.order.param.OrderCreateParam;
import cn.guanmai.open.bean.order.param.OrderProductParam;
import cn.guanmai.open.bean.product.OpenSaleSkuBean;
import cn.guanmai.open.bean.product.OpenSalemenuBean;
import cn.guanmai.open.bean.product.param.OpenSaleSkuFilterParam;
import cn.guanmai.open.finance.FinanceTest;
import cn.guanmai.open.impl.customer.OpenCustomerServiceImpl;
import cn.guanmai.open.impl.finance.OpenFinanceServiceImpl;
import cn.guanmai.open.impl.order.OpenOrderServiceImpl;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.impl.product.OpenSalemenuServiceImpl;
import cn.guanmai.open.interfaces.customer.OpenCustomerService;
import cn.guanmai.open.interfaces.finance.OpenFinanceService;
import cn.guanmai.open.interfaces.order.OpenOrderService;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.interfaces.product.OpenSalemenuService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Jun 17, 2019 5:45:40 PM 
* @todo TODO
* @version 1.0 
*/
public class FinanceAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(FinanceTest.class);
	private OpenFinanceService openFinanceService;
	private FinanceStrikeParam financeStrikeParam;
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
		openFinanceService = new OpenFinanceServiceImpl(access_token);
		openCustomerService = new OpenCustomerServiceImpl(access_token);
		openOrderService = new OpenOrderServiceImpl(access_token);
		openSalemenuService = new OpenSalemenuServiceImpl(access_token);
		openCategoryService = new OpenCategoryServiceImpl(access_token);
		try {
			List<OpenSalemenuBean> openSalemenus = null;
			List<OpenCustomerBean> openCustomers = openCustomerService.searchCustomer(null, null, 0, 100);
			Assert.assertNotEquals(openCustomers, null, "搜索查询商户失败");
			boolean result = false;
			Map<String, List<OpenSalemenuBean>> openSalemenuGroup = null;
			for (OpenCustomerBean customer : openCustomers) {

				result = openCustomerService.checkCustomerOrderStatus(customer.getCustomer_id());
				if (!result) {
					continue;
				}
				openCustomer = customer;

				openSalemenus = openSalemenuService.searchSalemenu(openCustomer.getCustomer_id(), 1);
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
			logger.error("商户下单遇到错误: ", e);
			Assert.fail("商户下单遇到错误: ", e);
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
		String deal_code = StringUtil.getRandomString(12).toUpperCase();
		financeStrikeParam = new FinanceStrikeParam();
		financeStrikeParam.setArrival_method_id("cash");
		financeStrikeParam.setCustomer_id(openCustomer.getCustomer_id());
		financeStrikeParam.setDeal_code(deal_code);
		financeStrikeParam.setOrder_ids(Arrays.asList(order_id));
		financeStrikeParam.setRemark("he");
	}

	@Test
	public void financeAbnormalTestCase01() {
		Reporter.log("测试点: 冲账异常测试,商户ID输入为空,断言失败");
		try {
			financeStrikeParam.setCustomer_id("");
			boolean result = openFinanceService.strikeFinance(financeStrikeParam);
			Assert.assertEquals(result, false, "冲账异常测试,商户ID输入为空,断言失败");
		} catch (Exception e) {
			logger.error("冲账遇到错误: ", e);
			Assert.fail("冲账遇到错误: ", e);
		}
	}

	@Test
	public void financeAbnormalTestCase02() {
		Reporter.log("测试点: 冲账异常测试,商户ID输入为别的站点的商户ID,断言失败");
		try {
			financeStrikeParam.setCustomer_id("S062175");
			boolean result = openFinanceService.strikeFinance(financeStrikeParam);
			Assert.assertEquals(result, false, "冲账异常测试,商户ID输入为别的站点的商户ID,断言失败");
		} catch (Exception e) {
			logger.error("冲账遇到错误: ", e);
			Assert.fail("冲账遇到错误: ", e);
		}
	}

	@Test
	public void financeAbnormalTestCase03() {
		Reporter.log("测试点: 冲账异常测试,商户ID输入与订单不匹配的商户,断言失败");
		try {
			List<OpenCustomerBean> openCustomers = openCustomerService.searchCustomer(null, null, 0, 100);
			Assert.assertNotEquals(openCustomers, null, "搜索查询商户失败");
			for (OpenCustomerBean customer : openCustomers) {
				if (!customer.getCustomer_id().equals(openCustomer.getCustomer_id())) {
					financeStrikeParam.setCustomer_id(customer.getCustomer_id());
					break;
				}
			}
			boolean result = openFinanceService.strikeFinance(financeStrikeParam);
			Assert.assertEquals(result, false, "冲账异常测试,商户ID输入与订单不匹配的商户,断言失败");
		} catch (Exception e) {
			logger.error("冲账遇到错误: ", e);
			Assert.fail("冲账遇到错误: ", e);
		}
	}

	@Test
	public void financeAbnormalTestCase04() {
		Reporter.log("测试点: 冲账异常测试,arrival_method_id输入为空,断言失败");
		try {
			financeStrikeParam.setArrival_method_id("");
			boolean result = openFinanceService.strikeFinance(financeStrikeParam);
			Assert.assertEquals(result, false, "冲账异常测试,arrival_method_id输入为空,断言失败");
		} catch (Exception e) {
			logger.error("冲账遇到错误: ", e);
			Assert.fail("冲账遇到错误: ", e);
		}
	}

	@Test
	public void financeAbnormalTestCase05() {
		Reporter.log("测试点: 冲账异常测试,arrival_method_id输入为非候选值,断言失败");
		try {
			financeStrikeParam.setArrival_method_id("22");
			boolean result = openFinanceService.strikeFinance(financeStrikeParam);
			Assert.assertEquals(result, false, "冲账异常测试,arrival_method_id输入为非候选值,断言失败");
		} catch (Exception e) {
			logger.error("冲账遇到错误: ", e);
			Assert.fail("冲账遇到错误: ", e);
		}
	}

	@Test
	public void financeAbnormalTestCase06() {
		Reporter.log("测试点: 冲账异常测试,deal_code输入为空,断言失败");
		try {
			financeStrikeParam.setDeal_code("");
			boolean result = openFinanceService.strikeFinance(financeStrikeParam);
			Assert.assertEquals(result, false, "冲账异常测试,deal_code输入为空,断言失败");
		} catch (Exception e) {
			logger.error("冲账遇到错误: ", e);
			Assert.fail("冲账遇到错误: ", e);
		}
	}

	@Test
	public void financeAbnormalTestCase07() {
		Reporter.log("测试点: 冲账异常测试,deal_code输入为空格,断言失败");
		try {
			financeStrikeParam.setDeal_code("      ");
			boolean result = openFinanceService.strikeFinance(financeStrikeParam);
			Assert.assertEquals(result, false, "冲账异常测试,deal_code输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("冲账遇到错误: ", e);
			Assert.fail("冲账遇到错误: ", e);
		}
	}

	@Test
	public void financeAbnormalTestCase08() {
		Reporter.log("测试点: 冲账异常测试,deal_code输入过长字符(大于32个字符),断言失败");
		try {
			financeStrikeParam.setDeal_code(StringUtil.getRandomString(33));
			boolean result = openFinanceService.strikeFinance(financeStrikeParam);
			Assert.assertEquals(result, false, "冲账异常测试,deal_code输入过长字符(大于32个字符),断言失败");
		} catch (Exception e) {
			logger.error("冲账遇到错误: ", e);
			Assert.fail("冲账遇到错误: ", e);
		}
	}

	@Test
	public void financeAbnormalTestCase09() {
		Reporter.log("测试点: 冲账异常测试,order_ids不传值,断言失败");
		try {
			financeStrikeParam.setOrder_ids(null);
			boolean result = openFinanceService.strikeFinance(financeStrikeParam);
			Assert.assertEquals(result, false, "冲账异常测试,order_ids不传值,断言失败");
		} catch (Exception e) {
			logger.error("冲账遇到错误: ", e);
			Assert.fail("冲账遇到错误: ", e);
		}
	}

	@Test
	public void financeAbnormalTestCase10() {
		Reporter.log("测试点: 冲账异常测试,order_ids输入为空,断言失败");
		try {
			financeStrikeParam.setOrder_ids(new ArrayList<>());
			boolean result = openFinanceService.strikeFinance(financeStrikeParam);
			Assert.assertEquals(result, false, "冲账异常测试,order_ids输入为空,断言失败");
		} catch (Exception e) {
			logger.error("冲账遇到错误: ", e);
			Assert.fail("冲账遇到错误: ", e);
		}
	}

	@Test
	public void financeAbnormalTestCase11() {
		Reporter.log("测试点: 冲账异常测试,order_ids输入为别的站点的订单ID,断言失败");
		try {
			financeStrikeParam.setOrder_ids(Arrays.asList("PL1234424"));
			boolean result = openFinanceService.strikeFinance(financeStrikeParam);
			Assert.assertEquals(result, false, "冲账异常测试,order_ids输入为别的站点的订单ID,断言失败");
		} catch (Exception e) {
			logger.error("冲账遇到错误: ", e);
			Assert.fail("冲账遇到错误: ", e);
		}
	}

	@Test
	public void financeAbnormalTestCase12() {
		Reporter.log("测试点: 冲账异常测试,order_ids输入删除的订单ID,断言失败");
		try {
			boolean result = openOrderService.deleteOrder(order_id);
			Assert.assertEquals(result, true, "删除订单失败");
			financeStrikeParam.setOrder_ids(Arrays.asList(order_id));
			result = openFinanceService.strikeFinance(financeStrikeParam);
			Assert.assertEquals(result, false, "冲账异常测试,order_ids输入删除的订单ID,断言失败");
		} catch (Exception e) {
			logger.error("冲账遇到错误: ", e);
			Assert.fail("冲账遇到错误: ", e);
		}
	}

	@Test
	public void financeAbnormalTestCase13() {
		Reporter.log("测试点: 退款异常测试,order_id输入为空,断言失败");
		try {
			boolean result = openFinanceService.refundOrderFinance("", "1");
			Assert.assertEquals(result, false, "退款异常测试,order_id输入为空,断言失败");
		} catch (Exception e) {
			logger.error("退款遇到错误: ", e);
			Assert.fail("退款遇到错误: ", e);
		}
	}

	@Test
	public void financeAbnormalTestCase14() {
		Reporter.log("测试点: 退款异常测试,order_id输入没有支付的订单,断言失败");
		try {
			boolean result = openFinanceService.refundOrderFinance(order_id, "1");
			Assert.assertEquals(result, false, "退款异常测试,order_id输入没有支付的订单,断言失败");
		} catch (Exception e) {
			logger.error("退款遇到错误: ", e);
			Assert.fail("退款遇到错误: ", e);
		}
	}

	@Test
	public void financeAbnormalTestCase15() {
		Reporter.log("测试点: 退款异常测试,order_id输入没有支付的订单,断言失败");
		try {
			financeStrikeParam.setOrder_ids(Arrays.asList(order_id));
			boolean result = openFinanceService.strikeFinance(financeStrikeParam);
			Assert.assertEquals(result, true, "冲账操作失败");
			result = openFinanceService.refundOrderFinance(order_id, "1");
			Assert.assertEquals(result, false, "整单付款的订单使用差额退款,断言失败");
		} catch (Exception e) {
			logger.error("退款遇到错误: ", e);
			Assert.fail("退款遇到错误: ", e);
		}
	}

	@Test
	public void financeAbnormalTestCase16() {
		Reporter.log("测试点: 退款异常测试,type 输入为空,断言失败");
		try {
			financeStrikeParam.setOrder_ids(Arrays.asList(order_id));
			boolean result = openFinanceService.strikeFinance(financeStrikeParam);
			Assert.assertEquals(result, true, "冲账操作失败");
			result = openFinanceService.refundOrderFinance(order_id, "");
			Assert.assertEquals(result, false, "退款异常测试,type 输入为空,断言失败");
		} catch (Exception e) {
			logger.error("退款遇到错误: ", e);
			Assert.fail("退款遇到错误: ", e);
		}
	}

	@Test
	public void financeAbnormalTestCase17() {
		Reporter.log("测试点: 退款异常测试,type 输入非候选值,断言失败");
		try {
			financeStrikeParam.setOrder_ids(Arrays.asList(order_id));
			boolean result = openFinanceService.strikeFinance(financeStrikeParam);
			Assert.assertEquals(result, true, "冲账操作失败");
			result = openFinanceService.refundOrderFinance(order_id, "3");
			Assert.assertEquals(result, false, "退款异常测试,type 输入非候选值,断言失败");
		} catch (Exception e) {
			logger.error("退款遇到错误: ", e);
			Assert.fail("退款遇到错误: ", e);
		}
	}

}
