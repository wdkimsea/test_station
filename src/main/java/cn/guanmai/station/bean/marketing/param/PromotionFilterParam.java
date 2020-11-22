package cn.guanmai.station.bean.marketing.param;

/* 
* @author liming 
* @date Feb 21, 2019 10:41:15 AM 
* @des 营销活动搜索过滤参数封装类
* @version 1.0 
*/
public class PromotionFilterParam {
	private Integer active;
	private Integer type;
	private Integer show_method;
	private String search_text;
	private int offset = 0;
	private int limit = 10;

	/**
	 * @return the active
	 */
	public Integer getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(Integer active) {
		this.active = active;
	}

	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @return the show_method
	 */
	public Integer getShow_method() {
		return show_method;
	}

	/**
	 * @param show_method the show_method to set
	 */
	public void setShow_method(Integer show_method) {
		this.show_method = show_method;
	}

	/**
	 * @return the search_text
	 */
	public String getSearch_text() {
		return search_text;
	}

	/**
	 * @param search_text the search_text to set
	 */
	public void setSearch_text(String search_text) {
		this.search_text = search_text;
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

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
