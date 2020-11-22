package cn.guanmai.station.bean.order.param;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jan 7, 2019 10:21:19 AM 
* @des 订单列表搜索过滤参数类
* @version 1.0 
*/
public class OrderFilterParam {
	private Integer search_type; // 新版UI特有参数
	private String start_date_new; // 新版UI特有参数
	private String end_date_new; // 新版UI特有参数

	private String start_date;
	private String end_date;
	private int query_type;
	private String search_text;
	private int offset;
	private int limit;

	private String receive_start_date;
	private String receive_end_date;

	private String receive_start_date_new; // 新UI特有参数
	private String receive_end_date_new; // 新UI特有参数

	private String time_config_id;
	private String cycle_start_time;
	private String cycle_end_time;

	// 订单状态
	private Integer status;

	// 出库状态
	private Integer stock_type;

	// 支付状态
	private Integer pay_status;

	// 地区编码
	private String search_area;

	// 配送线路ID
	private BigDecimal route_id;

	// 报价单
	private String salemenu_id;

	// 配送运营商
	private BigDecimal carrier_id;

	// 配送司机ID
	private BigDecimal driver_id;

	// 是否打印
	private Integer is_print;

	// 是否有订单备注
	private Integer has_remark;

	// 排序方式
	private String sort_type;

	// 收货方式
	private String receive_way;

	// 自提点ID
	private String pick_up_st_id;

	// 是否装车
	private Integer inspect_status;

	// 订单来源
	private Integer client;

	// 新版UI特有参数,搜索方式
	public int getSearch_type() {
		return search_type;
	}

	// 新版UI特有参数,搜索方式
	public void setSearch_type(int search_type) {
		this.search_type = search_type;
	}

	// 新版UI特有参数,搜索开始日期
	public String getStart_date_new() {
		return start_date_new;
	}

	// 新版UI特有参数,搜索开始日期
	public void setStart_date_new(String start_date_new) {
		this.start_date_new = start_date_new;
	}

	// 新版UI特有参数,搜索结束日期
	public String getEnd_date_new() {
		return end_date_new;
	}

