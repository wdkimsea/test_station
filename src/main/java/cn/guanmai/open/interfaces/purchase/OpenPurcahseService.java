package cn.guanmai.open.interfaces.purchase;

import java.util.List;

import cn.guanmai.open.bean.purchase.OpenPurcahserBean;
import cn.guanmai.open.bean.purchase.OpenPurchaseSheetBean;
import cn.guanmai.open.bean.purchase.OpenPurchaseSheetDetailBean;
import cn.guanmai.open.bean.purchase.OpenPurchaseTaskBean;
import cn.guanmai.open.bean.purchase.OpenTimeConfigBean;
import cn.guanmai.open.bean.purchase.param.OpenPurchaseSheetCommonParam;
import cn.guanmai.open.bean.purchase.param.OpenPurchaseSheetFilterParam;
import cn.guanmai.open.bean.purchase.param.OpenPurchaseTaskCreateParam;
import cn.guanmai.open.bean.purchase.param.OpenPurchaseTaskFilterParam;
import cn.guanmai.open.bean.purchase.param.OpenPurchaseTaskUpdateParam;
import cn.guanmai.open.bean.purchase.param.OpenPurchaserFilterParam;

/**
 * @author liming
 * @date 2019年11月11日
 * @time 下午7:48:01
 * @des TODO
 */

public interface OpenPurcahseService {
	/**
	 * 搜索过滤采购员
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<OpenPurcahserBean> queryPurchaser(OpenPurchaserFilterParam filterParam) throws Exception;

	/**
	 * 查询采购单列表
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<OpenPurchaseSheetBean> queryPurchaseSheet(OpenPurchaseSheetFilterParam filterParam) throws Exception;

	/**
	 * 获取采购单详情
	 * 
	 * @param purchase_sheet_id
	 * @return
	 * @throws Exception
	 */
	public OpenPurchaseSheetDetailBean getPurchaseSheetDetail(String purchase_sheet_id) throws Exception;

	/**
	 * 新建采购单据
	 * 
	 * @param openPurchaseSheetCreateParam
	 * @return
	 * @throws Exception
	 */
	public String createPurchaseSheet(OpenPurchaseSheetCommonParam openPurchaseSheetCreateParam) throws Exception;

	/**
	 * 修改采购单据
	 * 
	 * @param openPurchaseSheetUpdateParam
	 * @return
	 * @throws Exception
	 */
	public boolean updatePurchaseSheet(OpenPurchaseSheetCommonParam openPurchaseSheetUpdateParam) throws Exception;

	/**
	 * 提交采购单据
	 * 
	 * @param purchase_sheet_id
	 * @return
	 * @throws Exception
	 */
	public boolean submitPurchaseSheet(String purchase_sheet_id) throws Exception;

	/**
	 * 删除采购单据
	 * 
	 * @param purchase_sheet_id
	 * @return
	 * @throws Exception
	 */
	public boolean deletePurcahseSheet(String purchase_sheet_id) throws Exception;

	/**
	 * 获取运营时间
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<OpenTimeConfigBean> getTimeConfigs() throws Exception;

	/**
	 * 搜索过滤采购任务
	 * 
	 * @param openPurchaseTaskFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<OpenPurchaseTaskBean> queryPurcahseTask(OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam)
			throws Exception;

	/**
	 * 新进采购任务
	 * 
	 * @param openPurchaseTaskCreateParam
	 * @return
	 * @throws Exception
	 */
	public boolean createPurchaseTask(OpenPurchaseTaskCreateParam openPurchaseTaskCreateParam) throws Exception;

	/**
	 * 修改采购任务
	 * 
	 * @param openPurchaseTaskUpdateParam
	 * @return
	 * @throws Exception
	 */
	public boolean updatePurcahseTask(OpenPurchaseTaskUpdateParam openPurchaseTaskUpdateParam) throws Exception;

	/**
	 * 发布采购任务
	 * 
	 * @param task_id
	 * @return
	 * @throws Exception
	 */
	public boolean publishPurchaseTask(String task_id) throws Exception;
}
