package cn.guanmai.station.bean.delivery.param;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jun 19, 2019 10:02:57 AM 
* @des 套账单添加商品参数  /delivery/update
* @version 1.0 
*/
public class LegerAddSkuParam {
	private String id;
	private String name;
	private String category_title_1;
	private String sale_unit_name;
	private String std_unit_name;
	private BigDecimal sale_ratio;
	private BigDecimal quantity;
	private BigDecimal sale_price;
	private BigDecimal real_weight;
	private BigDecimal real_item_price;
	private int type;
	private int op_type;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the category_title_1
	 */
	public String getCategory_title_1() {
		return category_title_1;
	}

	/**
	 * @param category_title_1
	 *            the category_title_1 to set
	 */
	public void setCategory_title_1(String category_title_1) {
		this.category_title_1 = category_title_1;
	}

	/**
	 * @return the sale_unit_name
	 */
	public String getSale_unit_name() {
		return sale_unit_name;
	}

	/**
	 * @param sale_unit_name
	 *            the sale_unit_name to set
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
	 * @param std_unit_name
	 *            the std_unit_name to set
	 */
	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	/**
	 * @return the sale_ratio
	 */
	public BigDecimal getSale_ratio() {
		return sale_ratio;
	}

	/**
	 * @param sale_ratio
	 *            the sale_ratio to set
	 */
	public void setSale_ratio(BigDecimal sale_ratio) {
		this.sale_ratio = sale_ratio;
	}

	/**
	 * @return the quantity
	 */
	public BigDecimal getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the sale_price
	 */
	public BigDecimal getSale_price() {
		return sale_price;
	}

	/**
	 * @param sale_price
	 *            the sale_price to set
	 */
	public void setSale_price(BigDecimal sale_price) {
		this.sale_price = sale_price;
	}

	/**
	 * @return the real_weight
	 */
	public BigDecimal getReal_weight() {
		return real_weight;
	}

	/**
	 * @param real_weight
	 *            the real_weight to set
	 */
	public void setReal_weight(BigDecimal real_weight) {
		this.real_weight = real_weight;
	}

	/**
	 * @return the real_item_price
	 */
	public BigDecimal getReal_item_price() {
		return real_item_price;
	}

	/**
	 * @param real_item_price
	 *            the real_item_price to set
	 */
	public void setReal_item_price(BigDecimal real_item_price) {
		this.real_item_price = real_item_price;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the op_type
	 */
	public int getOp_type() {
		return op_type;
	}

	/**
	 * @param op_type
	 *            the op_type to set
	 */
	public void setOp_type(int op_type) {
		this.op_type = op_type;
	}

}
