package cn.guanmai.station.interfaces.invoicing;

import cn.guanmai.station.bean.invoicing.*;
import cn.guanmai.station.bean.invoicing.param.TransferLogFilterParam;
import cn.guanmai.station.bean.invoicing.param.TransferSheetCreateParam;
import cn.guanmai.station.bean.invoicing.param.TransferSheetFilterParam;
import cn.guanmai.station.bean.invoicing.param.TransferStockBatchFilterParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yangjinhai on 2019/8/7.
 */
public interface StockTransferService {

	/**
	 * 获取货位信息接口
	 */
	public List<StockTransferShelfBean> getStockInTransferShelf() throws Exception;

	/**
	 * 仓类移库搜索批次
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<TransferStockBatchBean> searchStockBatchNumber(TransferStockBatchFilterParam filterParam)
			throws Exception;

	/**
	 * 获取SPU列表
	 * 
	 * @param spuName
	 * @return
	 * @throws Exception
	 */
	public List<TransferSpuBean> searchTransferSpu(String spuName) throws Exception;

	/**
	 * 创建仓内移库单
	 */
	public String createStockTransfer(TransferSheetCreateParam stockTransferSheet) throws Exception;

	/**
	 * 移库单搜索过滤
	 * 
	 * @param transferSheetFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<TransferSheetBean> searchTransferSheet(TransferSheetFilterParam transferSheetFilterParam)
			throws Exception;

	/**
	 * 修改仓内移库单
	 * 
	 * @param transferSheetUpdateParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateTransferShee(TransferSheetCreateParam transferSheetUpdateParam) throws Exception;

	/**
	 * 根据sheet_no(移库单号)查看移库单详情
	 */
	public TransferSheetDetailBean getTransferSheetDetail(String sheet_no) throws Exception;

	/**
	 * 导出仓内移库列表
	 * 
	 * @param transferSheetFilterParam
	 * @return
	 * @throws Exception
	 */
	public BigDecimal exportTransferSheets(TransferSheetFilterParam transferSheetFilterParam) throws Exception;

	/**
	 * 搜索过滤移库记录
	 * 
	 * @param transferLogFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<TransferLogBean> searchTransferLog(TransferLogFilterParam transferLogFilterParam) throws Exception;

	/**
	 * 导出仓内移库记录
	 * 
	 * @param transferLogFilterParam
	 * @return
	 * @throws Exception
	 */
	public BigDecimal exportTransferLogs(TransferLogFilterParam transferLogFilterParam) throws Exception;

}
