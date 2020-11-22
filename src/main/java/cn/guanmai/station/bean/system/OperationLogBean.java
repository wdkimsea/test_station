package cn.guanmai.station.bean.system;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: liming
 * @Date: 2020年7月15日 下午2:40:45
 * @description:
 * @version: 1.0
 */

public class OperationLogBean {
	private String create_time;
	private String customer_name;
	private String fee_type;
	private String id;
	private int log_type;
	private String merchandise_name;
	private JSONObject modify;
	private String op_id;
	private int op_type;
	private String op_user;
	private String op_user_remark;

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLog_type() {
		return log_type;
	}

	public void setLog_type(int log_type) {
		this.log_type = log_type;
	}

	public String getMerchandise_name() {
		return merchandise_name;
	}

	public void setMerchandise_name(String merchandise_name) {
		this.merchandise_name = merchandise_name;
	}

	public JSONObject getModify() {
		return modify;
	}

	public void setModify(JSONObject modify) {
		this.modify = modify;
	}

	public String getOp_id() {
		return op_id;
	}

	public void setOp_id(String op_id) {
		this.op_id = op_id;
	}

	public int getOp_type() {
		return op_type;
	}

	public void setOp_type(int op_type) {
		this.op_type = op_type;
	}

	public String getOp_user() {
		return op_user;
	}

	public void setOp_user(String op_user) {
		this.op_user = op_user;
	}

	public String getOp_user_remark() {
		return op_user_remark;
	}

	public void setOp_user_remark(String op_user_remark) {
		this.op_user_remark = op_user_remark;
	}

}
