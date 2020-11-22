package cn.guanmai.station.interfaces.category;

import java.util.List;

import cn.guanmai.station.bean.category.CombineGoodsBean;
import cn.guanmai.station.bean.category.CombineGoodsDetailBean;
import cn.guanmai.station.bean.category.CombineGoodsPageBean;
import cn.guanmai.station.bean.category.param.CombineGoodsBatchFilterParam;
import cn.guanmai.station.bean.category.param.CombineGoodsFilterParam;
import cn.guanmai.station.bean.category.param.CombineGoodsParam;

/**
 * @author: liming
 * @Date: 2020年2月17日 下午3:52:26
 * @description: 组合商品相关接口
 * @version: 1.0
 */

public interface CombineGoodsService {
	/**
	 * 新建组合商品
	 * 
	 * @param combineGoodsCreateParam
	 * @return
	 * @throws Exception
	 */
	public String createCombineGoods(CombineGoodsParam combineGoodsCreateParam) throws Exception;

	/**
	 * 修改组合商品
	 * 
	 * @param combineGoodsEditParam
	 * @return
	 * @throws Exception
	 */
	public boolean editCombineGoods(CombineGoodsParam combineGoodsEditParam) throws Exception;

	/**
	 * 批量修改组合商品
	 * 
	 * @param combineGoodsEditParams
	 * @return
	 * @throws Exception
	 */
	public boolean batchEditCombineGoods(List<CombineGoodsParam> combineGoodsEditParams) throws Exception;

	/**
	 * 修改组合商品状态
	 * 
	 * @param id
	 * @param state
	 * @return
	 * @throws Exception
	 */
	public boolean editCombineGoodsState(String id, int state) throws Exception;

	/**
	 * 删除组合商品
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCombineGoods(String id) throws Exception;

	/**
	 * 获取组合商品详细信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public CombineGoodsDetailBean getCombineGoodsDetail(String id) throws Exception;

	/**
	 * 搜索过滤组合商品
	 * 
	 * @param combineGoodsFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<CombineGoodsBean> searchCombineGoods(CombineGoodsFilterParam combineGoodsFilterParam) throws Exception;

	/**
	 * 钩锁过滤组合-返回带有分页信息
	 * 
	 * @param combineGoodsFilterParam
	 * @return
	 * @throws Exception
	 */
	public CombineGoodsPageBean searchCombineGoodsPage(CombineGoodsFilterParam combineGoodsFilterParam)
			throws Exception;

	/**
	 * 组合商品批量修改搜索商品
	 * 
	 * @param combineGoodsBatchFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<CombineGoodsDetailBean> batchSearchCombineGoods(
			CombineGoodsBatchFilterParam combineGoodsBatchFilterParam) throws Exception;

	/**
	 * 导出组合商品
	 * 
	 * @param combineGoodsFilterParam
	 * @return
	 * @throws Exception
	 */
	public String exportCombineGoods(CombineGoodsFilterParam combineGoodsFilterParam) throws Exception;

	/**
	 * 营销活动拉取所有的组合商品
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CombineGoodsBean> promotionCombineGoods() throws Exception;

}
