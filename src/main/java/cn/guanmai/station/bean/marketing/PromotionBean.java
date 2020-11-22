package cn.guanmai.station.bean.marketing;

/* 
* @author liming 
* @date Feb 21, 2019 10:50:24 AM 
* @des 营销活动列表属性封装类
* @version 1.0 
*/
public class PromotionBean {
	private Integer active;
	private String create_time;
	private String id;
	private String name;
	private String operator;
	private Integer show_method;
	private Integer sku_nums;
	private Integer type;
	private int valid_sku_nums;

	/**
	 * @return the active
	 */
	public Integer getActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(Integer active) {
		this.active = active;
	}

	/**
	 * @return the create_time
	 */
	public String getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time
	 *            the create_time to set
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
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
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @return the show_method
	 */
	public Integer getShow_method() {
		return show_method;
	}

	/**
	 * @param show_method
	 *            the show_method to set
	 */
	public void setShow_method(Integer show_method) {
		this.show_method = show_method;
	}

	/**
	 * @return the sku_nums
	 */
	public Integer getSku_nums() {
		return sku_nums;
	}

	/**
	 * @param sku_nums
	 *            the sku_nums to set
	 */
	public void setSku_nums(Integer sku_nums) {
		this.sku_nums = sku_nums;
	}

	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @return the valid_sku_nums
	 */
	public int getValid_sku_nums() {
		return valid_sku_nums;
	}

	/**
	 * @param valid_sku_nums
	 *            the valid_sku_nums to set
	 */
	public void setValid_sku_nums(int valid_sku_nums) {
		this.valid_sku_nums = valid_sku_nums;
	}

}
