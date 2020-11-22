package cn.guanmai.station.bean.category;

import java.util.List;

/**
 * @author: liming
 * @Date: 2020年3月3日 上午10:36:28
 * @description: 智能菜单详情
 * @version: 1.0
 */

public class SmartMenuDetailBean {
	private String name;
	private String id;
	private List<Sku> skus;
	private List<CombineGoods> combine_goods;

	public class Sku {
		private String salemenu_id;
		private String id;
		private int state;
		private String sku_name;
		private String salemenu_name;

		public String getSalemenu_id() {
			return salemenu_id;
		}

		public void setSalemenu_id(String salemenu_id) {
			this.salemenu_id = salemenu_id;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
		}

		public String getSku_name() {
			return sku_name;
		}

		public void setSku_name(String sku_name) {
			this.sku_name = sku_name;
		}

		public String getSalemenu_name() {
			return salemenu_name;
		}

		public void setSalemenu_name(String salemenu_name) {
			this.salemenu_name = salemenu_name;
		}

	}

	public class CombineGoods {
		private String id;
		private String name;
		private int state;

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

		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Sku> getSkus() {
		return skus;
	}

	public void setSkus(List<Sku> skus) {
		this.skus = skus;
	}

	public List<CombineGoods> getCombine_goods() {
		return combine_goods;
	}

	public void setCombine_goods(List<CombineGoods> combine_goods) {
		this.combine_goods = combine_goods;
	}

}
