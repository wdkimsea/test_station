package cn.guanmai.open.bean.order.param;

/* 
* @author liming 
* @date Jun 5, 2019 11:47:25 AM 
* @des 接口 /order/after_sales/delete 对应参数
* @version 1.0 
*/
public class OrderRefundDeleteParam {
	private String id;
	private String sku_id;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
}
