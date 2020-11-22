package cn.guanmai.station.bean.order.param;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Nov 12, 2018 5:46:07 PM 
* @des 下单商品类
* @version 1.0 
*/
public class OrderSkuParam {
	private String sku_id;
	private BigDecimal amount;
	private BigDecimal unit_price;
	private String spu_remark;
	private String spu_id;
	private int is_price_timing;
	private BigDecimal fake_quantity;
	private boolean is_combine_goods;
	private String combine_goods_id;
	private String salemenu_id;

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
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
	 * @return the spu_remark
	 */
	public String getSpu_remark() {
		return spu_remark;
	}

	/**
	 * @param spu_remark the spu_remark to set
	 */
	public void setSpu_remark(String spu_remark) {
		this.spu_remark = spu_remark;
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
	public int getIs_price_timing() {
		return is_price_timing;
	}

	/**
	 * @param is_price_timing the is_price_timing to set
	 */
	public void setIs_price_timing(int is_price_timing) {
		this.is_price_timing = is_price_timing;
	}

	public BigDecimal getFake_quantity() {
		return fake_quantity;
	}

	public void setFake_quantity(BigDecimal fake_quantity) {
		this.fake_quantity = fake_quantity;
	}

	public boolean isIs_combine_goods() {
		return is_combine_goods;
	}

	public void setIs_combine_goods(boolean is_combine_goods) {
		this.is_combine_goods = is_combine_goods;
	}

	public String getCombine_goods_id() {
		return combine_goods_id;
	}

	public void setCombine_goods_id(String combine_goods_id) {
		this.combine_goods_id = combine_goods_id;
	}

	public String getSalemenu_id() {
		return salemenu_id;
	}

	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
	}

	public OrderSkuParam() {
		super();
	}

	public OrderSkuParam(String sku_id, String spu_id, BigDecimal unit_price, BigDecimal amount, String spu_remark,
			int is_price_timing) {
		super();
		this.sku_id = sku_id;
		this.amount = amount;
		this.unit_price = unit_price;
		this.spu_remark = spu_remark;
		this.spu_id = spu_id;
		this.is_price_timing = is_price_timing;
	}

}
