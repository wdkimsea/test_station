package cn.guanmai.station.bean.category.param;

import com.alibaba.fastjson.JSONArray;

/* 
* @author liming 
* @date Feb 18, 2019 10:22:36 AM 
* @des 报价单里销售SKU筛选过滤参数类
* @version 1.0 
*/
public class SalemenuSkuFilterParam {
	private String salemenu_id;
	private JSONArray category1_ids;
	private JSONArray category2_ids;
	private JSONArray pinlei_ids;
	private String text;
	private Integer state;
	private Integer formula;
	private Integer has_images;
	private Integer is_price_timing;
	private int offset = 0;
	private int limit = 20;

	private Integer export;

	/**
	 * @return the category1_ids
	 */
	public JSONArray getCategory1_ids() {
		return category1_ids;
	}

	/**
	 * @param category1_ids the category1_ids to set
	 */
	public void setCategory1_ids(JSONArray category1_ids) {
		this.category1_ids = category1_ids;
	}

	/**
	 * @return the category2_ids
	 */
	public JSONArray getCategory2_ids() {
		return category2_ids;
	}

	/**
	 * @param category2_ids the category2_ids to set
	 */
	public void setCategory2_ids(JSONArray category2_ids) {
		this.category2_ids = category2_ids;
	}

	/**
	 * @return the pinlei_ids
	 */
	public JSONArray getPinlei_ids() {
		return pinlei_ids;
	}

	/**
	 * @param pinlei_ids the pinlei_ids to set
	 */
	public void setPinlei_ids(JSONArray pinlei_ids) {
		this.pinlei_ids = pinlei_ids;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the state
	 */
	public Integer getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(Integer state) {
		this.state = state;
	}

	/**
	 * @return the salemenu_id
	 */
	public String getSalemenu_id() {
		return salemenu_id;
	}

	/**
	 * 必填,报价单ID
	 * 
	 * @param salemenu_id the salemenu_id to set
	 */
	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
	}

	public Integer getFormula() {
		return formula;
	}

	public void setFormula(Integer formula) {
		this.formula = formula;
	}

	public Integer getHas_images() {
		return has_images;
	}

	public void setHas_images(Integer has_images) {
		this.has_images = has_images;
	}

	public Integer getIs_price_timing() {
		return is_price_timing;
	}

	public void setIs_price_timing(Integer is_price_timing) {
		this.is_price_timing = is_price_timing;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Integer getExport() {
		return export;
	}

	public void setExport(Integer export) {
		this.export = export;
	}

}
