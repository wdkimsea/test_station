package cn.guanmai.open.bean.order.param;

/* 
* @author liming 
* @date Jun 5, 2019 11:37:42 AM 
* @des 接口 /order/after_sales/update 对应的参数
* @version 1.0 
*/
public class OrderRefundUpdateParam {
	private String id;
	private String sku_id;
	private String exception_reason;
	private String request_count;

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
	public String getException_reason() {
		return exception_reason;
	}

	/**
	 * @param exception_reason
	 *            the exception_reason to set
	 */
	public void setException_reason(String exception_reason) {
		this.exception_reason = exception_reason;
	}

	/**
	 * @return the request_count
	 */
	public String getRequest_count() {
		return request_count;
	}

	/**
	 * @param request_count
	 *            the request_count to set
	 */
	public void setRequest_count(String request_count) {
		this.request_count = request_count;
	}

}
