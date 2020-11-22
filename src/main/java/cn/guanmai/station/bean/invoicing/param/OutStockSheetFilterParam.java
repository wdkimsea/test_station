package cn.guanmai.station.bean.invoicing.param;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Feb 26, 2019 10:03:40 AM 
* @des 成品出库单搜索过滤参数封装类
* @version 1.0 
*/
public class OutStockSheetFilterParam {
	private int type = 1;
	private Integer status = 0;
	private Integer has_remark;
	private BigDecimal route_id;
	private String search_text;
	private int offset = 0;
	private int limit = 50;
	// 按运营周期专用参数
	private String time_config_id;
	private String cycle_start_time;
	private String cycle_end_time;

	private String start;
	private String start_date_new;
	private String end;
	private String end_date_new;
	private Integer export;
	private String address_label_id;

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * 1 按出库日期、2 按建单日期 、3 按运营周期
	 * 
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getHas_remark() {
		return has_remark;
	}

	public void setHas_remark(Integer has_remark) {
		this.has_remark = has_remark;
	}

	public BigDecimal getRoute_id() {
		return route_id;
	}

	public void setRoute_id(BigDecimal route_id) {
		this.route_id = route_id;
	}

	/**
	 * @return the search_text
	 */
	public String getSearch_text() {
		return search_text;
	}

	/**
	 * @param search_text the search_text to set
	 */
	public void setSearch_text(String search_text) {
		this.search_text = search_text;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the time_config_id
	 */
	public String getTime_config_id() {
		return time_config_id;
	}

	/**
	 * @param time_config_id the time_config_id to set
	 */
	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	/**
	 * @return the cycle_start_time
	 */
	public String getCycle_start_time() {
		return cycle_start_time;
	}

	/**
	 * @param cycle_start_time the cycle_start_time to set
	 */
	public void setCycle_start_time(String cycle_start_time) {
		this.cycle_start_time = cycle_start_time;
	}

	/**
	 * @return the cycle_end_time
	 */
	public String getCycle_end_time() {
		return cycle_end_time;
	}

	/**
	 * @param cycle_end_time the cycle_end_time to set
	 */
	public void setCycle_end_time(String cycle_end_time) {
		this.cycle_end_time = cycle_end_time;
	}

	/**
	 * @return the start
	 */
	public String getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(String start) {
		this.start = start;
	}

	public String getStart_date_new() {
		return start_date_new;
	}

	public void setStart_date_new(String start_date_new) {
		this.start_date_new = start_date_new;
	}

	public String getEnd_date_new() {
		return end_date_new;
	}

	public void setEnd_date_new(String end_date_new) {
		this.end_date_new = end_date_new;
	}

	/**
	 * @return the end
	 */
	public String getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(String end) {
		this.end = end;
	}

	/**
	 * @return the export
	 */
	public Integer getExport() {
		return export;
	}

	/**
	 * 导出成品出库单特有参数
	 * 
	 * @param export the export to set
	 */
	public void setExport() {
		this.export = 1;
	}

	public String getAddress_label_id() {
		return address_label_id;
	}

	public void setAddress_label_id(String address_label_id) {
		this.address_label_id = address_label_id;
	}

	public OutStockSheetFilterParam() {
		super();
	}

	/**
	 * 按运营周期搜索参数构造方法
	 * 
	 * @param status
	 * @param search_text
	 * @param offset
	 * @param limit
	 * @param time_config_id
	 * @param cycle_start_time
	 * @param cycle_end_time
	 */
	public OutStockSheetFilterParam(int status, String search_text, int offset, int limit, String time_config_id,
			String cycle_start_time, String cycle_end_time) {
		super();
		this.type = 3;
		this.status = status;
		this.search_text = search_text;
		this.offset = offset;
		this.limit = limit;
		this.time_config_id = time_config_id;
		this.cycle_start_time = cycle_start_time;
		this.cycle_end_time = cycle_end_time;
	}

	/**
	 * 按建单、出库日期搜索参数构造方法
	 * 
	 * @param type        1 出库日期、2 建单日期
	 * @param status
	 * @param search_text
	 * @param offset
	 * @param limit
	 * @param start
	 * @param end
	 */
	public OutStockSheetFilterParam(int type, int status, String search_text, int offset, int limit, String start,
			String end) {
		super();
		this.type = type;
		this.status = status;
		this.search_text = search_text;
		this.offset = offset;
		this.limit = limit;
		this.start = start;
		this.end = end;
	}

}
