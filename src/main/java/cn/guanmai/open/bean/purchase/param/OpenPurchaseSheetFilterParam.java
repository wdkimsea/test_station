package cn.guanmai.open.bean.purchase.param;

/**
 * @author liming
 * @date 2019年11月12日
 * @time 上午9:56:51
 * @des TODO
 */

public class OpenPurchaseSheetFilterParam {
	private String start_date;
	private String end_date;
	private String sheet_id;
	private String status;
	private String supplier_id;
	private String offset = "0";
	private String limit = "20";

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

	public String getSheet_id() {
		return sheet_id;
	}

	public void setSheet_id(String sheet_id) {
		this.sheet_id = sheet_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
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
