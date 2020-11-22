package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jan 7, 2019 5:22:11 PM 
* @todo TODO
* @version 1.0 
*/
public class RefundStockResultBean {
	private String spu_id;
	private String sku_id;
	private String purchase_sku_id;
	private BigDecimal refund_id;
	private String sku_name;
	private String order_id;
	private String address_id;
	private String resname;
	private BigDecimal request_amount;
	private BigDecimal request_refund_money; // 应退金额
	private BigDecimal sale_ratio;
	private BigDecimal std_ratio;
	private Integer state;
	private Integer solution_id;
	private BigDecimal real_amount; // 实退数
	private BigDecimal real_refund_money; // 实退金额
	private BigDecimal store_amount; // 入库数
	private BigDecimal in_stock_price; // 入库单价
	private String supplier_name;
	private String batch_number;

	public String getSpu_id() {
		return spu_id;
	}

	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
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
	 * @return the order_id
	 */
	public String getOrder_id() {
		return order_id;
	}

	/**
	 * @param order_id the order_id to set
	 */
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	/**
	 * @return the address_id
	 */
	public String getAddress_id() {
		return address_id;
	}

	/**
	 * @param address_id the address_id to set
	 */
	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	/**
	 * @return the resname
	 */
	public String getResname() {
		return resname;
	}

	/**
	 * @param resname the resname to set
	 */
	public void setResname(String resname) {
		this.resname = resname;
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

	public BigDecimal getRequest_refund_money() {
		return request_refund_money;
	}

	public void setRequest_refund_money(BigDecimal request_refund_money) {
		this.request_refund_money = request_refund_money;
	}

	public BigDecimal getReal_refund_money() {
		return real_refund_money;
	}

	public void setReal_refund_money(BigDecimal real_refund_money) {
		this.real_refund_money = real_refund_money;
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

	public BigDecimal getStd_ratio() {
		return std_ratio;
	}

	public void setStd_ratio(BigDecimal std_ratio) {
		this.std_ratio = std_ratio;
	}

	/**
	 * @return the state
	 */
	public Integer getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(Integer state) {
		this.state = state;
	}

	/**
	 * @return the solution_id
	 */
	public Integer getSolution_id() {
		return solution_id;
	}

	/**
	 * @param solution_id the solution_id to set
	 */
	public void setSolution_id(Integer solution_id) {
		this.solution_id = solution_id;
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
	 * @return the in_stock_price
	 */
	public BigDecimal getIn_stock_price() {
		return in_stock_price;
	}

	/**
	 * @param in_stock_price the in_stock_price to set
	 */
	public void setIn_stock_price(BigDecimal in_stock_price) {
		this.in_stock_price = in_stock_price;
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

}
