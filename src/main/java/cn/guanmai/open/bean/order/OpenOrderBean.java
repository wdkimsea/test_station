package cn.guanmai.open.bean.order;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jun 4, 2019 4:58:06 PM 
* @des 接口 /order/list 对应的结果
* @version 1.0 
*/
public class OpenOrderBean {
	private String order_id;
	private BigDecimal freight;
	private int status;
	private int pay_status;
	private BigDecimal out_stock_amount;
	private int sort_id;
	private BigDecimal order_amount;
	private BigDecimal abnormal_sku_amount;
	private BigDecimal refund_sku_amount;
	private BigDecimal coupon_amount;
	private BigDecimal total_amount;
	private String date_time;
	private String receive_begin_time;
	private String receive_end_time;
	private String remark;

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

	public Customer customer;

	public class Customer {
		private String customer_id;

		private String customer_name;
		private String address;
		private String receiver_phone;
		private String receiver_name;

		public String getCustomer_id() {
			return customer_id;
		}

		public void setCustomer_id(String customer_id) {
			this.customer_id = customer_id;
		}

		public String getCustomer_name() {
			return customer_name;
		}

		public void setCustomer_name(String customer_name) {
			this.customer_name = customer_name;
		}

		public String getReceiver_phone() {
			return receiver_phone;
		}

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
		 * @return the receiver_name
		 */
		public String getReceiver_name() {
			return receiver_name;
		}

		/**
		 * @param receiver_name
		 *            the receiver_name to set
		 */
		public void setReceiver_name(String receiver_name) {
			this.receiver_name = receiver_name;
		}
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
	 * @return the freight
	 */
	public BigDecimal getFreight() {
		return freight;
	}

	/**
	 * @param freight
	 *            the freight to set
	 */
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
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
	 * @return the pay_status
	 */
	public int getPay_status() {
		return pay_status;
	}

	/**
	 * @param pay_status
	 *            the pay_status to set
	 */
	public void setPay_status(int pay_status) {
		this.pay_status = pay_status;
	}

	/**
	 * @return the out_stock_amount
	 */
	public BigDecimal getOut_stock_amount() {
		return out_stock_amount;
	}

	/**
	 * @param out_stock_amount
	 *            the out_stock_amount to set
	 */
	public void setOut_stock_amount(BigDecimal out_stock_amount) {
		this.out_stock_amount = out_stock_amount;
	}

	/**
	 * @return the order_amount
	 */
	public BigDecimal getOrder_amount() {
		return order_amount;
	}

	/**
	 * @param order_amount
	 *            the order_amount to set
	 */
	public void setOrder_amount(BigDecimal order_amount) {
		this.order_amount = order_amount;
	}

	/**
	 * @return the coupon_amount
	 */
	public BigDecimal getCoupon_amount() {
		return coupon_amount;
	}

	/**
	 * @param coupon_amount
	 *            the coupon_amount to set
	 */
	public void setCoupon_amount(BigDecimal coupon_amount) {
		this.coupon_amount = coupon_amount;
	}

	/**
	 * @return the total_amount
	 */
	public BigDecimal getTotal_amount() {
		return total_amount;
	}

	/**
	 * @param total_amount
	 *            the total_amount to set
	 */
	public void setTotal_amount(BigDecimal total_amount) {
		this.total_amount = total_amount;
	}

	/**
	 * @return the sort_id
	 */
	public int getSort_id() {
		return sort_id;
	}

	/**
	 * @param sort_id
	 *            the sort_id to set
	 */
	public void setSort_id(int sort_id) {
		this.sort_id = sort_id;
	}

	/**
	 * @return the abnormal_sku_amount
	 */
	public BigDecimal getAbnormal_sku_amount() {
		return abnormal_sku_amount;
	}

	/**
	 * @param abnormal_sku_amount
	 *            the abnormal_sku_amount to set
	 */
	public void setAbnormal_sku_amount(BigDecimal abnormal_sku_amount) {
		this.abnormal_sku_amount = abnormal_sku_amount;
	}

	/**
	 * @return the refund_sku_amount
	 */
	public BigDecimal getRefund_sku_amount() {
		return refund_sku_amount;
	}

	/**
	 * @param refund_sku_amount
	 *            the refund_sku_amount to set
	 */
	public void setRefund_sku_amount(BigDecimal refund_sku_amount) {
		this.refund_sku_amount = refund_sku_amount;
	}

	/**
	 * @return the date_time
	 */
	public String getDate_time() {
		return date_time;
	}

	/**
	 * @param date_time
	 *            the date_time to set
	 */
	public void setDate_time(String date_time) {
		this.date_time = date_time;
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
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
