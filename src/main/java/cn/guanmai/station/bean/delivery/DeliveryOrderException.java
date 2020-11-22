package cn.guanmai.station.bean.delivery;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date May 21, 2019 7:20:03 PM 
* @des 司机APP获取的订单异常信息
* @version 1.0 
*/
public class DeliveryOrderException {
	@JSONField(name="order_no")
	private String order_id;

	private List<Detail> details;

	private List<SaleException> exceptions;

	private List<SaleRefund> refunds;

	public class Detail {
		@JSONField(name="id")
		private String sku_id;
		@JSONField(name="name")
		private String sku_name;
		private BigDecimal real_quantity;
		private BigDecimal sale_price;
		private BigDecimal sale_ratio;
		private BigDecimal std_sale_price;
		private String std_unit_name;

		/**
		 * @return the sku_id
		 */
		public String getSku_id() {
			return sku_id;
		}

		/**
		 * @param sku_id the sku_id to set
		 */
		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		/**
		 * @return the sku_name
		 */
		public String getSku_name() {
			return sku_name;
		}

		/**
		 * @param sku_name the sku_name to set
		 */
		public void setSku_name(String sku_name) {
			this.sku_name = sku_name;
		}

		/**
		 * @return the real_quantity
		 */
		public BigDecimal getReal_quantity() {
			return real_quantity;
		}

		/**
		 * @param real_quantity the real_quantity to set
		 */
		public void setReal_quantity(BigDecimal real_quantity) {
			this.real_quantity = real_quantity;
		}

		/**
		 * @return the sale_price
		 */
		public BigDecimal getSale_price() {
			return sale_price.divide(new BigDecimal("100"));
		}

		/**
		 * @param sale_price the sale_price to set
		 */
		public void setSale_price(BigDecimal sale_price) {
			this.sale_price = sale_price;
		}

		/**
		 * @return the sale_ratio
		 */
		public BigDecimal getSale_ratio() {
			return sale_ratio;
		}

		/**
		 * @param sale_ratio the sale_ratio to set
		 */
		public void setSale_ratio(BigDecimal sale_ratio) {
			this.sale_ratio = sale_ratio;
		}

		/**
		 * @return the std_sale_price
		 */
		public BigDecimal getStd_sale_price() {
			return std_sale_price.divide(new BigDecimal("100"));
		}

		/**
		 * @param std_sale_price the std_sale_price to set
		 */
		public void setStd_sale_price(BigDecimal std_sale_price) {
			this.std_sale_price = std_sale_price;
		}

		/**
		 * @return the std_unit_name
		 */
		public String getStd_unit_name() {
			return std_unit_name;
		}

		/**
		 * @param std_unit_name the std_unit_name to set
		 */
		public void setStd_unit_name(String std_unit_name) {
			this.std_unit_name = std_unit_name;
		}
	}

	public class SaleException {
		private String id;
		private String sku_id;
		private String sku_name;
		private int solution;
		private BigDecimal final_amount_forsale;
		private BigDecimal money_delta;

		/**
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
		 * @return the sku_id
		 */
		public String getSku_id() {
			return sku_id;
		}

		/**
		 * @param sku_id the sku_id to set
		 */
		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		/**
		 * @return the sku_name
		 */
		public String getSku_name() {
			return sku_name;
		}

		/**
		 * @param sku_name the sku_name to set
		 */
		public void setSku_name(String sku_name) {
			this.sku_name = sku_name;
		}

		/**
		 * @return the solution
		 */
		public int getSolution() {
			return solution;
		}

		/**
		 * @param solution the solution to set
		 */
		public void setSolution(int solution) {
			this.solution = solution;
		}

		public BigDecimal getFinal_amount_forsale() {
			return final_amount_forsale;
		}

		public void setFinal_amount_forsale(BigDecimal final_amount_forsale) {
			this.final_amount_forsale = final_amount_forsale;
		}

		/**
		 * @return the money_delta
		 */
		public BigDecimal getMoney_delta() {
			return money_delta;
		}

		/**
		 * @param money_delta the money_delta to set
		 */
		public void setMoney_delta(BigDecimal money_delta) {
			this.money_delta = money_delta;
		}

	}

	public class SaleRefund {
		private String id;
		private String sku_id;
		private String sku_name;
		private BigDecimal request_amount_forsale;
		private BigDecimal request_amount_money;

		/**
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
		 * @return the sku_id
		 */
		public String getSku_id() {
			return sku_id;
		}

		/**
		 * @param sku_id the sku_id to set
		 */
		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		/**
		 * @return the sku_name
		 */
		public String getSku_name() {
			return sku_name;
		}

		/**
		 * @param sku_name the sku_name to set
		 */
		public void setSku_name(String sku_name) {
			this.sku_name = sku_name;
		}

		public BigDecimal getRequest_amount_forsale() {
			return request_amount_forsale;
		}

		public void setRequest_amount_forsale(BigDecimal request_amount_forsale) {
			this.request_amount_forsale = request_amount_forsale;
		}

		/**
		 * @return the request_amount_money
		 */
		public BigDecimal getRequest_amount_money() {
			return request_amount_money;
		}

		/**
		 * @param request_amount_money the request_amount_money to set
		 */
		public void setRequest_amount_money(BigDecimal request_amount_money) {
			this.request_amount_money = request_amount_money;
		}
	}

	/**
	 * @return the exceptions
	 */
	public List<SaleException> getExceptions() {
		return exceptions;
	}

	/**
	 * @param exceptions the exceptions to set
	 */
	public void setExceptions(List<SaleException> exceptions) {
		this.exceptions = exceptions;
	}

	/**
	 * @return the refunds
	 */
	public List<SaleRefund> getRefunds() {
		return refunds;
	}

	/**
	 * @param refunds the refunds to set
	 */
	public void setRefunds(List<SaleRefund> refunds) {
		this.refunds = refunds;
	}

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

	/**
	 * @return the details
	 */
	public List<Detail> getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(List<Detail> details) {
		this.details = details;
	}

}
