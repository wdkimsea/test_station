package cn.guanmai.station.interfaces.category;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.station.bean.category.BatchSkuDetail;
import cn.guanmai.station.bean.category.Category1Bean;
import cn.guanmai.station.bean.category.Category2Bean;
import cn.guanmai.station.bean.category.MerchandiseTreeBean;
import cn.guanmai.station.bean.category.PinleiBean;
import cn.guanmai.station.bean.category.PurchaseSpecBean;
import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.category.SkuMeasurementBean;
import cn.guanmai.station.bean.category.SalemenuSkuBean;
import cn.guanmai.station.bean.category.SkuSimpleBean;
import cn.guanmai.station.bean.category.SkuSuppliersBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.bean.category.SpuIndexBean;
import cn.guanmai.station.bean.category.SpuSimpleBean;
import cn.guanmai.station.bean.category.param.BatchDeleteSkuParam;
import cn.guanmai.station.bean.category.param.BatchDeleteSpuParam;
import cn.guanmai.station.bean.category.param.BatchSkuCreateParam;
import cn.guanmai.station.bean.category.param.PurchaseSpecBatchEditParam;
import cn.guanmai.station.bean.category.param.PurchaseSpecFilterParam;
import cn.guanmai.station.bean.category.param.PurchaseSpecQuotePriceParam;
import cn.guanmai.station.bean.category.param.SkuFilterParam;
import cn.guanmai.station.bean.category.param.SalemenuSkuFilterParam;
import cn.guanmai.station.bean.category.param.SpuIndexFilterParam;
import cn.guanmai.station.bean.marketing.PromotionSkuBean;

/* 
* @author liming 
* @date Oct 31, 2018 2:37:16 PM 
* @des 商品分类接口
* @version 1.0 
*/
public interface CategoryService {
	/**
	 * 获取站点分类树
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<MerchandiseTreeBean> getMerchandiseTree() throws Exception;

	/**
	 * 获取一级分类列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Category1Bean> getCategory1List() throws Exception;

	/**
	 * 创建商品一级分类
	 * 
	 * @param category1
	 * @return
	 * @throws Exception
	 */
	public String createCategory1(Category1Bean category1) throws Exception;

	/**
	 * 使用名称获取指定一级分类
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public Category1Bean getCategory1ByName(String name) throws Exception;

	/**
	 * 使用ID获取一级分类
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Category1Bean getCategory1ById(String id) throws Exception;

	/**
	 * 通过ID删除商品一级分类
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCategory1ById(String id) throws Exception;

	/**
	 * 修改一级分类信息
	 * 
	 * @param category1
	 * @return
	 * @throws Exception
	 */
	public boolean updateCategory1(Category1Bean category1) throws Exception;

	/**
	 * 获取所有的一级分类图标
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean getCategory1Icon() throws Exception;

	/**
	 * 新建二级分类
	 * 
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public String createCategory2(Category2Bean category) throws Exception;

	/**
	 * 修改二级分类
	 * 
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public boolean updateCategory2(Category2Bean category) throws Exception;

	/**
	 * 根据名称,获取指定一级分类下的指定名称的二级分类
	 * 
	 * @param upstream_id
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public Category2Bean getCategory2ByName(String upstream_id, String name) throws Exception;

	/**
	 * 根据ID获取二级分类
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Category2Bean getCategory2ById(String id) throws Exception;

	/**
	 * 获取站点所有的二级分类
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Category2Bean> getCategory2List() throws Exception;

	/**
	 * 删除二级分类
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCategory2ById(String id) throws Exception;

	/**
	 * 创建品类
	 * 
	 * @param pinlei
	 * @return
	 * @throws Exception
	 */
	public String createPinlei(PinleiBean pinlei) throws Exception;

	/**
	 * 通过名称找寻指定的品类
	 * 
	 * @param upstream_id
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public PinleiBean getPinleiByName(String upstream_id, String name) throws Exception;

	/**
	 * 通过ID获取品类
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PinleiBean getPinleiById(String id) throws Exception;

	/**
	 * 获取品类列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<PinleiBean> getPinleiList() throws Exception;

	/**
	 * 修改品类
	 * 
	 * @param pinlei
	 * @return
	 * @throws Exception
	 */
	public boolean updatePinlei(PinleiBean pinlei) throws Exception;

	/**
	 * 删除品类
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deletePinlei(String id) throws Exception;

	/**
	 * 导出商品分类
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean exportMerchandise() throws Exception;

	/**
	 * 新建SPU
	 * 
	 * @param spu
	 * @return
	 * @throws Exception
	 */
	public String createSpu(SpuBean spu) throws Exception;

