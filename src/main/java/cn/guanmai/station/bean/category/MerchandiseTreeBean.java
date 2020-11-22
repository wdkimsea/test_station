package cn.guanmai.station.bean.category;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date May 28, 2019 5:39:39 PM 
* @des 站点分类树
* @version 1.0 
*/
public class MerchandiseTreeBean {
	@JSONField(name = "id")
	private String category1_id;
	@JSONField(name = "name")
	private String category1_name;

	@JSONField(name = "chtree")
	public List<Category2> category2s;

	public class Category2 {
		@JSONField(name = "id")
		private String category2_id;
		@JSONField(name = "name")
		private String category2_name;
		private String upstream_id;

		@JSONField(name = "chtree")
		public List<Pinlei> pinleis;

		public class Pinlei {
			@JSONField(name = "id")
			private String pinlei_id;
			@JSONField(name = "name")
			private String pinlei_name;
			private String upstream_id;

			@JSONField(name = "chtree")
			public List<Spu> spus;

			public class Spu {
				@JSONField(name = "id")
				private String spu_id;
				@JSONField(name = "name")
				private String spu_name;

				/**
				 * @return the spu_id
				 */
				public String getSpu_id() {
					return spu_id;
				}

				/**
				 * @param spu_id the spu_id to set
				 */
				public void setSpu_id(String spu_id) {
					this.spu_id = spu_id;
				}

				/**
				 * @return the spu_name
				 */
				public String getSpu_name() {
					return spu_name;
				}

				/**
				 * @param spu_name the spu_name to set
				 */
				public void setSpu_name(String spu_name) {
					this.spu_name = spu_name;
				}
			}

			/**
			 * @return the pinlei_id
			 */
			public String getPinlei_id() {
				return pinlei_id;
			}

			/**
			 * @param pinlei_id the pinlei_id to set
			 */
			public void setPinlei_id(String pinlei_id) {
				this.pinlei_id = pinlei_id;
			}

			/**
			 * @return the pinlei_name
			 */
			public String getPinlei_name() {
				return pinlei_name;
			}

			/**
			 * @param pinlei_name the pinlei_name to set
			 */
			public void setPinlei_name(String pinlei_name) {
				this.pinlei_name = pinlei_name;
			}

			/**
			 * @return the upstream_id
			 */
			public String getUpstream_id() {
				return upstream_id;
			}

			/**
			 * @param upstream_id the upstream_id to set
			 */
			public void setUpstream_id(String upstream_id) {
				this.upstream_id = upstream_id;
			}

			/**
			 * @return the spus
			 */
			public List<Spu> getSpus() {
				return spus;
			}

			/**
			 * @param spus the spus to set
			 */
			public void setSpus(List<Spu> spus) {
				this.spus = spus;
			}
		}

		/**
		 * @return the category2_id
		 */
		public String getCategory2_id() {
			return category2_id;
		}

		/**
		 * @param category2_id the category2_id to set
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
		 * @param category2_name the category2_name to set
		 */
		public void setCategory2_name(String category2_name) {
			this.category2_name = category2_name;
		}

		/**
		 * @return the upstream_id
		 */
		public String getUpstream_id() {
			return upstream_id;
		}

		/**
		 * @param upstream_id the upstream_id to set
		 */
		public void setUpstream_id(String upstream_id) {
			this.upstream_id = upstream_id;
		}

		/**
		 * @return the pinleis
		 */
		public List<Pinlei> getPinleis() {
			return pinleis;
		}

		/**
		 * @param pinleis the pinleis to set
		 */
		public void setPinleis(List<Pinlei> pinleis) {
			this.pinleis = pinleis;
		}
	}

	/**
	 * @return the category1_id
	 */
	public String getCategory1_id() {
		return category1_id;
	}

	/**
	 * @param category1_id the category1_id to set
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
	 * @param category1_name the category1_name to set
	 */
	public void setCategory1_name(String category1_name) {
		this.category1_name = category1_name;
	}

	/**
	 * @return the category2s
	 */
	public List<Category2> getCategory2s() {
		return category2s;
	}

	/**
	 * @param category2s the category2s to set
	 */
	public void setCategory2s(List<Category2> category2s) {
		this.category2s = category2s;
	}

}
