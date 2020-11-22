package cn.guanmai.open.bean.delivery;

/* 
* @author liming 
* @date Jun 5, 2019 5:42:44 PM 
* @des 接口 /delivery/carrier/list 对应的结果
* @version 1.0 
*/
public class OpenCarrierBean {
	private String carrier_id;
	private String carrier_name;
	private int driver_count;

	/**
	 * @return the carrier_id
	 */
	public String getCarrier_id() {
		return carrier_id;
	}

	/**
	 * @param carrier_id
	 *            the carrier_id to set
	 */
	public void setCarrier_id(String carrier_id) {
		this.carrier_id = carrier_id;
	}

	/**
	 * @return the carrier_name
	 */
	public String getCarrier_name() {
		return carrier_name;
	}

	/**
	 * @param carrier_name
	 *            the carrier_name to set
	 */
	public void setCarrier_name(String carrier_name) {
		this.carrier_name = carrier_name;
	}

	/**
	 * @return the driver_count
	 */
	public int getDriver_count() {
		return driver_count;
	}

	/**
	 * @param driver_count
	 *            the driver_count to set
	 */
	public void setDriver_count(int driver_count) {
		this.driver_count = driver_count;
	}

}
