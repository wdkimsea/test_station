package cn.guanmai.station.bean.jingcai;

import java.util.List;

/**
 * @author: liming
 * @Date: 2020年4月27日 下午7:40:20
 * @description:
 * @version: 1.0
 */

public class WorkshopBean {
	private String workshop_id;
	private String name;
	private String custom_id;

	private List<Technic> technics;

	public class Technic {
		private String name;
		private String technic_id;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTechnic_id() {
			return technic_id;
		}

		public void setTechnic_id(String technic_id) {
			this.technic_id = technic_id;
		}
	}

	public String getWorkshop_id() {
		return workshop_id;
	}

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

	public List<Technic> getTechnics() {
		return technics;
	}

	public void setTechnics(List<Technic> technics) {
		this.technics = technics;
	}

}
