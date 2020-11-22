package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年10月11日
 * @time 下午7:43:40
 * @des 接口 /station/stock/check/batch_number/list 对应的结果
 */

public class TransferStockBatchBean {
	private String batch_number;
	private String create_time;
	private String life_time;
	private String production_time;
	private String settle_supplier_id;
	private BigDecimal remain;
	private String shelf_id;
	private String shelf_name;
	private String supplier_name;

	public String getBatch_number() {
		return batch_number;
	}

	public void setBatch_number(String batch_number) {
		this.batch_number = batch_number;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getLife_time() {
		return life_time;
	}

	public void setLife_time(String life_time) {
		this.life_time = life_time;
	}

	public String getProduction_time() {
		return production_time;
	}

	public void setProduction_time(String production_time) {
		this.production_time = production_time;
	}

	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
	}

	public BigDecimal getRemain() {
		return remain.setScale(2, BigDecimal.ROUND_HALF_DOWN);
	}

	public void setRemain(BigDecimal remain) {
		this.remain = remain;
	}

	public String getShelf_id() {
		return shelf_id;
	}

	public void setShelf_id(String shelf_id) {
		this.shelf_id = shelf_id;
	}

	public String getShelf_name() {
		return shelf_name;
	}

	public void setShelf_name(String shelf_name) {
		this.shelf_name = shelf_name;
	}

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

}
