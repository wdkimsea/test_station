package cn.guanmai.manage.bean.custommange.param;

/**
 * @author: liming
 * @Date: 2020年8月3日 下午2:34:43
 * @description:
 * @version: 1.0
 */

public class MgStationSalesReportFilterParam {
	private String beginTime;
	private String endTime;
	private String station;
	private int type;
	private Integer export;

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getExport() {
		return export;
	}

	public void setExport(Integer export) {
		this.export = export;
	}

}
