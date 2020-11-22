package cn.guanmai.manage.bean.account.param;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年8月23日
 * @time 上午10:37:15
 * @des TODO
 */

public class StUserFilterParam {
	private Integer is_valid;
	private String station_id;
	private BigDecimal role_id;
	private Integer type_id;
	private String search_text;

	public Integer getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(Integer is_valid) {
		this.is_valid = is_valid;
	}

	public String getStation_id() {
		return station_id;
	}

	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	public BigDecimal getRole_id() {
		return role_id;
	}

	public void setRole_id(BigDecimal role_id) {
		this.role_id = role_id;
	}

	public Integer getType_id() {
		return type_id;
	}

	/**
	 * 999 管理员、0普通用户、1供应商
	 * 
	 * @param type_id
	 */
	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}

	public String getSearch_text() {
		return search_text;
	}

	public void setSearch_text(String search_text) {
		this.search_text = search_text;
	}

}
