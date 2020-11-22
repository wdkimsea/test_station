package cn.guanmai.station.bean.delivery;

/* 
* @author liming 
* @date May 21, 2019 2:54:56 PM 
* @des 装车验货信息
* @version 1.0 
*/
public class DeliveryConfirmInfoBean {
	private Integer inspected_sku_count;
	private Integer sku_count;
	private Integer uninspected_sku_count;

	/**
	 * @return the inspected_sku_count
	 */
	public Integer getInspected_sku_count() {
		return inspected_sku_count;
	}

	/**
	 * @param inspected_sku_count
	 *            the inspected_sku_count to set
	 */
	public void setInspected_sku_count(Integer inspected_sku_count) {
		this.inspected_sku_count = inspected_sku_count;
	}

	/**
	 * @return the sku_count
	 */
	public Integer getSku_count() {
		return sku_count;
	}

	/**
	 * @param sku_count
	 *            the sku_count to set
	 */
	public void setSku_count(Integer sku_count) {
		this.sku_count = sku_count;
	}

	/**
	 * @return the uninspected_sku_count
	 */
	public Integer getUninspected_sku_count() {
		return uninspected_sku_count;
	}

	/**
	 * @param uninspected_sku_count
	 *            the uninspected_sku_count to set
	 */
	public void setUninspected_sku_count(Integer uninspected_sku_count) {
		this.uninspected_sku_count = uninspected_sku_count;
	}

}
