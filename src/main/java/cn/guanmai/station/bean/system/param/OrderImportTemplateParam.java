package cn.guanmai.station.bean.system.param;

/**
 * @author liming
 * @date 2019年11月28日
 * @time 下午12:01:21
 * @des 接口 /station/order/batch/template/create 对应的参数
 */

public class OrderImportTemplateParam {
	private String name = "自动化测试专用模板(请勿修改)";
	private int type = 1;
	private int row_title = 1;
	private String relation_columns = "{\"1\":\"下单商户ID\",\"2\":\"下单商户名\",\"3\":\"下单商品ID\",\"4\":\"下单商品名\",\"5\":\"下单商品单价\",\"6\":\"下单商品数量\",\"7\":\"下单商品备注\"}";
	private String relationship = "[{\"system_key\":\"sid\",\"relation_name\":\"下单商户ID\",\"col_index\":1},{\"system_key\":\"resname\",\"relation_name\":\"下单商户名\",\"col_index\":2},{\"system_key\":\"sku_id\",\"relation_name\":\"下单商品ID\",\"col_index\":3},{\"system_key\":\"sku_name\",\"relation_name\":\"下单商品名\",\"col_index\":4},{\"system_key\":\"sale_price\",\"relation_name\":\"下单商品单价\",\"col_index\":5},{\"system_key\":\"quantity\",\"relation_name\":\"下单商品数量\",\"col_index\":6},{\"system_key\":\"spu_remark\",\"relation_name\":\"下单商品备注\",\"col_index\":7}]";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRow_title() {
		return row_title;
	}

	public void setRow_title(int row_title) {
		this.row_title = row_title;
	}

	public String getRelation_columns() {
		return relation_columns;
	}

	public void setRelation_columns(String relation_columns) {
		this.relation_columns = relation_columns;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

}
