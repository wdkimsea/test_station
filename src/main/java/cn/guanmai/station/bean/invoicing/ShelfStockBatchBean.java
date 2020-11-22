package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年11月27日
 * @time 上午10:09:49
 * @des 接口 /stock/check/batch_number/list 对应的参数
 */

public class ShelfStockBatchBean {
	private BigDecimal batch_avg_price;
	private String batch_number;
	private String create_time;
	private String life_time;
	private String production_time;
	private BigDecimal remain;
	private String settle_supplier_id;
	private String shelf_id;
	private String shelf_name;
	private String std_unit_name;
	private String supplier_name;

	public BigDecimal getBatch_avg_price() {
		return batch_avg_price;
	}

	public void setBatch_avg_price(BigDecimal batch_avg_price) {
		this.batch_avg_price = batch_avg_price;
	}

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

	public BigDecimal getRemain() {
		return remain;
	}

	public void setRemain(BigDecimal remain) {
		this.remain = remain;
	}

	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
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

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

}
