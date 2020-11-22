package cn.guanmai.station.interfaces.purchase;

import java.util.List;

import cn.guanmai.station.bean.purchase.PurchaseSheetBean;
import cn.guanmai.station.bean.purchase.PurchaseSheetDetailBean;
import cn.guanmai.station.bean.purchase.PurchaseSheetShareBean;
import cn.guanmai.station.bean.purchase.param.PurchaseSheetShareParam;

/* 
* @author liming 
* @date Nov 28, 2018 5:59:00 PM 
* @des 采购单据相关接口
* @version 1.0 
*/
public interface PurchaseSheetService {
	/**
	 * 搜索过滤采购单据
	 * 
	 * @param start_time         2018-11-28
	 * @param end_time           2018-11-28
	 * @param sheet_no           非必填参数
	 * @param settle_supplier_id 非必填参数
	 * @param status             非必填参数
	 * @return
	 */
	public List<PurchaseSheetBean> purchaseSheetArray(String start_time, String end_time, String sheet_no,
			String settle_supplier_id, String status) throws Exception;

	/**
	 * 删除采购单据
	 * 
	 * @param sheet_no
	 * @return
	 * @throws Exception
	 */
	public boolean deletePurchaseSheet(String sheet_no) throws Exception;

	/**
	 * 获取采购单据详情
	 * 
	 * @param sheet_no
	 * @return
	 */
	public PurchaseSheetDetailBean getPurchaseSheetDetail(String sheet_no) throws Exception;

	/**
	 * 提交采购单据
	 * 
	 * @param sheet_no
	 * @return
	 * @throws Exception
	 */
	public boolean submitPurchaseSheet(String sheet_no) throws Exception;

	/**
	 * 创建分享采购单据
	 * 
	 * @param sheet_no
	 * @return
	 * @throws Exception
	 */
	public String createSharePurchaseSheet(String sheet_no) throws Exception;

	/**
	 * 获取分享的采购单据
	 * 
	 * @param purchaseSheetShareParam
	 * @return
	 * @throws Exception
	 */
	public PurchaseSheetShareBean getSharePurchaseSheet(PurchaseSheetShareParam purchaseSheetShareParam)
			throws Exception;

}
