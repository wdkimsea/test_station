package cn.guanmai.station.bean.category.param;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: liming
 * @Date: 2020年2月17日 下午4:35:03
 * @description:
 * @version: 1.0
 */

public class CombineGoodsFilterParam {
	private String search_text;
	private Integer state;
	private int limit = 10;
	private int offset = 0;
	private int peek = 60;
	private int count = 1;
	private JSONObject page_obj;

	public String getSearch_text() {
		return search_text;
	}

	public void setSearch_text(String search_text) {
		this.search_text = search_text;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public JSONObject getPage_obj() {
		return page_obj;
	}

	public void setPage_obj(JSONObject page_obj) {
		this.page_obj = page_obj;
	}

}
