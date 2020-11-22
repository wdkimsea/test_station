package cn.guanmai.station.bean.category;

/**
 * @author: liming
 * @Date: 2020年3月11日 上午11:00:21
 * @description: 营销活动拉取的销售SKU接口
 * @version: 1.0
 */

public class SkuPromotionBean {
	private int state;
	private String salemenu_name;
	private String name;
	private String id;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getSalemenu_name() {
		return salemenu_name;
	}

	public void setSalemenu_name(String salemenu_name) {
		this.salemenu_name = salemenu_name;
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

}
