package cn.guanmai.station.bean.category;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;

import cn.guanmai.util.IntTypeAdapter;

/* 
* @author liming 
* @date Oct 31, 2018 11:22:48 AM 
* @des SKU Bean 文件
* @version 1.0 
*/
public class SkuBean {
	@JSONField(name = "sku_id", alternateNames = { "id" })
	private String id;
	private String spu_id;
	@JSONField(name = "name", alternateNames = { "sku_name" })
	private String name;
	private String salemenu_name;
	private String outer_id;
	private Integer suggest_price_min;
	private BigDecimal std_sale_price;
	private BigDecimal std_sale_price_forsale;
	@JSONField(serializeUsing = IntTypeAdapter.class)
	private int partframe;
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

	private Integer turnover_bind_type;

	/**
	 * 销售SKU ID,修改SKU 必填
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 销售SKU ID,修改SKU 必填
	 * 
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 销售规格所属SPU ,创建SKU时必填
	 * 
	 * @return the spu_id
	 */
	public String getSpu_id() {
		return spu_id;
	}

	/**
	 * 销售规格所属SPU ,创建SKU时必填
	 * 
	 * @param spu_id the spu_id to set
	 */
	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	/**
	 * 销售SKU名称,新建SKU必填
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 销售SKU名称,新建SKU必填
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 报价单名称,新建SKU不填
	 * 
	 * @return the salemenu_name
	 */
	public String getSalemenu_name() {
		return salemenu_name;
	}

	/**
	 * 报价单名称,新建SKU不填
	 * 
	 * @param salemenu_name the salemenu_name to set
	 */
	public void setSalemenu_name(String salemenu_name) {
		this.salemenu_name = salemenu_name;
	}

	/**
	 * 自定义编码,新建SKU非必填值
	 * 
	 * @return the outer_id
	 */
	public String getOuter_id() {
		return outer_id;
	}

	/**
	 * 自定义编码,新建SKU非必填值
	 * 
	 * @param outer_id the outer_id to set
	 */
	public void setOuter_id(String outer_id) {
		this.outer_id = outer_id;
	}

	/**
	 * 建议最小价格,新建SKU不填
	 * 
	 * @return the suggest_price_min
	 */
	public Integer getSuggest_price_min() {
		return suggest_price_min;
	}

	/**
	 * 建议最小价格,新建SKU不填
	 * 
	 * @param suggest_price_min the suggest_price_min to set
	 */
	public void setSuggest_price_min(Integer suggest_price_min) {
		this.suggest_price_min = suggest_price_min;
	}

	/**
	 * 基本销售单位价格,新建SKU必填
	 * 
	 * @return the std_sale_price
	 */
	public BigDecimal getStd_sale_price() {
		return std_sale_price;
	}

	/**
	 * 基本销售单位价格,新建SKU必填
	 * 
	 * @param std_sale_price the std_sale_price to set
	 */
	public void setStd_sale_price(BigDecimal std_sale_price) {
		this.std_sale_price = std_sale_price;
	}

	public BigDecimal getStd_sale_price_forsale() {
		return std_sale_price_forsale;
	}

