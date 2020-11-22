package cn.guanmai.bshop.service;

import cn.guanmai.bshop.bean.invoicing.*;
import cn.guanmai.bshop.bean.invoicing.param.BshopSpuInStockParam;
import cn.guanmai.bshop.bean.invoicing.param.BshopSpuOutStockParam;
import cn.guanmai.bshop.bean.invoicing.param.BshopSpuStockFilterParam;
import cn.guanmai.bshop.bean.invoicing.param.BshopWatiInStockFilterParam;

import java.util.List;

/**
 * Created by abc on 2019/8/16. 进销存相关接口类
 */
public interface BsInvoicingService {
	/**
	 * 商城进销存总货值统计接口
	 * 
	 * @param address_id
	 * @param searchText
	 * @return
	 * @throws Exception
	 */
	public BshopStockCountBean getStockCount(String address_id, String searchText) throws Exception;

	/**
	 * 商城进销存搜索过滤商品库存
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<BshopSpuStockBean> searchSpuStock(BshopSpuStockFilterParam filterParam) throws Exception;

	/**
	 * 获取进销存sku统计数据
	 * 
	 * @param address_id   商户ID
	 * @param search       搜索内容
	 * @param query_type   查询种类 1:按下单时间 2:按收货时间
	 * @param order_status 订单状态 1:配送中 2:已签收 -1:全部
	 * @return
	 */
	public boolean querySkuWaitInStock(String address_id, String search, String query_type, String order_status)
			throws Exception;

	/**
	 * 待入库SKU搜索过滤
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<BshopWaitInStockSkuBean> searchWaitInStockSku(BshopWatiInStockFilterParam filterParam) throws Exception;

	/**
	 * 商城进销存进行入库操作
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean createSpuInStock(BshopSpuInStockParam spuInStockParam) throws Exception;

	/**
	 * 获取指定商户的库存列表
	 * 
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public List<BshopSpuStockBean> getAddressSpuStockList(String address_id) throws Exception;

	/**
	 * 商城出库操作
	 * 
	 * @param spuOutStockParam
	 * @return
	 * @throws Exception
	 */
	public boolean createSpuStockOutput(BshopSpuOutStockParam spuOutStockParam) throws Exception;

}
