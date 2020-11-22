package cn.guanmai.open.bean.stock;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年10月28日
 * @time 下午4:26:16
 * @des TODO
 */

public class OpenStockInSheetBean {
	private String in_stock_sheet_id;
	private String supplier_id;
	private String supplier_name;
	private int status;
	private BigDecimal sku_amount;
	private BigDecimal discount_amount;
	private BigDecimal total_amount;
	private String create_date;
	private String in_stock_date;

	public String getIn_stock_sheet_id() {
		return in_stock_sheet_id;
	}

	public void setIn_stock_sheet_id(String in_stock_sheet_id) {
		this.in_stock_sheet_id = in_stock_sheet_id;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public BigDecimal getSku_amount() {
		return sku_amount;
	}

	public void setSku_amount(BigDecimal sku_amount) {
		this.sku_amount = sku_amount;
	}

	public BigDecimal getDiscount_amount() {
		return discount_amount;
	}

	public void setDiscount_amount(BigDecimal discount_amount) {
		this.discount_amount = discount_amount;
	}

	public BigDecimal getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(BigDecimal total_amount) {
		this.total_amount = total_amount;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getIn_stock_date() {
		return in_stock_date;
	}

	public void setIn_stock_date(String in_stock_date) {
		this.in_stock_date = in_stock_date;
	}

}
