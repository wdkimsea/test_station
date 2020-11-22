package cn.guanmai.station.bean.invoicing.param;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: liming
 * @Date: 2020年2月4日 下午2:05:31
 * @description: 应付总账&应付明细账 接口参数
 * @version: 1.0
 */

public class SettlementFilterParam {
	private String begin;
	private String end;
	private String settle_supplier_id;
	private int limit = 20;
	private int offset = 0;
	private int peek = 300;
	// 应付明细账翻页接口
	private JSONObject page_obj;

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
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

	public JSONObject getPage_obj() {
		return page_obj;
	}

	/**
	 * 应付明细账翻页参数
	 * 
	 * @param page_obj
	 */
	public void setPage_obj(JSONObject page_obj) {
		this.page_obj = page_obj;
	}

}
