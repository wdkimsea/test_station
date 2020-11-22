package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年7月25日 上午10:28:13
 * @des 接口 /stock/get_batch_out 对应的结果
 * @version 1.0
 */
public class BatchOutStockBean {
	private String batch_number;
	private String in_stock_time;
	private BigDecimal remain;
	private String supplier_id;

	/**
	 * @return the batch_number
	 */
	public String getBatch_number() {
		return batch_number;
	}

	/**
	 * @param batch_number
	 *            the batch_number to set
	 */
	public void setBatch_number(String batch_number) {
		this.batch_number = batch_number;
	}

	/**
	 * @return the in_stock_time
	 */
	public String getIn_stock_time() {
		return in_stock_time;
	}

	/**
	 * @param in_stock_time
	 *            the in_stock_time to set
	 */
	public void setIn_stock_time(String in_stock_time) {
		this.in_stock_time = in_stock_time;
	}

	/**
	 * @return the remain
	 */
	public BigDecimal getRemain() {
		return remain;
	}

	/**
	 * @param remain
	 *            the remain to set
	 */
	public void setRemain(BigDecimal remain) {
		this.remain = remain;
	}

	/**
	 * @return the supplier_id
	 */
	public String getSupplier_id() {
		return supplier_id;
	}

	/**
	 * @param supplier_id
	 *            the supplier_id to set
	 */
	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

}
