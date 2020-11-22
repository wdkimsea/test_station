package cn.guanmai.bshop.bean.account.param;

/**
 * @author: liming
 * @Date: 2020年6月18日 下午4:44:58
 * @description:
 * @version: 1.0
 */

public class BsRegisterParam {
	private String username;
	private String password;
	private Integer customer_type;
	private int is_bind_wechat;
	private Integer type;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getCustomer_type() {
		return customer_type;
	}

	public void setCustomer_type(Integer customer_type) {
		this.customer_type = customer_type;
	}

	public int getIs_bind_wechat() {
		return is_bind_wechat;
	}

	public void setIs_bind_wechat(int is_bind_wechat) {
		this.is_bind_wechat = is_bind_wechat;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
