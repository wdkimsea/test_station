package cn.guanmai.manage.interfaces.finance;

import java.math.BigDecimal;
import java.util.List;

import cn.guanmai.manage.bean.custommange.param.CustomerBillFilterParam;
import cn.guanmai.manage.bean.custommange.result.CustomerBillDetailBean;
import cn.guanmai.manage.bean.custommange.result.CustomerBillListBean;
import cn.guanmai.manage.bean.finance.param.FinanceOrderArrivalParamBean;
import cn.guanmai.manage.bean.finance.param.FinanceOrderParamBean;
import cn.guanmai.manage.bean.finance.param.StrikeBalanceParamBean;
import cn.guanmai.manage.bean.finance.result.FinanceOrderBean;

/* 
* @author liming 
* @date Jan 16, 2019 10:57:06 AM 
* @des 财务相关业务接口
* @version 1.0 
*/
public interface MgFinanceService {
	/**
	 * 账号充值
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean rechargeMoney(String kid, BigDecimal money) throws Exception;

	/**
	 * 添加到账凭证
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public boolean addStrikeBalance(StrikeBalanceParamBean paramBean) throws Exception;

	/**
	 * 商户结算查询
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public List<FinanceOrderBean> searchFinanceOrder(FinanceOrderParamBean paramBean) throws Exception;

	/**
	 * 导出商户结算数据
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public boolean exportFinanceOrder(FinanceOrderParamBean paramBean) throws Exception;

	/**
	 * 添加到账凭证
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public boolean addFinanceOrderArrival(FinanceOrderArrivalParamBean paramBean) throws Exception;

	/**
	 * 商户对账单列表
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public List<CustomerBillListBean> searchCustomerBill(CustomerBillFilterParam paramBean) throws Exception;

	/**
	 * 商户对账单详细信息
	 * 
	 * @param date_from
	 * @param date_to
	 * @param sid         商户SID
	 * @param search_type
	 * @return
	 * @throws Exception
	 */
	public CustomerBillDetailBean customerBillDetail(String date_from, String date_to, String sid, Integer search_type)
			throws Exception;


	/**
	 * 获取商户报表,按销售经理统计
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param saleEmployee
	 * @return
	 * @throws Exception
	 */
	public boolean customerReport(String beginTime, String endTime, BigDecimal saleEmployee) throws Exception;

	/**
	 * 获取商户报表,按站点统计
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param station_id
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public boolean customerReport(String beginTime, String endTime, String station_id) throws Exception;

	/**
	 * 按销售经理统计的商户报表详细
	 * 
	 * @param begin_time
	 * @param end_time
	 * @param saleEmployee_id
	 * @return
	 * @throws Exception
	 */
	public boolean customerReportDetail(String begin_time, String end_time, BigDecimal saleEmployee_id)
			throws Exception;

	/**
	 * 按站点统计的商户报表详细
	 * 
	 * @param begin_time
	 * @param end_time
	 * @param station_id
	 * @return
	 */
	public boolean customerReportDetail(String begin_time, String end_time, String station_id) throws Exception;

	/**
	 * 订单解锁和锁定
	 * 
	 * @param order_ids
	 * @param status    0:解锁、1:锁定
	 * @return
	 * @throws Exception
	 */
	public boolean updateOrderLockStatus(List<String> order_ids, int status) throws Exception;

	/**
	 * 订单退款
	 * 
	 * @param order_id
	 * @param type     2:整单退款
	 * @return
	 * @throws Exception
	 */
	public boolean refundOrder(String order_id, int type) throws Exception;

}
