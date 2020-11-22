package cn.guanmai.station.interfaces.jingcai;

import java.util.List;

import cn.guanmai.station.bean.jingcai.IngredientBean;
import cn.guanmai.station.bean.jingcai.ProductBean;

/**
 * @author: liming
 * @Date: 2020年4月28日 下午3:23:51
 * @description:
 * @version: 1.0
 */

public interface ProductService {
	/**
	 * 新建销售SKU
	 * 
	 * @param product
	 * @return
	 * @throws Exception
	 */
	public String createProduct(ProductBean product) throws Exception;

	/**
	 * 修改销售SKU
	 * 
	 * @param product
	 * @return
	 * @throws Exception
	 */
	public boolean updateProduct(ProductBean product) throws Exception;

	/**
	 * 删除销售SKU
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteProduct(String id) throws Exception;

	/**
	 * 获取销售SKU(净菜)
	 * 
	 * @param spu_id
	 * @return
	 * @throws Exception
	 */
	public List<ProductBean> getProducts(String spu_id) throws Exception;

	/**
	 * 搜寻净菜成分
	 * 
	 * @param q
	 * @return
	 * @throws Exception
	 */
	public List<IngredientBean> searchIngredient(String q) throws Exception;

	/**
	 * 获取指定的销售SKU(净菜)
	 * 
	 * @param spu_id
	 * @param sku_id
	 * @return
	 * @throws Exception
	 */
	public ProductBean getProduct(String spu_id, String sku_id) throws Exception;
	
	/**
	 * 搜索净菜原料百分比接口
	 * @param ingredients
	 * @return
	 * @throws Exception
	 */
	public boolean getPercentage(List<String> ingredients) throws Exception;
}
