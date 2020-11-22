package cn.guanmai.station.bean.system;

/**
 * @author liming
 * @date 2019年9月25日
 * @time 上午10:29:34
 * @des 店铺设置相关信息
 */

public class CustomizedBean {
	private String title; // 店铺名称

	private String phone; // 电话

	private int is_need_invitation_code; // 是否需要邀请码

	private int is_open_sku_detail; // 商品详情

	private int is_open_order_edit; // 商城商户自主改单

	private Integer order_edit_time_limit; // 商户自主改单时间限制

	private int is_open_manage_stock; // 商城进销存

	private int is_user_change_own_pwd;// 短信找回密码

	private int is_open_new_merchandise;// 新品需求

	private int is_verify_phone_on_register;// 手机号注册

	private int customer_regist_type; // 商户注册类型

	private int optional_receive_way; // 商户收货方式

	private int support_point_exchange; // 积分

	private int show_driver_location; // 司机位置

	private int show_enterprise_brand; // 企业品牌厅

	private String default_salemenu_id; // 默认报价单

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getIs_need_invitation_code() {
		return is_need_invitation_code;
	}

	public void setIs_need_invitation_code(int is_need_invitation_code) {
		this.is_need_invitation_code = is_need_invitation_code;
	}

	public int getIs_open_sku_detail() {
		return is_open_sku_detail;
	}

	public void setIs_open_sku_detail(int is_open_sku_detail) {
		this.is_open_sku_detail = is_open_sku_detail;
	}

	public int getIs_open_order_edit() {
		return is_open_order_edit;
	}

	public void setIs_open_order_edit(int is_open_order_edit) {
		this.is_open_order_edit = is_open_order_edit;
	}

	public Integer getOrder_edit_time_limit() {
		return order_edit_time_limit;
	}

	public void setOrder_edit_time_limit(Integer order_edit_time_limit) {
		this.order_edit_time_limit = order_edit_time_limit;
	}

	public int getIs_open_manage_stock() {
		return is_open_manage_stock;
	}

	public void setIs_open_manage_stock(int is_open_manage_stock) {
		this.is_open_manage_stock = is_open_manage_stock;
	}

	public int getIs_user_change_own_pwd() {
		return is_user_change_own_pwd;
	}

	public void setIs_user_change_own_pwd(int is_user_change_own_pwd) {
		this.is_user_change_own_pwd = is_user_change_own_pwd;
	}

	public int getIs_open_new_merchandise() {
		return is_open_new_merchandise;
	}

	public void setIs_open_new_merchandise(int is_open_new_merchandise) {
		this.is_open_new_merchandise = is_open_new_merchandise;
	}

	public int getIs_verify_phone_on_register() {
		return is_verify_phone_on_register;
	}

	public void setIs_verify_phone_on_register(int is_verify_phone_on_register) {
		this.is_verify_phone_on_register = is_verify_phone_on_register;
	}

	public int getCustomer_regist_type() {
		return customer_regist_type;
	}

	public void setCustomer_regist_type(int customer_regist_type) {
		this.customer_regist_type = customer_regist_type;
	}

	public int getOptional_receive_way() {
		return optional_receive_way;
	}

	public void setOptional_receive_way(int optional_receive_way) {
		this.optional_receive_way = optional_receive_way;
	}

	public int getSupport_point_exchange() {
		return support_point_exchange;
	}

	public void setSupport_point_exchange(int support_point_exchange) {
		this.support_point_exchange = support_point_exchange;
	}

	public int getShow_driver_location() {
		return show_driver_location;
	}

	public void setShow_driver_location(int show_driver_location) {
		this.show_driver_location = show_driver_location;
	}

	public int getShow_enterprise_brand() {
		return show_enterprise_brand;
	}

	public void setShow_enterprise_brand(int show_enterprise_brand) {
		this.show_enterprise_brand = show_enterprise_brand;
	}

	public String getDefault_salemenu_id() {
		return default_salemenu_id;
	}

	public void setDefault_salemenu_id(String default_salemenu_id) {
		this.default_salemenu_id = default_salemenu_id;
	}

}
