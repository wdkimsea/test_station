package cn.guanmai.manage.bean.ordermanage.param;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date Jan 22, 2019 3:47:19 PM 
* @todo TODO
* @version 1.0 
*/
public class OrderExceptionParamBean {
	private String token;
	private String id;
	private List<OrderException> exceptions;
	private List<OrderRefund> refunds;
	private List<OrderRemark> remarks;

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the exceptions
	 */
	public List<OrderException> getExceptions() {
		return exceptions;
	}

	/**
	 * @param exceptions
	 *            the exceptions to set
	 */
	public void setExceptions(List<OrderException> exceptions) {
		this.exceptions = exceptions;
	}

	/**
	 * @return the refunds
	 */
	public List<OrderRefund> getRefunds() {
		return refunds;
	}

	/**
	 * @param refunds
	 *            the refunds to set
	 */
	public void setRefunds(List<OrderRefund> refunds) {
		this.refunds = refunds;
	}

	/**
	 * @return the remarks
	 */
	public List<OrderRemark> getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks
	 *            the remarks to set
	 */
	public void setRemarks(List<OrderRemark> remarks) {
		this.remarks = remarks;
	}

	public class OrderRefund {
		private String sku_id;
		private int exception_reason;
		private BigDecimal request_amount;
		private int driver_id;

		/**
		 * @return the sku_id
		 */
		public String getSku_id() {
			return sku_id;
		}

		/**
		 * @param sku_id
		 *            the sku_id to set
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
		 * @return the driver_id
		 */
		public int getDriver_id() {
			return driver_id;
		}

		/**
		 * @param driver_id
		 *            the driver_id to set
		 */
		public void setDriver_id(int driver_id) {
			this.driver_id = driver_id;
		}

		/**
		 * @param exception_reason
		 *            the exception_reason to set
		 */
		public void setException_reason(int exception_reason) {
			this.exception_reason = exception_reason;
		}

		/**
		 * @return the request_amount
		 */
		public BigDecimal getRequest_amount() {
			return request_amount;
		}

		/**
		 * @param request_amount
		 *            the request_amount to set
		 */
		public void setRequest_amount(BigDecimal request_amount) {
			this.request_amount = request_amount;
		}

	}

	public class OrderException {
		private String sku_id;
		private int exception_reason;
		private int solution;
		private BigDecimal final_amount;
		private BigDecimal money_delta;

		/**
		 * @return the sku_id
		 */
		public String getSku_id() {
			return sku_id;
		}

		/**
		 * @param sku_id
		 *            the sku_id to set
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
		 * @param exception_reason
		 *            the exception_reason to set
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
		 * @param solution
		 *            the solution to set
		 */
		public void setSolution(int solution) {
			this.solution = solution;
		}

		/**
		 * @return the final_amount
		 */
		public BigDecimal getFinal_amount() {
			return final_amount;
		}

		/**
		 * @param final_amount
		 *            the final_amount to set
		 */
		public void setFinal_amount(BigDecimal final_amount) {
			this.final_amount = final_amount;
		}

		/**
		 * @return the money_delta
		 */
		public BigDecimal getMoney_delta() {
			return money_delta;
		}

		/**
		 * @param money_delta
		 *            the money_delta to set
		 */
		public void setMoney_delta(BigDecimal money_delta) {
			this.money_delta = money_delta;
		}

	}

	public class OrderRemark {

	}
}
