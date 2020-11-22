package cn.guanmai.station.bean.weight;

import java.util.List;

/* 
* @author liming 
* @date May 20, 2019 3:38:38 PM 
* @des 新版称重分组管理
* @version 1.0 
*/
public class WeightGroupBean {
	private String id;
	private String name;

	private List<Spu> spus;

	public class Spu {
		private String category1_id;
		private String category1_name;
		private String category2_id;
		private String category2_name;
		private String id;
		private String name;

		/**
		 * @return the category1_id
		 */
		public String getCategory1_id() {
			return category1_id;
		}

		/**
		 * @param category1_id
		 *            the category1_id to set
		 */
		public void setCategory1_id(String category1_id) {
			this.category1_id = category1_id;
		}

		/**
		 * @return the category1_name
		 */
		public String getCategory1_name() {
			return category1_name;
		}

		/**
		 * @param category1_name
		 *            the category1_name to set
		 */
		public void setCategory1_name(String category1_name) {
			this.category1_name = category1_name;
		}

		/**
		 * @return the category2_id
		 */
		public String getCategory2_id() {
			return category2_id;
		}

		/**
		 * @param category2_id
		 *            the category2_id to set
		 */
		public void setCategory2_id(String category2_id) {
			this.category2_id = category2_id;
		}

		/**
		 * @return the category2_name
		 */
		public String getCategory2_name() {
			return category2_name;
		}

		/**
		 * @param category2_name
		 *            the category2_name to set
		 */
		public void setCategory2_name(String category2_name) {
			this.category2_name = category2_name;
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
	 * @return the spus
	 */
	public List<Spu> getSpus() {
		return spus;
	}

	/**
	 * @param spus
	 *            the spus to set
	 */
	public void setSpus(List<Spu> spus) {
		this.spus = spus;
	}

}
