package cn.guanmai.station.bean.order;

import java.util.List;

/**
 * @author: liming
 * @Date: 2020年4月15日 上午10:37:41
 * @description:
 * @version: 1.0
 */

public class OrderReceiveTimeBean {
	private String time_config_id;
	private List<ReceiveTime> receive_times;

	public class ReceiveTime {
		private String day;
		private List<String> times;

		public String getDay() {
			return day;
		}

		public void setDay(String day) {
			this.day = day;
		}

		public List<String> getTimes() {
			return times;
		}

		public void setTimes(List<String> times) {
			this.times = times;
		}

		public ReceiveTime() {
			super();
		}

	}

	public String getTime_config_id() {
		return time_config_id;
	}

	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	public List<ReceiveTime> getReceive_times() {
		return receive_times;
	}

	public void setReceive_times(List<ReceiveTime> receive_times) {
		this.receive_times = receive_times;
	}

	public OrderReceiveTimeBean() {
		super();
	}

}
