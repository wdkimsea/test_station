package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Feb 28, 2019 4:10:46 PM 
* @des 商品报溢记录
* @version 1.0 
*/
public class StockIncreaseRecordBean {
	private String category_id_1;
	private String category_id_2;
	private String category_name_1;
	private String category_name_2;
	private String spu_id;
	private String name;
	private String std_unit_name;
	private BigDecimal increase_amount;
	private BigDecimal old_stock;
	private BigDecimal price;
	private String remark;
	private String create_time;
	private String operator;

	// 先进先出模式下特有属性值
	private String batch_number;

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
	 * @return the category_name_1
	 */
	public String getCategory_name_1() {
		return category_name_1;
	}

	/**
	 * @param category_name_1 the category_name_1 to set
	 */
	public void setCategory_name_1(String category_name_1) {
		this.category_name_1 = category_name_1;
	}

	/**
	 * @return the category_name_2
	 */
	public String getCategory_name_2() {
		return category_name_2;
	}

	/**
	 * @param category_name_2 the category_name_2 to set
	 */
	public void setCategory_name_2(String category_name_2) {
		this.category_name_2 = category_name_2;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the increase_amount
	 */
	public BigDecimal getIncrease_amount() {
		return increase_amount.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @param increase_amount the increase_amount to set
	 */
	public void setIncrease_amount(BigDecimal increase_amount) {
		this.increase_amount = increase_amount;
	}

	/**
	 * @return the old_stock
	 */
	public BigDecimal getOld_stock() {
		return old_stock;
	}

	/**
	 * @param old_stock the old_stock to set
	 */
	public void setOld_stock(BigDecimal old_stock) {
		this.old_stock = old_stock;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the create_time
	 */
	public String getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time the create_time to set
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * 先进先出模式下特有属性值
	 * 
	 * @return the batch_number
	 */
	public String getBatch_number() {
		return batch_number;
	}

	/**
	 * 先进先出模式下特有属性值
	 * 
	 * @param batch_number the batch_number to set
	 */
	public void setBatch_number(String batch_number) {
		this.batch_number = batch_number;
	}
}
