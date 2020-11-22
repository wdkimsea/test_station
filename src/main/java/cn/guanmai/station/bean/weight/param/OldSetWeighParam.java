package cn.guanmai.station.bean.weight.param;

import java.math.BigDecimal;

import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Jan 10, 2019 7:29:34 PM 
* @des 接口 { /station/weigh/set_weight } 对应的参数类
* @version 1.0 
*/
public class OldSetWeighParam {
	private String station_id;
	private boolean union_dispatch;
	private String time_config_id;
	private String cycle_start_time;
	private String operator_id;
	private Object data;

	/**
	 * @return the station_id
	 */
	public String getStation_id() {
		return station_id;
	}

	/**
	 * @param station_id
	 *            the station_id to set
	 */
	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	/**
	 * @return the union_dispatch
	 */
	public boolean isUnion_dispatch() {
		return union_dispatch;
	}

	/**
	 * @param union_dispatch
	 *            the union_dispatch to set
	 */
	public void setUnion_dispatch(boolean union_dispatch) {
		this.union_dispatch = union_dispatch;
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
	 * @return the operator_id
	 */
	public String getOperator_id() {
		return operator_id;
	}

	/**
	 * @param operator_id
	 *            the operator_id to set
	 */
	public void setOperator_id(String operator_id) {
		this.operator_id = operator_id;
	}

	/**
	 * @return the data
	 * @throws Exception 
	 */
	public Data getData() throws Exception {
		return JsonUtil.strToClassObject(String.valueOf(data), Data.class);
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Data data) {
		this.data = JsonUtil.objectToStr(data);
	}

	public class Data {
		private String order_id;
		private String detail_id;
		private BigDecimal real_weight;

		/**
		 * @return the order_id
		 */
		public String getOrder_id() {
			return order_id;
		}

		/**
		 * @param order_id
		 *            the order_id to set
		 */
		public void setOrder_id(String order_id) {
			this.order_id = order_id;
		}

		/**
		 * @return the detail_id
		 */
		public String getDetail_id() {
			return detail_id;
		}

		/**
		 * @param detail_id
		 *            the detail_id to set
		 */
		public void setDetail_id(String detail_id) {
			this.detail_id = detail_id;
		}

		/**
		 * @return the real_weight
		 */
		public BigDecimal getReal_weight() {
			return real_weight;
		}

		/**
		 * @param real_weight
		 *            the real_weight to set
		 */
		public void setReal_weight(BigDecimal real_weight) {
			this.real_weight = real_weight;
		}

	}

}
