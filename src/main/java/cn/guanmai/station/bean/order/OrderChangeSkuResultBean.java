package cn.guanmai.station.bean.order;

/**
 * @author: liming
 * @Date: 2020年7月9日 下午3:30:21
 * @description:
 * @version: 1.0
 */

public class OrderChangeSkuResultBean {
	private String new_sku_id;
	private String new_sku_name;
	private String err_msg;
	private String order_id;

	public String getNew_sku_id() {
		return new_sku_id;
	}

	public void setNew_sku_id(String new_sku_id) {
		this.new_sku_id = new_sku_id;
	}

	public String getNew_sku_name() {
		return new_sku_name;
	}

	public void setNew_sku_name(String new_sku_name) {
		this.new_sku_name = new_sku_name;
	}

	public String getErr_msg() {
		return err_msg;
	}

	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

}
