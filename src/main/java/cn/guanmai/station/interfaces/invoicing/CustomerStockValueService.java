package cn.guanmai.station.interfaces.invoicing;

import cn.guanmai.station.bean.invoicing.param.CustomerSpuStockLogFilterParam;
import cn.guanmai.station.bean.invoicing.CustomerStockCountBean;
import cn.guanmai.station.bean.invoicing.CustomerSpuStockLogBean;
import cn.guanmai.station.bean.invoicing.CustomerSpuStockBean;

import java.util.List;

/**
 * Created by yangjinhai on 2019/8/22.
 */
public interface CustomerStockValueService {

	/**
	 * 商户货值总计接口
	 * 
	 * @param search
	 * @return
	 * @throws Exception
	 */
	public CustomerStockCountBean getCustomerStockValueCount(String search) throws Exception;

	/**
	 * 商户货值成本获取指定商户的商品货值列表
	 * 
	 * @param address_id
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<CustomerSpuStockBean> getCustomerSpuStockList(String address_id) throws Exception;

	/**
	 * 导出商户货值
	 * 
	 * @param search
	 * @return
	 */
	public String exportCustomerSpuStocks(String search) throws Exception;

	/**
	 * 商户货值成本库存变动历史记录
	 * 
	 * @param address_id
	 * @param spu_id
	 * @return
	 * @throws Exception
	 */
	public List<CustomerSpuStockLogBean> searchCustomerSpuStockLog(CustomerSpuStockLogFilterParam filterParam)
			throws Exception;

	/**
	 * 导出商户货值成本变动历史记录
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public String exportCustomerSpuStockLog(CustomerSpuStockLogFilterParam filterParam) throws Exception;

}
