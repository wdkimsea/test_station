package cn.guanmai.open.bean.stock.param;

/**
 * @author liming
 * @date 2019年10月21日
 * @time 下午2:29:09
 * @des TODO
 */

public class OpenSupplierFilterParam {
	private String category2_id;
	private String offset = "0";
	private String limit = "20";

	public String getCategory2_id() {
		return category2_id;
	}

	public void setCategory2_id(String category2_id) {
		this.category2_id = category2_id;
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
