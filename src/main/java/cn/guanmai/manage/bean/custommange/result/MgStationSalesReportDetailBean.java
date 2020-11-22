package cn.guanmai.manage.bean.custommange.result;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author: liming
 * @Date: 2020年8月3日 下午2:56:18
 * @description:
 * @version: 1.0
 */

public class MgStationSalesReportDetailBean {
	private SaleEmployeeInfo saleEmployeeInfo;
	private Map<String, CustomerInfo> customerInfo;

	public class SaleEmployeeInfo {
		private Integer customer_num;
		private int order_num;
		private BigDecimal price;

		public Integer getCustomer_num() {
			return customer_num;
		}

		public void setCustomer_num(Integer customer_num) {
			this.customer_num = customer_num;
		}

		public int getOrder_num() {
			return order_num;
		}

		public void setOrder_num(int order_num) {
			this.order_num = order_num;
		}

		public BigDecimal getPrice() {
			return price.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
		}

	}

	public class CustomerInfo {
		private String SID;
		private String resname;
		private int order_num;
		private BigDecimal total_price;

		public String getSID() {
			return SID;
		}

		public void setSID(String sID) {
			SID = sID;
		}

		public String getResname() {
			return resname;
		}

		public void setResname(String resname) {
			this.resname = resname;
		}

		public int getOrder_num() {
			return order_num;
		}

		public void setOrder_num(int order_num) {
			this.order_num = order_num;
		}

		public BigDecimal getTotal_price() {
			return total_price.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
		}

		public void setTotal_price(BigDecimal total_price) {
			this.total_price = total_price;
		}
	}

	public SaleEmployeeInfo getSaleEmployeeInfo() {
		return saleEmployeeInfo;
	}

	public void setSaleEmployeeInfo(SaleEmployeeInfo saleEmployeeInfo) {
		this.saleEmployeeInfo = saleEmployeeInfo;
	}

	public Map<String, CustomerInfo> getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(Map<String, CustomerInfo> customerInfo) {
		this.customerInfo = customerInfo;
	}

}
