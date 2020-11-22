package cn.guanmai.station.impl.category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.category.CombineGoodsBean;
import cn.guanmai.station.bean.category.CombineGoodsDetailBean;
import cn.guanmai.station.bean.category.CombineGoodsPageBean;
import cn.guanmai.station.bean.category.param.CombineGoodsBatchFilterParam;
import cn.guanmai.station.bean.category.param.CombineGoodsFilterParam;
import cn.guanmai.station.bean.category.param.CombineGoodsParam;
import cn.guanmai.station.interfaces.category.CombineGoodsService;
import cn.guanmai.station.url.CategoryURL;
import cn.guanmai.util.JsonUtil;

/**
 * @author: liming
 * @Date: 2020年2月17日 下午4:46:23
 * @description:
 * @version: 1.0
 */

public class CombineGoodsServiceImpl implements CombineGoodsService {
	private BaseRequest baseRequest;

	public CombineGoodsServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public String createCombineGoods(CombineGoodsParam combineGoodsCreateParam) throws Exception {
		String url = CategoryURL.create_combine_goods_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, combineGoodsCreateParam);

		return retObj.getInteger("code") == 0 ? retObj.getString("data") : null;
	}

	@Override
	public boolean editCombineGoods(CombineGoodsParam combineGoodsEditParam) throws Exception {
		String url = CategoryURL.edit_combine_goods_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, combineGoodsEditParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean editCombineGoodsState(String id, int state) throws Exception {
		String url = CategoryURL.edit_combine_goods_state_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);
		paramMap.put("state", String.valueOf(state));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean batchEditCombineGoods(List<CombineGoodsParam> combineGoodsEditParams) throws Exception {
		String url = CategoryURL.batch_edit_combine_goods_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("data", JsonUtil.objectToStr(combineGoodsEditParams));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteCombineGoods(String id) throws Exception {
		String url = CategoryURL.delete_combine_goods_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public CombineGoodsDetailBean getCombineGoodsDetail(String id) throws Exception {
		String url = CategoryURL.get_combine_goods_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), CombineGoodsDetailBean.class)
				: null;
	}

	@Override
	public List<CombineGoodsBean> searchCombineGoods(CombineGoodsFilterParam combineGoodsFilterParam) throws Exception {
		String url = CategoryURL.search_combine_goods_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, combineGoodsFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), CombineGoodsBean.class)
				: null;
	}

	@Override
	public CombineGoodsPageBean searchCombineGoodsPage(CombineGoodsFilterParam combineGoodsFilterParam)
			throws Exception {
		String url = CategoryURL.search_combine_goods_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, combineGoodsFilterParam);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassObject(retObj.toString(), CombineGoodsPageBean.class)
				: null;
	}

	@Override
	public List<CombineGoodsDetailBean> batchSearchCombineGoods(
			CombineGoodsBatchFilterParam combineGoodsBatchFilterParam) throws Exception {
		String url = CategoryURL.batch_search_combine_goods_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, combineGoodsBatchFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), CombineGoodsDetailBean.class)
				: null;
	}

	@Override
	public String exportCombineGoods(CombineGoodsFilterParam combineGoodsFilterParam) throws Exception {
		String url = CategoryURL.export_combine_goods_url;

		String file_path = baseRequest.baseExport(url, RequestType.GET, combineGoodsFilterParam, "temp.xlsx");

		return file_path;
	}

	@Override
	public List<CombineGoodsBean> promotionCombineGoods() throws Exception {
		String url = CategoryURL.promotion_combine_goods_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), CombineGoodsBean.class)
				: null;
	}

}
