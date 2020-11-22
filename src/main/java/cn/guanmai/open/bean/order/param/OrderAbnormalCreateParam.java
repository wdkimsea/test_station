package cn.guanmai.open.bean.order.param;

/* 
* @author liming 
* @date Jun 4, 2019 5:34:28 PM 
* @des 接口 /order/abnormal/create 对应的参数
* @version 1.0 
*/
public class OrderAbnormalCreateParam {
	private String sku_id;
	private String exception_reason;
	private String final_count;

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
	 * @return the final_count
	 */
	public String getFinal_count() {
		return final_count;
	}

	/**
	 * @param final_count
	 *            the final_count to set
	 */
	public void setFinal_count(String final_count) {
		this.final_count = final_count;
	}

}
