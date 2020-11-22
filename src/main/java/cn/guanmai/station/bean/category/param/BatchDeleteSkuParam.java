package cn.guanmai.station.bean.category.param;

import com.alibaba.fastjson.JSONArray;

/* 
* @author liming 
* @date May 28, 2019 10:13:35 AM 
* @des 批量删除SKU参数
* @version 1.0 
*/
public class BatchDeleteSkuParam {
	private int all;
	private JSONArray category1_ids;
	private JSONArray category2_ids;
	private JSONArray pinlei_ids;
	private JSONArray salemenu_ids;
	private String q;
	private Integer state;
	private int search_from;
	private JSONArray sku_list;

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
	 * @return the search_from
	 */
	public int getSearch_from() {
		return search_from;
	}

	/**
	 * @param search_from the search_from to set
	 */
	public void setSearch_from(int search_from) {
		this.search_from = search_from;
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
	 * 直接传销售SKU ID列表删除
	 * 
	 * @param sku_list
	 * @param search_from 1=从报价单里选择销售SKU、2=从商品库列表选择销售SKU
	 */
	public BatchDeleteSkuParam(JSONArray sku_list, int search_from) {
		super();
		this.all = 0;
		this.sku_list = sku_list;
		this.search_from = search_from;
	}

	/**
	 * 搜索过滤后批量删除
	 * 
	 * @param category1_ids
	 * @param category2_ids
	 * @param pinlei_ids
	 * @param salemenu_ids
	 * @param q
	 * @param state
	 * @param search_from   1=从报价单里选择销售SKU、2=从商品库列表选择销售SKU
	 */
	public BatchDeleteSkuParam(JSONArray category1_ids, JSONArray category2_ids, JSONArray pinlei_ids,
			JSONArray salemenu_ids, String q, Integer state, int search_from) {
		super();
		this.all = 1;
		this.category1_ids = category1_ids;
		this.category2_ids = category2_ids;
		this.pinlei_ids = pinlei_ids;
		this.salemenu_ids = salemenu_ids;
		this.q = q;
		this.state = state;
		this.search_from = search_from;
	}

}
