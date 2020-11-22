package cn.guanmai.open.bean.stock;

import java.util.List;

/**
 * @author liming
 * @date 2019年10月21日
 * @time 下午2:30:58
 * @des TODO
 */

public class OpenSupplierDetailBean {
	private String supplier_id;
	private String supplier_name;
	private String supplier_no;
	private String supplier_phone;
	private String pay_method;
	private String company_address;
	private String company_name;
	private String location_lat;
	private String location_lon;
	private List<Category2> category2;

	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public String getSupplier_no() {
		return supplier_no;
	}

	public void setSupplier_no(String supplier_no) {
		this.supplier_no = supplier_no;
	}

	public String getPay_method() {
		return pay_method;
	}

	public void setPay_method(String pay_method) {
		this.pay_method = pay_method;
	}

	public String getSupplier_phone() {
		return supplier_phone;
	}

	public void setSupplier_phone(String supplier_phone) {
		this.supplier_phone = supplier_phone;
	}

	public String getCompany_address() {
		return company_address;
	}

	public void setCompany_address(String company_address) {
		this.company_address = company_address;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getLocation_lat() {
		return location_lat;
	}

	public void setLocation_lat(String location_lat) {
		this.location_lat = location_lat;
	}

	public String getLocation_lon() {
		return location_lon;
	}

	public void setLocation_lon(String location_lon) {
		this.location_lon = location_lon;
	}

	public class Category2 {
		private String category2_id;
		private String category2_name;

		public String getCategory2_id() {
			return category2_id;
		}

		public void setCategory2_id(String category2_id) {
			this.category2_id = category2_id;
		}

		public String getCategory2_name() {
			return category2_name;
		}

		public void setCategory2_name(String category2_name) {
			this.category2_name = category2_name;
		}

	}

	public List<Category2> getCategory2() {
		return category2;
	}

	public void setCategory2(List<Category2> category2) {
		this.category2 = category2;
	}

}
