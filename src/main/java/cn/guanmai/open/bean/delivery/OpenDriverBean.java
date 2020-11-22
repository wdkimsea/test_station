package cn.guanmai.open.bean.delivery;

/* 
* @author liming 
* @date Jun 5, 2019 5:20:08 PM 
* @des 接口 /delivery/driver/list 对应的结果
* @version 1.0 
*/
public class OpenDriverBean {
	private String account;
	private String driver_id;
	private String driver_name;
	private String phone;
	private String car_model_id;
	private String car_model_name;
	private int max_load;
	private String plate_number;
	private String carrier_id;
	private String carrier_name;
	private boolean delivered;
	private boolean login;
	private int state;
	private String station_id;
	private String station_name;

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

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
	 * @return the driver_name
	 */
	public String getDriver_name() {
		return driver_name;
	}

	/**
	 * @param driver_name
	 *            the driver_name to set
	 */
	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
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
	 * @return the car_model_name
	 */
	public String getCar_model_name() {
		return car_model_name;
	}

	/**
	 * @param car_model_name
	 *            the car_model_name to set
	 */
	public void setCar_model_name(String car_model_name) {
		this.car_model_name = car_model_name;
	}

	/**
	 * @return the max_load
	 */
	public int getMax_load() {
		return max_load;
	}

	/**
	 * @param max_load
	 *            the max_load to set
	 */
	public void setMax_load(int max_load) {
		this.max_load = max_load;
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
	 * @return the delivered
	 */
	public boolean isDelivered() {
		return delivered;
	}

	/**
	 * @param delivered
	 *            the delivered to set
	 */
	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}

	/**
	 * @return the login
	 */
	public boolean isLogin() {
		return login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(boolean login) {
		this.login = login;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * @return the station_id
	 */
	public String getStation_id() {
		return station_id;
	}

	/**
	 * @param station_id
	 *            the station_id to set
	 */
	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	/**
	 * @return the station_name
	 */
	public String getStation_name() {
		return station_name;
	}

	/**
	 * @param station_name
	 *            the station_name to set
	 */
	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}

}
