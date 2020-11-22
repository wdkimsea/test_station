package cn.guanmai.station.bean.order.param;

/**
 * @author: liming
 * @Date: 2020年7月7日 下午3:03:15
 * @description: 接口 /station/order/update/status/preconfig 对应的参数
 * @version: 1.0
 */

public class OrderStatusPreconfigParam {
	private String count;
	private String batch_remark;
	private int to_status;
	private int query_type;
	private String start_date_new;
	private String end_date_new;

	// 按运营周期时间
	private String time_config_id;
	private String start_cycle_time;
	private String end_cycle_time;

	// 按收货时间
	private String receive_start_date_new;
	private String receive_end_date_new;

	public String getCount() {
		return count;
	}

	/**
	 * 参数可以是整数和 all
	 * 
	 * @param count
	 */
	public void setCount(String count) {
		this.count = count;
	}

	public String getBatch_remark() {
		return batch_remark;
	}

	public void setBatch_remark(String batch_remark) {
		this.batch_remark = batch_remark;
	}

	public int getTo_status() {
		return to_status;
	}

	/**
	 * 参数值: 5、10、15
	 * 
	 * @param to_status
	 */
	public void setTo_status(int to_status) {
		this.to_status = to_status;
	}

	public int getQuery_type() {
		return query_type;
	}

	public void setQuery_type(int query_type) {
		this.query_type = query_type;
	}

	public String getStart_date_new() {
		return start_date_new;
	}

	public void setStart_date_new(String start_date_new) {
		this.start_date_new = start_date_new;
	}

	public String getEnd_date_new() {
		return end_date_new;
	}

	public void setEnd_date_new(String end_date_new) {
		this.end_date_new = end_date_new;
	}

	public String getTime_config_id() {
		return time_config_id;
	}

	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	public String getStart_cycle_time() {
		return start_cycle_time;
	}

	public void setStart_cycle_time(String start_cycle_time) {
		this.start_cycle_time = start_cycle_time;
	}

	public String getEnd_cycle_time() {
		return end_cycle_time;
	}

	public void setEnd_cycle_time(String end_cycle_time) {
		this.end_cycle_time = end_cycle_time;
	}

	public String getReceive_start_date_new() {
		return receive_start_date_new;
	}

	public void setReceive_start_date_new(String receive_start_date_new) {
		this.receive_start_date_new = receive_start_date_new;
	}

	public String getReceive_end_date_new() {
		return receive_end_date_new;
	}

	public void setReceive_end_date_new(String receive_end_date_new) {
		this.receive_end_date_new = receive_end_date_new;
	}

}
