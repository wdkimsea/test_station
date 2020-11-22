package cn.guanmai.station.bean.order.param;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONArray;

/* 
* @author liming 
* @date Jan 3, 2019 10:53:13 AM 
* @des 按商品查看搜索参数类
* @version 1.0 
*/
public class OrderSkuFilterParam {
	private String start_date;
	private String start_date_new; // 新版UI
	private String end_date;
	private String end_date_new; // 新版UI
	private String time_config_id;
	private int query_type;
	private String search_text;
	private JSONArray category1_ids;
	private JSONArray category2_ids;
	private JSONArray pinlei_ids;
	private Integer offset;
	private Integer limit;

	// 任务分拣备注
	private Integer batch_remark_is_null;

	private String batch_remark;

	// 是否记重商品
	private Integer is_weigh;

	// 是否已经称重
	private Integer weighted;

	// 订单状态
	private Integer status;

	// 支付状态
	private Integer pay_status;

	// 报价单ID
	private String salemenu_id;

	// 线路
	private BigDecimal route_id;

	/**
	 * @return the start_date
	 */
	public String getStart_date() {
		return start_date;
	}

	/**
	 * 搜索起始时间,必填参数,按下单日期和收货日期搜索参数格式 yyyy-MM-dd、按运营时间搜索参数格式 yyyy-MM-dd hh:mm
	 * 
	 * @param start_date the start_date to set
	 */
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	/**
	 * 新版UI,参数格式 yyyy-MM-dd 00:00
	 * 
	 * @return
	 */
	public String getStart_date_new() {
		return start_date_new;
	}

	/**
	 * 新版UI,参数格式 yyyy-MM-dd 00:00
	 * 
	 * @param start_date_new
	 */
	public void setStart_date_new(String start_date_new) {
		this.start_date_new = start_date_new;
	}

	/**
	 * 新版UI,参数格式 yyyy-MM-dd 00:00
	 * 
	 * @return
	 */
	public String getEnd_date_new() {
		return end_date_new;
	}

	/**
	 * 新版UI,参数格式 yyyy-MM-dd 00:00
	 * 
	 * @param end_date_new
	 */
	public void setEnd_date_new(String end_date_new) {
		this.end_date_new = end_date_new;
	}

	/**
	 * 搜索结束时间,必填参数,按下单日期和收货日期搜索参数格式 yyyy-MM-dd、按运营时间搜索参数格式 yyyy-MM-dd hh:mm
	 * 
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

	/**
	 * @return the time_config_id
	 */
	public String getTime_config_id() {
		return time_config_id;
	}

	/**
	 * 运营时间ID,搜索过滤非必填参数,按运营时间搜索商品的时候才传入
	 * 
	 * @param time_config_id the time_config_id to set
	 */
	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	/**
	 * @return the query_type
	 */
	public int getQuery_type() {
		return query_type;
	}

	/**
	 * 筛选方式,必填参数,1=按下单日期、2=按运营周期、3=按收货日期
	 * 
	 * @param query_type the query_type to set
	 */
	public void setQuery_type(int query_type) {
		this.query_type = query_type;
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
	 * @return the category1_ids
	 */
	public JSONArray getCategory1_ids() {
		return category1_ids;
	}

	/**
	 * @param category1_ids the category1_ids to set
	 */
	public void setCategory1_ids(JSONArray category1_ids) {
		this.category1_ids = category1_ids;
	}

	/**
	 * @return the category2_ids
	 */
	public JSONArray getCategory2_ids() {
		return category2_ids;
	}

	/**
	 * @param category2_ids the category2_ids to set
	 */
	public void setCategory2_ids(JSONArray category2_ids) {
		this.category2_ids = category2_ids;
	}

	/**
	 * @return the pinlei_ids
	 */
	public JSONArray getPinlei_ids() {
		return pinlei_ids;
	}

	/**
	 * @param pinlei_ids the pinlei_ids to set
	 */
	public void setPinlei_ids(JSONArray pinlei_ids) {
		this.pinlei_ids = pinlei_ids;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * 搜索起始值,必填参数,从0开始,每次加 limit 的值
	 * 
	 * @param offset the offset to set
	 */
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * 每页页数,必填参数,默认值为20
	 * 
	 * @param limit the limit to set
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	/**
	 * @return the batch_remark_is_null
	 */
	public Integer getBatch_remark_is_null() {
		return batch_remark_is_null;
	}

	/**
	 * 任务分拣备注,订单按商品查看搜索过滤非必填参数,1=无分拣备注
	 * 
	 * @param batch_remark_is_null the batch_remark_is_null to set
	 */
	public void setBatch_remark_is_null(Integer batch_remark_is_null) {
		this.batch_remark_is_null = batch_remark_is_null;
	}

	/**
	 * @return the batch_remark
	 */
	public String getBatch_remark() {
		return batch_remark;
	}

	/**
	 * 分拣备注,由把订单状态改为分拣中设置的分拣备注得来
	 * 
	 * @param batch_remark the batch_remark to set
	 */
	public void setBatch_remark(String batch_remark) {
		this.batch_remark = batch_remark;
	}

	/**
	 * @return the is_weigh
	 */
	public Integer getIs_weigh() {
		return is_weigh;
	}

	/**
	 * 是否计重商品,订单按商品查看搜索过滤非必填参数,0=不计重商品、1=计重商品
	 * 
	 * @param is_weigh the is_weigh to set
	 */
	public void setIs_weigh(Integer is_weigh) {
		this.is_weigh = is_weigh;
	}

	/**
	 * @return the weighted
	 */
	public Integer getWeighted() {
		return weighted;
	}

	/**
	 * 是否已经商品,订单按商品查看搜索过滤非必填参数,0=未称重、1=已称重
	 * 
	 * @param weighted the weighted to set
	 */
	public void setWeighted(Integer weighted) {
		this.weighted = weighted;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 订单状态,订单按商品查看搜索过滤非必填参数,1=待分拣、5=分拣中、10=配送中、15=已签收
	 * 
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the pay_status
	 */
	public Integer getPay_status() {
		return pay_status;
	}

	/**
	 * 支付状态,订单按商品查看搜索过滤非必填参数,1=未支付、5=部分支付、10=已支付
	 * 
	 * @param pay_status the pay_status to set
	 */
	public void setPay_status(Integer pay_status) {
		this.pay_status = pay_status;
	}

	/**
	 * @return the salemenu_id
	 */
	public String getSalemenu_id() {
		return salemenu_id;
	}

	/**
	 * 报价单ID,订单按商品查看搜索过滤非必填参数
	 * 
	 * @param salemenu_id the salemenu_id to set
	 */
	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
	}

	/**
	 * @return the route_id
	 */
	public BigDecimal getRoute_id() {
		return route_id;
	}

	/**
	 * 线路ID,订单按商品查看搜索过滤非必填参数
	 * 
	 * @param route_id the route_id to set
	 */
	public void setRoute_id(BigDecimal route_id) {
		this.route_id = route_id;
	}

	public OrderSkuFilterParam() {
		super();
	}

	public OrderSkuFilterParam(int query_type, String start_date, String end_date, String search_text, Integer offset,
			int limit) {
		super();
		this.query_type = query_type;
		this.start_date = start_date;
		this.end_date = end_date;
		this.search_text = search_text;
		this.offset = offset;
		this.limit = limit;
	}

}
