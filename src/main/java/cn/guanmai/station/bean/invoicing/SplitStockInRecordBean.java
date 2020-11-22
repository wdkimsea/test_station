package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年7月2日 下午2:51:05
 * @description:
 * @version: 1.0
 */

public class SplitStockInRecordBean {
	private String audit_time;
	private String category_2_name;
	private BigDecimal in_stock_amount;
	private BigDecimal in_stock_price;
	private BigDecimal in_stock_quantity;
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

	public BigDecimal getIn_stock_amount() {
		return in_stock_amount;
	}

	public void setIn_stock_amount(BigDecimal in_stock_amount) {
		this.in_stock_amount = in_stock_amount;
	}

	public BigDecimal getIn_stock_price() {
		return in_stock_price.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public void setIn_stock_price(BigDecimal in_stock_price) {
		this.in_stock_price = in_stock_price;
	}

	public BigDecimal getIn_stock_quantity() {
		return in_stock_quantity.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public void setIn_stock_quantity(BigDecimal in_stock_quantity) {
		this.in_stock_quantity = in_stock_quantity;
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
