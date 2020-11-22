package cn.guanmai.station.bean.marketing.param;

import java.util.List;

/* 
* @author liming 
* @date Feb 21, 2019 3:21:08 PM 
* @des 创建默认营销规则的参数构造类
* @version 1.0 
*/
public class PromotionDefaultParam extends PromotionBaseParam {
	private String id;

	private List<Sku> skus;

	public class Sku {
		private String name;
		private int state;
		private String id;
		private String salemenu_name;
		private String value;
		private boolean _gm_select;
		private String label_2_name;
		private String label_1_name;

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
		 * @return the state
		 */
		public int getState() {
			return state;
		}

		/**
		 * @param state
		 *            the state to set
		 */
		public void setState(int state) {
			this.state = state;
		}

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
		 * @return the salemenu_name
		 */
		public String getSalemenu_name() {
			return salemenu_name;
		}

		/**
		 * @param salemenu_name
		 *            the salemenu_name to set
		 */
		public void setSalemenu_name(String salemenu_name) {
			this.salemenu_name = salemenu_name;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param value
		 *            the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}

		/**
		 * @return the _gm_select
		 */
		public boolean is_gm_select() {
			return _gm_select;
		}

		/**
		 * @param _gm_select
		 *            the _gm_select to set
		 */
		public void set_gm_select(boolean _gm_select) {
			this._gm_select = _gm_select;
		}

		/**
		 * @return the label_2_name
		 */
		public String getLabel_2_name() {
			return label_2_name;
		}

		/**
		 * @param label_2_name
		 *            the label_2_name to set
		 */
		public void setLabel_2_name(String label_2_name) {
			this.label_2_name = label_2_name;
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
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 修改营销活动必填的参数
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
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

}
