package cn.guanmai.station.bean.weight.param;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年7月8日 上午11:43:03
 * @des 接口 /weight/weight_collect/order/list 对应的参数,ST-供应链-分拣-分拣明细-按订单分拣
 * @version 1.0
 */
public class WeightCollectOrderFilterParam {
	private String time_config_id;
	private String start_date;
	private String end_date;
	private String search;
	private BigDecimal route_id;
	private BigDecimal driver_id;
	private Integer status;
	private Integer sort_status;
	private Integer inspect_status;
	private Integer print_status;
	private String order_process_type_id;

	private int need_details;
	private int limit = 10;
	private int offset;

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
	 * @return the start_date
	 */
	public String getStart_date() {
		return start_date;
	}

	/**
	 * @param start_date the start_date to set
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
	 * @param end_date the end_date to set
	 */
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public BigDecimal getRoute_id() {
		return route_id;
	}

	public void setRoute_id(BigDecimal route_id) {
		this.route_id = route_id;
	}

	public BigDecimal getDriver_id() {
		return driver_id;
	}

	public void setDriver_id(BigDecimal driver_id) {
		this.driver_id = driver_id;
	}

	public Integer getInspect_status() {
		return inspect_status;
	}

	public void setInspect_status(Integer inspect_status) {
		this.inspect_status = inspect_status;
	}

	public Integer getPrint_status() {
		return print_status;
	}

	public void setPrint_status(Integer print_status) {
		this.print_status = print_status;
	}

	public String getOrder_process_type_id() {
		return order_process_type_id;
	}

	public void setOrder_process_type_id(String order_process_type_id) {
		this.order_process_type_id = order_process_type_id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSort_status() {
		return sort_status;
	}

	public void setSort_status(Integer sort_status) {
		this.sort_status = sort_status;
	}

	/**
	 * @return the need_details
	 */
	public int getNeed_details() {
		return need_details;
	}

	/**
	 * @param need_details the need_details to set
	 */
	public void setNeed_details(int need_details) {
		this.need_details = need_details;
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

}
