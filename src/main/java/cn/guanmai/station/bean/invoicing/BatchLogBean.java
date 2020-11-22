package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Mar 27, 2019 3:18:35 PM 
* @des 批次历史记录结果封装类
* @url /station/new#/sales_invoicing/inventory/product/change_record/T10001-JHD-2019-03-27-00003-00001
* @version 1.0 
*/
public class BatchLogBean {
	private BigDecimal now_stock_number;
	private BigDecimal old_stock_number;
	private String operate_time;
	private int stock_operate;

	/**
	 * @return the now_stock_number
	 */
	public BigDecimal getNow_stock_number() {
		return now_stock_number;
	}

	/**
	 * @param now_stock_number
	 *            the now_stock_number to set
	 */
	public void setNow_stock_number(BigDecimal now_stock_number) {
		this.now_stock_number = now_stock_number;
	}

	/**
	 * @return the old_stock_number
	 */
	public BigDecimal getOld_stock_number() {
		return old_stock_number;
	}

	/**
	 * @param old_stock_number
	 *            the old_stock_number to set
	 */
	public void setOld_stock_number(BigDecimal old_stock_number) {
		this.old_stock_number = old_stock_number;
	}

	/**
	 * @return the operate_time
	 */
	public String getOperate_time() {
		return operate_time;
	}

	/**
	 * @param operate_time
	 *            the operate_time to set
	 */
	public void setOperate_time(String operate_time) {
		this.operate_time = operate_time;
	}

	/**
	 * @return the stock_operate
	 */
	public int getStock_operate() {
		return stock_operate;
	}

	/**
	 * @param stock_operate
	 *            the stock_operate to set
	 */
	public void setStock_operate(int stock_operate) {
		this.stock_operate = stock_operate;
	}

}
