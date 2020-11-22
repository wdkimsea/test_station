package cn.guanmai.station.bean.category.param;

import java.util.List;

/**
 * @author: liming
 * @Date: 2020年2月25日 下午7:45:40
 * @description:
 * @version: 1.0
 */

public class CombineGoodsBatchFilterParam {
	private String search_text;
	private Integer all;
	private Integer state;
	private List<String> combine_goods_ids;

	public String getSearch_text() {
		return search_text;
	}

	public void setSearch_text(String search_text) {
		this.search_text = search_text;
	}

	public Integer getAll() {
		return all;
	}

	public void setAll(Integer all) {
		this.all = all;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public List<String> getCombine_goods_ids() {
		return combine_goods_ids;
	}

	public void setCombine_goods_ids(List<String> combine_goods_ids) {
		this.combine_goods_ids = combine_goods_ids;
	}

}
