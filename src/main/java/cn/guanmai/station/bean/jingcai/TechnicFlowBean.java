package cn.guanmai.station.bean.jingcai;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2020年04月28日 下午3:10:17
 * @des 接口 /process/technic_flow/get 对应的结果
 * @version 2.0
 */
public class TechnicFlowBean {
	private String id;
	private String name;
	private String technic_id;
	private int type;
	private String desc;
	private boolean active;

	private List<CustomCol> custom_cols;

	private List<CustomColParam> custom_col_params;

	public class CustomCol {
		private String col_id;
		private String col_param_id;

		public String getCol_id() {
			return col_id;
		}

		public void setCol_id(String col_id) {
			this.col_id = col_id;
		}

		public String getCol_param_id() {
			return col_param_id;
		}

		public void setCol_param_id(String col_param_id) {
			this.col_param_id = col_param_id;
		}
	}

	public class CustomColParam {
		private String col_name;
		private BigDecimal param_id;
		private String param_name;
		private BigDecimal col_id;

		public String getCol_name() {
			return col_name;
		}

		public void setCol_name(String col_name) {
			this.col_name = col_name;
		}

		public String getParam_name() {
			return param_name;
		}

		public void setParam_name(String param_name) {
			this.param_name = param_name;
		}

		public BigDecimal getParam_id() {
			return param_id;
		}

		public void setParam_id(BigDecimal param_id) {
			this.param_id = param_id;
		}

		public BigDecimal getCol_id() {
			return col_id;
		}

		public void setCol_id(BigDecimal col_id) {
			this.col_id = col_id;
		}

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTechnic_id() {
		return technic_id;
	}

	public void setTechnic_id(String technic_id) {
		this.technic_id = technic_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<CustomCol> getCustom_cols() {
		return custom_cols;
	}

	public void setCustom_cols(List<CustomCol> custom_cols) {
		this.custom_cols = custom_cols;
	}

	public List<CustomColParam> getCustom_col_params() {
		return custom_col_params;
	}

	public void setCustom_col_params(List<CustomColParam> custom_col_params) {
		this.custom_col_params = custom_col_params;
	}

}
