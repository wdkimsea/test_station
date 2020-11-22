package cn.guanmai.station.bean.delivery;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jan 4, 2019 11:02:55 AM 
* @des 接口 /station/driver/list 的结果, 配送司机
* @version 1.0 
*/
public class DriverBean {
	private BigDecimal id;
	private String account;
	private int can_edit;
	private String car_model;
	private BigDecimal car_model_id;
	private String carrier_name;
	private BigDecimal carrier_id;
	private Integer max_load;
	private String name;
	private String phone;
	private String plate_number;
	private int share;
	private int state;
	private String station_id;

	/**
	 * @return the id
	 */
	public BigDecimal getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(BigDecimal id) {
		this.id = id;
	}

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
	 * @return the can_edit
	 */
	public int getCan_edit() {
		return can_edit;
	}

	/**
	 * @param can_edit
	 *            the can_edit to set
	 */
	public void setCan_edit(int can_edit) {
		this.can_edit = can_edit;
	}

	/**
	 * @return the car_model
	 */
	public String getCar_model() {
		return car_model;
	}

	/**
	 * @param car_model
	 *            the car_model to set
	 */
	public void setCar_model(String car_model) {
		this.car_model = car_model;
	}

	/**
	 * @return the car_model_id
	 */
	public BigDecimal getCar_model_id() {
		return car_model_id;
	}

	/**
	 * @param car_model_id
	 *            the car_model_id to set
	 */
	public void setCar_model_id(BigDecimal car_model_id) {
		this.car_model_id = car_model_id;
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
	 * @return the carrier_id
	 */
	public BigDecimal getCarrier_id() {
		return carrier_id;
	}

	/**
	 * @param carrier_id
	 *            the carrier_id to set
	 */
	public void setCarrier_id(BigDecimal carrier_id) {
		this.carrier_id = carrier_id;
	}

	/**
	 * @return the max_load
	 */
	public Integer getMax_load() {
		return max_load;
	}

	/**
	 * @param max_load
	 *            the max_load to set
	 */
	public void setMax_load(Integer max_load) {
		this.max_load = max_load;
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
	 * @return the share
	 */
	public int getShare() {
		return share;
	}

	/**
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

}
