package cn.guanmai.station.bean.purchase.param;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2020年1月7日
 * @time 下午3:35:32
 * @des 接口 /purchase/task/create_many 对应的参数
 */

public class PurchaseTaskCreateParam {
	private String spec_id;
	private String settle_supplier_id;
	private BigDecimal plan_purchase_amount;
	private BigDecimal purchaser_id;

	public String getSpec_id() {
		return spec_id;
	}

	public void setSpec_id(String spec_id) {
		this.spec_id = spec_id;
	}

	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
	}

	public BigDecimal getPlan_purchase_amount() {
		return plan_purchase_amount;
	}

	public void setPlan_purchase_amount(BigDecimal plan_purchase_amount) {
		this.plan_purchase_amount = plan_purchase_amount;
	}

	public BigDecimal getPurchaser_id() {
		return purchaser_id;
	}

	public void setPurchaser_id(BigDecimal purchaser_id) {
		this.purchaser_id = purchaser_id;
	}

}
