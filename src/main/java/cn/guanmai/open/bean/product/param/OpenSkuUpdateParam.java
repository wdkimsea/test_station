package cn.guanmai.open.bean.product.param;

/*
* @author liming 
* @date Jun 10, 2019 5:11:54 PM 
* @des 开放平台修改SKU参数
* @version 1.0 
*/
public class OpenSkuUpdateParam {
	private String sku_id;
	private String sku_outer_id;
	private String new_sku_outer_id;
	private String sku_name;
	private String sale_price;
	private String sale_num_least;
	private String sale_ratio;
	private String sale_unit_name;
	private String desc;
	private String weighing;
	private String state;
	private String sale_stock_type;
	private String sale_stock_count;

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
	 * @return the sale_price
	 */
	public String getSale_price() {
		return sale_price;
	}

	/**
	 * @param sale_price the sale_price to set
	 */
	public void setSale_price(String sale_price) {
		this.sale_price = sale_price;
	}

	/**
	 * @return the sale_num_least
	 */
	public String getSale_num_least() {
		return sale_num_least;
	}

	/**
	 * @param sale_num_least the sale_num_least to set
	 */
	public void setSale_num_least(String sale_num_least) {
		this.sale_num_least = sale_num_least;
	}

	/**
	 * @return the sale_ratio
	 */
	public String getSale_ratio() {
		return sale_ratio;
	}

	/**
	 * @param sale_ratio the sale_ratio to set
	 */
	public void setSale_ratio(String sale_ratio) {
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
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the weighing
	 */
	public String getWeighing() {
		return weighing;
	}

	/**
	 * @param weighing the weighing to set
	 */
	public void setWeighing(String weighing) {
		this.weighing = weighing;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	public String getSku_outer_id() {
		return sku_outer_id;
	}

	public void setSku_outer_id(String sku_outer_id) {
		this.sku_outer_id = sku_outer_id;
	}

	public String getNew_sku_outer_id() {
		return new_sku_outer_id;
	}

	public void setNew_sku_outer_id(String new_sku_outer_id) {
		this.new_sku_outer_id = new_sku_outer_id;
	}

	public String getSale_stock_type() {
		return sale_stock_type;
	}

	public void setSale_stock_type(String sale_stock_type) {
		this.sale_stock_type = sale_stock_type;
	}

	public String getSale_stock_count() {
		return sale_stock_count;
	}

	public void setSale_stock_count(String sale_stock_count) {
		this.sale_stock_count = sale_stock_count;
	}

}
