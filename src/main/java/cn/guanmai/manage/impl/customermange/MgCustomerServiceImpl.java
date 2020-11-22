package cn.guanmai.manage.impl.customermange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.manage.bean.custommange.param.MgCustomerAddParam;
import cn.guanmai.manage.bean.custommange.param.MgCustomerEditParam;
import cn.guanmai.manage.bean.custommange.param.MgCustomerFilterParam;
import cn.guanmai.manage.bean.custommange.param.MgCustomerImportAddParam;
import cn.guanmai.manage.bean.custommange.result.MgCustomerDetailBean;
import cn.guanmai.manage.bean.custommange.result.CustomerEmployeeInfoBean;
import cn.guanmai.manage.bean.custommange.result.CustomerBaseInfoBean;
import cn.guanmai.manage.bean.custommange.result.MgCustomerBean;
import cn.guanmai.manage.interfaces.custommange.MgCustomerService;
import cn.guanmai.manage.url.CustommanageURL;
import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Jan 14, 2019 7:03:32 PM 
* @des 商户管理相关业务实现类
* @version 1.0 
*/
public class MgCustomerServiceImpl implements MgCustomerService {
	private BaseRequest baseRequest;

	public MgCustomerServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public CustomerBaseInfoBean getCustomerBaseInfo() throws Exception {
		String url = CustommanageURL.customer_base_info;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		CustomerBaseInfoBean customerBaseInfo = null;
		if (retObj.getInteger("code") == 0) {
			customerBaseInfo = new CustomerBaseInfoBean();

			List<CustomerBaseInfoBean.Salemenu> salemenus = new ArrayList<>();
			CustomerBaseInfoBean.Salemenu salemenu = null;

			Map<String, String> station_district_code_map = new HashMap<String, String>();
			JSONObject stationObj = retObj.getJSONObject("data").getJSONObject("station");
			for (Object obj : stationObj.keySet()) {
				String station_id = String.valueOf(obj);
				JSONObject station = stationObj.getJSONObject(station_id);
				List<String> distribute_city_ids = new ArrayList<String>();
				station_district_code_map.put(station_id, station.getString("district_code"));

				for (Object city_id : station.getJSONArray("distribute_city_ids")) {
					distribute_city_ids.add(String.valueOf(city_id));
				}
				salemenu = customerBaseInfo.new Salemenu();
				salemenu.setStation_id(station_id);
				salemenu.setSalemenu_ids(new ArrayList<String>());
				salemenu.setDistribute_city_ids(distribute_city_ids);
				salemenus.add(salemenu);
			}

			customerBaseInfo.setStation_district_code_map(station_district_code_map);

			JSONArray salemenuArray = retObj.getJSONObject("data").getJSONArray("station_salemenu");

			for (Object obj : salemenuArray) {
				JSONObject salemenuObj = JSONObject.parseObject(obj.toString());
				String station_id = salemenuObj.getString("stations_id");
				String salemenu_id = salemenuObj.getString("salemenu_id");

				salemenu = salemenus.stream().filter(s -> s.getStation_id().equals(station_id)).findAny().orElse(null);
				List<String> salemenu_ids = salemenu.getSalemenu_ids();
				salemenu_ids.add(salemenu_id);

			}
			customerBaseInfo.setSalemenus(salemenus);

			JSONObject sale_employee_obj = retObj.getJSONObject("data").getJSONObject("sale_employee");
			List<String> sale_employee_ids = new ArrayList<String>();
			for (Object obj : sale_employee_obj.keySet()) {
				String sale_employee_id = String.valueOf(obj);
				sale_employee_ids.add(sale_employee_id);
			}
			customerBaseInfo.setSale_employee_ids(sale_employee_ids);

			JSONObject create_employee_obj = retObj.getJSONObject("data").getJSONObject("create_employee");
			List<String> create_employee_ids = new ArrayList<String>();
			for (Object obj : create_employee_obj.keySet()) {
				String create_employee_id = String.valueOf(obj);
				create_employee_ids.add(create_employee_id);
			}
			customerBaseInfo.setCreate_employee_ids(create_employee_ids);

			List<CustomerBaseInfoBean.District> districts = new ArrayList<>();
			CustomerBaseInfoBean.District district = null;
			JSONArray cityArray = retObj.getJSONObject("data").getJSONArray("district");
			for (Object obj : cityArray) {
				JSONObject cityObj = JSONObject.parseObject(obj.toString());
				district = customerBaseInfo.new District();
				district.setCity_name(cityObj.getString("name"));
				district.setCity_code(cityObj.getString("code"));

				JSONObject areaObj = retObj.getJSONObject("data").getJSONObject("area");
				List<CustomerBaseInfoBean.District.Area> areaList = new ArrayList<>();
				for (Object key : areaObj.keySet()) {
					String area_code = String.valueOf(key);
					String city_code = areaObj.getJSONObject(area_code).getString("district_code");
					if (city_code.equals(district.getCity_code())) {
						CustomerBaseInfoBean.District.Area area = district.new Area();
						area.setArea_code(area_code);
						area.setArea_name(areaObj.getJSONObject(area_code).getString("name"));
						areaList.add(area);
						JSONArray streets = areaObj.getJSONObject(area_code).getJSONArray("child");

						List<CustomerBaseInfoBean.District.Area.Street> streetList = new ArrayList<>();
						for (Object s : streets) {
							JSONObject streetObj = JSONObject.parseObject(s.toString());
							for (Object o : streetObj.keySet()) {
								CustomerBaseInfoBean.District.Area.Street street = area.new Street();
								street.setStreet_code(String.valueOf(o));
								street.setStreet_name(streetObj.getString(String.valueOf(o)));
								streetList.add(street);
							}
						}
						area.setStreets(streetList);
						areaList.add(area);
					}
				}
				district.setAreas(areaList);
				districts.add(district);

			}
			customerBaseInfo.setDistrict(districts);
		}
		return customerBaseInfo;
	}

