package cn.guanmai.station.impl.invoicing;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.invoicing.RefundStockRecordBean;
import cn.guanmai.station.bean.invoicing.SplitStockInRecordBean;
import cn.guanmai.station.bean.invoicing.SplitStockOutRecordBean;
import cn.guanmai.station.bean.invoicing.StockAbandonGoodsRecordBean;
import cn.guanmai.station.bean.invoicing.InStockRecordBean;
import cn.guanmai.station.bean.invoicing.StockIncreaseRecordBean;
import cn.guanmai.station.bean.invoicing.StockLossRecordBean;
import cn.guanmai.station.bean.invoicing.OutStockRecordBean;
import cn.guanmai.station.bean.invoicing.ReturnStockRecordBean;
import cn.guanmai.station.bean.invoicing.param.InStockRecordFilterParam;
import cn.guanmai.station.bean.invoicing.param.OutStockRecordFilterParam;
import cn.guanmai.station.bean.invoicing.param.StockRecordFilterParam;
import cn.guanmai.station.interfaces.invoicing.StockRecordService;
import cn.guanmai.station.url.InvoicingURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Jan 8, 2019 7:55:20 PM 
* @des 库存记录实现类
* @version 1.0 
*/
public class StockRecordServiceImpl implements StockRecordService {
	private String url;
	private BaseRequest baseRequest;

	public StockRecordServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<RefundStockRecordBean> refundRecords(StockRecordFilterParam paramBean) throws Exception {
		url = InvoicingURL.refund_stock_record_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramBean);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), RefundStockRecordBean.class)
				: null;
	}

	@Override
	public List<StockAbandonGoodsRecordBean> abandonGoodsRecords(StockRecordFilterParam stockRecordFilterParam)
			throws Exception {
		url = InvoicingURL.abandon_goods_record_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, stockRecordFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), StockAbandonGoodsRecordBean.class)
				: null;
	}

	@Override
	public List<InStockRecordBean> inStockRecords(InStockRecordFilterParam stockInRecordFilterParam) throws Exception {
		url = InvoicingURL.in_stock_record_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, stockInRecordFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), InStockRecordBean.class)
				: null;
	}

	@Override
	public List<OutStockRecordBean> outStockRecords(OutStockRecordFilterParam paramBean) throws Exception {
		url = InvoicingURL.out_stock_record_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramBean);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OutStockRecordBean.class)
				: null;
	}

	@Override
	public List<ReturnStockRecordBean> returnStockRecords(StockRecordFilterParam paramBean) throws Exception {
		url = InvoicingURL.return_stock_record_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramBean);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), ReturnStockRecordBean.class)
				: null;
	}

	@Override
	public List<StockIncreaseRecordBean> increaseStockRecords(StockRecordFilterParam paramBean) throws Exception {
		url = InvoicingURL.increase_stock_record_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramBean);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), StockIncreaseRecordBean.class)
				: null;
	}

	@Override
	public List<StockLossRecordBean> lossStockRecords(StockRecordFilterParam stockRecordFilterParam) throws Exception {
		url = InvoicingURL.loss_stock_record_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, stockRecordFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), StockLossRecordBean.class)
				: null;
	}

	@Override
	public List<SplitStockInRecordBean> splitStockInRecords(StockRecordFilterParam stockRecordFilterParam)
			throws Exception {
		String url = InvoicingURL.split_in_stock_record_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, stockRecordFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SplitStockInRecordBean.class)
				: null;
	}

	@Override
	public List<SplitStockOutRecordBean> splitStockOutRecords(StockRecordFilterParam stockRecordFilterParam)
			throws Exception {
		String url = InvoicingURL.split_out_stock_record_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, stockRecordFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SplitStockOutRecordBean.class)
				: null;
	}

}
