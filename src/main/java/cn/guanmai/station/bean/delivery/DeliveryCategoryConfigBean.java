package cn.guanmai.station.bean.delivery;

import java.util.List;

/**
 * @author: liming
 * @Date: 2020年5月19日 下午4:42:48
 * @description: /delivery/category_config/get 接口对应的结果
 * @version: 1.0
 */

public class DeliveryCategoryConfigBean {
	private String id;
	private List<List<String>> category_config;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<List<String>> getCategory_config() {
		return category_config;
	}

	public void setCategory_config(List<List<String>> category_config) {
		this.category_config = category_config;
	}

}
