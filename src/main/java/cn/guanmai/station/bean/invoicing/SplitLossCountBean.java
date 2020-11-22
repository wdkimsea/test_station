package cn.guanmai.station.bean.invoicing;

/**
 * @author: liming
 * @Date: 2020年7月7日 上午10:24:07
 * @description: 接口 /stock/split/loss/count 对应的参数
 * @version: 1.0
 */

public class SplitLossCountBean {
	// 待分割商品数
	private int source_spu_count;

	// 分割单据数
	private int split_sheet_count;

	public int getSource_spu_count() {
		return source_spu_count;
	}

	public void setSource_spu_count(int source_spu_count) {
		this.source_spu_count = source_spu_count;
	}

	public int getSplit_sheet_count() {
		return split_sheet_count;
	}

	public void setSplit_sheet_count(int split_sheet_count) {
		this.split_sheet_count = split_sheet_count;
	}

}
