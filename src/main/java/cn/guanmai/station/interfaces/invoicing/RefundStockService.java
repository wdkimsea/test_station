package cn.guanmai.station.interfaces.invoicing;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.station.bean.invoicing.PurchaseSkuSettleSupplier;
import cn.guanmai.station.bean.invoicing.RefundStockResultBean;
import cn.guanmai.station.bean.invoicing.param.RefundStockFilterParam;
import cn.guanmai.station.bean.invoicing.param.RefundStockParam;

/* 
* @author liming 
* @date Jan 7, 2019 11:04:11 AM 
* @des 商户退货相关接口
* @version 1.0 
*/
public interface RefundStockService {
	/**
	 * 拉取商户退货列表接口
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public List<RefundStockResultBean> searchRefundStock(RefundStockFilterParam refundStockFilterParam) throws Exception;

	/**
	 * 商户退货入库数据导出接口
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public boolean exportRefundStockData(RefundStockFilterParam refundStockFilterParam) throws Exception;

	/**
	 * 获取采购规格对应的供应商列表
	 * 
	 * @param purchase_sku_id
	 * @return
	 * @throws Exception
	 */
	public List<PurchaseSkuSettleSupplier> getPurchaseSkuSettleSupplierList(String purchase_sku_id) throws Exception;

	/**
	 * 编辑商户退货接口
	 * 
	 * @param refundList
	 * @return
	 * @throws Exception
	 */
	public boolean editRefundStock(List<RefundStockParam> refundStockList) throws Exception;

	/**
	 * 商户退货处理的一些候选值
	 * 
	 * @return
	 */
	public JSONObject refundBaseInfo() throws Exception;

}
