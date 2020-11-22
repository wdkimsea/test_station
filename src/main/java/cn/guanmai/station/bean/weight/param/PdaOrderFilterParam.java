package cn.guanmai.station.bean.weight.param;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年7月31日 下午6:11:59
 * @des 接口 /weight/pda/order/list 对应的参数
 * @version 1.0
 */
public class PdaOrderFilterParam {
	private String time_config_id;
	private String date;
	private Integer status;
	private BigDecimal route_id;
	private String search_text;
	private String page_obj;

	/**
	 * @return the time_config_id
	 */
	public String getTime_config_id() {
		return time_config_id;
	}

	/**
	 * @param time_config_id
	 *            the time_config_id to set
	 */
	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the route_id
	 */
	public BigDecimal getRoute_id() {
		return route_id;
	}

	/**
	 * @param route_id
	 *            the route_id to set
	 */
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
	 * @param search_text
	 *            the search_text to set
	 */
	public void setSearch_text(String search_text) {
		this.search_text = search_text;
	}

	/**
	 * @return the page_obj
	 */
	public String getPage_obj() {
		return page_obj;
	}

	/**
	 * @param page_obj
	 *            the page_obj to set
	 */
	public void setPage_obj(String page_obj) {
		this.page_obj = page_obj;
	}

}
