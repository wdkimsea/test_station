package cn.guanmai.open.bean.purchase;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年11月12日
 * @time 上午9:53:13
 * @des TODO
 */

public class OpenPurchaseSheetBean {
	private String create_time;
	private String purchase_sheet_id;
	private String station_id;
	private int status;
	private String submit_time;
	private String supplier_id;
	private String supplier_name;
	private int purchase_task_num;
	private BigDecimal purchase_amount;
	private String in_stock_time;
	private String purchaser;
	private String purchaser_id;
	private String operator;
	private String operator_id;

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getPurchase_sheet_id() {
		return purchase_sheet_id;
	}

	public void setPurchase_sheet_id(String purchase_sheet_id) {
		this.purchase_sheet_id = purchase_sheet_id;
	}

	public String getStation_id() {
		return station_id;
	}

	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSubmit_time() {
		return submit_time;
	}

	public void setSubmit_time(String submit_time) {
		this.submit_time = submit_time;
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

	public int getPurchase_task_num() {
		return purchase_task_num;
	}

	public void setPurchase_task_num(int purchase_task_num) {
		this.purchase_task_num = purchase_task_num;
	}

	public BigDecimal getPurchase_amount() {
		return purchase_amount;
	}

	public void setPurchase_amount(BigDecimal purchase_amount) {
		this.purchase_amount = purchase_amount;
	}

	public String getIn_stock_time() {
		return in_stock_time;
	}

	public void setIn_stock_time(String in_stock_time) {
		this.in_stock_time = in_stock_time;
	}

	public String getPurchaser() {
		return purchaser;
	}

	public void setPurchaser(String purchaser) {
		this.purchaser = purchaser;
	}

	public String getPurchaser_id() {
		return purchaser_id;
	}

	public void setPurchaser_id(String purchaser_id) {
		this.purchaser_id = purchaser_id;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(String operator_id) {
		this.operator_id = operator_id;
	}

}
