package cn.guanmai.station.bean.invoicing.param;

/**
 * @author: liming
 * @Date: 2020年6月28日 下午7:55:08
 * @description:
 * @version: 1.0
 */

public class SplitSheetFiterParam {
	private String q;
	private String begin;
	private String end;
	private Integer status;
	private int limit = 10;
	private int offset = 0;
	private int peek = 60;

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
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

}
