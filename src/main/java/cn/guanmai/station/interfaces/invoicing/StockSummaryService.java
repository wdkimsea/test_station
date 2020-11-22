package cn.guanmai.station.interfaces.invoicing;

import java.util.List;

import cn.guanmai.station.bean.invoicing.StockSummaryBean;
import cn.guanmai.station.bean.invoicing.StockSummaryCategoryDetailBean;
import cn.guanmai.station.bean.invoicing.InStockSummarySpuDetailBean;
import cn.guanmai.station.bean.invoicing.OutStockSummarySpuDetailBean;
import cn.guanmai.station.bean.invoicing.param.StockSummaryFilterParam;

/**
 * @author liming
 * @date 2019年7月29日 下午4:11:33
 * @des 出入库相关业务接口
 * @version 1.0
 */
public interface StockSummaryService {

	/**
	 * 入库汇总按分类统计总计
	 * 
	 * @param baseParam
	 * @return
	 * @throws Exception
	 */
	public StockSummaryBean inStockSummaryByCategory(StockSummaryFilterParam stockSummaryFilterParam) throws Exception;

	/**
	 * 入库汇总按SPU统计总计
	 * 
	 * @param baseParam
	 * @return
	 * @throws Exception
	 */
	public StockSummaryBean inStockSummaryBySpu(StockSummaryFilterParam stockSummaryFilterParam) throws Exception;

	/**
	 * 出库汇总按分类统计总计
	 * 
	 * @param baseParam
	 * @return
	 * @throws Exception
	 */
	public StockSummaryBean outStockSummaryByCategory(StockSummaryFilterParam stockSummaryFilterParam) throws Exception;

	/**
	 * 出库汇总按SPU统计总计
	 * 
	 * @param baseParam
	 * @return
	 * @throws Exception
	 */
	public StockSummaryBean outStockSummaryBySpu(StockSummaryFilterParam stockSummaryFilterParam) throws Exception;

	/**
	 * 入库汇总按分类统计
	 * 
	 * @param baseParam
	 * @return
	 * @throws Exception
	 */
	public List<StockSummaryCategoryDetailBean> inStockSummaryDetailByCategory(StockSummaryFilterParam stockSummaryFilterParam) throws Exception;

	/**
	 * 入库汇总按SPU统计
	 * 
	 * @param baseParam
	 * @return
	 * @throws Exception
	 */
	public List<InStockSummarySpuDetailBean> inStockSummaryDetailBySpu(StockSummaryFilterParam stockSummaryFilterParam) throws Exception;

	/**
	 * 出库汇总按分类统计
	 * 
	 * @param baseParam
	 * @return
	 * @throws Exception
	 */
	public List<StockSummaryCategoryDetailBean> outStockSummaryDetailByCategory(StockSummaryFilterParam stockSummaryFilterParam) throws Exception;

	/**
	 * 出库汇总按SPU统计 , 此接口采用的是新翻页组件,参数limit=10,offset=0 定值,调用一次拉去所有的数据
	 * 
	 * @param baseParam
	 * @return
	 * @throws Exception
	 */
	public List<OutStockSummarySpuDetailBean> outStockSummaryDetailBySpu(StockSummaryFilterParam stockSummaryFilterParam) throws Exception;

	/**
	 * 导出入库汇总按SPU统计
	 * 
	 * @param baseParam
	 * @return
	 * @throws Exception
	 */
	public String exportInStockSummaryBySpu(StockSummaryFilterParam stockSummaryFilterParam) throws Exception;

	/**
	 * 导出入库汇总按分类统计
	 * 
	 * @param baseParam
	 * @return
	 * @throws Exception
	 */
	public String exportInStockSummaryByCategory(StockSummaryFilterParam stockSummaryFilterParam) throws Exception;

	/**
	 * 导出出库汇总按SPU统计
	 * 
	 * @param baseParam
	 * @return
	 * @throws Exception
	 */
	public String exportOutStockSummaryBySpu(StockSummaryFilterParam stockSummaryFilterParam) throws Exception;

	/**
	 * 导出出库汇总按分类统计
	 * 
	 * @param baseParam
	 * @return
	 * @throws Exception
	 */
	public String exportOutStockSummaryByCategory(StockSummaryFilterParam stockSummaryFilterParam) throws Exception;

}
