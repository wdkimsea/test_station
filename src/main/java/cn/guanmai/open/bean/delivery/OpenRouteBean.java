package cn.guanmai.open.bean.delivery;

/* 
* @author liming 
* @date Jun 5, 2019 7:09:40 PM 
* @des 接口 /delivery/route/list 对应的结果
* @version 1.0 
*/
public class OpenRouteBean {
	private String route_id;
	private String route_name;
	private String create_user;
	private String create_time;
	private int customer_count;

	/**
	 * @return the route_id
	 */
	public String getRoute_id() {
		return route_id;
	}

	/**
	 * @param route_id
	 *            the route_id to set
	 */
	public void setRoute_id(String route_id) {
		this.route_id = route_id;
	}

	/**
	 * @return the route_name
	 */
	public String getRoute_name() {
		return route_name;
	}

	/**
	 * @param route_name
	 *            the route_name to set
	 */
	public void setRoute_name(String route_name) {
		this.route_name = route_name;
	}

	/**
	 * @return the create_user
	 */
	public String getCreate_user() {
		return create_user;
	}

	/**
	 * @param create_user
	 *            the create_user to set
	 */
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	/**
	 * @return the create_time
	 */
	public String getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time
	 *            the create_time to set
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the customer_count
	 */
	public int getCustomer_count() {
		return customer_count;
	}

	/**
	 * @param customer_count
	 *            the customer_count to set
	 */
	public void setCustomer_count(int customer_count) {
		this.customer_count = customer_count;
	}

}
