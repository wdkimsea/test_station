package cn.guanmai.station.bean.purchase.param;

/**
 * @author liming
 * @date 2019年11月1日
 * @time 下午6:59:15
 * @des TODO
 */

public class PrioritySupplierFilterParam {
	private int limit = 10;
	private int offset = 0;
	private String sku_id;
	private String q;

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

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

}
