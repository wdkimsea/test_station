package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年11月26日
 * @time 下午7:42:23
 * @des 接口 /stock/shelf/spu/list 对应的结果
 */

public class ShelfSpuBean {
	private int batch_count;
	private String image;
	private String spu_id;
	private String spu_name;
	private BigDecimal stock_money;
	private BigDecimal stock_num;

	public int getBatch_count() {
		return batch_count;
	}

	public void setBatch_count(int batch_count) {
		this.batch_count = batch_count;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

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

	public BigDecimal getStock_money() {
		return stock_money;
	}

	public void setStock_money(BigDecimal stock_money) {
		this.stock_money = stock_money;
	}

	public BigDecimal getStock_num() {
		return stock_num;
	}

	public void setStock_num(BigDecimal stock_num) {
		this.stock_num = stock_num;
	}

}
