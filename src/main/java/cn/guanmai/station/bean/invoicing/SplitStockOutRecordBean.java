package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年7月2日 下午3:15:39
 * @description:
 * @version: 1.0
 */

public class SplitStockOutRecordBean {
	private String audit_time;
	private String category_2_name;
	private String id;
	private BigDecimal out_stock_amount;
	private BigDecimal out_stock_price;
	private BigDecimal out_stock_quantity;
	private String split_sheet_no;
	private String spu_id;
	private String spu_name;
	private String std_unit_name;

	public String getAudit_time() {
		return audit_time;
	}

	public void setAudit_time(String audit_time) {
		this.audit_time = audit_time;
	}

	public String getCategory_2_name() {
		return category_2_name;
	}

	public void setCategory_2_name(String category_2_name) {
		this.category_2_name = category_2_name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getOut_stock_amount() {
		return out_stock_amount;
	}

	public void setOut_stock_amount(BigDecimal out_stock_amount) {
		this.out_stock_amount = out_stock_amount;
	}

	public BigDecimal getOut_stock_price() {
		return out_stock_price;
	}

	public void setOut_stock_price(BigDecimal out_stock_price) {
		this.out_stock_price = out_stock_price;
	}

	public BigDecimal getOut_stock_quantity() {
		return out_stock_quantity;
	}

	public void setOut_stock_quantity(BigDecimal out_stock_quantity) {
		this.out_stock_quantity = out_stock_quantity;
	}

	public String getSplit_sheet_no() {
		return split_sheet_no;
	}

	public void setSplit_sheet_no(String split_sheet_no) {
		this.split_sheet_no = split_sheet_no;
	}

	public String getSpu_id() {
		return spu_id;
	}

	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	public String getSpu_name() {
		return spu_name;
	}

	public void setSpu_name(String spu_name) {
		this.spu_name = spu_name;
	}

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

}
