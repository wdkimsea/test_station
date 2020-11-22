package cn.guanmai.open.interfaces.finance;

import java.util.List;

import cn.guanmai.open.bean.finance.CashFlowBean;
import cn.guanmai.open.bean.finance.StrikeFlowBean;
import cn.guanmai.open.bean.finance.param.FinanceStrikeParam;

/* 
* @author liming 
* @date Jun 6, 2019 10:50:14 AM 
* @des 财务相业务接口
* @version 1.0 
*/
public interface OpenFinanceService {
	/**
	 * 冲账
	 * 
	 * @param financeStrikeParam
	 * @return
	 * @throws Exception
	 */
	public boolean strikeFinance(FinanceStrikeParam financeStrikeParam) throws Exception;

	/**
	 * 退款
	 * 
	 * @param order_id
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public boolean refundOrderFinance(String order_id, String type) throws Exception;

	/**
	 * 查询冲账流水
	 * 
	 * @param start_time
	 * @param end_time
	 * @param customer_id
	 * @return
	 * @throws Exception
	 */
	public List<StrikeFlowBean> searchStrikeFlow(String start_time, String end_time, String customer_id)
			throws Exception;

	/**
	 * 先进流水
	 * 
	 * @param offset
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<CashFlowBean> searchCashFlow(String start_time, String end_time, String customer_id, Integer offset, Integer limit) throws Exception;
}
