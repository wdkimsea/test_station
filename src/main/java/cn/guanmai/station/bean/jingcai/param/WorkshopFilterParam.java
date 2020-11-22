package cn.guanmai.station.bean.jingcai.param;

import com.alibaba.fastjson.JSONArray;

/**
 * @author: liming
 * @Date: 2020年4月27日 下午7:38:00
 * @description:
 * @version: 1.0
 */

public class WorkshopFilterParam {
	private String q;
	private int limit = 10;
	private int offset = 0;
	private int peek = 60;
	private JSONArray page_obj;

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
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

}
