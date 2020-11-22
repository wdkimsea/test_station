package cn.guanmai.open.impl.stock;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.open.bean.stock.OpenStockSaleRefundBean;
import cn.guanmai.open.bean.stock.param.OpenStockSaleRefundFilterParam;
import cn.guanmai.open.interfaces.stock.OpenStockSaleRefundService;
import cn.guanmai.open.url.OpenStockURL;
import cn.guanmai.request.impl.OpenRequestImpl;
import cn.guanmai.request.intefaces.OpenRequest;
import cn.guanmai.util.JsonUtil;

/**
 * @author liming
 * @date 2019年12月18日
 * @time 上午11:25:27
 * @des TODO
 */

public class OpenStockSaleRefundServiceImpl implements OpenStockSaleRefundService {
	private JSONObject retObj;
	private OpenRequest openRequest;

	public OpenStockSaleRefundServiceImpl(String access_token) {
		openRequest = new OpenRequestImpl(access_token);
	}

	@Override
	public List<OpenStockSaleRefundBean> filterStockSaleRefund(OpenStockSaleRefundFilterParam filterParam)
			throws Exception {
		String url = OpenStockURL.stock_sale_refund_list_url;

		retObj = openRequest.baseRequest(url, RequestType.GET, filterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenStockSaleRefundBean.class)
				: null;
	}

}
