package cn.guanmai.open.bean.purchase.param;

/**
 * @author liming
 * @date 2019年11月12日
 * @time 上午10:50:01
 * @des TODO
 */

public class OpenPurchaseTaskFilterParam {
	private String start_date;
	private String end_date;
	private String query_type;
	private String category1_id;
	private String category2_id;
	private String pinlei_id;
	private String supplier_id;
	private String status;
	private String purchaser_id;
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

	public String getQuery_type() {
		return query_type;
	}

	public void setQuery_type(String query_type) {
		this.query_type = query_type;
	}

	public String getCategory1_id() {
		return category1_id;
	}

	public void setCategory1_id(String category1_id) {
		this.category1_id = category1_id;
	}

	public String getCategory2_id() {
		return category2_id;
	}

	public void setCategory2_id(String category2_id) {
		this.category2_id = category2_id;
	}

	public String getPinlei_id() {
		return pinlei_id;
	}

	public void setPinlei_id(String pinlei_id) {
		this.pinlei_id = pinlei_id;
	}

	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPurchaser_id() {
		return purchaser_id;
	}

	public void setPurchaser_id(String purchaser_id) {
		this.purchaser_id = purchaser_id;
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
