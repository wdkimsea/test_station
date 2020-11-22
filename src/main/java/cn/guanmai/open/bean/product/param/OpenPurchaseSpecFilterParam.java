package cn.guanmai.open.bean.product.param;

/**
 * @author liming
 * @date 2019年10月23日
 * @time 上午11:27:31
 * @des TODO
 */

public class OpenPurchaseSpecFilterParam {
	private String category1_id;
	private String category2_id;
	private String pinlei_id;
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

	public String getPinlei_id() {
		return pinlei_id;
	}

	public void setPinlei_id(String pinlei_id) {
		this.pinlei_id = pinlei_id;
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
