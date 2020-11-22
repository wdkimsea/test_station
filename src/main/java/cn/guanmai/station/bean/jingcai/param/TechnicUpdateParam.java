package cn.guanmai.station.bean.jingcai.param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2019年8月7日 上午10:15:16
 * @des 接口 /process/technic/create 对应的参数
 * @version 1.0
 */
public class TechnicUpdateParam {
	private String id;
	private String name;
	private String custom_id;
	private BigDecimal default_role;
	private String desc;
	private List<CustomCol> custom_cols;
	private String technic_category_id;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 工艺名称
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the custom_id
	 */
	public String getCustom_id() {
		return custom_id;
	}

	/**
	 * 工艺编号
	 * 
	 * @param custom_id the custom_id to set
	 */
	public void setCustom_id(String custom_id) {
		this.custom_id = custom_id;
	}

	/**
	 * @return the default_role
	 */
	public BigDecimal getDefault_role() {
		return default_role;
	}

	/**
	 * 默认操作角色ID
	 * 
	 * @param default_role the default_role to set
	 */
	public void setDefault_role(BigDecimal default_role) {
		this.default_role = default_role;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public class CustomCol {
		private String col_id;
		private String col_name;
		private List<Param> params;

		public class Param {
			private String param_id;
			private String param_name;

			public String getParam_name() {
				return param_name;
			}

			public void setParam_name(String param_name) {
				this.param_name = param_name;
			}

			public String getParam_id() {
				return param_id;
			}

			public void setParam_id(String param_id) {
				this.param_id = param_id;
			}

		}

		public String getCol_id() {
			return col_id;
		}

		public void setCol_id(String col_id) {
			this.col_id = col_id;
		}

		public String getCol_name() {
			return col_name;
		}

		public void setCol_name(String col_name) {
			this.col_name = col_name;
		}

		public List<Param> getParams() {
			return params;
		}

		public void setParams(List<Param> params) {
			this.params = params;
		}

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<CustomCol> getCustom_cols() {
		return custom_cols;
	}

	public void setCustom_cols(List<CustomCol> custom_cols) {
		this.custom_cols = custom_cols;
	}

	public String getTechnic_category_id() {
		return technic_category_id;
	}

	public void setTechnic_category_id(String technic_category_id) {
		this.technic_category_id = technic_category_id;
	}

}
