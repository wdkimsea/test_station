package cn.guanmai.station.bean.delivery;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;


/* 
* @author liming 
* @date Jan 4, 2019 2:51:59 PM 
* @des 配送订单列表
* @version 1.0 
*/
public class DistributeOrderBean {
	@JSONField(name="id")
	private String order_id;
	// 路线
	private String route_name;

	private String customer_id;

	private String customer_name;

	private JSONArray address_id;

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
	 * @return the customer_id
	 */
	public String getCustomer_id() {
		return customer_id;
	}

	/**
	 * @param customer_id
	 *            the customer_id to set
	 */
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	/**
	 * @return the customer_name
	 */
	public String getCustomer_name() {
		return customer_name;
	}

	/**
	 * @param customer_name
	 *            the customer_name to set
	 */
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	/**
	 * @return the address_id
	 */
	public JSONArray getAddress_id() {
		return address_id;
	}

	/**
	 * @param address_id
	 *            the address_id to set
	 */
	public void setAddress_id(JSONArray address_id) {
		this.address_id = address_id;
	}

}
