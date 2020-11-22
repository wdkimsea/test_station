package cn.guanmai.manage.bean.custommange.param;

import com.alibaba.fastjson.JSONArray;

/**
 * @author liming
 * @date 2019年8月6日 下午7:15:01
 * @des 接口 /custommanage/restaurant/add 对应参数
 * @version 1.0
 */
public class MgCustomerAddParam {
	private String username;
	private String company_name;
	private String password;
	private boolean isEditPassword;
	private String telephone;
	private int customer_type;
	private int settle_way;
	private String payer_name;
	private String payer_telephone;
	private int finance_status;
	private int whitelist;
	private int check_out;
	private int pay_method;
	private int settle_date_type;
	private int is_credit;
	private String lng;
	private String lat;
	private String map_address;
	private String restaurant_name;
	private String receiver_name;
	private String receiver_telephone;
	private String district_code;
	private String area_level1;
	private String area_level2;
	private String restaurant_address;
	private String create_employee_id;
	private String sale_employee_id;
	private JSONArray salemenu_ids;

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
	 * @return the company_name
	 */
	public String getCompany_name() {
		return company_name;
	}

	/**
	 * @param company_name
	 *            the company_name to set
	 */
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
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
	 * @return the isEditPassword
	 */
	public boolean isEditPassword() {
		return isEditPassword;
	}

	/**
	 * @param isEditPassword
	 *            the isEditPassword to set
	 */
	public void setEditPassword(boolean isEditPassword) {
		this.isEditPassword = isEditPassword;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone
	 *            the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return the customer_type
	 */
	public int getCustomer_type() {
		return customer_type;
	}

	/**
	 * @param customer_type
	 *            the customer_type to set
	 */
	public void setCustomer_type(int customer_type) {
		this.customer_type = customer_type;
	}

	/**
	 * @return the settle_way
	 */
	public int getSettle_way() {
		return settle_way;
	}

	/**
	 * @param settle_way
	 *            the settle_way to set
	 */
	public void setSettle_way(int settle_way) {
		this.settle_way = settle_way;
	}

	/**
	 * @return the payer_name
	 */
	public String getPayer_name() {
		return payer_name;
	}

	/**
	 * @param payer_name
	 *            the payer_name to set
	 */
	public void setPayer_name(String payer_name) {
		this.payer_name = payer_name;
	}

	/**
	 * @return the payer_telephone
	 */
	public String getPayer_telephone() {
		return payer_telephone;
	}

	/**
	 * @param payer_telephone
	 *            the payer_telephone to set
	 */
	public void setPayer_telephone(String payer_telephone) {
		this.payer_telephone = payer_telephone;
	}

	/**
	 * @return the finance_status
	 */
	public int getFinance_status() {
		return finance_status;
	}

	/**
	 * @param finance_status
	 *            the finance_status to set
	 */
	public void setFinance_status(int finance_status) {
		this.finance_status = finance_status;
	}

	/**
	 * @return the whitelist
	 */
	public int getWhitelist() {
		return whitelist;
	}

	/**
	 * @param whitelist
	 *            the whitelist to set
	 */
	public void setWhitelist(int whitelist) {
		this.whitelist = whitelist;
	}

	/**
	 * @return the check_out
	 */
	public int getCheck_out() {
		return check_out;
	}

	/**
	 * @param check_out
	 *            the check_out to set
	 */
	public void setCheck_out(int check_out) {
		this.check_out = check_out;
	}

	/**
	 * @return the pay_method
	 */
	public int getPay_method() {
		return pay_method;
	}

	/**
	 * @param pay_method
	 *            the pay_method to set
	 */
	public void setPay_method(int pay_method) {
		this.pay_method = pay_method;
	}

	/**
	 * @return the settle_date_type
	 */
	public int getSettle_date_type() {
		return settle_date_type;
	}

	/**
	 * @param settle_date_type
	 *            the settle_date_type to set
	 */
	public void setSettle_date_type(int settle_date_type) {
		this.settle_date_type = settle_date_type;
	}

	/**
	 * @return the is_credit
	 */
	public int getIs_credit() {
		return is_credit;
	}

	/**
	 * @param is_credit
	 *            the is_credit to set
	 */
	public void setIs_credit(int is_credit) {
		this.is_credit = is_credit;
	}

	/**
	 * @return the lng
	 */
	public String getLng() {
		return lng;
	}

	/**
	 * @param lng
	 *            the lng to set
	 */
	public void setLng(String lng) {
		this.lng = lng;
	}

	/**
	 * @return the lat
	 */
	public String getLat() {
		return lat;
	}

	/**
	 * @param lat
	 *            the lat to set
	 */
	public void setLat(String lat) {
		this.lat = lat;
	}

	/**
	 * @return the map_address
	 */
	public String getMap_address() {
		return map_address;
	}

	/**
	 * @param map_address
	 *            the map_address to set
	 */
	public void setMap_address(String map_address) {
		this.map_address = map_address;
	}

	/**
	 * @return the restaurant_name
	 */
	public String getRestaurant_name() {
		return restaurant_name;
	}

	/**
	 * @param restaurant_name
	 *            the restaurant_name to set
	 */
	public void setRestaurant_name(String restaurant_name) {
		this.restaurant_name = restaurant_name;
	}

	/**
	 * @return the receiver_name
	 */
	public String getReceiver_name() {
		return receiver_name;
	}

	/**
	 * @param receiver_name
	 *            the receiver_name to set
	 */
	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}

