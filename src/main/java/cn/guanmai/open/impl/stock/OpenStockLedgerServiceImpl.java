package cn.guanmai.open.impl.stock;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.open.bean.stock.OpenStockLedgerBean;
import cn.guanmai.open.bean.stock.OpenStockLedgerDetailBean;
import cn.guanmai.open.bean.stock.param.OpenStockLegerFilterParam;
import cn.guanmai.open.interfaces.stock.OpenStockLedgerService;
import cn.guanmai.open.url.OpenStockURL;
import cn.guanmai.request.impl.OpenRequestImpl;
import cn.guanmai.request.intefaces.OpenRequest;
import cn.guanmai.util.JsonUtil;


/**
 * @author liming
 * @date 2019年10月23日
 * @time 下午4:20:31
 * @des TODO
 */

public class OpenStockLedgerServiceImpl implements OpenStockLedgerService {
	private OpenRequest openRequest;

	public OpenStockLedgerServiceImpl(String access_token) {
		openRequest = new OpenRequestImpl(access_token);
	}

	@Override
	public OpenStockLedgerBean queryStockLeger(OpenStockLegerFilterParam stockLegerFilterParam) throws Exception {
		String url = OpenStockURL.query_stock_ledger_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.GET, stockLegerFilterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OpenStockLedgerBean.class)
				: null;
	}

	@Override
	public OpenStockLedgerDetailBean getStockLegerDetail(OpenStockLegerFilterParam stockLegerFilterParam)
			throws Exception {
		String url = OpenStockURL.get_stock_ledger_detail_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.GET, stockLegerFilterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OpenStockLedgerDetailBean.class)
				: null;
	}

}
