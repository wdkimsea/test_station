package cn.guanmai.manage.impl.customermange;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.manage.bean.custommange.param.MgSalesReportDetailParam;
import cn.guanmai.manage.bean.custommange.param.MgSalesReportFiterParam;
import cn.guanmai.manage.bean.custommange.param.MgStationSalesReportDetailParam;
import cn.guanmai.manage.bean.custommange.param.MgStationSalesReportFilterParam;
import cn.guanmai.manage.bean.custommange.result.MgSalesReportBean;
import cn.guanmai.manage.bean.custommange.result.MgSalesReportDetailBean;
import cn.guanmai.manage.bean.custommange.result.MgStationSalesReportDetailBean;
import cn.guanmai.manage.interfaces.custommange.MgSalesReportService;
import cn.guanmai.manage.url.CustommanageURL;
import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.util.JsonUtil;

/**
 * @author: liming
 * @Date: 2020年7月30日 下午4:09:36
 * @description:
 * @version: 1.0
 */

public class MgSalesReportServiceImpl implements MgSalesReportService {
	private BaseRequest baseRequest;

	public MgSalesReportServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);

	}

	@Override
	public List<MgSalesReportBean> searchSalesReport(MgSalesReportFiterParam salesReportFiterParam) throws Exception {
		String url = CustommanageURL.sales_report_search_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, salesReportFiterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), MgSalesReportBean.class)
				: null;
	}

	@Override
	public boolean exportSalesReport(MgSalesReportFiterParam salesReportFiterParam) throws Exception {
		String url = CustommanageURL.sales_report_search_url;

		String filePath = baseRequest.baseExport(url, RequestType.GET, salesReportFiterParam, "temp.xlsx");

		return filePath != null;
	}

	@Override
	public MgSalesReportDetailBean getSalesReportDetail(MgSalesReportDetailParam mgSalesReportDetailParam)
			throws Exception {
		String url = CustommanageURL.sales_report_detail_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, mgSalesReportDetailParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), MgSalesReportDetailBean.class)
				: null;
	}

	@Override
	public boolean getSalesReportBaseInfo() throws Exception {
		String url = CustommanageURL.sales_report_baseinfo_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean searchStationSalesReport(MgStationSalesReportFilterParam mgStationSalesReportFilterParam)
			throws Exception {
		String url = CustommanageURL.sales_report_baseinfo_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, mgStationSalesReportFilterParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean exportStationSalesReport(MgStationSalesReportFilterParam mgStationSalesReportFilterParam)
			throws Exception {
		String url = CustommanageURL.sales_report_baseinfo_url;

		String filePath = baseRequest.baseExport(url, RequestType.GET, mgStationSalesReportFilterParam, "temp.xlsx");

		return filePath != null;
	}

	@Override
	public MgStationSalesReportDetailBean getMgStationSalesReportDetail(
			MgStationSalesReportDetailParam mgStationSalesReportDetailParam) throws Exception {
		String url = CustommanageURL.sales_report_staion_detail_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, mgStationSalesReportDetailParam);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(),
				MgStationSalesReportDetailBean.class) : null;
	}

}
