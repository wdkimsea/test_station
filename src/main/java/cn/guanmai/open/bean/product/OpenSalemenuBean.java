package cn.guanmai.open.bean.product;

import java.util.HashMap;
import java.util.List;

public class OpenSalemenuBean {
	private String salemenu_id;
	private String salemenu_name;
	private String salemenu_outer_name;
	private String desc;
	private String time_config_id;
	private String time_config_name;
	private Boolean is_active;
	private Integer salemenu_type;
	private HashMap<String, List<String>> receive_time;

	public String getId() {
		return salemenu_id;
	}

	public void setId(String id) {
		this.salemenu_id = id;
	}

	public String getName() {
		return salemenu_name;
	}

	public void setName(String name) {
		this.salemenu_name = name;
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

	public String getTime_config_id() {
		return time_config_id;
	}

	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	public String getTime_config_name() {
		return time_config_name;
	}

	public void setTime_config_name(String time_config_name) {
		this.time_config_name = time_config_name;
	}

	public Boolean getIs_active() {
		return is_active;
	}

	public void setIs_active(Boolean is_active) {
		this.is_active = is_active;
	}

	public Integer getSalemenu_type() {
		return salemenu_type;
	}

	public void setSalemenu_type(Integer salemenu_type) {
		this.salemenu_type = salemenu_type;
	}

	/**
	 * @return the receive_time
	 */
	public HashMap<String, List<String>> getReceive_time() {
		return receive_time;
	}

	/**
	 * @param receive_time the receive_time to set
	 */
	public void setReceive_time(HashMap<String, List<String>> receive_time) {
		this.receive_time = receive_time;
	}

}
