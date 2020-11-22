package cn.guanmai.station.bean.system;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Apr 3, 2019 4:12:44 PM 
* @des 地理标签
* @version 1.0 
*/
public class AreaBean {
	@JSONField(name="city")
	private String city_name;
	private String city_id;
	private List<District> districts;

	/**
	 * @return the city_name
	 */
	public String getCity_name() {
		return city_name;
	}

	/**
	 * @param city_name
	 *            the city_name to set
	 */
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	/**
	 * @return the city_id
	 */
	public String getCity_id() {
		return city_id;
	}

	/**
	 * @param city_id
	 *            the city_id to set
	 */
	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	/**
	 * @return the districts
	 */
	public List<District> getDistricts() {
		return districts;
	}

	/**
	 * @param districts
	 *            the districts to set
	 */
	public void setDistricts(List<District> districts) {
		this.districts = districts;
	}

	public class District {
		@JSONField(name="district")
		private String district_name;
		private String district_id;
		private List<Area> areas;

		/**
		 * @return the district_name
		 */
		public String getDistrict_name() {
			return district_name;
		}

		/**
		 * @param district_name
		 *            the district_name to set
		 */
		public void setDistrict_name(String district_name) {
			this.district_name = district_name;
		}

		/**
		 * @return the district_id
		 */
		public String getDistrict_id() {
			return district_id;
		}

		/**
		 * @param district_id
		 *            the district_id to set
		 */
		public void setDistrict_id(String district_id) {
			this.district_id = district_id;
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

		public class Area {
			@JSONField(name="area")
			private String area_name;
			private String area_id;

			/**
			 * @return the area_name
			 */
			public String getArea_name() {
				return area_name;
			}

			/**
			 * @param area_name
			 *            the area_name to set
			 */
			public void setArea_name(String area_name) {
				this.area_name = area_name;
			}

			/**
			 * @return the area_id
			 */
			public String getArea_id() {
				return area_id;
			}

			/**
			 * @param area_id
			 *            the area_id to set
			 */
			public void setArea_id(String area_id) {
				this.area_id = area_id;
			}
		}
	}

}
