package cn.guanmai.open.bean.customer.param;

import java.util.List;

import com.alibaba.fastjson.JSONArray;

/* 
* @author liming 
* @date Jun 6, 2019 10:02:19 AM 
* @des 接口 /customer/update 对应的参数
* @version 1.0 
*/
public class OpenCustomerUpdateParam {
	private String customer_id;
	private String customer_name;
	private String password;
	private String payer_name;
	private String payer_telephone;
	private String receiver_name;
	private String receiver_telephone;
	private String district_code;
	private String area_level1;
	private String area_level2;
	private String customer_address;
	private String res_custom_code;
	private Object salemenu_ids;

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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the payer_name
	 */
	public String getPayer_name() {
		return payer_name;
	}

	/**
	 * @param payer_name the payer_name to set
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
	 * @param payer_telephone the payer_telephone to set
	 */
	public void setPayer_telephone(String payer_telephone) {
		this.payer_telephone = payer_telephone;
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
	 * @param receiver_telephone the receiver_telephone to set
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

	public String getRes_custom_code() {
		return res_custom_code;
	}

	public void setRes_custom_code(String res_custom_code) {
		this.res_custom_code = res_custom_code;
	}

	/**
	 * @return the salemenu_ids
	 */
	public JSONArray getSalemenu_ids() {
		return JSONArray.parseArray(salemenu_ids.toString());
	}

	/**
	 * @param salemenu_ids the salemenu_ids to set
	 */
	public void setSalemenu_ids(List<String> salemenu_ids) {
		this.salemenu_ids = JSONArray.parseArray(salemenu_ids.toString());
	}

}
