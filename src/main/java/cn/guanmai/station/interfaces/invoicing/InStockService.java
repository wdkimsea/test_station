package cn.guanmai.station.interfaces.invoicing;

import java.math.BigDecimal;
import java.util.List;

import cn.guanmai.station.bean.invoicing.InStockDetailInfoBean;
import cn.guanmai.station.bean.invoicing.InStockSheetBean;
import cn.guanmai.station.bean.invoicing.param.InStockCreateParam;
import cn.guanmai.station.bean.invoicing.param.InStockSheetFilterParam;
import cn.guanmai.station.bean.invoicing.param.InStockSheetImportParam;

/* 
* @author liming 
* @date Feb 18, 2019 11:26:44 AM 
* @des 成品入库相关接口
* @version 1.0 
*/
public interface InStockService {
	/**
	 * 创建成品入库单
	 * 
	 * @param settle_supplier_id
	 * @param supplier_name
	 * @return
	 * @throws Exception
	 */
	public String createInStockSheet(String settle_supplier_id, String supplier_name) throws Exception;

	/**
	 * 新版采购入库,一步建立
	 * 
	 * @param inStockCreateParam
	 * @return
	 * @throws Exception
	 */
	public String createInStockSheet(InStockCreateParam inStockCreateParam) throws Exception;

	/**
	 * 获取入库单详细信息
	 * 
	 * @param sheet_id
	 * @return
	 * @throws Exception
	 */
	public InStockDetailInfoBean getInStockSheetDetail(String sheet_id) throws Exception;

	/**
	 * 获取指定供应商指定商品的入库均价
	 * 
	 * @param spec_id
	 * @param settle_supplier_id
	 * @return
	 * @throws Exception
	 */
	public BigDecimal getSupplieraveragePrice(String spec_id, String settle_supplier_id) throws Exception;

	/**
	 * 提交修改入库单
	 * 
	 * @param stockIn
	 * @return
	 * @throws Exception
	 */
	public boolean modifyInStockSheet(InStockDetailInfoBean stockIn) throws Exception;

	/**
	 * 成品入库单审核不通过操作接口
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean reviewInStockSheet(String sheet_id) throws Exception;

	/**
	 * 入库单审核不通过操作
	 * 
	 * @param sheet_id
	 * @return
	 * @throws Exception
	 */
	public boolean cancelInStockSheet(String sheet_id) throws Exception;

	/**
	 * 新增打印入库单日志
	 * 
	 * @param sheet_ids
	 * @return
	 * @throws Exception
	 */
	public boolean createInStockSheetPrintLog(List<String> sheet_ids) throws Exception;

	/**
	 * 打印成品入库单,拉取数据接口
	 * 
	 * @param sheet_ids
	 * @return
	 * @throws Exception
	 */
	public List<InStockDetailInfoBean> printInStockSheetDetail(List<String> sheet_ids) throws Exception;

	/**
	 * 导出成品入库单详细信息
	 * 
	 * @param sheet_id
	 * @return
	 * @throws Exception
	 */
	public boolean exportInStockSheetDetail(String sheet_id) throws Exception;

	/**
	 * 搜索过滤入库单
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<InStockSheetBean> searchInStockSheet(InStockSheetFilterParam filterParam) throws Exception;

	/**
	 * 导出成品入库单
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean exportInStockSheet(InStockSheetFilterParam filterParam) throws Exception;

	/**
	 * 下载批量导入入库单模板
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean downloadInStockSheetTemplate() throws Exception;

	/**
	 * 批量导入采购入库单
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public boolean importInStockSheet(List<InStockSheetImportParam> param) throws Exception;

}
