package cn.guanmai.station.bean.invoicing.param;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年7月18日 下午8:10:43
 * @des 入库单批量导入参数 /stock/in_stock_sheet/material/import
 * @version 1.0
 */
public class InStockSheetImportParam {
	private String supplier_id;
	private String purchase_sku_id;
	private String spec_id;
	private BigDecimal in_stock_amount;
	private BigDecimal different_price;
	private BigDecimal unit_price;
	private BigDecimal purchase_amount;
	private BigDecimal purchase_price;
	private BigDecimal sku_money;
	private String remark;

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

	/**
	 * @return the purchase_sku_id
	 */
	public String getPurchase_sku_id() {
		return purchase_sku_id;
	}

	/**
	 * @param purchase_sku_id
	 *            the purchase_sku_id to set
	 */
	public void setPurchase_sku_id(String purchase_sku_id) {
		this.purchase_sku_id = purchase_sku_id;
	}

	/**
	 * @return the spec_id
	 */
	public String getSpec_id() {
		return spec_id;
	}

	/**
	 * @param spec_id
	 *            the spec_id to set
	 */
	public void setSpec_id(String spec_id) {
		this.spec_id = spec_id;
	}

	/**
	 * @return the in_stock_amount
	 */
	public BigDecimal getIn_stock_amount() {
		return in_stock_amount;
	}

	/**
	 * @param in_stock_amount
	 *            the in_stock_amount to set
	 */
	public void setIn_stock_amount(BigDecimal in_stock_amount) {
		this.in_stock_amount = in_stock_amount;
	}

	/**
	 * @return the different_price
	 */
	public BigDecimal getDifferent_price() {
		return different_price;
	}

	/**
	 * @param different_price
	 *            the different_price to set
	 */
	public void setDifferent_price(BigDecimal different_price) {
		this.different_price = different_price;
	}

	/**
	 * @return the unit_price
	 */
	public BigDecimal getUnit_price() {
		return unit_price;
	}

	/**
	 * @param unit_price
	 *            the unit_price to set
	 */
	public void setUnit_price(BigDecimal unit_price) {
		this.unit_price = unit_price;
	}

	/**
	 * @return the purchase_amount
	 */
	public BigDecimal getPurchase_amount() {
		return purchase_amount;
	}

	/**
	 * @param purchase_amount
	 *            the purchase_amount to set
	 */
	public void setPurchase_amount(BigDecimal purchase_amount) {
		this.purchase_amount = purchase_amount;
	}

	/**
	 * @return the purchase_price
	 */
	public BigDecimal getPurchase_price() {
		return purchase_price;
	}

	/**
	 * @param purchase_price
	 *            the purchase_price to set
	 */
	public void setPurchase_price(BigDecimal purchase_price) {
		this.purchase_price = purchase_price;
	}

	/**
	 * @return the sku_money
	 */
	public BigDecimal getSku_money() {
		return sku_money;
	}

	/**
	 * @param sku_money
	 *            the sku_money to set
	 */
	public void setSku_money(BigDecimal sku_money) {
		this.sku_money = sku_money;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
