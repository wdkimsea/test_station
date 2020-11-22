package cn.guanmai.station.bean.category;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;

import cn.guanmai.station.bean.system.ServiceTimeBean;
import cn.guanmai.util.JsonUtil;
import cn.guanmai.util.IntTypeAdapter;

/* 
* @author liming 
* @date Nov 8, 2018 2:33:05 PM 
* @des 报价单
* @version 1.0 
*/
public class SalemenuBean {
	private String id;
	private String name;
	private Integer type;
	private String time_config_id;
	private String time_config_name;
	@JSONField(serializeUsing = IntTypeAdapter.class)
	private int is_active;
	private Object targets;
	private String supplier_name;
	private String about;
	private int sku_num;
	@JSONField(name = "time_config")
	private ServiceTimeBean serviceTime;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @return the time_config_id
	 */
	public String getTime_config_id() {
		return time_config_id;
	}

	/**
	 * @param time_config_id the time_config_id to set
	 */
	public void setTime_config_id(String time_config_id) {
		this.time_config_id = time_config_id;
	}

	public String getTime_config_name() {
		return time_config_name;
	}

	public void setTime_config_name(String time_config_name) {
		this.time_config_name = time_config_name;
	}

	public int getIs_active() {
		return is_active;
	}

	public void setIs_active(int is_active) {
		this.is_active = is_active;
	}

	/**
	 * @return the supplier_name
	 */
	public String getSupplier_name() {
		return supplier_name;
	}

	/**
	 * @param supplier_name the supplier_name to set
	 */
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	/**
	 * @return the about
	 */
	public String getAbout() {
		return about;
	}

	/**
	 * @param about the about to set
	 */
	public void setAbout(String about) {
		this.about = about;
	}

	/**
	 * @return the serviceTime
	 */
	public ServiceTimeBean getServiceTime() {
		return serviceTime;
	}

	/**
	 * @param serviceTime the serviceTime to set
	 */
	public void setServiceTime(ServiceTimeBean serviceTime) {
		this.serviceTime = serviceTime;
	}

	/**
	 * @return the targets
	 */
	public List<Target> getTargets() {
		return JsonUtil.strToClassList(JSONArray.toJSONString(targets), Target.class);
	}

	/**
	 * @return the sku_num
	 */
	public int getSku_num() {
		return sku_num;
	}

	/**
	 * @param sku_num the sku_num to set
	 */
	public void setSku_num(int sku_num) {
		this.sku_num = sku_num;
	}

	/**
	 * @param targets the targets to set
	 */
	public void setTargets(JSONArray targets) {
		this.targets = targets;
	}

	public SalemenuBean() {
		super();
	}

	/**
	 * 用于新建报价单的构造方法
	 * 
	 * @param name
	 * @param time_config_id
	 * @param is_active
	 * @param targets
	 * @param supplier_name
	 * @param about
	 */
	public SalemenuBean(String name, String time_config_id, int is_active, JSONArray targets, String supplier_name,
			String about) {
		super();
		this.name = name;
		this.time_config_id = time_config_id;
		this.is_active = is_active;
		this.targets = targets;
		this.supplier_name = supplier_name;
		this.about = about;
	}

	public class Target {
		private String id;
		private String name;

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		public Target(String id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

		public Target() {
			super();
		}

	}
}
