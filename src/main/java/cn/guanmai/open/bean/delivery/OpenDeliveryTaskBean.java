package cn.guanmai.open.bean.delivery;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jun 5, 2019 2:20:26 PM 
* @des 接口 /delivery/task/list 对应结果
* @version 1.0 
*/
public class OpenDeliveryTaskBean {
	private String order_id;
	private String sort_id;
	private String customer_id;
	private String customer_name;
	private String receive_address;
	private String area;
	private String receive_begin_time;
	private String receive_end_time;
	@SerializedName("order_amount")
	private BigDecimal total_price;
	private int order_pay_method;
	private int status;
	private String driver_id;
	private int driver_status;
	private String driver_name;
	private String carrier_id;
	private String route_id;
	private String route_name;

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
	 * @return the sort_id
	 */
	public String getSort_id() {
		return sort_id;
	}

	/**
	 * @param sort_id
	 *            the sort_id to set
	 */
	public void setSort_id(String sort_id) {
		this.sort_id = sort_id;
	}

	/**
	 * @return the customer_id
	 */
	public String getCustomer_id() {
		return customer_id;
	}

	/**
	 * @param customer_id
	 *            the customer_id to set
	 */
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	/**
	 * @return the customer_name
	 */
	public String getCustomer_name() {
		return customer_name;
	}

	/**
	 * @param customer_name
	 *            the customer_name to set
	 */
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	/**
	 * @return the receive_address
	 */
	public String getReceive_address() {
		return receive_address;
	}

	/**
	 * @param receive_address
	 *            the receive_address to set
	 */
	public void setReceive_address(String receive_address) {
		this.receive_address = receive_address;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param area
	 *            the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the receive_begin_time
	 */
	public String getReceive_begin_time() {
		return receive_begin_time;
	}

	/**
	 * @param receive_begin_time
	 *            the receive_begin_time to set
	 */
	public void setReceive_begin_time(String receive_begin_time) {
		this.receive_begin_time = receive_begin_time;
	}

	/**
	 * @return the receive_end_time
	 */
	public String getReceive_end_time() {
		return receive_end_time;
	}

	/**
	 * @param receive_end_time
	 *            the receive_end_time to set
	 */
	public void setReceive_end_time(String receive_end_time) {
		this.receive_end_time = receive_end_time;
	}

	/**
	 * @return the total_price
	 */
	public BigDecimal getTotal_price() {
		return total_price;
	}

	/**
	 * @param total_price
	 *            the total_price to set
	 */
	public void setTotal_price(BigDecimal total_price) {
		this.total_price = total_price;
	}

	/**
	 * @return the order_pay_method
	 */
	public int getOrder_pay_method() {
		return order_pay_method;
	}

	/**
	 * @param order_pay_method
	 *            the order_pay_method to set
	 */
	public void setOrder_pay_method(int order_pay_method) {
		this.order_pay_method = order_pay_method;
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

	/**
	 * @return the driver_id
	 */
	public String getDriver_id() {
		return driver_id;
	}

	/**
	 * @param driver_id
	 *            the driver_id to set
	 */
	public void setDriver_id(String driver_id) {
		this.driver_id = driver_id;
	}

	/**
	 * @return the driver_status
	 */
	public int getDriver_status() {
		return driver_status;
	}

	/**
	 * @param driver_status
	 *            the driver_status to set
	 */
	public void setDriver_status(int driver_status) {
		this.driver_status = driver_status;
	}

	/**
	 * @return the driver_name
	 */
	public String getDriver_name() {
		return driver_name;
	}

	/**
	 * @param driver_name
	 *            the driver_name to set
	 */
	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}

	/**
	 * @return the carrier_id
	 */
	public String getCarrier_id() {
		return carrier_id;
	}

	/**
	 * @param carrier_id
	 *            the carrier_id to set
	 */
	public void setCarrier_id(String carrier_id) {
		this.carrier_id = carrier_id;
	}

	/**
	 * @return the route_id
	 */
	public String getRoute_id() {
		return route_id;
	}

	/**
	 * @param route_id
	 *            the route_id to set
	 */
	public void setRoute_id(String route_id) {
		this.route_id = route_id;
	}

	/**
	 * @return the route_name
	 */
	public String getRoute_name() {
		return route_name;
	}

	/**
	 * @param route_name
	 *            the route_name to set
	 */
	public void setRoute_name(String route_name) {
		this.route_name = route_name;
	}

}
