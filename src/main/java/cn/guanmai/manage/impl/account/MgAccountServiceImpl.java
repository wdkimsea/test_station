package cn.guanmai.manage.impl.account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.manage.bean.account.MgPermissionBean;
import cn.guanmai.manage.bean.account.MgRoleBean;
import cn.guanmai.manage.bean.account.MgUserBean;
import cn.guanmai.manage.bean.account.StRoleBean;
import cn.guanmai.manage.bean.account.StUserBean;
import cn.guanmai.manage.bean.account.StationInfoBean;
import cn.guanmai.manage.bean.account.param.MgRoleCreateParam;
import cn.guanmai.manage.bean.account.param.MgUserCreateParam;
import cn.guanmai.manage.bean.account.param.MgUserFilterParam;
import cn.guanmai.manage.bean.account.param.StRoleCreateParam;
import cn.guanmai.manage.bean.account.param.StUserCreateParam;
import cn.guanmai.manage.bean.account.param.StUserFilterParam;
import cn.guanmai.manage.interfaces.account.MgAccountService;
import cn.guanmai.manage.url.ManageAccountURL;
import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.util.JsonUtil;

/**
 * @author liming
 * @date 2019年8月22日
 * @time 下午7:46:13
 * @des TODO
 */

public class MgAccountServiceImpl implements MgAccountService {
	private BaseRequest baseRequest;

	public MgAccountServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<String> getAllStationIds() throws Exception {
		String url = ManageAccountURL.gm_account_station_meta_info_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("stations", "1");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		List<String> station_ids = null;
		if (retObj.getInteger("code") == 0) {
			station_ids = new ArrayList<String>();
			JSONArray stations = retObj.getJSONObject("data").getJSONArray("stations");
			for (Object obj : stations) {
				JSONObject staiton = JSONObject.parseObject(obj.toString());
				station_ids.add(staiton.getString("id"));
			}
		}
		return station_ids;
	}

	@Override
	public List<StationInfoBean> getAllStations() throws Exception {
		String url = ManageAccountURL.gm_account_station_meta_info_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("stations", "1");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassList(
				retObj.getJSONObject("data").getJSONArray("stations").toString(), StationInfoBean.class) : null;
	}

	@Override
	public List<StRoleBean> searchStationRoles(String station_id, String search_text) throws Exception {
		String url = ManageAccountURL.gm_account_station_role_search_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		if (station_id != null) {
			paramMap.put("station_id", station_id);
		}

		if (search_text != null) {
			paramMap.put("search_text", search_text);
		}

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0 ? JsonUtil
				.strToClassList(retObj.getJSONObject("data").getJSONArray("roles").toString(), StRoleBean.class) : null;
	}

	public List<Integer> getStationPermissions(String station_id) throws Exception {
		String url = ManageAccountURL.gm_account_station_meta_info_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("station_id", station_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		List<Integer> station_permissions = null;
		if (retObj.getInteger("code") == 0) {
			station_permissions = new ArrayList<Integer>();
			JSONArray permissionArray = retObj.getJSONObject("data").getJSONArray("station_permissions");
			for (Object obj : permissionArray) {
				JSONObject permissionObj = JSONObject.parseObject(obj.toString());
				JSONArray contentArray = permissionObj.getJSONArray("content");
				for (Object c : contentArray) {
					JSONObject contentObj = JSONObject.parseObject(c.toString());
					JSONArray permissions = contentObj.getJSONArray("permissions");
					for (Object p : permissions) {
						JSONObject permission = JSONObject.parseObject(p.toString());
						station_permissions.add(permission.getInteger("id"));
					}
				}

			}
		}
		return station_permissions;
	}

	@Override
	public BigDecimal createStationRole(StRoleCreateParam stationRoleCreateParam) throws Exception {
		String url = ManageAccountURL.gm_account_station_role_create_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, stationRoleCreateParam);

		return retObj.getInteger("code") == 0 ? new BigDecimal(retObj.getJSONObject("data").getString("id")) : null;
	}

	@Override
	public List<StUserBean> searchStationUser(StUserFilterParam filterParam) throws Exception {
		String url = ManageAccountURL.gm_account_station_user_search_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);

		return retObj.getInteger("code") == 0 ? JsonUtil
				.strToClassList(retObj.getJSONObject("data").getJSONArray("users").toString(), StUserBean.class) : null;
	}

	@Override
	public BigDecimal createStationUser(StUserCreateParam createParam) throws Exception {
		String url = ManageAccountURL.gm_account_station_user_create_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, createParam);

		return retObj.getInteger("code") == 0 ? new BigDecimal(retObj.getJSONObject("data").getString("id")) : null;
	}

	@Override
	public boolean deleteStationUser(BigDecimal id) throws Exception {
		String url = ManageAccountURL.gm_account_station_user_delete_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", String.valueOf(id));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteStationRole(BigDecimal id) throws Exception {
		String url = ManageAccountURL.gm_account_station_role_delete_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", String.valueOf(id));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean changeStationUserPassword(BigDecimal user_id, String password) throws Exception {
		String url = ManageAccountURL.gm_account_station_user_update_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", String.valueOf(user_id));
		paramMap.put("password", password);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<MgRoleBean> searchManageRole(String search_text) throws Exception {
		String url = ManageAccountURL.gm_account_manage_role_search_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("search_text", search_text);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0 ? JsonUtil
				.strToClassList(retObj.getJSONObject("data").getJSONArray("roles").toString(), MgRoleBean.class) : null;
	}

	@Override
	public List<MgPermissionBean> getManagePermissions() throws Exception {
		String url = ManageAccountURL.gm_account_manage_meta_info_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("ma_permissions", "1");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		List<MgPermissionBean> mgPermissions = null;
		if (retObj.getInteger("code") == 0) {
			mgPermissions = new ArrayList<MgPermissionBean>();
			JSONArray ma_permissions = retObj.getJSONObject("data").getJSONArray("ma_permissions");
			for (Object obj : ma_permissions) {
				JSONObject ma_permission = JSONObject.parseObject(obj.toString());
				JSONArray contentArray = ma_permission.getJSONArray("content");
				mgPermissions.addAll(JsonUtil.strToClassList(contentArray.toString(), MgPermissionBean.class));
			}
		}
		return mgPermissions;
	}

	@Override
	public String createManageRole(MgRoleCreateParam mgRoleCreateParam) throws Exception {
		String url = ManageAccountURL.gm_account_manage_role_create_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, mgRoleCreateParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("id") : null;
	}

	@Override
	public List<MgUserBean> searchManagerUser(MgUserFilterParam mgUserFilterParam) throws Exception {
		String url = ManageAccountURL.gm_account_manager_user_search_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, mgUserFilterParam);

		return retObj.getInteger("code") == 0 ? JsonUtil
				.strToClassList(retObj.getJSONObject("data").getJSONArray("users").toString(), MgUserBean.class) : null;
	}

	@Override
	public String createManageUser(MgUserCreateParam mgUserCreateParam) throws Exception {
		String url = ManageAccountURL.gm_account_manager_user_create_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, mgUserCreateParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("id") : null;
	}

}
