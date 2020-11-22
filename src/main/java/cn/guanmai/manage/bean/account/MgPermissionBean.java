package cn.guanmai.manage.bean.account;

import java.util.List;

/**
 * @author: liming
 * @Date: 2020年7月30日 上午11:55:21
 * @description:
 * @version: 1.0
 */

public class MgPermissionBean {
	private String id;
	private String name;

	private List<Permission> permissions;

	public class Permission {
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

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

}
