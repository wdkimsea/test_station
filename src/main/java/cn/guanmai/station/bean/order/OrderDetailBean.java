package cn.guanmai.station.bean.order;

import com.google.gson.annotations.JsonAdapter;

import cn.guanmai.util.BooleanTypeAdapter;
import cn.guanmai.util.IntTypeAdapter;
import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Nov 13, 2018 7:02:59 PM 
* @des 订单详细
* @version 1.0 
*/
public class OrderDetailBean {
	@JSONField(name = "id", alternateNames = { "_id" })
	private String id;

	private BigDecimal total_price;

	private BigDecimal total_pay;

	private BigDecimal real_price;

	private BigDecimal abnormal_money;

	private BigDecimal refund_money;

	private List<Detail> details;

	// 订单异常信息
	private List<Abnormal> abnormals;

	// 订单退货信息
	private List<Refund> refunds;

	private Customer customer;

	private String time_config_id;

	// 订单配送费
	private BigDecimal freight;

	// 支付状态
	private Integer pay_status;

	// 订单状态
	private Integer status;

	private String remark;

	private List<String> sort_skus;

	private String create_time;

	public class Detail {
		@JSONField(name = "id")
		private String sku_id;
		private BigDecimal detail_id;
		@JSONField(name = "name")
		private String sku_name;
		private String spu_id;
		private String spu_remark;
		private String salemenu_id;
		private String category_title_1;
		private String category_title_2;
		private String sale_unit_name;
		private String std_unit_name;
		private String std_unit_name_forsale;

		@JsonAdapter(BooleanTypeAdapter.class)
		private boolean is_price_timing;
		@JsonAdapter(BooleanTypeAdapter.class)
		private boolean out_of_stock;
		@JsonAdapter(BooleanTypeAdapter.class)
		private boolean clean_food;

		@JsonAdapter(IntTypeAdapter.class)
		private int weighted;
		@JsonAdapter(IntTypeAdapter.class)
		private int is_weigh;
		@JsonAdapter(IntTypeAdapter.class)
		private int sort_way;

		private BigDecimal quantity;
		private BigDecimal fake_quantity;
		private BigDecimal std_unit_quantity;
		// private BigDecimal std_sale_price;
		private BigDecimal std_sale_price_forsale;
		private BigDecimal lock_price;
		private BigDecimal sale_ratio;
		private BigDecimal real_quantity;
		private BigDecimal std_real_quantity;
		// SKU销售金额,单位为元
		private BigDecimal sale_price;
		// SKU金额,单位为分
		private BigDecimal sale_money;

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
		 * @return the salemenu_id
		 */
		public String getSalemenu_id() {
			return salemenu_id;
		}

		/**
		 * @param salemenu_id the salemenu_id to set
		 */
		public void setSalemenu_id(String salemenu_id) {
			this.salemenu_id = salemenu_id;
		}

		/**
		 * @return the is_price_timing
		 */
		public boolean isIs_price_timing() {
			return is_price_timing;
		}

		/**
		 * @param is_price_timing the is_price_timing to set
		 */
		public void setIs_price_timing(boolean is_price_timing) {
			this.is_price_timing = is_price_timing;
		}

		/**
		 * @return the out_of_stock
		 */
		public boolean isOut_of_stock() {
			return out_of_stock;
		}

		/**
		 * @param out_of_stock the out_of_stock to set
		 */
		public void setOut_of_stock(boolean out_of_stock) {
			this.out_of_stock = out_of_stock;
		}

		public boolean isClean_food() {
			return clean_food;
		}

		public void setClean_food(boolean clean_food) {
			this.clean_food = clean_food;
		}

		/**
		 * @return the weighted
		 */
		public int getWeighted() {
			return weighted;
		}

		/**
		 * @param weighted the weighted to set
		 */
		public void setWeighted(int weighted) {
			this.weighted = weighted;
		}

		/**
		 * @return the is_weigh
		 */
		public int getIs_weigh() {
			return is_weigh;
		}

		/**
		 * @param is_weigh the is_weigh to set
		 */
		public void setIs_weigh(int is_weigh) {
			this.is_weigh = is_weigh;
		}

		/**
		 * @return the sort_way
		 */
		public int getSort_way() {
			return sort_way;
		}

