package cn.guanmai.station.bean.order.param;

import java.util.List;

/**
 * @author: liming
 * @Date: 2020年7月9日 上午10:59:54
 * @description:
 * @version: 1.0
 */

public class OrderDeleteSkuParam {
	private String sku_id;
	private List<String> order_ids;

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public List<String> getOrder_ids() {
		return order_ids;
	}

	public void setOrder_ids(List<String> order_ids) {
		this.order_ids = order_ids;
	}

}
