package cn.guanmai.station.bean.invoicing.param;

/**
 * @author: liming
 * @Date: 2020年7月7日 上午10:26:52
 * @description:
 * @version: 1.0
 */

public class SplitLossFilterParam {
	private String q;
	private int aggregate_by_day;
	private String begin;
	private String end;

	private Integer limit;
	private Integer offset;
	private Integer peek = 60;
	private Integer export;

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public int getAggregate_by_day() {
		return aggregate_by_day;
	}

	public void setAggregate_by_day(int aggregate_by_day) {
		this.aggregate_by_day = aggregate_by_day;
	}

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getEnd() {
		return end;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getPeek() {
		return peek;
	}

	public void setPeek(Integer peek) {
		this.peek = peek;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public Integer getExport() {
		return export;
	}

	public void setExport(Integer export) {
		this.export = export;
	}

}
