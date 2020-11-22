package cn.guanmai.manage.bean.custommange.result;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年7月30日 下午4:01:20
 * @description:
 * @version: 1.0
 */

public class MgSalesReportBean {
	private String sale_employee_id;
	private String sale_employee_name;
	private String sale_employee_username;

	private int order_num;
	private int all_address_num;
	private int order_address_num;

	private BigDecimal money_per_address;
	private BigDecimal money_per_order;
	private BigDecimal total_price;
	private BigDecimal total_pay_without_freight;

	public String getSale_employee_id() {
		return sale_employee_id;
	}

	public void setSale_employee_id(String sale_employee_id) {
		this.sale_employee_id = sale_employee_id;
	}

	public int getAll_address_num() {
		return all_address_num;
	}

	public void setAll_address_num(int all_address_num) {
		this.all_address_num = all_address_num;
	}

	public int getOrder_address_num() {
		return order_address_num;
	}

	public void setOrder_address_num(int order_address_num) {
		this.order_address_num = order_address_num;
	}

	public BigDecimal getTotal_price() {
		return total_price.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
	}

	public void setTotal_price(BigDecimal total_price) {
		this.total_price = total_price;
	}

	public String getSale_employee_name() {
		return sale_employee_name;
	}

	public void setSale_employee_name(String sale_employee_name) {
		this.sale_employee_name = sale_employee_name;
	}

	public String getSale_employee_username() {
		return sale_employee_username;
	}

	public void setSale_employee_username(String sale_employee_username) {
		this.sale_employee_username = sale_employee_username;
	}

	public int getOrder_num() {
		return order_num;
	}

	public void setOrder_num(int order_num) {
		this.order_num = order_num;
	}

	public BigDecimal getMoney_per_address() {
		return money_per_address.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
	}

	public void setMoney_per_address(BigDecimal money_per_address) {
		this.money_per_address = money_per_address;
	}

	public BigDecimal getMoney_per_order() {
		return money_per_order.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
	}

	public void setMoney_per_order(BigDecimal money_per_order) {
		this.money_per_order = money_per_order;
	}

	public BigDecimal getTotal_pay_without_freight() {
		return total_pay_without_freight.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
	}

	public void setTotal_pay_without_freight(BigDecimal total_pay_without_freight) {
		this.total_pay_without_freight = total_pay_without_freight;
	}

}
