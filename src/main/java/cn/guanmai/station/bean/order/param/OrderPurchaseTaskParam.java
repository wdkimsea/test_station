package cn.guanmai.station.bean.order.param;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年5月20日 下午2:23:36
 * @description: /purchase/task/create_by_order 接口对应的参数
 * @version: 1.0
 */

public class OrderPurchaseTaskParam {
	private String order_id;
	private String sku_id;
	private BigDecimal detail_id;
	private BigDecimal plan_purchase_amount;

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public BigDecimal getPlan_purchase_amount() {
		return plan_purchase_amount;
	}

	public void setPlan_purchase_amount(BigDecimal plan_purchase_amount) {
		this.plan_purchase_amount = plan_purchase_amount;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public BigDecimal getDetail_id() {
		return detail_id;
	}

	public void setDetail_id(BigDecimal detail_id) {
		this.detail_id = detail_id;
	}

}
