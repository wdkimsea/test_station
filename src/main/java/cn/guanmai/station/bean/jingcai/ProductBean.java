package cn.guanmai.station.bean.jingcai;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;

import cn.guanmai.util.IntTypeAdapter;

/**
 * @author: liming
 * @Date: 2020年4月28日 下午2:09:59
 * @description:
 * @version: 1.0
 */

public class ProductBean {
	@JSONField(name = "id", alternateNames = { "sku_id" })
	private String id;
	private String spu_id;

	@JSONField(name = "name", alternateNames = { "sku_name" })
	private String name;
	private String salemenu_name;
	private String outer_id;
	private BigDecimal std_sale_price;
	private BigDecimal std_sale_price_forsale;
	private String std_unit_name_forsale;
	@JSONField(serializeUsing = IntTypeAdapter.class)
	private int partframe;
	private Integer suggest_price_min;
	private Integer suggest_price_max;
	private String std_unit_name;
	@JSONField(serializeUsing = IntTypeAdapter.class)
	private int slitting;
	@JSONField(serializeUsing = IntTypeAdapter.class)
	private int clean_food;
	private BigDecimal sale_num_least;
	private String stocks;
	private JSONArray img_url;
	private String spu_name;
	private BigDecimal sale_ratio;
	private String sale_unit_name;
	private String desc;
	private String supplier_id;
	@JSONField(serializeUsing = IntTypeAdapter.class)
	private int is_price_timing;
	@JSONField(serializeUsing = IntTypeAdapter.class)
	private int is_weigh;
	private BigDecimal spu_stock;
	private String purchase_spec_id;
	private BigDecimal attrition_rate;
	private int stock_type;
	private String salemenu_id;
	private int state;
	private BigDecimal sale_price;
	private Integer remark_type;
	private Integer rounding;

	private List<Ingredient> ingredients;

	private CleanFoodInfo clean_food_info;

	public class Ingredient {
		private BigDecimal attrition_rate;
		private String supplier_id;
		private String std_unit_name;
		private String sale_unit_name;
		private String id;
		private BigDecimal ratio;
		private String name;
		private Integer technic_flow_len;
		private Integer remark_type;
		private BigDecimal sale_proportion;
		private BigDecimal proportion;
		private String category_id_2;
		private Integer version;

		public BigDecimal getAttrition_rate() {
			return attrition_rate;
		}

		public void setAttrition_rate(BigDecimal attrition_rate) {
			this.attrition_rate = attrition_rate;
		}

		public String getSupplier_id() {
			return supplier_id;
		}

		public void setSupplier_id(String supplier_id) {
			this.supplier_id = supplier_id;
		}

		public String getStd_unit_name() {
			return std_unit_name;
		}

