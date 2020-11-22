package cn.guanmai.manage.bean.finance.param;

/* 
* @author liming 
* @date Jan 17, 2019 10:56:26 AM 
* @des 商户结算订单搜索参数
* @version 1.0 
*/
public class FinanceOrderParamBean {
	// 搜索方式
	private int search_type;
	private String begin_time;
	private String end_time;
	// 支付状态 -1:全部 0:未支付 1:部分支付 2:已支付
	private int pay_status;

	private String search_text;
	private String service_time_id;
	private Integer freeze;
	private String station;
	private int offset;
	private int limit;
	private int count;

	/**
	 * @return the search_type
	 */
	public int getSearch_type() {
		return search_type;
	}

	/**
	 * @param search_type
	 *            the search_type to set
	 */
	public void setSearch_type(int search_type) {
		this.search_type = search_type;
	}

	/**
	 * @return the begin_time
	 */
	public String getBegin_time() {
		return begin_time;
	}

	/**
	 * @param begin_time
	 *            the begin_time to set
	 */
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}

	/**
	 * @return the end_time
	 */
	public String getEnd_time() {
		return end_time;
	}

	/**
	 * @param end_time
	 *            the end_time to set
	 */
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	/**
	 * @return the pay_status
	 */
	public int getPay_status() {
		return pay_status;
	}

	/**
	 * 支付状态 -1:全部 0:未支付 1:部分支付 2:已支付
	 * 
	 * @param pay_status
	 *            the pay_status to set
	 */
	public void setPay_status(int pay_status) {
		this.pay_status = pay_status;
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
	 * @return the service_time_id
	 */
	public String getService_time_id() {
		return service_time_id;
	}

	/**
	 * @param service_time_id
	 *            the service_time_id to set
	 */
	public void setService_time_id(String service_time_id) {
		this.service_time_id = service_time_id;
	}

	/**
	 * @return the freeze
	 */
	public int getFreeze() {
		return freeze;
	}

	/**
	 * @param freeze
	 *            the freeze to set
	 */
	public void setFreeze(Integer freeze) {
		this.freeze = freeze;
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
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * 按下单日期方式搜索商户结算订单的构造方法
	 * 
	 * @param begin_time
	 * @param end_time
	 */
	public FinanceOrderParamBean(String begin_time, String end_time) {
		this.pay_status = -1;
		this.search_type = 2;
		this.offset = 0;
		this.limit = 10;
		this.begin_time = begin_time;
		this.end_time = end_time;
	}

}
