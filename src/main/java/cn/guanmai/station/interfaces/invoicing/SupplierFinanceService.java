package cn.guanmai.station.interfaces.invoicing;

import java.math.BigDecimal;
import java.util.List;

import cn.guanmai.station.bean.invoicing.SettleSheetDetailBean;
import cn.guanmai.station.bean.invoicing.SettlementBean;
import cn.guanmai.station.bean.invoicing.SettlementCollectBean;
import cn.guanmai.station.bean.invoicing.SettlementDetailBean;
import cn.guanmai.station.bean.invoicing.SettlementDetailPageBean;
import cn.guanmai.station.bean.invoicing.SettleSheetBean;
import cn.guanmai.station.bean.invoicing.param.SettleSheetDetailSubmitParam;
import cn.guanmai.station.bean.invoicing.param.SettleSheetDetailFilterParam;
import cn.guanmai.station.bean.invoicing.param.SettleSheetFilterParam;
import cn.guanmai.station.bean.invoicing.param.SettlementFilterParam;

/* 
* @author liming 
* @date Mar 28, 2019 2:34:12 PM 
* @des 供应商结算相关接口
* @version 1.0 
*/
public interface SupplierFinanceService {
	/**
	 * 搜索过滤结款单
	 * 
	 * @param type
	 * @param start
	 * @param end
	 * @param receipt_type
	 * @param settle_supplier_id
	 * @return
	 * @throws Exception
	 */
	public List<SettleSheetBean> searchSettleSheet(SettleSheetFilterParam settleSheetFilterParam) throws Exception;

	/**
	 * 代处理单据加入结款单
	 * 
	 * @param settle_supplier_id
	 * @param sheet_nos
	 * @return
	 * @throws Exception
	 */
	public String addSettleSheet(String settle_supplier_id, List<String> sheet_nos) throws Exception;

	/**
	 * 加入已有结款单据
	 * 
	 * @param id
	 * @param sheet_nos
	 * @return
	 * @throws Exception
	 */
	public boolean addExistedSettleSheet(String id, List<String> sheet_nos) throws Exception;

	/**
	 * 搜索结款单据
	 * 
	 * @param settleSheetDetailFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<SettleSheetDetailBean> searchSettleSheetDetail(
			SettleSheetDetailFilterParam settleSheetDetailFilterParam) throws Exception;

	/**
	 * 获取结款单详细信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public SettleSheetDetailBean getSettleSheetDetail(String id) throws Exception;

	/**
	 * 提交结款单据
	 * 
	 * @param submitParam
	 * @return
	 * @throws Exception
	 */
	public boolean submitSettleSheetDetail(SettleSheetDetailSubmitParam submitParam) throws Exception;

	/**
	 * 标记结款单
	 * 
	 * @param id
	 * @param running_number
	 * @param real_pay
	 * @return
	 * @throws Exception
	 */
	public boolean markPayment(String id, String running_number, BigDecimal real_pay) throws Exception;

	/**
	 * 对结款单据进行红冲操作
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteSettleSheetDetail(String id) throws Exception;

	/**
	 * 审核不通过结款单据
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean reviewSettleSheetDetail(String id) throws Exception;

	/**
	 * 打印结款单据
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public SettleSheetDetailBean printSettleSheetDetail(String id) throws Exception;

	/**
	 * 导出结款单据
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean exportSettleSheetDetail(String id) throws Exception;

	/**
	 * 导出待处理单据列表
	 * 
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public boolean exportSettleSheet(String start, String end) throws Exception;

	/**
	 * 导出处理单据列表
	 * 
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public boolean exportPaymentList(String start, String end) throws Exception;

	/**
	 * 应付总账统计
	 * 
	 * @param begin
	 * @param end
	 * @param settle_supplier_id
	 * @return
	 * @throws Exception
	 */
	public SettlementCollectBean getSettlementCollect(String begin, String end, String settle_supplier_id)
			throws Exception;

	/**
	 * 应付总账列表条目
	 * 
	 * @param silterParam
	 * @return
	 * @throws Exception
	 */
	public List<SettlementBean> searchSettlement(SettlementFilterParam filterParam) throws Exception;

	/**
	 * 应付总账导出
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public boolean exportSettlement(SettlementFilterParam filterParam) throws Exception;

	/**
	 * 应付明细账搜索过滤
	 * 
	 * @param settlementFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<SettlementDetailBean> searchSettlementDetail(SettlementFilterParam settlementFilterParam)
			throws Exception;

	/**
	 * 带有分页信息的应付明细账搜索过滤
	 * 
	 * @param settlementFilterParam
	 * @return
	 * @throws Exception
	 */
	public SettlementDetailPageBean searchSettlementDetailPage(SettlementFilterParam settlementFilterParam)
			throws Exception;

	/**
	 * 应付明细账导出
	 * 
	 * @param settlementFilterParam
	 * @return
	 * @throws Exception
	 */
	public BigDecimal exportSettlementDetail(SettlementFilterParam settlementFilterParam) throws Exception;

}
