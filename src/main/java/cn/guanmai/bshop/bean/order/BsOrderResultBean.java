package cn.guanmai.bshop.bean.order;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年6月17日 上午10:15:41
 * @description:
 * @version: 1.0
 */

public class BsOrderResultBean {
	private int code;
	private String msg;

	private Extender extender;

	public class Extender {
		private String station_id;
		private String order_id;
		private BigDecimal total_pay;

		public String getStation_id() {
			return station_id;
		}

		public void setStation_id(String station_id) {
			this.station_id = station_id;
		}

		public String getOrder_id() {
			return order_id;
		}

		public void setOrder_id(String order_id) {
			this.order_id = order_id;
		}

		public BigDecimal getTotal_pay() {
			return total_pay;
		}

		public void setTotal_pay(BigDecimal total_pay) {
			this.total_pay = total_pay;
		}
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Extender getExtender() {
		return extender;
	}

	public void setExtender(Extender extender) {
		this.extender = extender;
	}

}
