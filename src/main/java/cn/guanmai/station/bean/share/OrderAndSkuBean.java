package cn.guanmai.station.bean.share;

/* 
* @author liming 
* @date Jan 2, 2019 11:45:37 AM 
* @des 订单商品批量设置缺货参数类
* @version 1.0 
*/
public class OrderAndSkuBean {
	private String order_id;
	private String sku_id;

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

	public OrderAndSkuBean(String order_id, String sku_id) {
		super();
		this.order_id = order_id;
		this.sku_id = sku_id;
	}

}
