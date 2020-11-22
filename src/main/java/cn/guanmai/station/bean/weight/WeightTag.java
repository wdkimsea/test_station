package cn.guanmai.station.bean.weight;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Apr 16, 2019 10:25:07 AM 
* @des 称重标签 ,老版本称重软件 {/station/weigh/get_task} 接口返回的数据
* @version 1.0 
*/
public class WeightTag {
	private String spu_id;
	@JSONField(name="product_id")
	private String sku_id;
	@JSONField(name="name")
	private String sku_name;
	private BigDecimal amount;
	private BigDecimal sale_ratio;
	private String detail_id;
	private String order_id;
	private boolean is_weigh;

	/**
	 * @return the spu_id
	 */
	public String getSpu_id() {
		return spu_id;
	}

	/**
	 * @param spu_id
	 *            the spu_id to set
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
	 * @param sku_id
	 *            the sku_id to set
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
	 * @param sku_name
	 *            the sku_name to set
	 */
	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
	 * @return the detail_id
	 */
	public String getDetail_id() {
		return detail_id;
	}

	/**
	 * @param detail_id
	 *            the detail_id to set
	 */
	public void setDetail_id(String detail_id) {
		this.detail_id = detail_id;
	}

	/**
	 * @return the order_id
	 */
	public String getOrder_id() {
		return order_id;
	}

	/**
	 * @param order_id
	 *            the order_id to set
	 */
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	/**
	 * @return the is_weigh
	 */
	public boolean isIs_weigh() {
		return is_weigh;
	}

	/**
	 * @param is_weigh
	 *            the is_weigh to set
	 */
	public void setIs_weigh(boolean is_weigh) {
		this.is_weigh = is_weigh;
	}

}
