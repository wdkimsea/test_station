package cn.guanmai.open.interfaces.stock;

import java.util.List;

import cn.guanmai.open.bean.stock.OpenStockInSheetBean;
import cn.guanmai.open.bean.stock.OpenStockInSheetDetailBean;
import cn.guanmai.open.bean.stock.param.OpenStockInCommonParam;
import cn.guanmai.open.bean.stock.param.OpenStockInSheetFilterParam;

/**
 * @author liming
 * @date 2019年10月22日
 * @time 下午5:52:31
 * @des TODO
 */

public interface OpenStockInService {

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<OpenStockInSheetBean> queryStockInSheet(OpenStockInSheetFilterParam stockInSheetFilterParam) throws Exception;

	/**
	 * 获取采购入库单详情
	 * 
	 * @param sheet_id
	 * @return
	 * @throws Exception
	 */
	public OpenStockInSheetDetailBean getStockInSheetDetail(String sheet_id) throws Exception;

	/**
	 * 新建采购入库单
	 * 
	 * @param OpenStockInCreateParam
	 * @return
	 * @throws Exception
	 */
	public String createStockInSheet(OpenStockInCommonParam stockInCreateParam) throws Exception;

	/**
	 * 修改采购入库单
	 * 
	 * @param OpenStockInCreateParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateStockInSheet(OpenStockInCommonParam stockInUpdateParam) throws Exception;

	/**
	 * 新增采购入库单条目
	 * 
	 * @param stockInCommonParam
	 * @return
	 * @throws Exception
	 */
	public boolean addStockInDetail(OpenStockInCommonParam stockInCommonParam) throws Exception;

	/**
	 * 修改采购入库单条目
	 * 
	 * @param stockInCommonParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateStockInDetail(OpenStockInCommonParam stockInCommonParam) throws Exception;

	/**
	 * 删除采购入库单条目
	 * 
	 * @param stockInCommonParam
	 * @return
	 * @throws Exception
	 */
	public boolean deleteStockInDetail(OpenStockInCommonParam stockInCommonParam) throws Exception;

	/**
	 * 提交采购入库单
	 * 
	 * @param in_stock_sheet_id
	 * @return
	 * @throws Exception
	 */
	public boolean submitStockInSheet(String in_stock_sheet_id) throws Exception;

	/**
	 * 冲销采购入库单
	 * 
	 * @param in_stock_sheet_id
	 * @return
	 * @throws Exception
	 */
	public boolean revertStockInsheet(String in_stock_sheet_id) throws Exception;

	/**
	 * 审核不通过采购入库单
	 * 
	 * @param in_stock_sheet_id
	 * @return
	 * @throws Exception
	 */
	public boolean rejectStockInSheet(String in_stock_sheet_id) throws Exception;
}
