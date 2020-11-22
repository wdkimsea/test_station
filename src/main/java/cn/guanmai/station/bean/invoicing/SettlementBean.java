package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年2月4日 下午2:01:51
 * @description: 应付总账数据条目结果
 * @version: 1.0
 */

public class SettlementBean {
	private String supplier_id;
	private String settle_supplier_id;
	private String name;
	private String company_name;
	private BigDecimal total_unpay;
	private BigDecimal early_unpay;
	private BigDecimal cur_should_pay;
	private BigDecimal cur_pay;

	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public BigDecimal getTotal_unpay() {
		return total_unpay;
	}

	public void setTotal_unpay(BigDecimal total_unpay) {
		this.total_unpay = total_unpay;
	}

	public BigDecimal getEarly_unpay() {
		return early_unpay;
	}

	public void setEarly_unpay(BigDecimal early_unpay) {
		this.early_unpay = early_unpay;
	}

	public BigDecimal getCur_should_pay() {
		return cur_should_pay;
	}

	public void setCur_should_pay(BigDecimal cur_should_pay) {
		this.cur_should_pay = cur_should_pay;
	}

	public BigDecimal getCur_pay() {
		return cur_pay;
	}

	public void setCur_pay(BigDecimal cur_pay) {
		this.cur_pay = cur_pay;
	}

}
