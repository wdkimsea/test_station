package cn.guanmai.station.bean.account.param;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Apr 23, 2019 4:09:46 PM 
* @des 搜索用户过滤参数
* @version 1.0 
*/
public class UserFilterParam {
	private Integer is_valid;
	private String station_id;
	private BigDecimal role_id;
	private Integer type_id;
	private String search_text;

	/**
	 * @return the is_valid
	 */
	public Integer getIs_valid() {
		return is_valid;
	}

	/**
	 * 用户状态, 0=无效、1=有效
	 * 
	 * @param is_valid
	 *            the is_valid to set
	 */
	public void setIs_valid(Integer is_valid) {
		this.is_valid = is_valid;
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
	 * @return the role_id
	 */
	public BigDecimal getRole_id() {
		return role_id;
	}

	/**
	 * @param role_id
	 *            the role_id to set
	 */
	public void setRole_id(BigDecimal role_id) {
		this.role_id = role_id;
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
	 * @return the search_text
	 */
	public String getSearch_text() {
		return search_text;
	}

	/**
	 * @param search_text
	 *            the search_text to set
	 */
	public void setSearch_text(String search_text) {
		this.search_text = search_text;
	}

}
