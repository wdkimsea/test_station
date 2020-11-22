package cn.guanmai.station.bean.category.param;

import java.util.List;

/**
 * @author: liming
 * @Date: 2020年3月11日 下午2:27:58
 * @description: 新建、修改智能菜单参数
 * @version: 1.0
 */

public class SmartMenuParam {
	private String id; // 修改智能菜单特有参数
	private String name;
	private List<String> sku_ids;
	private List<String> combine_good_ids;

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

	public List<String> getSku_ids() {
		return sku_ids;
	}

	public void setSku_ids(List<String> sku_ids) {
		this.sku_ids = sku_ids;
	}

	public List<String> getCombine_good_ids() {
		return combine_good_ids;
	}

	public void setCombine_good_ids(List<String> combine_good_ids) {
		this.combine_good_ids = combine_good_ids;
	}

}
