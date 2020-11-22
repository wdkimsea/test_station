package cn.guanmai.open.bean.stock.param;

/**
 * @author liming
 * @date 2019年10月22日
 * @time 下午7:33:07
 * @des TODO
 */

public class OpenStockLegerFilterParam {
	private String supplier_id;
	private String start_date;
	private String end_date;
	private String offset;
	private String limit;

	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

}
