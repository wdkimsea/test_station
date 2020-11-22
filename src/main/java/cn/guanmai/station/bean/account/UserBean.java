package cn.guanmai.station.bean.account;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date Apr 23, 2019 4:03:55 PM 
* @des 用户信息
* @version 1.0 
*/
public class UserBean {
	private String username;
	private Integer id;
	private Integer type_id;
	private String create_date;
	private boolean is_admin;
	private boolean is_valid;
	private List<Role> roles;

	public class Role {
		private BigDecimal id;
		private String name;

		/**
		 * @return the id
		 */
		public BigDecimal getId() {
			return id;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		public void setId(BigDecimal id) {
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

	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the type_id
	 */
	public Integer getType_id() {
		return type_id;
	}

	/**
	 * @param type_id
	 *            the type_id to set
	 */
	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}

	/**
	 * @return the create_date
	 */
	public String getCreate_date() {
		return create_date;
	}

	/**
	 * @param create_date
	 *            the create_date to set
	 */
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	/**
	 * @return the is_admin
	 */
	public boolean isIs_admin() {
		return is_admin;
	}

	/**
	 * @param is_admin
	 *            the is_admin to set
	 */
	public void setIs_admin(boolean is_admin) {
		this.is_admin = is_admin;
	}

	/**
	 * @return the is_valid
	 */
	public boolean isIs_valid() {
		return is_valid;
	}

	/**
	 * @param is_valid
	 *            the is_valid to set
	 */
	public void setIs_valid(boolean is_valid) {
		this.is_valid = is_valid;
	}

	/**
	 * @return the roles
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}
