package cn.guanmai.station.bean.invoicing.param;

/**
 * @author liming
 * @date 2019年11月26日
 * @time 下午7:44:42
 * @des 接口 /stock/shelf/spu/list 对应的参数
 */

public class ShelfSpuFilterParam {
	private String shelf_id;
	private String q;
	private int limit = 40;
	private String page_obj;

	public String getShelf_id() {
		return shelf_id;
	}

	public void setShelf_id(String shelf_id) {
		this.shelf_id = shelf_id;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getPage_obj() {
		return page_obj;
	}

	public void setPage_obj(String page_obj) {
		this.page_obj = page_obj;
	}

}
