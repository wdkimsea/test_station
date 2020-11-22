package cn.guanmai.station.bean.invoicing.param;

/**
 * @author liming
 * @date 2019年7月29日 下午4:05:08
 * @des 基础的参数类,很多的接口可以共用
 * @version 1.0
 */
public class BaseParam {
	private String begin;
	private String end;
	private String q;
	private int limit = 50;
	private int offset = 0;
	private String page_obj; // 新的翻页模式特有参数

	/**
	 * @return the begin
	 */
	public String getBegin() {
		return begin;
	}

	/**
	 * @param begin the begin to set
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
	 * @param end the end to set
	 */
	public void setEnd(String end) {
		this.end = end;
	}

	/**
	 * @return the q
	 */
	public String getQ() {
		return q;
	}

	/**
	 * @param q the q to set
	 */
	public void setQ(String q) {
		this.q = q;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
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
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * 新的翻页模式特有参数
	 * 
	 * @return the page_obj
	 */
	public String getPage_obj() {
		return page_obj;
	}

	/**
	 * 新的翻页模式特有参数
	 * 
	 * @param page_obj the page_obj to set
	 */
	public void setPage_obj(String page_obj) {
		this.page_obj = page_obj;
	}

}