	// 新版UI特有参数,搜索结束日期
	public void setEnd_date_new(String end_date_new) {
		this.end_date_new = end_date_new;
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

	/**
	 * @return the query_type
	 */
	public int getQuery_type() {
		return query_type;
	}

	/**
	 * 订单是否打印,搜索订单非必填参数,1=打印、2=为打印
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

	public OrderFilterParam() {
		super();
	}

	/**
	 * @return the receive_start_date
	 */
	public String getReceive_start_date() {
		return receive_start_date;
	}

	/**
	 * @param receive_start_date the receive_start_date to set
	 */
	public void setReceive_start_date(String receive_start_date) {
		this.receive_start_date = receive_start_date;
	}

	/**
	 * @return the receive_end_date
	 */
	public String getReceive_end_date() {
		return receive_end_date;
	}

	/**
	 * @param receive_end_date the receive_end_date to set
	 */
	public void setReceive_end_date(String receive_end_date) {
		this.receive_end_date = receive_end_date;
	}

	public String getReceive_start_date_new() {
		return receive_start_date_new;
	}

	public void setReceive_start_date_new(String receive_start_date_new) {
		this.receive_start_date_new = receive_start_date_new;
	}

	public String getReceive_end_date_new() {
		return receive_end_date_new;
	}

	public void setReceive_end_date_new(String receive_end_date_new) {
		this.receive_end_date_new = receive_end_date_new;
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
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 订单状态,搜索订单非必填参数 1=待分拣、5=分拣中、10=配送中、15=已签收
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
	 * 出库状态, 1 未出库, 2 已出库
	 * 
	 * @return
	 */
	public Integer getStock_type() {
		return stock_type;
	}

	/**
	 * 出库状态,1未出库,2已出库
	 * 
	 * @param stock_type
	 */
	public void setStock_type(Integer stock_type) {
		this.stock_type = stock_type;
	}

	/**
	 * 支付状态,搜索订单非必填参数, 1=未支付、5=部分支付、10=已支付
	 * 
	 * @param pay_status the pay_status to set
	 */
	public void setPay_status(Integer pay_status) {
		this.pay_status = pay_status;
	}

	/**
	 * @return the search_area
	 */
	public String getSearch_area() {
		return search_area;
	}

	/**
	 * 地区编号,搜索过滤订单非必填参数
	 * 
	 * @param search_area the search_area to set
	 */
	public void setSearch_area(String search_area) {
		this.search_area = search_area;
	}

	/**
	 * @return the route_id
	 */
	public BigDecimal getRoute_id() {
		return route_id;
	}

	/**
	 * 配送线路ID,搜索过滤订单非必填参数
	 * 
	 * @param route_id the route_id to set
	 */
	public void setRoute_id(BigDecimal route_id) {
		this.route_id = route_id;
	}

	/**
	 * @return the salemenu_id
	 */
	public String getSalemenu_id() {
		return salemenu_id;
	}

	/**
	 * 报价单ID,搜索过滤订单非必填参数
	 * 
	 * @param salemenu_id the salemenu_id to set
	 */
	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
	}

	/**
	 * @return the carrier_id
	 */
	public BigDecimal getCarrier_id() {
		return carrier_id;
	}

	/**
	 * 配送运营商ID,搜索过滤订单非必填参数
	 * 
	 * @param carrier_id the carrier_id to set
	 */
	public void setCarrier_id(BigDecimal carrier_id) {
		this.carrier_id = carrier_id;
	}

	/**
	 * @return the driver_id
	 */
	public BigDecimal getDriver_id() {
		return driver_id;
	}

	/**
	 * 配送司机ID,搜索过滤订单非必填参数
	 * 
	 * @param driver_id the driver_id to set
	 */
	public void setDriver_id(BigDecimal driver_id) {
		this.driver_id = driver_id;
	}

	/**
	 * @return the is_print
	 */
	public Integer getIs_print() {
		return is_print;
	}

	/**
	 * @param is_print the is_print to set
	 */
	public void setIs_print(Integer is_print) {
		this.is_print = is_print;
	}

	/**
	 * @return the has_remark
	 */
	public Integer getHas_remark() {
		return has_remark;
	}

	/**
	 * 是否有订单备注,搜索订单非必填参数,0=无备注、1=有备注
	 * 
	 * @param has_remark the has_remark to set
	 */
	public void setHas_remark(Integer has_remark) {
		this.has_remark = has_remark;
	}

	/**
	 * @return the sort_type
	 */
	public String getSort_type() {
		return sort_type;
	}

	/**
	 * 排序方式, addr_desc=按商户名降序排序、addr_asc=按商户名升序排序、
	 * price_desc=按订单金额降序排序、price_asc=按订单金额升序排序、
	 * status_desc=按订单状态降序排序、status_asc=按订单状态升序排序
	 * freight_desc=按订单运费降序排序、freight_asc=按订单运费升序排序
	 * 
	 * @param sort_type the sort_type to set
	 */
	public void setSort_type(String sort_type) {
		this.sort_type = sort_type;
	}

	public String getReceive_way() {
		return receive_way;
	}

	/**
	 * 收货方式为配送:1,自提:2,全部默认不传字段
	 * 
	 * @param receive_way
	 */
	public void setReceive_way(String receive_way) {
		this.receive_way = receive_way;
	}

	public String getPick_up_st_id() {
		return pick_up_st_id;
	}

	public void setPick_up_st_id(String pick_up_st_id) {
		this.pick_up_st_id = pick_up_st_id;
	}

	public Integer getInspect_status() {
		return inspect_status;
	}

	/**
	 * 是否装车
	 * 
	 * @param inspect_status
	 */
	public void setInspect_status(Integer inspect_status) {
		this.inspect_status = inspect_status;
	}

	public Integer getClient() {
		return client;
	}

	/**
	 * 订单来源
	 * 
	 * @param client
	 */
	public void setClient(Integer client) {
		this.client = client;
	}

}
