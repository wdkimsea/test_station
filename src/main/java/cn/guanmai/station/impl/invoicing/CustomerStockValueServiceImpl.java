package cn.guanmai.station.impl.invoicing;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.invoicing.param.CustomerSpuStockLogFilterParam;
import cn.guanmai.station.bean.invoicing.CustomerStockCountBean;
import cn.guanmai.station.bean.invoicing.CustomerSpuStockLogBean;
import cn.guanmai.station.bean.invoicing.CustomerSpuStockBean;
import cn.guanmai.station.interfaces.invoicing.CustomerStockValueService;
import cn.guanmai.station.url.InvoicingURL;
import cn.guanmai.util.JsonUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by yangjinhai on 2019/8/22.
 */
public class CustomerStockValueServiceImpl implements CustomerStockValueService {

	private BaseRequest baseRequest;

	public CustomerStockValueServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public CustomerStockCountBean getCustomerStockValueCount(String search) throws Exception {
		String urlStr = InvoicingURL.customer_stock_value_count_url;

		Map<String, String> params = new HashMap<>();
		params.put("search", search);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, params);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), CustomerStockCountBean.class)
				: null;
	}

	@Override
	public List<CustomerSpuStockBean> getCustomerSpuStockList(String address_id) throws Exception {
		String urlStr = InvoicingURL.customer_spu_stock_list_url;
		Map<String, String> params = new HashMap<>();
		params.put("address_id", address_id);
		params.put("limit", "20");
		params.put("offset", "0");

		List<CustomerSpuStockBean> customerSpuStockList = new ArrayList<CustomerSpuStockBean>();

		boolean more = true;
		while (more) {
			JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, params);
			if (retObj.getInteger("code") == 0) {
				customerSpuStockList.addAll(
						JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), CustomerSpuStockBean.class));
				more = retObj.getJSONObject("pagination").getBoolean("more");
				if (more) {
					params.put("page_obj", retObj.getJSONObject("pagination").getJSONArray("page_obj").toString());
				}
			} else {
				customerSpuStockList = null;
				more = false;
			}
		}
		return customerSpuStockList;
	}

	@Override
	public List<CustomerSpuStockLogBean> searchCustomerSpuStockLog(CustomerSpuStockLogFilterParam filterParam)
			throws Exception {
		String url = InvoicingURL.customer_spu_stock_log_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), CustomerSpuStockLogBean.class)
				: null;
	}

	@Override
	public String exportCustomerSpuStocks(String search) throws Exception {
		String url = InvoicingURL.customer_stock_value_list_url;

		Map<String, String> params = new HashMap<>();
		params.put("search", search);
		params.put("export", "1");

		String file_path = baseRequest.baseExport(url, RequestType.GET, params, "temp.xlsx");
		return file_path;
	}

	@Override
	public String exportCustomerSpuStockLog(CustomerSpuStockLogFilterParam filterParam) throws Exception {
		String url = InvoicingURL.customer_spu_stock_log_list_url;

		String file_path = baseRequest.baseExport(url, RequestType.GET, filterParam, "temp.xlsx");

		return file_path;
	}
}
