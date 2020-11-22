package cn.guanmai.station.bean.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author liming
 * @date 2019年12月11日
 * @time 上午11:55:04
 * @des 结款单模板,/fe/settle_tpl/list对应的结果
 */

public class SettleTemplateBean {
	private Content content;

	private String create_time;
	private String creator;
	private String id;
	private boolean is_default;

	public static class Content {
		private String name;
		private JSONObject header;
		private JSONArray contents;
		private JSONObject footer;
		private JSONObject page;
		private JSONObject sign;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public JSONObject getHeader() {
			return header;
		}

		public void setHeader(JSONObject header) {
			this.header = header;
		}

		public JSONArray getContents() {
			return contents;
		}

		public void setContents(JSONArray contents) {
			this.contents = contents;
		}

		public JSONObject getFooter() {
			return footer;
		}

		public void setFooter(JSONObject footer) {
			this.footer = footer;
		}

		public JSONObject getPage() {
			return page;
		}

		public void setPage(JSONObject page) {
			this.page = page;
		}

		public JSONObject getSign() {
			return sign;
		}

		public void setSign(JSONObject sign) {
			this.sign = sign;
		}

		public Content() {
			super();
		}

	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isIs_default() {
		return is_default;
	}

	public void setIs_default(boolean is_default) {
		this.is_default = is_default;
	}

	public SettleTemplateBean() {
		super();
	}

}
