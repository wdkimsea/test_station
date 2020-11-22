package cn.guanmai.station.bean.jingcai.param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2019年8月8日 下午4:23:15
 * @des 接口 /process/technic_flow/update_technic 对应的参数
 * @version 1.0
 */
public class TechnicFlowUpdateParam {
	private String id;
	private String name;
	private String desc;
	private String technic_id;
	private String sku_id;
	private String ingredient_id;
	private int remark_type;
	private int type;
	private boolean active;

	private List<CustomCol> custom_cols;

	public class CustomCol {
		private BigDecimal col_id;
		private BigDecimal col_param_id;

		public BigDecimal getCol_id() {
			return col_id;
		}

		public void setCol_id(BigDecimal col_id) {
			this.col_id = col_id;
		}

		public BigDecimal getCol_param_id() {
			return col_param_id;
		}

		public void setCol_param_id(BigDecimal col_param_id) {
			this.col_param_id = col_param_id;
		}

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

	/**
	 * @return the technic_id
	 */
	public String getTechnic_id() {
		return technic_id;
	}

	/**
	 * @param technic_id the technic_id to set
	 */
	public void setTechnic_id(String technic_id) {
		this.technic_id = technic_id;
	}

	/**
	 * @return the sku_id
	 */
	public String getSku_id() {
		return sku_id;
	}

	/**
	 * @param sku_id the sku_id to set
	 */
	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	/**
	 * @return the ingredient_id
	 */
	public String getIngredient_id() {
		return ingredient_id;
	}

	/**
	 * @param ingredient_id the ingredient_id to set
	 */
	public void setIngredient_id(String ingredient_id) {
		this.ingredient_id = ingredient_id;
	}

	/**
	 * @return the remark_type
	 */
	public int getRemark_type() {
		return remark_type;
	}

	/**
	 * @param remark_type the remark_type to set
	 */
	public void setRemark_type(int remark_type) {
		this.remark_type = remark_type;
	}

	public List<CustomCol> getCustom_cols() {
		return custom_cols;
	}

	public void setCustom_cols(List<CustomCol> custom_cols) {
		this.custom_cols = custom_cols;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
