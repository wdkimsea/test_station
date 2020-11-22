package cn.guanmai.manage.bean.finance.result;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Jan 17, 2019 11:40:05 AM 
* @des 商户结算订单列表类
* @version 1.0 
*/
public class FinanceOrderBean {
	@JSONField(name="id")
	private String order_id;

	private BigDecimal sale_money;

	/**
	 * @return the sale_money
	 */
	public BigDecimal getSale_money() {
		return sale_money;
	}

	/**
	 * @param sale_money
	 *            the sale_money to set
	 */
	public void setSale_money(BigDecimal sale_money) {
		this.sale_money = sale_money;
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

	private Customer customer;

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public class Customer {
		private String uid;

		/**
		 * @return the uid
		 */
		public String getUid() {
			return uid;
		}

		/**
		 * @param uid
		 *            the uid to set
		 */
		public void setUid(String uid) {
			this.uid = uid;
		}
	}
}
