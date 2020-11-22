package cn.guanmai.station.bean.weight;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2019年7月31日 下午6:24:10
 * @des
 * @version 1.0
 */
public class PdaOrderDetailBean {
	private String order_id;
	private String address_id;
	private String address_name;
	private String driver_id;
	private String driver_name;
	private int finish_count;
	private String sort_id;
	private BigDecimal sort_schedule;
	private int status;
	private int total_count;

	private List<Detail> details;

	public class Detail {
		private String sku_id;
		private String sku_name;
		private String std_unit_name;
		private String unit_name;

		private BigDecimal sale_ratio;
		private BigDecimal quantity;
		private BigDecimal std_quantity;
		private BigDecimal weighting_quantity;

		private int real_is_weight;
		private int sort_status;
		private int sort_schedule;

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
		 * @return the unit_name
		 */
		public String getUnit_name() {
			return unit_name;
		}

		/**
		 * @param unit_name the unit_name to set
		 */
		public void setUnit_name(String unit_name) {
			this.unit_name = unit_name;
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
		 * @return the std_quantity
		 */
		public BigDecimal getStd_quantity() {
			return std_quantity;
		}

		/**
		 * @param std_quantity the std_quantity to set
		 */
		public void setStd_quantity(BigDecimal std_quantity) {
			this.std_quantity = std_quantity;
		}

		/**
		 * @return the weighting_quantity
		 */
		public BigDecimal getWeighting_quantity() {
			return weighting_quantity;
		}

		/**
		 * @param weighting_quantity the weighting_quantity to set
		 */
		public void setWeighting_quantity(BigDecimal weighting_quantity) {
			this.weighting_quantity = weighting_quantity;
		}

		/**
		 * @return the real_is_weight
		 */
		public int getReal_is_weight() {
			return real_is_weight;
		}

		/**
		 * @param real_is_weight the real_is_weight to set
		 */
		public void setReal_is_weight(int real_is_weight) {
			this.real_is_weight = real_is_weight;
		}

		/**
		 * @return the sort_status
		 */
		public int getSort_status() {
			return sort_status;
		}

		/**
		 * @param sort_status the sort_status to set
		 */
		public void setSort_status(int sort_status) {
			this.sort_status = sort_status;
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
	 * @return the address_name
	 */
	public String getAddress_name() {
		return address_name;
	}

	/**
	 * @param address_name the address_name to set
	 */
	public void setAddress_name(String address_name) {
		this.address_name = address_name;
	}

	/**
	 * @return the driver_id
	 */
	public String getDriver_id() {
		return driver_id;
	}

	/**
	 * @param driver_id the driver_id to set
	 */
	public void setDriver_id(String driver_id) {
		this.driver_id = driver_id;
	}

	/**
	 * @return the driver_name
	 */
	public String getDriver_name() {
		return driver_name;
	}

	/**
	 * @param driver_name the driver_name to set
	 */
	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}

	/**
	 * @return the finish_count
	 */
	public int getFinish_count() {
		return finish_count;
	}

	/**
	 * @param finish_count the finish_count to set
	 */
	public void setFinish_count(int finish_count) {
		this.finish_count = finish_count;
	}

	public String getSort_id() {
		return sort_id;
	}

	public void setSort_id(String sort_id) {
		this.sort_id = sort_id;
	}

	/**
	 * @return the sort_schedule
	 */
	public BigDecimal getSort_schedule() {
		return sort_schedule;
	}

	/**
	 * @param sort_schedule the sort_schedule to set
	 */
	public void setSort_schedule(BigDecimal sort_schedule) {
		this.sort_schedule = sort_schedule;
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
	 * @return the total_count
	 */
	public int getTotal_count() {
		return total_count;
	}

	/**
	 * @param total_count the total_count to set
	 */
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
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

}
