package cn.guanmai.open.impl.product;

import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.open.bean.product.OpenCategory1Bean;
import cn.guanmai.open.bean.product.OpenCategory2Bean;
import cn.guanmai.open.bean.product.OpenPinleiBean;
import cn.guanmai.open.bean.product.OpenPurchaseSpecBean;
import cn.guanmai.open.bean.product.OpenReceiveTimeBean;
import cn.guanmai.open.bean.product.OpenSaleSkuBean;
import cn.guanmai.open.bean.product.OpenSaleSkuDetailBean;
import cn.guanmai.open.bean.product.OpenSpuBean;
import cn.guanmai.open.bean.product.param.*;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.url.OpenProductURL;
import cn.guanmai.request.impl.OpenRequestImpl;
import cn.guanmai.request.intefaces.OpenRequest;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Jun 3, 2019 3:55:45 PM 
* @des 商品分类相关业务实现
* @version 1.0 
*/
public class OpenCategoryServiceImpl implements OpenCategoryService {
	private JSONObject retObj;
	private OpenRequest openRequest;

	public OpenCategoryServiceImpl(String access_token) {
		openRequest = new OpenRequestImpl(access_token);
	}

	@Override
	public List<OpenCategory1Bean> getCategory1List() throws Exception {
		String url = OpenProductURL.category1_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("offset", "0");
		paramMap.put("limit", "100");

		retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenCategory1Bean.class)
				: null;
	}

	@Override
	public List<OpenCategory2Bean> getCategory2List(String category1_id, Integer offset, Integer limit)
			throws Exception {
		String url = OpenProductURL.category2_list_url;

		Map<String, String> paramMap = new HashMap<>();
		if (category1_id != null) {
			paramMap.put("category1_id", category1_id);
		}
		if (offset != null) {
			paramMap.put("offset", String.valueOf(offset));
		}
		if (limit != null) {
			paramMap.put("limit", String.valueOf(limit));
		}

		retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenCategory2Bean.class)
				: null;
	}

	@Override
	public List<OpenPinleiBean> getPinleiList(String category1_id, String category2_id, Integer offset, Integer limit)
			throws Exception {
		String url = OpenProductURL.pinlei_list_url;

		Map<String, String> paramMap = new HashMap<>();
		if (category1_id != null) {
			paramMap.put("category1_id", category1_id);
		}

		if (category2_id != null) {
			paramMap.put("category2_id", category2_id);
		}

		if (offset != null) {
			paramMap.put("offset", String.valueOf(offset));
		}

		if (limit != null) {
			paramMap.put("limit", String.valueOf(limit));
		}
		retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenPinleiBean.class)
				: null;
	}

	@Override
	public List<OpenSpuBean> getSpuBeanList(String category1_id, String category2_id, String pinlei_id, Integer offset,
			Integer limit) throws Exception {
		String url = OpenProductURL.spu_list_url;

		Map<String, String> paramMap = new HashMap<>();
		if (category1_id != null) {
			paramMap.put("category1_id", category1_id);
		}

		if (category2_id != null) {
			paramMap.put("category2_id", category2_id);
		}

		if (pinlei_id != null) {
			paramMap.put("pinlei_id", pinlei_id);
		}

		if (offset != null) {
			paramMap.put("offset", String.valueOf(offset));
		}

		if (limit != null) {
			paramMap.put("offset", String.valueOf(limit));
		}
		retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenSpuBean.class)
				: null;
	}

	@Override
	public OpenSpuBean getSpuBean(String spu_id) throws Exception {
		String url = OpenProductURL.spu_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);

		retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OpenSpuBean.class)
				: null;

	}

	@Override
	public String createSpu(OpenSpuCreateParam spuCreateParam) throws Exception {
		String url = OpenProductURL.spu_create_url;

		retObj = openRequest.baseRequest(url, RequestType.POST, spuCreateParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("spu_id") : null;
	}

	@Override
	public boolean updateSpu(OpenSpuUpdateParam spuUpdateParam) throws Exception {
		String url = OpenProductURL.spu_update_url;

		retObj = openRequest.baseRequest(url, RequestType.POST, spuUpdateParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteSpu(String spu_id) throws Exception {
		String url = OpenProductURL.spu_delete_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<String> get_std_unit_name_map() throws Exception {
		String url = OpenProductURL.spu_std_unit_name_List_url;

		retObj = openRequest.baseRequest(url, RequestType.GET);

		List<String> resultList = null;
		if (retObj.getInteger("code") == 0) {
			resultList = new ArrayList<String>();
			JSONArray dataArray = retObj.getJSONArray("data");
			for (int i = 0; i < dataArray.size(); i++) {
				resultList.add(dataArray.getString(i));
			}
		}
		return resultList;
	}

	@Override
	public String createSaleSku(OpenSkuCreateParam createParam) throws Exception {

		String url = OpenProductURL.sale_sku_create_url;

		retObj = openRequest.baseRequest(url, RequestType.POST, createParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("sku_id") : null;
	}

	@Override
	public boolean updateSaleSku(OpenSkuUpdateParam updateParam) throws Exception {

		String url = OpenProductURL.sale_sku_update_url;

		retObj = openRequest.baseRequest(url, RequestType.POST, updateParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteSaleSku(String sku_id) throws Exception {
		String url = OpenProductURL.sale_sku_delete_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("sku_id", sku_id);

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<OpenSaleSkuBean> seachSaleSku(OpenSaleSkuFilterParam filterParam) throws Exception {
		String url = OpenProductURL.sale_sku_search_url;

		retObj = openRequest.baseRequest(url, RequestType.GET, filterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenSaleSkuBean.class)
				: null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.guanmai.open.interfaces.product.OpenCategoryService#getReceiveTime(
	 * java.lang.String)
	 */
	@Override
	public OpenReceiveTimeBean getReceiveTime(String time_config_id) throws Exception {
		String url = OpenProductURL.receive_time_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("time_config_id", time_config_id);

		retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OpenReceiveTimeBean.class)
				: null;
	}

	@Override
	public List<OpenPurchaseSpecBean> queryPurchaseSpec(OpenPurchaseSpecFilterParam purchaseSpecFilterParam)
			throws Exception {
		String url = OpenProductURL.query_purchase_spec_url;

		retObj = openRequest.baseRequest(url, RequestType.GET, purchaseSpecFilterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenPurchaseSpecBean.class)
				: null;
	}

	@Override
	public OpenSaleSkuDetailBean getSaleSkuDetail(String sku_id) throws Exception {
		String url = OpenProductURL.sale_sku_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("sku_id", sku_id);

		retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OpenSaleSkuDetailBean.class)
				: null;
	}

	@Override
	public OpenSaleSkuDetailBean getSaleSkuDetailByOuterId(String sku_outer_id) throws Exception {
		String url = OpenProductURL.sale_sku_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("sku_outer_id", sku_outer_id);

		retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OpenSaleSkuDetailBean.class)
				: null;
	}

	@Override
	public String createCategory1(String name) throws Exception {
		String url = OpenProductURL.category1_create_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("name", name);

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("category1_id") : null;
	}

	@Override
	public boolean updateCategory1(String id, String name) throws Exception {
		String url = OpenProductURL.category1_update_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);
		paramMap.put("name", name);

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteCategory1(String id) throws Exception {
		String url = OpenProductURL.category1_delete_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public String createCategory2(String category1_id, String name) throws Exception {
		String url = OpenProductURL.category2_create_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("category1_id", category1_id);
		paramMap.put("name", name);

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("category2_id") : null;
	}

	@Override
	public boolean updateCategory2(String id, String name) throws Exception {
		String url = OpenProductURL.category2_update_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);
		paramMap.put("name", name);

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteCategory2(String id) throws Exception {
		String url = OpenProductURL.category2_delete_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public String createPinlei(String category2_id, String name) throws Exception {
		String url = OpenProductURL.pinlei_create_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("category2_id", category2_id);
		paramMap.put("name", name);

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("pinlei_id") : null;
	}

	@Override
	public boolean updatePinlei(String pinlei_id, String name) throws Exception {
		String url = OpenProductURL.pinlei_update_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", pinlei_id);
		paramMap.put("name", name);

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deletePinlei(String pinlei_id) throws Exception {
		String url = OpenProductURL.pinlei_delete_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", pinlei_id);

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public String createPurchaseSpec(OpenPurchaseSpecParam purchaseSpecParam) throws Exception {
		String url = OpenProductURL.purchase_spec_craeta_url;

		retObj = openRequest.baseRequest(url, RequestType.POST, purchaseSpecParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("spec_id") : null;
	}

	@Override
	public boolean updatePurchaseSpec(OpenPurchaseSpecParam purchaseSpecParam) throws Exception {
		String url = OpenProductURL.purchase_spec_update_url;

		retObj = openRequest.baseRequest(url, RequestType.POST, purchaseSpecParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deletePurcahseSpec(String spec_id) throws Exception {
		String url = OpenProductURL.purchase_spec_delete_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spec_id", spec_id);

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateQuotePrice(OpenQuotePriceParam quotePriceParam) throws Exception {
		String url = OpenProductURL.purchase_spec_quote_price_edit_url;

		retObj = openRequest.baseRequest(url, RequestType.POST, quotePriceParam);

		return retObj.getInteger("code") == 0;
	}

}
