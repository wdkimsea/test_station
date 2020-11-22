package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年2月6日 下午3:50:53
 * @description: 应付明细账列表 /stock/report/settlement/detail
 * @version: 1.0
 */

public class SettlementDetailBean {
	private String date;
	private BigDecimal should_pay;
	private String remark;
	private boolean subtotal;
	private Integer sheet_type;
	private String sheet_number;
	private String name;
	private BigDecimal pay;
	private BigDecimal unpay;
	private String supplier_id;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public BigDecimal getShould_pay() {
		return should_pay;
	}

	public void setShould_pay(BigDecimal should_pay) {
		this.should_pay = should_pay;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isSubtotal() {
		return subtotal;
	}

	public void setSubtotal(boolean subtotal) {
		this.subtotal = subtotal;
	}

	public Integer getSheet_type() {
		return sheet_type;
	}

	public void setSheet_type(Integer sheet_type) {
		this.sheet_type = sheet_type;
	}

	public String getSheet_number() {
		return sheet_number;
	}

	public void setSheet_number(String sheet_number) {
		this.sheet_number = sheet_number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPay() {
		return pay;
	}

	public void setPay(BigDecimal pay) {
		this.pay = pay;
	}

	public BigDecimal getUnpay() {
		return unpay;
	}

	public void setUnpay(BigDecimal unpay) {
		this.unpay = unpay;
	}

	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

}
