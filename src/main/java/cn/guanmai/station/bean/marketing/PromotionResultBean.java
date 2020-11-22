package cn.guanmai.station.bean.marketing;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liming
 * @Date: 2020年6月22日 下午6:01:09
 * @description:
 * @version: 1.0
 */

public class PromotionResultBean {
	private int code;
	private String id;
	private BigDecimal task_id;
	private List<String> deleteSkus;
	private List<String> usedSkus;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getTask_id() {
		return task_id;
	}

	public void setTask_id(BigDecimal task_id) {
		this.task_id = task_id;
	}

	public List<String> getDeleteSkus() {
		return deleteSkus;
	}

	public void setDeleteSkus(List<String> deleteSkus) {
		this.deleteSkus = deleteSkus;
	}

	public List<String> getUsedSkus() {
		return usedSkus;
	}

	public void setUsedSkus(List<String> usedSkus) {
		this.usedSkus = usedSkus;
	}

}
