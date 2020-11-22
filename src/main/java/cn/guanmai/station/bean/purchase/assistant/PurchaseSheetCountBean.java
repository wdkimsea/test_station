package cn.guanmai.station.bean.purchase.assistant;

/* 
* @author liming 
* @date May 27, 2019 3:07:05 PM 
* @des 接口 /purchase_assistant/purchase_sheet_count/get 对应的结果
*      采购助手APP采购单据页面,采购单据数据汇总结果
* @version 1.0 
*/
public class PurchaseSheetCountBean {
	private int all_purchase_sheet_num;
	private int edit_purchase_sheet_num;
	private String purchaser_id;
	private String station_id;

	/**
	 * @return the all_purchase_sheet_num
	 */
	public int getAll_purchase_sheet_num() {
		return all_purchase_sheet_num;
	}

	/**
	 * @param all_purchase_sheet_num
	 *            the all_purchase_sheet_num to set
	 */
	public void setAll_purchase_sheet_num(int all_purchase_sheet_num) {
		this.all_purchase_sheet_num = all_purchase_sheet_num;
	}

	/**
	 * @return the edit_purchase_sheet_num
	 */
	public int getEdit_purchase_sheet_num() {
		return edit_purchase_sheet_num;
	}

	/**
	 * @param edit_purchase_sheet_num
	 *            the edit_purchase_sheet_num to set
	 */
	public void setEdit_purchase_sheet_num(int edit_purchase_sheet_num) {
		this.edit_purchase_sheet_num = edit_purchase_sheet_num;
	}

	/**
	 * @return the purchaser_id
	 */
	public String getPurchaser_id() {
		return purchaser_id;
	}

	/**
	 * @param purchaser_id
	 *            the purchaser_id to set
	 */
	public void setPurchaser_id(String purchaser_id) {
		this.purchaser_id = purchaser_id;
	}

	/**
	 * @return the station_id
	 */
	public String getStation_id() {
		return station_id;
	}

	/**
	 * @param station_id
	 *            the station_id to set
	 */
	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}
}
