package cn.guanmai.station.bean.system.param;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: liming
 * @Date: 2020年7月15日 下午2:55:51
 * @description:
 * @version: 1.0
 */

public class OperationLogFilterParam {
	private String op_start_date;
	private String op_end_date;
	private Integer log_type;
	private Integer op_type;
	private String search_text;
	private int limit = 10;
	private int offset = 0;
	private JSONObject page_obj;

	public String getOp_start_date() {
		return op_start_date;
	}

	public void setOp_start_date(String op_start_date) {
		this.op_start_date = op_start_date;
	}

	public String getOp_end_date() {
		return op_end_date;
	}

	public void setOp_end_date(String op_end_date) {
		this.op_end_date = op_end_date;
	}

	public Integer getLog_type() {
		return log_type;
	}

	public void setLog_type(Integer log_type) {
		this.log_type = log_type;
	}

	public Integer getOp_type() {
		return op_type;
	}

	public void setOp_type(Integer op_type) {
		this.op_type = op_type;
	}

	public String getSearch_text() {
		return search_text;
	}

	public void setSearch_text(String search_text) {
		this.search_text = search_text;
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

	public JSONObject getPage_obj() {
		return page_obj;
	}

	public void setPage_obj(JSONObject page_obj) {
		this.page_obj = page_obj;
	}

}