		public void setStd_unit_name(String std_unit_name) {
			this.std_unit_name = std_unit_name;
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

		public void setId(String id) {
			this.id = id;
		}

		public BigDecimal getRatio() {
			return ratio;
		}

		public void setRatio(BigDecimal ratio) {
			this.ratio = ratio;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getTechnic_flow_len() {
			return technic_flow_len;
		}

		public void setTechnic_flow_len(Integer technic_flow_len) {
			this.technic_flow_len = technic_flow_len;
		}

		public Integer getRemark_type() {
			return remark_type;
		}

		public void setRemark_type(Integer remark_type) {
			this.remark_type = remark_type;
		}

		public BigDecimal getSale_proportion() {
			return sale_proportion;
		}

		public void setSale_proportion(BigDecimal sale_proportion) {
			this.sale_proportion = sale_proportion;
		}

		public BigDecimal getProportion() {
			return proportion;
		}

		public void setProportion(BigDecimal proportion) {
			this.proportion = proportion;
		}

		public String getCategory_id_2() {
			return category_id_2;
		}

		public void setCategory_id_2(String category_id_2) {
			this.category_id_2 = category_id_2;
		}

		public Integer getVersion() {
			return version;
		}

		public void setVersion(Integer version) {
			this.version = version;
		}
	}

	public class CleanFoodInfo {
		private String license;
		private String storage_condition;
		private String product_performance_standards;
		private String nutrition;
		private String material_description;
		private String cut_specification;
		private String origin_place;
		private Object process_label_id;
		private String recommended_method;
		private String combine_technic_status;
		private Object shelf_life;

		public String getLicense() {
			return license;
		}

		public void setLicense(String license) {
			this.license = license;
		}

		public String getStorage_condition() {
			return storage_condition;
		}

		public void setStorage_condition(String storage_condition) {
			this.storage_condition = storage_condition;
		}

		public String getProduct_performance_standards() {
			return product_performance_standards;
		}

		public void setProduct_performance_standards(String product_performance_standards) {
			this.product_performance_standards = product_performance_standards;
		}

		public String getNutrition() {
			return nutrition;
		}

		public void setNutrition(String nutrition) {
			this.nutrition = nutrition;
		}

		public String getMaterial_description() {
			return material_description;
		}

		public void setMaterial_description(String material_description) {
			this.material_description = material_description;
		}

		public String getCut_specification() {
			return cut_specification;
		}

		public void setCut_specification(String cut_specification) {
			this.cut_specification = cut_specification;
		}

		public String getOrigin_place() {
			return origin_place;
		}

		public void setOrigin_place(String origin_place) {
			this.origin_place = origin_place;
		}

		public String getProcess_label_id() {
			return String.valueOf(process_label_id);
		}

		public void setProcess_label_id(BigDecimal process_label_id) {
			this.process_label_id = process_label_id;
		}

		public void setShelf_life(Object shelf_life) {
			this.shelf_life = shelf_life;
		}

		public String getRecommended_method() {
			return recommended_method;
		}

		public void setRecommended_method(String recommended_method) {
			this.recommended_method = recommended_method;
		}

		public String getCombine_technic_status() {
			return combine_technic_status;
		}

		public void setCombine_technic_status(String combine_technic_status) {
			this.combine_technic_status = combine_technic_status;
		}

		public Object getShelf_life() {
			return shelf_life;
		}

		public void setShelf_life(int shelf_life) {
			this.shelf_life = shelf_life;
		}

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getStd_sale_price_forsale() {
		return std_sale_price_forsale;
	}

	public void setStd_sale_price_forsale(BigDecimal std_sale_price_forsale) {
		this.std_sale_price_forsale = std_sale_price_forsale.multiply(new BigDecimal("100").stripTrailingZeros());
	}

	public String getStd_unit_name_forsale() {
		return std_unit_name_forsale;
	}

	public void setStd_unit_name_forsale(String std_unit_name_forsale) {
		this.std_unit_name_forsale = std_unit_name_forsale;
	}

	public BigDecimal getSale_price() {
		return sale_price;
	}

	public void setSale_price(BigDecimal sale_price) {
		this.sale_price = sale_price.multiply(new BigDecimal("100").stripTrailingZeros());
	}

	public String getSale_unit_name() {
		return sale_unit_name;
	}

	public void setSale_unit_name(String sale_unit_name) {
		this.sale_unit_name = sale_unit_name;
	}

	public BigDecimal getSale_ratio() {
		return sale_ratio;
	}

	public void setSale_ratio(BigDecimal sale_ratio) {
		this.sale_ratio = sale_ratio;
	}

	public BigDecimal getSale_num_least() {
		return sale_num_least;
	}

	public void setSale_num_least(BigDecimal sale_num_least) {
		this.sale_num_least = sale_num_least;
	}

	public String getSalemenu_name() {
		return salemenu_name;
	}

	public void setSalemenu_name(String salemenu_name) {
		this.salemenu_name = salemenu_name;
	}

	public Integer getSuggest_price_min() {
		return suggest_price_min;
	}

	public void setSuggest_price_min(Integer suggest_price_min) {
		this.suggest_price_min = suggest_price_min;
	}

	public BigDecimal getStd_sale_price() {
		return std_sale_price;
	}

	public void setStd_sale_price(BigDecimal std_sale_price) {
		this.std_sale_price = std_sale_price;
	}

	public Integer getSuggest_price_max() {
		return suggest_price_max;
	}

	public void setSuggest_price_max(Integer suggest_price_max) {
		this.suggest_price_max = suggest_price_max;
	}

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	public String getStocks() {
		return stocks;
	}

	public void setStocks(String stocks) {
		this.stocks = stocks;
	}

	public JSONArray getImg_url() {
		return img_url;
	}

	public void setImg_url(JSONArray img_url) {
		this.img_url = img_url;
	}

	public String getSpu_name() {
		return spu_name;
	}

	public void setSpu_name(String spu_name) {
		this.spu_name = spu_name;
	}

	public BigDecimal getSpu_stock() {
		return spu_stock;
	}

	public void setSpu_stock(BigDecimal spu_stock) {
		this.spu_stock = spu_stock;
	}

	public void setPartframe(int partframe) {
		this.partframe = partframe;
	}

	public void setSlitting(int slitting) {
		this.slitting = slitting;
	}

	public void setClean_food(int clean_food) {
		this.clean_food = clean_food;
	}

	public void setIs_price_timing(int is_price_timing) {
		this.is_price_timing = is_price_timing;
	}

	public void setIs_weigh(int is_weigh) {
		this.is_weigh = is_weigh;
	}

	public void setStock_type(int stock_type) {
		this.stock_type = stock_type;
	}

	public void setState(int state) {
		this.state = state;
	}

	public BigDecimal getAttrition_rate() {
		return attrition_rate;
	}

	public void setAttrition_rate(BigDecimal attrition_rate) {
		this.attrition_rate = attrition_rate;
	}

	public int getStock_type() {
		return stock_type;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getIs_weigh() {
		return is_weigh;
	}

	public void setIs_weigh(Integer is_weigh) {
		this.is_weigh = is_weigh;
	}

	public Integer getIs_price_timing() {
		return is_price_timing;
	}

	public void setIs_price_timing(Integer is_price_timing) {
		this.is_price_timing = is_price_timing;
	}

	public Integer getSlitting() {
		return slitting;
	}

	public void setSlitting(Integer slitting) {
		this.slitting = slitting;
	}

	public Integer getPartframe() {
		return partframe;
	}

	public void setPartframe(Integer partframe) {
		this.partframe = partframe;
	}

	public Integer getRounding() {
		return rounding;
	}

	public void setRounding(Integer rounding) {
		this.rounding = rounding;
	}

	public String getOuter_id() {
		return outer_id;
	}

	public void setOuter_id(String outer_id) {
		this.outer_id = outer_id;
	}

	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	public String getPurchase_spec_id() {
		return purchase_spec_id;
	}

	public void setPurchase_spec_id(String purchase_spec_id) {
		this.purchase_spec_id = purchase_spec_id;
	}

	public Integer getClean_food() {
		return clean_food;
	}

	public void setClean_food(Integer clean_food) {
		this.clean_food = clean_food;
	}

	public Integer getRemark_type() {
		return remark_type;
	}

	public void setRemark_type(Integer remark_type) {
		this.remark_type = remark_type;
	}

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	public CleanFoodInfo getClean_food_info() {
		return clean_food_info;
	}

	public void setClean_food_info(CleanFoodInfo clean_food_info) {
		this.clean_food_info = clean_food_info;
	}

}
