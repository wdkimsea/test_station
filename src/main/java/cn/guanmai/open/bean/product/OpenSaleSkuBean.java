package cn.guanmai.open.bean.product;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jun 10, 2019 7:06:06 PM 
* @des 开放平台销售规格
* @version 1.0 
*/
public class OpenSaleSkuBean {
	private String sku_id;
	private String sale_unit_name;
	private String sku_name;
	private BigDecimal sale_ratio;
	private String std_unit_name;
	private BigDecimal sale_price;
	private BigDecimal std_sale_price;
	private String salemenu_id;
//	private String salemenu_name;
	private int state;

	/**
	 * @return the id
	 */
	public String getId() {
		return sku_id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.sku_id = id;
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
	 * @return the name
	 */
	public String getName() {
		return sku_name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.sku_name = name;
	}

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public String getSku_name() {
		return sku_name;
	}

	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	public BigDecimal getSale_ratio() {
		return sale_ratio;
	}

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
	 * @return the std_sale_price
	 */
	public BigDecimal getStd_sale_price() {
		return std_sale_price;
	}

	/**
	 * @param std_sale_price the std_sale_price to set
	 */
	public void setStd_sale_price(BigDecimal std_sale_price) {
		this.std_sale_price = std_sale_price;
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
//
//	/**
//	 * @return the salemenu_name
//	 */
//	public String getSalemenu_name() {
//		return salemenu_name;
//	}
//
//	/**
//	 * @param salemenu_name
//	 *            the salemenu_name to set
//	 */
//	public void setSalemenu_name(String salemenu_name) {
//		this.salemenu_name = salemenu_name;
//	}

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
