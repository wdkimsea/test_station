package cn.guanmai.station.bean.category.param;

/* 
* @author liming 
* @date Nov 8, 2018 3:51:24 PM 
* @des 过滤搜索报价单的参数
* @version 1.0 
*/
public class SalemenuFilterParam {
	private String time_config_id;
	private Integer type;
	private Integer is_active;
	private String q;
	private int with_sku_num;

	/**
	 * 报价单ID
	 * 
	 * @return the time_config_id
	 */
	public String getTime_config_id() {
		return time_config_id;
	}

	/**
	 * 报价单ID
	 * 
	 * @param time_config_id the time_config_id to set
	 */
	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	/**
	 * 类型 4为自售单,2为代售单
	 * 
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 类型 4为自售单,2为代售单
	 * 
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 状态 0为未激活,1为激活
	 * 
	 * @return the is_active
	 */
	public Integer getIs_active() {
		return is_active;
	}

	/**
	 * 状态 0为未激活,1为激活
	 * 
	 * @param is_active the is_active to set
	 */
	public void setIs_active(Integer is_active) {
		this.is_active = is_active;
	}

	/**
	 * 输入的搜索关键字
	 * 
	 * @return the q
	 */
	public String getQ() {
		return q;
	}

	/**
	 * 输入的搜索关键字
	 * 
	 * @param q the q to set
	 */
	public void setQ(String q) {
		this.q = q;
	}

	public int getWith_sku_num() {
		return with_sku_num;
	}

	public void setWith_sku_num(int with_sku_num) {
		this.with_sku_num = with_sku_num;
	}

	public SalemenuFilterParam() {
	}

	public SalemenuFilterParam(String time_config_id, Integer type, Integer is_active, String q) {
		super();
		this.time_config_id = time_config_id;
		this.type = type;
		this.is_active = is_active;
		this.q = q;
	}
}
