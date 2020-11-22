package cn.guanmai.manage.bean.account;

/**
 * @author: liming
 * @Date: 2020年7月30日 上午11:24:37
 * @description:
 * @version: 1.0
 */

public class MgRoleBean {
	private String id;
	private String name;
	private String description;
	private int type;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
