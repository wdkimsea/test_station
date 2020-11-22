package cn.guanmai.manage.bean.account.param;

import java.util.List;

/**
 * @author: liming
 * @Date: 2020年7月30日 上午11:34:05
 * @description:
 * @version: 1.0
 */

public class MgRoleCreateParam {
	private String name;
	private String description;
	private List<Integer> permission_ids;
	private int type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Integer> getPermission_ids() {
		return permission_ids;
	}

	public void setPermission_ids(List<Integer> permission_ids) {
		this.permission_ids = permission_ids;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
