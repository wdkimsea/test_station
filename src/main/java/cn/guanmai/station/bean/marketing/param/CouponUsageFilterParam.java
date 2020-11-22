package cn.guanmai.station.bean.marketing.param;

/**
 * @author: liming
 * @Date: 2020年6月1日 下午5:23:46
 * @description:
 * @version: 1.0
 */

public class CouponUsageFilterParam {
	private String q;
	private String collect_begin_time;
	private String collect_end_time;
	private Integer status;
	private Integer async;
	private int search_type = 1;
	private int limit = 10;
	private int offset = 0;
	private int peek = 60;

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getCollect_begin_time() {
		return collect_begin_time;
	}

	public void setCollect_begin_time(String collect_begin_time) {
		this.collect_begin_time = collect_begin_time;
	}

	public String getCollect_end_time() {
		return collect_end_time;
	}

	public void setCollect_end_time(String collect_end_time) {
		this.collect_end_time = collect_end_time;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getAsync() {
		return async;
	}

	public void setAsync(Integer async) {
		this.async = async;
	}

	public int getSearch_type() {
		return search_type;
	}

	public void setSearch_type(int search_type) {
		this.search_type = search_type;
	}

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

	public int getPeek() {
		return peek;
	}

	public void setPeek(int peek) {
		this.peek = peek;
	}

}
