package cn.guanmai.station.bean.purchase.param;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date Nov 16, 2018 10:14:18 AM 
* @des 采购任务过滤参数
* @version 1.0 
*/
public class PurchaseTaskFilterParam {
	private int q_type; // 1 按下单日期; 2 按运营周期; 3 按收货日期
	private Integer type;
	private String q;
	private List<String> category1_ids;
	private List<String> category2_ids;
	private List<String> pinlei_ids;
	private String settle_supplier_id;
	private String begin_time;
	private String end_time;
	private String time_config_id;
	private String purchaser_id;
	private Integer status;
	private List<Integer> order_status;
	private BigDecimal route_id;
	private Integer has_created_sheet;
	private Integer weight_status;
	private Integer limit;
	private Integer offset;
	private String page_obj;
	private Sort sort;

	public class Sort {
		private String opt;
		private int fileds;

		public String getOpt() {
			return opt;
		}

		/**
		 * 两种值asc,desc
		 * 
		 * @param opt
		 */
		public void setOpt(String opt) {
			this.opt = opt;
		}

		public int getFileds() {
			return fileds;
		}

		/**
		 * 取值范围[1,2,3,4]
		 * 
		 * @param fileds
		 */
		public void setFileds(int fileds) {
			this.fileds = fileds;
		}

		public Sort() {
			super();
		}

	}

	/**
	 * @return the q_type
	 */
	public int getQ_type() {
		return q_type;
	}

	/**
	 * @param q_type the q_type to set
	 */
	public void setQ_type(int q_type) {
		this.q_type = q_type;
	}

	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @return the q
	 */
	public String getQ() {
		return q;
	}

	/**
	 * 必填参数 , 1=按下单日期、2=按运营周期、3=按收货日期
	 * 
	 * @param q the q to set
	 */
	public void setQ(String q) {
		this.q = q;
	}

	public List<String> getCategory1_ids() {
		return category1_ids;
	}

	public void setCategory1_ids(List<String> category1_ids) {
		this.category1_ids = category1_ids;
	}

	public List<String> getCategory2_ids() {
		return category2_ids;
	}

	public void setCategory2_ids(List<String> category2_ids) {
		this.category2_ids = category2_ids;
	}

	public List<String> getPinlei_ids() {
		return pinlei_ids;
	}

	public void setPinlei_ids(List<String> pinlei_ids) {
		this.pinlei_ids = pinlei_ids;
	}

	public List<Integer> getOrder_status() {
		return order_status;
	}

	public void setOrder_status(List<Integer> order_status) {
		this.order_status = order_status;
	}

	/**
	 * @return the settle_supplier_id
	 */
	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	/**
	 * @param settle_supplier_id the settle_supplier_id to set
	 */
	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
	}

	/**
	 * @return the begin_time
	 */
	public String getBegin_time() {
		return begin_time;
	}

	/**
	 * 必填参数,搜索开始时间,格式: yyyy-MM-dd HH:mm:ss
	 * 
	 * @param begin_time the begin_time to set
	 */
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}

	/**
	 * @return the end_time
	 */
	public String getEnd_time() {
		return end_time;
	}

	/**
	 * 必填参数,搜索结束时间,格式: yyyy-MM-dd HH:mm:ss
	 * 
	 * @param end_time the end_time to set
	 */
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	/**
	 * @return the time_config_id
	 */
	public String getTime_config_id() {
		return time_config_id;
	}

	/**
	 * 当按运营周期搜索的时候填的参数
	 * 
	 * @param time_config_id the time_config_id to set
	 */
	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	public String getPurchaser_id() {
		return purchaser_id;
	}

	public void setPurchaser_id(String purchaser_id) {
		this.purchaser_id = purchaser_id;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the route_id
	 */
	public BigDecimal getRoute_id() {
		return route_id;
	}

	/**
	 * @param route_id the route_id to set
	 */
	public void setRoute_id(BigDecimal route_id) {
		this.route_id = route_id;
	}

	public Integer getHas_created_sheet() {
		return has_created_sheet;
	}

	public void setHas_created_sheet(Integer has_created_sheet) {
		this.has_created_sheet = has_created_sheet;
	}

	public Integer getWeight_status() {
		return weight_status;
	}

	/**
	 * 1 已完成分拣,2 未完成分拣
	 * 
	 * @param weight_status
	 */
	public void setWeight_status(Integer weight_status) {
		this.weight_status = weight_status;
	}

	/**
	 * @return the limit
	 */
	public Integer getLimit() {
		return limit;
	}

	/**
	 * 必填参数,默认值为 10
	 * 
	 * @param limit the limit to set
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getPage_obj() {
		return page_obj;
	}

	public void setPage_obj(String page_obj) {
		this.page_obj = page_obj;
	}

	public Sort getSort() {
		return sort;
	}

	/**
	 * 新版的采购任务ES才有的参数
	 * 
	 * @param sort
	 */
	public void setSort(Sort sort) {
		this.sort = sort;
	}

	public PurchaseTaskFilterParam() {
		super();
	}

}
