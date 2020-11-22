package cn.guanmai.manage.bean.custommange.result;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONArray;

/* 
* @author liming 
* @date Jan 14, 2019 5:22:03 PM 
* @des /custommanage/list 用户转化接口返回值的类
* @version 1.0 
*/
public class MgCustomerBean {
	private String KID;
	private String SID;
	private String addr_detail;
	private String area_l1;
	private String area_l2;
	private String balance;
	private Integer check_out;
	private String city;
	private String cname;
	private String create_employee_name;
	private String create_employee_phone;
	private String create_time;
	private Integer finance_status;
	private Integer keycustomer;
	private String name;
	private String pay_method;
	private String resname;
	private String sales_employee_name;
	private String sales_employee_phone;
	private JSONArray service_station;
	private String settle_way;
	private String telephone;
	private BigDecimal user_id;
	private String username;
	private Integer whitelist;

	/**
	 * @return the kID
	 */
	public String getKID() {
		return KID;
	}

	/**
	 * @param kID the kID to set
	 */
	public void setKID(String kID) {
		KID = kID;
	}

	/**
	 * @return the sID
	 */
	public String getSID() {
		return SID;
	}

	/**
	 * @param sID the sID to set
	 */
	public void setSID(String sID) {
		SID = sID;
	}

	/**
	 * @return the addr_detail
	 */
	public String getAddr_detail() {
		return addr_detail;
	}

	/**
	 * @param addr_detail the addr_detail to set
	 */
	public void setAddr_detail(String addr_detail) {
		this.addr_detail = addr_detail;
	}

	/**
	 * @return the area_l1
	 */
	public String getArea_l1() {
		return area_l1;
	}

	/**
	 * @param area_l1 the area_l1 to set
	 */
	public void setArea_l1(String area_l1) {
		this.area_l1 = area_l1;
	}

	/**
	 * @return the area_l2
	 */
	public String getArea_l2() {
		return area_l2;
	}

	/**
	 * @param area_l2 the area_l2 to set
	 */
	public void setArea_l2(String area_l2) {
		this.area_l2 = area_l2;
	}

	/**
	 * @return the balance
	 */
	public String getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(String balance) {
		this.balance = balance;
	}

	/**
	 * @return the check_out
	 */
	public Integer getCheck_out() {
		return check_out;
	}

	/**
	 * @param check_out the check_out to set
	 */
	public void setCheck_out(Integer check_out) {
		this.check_out = check_out;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the cname
	 */
	public String getCname() {
		return cname;
	}

	/**
	 * @param cname the cname to set
	 */
	public void setCname(String cname) {
		this.cname = cname;
	}

	/**
	 * @return the create_employee_name
	 */
	public String getCreate_employee_name() {
		return create_employee_name;
	}

	/**
	 * @param create_employee_name the create_employee_name to set
	 */
	public void setCreate_employee_name(String create_employee_name) {
		this.create_employee_name = create_employee_name;
	}

	/**
	 * @return the create_employee_phone
	 */
	public String getCreate_employee_phone() {
		return create_employee_phone;
	}

	/**
	 * @param create_employee_phone the create_employee_phone to set
	 */
	public void setCreate_employee_phone(String create_employee_phone) {
		this.create_employee_phone = create_employee_phone;
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
	 * @return the finance_status
	 */
	public Integer getFinance_status() {
		return finance_status;
	}

	/**
	 * @param finance_status the finance_status to set
	 */
	public void setFinance_status(Integer finance_status) {
		this.finance_status = finance_status;
	}

	/**
	 * @return the keycustomer
	 */
	public Integer getKeycustomer() {
		return keycustomer;
	}

	/**
	 * @param keycustomer the keycustomer to set
	 */
	public void setKeycustomer(Integer keycustomer) {
		this.keycustomer = keycustomer;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the sales_employee_name
	 */
	public String getSales_employee_name() {
		return sales_employee_name;
	}

	/**
	 * @param sales_employee_name the sales_employee_name to set
	 */
	public void setSales_employee_name(String sales_employee_name) {
		this.sales_employee_name = sales_employee_name;
	}

	/**
	 * @return the sales_employee_phone
	 */
	public String getSales_employee_phone() {
		return sales_employee_phone;
	}

	/**
	 * @param sales_employee_phone the sales_employee_phone to set
	 */
	public void setSales_employee_phone(String sales_employee_phone) {
		this.sales_employee_phone = sales_employee_phone;
	}

	/**
	 * @return the service_station
	 */
	public JSONArray getService_station() {
		return service_station;
	}

	/**
	 * @param service_station the service_station to set
	 */
	public void setService_station(JSONArray service_station) {
		this.service_station = service_station;
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
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return the user_id
	 */
	public BigDecimal getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(BigDecimal user_id) {
		this.user_id = user_id;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the whitelist
	 */
	public Integer getWhitelist() {
		return whitelist;
	}

	/**
	 * @param whitelist the whitelist to set
	 */
	public void setWhitelist(Integer whitelist) {
		this.whitelist = whitelist;
	}

}
