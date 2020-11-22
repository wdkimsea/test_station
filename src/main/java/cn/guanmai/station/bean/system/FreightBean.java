package cn.guanmai.station.bean.system;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author: liming
 * @Date: 2020年5月20日 下午4:39:34
 * @description:
 * @version: 1.0
 */

public class FreightBean {
	private String create_time;
	private String creator_name;
	@JSONField(name="default")
	private boolean is_default;
	private String edit_time;
	private String edit_user_name;
	private String id;
	private String name;

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getCreator_name() {
		return creator_name;
	}

	public void setCreator_name(String creator_name) {
		this.creator_name = creator_name;
	}

	public boolean isIs_default() {
		return is_default;
	}

	public void setIs_default(boolean is_default) {
		this.is_default = is_default;
	}

	public String getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}

	public String getEdit_user_name() {
		return edit_user_name;
	}

	public void setEdit_user_name(String edit_user_name) {
		this.edit_user_name = edit_user_name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