	/**
	 * @return the receiver_telephone
	 */
	public String getReceiver_telephone() {
		return receiver_telephone;
	}

	/**
	 * @param receiver_telephone
	 *            the receiver_telephone to set
	 */
	public void setReceiver_telephone(String receiver_telephone) {
		this.receiver_telephone = receiver_telephone;
	}

	/**
	 * @return the district_code
	 */
	public String getDistrict_code() {
		return district_code;
	}

	/**
	 * @param district_code
	 *            the district_code to set
	 */
	public void setDistrict_code(String district_code) {
		this.district_code = district_code;
	}

	/**
	 * @return the area_level1
	 */
	public String getArea_level1() {
		return area_level1;
	}

	/**
	 * @param area_level1
	 *            the area_level1 to set
	 */
	public void setArea_level1(String area_level1) {
		this.area_level1 = area_level1;
	}

	/**
	 * @return the area_level2
	 */
	public String getArea_level2() {
		return area_level2;
	}

	/**
	 * @param area_level2
	 *            the area_level2 to set
	 */
	public void setArea_level2(String area_level2) {
		this.area_level2 = area_level2;
	}

	/**
	 * @return the restaurant_address
	 */
	public String getRestaurant_address() {
		return restaurant_address;
	}

	/**
	 * @param restaurant_address
	 *            the restaurant_address to set
	 */
	public void setRestaurant_address(String restaurant_address) {
		this.restaurant_address = restaurant_address;
	}

	/**
	 * @return the create_employee_id
	 */
	public String getCreate_employee_id() {
		return create_employee_id;
	}

	/**
	 * @param create_employee_id
	 *            the create_employee_id to set
	 */
	public void setCreate_employee_id(String create_employee_id) {
		this.create_employee_id = create_employee_id;
	}

	/**
	 * @return the sale_employee_id
	 */
	public String getSale_employee_id() {
		return sale_employee_id;
	}

	/**
	 * @param sale_employee_id
	 *            the sale_employee_id to set
	 */
	public void setSale_employee_id(String sale_employee_id) {
		this.sale_employee_id = sale_employee_id;
	}

	/**
	 * @return the salemenu_ids
	 */
	public JSONArray getSalemenu_ids() {
		return salemenu_ids;
	}

	/**
	 * @param salemenu_ids
	 *            the salemenu_ids to set
	 */
	public void setSalemenu_ids(JSONArray salemenu_ids) {
		this.salemenu_ids = salemenu_ids;
	}

}
