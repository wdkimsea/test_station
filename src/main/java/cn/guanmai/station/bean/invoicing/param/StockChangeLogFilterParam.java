package cn.guanmai.station.bean.invoicing.param;

import com.alibaba.fastjson.JSONArray;

/* 
* @author liming 
* @date Feb 28, 2019 2:30:15 PM 
* @des 库存变动日志搜索过滤参数
* @version 1.0 
*/
public class StockChangeLogFilterParam {
	private String spu_id;
	private String q;
	// 搜索开始日期
	private String begin;
	// 搜索结束日期
	private String end;
	private int change_type;
	private int limit = 10;
	private int offset = 0;
	private JSONArray page_obj;

	/**
	 * @return the spu_id
	 */
	public String getSpu_id() {
		return spu_id;
	}

	/**
	 * @param spu_id the spu_id to set
	 */
	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	/**
	 * @return the q
	 */
	public String getQ() {
		return q;
	}

	/**
	 * @param q the q to set
	 */
	public void setQ(String q) {
		this.q = q;
	}

	/**
	 * @return the begin
	 */
	public String getBegin() {
		return begin;
	}

	/**
	 * @param begin the begin to set
	 */
	public void setBegin(String begin) {
		this.begin = begin;
	}

	/**
	 * @return the end
	 */
	public String getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(String end) {
		this.end = end;
	}

	/**
	 * @return the change_type
	 */
	public int getChange_type() {
		return change_type;
	}

	/**
	 * @param change_type the change_type to set
	 */
	public void setChange_type(int change_type) {
		this.change_type = change_type;
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

	public JSONArray getPage_obj() {
		return page_obj;
	}

	public void setPage_obj(JSONArray page_obj) {
		this.page_obj = page_obj;
	}

	public StockChangeLogFilterParam() {

	}

	public StockChangeLogFilterParam(String spu_id, String beginDate, String endDate) {
		super();
		this.spu_id = spu_id;
		this.begin = beginDate;
		this.end = endDate;
		this.limit = 10;
		this.offset = 0;
		this.change_type = 0;
	}

}
