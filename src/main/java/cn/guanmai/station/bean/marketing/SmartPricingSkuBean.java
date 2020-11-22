package cn.guanmai.station.bean.marketing;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date May 29, 2019 5:49:08 PM 
* @des 接口 /product/sku/smart_pricing/list 对应的结果
*      智能定价选取的商品结果
* @version 1.0 
*/
public class SmartPricingSkuBean {
	private String sku_id;

	@JSONField(name = "name")
	private String sku_name;
	private BigDecimal last_in_stock_price;
	private BigDecimal last_purchase_price;
	private BigDecimal last_quote_price;
	private BigDecimal new_price;
	private BigDecimal old_price;
	private boolean over_suggest_price;
	private BigDecimal ratio;
	private BigDecimal sale_price;
	private String sale_unit_name;
	private String salemenu_name;
	private String std_unit_name;
	private BigDecimal stock_avg_price;
	private BigDecimal suggest_price_max;
	private BigDecimal suggest_price_min;

	/**
	 * @return the last_in_stock_price
	 */
	public BigDecimal getLast_in_stock_price() {
		return last_in_stock_price;
	}

	/**
	 * @param last_in_stock_price
	 *            the last_in_stock_price to set
	 */
	public void setLast_in_stock_price(BigDecimal last_in_stock_price) {
		this.last_in_stock_price = last_in_stock_price;
	}

	/**
	 * @return the last_purchase_price
	 */
	public BigDecimal getLast_purchase_price() {
		return last_purchase_price;
	}

	/**
	 * @param last_purchase_price
	 *            the last_purchase_price to set
	 */
	public void setLast_purchase_price(BigDecimal last_purchase_price) {
		this.last_purchase_price = last_purchase_price;
	}

	/**
	 * @return the last_quote_price
	 */
	public BigDecimal getLast_quote_price() {
		return last_quote_price;
	}

	/**
	 * @param last_quote_price
	 *            the last_quote_price to set
	 */
	public void setLast_quote_price(BigDecimal last_quote_price) {
		this.last_quote_price = last_quote_price;
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
	 * @return the new_price
	 */
	public BigDecimal getNew_price() {
		return new_price;
	}

	/**
	 * @param new_price
	 *            the new_price to set
	 */
	public void setNew_price(BigDecimal new_price) {
		this.new_price = new_price;
	}

	/**
	 * @return the old_price
	 */
	public BigDecimal getOld_price() {
		return old_price;
	}

	/**
	 * @param old_price
	 *            the old_price to set
	 */
	public void setOld_price(BigDecimal old_price) {
		this.old_price = old_price;
	}

	/**
	 * @return the over_suggest_price
	 */
	public boolean isOver_suggest_price() {
		return over_suggest_price;
	}

	/**
	 * @param over_suggest_price
	 *            the over_suggest_price to set
	 */
	public void setOver_suggest_price(boolean over_suggest_price) {
		this.over_suggest_price = over_suggest_price;
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
		this.sale_price = sale_price;
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
	 * @return the salemenu_name
	 */
	public String getSalemenu_name() {
		return salemenu_name;
	}

	/**
	 * @param salemenu_name
	 *            the salemenu_name to set
	 */
	public void setSalemenu_name(String salemenu_name) {
		this.salemenu_name = salemenu_name;
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
	 * @return the stock_avg_price
	 */
	public BigDecimal getStock_avg_price() {
		return stock_avg_price;
	}

	/**
	 * @param stock_avg_price
	 *            the stock_avg_price to set
	 */
	public void setStock_avg_price(BigDecimal stock_avg_price) {
		this.stock_avg_price = stock_avg_price;
	}

	/**
	 * @return the suggest_price_max
	 */
	public BigDecimal getSuggest_price_max() {
		return suggest_price_max;
	}

	/**
	 * @param suggest_price_max
	 *            the suggest_price_max to set
	 */
	public void setSuggest_price_max(BigDecimal suggest_price_max) {
		this.suggest_price_max = suggest_price_max;
	}

	/**
	 * @return the suggest_price_min
	 */
	public BigDecimal getSuggest_price_min() {
		return suggest_price_min;
	}

	/**
	 * @param suggest_price_min
	 *            the suggest_price_min to set
	 */
	public void setSuggest_price_min(BigDecimal suggest_price_min) {
		this.suggest_price_min = suggest_price_min;
	}

}
