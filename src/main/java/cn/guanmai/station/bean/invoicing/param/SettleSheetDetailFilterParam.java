package cn.guanmai.station.bean.invoicing.param;

/**
 * @author liming
 * @date 2020年1月16日
 * @time 下午4:51:08
 * @des TODO
 */

public class SettleSheetDetailFilterParam {
	private String start;
	private String end;
	private int receipt_type = 5;
	private String settle_supplier_id;

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public int getReceipt_type() {
		return receipt_type;
	}

	/**
	 * -1:已删除,0:审核不通过,1:待提交,2:已提交待审核,3:已结款,5:全部单据状态
	 * 
	 * @param receipt_type
	 */
	public void setReceipt_type(int receipt_type) {
		this.receipt_type = receipt_type;
	}

	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
	}

}
