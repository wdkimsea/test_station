package cn.guanmai.station.bean.order.param;

import java.util.List;
import java.util.Map;

/* 
* @author liming 
* @date Nov 13, 2018 10:56:00 AM 
* @des 修改订单的Bean
* @version 1.0 
*/
public class OrderEditParam {
	private String order_id;
	private List<OrderCreateParam.OrderSku> details;
	private Map<String, OrderCreateParam.CombineGoods> combine_goods_map;
	private OrderData order_data;

	/**
	 * @return the order_id
	 */
	public String getOrder_id() {
		return order_id;
	}

	/**
	 * @param order_id the order_id to set
	 */
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public List<OrderCreateParam.OrderSku> getDetails() {
		return details;
	}

	public void setDetails(List<OrderCreateParam.OrderSku> details) {
		this.details = details;
	}

	public Map<String, OrderCreateParam.CombineGoods> getCombine_goods_map() {
		return combine_goods_map;
	}

	public void setCombine_goods_map(Map<String, OrderCreateParam.CombineGoods> combine_goods_map) {
		this.combine_goods_map = combine_goods_map;
	}

	/**
	 * @return the order_data
	 */
	public OrderData getOrder_data() {
		return order_data;
	}

	/**
	 * @param order_data the order_data to set
	 */
	public void setOrder_data(OrderData order_data) {
		this.order_data = order_data;
	}

	public class OrderData {
		private String receive_begin_time;
		private String receive_end_time;
		private String remark;

		public OrderData() {
			super();

		}

		public OrderData(String receive_begin_time, String receive_end_time, String remark) {
			super();
			this.receive_begin_time = receive_begin_time;
			this.receive_end_time = receive_end_time;
			this.remark = remark;
		}

		/**
		 * @return the receive_begin_time
		 */
		public String getReceive_begin_time() {
			return receive_begin_time;
		}

		/**
		 * @param receive_begin_time the receive_begin_time to set
		 */
		public void setReceive_begin_time(String receive_begin_time) {
			this.receive_begin_time = receive_begin_time;
		}

		/**
		 * @return the receive_end_time
		 */
		public String getReceive_end_time() {
			return receive_end_time;
		}

		/**
		 * @param receive_end_time the receive_end_time to set
		 */
		public void setReceive_end_time(String receive_end_time) {
			this.receive_end_time = receive_end_time;
		}

		/**
		 * @return the remark
		 */
		public String getRemark() {
			return remark;
		}

		/**
		 * @param remark the remark to set
		 */
		public void setRemark(String remark) {
			this.remark = remark;
		}
	}

}
