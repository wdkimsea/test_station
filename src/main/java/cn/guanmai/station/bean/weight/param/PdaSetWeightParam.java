package cn.guanmai.station.bean.weight.param;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONArray;

/**
 * @author liming
 * @date 2019年7月31日 下午5:46:02
 * @des 接口 /weight/pda/sort/set_weight 对应的参数, PDA 分拣
 * @version 1.0
 */
public class PdaSetWeightParam {
	private String order_id;
	private String sku_id;
	private BigDecimal detail_id;
	private JSONArray package_ids;
	private int sort_way;
	private BigDecimal weighting_quantity;
	private String employee_id;

	/**
	 * @return the order_id
	 */
	public String getOrder_id() {
		return order_id;
	}

	/**
	 * @param order_id the order_id to set
	 */
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
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

	public BigDecimal getDetail_id() {
		return detail_id;
	}

	public void setDetail_id(BigDecimal detail_id) {
		this.detail_id = detail_id;
	}

	/**
	 * @return the package_ids
	 */
	public JSONArray getPackage_ids() {
		return package_ids;
	}

	/**
	 * @param package_ids the package_ids to set
	 */
	public void setPackage_ids(JSONArray package_ids) {
		this.package_ids = package_ids;
	}

	/**
	 * @return the sort_way
	 */
	public int getSort_way() {
		return sort_way;
	}

	/**
	 * @param sort_way the sort_way to set
	 */
	public void setSort_way(int sort_way) {
		this.sort_way = sort_way;
	}

	/**
	 * @return the weighting_quantity
	 */
	public BigDecimal getWeighting_quantity() {
		return weighting_quantity;
	}

	/**
	 * @param weighting_quantity the weighting_quantity to set
	 */
	public void setWeighting_quantity(BigDecimal weighting_quantity) {
		this.weighting_quantity = weighting_quantity;
	}

	/**
	 * @return the employee_id
	 */
	public String getEmployee_id() {
		return employee_id;
	}

	/**
	 * @param employee_id the employee_id to set
	 */
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
}
