package cn.guanmai.open.bean.stock;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2019年10月22日
 * @time 下午7:35:38
 * @des TODO
 */

public class OpenStockLedgerDetailBean {
	private String supplier_id;
	private String supplier_name;
	private String supplier_no;
	private BigDecimal early_unpay;
	private BigDecimal cur_amount;
	private BigDecimal cur_pay;
	private BigDecimal total_unpay;

	public List<Detail> details;

	public class Detail {
		private BigDecimal early_unpay;
		private BigDecimal cur_amount;
		private BigDecimal cur_pay;
		private BigDecimal total_unpay;

		public BigDecimal getEarly_unpay() {
			return early_unpay;
		}

		public void setEarly_unpay(BigDecimal early_unpay) {
			this.early_unpay = early_unpay;
		}

		public BigDecimal getCur_amount() {
			return cur_amount;
		}

		public void setCur_amount(BigDecimal cur_amount) {
			this.cur_amount = cur_amount;
		}

		public BigDecimal getCur_pay() {
			return cur_pay;
		}

		public void setCur_pay(BigDecimal cur_pay) {
			this.cur_pay = cur_pay;
		}

		public BigDecimal getTotal_unpay() {
			return total_unpay;
		}

		public void setTotal_unpay(BigDecimal total_unpay) {
			this.total_unpay = total_unpay;
		}
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

	public String getSupplier_no() {
		return supplier_no;
	}

	public void setSupplier_no(String supplier_no) {
		this.supplier_no = supplier_no;
	}

	public BigDecimal getEarly_unpay() {
		return early_unpay;
	}

	public void setEarly_unpay(BigDecimal early_unpay) {
		this.early_unpay = early_unpay;
	}

	public BigDecimal getCur_amount() {
		return cur_amount;
	}

	public void setCur_amount(BigDecimal cur_amount) {
		this.cur_amount = cur_amount;
	}

	public BigDecimal getCur_pay() {
		return cur_pay;
	}

	public void setCur_pay(BigDecimal cur_pay) {
		this.cur_pay = cur_pay;
	}

	public BigDecimal getTotal_unpay() {
		return total_unpay;
	}

	public void setTotal_unpay(BigDecimal total_unpay) {
		this.total_unpay = total_unpay;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

}
