package cn.guanmai.open.bean.stock;

/**
 * @author liming
 * @date 2019年10月22日
 * @time 下午7:06:21
 * @des TODO
 */

public class OpenStockOutSheetBean {
	private String out_stock_sheet_id;
	private String create_date;
	private String customer_id;
	private String customer_name;
	private String status;
	private String out_stock_sheet_type;

	public String getOut_stock_sheet_id() {
		return out_stock_sheet_id;
	}

	public void setOut_stock_sheet_id(String out_stock_sheet_id) {
		this.out_stock_sheet_id = out_stock_sheet_id;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOut_stock_sheet_type() {
		return out_stock_sheet_type;
	}

	public void setOut_stock_sheet_type(String out_stock_sheet_type) {
		this.out_stock_sheet_type = out_stock_sheet_type;
	}

}
