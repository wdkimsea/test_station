package cn.guanmai.station.bean.invoicing.param;

/* 
* @author liming 
* @date Jan 7, 2019 11:14:21 AM 
* @des 商户退货搜索过滤参数类
* @version 1.0 
*/
public class RefundStockFilterParam {
	private String date_from;
	private String date_end;
	private Integer state;
	private String station_id;
	private String station_store_id;
	private String order_id;
	private String sid;
	private String resname;
	private int page = 0;
	private int num = 20;

	/**
	 * @return the date_from
	 */
	public String getDate_from() {
		return date_from;
	}

	/**
	 * @param date_from the date_from to set
	 */
	public void setDate_from(String date_from) {
		this.date_from = date_from;
	}

	/**
	 * @return the date_end
	 */
	public String getDate_end() {
		return date_end;
	}

	/**
	 * @param date_end the date_end to set
	 */
	public void setDate_end(String date_end) {
		this.date_end = date_end;
	}

	/**
	 * @return the state
	 */
	public Integer getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(Integer state) {
		this.state = state;
	}

	/**
	 * @return the station_id
	 */
	public String getStation_id() {
		return station_id;
	}

	/**
	 * @param station_id the station_id to set
	 */
	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	/**
	 * @return the station_store_id
	 */
	public String getStation_store_id() {
		return station_store_id;
	}

	/**
	 * @param station_store_id the station_store_id to set
	 */
	public void setStation_store_id(String station_store_id) {
		this.station_store_id = station_store_id;
	}

	/**
	 * @return the order_id
	 */
	public String getOrder_id() {
		return order_id;
	}

	/**
	 * @param order_id the order_id to set
	 */
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	/**
	 * @return the sid
	 */
	public String getSid() {
		return sid;
	}

	/**
	 * @param sid the sid to set
	 */
	public void setSid(String sid) {
		this.sid = sid;
	}

	/**
	 * @return the resname
	 */
	public String getResname() {
		return resname;
	}

	/**
	 * @param resname the resname to set
	 */
	public void setResname(String resname) {
		this.resname = resname;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return the num
	 */
	public int getNum() {
		return num;
	}

	/**
	 * @param num the num to set
	 */
	public void setNum(int num) {
		this.num = num;
	}

}
