package cn.guanmai.manage.bean.finance.param;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jan 16, 2019 10:50:10 AM 
* @version 1.0 
*/
public class StrikeBalanceParamBean {
	private String arrival_time;
	private String arrival_method;
	private String arrival_id;
	private String deal_code;
	private String remark;
	private BigDecimal arrival_money;
	private String kid;

	/**
	 * @return the arrival_time
	 */
	public String getArrival_time() {
		return arrival_time;
	}

	/**
	 * 到账日期,参数格式 2019-01-16
	 * 
	 * @param arrival_time
	 *            the arrival_time to set
	 */
	public void setArrival_time(String arrival_time) {
		this.arrival_time = arrival_time;
	}

	/**
	 * @return the arrival_method
	 */
	public String getArrival_method() {
		return arrival_method;
	}

	/**
	 * 到账方式,参数候选项 [微信对私、现金]
	 * 
	 * @param arrival_method
	 *            the arrival_method to set
	 */
	public void setArrival_method(String arrival_method) {
		this.arrival_method = arrival_method;
	}

	/**
	 * @return the arrival_id
	 */
	public String getArrival_id() {
		return arrival_id;
	}

	/**
	 * 到账ID,参数候选词 [wechat、cash]
	 * 
	 * @param arrival_id
	 *            the arrival_id to set
	 */
	public void setArrival_id(String arrival_id) {
		this.arrival_id = arrival_id;
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
	 * @return the arrival_money
	 */
	public BigDecimal getArrival_money() {
		return arrival_money;
	}

	/**
	 * 到账金额,金额单位为分
	 * 
	 * @param arrival_money
	 *            the arrival_money to set
	 */
	public void setArrival_money(BigDecimal arrival_money) {
		this.arrival_money = arrival_money;
	}

	/**
	 * @return the kid
	 */
	public String getKid() {
		return kid;
	}

	/**
	 * @param kid
	 *            the kid to set
	 */
	public void setKid(String kid) {
		this.kid = kid;
	}

}
