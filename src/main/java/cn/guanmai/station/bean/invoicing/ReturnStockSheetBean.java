package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Feb 27, 2019 4:47:27 PM 
* @des 成品退货列表类
* @version 1.0 
*/
public class ReturnStockSheetBean {
	private String id;
	private String settle_supplier_id;
	private String supplier_customer_id;
	private String supplier_name;
	private BigDecimal delta_money;
	private BigDecimal sku_money;
	private BigDecimal total_money;
	private Integer status;
	private Integer supplier_status;
	private String date_time;
	private String submit_time;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the settle_supplier_id
	 */
	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	/**
	 * @param settle_supplier_id the settle_supplier_id to set
	 */
	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
	}

	/**
	 * @return the supplier_customer_id
	 */
	public String getSupplier_customer_id() {
		return supplier_customer_id;
	}

	/**
	 * @param supplier_customer_id the supplier_customer_id to set
	 */
	public void setSupplier_customer_id(String supplier_customer_id) {
		this.supplier_customer_id = supplier_customer_id;
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

	/**
	 * @return the delta_money
	 */
	public BigDecimal getDelta_money() {
		return delta_money.divide(new BigDecimal("100"));
	}

	/**
	 * @param delta_money the delta_money to set
	 */
	public void setDelta_money(BigDecimal delta_money) {
		this.delta_money = delta_money;
	}

	/**
	 * @return the sku_money
	 */
	public BigDecimal getSku_money() {
		return sku_money.divide(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @param sku_money the sku_money to set
	 */
	public void setSku_money(BigDecimal sku_money) {
		this.sku_money = sku_money;
	}

	/**
	 * @return the total_money
	 */
	public BigDecimal getTotal_money() {
		return total_money;
	}

	/**
	 * @param total_money the total_money to set
	 */
	public void setTotal_money(BigDecimal total_money) {
		this.total_money = total_money;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSupplier_status() {
		return supplier_status;
	}

	public void setSupplier_status(Integer supplier_status) {
		this.supplier_status = supplier_status;
	}

	/**
	 * @return the date_time
	 */
	public String getDate_time() {
		return date_time;
	}

	/**
	 * @param date_time the date_time to set
	 */
	public void setDate_time(String date_time) {
		this.date_time = date_time;
	}

	/**
	 * @return the submit_time
	 */
	public String getSubmit_time() {
		return submit_time;
	}

	/**
	 * @param submit_time the submit_time to set
	 */
	public void setSubmit_time(String submit_time) {
		this.submit_time = submit_time;
	}
}
