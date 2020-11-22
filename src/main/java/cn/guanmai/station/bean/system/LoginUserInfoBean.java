package cn.guanmai.station.bean.system;

import com.alibaba.fastjson.JSONArray;

/* 
* @author liming 
* @date Apr 11, 2019 10:43:22 AM 
* @des 站点用户相关信息
* @version 1.0 
*/
public class LoginUserInfoBean {
	private int group_id;
	private String station_id;
	private int stock_method;
	private int type;
	private boolean is_staff;
	private JSONArray user_permission;
	private Profile profile;

	public class Profile {
		private int order_can_have_duplicate_sku;

		public int getOrder_can_have_duplicate_sku() {
			return order_can_have_duplicate_sku;
		}

		public void setOrder_can_have_duplicate_sku(int order_can_have_duplicate_sku) {
			this.order_can_have_duplicate_sku = order_can_have_duplicate_sku;
		}

		public Profile() {
			super();
		}
	}

	/**
	 * @return the station_id
	 */
	public String getStation_id() {
		return station_id;
	}

	/**
	 * @param station_id the station_id to set
	 */
	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	public int getStock_method() {
		return stock_method;
	}

	public void setStock_method(int stock_method) {
		this.stock_method = stock_method;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	public boolean isIs_staff() {
		return is_staff;
	}

	public void setIs_staff(boolean is_staff) {
		this.is_staff = is_staff;
	}

	/**
	 * @return the user_permission
	 */
	public JSONArray getUser_permission() {
		return user_permission;
	}

	/**
	 * @param user_permission the user_permission to set
	 */
	public void setUser_permission(JSONArray user_permission) {
		this.user_permission = user_permission;
	}

	/**
	 * @return the group_id
	 */
	public int getGroup_id() {
		return group_id;
	}

	/**
	 * @param group_id the group_id to set
	 */
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public LoginUserInfoBean() {
		super();
	}

}
