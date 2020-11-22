package cn.guanmai.station.bean.weight.param;

import com.alibaba.fastjson.JSONArray;

/**
 * @author liming
 * @date 2019年7月8日 上午11:56:12
 * @des 接口 /weight/weight_collect/sku/list 对应的结果,ST-供应链-分拣-分拣明细-按商品分拣
 * @version 1.0
 */
public class WeightCollectSkuFilterParam {
	private String time_config_id;
	private JSONArray category_id_1;
	private JSONArray category_id_2;
	private JSONArray pinlei_id;
	private String start_date;
	private String end_date;
	private String search;
	private String salemenu_id;
	private String order_process_type_id;
	private int limit = 10;
	private int offset;
	private Integer inspect_status;

	private Integer op_way;

	/**
	 * @return the time_config_id
	 */
	public String getTime_config_id() {
		return time_config_id;
	}

	/**
	 * @param time_config_id the time_config_id to set
	 */
	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	/**
	 * @return the category_id_1
	 */
	public JSONArray getCategory_id_1() {
		return category_id_1;
	}

	/**
	 * @param category_id_1 the category_id_1 to set
	 */
	public void setCategory_id_1(JSONArray category_id_1) {
		this.category_id_1 = category_id_1;
	}

	/**
	 * @return the category_id_2
	 */
	public JSONArray getCategory_id_2() {
		return category_id_2;
	}

	/**
	 * @param category_id_2 the category_id_2 to set
	 */
	public void setCategory_id_2(JSONArray category_id_2) {
		this.category_id_2 = category_id_2;
	}

	/**
	 * @return the pinlei_id
	 */
	public JSONArray getPinlei_id() {
		return pinlei_id;
	}

	/**
	 * @param pinlei_id the pinlei_id to set
	 */
	public void setPinlei_id(JSONArray pinlei_id) {
		this.pinlei_id = pinlei_id;
	}

	/**
	 * @return the start_date
	 */
	public String getStart_date() {
		return start_date;
	}

	/**
	 * @param start_date the start_date to set
	 */
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	/**
	 * @return the end_date
	 */
	public String getEnd_date() {
		return end_date;
	}

	/**
	 * @param end_date the end_date to set
	 */
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getSalemenu_id() {
		return salemenu_id;
	}

	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	public Integer getOp_way() {
		return op_way;
	}

	public String getOrder_process_type_id() {
		return order_process_type_id;
	}

	public void setOrder_process_type_id(String order_process_type_id) {
		this.order_process_type_id = order_process_type_id;
	}

	public Integer getInspect_status() {
		return inspect_status;
	}

	public void setInspect_status(Integer inspect_status) {
		this.inspect_status = inspect_status;
	}

	/**
	 * 过滤分拣缺货时传1
	 * 
	 * @param op_way
	 */
	public void setOp_way(Integer op_way) {
		this.op_way = op_way;
	}

}
