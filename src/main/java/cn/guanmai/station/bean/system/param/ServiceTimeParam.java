package cn.guanmai.station.bean.system.param;

/**
 * @author: liming
 * @Date: 2020年5月21日 下午6:54:19
 * @description: 运营时间创建、修改接口
 * @version: 1.0
 */

public class ServiceTimeParam {
	private String id;
	private String name;
	private String desc;
	private Integer type;
	private String final_distribute_time;
	private int final_distribute_time_span;
	private OrderTimeLimit order_time_limit;
	private ReceiveTimeLimit receive_time_limit;
	private int out_stock_interval;
	private String service_time_creator;
	private String task_begin_time;

	public class OrderTimeLimit {
		private int e_span_time;
		private String start;
		private String end;

		public int getE_span_time() {
			return e_span_time;
		}

		public void setE_span_time(int e_span_time) {
			this.e_span_time = e_span_time;
		}

		public String getStart() {
			return start;
		}

		public void setStart(String start) {
			this.start = start;
		}

		public String getEnd() {
			return end;
		}

		public void setEnd(String end) {
			this.end = end;
		}
	}

	public class ReceiveTimeLimit {
		private int receiveEndSpan;
		private int s_span_time;
		private int weekdays;
		private int e_span_time;
		private int receiveTimeSpan;
		private String start;
		private String end;

		public int getReceiveEndSpan() {
			return receiveEndSpan;
		}

		public void setReceiveEndSpan(int receiveEndSpan) {
			this.receiveEndSpan = receiveEndSpan;
		}

		public int getS_span_time() {
			return s_span_time;
		}

		public void setS_span_time(int s_span_time) {
			this.s_span_time = s_span_time;
		}

		public int getWeekdays() {
			return weekdays;
		}

		public void setWeekdays(int weekdays) {
			this.weekdays = weekdays;
		}

		public int getE_span_time() {
			return e_span_time;
		}

		public void setE_span_time(int e_span_time) {
			this.e_span_time = e_span_time;
		}

		public int getReceiveTimeSpan() {
			return receiveTimeSpan;
		}

		public void setReceiveTimeSpan(int receiveTimeSpan) {
			this.receiveTimeSpan = receiveTimeSpan;
		}

		public String getStart() {
			return start;
		}

		public void setStart(String start) {
			this.start = start;
		}

		public String getEnd() {
			return end;
		}

		public void setEnd(String end) {
			this.end = end;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getFinal_distribute_time() {
		return final_distribute_time;
	}

	public void setFinal_distribute_time(String final_distribute_time) {
		this.final_distribute_time = final_distribute_time;
	}

	public int getFinal_distribute_time_span() {
		return final_distribute_time_span;
	}

	public void setFinal_distribute_time_span(int final_distribute_time_span) {
		this.final_distribute_time_span = final_distribute_time_span;
	}

	public OrderTimeLimit getOrder_time_limit() {
		return order_time_limit;
	}

	public void setOrder_time_limit(OrderTimeLimit order_time_limit) {
		this.order_time_limit = order_time_limit;
	}

	public ReceiveTimeLimit getReceive_time_limit() {
		return receive_time_limit;
	}

	public void setReceive_time_limit(ReceiveTimeLimit receive_time_limit) {
		this.receive_time_limit = receive_time_limit;
	}

	public int getOut_stock_interval() {
		return out_stock_interval;
	}

	public void setOut_stock_interval(int out_stock_interval) {
		this.out_stock_interval = out_stock_interval;
	}

	public String getService_time_creator() {
		return service_time_creator;
	}

	public void setService_time_creator(String service_time_creator) {
		this.service_time_creator = service_time_creator;
	}

	public String getTask_begin_time() {
		return task_begin_time;
	}

	public void setTask_begin_time(String task_begin_time) {
		this.task_begin_time = task_begin_time;
	}

}
