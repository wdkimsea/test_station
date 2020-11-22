package cn.guanmai.station.bean.marketing.param;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONArray;

/* 
* @author liming 
* @date May 31, 2019 11:52:53 AM 
* @des 接口 /product/sku/smart_pricing/update 对应的参数
*      只能定价更新参数
* @version 1.0 
*/
public class SmartPricingUpdateParam {
	private int all;
	private JSONArray sku_list;
	private int formula_type;
	private int filter_price_region;
	private int filter_price_type;
	private int price_type;
	private int cal_type;
	private BigDecimal cal_num;
	private JSONArray re_category1_ids;
	private JSONArray re_category2_ids;
	private JSONArray re_pinlei_ids;
	private String re_q;
	private JSONArray modify_sku_list;

	/**
	 * @return the all
	 */
	public int getAll() {
		return all;
	}

	/**
	 * @param all the all to set
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
	 * @param sku_list the sku_list to set
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
	 * @param formula_type the formula_type to set
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
	 * @param filter_price_region the filter_price_region to set
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
	 * @param filter_price_type the filter_price_type to set
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
	 * @param price_type the price_type to set
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
	 * @param cal_type the cal_type to set
	 */
	public void setCal_type(int cal_type) {
		this.cal_type = cal_type;
	}

	/**
	 * @return the cal_num
	 */
	public BigDecimal getCal_num() {
		return cal_num;
	}

	/**
	 * @param cal_num the cal_num to set
	 */
	public void setCal_num(BigDecimal cal_num) {
		this.cal_num = cal_num;
	}

	/**
	 * @return the re_category1_ids
	 */
	public JSONArray getRe_category1_ids() {
		return re_category1_ids;
	}

	/**
	 * @param re_category1_ids the re_category1_ids to set
	 */
	public void setRe_category1_ids(JSONArray re_category1_ids) {
		this.re_category1_ids = re_category1_ids;
	}

	/**
	 * @return the re_category2_ids
	 */
	public JSONArray getRe_category2_ids() {
		return re_category2_ids;
	}

	/**
	 * @param re_category2_ids the re_category2_ids to set
	 */
	public void setRe_category2_ids(JSONArray re_category2_ids) {
		this.re_category2_ids = re_category2_ids;
	}

	/**
	 * @return the re_pinlei_ids
	 */
	public JSONArray getRe_pinlei_ids() {
		return re_pinlei_ids;
	}

	/**
	 * @param re_pinlei_ids the re_pinlei_ids to set
	 */
	public void setRe_pinlei_ids(JSONArray re_pinlei_ids) {
		this.re_pinlei_ids = re_pinlei_ids;
	}

	/**
	 * @return the re_q
	 */
	public String getRe_q() {
		return re_q;
	}

	/**
	 * @param re_q the re_q to set
	 */
	public void setRe_q(String re_q) {
		this.re_q = re_q;
	}

	/**
	 * @return the modify_sku_list
	 */
	public JSONArray getModify_sku_list() {
		return modify_sku_list;
	}

	/**
	 * @param modify_sku_list the modify_sku_list to set
	 */
	public void setModify_sku_list(JSONArray modify_sku_list) {
		this.modify_sku_list = modify_sku_list;
	}

}
