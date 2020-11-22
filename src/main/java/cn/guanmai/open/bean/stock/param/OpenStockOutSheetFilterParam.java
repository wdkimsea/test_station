package cn.guanmai.open.bean.stock.param;

/**
 * @author liming
 * @date 2019年10月22日
 * @time 下午7:13:58
 * @des TODO
 */

public class OpenStockOutSheetFilterParam {
	private String start_date;
	private String end_date;
	private String status;
	private String offset;
	private String limit;

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
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
