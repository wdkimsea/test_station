package cn.guanmai.station.bean.invoicing.param;

/**
 * @author liming
 * @date 2019年10月11日
 * @time 下午7:31:46
 * @des 接口 /station/stock/check/batch_number/list 对应的参数
 */

public class TransferStockBatchFilterParam {
	private String spu_id;
	private String q;
	private String shelf_id;
	private int remain_positive = 1;
	private int limit = 10;
	private int offset = 0;
	private int peek = 60;

	public String getSpu_id() {
		return spu_id;
	}

	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getShelf_id() {
		return shelf_id;
	}

	public void setShelf_id(String shelf_id) {
		this.shelf_id = shelf_id;
	}

	public int getRemain_positive() {
		return remain_positive;
	}

	public void setRemain_positive(int remain_positive) {
		this.remain_positive = remain_positive;
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
