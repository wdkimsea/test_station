package cn.guanmai.station.interfaces.invoicing;

import java.util.List;

import cn.guanmai.station.bean.invoicing.ReturnStockBatchBean;
import cn.guanmai.station.bean.invoicing.ReturnStockDetailBean;
import cn.guanmai.station.bean.invoicing.ReturnStockSheetBean;
import cn.guanmai.station.bean.invoicing.param.ReturnStockCreateParam;
import cn.guanmai.station.bean.invoicing.param.ReturnStockSheetFilterParam;
import cn.guanmai.station.bean.invoicing.param.ReturnStockSheetImportParam;

/* 
* @author liming 
* @date Feb 27, 2019 10:56:26 AM 
* @des 成品退货相关接口
* @version 1.0 
*/
public interface ReturnStockService {
	/**
	 * 新建成品退货单
	 * 
	 * @param settle_supplier_id
	 * @param supplier_name
	 * @return
	 * @throws Exception
	 */
	public String createReturnStockSheet(String settle_supplier_id, String supplier_name) throws Exception;

	/**
	 * 
	 * @param stockReturnCreateParam
	 * @return
	 * @throws Exception
	 */
	public String newCreateReturnStockSheet(ReturnStockCreateParam returnStockCreateParam) throws Exception;

	/**
	 * 获取成品退货单详细信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ReturnStockDetailBean getRetrunStockDetail(String id) throws Exception;

	/**
	 * 修改提交成品退货单
	 * 
	 * @param stockRetrun
	 * @return
	 * @throws Exception
	 */
	public boolean modifyReturnStockSheet(ReturnStockDetailBean stockReturn) throws Exception;

	/**
	 * 冲销成品退货单
	 * 
	 * @param sheet_id
	 * @return
	 * @throws Exception
	 */
	public boolean cancelReturnStockSheet(String sheet_id) throws Exception;

	/**
	 * 导出成品退货单
	 * 
	 * @param sheet_id
	 * @return
	 * @throws Exception
	 */
	public boolean exportReturnStockDetailInfo(String sheet_id) throws Exception;

	/**
	 * 搜索过滤成品退货单
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<ReturnStockSheetBean> searchReturnStockSheet(ReturnStockSheetFilterParam filterParam) throws Exception;

	/**
	 * 导出成品退货单列表
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public boolean exportReturnStockSheet(ReturnStockSheetFilterParam filterParam) throws Exception;

	/**
	 * 导出成品退货批量导入模板
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean downReturnStockTemplate() throws Exception;

	/**
	 * 采购退货批量导入
	 * 
	 * @param paramList
	 * @return
	 * @throws Exception
	 */
	public boolean importReturnStockSheet(List<ReturnStockSheetImportParam> paramList) throws Exception;

	/*****************************************************************************************************/
	/**************************************
	 * 以下是先进先出站点特有接口
	 **************************************/
	/*****************************************************************************************************/

	/**
	 * 搜索退货批次信息
	 * 
	 * @param purchase_spec_id
	 * @param supplier_id
	 * @return
	 * @throws Exception
	 */
	public List<ReturnStockBatchBean> searchReturnStockBatch(String purchase_spec_id, String supplier_id)
			throws Exception;

}
