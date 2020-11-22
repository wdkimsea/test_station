package cn.guanmai.station.interfaces.purchase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.order.param.OrderPurchaseTaskParam;
import cn.guanmai.station.bean.order.param.OrderSkuFilterParam;
import cn.guanmai.station.bean.purchase.PrioritySupplierBean;
import cn.guanmai.station.bean.purchase.PurcahseTaskSummaryBean;
import cn.guanmai.station.bean.purchase.PurcahseTaskSupplierBean;
import cn.guanmai.station.bean.purchase.PurchaseSpecSuppliersBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskHistoryBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskPrintBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskShareBean;
import cn.guanmai.station.bean.purchase.SupplyLimitBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean.PurchaseTaskData;
import cn.guanmai.station.bean.purchase.PurchaseTaskCanChangeSupplierBean;
import cn.guanmai.station.bean.purchase.param.PrioritySupplierFilterParam;
import cn.guanmai.station.bean.purchase.param.PurchaseSheetCreateParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskCreateParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskFilterParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskHistoryFilterParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskPrintParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskShareCreateParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskShareGetParam;
import cn.guanmai.station.bean.purchase.param.ReleasePurchaseTaskParam;
import cn.guanmai.station.bean.purchase.param.SupplyLimitFilterParam;

/* 
* @author liming 
* @date Nov 16, 2018 10:11:26 AM 
* @todo TODO
* @version 1.0 
*/
public interface PurchaseTaskService {
	/**
	 * 搜索过滤采购任务
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public PurchaseTaskBean searchPurchaseTask(PurchaseTaskFilterParam param) throws Exception;

	/**
	 * 新版采购任务过滤,从ES中拉取数据
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<PurchaseTaskData> newSearchPurchaseTask(PurchaseTaskFilterParam param) throws Exception;

	/**
	 * 查看采购任务关联的订单和采购单
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<PurchaseTaskHistoryBean> getPurchaseTaskHistorys(PurchaseTaskHistoryFilterParam filterParam)
			throws Exception;

	/**
	 * 获取采购任务总览
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public PurcahseTaskSummaryBean getPurcahseTaskSummary(PurchaseTaskFilterParam param) throws Exception;

	/**
	 * 获取建议采购数
	 * 
	 * @param pararm
	 * @return
	 * @throws Exception
	 */
	public List<SupplyLimitBean> searchSupplyLimit(SupplyLimitFilterParam param) throws Exception;

	/**
	 * 导出采购任务
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public boolean exportPurchaseTask(PurchaseTaskFilterParam param) throws Exception;

	/**
	 * 新版采购任务异步导出
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public BigDecimal exportPurchaseTaskV2(PurchaseTaskFilterParam param) throws Exception;

	/**
	 * 发布采购任务
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public boolean releasePurchaseTask(ReleasePurchaseTaskParam param) throws Exception;

	/**
	 * 生成采购单据
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public JSONArray createPruchaseSheet(PurchaseSheetCreateParam param) throws Exception;

	/**
	 * 获取所有的供应商
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getPurchaseTaskSettleSuppliers() throws Exception;

	/**
	 * 手工新建采购任务采购任务中的采购规格供应商列表
	 * 
	 * @param spec_id
	 * @return
	 * @throws Exception
	 */
	public List<PurcahseTaskSupplierBean> getPurcahseTaskSuppliers(String spec_id) throws Exception;

	/**
	 * 新建采购条目,搜索采购规格
	 * 
	 * @param spu_id
	 * @return
	 */
	public List<PurchaseSpecSuppliersBean> searchPurchaseSpecSuppliers(String spu_id) throws Exception;

	/**
	 * 获取供应商对应的采购员
	 * 
	 * @param settle_supplier_id
	 * @return
	 * @throws Exception
	 */
	public List<String> optionalSupplierSurchasers(String settle_supplier_id) throws Exception;

	/**
	 * 打印采购任务
	 * 
	 * @param purchaseTaskPrintParam
	 * @return
	 * @throws Exception
	 */
	public List<PurchaseTaskPrintBean> purchaseTaskPrint(PurchaseTaskPrintParam purchaseTaskPrintParam)
			throws Exception;

	/**
	 * 批量手工创建采购任务
	 * 
	 * @param tasks
	 * @return
	 * @throws Exception
	 */
	public boolean createPurchaseTasks(List<PurchaseTaskCreateParam> tasks) throws Exception;

	/**
	 * 采购任务切换采购员
	 * 
	 * @param task_ids
	 * @param purchaser_id
	 * @return
	 * @throws Exception
	 */
	public boolean purchaseTaskChangePurchaser(List<String> task_ids, String purchaser_id) throws Exception;

	/**
	 * 采购任务切换供应商
	 * 
	 * @param task_ids
	 * @param settle_supplier_id
	 * @return
	 * @throws Exception
	 */
	public boolean purchaseTaskChangeSupplier(List<String> task_ids, String settle_supplier_id) throws Exception;

	/**
	 * 获取采购任务可修改的供应商和采购员
	 * 
	 * @param q_type
	 * @param begin_time
	 * @param end_time
	 * @param spec_id
	 * @return
	 * @throws Exception
	 */
	public List<PurchaseTaskCanChangeSupplierBean> searchPurchaseTaskCanChangeSuppliers(int q_type, String begin_time,
			String end_time, String spec_id) throws Exception;

	/**
	 * 采购任务设置单次可供上线
	 * 
	 * @param settle_supplier_id
	 * @param spec_id
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public boolean purchaseTaskChangeSupplyLimit(String settle_supplier_id, String spec_id, BigDecimal limit)
			throws Exception;

	/**
	 * 获取优先供应商
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<PrioritySupplierBean> queryPrioritySupplier(PrioritySupplierFilterParam filterParam) throws Exception;

	/**
	 * 创建分享采购任务
	 * 
	 * @param purchaseTaskShareParam
	 * @return
	 * @throws Exception
	 */
	public String createSharePurchaseTask(PurchaseTaskShareCreateParam purchaseTaskShareParam) throws Exception;

	/**
	 * 获取分享的采购任务
	 * 
	 * @param purchaseTaskShareGetParam
	 * @return
	 * @throws Exception
	 */
	public List<PurchaseTaskShareBean> getSharePurchaseTask(PurchaseTaskShareGetParam purchaseTaskShareGetParam)
			throws Exception;

	/**
	 * 订单手工创建生成采购任务
	 * 
	 * @param orderSkuFilterParam
	 * @return
	 * @throws Exception
	 */
	public BigDecimal createPurchaseTaskByOrder(OrderSkuFilterParam orderSkuFilterParam) throws Exception;

	/**
	 * 选择个别商品手工进入采购
	 * 
	 * @param orderPurchaseTaskParams
	 * @return
	 * @throws Exception
	 */
	public BigDecimal createPurchaseTaskByOrder(List<OrderPurchaseTaskParam> orderPurchaseTaskParams) throws Exception;

}
