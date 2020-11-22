package cn.guanmai.station.bean.purchase.assistant;

import java.math.BigDecimal;

/* 
* @author liming 
* @date May 24, 2019 11:33:51 AM 
* @des 供应商对应的采购规格
* @version 1.0 
*/
public class SupplierSpecBean {
	private String spec_id;
	private String spec_name;
	private String remark;
	private String std_unit_name;
	private String purchaser_name;
	private BigDecimal price;
	private String origin_place;

	/**
	 * @return the spec_id
	 */
	public String getSpec_id() {
		return spec_id;
	}

	/**
	 * @param spec_id
	 *            the spec_id to set
	 */
	public void setSpec_id(String spec_id) {
		this.spec_id = spec_id;
	}

	/**
	 * @return the spec_name
	 */
	public String getSpec_name() {
		return spec_name;
	}

	/**
	 * @param spec_name
	 *            the spec_name to set
	 */
	public void setSpec_name(String spec_name) {
		this.spec_name = spec_name;
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
	 * @return the purchaser_name
	 */
	public String getPurchaser_name() {
		return purchaser_name;
	}

	/**
	 * @param purchaser_name
	 *            the purchaser_name to set
	 */
	public void setPurchaser_name(String purchaser_name) {
		this.purchaser_name = purchaser_name;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price.divide(new BigDecimal("100"));
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the origin_place
	 */
	public String getOrigin_place() {
		return origin_place;
	}

	/**
	 * @param origin_place
	 *            the origin_place to set
	 */
	public void setOrigin_place(String origin_place) {
		this.origin_place = origin_place;
	}

}
