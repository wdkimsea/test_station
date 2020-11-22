package cn.guanmai.station.bean.marketing.param;

import com.alibaba.fastjson.JSONArray;

/**
 * @author: liming
 * @Date: 2020年6月1日 下午5:14:43
 * @description:
 * @version: 1.0
 */

public class CouponFilterParam {
	private String q;
	private int count = 1;
	private int limit = 10;
	private int offset = 0;
	private int peek = 60;
	private JSONArray page_obj;
	private int search_type = 1;
	private Integer is_active;
	private Integer audience_type;

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getPeek() {
		return peek;
	}

	public void setPeek(int peek) {
		this.peek = peek;
	}

	public JSONArray getPage_obj() {
		return page_obj;
	}

	public void setPage_obj(JSONArray page_obj) {
		this.page_obj = page_obj;
	}

	public int getSearch_type() {
		return search_type;
	}

	public void setSearch_type(int search_type) {
		this.search_type = search_type;
	}

	public Integer getIs_active() {
		return is_active;
	}

	public void setIs_active(Integer is_active) {
		this.is_active = is_active;
	}

	public Integer getAudience_type() {
		return audience_type;
	}

	public void setAudience_type(Integer audience_type) {
		this.audience_type = audience_type;
	}
	
	

}
