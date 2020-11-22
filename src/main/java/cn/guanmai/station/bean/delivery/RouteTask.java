package cn.guanmai.station.bean.delivery;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Apr 1, 2019 7:46:47 PM 
* @des 线路任务
* @version 1.0 
*/
public class RouteTask {
	@JSONField(name="id")
	private BigDecimal route_id;
	private String route_name;
	private boolean has_unweighted;
	private int order_amount;
	private List<String> order_ids;
	private BigDecimal sale_money;

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

	/**
	 * @return the has_unweighted
	 */
	public boolean isHas_unweighted() {
		return has_unweighted;
	}

	/**
	 * @param has_unweighted
	 *            the has_unweighted to set
	 */
	public void setHas_unweighted(boolean has_unweighted) {
		this.has_unweighted = has_unweighted;
	}

	/**
	 * @return the order_amount
	 */
	public int getOrder_amount() {
		return order_amount;
	}

	/**
	 * @param order_amount
	 *            the order_amount to set
	 */
	public void setOrder_amount(int order_amount) {
		this.order_amount = order_amount;
	}

	/**
	 * @return the order_ids
	 */
	public List<String> getOrder_ids() {
		return order_ids;
	}

	/**
	 * @param order_ids
	 *            the order_ids to set
	 */
	public void setOrder_ids(List<String> order_ids) {
		this.order_ids = order_ids;
	}

	/**
	 * @return the sale_money
	 */
	public BigDecimal getSale_money() {
		return sale_money;
	}

	/**
	 * @param sale_money
	 *            the sale_money to set
	 */
	public void setSale_money(BigDecimal sale_money) {
		this.sale_money = sale_money;
	}
}