	/**
	 * 在指定品类下获取指定名称的SPU
	 * 
	 * @param upstream_id
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public SpuBean getSpuByName(String upstream_id, String name) throws Exception;

	/**
	 * 根据ID获取SPU
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public SpuBean getSpuById(String id) throws Exception;

	/**
	 * 搜索过滤SPU
	 * 
	 * @param q
	 * @return
	 * @throws Exception
	 */
	public List<SpuBean> branchSpu(String q) throws Exception;

	/**
	 * SPU简单搜索
	 * 
	 * @param q
	 * @return
	 * @throws Exception
	 */
	public List<SpuSimpleBean> searchSimpleSpu(String q) throws Exception;

	/**
	 * 修改SPU
	 * 
	 * @param spu
	 * @return
	 * @throws Exception
	 */
	public boolean updateSpu(SpuBean spu) throws Exception;

	/**
	 * 删除SPU
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteSpu(String id) throws Exception;

	/**
	 * 批量删除SPU
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public boolean batchDeleteSpu(BatchDeleteSpuParam param) throws Exception;

	/**
	 * 上传SPU图片
	 * 
	 * @param imagePath
	 * @return
	 */
	public String uploadSpuImage(String imagePath) throws Exception;

	/**
	 * 下载SPU图片
	 * 
	 * @param image_url
	 * @return
	 * @throws Exception
	 */
	public boolean downloadSpuImage(String image_url) throws Exception;

	/**
	 * 商品库过滤搜索
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<SpuIndexBean> searchSpuIndex(SpuIndexFilterParam filterParam) throws Exception;

	/**
	 * 批量sku详细信息
	 * 
	 * @param spu_ids
	 * @return
	 * @throws Exception
	 */
	public List<BatchSkuDetail> getBatchSpuDetails(List<String> spu_ids) throws Exception;

	/**
	 * 创建采购规格
	 * 
	 * @param purchaseSpec
	 * @return
	 * @throws Exception
	 */
	public String createPurchaseSpec(PurchaseSpecBean purchaseSpec) throws Exception;

	/**
	 * 修改采购规格
	 * 
	 * @param purchaseSpec
	 * @return
	 * @throws Exception
	 */
	public boolean updatePurchaseSpec(PurchaseSpecBean purchaseSpec) throws Exception;

	/**
	 * 删除采购规格
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deletePurchaseSpec(String id) throws Exception;

	/**
	 * 搜索过滤采购规格
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<PurchaseSpecBean> searchPurchaseSpec(PurchaseSpecFilterParam param) throws Exception;

	/**
	 * 根据ID获取采购规格
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PurchaseSpecBean getPurchaseSpecById(String id) throws Exception;

	/**
	 * 采购规格列表导出
	 * 
	 * @param purchaseSpecFilterParam
	 * @return
	 * @throws Exception
	 */
	public BigDecimal exportPurchaseSpec(PurchaseSpecFilterParam purchaseSpecFilterParam) throws Exception;

	/**
	 * 导出采购规格询价模板
	 * 
	 * @return
	 * @throws Exception
	 */
	public BigDecimal exportPurchaseSpecQuotePriceTemplate() throws Exception;

	/**
	 * 采购规格批量导入修改
	 * 
	 * @param spec_details
	 * @return
	 * @throws Exception
	 */
	public BigDecimal importEditPurchaseSpecs(List<PurchaseSpecBatchEditParam> spec_details) throws Exception;

	/**
	 * 采购规格询价导入
	 * 
	 * @param purchaseSpecQuotePriceParams
	 * @return
	 * @throws Exception
	 */
	public BigDecimal importPurchaseSpecQuotePrice(List<PurchaseSpecQuotePriceParam> purchaseSpecQuotePriceParams)
			throws Exception;

	/**
	 * 此方法用于销售SKU详细页面拉取对应的所有采购规格列表
	 * 
	 * @param spu_id
	 * @param supplier_id
	 * @return
	 * @throws Exception
	 */
	public List<PurchaseSpecBean> getPurchaseSpecArray(String spu_id, String supplier_id) throws Exception;

	/**
	 * 新建销售SKU
	 * 
	 * @param sku
	 * @return
	 * @throws Exception
	 */
	public String createSaleSku(SkuBean sku) throws Exception;

	/**
	 * 新建销售SKU
	 * 
	 * @param saleSkuCreateParam
	 * @return
	 */
	public String createJingCaiSaleSku(SkuBean jc_sku) throws Exception;

	/**
	 * 批量新建销售SKU
	 * 
	 * @param salemenu_id
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public boolean batchCreateSaleSku(String salemenu_id, List<BatchSkuCreateParam> param) throws Exception;

	/**
	 * 删除销售SKU
	 * 
	 * @param sku_id
	 * @return
	 */
	public boolean deleteSaleSku(String sku_id) throws Exception;

