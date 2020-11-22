package cn.guanmai.station.bean.weight;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Apr 2, 2019 4:10:24 PM 
* @des station 分拣
* @version 1.0 
*/
public class StationWeighTaskBean {
	private String address_id;
	private String category1_id;
	private String category2_id;
	private String pinlei_id;
	private String salemenu_id;
	private String order_id;
	private String sku_id;
	@JSONField(name="name")
	private String sku_name;
	private String sale_unit_name;
	private String std_unit_name;
	private String route;
	private String sort_remark;
	private String sort_id;
	private boolean is_print;
	private BigDecimal is_weight;
	private BigDecimal out_of_stock;
	private BigDecimal quantity;
	private BigDecimal real_is_weight;
	private BigDecimal real_quantity;
	private int status;
	private BigDecimal std_quantity;
	private BigDecimal std_real_quantity;
	private BigDecimal weighting_quantity;

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
	 * @return the category1_id
	 */
	public String getCategory1_id() {
		return category1_id;
	}

	/**
	 * @param category1_id
	 *            the category1_id to set
	 */
	public void setCategory1_id(String category1_id) {
		this.category1_id = category1_id;
	}

	/**
	 * @return the category2_id
	 */
	public String getCategory2_id() {
		return category2_id;
	}

	/**
	 * @param category2_id
	 *            the category2_id to set
	 */
	public void setCategory2_id(String category2_id) {
		this.category2_id = category2_id;
	}

	/**
	 * @return the pinlei_id
	 */
	public String getPinlei_id() {
		return pinlei_id;
	}

	/**
	 * @param pinlei_id
	 *            the pinlei_id to set
	 */
	public void setPinlei_id(String pinlei_id) {
		this.pinlei_id = pinlei_id;
	}

	/**
	 * @return the salemenu_id
	 */
	public String getSalemenu_id() {
		return salemenu_id;
	}

	/**
	 * @param salemenu_id
	 *            the salemenu_id to set
	 */
	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
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
	 * @return the sale_unit_name
	 */
	public String getSale_unit_name() {
		return sale_unit_name;
	}

	/**
	 * @param sale_unit_name
	 *            the sale_unit_name to set
	 */
	public void setSale_unit_name(String sale_unit_name) {
		this.sale_unit_name = sale_unit_name;
	}

	/**
	 * @return the std_unit_name
	 */
	public String getStd_unit_name() {
		return std_unit_name;
	}

	/**
	 * @param std_unit_name
	 *            the std_unit_name to set
	 */
	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
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
	 * @return the sort_remark
	 */
	public String getSort_remark() {
		return sort_remark;
	}

	/**
	 * @param sort_remark
	 *            the sort_remark to set
	 */
	public void setSort_remark(String sort_remark) {
		this.sort_remark = sort_remark;
	}

	/**
	 * @return the is_print
	 */
	public boolean isIs_print() {
		return is_print;
	}

	/**
	 * @param is_print
	 *            the is_print to set
	 */
	public void setIs_print(boolean is_print) {
		this.is_print = is_print;
	}

	/**
	 * @return the is_weight
	 */
	public BigDecimal getIs_weight() {
		return is_weight;
	}

	/**
	 * @param is_weight
	 *            the is_weight to set
	 */
	public void setIs_weight(BigDecimal is_weight) {
		this.is_weight = is_weight;
	}

	/**
	 * @return the out_of_stock
	 */
	public BigDecimal getOut_of_stock() {
		return out_of_stock;
	}

	/**
	 * @param out_of_stock
	 *            the out_of_stock to set
	 */
	public void setOut_of_stock(BigDecimal out_of_stock) {
		this.out_of_stock = out_of_stock;
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
	 * @return the real_is_weight
	 */
	public BigDecimal getReal_is_weight() {
		return real_is_weight;
	}

	/**
	 * @param real_is_weight
	 *            the real_is_weight to set
	 */
	public void setReal_is_weight(BigDecimal real_is_weight) {
		this.real_is_weight = real_is_weight;
	}

	/**
	 * @return the real_quantity
	 */
	public BigDecimal getReal_quantity() {
		return real_quantity;
	}

	/**
	 * @param real_quantity
	 *            the real_quantity to set
	 */
	public void setReal_quantity(BigDecimal real_quantity) {
		this.real_quantity = real_quantity;
	}

	/**
	 * @return the sort_id
	 */
	public String getSort_id() {
		return sort_id;
	}

	/**
	 * @param sort_id
	 *            the sort_id to set
	 */
	public void setSort_id(String sort_id) {
		this.sort_id = sort_id;
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
	 * @return the std_quantity
	 */
	public BigDecimal getStd_quantity() {
		return std_quantity;
	}

	/**
	 * @param std_quantity
	 *            the std_quantity to set
	 */
	public void setStd_quantity(BigDecimal std_quantity) {
		this.std_quantity = std_quantity;
	}

	/**
	 * @return the std_real_quantity
	 */
	public BigDecimal getStd_real_quantity() {
		return std_real_quantity;
	}

	/**
	 * @param std_real_quantity
	 *            the std_real_quantity to set
	 */
	public void setStd_real_quantity(BigDecimal std_real_quantity) {
		this.std_real_quantity = std_real_quantity;
	}

	/**
	 * @return the weighting_quantity
	 */
	public BigDecimal getWeighting_quantity() {
		return weighting_quantity;
	}

	/**
	 * @param weighting_quantity
	 *            the weighting_quantity to set
	 */
	public void setWeighting_quantity(BigDecimal weighting_quantity) {
		this.weighting_quantity = weighting_quantity;
	}

}
