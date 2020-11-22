package cn.guanmai.manage.custommange;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.manage.bean.custommange.param.CustomerBillFilterParam;
import cn.guanmai.manage.bean.custommange.result.CustomerBillDetailBean;
import cn.guanmai.manage.bean.custommange.result.CustomerBillListBean;
import cn.guanmai.manage.impl.finance.MgFinanceServiceImpl;
import cn.guanmai.manage.interfaces.finance.MgFinanceService;
import cn.guanmai.manage.tools.LoginManage;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Jan 15, 2019 2:49:04 PM 
* @des 商户对账单测试类
* @version 1.0 
*/
public class CustomerBillTest extends LoginManage {
	private Logger logger = LoggerFactory.getLogger(CustomerBillTest.class);
	private Map<String, String> ma_cookie;
	private MgFinanceService financeService;
	private String todayStr;

	@BeforeClass
	public void initData() {
		ma_cookie = getManageCookie();
		financeService = new MgFinanceServiceImpl(ma_cookie);
		todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
	}

	@Test
	public void customerBillTestCase01() {
		ReporterCSS.title("测试点: 获取商户对账单列表");
		CustomerBillFilterParam paramBean = new CustomerBillFilterParam();
		paramBean.setSearch_type(2);
		paramBean.setDate_from(todayStr);
		paramBean.setDate_to(todayStr);
		paramBean.setOffset(0);
		paramBean.setLimit(10);
		try {
			List<CustomerBillListBean> list = financeService.searchCustomerBill(paramBean);
			Assert.assertNotEquals(list, null, "获取商户对账单列表失败");
		} catch (Exception e) {
			logger.error("获取商户对账单列表出现错误: ", e);
			Assert.fail("获取商户对账单列表出现错误: ", e);
		}
	}

	@Test
	public void customerBillTestCase02() {
		ReporterCSS.title("测试点: 获取商户对账单详细信息");
		CustomerBillFilterParam paramBean = new CustomerBillFilterParam();
		paramBean.setSearch_type(2);
		paramBean.setDate_from(todayStr);
		paramBean.setDate_to(todayStr);
		paramBean.setOffset(0);
		paramBean.setLimit(10);
		try {
			List<CustomerBillListBean> list = financeService.searchCustomerBill(paramBean);
			Assert.assertNotEquals(list, null, "获取商户对账单列表失败");

			String sid = NumberUtil.roundNumberInList(list).getSID();

			CustomerBillDetailBean billDetail = financeService.customerBillDetail(todayStr, todayStr, sid, 2);

			Assert.assertNotEquals(billDetail, null, "获取对账单详细信息失败");
		} catch (Exception e) {
			logger.error("获取商户对账单详细信息出现错误: ", e);
			Assert.fail("获取商户对账单详细信息出现错误: ", e);
		}
	}

}
