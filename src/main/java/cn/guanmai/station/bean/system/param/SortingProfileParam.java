package cn.guanmai.station.bean.system.param;

/**
 * @author: liming
 * @Date: 2020年7月23日 下午5:54:26
 * @description:
 * @version: 1.0
 */

public class SortingProfileParam {
	private int generate_sort_num_rule = 1;
	private int sorting_product_code_type = 2;
	private int show_res_custom_code = 0;
	private int sorting_edit_lock = 0;
	private int sale_unit_sort_independence = 0;

	public int getGenerate_sort_num_rule() {
		return generate_sort_num_rule;
	}

	public void setGenerate_sort_num_rule(int generate_sort_num_rule) {
		this.generate_sort_num_rule = generate_sort_num_rule;
	}

	public int getSorting_product_code_type() {
		return sorting_product_code_type;
	}

	public void setSorting_product_code_type(int sorting_product_code_type) {
		this.sorting_product_code_type = sorting_product_code_type;
	}

	public int getShow_res_custom_code() {
		return show_res_custom_code;
	}

	public void setShow_res_custom_code(int show_res_custom_code) {
		this.show_res_custom_code = show_res_custom_code;
	}

	public int getSorting_edit_lock() {
		return sorting_edit_lock;
	}

	public void setSorting_edit_lock(int sorting_edit_lock) {
		this.sorting_edit_lock = sorting_edit_lock;
	}

	public int getSale_unit_sort_independence() {
		return sale_unit_sort_independence;
	}

	public void setSale_unit_sort_independence(int sale_unit_sort_independence) {
		this.sale_unit_sort_independence = sale_unit_sort_independence;
	}

}
