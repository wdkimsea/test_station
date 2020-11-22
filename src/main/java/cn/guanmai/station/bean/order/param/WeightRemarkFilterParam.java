package cn.guanmai.station.bean.order.param;

/**
 * @author: liming
 * @Date: 2020年7月8日 上午11:39:05
 * @description:
 * @version: 1.0
 */

public class WeightRemarkFilterParam {
	private int query_type;

	// 按下单时间
	private String start_date;
	private String end_date;

	// 按运营周期
	private String time_config_id;
	private String cycle_start_time;
	private String cycle_end_time;

	// 按收货时间
	private String receive_start_date;
	private String receive_end_date;

	public int getQuery_type() {
		return query_type;
	}

	public void setQuery_type(int query_type) {
		this.query_type = query_type;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getTime_config_id() {
		return time_config_id;
	}

	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	public String getCycle_start_time() {
		return cycle_start_time;
	}

	public void setCycle_start_time(String cycle_start_time) {
		this.cycle_start_time = cycle_start_time;
	}

	public String getCycle_end_time() {
		return cycle_end_time;
	}

	public void setCycle_end_time(String cycle_end_time) {
		this.cycle_end_time = cycle_end_time;
	}

	public String getReceive_start_date() {
		return receive_start_date;
	}

	public void setReceive_start_date(String receive_start_date) {
		this.receive_start_date = receive_start_date;
	}

	public String getReceive_end_date() {
		return receive_end_date;
	}

	public void setReceive_end_date(String receive_end_date) {
		this.receive_end_date = receive_end_date;
	}

	public WeightRemarkFilterParam() {
		super();
	}

	public WeightRemarkFilterParam(int query_type, String start_date, String end_date) {
		super();
		if (query_type == 1) {
			this.query_type = query_type;
			this.start_date = start_date;
			this.end_date = end_date;
		}

		if (query_type == 3) {
			this.query_type = query_type;
			this.receive_start_date = start_date;
			this.receive_end_date = end_date;
		}
	}

	public WeightRemarkFilterParam(int query_type, String time_config_id, String cycle_start_time,
			String cycle_end_time) {
		super();
		this.query_type = query_type;
		this.time_config_id = time_config_id;
		this.cycle_start_time = cycle_start_time;
		this.cycle_end_time = cycle_end_time;
	}

}
