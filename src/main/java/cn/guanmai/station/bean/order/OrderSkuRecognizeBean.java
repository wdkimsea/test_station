package cn.guanmai.station.bean.order;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date May 29, 2019 11:40:19 AM 
* @des 下单智能识别商品结果
* @version 1.0 
*/
public class OrderSkuRecognizeBean {
	private int error_num;

	@JSONField(name = "vaild")
	public List<List<Vaild>> vailds;

	public class Vaild {
		@JSONField(name = "id")
		private String sku_id;
		@JSONField(name = "name")
		private String sku_name;
		private BigDecimal sale_num;

		/**
		 * @return the sku_id
		 */
		public String getSku_id() {
			return sku_id;
		}

		/**
		 * @param sku_id the sku_id to set
		 */
		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		/**
		 * @return the sku_name
		 */
		public String getSku_name() {
			return sku_name;
		}

		/**
		 * @param sku_name the sku_name to set
		 */
		public void setSku_name(String sku_name) {
			this.sku_name = sku_name;
		}

		/**
		 * @return the sale_num
		 */
		public BigDecimal getSale_num() {
			return sale_num;
		}

		/**
		 * @param sale_num the sale_num to set
		 */
		public void setSale_num(BigDecimal sale_num) {
			this.sale_num = sale_num;
		}

	}

	/**
	 * @return the error_num
	 */
	public int getError_num() {
		return error_num;
	}

	/**
	 * @param error_num the error_num to set
	 */
	public void setError_num(int error_num) {
		this.error_num = error_num;
	}

	/**
	 * @return the vailds
	 */
	public List<List<Vaild>> getVailds() {
		return vailds;
	}

	/**
	 * @param vailds the vailds to set
	 */
	public void setVailds(List<List<Vaild>> vailds) {
		this.vailds = vailds;
	}
}
