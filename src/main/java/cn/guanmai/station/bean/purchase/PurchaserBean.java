package cn.guanmai.station.bean.purchase;

import java.util.List;

/* 
* @author liming 
* @date Nov 24, 2018 10:51:25 AM 
* @des 采购员类
* @version 1.0 
*/
public class PurchaserBean {
	private String id;
	private String username;
	private String password;
	private int is_allow_login;
	private boolean is_online;
	private String name;
	private String phone;
	private int status;

	private List<SettleSupplier> settle_suppliers;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the is_online
	 */
	public boolean isIs_online() {
		return is_online;
	}

	/**
	 * @param is_online the is_online to set
	 */
	public void setIs_online(boolean is_online) {
		this.is_online = is_online;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 姓名,新建采购员的时候必填
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 电话号码,注册的时候非必填
	 * 
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 0是无效,1是有效
	 * 
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public List<SettleSupplier> getSettle_suppliers() {
		return settle_suppliers;
	}

	public void setSettle_suppliers(List<SettleSupplier> settle_suppliers) {
		this.settle_suppliers = settle_suppliers;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 账号,必填
	 * 
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 密码,注册的时候必填
	 * 
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 登录采购APP的开关,0为不开启,1为开启
	 * 
	 * @return the is_allow_login
	 */
	public int getIs_allow_login() {
		return is_allow_login;
	}

	/**
	 * @param is_allow_login the is_allow_login to set
	 */
	public void setIs_allow_login(int is_allow_login) {
		this.is_allow_login = is_allow_login;
	}

	public static class SettleSupplier {
		private String id;
		private String name;

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public SettleSupplier() {
			super();
		}
	}

	public PurchaserBean() {
		super();
	}

}
