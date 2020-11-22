package cn.guanmai.manage.bean.account.param;

import java.util.List;

/**
 * @author: liming
 * @Date: 2020年7月30日 下午3:25:32
 * @description:
 * @version: 1.0
 */

public class MgUserCreateParam {
	private String username;
	private String name;
	private int is_admin;
	private int is_valid;
	private String password;
	private List<String> visible_station_ids;
	private List<Integer> role_ids;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIs_admin() {
		return is_admin;
	}

	public void setIs_admin(int is_admin) {
		this.is_admin = is_admin;
	}

	public int getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(int is_valid) {
		this.is_valid = is_valid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getVisible_station_ids() {
		return visible_station_ids;
	}

	public void setVisible_station_ids(List<String> visible_station_ids) {
		this.visible_station_ids = visible_station_ids;
	}

	public List<Integer> getRole_ids() {
		return role_ids;
	}

	public void setRole_ids(List<Integer> role_ids) {
		this.role_ids = role_ids;
	}

}
