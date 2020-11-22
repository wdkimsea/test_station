package cn.guanmai.station.bean.weight;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2019年7月31日 下午4:12:27
 * @des
 * @version 1.0
 */
public class PdaWeightSkuDetailBean {
	private String sku_name;
	private String std_unit_name;
	private String unit_name;
	private BigDecimal sale_ratio;
	private int total_count;
	private int finish_count;

	private List<Order> orders;

	public class Order {
		private String order_id;
		private String address_id;
		private String sku_id;
		private String sku_name;
		private String std_unit_name;
		private String sale_unit_name;
		private String driver_name;
		private String driver_id;
		private String route_name;

		private BigDecimal sale_ratio;
		private BigDecimal std_quantity;
		private BigDecimal quantity;
		private BigDecimal weighting_quantity;
		private BigDecimal route_id;

		private int sort_status;
		private int status;

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
		 * @return the address_id
		 */
		public String getAddress_id() {
			return address_id;
		}

		/**
		 * @param address_id
		 *            the address_id to set
		 */
		public void setAddress_id(String address_id) {
			this.address_id = address_id;
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
		 * @return the driver_name
		 */
		public String getDriver_name() {
			return driver_name;
		}

		/**
		 * @param driver_name
		 *            the driver_name to set
		 */
		public void setDriver_name(String driver_name) {
			this.driver_name = driver_name;
		}

		/**
		 * @return the driver_id
		 */
		public String getDriver_id() {
			return driver_id;
		}

		/**
		 * @param driver_id
		 *            the driver_id to set
		 */
		public void setDriver_id(String driver_id) {
			this.driver_id = driver_id;
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
		 * @return the std_quantity
		 */
		public BigDecimal getStd_quantity() {
			return std_quantity;
		}

		/**
		 * @param std_quantity
		 *            the std_quantity to set
		 */
		public void setStd_quantity(BigDecimal std_quantity) {
			this.std_quantity = std_quantity;
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
		 * @return the weighting_quantity
		 */
		public BigDecimal getWeighting_quantity() {
			return weighting_quantity;
		}

		/**
		 * @param weighting_quantity
		 *            the weighting_quantity to set
		 */
		public void setWeighting_quantity(BigDecimal weighting_quantity) {
			this.weighting_quantity = weighting_quantity;
		}

		/**
		 * @return the route_name
		 */
		public String getRoute_name() {
			return route_name;
		}

		/**
		 * @param route_name
		 *            the route_name to set
		 */
		public void setRoute_name(String route_name) {
			this.route_name = route_name;
		}

		/**
		 * @return the route_id
		 */
		public BigDecimal getRoute_id() {
			return route_id;
		}

		/**
		 * @param route_id
		 *            the route_id to set
		 */
		public void setRoute_id(BigDecimal route_id) {
			this.route_id = route_id;
		}

		/**
		 * @return the sort_status
		 */
		public int getSort_status() {
			return sort_status;
		}

		/**
		 * @param sort_status
		 *            the sort_status to set
		 */
		public void setSort_status(int sort_status) {
			this.sort_status = sort_status;
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

	/**
	 * @return the unit_name
	 */
	public String getUnit_name() {
		return unit_name;
	}

	/**
	 * @param unit_name
	 *            the unit_name to set
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
	 * @param sale_ratio
	 *            the sale_ratio to set
	 */
	public void setSale_ratio(BigDecimal sale_ratio) {
		this.sale_ratio = sale_ratio;
	}

	/**
	 * @return the total_count
	 */
	public int getTotal_count() {
		return total_count;
	}

	/**
	 * @param total_count
	 *            the total_count to set
	 */
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	/**
	 * @return the finish_count
	 */
	public int getFinish_count() {
		return finish_count;
	}

	/**
	 * @param finish_count
	 *            the finish_count to set
	 */
	public void setFinish_count(int finish_count) {
		this.finish_count = finish_count;
	}

	/**
	 * @return the orders
	 */
	public List<Order> getOrders() {
		return orders;
	}

	/**
	 * @param orders
	 *            the orders to set
	 */
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

}