	@Override
	public List<MgCustomerBean> searchCustomer(MgCustomerFilterParam filterParam) throws Exception {
		String urlStr = CustommanageURL.customer_search;
		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, filterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), MgCustomerBean.class)
				: null;

	}

	@Override
	public MgCustomerDetailBean getCustomerDetailInfoBySID(String sid) throws Exception {
		String urlStr = CustommanageURL.customer_detail_info;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", sid);
		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassObject(
				retObj.getJSONObject("data").getJSONObject("data").toString(), MgCustomerDetailBean.class) : null;
	}

	@Override
	public BigDecimal exportEditCustomerTemplate(List<String> sids) throws Exception {
		String url = CustommanageURL.export_eidt_customer_template_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("all", "0");
		paramMap.put("sids", JSONArray.toJSONString(sids));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}

	@Override
	public BigDecimal exportEditCustomerTemplate(MgCustomerFilterParam customerFilterParam) throws Exception {
		String url = CustommanageURL.export_eidt_customer_template_url;

		Map<String, String> paramMap = JsonUtil.objectToMap(customerFilterParam);
		paramMap.put("all", "1");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}

	@Override
	public BigDecimal importEditCustomer(String file_path) throws Exception {
		String url = CustommanageURL.customer_edit_import;

		JSONObject retObj = baseRequest.baseUploadRequest(url, new HashMap<String, String>(), "file", file_path);

		return retObj.getInteger("code") == 0 ? new BigDecimal(retObj.getJSONObject("data").getString("task_id"))
				: null;
	}

	@Override
	public boolean importAddCustomer(List<MgCustomerImportAddParam> customerImportAddParamList) throws Exception {
		String url = CustommanageURL.customer_add_import;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("data", JsonUtil.objectToStr(customerImportAddParamList));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public String createCustomer(MgCustomerAddParam customerAddParam) throws Exception {
		String urlStr = CustommanageURL.customer_add;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, customerAddParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("SID") : null;
	}

	@Override
	public boolean deleteCustomter(String sid) throws Exception {
		String url = CustommanageURL.customer_delete;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("sid", sid);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public CustomerEmployeeInfoBean getCustomerEmployeeInfo() throws Exception {
		String url = CustommanageURL.customer_employee_info;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), CustomerEmployeeInfoBean.class)
				: null;
	}

	@Override
	public Map<BigDecimal, String> getCustomerLabel() throws Exception {
		String url = CustommanageURL.customer_label;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("limit", "0");
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		Map<BigDecimal, String> labelMap = null;
		if (retObj.getInteger("code") == 0) {
			labelMap = new HashMap<BigDecimal, String>();
			JSONArray data = retObj.getJSONArray("data");
			for (Object obj : data) {
				JSONObject labelObj = JSONObject.parseObject(obj.toString());
				labelMap.put(new BigDecimal(labelObj.getString("id")), labelObj.getString("name"));
			}
		}
		return labelMap;
	}

	@Override
	public BigDecimal exportCustomer(MgCustomerFilterParam filterParam) throws Exception {
		String url = CustommanageURL.customer_search;

		filterParam.setExport(1);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);

		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}

	@Override
	public boolean editCustomer(MgCustomerEditParam customerEditParam) throws Exception {
		String url = CustommanageURL.customer_edit;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, customerEditParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean addSalemenu(String station_id, String sid, String salemenu_id) throws Exception {
		String url = CustommanageURL.customer_edit;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("status", "modifySSM");
		paramMap.put("service_station_id", station_id);
		paramMap.put("salemenu_id", salemenu_id);
		paramMap.put("id", sid);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0 || retObj.getString("msg").contains("类型不一致");
	}

	@Override
	public boolean deleteSalemenu(String sid, String record_id) throws Exception {
		String url = CustommanageURL.customer_edit;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("status", "deleteSSM");
		paramMap.put("record_id", record_id);
		paramMap.put("id", sid);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

}
