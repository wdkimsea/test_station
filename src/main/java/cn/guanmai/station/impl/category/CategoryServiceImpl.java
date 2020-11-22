package cn.guanmai.station.impl.category;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Reporter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
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
import cn.guanmai.station.bean.category.param.PurchaseSpecFilterParam;
import cn.guanmai.station.bean.category.param.PurchaseSpecQuotePriceParam;
import cn.guanmai.station.bean.category.param.SkuFilterParam;
import cn.guanmai.station.bean.category.param.SalemenuSkuFilterParam;
import cn.guanmai.station.bean.category.param.SpuIndexFilterParam;
import cn.guanmai.station.bean.marketing.PromotionSkuBean;
import cn.guanmai.station.impl.base.DownloadServiceImpl;
import cn.guanmai.station.interfaces.base.DownloadService;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.url.CategoryURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Oct 31, 2018 2:43:01 PM 
* @des 商品相关实现类
* @version 1.0 
*/
public class CategoryServiceImpl implements CategoryService {
	private BaseRequest baseRequest;
	private DownloadService downloadService;

	public CategoryServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
		downloadService = new DownloadServiceImpl(headers);

	}

	@Override
	public List<MerchandiseTreeBean> getMerchandiseTree() throws Exception {
		String urlStr = CategoryURL.merchandise_tree_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), MerchandiseTreeBean.class)
				: null;
	}

	@Override
	public List<Category1Bean> getCategory1List() throws Exception {
		JSONObject retObj = baseRequest.baseRequest(CategoryURL.caterory1_list_url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), Category1Bean.class)
				: null;
	}

	@Override
	public String createCategory1(Category1Bean category1) throws Exception {
		String urlStr = CategoryURL.create_category1_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, category1);

		return retObj.getInteger("code") == 0 ? retObj.getString("data") : null;
	}

	@Override
	public Category1Bean getCategory1ByName(String name) throws Exception {
		JSONObject retObj = baseRequest.baseRequest(CategoryURL.caterory1_list_url, RequestType.GET);

		Category1Bean category1 = null;
		if (retObj.getInteger("code") == 0) {
			List<Category1Bean> category1BeanList = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(),
					Category1Bean.class);

			category1 = category1BeanList.stream().filter(c -> c.getName().equals(name)).findAny().orElse(null);
		} else {
			throw new Exception("获取一级分类列表失败," + retObj);
		}
		return category1;
	}

	@Override
	public boolean deleteCategory1ById(String id) throws Exception {
		String urlStr = CategoryURL.delete_caterory1_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateCategory1(Category1Bean category1) throws Exception {
		String urlStr = CategoryURL.update_caterory1_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, category1);

		return retObj.getInteger("code") == 0;

	}

	@Override
	public Category1Bean getCategory1ById(String id) throws Exception {
		Category1Bean category = null;

		JSONObject retObj = baseRequest.baseRequest(CategoryURL.caterory1_list_url, RequestType.GET);

		if (retObj.getInteger("code") == 0) {
			List<Category1Bean> category1BeanList = JsonUtil.strToClassList(retObj.getString("data"),
					Category1Bean.class);
			for (Category1Bean category1Bean : category1BeanList) {
				if (category1Bean.getId().equals(id.trim())) {
					category = category1Bean;
					break;
				}
			}
		}
		return category;
	}

	@Override
	public boolean getCategory1Icon() throws Exception {
		String urlStr = CategoryURL.get_caterory1_icon_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET);

		return retObj.getInteger("code") == 0 && retObj.getJSONArray("data").size() > 0;
	}

	@Override
	public String createCategory2(Category2Bean category) throws Exception {
		String urlStr = CategoryURL.create_category2_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, category);

		return retObj.getInteger("code") == 0 ? retObj.getString("data") : null;
	}

	@Override
	public Category2Bean getCategory2ByName(String upstream_id, String name) throws Exception {
		Category2Bean category2 = null;

		JSONObject retObj = baseRequest.baseRequest(CategoryURL.caterory2_list_url, RequestType.GET);

		if (retObj.getInteger("code") == 0) {
			List<Category2Bean> category2BeanList = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(),
					Category2Bean.class);
			category2 = category2BeanList.stream()
					.filter(c -> c.getName().equals(name) && c.getUpstream_id().equals(upstream_id)).findAny()
					.orElse(null);
		} else {
			throw new Exception("获取二级分类列表失败," + retObj);
		}
		return category2;
	}

	@Override
	public Category2Bean getCategory2ById(String id) throws Exception {
		Category2Bean category2 = null;

		JSONObject retObj = baseRequest.baseRequest(CategoryURL.caterory2_list_url, RequestType.GET);

		if (retObj.getInteger("code") == 0) {
			List<Category2Bean> category2BeanList = JsonUtil.strToClassList(retObj.getString("data"),
					Category2Bean.class);
			for (Category2Bean category2Bean : category2BeanList) {
				if (category2Bean.getId().equals(id)) {
					category2 = category2Bean;
					break;
				}
			}
		}
		return category2;
	}

	@Override
	public List<Category2Bean> getCategory2List() throws Exception {
		List<Category2Bean> category2Array = null;

		JSONObject retObj = baseRequest.baseRequest(CategoryURL.caterory2_list_url, RequestType.GET);

		if (retObj.getInteger("code") == 0) {
			category2Array = JsonUtil.strToClassList(retObj.getString("data"), Category2Bean.class);
		}
		return category2Array;
	}

	@Override
	public boolean deleteCategory2ById(String id) throws Exception {

		String urlStr = CategoryURL.delete_caterory2_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateCategory2(Category2Bean category) throws Exception {
		String urlStr = CategoryURL.update_caterory2_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, category);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public String createPinlei(PinleiBean pinlei) throws Exception {
		String urlStr = CategoryURL.create_pinlei_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, pinlei);

		return retObj.getInteger("code") == 0 ? retObj.getString("data") : null;

	}

	@Override
	public PinleiBean getPinleiByName(String upstream_id, String name) throws Exception {
		PinleiBean pinlei = null;
		JSONObject retObj = baseRequest.baseRequest(CategoryURL.pinlei_list_url, RequestType.GET);

		if (retObj.getInteger("code") == 0) {
			List<PinleiBean> pinleiBeanList = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(),
					PinleiBean.class);
			pinlei = pinleiBeanList.stream()
					.filter(p -> p.getName().equals(name) && p.getUpstream_id().equals(upstream_id)).findAny()
					.orElse(null);
		} else {
			throw new Exception("获取品类分类列表失败," + retObj);
		}
		return pinlei;
	}

	@Override
	public PinleiBean getPinleiById(String id) throws Exception {
		PinleiBean pinlei = null;

		JSONObject retObj = baseRequest.baseRequest(CategoryURL.pinlei_list_url, RequestType.GET);

		if (retObj.getInteger("code") == 0) {
			List<PinleiBean> pinleiBeanList = JsonUtil.strToClassList(retObj.getString("data"), PinleiBean.class);
			for (PinleiBean pinleiBean : pinleiBeanList) {
				if (pinleiBean.getId().equals(id)) {
					pinlei = pinleiBean;
					break;
				}
			}
		}
		return pinlei;
	}

	@Override
	public List<PinleiBean> getPinleiList() throws Exception {
		JSONObject retObj = baseRequest.baseRequest(CategoryURL.pinlei_list_url, RequestType.GET);
		List<PinleiBean> pinleiList = null;
		if (retObj.getInteger("code") == 0) {
			pinleiList = JsonUtil.strToClassList(retObj.getString("data"), PinleiBean.class);
		}
		return pinleiList;
	}

	@Override
	public boolean updatePinlei(PinleiBean pinlei) throws Exception {
		String urlStr = CategoryURL.update_pinlei_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, pinlei);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deletePinlei(String id) throws Exception {
		String urlStr = CategoryURL.delete_pinlei_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;

	}

	@Override
	public boolean exportMerchandise() throws Exception {
		String url = CategoryURL.export_merchandise_url;

		String file_path = baseRequest.baseExport(url, RequestType.GET, new HashMap<String, String>(), "temp.xlsx");

		return file_path != null;
	}

	@Override
	public String createSpu(SpuBean spu) throws Exception {
		String urlStr = CategoryURL.create_spu_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, spu);

		return retObj.getInteger("code") == 0 ? retObj.getString("data") : null;
	}

	@Override
	public SpuBean getSpuByName(String upstream_id, String name) throws Exception {
		SpuBean spu = null;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("pinlei_id", upstream_id);

		JSONObject retObj = baseRequest.baseRequest(CategoryURL.spu_list_url, RequestType.GET, paramMap);

		if (retObj.getInteger("code") == 0) {
			List<SpuBean> spuBeanList = JsonUtil.strToClassList(retObj.getString("data"), SpuBean.class);
			spu = spuBeanList.stream().filter(s -> s.getName().equals(name)).findAny().orElse(null);
		} else {
			throw new Exception("获取指定品类下的SPU商品列表出现错误");
		}
		return spu;
	}

	@Override
	public List<SkuBean> getSaleSkus(String spu_id) throws Exception {
		String url = CategoryURL.sku_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SkuBean.class)
				: null;
	}

	@Override
	public SpuBean getSpuById(String id) throws Exception {
		SpuBean spu = null;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", id);

		JSONObject retObj = baseRequest.baseRequest(CategoryURL.spu_info_url, RequestType.GET, paramMap);

		if (retObj.getInteger("code") == 0) {
			spu = JsonUtil.strToClassObject(retObj.getString("data"), SpuBean.class);
		}
		return spu;
	}

	@Override
	public List<SpuBean> branchSpu(String q) throws Exception {
		List<SpuBean> spuBeanList = null;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("q", q);
		paramMap.put("limit", "30");

		JSONObject retObj = baseRequest.baseRequest(CategoryURL.spu_branch_url, RequestType.GET, paramMap);

		if (retObj.getInteger("code") == 0) {
			spuBeanList = JsonUtil.strToClassList(retObj.getString("data"), SpuBean.class);
		}
		return spuBeanList;
	}

	@Override
	public List<SpuSimpleBean> searchSimpleSpu(String q) throws Exception {
		String url = CategoryURL.merchandise_spu_simple_search_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("q", q);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SpuSimpleBean.class)
				: null;
	}

	@Override
	public boolean updateSpu(SpuBean spu) throws Exception {
		String urlStr = CategoryURL.update_spu_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, spu);

		return retObj.getInteger("code") == 0;

	}

	@Override
	public boolean deleteSpu(String id) throws Exception {
		String urlStr = CategoryURL.delete_spu_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean batchDeleteSpu(BatchDeleteSpuParam param) throws Exception {
		String urlStr = CategoryURL.batch_delete_spu_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, param);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public String uploadSpuImage(String imagePath) throws Exception {
		String image_id = null;
		String urlStr = CategoryURL.upload_spu_image_url;

		JSONObject retObj = baseRequest.baseUploadRequest(urlStr, new HashMap<String, String>(), "image_file",
				imagePath);

		if (retObj.getInteger("code") == 0) {
			image_id = retObj.getJSONObject("data").getString("img_path_id");
		}
		return image_id;
	}

	@Override
	public boolean downloadSpuImage(String image_url) throws Exception {
		String filePath = downloadService.downloadFile(image_url);
		return filePath != null;

	}

	@Override
	public List<SpuIndexBean> searchSpuIndex(SpuIndexFilterParam filterParam) throws Exception {
		String url = CategoryURL.spu_index_url;
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SpuIndexBean.class)
				: null;
	}

	@Override
	public List<BatchSkuDetail> getBatchSpuDetails(List<String> spu_ids) throws Exception {
		String url = CategoryURL.batch_sku_details_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("spu_ids", JSONArray.toJSONString(spu_ids));
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), BatchSkuDetail.class)
				: null;
	}

	@Override
	public String createPurchaseSpec(PurchaseSpecBean purchaseSpec) throws Exception {
		Reporter.log("创建商品采购规格 ");
		String urlStr = CategoryURL.create_purchase_spec_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, purchaseSpec);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("purchase_spec_id") : null;
	}

	@Override
	public boolean updatePurchaseSpec(PurchaseSpecBean purchaseSpec) throws Exception {
		String urlStr = CategoryURL.update_purchase_spec_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, purchaseSpec);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deletePurchaseSpec(String id) throws Exception {
		String urlStr = CategoryURL.delete_purchase_spec_url;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<PurchaseSpecBean> searchPurchaseSpec(PurchaseSpecFilterParam param) throws Exception {
		List<PurchaseSpecBean> purchaseSpecList = null;

		JSONObject retObj = baseRequest.baseRequest(CategoryURL.search_purchase_spec_url, RequestType.GET, param);

		if (retObj.getInteger("code") == 0) {
			purchaseSpecList = JsonUtil.strToClassList(
					retObj.getJSONObject("data").getJSONArray("purchase_spec").toString(), PurchaseSpecBean.class);
		}
		return purchaseSpecList;
	}

	@Override
	public BigDecimal exportPurchaseSpec(PurchaseSpecFilterParam purchaseSpecFilterParam) throws Exception {
		String url = CategoryURL.export_purchase_spec_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, purchaseSpecFilterParam);

		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}

	@Override
	public BigDecimal exportPurchaseSpecQuotePriceTemplate() throws Exception {
		String url = CategoryURL.search_purchase_spec_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("export", "1");
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}

	@Override
	public BigDecimal importPurchaseSpecQuotePrice(List<PurchaseSpecQuotePriceParam> purchaseSpecQuotePriceParams)
			throws Exception {
		String url = CategoryURL.import_purchase_spec_quote_price_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("quoted_price", JsonUtil.objectToStr(purchaseSpecQuotePriceParams));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0 ? new BigDecimal(retObj.getJSONObject("data").getString("task_id"))
				: null;
	}

	@Override
	public BigDecimal importEditPurchaseSpecs(
			List<cn.guanmai.station.bean.category.param.PurchaseSpecBatchEditParam> spec_details) throws Exception {
		String url = CategoryURL.import_batch_edit_purchase_spec_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spec_details", JsonUtil.objectToStr(spec_details));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0 ? new BigDecimal(retObj.getJSONObject("data").getString("task_id"))
				: null;
	}

	@Override
	public PurchaseSpecBean getPurchaseSpecById(String id) throws Exception {
		PurchaseSpecBean purchaseSpec = null;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("search_text", id);
		paramMap.put("offset", "0");
		paramMap.put("limit", "10");

		JSONObject retObj = baseRequest.baseRequest(CategoryURL.search_purchase_spec_url, RequestType.GET, paramMap);

		if (retObj.getInteger("code") == 0) {
			List<PurchaseSpecBean> purchaseSpecBeanList = JsonUtil.strToClassList(
					retObj.getJSONObject("data").getJSONArray("purchase_spec").toString(), PurchaseSpecBean.class);

			purchaseSpec = purchaseSpecBeanList.stream().filter(s -> s.getId().equals(id)).findAny().orElse(null);
		}
		return purchaseSpec;
	}

	@Override
	public List<PurchaseSpecBean> getPurchaseSpecArray(String spu_id, String supplier_id) throws Exception {
		String url = CategoryURL.purchase_spec_array_in_sku;
		List<PurchaseSpecBean> purchaseSpecArray = null;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);
		paramMap.put("supplier_id", supplier_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		if (retObj.getInteger("code") == 0) {
			purchaseSpecArray = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), PurchaseSpecBean.class);
		}
		return purchaseSpecArray;
	}

	@Override
	public String createSaleSku(SkuBean sku) throws Exception {
		String urlStr = CategoryURL.create_sku_url;

		Map<String, String> paramMap = JsonUtil.objectToMap(sku);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0 ? retObj.getString("data") : null;

	}

	@Override
	public String createJingCaiSaleSku(SkuBean jc_sku) throws Exception {
		String url = CategoryURL.create_sku_url;

		Map<String, String> paramMap = JsonUtil.objectToMap(jc_sku);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0 ? retObj.getString("data") : null;
	}

	@Override
	public boolean batchCreateSaleSku(String salemenu_id, List<BatchSkuCreateParam> param) throws Exception {
		String url = CategoryURL.batch_create_sku_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("salemenu_id", salemenu_id);
		paramMap.put("skus", JsonUtil.objectToStr(param));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteSaleSku(String sku_id) throws Exception {
		String urlStr = CategoryURL.delete_sku_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", sku_id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;

	}

	@Override
	public boolean batchDeleteSaleSku(BatchDeleteSkuParam param) throws Exception {
		String url = CategoryURL.batch_delete_sku_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, param);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateSaleSku(SkuBean sku) throws Exception {
		String urlStr = CategoryURL.update_sku_url;
		// 修改SKU不能带上报价单ID
		sku.setSalemenu_id(null);
		// 这里不知道为啥json字符串序列化后,std_sale_price_forsale 变成了一位小数,系统只能接收整数
		if (sku.getStd_sale_price_forsale() != null) {
			sku.setStd_sale_price_forsale(sku.getStd_sale_price_forsale().setScale(0));
		}
		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, sku);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public SkuBean getSaleSkuById(String spu_id, String sku_id) throws Exception {
		SkuBean sku = null;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);

		JSONObject retObj = baseRequest.baseRequest(CategoryURL.sku_list_url, RequestType.GET, paramMap);
		if (retObj.getInteger("code") == 0) {
			List<SkuBean> skuArray = JsonUtil.strToClassList(retObj.getJSONArray("data").toJSONString(), SkuBean.class);
			sku = skuArray.stream().filter(s -> s.getId().equals(sku_id)).findAny().orElse(null);
		} else {
			throw new Exception("获取销售SKU详细信息报错");
		}
		if (sku != null) {
			sku.setSpu_id(spu_id);
		}
		return sku;
	}

	@Override
	public SkuBean getJingCaiSaleSkuById(String spu_id, String jc_sku_id) throws Exception {
		SkuBean jc_sku = null;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);

		JSONObject retObj = baseRequest.baseRequest(CategoryURL.sku_list_url, RequestType.GET, paramMap);
		if (retObj.getInteger("code") == 0) {
			List<SkuBean> skuArray = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SkuBean.class);
			jc_sku = skuArray.stream().filter(s -> s.getId().equals(jc_sku_id)).findAny().orElse(null);
		} else {
			throw new Exception("获取销售SKU详细信息报错");
		}
		if (jc_sku != null) {
			jc_sku.setSpu_id(spu_id);
		}
		jc_sku.setSpu_id(spu_id);
		return jc_sku;
	}

	@Override
	public boolean updateSaleSkuStatus(String id, boolean state) throws Exception {
		String urlStr = CategoryURL.update_sku_status_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);
		paramMap.put("state", String.valueOf(state == true ? 1 : 0));

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<String> getSkuSupplierList(String spu_id) throws Exception {
		String urlStr = CategoryURL.sku_supplier_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);
		List<String> supplier_ids = null;
		if (retObj.getInteger("code") == 0) {
			JSONArray dataArray = retObj.getJSONArray("data");
			supplier_ids = new ArrayList<>();
			for (Object obj : dataArray) {
				supplier_ids.add(JSON.parseObject(obj.toString()).getString("id"));
			}
		}
		return supplier_ids;
	}

	@Override
	public SkuSuppliersBean getSkuSupplierListNew(String spu_id) throws Exception {
		String urlStr = CategoryURL.get_sku_supplier_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), SkuSuppliersBean.class)
				: null;
	}

	@Override
	public List<SalemenuSkuBean> searchSkuInSalemenu(SalemenuSkuFilterParam filterParam) throws Exception {
		String url = CategoryURL.search_sku_in_salemenu_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SalemenuSkuBean.class)
				: null;
	}

	@Override
	public String exportSkuInSalemenu(SalemenuSkuFilterParam filterParam) throws Exception {
		String url = CategoryURL.search_sku_in_salemenu_url;

		String filePath = baseRequest.baseExport(url, RequestType.GET, filterParam, "skus.xlsx");

		return filePath;
	}

	@Override
	public JSONObject importUpdateSkus(String station_id, String salemenu_id, String file_path) throws Exception {
		String url = CategoryURL.import_update_sku_url + "/" + station_id + "/" + salemenu_id + "/sku/";

		JSONObject retObj = baseRequest.baseUploadRequest(url, new HashMap<String, String>(), "import_file", file_path);
		return retObj;
	}

	@Override
	public boolean importCreateSkus(String salemenu_id, String group_id, String station_id, String file_path)
			throws Exception {
		String url = CategoryURL.import_create_sku_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("salemenu_id", salemenu_id);
		paramMap.put("group_id", group_id);
		paramMap.put("station_id", station_id);

		JSONObject retObj = baseRequest.baseUploadRequest(url, paramMap, "excel", file_path);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public SalemenuSkuBean getSkuInSalemenu(String salemenu_id, String sku_id) throws Exception {
		String url = CategoryURL.search_sku_in_salemenu_url;

		SalemenuSkuFilterParam filterParam = new SalemenuSkuFilterParam();
		filterParam.setSalemenu_id(salemenu_id);
		filterParam.setText(sku_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);

		SalemenuSkuBean salemenuSku = null;
		if (retObj.getInteger("code") == 0) {
			List<SalemenuSkuBean> skuSalemenuList = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(),
					SalemenuSkuBean.class);
			salemenuSku = skuSalemenuList.stream().filter(s -> s.getSku_id().equals(sku_id)).findAny()
					.orElse(new SalemenuSkuBean());
		}
		return salemenuSku;
	}

	@Override
	public List<SkuSimpleBean> searchSaleSku(SkuFilterParam skuFilterParam) throws Exception {
		String url = CategoryURL.search_sku_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, skuFilterParam);

		List<SkuSimpleBean> skuList = null;
		if (retObj.getInteger("code") == 0) {
			skuList = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SkuSimpleBean.class);
		}
		return skuList;
	}

	@Override
	public List<PromotionSkuBean> searchPromotionSku(String search_text, int limit) throws Exception {
		String url = CategoryURL.search_sku_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("search_text", search_text);
		paramMap.put("limit", String.valueOf(limit));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), PromotionSkuBean.class)
				: null;
	}

	@Override
	public Map<String, List<SkuMeasurementBean>> getSkuMeasurementMap() throws Exception {
		String url = CategoryURL.sku_measurement_list;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		Map<String, List<SkuMeasurementBean>> skuMeasurementMap = null;
		List<SkuMeasurementBean> skuMeasurementList = null;
		String unit_name = null;
		if (retObj.getInteger("code") == 0) {
			JSONObject dataObj = retObj.getJSONObject("data");
			skuMeasurementMap = new HashMap<String, List<SkuMeasurementBean>>();
			for (Object obj : dataObj.keySet()) {
				unit_name = String.valueOf(obj);
				skuMeasurementList = JsonUtil.strToClassList(dataObj.getJSONArray(unit_name).toString(),
						SkuMeasurementBean.class);
				skuMeasurementMap.put(unit_name, skuMeasurementList);
			}
		}
		return skuMeasurementMap;
	}

	@Override
	public BigDecimal asyncExportIndexSkus(SpuIndexFilterParam spuIndexFilterParam) throws Exception {
		String url = CategoryURL.index_sku_export_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, spuIndexFilterParam);

		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}

	@Override
	public String downLoadIndexSkuFile(String file_url) throws Exception {

		String filePath = baseRequest.baseExport(file_url, RequestType.GET, new Object(), "temp.xlsx");

		return filePath;
	}

	@Override
	public BigDecimal importUpdateIndexSku(String file_path) throws Exception {
		String url = CategoryURL.index_sku_import_update_url;

		JSONObject retObj = baseRequest.baseUploadRequest(url, new HashMap<String, String>(), "import_file", file_path);
		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}

	@Override
	public BigDecimal importCreateMerchandise(Map<String, String> fileMap, List<String> salemenus) throws Exception {
		String url = CategoryURL.import_create_merchandise_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("salemenus", JSON.parseArray(salemenus.toString()).toString());
		paramMap.put("is_clean_food", "0");
		JSONObject retObj = baseRequest.baseUploadRequest(url, paramMap, fileMap);

		return retObj.getInteger("code") == 0 ? new BigDecimal(retObj.getJSONObject("data").getString("task_id"))
				: null;
	}

}
