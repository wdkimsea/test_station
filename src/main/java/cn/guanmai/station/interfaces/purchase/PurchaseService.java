package cn.guanmai.station.interfaces.purchase;

import cn.guanmai.station.bean.purchase.PurchaseSpecRefPriceBean;

/**
 * @author: liming
 * @Date: 2020年3月13日 上午10:09:44
 * @description:
 * @version: 1.0
 */

public interface PurchaseService {
	/**
	 * 获取采购规格最近的一些价格
	 * 
	 * @param spu_id
	 * @param purchase_spec
	 * @param settle_supplier_id
	 * @return
	 * @throws Exception
	 */
	public PurchaseSpecRefPriceBean getPurchaseSpecRefPrice(String spu_id, String purchase_spec_id,
			String settle_supplier_id) throws Exception;;
}
