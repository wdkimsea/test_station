package cn.guanmai.station.bean.purchase.param.assistant;

import java.util.List;

/* 
* @author liming 
* @date May 27, 2019 11:26:48 AM 
* @des 采购助手APP新建采购单对应的参数
* @version 1.0 
*/
public class SheetCreateParam {
	private String time_config_id;
	private String cycle_start_time;
	private List<String> release_ids;
	private int q_type;
	private String begin_time;
	private String end_time;

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
	 * @return the release_ids
	 */
	public List<String> getRelease_ids() {
		return release_ids;
	}

	/**
	 * @param release_ids
	 *            the release_ids to set
	 */
	public void setRelease_ids(List<String> release_ids) {
		this.release_ids = release_ids;
	}

	/**
	 * @return the q_type
	 */
	public int getQ_type() {
		return q_type;
	}

	/**
	 * @param q_type
	 *            the q_type to set
	 */
	public void setQ_type(int q_type) {
		this.q_type = q_type;
	}

	/**
	 * @return the begin_time
	 */
	public String getBegin_time() {
		return begin_time;
	}

	/**
	 * @param begin_time
	 *            the begin_time to set
	 */
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}

	/**
	 * @return the end_time
	 */
	public String getEnd_time() {
		return end_time;
	}

	/**
	 * @param end_time
	 *            the end_time to set
	 */
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
}
