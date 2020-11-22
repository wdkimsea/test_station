package cn.guanmai.manage.bean.ordermanage.param;

/**
 * @program: station
 * @description: lk查询信息查询
 * @author: weird
 * @create: 2019-01-21 18:09
 **/
public class LkOrderInfoParamBean {
	private String region;
	private String order_status;
	private String station;

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region
	 *            the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the order_status
	 */
	public String getOrder_status() {
		return order_status;
	}

	/**
	 * @param order_status
	 *            the order_status to set
	 */
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
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

	public LkOrderInfoParamBean(String region, String order_status, String station) {
		this.region = region;
		this.order_status = order_status;
		this.station = station;
	}
}
