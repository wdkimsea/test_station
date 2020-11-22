package cn.guanmai.station.bean.purchase.assistant;

import java.math.BigDecimal;

/* 
* @author liming 
* @date May 27, 2019 2:37:52 PM 
* @des 接口 /purchase_assistant/settle_supplier/supply_sku 对应的结果
*      采购助手采购单新建采购条目时拉取到的采购规格列表
* @version 1.0 
*/
public class SupplySkuBean {
	private String spec_id;
	private String spec_name;
	private String std_unit_name;
	private String sale_unit_name;
	private BigDecimal sale_ratio;
	private String settle_supplier_id;

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
	 * @return the spec_name
	 */
	public String getSpec_name() {
		return spec_name;
	}

	/**
	 * @param spec_name
	 *            the spec_name to set
	 */
	public void setSpec_name(String spec_name) {
		this.spec_name = spec_name;
	}

	/**
	 * @return the std_unit_name
	 */
	public String getStd_unit_name() {
		return std_unit_name;
	}

	/**
	 * @param std_unit_name
	 *            the std_unit_name to set
	 */
	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	/**
	 * @return the sale_unit_name
	 */
	public String getSale_unit_name() {
		return sale_unit_name;
	}

	/**
	 * @param sale_unit_name
	 *            the sale_unit_name to set
	 */
	public void setSale_unit_name(String sale_unit_name) {
		this.sale_unit_name = sale_unit_name;
	}

	/**
	 * @return the sale_ratio
	 */
	public BigDecimal getSale_ratio() {
		return sale_ratio;
	}

	/**
	 * @param sale_ratio
	 *            the sale_ratio to set
	 */
	public void setSale_ratio(BigDecimal sale_ratio) {
		this.sale_ratio = sale_ratio;
	}

	/**
	 * @return the settle_supplier_id
	 */
	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	/**
	 * @param settle_supplier_id
	 *            the settle_supplier_id to set
	 */
	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
	}
}
