package cn.guanmai.bshop.impl;

import cn.guanmai.bshop.bean.*;
import cn.guanmai.bshop.bean.account.BsAccountBean;
import cn.guanmai.bshop.bean.order.PayMethod;
import cn.guanmai.bshop.bean.product.BsProductBean;
import cn.guanmai.bshop.service.BshopService;
import cn.guanmai.bshop.url.BsURL;
import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.util.ConfigureUtil;
import cn.guanmai.util.JsonUtil;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/* 
* @author liming 
* @date Jan 12, 2019 10:42:19 AM 
* @des bshop 相关实现类
* @version 1.0 
*/
public class BshopServiceImpl implements BshopService {
	private static Logger logger = LoggerFactory.getLogger(BshopServiceImpl.class);
	private BaseRequest baseRequest;
	private JSONObject retObj;
	private JSONArray dataArray;

	public BshopServiceImpl(Map<String, String> bshop_cookie) {
		baseRequest = new BaseRequestImpl(bshop_cookie);
	}

	@Override
	public BsAccountBean getAccountInfo() throws Exception {
		String url = BsURL.user_account_url;

		retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), BsAccountBean.class)
				: null;
	}

	@Override
	public boolean setAddress(SetAddressBean setAddressBean, String stationId) throws Exception {
		String url = BsURL.confirm_order_url;
		retObj = baseRequest.baseRequest(url, RequestType.GET);

		JSONArray orders = retObj.getJSONObject("data").getJSONArray("orders");

		for (int i = 0; i < orders.size(); i++) {
			JSONObject orderObj = orders.getJSONObject(i);
			String station_id = orderObj.getString("station_id");
			// 为true,说明站点ID一致,继续执行,为false,说明ID不一致,报价单是其他站点的
			boolean resultStationId = stationId.equals(station_id);
			if (!resultStationId) {
				// resultStationId是false
				Assert.assertNotEquals(resultStationId, false, "确认bshop订单时返回的站点Id同stationId不一致,无法设置自提点");
			}
		}

		String urlAddress = BsURL.set_address_url;

		retObj = baseRequest.baseRequest(urlAddress, RequestType.POST, setAddressBean);

		return retObj.getInteger("code") == 0;
	}

	/**
	 * 选择店铺
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean setAddress(String address_id) throws Exception {
		String url = BsURL.set_address_url;
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("address_id", address_id);

		retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<BsProductBean> searchSaleProducts(String text) throws Exception {
		String url = BsURL.search_sku_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("text", text);

		retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), BsProductBean.class)
				: null;
	}

	@Override
	public CartUpdateResult updateCart(Map<String, BigDecimal> skuMap) throws Exception {
		String url = BsURL.update_cart_url;

		Map<String, String> paramMap = new HashMap<>();
		JSONArray skus = new JSONArray();
		skus.add(JsonUtil.objectToStr(skuMap));
		paramMap.put("skus", skus.toString());

		retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), CartUpdateResult.class)
				: null;
	}

	@Override
	public JSONArray getSalemenuArray() throws Exception {
		String url = BsURL.get_salemenu_url;

		retObj = baseRequest.baseRequest(url, RequestType.GET);

		dataArray = retObj.getJSONArray("data");
		return dataArray;
	}

	@Override
	public Map<String, Boolean> salemenuStatus() throws Exception {
		// 判断报价单是否在运营时间范围内
		retObj = baseRequest.baseRequest(BsURL.get_salemenu_url, RequestType.GET);
		dataArray = retObj.getJSONArray("data");

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date now = new Date();
		Map<String, Boolean> salemenuMap = new HashMap<String, Boolean>();
		for (Object obj : dataArray) {
			JSONObject salemenuObj = JSONObject.parseObject(obj.toString());
			String order_begin_time = salemenuObj.getString("order_begin_time");
			String order_end_time = salemenuObj.getString("order_end_time");
			Date begin_time = df.parse(order_begin_time);
			Date end_time = df.parse(order_end_time);
			if (now.getTime() >= begin_time.getTime() && now.getTime() <= end_time.getTime()) {
				salemenuMap.put(salemenuObj.getString("_id"), true);
			} else {
				salemenuMap.put(salemenuObj.getString("_id"), false);
			}

		}
		return salemenuMap;
	}

	@Override
	public boolean setReceiveTime() throws Exception {
		String url = BsURL.confirm_order_url;

		retObj = baseRequest.baseRequest(url, RequestType.GET);

		if (retObj.getInteger("code") != 0) {
			return false;
		}
		JSONArray orders = retObj.getJSONObject("data").getJSONArray("orders");

		Map<String, String> paramMap = null;
		url = BsURL.set_receive_time_url;
		for (int i = 0; i < orders.size(); i++) {
			JSONObject orderObj = orders.getJSONObject(i);
			String station_id = orderObj.getString("station_id");
			JSONObject receive_time_limit = orderObj.getJSONObject("receive_time").getJSONObject("receive_time_limit");
			int s_span_time = receive_time_limit.getInteger("s_span_time");
			int receiveTimeSpan = receive_time_limit.getInteger("receiveTimeSpan");
			String r_start = receive_time_limit.getString("r_start");
			String r_end = TimeUtil.calculateTime("HH:mm", r_start, receiveTimeSpan, Calendar.MINUTE);
			String time_config_id = receive_time_limit.getString("time_config_id");

			paramMap = new HashMap<>();
			paramMap.put("defaultStart", r_start);
			paramMap.put("defaultEnd", r_end);
			paramMap.put("defaultSpanStartFlag", String.valueOf(s_span_time + 1));
			paramMap.put("defaultSpanEndFlag", String.valueOf(s_span_time + 1));
			paramMap.put("time_config_id", time_config_id);
			paramMap.put("station_id", station_id);

			retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

			if (retObj.getInteger("code") != 0)
				return false;

		}
		return true;
	}

	@Override
	public boolean setReceiveAddress(String address_id, int receive_way, String pick_up_st_id) throws Exception {
		String url = BsURL.set_receive_address_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("address_id", address_id);
		paramMap.put("receive_way", String.valueOf(receive_way));
		if (receive_way == 2 && pick_up_st_id != null) {
			paramMap.put("pick_up_st_id", pick_up_st_id);
		}

		retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean setPaymethod(PayMethod paymethod) throws Exception {
		String url = BsURL.confirm_order_url;
		retObj = baseRequest.baseRequest(url, RequestType.GET);

		JSONArray orders = retObj.getJSONObject("data").getJSONArray("orders");

		Map<String, String> paramMap = null;
		url = BsURL.paymethod_url;
		for (int i = 0; i < orders.size(); i++) {
			JSONObject orderObj = orders.getJSONObject(i);
			String station_id = orderObj.getString("station_id");
			JSONObject receive_time_limit = orderObj.getJSONObject("receive_time").getJSONObject("receive_time_limit");
			String time_config_id = receive_time_limit.getString("time_config_id");

			paramMap = new HashMap<>();
			paramMap.put("type", paymethod == PayMethod.One ? "1" : "2");
			paramMap.put("time_config_id", time_config_id);
			paramMap.put("station_id", station_id);

			retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

			if (retObj.getInteger("code") != 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	public List<String> submitCart(boolean combineOrder) throws Exception {
		String url = BsURL.confirm_order_url;
		retObj = baseRequest.baseRequest(url, RequestType.GET);

		JSONArray orders = retObj.getJSONObject("data").getJSONArray("orders");
		JSONArray skuArray = new JSONArray();
		for (int i = 0; i < orders.size(); i++) {
			JSONObject orderObj = orders.getJSONObject(i);
			JSONObject skuObj = new JSONObject();
			skuObj.put("salemenu_ids", orderObj.getJSONArray("salemenu_ids"));
			skuObj.put("sku_ids", orderObj.getJSONArray("sku_ids"));
			skuObj.put("station_id", orderObj.getString("station_id"));
			skuObj.put("spu_remark", new JSONObject());
			if (combineOrder) {
				if (orderObj.get("order_id") != null) {
					skuObj.put("order_id", orderObj.getString("order_id"));
				}
			}
			skuArray.add(skuObj);
		}

		url = BsURL.submit_order_url;
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("orders", skuArray.toString());

		retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		if (retObj.getInteger("code") != 0) {
			return null;
		}
		dataArray = retObj.getJSONArray("data");
		List<String> retArray = new ArrayList<>();
		for (int i = 0; i < dataArray.size(); i++) {
			JSONObject dataObj = dataArray.getJSONObject(i);
			if (dataObj.getInteger("code") == 0) {
				retArray.add(dataObj.getJSONObject("extender").getString("_id"));
			}
		}
		return retArray;
	}

	@Override
	public boolean getCart() throws Exception {
		retObj = baseRequest.baseRequest(BsURL.get_cart_url, RequestType.GET);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean favoriteList() throws Exception {
		retObj = baseRequest.baseRequest(BsURL.get_favorite_list_url, RequestType.GET);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean promotionList() throws Exception {
		retObj = baseRequest.baseRequest(BsURL.get_promotion_list_url, RequestType.GET);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean hotSearch() throws Exception {
		retObj = baseRequest.baseRequest(BsURL.hot_search, RequestType.GET);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean userAccount() throws Exception {
		retObj = baseRequest.baseRequest(BsURL.user_account_url, RequestType.GET);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean unpayOrderList() throws Exception {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("limit", "5");
		paramMap.put("offset", "0");
		paramMap.put("type", "0");

		retObj = baseRequest.baseRequest(BsURL.order_list_url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean payOrderList() throws Exception {

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("limit", "5");
		paramMap.put("offset", "0");
		paramMap.put("type", "1");

		retObj = baseRequest.baseRequest(BsURL.order_list_url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean orderCount() throws Exception {
		retObj = baseRequest.baseRequest(BsURL.order_count_url, RequestType.GET);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public OrderDetailBean orderDetail(String order_id) throws Exception {
		String url = BsURL.order_detail_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);

		retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OrderDetailBean.class)
				: null;
	}

	@Override
	public boolean addFavoriteSpu(JSONArray list_ids, String spu_id) throws Exception {
		String url = BsURL.update_sku_to_favorite_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("list_ids", String.valueOf(list_ids));
		paramMap.put("spu_id", spu_id);

		retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public String getRandomSpu() throws Exception {
		retObj = baseRequest.baseRequest(BsURL.get_category_url, RequestType.GET);

		Random random = new Random();
		dataArray = retObj.getJSONArray("data");
		JSONObject categoryObj = dataArray.getJSONObject(random.nextInt(dataArray.size()));

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("level", "1");
		paramMap.put("category_id", categoryObj.getString("id"));

		retObj = baseRequest.baseRequest(BsURL.get_sku_url, RequestType.GET, paramMap);

		dataArray = retObj.getJSONArray("data");
		if (dataArray.size() == 0) {
			return null;
		}

		JSONObject spuObj = dataArray.getJSONObject(random.nextInt(dataArray.size()));

		return spuObj.getString("id");
	}

	@Override
	public boolean editUserInfo() throws Exception {
		retObj = baseRequest.baseRequest(BsURL.user_account_url, RequestType.GET);

		JSONObject userObj = retObj.getJSONObject("data").getJSONArray("addresses").getJSONObject(0);

		String id = String.valueOf(userObj.get("id"));
		String telephone = userObj.getString("telephone");
		String resname = userObj.getString("resname");
		String name = userObj.getString("name");

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("address_id", id);
		paramMap.put("telephone", telephone);
		paramMap.put("resname", resname);
		paramMap.put("name", name);

		retObj = baseRequest.baseRequest(BsURL.edit_user_address_url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean addSubaccount() throws Exception {
		retObj = baseRequest.baseRequest(BsURL.user_account_url, RequestType.GET);

		JSONObject userObj = retObj.getJSONObject("data").getJSONArray("addresses").getJSONObject(0);

		// 添加子账号
		String id = String.valueOf(userObj.get("id"));
		JSONArray idArray = new JSONArray();
		idArray.add(id);
		String usrname = "129" + StringUtil.getRandomNumber(8);

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("username", usrname);
		paramMap.put("sub_permissions", "5");
		paramMap.put("addresses", idArray.toString());

		retObj = baseRequest.baseRequest(BsURL.add_subaccount_url, RequestType.POST, paramMap);

		logger.info("添加子账号返回: " + retObj.toString());
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteSubaccount() throws Exception {
		retObj = baseRequest.baseRequest(BsURL.add_subaccount_url, RequestType.GET);

		dataArray = retObj.getJSONArray("data");

		String id = dataArray.getJSONObject(0).get("id").toString();

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("sub_id", id);
		paramMap.put("delete", "1");

		retObj = baseRequest.baseRequest(BsURL.add_subaccount_url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public int changePassword() throws Exception {
		String password = ConfigureUtil.getValueByKey("bshopPwd");

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("old_pwd", password);
		paramMap.put("new_pwd", password);

		retObj = baseRequest.baseRequest(BsURL.add_subaccount_url, RequestType.POST, paramMap);

		logger.info("修改密码返回信息: " + retObj);
		return retObj.getInteger("code");
	}

	@Override
	public boolean getHomepageCustomized() throws Exception {
		String url = BsURL.homepage_customized_url;

		retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean getPromotionSku() throws Exception {
		String url = BsURL.promotion_sku_url;

		retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<CouponBean> getAvailCouponList() throws Exception {
		String url = BsURL.avail_coupon_list_url;

		retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), CouponBean.class)
				: null;
	}

	@Override
	public List<CouponBean> getVisibleCouponList() throws Exception {
		String url = BsURL.visible_coupon_list_url;

		retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), CouponBean.class)
				: null;
	}

}
