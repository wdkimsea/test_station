package cn.guanmai.station.bean.order.param;

/**
 * @author liming
 * @date 2019年12月26日
 * @time 下午2:53:41
 * @des TODO
 */

public class OrderSkuPriceAutoUpdateParam extends OrderSkuFilterParam {
	private int price_unit_type;

	public int getPrice_unit_type() {
		return price_unit_type;
	}

	public void setPrice_unit_type(int price_unit_type) {
		this.price_unit_type = price_unit_type;
	}

}
