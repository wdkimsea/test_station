package cn.guanmai.station.bean.marketing;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

import cn.guanmai.util.BooleanTypeAdapter;

/* 
* @author liming 
* @date Feb 21, 2019 5:27:24 PM 
* @des 接口 /station/skus  对应的结果,营销活动查询商品返回的结果
* 同时也是参数类,用来编辑提交营销活动的商品
* @version 1.0 
*/
public class PromotionSkuBean {
	private String category_id_1;
	private String category_id_2;
	private String salemenu_id;
	private String spu_id;
	private String id;
	@JSONField(serializeUsing = BooleanTypeAdapter.class)
	private boolean is_price_timing;
	private BigDecimal sale_price;
	private BigDecimal unit_price;

	private BigDecimal price;
	private BigDecimal limit_number;
	private String label_1_name;

	/**
	 * @return the category_id_1
	 */
	public String getCategory_id_1() {
		return category_id_1;
	}

	/**
	 * @param category_id_1 the category_id_1 to set
	 */
	public void setCategory_id_1(String category_id_1) {
		this.category_id_1 = category_id_1;
	}

	/**
	 * @return the category_id_2
	 */
	public String getCategory_id_2() {
		return category_id_2;
	}

	/**
	 * @param category_id_2 the category_id_2 to set
	 */
	public void setCategory_id_2(String category_id_2) {
		this.category_id_2 = category_id_2;
	}

	/**
	 * @return the salemenu_id
	 */
	public String getSalemenu_id() {
		return salemenu_id;
	}

	/**
	 * @param salemenu_id the salemenu_id to set
	 */
	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the sale_price
	 */
	public BigDecimal getSale_price() {
		return sale_price;
	}

	/**
	 * @param sale_price the sale_price to set
	 */
	public void setSale_price(BigDecimal sale_price) {
		this.sale_price = sale_price;
	}

	/**
	 * @return the unit_price
	 */
	public BigDecimal getUnit_price() {
		return unit_price;
	}

	/**
	 * @param unit_price the unit_price to set
	 */
	public void setUnit_price(BigDecimal unit_price) {
		this.unit_price = unit_price;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the limit_number
	 */
	public BigDecimal getLimit_number() {
		return limit_number;
	}

	/**
	 * @param limit_number the limit_number to set
	 */
	public void setLimit_number(BigDecimal limit_number) {
		this.limit_number = limit_number;
	}

	/**
	 * @return the label_1_name
	 */
	public String getLabel_1_name() {
		return label_1_name;
	}

	/**
	 * @param label_1_name the label_1_name to set
	 */
	public void setLabel_1_name(String label_1_name) {
		this.label_1_name = label_1_name;
	}

}
