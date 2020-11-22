package cn.guanmai.bshop.bean;

import java.math.BigDecimal;

/* 
* @author liming 
* @date May 20, 2019 5:08:23 PM 
* @des 优惠券
* @version 1.0 
*/
public class CouponBean {
	private int audience_type;
	private int days_remaining;
	private String description;
	private String expiring_time;
	private BigDecimal id;
	private BigDecimal min_total_price;
	private String name;
	private String order_id_lock;
	private BigDecimal price_value;
	private String station_id;
	private int status;

	/**
	 * @return the audience_type
	 */
	public int getAudience_type() {
		return audience_type;
	}

	/**
	 * @param audience_type
	 *            the audience_type to set
	 */
	public void setAudience_type(int audience_type) {
		this.audience_type = audience_type;
	}

	/**
	 * @return the days_remaining
	 */
	public int getDays_remaining() {
		return days_remaining;
	}

	/**
	 * @param days_remaining
	 *            the days_remaining to set
	 */
	public void setDays_remaining(int days_remaining) {
		this.days_remaining = days_remaining;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the expiring_time
	 */
	public String getExpiring_time() {
		return expiring_time;
	}

	/**
	 * @param expiring_time
	 *            the expiring_time to set
	 */
	public void setExpiring_time(String expiring_time) {
		this.expiring_time = expiring_time;
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

	/**
	 * @return the min_total_price
	 */
	public BigDecimal getMin_total_price() {
		return min_total_price;
	}

	/**
	 * @param min_total_price
	 *            the min_total_price to set
	 */
	public void setMin_total_price(BigDecimal min_total_price) {
		this.min_total_price = min_total_price;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the order_id_lock
	 */
	public String getOrder_id_lock() {
		return order_id_lock;
	}

	/**
	 * @param order_id_lock
	 *            the order_id_lock to set
	 */
	public void setOrder_id_lock(String order_id_lock) {
		this.order_id_lock = order_id_lock;
	}

	/**
	 * @return the price_value
	 */
	public BigDecimal getPrice_value() {
		return price_value;
	}

	/**
	 * @param price_value
	 *            the price_value to set
	 */
	public void setPrice_value(BigDecimal price_value) {
		this.price_value = price_value;
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
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

}
