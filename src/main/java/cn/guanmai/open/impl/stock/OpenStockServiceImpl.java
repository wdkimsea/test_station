package cn.guanmai.open.impl.stock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.open.bean.stock.OpenStockBean;
import cn.guanmai.open.bean.stock.param.OpenStockFilterParam;
import cn.guanmai.open.interfaces.stock.OpenStockService;
import cn.guanmai.open.url.OpenStockURL;
import cn.guanmai.request.impl.OpenRequestImpl;
import cn.guanmai.request.intefaces.OpenRequest;
import cn.guanmai.util.JsonUtil;

/**
 * @author liming
 * @date 2019年12月18日
 * @time 上午11:13:50
 * @des TODO
 */

public class OpenStockServiceImpl implements OpenStockService {
	private JSONObject retObj;
	private OpenRequest openRequest;

	public OpenStockServiceImpl(String access_token) {
		openRequest = new OpenRequestImpl(access_token);
	}

	@Override
	public List<OpenStockBean> queryStock(OpenStockFilterParam filterParam) throws Exception {
		String url = OpenStockURL.stock_list_url;

		retObj = openRequest.baseRequest(url, RequestType.GET, filterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenStockBean.class)
				: null;
	}

	@Override
	public boolean updateStock(String spu_id, String stock, String remark) throws Exception {
		String url = OpenStockURL.stock_update_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);
		paramMap.put("stock", stock);
		paramMap.put("remark", remark);

		retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

}
