package cn.guanmai.station.impl.delivery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.testng.Assert;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.delivery.RouteBean;
import cn.guanmai.station.bean.delivery.RouteBindCustomerBean;
import cn.guanmai.station.bean.delivery.RouteTask;
import cn.guanmai.station.bean.delivery.param.RouteTaskFilterParam;
import cn.guanmai.station.interfaces.delivery.RouteService;
import cn.guanmai.station.url.DistributeURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Apr 1, 2019 2:40:51 PM 
* @todo TODO
* @version 1.0 
*/
public class RouteServiceImpl implements RouteService {
	private BaseRequest baseRequest;

	public RouteServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public boolean createRoute(String name) throws Exception {
		String url = DistributeURL.create_route_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("name", name);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteRoute(BigDecimal id) throws Exception {
		String url = DistributeURL.delete_route_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", String.valueOf(id));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<RouteBean> getAllRoutes() throws Exception {
		String url = DistributeURL.get_route_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("limit", "1000");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), RouteBean.class)
				: null;
	}

	@Override
	public List<RouteBean> getRouteList(String search_text, int offset, int limit) throws Exception {
		String url = DistributeURL.get_route_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("search_text", search_text);
		paramMap.put("offset", String.valueOf(offset));
		paramMap.put("limit", String.valueOf(limit));
		paramMap.put("peek", "60");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), RouteBean.class)
				: null;
	}

	@Override
	public List<RouteBindCustomerBean> getRouteBindCustomer(BigDecimal route_id) throws Exception {
		String url = DistributeURL.get_route_bind_customer_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", String.valueOf(route_id));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), RouteBindCustomerBean.class)
				: null;
	}

	@Override
	public boolean updateRouteBindCustomer(BigDecimal address_route_id, List<BigDecimal> address_ids) throws Exception {
		String url = DistributeURL.update_route_bind_customer_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", String.valueOf(address_route_id));
		paramMap.put("address_ids", address_ids.toString());

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean exportRoute(String search_text) throws Exception {
		String url = DistributeURL.get_route_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("search_text", search_text);
		paramMap.put("offset", "0");
		paramMap.put("limit", "10");
		paramMap.put("peek", "60");
		paramMap.put("export", "1");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<RouteTask> searchRouteTasks(RouteTaskFilterParam filterParam) throws Exception {
		String url = DistributeURL.search_route_task_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONObject("data").getJSONArray("route_tasks").toString(),
						RouteTask.class)
				: null;
	}

	@Override
	public BigDecimal initRouteData() throws Exception {
		String route_name = "人民路";
		BigDecimal route_id = null;
		List<RouteBean> routeList = getRouteList(route_name, 0, 10);
		Assert.assertNotEquals(routeList, null, "获取配送线路列表失败");

		if (routeList.size() == 0) {
			boolean result = createRoute(route_name);
			Assert.assertEquals(result, true, "创建配送线路失败");
			routeList = getRouteList(route_name, 0, 10);
		}
		RouteBean Route = routeList.stream().filter(a -> a.getName().contains(route_name)).findAny().orElse(null);
		Assert.assertNotEquals(Route, null, "没有找到名称为 " + route_name + " 的配送线路");

		route_id = Route.getId();

		List<RouteBindCustomerBean> routeBindCustomerList = getRouteBindCustomer(route_id);

		List<RouteBindCustomerBean> routeNoBindCustomerList = routeBindCustomerList.stream()
				.filter(a -> a.getRoute_id() == null).collect(Collectors.toList());

		if (routeNoBindCustomerList.size() > 0) {
			List<BigDecimal> address_ids = new ArrayList<>();
			for (RouteBindCustomerBean a : routeBindCustomerList) {
				if (a.getRoute_id() == null || a.getRoute_id().compareTo(route_id) == 0) {
					address_ids.add(a.getAddress_id());
				}
			}
			boolean result = updateRouteBindCustomer(route_id, address_ids);
			Assert.assertEquals(result, true, "更新配送线路绑定的商户失败");
		}
		return route_id;
	}

}
