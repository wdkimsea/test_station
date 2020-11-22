package cn.guanmai.station.bean.system;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author: liming
 * @Date: 2020年7月15日 下午3:21:45
 * @description:
 * @version: 1.0
 */

public class OperationLogDetailBean {
	private String change_url;
	private String create_time;
	private String fee_type;
	private int group_id;
	private int log_type;
	private JSONObject modify;
	private String op_id;
	private int op_source;
	private int op_type;
	private String op_user;
	private String station_id;
	@JSONField(name="_id")
	private String id;

	public String getChange_url() {
		return change_url;
	}

	public void setChange_url(String change_url) {
		this.change_url = change_url;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public int getGroup_id() {
		return group_id;
	}

	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}

	public int getLog_type() {
		return log_type;
	}

	public void setLog_type(int log_type) {
		this.log_type = log_type;
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

	public int getOp_source() {
		return op_source;
	}

	public void setOp_source(int op_source) {
		this.op_source = op_source;
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

	public String getStation_id() {
		return station_id;
	}

	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
