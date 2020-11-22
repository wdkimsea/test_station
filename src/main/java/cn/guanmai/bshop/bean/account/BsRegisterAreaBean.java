package cn.guanmai.bshop.bean.account;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author: liming
 * @Date: 2020年6月18日 下午4:53:09
 * @description:
 * @version: 1.0
 */

public class BsRegisterAreaBean {
	private String city_id;
	private String name;

	@JSONField(name="area_level1")
	private List<AreaLevel1> area_level1_list;

	public class AreaLevel1 {
		private String area1_id;
		private String area1_name;
		private String district_code;

		@JSONField(name="area_level2")
		private List<AreaLevel2> area_level2_list;

		public class AreaLevel2 {
			private String area2_id;
			private String area2_name;

			public String getArea2_id() {
				return area2_id;
			}

			public void setArea2_id(String area2_id) {
				this.area2_id = area2_id;
			}

			public String getArea2_name() {
				return area2_name;
			}

			public void setArea2_name(String area2_name) {
				this.area2_name = area2_name;
			}
		}

		public String getArea1_id() {
			return area1_id;
		}

		public void setArea1_id(String area1_id) {
			this.area1_id = area1_id;
		}

		public String getArea1_name() {
			return area1_name;
		}

		public void setArea1_name(String area1_name) {
			this.area1_name = area1_name;
		}

		public String getDistrict_code() {
			return district_code;
		}

		public void setDistrict_code(String district_code) {
			this.district_code = district_code;
		}

		public List<AreaLevel2> getArea_level2_list() {
			return area_level2_list;
		}

		public void setArea_level2_list(List<AreaLevel2> area_level2_list) {
			this.area_level2_list = area_level2_list;
		}

	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AreaLevel1> getArea_level1_list() {
		return area_level1_list;
	}

	public void setArea_level1_list(List<AreaLevel1> area_level1_list) {
		this.area_level1_list = area_level1_list;
	}

}
