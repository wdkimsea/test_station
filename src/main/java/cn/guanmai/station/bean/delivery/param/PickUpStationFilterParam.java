package cn.guanmai.station.bean.delivery.param;

/**
 * @author liming
 * @date 2019年10月10日
 * @time 下午3:23:21
 * @des 接口 /station/pick_up_station/list 对应的参数
 */

public class PickUpStationFilterParam {
	private Integer business_status;
	private String district_code;
	private String area_l1;
	private String area_l2;
	private int limit = 10;
	private int offset;
	private int peek = 60;
	private String search_text;

	public Integer getBusiness_status() {
		return business_status;
	}

	public void setBusiness_status(Integer business_status) {
		this.business_status = business_status;
	}

	public String getDistrict_code() {
		return district_code;
	}

	public void setDistrict_code(String district_code) {
		this.district_code = district_code;
	}

	public String getArea_l1() {
		return area_l1;
	}

	public void setArea_l1(String area_l1) {
		this.area_l1 = area_l1;
	}

	public String getArea_l2() {
		return area_l2;
	}

	public void setArea_l2(String area_l2) {
		this.area_l2 = area_l2;
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

	public int getPeek() {
		return peek;
	}

	public void setPeek(int peek) {
		this.peek = peek;
	}

	public String getSearch_text() {
		return search_text;
	}

	public void setSearch_text(String search_text) {
		this.search_text = search_text;
	}

}
