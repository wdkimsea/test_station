package cn.guanmai.station.bean.jingcai;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年4月28日 下午7:12:38
 * @description:
 * @version: 1.0
 */

public class IngredientBean {
	private String id;
	private String name;
	private int remark_type;
	private String std_unit_name;
	private String sale_unit_name;
	private String category_id_1;
	private String category_id_2;
	private BigDecimal ratio;

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

	public int getRemark_type() {
		return remark_type;
	}

	public void setRemark_type(int remark_type) {
		this.remark_type = remark_type;
	}

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	public String getSale_unit_name() {
		return sale_unit_name;
	}

	public void setSale_unit_name(String sale_unit_name) {
		this.sale_unit_name = sale_unit_name;
	}

	public String getCategory_id_1() {
		return category_id_1;
	}

	public void setCategory_id_1(String category_id_1) {
		this.category_id_1 = category_id_1;
	}

	public String getCategory_id_2() {
		return category_id_2;
	}

	public void setCategory_id_2(String category_id_2) {
		this.category_id_2 = category_id_2;
	}

	public BigDecimal getRatio() {
		return ratio;
	}

	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}
}
