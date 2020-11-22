package cn.guanmai.station.bean.weight;

import java.util.List;

/* 
* @author liming 
* @date Apr 2, 2019 11:28:39 AM 
* @des ST 供应商-分拣-分拣任务统计信息
* @version 1.0 
*/
public class WeightCollectInfoBean {
	private List<CategorySchedule> category_schedule;

	private SortData sort_data;

	private TotalSchedule total_schedule;

	public class CategorySchedule {
		private int finished_count;
		private String name;
		private int out_of_stock_count;
		private int total_count;
		private int unfinished_count;

		/**
		 * @return the finished_count
		 */
		public int getFinished_count() {
			return finished_count;
		}

		/**
		 * @param finished_count
		 *            the finished_count to set
		 */
		public void setFinished_count(int finished_count) {
			this.finished_count = finished_count;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the out_of_stock_count
		 */
		public int getOut_of_stock_count() {
			return out_of_stock_count;
		}

		/**
		 * @param out_of_stock_count
		 *            the out_of_stock_count to set
		 */
		public void setOut_of_stock_count(int out_of_stock_count) {
			this.out_of_stock_count = out_of_stock_count;
		}

		/**
		 * @return the total_count
		 */
		public int getTotal_count() {
			return total_count;
		}

		/**
		 * @param total_count
		 *            the total_count to set
		 */
		public void setTotal_count(int total_count) {
			this.total_count = total_count;
		}

		/**
		 * @return the unfinished_count
		 */
		public int getUnfinished_count() {
			return unfinished_count;
		}

		/**
		 * @param unfinished_count
		 *            the unfinished_count to set
		 */
		public void setUnfinished_count(int unfinished_count) {
			this.unfinished_count = unfinished_count;
		}

	}

	public class SortData {
		private int address_count;
		private int sku_count;
		private int unweight_count;
		private int weight_count;

		/**
		 * @return the address_count
		 */
		public int getAddress_count() {
			return address_count;
		}

		/**
		 * @param address_count
		 *            the address_count to set
		 */
		public void setAddress_count(int address_count) {
			this.address_count = address_count;
		}

		/**
		 * @return the sku_count
		 */
		public int getSku_count() {
			return sku_count;
		}

		/**
		 * @param sku_count
		 *            the sku_count to set
		 */
		public void setSku_count(int sku_count) {
			this.sku_count = sku_count;
		}

		/**
		 * @return the unweight_count
		 */
		public int getUnweight_count() {
			return unweight_count;
		}

		/**
		 * @param unweight_count
		 *            the unweight_count to set
		 */
		public void setUnweight_count(int unweight_count) {
			this.unweight_count = unweight_count;
		}

		/**
		 * @return the weight_count
		 */
		public int getWeight_count() {
			return weight_count;
		}

		/**
		 * @param weight_count
		 *            the weight_count to set
		 */
		public void setWeight_count(int weight_count) {
			this.weight_count = weight_count;
		}
	}

	public class TotalSchedule {
		private int finished_count;
		private int out_of_stock_count;
		private int total_count;
		private int unfinished_count;

		/**
		 * @return the finished_count
		 */
		public int getFinished_count() {
			return finished_count;
		}

		/**
		 * @param finished_count
		 *            the finished_count to set
		 */
		public void setFinished_count(int finished_count) {
			this.finished_count = finished_count;
		}

		/**
		 * @return the out_of_stock_count
		 */
		public int getOut_of_stock_count() {
			return out_of_stock_count;
		}

		/**
		 * @param out_of_stock_count
		 *            the out_of_stock_count to set
		 */
		public void setOut_of_stock_count(int out_of_stock_count) {
			this.out_of_stock_count = out_of_stock_count;
		}

		/**
		 * @return the total_count
		 */
		public int getTotal_count() {
			return total_count;
		}

		/**
		 * @param total_count
		 *            the total_count to set
		 */
		public void setTotal_count(int total_count) {
			this.total_count = total_count;
		}

		/**
		 * @return the unfinished_count
		 */
		public int getUnfinished_count() {
			return unfinished_count;
		}

		/**
		 * @param unfinished_count
		 *            the unfinished_count to set
		 */
		public void setUnfinished_count(int unfinished_count) {
			this.unfinished_count = unfinished_count;
		}
	}

	/**
	 * @return the category_schedule
	 */
	public List<CategorySchedule> getCategory_schedule() {
		return category_schedule;
	}

	/**
	 * @param category_schedule
	 *            the category_schedule to set
	 */
	public void setCategory_schedule(List<CategorySchedule> category_schedule) {
		this.category_schedule = category_schedule;
	}

	/**
	 * @return the sort_data
	 */
	public SortData getSort_data() {
		return sort_data;
	}

	/**
	 * @param sort_data
	 *            the sort_data to set
	 */
	public void setSort_data(SortData sort_data) {
		this.sort_data = sort_data;
	}

	/**
	 * @return the total_schedule
	 */
	public TotalSchedule getTotal_schedule() {
		return total_schedule;
	}

	/**
	 * @param total_schedule
	 *            the total_schedule to set
	 */
	public void setTotal_schedule(TotalSchedule total_schedule) {
		this.total_schedule = total_schedule;
	}

}
