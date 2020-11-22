package cn.guanmai.manage.ordermanage;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.manage.bean.finance.param.FinanceOrderParamBean;
import cn.guanmai.manage.bean.finance.result.FinanceOrderBean;
import cn.guanmai.manage.bean.ordermanage.param.DailyOrderParamBean;
import cn.guanmai.manage.bean.ordermanage.param.OrderExceptionParamBean;
import cn.guanmai.manage.bean.ordermanage.param.OrderExceptionParamBean.OrderException;
import cn.guanmai.manage.bean.ordermanage.param.OrderExceptionParamBean.OrderRefund;
import cn.guanmai.manage.bean.ordermanage.param.OrderExceptionParamBean.OrderRemark;
import cn.guanmai.manage.bean.ordermanage.result.DailyOrderBean;
import cn.guanmai.manage.bean.ordermanage.result.OrderDetailInfoBean;
import cn.guanmai.manage.bean.ordermanage.result.OrderDetailInfoBean.Details;
import cn.guanmai.manage.impl.finance.MgFinanceServiceImpl;
import cn.guanmai.manage.impl.ordermanage.OrderManageServiceImpl;
import cn.guanmai.manage.interfaces.finance.MgFinanceService;
import cn.guanmai.manage.interfaces.ordermanange.OrderManangeService;
import cn.guanmai.manage.tools.LoginManage;
import cn.guanmai.util.NumberUtil;

/* 
* @author liming 
* @date Jan 18, 2019 7:03:09 PM 
* @todo TODO
* @version 1.0 
*/
public class OrderManageTest extends LoginManage {
	private Logger logger = LoggerFactory.getLogger(OrderManageTest.class);
	private Map<String, String> ma_cookie;
	private OrderManangeService orderManangeService;
	private MgFinanceService financeService;
	private String todayStr;
	private String previous_month_day;

	@BeforeClass
	public void initData() {
		ma_cookie = getManageCookie();
		orderManangeService = new OrderManageServiceImpl(ma_cookie);
		financeService = new MgFinanceServiceImpl(ma_cookie);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		todayStr = format.format(new Date());

		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.MONTH, -1);
		previous_month_day = format.format(ca.getTime());
	}

	@Test
	public void orderManageTestCase01() {
		try {
			List<String> districtCodeList = orderManangeService.districtCodeList();
			Assert.assertNotEquals(districtCodeList, null, "获取地区编号失败");
			DailyOrderParamBean paramBean = new DailyOrderParamBean();
			paramBean.setDate(todayStr);
			paramBean.setNum(50);
			paramBean.setPage(0);

			for (String code : districtCodeList) {
				paramBean.setDistrict_code(code);
				List<DailyOrderBean> dailyOrderList = orderManangeService.searchDailyOrder(paramBean);
				Assert.assertNotEquals(dailyOrderList, null, "售后-异常-每日订单搜索失败");
			}

		} catch (Exception e) {
			logger.error("售后-异常-每日订单搜索遇到错误: ", e);
			Assert.fail("售后-异常-每日订单搜索遇到错误: ", e);
		}
	}

	@Test
	public void orderManageTestCase02() {
		try {

			List<String> districtCodeList = orderManangeService.districtCodeList();
			Assert.assertNotEquals(districtCodeList, null, "获取地区编号失败");
			DailyOrderParamBean paramBean = new DailyOrderParamBean();
			paramBean.setDate(todayStr);
			paramBean.setNum(50);
			paramBean.setPage(0);

			for (String code : districtCodeList) {
				paramBean.setDistrict_code(code);
				boolean result = orderManangeService.exportDailyOrder(paramBean);
				Assert.assertEquals(result, true, "导出每日订单失败");
			}

		} catch (Exception e) {
			logger.error("导出每日订单失败遇到错误: ", e);
			Assert.fail("导出每日订单失败遇到错误: ", e);
		}
	}

	@Test
	public void orderManageTestCase03() {
		try {
			FinanceOrderParamBean paramBean = new FinanceOrderParamBean(previous_month_day, todayStr);
			paramBean.setPay_status(0);
			List<FinanceOrderBean> financeOrderList = financeService.searchFinanceOrder(paramBean);
			Assert.assertNotEquals(financeOrderList, null, "商户结算,获取结算订单数据失败");

			Assert.assertEquals(financeOrderList.size() > 0, true, "经查询,近一个月没有未支付的订单,无法进行后续步骤");

			String order_id = NumberUtil.roundNumberInList(financeOrderList).getOrder_id();

			boolean result = orderManangeService.searchOrder(order_id);
			Assert.assertEquals(result, true, "用户订单异常页面,按订单搜索失败");

			OrderDetailInfoBean orderDetailInfo = orderManangeService.getOrderDetailInfo(order_id);
			Assert.assertNotEquals(orderDetailInfo, null, "用户订单异常页面,获取订单详细信息失败");

			String token = orderManangeService.getToken(order_id);
			Assert.assertNotEquals(token, null, "获取token失败,无法进行添加订单异常售后操作");

			// 随机取一个商品进行添加售后
			Details detail = NumberUtil.roundNumberInList(orderDetailInfo.getDetails());

			OrderExceptionParamBean orderExceptionParam = new OrderExceptionParamBean();
			orderExceptionParam.setToken(token);
			orderExceptionParam.setId(order_id);
			orderExceptionParam.setExceptions(new ArrayList<OrderException>());
			orderExceptionParam.setRemarks(new ArrayList<OrderRemark>());

			List<OrderRefund> orderRundList = new ArrayList<OrderRefund>();
			OrderRefund orderRund = orderExceptionParam.new OrderRefund();
			orderRund.setSku_id(detail.getSku_id());
			orderRund.setException_reason(1);
			orderRund.setDriver_id(0);
			orderRund.setRequest_amount(detail.getQuantity().subtract(new BigDecimal("0.1")));
			orderRundList.add(orderRund);

			orderExceptionParam.setRefunds(orderRundList);

			result = orderManangeService.addOrderException(orderExceptionParam);
			Assert.assertEquals(result, true, "添加订单售后失败");

		} catch (Exception e) {
			logger.error("为订单添加售后信息的过程中遇到错误: ", e);
			Assert.fail("为订单添加售后信息的过程中遇到错误: ", e);
		}
	}

}