	public void setStd_sale_price_forsale(BigDecimal std_sale_price_forsale) {
		this.std_sale_price_forsale = std_sale_price_forsale;
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
	 * 建议最大价格,新建SKU不填
	 * 
	 * @return the suggest_price_max
	 */
	public Integer getSuggest_price_max() {
		return suggest_price_max;
	}

	/**
	 * 建议最大价格,新建SKU不填
	 * 
	 * @param suggest_price_max the suggest_price_max to set
	 */
	public void setSuggest_price_max(Integer suggest_price_max) {
		this.suggest_price_max = suggest_price_max;
	}

	/**
	 * 基本单位,新建SKU必填
	 * 
	 * @return the std_unit_name
	 */
	public String getStd_unit_name() {
		return std_unit_name;
	}

	/**
	 * 基本单位,新建SKU必填
	 * 
	 * @param std_unit_name the std_unit_name to set
	 */
	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	/**
	 * 是否分切,新建SKU必填
	 * 
	 * @return the slitting
	 */
	public int getSlitting() {
		return slitting;
	}

	/**
	 * 是否分切,新建SKU必填
	 * 
	 * @param slitting the slitting to set
	 */
	public void setSlitting(int slitting) {
		this.slitting = slitting;
	}

	/**
	 * 是否净菜,新建SKU必填
	 * 
	 * @return the clean_food
	 */
	public int getClean_food() {
		return clean_food;
	}

	/**
	 * 是否净菜,新建SKU必填
	 * 
	 * @param clean_food the clean_food to set
	 */
	public void setClean_food(int clean_food) {
		this.clean_food = clean_food;
	}

	/**
	 * 最小下单数,新建SKU必填
	 * 
	 * @return the sale_num_least
	 */
	public BigDecimal getSale_num_least() {
		return sale_num_least;
	}

	/**
	 * 最小下单数,新建SKU必填
	 * 
	 * @param sale_num_least the sale_num_least to set
	 */
	public void setSale_num_least(BigDecimal sale_num_least) {
		this.sale_num_least = sale_num_least;
	}

	/**
	 * 库存数,新建SKU必填
	 * 
	 * @return the stocks
	 */
	public String getStocks() {
		return stocks;
	}

	/**
	 * 库存数,新建SKU必填
	 * 
	 * @param stocks the stocks to set
	 */
	public void setStocks(String stocks) {
		this.stocks = stocks;
	}

	/**
	 * @return the img_url
	 */
	public JSONArray getImg_url() {
		return img_url;
	}

	/**
	 * @param img_url the img_url to set
	 */
	public void setImg_url(JSONArray img_url) {
		this.img_url = img_url;
	}

	/**
	 * SPU名称,新建SKU不填
	 * 
	 * @return the spu_name
	 */
	public String getSpu_name() {
		return spu_name;
	}

	/**
	 * SPU名称,新建SKU不填
	 * 
	 * @param spu_name the spu_name to set
	 */
	public void setSpu_name(String spu_name) {
		this.spu_name = spu_name;
	}

	/**
	 * 销售比例,新建SKU必填
	 * 
	 * @return the sale_ratio
	 */
	public BigDecimal getSale_ratio() {
		return sale_ratio;
	}

	/**
	 * 销售比例,新建SKU必填
	 * 
	 * @param sale_ratio the sale_ratio to set
	 */
	public void setSale_ratio(BigDecimal sale_ratio) {
		this.sale_ratio = sale_ratio;
	}

	/**
	 * 销售单位,新建SKU必填
	 * 
	 * @return the sale_unit_name
	 */
	public String getSale_unit_name() {
		return sale_unit_name;
	}

	/**
	 * 销售单位,新建SKU必填
	 * 
	 * @param sale_unit_name the sale_unit_name to set
	 */
	public void setSale_unit_name(String sale_unit_name) {
		this.sale_unit_name = sale_unit_name;
	}

	/**
	 * 描述,新建SKU选填
	 * 
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * 描述,新建SKU选填
	 * 
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * 供应商ID,新建SKU必填
	 * 
	 * @return the supplier_id
	 */
	public String getSupplier_id() {
		return supplier_id;
	}

	/**
	 * 供应商ID,新建SKU必填
	 * 
	 * @param supplier_id the supplier_id to set
	 */
	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	/**
	 * 是否时价,新建SKU必填
	 * 
	 * @return the is_price_timing
	 */
	public int getIs_price_timing() {
		return is_price_timing;
	}

	/**
	 * 是否时价,新建SKU必填
	 * 
	 * @param is_price_timing the is_price_timing to set
	 */
	public void setIs_price_timing(int is_price_timing) {
		this.is_price_timing = is_price_timing;
	}

	/**
	 * 是否称重,新建SKU必填
	 * 
	 * @return the is_weigh
	 */
	public int getIs_weigh() {
		return is_weigh;
	}

	/**
	 * 是否称重,新建SKU必填
	 * 
	 * @param is_weigh the is_weigh to set
	 */
	public void setIs_weigh(int is_weigh) {
		this.is_weigh = is_weigh;
	}

	/**
	 * SPU库存,新建SKU不填
	 * 
	 * @return the spu_stock
	 */
	public BigDecimal getSpu_stock() {
		return spu_stock;
	}

	/**
	 * SPU库存,新建SKU不填
	 * 
	 * @param spu_stock the spu_stock to set
	 */
	public void setSpu_stock(BigDecimal spu_stock) {
		this.spu_stock = spu_stock;
	}

	/**
	 * 采购规格ID,新建SKU必填
	 * 
	 * @return the purchase_spec_id
	 */
	public String getPurchase_spec_id() {
		return purchase_spec_id;
	}

	/**
	 * 采购规格ID,新建SKU必填
	 * 
	 * @param purchase_spec_id the purchase_spec_id to set
	 */
	public void setPurchase_spec_id(String purchase_spec_id) {
		this.purchase_spec_id = purchase_spec_id;
	}

	/**
	 * 损耗比例,新建SKU必填
	 * 
	 * @return the attrition_rate
	 */
	public BigDecimal getAttrition_rate() {
		return attrition_rate;
	}

	/**
	 * 损耗比例,新建SKU必填
	 * 
	 * @param attrition_rate the attrition_rate to set
	 */
	public void setAttrition_rate(BigDecimal attrition_rate) {
		this.attrition_rate = attrition_rate;
	}

	/**
	 * 库存类型,新建SKU必填
	 * 
	 * @return the stock_type
	 */
	public int getStock_type() {
		return stock_type;
	}

	/**
	 * 库存类型,新建SKU必填
	 * 
	 * @param stock_type the stock_type to set
	 */
	public void setStock_type(int stock_type) {
		this.stock_type = stock_type;
	}

	/**
	 * 报价单ID,新建SKU必填
	 * 
	 * @return the salemenu_id
	 */
	public String getSalemenu_id() {
		return salemenu_id;
	}

	/**
	 * 报价单ID,新建SKU必填
	 * 
	 * @param salemenu_id the salemenu_id to set
	 */
	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
	}

