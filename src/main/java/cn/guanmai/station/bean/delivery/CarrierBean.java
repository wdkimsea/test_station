package cn.guanmai.station.bean.delivery;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jan 4, 2019 11:18:35 AM 
* @des  配送-承运商
* @version 1.0 
*/
public class CarrierBean {
	private String company_name;
	/**
	 * 有效司机
	 */
	private int count;
	private BigDecimal id;

	/**
	 * @return the company_name
	 */
	public String getCompany_name() {
		return company_name;
	}

	/**
	 * @param company_name
	 *            the company_name to set
	 */
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the id
	 */
	public BigDecimal getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(BigDecimal id) {
		this.id = id;
	}

}
