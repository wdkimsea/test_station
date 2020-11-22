package cn.guanmai.station.bean.order.param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liming
 * @Date: 2020年7月8日 下午5:34:44
 * @description: 订单替换商品参数,对应接口 /station/order/batch_change_skus
 * @version: 1.0
 */

public class OrderChangeSkuParam {
	private String sku_id;
	private List<OrderInfo> order_infos;

	public class OrderInfo {
		private String order_id;
		private BigDecimal quantity;
		private String resname;

		public String getOrder_id() {
			return order_id;
		}

		public void setOrder_id(String order_id) {
			this.order_id = order_id;
		}

		public BigDecimal getQuantity() {
			return quantity;
		}

		public void setQuantity(BigDecimal quantity) {
			this.quantity = quantity;
		}

		public String getResname() {
			return resname;
		}

		public void setResname(String resname) {
			this.resname = resname;
		}
	}

	private String new_sku_id;
	private int change_type;
	private BigDecimal change_quantity;
	private String spu_remark;
	private String salemenu_name;

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public List<OrderInfo> getOrder_infos() {
		return order_infos;
	}

	public void setOrder_infos(List<OrderInfo> order_infos) {
		this.order_infos = order_infos;
	}

	public String getNew_sku_id() {
		return new_sku_id;
	}

	public void setNew_sku_id(String new_sku_id) {
		this.new_sku_id = new_sku_id;
	}

	public int getChange_type() {
		return change_type;
	}

	public void setChange_type(int change_type) {
		this.change_type = change_type;
	}

	public BigDecimal getChange_quantity() {
		return change_quantity;
	}

	public void setChange_quantity(BigDecimal change_quantity) {
		this.change_quantity = change_quantity;
	}

	public String getSpu_remark() {
		return spu_remark;
	}

	public void setSpu_remark(String spu_remark) {
		this.spu_remark = spu_remark;
	}

	public String getSalemenu_name() {
		return salemenu_name;
	}

	public void setSalemenu_name(String salemenu_name) {
		this.salemenu_name = salemenu_name;
	}

}
