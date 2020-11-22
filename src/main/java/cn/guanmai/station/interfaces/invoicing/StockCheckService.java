package cn.guanmai.station.interfaces.invoicing;

import java.math.BigDecimal;
import java.util.List;

import cn.guanmai.station.bean.category.CategoriesBean;
import cn.guanmai.station.bean.invoicing.BatchLogBean;
import cn.guanmai.station.bean.invoicing.SpuStockBean;
import cn.guanmai.station.bean.invoicing.StockBatchBean;
import cn.guanmai.station.bean.invoicing.StockBatchCheckResultBean;
import cn.guanmai.station.bean.invoicing.StockChangeLogBean;
import cn.guanmai.station.bean.invoicing.param.SpuStockCheckParam;
import cn.guanmai.station.bean.invoicing.param.StockAvgPriceUpdateParam;
import cn.guanmai.station.bean.invoicing.param.BatchStockCheckParam;
import cn.guanmai.station.bean.invoicing.param.StockChangeLogFilterParam;
import cn.guanmai.station.bean.invoicing.param.StockCheckFilterParam;
import cn.guanmai.station.bean.invoicing.param.StockCheckTemplateParam;

/* 
* @author liming 
* @date Feb 28, 2019 9:56:06 AM 
* @des 商品盘点相关接口
* @version 1.0 
*/
public interface StockCheckService {

	/**
	 * 获取站点所有的一二级分类,进销存分类过滤搜索的参数值
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CategoriesBean> getCategories() throws Exception;

	/**
	 * 按商品盘点列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SpuStockBean> searchStockCheck(StockCheckFilterParam filterParam) throws Exception;

	/**
	 * 获取指定SPU的库存信息
	 * 
	 * @param spu_id
	 * @return
	 * @throws Exception
	 */
	public SpuStockBean getSpuStock(String spu_id) throws Exception;

	/**
	 * 修改SPU库存
	 * 
	 * @param spu_id
	 * @param new_stock
	 * @param remark
	 * @return
	 * @throws Exception
	 */
	public boolean editSpuStock(String spu_id, BigDecimal new_stock, String remark) throws Exception;

	/**
	 * 下载批量盘点模板
	 * 
	 * @return
	 * @throws Exception
	 */
	public BigDecimal downloadStockCheckTemplate() throws Exception;

	/**
	 * 先进先出站点下载批量盘点模板(步骤1创建异步任务)
	 * 
	 * @param stockCheckTemplateParam
	 * @return
	 * @throws Exception
	 */
	public BigDecimal downloadStockCheckTemplateStep1(StockCheckTemplateParam stockCheckTemplateParam) throws Exception;

	/**
	 * 先进先出站点下载批量盘点模板(步骤2下载模板)
	 * 
	 * @param link
	 * @return
	 * @throws Exception
	 */
	public String downloadStockCheckTemplateStep2(String link) throws Exception;

	/**
	 * 先进先出站点上传批量盘点模板
	 * 
	 * @param file_path
	 * @return
	 * @throws Exception
	 */
	public List<StockBatchCheckResultBean> uploadStockCheckTemplate(String file_path) throws Exception;

	/**
	 * 先进先出站点批次盘点
	 * 
	 * @param stockBatchCheckParams
	 * @return
	 * @throws Exception
	 */
	public boolean checkBatchStock(List<BatchStockCheckParam> stockBatchCheckParams) throws Exception;

	/**
	 * 导出库存盘点数据
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public boolean downloadStockCheckFile(StockCheckFilterParam filterParam) throws Exception;

	/**
	 * 安全库存提醒接口
	 * 
	 * @return
	 * @throws Exception
	 */
	public String stockWarning() throws Exception;

	/**
	 * 设置SPU安全库存下线阈值
	 * 
	 * @param spu_id
	 * @param threshold
	 * @return
	 * @throws Exception
	 */
	public boolean setSpuStockLowerThreshold(String spu_id, BigDecimal threshold) throws Exception;

	/**
	 * 设置SPU安全库存上线阈值
	 * 
	 * @param spu_id
	 * @param threshold
	 * @return
	 * @throws Exception
	 */
	public boolean setSpuStockUpperThreshold(String spu_id, BigDecimal threshold) throws Exception;

	/**
	 * 取消SPU安全库存下线阈值
	 * 
	 * @param spu_id
	 * @return
	 * @throws Exception
	 */
	public boolean cancelSpuStockLowerThreshold(String spu_id) throws Exception;

	/**
	 * 取消SPU安全库存上线阈值
	 * 
	 * @param spu_id
	 * @return
	 * @throws Exception
	 */
	public boolean cancelSpuStockUpperThreshold(String spu_id) throws Exception;

	/**
	 * 获取SPU库存变动记录
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<StockChangeLogBean> getSpuStockChangeLogList(StockChangeLogFilterParam filterParam) throws Exception;

	/**
	 * 获取过滤条件的所有库存变动历史记录
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<StockChangeLogBean> getSpuStockChangeLogs(StockChangeLogFilterParam filterParam) throws Exception;

	/**
	 * SPU库存批量盘点(加权平均站点)
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public BigDecimal spuBatchStockCheck(List<SpuStockCheckParam> param) throws Exception;

	/**
	 * 修复库存均价
	 * 
	 * @param updateParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateStockAveragePrice(List<StockAvgPriceUpdateParam> updateParam) throws Exception;

	/*******************************************************************************************/
	/**********************************
	 * 以下是先进先出站点的盘点业务接口
	 ***************************/
	/*******************************************************************************************/

	/**
	 * 拉取指定SPU对应的批次信息列表
	 * 
	 * @param text
	 * @param offset
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<StockBatchBean> searchStockBatch(String text, int offset, int limit) throws Exception;

	/**
	 * 获取指定批次的信息
	 * 
	 * @param batch_number
	 * @return
	 * @throws Exception
	 */
	public StockBatchBean getStockBatch(String batch_number) throws Exception;

	/**
	 * 先进先出站点批次盘点操作
	 * 
	 * @param batch_number
	 * @param new_remain
	 * @param remark
	 * @return
	 * @throws Exception
	 */
	public boolean editBatchStock(String batch_number, BigDecimal new_remain, String remark) throws Exception;

	/**
	 * 获取指定批次的库存历史记录
	 * 
	 * @param start_time
	 * @param end_time
	 * @param batch_number
	 * @return
	 */
	public List<BatchLogBean> getBatchLog(String start_time, String end_time, String batch_number) throws Exception;

}
