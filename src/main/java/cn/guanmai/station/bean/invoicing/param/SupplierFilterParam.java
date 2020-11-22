package cn.guanmai.station.bean.invoicing.param;

/**
 * @author liming
 * @date 2019年11月25日
 * @time 下午3:38:14
 * @des TODO
 */

public class SupplierFilterParam {
	private String search_text;
	private int offset = 0;
	private int limit = 20;

	public String getSearch_text() {
		return search_text;
	}

	public void setSearch_text(String search_text) {
		this.search_text = search_text;
	}

	public int getOffset() {
		return offset;
	}

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
