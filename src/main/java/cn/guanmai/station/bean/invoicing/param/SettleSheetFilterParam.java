package cn.guanmai.station.bean.invoicing.param;

/**
 * @author liming
 * @date 2020年1月16日
 * @time 上午11:21:19
 * @des TODO
 */

public class SettleSheetFilterParam {
	private int type;
	private String start;
	private String end;
	private int receipt_type;
	private String settle_supplier_id;

	public int getType() {
		return type;
	}

	/**
	 * 1: 按入库/退货日期, 2:按建单日期
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

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
	 * 5: 全部单据类型, 1: 采购入库单, 2: 采购退货单
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
