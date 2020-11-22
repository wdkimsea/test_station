package cn.guanmai.station.bean.marketing.param;

import com.alibaba.fastjson.JSONArray;

/* 
* @author liming 
* @date May 29, 2019 5:50:28 PM 
* @des 接口 /product/sku/smart_pricing/list 对应的参数
* @version 1.0 
*/
public class SmartPricingSkuFilterParam {
	private int all;
	private JSONArray sku_list;
	private int formula_type;
	private int filter_price_region;
	private int filter_price_type;
	private int price_type;
	private int cal_type;
	private int cal_num;

	/**
	 * @return the all
	 */
	public int getAll() {
		return all;
	}

	/**
	 * 是否选取全部商品.设置为 0时smart_price_sku_list必传,设置为1时
	 * category1_ids,category2_ids,pinlei_ids,q 必传
	 * 
	 * @param all
	 *            the all to set
	 */
	public void setAll(int all) {
		this.all = all;
	}

	/**
	 * @return the sku_list
	 */
	public JSONArray getSku_list() {
		return sku_list;
	}

	/**
	 * @param sku_list
	 *            the sku_list to set
	 */
	public void setSku_list(JSONArray sku_list) {
		this.sku_list = sku_list;
	}

	/**
	 * @return the formula_type
	 */
	public int getFormula_type() {
		return formula_type;
	}

	/**
	 * @param formula_type
	 *            the formula_type to set
	 */
	public void setFormula_type(int formula_type) {
		this.formula_type = formula_type;
	}

	/**
	 * @return the filter_price_region
	 */
	public int getFilter_price_region() {
		return filter_price_region;
	}

	/**
	 * @param filter_price_region
	 *            the filter_price_region to set
	 */
	public void setFilter_price_region(int filter_price_region) {
		this.filter_price_region = filter_price_region;
	}

	/**
	 * @return the filter_price_type
	 */
	public int getFilter_price_type() {
		return filter_price_type;
	}

	/**
	 * @param filter_price_type
	 *            the filter_price_type to set
	 */
	public void setFilter_price_type(int filter_price_type) {
		this.filter_price_type = filter_price_type;
	}

	/**
	 * @return the price_type
	 */
	public int getPrice_type() {
		return price_type;
	}

	/**
	 * @param price_type
	 *            the price_type to set
	 */
	public void setPrice_type(int price_type) {
		this.price_type = price_type;
	}

	/**
	 * @return the cal_type
	 */
	public int getCal_type() {
		return cal_type;
	}

	/**
	 * @param cal_type
	 *            the cal_type to set
	 */
	public void setCal_type(int cal_type) {
		this.cal_type = cal_type;
	}

	/**
	 * @return the cal_num
	 */
	public int getCal_num() {
		return cal_num;
	}

	/**
	 * @param cal_num
	 *            the cal_num to set
	 */
	public void setCal_num(int cal_num) {
		this.cal_num = cal_num;
	}
}
