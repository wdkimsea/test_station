package cn.guanmai.station.bean.jingcai.param;

import java.util.List;

/**
 * @author: liming
 * @Date: 2020年4月27日 下午7:20:37
 * @description:
 * @version: 1.0
 */

public class WorkshopParam {
	private String workshop_id;
	private String name;
	private String custom_id;
	private List<String> technics;

	public String getWorkshop_id() {
		return workshop_id;
	}

	/**
	 * 修改的时候才填的参数
	 * 
	 * @param workshop_id
	 */
	public void setWorkshop_id(String workshop_id) {
		this.workshop_id = workshop_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCustom_id() {
		return custom_id;
	}

	public void setCustom_id(String custom_id) {
		this.custom_id = custom_id;
	}

	public List<String> getTechnics() {
		return technics;
	}

	public void setTechnics(List<String> technics) {
		this.technics = technics;
	}

}
