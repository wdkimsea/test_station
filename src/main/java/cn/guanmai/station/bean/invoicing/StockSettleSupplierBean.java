package cn.guanmai.station.bean.invoicing;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author: liming
 * @Date: 2020年2月6日 下午2:59:32
 * @description: 进销存供应商
 * @version: 1.0
 */

public class StockSettleSupplierBean {
	private String customer_id;
	@JSONField(name="_id")
	private String id;
	private String name;
	private int state;
	private String settle_supplier_id;

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
	}

}
