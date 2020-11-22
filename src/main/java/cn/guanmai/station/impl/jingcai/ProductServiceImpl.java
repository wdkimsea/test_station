package cn.guanmai.station.impl.jingcai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.jingcai.IngredientBean;
import cn.guanmai.station.bean.jingcai.ProductBean;
import cn.guanmai.station.interfaces.jingcai.ProductService;
import cn.guanmai.station.url.JingcaiURL;
import cn.guanmai.util.JsonUtil;

/**
 * @author: liming
 * @Date: 2020年4月28日 下午4:00:39
 * @description:
 * @version: 1.0
 */

public class ProductServiceImpl implements ProductService {
	private BaseRequest baseRequest;

	public ProductServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public String createProduct(ProductBean product) throws Exception {
		String url = JingcaiURL.product_create_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, product);

		return retObj.getInteger("code") == 0 ? retObj.getString("data") : null;
	}

	@Override
	public boolean updateProduct(ProductBean product) throws Exception {
		String url = JingcaiURL.product_update_url;

		product.setSalemenu_id(null);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, product);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteProduct(String id) throws Exception {
		String url = JingcaiURL.product_delete_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<ProductBean> getProducts(String spu_id) throws Exception {
		String url = JingcaiURL.product_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), ProductBean.class)
				: null;

	}

	@Override
	public List<IngredientBean> searchIngredient(String q) throws Exception {
		String url = JingcaiURL.product_ingredient_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("q", q);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		List<IngredientBean> ingredients = null;
		if (retObj.getInteger("code") == 0) {
			ingredients = new ArrayList<IngredientBean>();
			JSONArray dataArray = retObj.getJSONArray("data");
			for (Object obj : dataArray) {
				JSONObject dataObj = JSONObject.parseObject(obj.toString());
				ingredients.addAll(
						JsonUtil.strToClassList(dataObj.getJSONArray("ingredients").toString(), IngredientBean.class));
			}
		}
		return ingredients;
	}

	@Override
	public ProductBean getProduct(String spu_id, String sku_id) throws Exception {
		String url = JingcaiURL.product_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		ProductBean product = null;
		if (retObj.getInteger("code") == 0) {
			List<ProductBean> products = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(),
					ProductBean.class);
			product = products.stream().filter(p -> p.getId().equals(sku_id)).findAny().orElse(null);
		}
		return product;
	}

	@Override
	public boolean getPercentage(List<String> ingredients) throws Exception {
		String url = JingcaiURL.product_sku_percentage_get_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("ingredients", JsonUtil.objectToStr(ingredients));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0;
	}

}
