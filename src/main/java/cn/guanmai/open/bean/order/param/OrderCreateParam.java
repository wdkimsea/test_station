package cn.guanmai.open.bean.order.param;

import java.util.List;

/* 
* @author liming 
* @date Jun 4, 2019 2:44:52 PM 
* @des 接口 /order/create 对应的参数
* @version 1.0 
*/
public class OrderCreateParam {
	private String customer_id;
	private String time_config_id;
	private String receive_begin_time;
	private String receive_end_time;
	private String remark;
	private String customer_address;
	private List<OrderProductParam> products;

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
	 * @return the time_config_id
	 */
	public String getTime_config_id() {
		return time_config_id;
	}

	/**
	 * @param time_config_id
	 *            the time_config_id to set
	 */
	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
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
	 * @return the customer_address
	 */
	public String getCustomer_address() {
		return customer_address;
	}

	/**
	 * @param customer_address
	 *            the customer_address to set
	 */
	public void setCustomer_address(String customer_address) {
		this.customer_address = customer_address;
	}

	/**
	 * @return the products
	 */
	public List<OrderProductParam> getProducts() {
		return products;
	}

	/**
	 * @param products
	 *            the products to set
	 */
	public void setProducts(List<OrderProductParam> products) {
		this.products = products;
	}

}
