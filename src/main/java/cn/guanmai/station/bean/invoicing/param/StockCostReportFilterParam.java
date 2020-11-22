package cn.guanmai.station.bean.invoicing.param;

/**
 * @author liming
 * @date 2019年7月24日 下午3:58:34
 * @des 接口 /station/report/value 对应的参数
 * @version 1.0
 */
public class StockCostReportFilterParam {
	private String begin;
	private String end;
	private int view_type;
	private int limit;
	private int offset;

	/**
	 * @return the begin
	 */
	public String getBegin() {
		return begin;
	}

	/**
	 * @param begin
	 *            the begin to set
	 */
	public void setBegin(String begin) {
		this.begin = begin;
	}

	/**
	 * @return the end
	 */
	public String getEnd() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(String end) {
		this.end = end;
	}

	/**
	 * @return the view_type
	 */
	public int getView_type() {
		return view_type;
	}

	/**
	 * @param view_type
	 *            the view_type to set
	 */
	public void setView_type(int view_type) {
		this.view_type = view_type;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

}
