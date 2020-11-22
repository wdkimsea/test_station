package cn.guanmai.station.bean.category.param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liming
 * @Date: 2020年2月17日 下午3:56:37
 * @description:
 * @version: 1.0
 */

public class CombineGoodsParam {
	private String id;
	private String name;
	private String sale_unit_name;
	private int state;
	private String desc;
	private List<String> salemenu_ids;
	private List<String> images;

	private List<Spu> spus;

	private List<Sku> skus;

	public class Spu {
		private String spu_id;
		private BigDecimal quantity;

		public String getSpu_id() {
			return spu_id;
		}

		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		public BigDecimal getQuantity() {
			return quantity;
		}

		public void setQuantity(BigDecimal quantity) {
			this.quantity = quantity;
		}

	}

	public class Sku {
		private String sku_id;
		private String spu_id;
		private String salemenu_id;

		public String getSku_id() {
			return sku_id;
		}

		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		public String getSpu_id() {
			return spu_id;
		}

		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		public String getSalemenu_id() {
			return salemenu_id;
		}

		public void setSalemenu_id(String salemenu_id) {
			this.salemenu_id = salemenu_id;
		}

	}

	public String getName() {
		return name;
	}

	public String getSale_unit_name() {
		return sale_unit_name;
	}

	public void setSale_unit_name(String sale_unit_name) {
		this.sale_unit_name = sale_unit_name;
	}

	public String getId() {
		return id;
	}

	/**
	 * 修改组合商品特有参数
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<String> getSalemenu_ids() {
		return salemenu_ids;
	}

	public void setSalemenu_ids(List<String> salemenu_ids) {
		this.salemenu_ids = salemenu_ids;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public List<Spu> getSpus() {
		return spus;
	}

	public void setSpus(List<Spu> spus) {
		this.spus = spus;
	}

	public List<Sku> getSkus() {
		return skus;
	}

	public void setSkus(List<Sku> skus) {
		this.skus = skus;
	}

}
