package cn.guanmai.manage.bean.custommange.result;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @author liming
 * @date 2019年8月20日
 * @time 上午10:34:17
 * @des 商户信息批量修改 Excel 对应的 Model
 */

public class CustomerEditModel {
	@ExcelProperty(value = "商户ID", index = 0)
	private String sid;

	@ExcelProperty(value = "账户（必填）", index = 1)
	private String username;

	@ExcelProperty(value = "结款人", index = 2)
	private String payer_name;

	@ExcelProperty(value = "结款电话", index = 3)
	private String payer_phone;

	@ExcelProperty(value = "结算方式（1为先货后款，2为先款后货）", index = 4)
	private int settle_way;

	@ExcelProperty(value = "账期方式（1为按周期，2为按额度）", index = 5)
	private int pay_method;

	@ExcelProperty(value = "时间维度（1为按下单时间，2为按收货时间）", index = 6)
	private int settle_date_type;

	@ExcelProperty(value = "冻结（0为非冻结，1为冻结）", index = 7)
	private int freezeState;

	@ExcelProperty(value = "财务白名单（0为不加入白名单，1为加入白名单）", index = 8)
	private int white;

	@ExcelProperty(value = "公司名", index = 9)
	private String company_name;

	@ExcelProperty(value = "店铺名", index = 10)
	private String shop_name;

	@ExcelProperty(value = "商户自定义编码", index = 11)
	private String res_custom_code;

	@ExcelProperty(value = "统一社会信用代码", index = 12)
	private String uniform_social_credit_code;

	@ExcelProperty(value = "商户标签", index = 13)
	private String address_label_id;

	@ExcelProperty(value = "收货人", index = 14)
	private String receiver_name;

	@ExcelProperty(value = "手机号", index = 15)
	private String receiver_phone;

	@ExcelProperty(value = "地理标签", index = 16)
	private String area_code;

	@ExcelProperty(value = "店铺地址", index = 17)
	private String shop_address;

	@ExcelProperty(value = "开户经理", index = 18)
	private String create_employee_name;

	@ExcelProperty(value = "销售经理", index = 19)
	private String sale_employee_name;

	@ExcelProperty(value = "报价单ID", index = 20)
	private String salemeuns;

	@ExcelProperty(value = "改单审核(0:不审核,1:审核)", index = 21)
	private Integer order_audit;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPayer_name() {
		return payer_name;
	}

	public void setPayer_name(String payer_name) {
		this.payer_name = payer_name;
	}

	public String getPayer_phone() {
		return payer_phone;
	}

	public void setPayer_phone(String payer_phone) {
		this.payer_phone = payer_phone;
	}

	public int getSettle_way() {
		return settle_way;
	}

	public void setSettle_way(int settle_way) {
		this.settle_way = settle_way;
	}

	public int getPay_method() {
		return pay_method;
	}

	public void setPay_method(int pay_method) {
		this.pay_method = pay_method;
	}

	public int getSettle_date_type() {
		return settle_date_type;
	}

	public void setSettle_date_type(int settle_date_type) {
		this.settle_date_type = settle_date_type;
	}

	public int getFreezeState() {
		return freezeState;
	}

	public void setFreezeState(int freezeState) {
		this.freezeState = freezeState;
	}

	public int getWhite() {
		return white;
	}

	public void setWhite(int white) {
		this.white = white;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getReceiver_name() {
		return receiver_name;
	}

	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}

	public String getReceiver_phone() {
		return receiver_phone;
	}

	public void setReceiver_phone(String receiver_phone) {
		this.receiver_phone = receiver_phone;
	}

	public String getArea_code() {
		return area_code;
	}

	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}

	public String getShop_address() {
		return shop_address;
	}

	public void setShop_address(String shop_address) {
		this.shop_address = shop_address;
	}

	public String getCreate_employee_name() {
		return create_employee_name;
	}

	public void setCreate_employee_name(String create_employee_name) {
		this.create_employee_name = create_employee_name;
	}

	public String getSale_employee_name() {
		return sale_employee_name;
	}

	public void setSale_employee_name(String sale_employee_name) {
		this.sale_employee_name = sale_employee_name;
	}

	public String getSalemeuns() {
		return salemeuns;
	}

	public void setSalemeuns(String salemeuns) {
		this.salemeuns = salemeuns;
	}

	public Integer getOrder_audit() {
		return order_audit;
	}

	public void setOrder_audit(Integer order_audit) {
		this.order_audit = order_audit;
	}

	public String getRes_custom_code() {
		return res_custom_code;
	}

	public void setRes_custom_code(String res_custom_code) {
		this.res_custom_code = res_custom_code;
	}

	public String getUniform_social_credit_code() {
		return uniform_social_credit_code;
	}

	public void setUniform_social_credit_code(String uniform_social_credit_code) {
		this.uniform_social_credit_code = uniform_social_credit_code;
	}

	public String getAddress_label_id() {
		return address_label_id;
	}

	public void setAddress_label_id(String address_label_id) {
		this.address_label_id = address_label_id;
	}

	public CustomerEditModel() {
		super();
	}

}
