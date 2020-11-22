package cn.guanmai.station.bean.order.param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/* 
* @author liming 
* @date Nov 12, 2018 5:02:13 PM 
* @todo TODO
* @version 1.0 
*/
public class OrderCreateParam {
	private Map<String, CombineGoods> combine_goods_map;
	private List<OrderSku> details;
	private String address_id;
	private String uid;
	private String receive_begin_time;
	private String receive_end_time;
	private String time_config_id;
	private String remark;
	private String address;
	private Integer force;

	// 补录订单特有参数
	private String date_time;

	public List<OrderSku> getDetails() {
		return details;
	}

	public void setDetails(List<OrderSku> details) {
		this.details = details;
	}

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
	 * @return the force
	 */
	public Integer getForce() {
		return force;
	}

	/**
	 * @param force the force to set
	 */
	public void setForce(Integer force) {
		this.force = force;
	}

	public Map<String, CombineGoods> getCombine_goods_map() {
		return combine_goods_map;
	}

	public void setCombine_goods_map(Map<String, CombineGoods> combine_goods_map) {
		this.combine_goods_map = combine_goods_map;
	}

	public String getDate_time() {
		return date_time;
	}

	/**
	 * 补录订单特有的参数
	 * 
	 * @param date_time
	 */
	public void setDate_time(String date_time) {
		this.date_time = date_time;
	}

	public class OrderSku {
		private String sku_id;
		private BigDecimal amount;
		private BigDecimal unit_price;
		private String spu_remark;
		private String spu_id;
		private int is_price_timing;
		private BigDecimal fake_quantity;
		private boolean is_combine_goods;
		private String combine_goods_id;
		private String salemenu_id;

		public String getSku_id() {
			return sku_id;
		}

		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

		public BigDecimal getUnit_price() {
			return unit_price;
		}

		public void setUnit_price(BigDecimal unit_price) {
			this.unit_price = unit_price;
		}

		public String getSpu_remark() {
			return spu_remark;
		}

		public void setSpu_remark(String spu_remark) {
			this.spu_remark = spu_remark;
		}

		public String getSpu_id() {
			return spu_id;
		}

		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		public int getIs_price_timing() {
			return is_price_timing;
		}

		public void setIs_price_timing(int is_price_timing) {
			this.is_price_timing = is_price_timing;
		}

		public BigDecimal getFake_quantity() {
			return fake_quantity;
		}

		public void setFake_quantity(BigDecimal fake_quantity) {
			this.fake_quantity = fake_quantity;
		}

		public boolean isIs_combine_goods() {
			return is_combine_goods;
		}

		public void setIs_combine_goods(boolean is_combine_goods) {
			this.is_combine_goods = is_combine_goods;
		}

		public String getCombine_goods_id() {
			return combine_goods_id;
		}

		public void setCombine_goods_id(String combine_goods_id) {
			this.combine_goods_id = combine_goods_id;
		}

		public String getSalemenu_id() {
			return salemenu_id;
		}

		public void setSalemenu_id(String salemenu_id) {
			this.salemenu_id = salemenu_id;
		}

		public OrderSku() {
			super();
		}

	}

	public class CombineGoods {
		private BigDecimal quantity;
		private BigDecimal fake_quantity;
		private String name;
		private String imgs;
		private String sale_unit_name;
		private Map<String, BigDecimal> skus_ratio;
		private Map<String, BigDecimal> real;

		public BigDecimal getQuantity() {
			return quantity;
		}

		public void setQuantity(BigDecimal quantity) {
			this.quantity = quantity;
		}

		public BigDecimal getFake_quantity() {
			return fake_quantity;
		}

		public void setFake_quantity(BigDecimal fake_quantity) {
			this.fake_quantity = fake_quantity;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getImgs() {
			return imgs;
		}

		public void setImgs(String imgs) {
			this.imgs = imgs;
		}

		public String getSale_unit_name() {
			return sale_unit_name;
		}

		public void setSale_unit_name(String sale_unit_name) {
			this.sale_unit_name = sale_unit_name;
		}

		public Map<String, BigDecimal> getSkus_ratio() {
			return skus_ratio;
		}

		public void setSkus_ratio(Map<String, BigDecimal> skus_ratio) {
			this.skus_ratio = skus_ratio;
		}

		public Map<String, BigDecimal> getReal() {
			return real;
		}

		public void setReal(Map<String, BigDecimal> real) {
			this.real = real;
		}

		public CombineGoods() {
			super();
		}
	}

	public OrderCreateParam() {
		super();
	}

}
