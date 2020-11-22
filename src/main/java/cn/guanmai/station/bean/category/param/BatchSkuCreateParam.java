package cn.guanmai.station.bean.category.param;

import java.math.BigDecimal;

/* 
* @author liming 
* @date May 28, 2019 7:21:15 PM 
* @des 批量新建SKU参数
* @version 1.0 
*/
public class BatchSkuCreateParam {
	private String spu_id;
	private String sku_name;
	private BigDecimal sale_price;
	private BigDecimal ratio;
	private String sale_unit_name;
	private BigDecimal sale_num_least;
	private int is_weigh;
	private int state;
	private int is_price_timing;
	private int stock_type;
	private BigDecimal stock;
	private String supplier_id;
	private String pur_spec_id;

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
	 * @return the sale_price
	 */
	public BigDecimal getSale_price() {
		return sale_price;
	}

	/**
	 * @param sale_price
	 *            the sale_price to set
	 */
	public void setSale_price(BigDecimal sale_price) {
		this.sale_price = sale_price.multiply(new BigDecimal("100"));
	}

	/**
	 * @return the ratio
	 */
	public BigDecimal getRatio() {
		return ratio;
	}

	/**
	 * @param ratio
	 *            the ratio to set
	 */
	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
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
	 * @return the sale_num_least
	 */
	public BigDecimal getSale_num_least() {
		return sale_num_least;
	}

	/**
	 * @param sale_num_least
	 *            the sale_num_least to set
	 */
	public void setSale_num_least(BigDecimal sale_num_least) {
		this.sale_num_least = sale_num_least;
	}

	/**
	 * @return the is_weigh
	 */
	public int getIs_weigh() {
		return is_weigh;
	}

	/**
	 * @param is_weigh
	 *            the is_weigh to set
	 */
	public void setIs_weigh(int is_weigh) {
		this.is_weigh = is_weigh;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * @return the is_price_timing
	 */
	public int getIs_price_timing() {
		return is_price_timing;
	}

	/**
	 * @param is_price_timing
	 *            the is_price_timing to set
	 */
	public void setIs_price_timing(int is_price_timing) {
		this.is_price_timing = is_price_timing;
	}

	/**
	 * @return the stock_type
	 */
	public int getStock_type() {
		return stock_type;
	}

	/**
	 * @param stock_type
	 *            the stock_type to set
	 */
	public void setStock_type(int stock_type) {
		this.stock_type = stock_type;
	}

	/**
	 * @return the stock
	 */
	public BigDecimal getStock() {
		return stock;
	}

	/**
	 * @param stock
	 *            the stock to set
	 */
	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	/**
	 * @return the supplier_id
	 */
	public String getSupplier_id() {
		return supplier_id;
	}

	/**
	 * @param supplier_id
	 *            the supplier_id to set
	 */
	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	/**
	 * @return the pur_spec_id
	 */
	public String getPur_spec_id() {
		return pur_spec_id;
	}

	/**
	 * @param pur_spec_id
	 *            the pur_spec_id to set
	 */
	public void setPur_spec_id(String pur_spec_id) {
		this.pur_spec_id = pur_spec_id;
	}

}
