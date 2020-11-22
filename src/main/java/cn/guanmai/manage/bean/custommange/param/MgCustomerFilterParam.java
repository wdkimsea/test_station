package cn.guanmai.manage.bean.custommange.param;

/* 
* @author liming 
* @date Jan 14, 2019 5:15:30 PM 
* @des /custommanage/list 接口查询过滤商户参数
* @version 1.0 
*/
public class MgCustomerFilterParam {
	private String stationMenu;
	private Integer page;
	private String city;
	private String firstMenu;
	private String secondMenu;

	private String crtManager;
	private String saleManager;
	private String saleMenu;
	private Integer num;
	private Integer check_out;
	private Integer settle_way;
	private String search_text;
	private Integer export;

	public String getStationMenu() {
		return stationMenu;
	}

	public void setStationMenu(String stationMenu) {
		this.stationMenu = stationMenu;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getFirstMenu() {
		return firstMenu;
	}

	public void setFirstMenu(String firstMenu) {
		this.firstMenu = firstMenu;
	}

	public String getSecondMenu() {
		return secondMenu;
	}

	public void setSecondMenu(String secondMenu) {
		this.secondMenu = secondMenu;
	}

	public String getCrtManager() {
		return crtManager;
	}

	public void setCrtManager(String crtManager) {
		this.crtManager = crtManager;
	}

	public String getSaleManager() {
		return saleManager;
	}

	public void setSaleManager(String saleManager) {
		this.saleManager = saleManager;
	}

	public String getSaleMenu() {
		return saleMenu;
	}

	public void setSaleMenu(String saleMenu) {
		this.saleMenu = saleMenu;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getCheck_out() {
		return check_out;
	}

	public void setCheck_out(Integer check_out) {
		this.check_out = check_out;
	}

	public Integer getSettle_way() {
		return settle_way;
	}

	public void setSettle_way(Integer settle_way) {
		this.settle_way = settle_way;
	}

	public String getSearch_text() {
		return search_text;
	}

	public void setSearch_text(String search_text) {
		this.search_text = search_text;
	}

	public Integer getExport() {
		return export;
	}

	public void setExport(Integer export) {
		this.export = export;
	}

	/**
	 * 默认参数构造方法,取的是页面发送的默认值
	 * 
	 */
	public MgCustomerFilterParam() {
		this.firstMenu = "0";
		this.secondMenu = "0";
		this.stationMenu = "0";
		this.page = 1;
		this.city = "0";
		this.crtManager = "0";
		this.saleManager = "0";
		this.saleMenu = "0";
		this.num = 10;
	}

}
