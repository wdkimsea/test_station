package cn.guanmai.station.bean.system;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author liming
 * @date 2019年11月28日
 * @time 下午2:19:20
 * @des 接口 /station/order/batch/template/list 对应的参数
 */

public class OrderImportTemlateBean {
	private String id;
	private String name;
	private int row_title;
	private int status;
	private int type;
	private JSONObject relation_columns;
	@JSONField(name = "relationship")
	private List<Relationship> relationships;

	public class Relationship {
		private int col_index;
		private String relation_name;
		private String system_key;

		public int getCol_index() {
			return col_index;
		}

		public void setCol_index(int col_index) {
			this.col_index = col_index;
		}

		public String getRelation_name() {
			return relation_name;
		}

		public void setRelation_name(String relation_name) {
			this.relation_name = relation_name;
		}

		public String getSystem_key() {
			return system_key;
		}

		public void setSystem_key(String system_key) {
			this.system_key = system_key;
		}

		public Relationship() {
			super();
		}

	}

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

	public int getRow_title() {
		return row_title;
	}

	public void setRow_title(int row_title) {
		this.row_title = row_title;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public JSONObject getRelation_columns() {
		return relation_columns;
	}

	public void setRelation_columns(JSONObject relation_columns) {
		this.relation_columns = relation_columns;
	}

	public List<Relationship> getRelationships() {
		return relationships;
	}

	public void setRelationships(List<Relationship> relationships) {
		this.relationships = relationships;
	}

	public OrderImportTemlateBean() {
		super();
	}

}
