package cn.guanmai.station.bean.marketing.param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liming
 * @Date: 2020年3月16日 下午7:22:59
 * @description: 定价公式参数
 * @version: 1.0
 */

public class SmartFormulaPricingParam {
	private int formula_status;
	private int price_type;
	private int cal_type;
	private BigDecimal cal_num;
	private int all;
	private List<String> sku_list;

	public int getFormula_status() {
		return formula_status;
	}

	public void setFormula_status(int formula_status) {
		this.formula_status = formula_status;
	}

	public int getPrice_type() {
		return price_type;
	}

	public void setPrice_type(int price_type) {
		this.price_type = price_type;
	}

	public int getCal_type() {
		return cal_type;
	}

	public void setCal_type(int cal_type) {
		this.cal_type = cal_type;
	}

	public BigDecimal getCal_num() {
		return cal_num;
	}

	public void setCal_num(BigDecimal cal_num) {
		this.cal_num = cal_num;
	}

	public int getAll() {
		return all;
	}

	public void setAll(int all) {
		this.all = all;
	}

	public List<String> getSku_list() {
		return sku_list;
	}

	public void setSku_list(List<String> sku_list) {
		this.sku_list = sku_list;
	}

}