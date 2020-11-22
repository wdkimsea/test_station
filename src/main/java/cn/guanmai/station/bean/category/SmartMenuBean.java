package cn.guanmai.station.bean.category;

/**
 * @author: liming
 * @Date: 2020年3月3日 上午10:43:06
 * @description: 智能菜单列表搜索
 * @version: 1.0
 */

public class SmartMenuBean {
	private String creator;
	private String name;
	private String id;
	private int sku_num;
	private String create_time;

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSku_num() {
		return sku_num;
	}

	public void setSku_num(int sku_num) {
		this.sku_num = sku_num;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}
