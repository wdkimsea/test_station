package cn.guanmai.station.bean.weight.param;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Apr 12, 2019 2:21:36 PM 
* @des 新版称重软件打印接口 { /weight/sku/print/infos } 返回的结果
* @version 1.0 
*/
public class PrintInfoBean {
	@JSONField(name="id")
	private String sku_id;
	@JSONField(name="name")
	private String sku_name;
	private BigDecimal sale_ratio;
	private String std_unit_name;
	private String sale_unit_name;
	private String remark;
	private String package_id;
	private BigDecimal quantity;
	private BigDecimal weighting_quantity;

	private Order order;

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

	public String getPackage_id() {
		return package_id;
	}

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
	 * @param quantity the quantity to set
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
	 * @param weighting_quantity the weighting_quantity to set
	 */
	public void setWeighting_quantity(BigDecimal weighting_quantity) {
		this.weighting_quantity = weighting_quantity;
	}

	/**
	 * @return the order
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	public class Order {
		private String address_id;
		private String address_name;

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
	}

}
