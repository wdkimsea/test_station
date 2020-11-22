package cn.guanmai.bshop.bean.marketing;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年6月15日 下午7:44:50
 * @description:
 * @version: 1.0
 */

public class BsCouponAvailBean {
	private String id;
	private String name;
	private String expiring_time;
	private String station_id;
	private String description;
	private String order_id_lock;

	private BigDecimal min_total_price;
	private BigDecimal price_value;

	private int status;
	private int days_remaining;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExpiring_time() {
		return expiring_time;
	}

	public void setExpiring_time(String expiring_time) {
		this.expiring_time = expiring_time;
	}

	public String getStation_id() {
		return station_id;
	}

	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrder_id_lock() {
		return order_id_lock;
	}

	public void setOrder_id_lock(String order_id_lock) {
		this.order_id_lock = order_id_lock;
	}

	public BigDecimal getMin_total_price() {
		return min_total_price;
	}

	public void setMin_total_price(BigDecimal min_total_price) {
		this.min_total_price = min_total_price;
	}

	public BigDecimal getPrice_value() {
		return price_value;
	}

	public void setPrice_value(BigDecimal price_value) {
		this.price_value = price_value;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getDays_remaining() {
		return days_remaining;
	}

	public void setDays_remaining(int days_remaining) {
		this.days_remaining = days_remaining;
	}

}
