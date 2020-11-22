package cn.guanmai.station.bean.weight.param;

/* 
* @author liming 
* @date Jan 10, 2019 11:21:58 AM 
* @des 接口 { /station/weigh/union_dispatch/fast_task } 对应的参数类
* @version 1.0 
*/
public class UnionDispatchParam {
	// 必填参数
	private String station_id;

	// 必填参数,默认值为0
	private int weigh_filter;

	// 必填参数
	private String time_config_id;

	// 必填参数,参数样式 2019-01-10 08:00:00
	private String cycle_start_time;

	/**
	 * @return the station_id
	 */
	public String getStation_id() {
		return station_id;
	}

	/**
	 * 必填参数
	 * 
	 * @param station_id
	 *            the station_id to set
	 */
	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	/**
	 * @return the weigh_filter
	 */
	public int getWeigh_filter() {
		return weigh_filter;
	}

	/**
	 * 必填参数<br/>
	 * 参数默认值: 0
	 * 
	 * @param weigh_filter
	 *            the weigh_filter to set
	 */
	public void setWeigh_filter(int weigh_filter) {
		this.weigh_filter = weigh_filter;
	}

	/**
	 * 
	 * @return the time_config_id
	 */
	public String getTime_config_id() {
		return time_config_id;
	}

	/**
	 * 必填参数
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
	 * 参数样式: 2019-01-10 08:00:00
	 * 
	 * @param cycle_start_time
	 *            the cycle_start_time to set
	 */
	public void setCycle_start_time(String cycle_start_time) {
		this.cycle_start_time = cycle_start_time;
	}

}
