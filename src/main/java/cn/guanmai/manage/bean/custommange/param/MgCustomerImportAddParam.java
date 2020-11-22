package cn.guanmai.manage.bean.custommange.param;

import java.util.List;

/**
 * @author liming
 * @date 2019年8月20日
 * @time 下午3:08:47
 * @des 接口 /custommanage/restaurant/import 对应的参数
 */

public class MgCustomerImportAddParam {
	// 账户名
	private String username;

	private String payer_name;

	private int settle_way;

	private int account_period_way;

	private int pay_method;

	private int settle_date_type;

	private int finance_status;

	private int whitelist;

	private String payer_telephone;

	private String company_name;

	private String restaurant_name;

	private Integer address_label_id = -1;

	private String receiver_name;

	private String receiver_telephone;

	private String district_code;

	private String area_level1;

	private String area_level2;

	private String restaurant_address;

	private String sale_employee_id;

	private List<String> salemenu_ids;

	private String fee_type = "CNY";

	public String getUsername() {
		return username;
	}

	/**
	 * 账户名
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPayer_name() {
		return payer_name;
	}

	/**
	 * 结款人
	 * 
	 * @param payer_name
	 */
	public void setPayer_name(String payer_name) {
		this.payer_name = payer_name;
	}

	public int getSettle_way() {
		return settle_way;
	}

	/**
	 * 结算方式(1为先货后款，2为先款后货)
	 * 
	 * @param settle_way
	 */
	public void setSettle_way(int settle_way) {
		this.settle_way = settle_way;
	}

	public int getAccount_period_way() {
		return account_period_way;
	}

	/**
	 * 账期方式(1为按周期，2为按额度)
	 * 
	 * @param account_period_way
	 */
	public void setAccount_period_way(int account_period_way) {
		this.account_period_way = account_period_way;
	}

	public int getPay_method() {
		return pay_method;
	}

	/**
	 * 结款周期(1为日结，2为周结)
	 * 
	 * @param pay_method
	 */
	public void setPay_method(int pay_method) {
		this.pay_method = pay_method;
	}

	public int getSettle_date_type() {
		return settle_date_type;
	}

	/**
	 * 时间维度(1为按下单时间，2为按收货时间)
	 * 
	 * @param settle_date_type
	 */
	public void setSettle_date_type(int settle_date_type) {
		this.settle_date_type = settle_date_type;
	}

	public int getFinance_status() {
		return finance_status;
	}

	/**
	 * 冻结(0为非冻结，1为冻结)
	 * 
	 * @param finance_status
	 */
	public void setFinance_status(int finance_status) {
		this.finance_status = finance_status;
	}

	public int getWhitelist() {
		return whitelist;
	}

	/**
	 * 财务白名单（0为不加入白名单，1为加入白名单）
	 * 
	 * @param whitelist
	 */
	public void setWhitelist(int whitelist) {
		this.whitelist = whitelist;
	}

	public String getPayer_telephone() {
		return payer_telephone;
	}

	/**
	 * 结款电话
	 * 
	 * @param payer_telephone
	 */
	public void setPayer_telephone(String payer_telephone) {
		this.payer_telephone = payer_telephone;
	}

	public String getCompany_name() {
		return company_name;
	}

	/**
	 * 公司名
	 * 
	 * @param company_name
	 */
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getRestaurant_name() {
		return restaurant_name;
	}

	/**
	 * 店铺名
	 * 
	 * @param restaurant_name
	 */
	public void setRestaurant_name(String restaurant_name) {
		this.restaurant_name = restaurant_name;
	}

	public int getAddress_label_id() {
		return address_label_id;
	}

	public void setAddress_label_id(Integer address_label_id) {
		this.address_label_id = address_label_id;
	}

	public String getReceiver_name() {
		return receiver_name;
	}

	/**
	 * 收货人
	 * 
	 * @param receiver_name
	 */
	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}

	public String getReceiver_telephone() {
		return receiver_telephone;
	}

	/**
	 * 收货人电话
	 * 
	 * @param receiver_telephone
	 */
	public void setReceiver_telephone(String receiver_telephone) {
		this.receiver_telephone = receiver_telephone;
	}

	public String getDistrict_code() {
		return district_code;
	}

	/**
	 * 地理标签-市级
	 * 
	 * @param district_code
	 */
	public void setDistrict_code(String district_code) {
		this.district_code = district_code;
	}

	public String getArea_level1() {
		return area_level1;
	}

	/**
	 * 地理标签-区级
	 * 
	 * @param area_level1
	 */
	public void setArea_level1(String area_level1) {
		this.area_level1 = area_level1;
	}

	public String getArea_level2() {
		return area_level2;
	}

	/**
	 * 地理标签-街道级
	 * 
	 * @param area_level2
	 */
	public void setArea_level2(String area_level2) {
		this.area_level2 = area_level2;
	}

	public String getRestaurant_address() {
		return restaurant_address;
	}

	/**
	 * 店铺地址-用于地图显示
	 * 
	 * @param restaurant_address
	 */
	public void setRestaurant_address(String restaurant_address) {
		this.restaurant_address = restaurant_address;
	}

	public String getSale_employee_id() {
		return sale_employee_id;
	}

	/**
	 * 销售经理
	 * 
	 * @param sale_employee_id
	 */
	public void setSale_employee_id(String sale_employee_id) {
		this.sale_employee_id = sale_employee_id;
	}

	public List<String> getSalemenu_ids() {
		return salemenu_ids;
	}

	/**
	 * 报价单列表
	 * 
	 * @param salemenu_ids
	 */
	public void setSalemenu_ids(List<String> salemenu_ids) {
		this.salemenu_ids = salemenu_ids;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

}
