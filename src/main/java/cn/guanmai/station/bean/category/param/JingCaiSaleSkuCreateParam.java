package cn.guanmai.station.bean.category.param;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSONArray;

/**
 * @author liming
 * @date 2019年8月7日 下午4:36:09
 * @des 接口 /product/sku_sale/create 对应的参数
 * @version 1.0
 */
public class JingCaiSaleSkuCreateParam {
	private String spu_id;
	private String name;
	private String outer_id;
	private String salemenu_id;
	private String supplier_id;
	private String purchase_spec_id;
	private String sale_unit_name;
	private String desc;

	private int is_price_timing;
	private int is_weigh;
	private int state;
	private int stock_type;
	private int partframe;
	private int slitting;
	private int clean_food;
	private int remark_type;
	private int bind_turnover;
	private int turnover_bind_type;

	private BigDecimal std_sale_price;
	private BigDecimal sale_price;
	private BigDecimal sale_ratio;
	private BigDecimal sale_num_least;
	private BigDecimal attrition_rate;
	private BigDecimal stocks;

	private JSONArray images;

	private List<Ingredient> ingredients;

	public class Ingredient {
		private String name;
		private String category_id_1;
		private String category_id_2;
		private String sale_unit_name;
		private String std_unit_name;
		private String id;

		private BigDecimal attrition_rate;
		private BigDecimal ratio;

		private int remark_type;
		private int sale_proportion;
		private int proportion;

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the category_id_1
		 */
		public String getCategory_id_1() {
			return category_id_1;
		}

		/**
		 * @param category_id_1 the category_id_1 to set
		 */
		public void setCategory_id_1(String category_id_1) {
			this.category_id_1 = category_id_1;
		}

		/**
		 * @return the category_id_2
		 */
		public String getCategory_id_2() {
			return category_id_2;
		}

		/**
		 * @param category_id_2 the category_id_2 to set
		 */
		public void setCategory_id_2(String category_id_2) {
			this.category_id_2 = category_id_2;
		}

		/**
		 * @return the sale_unit_name
		 */
		public String getSale_unit_name() {
			return sale_unit_name;
		}

		/**
		 * @param sale_unit_name the sale_unit_name to set
		 */
		public void setSale_unit_name(String sale_unit_name) {
			this.sale_unit_name = sale_unit_name;
		}

		/**
		 * @return the std_unit_name
		 */
		public String getStd_unit_name() {
			return std_unit_name;
		}

		/**
		 * @param std_unit_name the std_unit_name to set
		 */
		public void setStd_unit_name(String std_unit_name) {
			this.std_unit_name = std_unit_name;
		}

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @return the attrition_rate
		 */
		public BigDecimal getAttrition_rate() {
			return attrition_rate;
		}

		/**
		 * @param attrition_rate the attrition_rate to set
		 */
		public void setAttrition_rate(BigDecimal attrition_rate) {
			this.attrition_rate = attrition_rate;
		}

		/**
		 * @return the ratio
		 */
		public BigDecimal getRatio() {
			return ratio;
		}

		/**
		 * @param ratio the ratio to set
		 */
		public void setRatio(BigDecimal ratio) {
			this.ratio = ratio;
		}

		/**
		 * @return the remark_type
		 */
		public int getRemark_type() {
			return remark_type;
		}

		/**
		 * @param remark_type the remark_type to set
		 */
		public void setRemark_type(int remark_type) {
			this.remark_type = remark_type;
		}

		/**
		 * @return the sale_proportion
		 */
		public int getSale_proportion() {
			return sale_proportion;
		}

		/**
		 * @param sale_proportion the sale_proportion to set
		 */
		public void setSale_proportion(int sale_proportion) {
			this.sale_proportion = sale_proportion;
		}

		/**
		 * @return the proportion
		 */
		public int getProportion() {
			return proportion;
		}

		/**
		 * @param proportion the proportion to set
		 */
		public void setProportion(int proportion) {
			this.proportion = proportion;
		}

	}

	/**
	 * @return the spu_id
	 */
	public String getSpu_id() {
		return spu_id;
	}

	/**
	 * @param spu_id the spu_id to set
	 */
	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the outer_id
	 */
	public String getOuter_id() {
		return outer_id;
	}

	/**
	 * @param outer_id the outer_id to set
	 */
	public void setOuter_id(String outer_id) {
		this.outer_id = outer_id;
	}

	/**
	 * @return the salemenu_id
	 */
	public String getSalemenu_id() {
		return salemenu_id;
	}

