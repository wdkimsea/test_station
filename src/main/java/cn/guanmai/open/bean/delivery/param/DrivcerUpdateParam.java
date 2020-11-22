package cn.guanmai.open.bean.delivery.param;

/* 
* @author liming 
* @date Jun 5, 2019 5:11:19 PM 
* @des 接口 /delivery/driver/update 对应的参数
* @version 1.0 
*/
public class DrivcerUpdateParam {
	private String driver_id;
	private String name;
	private String phone;
	private String password;
	private String carrier_id;
	private String car_model_id;
	private String plate_number;
	private String state;

	/**
	 * @return the driver_id
	 */
	public String getDriver_id() {
		return driver_id;
	}

	/**
	 * @param driver_id
	 *            the driver_id to set
	 */
	public void setDriver_id(String driver_id) {
		this.driver_id = driver_id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

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
	 * @return the car_model_id
	 */
	public String getCar_model_id() {
		return car_model_id;
	}

	/**
	 * @param car_model_id
	 *            the car_model_id to set
	 */
	public void setCar_model_id(String car_model_id) {
		this.car_model_id = car_model_id;
	}

	/**
	 * @return the plate_number
	 */
	public String getPlate_number() {
		return plate_number;
	}

	/**
	 * @param plate_number
	 *            the plate_number to set
	 */
	public void setPlate_number(String plate_number) {
		this.plate_number = plate_number;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

}