	/**
	 * 状态,新建SKU必填
	 * 
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * 状态,新建SKU必填
	 * 
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * 销售价格,新建SKU必填
	 * 
	 * @return the sale_price
	 */
	public BigDecimal getSale_price() {
		return sale_price.divide(new BigDecimal("100"));
	}

	/**
	 * 销售价格,新建SKU必填
	 * 
	 * @param sale_price the sale_price to set
	 */
	public void setSale_price(BigDecimal sale_price) {
		this.sale_price = sale_price;
	}

	public SkuBean() {
		super();
	}

	/**
	 * @return the remark_type
	 */
	public Integer getRemark_type() {
		return remark_type;
	}

	/**
	 * @param remark_type the remark_type to set
	 */
	public void setRemark_type(Integer remark_type) {
		this.remark_type = remark_type;
	}

	public Integer getRounding() {
		return rounding;
	}

	public void setRounding(Integer rounding) {
		this.rounding = rounding;
	}

	public Integer getTurnover_bind_type() {
		return turnover_bind_type;
	}

	public void setTurnover_bind_type(Integer turnover_bind_type) {
		this.turnover_bind_type = turnover_bind_type;
	}

	/**
	 * 创建SKU使用到的构造方法
	 * 
	 * @param spu_id
	 * @param name
	 * @param outer_id
	 * @param std_sale_price
	 * @param partframe
	 * @param slitting
	 * @param sale_num_least
	 * @param stocks
	 * @param sale_ratio
	 * @param sale_unit_name
	 * @param desc
	 * @param supplier_id
	 * @param is_price_timing
	 * @param is_weigh
	 * @param purchase_spec_id
	 * @param attrition_rate
	 * @param stock_type
	 * @param salemenu_id
	 * @param state
	 * @param sale_price
	 */
	public SkuBean(String spu_id, String name, String outer_id, BigDecimal std_sale_price, int partframe, int slitting,
			BigDecimal sale_num_least, String stocks, BigDecimal sale_ratio, String sale_unit_name, String desc,
			String supplier_id, int is_price_timing, int is_weigh, String purchase_spec_id, BigDecimal attrition_rate,
			int stock_type, String salemenu_id, int state, BigDecimal sale_price, Integer remark_type) {
		super();
		this.spu_id = spu_id;
		this.name = name;
		this.outer_id = outer_id;
		this.std_sale_price = std_sale_price;
		this.partframe = partframe;
		this.slitting = slitting;
		this.sale_num_least = sale_num_least;
		this.stocks = stocks;
		this.sale_ratio = sale_ratio;
		this.sale_unit_name = sale_unit_name;
		this.desc = desc;
		this.supplier_id = supplier_id;
		this.is_price_timing = is_price_timing;
		this.is_weigh = is_weigh;
		this.purchase_spec_id = purchase_spec_id;
		this.attrition_rate = attrition_rate;
		this.stock_type = stock_type;
		this.salemenu_id = salemenu_id;
		this.state = state;
		this.sale_price = sale_price;
		this.remark_type = remark_type;
	}
}
