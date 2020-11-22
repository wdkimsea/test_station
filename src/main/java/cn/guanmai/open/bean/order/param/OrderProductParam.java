package cn.guanmai.open.bean.order.param;

/* 
* @author liming 
* @date Jun 11, 2019 2:59:34 PM 
* @des 下单、修改订单商品参数
* @version 1.0 
*/
public class OrderProductParam {
	private String sku_id;
	private String count;
	private String price;
	private String remark;
	private String real_count;
	private String is_price_timing;

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
	 * @return the count
	 */
	public String getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(String count) {
		this.count = count;
	}

	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
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

	public String getReal_count() {
		return real_count;
	}

	public void setReal_count(String real_count) {
		this.real_count = real_count;
	}

	/**
	 * @return the is_price_timing
	 */
	public String getIs_price_timing() {
		return is_price_timing;
	}

	/**
	 * @param is_price_timing the is_price_timing to set
	 */
	public void setIs_price_timing(String is_price_timing) {
		this.is_price_timing = is_price_timing;
	}
}
