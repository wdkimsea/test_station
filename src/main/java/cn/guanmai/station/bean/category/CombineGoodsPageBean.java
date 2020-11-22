package cn.guanmai.station.bean.category;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author: liming
 * @Date: 2020年5月19日 下午2:36:15
 * @description:
 * @version: 1.0
 */

public class CombineGoodsPageBean {
	@JSONField(name = "data")
	private List<CombineGoodsBean> combineGoodsList;

	private Pagination pagination;

	public static class Pagination {
		private int count;
		private boolean more;
		private Object page_obj;
		private int peek;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public boolean isMore() {
			return more;
		}

		public void setMore(boolean more) {
			this.more = more;
		}

		public JSONObject getPage_obj() {
			return JSON.parseObject(page_obj.toString());
		}

		public void setPage_obj(Object page_obj) {
			this.page_obj = page_obj;
		}

		public int getPeek() {
			return peek;
		}

		public void setPeek(int peek) {
			this.peek = peek;
		}

		public Pagination() {
			super();
		}

	}

	public List<CombineGoodsBean> getCombineGoodsList() {
		return combineGoodsList;
	}

	public void setCombineGoodsList(List<CombineGoodsBean> combineGoodsList) {
		this.combineGoodsList = combineGoodsList;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public CombineGoodsPageBean() {
		super();
	}

}
