package cn.guanmai.manage.finance;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.manage.bean.finance.param.FinanceOrderArrivalParamBean;
import cn.guanmai.manage.bean.finance.param.FinanceOrderParamBean;
import cn.guanmai.manage.bean.finance.param.StrikeBalanceParamBean;
import cn.guanmai.manage.bean.finance.result.FinanceOrderBean;
import cn.guanmai.manage.impl.finance.MgFinanceServiceImpl;
import cn.guanmai.manage.interfaces.finance.MgFinanceService;
import cn.guanmai.manage.tools.LoginManage;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Jan 16, 2019 11:05:55 AM 
* @des 财务相关测试用例
* @version 1.0 
*/
public class FinanceTest extends LoginManage {
	private Logger logger = LoggerFactory.getLogger(FinanceTest.class);
	private Map<String, String> ma_cookie;
	private MgFinanceService financeService;
	private String todayStr;
	private String previous_month_day;

	@BeforeClass
	public void initData() {
		ma_cookie = getManageCookie();
		financeService = new MgFinanceServiceImpl(ma_cookie);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		todayStr = format.format(new Date());

		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.MONTH, -1);
		previous_month_day = format.format(ca.getTime());
	}

	/**
	 * 添加到账凭证
	 * 
	 */
	@Test
	public void financeTestCase01() {
		try {

			FinanceOrderParamBean paramBean = new FinanceOrderParamBean(previous_month_day, todayStr);
			List<FinanceOrderBean> financeOrderList = financeService.searchFinanceOrder(paramBean);
			Assert.assertNotEquals(financeOrderList, null, "商户结算,获取结算订单数据失败");

			Assert.assertEquals(financeOrderList.size() > 0, true, "经查询,近一个月无没支付的订单,无法进行后续步骤");

			String kid = NumberUtil.roundNumberInList(financeOrderList).getCustomer().getUid();

			StrikeBalanceParamBean strikeBalance = new StrikeBalanceParamBean();
			strikeBalance.setArrival_time(todayStr);
			strikeBalance.setArrival_method("微信对私");
			strikeBalance.setArrival_id("wechat");
			strikeBalance.setArrival_money(new BigDecimal("10000"));
			strikeBalance.setDeal_code("ZDH" + StringUtil.getRandomString(13).toUpperCase());
			strikeBalance.setKid(kid.replace("K", ""));
			strikeBalance.setRemark("自动化添加");

			boolean result = financeService.addStrikeBalance(strikeBalance);
			Assert.assertEquals(result, true, "新增到账凭证失败");

		} catch (Exception e) {
			logger.error("进行添加到账凭证的过程中出现错误: ", e);
			Assert.fail("进行添加到账凭证的过程中出现错误: ", e);
		}
	}

	/**
	 * 导出商户结算列表
	 * 
	 */
	@Test
	public void financeTestCase02() {
		try {

			FinanceOrderParamBean paramBean = new FinanceOrderParamBean(previous_month_day, todayStr);
			boolean result = financeService.exportFinanceOrder(paramBean);
			Assert.assertEquals(result, true, "导出商户结算列表失败");
		} catch (Exception e) {
			logger.error("导出商户结算列表出现错误: ", e);
			Assert.fail("导出商户结算列表出现错误: ", e);
		}
	}

	@Test
	public void financeTestCase03() {
		try {

			FinanceOrderParamBean paramBean = new FinanceOrderParamBean(previous_month_day, todayStr);
			paramBean.setPay_status(0);

			List<FinanceOrderBean> financeOrderList = financeService.searchFinanceOrder(paramBean);
			Assert.assertNotEquals(financeOrderList, null, "商户结算,获取结算订单数据失败");

			Assert.assertEquals(financeOrderList.size() > 0, true, "经查询,近一个月无没支付的订单,无法进行后续步骤");

			FinanceOrderBean financeOrder = financeOrderList.get(0);

			FinanceOrderArrivalParamBean financeOrderArrivalParam = new FinanceOrderArrivalParamBean();
			financeOrderArrivalParam.setArrival_account("wechat");
			financeOrderArrivalParam.setArrival_channel("微信对私");
			financeOrderArrivalParam.setArrival_time(todayStr);
			financeOrderArrivalParam.setDeal_code("ZDH" + StringUtil.getRandomString(7).toUpperCase());
			financeOrderArrivalParam.setIs_wipe_zero(false);
			financeOrderArrivalParam.setOrder_id(financeOrder.getOrder_id());
			financeOrderArrivalParam.setPay_status(1);
			financeOrderArrivalParam.setReal_pay(financeOrder.getSale_money());
			financeOrderArrivalParam.setRemark("自动化添加");
			financeOrderArrivalParam.setRest_pay(financeOrder.getSale_money());
			financeOrderArrivalParam.setUser_id(financeOrder.getCustomer().getUid().replace("K", ""));

			boolean result = financeService.addFinanceOrderArrival(financeOrderArrivalParam);

			Assert.assertEquals(result, true, "添加到账凭证失败");
		} catch (Exception e) {
			logger.error("添加到账凭证过程中遇到错误: ", e);
			Assert.fail("添加到账凭证过程中遇到错误: ", e);
		}
	}
}
