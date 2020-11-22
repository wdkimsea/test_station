package cn.guanmai.station.bean.weight.param;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONArray;

/**
 * @author liming
 * @date 2019年7月31日 下午4:25:09
 * @des 接口 /weight/pda/sku/detail 对应的参数
 * @version 1.0
 */
public class PdaWeightSkuDetailFilterParam {
	private JSONArray sku_ids;
	private String date;
	private String time_config_id;
	private Integer status;
	private Integer sort_status;
	private BigDecimal route_id;
	private String search_text;

	/**
	 * @return the sku_ids
	 */
	public JSONArray getSku_ids() {
		return sku_ids;
	}

	/**
	 * @param sku_ids the sku_ids to set
	 */
	public void setSku_ids(JSONArray sku_ids) {
		this.sku_ids = sku_ids;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
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
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 订单状态 5 分拣中、10 配送中、15已签收
	 * 
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the sort_status
	 */
	public Integer getSort_status() {
		return sort_status;
	}

	/**
	 * 商品分拣状态, 1 未分拣、2 已分拣、3 缺货、4已打印
	 * 
	 * @param sort_status the sort_status to set
	 */
	public void setSort_status(Integer sort_status) {
		this.sort_status = sort_status;
	}

	/**
	 * @return the route_id
	 */
	public BigDecimal getRoute_id() {
		return route_id;
	}

	/**
	 * @param route_id the route_id to set
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
	 * @param search_text the search_text to set
	 */
	public void setSearch_text(String search_text) {
		this.search_text = search_text;
	}

}
