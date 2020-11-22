package cn.guanmai.station.bean.delivery;

/* 
* @author liming 
* @date May 7, 2019 7:12:38 PM 
* @des 司机APP首页数据
* @version 1.0 
*/
public class HomePageBean {
	private int finished;

	public class month {
		private int addresses;
		private int finished;
		private int sales;

		/**
		 * @return the addresses
		 */
		public int getAddresses() {
			return addresses;
		}

		/**
		 * @param addresses
		 *            the addresses to set
		 */
		public void setAddresses(int addresses) {
			this.addresses = addresses;
		}

		/**
		 * @return the finished
		 */
		public int getFinished() {
			return finished;
		}

		/**
		 * @param finished
		 *            the finished to set
		 */
		public void setFinished(int finished) {
			this.finished = finished;
		}

		/**
		 * @return the sales
		 */
		public int getSales() {
			return sales;
		}

		/**
		 * @param sales
		 *            the sales to set
		 */
		public void setSales(int sales) {
			this.sales = sales;
		}

	}

	private int to_diliver;

	/**
	 * @return the finished
	 */
	public int getFinished() {
		return finished;
	}

	/**
	 * @param finished
	 *            the finished to set
	 */
	public void setFinished(int finished) {
		this.finished = finished;
	}

	/**
	 * @return the to_diliver
	 */
	public int getTo_diliver() {
		return to_diliver;
	}

	/**
	 * @param to_diliver
	 *            the to_diliver to set
	 */
	public void setTo_diliver(int to_diliver) {
		this.to_diliver = to_diliver;
	}

}
