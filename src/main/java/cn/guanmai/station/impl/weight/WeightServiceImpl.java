package cn.guanmai.station.impl.weight;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderDetailBean.Detail;
import cn.guanmai.station.bean.share.OrderAndSkuBean;
import cn.guanmai.station.bean.weight.CategoryTree;
import cn.guanmai.station.bean.weight.PdaOrderBean;
import cn.guanmai.station.bean.weight.PdaOrderDetailBean;
import cn.guanmai.station.bean.weight.PdaPatckageInfoBean;
import cn.guanmai.station.bean.weight.PdaWeightSkuBean;
import cn.guanmai.station.bean.weight.PdaWeightSkuDetailBean;
import cn.guanmai.station.bean.weight.PdaWeightSortDetailBean;
import cn.guanmai.station.bean.weight.PreSortingSkuBean;
import cn.guanmai.station.bean.weight.PreSortingSkuPackageBean;
import cn.guanmai.station.bean.weight.WeightBasketBean;
import cn.guanmai.station.bean.weight.WeightCategoryTreeBean;
import cn.guanmai.station.bean.weight.WeightCollectInfoBean;
import cn.guanmai.station.bean.weight.WeightCollectOrderBean;
import cn.guanmai.station.bean.weight.WeightCollectSkuBean;
import cn.guanmai.station.bean.weight.WeightGroupBean;
import cn.guanmai.station.bean.weight.WeightSkuBean;
import cn.guanmai.station.bean.weight.WeightTag;
import cn.guanmai.station.bean.weight.param.BatchOutOfStockParam;
import cn.guanmai.station.bean.weight.param.ChecklistParam;
import cn.guanmai.station.bean.weight.param.DiffOrderWeighParam;
import cn.guanmai.station.bean.weight.param.OldSetWeighParam;
import cn.guanmai.station.bean.weight.param.OutOfStockParam;
import cn.guanmai.station.bean.weight.param.PackWeighDataParam;
import cn.guanmai.station.bean.weight.param.PdaOrderDetailParam;
import cn.guanmai.station.bean.weight.param.PdaOrderFilterParam;
import cn.guanmai.station.bean.weight.param.PdaOutOfStockParam;
import cn.guanmai.station.bean.weight.param.PdaSetWeightParam;
import cn.guanmai.station.bean.weight.param.PdaWeightSkuDetailFilterParam;
import cn.guanmai.station.bean.weight.param.PrintInfoBean;
import cn.guanmai.station.bean.weight.param.SetWeightParam;
import cn.guanmai.station.bean.weight.param.UnionDispatchParam;
import cn.guanmai.station.bean.weight.param.WeighAllDataParam;
import cn.guanmai.station.bean.weight.param.WeighTaskParam;
import cn.guanmai.station.bean.weight.param.WeightCollectOrderFilterParam;
import cn.guanmai.station.bean.weight.param.WeightCollectSkuFilterParam;
import cn.guanmai.station.bean.weight.param.WeightDataFilterParam;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.weight.WeightService;
import cn.guanmai.station.url.WeightURL;
import cn.guanmai.util.JsonUtil;
import cn.guanmai.util.NumberUtil;

/* 
* @author liming 
* @date Jan 8, 2019 12:06:30 PM 
* @des 称重相关业务实现类
* @version 1.0 
*/
public class WeightServiceImpl implements WeightService {
	private BaseRequest baseRequest;
	private BaseRequest baseRequestWithAgent;
	private OrderService orderService;

