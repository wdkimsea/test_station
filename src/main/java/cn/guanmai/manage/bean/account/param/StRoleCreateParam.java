package cn.guanmai.manage.bean.account.param;

import java.util.List;

/**
 * @author liming
 * @date 2019年8月23日
 * @time 上午10:28:17
 * @des 接口 /gm_account/station/role/create 对应的参数
 */

public class StRoleCreateParam {
	private String name;
	private String description;
	private String station_id;
	private List<Integer> permission_ids;

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

	public String getStation_id() {
		return station_id;
	}

	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	public List<Integer> getPermission_ids() {
		return permission_ids;
	}

	public void setPermission_ids(List<Integer> permission_ids) {
		this.permission_ids = permission_ids;
	}

}
