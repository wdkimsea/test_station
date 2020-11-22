package cn.guanmai.station.bean.account.param;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date Apr 23, 2019 4:54:22 PM 
* @des 用户添加参数类
* @version 1.0 
*/
public class UserAddParam {
	private String name;
	private String username;
	private String password;
	private String station_id;
	private boolean is_admin;
	private int is_valid;
	private String phone;
	private String email;
	private String card_id;
	private String card_emblem_img;
	private String card_photo_img;
	private String card_photo_img_url;
	private String card_emblem_img_url;
	private Integer type_id;
	private List<BigDecimal> role_ids;

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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
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
	 * @return the is_admin
	 */
	public boolean isIs_admin() {
		return is_admin;
	}

	/**
	 * @param is_admin
	 *            the is_admin to set
	 */
	public void setIs_admin(boolean is_admin) {
		this.is_admin = is_admin;
	}

	/**
	 * @return the is_valid
	 */
	public int getIs_valid() {
		return is_valid;
	}

	/**
	 * @param is_valid
	 *            the is_valid to set
	 */
	public void setIs_valid(int is_valid) {
		this.is_valid = is_valid;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the card_id
	 */
	public String getCard_id() {
		return card_id;
	}

	/**
	 * @param card_id
	 *            the card_id to set
	 */
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	/**
	 * @return the card_emblem_img
	 */
	public String getCard_emblem_img() {
		return card_emblem_img;
	}

	/**
	 * @param card_emblem_img
	 *            the card_emblem_img to set
	 */
	public void setCard_emblem_img(String card_emblem_img) {
		this.card_emblem_img = card_emblem_img;
	}

	/**
	 * @return the card_photo_img
	 */
	public String getCard_photo_img() {
		return card_photo_img;
	}

	/**
	 * @param card_photo_img
	 *            the card_photo_img to set
	 */
	public void setCard_photo_img(String card_photo_img) {
		this.card_photo_img = card_photo_img;
	}

	/**
	 * @return the card_photo_img_url
	 */
	public String getCard_photo_img_url() {
		return card_photo_img_url;
	}

	/**
	 * @param card_photo_img_url
	 *            the card_photo_img_url to set
	 */
	public void setCard_photo_img_url(String card_photo_img_url) {
		this.card_photo_img_url = card_photo_img_url;
	}

	/**
	 * @return the card_emblem_img_url
	 */
	public String getCard_emblem_img_url() {
		return card_emblem_img_url;
	}

	/**
	 * @param card_emblem_img_url
	 *            the card_emblem_img_url to set
	 */
	public void setCard_emblem_img_url(String card_emblem_img_url) {
		this.card_emblem_img_url = card_emblem_img_url;
	}

	/**
	 * @return the type_id
	 */
	public Integer getType_id() {
		return type_id;
	}

	/**
	 * @param type_id
	 *            the type_id to set
	 */
	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}

	/**
	 * @return the role_ids
	 */
	public List<BigDecimal> getRole_ids() {
		return role_ids;
	}

	/**
	 * @param role_ids
	 *            the role_ids to set
	 */
	public void setRole_ids(List<BigDecimal> role_ids) {
		this.role_ids = role_ids;
	}

}
