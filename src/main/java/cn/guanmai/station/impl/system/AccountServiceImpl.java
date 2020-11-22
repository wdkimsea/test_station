package cn.guanmai.station.impl.system;

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
import cn.guanmai.station.bean.account.RoleBean;
import cn.guanmai.station.bean.account.RoleDetailBean;
import cn.guanmai.station.bean.account.UserBean;
import cn.guanmai.station.bean.account.UserDetailBean;
import cn.guanmai.station.bean.account.param.UserAddParam;
import cn.guanmai.station.bean.account.param.UserFilterParam;
import cn.guanmai.station.bean.account.param.UserUpdataParam;
import cn.guanmai.station.interfaces.system.AccountService;
import cn.guanmai.station.url.AccountURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Apr 23, 2019 11:18:35 AM 
* @des 
* @version 1.0 
*/
public class AccountServiceImpl implements AccountService {
	private BaseRequest baseRequest;

	public AccountServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<Integer> getAllPermissions(String station_id) throws Exception {
		String url = AccountURL.get_permissions_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("stations", "1");
		paramMap.put("station_id", station_id);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		List<Integer> permissionList = null;
		if (retObj.getInteger("code") == 0) {
			permissionList = new ArrayList<>();
			JSONArray station_permissions = retObj.getJSONObject("data").getJSONArray("station_permissions");
			for (Object obj : station_permissions) {
				JSONObject moduleObj = JSONObject.parseObject(obj.toString());
				JSONArray contentArray = moduleObj.getJSONArray("content");
				for (int i = 0; i < contentArray.size(); i++) {
					JSONObject contentObj = contentArray.getJSONObject(i);
					JSONArray permissions = contentObj.getJSONArray("permissions");
					for (int j = 0; j < permissions.size(); j++) {
						permissionList.add(permissions.getJSONObject(j).getInteger("id"));
					}
				}
			}
		}
		return permissionList;
	}

	@Override
	public List<RoleBean> searchRole(String station_id, String search_text) throws Exception {
		String url = AccountURL.search_role_url;

		JSONObject retObj = null;
		if (search_text == null && station_id == null) {
			retObj = baseRequest.baseRequest(url, RequestType.GET);
		} else {
			Map<String, String> paramMap = new HashMap<>();
			if (station_id != null) {
				paramMap.put("station_id", station_id);
			}
			if (search_text != null) {
				paramMap.put("search_text", search_text);
			}
			retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		}

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONObject("data").getJSONArray("roles").toString(), RoleBean.class)
				: null;
	}

	@Override
	public BigDecimal addRole(RoleDetailBean param) throws Exception {
		String url = AccountURL.add_role_url;
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, param);

		return retObj.getInteger("code") == 0 ? new BigDecimal(retObj.getJSONObject("data").getString("id")) : null;
	}

	@Override
	public RoleDetailBean getRoleDetail(BigDecimal id) throws Exception {
		String url = AccountURL.get_role_detail_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("id", String.valueOf(id));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), RoleDetailBean.class)
				: null;
	}

	@Override
	public boolean updateRole(RoleDetailBean roleDetail) throws Exception {
		String url = AccountURL.update_role_url;
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, roleDetail);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteRole(BigDecimal id) throws Exception {
		String url = AccountURL.delete_role_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("id", String.valueOf(id));
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<UserBean> searchUser(UserFilterParam filterParam) throws Exception {
		String url = AccountURL.search_user_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONObject("data").getJSONArray("users").toString(), UserBean.class)
				: null;
	}

	@Override
	public UserDetailBean getUserDetail(Integer id) throws Exception {
		String url = AccountURL.get_user_detail_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("id", String.valueOf(id));
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), UserDetailBean.class)
				: null;
	}

	@Override
	public boolean updateUser(UserUpdataParam param) throws Exception {
		String url = AccountURL.update_user_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, param);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public Integer addUser(UserAddParam param) throws Exception {
		String url = AccountURL.create_user_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, param);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getInteger("id") : null;
	}

	@Override
	public boolean updateUserPwd(int id, String password) throws Exception {
		String url = AccountURL.update_user_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("id", String.valueOf(id));
		paramMap.put("password", password);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteUser(Integer id) throws Exception {
		String url = AccountURL.delete_user_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("id", String.valueOf(id));
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

}
