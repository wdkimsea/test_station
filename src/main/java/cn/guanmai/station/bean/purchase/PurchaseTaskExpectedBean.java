package cn.guanmai.station.bean.purchase;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2019年12月23日
 * @time 下午7:23:29
 * @des 采购任务期望结果
 */

public class PurchaseTaskExpectedBean {
	private String order_id; // 订单ID
	private List<String> sku_ids; // 销售规格ID
	private String spu_name; // SPU名称
	private String spec_id; // 采购规格ID
	private String supplier_id;// 供应商ID
	private BigDecimal plan_purchase_amount; // 预期采购数量

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public List<String> getSku_ids() {
		return sku_ids;
	}

	public void setSku_ids(List<String> sku_ids) {
		this.sku_ids = sku_ids;
	}

	public String getSpu_name() {
		return spu_name;
	}

	public void setSpu_name(String spu_name) {
		this.spu_name = spu_name;
	}

	public String getSpec_id() {
		return spec_id;
	}

	public void setSpec_id(String spec_id) {
		this.spec_id = spec_id;
	}

	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	public BigDecimal getPlan_purchase_amount() {
		return plan_purchase_amount;
	}

	public void setPlan_purchase_amount(BigDecimal plan_purchase_amount) {
		this.plan_purchase_amount = plan_purchase_amount;
	}

}
