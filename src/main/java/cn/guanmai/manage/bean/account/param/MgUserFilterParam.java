package cn.guanmai.manage.bean.account.param;

/**
 * @author: liming
 * @Date: 2020年7月30日 上午11:08:00
 * @description:
 * @version: 1.0
 */

public class MgUserFilterParam {
	private String search_text;
	private String role_id;
	private Integer is_valid;
	private Integer is_admin;

	public String getSearch_text() {
		return search_text;
	}

	public void setSearch_text(String search_text) {
		this.search_text = search_text;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public Integer getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(Integer is_valid) {
		this.is_valid = is_valid;
	}

	public Integer getIs_admin() {
		return is_admin;
	}

	public void setIs_admin(Integer is_admin) {
		this.is_admin = is_admin;
	}
}
