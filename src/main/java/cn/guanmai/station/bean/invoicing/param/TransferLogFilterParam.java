package cn.guanmai.station.bean.invoicing.param;

/**
 * @author liming
 * @date 2019年10月14日
 * @time 下午6:00:50
 * @des TODO
 */

public class TransferLogFilterParam {
	private String begin;
	private String end;
	private String q;
	private String supplier_id;

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

	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
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
