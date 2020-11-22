package cn.guanmai.station.bean.weight.param;

import com.alibaba.fastjson.JSONArray;

/* 
* @author liming 
* @date Jan 10, 2019 3:17:55 PM 
* @des 接口 { /station/weigh/get_task } 对应的参数类
* @version 1.0 
*/
public class WeighTaskParam {
	private String station_id;

	private boolean is_weigh;

	private boolean union_dispatch;

	private String operator_id;

	private String time_config_id;

	private String cycle_start_time;

	private boolean out_stock_filte;

	private JSONArray spu_ids;

	private JSONArray product_id_list;

	private String product_id;

	/**
	 * @return the station_id
	 */
	public String getStation_id() {
		return station_id;
	}

	/**
	 * @param station_id
	 *            the station_id to set
	 */
	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	/**
	 * @return the is_weigh
	 */
	public boolean isIs_weigh() {
		return is_weigh;
	}

	/**
	 * @param is_weigh
	 *            the is_weigh to set
	 */
	public void setIs_weigh(boolean is_weigh) {
		this.is_weigh = is_weigh;
	}

	/**
	 * @return the union_dispatch
	 */
	public boolean isUnion_dispatch() {
		return union_dispatch;
	}

	/**
	 * @param union_dispatch
	 *            the union_dispatch to set
	 */
	public void setUnion_dispatch(boolean union_dispatch) {
		this.union_dispatch = union_dispatch;
	}

	/**
	 * @return the operator_id
	 */
	public String getOperator_id() {
		return operator_id;
	}

	/**
	 * @param operator_id
	 *            the operator_id to set
	 */
	public void setOperator_id(String operator_id) {
		this.operator_id = operator_id;
	}

	/**
	 * @return the time_config_id
	 */
	public String getTime_config_id() {
		return time_config_id;
	}

	/**
	 * @param time_config_id
	 *            the time_config_id to set
	 */
	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	/**
	 * @return the cycle_start_time
	 */
	public String getCycle_start_time() {
		return cycle_start_time;
	}

	/**
	 * @param cycle_start_time
	 *            the cycle_start_time to set
	 */
	public void setCycle_start_time(String cycle_start_time) {
		this.cycle_start_time = cycle_start_time;
	}

	/**
	 * @return the out_stock_filte
	 */
	public boolean isOut_stock_filte() {
		return out_stock_filte;
	}

	/**
	 * @param out_stock_filte
	 *            the out_stock_filte to set
	 */
	public void setOut_stock_filte(boolean out_stock_filte) {
		this.out_stock_filte = out_stock_filte;
	}

	/**
	 * @return the spu_ids
	 */
	public JSONArray getSpu_ids() {
		return spu_ids;
	}

	/**
	 * @param spu_ids
	 *            the spu_ids to set
	 */
	public void setSpu_ids(JSONArray spu_ids) {
		this.spu_ids = spu_ids;
	}

	/**
	 * @return the product_id_list
	 */
	public JSONArray getProduct_id_list() {
		return product_id_list;
	}

	/**
	 * @param product_id_list
	 *            the product_id_list to set
	 */
	public void setProduct_id_list(JSONArray product_id_list) {
		this.product_id_list = product_id_list;
	}

	/**
	 * @return the product_id
	 */
	public String getProduct_id() {
		return product_id;
	}

	/**
	 * @param product_id
	 *            the product_id to set
	 */
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public WeighTaskParam() {
		super();
	}

	/**
	 * 参数构造方法一
	 * 
	 * @param station_id
	 * @param is_weigh
	 *            <br/>
	 *            默认值为 true
	 * @param union_dispatch
	 *            <br/>
	 *            默认值为true
	 * @param operator_id
	 * @param time_config_id
	 * @param cycle_start_time
	 *            <br/>
	 *            参数样式 2019-01-11 06:00:00
	 * @param out_stock_filte
	 *            <br/>
	 *            默认值为 false
	 * @param spu_ids
	 */
	public WeighTaskParam(String station_id, boolean is_weigh, boolean union_dispatch, String operator_id,
			String time_config_id, String cycle_start_time, boolean out_stock_filte, JSONArray spu_ids) {
		this.station_id = station_id;
		this.is_weigh = is_weigh;
		this.union_dispatch = union_dispatch;
		this.operator_id = operator_id;
		this.time_config_id = time_config_id;
		this.cycle_start_time = cycle_start_time;
		this.out_stock_filte = out_stock_filte;
		this.spu_ids = spu_ids;
	}

	/**
	 * 参数构造方法二
	 * 
	 * @param station_id
	 * @param union_dispatch
	 *            <br/>
	 *            默认值为true
	 * @param time_config_id
	 * @param cycle_start_time
	 *            <br/>
	 *            参数样式 2019-01-11 06:00:00
	 * @param out_stock_filte
	 *            <br/>
	 *            默认值为 false
	 * @param product_id_list
	 */
	public WeighTaskParam(String station_id, boolean union_dispatch, String time_config_id, String cycle_start_time,
			boolean out_stock_filte, JSONArray product_id_list) {
		this.station_id = station_id;
		this.union_dispatch = union_dispatch;
		this.time_config_id = time_config_id;
		this.cycle_start_time = cycle_start_time;
		this.out_stock_filte = out_stock_filte;
		this.product_id_list = product_id_list;
		this.is_weigh = true;
	}

	/**
	 * 参数构造方法三
	 * 
	 * @param station_id
	 * @param union_dispatch<br/>
	 *            默认值为 true
	 * @param operator_id
	 * @param time_config_id
	 * @param cycle_start_time
	 *            <br/>
	 *            参数样式 2019-01-11 06:00:00
	 * @param out_stock_filte
	 *            <br/>
	 *            默认值为 false
	 * @param product_id
	 */
	public WeighTaskParam(String station_id, boolean union_dispatch, String operator_id, String time_config_id,
			String cycle_start_time, boolean out_stock_filte, String product_id) {
		this.station_id = station_id;
		this.union_dispatch = union_dispatch;
		this.operator_id = operator_id;
		this.time_config_id = time_config_id;
		this.cycle_start_time = cycle_start_time;
		this.out_stock_filte = out_stock_filte;
		this.product_id = product_id;
		this.is_weigh = true;
	}

}
