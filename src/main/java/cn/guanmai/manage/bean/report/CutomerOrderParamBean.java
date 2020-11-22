package cn.guanmai.manage.bean.report;

/**
 * @program: station
 * @description: 订单统计的查询参数
 * @author: weird
 * @create: 2019-01-18 14:50
 **/
public class CutomerOrderParamBean {
	private String start_time;
	private String end_time;
	private Integer search_type;
	private String search_text;

	/**
	 * @return the start_time
	 */
	public String getStart_time() {
		return start_time;
	}

	/**
	 * @param start_time
	 *            the start_time to set
	 */
	public void setStart_time(String start_time) {
		this.start_time = start_time;
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
	 * @return the search_type
	 */
	public Integer getSearch_type() {
		return search_type;
	}

	/**
	 * @param search_type
	 *            the search_type to set
	 */
	public void setSearch_type(Integer search_type) {
		this.search_type = search_type;
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

	public CutomerOrderParamBean(String start_time, String end_time, Integer search_type, String search_text) {
		this.start_time = start_time;
		this.end_time = end_time;
		this.search_type = search_type;
		this.search_text = search_text;
	}
}
