package cn.guanmai.station.bean.invoicing.param;

/**
 * @author liming
 * @date 2019年11月27日
 * @time 上午10:14:49
 * @des 接口 /stock/check/batch_number/list 对应的参数
 */

public class ShelfStockBatchFilterParam {
	private String spu_id;
	private String shelf_id;
	private int view_shelf = 1;
	private int remain_positive = 1;
	private int batch_edit = 1;
	private int peek = 60;
	private int offset = 0;
	private int limit = 10;

	public String getSpu_id() {
		return spu_id;
	}

	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	public String getShelf_id() {
		return shelf_id;
	}

	public void setShelf_id(String shelf_id) {
		this.shelf_id = shelf_id;
	}

	public int getView_shelf() {
		return view_shelf;
	}

	public void setView_shelf(int view_shelf) {
		this.view_shelf = view_shelf;
	}

	public int getRemain_positive() {
		return remain_positive;
	}

	public void setRemain_positive(int remain_positive) {
		this.remain_positive = remain_positive;
	}

	public int getBatch_edit() {
		return batch_edit;
	}

	public void setBatch_edit(int batch_edit) {
		this.batch_edit = batch_edit;
	}

	public int getPeek() {
		return peek;
	}

	public void setPeek(int peek) {
		this.peek = peek;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
