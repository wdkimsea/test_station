package cn.guanmai.station.bean.purchase.assistant;

import java.math.BigDecimal;

/* 
* @author liming 
* @date May 27, 2019 3:26:43 PM 
* @des 接口 /purchase_assistant/purchase_sheets 对应的结果
*      采购助手采购单据页面-采购单据列表数据
* @version 1.0 
*/
public class PurchaseSheetBean {
	private String id;
	private String create_time;
	private String settle_supplier_name;
	private BigDecimal spec_money;
	private BigDecimal spec_num;
	private int status;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the create_time
	 */
	public String getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time
	 *            the create_time to set
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the settle_supplier_name
	 */
	public String getSettle_supplier_name() {
		return settle_supplier_name;
	}

	/**
	 * @param settle_supplier_name
	 *            the settle_supplier_name to set
	 */
	public void setSettle_supplier_name(String settle_supplier_name) {
		this.settle_supplier_name = settle_supplier_name;
	}

	/**
	 * @return the spec_money
	 */
	public BigDecimal getSpec_money() {
		return spec_money;
	}

	/**
	 * @param spec_money
	 *            the spec_money to set
	 */
	public void setSpec_money(BigDecimal spec_money) {
		this.spec_money = spec_money;
	}

	/**
	 * @return the spec_num
	 */
	public BigDecimal getSpec_num() {
		return spec_num;
	}

	/**
	 * @param spec_num
	 *            the spec_num to set
	 */
	public void setSpec_num(BigDecimal spec_num) {
		this.spec_num = spec_num;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

}
