package cn.guanmai.station.bean.category;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;


/* 
* @author liming 
* @date Oct 31, 2018 11:23:40 AM 
* @des 采购规格 Bean 文件
* @version 1.0 
*/
public class PurchaseSpecBean {
	private String id;
	private String name;
	private String std_unit;
	private String unit_name;
	private String purchase_unit;
	private String description;
	private String barcode;
	private BigDecimal ratio;
	private String category_1;
	private String category_1_name;
	private String category_2;
	private String category_2_name;
	private String pinlei;
	private String pinlei_name;
	private String spu_id;
	private String spu_name;
	private int bind;

	// 下面这几个属性是在商品SKU详细页面使用到的属性

	// 采购规格单位
	private String purchase_unit_name;

	// 参考价格
	private double ref_price;

	// 所属SPU基本单位
	private String std_unit_name;

	@JSONField(name="last_quoted_detail")
	private List<LastQuotedDetail> last_quoted_details;

	public class LastQuotedDetail {
		private String origin_place;
		private BigDecimal price;
		private BigDecimal purchase_price;
		private boolean quoted_from_supplier;
		private String remark;
		private String supplier_id;
		private String time;

		public String getOrigin_place() {
			return origin_place;
		}

		public void setOrigin_place(String origin_place) {
			this.origin_place = origin_place;
		}

		public BigDecimal getPrice() {
			return price.divide(new BigDecimal("100"));
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
		}

		public BigDecimal getPurchase_price() {
			return purchase_price.divide(new BigDecimal("100"));
		}

		public void setPurchase_price(BigDecimal purchase_price) {
			this.purchase_price = purchase_price;
		}

		public boolean isQuoted_from_supplier() {
			return quoted_from_supplier;
		}

