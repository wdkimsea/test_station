package cn.guanmai.station.bean.account;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date Apr 23, 2019 4:29:41 PM 
* @des 用户详细信息类
* @version 1.0 
*/
public class UserDetailBean {
	private String username;
	private String name;
	private Integer id;
	private Integer type_id;
	private String create_date;
	private boolean is_admin;
	private boolean is_valid;
	private String card_emblem_img_url;
	private String card_photo_img_url;
	private String card_id;
	private String phone;
	private String email;
	private String station_id;
	private List<Role> roles;

	public class Role {
		private BigDecimal id;
		private String name;

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
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
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
	 * @return the create_date
	 */
	public String getCreate_date() {
		return create_date;
	}

	/**
	 * @param create_date
	 *            the create_date to set
	 */
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
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
	public boolean isIs_valid() {
		return is_valid;
	}

	/**
	 * @param is_valid
	 *            the is_valid to set
	 */
	public void setIs_valid(boolean is_valid) {
		this.is_valid = is_valid;
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
	 * @return the roles
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
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
