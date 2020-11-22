package cn.guanmai.station.bean.weight.param;

/* 
* @author liming 
* @date Jan 10, 2019 4:54:30 PM 
* @des 接口 { /station/weigh/pack_data } 对应的参数类
* @version 1.0 
*/
public class PackWeighDataParam {
	private String station_id;
	private String time_config_id;
	private boolean union_dispatch;
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
	 * @return the union_dispatch
	 */
	public boolean isUnion_dispatch() {
		return union_dispatch;
	}

	/**
	 * 必填参数,默认为true
	 * 
	 * @param union_dispatch
	 *            the union_dispatch to set
	 */
	public void setUnion_dispatch(boolean union_dispatch) {
		this.union_dispatch = union_dispatch;
	}

	/**
	 * @return the cycle_start_time
	 */
	public String getCycle_start_time() {
		return cycle_start_time;
	}

	/**
	 * 必填参数,参数样式为 2019-01-11 06:00:00
	 * 
	 * @param cycle_start_time
	 *            the cycle_start_time to set
	 */
	public void setCycle_start_time(String cycle_start_time) {
		this.cycle_start_time = cycle_start_time;
	}

	public PackWeighDataParam(String station_id, String time_config_id, boolean union_dispatch,
			String cycle_start_time) {
		super();
		this.station_id = station_id;
		this.time_config_id = time_config_id;
		this.union_dispatch = union_dispatch;
		this.cycle_start_time = cycle_start_time;
	}

}
