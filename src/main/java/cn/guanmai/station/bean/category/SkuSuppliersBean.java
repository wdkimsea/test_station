package cn.guanmai.station.bean.category;

import java.util.List;

/**
 * @author liming
 * @date 2019年11月25日
 * @time 下午3:21:35
 * @des TODO
 */

public class SkuSuppliersBean {
	private List<OtherSupplier> other_suppliers;

	private List<RecommendSupplier> recommend_suppliers;

	public class OtherSupplier {
		private String id;
		private String name;
		private int upstream;

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

		public int getUpstream() {
			return upstream;
		}

		public void setUpstream(int upstream) {
			this.upstream = upstream;
		}
	}

	public class RecommendSupplier {
		private String id;
		private String name;
		private int upstream;

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

		public int getUpstream() {
			return upstream;
		}

		public void setUpstream(int upstream) {
			this.upstream = upstream;
		}
	}

	public List<OtherSupplier> getOther_suppliers() {
		return other_suppliers;
	}

	public void setOther_suppliers(List<OtherSupplier> other_suppliers) {
		this.other_suppliers = other_suppliers;
	}

	public List<RecommendSupplier> getRecommend_suppliers() {
		return recommend_suppliers;
	}

	public void setRecommend_suppliers(List<RecommendSupplier> recommend_suppliers) {
		this.recommend_suppliers = recommend_suppliers;
	}

}
