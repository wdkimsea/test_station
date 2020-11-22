package cn.guanmai.open.impl.stock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.open.bean.stock.OpenSupplierBean;
import cn.guanmai.open.bean.stock.OpenSupplierDetailBean;
import cn.guanmai.open.bean.stock.param.OpenSupplierCommonParam;
import cn.guanmai.open.bean.stock.param.OpenSupplierFilterParam;
import cn.guanmai.open.interfaces.stock.OpenSupplierService;
import cn.guanmai.open.url.OpenStockURL;
import cn.guanmai.request.impl.OpenRequestImpl;
import cn.guanmai.request.intefaces.OpenRequest;
import cn.guanmai.util.JsonUtil;

/**
 * @author liming
 * @date 2019年10月21日
 * @time 下午3:44:22
 * @des TODO
 */

public class OpenSupplierServiceImpl implements OpenSupplierService {
	private OpenRequest openRequest;

	public OpenSupplierServiceImpl(String access_token) {
		openRequest = new OpenRequestImpl(access_token);
	}

	@Override
	public List<OpenSupplierBean> querySupplier(OpenSupplierFilterParam filterParam) throws Exception {
		String url = OpenStockURL.query_supplier_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.GET, filterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenSupplierBean.class)
				: null;
	}

	@Override
	public OpenSupplierDetailBean getSupplierDetail(String supplier_id) throws Exception {
		String url = OpenStockURL.get_supplier_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("supplier_id", supplier_id);

		JSONObject retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OpenSupplierDetailBean.class)
				: null;
	}

	@Override
	public String createSupplier(OpenSupplierCommonParam openSupplierCreateParam) throws Exception {
		String url = OpenStockURL.create_supplier_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, openSupplierCreateParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("supplier_id") : null;
	}

	@Override
	public boolean updateSupplier(OpenSupplierCommonParam openSupplierUpdateParam) throws Exception {
		String url = OpenStockURL.update_supplier_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, openSupplierUpdateParam);

		return retObj.getInteger("code") == 0;
	}
}
