package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年7月29日 下午5:38:16
 * @des 接口 /stock/out_stock_summary_by_spu/get 对应的结果
 * @version 1.0
 */
public class StockSummaryBean {
	private int data_num;
	private int spu_num;
	private BigDecimal total_value;

	/**
	 * @return the data_num
	 */
	public int getData_num() {
		return data_num;
	}

	/**
	 * @param data_num
	 *            the data_num to set
	 */
	public void setData_num(int data_num) {
		this.data_num = data_num;
	}

	/**
	 * @return the spu_num
	 */
	public int getSpu_num() {
		return spu_num;
	}

	/**
	 * @param spu_num
	 *            the spu_num to set
	 */
	public void setSpu_num(int spu_num) {
		this.spu_num = spu_num;
	}

	/**
	 * @return the total_value
	 */
	public BigDecimal getTotal_value() {
		return total_value;
	}

	/**
	 * @param total_value
	 *            the total_value to set
	 */
	public void setTotal_value(BigDecimal total_value) {
		this.total_value = total_value;
	}

}
