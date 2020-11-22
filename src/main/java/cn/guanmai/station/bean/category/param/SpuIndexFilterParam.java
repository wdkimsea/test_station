package cn.guanmai.station.bean.category.param;

import com.alibaba.fastjson.JSONArray;

/* 
* @author liming 
* @date Apr 8, 2019 6:47:20 PM 
* @des 商品库过滤搜索参数
* @version 1.0 
*/
public class SpuIndexFilterParam {
	private JSONArray category1_ids = new JSONArray();
	private JSONArray category2_ids = new JSONArray();
	private JSONArray pinlei_ids = new JSONArray();
	private JSONArray salemenu_ids = new JSONArray();
	private String q;
	private Integer offset;
	private Integer limit;

	private Integer salemenu_is_active;
	private Integer has_images;
	private Integer is_price_timing;

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
	 * @return the salemenu_ids
	 */
	public JSONArray getSalemenu_ids() {
		return salemenu_ids;
	}

	/**
	 * @param salemenu_ids the salemenu_ids to set
	 */
	public void setSalemenu_ids(JSONArray salemenu_ids) {
		this.salemenu_ids = salemenu_ids;
	}

	/**
	 * @return the q
	 */
	public String getQ() {
		return q;
	}

	/**
	 * @param q the q to set
	 */
	public void setQ(String q) {
		this.q = q;
	}

	/**
	 * @return the offset
	 */
	public Integer getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	/**
	 * @return the limit
	 */
	public Integer getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getSalemenu_is_active() {
		return salemenu_is_active;
	}

	public void setSalemenu_is_active(Integer salemenu_is_active) {
		this.salemenu_is_active = salemenu_is_active;
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

}
