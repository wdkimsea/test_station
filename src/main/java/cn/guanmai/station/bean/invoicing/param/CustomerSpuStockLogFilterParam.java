package cn.guanmai.station.bean.invoicing.param;

/**
 * @author liming
 * @date 2019年9月27日
 * @time 下午5:48:24
 * @des 接口 /stock/address/spu_stock/log/list 对应的参数
 */

public class CustomerSpuStockLogFilterParam {
	private String start_time;
	private String end_time;
	private int op_type = -1;
	private String address_id;
	private String spu_id;
	private int limit = 20;
	private int offset;
	private Integer export;

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public int getOp_type() {
		return op_type;
	}

	public void setOp_type(int op_type) {
		this.op_type = op_type;
	}

	public String getAddress_id() {
		return address_id;
	}

	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	public String getSpu_id() {
		return spu_id;
	}

	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
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

	public Integer getExport() {
		return export;
	}

	public void setExport(Integer export) {
		this.export = export;
	}

}
