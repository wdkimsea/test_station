package cn.guanmai.manage.bean.custommange.param;

import java.util.List;

/**
 * @author: liming
 * @Date: 2020年7月30日 下午3:50:52
 * @description:
 * @version: 1.0
 */

public class MgSalesReportFiterParam {
	private int search_date_type;
	private String begin_date;
	private String end_date;
	private Integer export;
	private List<Integer> role_ids;
	private List<Integer> sale_employee_ids;
	private String sort_type;
	private int sort_desc;
	private int limit;
	private int offset;

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

	public Integer getExport() {
		return export;
	}

	public void setExport(Integer export) {
		this.export = export;
	}

	public List<Integer> getRole_ids() {
		return role_ids;
	}

	public void setRole_ids(List<Integer> role_ids) {
		this.role_ids = role_ids;
	}

	public List<Integer> getSale_employee_ids() {
		return sale_employee_ids;
	}

	public void setSale_employee_ids(List<Integer> sale_employee_ids) {
		this.sale_employee_ids = sale_employee_ids;
	}

	public String getSort_type() {
		return sort_type;
	}

	public void setSort_type(String sort_type) {
		this.sort_type = sort_type;
	}

	public int getSort_desc() {
		return sort_desc;
	}

	public void setSort_desc(int sort_desc) {
		this.sort_desc = sort_desc;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

}
