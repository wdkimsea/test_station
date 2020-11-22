package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年6月28日 下午7:40:12
 * @description: 分割单据
 * @version: 1.0
 */

public class SplitSheetBean {
	private String id;
	private BigDecimal loss_quantity;
	private String operator;
	private String plan_name;
	private BigDecimal remain_quantity;
	private BigDecimal source_quantity;
	private String source_spu_id;
	private String source_spu_name;
	private String split_sheet_no;
	private String split_time;
	private int status;
	private String std_unit_name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getLoss_quantity() {
		return loss_quantity;
	}

	public void setLoss_quantity(BigDecimal loss_quantity) {
		this.loss_quantity = loss_quantity;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getPlan_name() {
		return plan_name;
	}

	public void setPlan_name(String plan_name) {
		this.plan_name = plan_name;
	}

	public BigDecimal getRemain_quantity() {
		return remain_quantity;
	}

	public void setRemain_quantity(BigDecimal remain_quantity) {
		this.remain_quantity = remain_quantity;
	}

	public BigDecimal getSource_quantity() {
		return source_quantity;
	}

	public void setSource_quantity(BigDecimal source_quantity) {
		this.source_quantity = source_quantity;
	}

	public String getSource_spu_id() {
		return source_spu_id;
	}

	public void setSource_spu_id(String source_spu_id) {
		this.source_spu_id = source_spu_id;
	}

	public String getSource_spu_name() {
		return source_spu_name;
	}

	public void setSource_spu_name(String source_spu_name) {
		this.source_spu_name = source_spu_name;
	}

	public String getSplit_sheet_no() {
		return split_sheet_no;
	}

	public void setSplit_sheet_no(String split_sheet_no) {
		this.split_sheet_no = split_sheet_no;
	}

	public String getSplit_time() {
		return split_time;
	}

	public void setSplit_time(String split_time) {
		this.split_time = split_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

}
