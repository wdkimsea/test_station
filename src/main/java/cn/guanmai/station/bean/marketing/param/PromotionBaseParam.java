package cn.guanmai.station.bean.marketing.param;

import java.util.List;

/* 
* @author liming 
* @date Feb 21, 2019 3:11:46 PM 
* @des 创建营销活动的基础数据封装类
* @version 1.0 
*/
public class PromotionBaseParam {
	private String name;
	private int active;
	private int show_method;
	private int sort;
	private int enable_label_2;
	private String label_1_name;

	private List<Label_2> label_2;

	public class Label_2 {
		private String name;
		private Integer sort;

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the sort
		 */
		public Integer getSort() {
			return sort;
		}

		/**
		 * @param sort
		 *            the sort to set
		 */
		public void setSort(Integer sort) {
			this.sort = sort;
		}
	}

	private String pic_url;

	private int type;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the active
	 */
	public int getActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(int active) {
		this.active = active;
	}

	/**
	 * @return the show_method
	 */
	public int getShow_method() {
		return show_method;
	}

	/**
	 * @param show_method
	 *            the show_method to set
	 */
	public void setShow_method(int show_method) {
		this.show_method = show_method;
	}

	/**
	 * @return the sort
	 */
	public int getSort() {
		return sort;
	}

	/**
	 * @param sort
	 *            the sort to set
	 */
	public void setSort(int sort) {
		this.sort = sort;
	}

	/**
	 * @return the enable_label_2
	 */
	public int getEnable_label_2() {
		return enable_label_2;
	}

	/**
	 * @param enable_label_2
	 *            the enable_label_2 to set
	 */
	public void setEnable_label_2(int enable_label_2) {
		this.enable_label_2 = enable_label_2;
	}

	/**
	 * @return the label_1_name
	 */
	public String getLabel_1_name() {
		return label_1_name;
	}

	/**
	 * @param label_1_name
	 *            the label_1_name to set
	 */
	public void setLabel_1_name(String label_1_name) {
		this.label_1_name = label_1_name;
	}

	/**
	 * @return the label_2
	 */
	public List<Label_2> getLabel_2() {
		return label_2;
	}

	/**
	 * @param label_2
	 *            the label_2 to set
	 */
	public void setLabel_2(List<Label_2> label_2) {
		this.label_2 = label_2;
	}

	/**
	 * @return the pic_url
	 */
	public String getPic_url() {
		return pic_url;
	}

	/**
	 * @param pic_url
	 *            the pic_url to set
	 */
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
}
