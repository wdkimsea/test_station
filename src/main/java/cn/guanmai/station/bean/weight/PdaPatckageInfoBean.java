package cn.guanmai.station.bean.weight;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年7月31日 下午2:58:40
 * @des 接口 /weight/pda/package/get 对应的结果, 包装编码查询
 * @version 1.0
 */
public class PdaPatckageInfoBean {
	private String package_id;
	private String spu_id;
	private String sku_id;
	private String sku_name;
	private BigDecimal quantity;
	private String package_operator_name;
	private String package_date;
	private int status;
	private String sort_date;
	private String sort_operator_name;
	private String order_id;
	private String address_name;
	private String address_id;
	private String route;
	private int sort_id;

	/**
	 * @return the package_id
	 */
	public String getPackage_id() {
		return package_id;
	}

	/**
	 * @param package_id
	 *            the package_id to set
	 */
	public void setPackage_id(String package_id) {
		this.package_id = package_id;
	}

	/**
	 * @return the spu_id
	 */
	public String getSpu_id() {
		return spu_id;
	}

	/**
	 * @param spu_id
	 *            the spu_id to set
	 */
	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	/**
	 * @return the sku_id
	 */
	public String getSku_id() {
		return sku_id;
	}

	/**
	 * @param sku_id
	 *            the sku_id to set
	 */
	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	/**
	 * @return the sku_name
	 */
	public String getSku_name() {
		return sku_name;
	}

	/**
	 * @param sku_name
	 *            the sku_name to set
	 */
	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	/**
	 * @return the quantity
	 */
	public BigDecimal getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the package_operator_name
	 */
	public String getPackage_operator_name() {
		return package_operator_name;
	}

	/**
	 * @param package_operator_name
	 *            the package_operator_name to set
	 */
	public void setPackage_operator_name(String package_operator_name) {
		this.package_operator_name = package_operator_name;
	}

	/**
	 * @return the package_date
	 */
	public String getPackage_date() {
		return package_date;
	}

	/**
	 * @param package_date
	 *            the package_date to set
	 */
	public void setPackage_date(String package_date) {
		this.package_date = package_date;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the sort_date
	 */
	public String getSort_date() {
		return sort_date;
	}

	/**
	 * @param sort_date
	 *            the sort_date to set
	 */
	public void setSort_date(String sort_date) {
		this.sort_date = sort_date;
	}

	/**
	 * @return the sort_operator_name
	 */
	public String getSort_operator_name() {
		return sort_operator_name;
	}

	/**
	 * @param sort_operator_name
	 *            the sort_operator_name to set
	 */
	public void setSort_operator_name(String sort_operator_name) {
		this.sort_operator_name = sort_operator_name;
	}

	/**
	 * @return the order_id
	 */
	public String getOrder_id() {
		return order_id;
	}

	/**
	 * @param order_id
	 *            the order_id to set
	 */
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
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
	public String getAddress_id() {
		return address_id;
	}

	/**
	 * @param address_id
	 *            the address_id to set
	 */
	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	/**
	 * @return the route
	 */
	public String getRoute() {
		return route;
	}

	/**
	 * @param route
	 *            the route to set
	 */
	public void setRoute(String route) {
		this.route = route;
	}

	/**
	 * @return the sort_id
	 */
	public int getSort_id() {
		return sort_id;
	}

	/**
	 * @param sort_id
	 *            the sort_id to set
	 */
	public void setSort_id(int sort_id) {
		this.sort_id = sort_id;
	}

}
