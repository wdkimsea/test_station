package cn.guanmai.open.interfaces.stock;

import java.util.List;

import cn.guanmai.open.bean.stock.OpenStockOutDetailBean;
import cn.guanmai.open.bean.stock.OpenStockOutSheetBean;
import cn.guanmai.open.bean.stock.param.OpenStockOutCommonParam;
import cn.guanmai.open.bean.stock.param.OpenStockOutSheetFilterParam;

/**
 * @author liming
 * @date 2019年10月22日
 * @time 下午7:04:50
 * @des TODO
 */

public interface OpenStockOutService {
	/**
	 * 搜索过滤出库单
	 * 
	 * @param stockOutSheetFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<OpenStockOutSheetBean> queryStockOutSheet(OpenStockOutSheetFilterParam stockOutSheetFilterParam)
			throws Exception;

	/**
	 * 获取出库单详情
	 * 
	 * @param out_stock_sheet_id
	 * @return
	 * @throws Exception
	 */
	public OpenStockOutDetailBean getStockOutDetail(String out_stock_sheet_id) throws Exception;

	/**
	 * 新建出库单
	 * 
	 * @param stockOutCreateParam
	 * @return
	 * @throws Exception
	 */
	public String createStockOutSheet(OpenStockOutCommonParam stockOutCreateParam) throws Exception;

	/**
	 * 修改出库单
	 * 
	 * @param stockOutUpdateParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateStockOutSheet(OpenStockOutCommonParam stockOutUpdateParam) throws Exception;

	/**
	 * 提交出库单
	 * 
	 * @param out_stock_sheet_id
	 * @return
	 * @throws Exception
	 */
	public boolean submitStockOutSheet(String out_stock_sheet_id) throws Exception;

	/**
	 * 冲销出库单
	 * 
	 * @param out_stock_sheet_id
	 * @return
	 * @throws Exception
	 */
	public boolean revertStockOutSheet(String out_stock_sheet_id) throws Exception;
}
