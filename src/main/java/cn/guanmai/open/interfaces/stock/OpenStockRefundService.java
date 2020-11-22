package cn.guanmai.open.interfaces.stock;

import java.util.List;

import cn.guanmai.open.bean.stock.OpenStockRefundSheetBean;
import cn.guanmai.open.bean.stock.OpenStockRefundSheetDetailBean;
import cn.guanmai.open.bean.stock.param.OpenStockRefundSheetCommonParam;
import cn.guanmai.open.bean.stock.param.OpenStockRefundSheetFiterParam;

/**
 * @author liming
 * @date 2019年10月21日
 * @time 下午4:45:48
 * @des TODO
 */

public interface OpenStockRefundService {

	/**
	 * 搜索过滤成品退货单
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<OpenStockRefundSheetBean> queryStockRefundSheet(OpenStockRefundSheetFiterParam filterParam)
			throws Exception;

	/**
	 * 获取采购退货单详细信息
	 * 
	 * @param sheet_id
	 * @return
	 * @throws Exception
	 */
	public OpenStockRefundSheetDetailBean getStockRefundSheetDetail(String sheet_id) throws Exception;

	/**
	 * 新建采购退货单
	 * 
	 * @param stockRefundSheetCreateParam
	 * @return
	 * @throws Exception
	 */
	public String createStockRefundSheet(OpenStockRefundSheetCommonParam stockRefundSheetCreateParam) throws Exception;

	/**
	 * 修改采购退货单
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean updateStockRefundSheet(OpenStockRefundSheetCommonParam openStockRefundSheetUpdateParam)
			throws Exception;

	/**
	 * 提交采购退货单
	 * 
	 * @param refund_sheet_id
	 * @return
	 * @throws Exception
	 */
	public boolean submitStockRefundSheet(String refund_sheet_id) throws Exception;

	/**
	 * 冲销采购退货单
	 * 
	 * @param refund_sheet_id
	 * @return
	 * @throws Exception
	 */
	public boolean revertStockRefundSheet(String refund_sheet_id) throws Exception;

	/**
	 * 审核不通过采购退货单
	 * 
	 * @param refund_sheet_id
	 * @return
	 * @throws Exception
	 */
	public boolean rejectStockRefundSheet(String refund_sheet_id) throws Exception;

}
