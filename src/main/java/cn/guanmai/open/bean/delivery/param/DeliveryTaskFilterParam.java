package cn.guanmai.open.bean.delivery.param;

/* 
* @author liming 
* @date Jun 5, 2019 2:39:57 PM 
* @des 接口 /delivery/task/list 对应的参数
* @version 1.0 
*/
public class DeliveryTaskFilterParam {
	private String query_type;
	private String start_date;
	private String end_date;
	private String driver_id;
	private String carrier_id;
	private String route_id;
	private int offset;
	private int limit;

	public String getQuery_type() {
		return query_type;
	}

	public void setQuery_type(String query_type) {
		this.query_type = query_type;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	/**
	 * @return the driver_id
	 */
	public String getDriver_id() {
		return driver_id;
	}

	/**
	 * @param driver_id the driver_id to set
	 */
	public void setDriver_id(String driver_id) {
		this.driver_id = driver_id;
	}

	/**
	 * @return the carrier_id
	 */
	public String getCarrier_id() {
		return carrier_id;
	}

	/**
	 * @param carrier_id the carrier_id to set
	 */
	public void setCarrier_id(String carrier_id) {
		this.carrier_id = carrier_id;
	}

	/**
	 * @return the route_id
	 */
	public String getRoute_id() {
		return route_id;
	}

	/**
	 * @param route_id the route_id to set
	 */
	public void setRoute_id(String route_id) {
		this.route_id = route_id;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

}
