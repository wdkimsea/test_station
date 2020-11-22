package cn.guanmai.station.bean.delivery.param;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jun 19, 2019 6:01:26 PM 
* @des 接口 /station/driver/update 对应的参数 
* @version 1.0 
*/
public class DriverUpdateParam extends DriverCreateParam {
	private BigDecimal driver_id; // O

	/**
	 * @return the driver_id
	 */
	public BigDecimal getDriver_id() {
		return driver_id;
	}

	/**
	 * @param driver_id
	 *            the driver_id to set
	 */
	public void setDriver_id(BigDecimal driver_id) {
		this.driver_id = driver_id;
	}

}
