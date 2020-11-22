package cn.guanmai.station.bean.invoicing;

/* 
* @author liming 
* @date Feb 26, 2019 9:56:13 AM 
* @des 成品出库单列表
* @version 1.0 
*/
public class OutStockSheetBean {
	private String id;
	private String date_time;
	private String money;
	private String out_stock_target;
	private String out_stock_time;
	private String out_stock_remark;
	private String receive_begin_time;
	private int status;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the date_time
	 */
	public String getDate_time() {
		return date_time;
	}

	/**
	 * @param date_time the date_time to set
	 */
	public void setDate_time(String date_time) {
		this.date_time = date_time;
	}

	/**
	 * @return the money
	 */
	public String getMoney() {
		return money;
	}

	/**
	 * @param money the money to set
	 */
	public void setMoney(String money) {
		this.money = money;
	}

	/**
	 * @return the out_stock_target
	 */
	public String getOut_stock_target() {
		return out_stock_target;
	}

	/**
	 * @param out_stock_target the out_stock_target to set
	 */
	public void setOut_stock_target(String out_stock_target) {
		this.out_stock_target = out_stock_target;
	}

	public String getOut_stock_time() {
		return out_stock_time;
	}

	public void setOut_stock_time(String out_stock_time) {
		this.out_stock_time = out_stock_time;
	}

	public String getOut_stock_remark() {
		return out_stock_remark;
	}

	public void setOut_stock_remark(String out_stock_remark) {
		this.out_stock_remark = out_stock_remark;
	}

	public String getReceive_begin_time() {
		return receive_begin_time;
	}

	public void setReceive_begin_time(String receive_begin_time) {
		this.receive_begin_time = receive_begin_time;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

}
