package cn.guanmai.station.bean.delivery.param;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jun 18, 2019 7:15:36 PM 
* @des 套账更新商品参数  /delivery/update 接口对应的参数
* @version 1.0 
*/
public class LegerUpdateSkuParam {
	private String id;
	private int type;
	private BigDecimal raw_id;
	private int op_type;
	private BigDecimal quantity;
	private Integer quantity_lock;
	private BigDecimal sale_price;
	private Integer sale_price_lock;
	private BigDecimal real_weight;
	private Integer real_weight_lock;
	private BigDecimal real_item_price;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 商品SKU ID
	 * 
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the raw_id
	 */
	public BigDecimal getRaw_id() {
		return raw_id;
	}

	/**
	 * @param raw_id the raw_id to set
	 */
	public void setRaw_id(BigDecimal raw_id) {
		this.raw_id = raw_id;
	}

	/**
	 * @return the op_type
	 */
	public int getOp_type() {
		return op_type;
	}

	/**
	 * @param op_type the op_type to set
	 */
	public void setOp_type(int op_type) {
		this.op_type = op_type;
	}

	/**
	 * @return the quantity
	 */
	public BigDecimal getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
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
	 * @param sale_price the sale_price to set
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
	 * @param real_weight the real_weight to set
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
	 * @param real_item_price the real_item_price to set
	 */
	public void setReal_item_price(BigDecimal real_item_price) {
		this.real_item_price = real_item_price;
	}

	public Integer getQuantity_lock() {
		return quantity_lock;
	}

	public void setQuantity_lock(Integer quantity_lock) {
		this.quantity_lock = quantity_lock;
	}

	public Integer getSale_price_lock() {
		return sale_price_lock;
	}

	public void setSale_price_lock(Integer sale_price_lock) {
		this.sale_price_lock = sale_price_lock;
	}

	public Integer getReal_weight_lock() {
		return real_weight_lock;
	}

	public void setReal_weight_lock(Integer real_weight_lock) {
		this.real_weight_lock = real_weight_lock;
	}

}
