package cn.guanmai.station.bean.purchase;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Apr 19, 2019 10:14:15 AM 
* @des 建议采购
* @version 1.0 
*/
public class SupplyLimitBean {
	private BigDecimal supplier_distribute_amount;
	private BigDecimal supply_limit;
	private BigDecimal supplier_purchased_amount;

	/**
	 * @return the supplier_distribute_amount
	 */
	public BigDecimal getSupplier_distribute_amount() {
		return supplier_distribute_amount;
	}

	/**
	 * @param supplier_distribute_amount the supplier_distribute_amount to set
	 */
	public void setSupplier_distribute_amount(BigDecimal supplier_distribute_amount) {
		this.supplier_distribute_amount = supplier_distribute_amount;
	}

	public BigDecimal getSupply_limit() {
		return supply_limit;
	}

	public void setSupply_limit(BigDecimal supply_limit) {
		this.supply_limit = supply_limit;
	}

	/**
	 * @return the supplier_purchased_amount
	 */
	public BigDecimal getSupplier_purchased_amount() {
		return supplier_purchased_amount;
	}

	/**
	 * @param supplier_purchased_amount the supplier_purchased_amount to set
	 */
	public void setSupplier_purchased_amount(BigDecimal supplier_purchased_amount) {
		this.supplier_purchased_amount = supplier_purchased_amount;
	}

}
