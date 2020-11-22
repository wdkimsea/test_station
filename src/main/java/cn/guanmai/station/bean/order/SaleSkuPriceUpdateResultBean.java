package cn.guanmai.station.bean.order;

/* 
* @author liming 
* @date Jan 3, 2019 4:19:01 PM 
* @des 同步最新采购价处理结果类
* @version 1.0 
*/
public class SaleSkuPriceUpdateResultBean {
	private String address_id;
	private String address_name;
	private String order_id;
	private String reason;
	private String salemenu_id;
	private String salemenu_name;
	private String sku_id;
	private String sku_name;
	private String std_sale_price;
	private String std_unit_name;

	/**
	 * @return the address_id
	 */
	public String getAddress_id() {
		return address_id;
	}

	/**
	 * @param address_id
	 *            the address_id to set
	 */
	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	/**
	 * @return the address_name
	 */
	public String getAddress_name() {
		return address_name;
	}

	/**
	 * @param address_name
	 *            the address_name to set
	 */
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
	 * @param order_id
	 *            the order_id to set
	 */
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason
	 *            the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return the salemenu_id
	 */
	public String getSalemenu_id() {
		return salemenu_id;
	}

	/**
	 * @param salemenu_id
	 *            the salemenu_id to set
	 */
	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
	}

	/**
	 * @return the salemenu_name
	 */
	public String getSalemenu_name() {
		return salemenu_name;
	}

	/**
	 * @param salemenu_name
	 *            the salemenu_name to set
	 */
	public void setSalemenu_name(String salemenu_name) {
		this.salemenu_name = salemenu_name;
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
	 * @return the std_sale_price
	 */
	public String getStd_sale_price() {
		return std_sale_price;
	}

	/**
	 * @param std_sale_price
	 *            the std_sale_price to set
	 */
	public void setStd_sale_price(String std_sale_price) {
		this.std_sale_price = std_sale_price;
	}

	/**
	 * @return the std_unit_name
	 */
	public String getStd_unit_name() {
		return std_unit_name;
	}

	/**
	 * @param std_unit_name
	 *            the std_unit_name to set
	 */
	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	public SaleSkuPriceUpdateResultBean(String address_id, String address_name, String order_id, String reason,
			String salemenu_id, String salemenu_name, String sku_id, String sku_name, String std_sale_price,
			String std_unit_name) {
		super();
		this.address_id = address_id;
		this.address_name = address_name;
		this.order_id = order_id;
		this.reason = reason;
		this.salemenu_id = salemenu_id;
		this.salemenu_name = salemenu_name;
		this.sku_id = sku_id;
		this.sku_name = sku_name;
		this.std_sale_price = std_sale_price;
		this.std_unit_name = std_unit_name;
	}
}
