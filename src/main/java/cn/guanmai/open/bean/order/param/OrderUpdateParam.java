package cn.guanmai.open.bean.order.param;

/* 
* @author liming 
* @date Jun 4, 2019 4:16:34 PM 
* @des 接口 /order/update 对应的参数
* @version 1.0 
*/
public class OrderUpdateParam {
	private String order_id;
	private String receive_begin_time;
	private String receive_end_time;
	private Integer status;
	private String remark;

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
	 * @return the receive_begin_time
	 */
	public String getReceive_begin_time() {
		return receive_begin_time;
	}

	/**
	 * @param receive_begin_time
	 *            the receive_begin_time to set
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
	 * @param receive_end_time
	 *            the receive_end_time to set
	 */
	public void setReceive_end_time(String receive_end_time) {
		this.receive_end_time = receive_end_time;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
