package cn.guanmai.station.bean.category.param;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年7月3日 下午3:42:00
 * @description: 采购规格批量导入修改参数
 * @version: 1.0
 */

public class PurchaseSpecBatchEditParam {
	private String pur_spec_id;
	private String name;
	private BigDecimal ratio;
	private String purchase_unit;
	private String purchase_desc;
	private BigDecimal max_stock_unit_price;

	public String getPur_spec_id() {
		return pur_spec_id;
	}

	public void setPur_spec_id(String pur_spec_id) {
		this.pur_spec_id = pur_spec_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getRatio() {
		return ratio;
	}

	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}

	public String getPurchase_unit() {
		return purchase_unit;
	}

	public void setPurchase_unit(String purchase_unit) {
		this.purchase_unit = purchase_unit;
	}

	public String getPurchase_desc() {
		return purchase_desc;
	}

	public void setPurchase_desc(String purchase_desc) {
		this.purchase_desc = purchase_desc;
	}

	public BigDecimal getMax_stock_unit_price() {
		return max_stock_unit_price;
	}

	public void setMax_stock_unit_price(BigDecimal max_stock_unit_price) {
		this.max_stock_unit_price = max_stock_unit_price;
	}

}
