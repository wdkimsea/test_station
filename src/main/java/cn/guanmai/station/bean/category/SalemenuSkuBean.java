package cn.guanmai.station.bean.category;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

import cn.guanmai.util.BooleanTypeAdapter;

/* 
* @author liming 
* @date May 28, 2019 2:19:16 PM 
* @des 指定报价单里的销售规格列表信息
* @version 1.0 
*/
public class SalemenuSkuBean {
	@JSONField(name = "category_id_1")
	private String category1_id;

	@JSONField(name = "category_id_2")
	private String category2_id;

	private String pinlei_name;

	private String spu_name;
	private String spu_id;
	private String sku_name;
	private String sku_id;
	private String sku_image;
	private String outer_id;

	private BigDecimal sale_price;
	private BigDecimal std_sale_price;
	private BigDecimal sale_ratio;
	private String sale_unit_name;
	private String std_unit_name;
	@JSONField(serializeUsing = BooleanTypeAdapter.class)
	private boolean is_price_timing;

	private int state;
	private int formula_status;

	private FormulaInfo formula_info;

	/**
	 * @return the category1_id
	 */
	public String getCategory1_id() {
		return category1_id;
	}

	/**
	 * @param category1_id the category1_id to set
	 */
	public void setCategory1_id(String category1_id) {
		this.category1_id = category1_id;
	}

	/**
	 * @return the category2_id
	 */
	public String getCategory2_id() {
		return category2_id;
	}

	/**
	 * @param category2_id the category2_id to set
	 */
	public void setCategory2_id(String category2_id) {
		this.category2_id = category2_id;
	}

	/**
	 * @return the pinlei_name
	 */
	public String getPinlei_name() {
		return pinlei_name;
	}

	/**
	 * @param pinlei_name the pinlei_name to set
	 */
	public void setPinlei_name(String pinlei_name) {
		this.pinlei_name = pinlei_name;
	}

	/**
	 * @return the spu_name
	 */
	public String getSpu_name() {
		return spu_name;
	}

	/**
	 * @param spu_name the spu_name to set
	 */
	public void setSpu_name(String spu_name) {
		this.spu_name = spu_name;
	}

	/**
	 * @return the spu_id
	 */
	public String getSpu_id() {
		return spu_id;
	}

	/**
	 * @param spu_id the spu_id to set
	 */
	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	/**
	 * @return the sku_name
	 */
	public String getSku_name() {
		return sku_name;
	}

	/**
	 * @param sku_name the sku_name to set
	 */
	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	/**
	 * @return the sku_id
	 */
	public String getSku_id() {
		return sku_id;
	}

	/**
	 * @param sku_id the sku_id to set
	 */
	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public String getSku_image() {
		return sku_image;
	}

	public void setSku_image(String sku_image) {
		this.sku_image = sku_image;
	}

	public String getOuter_id() {
		return outer_id;
	}

	public void setOuter_id(String outer_id) {
		this.outer_id = outer_id;
	}

	/**
	 * @return the sale_price
	 */
	public BigDecimal getSale_price() {
		return sale_price.divide(new BigDecimal("100"));
	}

	/**
	 * @param sale_price the sale_price to set
	 */
	public void setSale_price(BigDecimal sale_price) {
		this.sale_price = sale_price;
	}

	/**
	 * @return the std_sale_price
	 */
	public BigDecimal getStd_sale_price() {
		return std_sale_price.divide(new BigDecimal("100"));
	}

	/**
	 * @param std_sale_price the std_sale_price to set
	 */
	public void setStd_sale_price(BigDecimal std_sale_price) {
		this.std_sale_price = std_sale_price;
	}

	/**
	 * @return the sale_ratio
	 */
	public BigDecimal getSale_ratio() {
		return sale_ratio;
	}

	/**
	 * @param sale_ratio the sale_ratio to set
	 */
	public void setSale_ratio(BigDecimal sale_ratio) {
		this.sale_ratio = sale_ratio;
	}

	/**
	 * @return the sale_unit_name
	 */
	public String getSale_unit_name() {
		return sale_unit_name;
	}

	/**
	 * @param sale_unit_name the sale_unit_name to set
	 */
	public void setSale_unit_name(String sale_unit_name) {
		this.sale_unit_name = sale_unit_name;
	}

	/**
	 * @return the std_unit_name
	 */
	public String getStd_unit_name() {
		return std_unit_name;
	}

	/**
	 * @param std_unit_name the std_unit_name to set
	 */
	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	/**
	 * @return the is_price_timing
	 */
	public boolean isIs_price_timing() {
		return is_price_timing;
	}

	/**
	 * @param is_price_timing the is_price_timing to set
	 */
	public void setIs_price_timing(boolean is_price_timing) {
		this.is_price_timing = is_price_timing;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}

	public int getFormula_status() {
		return formula_status;
	}

	public void setFormula_status(int formula_status) {
		this.formula_status = formula_status;
	}

	public class FormulaInfo {
		private BigDecimal cal_num;
		private int price_type;
		private int cal_type;

		public BigDecimal getCal_num() {
			return cal_num;
		}

		public void setCal_num(BigDecimal cal_num) {
			this.cal_num = cal_num;
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
	}

	public FormulaInfo getFormula_info() {
		return formula_info;
	}

	public void setFormula_info(FormulaInfo formula_info) {
		this.formula_info = formula_info;
	}

}
