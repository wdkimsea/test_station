package cn.guanmai.manage.bean.custommange.result;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date Jan 15, 2019 11:32:12 AM 
* @des 商户详细信息类
* @version 1.0 
*/
public class MgCustomerDetailBean {
	private String KID;
	private String SID;
	private String username;
	private String area_detail;
	private String area_l1;
	private String area_l1_code;
	private String area_l1_id;
	private String area_l2;
	private String area_l2_code;
	private String area_l2_id;
	private BigDecimal available_credit;
	private Integer begin_day;
	private String begintime;
	private String bill_address;
	private Integer check_out;
	private String city;
	private String cname;
	private String create_employee_id;
	private String create_employee_name;
	private String credit_limit;
	private String district_code;
	private String effective_date;
	private String endtime;
	private Integer finance_status;
	private Integer is_credit;
	private Integer is_whitelist;
	private Integer keycustomer;
	private Integer pay_method;
	private String payment_name;
	private String payment_telephone;
	private String receiveperson;
	private String receivephone;
	private String resname;
	private String map_address;
	private String sales_employee_id;
	private String sales_employee_name;
	private Integer settle_day;
	private Integer settle_date_type;
	private Integer settle_remind;
	private Integer settle_way;
	private String lat;
	private String lng;

	private List<Salemenu> salemenus;

	public class Salemenu {
		private String id;
		private String salemenu_id;
		private String salemenu_name;
		private String restaurant_id;
		private String station_id;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getSalemenu_id() {
			return salemenu_id;
		}

		public void setSalemenu_id(String salemenu_id) {
			this.salemenu_id = salemenu_id;
		}

		public String getSalemenu_name() {
			return salemenu_name;
		}

		public void setSalemenu_name(String salemenu_name) {
			this.salemenu_name = salemenu_name;
		}

		public String getRestaurant_id() {
			return restaurant_id;
		}

		public void setRestaurant_id(String restaurant_id) {
			this.restaurant_id = restaurant_id;
		}

		public String getStation_id() {
			return station_id;
		}

		public void setStation_id(String station_id) {
			this.station_id = station_id;
		}

	}

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
	 * @return the area_detail
	 */
	public String getArea_detail() {
		return area_detail;
	}

