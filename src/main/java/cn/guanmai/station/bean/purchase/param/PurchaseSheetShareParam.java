package cn.guanmai.station.bean.purchase.param;

/**
 * @author liming
 * @date 2019年11月11日
 * @time 上午10:58:38
 * @des TODO
 */

public class PurchaseSheetShareParam {
	private String sheet_no;
	private String token;
	private String __trace_group_id;
	private String station_id;

	public String getSheet_no() {
		return sheet_no;
	}

	public void setSheet_no(String sheet_no) {
		this.sheet_no = sheet_no;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String get__trace_group_id() {
		return __trace_group_id;
	}

	public void set__trace_group_id(String __trace_group_id) {
		this.__trace_group_id = __trace_group_id;
	}

	public String getStation_id() {
		return station_id;
	}

	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

}
