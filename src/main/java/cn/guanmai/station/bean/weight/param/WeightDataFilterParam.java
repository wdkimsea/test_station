package cn.guanmai.station.bean.weight.param;

import java.util.List;

/**
 * @author liming
 * @date 2019年7月30日 上午11:52:44
 * @des 新版称重软件获取称重数据参数
 * @des 接口 /weight/skus 对应的参数
 * @version 1.0
 */
public class WeightDataFilterParam {
	private String time_config_id;
	private String date;
	private int is_weight = 1;

	private List<String> spu_ids;

	private List<String> group_ids;

	private Integer out_of_group;

	/**
	 * @return the time_config_id
	 */
	public String getTime_config_id() {
		return time_config_id;
	}

	/**
	 * @param time_config_id the time_config_id to set
	 */
	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * 必填参数,参数形式 yyyy-MM-dd
	 * 
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * 是否称重商品
	 * 
	 * @return the is_weight
	 */
	public int getIs_weight() {
		return is_weight;
	}

	/**
	 * @param is_weight the is_weight to set
	 */
	public void setIs_weight(int is_weight) {
		this.is_weight = is_weight;
	}

	public List<String> getSpu_ids() {
		return spu_ids;
	}

	public void setSpu_ids(List<String> spu_ids) {
		this.spu_ids = spu_ids;
	}

	public List<String> getGroup_ids() {
		return group_ids;
	}

	public void setGroup_ids(List<String> group_ids) {
		this.group_ids = group_ids;
	}

	/**
	 * 
	 * @return the out_of_group
	 */
	public Integer getOut_of_group() {
		return out_of_group;
	}

	/**
	 * 未分组商品, 取值范围 [0,1]
	 * 
	 * @param out_of_group the out_of_group to set
	 */
	public void setOut_of_group(Integer out_of_group) {
		this.out_of_group = out_of_group;
	}

}
