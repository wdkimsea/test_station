package cn.guanmai.open.bean.customer;

import java.util.List;

/* 
* @author liming 
* @date Jun 17, 2019 10:17:47 AM 
* @des 开放平台地理标签列表
* @version 1.0 
*/
public class OpenCustomerAreaBean {
	private String name;
	private String code;
	private List<AreaLevel1> area_level1;

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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the area_level1
	 */
	public List<AreaLevel1> getArea_level1() {
		return area_level1;
	}

	/**
	 * @param area_level1
	 *            the area_level1 to set
	 */
	public void setArea_level1(List<AreaLevel1> area_level1) {
		this.area_level1 = area_level1;
	}

	public class AreaLevel1 {
		private String name;
		private String code;

		private List<AreaLevel2> area_level2;

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
		 * @return the code
		 */
		public String getCode() {
			return code;
		}

		/**
		 * @param code
		 *            the code to set
		 */
		public void setCode(String code) {
			this.code = code;
		}

		/**
		 * @return the area_level2
		 */
		public List<AreaLevel2> getArea_level2() {
			return area_level2;
		}

		/**
		 * @param area_level2
		 *            the area_level2 to set
		 */
		public void setArea_level2(List<AreaLevel2> area_level2) {
			this.area_level2 = area_level2;
		}

		public class AreaLevel2 {
			private String name;
			private String code;

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
			 * @return the code
			 */
			public String getCode() {
				return code;
			}

			/**
			 * @param code
			 *            the code to set
			 */
			public void setCode(String code) {
				this.code = code;
			}

		}
	}
}
