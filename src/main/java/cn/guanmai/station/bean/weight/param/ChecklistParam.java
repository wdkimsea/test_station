package cn.guanmai.station.bean.weight.param;

/* 
* @author liming 
* @date Jan 9, 2019 4:12:35 PM 
* @des 接口  {/station/weigh/checklist/get} 对应的参数类
* @version 1.0 
*/
public class ChecklistParam {
	private String station_id;

	// 非必填参数
	private String date;

	// 非必填参数
	private String sort_id;

	// 非必填参数
	private String order_id;

	// 必填参数,默认值为 False
	private String sort_by_amiba;

	// 必填参数
	private String time_config_id;

	// 必填参数,参数样式 2019-01-10 06:00:00
	private String cycle_start_time;

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
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * 非必填参数
	 * 
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the sort_id
	 */
	public String getSort_id() {
		return sort_id;
	}

	/**
	 * 非必填参数
	 * 
	 * @param sort_id
	 *            the sort_id to set
	 */
	public void setSort_id(String sort_id) {
		this.sort_id = sort_id;
	}

	/**
	 * @return the order_id
	 */
	public String getOrder_id() {
		return order_id;
	}

	/**
	 * 非必填参数
	 * 
	 * @param order_id
	 *            the order_id to set
	 */
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	/**
	 * @return the sort_by_amiba
	 */
	public String isSort_by_amiba() {
		return sort_by_amiba;
	}

	/**
	 * 必填参数<br/>
	 * 参数默认值: False
	 * 
	 * @param sort_by_amiba
	 *            the sort_by_amiba to set
	 */
	public void setSort_by_amiba(String sort_by_amiba) {
		this.sort_by_amiba = sort_by_amiba;
	}

	/**
	 * @return the time_config_id
	 */
	public String getTime_config_id() {
		return time_config_id;
	}

	/**
	 * 必填参数<br/>
	 * 
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
	 * 必填参数<br/>
	 * 参数样式: 2019-01-10 06:00:00
	 * 
	 * @param cycle_start_time
	 *            the cycle_start_time to set
	 */
	public void setCycle_start_time(String cycle_start_time) {
		this.cycle_start_time = cycle_start_time;
	}

}
