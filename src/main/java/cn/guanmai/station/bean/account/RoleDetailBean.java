package cn.guanmai.station.bean.account;

import java.util.List;

/* 
* @author liming 
* @date Apr 23, 2019 11:31:03 AM 
* @des 角色相信信息
* @version 1.0 
*/
public class RoleDetailBean {
	private String id;
	private String name;
	private String description;
	private String station_id;
	private List<Integer> permission_ids;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the station_id
	 */
	public String getStation_id() {
		return station_id;
	}

	/**
	 * @param station_id
	 *            the station_id to set
	 */
	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	/**
	 * @return the permission_ids
	 */
	public List<Integer> getPermission_ids() {
		return permission_ids;
	}

	/**
	 * @param permission_ids
	 *            the permission_ids to set
	 */
	public void setPermission_ids(List<Integer> permission_ids) {
		this.permission_ids = permission_ids;
	}

	/**
	 * 新增角色构造参数
	 * 
	 * @param name
	 * @param description
	 * @param station_id
	 * @param permission_ids
	 */
	public RoleDetailBean(String name, String description, String station_id, List<Integer> permission_ids) {
		super();
		this.name = name;
		this.description = description;
		this.station_id = station_id;
		this.permission_ids = permission_ids;
	}

	public RoleDetailBean() {
		super();
	}

}
