package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author liming
 * @date 2019年11月27日
 * @time 上午11:29:09
 * @des 接口 /stock/shelf/list 对应的结果
 */

public class ShelfSpuStockBean {
	private String root;

	@JSONField(name="shelf_list")
	private List<ShelfStock> shelf_stock_list;

	public class ShelfStock {
		private int batch_count;
		private boolean is_distribution;
		private String shelf_id;
		private String shelf_name;
		private BigDecimal stock_money;
		private BigDecimal stock_num;

		public int getBatch_count() {
			return batch_count;
		}

		public void setBatch_count(int batch_count) {
			this.batch_count = batch_count;
		}

		public boolean isIs_distribution() {
			return is_distribution;
		}

		public void setIs_distribution(boolean is_distribution) {
			this.is_distribution = is_distribution;
		}

		public String getShelf_id() {
			return shelf_id;
		}

		public void setShelf_id(String shelf_id) {
			this.shelf_id = shelf_id;
		}

		public String getShelf_name() {
			return shelf_name;
		}

		public void setShelf_name(String shelf_name) {
			this.shelf_name = shelf_name;
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

	/**
	 * 根目录名称
	 * 
	 * @return
	 */
	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public List<ShelfStock> getShelf_stock_list() {
		return shelf_stock_list;
	}

	public void setShelf_stock_list(List<ShelfStock> shelf_stock_list) {
		this.shelf_stock_list = shelf_stock_list;
	}

}
