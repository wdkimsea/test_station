package cn.guanmai.station.bean.delivery;

import java.math.BigDecimal;

/* 
* @author liming 
* @date May 9, 2019 2:42:06 PM 
* @des 司机APP配送任务
* @version 1.0 
*/
public class DeliveryTaskBean {
	private String address;
	private String order_id;
	private String delivery_id;
	private String receive_begin_time;
	private String receive_end_time;
	private String pagination;
	private String resname;
	private String receiver_phone;
	private BigDecimal total_pay;
	private Integer pay_status;

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
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
	 * @return the delivery_id
	 */
	public String getDelivery_id() {
		return delivery_id;
	}

	/**
	 * @param delivery_id
	 *            the delivery_id to set
	 */
	public void setDelivery_id(String delivery_id) {
		this.delivery_id = delivery_id;
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
	 * @return the pagination
	 */
	public String getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            the pagination to set
	 */
	public void setPagination(String pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return the resname
	 */
	public String getResname() {
		return resname;
	}

	/**
	 * @param resname
	 *            the resname to set
	 */
	public void setResname(String resname) {
		this.resname = resname;
	}

	/**
	 * @return the receiver_phone
	 */
	public String getReceiver_phone() {
		return receiver_phone;
	}

	/**
	 * @param receiver_phone
	 *            the receiver_phone to set
	 */
	public void setReceiver_phone(String receiver_phone) {
		this.receiver_phone = receiver_phone;
	}

	/**
	 * @return the total_pay
	 */
	public BigDecimal getTotal_pay() {
		return total_pay.divide(new BigDecimal("100"));
	}

	/**
	 * @param total_pay
	 *            the total_pay to set
	 */
	public void setTotal_pay(BigDecimal total_pay) {
		this.total_pay = total_pay;
	}

	/**
	 * @return the pay_status
	 */
	public Integer getPay_status() {
		return pay_status;
	}

	/**
	 * @param pay_status
	 *            the pay_status to set
	 */
	public void setPay_status(Integer pay_status) {
		this.pay_status = pay_status;
	}

}
