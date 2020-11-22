package cn.guanmai.manage.bean.finance.result;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jan 18, 2019 3:19:42 PM 
* @des 财务结算到账凭证 结果类
* @version 1.0 
*/
public class StrikeBalanceBean {
	private String id;

	private BigDecimal strike_money;

	private String deal_code;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the strike_money
	 */
	public BigDecimal getStrike_money() {
		return strike_money;
	}

	/**
	 * @param strike_money
	 *            the strike_money to set
	 */
	public void setStrike_money(BigDecimal strike_money) {
		this.strike_money = strike_money;
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
}
