package cn.guanmai.station.bean.purchase.param;

import java.util.List;

/**
 * @author: liming
 * @Date: 2020年4月21日 下午2:17:37
 * @description: 新建、修改、供应商传的参数
 * @version: 1.0
 */

public class PurchaserParam {
	private String id;
	private String name;
	private String username;
	private String phone;
	private String password;
	private int is_allow_login;
	private int status;
	private List<String> settle_suppliers;

	public String getId() {
		return id;
	}

	/**
	 * 修改的采购员的时候才传
	 * 
	 * @param id
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getIs_allow_login() {
		return is_allow_login;
	}

	public void setIs_allow_login(int is_allow_login) {
		this.is_allow_login = is_allow_login;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<String> getSettle_suppliers() {
		return settle_suppliers;
	}

	public void setSettle_suppliers(List<String> settle_suppliers) {
		this.settle_suppliers = settle_suppliers;
	}

}
