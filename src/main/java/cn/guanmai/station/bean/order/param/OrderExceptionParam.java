package cn.guanmai.station.bean.order.param;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jan 3, 2019 2:17:54 PM 
* @des 订单异常类
* @version 1.0 
*/
public class OrderExceptionParam {
	private BigDecimal final_amount;
	private String sku_id;
	private int exception_reason;
	private int exception_info;
	private int solution;

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
	 * @return the exception_info
	 */
	public int getException_info() {
		return exception_info;
	}

	/**
	 * @param exception_info
	 *            the exception_info to set
	 */
	public void setException_info(int exception_info) {
		this.exception_info = exception_info;
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

	public OrderExceptionParam(BigDecimal final_amount, String sku_id, int exception_reason, int exception_info,
			int solution) {
		super();
		this.final_amount = final_amount;
		this.sku_id = sku_id;
		this.exception_reason = exception_reason;
		this.exception_info = exception_info;
		this.solution = solution;
	}

}
