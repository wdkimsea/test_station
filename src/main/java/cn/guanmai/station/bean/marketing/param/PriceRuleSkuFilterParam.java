package cn.guanmai.station.bean.marketing.param;

/* 
* @author liming 
* @date Feb 20, 2019 4:37:35 PM 
* @des 限时锁价按商户商品查看过滤搜索参数封装类
* @version 1.0 
*/
public class PriceRuleSkuFilterParam {
	private String address_text;
	private String sku_text;
	private String station_id;
	private int status;
	private int cur_page;
	private int cnt_per_page;
	private Type type;

	public enum Type {
		stataion, customer
	}

	/**
	 * @return the address_text
	 */
	public String getAddress_text() {
		return address_text;
	}

	/**
	 * @param address_text
	 *            the address_text to set
	 */
	public void setAddress_text(String address_text) {
		this.address_text = address_text;
	}

	/**
	 * @return the sku_text
	 */
	public String getSku_text() {
		return sku_text;
	}

	/**
	 * @param sku_text
	 *            the sku_text to set
	 */
	public void setSku_text(String sku_text) {
		this.sku_text = sku_text;
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
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
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
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * 过滤参数必填参数构造方法
	 * 
	 * @param status
	 * @param cur_page
	 * @param cnt_per_page
	 * @param type
	 */
	public PriceRuleSkuFilterParam(int status, int cur_page, int cnt_per_page, Type type) {
		super();
		this.status = status;
		this.cur_page = cur_page;
		this.cnt_per_page = cnt_per_page;
		this.type = type;
	}

}
