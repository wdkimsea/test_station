package cn.guanmai.station.bean.invoicing;

/**
 * Created by yangjinhai on 2019/8/8.
 */
public class TransferSheetBean {
	private String create_time;
	private String creator;
	private String sheet_no;
	private int status;

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getSheet_no() {
		return sheet_no;
	}

	public void setSheet_no(String sheet_no) {
		this.sheet_no = sheet_no;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
