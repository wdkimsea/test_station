package cn.guanmai.station.bean.purchase.assistant;

/* 
* @author liming 
* @date May 23, 2019 8:12:06 PM 
* @des 采购助手APP今日任务
* @version 1.0 
*/
public class DailyWorkBean {
	private int purchase_sheets_created;
	private int purchase_tasks_todo;
	private int spus_quoted;

	/**
	 * @return the purchase_sheets_created
	 */
	public int getPurchase_sheets_created() {
		return purchase_sheets_created;
	}

	/**
	 * @param purchase_sheets_created
	 *            the purchase_sheets_created to set
	 */
	public void setPurchase_sheets_created(int purchase_sheets_created) {
		this.purchase_sheets_created = purchase_sheets_created;
	}

	/**
	 * @return the purchase_tasks_todo
	 */
	public int getPurchase_tasks_todo() {
		return purchase_tasks_todo;
	}

	/**
	 * @param purchase_tasks_todo
	 *            the purchase_tasks_todo to set
	 */
	public void setPurchase_tasks_todo(int purchase_tasks_todo) {
		this.purchase_tasks_todo = purchase_tasks_todo;
	}

	/**
	 * @return the spus_quoted
	 */
	public int getSpus_quoted() {
		return spus_quoted;
	}

	/**
	 * @param spus_quoted
	 *            the spus_quoted to set
	 */
	public void setSpus_quoted(int spus_quoted) {
		this.spus_quoted = spus_quoted;
	}

}
