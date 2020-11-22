package cn.guanmai.station.bean.marketing.param;

/* 
* @author liming 
* @date Feb 19, 2019 5:43:31 PM 
* @des 搜素过滤限时锁价参数类
* @version 1.0 
*/
public class PriceRuleFilterParam {
	private String price_rule_id;
	private String salemenu_id;
	private String salemenu_name;
	private String station_id;
	private Integer status;
	private int cur_page;
	private int cnt_per_page;

	/**
	 * @return the price_rule_id
	 */
	public String getPrice_rule_id() {
		return price_rule_id;
	}

	/**
	 * @param price_rule_id
	 *            the price_rule_id to set
	 */
	public void setPrice_rule_id(String price_rule_id) {
		this.price_rule_id = price_rule_id;
	}

	/**
	 * @return the salemenu_id
	 */
	public String getSalemenu_id() {
		return salemenu_id;
	}

	/**
	 * @param salemenu_id
	 *            the salemenu_id to set
	 */
	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
	}

	/**
	 * @return the salemenu_name
	 */
	public String getSalemenu_name() {
		return salemenu_name;
	}

	/**
	 * @param salemenu_name
	 *            the salemenu_name to set
	 */
	public void setSalemenu_name(String salemenu_name) {
		this.salemenu_name = salemenu_name;
	}

	/**
	 * @return the station_id
	 */
	public String getStation_id() {
		return station_id;
	}

	/**
	 * @param station_id
	 *            the station_id to set
	 */
	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the cur_page
	 */
	public int getCur_page() {
		return cur_page;
	}

	/**
	 * @param cur_page
	 *            the cur_page to set
	 */
	public void setCur_page(int cur_page) {
		this.cur_page = cur_page;
	}

	/**
	 * @return the cnt_per_page
	 */
	public int getCnt_per_page() {
		return cnt_per_page;
	}

	/**
	 * @param cnt_per_page
	 *            the cnt_per_page to set
	 */
	public void setCnt_per_page(int cnt_per_page) {
		this.cnt_per_page = cnt_per_page;
	}

	/**
	 * 搜索过滤锁价规则必填参数构造方法
	 * 
	 * @param status
	 * @param cur_page
	 * @param cnt_per_page
	 */
	public PriceRuleFilterParam(Integer status, int cur_page, int cnt_per_page) {
		super();
		this.status = status;
		this.cur_page = cur_page;
		this.cnt_per_page = cnt_per_page;
	}

}
