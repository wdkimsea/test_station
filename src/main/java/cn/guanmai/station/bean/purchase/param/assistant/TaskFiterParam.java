package cn.guanmai.station.bean.purchase.param.assistant;

/* 
* @author liming 
* @date May 24, 2019 5:52:10 PM 
* @des 采购助手采购任务过滤参数
* @version 1.0 
*/
public class TaskFiterParam {
	private int q_type;
	private String time_config_id;
	private String begin_time;
	private String end_time;
	private int status;
	private int sort_type;
	private String search_text;
	private int limit = 20;
	private String page_obj;

	/**
	 * @return the q_type
	 */
	public int getQ_type() {
		return q_type;
	}

	/**
	 * 1 按下单时间、2 按运营周期、3 按收货时间
	 * 
	 * @param q_type the q_type to set
	 */
	public void setQ_type(int q_type) {
		this.q_type = q_type;
	}

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
	 * @return the begin_time
	 */
	public String getBegin_time() {
		return begin_time;
	}

	/**
	 * 时间格式 2019-05-24 06:00:00
	 * 
	 * @param begin_time the begin_time to set
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
	 * 时间格式 2019-05-24 06:00:00
	 * 
	 * @param end_time the end_time to set
	 */
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the sort_type
	 */
	public int getSort_type() {
		return sort_type;
	}

	/**
	 * @param sort_type the sort_type to set
	 */
	public void setSort_type(int sort_type) {
		this.sort_type = sort_type;
	}

	/**
	 * @return the search_text
	 */
	public String getSearch_text() {
		return search_text;
	}

	/**
	 * @param search_text the search_text to set
	 */
	public void setSearch_text(String search_text) {
		this.search_text = search_text;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getPage_obj() {
		return page_obj;
	}

	public void setPage_obj(String page_obj) {
		this.page_obj = page_obj;
	}

}
