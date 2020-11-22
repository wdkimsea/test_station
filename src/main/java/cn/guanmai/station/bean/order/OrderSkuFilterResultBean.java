package cn.guanmai.station.bean.order;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Jan 3, 2019 11:01:17 AM 
* @des 订单列表,按商品查看过滤结果类
* @version 1.0 
*/
public class OrderSkuFilterResultBean {
	private String order_id;
	@JSONField(name = "id")
	private String sku_id;
	@JSONField(name = "name")
	private String sku_name;
	private BigDecimal sale_ratio;
	private BigDecimal std_sale_price;
	private BigDecimal sale_money;
	private BigDecimal sale_price;
	private BigDecimal quantity;
	private BigDecimal std_unit_quantity;

	private int is_weigh;
	private int weighted;
	private int status;

	private String salemenu_id;
	private String salemenu_name;
	private String route_name;

	// 商户信息相关字段
	private String address_id;
	private String resname;
	private String spu_remark;

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
	 * @return the sku_id
	 */
	public String getSku_id() {
		return sku_id;
	}

	/**
	 * @param sku_id the sku_id to set
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
	 * @param sku_name the sku_name to set
	 */
	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	/**
	 * @return the sale_ratio
	 */
	public BigDecimal getSale_ratio() {
		return sale_ratio;
	}

	/**
	 * @param sale_ratio the sale_ratio to set
	 */
	public void setSale_ratio(BigDecimal sale_ratio) {
		this.sale_ratio = sale_ratio;
	}

	/**
	 * @return the std_sale_price
	 */
	public BigDecimal getStd_sale_price() {
		return std_sale_price;
	}

	/**
	 * @param std_sale_price the std_sale_price to set
	 */
	public void setStd_sale_price(BigDecimal std_sale_price) {
		this.std_sale_price = std_sale_price;
	}

	/**
	 * @return the sale_money
	 */
	public BigDecimal getSale_money() {
		return sale_money;
	}

	/**
	 * @param sale_money the sale_money to set
	 */
	public void setSale_money(BigDecimal sale_money) {
		this.sale_money = sale_money;
	}

	/**
	 * @return the sale_price
	 */
	public BigDecimal getSale_price() {
		return sale_price;
	}

	/**
	 * @param sale_price the sale_price to set
	 */
	public void setSale_price(BigDecimal sale_price) {
		this.sale_price = sale_price;
	}

	/**
	 * @return the quantity
	 */
	public BigDecimal getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getStd_unit_quantity() {
		return std_unit_quantity;
	}

	public void setStd_unit_quantity(BigDecimal std_unit_quantity) {
		this.std_unit_quantity = std_unit_quantity;
	}

	/**
	 * @return the is_weigh
	 */
	public int getIs_weigh() {
		return is_weigh;
	}

	/**
	 * @param is_weigh the is_weigh to set
	 */
	public void setIs_weigh(int is_weigh) {
		this.is_weigh = is_weigh;
	}

	/**
	 * @return the weighted
	 */
	public int getWeighted() {
		return weighted;
	}

	/**
	 * @param weighted the weighted to set
	 */
	public void setWeighted(int weighted) {
		this.weighted = weighted;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the salemenu_id
	 */
	public String getSalemenu_id() {
		return salemenu_id;
	}

	/**
	 * @param salemenu_id the salemenu_id to set
	 */
	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
	}

	public String getSalemenu_name() {
		return salemenu_name;
	}

	public void setSalemenu_name(String salemenu_name) {
		this.salemenu_name = salemenu_name;
	}

	/**
	 * @return the route_name
	 */
	public String getRoute_name() {
		return route_name;
	}

	/**
	 * @param route_name the route_name to set
	 */
	public void setRoute_name(String route_name) {
		this.route_name = route_name;
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

	public String getSpu_remark() {
		return spu_remark;
	}

	public void setSpu_remark(String spu_remark) {
		this.spu_remark = spu_remark;
	}

}
