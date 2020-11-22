package cn.guanmai.station.bean.weight;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * @author liming
 * @date 2019年7月31日 下午4:47:06
 * @des 接口 /weight/pda/sort/detail 对应的结果
 * @version 1.0
 */
public class PdaWeightSortDetailBean {
	private String order_id;
	private String spu_id;
	private String sku_name;
	private String sku_id;
	private String std_unit_name;
	private String unit_name;

	private BigDecimal quantity;
	private BigDecimal sale_ratio;
	private BigDecimal std_quantity;
	private BigDecimal weighting_quantity;

	private BigDecimal package_count;

	@JSONField(name="packages")
	private List<PackageInfo> packageInfos;

	public class PackageInfo {
		private String package_id;
		private BigDecimal quantity;

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
	 * @return the package_count
	 */
	public BigDecimal getPackage_count() {
		return package_count;
	}

	/**
	 * @param package_count
	 *            the package_count to set
	 */
	public void setPackage_count(BigDecimal package_count) {
		this.package_count = package_count;
	}

}
