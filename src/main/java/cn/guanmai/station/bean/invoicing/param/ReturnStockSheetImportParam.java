package cn.guanmai.station.bean.invoicing.param;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年7月24日 下午4:34:52
 * @des 接口 /stock/return_stock_sheet/import 对应的参数
 * @version 1.0
 */
public class ReturnStockSheetImportParam {
	private String supplier_id;
	private String return_sku_id;
	private BigDecimal return_stock_amount;
	private BigDecimal unit_price;
	private BigDecimal sku_money;
	private BigDecimal different_price;

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
	 * @return the return_sku_id
	 */
	public String getReturn_sku_id() {
		return return_sku_id;
	}

	/**
	 * @param return_sku_id
	 *            the return_sku_id to set
	 */
	public void setReturn_sku_id(String return_sku_id) {
		this.return_sku_id = return_sku_id;
	}

	/**
	 * @return the return_stock_amount
	 */
	public BigDecimal getReturn_stock_amount() {
		return return_stock_amount;
	}

	/**
	 * @param return_stock_amount
	 *            the return_stock_amount to set
	 */
	public void setReturn_stock_amount(BigDecimal return_stock_amount) {
		this.return_stock_amount = return_stock_amount;
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

}
