package cn.guanmai.station.bean.purchase;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年8月14日
 * @time 上午11:00:30
 * @des TODO
 */

public class PurchaseTaskHistoryBean {
	private BigDecimal purchase_amount;
	private String purchase_sheet_id;
	private String purchase_time;
	private BigDecimal purchase_unit_price;
	private String purchaser_name;
	private BigDecimal sale_ratio;
	private String sale_unit_name;
	private String settle_supplier_id;
	private String settle_supplier_name;
	private String sku_id;
	private String sku_name;
	private int status;
	private String std_unit_name;

	public BigDecimal getPurchase_amount() {
		return purchase_amount;
	}

	public void setPurchase_amount(BigDecimal purchase_amount) {
		this.purchase_amount = purchase_amount;
	}

	public String getPurchase_sheet_id() {
		return purchase_sheet_id;
	}

	public void setPurchase_sheet_id(String purchase_sheet_id) {
		this.purchase_sheet_id = purchase_sheet_id;
	}

	public String getPurchase_time() {
		return purchase_time;
	}

	public void setPurchase_time(String purchase_time) {
		this.purchase_time = purchase_time;
	}

	public BigDecimal getPurchase_unit_price() {
		return purchase_unit_price;
	}

	public void setPurchase_unit_price(BigDecimal purchase_unit_price) {
		this.purchase_unit_price = purchase_unit_price;
	}

	public String getPurchaser_name() {
		return purchaser_name;
	}

	public void setPurchaser_name(String purchaser_name) {
		this.purchaser_name = purchaser_name;
	}

	public BigDecimal getSale_ratio() {
		return sale_ratio;
	}

	public void setSale_ratio(BigDecimal sale_ratio) {
		this.sale_ratio = sale_ratio;
	}

	public String getSale_unit_name() {
		return sale_unit_name;
	}

	public void setSale_unit_name(String sale_unit_name) {
		this.sale_unit_name = sale_unit_name;
	}

	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
	}

	public String getSettle_supplier_name() {
		return settle_supplier_name;
	}

	public void setSettle_supplier_name(String settle_supplier_name) {
		this.settle_supplier_name = settle_supplier_name;
	}

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public String getSku_name() {
		return sku_name;
	}

	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

}
