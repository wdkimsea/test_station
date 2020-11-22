package cn.guanmai.station.bean.invoicing;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Apr 24, 2019 5:01:10 PM 
* @des 供应商列表展示的信息
* @version 1.0 
*/
public class SupplierBean {
	private String supplier_id;
	private String customer_id;
	private String name;
	private String phone;
	
	@JSONField(name="merchandise")
	private List<String> merchandises;

	/**
	 * @return the supplier_id
	 */
	public String getSupplier_id() {
		return supplier_id;
	}

	/**
	 * @param supplier_id the supplier_id to set
	 */
	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	/**
	 * @return the customer_id
	 */
	public String getCustomer_id() {
		return customer_id;
	}

	/**
	 * @param customer_id the customer_id to set
	 */
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<String> getMerchandises() {
		return merchandises;
	}

	public void setMerchandises(List<String> merchandises) {
		this.merchandises = merchandises;
	}

}
