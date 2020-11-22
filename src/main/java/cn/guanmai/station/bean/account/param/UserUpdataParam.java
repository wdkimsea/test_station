package cn.guanmai.station.bean.account.param;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date Apr 23, 2019 4:44:13 PM 
* @des 用户更新参数类
* @version 1.0 
*/
public class UserUpdataParam {
	private Integer id;
	private String name;
	private String username;
	private boolean is_admin;
	private String email;
	private String phone;
	private Integer type_id;
	private String station_id;
	private List<BigDecimal> role_ids;
	private int is_valid;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 必填参数
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 必填参数
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 必填参数
	 * 
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the is_admin
	 */
	public boolean isIs_admin() {
		return is_admin;
	}

	/**
	 * 必填参数
	 * 
	 * @param is_admin
	 *            the is_admin to set
	 */
	public void setIs_admin(boolean is_admin) {
		this.is_admin = is_admin;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the type_id
	 */
	public Integer getType_id() {
		return type_id;
	}

	/**
	 * 必填参数
	 * 
	 * @param type_id
	 *            the type_id to set
	 */
	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}

	/**
	 * @return the station_id
	 */
	public String getStation_id() {
		return station_id;
	}

	/**
	 * 必填参数
	 * 
	 * @param station_id
	 *            the station_id to set
	 */
	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	/**
	 * @return the role_ids
	 */
	public List<BigDecimal> getRole_ids() {
		return role_ids;
	}

	/**
	 * 必填参数
	 * 
	 * @param role_ids
	 *            the role_ids to set
	 */
	public void setRole_ids(List<BigDecimal> role_ids) {
		this.role_ids = role_ids;
	}

	/**
	 * @return the is_valid
	 */
	public int getIs_valid() {
		return is_valid;
	}

	/**
	 * 必填参数
	 * 
	 * @param is_valid
	 *            the is_valid to set
	 */
	public void setIs_valid(int is_valid) {
		this.is_valid = is_valid;
	}
}
