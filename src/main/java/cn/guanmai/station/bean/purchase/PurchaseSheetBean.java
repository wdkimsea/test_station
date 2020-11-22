package cn.guanmai.station.bean.purchase;

/* 
* @author liming 
* @date Nov 28, 2018 5:55:31 PM 
* @des 采购单据类
* @version 1.0 
*/
public class PurchaseSheetBean {
	private String create_time;
	private String id;
	private String in_stock_time;
	private String modify_time;
	private String operator;
	private String operator_id;

	/**
	 * @return the create_time
	 */
	public String getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time
	 *            the create_time to set
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the in_stock_time
	 */
	public String getIn_stock_time() {
		return in_stock_time;
	}

	/**
	 * @param in_stock_time
	 *            the in_stock_time to set
	 */
	public void setIn_stock_time(String in_stock_time) {
		this.in_stock_time = in_stock_time;
	}

	/**
	 * @return the modify_time
	 */
	public String getModify_time() {
		return modify_time;
	}

	/**
	 * @param modify_time
	 *            the modify_time to set
	 */
	public void setModify_time(String modify_time) {
		this.modify_time = modify_time;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @return the operator_id
	 */
	public String getOperator_id() {
		return operator_id;
	}

	/**
	 * @param operator_id
	 *            the operator_id to set
	 */
	public void setOperator_id(String operator_id) {
		this.operator_id = operator_id;
	}

	private int status;

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
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

}
