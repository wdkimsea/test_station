package cn.guanmai.open.finance;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.manage.impl.finance.MgFinanceServiceImpl;
import cn.guanmai.manage.interfaces.finance.MgFinanceService;
import cn.guanmai.open.bean.finance.CashFlowBean;
import cn.guanmai.open.bean.finance.StrikeFlowBean;
import cn.guanmai.open.bean.finance.param.FinanceStrikeParam;
import cn.guanmai.open.impl.finance.OpenFinanceServiceImpl;
import cn.guanmai.open.interfaces.finance.OpenFinanceService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Jun 17, 2019 3:20:25 PM 
* @des 开放平台冲账测试
* @version 1.0 
*/
public class FinanceTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(FinanceTest.class);
	private OpenFinanceService openFinanceService;
	private MgFinanceService financeService;
	private OrderTool orderTool;
	private OrderService orderService;
	private String order_id;
	private OrderDetailBean orderDetail;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();

		Map<String, String> st_headers = getSt_headers();

		openFinanceService = new OpenFinanceServiceImpl(access_token);
		orderTool = new OrderTool(st_headers);
		orderService = new OrderServiceImpl(st_headers);

		Map<String, String> ma_headers = getMa_headers();
		financeService = new MgFinanceServiceImpl(ma_headers);
	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			order_id = orderTool.oneStepCreateOrder(6);
			Assert.assertNotEquals(order_id, null, "通过开放平台下单失败");
			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详情失败");
		} catch (Exception e) {
			logger.error("商户下单过程中遇到错误: ", e);
			Assert.fail("商户下单过程中遇到错误: ", e);
		}
	}

	@Test
	public void financeTestCase01() {
		Reporter.log("测试点: 批量冲账");
		try {
			List<String> order_ids = Arrays.asList(order_id);

			String deal_code = StringUtil.getRandomString(12).toUpperCase();
			FinanceStrikeParam financeStrikeParam = new FinanceStrikeParam();
			financeStrikeParam.setArrival_method_id("cash");
			financeStrikeParam.setCustomer_id(orderDetail.getCustomer().getAddress_id());
			financeStrikeParam.setDeal_code(deal_code);
			financeStrikeParam.setOrder_ids(order_ids);
			financeStrikeParam.setRemark("he");

			boolean result = openFinanceService.strikeFinance(financeStrikeParam);
			Assert.assertEquals(result, true, "冲账失败");
		} catch (Exception e) {
			logger.error("冲账过程中遇到错误: ", e);
			Assert.fail("冲账过程中遇到错误: ", e);
		}
	}

	@Test
	public void financeTestCase02() {
		Reporter.log("测试点: 订单退款");
		try {

			String deal_code = StringUtil.getRandomString(12).toUpperCase();
			FinanceStrikeParam financeStrikeParam = new FinanceStrikeParam();
			financeStrikeParam.setArrival_method_id("cash");
			financeStrikeParam.setCustomer_id(orderDetail.getCustomer().getAddress_id());
			financeStrikeParam.setDeal_code(deal_code);
			financeStrikeParam.setOrder_ids(Arrays.asList(order_id));
			financeStrikeParam.setRemark("he");

			boolean result = openFinanceService.strikeFinance(financeStrikeParam);
			Assert.assertEquals(result, true, "冲账失败");

			// TODO 需解锁订单
			result = financeService.updateOrderLockStatus(Arrays.asList(order_id), 0);
			Assert.assertEquals(result, true, "订单 " + order_id + " 解锁失败");

			result = openFinanceService.refundOrderFinance(order_id, "2");
			Assert.assertEquals(result, true, "整单退款失败");
		} catch (Exception e) {
			logger.error("退款过程中遇到错误: ", e);
			Assert.fail("退款过程中遇到错误: ", e);
		}
	}

	@Test
	public void financeTestCase03() {
		Reporter.log("测试点: 查询冲账流水");
		try {
			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			List<StrikeFlowBean> strikeFlowList = openFinanceService.searchStrikeFlow(todayStr, todayStr,
					orderDetail.getCustomer().getAddress_id());
			Assert.assertNotEquals(strikeFlowList, null, "查询冲账流水失败");
		} catch (Exception e) {
			logger.error("查询冲账流水遇到错误: ", e);
			Assert.fail("查询冲账流水遇到错误: ", e);
		}
	}

	@Test
	public void financeTestCase04() {
		Reporter.log("测试点: 查询余额流水");
		try {
			String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
			List<CashFlowBean> cashFlowList = openFinanceService.searchCashFlow(todayStr, todayStr,
					orderDetail.getCustomer().getAddress_id(), 0, 20);
			Assert.assertNotEquals(cashFlowList, null, "查询余额流水");
		} catch (Exception e) {
			logger.error("查询余额流水遇到错误: ", e);
			Assert.fail("查询余额流水遇到错误: ", e);
		}
	}
}
