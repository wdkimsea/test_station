package cn.guanmai.station.bean.system;

import java.util.List;

/**
 * @author: liming
 * @Date: 2020年3月12日 上午10:48:14
 * @description: 运营时间下单日期、收货日期限制结果
 * @version: 1.0
 */

public class ServiceTimeLimitBean {
	private String time_config_id;
	private String order_start_time;
	private String order_end_time;

	private List<String> receive_dates;

	private String receive_start_time;
	private String receive_end_time;
	private int receiveTimeSpan;

	public String getTime_config_id() {
		return time_config_id;
	}

	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	public String getOrder_start_time() {
		return order_start_time;
	}

	public void setOrder_start_time(String order_start_time) {
		this.order_start_time = order_start_time;
	}

	public String getOrder_end_time() {
		return order_end_time;
	}

	public void setOrder_end_time(String order_end_time) {
		this.order_end_time = order_end_time;
	}

	public List<String> getReceive_dates() {
		return receive_dates;
	}

	public void setReceive_dates(List<String> receive_dates) {
		this.receive_dates = receive_dates;
	}

	public String getReceive_start_time() {
		return receive_start_time;
	}

	public void setReceive_start_time(String receive_start_time) {
		this.receive_start_time = receive_start_time;
	}

	public String getReceive_end_time() {
		return receive_end_time;
	}

	public void setReceive_end_time(String receive_end_time) {
		this.receive_end_time = receive_end_time;
	}

	public int getReceiveTimeSpan() {
		return receiveTimeSpan;
	}

	public void setReceiveTimeSpan(int receiveTimeSpan) {
		this.receiveTimeSpan = receiveTimeSpan;
	}

	public ServiceTimeLimitBean() {
		super();
	}

}
