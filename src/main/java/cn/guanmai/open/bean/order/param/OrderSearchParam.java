package cn.guanmai.open.bean.order.param;

/* 
* @author liming 
* @date Jun 4, 2019 5:04:33 PM 
* @des 接口 /order/list 对应的参数
* @version 1.0 
*/
public class OrderSearchParam {
	private String query_type;
	private String start_date;
	private String end_date;
	private String status;
	private String pay_status;
	private String offset;
	private String limit;

	/**
	 * @return the query_type
	 */
	public String getQuery_type() {
		return query_type;
	}

	/**
	 * @param query_type
	 *            the query_type to set
	 */
	public void setQuery_type(String query_type) {
		this.query_type = query_type;
	}

	/**
	 * @return the start_date
	 */
	public String getStart_date() {
		return start_date;
	}

	/**
	 * @param start_date
	 *            the start_date to set
	 */
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	/**
	 * @return the end_date
	 */
	public String getEnd_date() {
		return end_date;
	}

	/**
	 * @param end_date
	 *            the end_date to set
	 */
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the pay_status
	 */
	public String getPay_status() {
		return pay_status;
	}

	/**
	 * @param pay_status
	 *            the pay_status to set
	 */
	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}

	/**
	 * @return the offset
	 */
	public String getOffset() {
		return offset;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(String offset) {
		this.offset = offset;
	}

	/**
	 * @return the limit
	 */
	public String getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(String limit) {
		this.limit = limit;
	}
}
