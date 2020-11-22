package cn.guanmai.station.interfaces.invoicing;

import java.util.List;

import cn.guanmai.station.bean.invoicing.RefundStockRecordBean;
import cn.guanmai.station.bean.invoicing.SplitStockInRecordBean;
import cn.guanmai.station.bean.invoicing.SplitStockOutRecordBean;
import cn.guanmai.station.bean.invoicing.StockAbandonGoodsRecordBean;
import cn.guanmai.station.bean.invoicing.InStockRecordBean;
import cn.guanmai.station.bean.invoicing.StockIncreaseRecordBean;
import cn.guanmai.station.bean.invoicing.StockLossRecordBean;
import cn.guanmai.station.bean.invoicing.OutStockRecordBean;
import cn.guanmai.station.bean.invoicing.ReturnStockRecordBean;
import cn.guanmai.station.bean.invoicing.param.InStockRecordFilterParam;
import cn.guanmai.station.bean.invoicing.param.OutStockRecordFilterParam;
import cn.guanmai.station.bean.invoicing.param.StockRecordFilterParam;

/* 
* @author liming 
* @date Jan 8, 2019 7:37:59 PM 
* @des 库存记录相关接口
* @version 1.0 
*/
public interface StockRecordService {
	/**
	 * 成品入库记录
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public List<InStockRecordBean> inStockRecords(InStockRecordFilterParam stockInRecordFilterParam)
			throws Exception;

	/**
	 * 商户退货入库记录
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public List<RefundStockRecordBean> refundRecords(StockRecordFilterParam paramBean) throws Exception;

	/**
	 * 商户退货放弃取货记录
	 * 
	 * @param stockRecordFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<StockAbandonGoodsRecordBean> abandonGoodsRecords(StockRecordFilterParam stockRecordFilterParam)
			throws Exception;

	/**
	 * 成品出库记录
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public List<OutStockRecordBean> outStockRecords(OutStockRecordFilterParam paramBean) throws Exception;

	/**
	 * 成品退货记录
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public List<ReturnStockRecordBean> returnStockRecords(StockRecordFilterParam paramBean) throws Exception;

	/**
	 * 库存报溢记录
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public List<StockIncreaseRecordBean> increaseStockRecords(StockRecordFilterParam paramBean) throws Exception;

	/**
	 * 库存报损记录
	 * 
	 * @param stockRecordFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<StockLossRecordBean> lossStockRecords(StockRecordFilterParam stockRecordFilterParam)
			throws Exception;

	/**
	 * 分割入库记录
	 * 
	 * @param stockRecordFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<SplitStockInRecordBean> splitStockInRecords(StockRecordFilterParam stockRecordFilterParam)
			throws Exception;

	/**
	 * 分割出库记录
	 * 
	 * @param stockRecordFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<SplitStockOutRecordBean> splitStockOutRecords(StockRecordFilterParam stockRecordFilterParam)
			throws Exception;
	
	

}
