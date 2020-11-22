package cn.guanmai.manage.tools;

import cn.guanmai.manage.bean.finance.param.FinanceOrderParamBean;
import cn.guanmai.manage.bean.finance.result.FinanceOrderBean;
import cn.guanmai.manage.impl.finance.MgFinanceServiceImpl;
import cn.guanmai.manage.interfaces.finance.MgFinanceService;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

/**
 * @program: station
 * @description: 获取部分订单
 * @author: weird
 * @create: 2019-01-21 17:37
 **/
public class GetOrdersUtil {

	public static List<FinanceOrderBean> get_orders(String start_date, String end_date, Map<String, String> cookie)
			throws Exception {
		FinanceOrderParamBean paramBean = new FinanceOrderParamBean(start_date, end_date);
		MgFinanceService financeService = new MgFinanceServiceImpl(cookie);
		List<FinanceOrderBean> financeOrderList = financeService.searchFinanceOrder(paramBean);
		Assert.assertNotEquals(financeOrderList, null, "商户结算,获取结算订单数据失败");
		Assert.assertTrue(financeOrderList.size() > 0, "经查询,近段时间无订单,无法进行后续步骤");
		return financeOrderList;
	}
}
