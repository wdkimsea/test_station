package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Mar 28, 2019 2:37:04 PM 
* @des 供应商结款单
* @url /station/new#/sales_invoicing/finance/payment_review
* @version 1.0 
*/
public class SettleSheetBean {
	private String id;
	private String date_time;
	private String settle_supplier_id;
	private Integer supplier_status;
	private BigDecimal sku_money;
	private String submit_time;
	private String supplier_name;
	private BigDecimal total_price;
	private int type;

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

	public Integer getSupplier_status() {
		return supplier_status;
	}

	public void setSupplier_status(Integer supplier_status) {
		this.supplier_status = supplier_status;
	}

	/**
	 * @return the sku_money
	 */
	public BigDecimal getSku_money() {
		return sku_money.divide(new BigDecimal("100").setScale(2, BigDecimal.ROUND_HALF_UP));
	}

	/**
	 * @param sku_money the sku_money to set
	 */
	public void setSku_money(BigDecimal sku_money) {
		this.sku_money = sku_money;
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
	 * @return the total_price
	 */
	public BigDecimal getTotal_price() {
		return total_price;
	}

	/**
	 * @param total_price the total_price to set
	 */
	public void setTotal_price(BigDecimal total_price) {
		this.total_price = total_price;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

}
