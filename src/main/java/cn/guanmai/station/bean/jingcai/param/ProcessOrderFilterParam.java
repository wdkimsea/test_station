package cn.guanmai.station.bean.jingcai.param;

/**
 * @author: liming
 * @Date: 2020年5月18日 下午2:40:39
 * @description:
 * @version: 1.0
 */

public class ProcessOrderFilterParam {
	private String begin;
	private String end;
	private int peed = 300;
	private String q;
	private int date_type;
	private int offset = 0;
	private int limit = 50;
	private Integer status;
	private String page_obj;
	private Integer need_unrelease;
	private Integer source_type;

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

	public int getPeed() {
		return peed;
	}

	public void setPeed(int peed) {
		this.peed = peed;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public int getDate_type() {
		return date_type;
	}

	/**
	 * 按下达日期 5、按计划完成日期 2
	 * 
	 * @param date_type
	 */
	public void setDate_type(int date_type) {
		this.date_type = date_type;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPage_obj() {
		return page_obj;
	}

	public void setPage_obj(String page_obj) {
		this.page_obj = page_obj;
	}

	public Integer getNeed_unrelease() {
		return need_unrelease;
	}

	public void setNeed_unrelease(Integer need_unrelease) {
		this.need_unrelease = need_unrelease;
	}

	public Integer getSource_type() {
		return source_type;
	}

	public void setSource_type(Integer source_type) {
		this.source_type = source_type;
	}

}
