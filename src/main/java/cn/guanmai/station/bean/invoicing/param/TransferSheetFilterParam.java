package cn.guanmai.station.bean.invoicing.param;

/**
 * @author liming
 * @date 2019年10月11日
 * @time 下午8:10:29
 * @des TODO
 */

public class TransferSheetFilterParam {
	private String begin;
	private String end;
	private String q;
	private Integer status;
	private int limit = 10;
	private int offset = 0;
	private int peek = 60;

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
