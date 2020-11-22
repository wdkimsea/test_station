package cn.guanmai.manage.bean.ordermanage.param;

/**
 * @program: station
 * @description: lk订单搜索
 * @author: weird
 * @create: 2019-01-21 17:49
 **/
public class LkOrderManageParamBean {
	private String start_date;
	private String end_date;
	private String city;
	private String station;
	private String order_id;

	/**
	 * @return the start_date
	 */
	public String getStart_date() {
		return start_date;
	}

	/**
	 * @param start_date
	 *            the start_date to set
	 */
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	/**
	 * @return the end_date
	 */
	public String getEnd_date() {
		return end_date;
	}

	/**
	 * @param end_date
	 *            the end_date to set
	 */
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the station
	 */
	public String getStation() {
		return station;
	}

	/**
	 * @param station
	 *            the station to set
	 */
	public void setStation(String station) {
		this.station = station;
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

	public LkOrderManageParamBean(String start_date, String end_date, String city, String station, String order_id) {
		this.start_date = start_date;
		this.end_date = end_date;
		this.city = city;
		this.station = station;
		this.order_id = order_id;
	}
}
