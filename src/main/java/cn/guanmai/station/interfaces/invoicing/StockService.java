package cn.guanmai.station.interfaces.invoicing;

import java.util.List;
import java.util.Map;

import cn.guanmai.station.bean.invoicing.StockSettleSupplierBean;
import cn.guanmai.station.bean.invoicing.SupplySkuBean;
import cn.guanmai.station.bean.invoicing.param.StockCostReportFilterParam;

/* 
* @author liming 
* @date Feb 27, 2019 11:37:47 AM 
* @des 进销存公用接口
* @version 1.0 
*/
public interface StockService {
	public List<SupplySkuBean> getSupplySkuList(String name, String sheet_id) throws Exception;

	/**
	 * 新版搜索采购入库商品
	 * 
	 * @param name
	 * @param settle_supplier_id
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<SupplySkuBean>> newSearchSupplySku(String name, String settle_supplier_id) throws Exception;

	/**
	 * 货值成本表
	 * 
	 * @param filterParam
	 * @return
	 */
	public boolean getStockCostReport(StockCostReportFilterParam filterParam) throws Exception;

	/**
	 * 获取供应商列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<StockSettleSupplierBean> getStockSettleSuppliers() throws Exception;

}
