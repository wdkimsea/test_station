package cn.guanmai.open.interfaces.stock;

import java.util.List;

import cn.guanmai.open.bean.stock.OpenStockSaleRefundBean;
import cn.guanmai.open.bean.stock.param.OpenStockSaleRefundFilterParam;

/**
 * @author liming
 * @date 2019年12月18日
 * @time 上午10:40:38
 * @des TODO
 */

public interface OpenStockSaleRefundService {
	/**
	 * 获取商户退货列表
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<OpenStockSaleRefundBean> filterStockSaleRefund(OpenStockSaleRefundFilterParam filterParam)
			throws Exception;

}
