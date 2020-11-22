package cn.guanmai.bshop.bean.invoicing.param;


/**
 * @author liming
 * @date 2019年9月26日
 * @time 下午3:48:55
 * @des bshop 接口 /stock/spu/list 对应的参数
 */

public class BshopSpuStockFilterParam {
	private int offset = 0;
	private int limit = 10;
	private String address_id;
	private String search;
	private int sort = 1;
	private Object page_obj;

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

	public String getAddress_id() {
		return address_id;
	}

	public void setAddress_id(String address_id) {
		this.address_id = address_id.replaceFirst("S(0*)", "");
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public Object getPage_obj() {
		return page_obj;
	}

	public void setPage_obj(Object page_obj) {
		this.page_obj = page_obj;
	}

}
