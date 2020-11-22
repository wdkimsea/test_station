package cn.guanmai.station.interfaces.invoicing;

import java.util.List;

import cn.guanmai.station.bean.invoicing.BatchOutStockBean;
import cn.guanmai.station.bean.invoicing.OutStockDetailBean;
import cn.guanmai.station.bean.invoicing.OutStockSheetBean;
import cn.guanmai.station.bean.invoicing.OutStockRemindBean;
import cn.guanmai.station.bean.invoicing.StockSaleSkuBean;
import cn.guanmai.station.bean.invoicing.param.NegativeStockRemindParam;
import cn.guanmai.station.bean.invoicing.param.OutStockModifyParam;
import cn.guanmai.station.bean.invoicing.param.OutStockPriceUpdateParam;
import cn.guanmai.station.bean.invoicing.param.OutStockSheetCreateParam;
import cn.guanmai.station.bean.invoicing.param.OutStockSheetFilterParam;

/* 
* @author liming 
* @date Feb 26, 2019 9:48:06 AM 
* @des 销售出库单相关业务接口
* @version 1.0 
*/
public interface OutStockService {
	/**
	 * 搜索查询出库单
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<OutStockSheetBean> searchOutStockSheet(OutStockSheetFilterParam outStockSheetFilterParam)
			throws Exception;

	/**
	 * 获取指定出库单的详细信息
	 * 
	 * @param sheet_id
	 * @return
	 * @throws Exception
	 */
	public OutStockDetailBean getOutStockDetailInfo(String sheet_id) throws Exception;

	/**
	 * 订单是否同步生成出库单
	 * 
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	public boolean asyncOrderToOutStockSheet(String order_id) throws Exception;

	/**
	 * 出库单手动出库
	 * 
	 * @param outStockModifyParam
	 * @return
	 * @throws Exception
	 */
	public boolean modifyOutStockSheet(OutStockModifyParam outStockModifyParam) throws Exception;

	/**
	 * 冲销出库单
	 * 
	 * @param sheet_id
	 * @return
	 * @throws Exception
	 */
	public boolean cancelOutStockSheet(String sheet_id) throws Exception;

	/**
	 * 手工出库库存不足提醒
	 * 
	 * @param negativeStockRemindParam
	 * @return
	 * @throws Exception
	 */
	public List<OutStockRemindBean> singleOutStockRemind(NegativeStockRemindParam negativeStockRemindParam)
			throws Exception;

	/**
	 * 批量出库负库存提醒
	 * 
	 * @param outStockSheetFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<OutStockRemindBean> batchOutStockRemind(OutStockSheetFilterParam outStockSheetFilterParam)
			throws Exception;

	/**
	 * 批量出库负库存提醒
	 * 
	 * @param out_stock_sheet_ids
	 * @return
	 * @throws Exception
	 */
	public List<OutStockRemindBean> batchOutStockRemind(List<String> out_stock_sheet_ids) throws Exception;

	/**
	 * 新的批量出库负库存提醒
	 * 
	 * @param outStockSheetFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<OutStockRemindBean> newBatchOutStockRemind(OutStockSheetFilterParam outStockSheetFilterParam)
			throws Exception;

	/**
	 * 新的批量出库负库存提醒
	 * 
	 * @param out_stock_sheet_ids
	 * @return
	 * @throws Exception
	 */
	public List<OutStockRemindBean> newBatchOutStockRemind(List<String> out_stock_sheet_ids) throws Exception;

	/**
	 * 导出成品出库单
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public boolean exportOutStockSheet(OutStockSheetFilterParam filterParam) throws Exception;

	/**
	 * 批量出库
	 * 
	 * @param filterParam
	 * @return task_url
	 * @throws Exception
	 */
	public String batchOutStock(OutStockSheetFilterParam filterParam) throws Exception;

	/**
	 * 手工新建销售出库单
	 * 
	 * @param out_stock_target
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean createOutStockSheet(String out_stock_target, String id) throws Exception;

	/**
	 * 新版手工新建销售出库单
	 * 
	 * @param stockOutSheetCreateParam
	 * @return
	 * @throws Exception
	 */
	public boolean createOutStockSheet(OutStockSheetCreateParam stockOutSheetCreateParam) throws Exception;

	/**
	 * 手工修复出库均价
	 * 
	 * @param outStockPriceUpdateParams
	 * @return
	 * @throws Exception
	 */
	public boolean updateOutStockPrice(List<OutStockPriceUpdateParam> outStockPriceUpdateParams) throws Exception;

	/**
	 * 销售出库单打印
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public List<OutStockDetailBean> printStockOutSheet(List<String> ids) throws Exception;

	/**
	 * 出库搜索查询销售商品
	 * 
	 * @param name
	 * @param out_stock_customer_id
	 * @return
	 * @throws Exception
	 */
	public List<StockSaleSkuBean> searchStockSaleSku(String name, String out_stock_customer_id) throws Exception;

	/**********************************************************************************/
	/****************************** 以下是先进先出站点的接口 *****************************/
	/**********************************************************************************/

	/**
	 * 先进先出成品出库手工出库拉取批次接口
	 * 
	 * @param sku_id
	 * @param q
	 * @return
	 * @throws Exception
	 */
	public List<BatchOutStockBean> searchOutStockBatch(String sku_id, String q) throws Exception;

	/**
	 * 先进先出站点提交出库单
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean modifyStockOutSheetInFIFO(OutStockDetailBean stockOutDetailBean) throws Exception;

}
