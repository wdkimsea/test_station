package cn.guanmai.open.interfaces.stock;

import cn.guanmai.open.bean.stock.OpenStockLedgerBean;
import cn.guanmai.open.bean.stock.OpenStockLedgerDetailBean;
import cn.guanmai.open.bean.stock.param.OpenStockLegerFilterParam;


/**
 * @author liming
 * @date 2019年10月22日
 * @time 下午7:27:28
 * @des TODO
 */

public interface OpenStockLedgerService {

	/**
	 * 查询总账列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public OpenStockLedgerBean queryStockLeger(OpenStockLegerFilterParam stockLegerFilterParam) throws Exception;

	/**
	 * 查询总账详情
	 * 
	 * @param stockLegerFilterParam
	 * @return
	 * @throws Exception
	 */
	public OpenStockLedgerDetailBean getStockLegerDetail(OpenStockLegerFilterParam stockLegerFilterParam)
			throws Exception;
}
