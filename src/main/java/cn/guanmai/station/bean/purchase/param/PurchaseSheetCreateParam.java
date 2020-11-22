package cn.guanmai.station.bean.purchase.param;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date Nov 28, 2018 11:59:20 AM 
* @des 创建采购单据时的参数
* @version 1.0 
*/
public class PurchaseSheetCreateParam {
	private List<BigDecimal> release_ids;
	private int q_type;
	private String begin_time;
	private String end_time;
	private List<String> category1_ids;
	private List<String> category2_ids;
	private List<String> pinlei_ids;

	/**
	 * @return the release_ids
	 */
	public List<BigDecimal> getRelease_ids() {
		return release_ids;
	}

	/**
	 * @param release_ids the release_ids to set
	 */
	public void setRelease_ids(List<BigDecimal> release_ids) {
		this.release_ids = release_ids;
	}

	/**
	 * @return the q_type
	 */
	public int getQ_type() {
		return q_type;
	}

	/**
	 * 参数样式: 1
	 * 
	 * @param q_type the q_type to set
	 */
	public void setQ_type(int q_type) {
		this.q_type = q_type;
	}

	/**
	 * @return the begin_time
	 */
	public String getBegin_time() {
		return begin_time;
	}

	/**
	 * 参数样式: 2018-11-28 00:00:00
	 * 
	 * @param begin_time the begin_time to set
	 */
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}

	/**
	 * @return the end_time
	 */
	public String getEnd_time() {
		return end_time;
	}

	/**
	 * 参数样式: 2018-11-28 00:00:00
	 * 
	 * @param end_time the end_time to set
	 */
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public List<String> getCategory1_ids() {
		return category1_ids;
	}

	public void setCategory1_ids(List<String> category1_ids) {
		this.category1_ids = category1_ids;
	}

	public List<String> getCategory2_ids() {
		return category2_ids;
	}

	public void setCategory2_ids(List<String> category2_ids) {
		this.category2_ids = category2_ids;
	}

	public List<String> getPinlei_ids() {
		return pinlei_ids;
	}

	public void setPinlei_ids(List<String> pinlei_ids) {
		this.pinlei_ids = pinlei_ids;
	}

}
