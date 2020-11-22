package cn.guanmai.station.bean.weight;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2019年7月31日 上午11:45:26
 * @des 接口 /weight/package/sku/package/list 对应的结果,预分拣SKU的包装详情
 * @version 1.0
 */
public class PreSortingSkuPackageBean {
	private String spu_id;
	private String sku_id;
	private String sku_name;
	private String std_unit_name;
	private String unit_name;
	private BigDecimal sale_ratio;
	private BigDecimal sort_count;
	private BigDecimal total_count;
	private BigDecimal unsort_count;

	private List<PackageDetail> packages;

	/**
	 * @return the spu_id
	 */
	public String getSpu_id() {
		return spu_id;
	}

	/**
	 * @param spu_id
	 *            the spu_id to set
	 */
	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
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
	 * @return the sort_count
	 */
	public BigDecimal getSort_count() {
		return sort_count;
	}

	/**
	 * @param sort_count
	 *            the sort_count to set
	 */
	public void setSort_count(BigDecimal sort_count) {
		this.sort_count = sort_count;
	}

	/**
	 * @return the total_count
	 */
	public BigDecimal getTotal_count() {
		return total_count;
	}

	/**
	 * @param total_count
	 *            the total_count to set
	 */
	public void setTotal_count(BigDecimal total_count) {
		this.total_count = total_count;
	}

	/**
	 * @return the unsort_count
	 */
	public BigDecimal getUnsort_count() {
		return unsort_count;
	}

	/**
	 * @param unsort_count
	 *            the unsort_count to set
	 */
	public void setUnsort_count(BigDecimal unsort_count) {
		this.unsort_count = unsort_count;
	}

	/**
	 * @return the packages
	 */
	public List<PackageDetail> getPackages() {
		return packages;
	}

	/**
	 * @param packages
	 *            the packages to set
	 */
	public void setPackages(List<PackageDetail> packages) {
		this.packages = packages;
	}

	public class PackageDetail {
		private String order_id;
		private String package_date;
		private String package_id;
		private BigDecimal quantity;
		private int sort_status;

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
		 * @return the package_date
		 */
		public String getPackage_date() {
			return package_date;
		}

		/**
		 * @param package_date
		 *            the package_date to set
		 */
		public void setPackage_date(String package_date) {
			this.package_date = package_date;
		}

		/**
		 * @return the package_id
		 */
		public String getPackage_id() {
			return package_id;
		}

		/**
		 * @param package_id
		 *            the package_id to set
		 */
		public void setPackage_id(String package_id) {
			this.package_id = package_id;
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
	}
}
