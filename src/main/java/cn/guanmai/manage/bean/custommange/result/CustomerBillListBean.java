package cn.guanmai.manage.bean.custommange.result;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jan 15, 2019 2:32:01 PM 
* @des 商户对账单查询列表类  /custommanage/bill/search
* @version 1.0 
*/
public class CustomerBillListBean {
	private String KID;
	private String SID;
	private String addr_detail;
	private BigDecimal amount;
	private BigDecimal bill_tax;
	private String company_name;
	private String consignee_name;
	private String consignee_phone;
	private String district_code;
	private String district_name;
	private BigDecimal lack;
	private BigDecimal paid_amount;
	private Integer pay_method;
	private BigDecimal pay_method_id;
	private String payment_name;
	private String payment_telephone;
	private BigDecimal refund_amount;
	private String resname;
	private BigDecimal sale_employee_id;
	private String sale_employee_name;
	private Integer settle_way;
	private String username;

	/**
	 * @return the kID
	 */
	public String getKID() {
		return KID;
	}

	/**
	 * @param kID
	 *            the kID to set
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
	 * @param sID
	 *            the sID to set
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
	 * @param addr_detail
	 *            the addr_detail to set
	 */
	public void setAddr_detail(String addr_detail) {
		this.addr_detail = addr_detail;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the bill_tax
	 */
	public BigDecimal getBill_tax() {
		return bill_tax;
	}

	/**
	 * @param bill_tax
	 *            the bill_tax to set
	 */
	public void setBill_tax(BigDecimal bill_tax) {
		this.bill_tax = bill_tax;
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
	 * @return the consignee_name
	 */
	public String getConsignee_name() {
		return consignee_name;
	}

	/**
	 * @param consignee_name
	 *            the consignee_name to set
	 */
	public void setConsignee_name(String consignee_name) {
		this.consignee_name = consignee_name;
	}

	/**
	 * @return the consignee_phone
	 */
	public String getConsignee_phone() {
		return consignee_phone;
	}

	/**
	 * @param consignee_phone
	 *            the consignee_phone to set
	 */
	public void setConsignee_phone(String consignee_phone) {
		this.consignee_phone = consignee_phone;
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
	 * @return the district_name
	 */
	public String getDistrict_name() {
		return district_name;
	}

	/**
	 * @param district_name
	 *            the district_name to set
	 */
	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}

	/**
	 * @return the lack
	 */
	public BigDecimal getLack() {
		return lack;
	}

	/**
	 * @param lack
	 *            the lack to set
	 */
	public void setLack(BigDecimal lack) {
		this.lack = lack;
	}

	/**
	 * @return the paid_amount
	 */
	public BigDecimal getPaid_amount() {
		return paid_amount;
	}

	/**
	 * @param paid_amount
	 *            the paid_amount to set
	 */
	public void setPaid_amount(BigDecimal paid_amount) {
		this.paid_amount = paid_amount;
	}

	/**
	 * @return the pay_method
	 */
	public Integer getPay_method() {
		return pay_method;
	}

	/**
	 * @param pay_method
	 *            the pay_method to set
	 */
	public void setPay_method(Integer pay_method) {
		this.pay_method = pay_method;
	}

	/**
	 * @return the pay_method_id
	 */
	public BigDecimal getPay_method_id() {
		return pay_method_id;
	}

	/**
	 * @param pay_method_id
	 *            the pay_method_id to set
	 */
	public void setPay_method_id(BigDecimal pay_method_id) {
		this.pay_method_id = pay_method_id;
	}

	/**
	 * @return the payment_name
	 */
	public String getPayment_name() {
		return payment_name;
	}

	/**
	 * @param payment_name
	 *            the payment_name to set
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
	 * @param payment_telephone
	 *            the payment_telephone to set
	 */
	public void setPayment_telephone(String payment_telephone) {
		this.payment_telephone = payment_telephone;
	}

	/**
	 * @return the refund_amount
	 */
	public BigDecimal getRefund_amount() {
		return refund_amount;
	}

	/**
	 * @param refund_amount
	 *            the refund_amount to set
	 */
	public void setRefund_amount(BigDecimal refund_amount) {
		this.refund_amount = refund_amount;
	}

	/**
	 * @return the resname
	 */
	public String getResname() {
		return resname;
	}

	/**
	 * @param resname
	 *            the resname to set
	 */
	public void setResname(String resname) {
		this.resname = resname;
	}

	/**
	 * @return the sale_employee_id
	 */
	public BigDecimal getSale_employee_id() {
		return sale_employee_id;
	}

	/**
	 * @param sale_employee_id
	 *            the sale_employee_id to set
	 */
	public void setSale_employee_id(BigDecimal sale_employee_id) {
		this.sale_employee_id = sale_employee_id;
	}

	/**
	 * @return the sale_employee_name
	 */
	public String getSale_employee_name() {
		return sale_employee_name;
	}

	/**
	 * @param sale_employee_name
	 *            the sale_employee_name to set
	 */
	public void setSale_employee_name(String sale_employee_name) {
		this.sale_employee_name = sale_employee_name;
	}

	/**
	 * @return the settle_way
	 */
	public Integer getSettle_way() {
		return settle_way;
	}

	/**
	 * @param settle_way
	 *            the settle_way to set
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
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

}
