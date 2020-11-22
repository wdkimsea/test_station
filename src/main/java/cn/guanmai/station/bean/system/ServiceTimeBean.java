package cn.guanmai.station.bean.system;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Nov 8, 2018 11:04:57 AM 
* @des 运营时间
* @version 1.0 
*/
public class ServiceTimeBean {
	@JSONField(name = "id", alternateNames = { "_id" })
	private String id;
	private String desc;
	private String final_distribute_time;
	private int final_distribute_time_span;
	private String name;
	private String service_time_creator;
	private String task_begin_time;
	private int type;

	private OrderTimeLimit order_time_limit;
	private ReceiveTimeLimit receive_time_limit;

	/**
	 * 此ID为运营时间详细信息中的ID
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the final_distribute_time
	 */
	public String getFinal_distribute_time() {
		return final_distribute_time;
	}

	/**
	 * @param final_distribute_time the final_distribute_time to set
	 */
	public void setFinal_distribute_time(String final_distribute_time) {
		this.final_distribute_time = final_distribute_time;
	}

	/**
	 * @return the final_distribute_time_span
	 */
	public int getFinal_distribute_time_span() {
		return final_distribute_time_span;
	}

	/**
	 * @param final_distribute_time_span the final_distribute_time_span to set
	 */
	public void setFinal_distribute_time_span(int final_distribute_time_span) {
		this.final_distribute_time_span = final_distribute_time_span;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the service_time_creator
	 */
	public String getService_time_creator() {
		return service_time_creator;
	}

	/**
	 * @param service_time_creator the service_time_creator to set
	 */
	public void setService_time_creator(String service_time_creator) {
		this.service_time_creator = service_time_creator;
	}

	/**
	 * @return the task_begin_time
	 */
	public String getTask_begin_time() {
		return task_begin_time;
	}

	/**
	 * @param task_begin_time the task_begin_time to set
	 */
	public void setTask_begin_time(String task_begin_time) {
		this.task_begin_time = task_begin_time;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the order_time_limit
	 */
	public OrderTimeLimit getOrder_time_limit() {
		return order_time_limit;
	}

	/**
	 * @param order_time_limit the order_time_limit to set
	 */
	public void setOrder_time_limit(OrderTimeLimit order_time_limit) {
		this.order_time_limit = order_time_limit;
	}

	/**
	 * @return the receive_time_limit
	 */
	public ReceiveTimeLimit getReceive_time_limit() {
		return receive_time_limit;
	}

	/**
	 * @param receive_time_limit the receive_time_limit to set
	 */
	public void setReceive_time_limit(ReceiveTimeLimit receive_time_limit) {
		this.receive_time_limit = receive_time_limit;
	}

	public ServiceTimeBean() {
	}

	public ServiceTimeBean(String id, String desc, String final_distribute_time, int final_distribute_time_span,
			String name, String service_time_creator, String task_begin_time, int type, OrderTimeLimit order_time_limit,
			ReceiveTimeLimit receive_time_limit) {
		super();
		this.id = id;
		this.desc = desc;
		this.final_distribute_time = final_distribute_time;
		this.final_distribute_time_span = final_distribute_time_span;
		this.name = name;
		this.service_time_creator = service_time_creator;
		this.task_begin_time = task_begin_time;
		this.type = type;
		this.order_time_limit = order_time_limit;
		this.receive_time_limit = receive_time_limit;
	}

	public class OrderTimeLimit {
		private int e_span_time;
		private String end;
		private String start;

		/**
		 * @return the e_span_time
		 */
		public int getE_span_time() {
			return e_span_time;
		}

		/**
		 * @param e_span_time the e_span_time to set
		 */
		public void setE_span_time(int e_span_time) {
			this.e_span_time = e_span_time;
		}

		/**
		 * @return the end
		 */
		public String getEnd() {
			return end;
		}

		/**
		 * @param end the end to set
		 */
		public void setEnd(String end) {
			this.end = end;
		}

		/**
		 * @return the start
		 */
		public String getStart() {
			return start;
		}

		/**
		 * @param start the start to set
		 */
		public void setStart(String start) {
			this.start = start;
		}

		public OrderTimeLimit() {
			super();
		}

		public OrderTimeLimit(int e_span_time, String start, String end) {
			super();
			this.e_span_time = e_span_time;
			this.end = end;
			this.start = start;
		}

	}

	public class ReceiveTimeLimit {
		private int e_span_time;
		private String end;
		private int receiveEndSpan;
		private int receiveTimeSpan;
		private int s_span_time;
		private String start;
		private int weekdays;

		/**
		 * @return the e_span_time
		 */
		public int getE_span_time() {
			return e_span_time;
		}

		/**
		 * @param e_span_time the e_span_time to set
		 */
		public void setE_span_time(int e_span_time) {
			this.e_span_time = e_span_time;
		}

		/**
		 * @return the end
		 */
		public String getEnd() {
			return end;
		}

		/**
		 * @param end the end to set
		 */
		public void setEnd(String end) {
			this.end = end;
		}

		/**
		 * @return the receiveEndSpan
		 */
		public int getReceiveEndSpan() {
			return receiveEndSpan;
		}

		/**
		 * @param receiveEndSpan the receiveEndSpan to set
		 */
		public void setReceiveEndSpan(int receiveEndSpan) {
			this.receiveEndSpan = receiveEndSpan;
		}

		public int getReceiveTimeSpan() {
			return receiveTimeSpan;
		}

		public void setReceiveTimeSpan(int receiveTimeSpan) {
			this.receiveTimeSpan = receiveTimeSpan;
		}

		/**
		 * @return the s_span_time
		 */
		public int getS_span_time() {
			return s_span_time;
		}

		/**
		 * @param s_span_time the s_span_time to set
		 */
		public void setS_span_time(int s_span_time) {
			this.s_span_time = s_span_time;
		}

		/**
		 * @return the start
		 */
		public String getStart() {
			return start;
		}

		/**
		 * @param start the start to set
		 */
		public void setStart(String start) {
			this.start = start;
		}

		public int getWeekdays() {
			return weekdays;
		}

		public void setWeekdays(int weekdays) {
			this.weekdays = weekdays;
		}

		public ReceiveTimeLimit() {
			super();
		}

	}

}
