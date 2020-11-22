package cn.guanmai.station.bean.delivery.param;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jun 19, 2019 2:54:58 PM 
* @des 接口 /station/driver/create 对应的参数,创建司机
* @version 1.0 
*/
public class DriverCreateParam {
	private String name; // M
	private String phone; // O
	private BigDecimal carrier_id; // M
	private BigDecimal car_model_id; // M
	private String plate_number; // O
	private int share; // M
	private int state; // M
	private String account; // M
	private String password; // M
	private String password_check; // M
	private int allow_login; // M
	private int max_load; // M

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 必填参数,司机名称
	 * 
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
	 * 选填参数,司机电话
	 * 
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 
	 * @return the carrier_id
	 */
	public BigDecimal getCarrier_id() {
		return carrier_id;
	}

	/**
	 * 必填参数,绑定的承运商
	 * 
	 * @param carrier_id
	 *            the carrier_id to set
	 */
	public void setCarrier_id(BigDecimal carrier_id) {
		this.carrier_id = carrier_id;
	}

	/**
	 * @return the car_model_id
	 */
	public BigDecimal getCar_model_id() {
		return car_model_id;
	}

	/**
	 * 必填参数,绑定的车型
	 * 
	 * @param car_model_id
	 *            the car_model_id to set
	 */
	public void setCar_model_id(BigDecimal car_model_id) {
		this.car_model_id = car_model_id;
	}

	/**
	 * @return the plate_number
	 */
	public String getPlate_number() {
		return plate_number;
	}

	/**
	 * 选填参数,车牌号码
	 * 
	 * @param plate_number
	 *            the plate_number to set
	 */
	public void setPlate_number(String plate_number) {
		this.plate_number = plate_number;
	}

	/**
	 * @return the share
	 */
	public int getShare() {
		return share;
	}

	/**
	 * 必填参数,是否共享
	 * 
	 * @param share
	 *            the share to set
	 */
	public void setShare(int share) {
		this.share = share;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * 必填参数,状态
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * 必填参数,司机账号,用来登录
	 * 
	 * @param account
	 *            the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 必填参数,登录密码
	 * 
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the password_check
	 */
	public String getPassword_check() {
		return password_check;
	}

	/**
	 * 必填参数,二次确认密码
	 * 
	 * @param password_check
	 *            the password_check to set
	 */
	public void setPassword_check(String password_check) {
		this.password_check = password_check;
	}

	/**
	 * @return the allow_login
	 */
	public int getAllow_login() {
		return allow_login;
	}

	/**
	 * 必填参数,是否允许登录司机APP
	 * 
	 * @param allow_login
	 *            the allow_login to set
	 */
	public void setAllow_login(int allow_login) {
		this.allow_login = allow_login;
	}

	/**
	 * @return the max_load
	 */
	public int getMax_load() {
		return max_load;
	}

	/**
	 * 必填参数,满载数,从车型绑定的值而来
	 * 
	 * @param max_load
	 *            the max_load to set
	 */
	public void setMax_load(int max_load) {
		this.max_load = max_load;
	}

}
