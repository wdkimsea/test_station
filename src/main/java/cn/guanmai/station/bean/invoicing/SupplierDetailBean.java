package cn.guanmai.station.bean.invoicing;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Nov 7, 2018 3:17:40 PM 
* @des 供应商
* @version 1.0 
*/
public class SupplierDetailBean {
	@JSONField(name = "id", alternateNames = { "_id" })
	private String id;
	private String customer_id;
	private String name;
	private String phone;
	private String company_name;
	private String company_address;
	private JSONArray merchandise;
	private String finance_manager;
	private String finance_manager_phone;
	private String account_name;
	private String bank;
	private String card_no;
	private String business_licence;
	private int pay_method;
	private String location_lat;
	private String location_lon;
	private JSONArray qualification_images;
	private String username;
	private int is_active;
	private String supplier_id;
	private String default_purchaser_id; // 默认采购员ID
	private String default_purchaser_name;
	private String user_id;

	/**
	 * 系统分配的ID
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 系统分配的ID
	 * 
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 自定义ID
	 * 
	 * @return the customer_id
	 */
	public String getCustomer_id() {
		return customer_id;
	}

	/**
	 * 自定义ID
	 * 
	 * @param customer_id the customer_id to set
	 */
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	/**
	 * 供应商名称
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 供应商名称
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 联系电话
	 * 
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 联系电话
	 * 
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 公司名称
	 * 
	 * @return the company_name
	 */
	public String getCompany_name() {
		return company_name;
	}

	/**
	 * 公司名称
	 * 
	 * @param company_name the company_name to set
	 */
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	/**
	 * 公司地址
	 * 
	 * @return the company_address
	 */
	public String getCompany_address() {
		return company_address;
	}

	/**
	 * 公司地址
	 * 
	 * @param company_address the company_address to set
	 */
	public void setCompany_address(String company_address) {
		this.company_address = company_address;
	}

	/**
	 * 绑定的的二级分类
	 * 
	 * @return the merchandise
	 */
	public JSONArray getMerchandise() {
		return merchandise;
	}

	/**
	 * 绑定的的二级分类
	 * 
	 * @param merchandise the merchandise to set
	 */
	public void setMerchandise(JSONArray merchandise) {
		this.merchandise = merchandise;
	}

	/**
	 * 财务联系人
	 * 
	 * @return the finance_manager
	 */
	public String getFinance_manager() {
		return finance_manager;
	}

	/**
	 * 财务联系人
	 * 
	 * @param finance_manager the finance_manager to set
	 */
	public void setFinance_manager(String finance_manager) {
		this.finance_manager = finance_manager;
	}

	/**
	 * 财务联系电话
	 * 
	 * @return the finance_manager_phone
	 */
	public String getFinance_manager_phone() {
		return finance_manager_phone;
	}

	/**
	 * 财务联系电话
	 * 
	 * @param finance_manager_phone the finance_manager_phone to set
	 */
	public void setFinance_manager_phone(String finance_manager_phone) {
		this.finance_manager_phone = finance_manager_phone;
	}

	/**
	 * 开户名
	 * 
	 * @return the account_name
	 */
	public String getAccount_name() {
		return account_name;
	}

	/**
	 * 开户名
	 * 
	 * @param account_name the account_name to set
	 */
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	/**
	 * 开户银行
	 * 
	 * @return the bank
	 */
	public String getBank() {
		return bank;
	}

	/**
	 * 开户银行
	 * 
	 * @param bank the bank to set
	 */
	public void setBank(String bank) {
		this.bank = bank;
	}

	/**
	 * 银行卡号
	 * 
	 * @return the card_no
	 */
	public String getCard_no() {
		return card_no;
	}

	/**
	 * 银行卡号
	 * 
	 * @param card_no the card_no to set
	 */
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	/**
	 * 营业执照
	 * 
	 * @return the business_licence
	 */
	public String getBusiness_licence() {
		return business_licence;
	}

	/**
	 * 营业执照
	 * 
	 * @param business_licence the business_licence to set
	 */
	public void setBusiness_licence(String business_licence) {
		this.business_licence = business_licence;
	}

	/**
	 * 付款方式
	 * 
	 * @return the pay_method
	 */
	public int getPay_method() {
		return pay_method;
	}

	/**
	 * 付款方式
	 * 
	 * @param pay_method the pay_method to set
	 */
	public void setPay_method(int pay_method) {
		this.pay_method = pay_method;
	}

	/**
	 * @return the location_lat
	 */
	public String getLocation_lat() {
		return location_lat;
	}

	/**
	 * @param location_lat the location_lat to set
	 */
	public void setLocation_lat(String location_lat) {
		this.location_lat = location_lat;
	}

	/**
	 * @return the location_lon
	 */
	public String getLocation_lon() {
		return location_lon;
	}

	/**
	 * @param location_lon the location_lon to set
	 */
	public void setLocation_lon(String location_lon) {
		this.location_lon = location_lon;
	}

	/**
	 * 资料图片
	 * 
	 * @return the qualification_images
	 */
	public JSONArray getQualification_images() {
		return qualification_images;
	}

	/**
	 * 资料图片
	 * 
	 * @param qualification_images the qualification_images to set
	 */
	public void setQualification_images(JSONArray qualification_images) {
		this.qualification_images = qualification_images;
	}

	/**
	 * 登录账号
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 登录账号
	 * 
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 账号状态
	 * 
	 * @return the is_active
	 */
	public int getIs_active() {
		return is_active;
	}

	/**
	 * 账号状态
	 * 
	 * @param is_active the is_active to set
	 */
	public void setIs_active(int is_active) {
		this.is_active = is_active;
	}

	/**
	 * 搜索显示用的系统ID
	 * 
	 * @return the supplier_id
	 */
	public String getSupplier_id() {
		return supplier_id;
	}

	/**
	 * 搜索显示用的系统ID
	 * 
	 * @param supplier_id the supplier_id to set
	 */
	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	public SupplierDetailBean() {
		super();
	}

	/**
	 * @return the default_purchaser_id
	 */
	public String getDefault_purchaser_id() {
		return default_purchaser_id;
	}

	/**
	 * @param default_purchaser_id the default_purchaser_id to set
	 */
	public void setDefault_purchaser_id(String default_purchaser_id) {
		this.default_purchaser_id = default_purchaser_id;
	}

	public String getDefault_purchaser_name() {
		return default_purchaser_name;
	}

	public void setDefault_purchaser_name(String default_purchaser_name) {
		this.default_purchaser_name = default_purchaser_name;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	/**
	 * 用于创建供应商的构造方法
	 * 
	 * @param customer_id
	 * @param name
	 * @param merchandise
	 * @param pay_method
	 */
	public SupplierDetailBean(String customer_id, String name, JSONArray merchandise, int pay_method) {
		super();
		this.customer_id = customer_id;
		this.name = name;
		this.merchandise = merchandise;
		this.pay_method = pay_method;
	}

	/**
	 * 搜索供应商的反序列化构造方法
	 * 
	 * @param customer_id
	 * @param name
	 * @param phone
	 * @param supplier_id
	 */
	public SupplierDetailBean(String customer_id, String name, String phone, String supplier_id) {
		super();
		this.customer_id = customer_id;
		this.name = name;
		this.phone = phone;
		this.supplier_id = supplier_id;
	}

}
