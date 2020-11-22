package cn.guanmai.station.bean.marketing;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author: liming
 * @Date: 2020年6月2日 下午5:03:03
 * @description:
 * @version: 1.0
 */

public class CouponPageBean {
	@JSONField(name="data")
	private List<CouponBean> coupons;
	private int code;
	private String msg;
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

		public JSONArray getPage_obj() {
			return JSON.parseArray(page_obj.toString());
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

	public List<CouponBean> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<CouponBean> coupons) {
		this.coupons = coupons;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}
}
