package cn.guanmai.open.bean.customer;

/* 
* @author liming 
* @date Jun 6, 2019 10:08:01 AM 
* @des 接口 /customer/list 对应的参数
* @version 1.0 
*/
public class OpenCustomerBean {
	private String customer_id;
	private String customer_name;
	private String customer_address;
	private String customer_telephone;
	private String payer_name;
	private String payer_telephone;
	private String resname;
	private String receiver_name;
	private String receiver_telephone;
	private String district_code;
	private String district_name;
	private String area_level1;
	private String area_level1_name;
	private String area_level2;
	private String area_level2_name;
	private String pay_method;
	private String settle_way;
	private String whitelist;
	private String create_time;
	private String res_custom_code;
	private int check_out;

	/**
	 * @return the customer_id
	 */
	public String getCustomer_id() {
		return customer_id;
	}

	/**
	 * @param customer_id the customer_id to set
	 */
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	/**
	 * @return the customer_name
	 */
	public String getCustomer_name() {
		return customer_name;
	}

	/**
	 * @param customer_name the customer_name to set
	 */
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	/**
	 * @return the customer_address
	 */
	public String getCustomer_address() {
		return customer_address;
	}

	/**
	 * @param customer_address the customer_address to set
	 */
	public void setCustomer_address(String customer_address) {
		this.customer_address = customer_address;
	}

	/**
	 * @return the customer_telephone
	 */
	public String getCustomer_telephone() {
		return customer_telephone;
	}

	/**
	 * @param customer_telephone the customer_telephone to set
	 */
	public void setCustomer_telephone(String customer_telephone) {
		this.customer_telephone = customer_telephone;
	}

	public String getPayer_name() {
		return payer_name;
	}

	public void setPayer_name(String payer_name) {
		this.payer_name = payer_name;
	}

	public String getPayer_telephone() {
		return payer_telephone;
	}

	public void setPayer_telephone(String payer_telephone) {
		this.payer_telephone = payer_telephone;
	}

	/**
	 * @return the resname
	 */
	public String getResname() {
		return resname;
	}

	/**
	 * @param resname the resname to set
	 */
	public void setResname(String resname) {
		this.resname = resname;
	}

	/**
	 * @return the receiver_name
	 */
	public String getReceiver_name() {
		return receiver_name;
	}

	/**
	 * @param receiver_name the receiver_name to set
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
	 * @param receiver_telephone the receiver_phone to set
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
	 * @param district_code the district_code to set
	 */
	public void setDistrict_code(String district_code) {
		this.district_code = district_code;
	}

	/**
	 * @return the district_name
	 */
	public String getDistrict_name() {
		return district_name;
	}

	/**
	 * @param district_name the district_name to set
	 */
	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}

	/**
	 * @return the area_level1
	 */
	public String getArea_level1() {
		return area_level1;
	}

	/**
	 * @param area_level1 the area_level1 to set
	 */
	public void setArea_level1(String area_level1) {
		this.area_level1 = area_level1;
	}

	/**
	 * @return the area_level1_name
	 */
	public String getArea_level1_name() {
		return area_level1_name;
	}

	/**
	 * @param area_level1_name the area_level1_name to set
	 */
	public void setArea_level1_name(String area_level1_name) {
		this.area_level1_name = area_level1_name;
	}

	/**
	 * @return the area_level2
	 */
	public String getArea_level2() {
		return area_level2;
	}

	/**
	 * @param area_level2 the area_level2 to set
	 */
	public void setArea_level2(String area_level2) {
		this.area_level2 = area_level2;
	}

	/**
	 * @return the area_level2_name
	 */
	public String getArea_level2_name() {
		return area_level2_name;
	}

	/**
	 * @param area_level2_name the area_level2_name to set
	 */
	public void setArea_level2_name(String area_level2_name) {
		this.area_level2_name = area_level2_name;
	}

	/**
	 * @return the pay_method
	 */
	public String getPay_method() {
		return pay_method;
	}

	/**
	 * @param pay_method the pay_method to set
	 */
	public void setPay_method(String pay_method) {
		this.pay_method = pay_method;
	}

	/**
	 * @return the settle_way
	 */
	public String getSettle_way() {
		return settle_way;
	}

	/**
	 * @param settle_way the settle_way to set
	 */
	public void setSettle_way(String settle_way) {
		this.settle_way = settle_way;
	}

	/**
	 * @return the whitelist
	 */
	public String getWhitelist() {
		return whitelist;
	}

	/**
	 * @param whitelist the whitelist to set
	 */
	public void setWhitelist(String whitelist) {
		this.whitelist = whitelist;
	}

	/**
	 * @return the create_time
	 */
	public String getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time the create_time to set
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the check_out
	 */
	public int getCheck_out() {
		return check_out;
	}

	/**
	 * @param check_out the check_out to set
	 */
	public void setCheck_out(int check_out) {
		this.check_out = check_out;
	}

	public String getRes_custom_code() {
		return res_custom_code;
	}

	public void setRes_custom_code(String res_custom_code) {
		this.res_custom_code = res_custom_code;
	}

}
