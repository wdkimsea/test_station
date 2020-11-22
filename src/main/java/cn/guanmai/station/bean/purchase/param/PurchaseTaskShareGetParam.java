package cn.guanmai.station.bean.purchase.param;

/**
 * @author liming
 * @date 2019年11月11日
 * @time 上午11:49:45
 * @des TODO
 */

public class PurchaseTaskShareGetParam {
	private String token;
	private String __trace_group_id;
	private String station_id;
	private String begin_time;
	private String end_time;
	private String q;
	private int q_type = 1;
	private String settle_supplier_id;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String get__trace_group_id() {
		return __trace_group_id;
	}

	public void set__trace_group_id(String __trace_group_id) {
		this.__trace_group_id = __trace_group_id;
	}

	public String getStation_id() {
		return station_id;
	}

	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	public String getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public int getQ_type() {
		return q_type;
	}

	public void setQ_type(int q_type) {
		this.q_type = q_type;
	}

	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
	}

}
