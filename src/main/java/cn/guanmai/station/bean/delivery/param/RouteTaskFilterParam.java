package cn.guanmai.station.bean.delivery.param;

/* 
* @author liming 
* @date Apr 1, 2019 8:01:47 PM 
* @des 线路任务列表过滤参数
* @version 1.0 
*/
public class RouteTaskFilterParam {
	private int date_type;
	private String start_date;
	private String end_date;
	private String q;

	private String time_config_id;

	/**
	 * @return the date_type
	 */
	public int getDate_type() {
		return date_type;
	}

	/**
	 * @param date_type
	 *            the date_type to set
	 */
	public void setDate_type(int date_type) {
		this.date_type = date_type;
	}

	/**
	 * @return the start_date
	 */
	public String getStart_date() {
		return start_date;
	}

	/**
	 * @param start_date
	 *            the start_date to set
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
	 * @param end_date
	 *            the end_date to set
	 */
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
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

}
