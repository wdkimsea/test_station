package cn.guanmai.station.bean.weight;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

import cn.guanmai.util.BooleanTypeAdapter;

/* 
* @author liming 
* @date Apr 11, 2019 2:15:43 PM 
* @des 称重商品
* @version 1.0 
*/
public class WeightSkuBean {
	private String order_id;

	@JSONField(name = "id")
	private String sku_id;
	private String spu_id;
	private String remark;
	private String route_name;
	private String last_print_time;

	private BigDecimal quantity;
	private BigDecimal weighting_quantity;
	private BigDecimal real_quantity;
	private BigDecimal sale_ratio;

	@JSONField(serializeUsing = BooleanTypeAdapter.class)
	private boolean is_weight;
	@JSONField(serializeUsing = BooleanTypeAdapter.class)
	private boolean has_weighted;
	private boolean out_of_stock;

	private int sort_way;

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
	 * @return the spu_id
	 */
	public String getSpu_id() {
		return spu_id;
	}

	/**
	 * @param spu_id the spu_id to set
	 */
	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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

	/**
	 * @return the weighting_quantity
	 */
	public BigDecimal getWeighting_quantity() {
		return weighting_quantity;
	}

	/**
	 * @param weighting_quantity the weighting_quantity to set
	 */
	public void setWeighting_quantity(BigDecimal weighting_quantity) {
		this.weighting_quantity = weighting_quantity;
	}

	/**
	 * @return the real_quantity
	 */
	public BigDecimal getReal_quantity() {
		return real_quantity;
	}

	/**
	 * @param real_quantity the real_quantity to set
	 */
	public void setReal_quantity(BigDecimal real_quantity) {
		this.real_quantity = real_quantity;
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
	 * @return the is_weight
	 */
	public boolean isIs_weight() {
		return is_weight;
	}

	/**
	 * @param is_weight the is_weight to set
	 */
	public void setIs_weight(boolean is_weight) {
		this.is_weight = is_weight;
	}

	/**
	 * @return the has_weighted
	 */
	public boolean isHas_weighted() {
		return has_weighted;
	}

	/**
	 * @param has_weighted the has_weighted to set
	 */
	public void setHas_weighted(boolean has_weighted) {
		this.has_weighted = has_weighted;
	}

	/**
	 * @return the out_of_stock
	 */
	public boolean isOut_of_stock() {
		return out_of_stock;
	}

	/**
	 * @param out_of_stock the out_of_stock to set
	 */
	public void setOut_of_stock(boolean out_of_stock) {
		this.out_of_stock = out_of_stock;
	}

	/**
	 * @return the last_print_time
	 */
	public String getLast_print_time() {
		return last_print_time;
	}

	/**
	 * @param last_print_time the last_print_time to set
	 */
	public void setLast_print_time(String last_print_time) {
		this.last_print_time = last_print_time;
	}

	/**
	 * @return the sort_way
	 */
	public int getSort_way() {
		return sort_way;
	}

	/**
	 * @param sort_way the sort_way to set
	 */
	public void setSort_way(int sort_way) {
		this.sort_way = sort_way;
	}

}
