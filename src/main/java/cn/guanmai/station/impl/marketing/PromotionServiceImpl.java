package cn.guanmai.station.impl.marketing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.category.SkuPromotionBean;
import cn.guanmai.station.bean.marketing.PromotionBean;
import cn.guanmai.station.bean.marketing.PromotionDetailBean;
import cn.guanmai.station.bean.marketing.PromotionResultBean;
import cn.guanmai.station.bean.marketing.param.PromotionDefaultParam;
import cn.guanmai.station.bean.marketing.param.PromotionFilterParam;
import cn.guanmai.station.bean.marketing.param.PromotionLimitParam;
import cn.guanmai.station.interfaces.marketing.PromotionService;
import cn.guanmai.station.url.MarketingURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Feb 21, 2019 11:08:09 AM 
* @des 营销活动业务实现类
* @version 1.0 
*/
public class PromotionServiceImpl implements PromotionService {
	private BaseRequest baseRequest;

	public PromotionServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<PromotionBean> searchPromotion(PromotionFilterParam filterParam) throws Exception {
		String url = MarketingURL.search_promotion_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);

		List<PromotionBean> promotionList = null;
		if (retObj.getInteger("code") == 0) {
			promotionList = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), PromotionBean.class);
		}
		return promotionList;
	}

	@Override
	public PromotionDetailBean getPromotionDetailById(String id) throws Exception {
		String url = MarketingURL.get_promotion_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		PromotionDetailBean promotionDetail = null;
		if (retObj.getInteger("code") == 0) {
			promotionDetail = JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(),
					PromotionDetailBean.class);
		}
		return promotionDetail;
	}

	@Override
	public boolean deletePromotion(String id) throws Exception {
		String url = MarketingURL.delete_promotion_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<SkuPromotionBean> promotionSkus() throws Exception {
		String url = MarketingURL.promotion_sku_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		List<SkuPromotionBean> skuPromotionList = null;
		if (retObj.getInteger("code") == 0) {
			skuPromotionList = new ArrayList<SkuPromotionBean>();
			JSONArray category1List = retObj.getJSONArray("data");
			for (Object category1Obj : category1List) {
				JSONObject category1 = JSONObject.parseObject(category1Obj.toString());
				JSONArray category2List = category1.getJSONArray("children");

				for (Object category2Obj : category2List) {
					JSONObject category2 = JSONObject.parseObject(category2Obj.toString());
					JSONArray skuList = category2.getJSONArray("children");
					List<SkuPromotionBean> tempSkuPromotions = JsonUtil.strToClassList(skuList.toString(),
							SkuPromotionBean.class);
					skuPromotionList.addAll(tempSkuPromotions);
				}
			}
		}
		return skuPromotionList;
	}

	@Override
	public String createPromotionDefault(PromotionDefaultParam param) throws Exception {
		String url = MarketingURL.create_promotion_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("name", param.getName());
		paramMap.put("active", String.valueOf(param.getActive()));
		paramMap.put("show_method", String.valueOf(param.getShow_method()));
		paramMap.put("sort", String.valueOf(param.getSort()));
		paramMap.put("enable_label_2", String.valueOf(param.getEnable_label_2()));
		paramMap.put("label_1_name", param.getLabel_1_name());
		paramMap.put("label_2", JsonUtil.objectToStr(param.getLabel_2()));
		paramMap.put("pic_url", param.getPic_url());
		paramMap.put("type", String.valueOf(param.getType()));
		paramMap.put("skus", JsonUtil.objectToStr(param.getSkus()));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("id") : null;
	}

	@Override
	public PromotionResultBean createPromotionLimit(PromotionLimitParam param) throws Exception {
		String url = MarketingURL.async_create_promotion_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("name", param.getName());
		paramMap.put("active", String.valueOf(param.getActive()));
		paramMap.put("show_method", String.valueOf(param.getShow_method()));
		paramMap.put("sort", String.valueOf(param.getSort()));
		paramMap.put("enable_label_2", String.valueOf(param.getEnable_label_2()));
		paramMap.put("label_1_name", param.getLabel_1_name());
		paramMap.put("label_2", JsonUtil.objectToStr(param.getLabel_2()));
		paramMap.put("pic_url", param.getPic_url());
		paramMap.put("type", String.valueOf(param.getType()));
		paramMap.put("skus", JsonUtil.objectToStr(param.getSkus()));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		int code = retObj.getInteger("code");
		PromotionResultBean promotionResult = new PromotionResultBean();
		promotionResult.setCode(code);
		if (code == 0) {
			String task_url = retObj.getJSONObject("data").getString("task_url");
			BigDecimal task_id = new BigDecimal(task_url.split("=")[1]);
			promotionResult.setTask_id(task_id);
		} else if (code == 1) {
			JSONArray dataArray = retObj.getJSONArray("data");
			List<String> usedSkus = new ArrayList<String>();
			for (Object obj : dataArray) {
				JSONObject dataObj = JSONObject.parseObject(obj.toString());
				usedSkus.add(dataObj.getString("sku_id"));
			}
			promotionResult.setUsedSkus(usedSkus);
		} else if (code == 4) {
			String msg = retObj.getString("msg");
			List<String> deleteSkus = Arrays.asList(msg.split(":")[1].split(","));
			promotionResult.setDeleteSkus(deleteSkus);
		} else {
			promotionResult = null;
		}
		return promotionResult;
	}

	@Override
	public boolean updatePromotionDefault(PromotionDefaultParam param) throws Exception {
		String url = MarketingURL.update_promotion_url;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", param.getId());
		paramMap.put("name", param.getName());
		paramMap.put("active", String.valueOf(param.getActive()));
		paramMap.put("show_method", String.valueOf(param.getShow_method()));
		paramMap.put("sort", String.valueOf(param.getSort()));
		paramMap.put("enable_label_2", String.valueOf(param.getEnable_label_2()));
		paramMap.put("label_1_name", param.getLabel_1_name());
		paramMap.put("label_2", JsonUtil.objectToStr(param.getLabel_2()));
		paramMap.put("pic_url", param.getPic_url());
		paramMap.put("type", String.valueOf(param.getType()));
		paramMap.put("skus", JsonUtil.objectToStr(param.getSkus()));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public PromotionResultBean updatePromotionLimit(PromotionLimitParam param) throws Exception {
		String url = MarketingURL.update_promotion_url;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", param.getId());
		paramMap.put("name", param.getName());
		paramMap.put("active", String.valueOf(param.getActive()));
		paramMap.put("show_method", String.valueOf(param.getShow_method()));
		paramMap.put("sort", String.valueOf(param.getSort()));
		paramMap.put("enable_label_2", String.valueOf(param.getEnable_label_2()));
		paramMap.put("label_1_name", param.getLabel_1_name());
		paramMap.put("label_2", JsonUtil.objectToStr(param.getLabel_2()));
		paramMap.put("pic_url", param.getPic_url());
		paramMap.put("type", String.valueOf(param.getType()));
		paramMap.put("skus", JsonUtil.objectToStr(param.getSkus()));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		int code = retObj.getInteger("code");
		PromotionResultBean promotionResult = new PromotionResultBean();
		promotionResult.setCode(code);

		if (code != 0) {
			if (code == 1) {
				JSONArray dataArray = retObj.getJSONArray("data");
				List<String> usedSkus = new ArrayList<String>();
				for (Object obj : dataArray) {
					JSONObject dataObj = JSONObject.parseObject(obj.toString());
					usedSkus.add(dataObj.getString("sku_id"));
				}
				promotionResult.setUsedSkus(usedSkus);
			} else if (code == 4) {
				String msg = retObj.getString("msg");
				List<String> deleteSkus = Arrays.asList(msg.split(":")[1].split(","));
				promotionResult.setDeleteSkus(deleteSkus);
			} else {
				promotionResult = null;
			}
		}
		return promotionResult;
	}

}
