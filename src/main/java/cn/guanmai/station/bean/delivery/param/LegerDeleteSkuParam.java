package cn.guanmai.station.bean.delivery.param;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Jun 18, 2019 7:58:21 PM 
* @des 套账单删除商品 /delivery/update 接口对应的参数
* @version 1.0 
*/
public class LegerDeleteSkuParam {
	private String id;
	private int type;
	private BigDecimal raw_id;
	private int op_type;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the raw_id
	 */
	public BigDecimal getRaw_id() {
		return raw_id;
	}

	/**
	 * @param raw_id
	 *            the raw_id to set
	 */
	public void setRaw_id(BigDecimal raw_id) {
		this.raw_id = raw_id;
	}

	/**
	 * @return the op_type
	 */
	public int getOp_type() {
		return op_type;
	}

	/**
	 * @param op_type
	 *            the op_type to set
	 */
	public void setOp_type(int op_type) {
		this.op_type = op_type;
	}

}
