package cn.guanmai.station.bean.jingcai;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2020年4月26日 上午10:23:20
 * @des
 * @version 1.0
 */
public class TechnicBean {
	private String id;
	private String name;
	private String custom_id;
	private BigDecimal default_role;
	private String desc;
	private List<CustomColParam> custom_col_params;
	private List<CustomCol> custom_cols;
	private String technic_category_id;

	// 修改时带的参数,强制修改 1
	private Integer force;

	public String getId() {
		return id;
	}

	/**
	 * 修改的时候才带的参数
	 * 
	 * @param id
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

	public class CustomCol {
		private BigDecimal col_id;
		private String col_name;

		private List<Param> params;

		public class Param {
			private BigDecimal param_id;
			private String param_name;

			public BigDecimal getParam_id() {
				return param_id;
			}

			public void setParam_id(BigDecimal param_id) {
				this.param_id = param_id;
			}

			public String getParam_name() {
				return param_name;
			}

			public void setParam_name(String param_name) {
				this.param_name = param_name;
			}
		}

		public BigDecimal getCol_id() {
			return col_id;
		}

		public void setCol_id(BigDecimal col_id) {
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

	public class CustomColParam {
		private BigDecimal col_id;
		private String col_name;

		private List<Param> params;

		public class Param {
			private BigDecimal param_id;
			private String param_name;

			public BigDecimal getParam_id() {
				return param_id;
			}

			public void setParam_id(BigDecimal param_id) {
				this.param_id = param_id;
			}

			public String getParam_name() {
				return param_name;
			}

			public void setParam_name(String param_name) {
				this.param_name = param_name;
			}
		}

		public BigDecimal getCol_id() {
			return col_id;
		}

		public void setCol_id(BigDecimal col_id) {
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

	public List<CustomColParam> getCustom_col_params() {
		return custom_col_params;
	}

	public void setCustom_col_params(List<CustomColParam> custom_col_params) {
		this.custom_col_params = custom_col_params;
	}

	public Integer getForce() {
		return force;
	}

	/**
	 * 修改工艺带的参数,强制修改 1
	 * 
	 * @param force
	 */
	public void setForce(Integer force) {
		this.force = force;
	}

}
