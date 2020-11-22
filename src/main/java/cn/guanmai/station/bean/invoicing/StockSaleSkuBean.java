package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Feb 27, 2019 10:14:23 AM 
* @des 出库搜索销售商品结果类
* @version 1.0 
*/
public class StockSaleSkuBean {
	private String category_id_1;
	private String category_id_1_name;
	private String category_id_2;
	private String category_id_2_name;
	private BigDecimal sale_price;
	private BigDecimal sale_ratio;
	private String sale_unit_name;
	private String sku_id;
	private String sku_name;
	private String spu_id;
	private String station_id;
	private String std_unit_name;
	private String std_unit_name_forsale;

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
	 * @return the category_id_1_name
	 */
	public String getCategory_id_1_name() {
		return category_id_1_name;
	}

	/**
	 * @param category_id_1_name the category_id_1_name to set
	 */
	public void setCategory_id_1_name(String category_id_1_name) {
		this.category_id_1_name = category_id_1_name;
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
	 * @return the category_id_2_name
	 */
	public String getCategory_id_2_name() {
		return category_id_2_name;
	}

	/**
	 * @param category_id_2_name the category_id_2_name to set
	 */
	public void setCategory_id_2_name(String category_id_2_name) {
		this.category_id_2_name = category_id_2_name;
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
	 * @return the station_id
	 */
	public String getStation_id() {
		return station_id;
	}

	/**
	 * @param station_id the station_id to set
	 */
	public void setStation_id(String station_id) {
		this.station_id = station_id;
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
