package cn.guanmai.manage.bean.custommange.result;

import java.util.List;
import java.util.Map;

/**
 * @author liming
 * @date 2019年8月20日
 * @time 下午4:16:49
 * @des 商户一些基础信息,用于业务展示
 */

public class CustomerBaseInfoBean {
	private List<District> district;

	private List<Salemenu> salemenus;

	private List<String> sale_employee_ids;

	public List<String> create_employee_ids;

	public Map<String, String> station_district_code_map;

	public List<District> getDistrict() {
		return district;
	}

	public void setDistrict(List<District> district) {
		this.district = district;
	}

	public List<String> getSale_employee_ids() {
		return sale_employee_ids;
	}

	public void setSale_employee_ids(List<String> sale_employee_ids) {
		this.sale_employee_ids = sale_employee_ids;
	}

	public List<String> getCreate_employee_ids() {
		return create_employee_ids;
	}

	public void setCreate_employee_ids(List<String> create_employee_ids) {
		this.create_employee_ids = create_employee_ids;
	}

	public List<Salemenu> getSalemenus() {
		return salemenus;
	}

	public void setSalemenus(List<Salemenu> salemenus) {
		this.salemenus = salemenus;
	}

	public Map<String, String> getStation_district_code_map() {
		return station_district_code_map;
	}

	public void setStation_district_code_map(Map<String, String> station_district_code_map) {
		this.station_district_code_map = station_district_code_map;
	}

	public class District {
		private String city_name;
		private String city_code;

		private List<Area> areas;

		public String getCity_name() {
			return city_name;
		}

		public void setCity_name(String city_name) {
			this.city_name = city_name;
		}

		public String getCity_code() {
			return city_code;
		}

		public void setCity_code(String city_code) {
			this.city_code = city_code;
		}

		public List<Area> getAreas() {
			return areas;
		}

		public void setAreas(List<Area> areas) {
			this.areas = areas;
		}

		public class Area {
			private String area_name;
			private String area_code;

			private List<Street> streets;

			public String getArea_name() {
				return area_name;
			}

			public void setArea_name(String area_name) {
				this.area_name = area_name;
			}

			public String getArea_code() {
				return area_code;
			}

			public void setArea_code(String area_code) {
				this.area_code = area_code;
			}

			public List<Street> getStreets() {
				return streets;
			}

			public void setStreets(List<Street> streets) {
				this.streets = streets;
			}

			public class Street {
				private String street_name;
				private String street_code;

				public String getStreet_name() {
					return street_name;
				}

				public void setStreet_name(String street_name) {
					this.street_name = street_name;
				}

				public String getStreet_code() {
					return street_code;
				}

				public void setStreet_code(String street_code) {
					this.street_code = street_code;
				}
			}
		}

	}

	public class Salemenu {
		private String station_id;
		private List<String> distribute_city_ids;
		private List<String> salemenu_ids;

		public String getStation_id() {
			return station_id;
		}

		public void setStation_id(String station_id) {
			this.station_id = station_id;
		}

		public List<String> getDistribute_city_ids() {
			return distribute_city_ids;
		}

		public void setDistribute_city_ids(List<String> distribute_city_ids) {
			this.distribute_city_ids = distribute_city_ids;
		}

		public List<String> getSalemenu_ids() {
			return salemenu_ids;
		}

		public void setSalemenu_ids(List<String> salemenu_ids) {
			this.salemenu_ids = salemenu_ids;
		}
	}
}
