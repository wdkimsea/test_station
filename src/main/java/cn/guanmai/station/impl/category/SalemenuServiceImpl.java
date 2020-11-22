package cn.guanmai.station.impl.category;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.async.AsyncTaskResultBean;
import cn.guanmai.station.bean.category.SalemenuBean;
import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.category.param.SalemenuFilterParam;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.category.SalemenuService;
import cn.guanmai.station.url.CategoryURL;
import cn.guanmai.station.url.SalemenuURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Feb 15, 2019 11:53:22 AM 
* @todo 报价单相关实现
* @version 1.0 
*/
public class SalemenuServiceImpl implements SalemenuService {

	private BaseRequest baseRequest;
	private AsyncService asyncService;

	public SalemenuServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
	}

	@Override
	public SalemenuBean getSalemenuById(String id) throws Exception {
		SalemenuBean salemenu = null;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(SalemenuURL.salemenu_info_url, RequestType.GET, paramMap);

		if (retObj.getInteger("code") == 0) {
			salemenu = JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), SalemenuBean.class);
		}
		return salemenu;

	}

	@Override
	public List<SalemenuBean> searchSalemenu(SalemenuFilterParam param) throws Exception {
		List<SalemenuBean> salemenuList = null;

		JSONObject retObj = baseRequest.baseRequest(SalemenuURL.search_salemenu_url, RequestType.GET, param);

		if (retObj.getInteger("code") == 0) {
			salemenuList = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SalemenuBean.class);
		}
		return salemenuList;
	}

	@Override
	public String createSalemenu(SalemenuBean salemenu) throws Exception {
		String urlStr = SalemenuURL.create_salemenu_url;
		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, salemenu);

		return retObj.getInteger("code") == 0 ? retObj.getString("data") : null;

	}

	@Override
	public boolean updateSalemenu(SalemenuBean salemenu) throws Exception {
		String urlStr = SalemenuURL.update_salemenu_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, salemenu);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<SalemenuBean.Target> getTargetArray() throws Exception {
		List<SalemenuBean.Target> targetList = null;

		JSONObject retObj = baseRequest.baseRequest(CategoryURL.target_list_url, RequestType.GET);

		if (retObj.getInteger("code") == 0) {
			targetList = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SalemenuBean.Target.class);
		}
		return targetList;
	}

	@Override
	public boolean deleteSalemenu(String salemenu_id) throws Exception {
		boolean result = false;

		String url = SalemenuURL.delete_salemenu_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", salemenu_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		BigDecimal task_id = null;
		if (retObj.getInteger("code") == 0) {
			Thread.sleep(1000);
			List<AsyncTaskResultBean> asyncTasks = asyncService.getAsyncTaskResultList();
			for (AsyncTaskResultBean asyncTask : asyncTasks) {
				if (asyncTask.getTask_name().contains(salemenu_id)) {
					task_id = asyncTask.getTask_id();
					break;
				}
			}
			if (task_id != null) {
				result = asyncService.getAsyncTaskResult(task_id, "删除报价单成功");
			}
		}
		return result;
	}

	@Override
	public BigDecimal createShareSalemenu(String salemenu_id) throws Exception {
		String urlStr = SalemenuURL.create_share_salemenu_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("salemenu_id", salemenu_id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0 ? new BigDecimal(retObj.getJSONObject("data").getString("id")) : null;
	}

	@Override
	public Map<String, SkuBean> getShareSalemenu(BigDecimal share_id) throws Exception {
		String urlStr = SalemenuURL.get_share_salemenu_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("share_id", String.valueOf(share_id));

		JSONObject retObj = baseRequest.baseRequestWithoutCookie(urlStr, RequestType.GET, paramMap);

		List<SkuBean> skuList = null;

		if (retObj.getInteger("code") == 0) {
			skuList = new ArrayList<SkuBean>();
			JSONObject sku_data = retObj.getJSONObject("data").getJSONObject("sku_data");
			for (Object key : sku_data.keySet()) {
				JSONArray skuArray = sku_data.getJSONArray(String.valueOf(key));
				List<SkuBean> temp_sku_list = JsonUtil.strToClassList(skuArray.toString(), SkuBean.class);
				skuList.addAll(temp_sku_list);
			}
		}
		return skuList.stream().collect(Collectors.toMap(SkuBean::getId, a -> a, (k1, k2) -> k1));
	}

	@Override
	public Map<String, SkuBean> getSalemenuSkus(String salemenu_id) throws Exception {
		String urlStr = SalemenuURL.get_salemenu_skus_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("salemenu_id", salemenu_id);
		paramMap.put("limit", "10");
		int offset = 0;
		List<SkuBean> skuList = null;
		while (true) {
			paramMap.put("offset", String.valueOf(offset));
			JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

			if (retObj.getInteger("code") == 0) {
				skuList = new ArrayList<SkuBean>();
				JSONArray sku_data = retObj.getJSONArray("data");
				List<SkuBean> temp_sku_list = JsonUtil.strToClassList(sku_data.toString(), SkuBean.class);
				skuList.addAll(temp_sku_list);
				if (temp_sku_list.size() <= 10) {
					offset += 10;
				} else {
					break;
				}
			} else {
				break;
			}
		}
		return skuList.stream().collect(Collectors.toMap(SkuBean::getId, a -> a, (k1, k2) -> k1));
	}

	@Override
	public JSONArray getAllSalemenu() throws Exception {
		String url = SalemenuURL.get_all_salemenu_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("json", "1");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		if (retObj.getInteger("code") == 0) {
			return retObj.getJSONArray("data");
		}
		return null;
	}

	@Override
	public List<SalemenuBean> getSalemenuList(int type, int is_active) throws Exception {
		String url = SalemenuURL.get_salemenu_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("type", String.valueOf(type));
		paramMap.put("is_active", String.valueOf(is_active));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SalemenuBean.class)
				: null;
	}

}
