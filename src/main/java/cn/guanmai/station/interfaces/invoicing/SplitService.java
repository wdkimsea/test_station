package cn.guanmai.station.interfaces.invoicing;

import java.math.BigDecimal;
import java.util.List;

import cn.guanmai.station.bean.invoicing.SplitLossBean;
import cn.guanmai.station.bean.invoicing.SplitLossCountBean;
import cn.guanmai.station.bean.invoicing.SplitPlanBean;
import cn.guanmai.station.bean.invoicing.SplitPlanDetailBean;
import cn.guanmai.station.bean.invoicing.SplitSheetBean;
import cn.guanmai.station.bean.invoicing.SplitSheetDetailBean;
import cn.guanmai.station.bean.invoicing.param.SplitLossFilterParam;
import cn.guanmai.station.bean.invoicing.param.SplitPlanFilterParam;
import cn.guanmai.station.bean.invoicing.param.SplitPlanParam;
import cn.guanmai.station.bean.invoicing.param.SplitSheetParam;
import cn.guanmai.station.bean.invoicing.param.SplitSheetFiterParam;

/**
 * @author: liming
 * @Date: 2020年6月28日 上午11:41:55
 * @description: 分割相关接口
 * @version: 1.0
 */

public interface SplitService {
	/**
	 * 搜索过滤分割方案
	 * 
	 * @param splitPlanFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<SplitPlanBean> searchSplitPlan(SplitPlanFilterParam splitPlanFilterParam) throws Exception;

	/**
	 * 新建分割方案
	 * 
	 * @param splitPlanParam
	 * @return
	 * @throws Exception
	 */
	public String createSplitPlan(SplitPlanParam splitPlanParam) throws Exception;

	/**
	 * 获取分割方案详情
	 * 
	 * @param splitPlanId
	 * @return
	 * @throws Exception
	 */
	public SplitPlanDetailBean getSplitPlanDetail(String splitPlanId) throws Exception;

	/**
	 * 修改分割方案
	 * 
	 * @param splitPlanParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateSplitPlan(SplitPlanParam splitPlanParam) throws Exception;

	/**
	 * 删除分割方案
	 * 
	 * @param id
	 * @param version
	 * @return
	 * @throws Exception
	 */
	public boolean deleteSplitPlan(String id, int version) throws Exception;

	/**
	 * 搜索过滤分割单据
	 * 
	 * @param splitSheetFiterParam
	 * @return
	 * @throws Exception
	 */
	public List<SplitSheetBean> searchSplitSheet(SplitSheetFiterParam splitSheetFiterParam) throws Exception;

	/**
	 * 新建分割单据
	 * 
	 * @param splitSheetCreateParam
	 * @return
	 * @throws Exception
	 */
	public String createSplitSheet(SplitSheetParam splitSheetCreateParam) throws Exception;

	/**
	 * 修改分割单据
	 * 
	 * @param splitSheetUpdateParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateSplitSheet(SplitSheetParam splitSheetUpdateParam) throws Exception;

	/**
	 * 更新分割单据状态
	 * 
	 * @param id
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public boolean updateSplitSheetStatus(String id, int status) throws Exception;

	/**
	 * 获取分割单据详情
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public SplitSheetDetailBean getSplitSheetDetail(String id) throws Exception;

	/**
	 * 删除分割单据
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteSplitSheet(String id) throws Exception;

	/**
	 * 分割损耗统计
	 * 
	 * @param splitLossFilterParam
	 * @return
	 * @throws Exception
	 */
	public SplitLossCountBean getSplitLossCount(SplitLossFilterParam splitLossFilterParam) throws Exception;

	/**
	 * 分割损耗列表
	 * 
	 * @param splitLossFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<SplitLossBean> searchSplitLoss(SplitLossFilterParam splitLossFilterParam) throws Exception;

	/**
	 * 分割损耗列表导出
	 * 
	 * @param splitLossFilterParam
	 * @return
	 * @throws Exception
	 */
	public BigDecimal exportSplitLoss(SplitLossFilterParam splitLossFilterParam) throws Exception;

}
