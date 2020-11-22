package cn.guanmai.open.bean.order;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date Jun 4, 2019 4:24:20 PM 
* @des 接口 /order/get 对应的参数
* @version 1.0 
*/
public class OpenOrderDetailBean {
	private BigDecimal freight;
	private int status;
	private int pay_status;
	private BigDecimal out_stock_amount;
	private String sort_id;
	private BigDecimal order_amount;
	private BigDecimal total_amount;
	private String date_time;
	private String remark;
	private String receive_begin_time;
	private String receive_end_time;

	/**
	 * @return the receive_begin_time
	 */
	public String getReceive_begin_time() {
		return receive_begin_time;
	}

	/**
	 * @param receive_begin_time the receive_begin_time to set
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
	 * @param receive_end_time the receive_end_time to set
	 */
	public void setReceive_end_time(String receive_end_time) {
		this.receive_end_time = receive_end_time;
	}

	private Customer customer;

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
		 * @param address the address to set
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
		 * @param receiver_name the receiver_name to set
		 */
		public void setReceiver_name(String receiver_name) {
			this.receiver_name = receiver_name;
		}
	}

	public List<Detail> details;

	public class Detail {
		private String sku_id;
		private String sku_name;
		private String spu_id;
		private String category1_name;
		private String category2_name;
		private String std_unit_name;
		private String sale_unit_name;
		private BigDecimal total_amount;
		private BigDecimal real_quantity;
		private String spu_remark;
		private BigDecimal accept_quantity;
		private BigDecimal quantity;
		private BigDecimal sale_price;
		private BigDecimal sale_ratio;
		private boolean weighing;
		private boolean has_weighed;
		private boolean out_of_stock;
		private String salemenu_id;

		/**
		 * @return the sku_id
		 */
		public String getSku_id() {
			return sku_id;
		}

		/**
		 * @param sku_id the sku_id to set
		 */
		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		/**
		 * @return the sku_name
		 */
		public String getSku_name() {
			return sku_name;
		}

		/**
		 * @param sku_name the sku_name to set
		 */
		public void setSku_name(String sku_name) {
			this.sku_name = sku_name;
		}

		/**
		 * @return the spu_id
		 */
		public String getSpu_id() {
			return spu_id;
		}

		/**
		 * @param spu_id the spu_id to set
		 */
		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		public String getCategory1_name() {
			return category1_name;
		}

		public void setCategory1_name(String category1_name) {
			this.category1_name = category1_name;
		}

		public String getCategory2_name() {
			return category2_name;
		}

		public void setCategory2_name(String category2_name) {
			this.category2_name = category2_name;
		}

		/**
		 * @return the total_amount
		 */
		public BigDecimal getTotal_amount() {
			return total_amount;
		}

		/**
		 * @param total_amount the total_amount to set
		 */
		public void setTotal_amount(BigDecimal total_amount) {
			this.total_amount = total_amount;
		}

		public boolean isOut_of_stock() {
			return out_of_stock;
		}

		public void setOut_of_stock(boolean out_of_stock) {
			this.out_of_stock = out_of_stock;
		}

		public String getSalemenu_id() {
			return salemenu_id;
		}

		public void setSalemenu_id(String salemenu_id) {
			this.salemenu_id = salemenu_id;
		}

		/**
		 * @return the std_unit_name
		 */
		public String getStd_unit_name() {
			return std_unit_name;
		}

		/**
		 * @param std_unit_name the std_unit_name to set
		 */
		public void setStd_unit_name(String std_unit_name) {
			this.std_unit_name = std_unit_name;
		}

		/**
		 * @return the sale_unit_name
		 */
		public String getSale_unit_name() {
			return sale_unit_name;
		}

		/**
		 * @param sale_unit_name the sale_unit_name to set
		 */
		public void setSale_unit_name(String sale_unit_name) {
			this.sale_unit_name = sale_unit_name;
		}

		/**
		 * @return the real_quantity
		 */
		public BigDecimal getReal_quantity() {
			return real_quantity;
		}

		/**
		 * @param real_quantity the real_quantity to set
		 */
		public void setReal_quantity(BigDecimal real_quantity) {
			this.real_quantity = real_quantity;
		}

		/**
		 * @return the spu_remark
		 */
		public String getSpu_remark() {
			return spu_remark;
		}

		/**
		 * @param spu_remark the spu_remark to set
		 */
		public void setSpu_remark(String spu_remark) {
			this.spu_remark = spu_remark;
		}

		/**
		 * @return the accept_quantity
		 */
		public BigDecimal getAccept_quantity() {
			return accept_quantity;
		}

		/**
		 * @param accept_quantity the accept_quantity to set
		 */
		public void setAccept_quantity(BigDecimal accept_quantity) {
			this.accept_quantity = accept_quantity;
		}

		/**
		 * @return the quantity
		 */
		public BigDecimal getQuantity() {
			return quantity;
		}

		/**
		 * @param quantity the quantity to set
		 */
		public void setQuantity(BigDecimal quantity) {
			this.quantity = quantity;
		}

		/**
		 * @return the sale_price
		 */
		public BigDecimal getSale_price() {
			return sale_price;
		}

		/**
		 * @param sale_price the sale_price to set
		 */
		public void setSale_price(BigDecimal sale_price) {
			this.sale_price = sale_price;
		}

		/**
		 * @return the sale_ratio
		 */
		public BigDecimal getSale_ratio() {
			return sale_ratio;
		}

		/**
		 * @param sale_ratio the sale_ratio to set
		 */
		public void setSale_ratio(BigDecimal sale_ratio) {
			this.sale_ratio = sale_ratio;
		}

		/**
		 * @return the weighing
		 */
		public boolean isWeighing() {
			return weighing;
		}

		/**
		 * @param weighing the weighing to set
		 */
		public void setWeighing(boolean weighing) {
			this.weighing = weighing;
		}

		/**
		 * @return the has_weighed
		 */
		public boolean isHas_weighed() {
			return has_weighed;
		}

		/**
		 * @param has_weighed the has_weighed to set
		 */
		public void setHas_weighed(boolean has_weighed) {
			this.has_weighed = has_weighed;
		}

	}

	public List<Abnormal> abnormals;

	public class Abnormal {
		private BigDecimal final_count;
		private String sku_id;
		private String text;
		private String id;

		public int getException_reason() {
			return exception_reason;
		}

		public void setException_reason(int exception_reason) {
			this.exception_reason = exception_reason;
		}

		public String getException_reason_name() {
			return exception_reason_name;
		}

		public void setException_reason_name(String exception_reason_name) {
			this.exception_reason_name = exception_reason_name;
		}

		private int exception_reason;
		private BigDecimal delta_amount;
		private String exception_reason_name;

		/**
		 * @return the final_count
		 */
		public BigDecimal getFinal_count() {
			return final_count;
		}

		/**
		 * @param final_count the final_count to set
		 */
		public void setFinal_count(BigDecimal final_count) {
			this.final_count = final_count;
		}

		/**
		 * @return the sku_id
		 */
		public String getSku_id() {
			return sku_id;
		}

		/**
		 * @param sku_id the sku_id to set
		 */
		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		/**
		 * @return the text
		 */
		public String getText() {
			return text;
		}

		/**
		 * @param text the text to set
		 */
		public void setText(String text) {
			this.text = text;
		}

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @return the delta_amount
		 */
		public BigDecimal getDelta_amount() {
			return delta_amount;
		}

		/**
		 * @param delta_amount the delta_amount to set
		 */
		public void setDelta_amount(BigDecimal delta_amount) {
			this.delta_amount = delta_amount;
		}

	}

	public List<Refund> refunds;

	public class Refund {
		private String id;
		private String station_store_id;
		private String out_sku_id;
		private BigDecimal real_count;
		private String out_order_id;
		private BigDecimal request_count;
		private String sku_id;
		private int state;
		private int exception_reason;
		private String text;
		private String exception_reason_name;

		public int getException_reason() {
			return exception_reason;
		}

		public void setException_reason(int exception_reason) {
			this.exception_reason = exception_reason;
		}

		public String getException_reason_name() {
			return exception_reason_name;
		}

		public void setException_reason_name(String exception_reason_name) {
			this.exception_reason_name = exception_reason_name;
		}

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @return the station_store_id
		 */
		public String getStation_store_id() {
			return station_store_id;
		}

		/**
		 * @param station_store_id the station_store_id to set
		 */
		public void setStation_store_id(String station_store_id) {
			this.station_store_id = station_store_id;
		}

		/**
		 * @return the out_sku_id
		 */
		public String getOut_sku_id() {
			return out_sku_id;
		}

		/**
		 * @param out_sku_id the out_sku_id to set
		 */
		public void setOut_sku_id(String out_sku_id) {
			this.out_sku_id = out_sku_id;
		}

		/**
		 * @return the real_count
		 */
		public BigDecimal getReal_count() {
			return real_count;
		}

		/**
		 * @param real_count the real_count to set
		 */
		public void setReal_count(BigDecimal real_count) {
			this.real_count = real_count;
		}

		/**
		 * @return the out_order_id
		 */
		public String getOut_order_id() {
			return out_order_id;
		}

		/**
		 * @param out_order_id the out_order_id to set
		 */
		public void setOut_order_id(String out_order_id) {
			this.out_order_id = out_order_id;
		}

		/**
		 * @return the request_count
		 */
		public BigDecimal getRequest_count() {
			return request_count;
		}

		/**
		 * @param request_count the request_count to set
		 */
		public void setRequest_count(BigDecimal request_count) {
			this.request_count = request_count;
		}

		/**
		 * @return the sku_id
		 */
		public String getSku_id() {
			return sku_id;
		}

		/**
		 * @param sku_id the sku_id to set
		 */
		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		/**
		 * @return the state
		 */
		public int getState() {
			return state;
		}

		/**
		 * @param state the state to set
		 */
		public void setState(int state) {
			this.state = state;
		}

		/**
		 * @return the text
		 */
		public String getText() {
			return text;
		}

		/**
		 * @param text the text to set
		 */
		public void setText(String text) {
			this.text = text;
		}
	}

	/**
	 * @return the freight
	 */
	public BigDecimal getFreight() {
		return freight;
	}

	/**
	 * @param freight the freight to set
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
	 * @param status the status to set
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
	 * @param pay_status the pay_status to set
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
	 * @param out_stock_amount the out_stock_amount to set
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
	 * @param order_amount the order_amount to set
	 */
	public void setOrder_amount(BigDecimal order_amount) {
		this.order_amount = order_amount;
	}

	/**
	 * @return the total_amount
	 */
	public BigDecimal getTotal_amount() {
		return total_amount;
	}

	/**
	 * @param total_amount the total_amount to set
	 */
	public void setTotal_amount(BigDecimal total_amount) {
		this.total_amount = total_amount;
	}

	public String getSort_id() {
		return sort_id;
	}

	public void setSort_id(String sort_id) {
		this.sort_id = sort_id;
	}

	/**
	 * @return the date_time
	 */
	public String getDate_time() {
		return date_time;
	}

	/**
	 * @param date_time the date_time to set
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
	 * @param remark the remark to set
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
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the details
	 */
	public List<Detail> getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(List<Detail> details) {
		this.details = details;
	}

	/**
	 * @return the abnormals
	 */
	public List<Abnormal> getAbnormals() {
		return abnormals;
	}

	/**
	 * @param abnormals the abnormals to set
	 */
	public void setAbnormals(List<Abnormal> abnormals) {
		this.abnormals = abnormals;
	}

	/**
	 * @return the refunds
	 */
	public List<Refund> getRefunds() {
		return refunds;
	}

	/**
	 * @param refunds the refunds to set
	 */
	public void setRefunds(List<Refund> refunds) {
		this.refunds = refunds;
	}
}
