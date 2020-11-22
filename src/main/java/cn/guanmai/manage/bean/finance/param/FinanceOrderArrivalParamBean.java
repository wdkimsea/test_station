package cn.guanmai.manage.bean.finance.param;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jan 18, 2019 10:33:56 AM 
* @des 提交到账凭证参数类
* @version 1.0 
*/
public class FinanceOrderArrivalParamBean {
	private String arrival_account;

	private String arrival_channel;

	private String arrival_time;

	private String deal_code;

	private boolean is_wipe_zero;

	private String order_id;

	private int pay_status;

	private BigDecimal real_pay;

	private String remark;

	private BigDecimal rest_pay;

	private String user_id;

	/**
	 * @return the arrival_account
	 */
	public String getArrival_account() {
		return arrival_account;
	}

	/**
	 * @param arrival_account
	 *            the arrival_account to set
	 */
	public void setArrival_account(String arrival_account) {
		this.arrival_account = arrival_account;
	}

	/**
	 * @return the arrival_channel
	 */
	public String getArrival_channel() {
		return arrival_channel;
	}

	/**
	 * @param arrival_channel
	 *            the arrival_channel to set
	 */
	public void setArrival_channel(String arrival_channel) {
		this.arrival_channel = arrival_channel;
	}

	/**
	 * @return the arrival_time
	 */
	public String getArrival_time() {
		return arrival_time;
	}

	/**
	 * @param arrival_time
	 *            the arrival_time to set
	 */
	public void setArrival_time(String arrival_time) {
		this.arrival_time = arrival_time;
	}

	/**
	 * @return the deal_code
	 */
	public String getDeal_code() {
		return deal_code;
	}

	/**
	 * @param deal_code
	 *            the deal_code to set
	 */
	public void setDeal_code(String deal_code) {
		this.deal_code = deal_code;
	}

	/**
	 * @return the is_wipe_zero
	 */
	public boolean isIs_wipe_zero() {
		return is_wipe_zero;
	}

	/**
	 * @param is_wipe_zero
	 *            the is_wipe_zero to set
	 */
	public void setIs_wipe_zero(boolean is_wipe_zero) {
		this.is_wipe_zero = is_wipe_zero;
	}

	/**
	 * @return the order_id
	 */
	public String getOrder_id() {
		return order_id;
	}

	/**
	 * @param order_id
	 *            the order_id to set
	 */
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	/**
	 * @return the pay_status
	 */
	public int getPay_status() {
		return pay_status;
	}

	/**
	 * @param pay_status
	 *            the pay_status to set
	 */
	public void setPay_status(int pay_status) {
		this.pay_status = pay_status;
	}

	/**
	 * @return the real_pay
	 */
	public BigDecimal getReal_pay() {
		return real_pay;
	}

	/**
	 * @param real_pay
	 *            the real_pay to set
	 */
	public void setReal_pay(BigDecimal real_pay) {
		this.real_pay = real_pay;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the rest_pay
	 */
	public BigDecimal getRest_pay() {
		return rest_pay;
	}

	/**
	 * @param rest_pay
	 *            the rest_pay to set
	 */
	public void setRest_pay(BigDecimal rest_pay) {
		this.rest_pay = rest_pay;
	}

	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id
	 *            the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}
