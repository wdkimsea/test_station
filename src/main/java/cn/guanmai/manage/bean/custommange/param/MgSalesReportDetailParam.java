package cn.guanmai.manage.bean.custommange.param;

/**
 * @author: liming
 * @Date: 2020年7月30日 下午7:52:53
 * @description:
 * @version: 1.0
 */

public class MgSalesReportDetailParam {
	private int search_date_type;
	private String begin_date;
	private String end_date;
	private String sale_employee_id;

	public int getSearch_date_type() {
		return search_date_type;
	}

	public void setSearch_date_type(int search_date_type) {
		this.search_date_type = search_date_type;
	}

	public String getBegin_date() {
		return begin_date;
	}

	public void setBegin_date(String begin_date) {
		this.begin_date = begin_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getSale_employee_id() {
		return sale_employee_id;
	}

	public void setSale_employee_id(String sale_employee_id) {
		this.sale_employee_id = sale_employee_id;
	}

}
