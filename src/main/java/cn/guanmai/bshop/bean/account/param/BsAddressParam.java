package cn.guanmai.bshop.bean.account.param;

/**
 * @author: liming
 * @Date: 2020年6月18日 下午5:03:30
 * @description:
 * @version: 1.0
 */

public class BsAddressParam {
	private String resname;
	private String name;
	private String telephone;
	private String area;
	private String addr_detail = "广东省深圳市南山区粤海街道高新中三道9号环球数码大厦";
	private String lat = "22.54331";
	private String lng = "113.93671";
	private String map_address = "广东省深圳市南山区粤海街道高新中三道9号环球数码大厦";

	public String getResname() {
		return resname;
	}

	public void setResname(String resname) {
		this.resname = resname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddr_detail() {
		return addr_detail;
	}

	public void setAddr_detail(String addr_detail) {
		this.addr_detail = addr_detail;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getMap_address() {
		return map_address;
	}

	public void setMap_address(String map_address) {
		this.map_address = map_address;
	}

}
