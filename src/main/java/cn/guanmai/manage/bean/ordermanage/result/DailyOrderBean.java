package cn.guanmai.manage.bean.ordermanage.result;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Jan 18, 2019 6:45:46 PM 
* @des 每日订单搜索结果类
* @version 1.0 
*/
public class DailyOrderBean {
	@JSONField(name = "_id")
	private String order_id;
	private String address;
	private String address_id;
	private String area_id;
	private String area_name;
	private BigDecimal origin_total_price;
	private String resname;
	private String source_order_id;
	private String station_id;
	private BigDecimal total_pay;
	private BigDecimal total_price;
	private String uid;

	/**
	 * @return the order_id
	 */
	public String getOrder_id() {
		return order_id;
	}

	/**
	 * @param order_id the order_id to set
	 */
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the address_id
	 */
	public String getAddress_id() {
		return address_id;
	}

	/**
	 * @param address_id the address_id to set
	 */
	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	/**
	 * @return the area_id
	 */
	public String getArea_id() {
		return area_id;
	}

	/**
	 * @param area_id the area_id to set
	 */
	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	/**
	 * @return the area_name
	 */
	public String getArea_name() {
		return area_name;
	}

	/**
	 * @param area_name the area_name to set
	 */
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	/**
	 * @return the origin_total_price
	 */
	public BigDecimal getOrigin_total_price() {
		return origin_total_price;
	}

	/**
	 * @param origin_total_price the origin_total_price to set
	 */
	public void setOrigin_total_price(BigDecimal origin_total_price) {
		this.origin_total_price = origin_total_price;
	}

	/**
	 * @return the resname
	 */
	public String getResname() {
		return resname;
	}

	/**
	 * @param resname the resname to set
	 */
	public void setResname(String resname) {
		this.resname = resname;
	}

	/**
	 * @return the source_order_id
	 */
	public String getSource_order_id() {
		return source_order_id;
	}

	/**
	 * @param source_order_id the source_order_id to set
	 */
	public void setSource_order_id(String source_order_id) {
		this.source_order_id = source_order_id;
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

	/**
	 * @return the total_pay
	 */
	public BigDecimal getTotal_pay() {
		return total_pay;
	}

	/**
	 * @param total_pay the total_pay to set
	 */
	public void setTotal_pay(BigDecimal total_pay) {
		this.total_pay = total_pay;
	}

	/**
	 * @return the total_price
	 */
	public BigDecimal getTotal_price() {
		return total_price;
	}

	/**
	 * @param total_price the total_price to set
	 */
	public void setTotal_price(BigDecimal total_price) {
		this.total_price = total_price;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

}
