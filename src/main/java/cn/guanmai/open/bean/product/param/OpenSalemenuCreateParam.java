package cn.guanmai.open.bean.product.param;

/**
 * @author: liming
 * @Date: 2020年2月7日 上午11:06:38
 * @description: 新建报价单参数
 * @version: 1.0
 */

public class OpenSalemenuCreateParam {
	private String salemenu_name;
	private String time_config_id;
	private String salemenu_outer_name;
	private String desc;
	private Integer is_active;

	public String getSalemenu_name() {
		return salemenu_name;
	}

	public void setSalemenu_name(String salemenu_name) {
		this.salemenu_name = salemenu_name;
	}

	public String getTime_config_id() {
		return time_config_id;
	}

	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	public String getSalemenu_outer_name() {
		return salemenu_outer_name;
	}

	public void setSalemenu_outer_name(String salemenu_outer_name) {
		this.salemenu_outer_name = salemenu_outer_name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getIs_active() {
		return is_active;
	}

	public void setIs_active(Integer is_active) {
		this.is_active = is_active;
	}

}
