package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Feb 28, 2019 4:03:47 PM 
* @des 成品退货记录
* @version 1.0 
*/
public class ReturnStockRecordBean {
	private String sheet_no;

	private String category_id_1;
	private String category_id_2;
	private String category_name_1;
	private String category_name_2;
	private String name;
	private String std_unit_name;
	private String sku_id;
	private String spu_id;

	private BigDecimal all_price;
	private BigDecimal return_amount;
	private BigDecimal price;

	private String submit_time;
	private String operator;
	private String remark;

	/**
	 * @return the sheet_no
	 */
	public String getSheet_no() {
		return sheet_no;
	}

	/**
	 * @param sheet_no the sheet_no to set
	 */
	public void setSheet_no(String sheet_no) {
		this.sheet_no = sheet_no;
	}

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
	 * @return the all_price
	 */
	public BigDecimal getAll_price() {
		return all_price.divide(new BigDecimal("100"));
	}

	/**
	 * @param all_price the all_price to set
	 */
	public void setAll_price(BigDecimal all_price) {
		this.all_price = all_price;
	}

	/**
	 * @return the return_amount
	 */
	public BigDecimal getReturn_amount() {
		return return_amount;
	}

	/**
	 * @param return_amount the return_amount to set
	 */
	public void setReturn_amount(BigDecimal return_amount) {
		this.return_amount = return_amount;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price.divide(new BigDecimal("100"));
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the submit_time
	 */
	public String getSubmit_time() {
		return submit_time;
	}

	/**
	 * @param submit_time the submit_time to set
	 */
	public void setSubmit_time(String submit_time) {
		this.submit_time = submit_time;
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

}
