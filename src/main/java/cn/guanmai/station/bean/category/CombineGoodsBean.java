package cn.guanmai.station.bean.category;

import java.util.List;

/**
 * @author: liming
 * @Date: 2020年2月17日 下午4:29:40
 * @description:
 * @version: 1.0
 */

public class CombineGoodsBean {
	private String id;
	private String name;
	private String image;
	private int state;

	private List<Salemenu> salemenus;

	public class Salemenu {
		private String salemenu_id;
		private String salemenu_name;

		public String getSalemenu_id() {
			return salemenu_id;
		}

		public void setSalemenu_id(String salemenu_id) {
			this.salemenu_id = salemenu_id;
		}

		public String getSalemenu_name() {
			return salemenu_name;
		}

		public void setSalemenu_name(String salemenu_name) {
			this.salemenu_name = salemenu_name;
		}

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public List<Salemenu> getSalemenus() {
		return salemenus;
	}

	public void setSalemenus(List<Salemenu> salemenus) {
		this.salemenus = salemenus;
	}

}