	public WeightServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);

		orderService = new OrderServiceImpl(headers);
		Map<String, String> old_weigh_system_headers = new HashMap<String, String>();
		old_weigh_system_headers.putAll(headers);
		old_weigh_system_headers.put("User-Agent", "GmWeighting/2.1.0.23");
		baseRequestWithAgent = new BaseRequestImpl(old_weigh_system_headers);
	}

	@Override
	public boolean setEmployee(String employee_name) throws Exception {
		String url = WeightURL.set_employee_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("number", employee_name);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<WeightBasketBean> getWeightBaskets() throws Exception {
		String url = WeightURL.get_weight_basket_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), WeightBasketBean.class)
				: null;
	}

	@Override
	public boolean createWeightBasket(WeightBasketBean weightBasket) throws Exception {
		String url = WeightURL.create_weight_basket_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, weightBasket);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteWeightBasket(String id) throws Exception {
		String url = WeightURL.delete_weight_basket_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateWeightBasket(WeightBasketBean weightBasket) throws Exception {
		String url = WeightURL.update_weight_basket_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, weightBasket);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<WeightSkuBean> getWeightSkus(String time_config_id, String date) throws Exception {
		String url = WeightURL.get_weight_skus_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("time_config_id", time_config_id);
		paramMap.put("date", date);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassList(
				retObj.getJSONObject("data").getJSONArray("skus").toString(), WeightSkuBean.class) : null;
	}

	@Override
	public List<WeightSkuBean> getWeightSkus(WeightDataFilterParam filterParam) throws Exception {
		String url = WeightURL.get_weight_skus_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, filterParam);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassList(
				retObj.getJSONObject("data").getJSONArray("skus").toString(), WeightSkuBean.class) : null;
	}

	@Override
	public List<String> getWeightOrders(String time_config_id, String date) throws Exception {
		String url = WeightURL.get_weight_skus_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("time_config_id", time_config_id);
		paramMap.put("date", date);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		List<String> order_ids = null;
		if (retObj.getInteger("code") == 0) {
			order_ids = new ArrayList<>();
			for (Object key : retObj.getJSONObject("data").getJSONObject("orders").keySet()) {
				String order_id = String.valueOf(key);
				order_ids.add(order_id);
			}
		}
		return order_ids;
	}

	@Override
	public boolean batchOutOfStock(List<OrderAndSkuBean> orderSkuList) throws Exception {
		String urlStr = WeightURL.batch_out_of_stock_url;

		String paramStr = JsonUtil.objectToStr(orderSkuList);

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("skus", paramStr);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean batchOutOfStock(BatchOutOfStockParam paramBean) throws Exception {
		String urlStr = WeightURL.batch_out_of_stock_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramBean);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean batchOutOfStock(WeightCollectSkuFilterParam weightCollectSkuFilterParam) throws Exception {
		String urlStr = WeightURL.batch_out_of_stock_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, weightCollectSkuFilterParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean outOfStock(List<OutOfStockParam> outOfStockList) throws Exception {
		String urlStr = WeightURL.out_of_stock_url;

		String paramStr = JsonUtil.objectToStr(outOfStockList);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("skus", paramStr);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean setWeight(SetWeightParam setWeightParam) throws Exception {
		String urlStr = WeightURL.set_weight_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, setWeightParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<PrintInfoBean> getWeightSkuPrintInfo(List<OrderAndSkuBean> orderAndskuList) throws Exception {
		String url = WeightURL.get_weight_sku_print_info_url;

		String paramStr = JsonUtil.objectToStr(orderAndskuList);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("skus", paramStr);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), PrintInfoBean.class)
				: null;
	}

	@Override
	public boolean printSkuWeight(List<OrderAndSkuBean> printParam) throws Exception {
		String url = WeightURL.print_sku_weitht_url;

		String paramStr = JsonUtil.objectToStr(printParam);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("skus", paramStr);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<WeightGroupBean> getWeightGroupList() throws Exception {
		String url = WeightURL.get_weight_group_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), WeightGroupBean.class)
				: null;
	}

	@Override
	public String createWeightGroup(String name, List<String> spu_ids) throws Exception {
		String url = WeightURL.create_weight_group_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("name", name);
		paramMap.put("spu_ids", JSONArray.toJSONString(spu_ids));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("id") : null;
	}

	@Override
	public WeightGroupBean getWeightGroupDetail(String id) throws Exception {
		String url = WeightURL.get_weight_group_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), WeightGroupBean.class)
				: null;
	}

	@Override
	public boolean deleteWeightGroup(String id) throws Exception {
		String url = WeightURL.delete_weight_group_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateWeightGroup(String group_id, List<String> spu_ids) throws Exception {
		String url = WeightURL.update_weight_group_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("group_id", group_id);
		paramMap.put("spu_ids", JSONArray.toJSONString(spu_ids));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<WeightCategoryTreeBean> getWeightCategoryTree(String time_config_id, String date, boolean is_weight)
			throws Exception {
		String url = WeightURL.get_weight_category_tree_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("time_config_id", time_config_id);
		paramMap.put("date", date);
		paramMap.put("is_weight", String.valueOf(is_weight ? 1 : 0));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), WeightCategoryTreeBean.class)
				: null;
	}

	@Override
	public List<WeightCategoryTreeBean> getWeightCategoryUngroupTree() throws Exception {
		String url = WeightURL.get_weight_category_ungroup_tree_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), WeightCategoryTreeBean.class)
				: null;
	}

	@Override
	public boolean oneStepWeightOrder(String order_id) throws Exception {
		boolean result = true;
		OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
		SetWeightParam setWeightParam = new SetWeightParam();
		List<SetWeightParam.Weight> weights = null;
		SetWeightParam.Weight weight = null;
		String sku_id = null;
		BigDecimal set_weigh = null;
		for (Detail detail : orderDetail.getDetails()) {
			weights = new ArrayList<SetWeightParam.Weight>();

			sku_id = detail.getSku_id();
			set_weigh = detail.getStd_unit_quantity().add(NumberUtil.getRandomNumber(0, 3, 1));
			weight = setWeightParam.new Weight(order_id, sku_id, new BigDecimal("0"), set_weigh, false,
					detail.getSort_way());
			weights.add(weight);
			setWeightParam.setWeights(weights);
			result = setWeight(setWeightParam);
			if (!result) {
				break;
			}
		}
		return result;
	}

	@Override
	public boolean unOutOfStock(List<OutOfStockParam> outOfStockList) throws Exception {
		String urlStr = WeightURL.un_out_of_stock_url;

		String paramStr = JsonUtil.objectToStr(outOfStockList);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("skus", paramStr);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean getWeighChecklist(ChecklistParam paramBean) throws Exception {
		String urlStr = WeightURL.weigh_checklist_url;

		JSONObject retObj = baseRequestWithAgent.baseRequest(urlStr, RequestType.GET, paramBean);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean unionDispath(UnionDispatchParam paramBean) throws Exception {
		String urlStr = WeightURL.old_union_dispath_fast_task_url;

		JSONObject retObj = baseRequestWithAgent.baseRequest(urlStr, RequestType.GET, paramBean);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<CategoryTree> getCategoryTree(String station_id, String time_config_id, String cycle_start_time)
			throws Exception {
		String urlStr = WeightURL.old_what_can_i_do_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("station_id", station_id);
		paramMap.put("time_config_id", time_config_id);
		paramMap.put("cycle_start_time", cycle_start_time);
		paramMap.put("version", "3");
		paramMap.put("union_dispatch", "true");
		paramMap.put("filter", "category");

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), CategoryTree.class);
	}

	@Override
	public Map<String, String> getSpuByCategory2(String station_id, String time_config_id, String cycle_start_time,
			JSONArray category2_ids) throws Exception {
		String urlStr = WeightURL.old_what_can_i_do_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("station_id", station_id);
		paramMap.put("time_config_id", time_config_id);
		paramMap.put("cycle_start_time", cycle_start_time);
		paramMap.put("version", "3");
		paramMap.put("union_dispatch", "true");
		paramMap.put("category_id_2", category2_ids.toString());

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		Map<String, String> spuMap = new HashMap<>();
		JSONArray dataArray = retObj.getJSONArray("data");
		for (Object obj : dataArray) {
			JSONObject spuObj = JSONObject.parseObject(obj.toString());
			spuMap.put(spuObj.getString("spu_id"), spuObj.getString("name"));
		}
		return spuMap;

	}

	@Override
	public boolean getWeighAllData(WeighAllDataParam paramBean) throws Exception {
		String urlStr = WeightURL.old_get_weigh_all_data_url;

		JSONObject retObj = baseRequestWithAgent.baseRequest(urlStr, RequestType.GET, paramBean);

		return retObj.keySet().size() > 0;
	}

	@Override
	public boolean getDiffOrderWeight(DiffOrderWeighParam paramBean) throws Exception {
		String urlStr = WeightURL.old_get_diff_order_weight_url;
		JSONObject retObj = baseRequestWithAgent.baseRequest(urlStr, RequestType.GET, paramBean);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<WeightTag> getWeighTask(WeighTaskParam paramBean) throws Exception {
		String urlStr = WeightURL.old_get_weigh_task_url;

		JSONObject retObj = baseRequestWithAgent.baseRequest(urlStr, RequestType.POST, paramBean);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONObject("data").getJSONArray("tag_results").toString(),
						WeightTag.class)
				: null;
	}

	@Override
	public boolean packWeighData(PackWeighDataParam paramBean) throws Exception {
		String urlStr = WeightURL.old_pack_weigh_data_url;

		JSONObject retObj = baseRequestWithAgent.baseRequest(urlStr, RequestType.GET, paramBean);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean setWeight(OldSetWeighParam paramBean) throws Exception {
		String urlStr = WeightURL.old_set_weight_url;

		JSONObject retObj = baseRequestWithAgent.baseRequest(urlStr, RequestType.GET, paramBean);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public WeightCollectInfoBean getWeightCollectInfo(String time_config_id, String target_date) throws Exception {
		String url = WeightURL.get_station_weigh_info_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("time_config_id", time_config_id);
		paramMap.put("target_date", target_date);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), WeightCollectInfoBean.class)
				: null;
	}

	@Override
	public boolean getWeightCollectRandomOrderInfo(String time_config_id, String target_date, int random_num)
			throws Exception {
		String url = WeightURL.weight_collect_randomorder_list_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("time_config_id", time_config_id);
		paramMap.put("target_date", target_date);
		paramMap.put("random_num", String.valueOf(random_num));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean getgetWeightCollectRandomSkuInfo(String time_config_id, String target_date, int random_num)
			throws Exception {
		String url = WeightURL.weight_collect_randomsku_list_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("time_config_id", time_config_id);
		paramMap.put("target_date", target_date);
		paramMap.put("random_num", String.valueOf(random_num));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<WeightCollectOrderBean> getWeightCollectOrderInfo(WeightCollectOrderFilterParam filterParam)
			throws Exception {
		String url = WeightURL.weight_collect_order_list_url;
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);
		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassList(
				retObj.getJSONObject("data").getJSONArray("orders").toString(), WeightCollectOrderBean.class) : null;
	}

	@Override
	public List<WeightCollectSkuBean> getWeightCollectSkuInfo(WeightCollectSkuFilterParam filterParam)
			throws Exception {
		String url = WeightURL.weight_collect_sku_list_url;
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);
		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassList(
				retObj.getJSONObject("data").getJSONArray("skus").toString(), WeightCollectSkuBean.class) : null;
	}

	@Override
	public boolean stationBatchOutOfStock(List<OrderAndSkuBean> orderSkuList) throws Exception {
		String url = WeightURL.st_batch_out_of_stock_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("skus", JsonUtil.objectToStr(orderSkuList));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean stationSetWeight(SetWeightParam setWeightParam) throws Exception {
		String url = WeightURL.set_weight_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, setWeightParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<PreSortingSkuBean> getPreSortingSkuList(List<String> spu_ids, String start_date, String end_date)
			throws Exception {
		String url = WeightURL.weight_package_sku_list_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("spu_ids", JSONArray.toJSONString(spu_ids));
		paramMap.put("start_date", start_date);
		paramMap.put("end_date", end_date);
		paramMap.put("limit", "20000");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), PreSortingSkuBean.class)
				: null;
	}

	@Override
	public PreSortingSkuPackageBean getPreSortingSkuPackage(String sku_id, List<String> sku_ids, String start_date,
			String end_date) throws Exception {
		String url = WeightURL.weight_package_sku_package_list_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("sku_id", sku_id);
		paramMap.put("sku_ids", JSONArray.toJSONString(sku_ids));
		paramMap.put("start_date", start_date);
		paramMap.put("end_date", end_date);
		paramMap.put("limit", "20000");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), PreSortingSkuPackageBean.class)
				: null;
	}

	@Override
	public List<String> createWeightPackage(String sku_id, BigDecimal quantity, Integer count) throws Exception {
		String url = WeightURL.create_weight_package_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("sku_id", sku_id);
		paramMap.put("quantity", String.valueOf(quantity));

		if (count != null && count > 0) {
			paramMap.put("count", String.valueOf(count));
		}

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		List<String> package_ids = null;
		if (retObj.getInteger("code") == 0) {
			package_ids = new ArrayList<>();
			JSONArray dataArray = retObj.getJSONArray("data");
			for (Object obj : dataArray) {
				package_ids.add(JSONObject.parseObject(obj.toString()).getString("package_id"));
			}
		}
		return package_ids;
	}

	@Override
	public boolean deleteWeightPackage(String package_id) throws Exception {
		String url = WeightURL.delete_weight_package_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("package_id", package_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public PreSortingSkuPackageBean searchPreSortingSkuPackage(String package_id, List<String> spu_ids,
			String start_date, String end_date) throws Exception {
		String url = WeightURL.get_weight_package_sku_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("package_id", package_id);
		paramMap.put("spu_ids", JSONArray.toJSONString(spu_ids));
		paramMap.put("start_date", start_date);
		paramMap.put("end_date", end_date);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), PreSortingSkuPackageBean.class)
				: null;
	}

	@Override
	public PdaPatckageInfoBean searchPackageInPda(String package_id) throws Exception {
		String url = WeightURL.search_pda_package_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("package_id", package_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), PdaPatckageInfoBean.class)
				: null;
	}

	@Override
	public List<PdaWeightSkuBean> searchPdaWeightSkus(String search_text, String date, String time_config_id)
			throws Exception {
		String url = WeightURL.search_pda_weight_sku_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("search_text", search_text);
		paramMap.put("date", date);
		paramMap.put("time_config_id", time_config_id);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), PdaWeightSkuBean.class)
				: null;
	}

	@Override
	public PdaWeightSkuDetailBean getPdaWeightSkuDetail(PdaWeightSkuDetailFilterParam filterParam) throws Exception {
		String url = WeightURL.pda_weight_sku_detail_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), PdaWeightSkuDetailBean.class)
				: null;
	}

	@Override
	public PdaWeightSortDetailBean getPdaWeightSortDetail(String order_id, String sku_id, BigDecimal detail_id)
			throws Exception {
		String url = WeightURL.pda_weight_sort_detail_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);
		paramMap.put("sku_id", sku_id);
		if (detail_id != null) {
			paramMap.put("detail_id", String.valueOf(detail_id));
		}
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), PdaWeightSortDetailBean.class)
				: null;
	}

	@Override
	public boolean setWeightOfPda(PdaSetWeightParam pdaSetWeightParam) throws Exception {
		String url = WeightURL.pda_sort_set_weight_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, pdaSetWeightParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean outOfStockOfPda(PdaOutOfStockParam pdaOutOfStockParam) throws Exception {
		String url = WeightURL.pda_out_of_stock_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, pdaOutOfStockParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<PdaOrderBean> searchPdaOrders(PdaOrderFilterParam filterParam) throws Exception {
		String url = WeightURL.pda_order_list_url;

		boolean more = true;
		List<PdaOrderBean> pdaOrders = new ArrayList<>();
		JSONObject retObj = null;
		filterParam.setPage_obj(null);
		while (more) {
			retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);
			if (retObj.getInteger("code") == 0) {
				pdaOrders.addAll(JsonUtil.strToClassList(retObj.getJSONObject("data").getJSONArray("orders").toString(),
						PdaOrderBean.class));
				more = retObj.getJSONObject("pagination").getBoolean("more");
				if (more) {
					filterParam.setPage_obj(retObj.getJSONObject("pagination").getString("page_obj"));
				}
			} else {
				pdaOrders = null;
				break;
			}
		}
		return pdaOrders;
	}

	@Override
	public PdaOrderDetailBean getPdaOrderDetailBean(PdaOrderDetailParam pdaOrderDetailParam) throws Exception {
		String url = WeightURL.pda_order_detail_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, pdaOrderDetailParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), PdaOrderDetailBean.class)
				: null;
	}

}
