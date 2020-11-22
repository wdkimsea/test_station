package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Feb 28, 2019 2:17:49 PM 
* @des SPU库存变动日志
* @version 1.0 
*/
public class StockChangeLogBean {
	private BigDecimal old_avg_price;
	private BigDecimal old_stock;
	private BigDecimal old_stock_value;

	// 变动库存
	private BigDecimal amount;

	// 变动后库存均价
	private BigDecimal avg_price;
	private String change_type;
	private String create_time;
	private String operator;
	private String sheet_number;

	// 变动后库存
	private BigDecimal stock;
	// 变动后库存均价
	private BigDecimal stock_value;
	// 库存货值变动
	private BigDecimal stock_value_change;

	/**
	 * @return the old_avg_price
	 */
	public BigDecimal getOld_avg_price() {
		return old_avg_price;
	}

	/**
	 * @param old_avg_price
	 *            the old_avg_price to set
	 */
	public void setOld_avg_price(BigDecimal old_avg_price) {
		this.old_avg_price = old_avg_price;
	}

	/**
	 * @return the old_stock
	 */
	public BigDecimal getOld_stock() {
		return old_stock;
	}

	/**
	 * @param old_stock
	 *            the old_stock to set
	 */
	public void setOld_stock(BigDecimal old_stock) {
		this.old_stock = old_stock;
	}

	/**
	 * @return the old_stock_value
	 */
	public BigDecimal getOld_stock_value() {
		return old_stock_value;
	}

	/**
	 * @param old_stock_value
	 *            the old_stock_value to set
	 */
	public void setOld_stock_value(BigDecimal old_stock_value) {
		this.old_stock_value = old_stock_value;
	}

	/**
	 * 变动库存
	 * 
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 变动后库存均价
	 * 
	 * @return the avg_price
	 */
	public BigDecimal getAvg_price() {
		return avg_price;
	}

	/**
	 * @param avg_price
	 *            the avg_price to set
	 */
	public void setAvg_price(BigDecimal avg_price) {
		this.avg_price = avg_price;
	}

	/**
	 * @return the change_type
	 */
	public String getChange_type() {
		return change_type;
	}

	/**
	 * @param change_type
	 *            the change_type to set
	 */
	public void setChange_type(String change_type) {
		this.change_type = change_type;
	}

	/**
	 * @return the create_time
	 */
	public String getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time
	 *            the create_time to set
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @return the sheet_number
	 */
	public String getSheet_number() {
		return sheet_number;
	}

	/**
	 * @param sheet_number
	 *            the sheet_number to set
	 */
	public void setSheet_number(String sheet_number) {
		this.sheet_number = sheet_number;
	}

	/**
	 * 变动后库存
	 * 
	 * @return the stock
	 */
	public BigDecimal getStock() {
		return stock;
	}

	/**
	 * @param stock
	 *            the stock to set
	 */
	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	/**
	 * 变动后库存货值
	 * 
	 * @return the stock_value
	 */
	public BigDecimal getStock_value() {
		return stock_value;
	}

	/**
	 * 变动后库存货值
	 * 
	 * @param stock_value
	 *            the stock_value to set
	 */
	public void setStock_value(BigDecimal stock_value) {
		this.stock_value = stock_value;
	}

	/**
	 * 库存货值变动
	 * 
	 * @return the stock_value_change
	 */
	public BigDecimal getStock_value_change() {
		return stock_value_change;
	}

	/**
	 * @param stock_value_change
	 *            the stock_value_change to set
	 */
	public void setStock_value_change(BigDecimal stock_value_change) {
		this.stock_value_change = stock_value_change;
	}
}
