package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Mar 27, 2019 10:10:20 AM 
* @des 先进先出出库单搜索商品对应的批次信息类
* @version 1.0 
*/
public class ReturnStockBatchBean {
	private String batch_number;
	private String in_stock_time;
	private String life_time;
	private BigDecimal remain;
	private BigDecimal avg_price;
	private String shelf_location_id;
	private String shelf_name;
	private String supplier_id;
	private String supplier_name;

	/**
	 * @return the batch_number
	 */
	public String getBatch_number() {
		return batch_number;
	}

	/**
	 * @param batch_number the batch_number to set
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
	 * @param in_stock_time the in_stock_time to set
	 */
	public void setIn_stock_time(String in_stock_time) {
		this.in_stock_time = in_stock_time;
	}

	/**
	 * @return the life_time
	 */
	public String getLife_time() {
		return life_time;
	}

	/**
	 * @param life_time the life_time to set
	 */
	public void setLife_time(String life_time) {
		this.life_time = life_time;
	}

	/**
	 * @return the remain
	 */
	public BigDecimal getRemain() {
		return remain.setScale(2, BigDecimal.ROUND_HALF_DOWN);
	}

	/**
	 * @param remain the remain to set
	 */
	public void setRemain(BigDecimal remain) {
		this.remain = remain;
	}

	public BigDecimal getAvg_price() {
		return avg_price.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
	}

	public void setAvg_price(BigDecimal avg_price) {
		this.avg_price = avg_price;
	}

	/**
	 * @return the shelf_location_id
	 */
	public String getShelf_location_id() {
		return shelf_location_id;
	}

	/**
	 * @param shelf_location_id the shelf_location_id to set
	 */
	public void setShelf_location_id(String shelf_location_id) {
		this.shelf_location_id = shelf_location_id;
	}

	/**
	 * @return the shelf_name
	 */
	public String getShelf_name() {
		return shelf_name;
	}

	/**
	 * @param shelf_name the shelf_name to set
	 */
	public void setShelf_name(String shelf_name) {
		this.shelf_name = shelf_name;
	}

	/**
	 * @return the supplier_id
	 */
	public String getSupplier_id() {
		return supplier_id;
	}

	/**
	 * @param supplier_id the supplier_id to set
	 */
	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	/**
	 * @return the supplier_name
	 */
	public String getSupplier_name() {
		return supplier_name;
	}

	/**
	 * @param supplier_name the supplier_name to set
	 */
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

}
