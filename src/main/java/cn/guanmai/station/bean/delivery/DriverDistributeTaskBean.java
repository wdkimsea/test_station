package cn.guanmai.station.bean.delivery;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONArray;

/* 
* @author liming 
* @date Jan 4, 2019 4:15:31 PM 
* @des 司机配送任务类
* @version 1.0 
*/
public class DriverDistributeTaskBean {
	// 承运商名称
	private String carrier_name;
	// 配送商户数
	private int customer_count;
	// 配送商户id列表
	private JSONArray customer_ids;
	// 司机ID
	private int driver_id;
	// 司机名称
	private String driver_name;
	// 是否含有未称重订单
	private boolean has_unweighted;
	// 配送订单数量
	private int order_count;
	// 配送订单列表
	private JSONArray order_ids;
	// 配送订单金额总额
	private BigDecimal sale_money;

	/**
	 * @return the carrier_name
	 */
	public String getCarrier_name() {
		return carrier_name;
	}

	/**
	 * @param carrier_name the carrier_name to set
	 */
	public void setCarrier_name(String carrier_name) {
		this.carrier_name = carrier_name;
	}

	/**
	 * @return the customer_count
	 */
	public int getCustomer_count() {
		return customer_count;
	}

	/**
	 * @param customer_count the customer_count to set
	 */
	public void setCustomer_count(int customer_count) {
		this.customer_count = customer_count;
	}

	/**
	 * @return the customer_ids
	 */
	public JSONArray getCustomer_ids() {
		return customer_ids;
	}

	/**
	 * @param customer_ids the customer_ids to set
	 */
	public void setCustomer_ids(JSONArray customer_ids) {
		this.customer_ids = customer_ids;
	}

	/**
	 * @return the driver_id
	 */
	public int getDriver_id() {
		return driver_id;
	}

	/**
	 * @param driver_id the driver_id to set
	 */
	public void setDriver_id(int driver_id) {
		this.driver_id = driver_id;
	}

	/**
	 * @return the driver_name
	 */
	public String getDriver_name() {
		return driver_name;
	}

	/**
	 * @param driver_name the driver_name to set
	 */
	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}

	/**
	 * @return the has_unweighted
	 */
	public boolean isHas_unweighted() {
		return has_unweighted;
	}

	/**
	 * @param has_unweighted the has_unweighted to set
	 */
	public void setHas_unweighted(boolean has_unweighted) {
		this.has_unweighted = has_unweighted;
	}

	/**
	 * @return the order_count
	 */
	public int getOrder_count() {
		return order_count;
	}

	/**
	 * @param order_count the order_count to set
	 */
	public void setOrder_count(int order_count) {
		this.order_count = order_count;
	}

	/**
	 * @return the order_ids
	 */
	public JSONArray getOrder_ids() {
		return order_ids;
	}

	/**
	 * @param order_ids the order_ids to set
	 */
	public void setOrder_ids(JSONArray order_ids) {
		this.order_ids = order_ids;
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

}
