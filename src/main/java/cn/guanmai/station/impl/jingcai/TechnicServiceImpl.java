package cn.guanmai.station.impl.jingcai;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.account.RoleBean;
import cn.guanmai.station.bean.jingcai.TechnicBean;
import cn.guanmai.station.bean.jingcai.TechnicCategoryBean;
import cn.guanmai.station.bean.jingcai.TechnicFlowBean;
import cn.guanmai.station.bean.jingcai.param.TechnicFilterParam;
import cn.guanmai.station.bean.jingcai.param.TechnicFlowCreateParam;
import cn.guanmai.station.bean.jingcai.param.TechnicFlowUpdateParam;
import cn.guanmai.station.impl.system.AccountServiceImpl;
import cn.guanmai.station.interfaces.jingcai.TechnicService;
import cn.guanmai.station.interfaces.system.AccountService;
import cn.guanmai.station.url.JingcaiURL;
import cn.guanmai.util.JsonUtil;

/**
 * @author liming
 * @date 2019年8月7日 上午10:35:28
 * @des
 * @version 1.0
 */
public class TechnicServiceImpl implements TechnicService {
	private BaseRequest baseRequest;
	private AccountService accountService;

	public TechnicServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
		accountService = new AccountServiceImpl(headers);
	}

	@Override
	public String createTechnicCategory(String name) throws Exception {
		String url = JingcaiURL.technic_category_create_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("name", name);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("id") : null;
	}

	@Override
	public boolean deleteTechnicCategory(String id) throws Exception {
		String url = JingcaiURL.technic_category_delete_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<TechnicCategoryBean> searchTechnicCategory(String q) throws Exception {
		String url = JingcaiURL.technic_category_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("limit", "1000");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), TechnicCategoryBean.class)
				: null;
	}

	@Override
	public String createTechnic(TechnicBean technicCreateParam) throws Exception {
		String url = JingcaiURL.technic_create_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, technicCreateParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("id") : null;
	}

	@Override
	public boolean updateTechnic(TechnicBean technic) throws Exception {
		String url = JingcaiURL.technic_update_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, technic);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<TechnicBean> searchTechnic(TechnicFilterParam technicFilterParam) throws Exception {
		String url = JingcaiURL.technic_list_url;

		boolean more = true;
		JSONObject retObj = null;
		List<TechnicBean> technicList = new ArrayList<TechnicBean>();
		String page_obj = null;
		while (more) {
			retObj = baseRequest.baseRequest(url, RequestType.GET, technicFilterParam);
			if (retObj.getInteger("code") == 0) {
				technicList.addAll(JsonUtil.strToClassList(
						retObj.getJSONObject("data").getJSONArray("technic_data").toString(), TechnicBean.class));
				more = retObj.getJSONObject("data").getJSONObject("pagination").getBoolean("more");
				if (!more) {
					break;
				}
				page_obj = retObj.getJSONObject("data").getJSONObject("pagination").getString("page_obj");
				technicFilterParam.setPage_obj(page_obj);
				technicFilterParam.setOffset(technicFilterParam.getOffset() + 1);
			} else {
				more = false;
				technicList = null;
			}
		}
		return technicList;
	}

	@Override
	public List<TechnicBean> getAllTechnics() throws Exception {
		String url = JingcaiURL.technic_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("limit", "0");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassList(
				retObj.getJSONObject("data").getJSONArray("technic_data").toString(), TechnicBean.class) : null;
	}

	@Override
	public TechnicBean getTechnic(String id) throws Exception {
		String url = JingcaiURL.technic_get_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", String.valueOf(id));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), TechnicBean.class)
				: null;
	}

	@Override
	public boolean deleteTechnic(String id) throws Exception {
		String url = JingcaiURL.technic_delete_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean importTechnic(List<TechnicBean> technics) throws Exception {
		String url = JingcaiURL.technic_import_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("technics", JsonUtil.objectToStr(technics));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public void initTechnic() throws Exception {
		String technic_category_name = "手工";
		List<TechnicCategoryBean> technicCategorys = searchTechnicCategory(technic_category_name);
		if (technicCategorys == null) {
			throw new Exception("获取工艺类型失败");
		}

		TechnicCategoryBean technicCategory = technicCategorys.stream()
				.filter(t -> t.getName().equals(technic_category_name)).findFirst().orElse(null);

		String technic_category_id = null;
		if (technicCategory == null) {
			technic_category_id = createTechnicCategory(technic_category_name);
			if (technic_category_id == null) {
				throw new Exception("新建工艺类型失败");
			}
		} else {
			technic_category_id = technicCategory.getId();
		}

		List<RoleBean> roles = accountService.searchRole(null, null);
		if (roles == null) {
			throw new Exception("拉取站点角色失败");
		}

		Random random = new Random();
		RoleBean role = roles.get(random.nextInt(roles.size()));
		BigDecimal role_id = role.getId();

		TechnicFilterParam technicFilterParam = new TechnicFilterParam();
		technicFilterParam.setQ("清洗");

		List<TechnicBean> technics = searchTechnic(technicFilterParam);
		if (technics == null) {
			throw new Exception("查询工艺信息失败");
		}

		TechnicBean technic = technics.stream().filter(t -> t.getName().equals("清洗")).findAny()
				.orElse(new TechnicBean());
		technic.setName("清洗");
		technic.setCustom_id("GY-QX");
		technic.setDesc("对商品进行初步处理,对其进行浸泡和清洗");
		technic.setDefault_role(role_id);
		technic.setTechnic_category_id(technic_category_id);

		List<TechnicBean.CustomCol> customCols = new ArrayList<>();
		TechnicBean.CustomCol customCol = technic.new CustomCol();
		customCol.setCol_name("浸泡");

		List<TechnicBean.CustomCol.Param> params = new ArrayList<>();
		TechnicBean.CustomCol.Param param = customCol.new Param();
		param.setParam_name("10分钟");
		params.add(param);

		param = customCol.new Param();
		param.setParam_name("20分钟");
		params.add(param);

		param = customCol.new Param();
		param.setParam_name("30分钟");
		params.add(param);

		customCol.setParams(params);

		customCols.add(customCol);

		customCol = technic.new CustomCol();
		customCol.setCol_name("漂洗");

		params = new ArrayList<>();
		param = customCol.new Param();
		param.setParam_name("一次");
		params.add(param);

		param = customCol.new Param();
		param.setParam_name("两次");
		params.add(param);

		param = customCol.new Param();
		param.setParam_name("三次");
		params.add(param);

		customCol.setParams(params);

		customCols.add(customCol);

		technic.setCustom_cols(customCols);

		if (technic.getId() == null) {
			String technic_id = createTechnic(technic);
			if (technic_id == null) {
				throw new Exception("新建工艺信息失败");
			}
		} else {
			technic.setForce(1);
			boolean result = updateTechnic(technic);
			if (!result) {
				throw new Exception("修改工艺信息失败");
			}
		}

		technicFilterParam = new TechnicFilterParam();
		technicFilterParam.setQ("分切");

		technics = searchTechnic(technicFilterParam);
		if (technics == null) {
			throw new Exception("查询工艺信息失败");
		}

		technic = technics.stream().filter(t -> t.getName().equals("分切")).findAny().orElse(new TechnicBean());

		technic.setName("分切");
		technic.setCustom_id("GY-FQ");
		technic.setDesc("对商品进行进一步处理,削皮、切块、切丝");
		technic.setDefault_role(role_id);
		technic.setTechnic_category_id(technic_category_id);

		customCols = new ArrayList<>();
		customCol = technic.new CustomCol();
		customCol.setCol_name("切");

		params = new ArrayList<>();
		param = customCol.new Param();
		param.setParam_name("切块");
		params.add(param);

		param = customCol.new Param();
		param.setParam_name("切丝");
		params.add(param);

		param = customCol.new Param();
		param.setParam_name("削皮");
		params.add(param);

		customCol.setParams(params);

		customCols.add(customCol);

		technic.setCustom_cols(customCols);
		if (technic.getId() == null) {
			String technic_id = createTechnic(technic);
			if (technic_id == null) {
				throw new Exception("新建工艺信息失败");
			}
		} else {
			technic.setForce(1);
			boolean result = updateTechnic(technic);
			if (!result) {
				throw new Exception("修改工艺信息失败");
			}
		}
	}

	@Override
	public List<TechnicFlowBean> getTechnicFlows(String sku_id, String ingredient_id) throws Exception {
		String url = JingcaiURL.process_technic_flow_get_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("sku_id", sku_id);
		paramMap.put("ingredient_id", ingredient_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), TechnicFlowBean.class)
				: null;
	}

	@Override
	public String createTechnicFlow(TechnicFlowCreateParam technicFlowCreateParam) throws Exception {
		String url = JingcaiURL.process_technic_flow_create_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, technicFlowCreateParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("id") : null;
	}

	@Override
	public boolean updateTechnicFlow(TechnicFlowUpdateParam technicFlowUpdateParam) throws Exception {
		String url = JingcaiURL.process_technic_flow_update_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, technicFlowUpdateParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteTechnicFlow(String id) throws Exception {
		String url = JingcaiURL.process_technic_flow_delete_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean changeTechnicFlow(String id, String next_id) throws Exception {
		String url = JingcaiURL.process_technic_flow_change_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);
		paramMap.put("next_id", next_id);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

}
