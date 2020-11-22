package cn.guanmai.station.bean.weight;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author liming
 * @date 2019年7月8日 上午11:30:13
 * @des 接口 /weight/weight_collect/order/list 对应的结果, ST-供应链-分拣-分拣明细-按订单分拣
 * @version 1.0
 */
public class WeightCollectOrderBean {
	private String address_name;
	private String driver_name;
	private int finished;
	@JSONField(name = "id")
	private String order_id;
	private Integer order_status;
	private String route;
	private String sord_id;
	private int print_times;
	private int total;

	/**
	 * @return the address_name
	 */
	public String getAddress_name() {
		return address_name;
	}

	/**
	 * @param address_name the address_name to set
	 */
	public void setAddress_name(String address_name) {
		this.address_name = address_name;
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

	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public Integer getOrder_status() {
		return order_status;
	}

	public void setOrder_status(Integer order_status) {
		this.order_status = order_status;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getSord_id() {
		return sord_id;
	}

	public void setSord_id(String sord_id) {
		this.sord_id = sord_id;
	}

	public int getPrint_times() {
		return print_times;
	}

	public void setPrint_times(int print_times) {
		this.print_times = print_times;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
