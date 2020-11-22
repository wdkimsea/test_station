package cn.guanmai.station.bean.category.param;

/**
 * @author: liming
 * @Date: 2020年2月18日 上午11:07:52
 * @description:
 * @version: 1.0
 */

public class SkuFilterParam {
	private String spu_id;
	private String salemenu_id;
	private String search_text;
	private int limit = 20;

	public String getSpu_id() {
		return spu_id;
	}

	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	public String getSalemenu_id() {
		return salemenu_id;
	}

	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
	}

	public String getSearch_text() {
		return search_text;
	}

	public void setSearch_text(String search_text) {
		this.search_text = search_text;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
