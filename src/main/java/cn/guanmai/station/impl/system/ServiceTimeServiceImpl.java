package cn.guanmai.station.impl.system;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.system.ServiceTimeBean;
import cn.guanmai.station.bean.system.ServiceTimeLimitBean;
import cn.guanmai.station.bean.system.param.ServiceTimeParam;
import cn.guanmai.station.interfaces.system.ServiceTimeService;
import cn.guanmai.station.url.SystemURL;
import cn.guanmai.util.JsonUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Nov 8, 2018 11:17:34 AM 
* @des 服务时间实现类
* @version 1.0 
*/
public class ServiceTimeServiceImpl implements ServiceTimeService {
	private BaseRequest baseRequest;

	public ServiceTimeServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public ServiceTimeBean getServiceTimeById(String id) throws Exception {
		ServiceTimeBean serviceTime = null;

		String urlStr = SystemURL.service_time_old_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("less", "1");
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		if (retObj.getInteger("code") == 0) {
			serviceTime = JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), ServiceTimeBean.class);
		}

		return serviceTime;
	}

	@Override
	public ServiceTimeLimitBean getServiceTimeLimit(String id) throws Exception {
		ServiceTimeBean serviceTime = null;

		String urlStr = SystemURL.service_time_old_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("less", "1");
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		ServiceTimeLimitBean serviceTimeLimit = null;
		if (retObj.getInteger("code") == 0) {
			serviceTime = JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), ServiceTimeBean.class);

			serviceTimeLimit = new ServiceTimeLimitBean();
			String order_start_time = serviceTime.getOrder_time_limit().getStart();
			String order_end_time = serviceTime.getOrder_time_limit().getEnd();
			serviceTimeLimit.setOrder_start_time(order_start_time);
			serviceTimeLimit.setOrder_end_time(order_end_time);

			ServiceTimeBean.ReceiveTimeLimit receiveTimeLimit = serviceTime.getReceive_time_limit();
			int weekdays = receiveTimeLimit.getWeekdays();
			String weekdaysBinary = Integer.toBinaryString(weekdays);

			int s_span_time = receiveTimeLimit.getS_span_time();
			int e_span_time = receiveTimeLimit.getE_span_time();

			String today = TimeUtil.getCurrentTime("yyyy-MM-dd");
			String receive_start_date = TimeUtil.calculateTime("yyyy-MM-dd", today, s_span_time, Calendar.DATE);
			String receive_end_date = TimeUtil.calculateTime("yyyy-MM-dd", today, e_span_time, Calendar.DATE);

			List<String> receive_dates = new ArrayList<String>();
			int lenth = weekdaysBinary.length();
			while (TimeUtil.compareDate("yyyy-MM-dd", receive_start_date, receive_end_date) <= 0) {
				int weekday = TimeUtil.getDateOfWeek(receive_start_date);
				if (lenth >= weekday) {
					char temp = weekdaysBinary.charAt(lenth - weekday);
					if (String.valueOf(temp).equals("1")) {
						receive_dates.add(receive_start_date);
					}
				}
				receive_start_date = TimeUtil.calculateTime("yyyy-MM-dd", receive_start_date, 1, Calendar.DATE);
			}
			serviceTimeLimit.setReceive_dates(receive_dates);
			serviceTimeLimit.setReceive_start_time(receiveTimeLimit.getStart());
			serviceTimeLimit.setReceive_end_time(receiveTimeLimit.getEnd());
			serviceTimeLimit.setReceiveTimeSpan(receiveTimeLimit.getReceiveTimeSpan());
		}
		return serviceTimeLimit;
	}

	@Override
	public List<String> getServiceTimeDateDetails(String id) throws Exception {
		String urlStr = SystemURL.service_time_old_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("date_detail", "true");
		paramMap.put("cycle_days", "2");
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		List<String> dateDetails = null;
		if (retObj.getInteger("code") == 0) {
			dateDetails = new ArrayList<>();
			JSONArray dataArray = retObj.getJSONArray("data");
			for (Object obj : dataArray) {
				JSONObject timeObj = JSONObject.parseObject(obj.toString());
				dateDetails.add(timeObj.getString("cycle_start_time"));
			}
		}
		return dateDetails;
	}

	@Override
	public List<ServiceTimeBean> serviceTimeList() throws Exception {
		List<ServiceTimeBean> ServiceTimeList = null;

		String urlStr = SystemURL.service_time_old_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET);

		if (retObj.getInteger("code") == 0) {
			ServiceTimeList = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), ServiceTimeBean.class);
		}
		return ServiceTimeList;
	}

	@Override
	public List<ServiceTimeBean> allTimeConfig() throws Exception {
		List<ServiceTimeBean> ServiceTimeList = null;

		String urlStr = SystemURL.service_time_old_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("all_time_config", "1");

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		if (retObj.getInteger("code") == 0) {
			ServiceTimeList = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), ServiceTimeBean.class);
		}
		return ServiceTimeList;
	}

	@Override
	public boolean createServiceTime(ServiceTimeBean serviceTime) throws Exception {
		String urlStr = SystemURL.service_time_old_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("name", serviceTime.getName());
		paramMap.put("desc", serviceTime.getDesc());
		paramMap.put("final_distribute_time", serviceTime.getFinal_distribute_time());
		paramMap.put("final_distribute_time_span", String.valueOf(serviceTime.getFinal_distribute_time_span()));
		paramMap.put("order_time_limit", JsonUtil.objectToStr(serviceTime.getOrder_time_limit()));
		paramMap.put("receive_time_limit", JsonUtil.objectToStr(serviceTime.getReceive_time_limit()));

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<ServiceTimeBean> serviceTimeList(int details) throws Exception {
		String url = SystemURL.service_time_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("details", String.valueOf(details));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), ServiceTimeBean.class)
				: null;

	}

	@Override
	public ServiceTimeBean getServiceTime(String id) throws Exception {
		String url = SystemURL.service_time_get_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), ServiceTimeBean.class)
				: null;
	}

	@Override
	public boolean createServiceTime(ServiceTimeParam serviceTimeParam) throws Exception {
		String url = SystemURL.service_time_save_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, serviceTimeParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateServiceTime(ServiceTimeParam serviceTimeParam) throws Exception {
		String url = SystemURL.service_time_save_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, serviceTimeParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteServiceTime(String id) throws Exception {
		String url = SystemURL.service_time_delete_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

}
