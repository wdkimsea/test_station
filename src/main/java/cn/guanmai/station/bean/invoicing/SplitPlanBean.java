package cn.guanmai.station.bean.invoicing;

/**
 * @author: liming
 * @Date: 2020年6月28日 下午3:41:44
 * @description:
 * @version: 1.0
 */

public class SplitPlanBean {
	private String id;
	private String name;
	private String remark;
	private String source_spu_id;
	private String source_spu_name;
	private int has_deleted_spu;
	private int version;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSource_spu_id() {
		return source_spu_id;
	}

	public void setSource_spu_id(String source_spu_id) {
		this.source_spu_id = source_spu_id;
	}

	public String getSource_spu_name() {
		return source_spu_name;
	}

	public void setSource_spu_name(String source_spu_name) {
		this.source_spu_name = source_spu_name;
	}

	public int getHas_deleted_spu() {
		return has_deleted_spu;
	}

	public void setHas_deleted_spu(int has_deleted_spu) {
		this.has_deleted_spu = has_deleted_spu;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
