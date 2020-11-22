package cn.guanmai.station.bean.delivery.param;

/* 
* @author liming 
* @date Jan 4, 2019 3:06:55 PM 
* @des 搜索配送订单参数类
* @version 1.0 
*/
public class DistributeOrderFilterParam {
	private String order_start_time;
	private String order_end_time;
	private int offset;
	private int limit;
	private String search_text;

	private String cycle_start_time;
	private String cycle_end_time;
	private String time_config_id;

	private String receive_start_time;
	private String receive_end_time;

	/**
	 * @return the order_start_time
	 */
	public String getOrder_start_time() {
		return order_start_time;
	}

	/**
	 * @param order_start_time
	 *            the order_start_time to set
	 */
	public void setOrder_start_time(String order_start_time) {
		this.order_start_time = order_start_time;
	}

	/**
	 * @return the order_end_time
	 */
	public String getOrder_end_time() {
		return order_end_time;
	}

	/**
	 * @param order_end_time
	 *            the order_end_time to set
	 */
	public void setOrder_end_time(String order_end_time) {
		this.order_end_time = order_end_time;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset
	 *            the offset to set
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
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the search_text
	 */
	public String getSearch_text() {
		return search_text;
	}

	/**
	 * @param search_text
	 *            the search_text to set
	 */
	public void setSearch_text(String search_text) {
		this.search_text = search_text;
	}

	/**
	 * @return the cycle_start_time
	 */
	public String getCycle_start_time() {
		return cycle_start_time;
	}

	/**
	 * @param cycle_start_time
	 *            the cycle_start_time to set
	 */
	public void setCycle_start_time(String cycle_start_time) {
		this.cycle_start_time = cycle_start_time;
	}

	/**
	 * @return the cycle_end_time
	 */
	public String getCycle_end_time() {
		return cycle_end_time;
	}

	/**
	 * @param cycle_end_time
	 *            the cycle_end_time to set
	 */
	public void setCycle_end_time(String cycle_end_time) {
		this.cycle_end_time = cycle_end_time;
	}

	/**
	 * @return the time_config_id
	 */
	public String getTime_config_id() {
		return time_config_id;
	}

	/**
	 * @param time_config_id
	 *            the time_config_id to set
	 */
	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	/**
	 * @return the receive_start_time
	 */
	public String getReceive_start_time() {
		return receive_start_time;
	}

	/**
	 * @param receive_start_time
	 *            the receive_start_time to set
	 */
	public void setReceive_start_time(String receive_start_time) {
		this.receive_start_time = receive_start_time;
	}

	/**
	 * @return the receive_end_time
	 */
	public String getReceive_end_time() {
		return receive_end_time;
	}

	/**
	 * @param receive_end_time
	 *            the receive_end_time to set
	 */
	public void setReceive_end_time(String receive_end_time) {
		this.receive_end_time = receive_end_time;
	}

	/**
	 * 按下单日期方式搜索的参数构造方法
	 * 
	 * @param order_start_time
	 *            <br/>
	 *            参数样式: 2019-01-04
	 * @param order_end_time
	 *            <br/>
	 *            参数样式: 2019-01-04
	 * @param offset
	 * @param limit
	 * @param search_text
	 */
	public DistributeOrderFilterParam(String order_start_time, String order_end_time, int offset, int limit,
			String search_text) {
		super();
		this.order_start_time = order_start_time;
		this.order_end_time = order_end_time;
		this.offset = offset;
		this.limit = limit;
		this.search_text = search_text;
	}

	/**
	 * 按运营周期方式搜索的参数构造方法
	 * 
	 * 
	 * @param cycle_start_time
	 *            <br/>
	 *            参数样式:2019-01-04 06:00
	 * @param cycle_end_time
	 *            <br/>
	 *            参数样式:2019-01-05 06:00
	 * @param time_config_id
	 * @param offset
	 * @param limit
	 * @param search_text
	 */
	public DistributeOrderFilterParam(String cycle_start_time, String cycle_end_time, String time_config_id, int offset,
			int limit, String search_text) {
		super();
		this.offset = offset;
		this.limit = limit;
		this.cycle_start_time = cycle_start_time;
		this.cycle_end_time = cycle_end_time;
		this.time_config_id = time_config_id;
		this.search_text = search_text;
	}

	/**
	 * 按收货日期方式搜索的参数构造方法
	 * 
	 * @param limit
	 * @param offset
	 * @param receive_start_time
	 *            <br/>
	 *            参数样式: 2019-01-04
	 * @param receive_end_time
	 *            <br/>
	 *            参数样式: 2019-01-04
	 * @param search_text
	 */
	public DistributeOrderFilterParam(int offset, int limit, String receive_start_time, String receive_end_time,
			String search_text) {
		super();
		this.offset = offset;
		this.limit = limit;
		this.receive_start_time = receive_start_time;
		this.receive_end_time = receive_end_time;
		this.search_text = search_text;
	}

}
