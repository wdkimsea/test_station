package cn.guanmai.station.bean.order;

/* 
* @author liming 
* @date Apr 2, 2019 11:09:19 AM 
* @des 订单所处运营周期
* @version 1.0 
*/
public class OrderCycle {
	private String time_config_id;
	private String cycle_start_time;
	private String cycle_end_time;
	private int type;

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
	 * @return the cycle_end_time
	 */
	public String getCycle_end_time() {
		return cycle_end_time;
	}

	/**
	 * @param cycle_end_time
	 *            the cycle_end_time to set
	 */
	public void setCycle_end_time(String cycle_end_time) {
		this.cycle_end_time = cycle_end_time;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

}
