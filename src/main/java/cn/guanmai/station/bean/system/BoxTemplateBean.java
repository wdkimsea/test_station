package cn.guanmai.station.bean.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author: liming
 * @Date: 2020年7月10日 上午11:52:21
 * @description:
 * @version: 1.0
 */

public class BoxTemplateBean {
	private String id;
	private String name;
	private String create_time;
	private String creator;
	private String is_default;

	private Content content;

	public class Content {
		private JSONArray contents;
		private JSONObject footer;
		private JSONObject header;
		private String name;
		private JSONObject page;
		private JSONObject sign;

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

		public JSONObject getHeader() {
			return header;
		}

		public void setHeader(JSONObject header) {
			this.header = header;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
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

	public String getIs_default() {
		return is_default;
	}

	public void setIs_default(String is_default) {
		this.is_default = is_default;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}
}
