package cn.guanmai.station.bean.purchase;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2019年11月11日
 * @time 上午10:48:21
 * @des TODO
 */

public class PurchaseSheetShareBean {
	private String in_stock_sheet_id;
	private PurchaseSheet purchase_sheet;
	private List<Task> tasks;

	public class PurchaseSheet {
		private String create_time;
		private String customer_id;
		private String operator;
		private String purchase_sheet_id;
		private String purchaser_id;
		private String purchaser_name;
		private String purchaser_phone_num;
		private String settle_supplier_id;
		private String sheet_remark;
		private String submit_time;
		private String supplier_name;

		public String getCreate_time() {
			return create_time;
		}

		public void setCreate_time(String create_time) {
			this.create_time = create_time;
		}

		public String getCustomer_id() {
			return customer_id;
		}

		public void setCustomer_id(String customer_id) {
			this.customer_id = customer_id;
		}

		public String getOperator() {
			return operator;
		}

		public void setOperator(String operator) {
			this.operator = operator;
		}

		public String getPurchase_sheet_id() {
			return purchase_sheet_id;
		}

		public void setPurchase_sheet_id(String purchase_sheet_id) {
			this.purchase_sheet_id = purchase_sheet_id;
		}

		public String getPurchaser_id() {
			return purchaser_id;
		}

		public void setPurchaser_id(String purchaser_id) {
			this.purchaser_id = purchaser_id;
		}

		public String getPurchaser_name() {
			return purchaser_name;
		}

		public void setPurchaser_name(String purchaser_name) {
			this.purchaser_name = purchaser_name;
		}

		public String getPurchaser_phone_num() {
			return purchaser_phone_num;
		}

		public void setPurchaser_phone_num(String purchaser_phone_num) {
			this.purchaser_phone_num = purchaser_phone_num;
		}

		public String getSettle_supplier_id() {
			return settle_supplier_id;
		}

		public void setSettle_supplier_id(String settle_supplier_id) {
			this.settle_supplier_id = settle_supplier_id;
		}

		public String getSheet_remark() {
			return sheet_remark;
		}

		public void setSheet_remark(String sheet_remark) {
			this.sheet_remark = sheet_remark;
		}

		public String getSubmit_time() {
			return submit_time;
		}

		public void setSubmit_time(String submit_time) {
			this.submit_time = submit_time;
		}

		public String getSupplier_name() {
			return supplier_name;
		}

		public void setSupplier_name(String supplier_name) {
			this.supplier_name = supplier_name;
		}

	}

	public class Task {
		private String spec_name;
		private String spec_id;
		private String std_unit_name;
		private BigDecimal ratio;
		private String purchase_unit_name;
		private BigDecimal plan_amount;
		private BigDecimal purchase_amount;

		public String getSpec_name() {
			return spec_name;
		}

		public void setSpec_name(String spec_name) {
			this.spec_name = spec_name;
		}

		public String getSpec_id() {
			return spec_id;
		}

		public void setSpec_id(String spec_id) {
			this.spec_id = spec_id;
		}

		public String getStd_unit_name() {
			return std_unit_name;
		}

		public void setStd_unit_name(String std_unit_name) {
			this.std_unit_name = std_unit_name;
		}

		public BigDecimal getRatio() {
			return ratio;
		}

		public void setRatio(BigDecimal ratio) {
			this.ratio = ratio;
		}

		public String getPurchase_unit_name() {
			return purchase_unit_name;
		}

		public void setPurchase_unit_name(String purchase_unit_name) {
			this.purchase_unit_name = purchase_unit_name;
		}

		public BigDecimal getPlan_amount() {
			return plan_amount;
		}

		public void setPlan_amount(BigDecimal plan_amount) {
			this.plan_amount = plan_amount;
		}

		public BigDecimal getPurchase_amount() {
			return purchase_amount;
		}

		public void setPurchase_amount(BigDecimal purchase_amount) {
			this.purchase_amount = purchase_amount;
		}
	}

	public String getIn_stock_sheet_id() {
		return in_stock_sheet_id;
	}

	public void setIn_stock_sheet_id(String in_stock_sheet_id) {
		this.in_stock_sheet_id = in_stock_sheet_id;
	}

	public PurchaseSheet getPurchase_sheet() {
		return purchase_sheet;
	}

	public void setPurchase_sheet(PurchaseSheet purchase_sheet) {
		this.purchase_sheet = purchase_sheet;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

}
