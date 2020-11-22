package cn.guanmai.open.interfaces.stock;

import java.util.List;

import cn.guanmai.open.bean.stock.OpenStockBean;
import cn.guanmai.open.bean.stock.param.OpenStockFilterParam;

/**
 * @author liming
 * @date 2019年12月18日
 * @time 上午10:52:54
 * @des TODO
 */

public interface OpenStockService {
	/**
	 * 库存列表
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<OpenStockBean> queryStock(OpenStockFilterParam filterParam) throws Exception;

	/**
	 * 修改库存
	 * 
	 * @param spu_id
	 * @param stock
	 * @param remark
	 * @return
	 * @throws Exception
	 */
	public boolean updateStock(String spu_id, String stock, String remark) throws Exception;

}
