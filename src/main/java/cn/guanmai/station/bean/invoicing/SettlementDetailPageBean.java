package cn.guanmai.station.bean.invoicing;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author: liming
 * @Date: 2020年5月20日 下午4:00:14
 * @description: /stock/report/settlement/detail 应付明细账带有分页信息的返回值
 * @version: 1.0
 */

public class SettlementDetailPageBean {
	@JSONField(name="data")
	private List<SettlementDetailBean> settlementDetails;
	private Pagination pagination;

	public class Pagination {
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
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public List<SettlementDetailBean> getSettlementDetails() {
		return settlementDetails;
	}

	public void setSettlementDetails(List<SettlementDetailBean> settlementDetails) {
		this.settlementDetails = settlementDetails;
	}

}
