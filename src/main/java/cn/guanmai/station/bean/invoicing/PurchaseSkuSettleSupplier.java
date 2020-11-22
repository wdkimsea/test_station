package cn.guanmai.station.bean.invoicing;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Jan 7, 2019 4:32:12 PM 
* @des 采购规格对应的供应商类
* @version 1.0 
*/
public class PurchaseSkuSettleSupplier {
	@JSONField(name="settle_supplier_id")
	private String supplier_id;
	@JSONField(name="name")
	private String supplier_name;

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
	 * @return the supplier_name
	 */
	public String getSupplier_name() {
		return supplier_name;
	}

	/**
	 * @param supplier_name
	 *            the supplier_name to set
	 */
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

}
