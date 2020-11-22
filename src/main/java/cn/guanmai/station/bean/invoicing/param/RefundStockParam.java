package cn.guanmai.station.bean.invoicing.param;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jan 7, 2019 5:22:36 PM 
* @des 商户退货处理请求参数类
* @version 1.0 
*/
public class RefundStockParam {
	private BigDecimal refund_id;
	private String sku_name;
	private String sku_id;
	private int solution;
	private int driver_id;
	private BigDecimal in_stock_price;
	private boolean disabled_in_stock_price;
	private BigDecimal real_amount;
	private BigDecimal store_amount;
	private BigDecimal request_amount;
	private String description;
	private String shelf_name;
	private String shelf_id;
	private String supplier_id;
	private String supplier_name;
	private BigDecimal sale_ratio;
	private String purchase_sku_id;

	/**
	 * @return the refund_id
	 */
	public BigDecimal getRefund_id() {
		return refund_id;
	}

	/**
	 * @param refund_id the refund_id to set
	 */
	public void setRefund_id(BigDecimal refund_id) {
		this.refund_id = refund_id;
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

	/**
	 * @return the solution
	 */
	public int getSolution() {
		return solution;
	}

	/**
	 * 二级入库 160,放弃取货157
	 * 
	 * @param solution the solution to set
	 */
	public void setSolution(int solution) {
		this.solution = solution;
	}

	/**
	 * @return the driver_id
	 */
	public int getDriver_id() {
		return driver_id;
	}

	/**
	 * @param driver_id the driver_id to set
	 */
	public void setDriver_id(int driver_id) {
		this.driver_id = driver_id;
	}

	/**
	 * @return the in_stock_price
	 */
	public BigDecimal getIn_stock_price() {
		return in_stock_price;
	}

	/**
	 * 入库单价(单位为分)
	 * 
	 * @param in_stock_price the in_stock_price to set
	 */
	public void setIn_stock_price(BigDecimal in_stock_price) {
		this.in_stock_price = in_stock_price;
	}

	/**
	 * @return the disabled_in_stock_price
	 */
	public boolean isDisabled_in_stock_price() {
		return disabled_in_stock_price;
	}

	/**
	 * @param disabled_in_stock_price the disabled_in_stock_price to set
	 */
	public void setDisabled_in_stock_price(boolean disabled_in_stock_price) {
		this.disabled_in_stock_price = disabled_in_stock_price;
	}

	/**
	 * @return the real_amount
	 */
	public BigDecimal getReal_amount() {
		return real_amount;
	}

	/**
	 * @param real_amount the real_amount to set
	 */
	public void setReal_amount(BigDecimal real_amount) {
		this.real_amount = real_amount;
	}

	/**
	 * @return the store_amount
	 */
	public BigDecimal getStore_amount() {
		return store_amount;
	}

	/**
	 * @param store_amount the store_amount to set
	 */
	public void setStore_amount(BigDecimal store_amount) {
		this.store_amount = store_amount;
	}

	/**
	 * @return the request_amount
	 */
	public BigDecimal getRequest_amount() {
		return request_amount;
	}

	/**
	 * @param request_amount the request_amount to set
	 */
	public void setRequest_amount(BigDecimal request_amount) {
		this.request_amount = request_amount;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @return the shelf_id
	 */
	public String getShelf_id() {
		return shelf_id;
	}

	/**
	 * @param shelf_id the shelf_id to set
	 */
	public void setShelf_id(String shelf_id) {
		this.shelf_id = shelf_id;
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

	/**
	 * @return the sale_ratio
	 */
	public BigDecimal getSale_ratio() {
		return sale_ratio;
	}

	/**
	 * @param sale_ratio the sale_ratio to set
	 */
	public void setSale_ratio(BigDecimal sale_ratio) {
		this.sale_ratio = sale_ratio;
	}

	/**
	 * @return the purchase_sku_id
	 */
	public String getPurchase_sku_id() {
		return purchase_sku_id;
	}

	/**
	 * @param purchase_sku_id the purchase_sku_id to set
	 */
	public void setPurchase_sku_id(String purchase_sku_id) {
		this.purchase_sku_id = purchase_sku_id;
	}

	public RefundStockParam() {
	}

	/**
	 * 放弃取货的构造方法
	 * 
	 * @param refund_id
	 * @param sku_id
	 * @param sku_name
	 * @param real_amount
	 * @param sale_ratio
	 * @param purchase_sku_id
	 */
	public RefundStockParam(BigDecimal refund_id, String sku_id, String sku_name, BigDecimal real_amount,
			BigDecimal sale_ratio, String purchase_sku_id) {
		this.refund_id = refund_id;
		this.sku_id = sku_id;
		this.sku_name = sku_name;
		this.solution = 157;
		this.driver_id = 0;
		this.in_stock_price = BigDecimal.ZERO;
		this.disabled_in_stock_price = false;
		this.real_amount = real_amount;
		this.sale_ratio = sale_ratio;
		this.purchase_sku_id = purchase_sku_id;
	}

	/**
	 * 二级入库构造方法
	 * 
	 * @param refund_id
	 * @param sku_id
	 * @param sku_name
	 * @param in_stock_price
	 * @param real_amount
	 * @param store_amount
	 * @param request_amount
	 * @param supplier_id
	 * @param supplier_name
	 * @param sale_ratio
	 * @param purchase_sku_id
	 */
	public RefundStockParam(BigDecimal refund_id, String sku_id, String sku_name, BigDecimal in_stock_price,
			BigDecimal real_amount, BigDecimal store_amount, BigDecimal request_amount, String supplier_id,
			String supplier_name, BigDecimal sale_ratio, String purchase_sku_id) {
		this.refund_id = refund_id;
		this.sku_id = sku_id;
		this.sku_name = sku_name;
		this.solution = 160;
		this.driver_id = 0;
		this.in_stock_price = in_stock_price;
		this.disabled_in_stock_price = false;
		this.real_amount = real_amount;
		this.sale_ratio = sale_ratio;
		this.store_amount = store_amount;
		this.request_amount = request_amount;
		this.supplier_id = supplier_id;
		this.supplier_name = supplier_name;
		this.sale_ratio = sale_ratio;
		this.purchase_sku_id = purchase_sku_id;

	}

}
