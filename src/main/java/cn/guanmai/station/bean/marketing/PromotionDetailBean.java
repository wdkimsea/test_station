package cn.guanmai.station.bean.marketing;

import java.util.List;

/* 
* @author liming 
* @date Feb 21, 2019 11:15:31 AM 
* @des 营销活动详细信息类
* @version 1.0 
*/
public class PromotionDetailBean {
	private Integer active;
	private String cms_key;
	private List<Label_2> label_2;

	public class Label_2 {
		private String id;
		private String name;
		private Integer sort;

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

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

	private String name;
	private String pic_url;
	private int show_method;

	private List<Sku> skus;

	public class Sku {
		private String id;
		private String name;

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

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
	}

	private int type;

	/**
	 * @return the active
	 */
	public Integer getActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(Integer active) {
		this.active = active;
	}

	/**
	 * @return the cms_key
	 */
	public String getCms_key() {
		return cms_key;
	}

	/**
	 * @param cms_key
	 *            the cms_key to set
	 */
	public void setCms_key(String cms_key) {
		this.cms_key = cms_key;
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
	 * @return the skus
	 */
	public List<Sku> getSkus() {
		return skus;
	}

	/**
	 * @param skus
	 *            the skus to set
	 */
	public void setSkus(List<Sku> skus) {
		this.skus = skus;
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
