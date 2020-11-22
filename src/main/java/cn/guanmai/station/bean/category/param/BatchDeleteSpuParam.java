package cn.guanmai.station.bean.category.param;

import com.alibaba.fastjson.JSONArray;

/* 
* @author liming 
* @date May 28, 2019 2:55:17 PM 
* @des 批量删除SPU参数
* @version 1.0 
*/
public class BatchDeleteSpuParam {
	private int all;
	private JSONArray spu_list;
	private JSONArray category1_ids;
	private JSONArray category2_ids;
	private JSONArray pinlei_ids;
	private JSONArray salemenu_ids;
	private String q;

	/**
	 * @return the all
	 */
	public int getAll() {
		return all;
	}

	/**
	 * @param all
	 *            the all to set
	 */
	public void setAll(int all) {
		this.all = all;
	}

	/**
	 * @return the spu_list
	 */
	public JSONArray getSpu_list() {
		return spu_list;
	}

	/**
	 * @param spu_list
	 *            the spu_list to set
	 */
	public void setSpu_list(JSONArray spu_list) {
		this.spu_list = spu_list;
	}

	/**
	 * @return the category1_ids
	 */
	public JSONArray getCategory1_ids() {
		return category1_ids;
	}

	/**
	 * @param category1_ids
	 *            the category1_ids to set
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
	 * @param category2_ids
	 *            the category2_ids to set
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
	 * @param pinlei_ids
	 *            the pinlei_ids to set
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
	 * @param salemenu_ids
	 *            the salemenu_ids to set
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
	 * @param q
	 *            the q to set
	 */
	public void setQ(String q) {
		this.q = q;
	}

	/**
	 * 直接传SPU ID列表进行批量删除
	 * 
	 * @param spu_list
	 */
	public BatchDeleteSpuParam(JSONArray spu_list) {
		super();
		this.all = 0;
		this.spu_list = spu_list;
	}

	/**
	 * 搜索过滤后批量删除
	 * 
	 * @param category1_ids
	 * @param category2_ids
	 * @param pinlei_ids
	 * @param salemenu_ids
	 * @param q
	 */
	public BatchDeleteSpuParam(JSONArray category1_ids, JSONArray category2_ids, JSONArray pinlei_ids,
			JSONArray salemenu_ids, String q) {
		super();
		this.all = 1;
		this.category1_ids = category1_ids;
		this.category2_ids = category2_ids;
		this.pinlei_ids = pinlei_ids;
		this.salemenu_ids = salemenu_ids;
		this.q = q;
	}

}
