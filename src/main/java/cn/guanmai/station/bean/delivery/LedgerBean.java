package cn.guanmai.station.bean.delivery;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Jun 18, 2019 4:47:37 PM 
* @des 套账单  /delivery/get 接口对应的返回结果
* @version 1.0 
*/
public class LedgerBean {
	private String id;
	private String resname;
	private BigDecimal total_price;
	private BigDecimal real_price;
	private BigDecimal total_pay;
	private String receiver_name;
	private String remark;
	private String address;
	private String delivery_id;
	private String order_time;
	private String receive_begin_time;
	private String receive_end_time;
	private BigDecimal freight;
	private Integer sync_add_sku;
	private Integer sync_del_order;
	private Integer sync_del_sku;
	private List<Detail> details;

	public class Detail {
		private String category_title_1;
		@JSONField(name="id")
		private String sku_id;
		@JSONField(name="name")
		private String sku_name;
		private BigDecimal quantity;
		private BigDecimal quantity_lock;
		private BigDecimal raw_id;
		private BigDecimal real_item_price;
		private BigDecimal real_weight;
		private BigDecimal real_weight_lock;
		private BigDecimal sale_price;
		private BigDecimal sale_price_lock;
		private BigDecimal sale_ratio;
		private String sale_unit_name;
		private String std_unit_name;

		/**
		 * @return the category_title_1
		 */
		public String getCategory_title_1() {
			return category_title_1;
		}

		/**
		 * @param category_title_1
		 *            the category_title_1 to set
		 */
		public void setCategory_title_1(String category_title_1) {
			this.category_title_1 = category_title_1;
		}

		/**
		 * @return the sku_id
		 */
		public String getSku_id() {
			return sku_id;
		}

		/**
		 * @param sku_id
		 *            the sku_id to set
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
		 * @param sku_name
		 *            the sku_name to set
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
		 * @param quantity
		 *            the quantity to set
		 */
		public void setQuantity(BigDecimal quantity) {
			this.quantity = quantity;
		}

		/**
		 * @return the quantity_lock
		 */
		public BigDecimal getQuantity_lock() {
			return quantity_lock;
		}

		/**
		 * @param quantity_lock
		 *            the quantity_lock to set
		 */
		public void setQuantity_lock(BigDecimal quantity_lock) {
			this.quantity_lock = quantity_lock;
		}

		/**
		 * @return the raw_id
		 */
		public BigDecimal getRaw_id() {
			return raw_id;
		}

		/**
		 * @param raw_id
		 *            the raw_id to set
		 */
		public void setRaw_id(BigDecimal raw_id) {
			this.raw_id = raw_id;
		}

		/**
		 * @return the real_item_price
		 */
		public BigDecimal getReal_item_price() {
			return real_item_price;
		}

		/**
		 * @param real_item_price
		 *            the real_item_price to set
		 */
		public void setReal_item_price(BigDecimal real_item_price) {
			this.real_item_price = real_item_price;
		}

		/**
		 * @return the real_weight
		 */
		public BigDecimal getReal_weight() {
			return real_weight;
		}

		/**
		 * @param real_weight
		 *            the real_weight to set
		 */
		public void setReal_weight(BigDecimal real_weight) {
			this.real_weight = real_weight;
		}

		/**
		 * @return the real_weight_lock
		 */
		public BigDecimal getReal_weight_lock() {
			return real_weight_lock;
		}

		/**
		 * @param real_weight_lock
		 *            the real_weight_lock to set
		 */
		public void setReal_weight_lock(BigDecimal real_weight_lock) {
			this.real_weight_lock = real_weight_lock;
		}

		/**
		 * @return the sale_price
		 */
		public BigDecimal getSale_price() {
			return sale_price;
		}

		/**
		 * @param sale_price
		 *            the sale_price to set
		 */
		public void setSale_price(BigDecimal sale_price) {
			this.sale_price = sale_price;
		}

		/**
		 * @return the sale_price_lock
		 */
		public BigDecimal getSale_price_lock() {
			return sale_price_lock;
		}

		/**
		 * @param sale_price_lock
		 *            the sale_price_lock to set
		 */
		public void setSale_price_lock(BigDecimal sale_price_lock) {
			this.sale_price_lock = sale_price_lock;
		}

		/**
		 * @return the sale_ratio
		 */
		public BigDecimal getSale_ratio() {
			return sale_ratio;
		}

		/**
		 * @param sale_ratio
		 *            the sale_ratio to set
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
		 * @param sale_unit_name
		 *            the sale_unit_name to set
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
		 * @param std_unit_name
		 *            the std_unit_name to set
		 */
		public void setStd_unit_name(String std_unit_name) {
			this.std_unit_name = std_unit_name;
		}
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the real_price
	 */
	public BigDecimal getReal_price() {
		return real_price;
	}

	/**
	 * @param real_price
	 *            the real_price to set
	 */
	public void setReal_price(BigDecimal real_price) {
		this.real_price = real_price;
	}

	/**
	 * @return the total_pay
	 */
	public BigDecimal getTotal_pay() {
		return total_pay;
	}

	/**
	 * @param total_pay
	 *            the total_pay to set
	 */
	public void setTotal_pay(BigDecimal total_pay) {
		this.total_pay = total_pay;
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
	 * @return the order_time
	 */
	public String getOrder_time() {
		return order_time;
	}

	/**
	 * @param order_time
	 *            the order_time to set
	 */
	public void setOrder_time(String order_time) {
		this.order_time = order_time;
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
	 * @return the sync_add_sku
	 */
	public Integer getSync_add_sku() {
		return sync_add_sku;
	}

	/**
	 * @param sync_add_sku
	 *            the sync_add_sku to set
	 */
	public void setSync_add_sku(Integer sync_add_sku) {
		this.sync_add_sku = sync_add_sku;
	}

	/**
	 * @return the sync_del_order
	 */
	public Integer getSync_del_order() {
		return sync_del_order;
	}

	/**
	 * @param sync_del_order
	 *            the sync_del_order to set
	 */
	public void setSync_del_order(Integer sync_del_order) {
		this.sync_del_order = sync_del_order;
	}

	/**
	 * @return the sync_del_sku
	 */
	public Integer getSync_del_sku() {
		return sync_del_sku;
	}

	/**
	 * @param sync_del_sku
	 *            the sync_del_sku to set
	 */
	public void setSync_del_sku(Integer sync_del_sku) {
		this.sync_del_sku = sync_del_sku;
	}

	/**
	 * @return the details
	 */
	public List<Detail> getDetails() {
		return details;
	}

	/**
	 * @param details
	 *            the details to set
	 */
	public void setDetails(List<Detail> details) {
		this.details = details;
	}
}
