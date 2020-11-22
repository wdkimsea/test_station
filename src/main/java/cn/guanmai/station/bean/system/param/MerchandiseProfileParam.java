package cn.guanmai.station.bean.system.param;

/**
 * @author: liming
 * @Date: 2020年3月18日 上午10:55:38
 * @description:
 * @version: 1.0
 */

public class MerchandiseProfileParam {
	private int suggest_price;
	private int show_sku_outer_id;
	private int show_tax_rate;
	private int sync_price_timing;

	public int getSuggest_price() {
		return suggest_price;
	}

	public void setSuggest_price(int suggest_price) {
		this.suggest_price = suggest_price;
	}

	public int getShow_sku_outer_id() {
		return show_sku_outer_id;
	}

	public void setShow_sku_outer_id(int show_sku_outer_id) {
		this.show_sku_outer_id = show_sku_outer_id;
	}

	public int getShow_tax_rate() {
		return show_tax_rate;
	}

	public void setShow_tax_rate(int show_tax_rate) {
		this.show_tax_rate = show_tax_rate;
	}

	public int getSync_price_timing() {
		return sync_price_timing;
	}

	public void setSync_price_timing(int sync_price_timing) {
		this.sync_price_timing = sync_price_timing;
	}
}
