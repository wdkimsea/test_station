package cn.guanmai.station.interfaces.invoicing;

import cn.guanmai.station.bean.invoicing.ShelfBean;
import cn.guanmai.station.bean.invoicing.ShelfSpuBean;
import cn.guanmai.station.bean.invoicing.ShelfSpuStockBean;
import cn.guanmai.station.bean.invoicing.ShelfStockBatchBean;
import cn.guanmai.station.bean.invoicing.param.ShelfSpuFilterParam;
import cn.guanmai.station.bean.invoicing.param.ShelfStockBatchFilterParam;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by yangjinhai on 2019/7/30.
 */
public interface ShelfService {
	/**
	 * 获取货位管理信息
	 *
	 * @return
	 */
	public List<ShelfBean> getShelf() throws Exception;

	/**
	 * 添加元货位
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public String addShelf(String name) throws Exception;

	/**
	 * 父货位下添加子货位
	 * 
	 * @param parent_id
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public String addShelf(String parent_id, String name) throws Exception;

	/**
	 * 删除货位
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteShelf(String id) throws Exception;

	/**
	 * 修改货位名称
	 * 
	 * @param newName
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean modifyShelf(String newName, String id) throws Exception;

	/**
	 * 根据货位ID获取SPU库存数
	 * 
	 * @param shelf_id
	 * @return
	 * @throws Exception
	 */
	public JSONObject getShelfSpuStockSummaryByShelf(String shelf_id) throws Exception;

	/**
	 * 根据SPU获取所有的货位库存数
	 * 
	 * @param spu_id
	 * @return
	 * @throws Exception
	 */
	public JSONObject getShelfSpuStockSummaryBySpu(String spu_id) throws Exception;

	/**
	 * 货位商品搜索过滤
	 * 
	 * @param shelfSpuFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<ShelfSpuBean> queryShelfSpu(ShelfSpuFilterParam shelfSpuFilterParam) throws Exception;

	/**
	 * 货位商品搜索负库存商品
	 * 
	 * @param shelfSpuFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<ShelfSpuBean> queryShelfNegativeSpu(ShelfSpuFilterParam shelfSpuFilterParam) throws Exception;

	/**
	 * 获取库存批次查询
	 * 
	 * @param shelfStockBatchFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<ShelfStockBatchBean> queryShelfStockBatch(ShelfStockBatchFilterParam shelfStockBatchFilterParam)
			throws Exception;

	/**
	 * 获取指定SPU在所有的货位上的库存情况
	 * 
	 * @param spu_id
	 * @return
	 * @throws Exception
	 */
	public List<ShelfSpuStockBean> getShelfSpuStockInfo(String spu_id) throws Exception;

}
