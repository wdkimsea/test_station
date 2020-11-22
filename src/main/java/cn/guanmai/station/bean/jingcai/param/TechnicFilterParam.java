package cn.guanmai.station.bean.jingcai.param;

/**
 * @author: liming
 * @Date: 2020年4月27日 下午6:06:04
 * @description:
 * @version: 1.0
 */

public class TechnicFilterParam {
	private String q;
	private String technic_category_id;
	private int peek = 60;
	private int limit = 10;
	private int offset = 0;
	private String page_obj;

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getTechnic_category_id() {
		return technic_category_id;
	}

	public void setTechnic_category_id(String technic_category_id) {
		this.technic_category_id = technic_category_id;
	}

	public int getPeek() {
		return peek;
	}

	public void setPeek(int peek) {
		this.peek = peek;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getPage_obj() {
		return page_obj;
	}

	public void setPage_obj(String page_obj) {
		this.page_obj = page_obj;
	}

}
