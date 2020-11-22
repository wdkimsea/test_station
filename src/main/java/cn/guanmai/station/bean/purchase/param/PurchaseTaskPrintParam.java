package cn.guanmai.station.bean.purchase.param;

/**
 * @author liming
 * @date 2019年8月14日
 * @time 下午8:03:48
 * @des TODO
 */

public class PurchaseTaskPrintParam {
	private String begin_time;
	private String end_time;
	private String print_what;
	private int q_type;
	private int is_print;

	public String getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getPrint_what() {
		return print_what;
	}

	public void setPrint_what(String print_what) {
		this.print_what = print_what;
	}

	public int getQ_type() {
		return q_type;
	}

	public void setQ_type(int q_type) {
		this.q_type = q_type;
	}

	public int getIs_print() {
		return is_print;
	}

	public void setIs_print(int is_print) {
		this.is_print = is_print;
	}
}
