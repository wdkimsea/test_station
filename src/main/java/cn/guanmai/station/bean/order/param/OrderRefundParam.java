package cn.guanmai.station.bean.order.param;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jan 3, 2019 2:18:28 PM 
* @des 订单退货类
* @version 1.0 
*/
public class OrderRefundParam {
	private BigDecimal request_amount;
	private String sku_id;
	private String station_store_id;
	private int exception_reason;
	private int exception_info;

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
	 * @return the station_store_id
	 */
	public String getStation_store_id() {
		return station_store_id;
	}

	/**
	 * @param station_store_id
	 *            the station_store_id to set
	 */
	public void setStation_store_id(String station_store_id) {
		this.station_store_id = station_store_id;
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

	public OrderRefundParam(BigDecimal request_amount, String sku_id, String station_store_id, int exception_reason,
			int exception_info) {
		super();
		this.request_amount = request_amount;
		this.sku_id = sku_id;
		this.station_store_id = station_store_id;
		this.exception_reason = exception_reason;
		this.exception_info = exception_info;
	}

}
