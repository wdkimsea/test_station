package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Feb 28, 2019 3:52:43 PM 
* @des 成品出库日志
* @version 1.0 
*/
public class OutStockRecordBean {
	private String address_id;
	private String address_name;
	private String order_id;
	private String outer_id;
	private String category_id_1;
	private String category_id_2;
	private String category_name_1;
	private String category_name_2;
	private String spu_id;
	private String sku_id;
	private String std_unit_name;
	private String sale_unit_name;
	private String name;

	private BigDecimal out_stock_base;
	private BigDecimal out_stock_sale;
	private BigDecimal price;

	private String operator;
	private boolean clean_food;
	private String create_time;
	private String type;

	public String getAddress_id() {
		return address_id == null ? "0" : address_id;
	}

	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	public String getAddress_name() {
		return address_name;
	}

	public void setAddress_name(String address_name) {
		this.address_name = address_name;
	}

	/**
	 * @return the order_id
	 */
	public String getOrder_id() {
		return order_id;
	}

	/**
	 * @param order_id the order_id to set
	 */
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	/**
	 * @return the outer_id
	 */
	public String getOuter_id() {
		return outer_id;
	}

	/**
	 * @param outer_id the outer_id to set
	 */
	public void setOuter_id(String outer_id) {
		this.outer_id = outer_id;
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
	 * @return the out_stock_base
	 */
	public BigDecimal getOut_stock_base() {
		return out_stock_base;
	}

	/**
	 * @param out_stock_base the out_stock_base to set
	 */
	public void setOut_stock_base(BigDecimal out_stock_base) {
		this.out_stock_base = out_stock_base;
	}

	/**
	 * @return the out_stock_sale
	 */
	public BigDecimal getOut_stock_sale() {
		return out_stock_sale;
	}

	/**
	 * @param out_stock_sale the out_stock_sale to set
	 */
	public void setOut_stock_sale(BigDecimal out_stock_sale) {
		this.out_stock_sale = out_stock_sale;
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
	 * @return the clean_food
	 */
	public boolean isClean_food() {
		return clean_food;
	}

	/**
	 * @param clean_food the clean_food to set
	 */
	public void setClean_food(boolean clean_food) {
		this.clean_food = clean_food;
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}
