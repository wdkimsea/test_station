package cn.guanmai.station.bean.delivery;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Apr 1, 2019 11:13:42 AM 
* @des 路线详细信息类
* @version 1.0 
*/
public class RouteBindCustomerBean {
	private String area;
	@JSONField(name="name")
	private String address_name;
	@JSONField(name="sid")
	private BigDecimal address_id;
	private BigDecimal route_id;
	private String route_name;

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param area
	 *            the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the address_name
	 */
	public String getAddress_name() {
		return address_name;
	}

	/**
	 * @param address_name
	 *            the address_name to set
	 */
	public void setAddress_name(String address_name) {
		this.address_name = address_name;
	}

	/**
	 * @return the address_id
	 */
	public BigDecimal getAddress_id() {
		return address_id;
	}

	/**
	 * @param address_id
	 *            the address_id to set
	 */
	public void setAddress_id(BigDecimal address_id) {
		this.address_id = address_id;
	}

	/**
	 * @return the route_id
	 */
	public BigDecimal getRoute_id() {
		return route_id;
	}

	/**
	 * @param route_id
	 *            the route_id to set
	 */
	public void setRoute_id(BigDecimal route_id) {
		this.route_id = route_id;
	}

	/**
	 * @return the route_name
	 */
	public String getRoute_name() {
		return route_name;
	}

	/**
	 * @param route_name
	 *            the route_name to set
	 */
	public void setRoute_name(String route_name) {
		this.route_name = route_name;
	}
}
