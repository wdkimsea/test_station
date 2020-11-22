package cn.guanmai.open.bean.product;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2019年10月31日
 * @time 下午5:31:33
 * @des TODO
 */

public class OpenSaleSkuDetailBean {
	private String sku_id;
	private String sku_name;
	private String spu_id;
	private String sale_ratio;
	private String salemenu_id;
	private String state;
	private String weighing;
	private String is_price_timing;
	private String std_unit_name;
	private String sale_unit_name;
	private String sale_price;
	private String std_sale_price;
	private String category1_id;
	private String category2_id;
	private String pinlei_id;
	private String create_time;
	private List<String> img_url;
	private String desc;
	private String supplier_id;
	private String sku_outer_id;


	private BigDecimal last_quote_price;
	private BigDecimal last_purchase_price;
	private BigDecimal last_in_stock_price;
	private BigDecimal stock_avg_price;
	private BigDecimal latest_quote_price;
	private BigDecimal latest_in_stock_price;

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	public String getSku_name() {
		return sku_name;
	}

	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	public String getSpu_id() {
		return spu_id;
	}

	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	public String getSale_ratio() {
		return sale_ratio;
	}

	public void setSale_ratio(String sale_ratio) {
		this.sale_ratio = sale_ratio;
	}

	public String getSalemenu_id() {
		return salemenu_id;
	}

	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getWeighing() {
		return weighing;
	}

	public void setWeighing(String weighing) {
		this.weighing = weighing;
	}

	public String getIs_price_timing() {
		return is_price_timing;
	}

	public void setIs_price_timing(String is_price_timing) {
		this.is_price_timing = is_price_timing;
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

	public String getSale_price() {
		return sale_price;
	}

	public void setSale_price(String sale_price) {
		this.sale_price = sale_price;
	}

	public String getStd_sale_price() {
		return std_sale_price;
	}

	public void setStd_sale_price(String std_sale_price) {
		this.std_sale_price = std_sale_price;
	}

	public String getCategory1_id() {
		return category1_id;
	}

	public void setCategory1_id(String category1_id) {
		this.category1_id = category1_id;
	}

	public String getCategory2_id() {
		return category2_id;
	}

	public void setCategory2_id(String category2_id) {
		this.category2_id = category2_id;
	}

	public String getPinlei_id() {
		return pinlei_id;
	}

	public void setPinlei_id(String pinlei_id) {
		this.pinlei_id = pinlei_id;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public List<String> getImg_url() {
		return img_url;
	}

	public void setImg_url(List<String> img_url) {
		this.img_url = img_url;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	public String getSku_outer_id() {
		return sku_outer_id;
	}

	public void setSku_outer_id(String sku_outer_id) {
		this.sku_outer_id = sku_outer_id;
	}

	public BigDecimal getLast_quote_price() {
		return last_quote_price;
	}

	public void setLast_quote_price(BigDecimal last_quote_price) {
		this.last_quote_price = last_quote_price;
	}

	public BigDecimal getLast_purchase_price() {
		return last_purchase_price;
	}

	public void setLast_purchase_price(BigDecimal last_purchase_price) {
		this.last_purchase_price = last_purchase_price;
	}

	public BigDecimal getLast_in_stock_price() {
		return last_in_stock_price;
	}

	public void setLast_in_stock_price(BigDecimal last_in_stock_price) {
		this.last_in_stock_price = last_in_stock_price;
	}

	public BigDecimal getStock_avg_price() {
		return stock_avg_price;
	}

	public void setStock_avg_price(BigDecimal stock_avg_price) {
		this.stock_avg_price = stock_avg_price;
	}

	public BigDecimal getLatest_quote_price() {
		return latest_quote_price;
	}

	public void setLatest_quote_price(BigDecimal latest_quote_price) {
		this.latest_quote_price = latest_quote_price;
	}

	public BigDecimal getLatest_in_stock_price() {
		return latest_in_stock_price;
	}

	public void setLatest_in_stock_price(BigDecimal latest_in_stock_price) {
		this.latest_in_stock_price = latest_in_stock_price;
	}

}
