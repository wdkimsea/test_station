package cn.guanmai.open.bean.product.param;

public class OpenSkuCreateParam implements Cloneable {
	private String supplier_id;
	private String salemenu_id;
	private String spec_id;
	private String sku_name;
	private String sale_price;
	private String sale_num_least;
	private String sale_ratio;
	private String sale_unit_name;
	private String desc;
	private String weighing;
	private String state;
	private String is_price_timing;
	private String sku_outer_id;

	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	public String getSalemenu_id() {
		return salemenu_id;
	}

	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
	}

	public String getSpec_id() {
		return spec_id;
	}

	public void setSpec_id(String spec_id) {
		this.spec_id = spec_id;
	}

	public String getSku_name() {
		return sku_name;
	}

	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	public String getSale_price() {
		return sale_price;
	}

	public void setSale_price(String sale_price) {
		this.sale_price = sale_price;
	}

	public String getSale_num_least() {
		return sale_num_least;
	}

	public void setSale_num_least(String sale_num_least) {
		this.sale_num_least = sale_num_least;
	}

	public String getSale_ratio() {
		return sale_ratio;
	}

	public void setSale_ratio(String sale_ratio) {
		this.sale_ratio = sale_ratio;
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

	public String getWeighing() {
		return weighing;
	}

	public void setWeighing(String weighing) {
		this.weighing = weighing;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIs_price_timing() {
		return is_price_timing;
	}

	public void setIs_price_timing(String is_price_timing) {
		this.is_price_timing = is_price_timing;
	}

	@Override
	public OpenSkuCreateParam clone() {
		OpenSkuCreateParam res = null;
		try {
			res = (OpenSkuCreateParam) super.clone(); // 浅复制
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return res;
	}

	public String getSku_outer_id() {
		return sku_outer_id;
	}

	public void setSku_outer_id(String sku_outer_id) {
		this.sku_outer_id = sku_outer_id;
	}

}
