package cn.guanmai.manage.bean.account;

/**
 * @author: liming
 * @Date: 2020年7月14日 下午2:51:34
 * @description:
 * @version: 1.0
 */

public class StationInfoBean {
	private String id;
	private String name;
	private Integer distribute_type;

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

	public Integer getDistribute_type() {
		return distribute_type;
	}

	public void setDistribute_type(Integer distribute_type) {
		this.distribute_type = distribute_type;
	}

}
