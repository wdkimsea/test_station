package cn.guanmai.open.bean.delivery;

import java.util.List;

/* 
* @author liming 
* @date Jun 5, 2019 7:14:49 PM 
* @des 接口 /delivery/route/get 对应的结果
* @version 1.0 
*/
public class OpenRouteDetailBean {
	private String route_id;
	private String route_name;
	private List<Customer> customers;

	public class Customer {
		private String customer_id;
		private String customer_name;
		private String area;

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
		 * @return the name
		 */
		public String getCustomerName() {
			return customer_name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setCustomerName(String name) {
			this.customer_name = name;
		}

		/**
		 * @return the area
		 */
		public String getArea() {
			return area;
		}

		/**
		 * @param area
		 *            the area to set
		 */
		public void setArea(String area) {
			this.area = area;
		}
	}

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
	 * @return the customers
	 */
	public List<Customer> getCustomers() {
		return customers;
	}

	/**
	 * @param customers
	 *            the customers to set
	 */
	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}
}
