package cn.guanmai.station.bean.purchase.param;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年8月14日
 * @time 上午11:05:58
 * @des TODO
 */

public class PurchaseTaskHistoryFilterParam {
	private int q_type;
	private String sku_id;
	private BigDecimal release_id;
	private String begin_time;
	private String end_time;

	public int getQ_type() {
		return q_type;
	}

	public void setQ_type(int q_type) {
		this.q_type = q_type;
	}

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public BigDecimal getRelease_id() {
		return release_id;
	}

	public void setRelease_id(BigDecimal release_id) {
		this.release_id = release_id;
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