		public void setQuoted_from_supplier(boolean quoted_from_supplier) {
			this.quoted_from_supplier = quoted_from_supplier;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getSupplier_id() {
			return supplier_id;
		}

		public void setSupplier_id(String supplier_id) {
			this.supplier_id = supplier_id;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}
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
	 * @return the std_unit
	 */
	public String getStd_unit() {
		return std_unit;
	}

	/**
	 * @param std_unit the std_unit to set
	 */
	public void setStd_unit(String std_unit) {
		this.std_unit = std_unit;
	}

	/**
	 * 新建、修改采购规格传的参数
	 * 
	 * @return the unit_name
	 */
	public String getUnit_name() {
		return unit_name;
	}

	/**
	 * @param unit_name the unit_name to set
	 */
	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

	/**
	 * @return the purchase_unit
	 */
	public String getPurchase_unit() {
		return purchase_unit;
	}

	/**
	 * @param purchase_unit the purchase_unit to set
	 */
	public void setPurchase_unit(String purchase_unit) {
		this.purchase_unit = purchase_unit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
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
	 * @return the category_1
	 */
	public String getCategory_1() {
		return category_1;
	}

	/**
	 * @param category_1 the category_1 to set
	 */
	public void setCategory_1(String category_1) {
		this.category_1 = category_1;
	}

	/**
	 * @return the category_1_name
	 */
	public String getCategory_1_name() {
		return category_1_name;
	}

	/**
	 * @param category_1_name the category_1_name to set
	 */
	public void setCategory_1_name(String category_1_name) {
		this.category_1_name = category_1_name;
	}

	/**
	 * @return the category_2
	 */
	public String getCategory_2() {
		return category_2;
	}

	/**
	 * @param category_2 the category_2 to set
	 */
	public void setCategory_2(String category_2) {
		this.category_2 = category_2;
	}

	/**
	 * @return the category_2_name
	 */
	public String getCategory_2_name() {
		return category_2_name;
	}

	/**
	 * @param category_2_name the category_2_name to set
	 */
	public void setCategory_2_name(String category_2_name) {
		this.category_2_name = category_2_name;
	}

	/**
	 * @return the pinlei
	 */
	public String getPinlei() {
		return pinlei;
	}

	/**
	 * @param pinlei the pinlei to set
	 */
	public void setPinlei(String pinlei) {
		this.pinlei = pinlei;
	}

	/**
	 * @return the pinlei_name
	 */
	public String getPinlei_name() {
		return pinlei_name;
	}

	/**
	 * @param pinlei_name the pinlei_name to set
	 */
	public void setPinlei_name(String pinlei_name) {
		this.pinlei_name = pinlei_name;
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
	 * @return the spu_name
	 */
	public String getSpu_name() {
		return spu_name;
	}

	/**
	 * @param spu_name the spu_name to set
	 */
	public void setSpu_name(String spu_name) {
		this.spu_name = spu_name;
	}

	/**
	 * @return the bind
	 */
	public int getBind() {
		return bind;
	}

	/**
	 * @param bind the bind to set
	 */
	public void setBind(int bind) {
		this.bind = bind;
	}

	public PurchaseSpecBean() {
		super();
	}

	/**
	 * 采购规格 , 用于销售SKU详细页面的采购规格显示
	 * 
	 * @return the purchase_unit_name
	 */
	public String getPurchase_unit_name() {
		return purchase_unit_name;
	}

	/**
	 * 采购规格 , 用于销售SKU详细页面的采购规格显示
	 * 
	 * @param purchase_unit_name the purchase_unit_name to set
	 */
	public void setPurchase_unit_name(String purchase_unit_name) {
		this.purchase_unit_name = purchase_unit_name;
	}

	/**
	 * 参考价格 , 用于销售SKU详细页面的采购规格显示
	 * 
	 * @return the ref_price
	 */
	public double getRef_price() {
		return ref_price;
	}

	/**
	 * 参考价格 , 用于销售SKU详细页面的采购规格显示
	 * 
	 * @param ref_price the ref_price to set
	 */
	public void setRef_price(double ref_price) {
		this.ref_price = ref_price;
	}

	/**
	 * 采购规格所属SPU基本单位 , 用于销售SKU详细页面的采购规格显示
	 * 
	 * @return the std_unit_name
	 */
	public String getStd_unit_name() {
		return std_unit_name;
	}

	/**
	 * 采购规格所属SPU基本单位 , 用于销售SKU详细页面的采购规格显示
	 * 
	 * @param std_unit_name the std_unit_name to set
	 */
	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	/**
	 * 用于显示采购规格详细的构造方法
	 * 
	 * @param id
	 * @param name
	 * @param std_unit
	 * @param purchase_unit
	 * @param ratio
	 * @param category_1
	 * @param category_1_name
	 * @param category_2
	 * @param category_2_name
	 * @param pinlei
	 * @param pinlei_name
	 * @param spu_id
	 * @param spu_name
	 * @param bind
	 */
	public PurchaseSpecBean(String id, String name, String std_unit, String purchase_unit, BigDecimal ratio,
			String category_1, String category_1_name, String category_2, String category_2_name, String pinlei,
			String pinlei_name, String spu_id, String spu_name, int bind) {
		super();
		this.id = id;
		this.name = name;
		this.std_unit = std_unit;
		this.purchase_unit = purchase_unit;
		this.ratio = ratio;
		this.category_1 = category_1;
		this.category_1_name = category_1_name;
		this.category_2 = category_2;
		this.category_2_name = category_2_name;
		this.pinlei = pinlei;
		this.pinlei_name = pinlei_name;
		this.spu_id = spu_id;
		this.spu_name = spu_name;
		this.bind = bind;
	}

	/**
	 * 用于创建采购规格的构造方法
	 * 
	 * @param name
	 * @param description;
	 * @param barcode;
	 * @param unit_name
	 * @param ratio
	 * @param category_1
	 * @param category_2
	 * @param pinlei
	 * @param spu_id
	 */
	public PurchaseSpecBean(String name, String description, String barcode, String unit_name, BigDecimal ratio,
			String category_1, String category_2, String pinlei, String spu_id) {
		this.name = name;
		this.description = description;
		this.barcode = barcode;
		this.unit_name = unit_name;
		this.ratio = ratio;
		this.category_1 = category_1;
		this.category_2 = category_2;
		this.pinlei = pinlei;
		this.spu_id = spu_id;
	}

	/**
	 * 此构造方法用于销售SKU展示的采购规格
	 * 
	 * @param id
	 * @param name
	 * @param ratio
	 * @param purchase_unit_name
	 * @param ref_price
	 * @param std_unit_name
	 */
	public PurchaseSpecBean(String id, String name, BigDecimal ratio, String purchase_unit_name, double ref_price,
			String std_unit_name) {
		super();
		this.id = id;
		this.name = name;
		this.ratio = ratio;
		this.purchase_unit_name = purchase_unit_name;
		this.ref_price = ref_price;
		this.std_unit_name = std_unit_name;
	}

	public List<LastQuotedDetail> getLast_quoted_details() {
		return last_quoted_details;
	}

	public void setLast_quoted_details(List<LastQuotedDetail> last_quoted_details) {
		this.last_quoted_details = last_quoted_details;
	}

}
