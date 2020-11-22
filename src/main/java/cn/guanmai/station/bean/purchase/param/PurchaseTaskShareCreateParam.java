package cn.guanmai.station.bean.purchase.param;

/**
 * @author liming
 * @date 2019年11月11日
 * @time 上午11:24:33
 * @des TODO
 */

public class PurchaseTaskShareCreateParam {
	private int q_type = 1;
	private String q;
	private String settle_supplier_id;
	private String begin_time;
	private String end_time;

	public int getQ_type() {
		return q_type;
	}

	public void setQ_type(int q_type) {
		this.q_type = q_type;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
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

}