	/**
	 * @param salemenu_id the salemenu_id to set
	 */
	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
	}

	/**
	 * @return the supplier_id
	 */
	public String getSupplier_id() {
		return supplier_id;
	}

	/**
	 * @param supplier_id the supplier_id to set
	 */
	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	/**
	 * @return the purchase_spec_id
	 */
	public String getPurchase_spec_id() {
		return purchase_spec_id;
	}

	/**
	 * @param purchase_spec_id the purchase_spec_id to set
	 */
	public void setPurchase_spec_id(String purchase_spec_id) {
		this.purchase_spec_id = purchase_spec_id;
	}

	/**
	 * @return the sale_unit_name
	 */
	public String getSale_unit_name() {
		return sale_unit_name;
	}

	/**
	 * @param sale_unit_name the sale_unit_name to set
	 */
	public void setSale_unit_name(String sale_unit_name) {
		this.sale_unit_name = sale_unit_name;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the is_price_timing
	 */
	public int getIs_price_timing() {
		return is_price_timing;
	}

	/**
	 * @param is_price_timing the is_price_timing to set
	 */
	public void setIs_price_timing(int is_price_timing) {
		this.is_price_timing = is_price_timing;
	}

	/**
	 * @return the is_weigh
	 */
	public int getIs_weigh() {
		return is_weigh;
	}

	/**
	 * @param is_weigh the is_weigh to set
	 */
	public void setIs_weigh(int is_weigh) {
		this.is_weigh = is_weigh;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * @return the stock_type
	 */
	public int getStock_type() {
		return stock_type;
	}

	/**
	 * @param stock_type the stock_type to set
	 */
	public void setStock_type(int stock_type) {
		this.stock_type = stock_type;
	}

	/**
	 * @return the partframe
	 */
	public int getPartframe() {
		return partframe;
	}

	/**
	 * @param partframe the partframe to set
	 */
	public void setPartframe(int partframe) {
		this.partframe = partframe;
	}

	/**
	 * @return the slitting
	 */
	public int getSlitting() {
		return slitting;
	}

	/**
	 * @param slitting the slitting to set
	 */
	public void setSlitting(int slitting) {
		this.slitting = slitting;
	}

	/**
	 * @return the clean_food
	 */
	public int getClean_food() {
		return clean_food;
	}

	/**
	 * @param clean_food the clean_food to set
	 */
	public void setClean_food(int clean_food) {
		this.clean_food = clean_food;
	}

	/**
	 * @return the remark_type
	 */
	public int getRemark_type() {
		return remark_type;
	}

	/**
	 * @param remark_type the remark_type to set
	 */
	public void setRemark_type(int remark_type) {
		this.remark_type = remark_type;
	}

	/**
	 * @return the bind_turnover
	 */
	public int getBind_turnover() {
		return bind_turnover;
	}

	/**
	 * @param bind_turnover the bind_turnover to set
	 */
	public void setBind_turnover(int bind_turnover) {
		this.bind_turnover = bind_turnover;
	}

	/**
	 * @return the turnover_bind_type
	 */
	public int getTurnover_bind_type() {
		return turnover_bind_type;
	}

	/**
	 * @param turnover_bind_type the turnover_bind_type to set
	 */
	public void setTurnover_bind_type(int turnover_bind_type) {
		this.turnover_bind_type = turnover_bind_type;
	}

	/**
	 * @return the std_sale_price
	 */
	public BigDecimal getStd_sale_price() {
		return std_sale_price;
	}

	/**
	 * @param std_sale_price the std_sale_price to set
	 */
	public void setStd_sale_price(BigDecimal std_sale_price) {
		this.std_sale_price = std_sale_price.multiply(new BigDecimal("100"));
	}

	/**
	 * @return the sale_price
	 */
	public BigDecimal getSale_price() {
		return sale_price;
	}

	/**
	 * @param sale_price the sale_price to set
	 */
	public void setSale_price(BigDecimal sale_price) {
		this.sale_price = sale_price.multiply(new BigDecimal("100"));
	}

	/**
	 * @return the sale_ratio
	 */
	public BigDecimal getSale_ratio() {
		return sale_ratio;
	}

	/**
	 * @param sale_ratio the sale_ratio to set
	 */
	public void setSale_ratio(BigDecimal sale_ratio) {
		this.sale_ratio = sale_ratio;
	}

	/**
	 * @return the sale_num_least
	 */
	public BigDecimal getSale_num_least() {
		return sale_num_least;
	}

	/**
	 * @param sale_num_least the sale_num_least to set
	 */
	public void setSale_num_least(BigDecimal sale_num_least) {
		this.sale_num_least = sale_num_least;
	}

	/**
	 * @return the attrition_rate
	 */
	public BigDecimal getAttrition_rate() {
		return attrition_rate;
	}

	/**
	 * @param attrition_rate the attrition_rate to set
	 */
	public void setAttrition_rate(BigDecimal attrition_rate) {
		this.attrition_rate = attrition_rate;
	}

	/**
	 * @return the stocks
	 */
	public BigDecimal getStocks() {
		return stocks;
	}

	/**
	 * @param stocks the stocks to set
	 */
	public void setStocks(BigDecimal stocks) {
		this.stocks = stocks;
	}

	/**
	 * @return the images
	 */
	public JSONArray getImages() {
		return images;
	}

	/**
	 * @param images the images to set
	 */
	public void setImages(JSONArray images) {
		this.images = images;
	}

	/**
	 * @return the ingredients
	 */
	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	/**
	 * @param ingredients the ingredients to set
	 */
	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

}
