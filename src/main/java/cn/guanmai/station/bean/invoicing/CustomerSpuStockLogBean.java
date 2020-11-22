package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/**
 * @author
 * @date 2019年9月27日
 * @time 下午5:13:04
 * @des 接口 /stock/address/spu_stock/log/list 对应的结果
 */
public class CustomerSpuStockLogBean {
	private BigDecimal amount;
	private BigDecimal delta_stock;
	private BigDecimal old_stock;
	private String op_time;
	private int op_type;
	private String op_user;
	private String std_unit_name;
	private BigDecimal std_unit_price;
	private BigDecimal stock;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getDelta_stock() {
		return delta_stock;
	}

	public void setDelta_stock(BigDecimal delta_stock) {
		this.delta_stock = delta_stock;
	}

	public BigDecimal getOld_stock() {
		return old_stock;
	}

	public void setOld_stock(BigDecimal old_stock) {
		this.old_stock = old_stock;
	}

	public String getOp_time() {
		return op_time;
	}

	public void setOp_time(String op_time) {
		this.op_time = op_time;
	}

	public int getOp_type() {
		return op_type;
	}

	public void setOp_type(int op_type) {
		this.op_type = op_type;
	}

	public String getOp_user() {
		return op_user;
	}

	public void setOp_user(String op_user) {
		this.op_user = op_user;
	}

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	public BigDecimal getStd_unit_price() {
		return std_unit_price;
	}

	public void setStd_unit_price(BigDecimal std_unit_price) {
		this.std_unit_price = std_unit_price;
	}

	public BigDecimal getStock() {
		return stock;
	}

	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}
}
