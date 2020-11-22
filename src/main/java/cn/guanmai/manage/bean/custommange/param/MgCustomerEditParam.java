package cn.guanmai.manage.bean.custommange.param;

/**
 * @author liming
 * @date 2019年8月21日
 * @time 下午4:44:29
 * @des 接口 /custommanage/edit 对应的参数
 */

public class MgCustomerEditParam {
	private String id;
	private String new_cname;
	private String telephone;
	private String newPwd;
	private Integer white;
	private Integer check_out;
	private String saleEmployeeValue;
	private String creatEmployeeValue;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNew_cname() {
		return new_cname;
	}

	public void setNew_cname(String new_cname) {
		this.new_cname = new_cname;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	public Integer getWhite() {
		return white;
	}

	public void setWhite(Integer white) {
		this.white = white;
	}

	public Integer getCheck_out() {
		return check_out;
	}

	public void setCheck_out(Integer check_out) {
		this.check_out = check_out;
	}

	public String getSaleEmployeeValue() {
		return saleEmployeeValue;
	}

	public void setSaleEmployeeValue(String saleEmployeeValue) {
		this.saleEmployeeValue = saleEmployeeValue;
	}

	public String getCreatEmployeeValue() {
		return creatEmployeeValue;
	}

	public void setCreatEmployeeValue(String creatEmployeeValue) {
		this.creatEmployeeValue = creatEmployeeValue;
	}

}
