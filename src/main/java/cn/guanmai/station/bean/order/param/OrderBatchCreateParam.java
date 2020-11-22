package cn.guanmai.station.bean.order.param;

import java.util.List;

/**
 * 
 * @author Administrator
 * @author liming
 * @since 2019-08-13
 */
public class OrderBatchCreateParam {
	private String task_id;
	private String file_name;
	private String time_config_id;
	private List<Data> data;

	public class Data {
		private String address_id;
		private String uid;
		private String receive_begin_time;
		private String receive_end_time;

		private List<OrderSkuParam> details;

		private String timeStart;
		private String timeEnd;
		private int flagStart;
		private int flagEnd;

		public String getAddress_id() {
			return address_id;
		}

		public void setAddress_id(String address_id) {
			this.address_id = address_id;
		}

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getReceive_begin_time() {
			return receive_begin_time;
		}

		public void setReceive_begin_time(String receive_begin_time) {
			this.receive_begin_time = receive_begin_time;
		}

		public String getReceive_end_time() {
			return receive_end_time;
		}

		public void setReceive_end_time(String receive_end_time) {
			this.receive_end_time = receive_end_time;
		}

		public List<OrderSkuParam> getDetails() {
			return details;
		}

		public void setDetails(List<OrderSkuParam> details) {
			this.details = details;
		}

		public String getTimeStart() {
			return timeStart;
		}

		public void setTimeStart(String timeStart) {
			this.timeStart = timeStart;
		}

		public String getTimeEnd() {
			return timeEnd;
		}

		public void setTimeEnd(String timeEnd) {
			this.timeEnd = timeEnd;
		}

		public int getFlagStart() {
			return flagStart;
		}

		public void setFlagStart(int flagStart) {
			this.flagStart = flagStart;
		}

		public int getFlagEnd() {
			return flagEnd;
		}

		public void setFlagEnd(int flagEnd) {
			this.flagEnd = flagEnd;
		}
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getTime_config_id() {
		return time_config_id;
	}

	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

}
