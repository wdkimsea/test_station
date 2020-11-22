package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Mar 27, 2019 11:44:45 AM 
* @des 先进先出站点商品批次封装类
* @url /station/new#/sales_invoicing/inventory/product/C******
* @version 1.0 
*/
public class StockBatchBean {
	private String batch_number;
	private String category_id_2;
	private String category_name_2;
	private String life_time;
	private BigDecimal price;
	private String purchase_unit;
	private BigDecimal ratio;
	private BigDecimal remain;
	private String settle_supplier_id;
	private String shelf_name;
	private String sku_id;
	private String spu_id;
	private String sku_name;
	private String std_unit;
	private String supplier_customer_id;
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
	 * @return the category_id_2
	 */
	public String getCategory_id_2() {
		return category_id_2;
	}

	/**
	 * @param category_id_2 the category_id_2 to set
	 */
	public void setCategory_id_2(String category_id_2) {
		this.category_id_2 = category_id_2;
	}

	/**
	 * @return the category_name_2
	 */
	public String getCategory_name_2() {
		return category_name_2;
	}

	/**
	 * @param category_name_2 the category_name_2 to set
	 */
	public void setCategory_name_2(String category_name_2) {
		this.category_name_2 = category_name_2;
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
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the purchase_unit
	 */
	public String getPurchase_unit() {
		return purchase_unit;
	}

	/**
	 * @param purchase_unit the purchase_unit to set
	 */
	public void setPurchase_unit(String purchase_unit) {
		this.purchase_unit = purchase_unit;
	}

	/**
	 * @return the ratio
	 */
	public BigDecimal getRatio() {
		return ratio;
	}

	/**
	 * @param ratio the ratio to set
	 */
	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}

	/**
	 * @return the remain
	 */
	public BigDecimal getRemain() {
		return remain.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @param remain the remain to set
	 */
	public void setRemain(BigDecimal remain) {
		this.remain = remain;
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
	 * @return the sku_id
	 */
	public String getSku_id() {
		return sku_id;
	}

	/**
	 * @param sku_id the sku_id to set
	 */
	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public String getSpu_id() {
		return spu_id;
	}

	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	/**
	 * @return the sku_name
	 */
	public String getSku_name() {
		return sku_name;
	}

	/**
	 * @param sku_name the sku_name to set
	 */
	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	/**
	 * @return the std_unit
	 */
	public String getStd_unit() {
		return std_unit;
	}

	/**
	 * @param std_unit the std_unit to set
	 */
	public void setStd_unit(String std_unit) {
		this.std_unit = std_unit;
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

}
