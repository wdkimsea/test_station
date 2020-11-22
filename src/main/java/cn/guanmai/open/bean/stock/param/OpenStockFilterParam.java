package cn.guanmai.open.bean.stock.param;

/**
 * @author liming
 * @date 2019年12月18日
 * @time 上午10:57:02
 * @des TODO
 */

public class OpenStockFilterParam {
	private String category1_id;
	private String category2_id;
	private String status;
	private String offset;
	private String limit;

	public String getCategory1_id() {
		return category1_id;
	}

	public void setCategory1_id(String category1_id) {
		this.category1_id = category1_id;
	}

	public String getCategory2_id() {
		return category2_id;
	}

	public void setCategory2_id(String category2_id) {
		this.category2_id = category2_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}
}