	/**
	 * @param area_detail the area_detail to set
	 */
	public void setArea_detail(String area_detail) {
		this.area_detail = area_detail;
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
	 * @return the area_l1_code
	 */
	public String getArea_l1_code() {
		return area_l1_code;
	}

	/**
	 * @param area_l1_code the area_l1_code to set
	 */
	public void setArea_l1_code(String area_l1_code) {
		this.area_l1_code = area_l1_code;
	}

	/**
	 * @return the area_l1_id
	 */
	public String getArea_l1_id() {
		return area_l1_id;
	}

	/**
	 * @param area_l1_id the area_l1_id to set
	 */
	public void setArea_l1_id(String area_l1_id) {
		this.area_l1_id = area_l1_id;
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
	 * @return the area_l2_code
	 */
	public String getArea_l2_code() {
		return area_l2_code;
	}

	/**
	 * @param area_l2_code the area_l2_code to set
	 */
	public void setArea_l2_code(String area_l2_code) {
		this.area_l2_code = area_l2_code;
	}

	/**
	 * @return the area_l2_id
	 */
	public String getArea_l2_id() {
		return area_l2_id;
	}

	/**
	 * @param area_l2_id the area_l2_id to set
	 */
	public void setArea_l2_id(String area_l2_id) {
		this.area_l2_id = area_l2_id;
	}

	/**
	 * @return the available_credit
	 */
	public BigDecimal getAvailable_credit() {
		return available_credit;
	}

	/**
	 * @param available_credit the available_credit to set
	 */
	public void setAvailable_credit(BigDecimal available_credit) {
		this.available_credit = available_credit;
	}

	/**
	 * @return the begin_day
	 */
	public Integer getBegin_day() {
		return begin_day;
	}

	/**
	 * @param begin_day the begin_day to set
	 */
	public void setBegin_day(Integer begin_day) {
		this.begin_day = begin_day;
	}

	/**
	 * @return the begintime
	 */
	public String getBegintime() {
		return begintime;
	}

	/**
	 * @param begintime the begintime to set
	 */
	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	/**
	 * @return the bill_address
	 */
	public String getBill_address() {
		return bill_address;
	}

	/**
	 * @param bill_address the bill_address to set
	 */
	public void setBill_address(String bill_address) {
		this.bill_address = bill_address;
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
	 * @return the create_employee_id
	 */
	public String getCreate_employee_id() {
		return create_employee_id;
	}

	/**
	 * @param create_employee_id the create_employee_id to set
	 */
	public void setCreate_employee_id(String create_employee_id) {
		this.create_employee_id = create_employee_id;
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
	 * @return the credit_limit
	 */
	public String getCredit_limit() {
		return credit_limit;
	}

	/**
	 * @param credit_limit the credit_limit to set
	 */
	public void setCredit_limit(String credit_limit) {
		this.credit_limit = credit_limit;
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
	 * @return the effective_date
	 */
	public String getEffective_date() {
		return effective_date;
	}

	/**
	 * @param effective_date the effective_date to set
	 */
	public void setEffective_date(String effective_date) {
		this.effective_date = effective_date;
	}

	/**
	 * @return the endtime
	 */
	public String getEndtime() {
		return endtime;
	}

	/**
	 * @param endtime the endtime to set
	 */
	public void setEndtime(String endtime) {
		this.endtime = endtime;
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
	 * @return the is_credit
	 */
	public Integer getIs_credit() {
		return is_credit;
	}

	/**
	 * @param is_credit the is_credit to set
	 */
	public void setIs_credit(Integer is_credit) {
		this.is_credit = is_credit;
	}

	/**
	 * @return the is_whitelist
	 */
	public Integer getIs_whitelist() {
		return is_whitelist;
	}

	/**
	 * @param is_whitelist the is_whitelist to set
	 */
	public void setIs_whitelist(Integer is_whitelist) {
		this.is_whitelist = is_whitelist;
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
	 * @return the pay_method
	 */
	public Integer getPay_method() {
		return pay_method;
	}

	/**
	 * @param pay_method the pay_method to set
	 */
	public void setPay_method(Integer pay_method) {
		this.pay_method = pay_method;
	}

	/**
	 * @return the payment_name
	 */
	public String getPayment_name() {
		return payment_name;
	}

	/**
	 * @param payment_name the payment_name to set
	 */
	public void setPayment_name(String payment_name) {
		this.payment_name = payment_name;
	}

	/**
	 * @return the payment_telephone
	 */
	public String getPayment_telephone() {
		return payment_telephone;
	}

	/**
	 * @param payment_telephone the payment_telephone to set
	 */
	public void setPayment_telephone(String payment_telephone) {
		this.payment_telephone = payment_telephone;
	}

	/**
	 * @return the receiveperson
	 */
	public String getReceiveperson() {
		return receiveperson;
	}

	/**
	 * @param receiveperson the receiveperson to set
	 */
	public void setReceiveperson(String receiveperson) {
		this.receiveperson = receiveperson;
	}

	/**
	 * @return the receivephone
	 */
	public String getReceivephone() {
		return receivephone;
	}

	/**
	 * @param receivephone the receivephone to set
	 */
	public void setReceivephone(String receivephone) {
		this.receivephone = receivephone;
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

	public String getMap_address() {
		return map_address;
	}

	public void setMap_address(String map_address) {
		this.map_address = map_address;
	}

	public List<Salemenu> getSalemenus() {
		return salemenus;
	}

	public void setSalemenus(List<Salemenu> salemenus) {
		this.salemenus = salemenus;
	}

	/**
	 * @return the sales_employee_id
	 */
	public String getSales_employee_id() {
		return sales_employee_id;
	}

	/**
	 * @param sales_employee_id the sales_employee_id to set
	 */
	public void setSales_employee_id(String sales_employee_id) {
		this.sales_employee_id = sales_employee_id;
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
	 * @return the settle_day
	 */
	public Integer getSettle_day() {
		return settle_day;
	}

	/**
	 * @param settle_day the settle_day to set
	 */
	public void setSettle_day(Integer settle_day) {
		this.settle_day = settle_day;
	}

	public Integer getSettle_date_type() {
		return settle_date_type;
	}

	public void setSettle_date_type(Integer settle_date_type) {
		this.settle_date_type = settle_date_type;
	}

	/**
	 * @return the settle_remind
	 */
	public Integer getSettle_remind() {
		return settle_remind;
	}

	/**
	 * @param settle_remind the settle_remind to set
	 */
	public void setSettle_remind(Integer settle_remind) {
		this.settle_remind = settle_remind;
	}

	/**
	 * @return the settle_way
	 */
	public Integer getSettle_way() {
		return settle_way;
	}

	/**
	 * @param settle_way the settle_way to set
	 */
	public void setSettle_way(Integer settle_way) {
		this.settle_way = settle_way;
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

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

}
