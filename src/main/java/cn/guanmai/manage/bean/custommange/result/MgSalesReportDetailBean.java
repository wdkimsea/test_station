package cn.guanmai.manage.bean.custommange.result;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liming
 * @Date: 2020年7月30日 下午7:45:37
 * @description:
 * @version: 1.0
 */

public class MgSalesReportDetailBean {
	private int all_address_num;
	private int new_address_num;

	private List<OrderAddress> order_address;

	private List<Order> orders;

	public class OrderAddress {
		private String address_id;
		private String address_name;
		private String fist_order_time;
		private String last_order_time;
		private String register_time;

		public String getAddress_id() {
			return address_id;
		}

		public void setAddress_id(String address_id) {
			this.address_id = address_id;
		}

		public String getAddress_name() {
			return address_name;
		}

		public void setAddress_name(String address_name) {
			this.address_name = address_name;
		}

		public String getFist_order_time() {
			return fist_order_time;
		}

		public void setFist_order_time(String fist_order_time) {
			this.fist_order_time = fist_order_time;
		}

		public String getLast_order_time() {
			return last_order_time;
		}

		public void setLast_order_time(String last_order_time) {
			this.last_order_time = last_order_time;
		}

		public String getRegister_time() {
			return register_time;
		}

		public void setRegister_time(String register_time) {
			this.register_time = register_time;
		}
	}

	public class Order {
		private String address_id;
		private String address_name;
		private String order_id;
		private BigDecimal total_price;

		public String getAddress_id() {
			return address_id;
		}

		public void setAddress_id(String address_id) {
			this.address_id = address_id;
		}

		public String getAddress_name() {
			return address_name;
		}

		public void setAddress_name(String address_name) {
			this.address_name = address_name;
		}

		public String getOrder_id() {
			return order_id;
		}

		public void setOrder_id(String order_id) {
			this.order_id = order_id;
		}

		public BigDecimal getTotal_price() {
			return total_price;
		}

		public void setTotal_price(BigDecimal total_price) {
			this.total_price = total_price;
		}
	}

	public int getAll_address_num() {
		return all_address_num;
	}

	public void setAll_address_num(int all_address_num) {
		this.all_address_num = all_address_num;
	}

	public int getNew_address_num() {
		return new_address_num;
	}

	public void setNew_address_num(int new_address_num) {
		this.new_address_num = new_address_num;
	}

	public List<OrderAddress> getOrder_address() {
		return order_address;
	}

	public void setOrder_address(List<OrderAddress> order_address) {
		this.order_address = order_address;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
}
