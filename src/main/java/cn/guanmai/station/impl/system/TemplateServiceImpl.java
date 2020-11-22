package cn.guanmai.station.impl.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.system.BoxTemplateBean;
import cn.guanmai.station.bean.system.DistributeTemplateBean;
import cn.guanmai.station.bean.system.OrderImportTemlateBean;
import cn.guanmai.station.bean.system.PrintTagTemplateBean;
import cn.guanmai.station.bean.system.PurchaseTemplateBean;
import cn.guanmai.station.bean.system.SettleTemplateBean;
import cn.guanmai.station.bean.system.StockInTemplateBean;
import cn.guanmai.station.bean.system.StockOutTemplateBean;
import cn.guanmai.station.bean.system.param.OrderImportTemplateParam;
import cn.guanmai.station.interfaces.system.TemplateService;
import cn.guanmai.station.url.SystemURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Jun 20, 2019 7:31:17 PM 
* @des 打印模板相关业务实现
* @version 1.0 
*/
public class TemplateServiceImpl implements TemplateService {
	private BaseRequest baseRequest;

	public TemplateServiceImpl(Map<String, String> headers) {
		headers.put("X-Guanmai-Client", "GmStation/1.0.0");
		baseRequest = new BaseRequestImpl(headers);

	}

	@Override
	public List<PurchaseTemplateBean> getPurchaseTemplateList() throws Exception {
		String url = SystemURL.purchase_template_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, new HashMap<>());
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), PurchaseTemplateBean.class)
				: null;
	}

	@Override
	public List<StockInTemplateBean> getStockInTemplateList() throws Exception {
		String url = SystemURL.stock_in_template_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, new HashMap<String, String>());

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), StockInTemplateBean.class)
				: null;
	}

	@Override
	public List<StockOutTemplateBean> getStockOutTemplateList() throws Exception {
		String url = SystemURL.stock_out_template_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, new HashMap<String, String>());

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), StockOutTemplateBean.class)
				: null;
	}

	@Override
	public List<PrintTagTemplateBean> getPrintTagTemplateList() throws Exception {
		String url = SystemURL.print_tag_template_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassList(
				retObj.getJSONObject("data").getJSONArray("result_list").toString(), PrintTagTemplateBean.class) : null;
	}

	@Override
	public List<BoxTemplateBean> getBoxTemplateList() throws Exception {
		String url = SystemURL.box_template_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), BoxTemplateBean.class)
				: null;
	}

	@Override
	public PurchaseTemplateBean.Content getPurchaseTemplateDetail(String id) throws Exception {
		String url = SystemURL.purchase_template_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassObject(retObj.getJSONObject("data").toJSONString(),
				PurchaseTemplateBean.Content.class) : null;
	}

	@Override
	public StockInTemplateBean.Content getStockInTemplateDetail(String id) throws Exception {
		String url = SystemURL.stock_in_template_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), StockInTemplateBean.Content.class)
				: null;
	}

	@Override
	public StockOutTemplateBean.Content getStockOutTemplateDetail(String id) throws Exception {
		String url = SystemURL.stock_out_template_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), StockOutTemplateBean.Content.class)
				: null;
	}

	@Override
	public PrintTagTemplateBean.Content getPrintTagTemplateDetail(String id) throws Exception {
		String url = SystemURL.print_tag_template_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").getJSONObject("content").toString(),
						PrintTagTemplateBean.Content.class)
				: null;
	}

	@Override
	public BoxTemplateBean getBoxTemplateDetail(String id) throws Exception {
		String url = SystemURL.box_tempalte_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), BoxTemplateBean.class)
				: null;
	}

	@Override
	public String createOrderImportTemplate(OrderImportTemplateParam orderImportTemplateParam) throws Exception {
		String url = SystemURL.order_import_template_create_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, orderImportTemplateParam);

		return retObj.getInteger("code") == 0 ? retObj.getString("data") : null;
	}

	@Override
	public List<OrderImportTemlateBean> getOrderImportTemlateList() throws Exception {
		String url = SystemURL.order_import_template_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OrderImportTemlateBean.class)
				: null;
	}

	@Override
	public OrderImportTemlateBean getOrderImportTemlateDetail(String id) throws Exception {
		String url = SystemURL.order_import_template_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OrderImportTemlateBean.class)
				: null;
	}

	@Override
	public List<DistributeTemplateBean> getDistributeTemplateList() throws Exception {
		String url = SystemURL.distribute_template_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), DistributeTemplateBean.class)
				: null;
	}

	@Override
	public DistributeTemplateBean getDistributeTemplateDetailInfo(String id) throws Exception {
		String url = SystemURL.distribute_template_detail_info_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), DistributeTemplateBean.class)
				: null;
	}

	@Override
	public List<SettleTemplateBean> getSettleTemplateList() throws Exception {
		String url = SystemURL.settle_template_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SettleTemplateBean.class)
				: null;
	}

	@Override
	public SettleTemplateBean.Content getSettleTemplateDetailInfo(String id) throws Exception {
		String url = SystemURL.settle_template_detail_info_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), SettleTemplateBean.Content.class)
				: null;
	}

}
