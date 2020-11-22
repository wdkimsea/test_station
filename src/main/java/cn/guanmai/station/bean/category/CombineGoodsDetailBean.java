package cn.guanmai.station.bean.category;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author: liming
 * @Date: 2020年2月17日 下午4:15:49
 * @description:
 * @version: 1.0
 */

public class CombineGoodsDetailBean {
	private String id;
	private String name;
	private String sale_unit_name;
	private String desc;
	private int state;
	private List<String> images;
	private Map<String, String> salemenus;
	private Map<String, Spu> spus;
	private List<Sku> skus;

	public class Spu {
		private String category_id_2;
		private String category_title_2;
		private String id;
		private String name;
		private String std_unit_name;
		private BigDecimal quantity;

		public String getCategory_id_2() {
			return category_id_2;
		}

		public void setCategory_id_2(String category_id_2) {
			this.category_id_2 = category_id_2;
		}

		public String getCategory_title_2() {
			return category_title_2;
		}

		public void setCategory_title_2(String category_title_2) {
			this.category_title_2 = category_title_2;
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

		public String getStd_unit_name() {
			return std_unit_name;
		}

		public void setStd_unit_name(String std_unit_name) {
			this.std_unit_name = std_unit_name;
		}

		public BigDecimal getQuantity() {
			return quantity;
		}

		public void setQuantity(BigDecimal quantity) {
			this.quantity = quantity;
		}

	}

	public class Sku {
		private String spu_id;
		private String spu_name;
		private String id;
		private String sku_name;
		private String salemenu_id;
		private String sale_unit_name;
		private int status;
		private int state;
		private BigDecimal sale_ratio;
		private BigDecimal sale_price;

		public String getSpu_id() {
			return spu_id;
		}

		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		public String getSpu_name() {
			return spu_name;
		}

		public void setSpu_name(String spu_name) {
			this.spu_name = spu_name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getSku_name() {
			return sku_name;
		}

		public void setSku_name(String sku_name) {
			this.sku_name = sku_name;
		}

		public String getSalemenu_id() {
			return salemenu_id;
		}

		public void setSalemenu_id(String salemenu_id) {
			this.salemenu_id = salemenu_id;
		}

		public String getSale_unit_name() {
			return sale_unit_name;
		}

		public void setSale_unit_name(String sale_unit_name) {
			this.sale_unit_name = sale_unit_name;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
		}

		public BigDecimal getSale_ratio() {
			return sale_ratio;
		}

		public void setSale_ratio(BigDecimal sale_ratio) {
			this.sale_ratio = sale_ratio;
		}

		public BigDecimal getSale_price() {
			return sale_price;
		}

		public void setSale_price(BigDecimal sale_price) {
			this.sale_price = sale_price;
		}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSale_unit_name() {
		return sale_unit_name;
	}

	public void setSale_unit_name(String sale_unit_name) {
		this.sale_unit_name = sale_unit_name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public Map<String, String> getSalemenus() {
		return salemenus;
	}

	public void setSalemenus(Map<String, String> salemenus) {
		this.salemenus = salemenus;
	}

	public Map<String, Spu> getSpus() {
		return spus;
	}

	public void setSpus(Map<String, Spu> spus) {
		this.spus = spus;
	}

	public List<Sku> getSkus() {
		return skus;
	}

	public void setSkus(List<Sku> skus) {
		this.skus = skus;
	}

}
