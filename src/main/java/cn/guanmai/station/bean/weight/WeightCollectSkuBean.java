package cn.guanmai.station.bean.weight;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author liming
 * @date 2019年7月8日 上午11:29:59
 * @des 接口 /weight/weight_collect/sku/list对应结果, ST-供应链-分拣-分拣明细-按商品分拣
 * @version 1.0
 */
public class WeightCollectSkuBean {
	private String category1_name;
	private String category2_name;
	private String category_id_1;
	private String category_id_2;
	private String pinlei_name;
	private String pinlei_id;
	private String salemenu_name;
	private String salemenu_id;
	@JSONField(name = "name")
	private String sku_name;
	private String sku_id;
	private int sort_schedule;

	private List<Order> orders;

	/**
	 * @return the category1_name
	 */
	public String getCategory1_name() {
		return category1_name;
	}

	/**
	 * @param category1_name the category1_name to set
	 */
	public void setCategory1_name(String category1_name) {
		this.category1_name = category1_name;
	}

	/**
	 * @return the category2_name
	 */
	public String getCategory2_name() {
		return category2_name;
	}

	/**
	 * @param category2_name the category2_name to set
	 */
	public void setCategory2_name(String category2_name) {
		this.category2_name = category2_name;
	}

	/**
	 * @return the category_id_1
	 */
	public String getCategory_id_1() {
		return category_id_1;
	}

	/**
	 * @param category_id_1 the category_id_1 to set
	 */
	public void setCategory_id_1(String category_id_1) {
		this.category_id_1 = category_id_1;
	}

	/**
	 * @return the category_id_2
	 */
	public String getCategory_id_2() {
		return category_id_2;
	}

	/**
	 * @param category_id_2 the category_id_2 to set
	 */
	public void setCategory_id_2(String category_id_2) {
		this.category_id_2 = category_id_2;
	}

	/**
	 * @return the pinlei_name
	 */
	public String getPinlei_name() {
		return pinlei_name;
	}

	/**
	 * @param pinlei_name the pinlei_name to set
	 */
	public void setPinlei_name(String pinlei_name) {
		this.pinlei_name = pinlei_name;
	}

	/**
	 * @return the pinlei_id
	 */
	public String getPinlei_id() {
		return pinlei_id;
	}

	/**
	 * @param pinlei_id the pinlei_id to set
	 */
	public void setPinlei_id(String pinlei_id) {
		this.pinlei_id = pinlei_id;
	}

	/**
	 * @return the salemenu_name
	 */
	public String getSalemenu_name() {
		return salemenu_name;
	}

	/**
	 * @param salemenu_name the salemenu_name to set
	 */
	public void setSalemenu_name(String salemenu_name) {
		this.salemenu_name = salemenu_name;
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
	 * @return the sort_schedule
	 */
	public int getSort_schedule() {
		return sort_schedule;
	}

	/**
	 * @param sort_schedule the sort_schedule to set
	 */
	public void setSort_schedule(int sort_schedule) {
		this.sort_schedule = sort_schedule;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public class Order {
		private String order_id;
		private String address_id;
		private String resname;
		private Integer is_weight;
		private Integer out_of_stock;
		private BigDecimal detail_id;
		private BigDecimal real_quantity;
		private BigDecimal std_real_quantity;
		private BigDecimal std_quantity;
		private BigDecimal weighting_quantity;

		private String std_unit_name;
		private String std_unit_name_forsale;
		private String sale_unit_name;

		public String getOrder_id() {
			return order_id;
		}

		public void setOrder_id(String order_id) {
			this.order_id = order_id;
		}

		public String getAddress_id() {
			return address_id;
		}

		public void setAddress_id(String address_id) {
			this.address_id = address_id;
		}

		public String getResname() {
			return resname;
		}

		public void setResname(String resname) {
			this.resname = resname;
		}

		public Integer getIs_weight() {
			return is_weight;
		}

		public void setIs_weight(Integer is_weight) {
			this.is_weight = is_weight;
		}

		public Integer getOut_of_stock() {
			return out_of_stock;
		}

		public void setOut_of_stock(Integer out_of_stock) {
			this.out_of_stock = out_of_stock;
		}

		public BigDecimal getDetail_id() {
			return detail_id;
		}

		public void setDetail_id(BigDecimal detail_id) {
			this.detail_id = detail_id;
		}

		public BigDecimal getReal_quantity() {
			return real_quantity;
		}

		public void setReal_quantity(BigDecimal real_quantity) {
			this.real_quantity = real_quantity;
		}

		public BigDecimal getStd_real_quantity() {
			return std_real_quantity;
		}

		public void setStd_real_quantity(BigDecimal std_real_quantity) {
			this.std_real_quantity = std_real_quantity;
		}

		public BigDecimal getStd_quantity() {
			return std_quantity;
		}

		public void setStd_quantity(BigDecimal std_quantity) {
			this.std_quantity = std_quantity;
		}

		public BigDecimal getWeighting_quantity() {
			return weighting_quantity;
		}

		public void setWeighting_quantity(BigDecimal weighting_quantity) {
			this.weighting_quantity = weighting_quantity;
		}

		public String getStd_unit_name() {
			return std_unit_name;
		}

		public void setStd_unit_name(String std_unit_name) {
			this.std_unit_name = std_unit_name;
		}

		public String getStd_unit_name_forsale() {
			return std_unit_name_forsale;
		}

		public void setStd_unit_name_forsale(String std_unit_name_forsale) {
			this.std_unit_name_forsale = std_unit_name_forsale;
		}

		public String getSale_unit_name() {
			return sale_unit_name;
		}

		public void setSale_unit_name(String sale_unit_name) {
			this.sale_unit_name = sale_unit_name;
		}
	}

}
