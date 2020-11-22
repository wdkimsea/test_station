package cn.guanmai.manage.bean.custommange.param;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jan 15, 2019 2:37:45 PM 
* @des 商户对账单查询过滤参数类
* @version 1.0 
*/
public class CustomerBillFilterParam {
	private int search_type;
	private String station_id;
	private BigDecimal sale_employee_id;
	private Integer settle_way;
	private Integer pay_method;
	private String date_from;
	private String date_to;
	private int offset;
	private int limit;

	public CustomerBillFilterParam() {
		super();
	}

	/**
	 * @return the search_type
	 */
	public int getSearch_type() {
		return search_type;
	}

	/**
	 * @param search_type
	 *            the search_type to set
	 */
	public void setSearch_type(int search_type) {
		this.search_type = search_type;
	}

	/**
	 * @return the station_id
	 */
	public String getStation_id() {
		return station_id;
	}

	/**
	 * @param station_id
	 *            the station_id to set
	 */
	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	/**
	 * @return the sale_employee_id
	 */
	public BigDecimal getSale_employee_id() {
		return sale_employee_id;
	}

	/**
	 * @param sale_employee_id
	 *            the sale_employee_id to set
	 */
	public void setSale_employee_id(BigDecimal sale_employee_id) {
		this.sale_employee_id = sale_employee_id;
	}

	/**
	 * @return the settle_way
	 */
	public Integer getSettle_way() {
		return settle_way;
	}

	/**
	 * @param settle_way
	 *            the settle_way to set
	 */
	public void setSettle_way(Integer settle_way) {
		this.settle_way = settle_way;
	}

	/**
	 * @return the pay_method
	 */
	public Integer getPay_method() {
		return pay_method;
	}

	/**
	 * @param pay_method
	 *            the pay_method to set
	 */
	public void setPay_method(Integer pay_method) {
		this.pay_method = pay_method;
	}

	/**
	 * @return the date_from
	 */
	public String getDate_from() {
		return date_from;
	}

	/**
	 * @param date_from
	 *            the date_from to set
	 */
	public void setDate_from(String date_from) {
		this.date_from = date_from;
	}

	/**
	 * @return the date_to
	 */
	public String getDate_to() {
		return date_to;
	}

	/**
	 * @param date_to
	 *            the date_to to set
	 */
	public void setDate_to(String date_to) {
		this.date_to = date_to;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

}
