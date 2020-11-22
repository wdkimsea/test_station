package cn.guanmai.open.bean.stock;

/**
 * @author liming
 * @date 2019年10月21日
 * @time 下午4:47:32
 * @des TODO
 */

public class OpenStockRefundSheetBean {
	private String supplier_refund_sheet_id;
	private String supplier_id;
	private String supplier_name;
	private String status;
	private String sku_money;
	private String discount_amount;
	private String total_money;
	private String create_date;
	private String refund_date;

	public String getSupplier_refund_sheet_id() {
		return supplier_refund_sheet_id;
	}

	public void setSupplier_refund_sheet_id(String supplier_refund_sheet_id) {
		this.supplier_refund_sheet_id = supplier_refund_sheet_id;
	}

	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSku_money() {
		return sku_money;
	}

	public void setSku_money(String sku_money) {
		this.sku_money = sku_money;
	}

	public String getDiscount_amount() {
		return discount_amount;
	}

	public void setDiscount_amount(String discount_amount) {
		this.discount_amount = discount_amount;
	}

	public String getTotal_money() {
		return total_money;
	}

	public void setTotal_money(String total_money) {
		this.total_money = total_money;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getRefund_date() {
		return refund_date;
	}

	public void setRefund_date(String refund_date) {
		this.refund_date = refund_date;
	}

}
