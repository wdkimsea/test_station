package cn.guanmai.manage.bean.account;

import java.util.List;

/**
 * @author: liming
 * @Date: 2020年7月30日 上午11:03:54
 * @description:
 * @version: 1.0
 */

public class MgUserBean {
	private String id;
	private String name;
	private String username;
	private String create_date;
	private boolean is_admin;
	private boolean is_valid;

	private List<Role> roles;
	private List<VisibleStation> visible_stations;

	public class Role {
		private String id;
		private String name;

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

	public class VisibleStation {
		private String id;
		private String name;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public boolean isIs_admin() {
		return is_admin;
	}

	public void setIs_admin(boolean is_admin) {
		this.is_admin = is_admin;
	}

	public boolean isIs_valid() {
		return is_valid;
	}

	public void setIs_valid(boolean is_valid) {
		this.is_valid = is_valid;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<VisibleStation> getVisible_stations() {
		return visible_stations;
	}

	public void setVisible_stations(List<VisibleStation> visible_stations) {
		this.visible_stations = visible_stations;
	}

}
