package cn.guanmai.station.bean.purchase;

import java.math.BigDecimal;
import java.util.List;

import cn.guanmai.station.bean.purchase.PurchaserBean.SettleSupplier;

/* 
* @author liming 
* @date Nov 28, 2018 5:09:29 PM 
* @des 新建采购条目时搜索的结果类
* @version 1.0 
*/
public class PurchaseSpecSuppliersBean {
	private String sale_unit_name;
	private BigDecimal sale_ratio;
	private String sku_id;
	private String spec_id;
	private String std_unit_name;

	private List<SettleSupplier> settle_suppliers;

	/**
	 * @return the sale_unit_name
	 */
	public String getSale_unit_name() {
		return sale_unit_name;
	}

	/**
	 * @param sale_unit_name the sale_unit_name to set
	 */
	public void setSale_unit_name(String sale_unit_name) {
		this.sale_unit_name = sale_unit_name;
	}

	public BigDecimal getSale_ratio() {
		return sale_ratio;
	}

	public void setSale_ratio(BigDecimal sale_ratio) {
		this.sale_ratio = sale_ratio;
	}

	public String getSpec_id() {
		return spec_id;
	}

	public void setSpec_id(String spec_id) {
		this.spec_id = spec_id;
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
	 * @return the std_unit_name
	 */
	public String getStd_unit_name() {
		return std_unit_name;
	}

	/**
	 * @param std_unit_name the std_unit_name to set
	 */
	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	/**
	 * @return the settle_suppliers
	 */
	public List<SettleSupplier> getSettle_suppliers() {
		return settle_suppliers;
	}

	/**
	 * @param settle_suppliers the settle_suppliers to set
	 */
	public void setSettle_suppliers(List<SettleSupplier> settle_suppliers) {
		this.settle_suppliers = settle_suppliers;
	}

}
