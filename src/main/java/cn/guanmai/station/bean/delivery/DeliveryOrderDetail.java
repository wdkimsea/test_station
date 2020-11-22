package cn.guanmai.station.bean.delivery;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date May 21, 2019 10:45:03 AM 
* @des 配送订单详细信息
* @version 1.0 
*/
public class DeliveryOrderDetail {
	private BigDecimal abnormal_money;
	private String address;
	private BigDecimal coupon_amount;
	private String delivery_id;

	private List<Detail> details;

	public class Detail {
		private String sku_id;
		private BigDecimal detail_id;
		@JSONField(name = "name")
		private String sku_name;
		private BigDecimal quantity;
		private BigDecimal real_quantity;
		private BigDecimal sale_ratio;
		private String sale_unit_name;
		private String std_unit_name;
		private BigDecimal total_item_price;

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

		public BigDecimal getDetail_id() {
			return detail_id;
		}

		public void setDetail_id(BigDecimal detail_id) {
			this.detail_id = detail_id;
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
		 * @return the total_item_price
		 */
		public BigDecimal getTotal_item_price() {
			return total_item_price.divide(new BigDecimal("100"));
		}

		/**
		 * @param total_item_price the total_item_price to set
		 */
		public void setTotal_item_price(BigDecimal total_item_price) {
			this.total_item_price = total_item_price;
		}
	}

	private BigDecimal freight;

	private Integer inspect_status;
	private String kid;
	@JSONField(name = "order_no")
	private String order_id;
	private Integer pay_status;
	private BigDecimal real_price;
	private String receive_begin_time;
	private String receive_end_time;
	private String receiver_name;
	private String receiver_phone;
	private BigDecimal refund_money;
	private String resname;
	private String sid;
	private Integer status;
	private BigDecimal total_pay;
	private BigDecimal total_price;

	/**
	 * @return the abnormal_money
	 */
	public BigDecimal getAbnormal_money() {
		return abnormal_money;
	}

	/**
	 * @param abnormal_money the abnormal_money to set
	 */
	public void setAbnormal_money(BigDecimal abnormal_money) {
		this.abnormal_money = abnormal_money;
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
	 * @return the coupon_amount
	 */
	public BigDecimal getCoupon_amount() {
		return coupon_amount;
	}

	/**
	 * @param coupon_amount the coupon_amount to set
	 */
	public void setCoupon_amount(BigDecimal coupon_amount) {
		this.coupon_amount = coupon_amount;
	}

	/**
	 * @return the delivery_id
	 */
	public String getDelivery_id() {
		return delivery_id;
	}

	/**
	 * @param delivery_id the delivery_id to set
	 */
	public void setDelivery_id(String delivery_id) {
		this.delivery_id = delivery_id;
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
	 * @return the inspect_status
	 */
	public Integer getInspect_status() {
		return inspect_status;
	}

	/**
	 * @param inspect_status the inspect_status to set
	 */
	public void setInspect_status(Integer inspect_status) {
		this.inspect_status = inspect_status;
	}

	/**
	 * @return the kid
	 */
	public String getKid() {
		return kid;
	}

	/**
	 * @param kid the kid to set
	 */
	public void setKid(String kid) {
		this.kid = kid;
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
	 * @return the pay_status
	 */
	public Integer getPay_status() {
		return pay_status;
	}

	/**
	 * @param pay_status the pay_status to set
	 */
	public void setPay_status(Integer pay_status) {
		this.pay_status = pay_status;
	}

	/**
	 * @return the real_price
	 */
	public BigDecimal getReal_price() {
		return real_price.divide(new BigDecimal("100"));
	}

	/**
	 * @param real_price the real_price to set
	 */
	public void setReal_price(BigDecimal real_price) {
		this.real_price = real_price;
	}

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

	/**
	 * @return the receiver_phone
	 */
	public String getReceiver_phone() {
		return receiver_phone;
	}

	/**
	 * @param receiver_phone the receiver_phone to set
	 */
	public void setReceiver_phone(String receiver_phone) {
		this.receiver_phone = receiver_phone;
	}

	/**
	 * @return the refund_money
	 */
	public BigDecimal getRefund_money() {
		return refund_money;
	}

	/**
	 * @param refund_money the refund_money to set
	 */
	public void setRefund_money(BigDecimal refund_money) {
		this.refund_money = refund_money;
	}

	/**
	 * @return the resname
	 */
	public String getResname() {
		return resname;
	}

	/**
	 * @param resname the resname to set
	 */
	public void setResname(String resname) {
		this.resname = resname;
	}

	/**
	 * @return the sid
	 */
	public String getSid() {
		return sid;
	}

	/**
	 * @param sid the sid to set
	 */
	public void setSid(String sid) {
		this.sid = sid;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the total_pay
	 */
	public BigDecimal getTotal_pay() {
		return total_pay.divide(new BigDecimal("100"));
	}

	/**
	 * @param total_pay the total_pay to set
	 */
	public void setTotal_pay(BigDecimal total_pay) {
		this.total_pay = total_pay;
	}

	/**
	 * @return the total_price
	 */
	public BigDecimal getTotal_price() {
		return total_price.divide(new BigDecimal("100"));
	}

	/**
	 * @param total_price the total_price to set
	 */
	public void setTotal_price(BigDecimal total_price) {
		this.total_price = total_price;
	}

}
