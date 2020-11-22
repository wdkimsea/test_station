package cn.guanmai.station.bean.invoicing.param;

import java.util.List;

/* 
* @author liming 
* @date Jan 8, 2019 7:48:53 PM 
* @des 库存记录搜索过滤条件类
* @version 1.0 
*/
public class StockRecordFilterParam {
	private Integer time_type;
	private String begin;
	private String end;
	private List<String> category_id_1;
	private List<String> category_id_2;
	private String text;
	private String q;
	private int offset = 0;
	private int limit = 20;

	public Integer getTime_type() {
		return time_type;
	}

	/**
	 * 1=提交时间,2=入库时间
	 * 
	 * @param time_type
	 */
	public void setTime_type(Integer time_type) {
		this.time_type = time_type;
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

	public List<String> getCategory_id_1() {
		return category_id_1;
	}

	public void setCategory_id_1(List<String> category_id_1) {
		this.category_id_1 = category_id_1;
	}

	public List<String> getCategory_id_2() {
		return category_id_2;
	}

	public void setCategory_id_2(List<String> category_id_2) {
		this.category_id_2 = category_id_2;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
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

}
