package cn.guanmai.open.bean.area;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/* 
* @author liming 
* @date Jun 6, 2019 10:35:40 AM 
* @todo TODO
* @version 1.0 
*/
public class AreaBean {
	private String name;
	private String code;
	@SerializedName("area")
	private List<Area> areas;

	public class Area {
		private String name;
		private String code;
		@SerializedName("street")
		private List<Street> streets;

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
		 * @return the streets
		 */
		public List<Street> getStreets() {
			return streets;
		}

		/**
		 * @param streets
		 *            the streets to set
		 */
		public void setStreets(List<Street> streets) {
			this.streets = streets;
		}

		public class Street {
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
	 * @return the areas
	 */
	public List<Area> getAreas() {
		return areas;
	}

	/**
	 * @param areas
	 *            the areas to set
	 */
	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

}
