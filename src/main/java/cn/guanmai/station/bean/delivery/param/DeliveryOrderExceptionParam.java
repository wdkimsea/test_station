package cn.guanmai.station.bean.delivery.param;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date May 21, 2019 7:42:31 PM 
* @des 司机APP添加售后参数
* @version 1.0 
*/
public class DeliveryOrderExceptionParam {
	private String order_id;

	private List<RefundParam> refunds;

	private List<ExceptionParam> exceptions;

	public class RefundParam {
		private String sku_id;
		private BigDecimal detail_id;
		private BigDecimal order_detail_id;
		private int exception_reason;
		private BigDecimal request_amount_forsale;
		private String station_blame_id;
		private String station_to_id;
		private String station_store_id;
		private BigDecimal lose_money;
		private String description;
		private int state;

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
		 * @return the exception_reason
		 */
		public int getException_reason() {
			return exception_reason;
		}

		/**
		 * @param exception_reason the exception_reason to set
		 */
		public void setException_reason(int exception_reason) {
			this.exception_reason = exception_reason;
		}

		public BigDecimal getDetail_id() {
			return detail_id;
		}

		public void setDetail_id(BigDecimal detail_id) {
			this.detail_id = detail_id;
		}

		public BigDecimal getOrder_detail_id() {
			return order_detail_id;
		}

		public void setOrder_detail_id(BigDecimal order_detail_id) {
			this.order_detail_id = order_detail_id;
		}

		public BigDecimal getRequest_amount_forsale() {
			return request_amount_forsale;
		}

		public void setRequest_amount_forsale(BigDecimal request_amount_forsale) {
			this.request_amount_forsale = request_amount_forsale;
		}

		/**
		 * @return the station_blame_id
		 */
		public String getStation_blame_id() {
			return station_blame_id;
		}

		/**
		 * @param station_blame_id the station_blame_id to set
		 */
		public void setStation_blame_id(String station_blame_id) {
			this.station_blame_id = station_blame_id;
		}

		/**
		 * @return the station_to_id
		 */
		public String getStation_to_id() {
			return station_to_id;
		}

		/**
		 * @param station_to_id the station_to_id to set
		 */
		public void setStation_to_id(String station_to_id) {
			this.station_to_id = station_to_id;
		}

		/**
		 * @return the station_store_id
		 */
		public String getStation_store_id() {
			return station_store_id;
		}

		/**
		 * @param station_store_id the station_store_id to set
		 */
		public void setStation_store_id(String station_store_id) {
			this.station_store_id = station_store_id;
		}

		/**
		 * @return the lose_money
		 */
		public BigDecimal getLose_money() {
			return lose_money;
		}

		/**
		 * @param lose_money the lose_money to set
		 */
		public void setLose_money(BigDecimal lose_money) {
			this.lose_money = lose_money;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
		}

		/**
		 * @return the state
		 */
		public int getState() {
			return state;
		}

		/**
		 * @param state the state to set
		 */
		public void setState(int state) {
			this.state = state;
		}
	}

	public class ExceptionParam {
		private String sku_id;
		private BigDecimal order_detail_id;
		private int exception_reason;
		private int solution;
		private BigDecimal money_delta;
		private BigDecimal lose_money;
		private String description;
		private BigDecimal final_amount_forsale;

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

		public BigDecimal getOrder_detail_id() {
			return order_detail_id;
		}

		public void setOrder_detail_id(BigDecimal order_detail_id) {
			this.order_detail_id = order_detail_id;
		}

		/**
		 * @return the exception_reason
		 */
		public int getException_reason() {
			return exception_reason;
		}

		/**
		 * @param exception_reason the exception_reason to set
		 */
		public void setException_reason(int exception_reason) {
			this.exception_reason = exception_reason;
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

		/**
		 * @return the lose_money
		 */
		public BigDecimal getLose_money() {
			return lose_money;
		}

		/**
		 * @param lose_money the lose_money to set
		 */
		public void setLose_money(BigDecimal lose_money) {
			this.lose_money = lose_money;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
		}

		public BigDecimal getFinal_amount_forsale() {
			return final_amount_forsale;
		}

		public void setFinal_amount_forsale(BigDecimal final_amount_forsale) {
			this.final_amount_forsale = final_amount_forsale;
		}

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
	 * @return the refunds
	 */
	public List<RefundParam> getRefunds() {
		return refunds;
	}

	/**
	 * @param refunds the refunds to set
	 */
	public void setRefunds(List<RefundParam> refunds) {
		this.refunds = refunds;
	}

	/**
	 * @return the exceptions
	 */
	public List<ExceptionParam> getExceptions() {
		return exceptions;
	}

	/**
	 * @param exceptions the exceptions to set
	 */
	public void setExceptions(List<ExceptionParam> exceptions) {
		this.exceptions = exceptions;
	}

}
