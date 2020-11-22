package cn.guanmai.bshop.bean;

/**
 * @author liming
 * @date 2019年10月11日
 * @time 上午9:57:45
 * @des 接口 /user/optional_info 对应的参数
 */

public class BshopOptionalInfoBean {
	private int customer_regist_type;
	private int optional_receive_way;
	private int show_driver_location;

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

	public int getShow_driver_location() {
		return show_driver_location;
	}

	public void setShow_driver_location(int show_driver_location) {
		this.show_driver_location = show_driver_location;
	}

}
