package cn.guanmai.manage.bean.ordermanage.param;

/**
 * @program: station
 * @description: 用户订单异常搜索参数
 * @author: weird
 * @create: 2019-01-21 17:06
 **/
public class OrderManageParamBean {
	private String start_date;
	private String end_date;
	private String order_id;

	/**
	 * @return the start_date
	 */
	public String getStart_date() {
		return start_date;
	}

	/**
	 * @param start_date
	 *            the start_date to set
	 */
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	/**
	 * @return the end_date
	 */
	public String getEnd_date() {
		return end_date;
	}

	/**
	 * @param end_date
	 *            the end_date to set
	 */
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

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

	public OrderManageParamBean(String start_date, String end_date, String order_id) {
		this.start_date = start_date;
		this.end_date = end_date;
		this.order_id = order_id;
	}
}