	/**
	 * 批量删除销售SKU
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public boolean batchDeleteSaleSku(BatchDeleteSkuParam param) throws Exception;

	/**
	 * 修改SKU
	 * 
	 * @param sku
	 * @return
	 */
	public boolean updateSaleSku(SkuBean sku) throws Exception;

	/**
	 * 获取销售sku列表
	 * 
	 * @param spu_id
	 * @return
	 * @throws Exception
	 */
	public List<SkuBean> getSaleSkus(String spu_id) throws Exception;

	/**
	 * 根据SPU ID和SKU ID 获取指定SKU
	 * 
	 * @param spu_id
	 * @param sku_id
	 * @return
	 * @throws Exception
	 */
	public SkuBean getSaleSkuById(String spu_id, String sku_id) throws Exception;

	/**
	 * 根据SPU ID和SKU ID 获取指定净菜SKU
	 * 
	 * @param spu_id
	 * @param jc_sku_id
	 * @return
	 * @throws Exception
	 */
	public SkuBean getJingCaiSaleSkuById(String spu_id, String jc_sku_id) throws Exception;

	/**
	 * 修改销售SKU销售状态
	 * 
	 * @param id
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public boolean updateSaleSkuStatus(String id, boolean state) throws Exception;

	/**
	 * 新建、修改SKU的时候来取所有对应的供应商列表
	 * 
	 * @param spu_id
	 * @return
	 * @throws Exception
	 */
	public List<String> getSkuSupplierList(String spu_id) throws Exception;

	/**
	 * 新建、修改SKU的时候来取所有对应的供应商列表(新)
	 * 
	 * @param spu_id
	 * @return
	 * @throws Exception
	 */
	public SkuSuppliersBean getSkuSupplierListNew(String spu_id) throws Exception;

	/**
	 * 报价单里搜索过滤销售规格
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<SalemenuSkuBean> searchSkuInSalemenu(SalemenuSkuFilterParam filterParam) throws Exception;

	/**
	 * 导出报价单里的商品列表
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public String exportSkuInSalemenu(SalemenuSkuFilterParam filterParam) throws Exception;

	/**
	 * 报价单里商品批量导入修改
	 * 
	 * @param station_id
	 * @param salemenu_id
	 * @param file_path
	 * @return
	 * @throws Exception
	 */
	public JSONObject importUpdateSkus(String station_id, String salemenu_id, String file_path) throws Exception;

	/**
	 * 报价单里商品批量导入新建
	 * 
	 * @param salemenu_id
	 * @param group_id
	 * @param station_id
	 * @param file_path
	 * @return
	 * @throws Exception
	 */
	public boolean importCreateSkus(String salemenu_id, String group_id, String station_id, String file_path)
			throws Exception;

	/**
	 * 在指定报价单里获取指定的销售SKU
	 * 
	 * @param salemenu_id
	 * @param sku_id
	 * @return
	 * @throws Exception
	 */
	public SalemenuSkuBean getSkuInSalemenu(String salemenu_id, String sku_id) throws Exception;

	/**
	 * 搜索过滤销售SKU,默认最多返回20个
	 * 
	 * @param <T>
	 * 
	 * @param search_text
	 * @return
	 * @throws Exception
	 */
	public List<SkuSimpleBean> searchSaleSku(SkuFilterParam skuFilterParam) throws Exception;

	/**
	 * 营销活动搜索商品
	 * 
	 * @param search_text
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<PromotionSkuBean> searchPromotionSku(String search_text, int limit) throws Exception;

	/**
	 * 获取单位对应关系列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<SkuMeasurementBean>> getSkuMeasurementMap() throws Exception;

	/**
	 * 商品库异步导出销售SKU
	 * 
	 * @param spuIndexFilterParam
	 * @return
	 * @throws Exception
	 */
	public BigDecimal asyncExportIndexSkus(SpuIndexFilterParam spuIndexFilterParam) throws Exception;

	/**
	 * 传入文件URL,下载后返回文件路径
	 * 
	 * @param file_url
	 * @return
	 * @throws Exception
	 */
	public String downLoadIndexSkuFile(String file_url) throws Exception;

	/**
	 * 商品库导入批量修改销售SKU
	 * 
	 * @param file_path
	 * @return
	 * @throws Exception
	 */
	public BigDecimal importUpdateIndexSku(String file_path) throws Exception;

	/**
	 * 
	 * @param fileMap
	 * @param salemenus
	 * @return
	 * @throws Exception
	 */
	public BigDecimal importCreateMerchandise(Map<String, String> fileMap, List<String> salemenus) throws Exception;
}
