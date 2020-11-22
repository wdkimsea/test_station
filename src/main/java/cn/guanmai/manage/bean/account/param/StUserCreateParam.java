package cn.guanmai.manage.bean.account.param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2019年8月23日
 * @time 上午10:48:18
 * @des 接口 /gm_account/station/user/create 对应的参数
 */

public class StUserCreateParam {
	private String name;
	private String username;
	private String station_id;
	private String password;
	private boolean is_admin;
	private int is_valid;
	private int type_id;
	private List<BigDecimal> role_ids;

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

	public String getStation_id() {
		return station_id;
	}

	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isIs_admin() {
		return is_admin;
	}

	public void setIs_admin(boolean is_admin) {
		this.is_admin = is_admin;
	}

	public int getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(int is_valid) {
		this.is_valid = is_valid;
	}

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}

	public List<BigDecimal> getRole_ids() {
		return role_ids;
	}

	public void setRole_ids(List<BigDecimal> role_ids) {
		this.role_ids = role_ids;
	}

}
