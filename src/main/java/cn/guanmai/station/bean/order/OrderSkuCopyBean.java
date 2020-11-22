package cn.guanmai.station.bean.order;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date May 29, 2019 3:46:41 PM 
* @des 接口 /station/order/copy 对应的结果
*      复制订单
* @version 1.0 
*/
public class OrderSkuCopyBean {
	@JSONField(name = "id")
	private String sku_id;
	@JSONField(name = "name")
	private String sku_name;
	private boolean is_price_timing;
	private BigDecimal sale_price;
	private BigDecimal quantity;
	private String std_unit_name;
	private String sale_unit_name;
	private String spu_remark;

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

}