		/**
		 * @param sort_way the sort_way to set
		 */
		public void setSort_way(int sort_way) {
			this.sort_way = sort_way;
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

		public BigDecimal getFake_quantity() {
			return fake_quantity;
		}

		public void setFake_quantity(BigDecimal fake_quantity) {
			this.fake_quantity = fake_quantity;
		}

		/**
		 * @return the std_unit_quantity
		 */
		public BigDecimal getStd_unit_quantity() {
			return std_unit_quantity;
		}

		/**
		 * @param std_unit_quantity the std_unit_quantity to set
		 */
		public void setStd_unit_quantity(BigDecimal std_unit_quantity) {
			this.std_unit_quantity = std_unit_quantity;
		}

		public BigDecimal getStd_sale_price_forsale() {
			return std_sale_price_forsale.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
		}

		public void setStd_sale_price_forsale(BigDecimal std_sale_price_forsale) {
			this.std_sale_price_forsale = std_sale_price_forsale;
		}

		public BigDecimal getLock_price() {
			return lock_price;
		}

		public void setLock_price(BigDecimal lock_price) {
			this.lock_price = lock_price;
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
		 * @return the std_real_quantity
		 */
		public BigDecimal getStd_real_quantity() {
			return std_real_quantity;
		}

		/**
		 * @param std_real_quantity the std_real_quantity to set
		 */
		public void setStd_real_quantity(BigDecimal std_real_quantity) {
			this.std_real_quantity = std_real_quantity;
		}

		/**
		 * SKU销售金额,单位为元
		 * 
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
		 * SKU金额,单位为分
		 * 
		 * @return the sale_money
		 */
		public BigDecimal getSale_money() {
			return sale_money;
		}

		/**
		 * @param sale_money the sale_money to set
		 */
		public void setSale_money(BigDecimal sale_money) {
			this.sale_money = sale_money;
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
		 * @return the category_title_1
		 */
		public String getCategory_title_1() {
			return category_title_1;
		}

		/**
		 * @param category_title_1 the category_title_1 to set
		 */
		public void setCategory_title_1(String category_title_1) {
			this.category_title_1 = category_title_1;
		}

		/**
		 * @return the category_title_2
		 */
		public String getCategory_title_2() {
			return category_title_2;
		}

		/**
		 * @param category_title_2 the category_title_2 to set
		 */
		public void setCategory_title_2(String category_title_2) {
			this.category_title_2 = category_title_2;
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

		public String getStd_unit_name_forsale() {
			return std_unit_name_forsale;
		}

		public void setStd_unit_name_forsale(String std_unit_name_forsale) {
			this.std_unit_name_forsale = std_unit_name_forsale;
		}

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
	 * @return the total_price
	 */
	public BigDecimal getTotal_price() {
		return total_price.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @param total_price the total_price to set
	 */
	public void setTotal_price(BigDecimal total_price) {
		this.total_price = total_price;
	}

	/**
	 * @return the total_pay
	 */
	public BigDecimal getTotal_pay() {
		return total_pay.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @param total_pay the total_pay to set
	 */
	public void setTotal_pay(BigDecimal total_pay) {
		this.total_pay = total_pay;
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
	 * @return the abnormal_money
	 */
	public BigDecimal getAbnormal_money() {
		return abnormal_money.divide(new BigDecimal("100"));
	}

	/**
	 * @param abnormal_money the abnormal_money to set
	 */
	public void setAbnormal_money(BigDecimal abnormal_money) {
		this.abnormal_money = abnormal_money;
	}

	/**
	 * @return the refund_money
	 */
	public BigDecimal getRefund_money() {
		return refund_money.divide(new BigDecimal("100"));
	}

	/**
	 * @param refund_money the refund_money to set
	 */
	public void setRefund_money(BigDecimal refund_money) {
		this.refund_money = refund_money;
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
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
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

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the time_config_id
	 */
	public String getTime_config_id() {
		return time_config_id;
	}

	/**
	 * @param time_config_id the time_config_id to set
	 */
	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	/**
	 * @return the freight
	 */
	public BigDecimal getFreight() {
		return freight.divide(new BigDecimal("100"));
	}

	/**
	 * @param freight the freight to set
	 */
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
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

	public class Abnormal {
		private String detail_id;
		private String sku_id;
		private String order_detail_id;
		// private BigDecimal final_amount;
		private BigDecimal final_amount_forsale;
		private BigDecimal id;
		private BigDecimal money_delta;
		private String text;
		private int type_id;
		private String type_text;

		/**
		 * @return the detail_id
		 */
		public String getDetail_id() {
			return detail_id;
		}

		/**
		 * @param detail_id the detail_id to set
		 */
		public void setDetail_id(String detail_id) {
			this.detail_id = detail_id;
		}

		public String getSku_id() {
			return sku_id;
		}

		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		public String getOrder_detail_id() {
			return order_detail_id;
		}

		public void setOrder_detail_id(String order_detail_id) {
			this.order_detail_id = order_detail_id;
		}

		public BigDecimal getFinal_amount_forsale() {
			return final_amount_forsale;
		}

		public void setFinal_amount_forsale(BigDecimal final_amount_forsale) {
			this.final_amount_forsale = final_amount_forsale;
		}

		/**
		 * @return the id
		 */
		public BigDecimal getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(BigDecimal id) {
			this.id = id;
		}

		/**
		 * @return the money_delta
		 */
		public BigDecimal getMoney_delta() {
			return money_delta.divide(new BigDecimal("100"));
		}

		/**
		 * @param money_delta the money_delta to set
		 */
		public void setMoney_delta(BigDecimal money_delta) {
			this.money_delta = money_delta;
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
		 * @return the type_id
		 */
		public int getType_id() {
			return type_id;
		}

		/**
		 * @param type_id the type_id to set
		 */
		public void setType_id(int type_id) {
			this.type_id = type_id;
		}

		/**
		 * @return the type_text
		 */
		public String getType_text() {
			return type_text;
		}

		/**
		 * @param type_text the type_text to set
		 */
		public void setType_text(String type_text) {
			this.type_text = type_text;
		}
	}

	public class Refund {
		private String id;
		private String detail_id;
		private String sku_id;
		private String order_detail_id;
		private BigDecimal request_amount_forsale;
		private int state;

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
		 * @return the detail_id
		 */
		public String getDetail_id() {
			return detail_id;
		}

		/**
		 * @param detail_id the detail_id to set
		 */
		public void setDetail_id(String detail_id) {
			this.detail_id = detail_id;
		}

		public String getSku_id() {
			return sku_id;
		}

		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		public String getOrder_detail_id() {
			return order_detail_id;
		}

		public void setOrder_detail_id(String order_detail_id) {
			this.order_detail_id = order_detail_id;
		}

		public BigDecimal getRequest_amount_forsale() {
			return request_amount_forsale;
		}

		public void setRequest_amount_forsale(BigDecimal request_amount_forsale) {
			this.request_amount_forsale = request_amount_forsale;
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

	}

	public class Customer {
		private String address;
		private String uid;
		private String address_id;
		private String receive_begin_time;
		private String receive_end_time;
		private String receiver_phone;
		private String receive_way;
		private Extender extender;

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
		 * @return the uid
		 */
		public String getUid() {
			return uid;
		}

		/**
		 * @param uid the uid to set
		 */
		public void setUid(String uid) {
			this.uid = uid;
		}

		/**
		 * 商户ID,不带S
		 * 
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
		 * @return the receiver_phone
		 */
		public String getReceiver_phone() {
			return receiver_phone;
		}

		public String getReceive_way() {
			return receive_way;
		}

		public void setReceive_way(String receive_way) {
			this.receive_way = receive_way;
		}

		/**
		 * @param receiver_phone the receiver_phone to set
		 */
		public void setReceiver_phone(String receiver_phone) {
			this.receiver_phone = receiver_phone;
		}

		/**
		 * @return the extender
		 */
		public Extender getExtender() {
			return extender;
		}

		/**
		 * @param extender the extender to set
		 */
		public void setExtender(Extender extender) {
			this.extender = extender;
		}

		public class Extender {
			private String resname;

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
		}
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<String> getSort_skus() {
		return sort_skus;
	}

	public void setSort_skus(List<String> sort_skus) {
		this.sort_skus = sort_skus;
	}

	public String getCreate_time() {
		return create_time.replaceAll("T", " ");
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}
