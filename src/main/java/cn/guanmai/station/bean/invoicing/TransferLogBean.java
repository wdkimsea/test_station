package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年10月14日
 * @time 下午6:04:01
 * @des TODO
 */

public class TransferLogBean {
	private String category_1_name;
	private String category_2_name;
	private String pinlei_name;
	private String out_batch_num;
	private String sheet_no;
	private String spu_id;
	private String spu_name;
	private String std_unit_name;
	private String supplier_name;
	private String submit_time;
	private String remark;
	private String in_batch_num;
	private BigDecimal out_amount;

	public String getCategory_1_name() {
		return category_1_name;
	}

	public void setCategory_1_name(String category_1_name) {
		this.category_1_name = category_1_name;
	}

	public String getCategory_2_name() {
		return category_2_name;
	}

	public void setCategory_2_name(String category_2_name) {
		this.category_2_name = category_2_name;
	}

	public String getPinlei_name() {
		return pinlei_name;
	}

	public void setPinlei_name(String pinlei_name) {
		this.pinlei_name = pinlei_name;
	}

	public String getOut_batch_num() {
		return out_batch_num;
	}

	public void setOut_batch_num(String out_batch_num) {
		this.out_batch_num = out_batch_num;
	}

	public String getSheet_no() {
		return sheet_no;
	}

	public void setSheet_no(String sheet_no) {
		this.sheet_no = sheet_no;
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

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public String getSubmit_time() {
		return submit_time;
	}

	public void setSubmit_time(String submit_time) {
		this.submit_time = submit_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIn_batch_num() {
		return in_batch_num;
	}

	public void setIn_batch_num(String in_batch_num) {
		this.in_batch_num = in_batch_num;
	}

	public BigDecimal getOut_amount() {
		return out_amount;
	}

	public void setOut_amount(BigDecimal out_amount) {
		this.out_amount = out_amount;
	}

}
