package cn.guanmai.open.interfaces.product;

import java.util.List;

import cn.guanmai.open.bean.product.OpenCategory1Bean;
import cn.guanmai.open.bean.product.OpenCategory2Bean;
import cn.guanmai.open.bean.product.OpenPinleiBean;
import cn.guanmai.open.bean.product.OpenPurchaseSpecBean;
import cn.guanmai.open.bean.product.OpenReceiveTimeBean;
import cn.guanmai.open.bean.product.OpenSaleSkuBean;
import cn.guanmai.open.bean.product.OpenSaleSkuDetailBean;
import cn.guanmai.open.bean.product.OpenSpuBean;
import cn.guanmai.open.bean.product.param.*;

/* 
* @author liming 
* @date Jun 3, 2019 3:54:13 PM 
* @des 商品分类相关业务接口
* @version 1.0 
*/
public interface OpenCategoryService {
	/**
	 * 获取一级分类列表
	 * 
	 * @return
	 */
	public List<OpenCategory1Bean> getCategory1List() throws Exception;

	/**
	 * 新建一级分类,返回一级分类ID
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public String createCategory1(String name) throws Exception;

	/**
	 * 修改一级分类
	 * 
	 * @param id
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public boolean updateCategory1(String id, String name) throws Exception;

	/**
	 * 删除一级分类
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCategory1(String id) throws Exception;

	/**
	 * 获取二级分类列表
	 * 
	 * @param category1_id 选填
	 * @param offset       选填
	 * @param limit        选填
	 * @return
	 * @throws Exception
	 */
	public List<OpenCategory2Bean> getCategory2List(String category1_id, Integer offset, Integer limit)
			throws Exception;

	/**
	 * 新建二级分类
	 * 
	 * @param category1_id
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public String createCategory2(String category1_id, String name) throws Exception;

	/**
	 * 修改二级分类
	 * 
	 * @param id
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public boolean updateCategory2(String id, String name) throws Exception;

	/**
	 * 删除二级分类
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCategory2(String id) throws Exception;

	/**
	 * 获取品类分类列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<OpenPinleiBean> getPinleiList(String category1_id, String category2_id, Integer offset, Integer limit)
			throws Exception;

	/**
	 * 新建品类
	 * 
	 * @param category2_id
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public String createPinlei(String category2_id, String name) throws Exception;

	/**
	 * 修改品类
	 * 
	 * @param pinlei_id
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public boolean updatePinlei(String pinlei_id, String name) throws Exception;

	/**
	 * 删除品类
	 * 
	 * @param pinlei_id
	 * @return
	 * @throws Exception
	 */
	public boolean deletePinlei(String pinlei_id) throws Exception;

	/**
	 * 获取SPU列表
	 * 
	 * @param category1_id
	 * @param category2_id
	 * @param pinlei_id
	 * @param offset
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<OpenSpuBean> getSpuBeanList(String category1_id, String category2_id, String pinlei_id, Integer offset,
			Integer limit) throws Exception;

	/**
	 * 获取指定SPU
	 * 
	 * @return
	 * @throws Exception
	 */
	public OpenSpuBean getSpuBean(String spu_id) throws Exception;

	/**
	 * 新建SPU
	 * 
	 * @param OpenSpuCreateParam
	 * @return
	 * @throws Exception
	 */
	public String createSpu(OpenSpuCreateParam spuCreateParam) throws Exception;

	/**
	 * 修改SPU
	 * 
	 * @param spuUpdateParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateSpu(OpenSpuUpdateParam spuUpdateParam) throws Exception;

	/**
	 * 删除SPU
	 * 
	 * @param spu_id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteSpu(String spu_id) throws Exception;

	/**
	 * 获取SPU基本单位列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> get_std_unit_name_map() throws Exception;

	/**
	 * 新建销售SKU
	 *
	 * @param createParam
	 * @return
	 * @throws Exception
	 */
	public String createSaleSku(OpenSkuCreateParam createParam) throws Exception;

	/**
	 * 获取SKU详情
	 * 
	 * @param sku_id
	 * @return
	 * @throws Exception
	 */
	public OpenSaleSkuDetailBean getSaleSkuDetail(String sku_id) throws Exception;

	/**
	 * 获取SKU详情
	 *
	 * @param sku_outer_id
	 * @return
	 * @throws Exception
	 */
	public OpenSaleSkuDetailBean getSaleSkuDetailByOuterId(String sku_outer_id) throws Exception;

	/**
	 * 修改销售SKU
	 * 
	 * @param updateParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateSaleSku(OpenSkuUpdateParam updateParam) throws Exception;

	/**
	 * 删除修改销售SKU
	 * 
	 * @param sku_id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteSaleSku(String sku_id) throws Exception;

	/**
	 * 搜索过滤销售SKU
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<OpenSaleSkuBean> seachSaleSku(OpenSaleSkuFilterParam filterParam) throws Exception;

	/**
	 * 获取运营时间的收货时间
	 * 
	 * @param time_config_id
	 * @return
	 * @throws Exception
	 */
	public OpenReceiveTimeBean getReceiveTime(String time_config_id) throws Exception;

	/**
	 * 搜索采购规格
	 * 
	 * @param purchaseSpecFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<OpenPurchaseSpecBean> queryPurchaseSpec(OpenPurchaseSpecFilterParam purchaseSpecFilterParam)
			throws Exception;

	/**
	 * 新建采购规格
	 * 
	 * @param purchaseSpecParam
	 * @return
	 * @throws Exception
	 */
	public String createPurchaseSpec(OpenPurchaseSpecParam purchaseSpecParam) throws Exception;

	/**
	 * 修改采购规格
	 * 
	 * @param purchaseSpecParam
	 * @return
	 * @throws Exception
	 */
	public boolean updatePurchaseSpec(OpenPurchaseSpecParam purchaseSpecParam) throws Exception;

	/**
	 * 删除采购规格
	 * 
	 * @param spec_id
	 * @return
	 * @throws Exception
	 */
	public boolean deletePurcahseSpec(String spec_id) throws Exception;

	/**
	 * 更新采购询价
	 * 
	 * @param quotePriceParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateQuotePrice(OpenQuotePriceParam quotePriceParam) throws Exception;
}
