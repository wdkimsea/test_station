package cn.guanmai.bshop.bean.invoicing.param;

import com.alibaba.fastjson.JSONObject;

/**
 * @author liming
 * @date 2019年9月25日
 * @time 下午2:34:50
 * @des TODO
 */

public class BshopWatiInStockFilterParam {
	private int order_status = -1;
	private int query_type = 1;
	private int sort = 1;
	private String search;
	private String start_time;
	private String end_time;
	private String address_id;
	private JSONObject page_obj;
	private int offset = 0;
	private int limit = 10;

	public int getOrder_status() {
		return order_status;
	}

	public void setOrder_status(int order_status) {
		this.order_status = order_status;
	}

	public int getQuery_type() {
		return query_type;
	}

	public void setQuery_type(int query_type) {
		this.query_type = query_type;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getAddress_id() {
		return address_id;
	}

	public void setAddress_id(String address_id) {
		this.address_id = address_id.replaceFirst("S(0*)", "");
	}

	public JSONObject getPage_obj() {
		return page_obj;
	}

	public void setPage_obj(JSONObject page_obj) {
		this.page_obj = page_obj;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
