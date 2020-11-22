package cn.guanmai.station.bean.delivery.param;

/**
 * @author: liming
 * @Date: 2020年5月11日 下午2:44:21
 * @description:
 * @version: 1.0
 */

public class DeliverySettingParam {
	private String order_id;
	private int sync_quantity_from;
	private int sync_add_sku;
	private int sync_del_order;
	private int sync_del_sku;

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public int getSync_quantity_from() {
		return sync_quantity_from;
	}

	public void setSync_quantity_from(int sync_quantity_from) {
		this.sync_quantity_from = sync_quantity_from;
	}

	public int getSync_add_sku() {
		return sync_add_sku;
	}

	public void setSync_add_sku(int sync_add_sku) {
		this.sync_add_sku = sync_add_sku;
	}

	public int getSync_del_order() {
		return sync_del_order;
	}

	public void setSync_del_order(int sync_del_order) {
		this.sync_del_order = sync_del_order;
	}

	public int getSync_del_sku() {
		return sync_del_sku;
	}

	public void setSync_del_sku(int sync_del_sku) {
		this.sync_del_sku = sync_del_sku;
	}

}
