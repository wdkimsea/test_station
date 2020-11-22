package cn.guanmai.station.bean.order;

import java.math.BigDecimal;

/* 
* @author liming 
* @date May 29, 2019 2:51:02 PM 
* @des 接口 /station/order/recent_order/get 对应的结果
*      指定客户指定运营时间下的近10条历史记录
* @version 1.0 
*/
public class RecentOrderBean {
	private String id;
	private String date_time_str;
	private int merchandise_count;
	private BigDecimal origin_total_price;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the date_time_str
	 */
	public String getDate_time_str() {
		return date_time_str;
	}

	/**
	 * @param date_time_str
	 *            the date_time_str to set
	 */
	public void setDate_time_str(String date_time_str) {
		this.date_time_str = date_time_str;
	}

	/**
	 * @return the merchandise_count
	 */
	public int getMerchandise_count() {
		return merchandise_count;
	}

	/**
	 * @param merchandise_count
	 *            the merchandise_count to set
	 */
	public void setMerchandise_count(int merchandise_count) {
		this.merchandise_count = merchandise_count;
	}

	/**
	 * @return the origin_total_price
	 */
	public BigDecimal getOrigin_total_price() {
		return origin_total_price;
	}

	/**
	 * @param origin_total_price
	 *            the origin_total_price to set
	 */
	public void setOrigin_total_price(BigDecimal origin_total_price) {
		this.origin_total_price = origin_total_price;
	}

}
