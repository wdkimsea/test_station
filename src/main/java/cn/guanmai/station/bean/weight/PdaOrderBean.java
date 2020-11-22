package cn.guanmai.station.bean.weight;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年7月31日 下午6:11:35
 * @des 接口 /weight/pda/order/list 对应的结果
 * @version 1.0
 */
public class PdaOrderBean {
	private String address_id;
	private String address_name;
	private BigDecimal driver_id;
	private String driver_name;
	private int finish_count;
	private boolean index;
	private String order_id;
	private BigDecimal route_id;
	private String route_name;
	private String sort_id;
	private BigDecimal sort_schedule;
	private int status;
	private int total_count;

	/**
	 * @return the address_id
	 */
	public String getAddress_id() {
		return address_id;
	}

	/**
	 * @param address_id the address_id to set
	 */
	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	/**
	 * @return the address_name
	 */
	public String getAddress_name() {
		return address_name;
	}

	/**
	 * @param address_name the address_name to set
	 */
	public void setAddress_name(String address_name) {
		this.address_name = address_name;
	}

	/**
	 * @return the driver_id
	 */
	public BigDecimal getDriver_id() {
		return driver_id;
	}

	/**
	 * @param driver_id the driver_id to set
	 */
	public void setDriver_id(BigDecimal driver_id) {
		this.driver_id = driver_id;
	}

	/**
	 * @return the driver_name
	 */
	public String getDriver_name() {
		return driver_name;
	}

	/**
	 * @param driver_name the driver_name to set
	 */
	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}

	/**
	 * @return the finish_count
	 */
	public int getFinish_count() {
		return finish_count;
	}

	/**
	 * @param finish_count the finish_count to set
	 */
	public void setFinish_count(int finish_count) {
		this.finish_count = finish_count;
	}

	/**
	 * @return the index
	 */
	public boolean isIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(boolean index) {
		this.index = index;
	}

	/**
	 * @return the order_id
	 */
	public String getOrder_id() {
		return order_id;
	}

	/**
	 * @param order_id the order_id to set
	 */
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	/**
	 * @return the route_id
	 */
	public BigDecimal getRoute_id() {
		return route_id;
	}

	/**
	 * @param route_id the route_id to set
	 */
	public void setRoute_id(BigDecimal route_id) {
		this.route_id = route_id;
	}

	/**
	 * @return the route_name
	 */
	public String getRoute_name() {
		return route_name;
	}

	/**
	 * @param route_name the route_name to set
	 */
	public void setRoute_name(String route_name) {
		this.route_name = route_name;
	}

	/**
	 * @return the sort_id
	 */
	public String getSort_id() {
		return sort_id;
	}

	/**
	 * @param sort_id the sort_id to set
	 */
	public void setSort_id(String sort_id) {
		this.sort_id = sort_id;
	}

	/**
	 * @return the sort_schedule
	 */
	public BigDecimal getSort_schedule() {
		return sort_schedule;
	}

	/**
	 * @param sort_schedule the sort_schedule to set
	 */
	public void setSort_schedule(BigDecimal sort_schedule) {
		this.sort_schedule = sort_schedule;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the total_count
	 */
	public int getTotal_count() {
		return total_count;
	}

	/**
	 * @param total_count the total_count to set
	 */
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

}
