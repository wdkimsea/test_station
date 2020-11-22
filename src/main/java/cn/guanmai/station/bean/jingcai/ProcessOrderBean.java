package cn.guanmai.station.bean.jingcai;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年5月18日 下午2:35:59
 * @description: /stock/process/process_order/list 接口对应的结果
 * @version: 1.0
 */

public class ProcessOrderBean {
	private String create_time;
	private String creator;
	private String custom_id;
	private BigDecimal finish_amount;
	private String id;
	private BigDecimal plan_amount;
	private String plan_finish_time;
	private String release_time;
	private String sale_unit_name;
	private String sku_id;
	private String sku_name;
	private int source_type;
	private String start_time;
	private int status;
	private String std_unit_name;

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCustom_id() {
		return custom_id;
	}

	public void setCustom_id(String custom_id) {
		this.custom_id = custom_id;
	}

	public BigDecimal getFinish_amount() {
		return finish_amount;
	}

	public void setFinish_amount(BigDecimal finish_amount) {
		this.finish_amount = finish_amount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getPlan_amount() {
		return plan_amount;
	}

	public void setPlan_amount(BigDecimal plan_amount) {
		this.plan_amount = plan_amount;
	}

	public String getPlan_finish_time() {
		return plan_finish_time;
	}

	public void setPlan_finish_time(String plan_finish_time) {
		this.plan_finish_time = plan_finish_time;
	}

	public String getRelease_time() {
		return release_time;
	}

	public void setRelease_time(String release_time) {
		this.release_time = release_time;
	}

	public String getSale_unit_name() {
		return sale_unit_name;
	}

	public void setSale_unit_name(String sale_unit_name) {
		this.sale_unit_name = sale_unit_name;
	}

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public String getSku_name() {
		return sku_name;
	}

	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	public int getSource_type() {
		return source_type;
	}

	public void setSource_type(int source_type) {
		this.source_type = source_type;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

}
