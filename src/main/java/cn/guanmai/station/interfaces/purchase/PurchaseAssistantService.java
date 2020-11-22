package cn.guanmai.station.interfaces.purchase;

import java.math.BigDecimal;
import java.util.List;

import cn.guanmai.station.bean.purchase.PurchaserBean;
import cn.guanmai.station.bean.purchase.assistant.DailyCountBean;
import cn.guanmai.station.bean.purchase.assistant.DailyWorkBean;
import cn.guanmai.station.bean.purchase.assistant.PurchaseAssistantTaskBean;
import cn.guanmai.station.bean.purchase.assistant.PurchaseSheetBean;
import cn.guanmai.station.bean.purchase.assistant.PurchaseSheetCountBean;
import cn.guanmai.station.bean.purchase.assistant.PurchaseSheetDetailBean;
import cn.guanmai.station.bean.purchase.assistant.SupplierCountBean;
import cn.guanmai.station.bean.purchase.assistant.SupplierSpecBean;
import cn.guanmai.station.bean.purchase.assistant.SupplySkuBean;
import cn.guanmai.station.bean.purchase.param.assistant.SheetCreateParam;
import cn.guanmai.station.bean.purchase.param.assistant.TaskFiterParam;
import cn.guanmai.station.bean.system.ServiceTimeBean;

/* 
* @author liming 
* @date May 23, 2019 10:36:08 AM 
* @des 采购助手APP相关接口
* @version 1.0 
*/
public interface PurchaseAssistantService {
	/**
	 * 获取采购助手登录账号的基本信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public PurchaserBean getPurchaserInfo() throws Exception;

	/**
	 * 获取登录账号绑定的供应商列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getQuotedSettleSuppliers() throws Exception;

	/**
	 * 采购助手获取运营时间
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ServiceTimeBean> getPurchaseAssistantServiceTime() throws Exception;

	/**
	 * 采购助手今日任务
	 * 
	 * @return
	 * @throws Exception
	 */
	public DailyWorkBean getPurchaseAssistantDailyWork() throws Exception;

	/**
	 * 采购金额趋势汇总
	 * 
	 * @param days
	 * @return
	 * @throws Exception
	 */
	public DailyCountBean getPurchaseAssistantDailyCount(int days) throws Exception;

	/**
	 * 采购金额分布统计(供应商维度)
	 * 
	 * @param days
	 * @return
	 * @throws Exception
	 */
	public SupplierCountBean getPurchaseAssistantSupplierCount(int days) throws Exception;

	/**
	 * 获取供应商对应的采购规格列表
	 * 
	 * @param supplier_id
	 * @param with_task
	 * @param search_text
	 * @return
	 * @throws Exception
	 */
	public List<SupplierSpecBean> getSupplierSpecs(String supplier_id, int with_task, String search_text)
			throws Exception;

	/**
	 * 更新采购规格询价
	 * 
	 * @param spec_id
	 *            采购规格ID
	 * @param price
	 *            采购规格询价
	 * @param remark
	 *            采购规格备注
	 * @param origin_place
	 *            采购规格产地
	 * @param settle_supplier_id
	 *            采购规格供应商
	 * @return
	 * @throws Exception
	 */
	public boolean updateSpecQuotedPrice(String spec_id, BigDecimal price, String remark, String origin_place,
			String settle_supplier_id) throws Exception;

	/**
	 * 采购助手过滤采购任务
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<PurchaseAssistantTaskBean> searchPurchaseAssistantTask(TaskFiterParam filterParam) throws Exception;

	/**
	 * 采购助手新建采购任务
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<String> createPurchaseAssistantSheet(SheetCreateParam param) throws Exception;

	/**
	 * 采购助手指定供应商能提供的采购规格
	 * 
	 * @param settle_supplier_id
	 * @return
	 * @throws Exception
	 */
	public List<SupplySkuBean> getSettleSupplierSupplySkus(String settle_supplier_id) throws Exception;

	/**
	 * 采购助手采购单据页面统计汇总
	 * 
	 * @param begin_time
	 * @param end_time
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public PurchaseSheetCountBean getPurchaseSheetCountInfo(String begin_time, String end_time, int status)
			throws Exception;

	/**
	 * 采购助手-采购单据页面-采购单据列表
	 * 
	 * @param begin_time
	 * @param end_time
	 * @param sort_type
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public List<PurchaseSheetBean> getPurchaseSheets(String begin_time, String end_time, int sort_type, int status)
			throws Exception;

	/**
	 * 采购助手-采购单据页面-获取采购单据详情
	 * 
	 * @param sheet_no
	 * @return
	 * @throws Exception
	 */
	public PurchaseSheetDetailBean getPurchaseSheetDetail(String sheet_no) throws Exception;

	/**
	 * 采购助手-编辑采购单据详情
	 * 
	 * @param sheet_no
	 * @param purchaseSheetDetail
	 * @return
	 * @throws Exception
	 */
	public boolean modifyPurchaseSheet(String sheet_no, PurchaseSheetDetailBean purchaseSheetDetail) throws Exception;

	/**
	 * 删除采购单据
	 * 
	 * @param sheet_no
	 * @return
	 * @throws Exception
	 */
	public boolean deletePurchaseSheet(String sheet_no) throws Exception;

	/**
	 * 提交采购单据
	 * 
	 * @param sheet_no
	 * @return
	 * @throws Exception
	 */
	public boolean submitPurchaseAssistantSheet(String sheet_no) throws Exception;

	/**
	 * 采购单据标记完成
	 * 
	 * @param release_ids
	 * @return
	 * @throws Exception
	 */
	public boolean finishTask(List<BigDecimal> release_ids) throws Exception;

}
