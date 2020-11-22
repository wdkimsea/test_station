package cn.guanmai.station.bean.delivery;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Apr 1, 2019 10:53:40 AM 
* @des 线路
* @version 1.0 
*/
public class RouteBean {
	private String name;
	private BigDecimal id;
	private int address_count;
	private String create_user;
	private String create_time;

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
	 * @return the address_count
	 */
	public int getAddress_count() {
		return address_count;
	}

	/**
	 * @param address_count
	 *            the address_count to set
	 */
	public void setAddress_count(int address_count) {
		this.address_count = address_count;
	}

	/**
	 * @return the create_user
	 */
	public String getCreate_user() {
		return create_user;
	}

	/**
	 * @param create_user
	 *            the create_user to set
	 */
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	/**
	 * @return the create_time
	 */
	public String getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time
	 *            the create_time to set
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}
