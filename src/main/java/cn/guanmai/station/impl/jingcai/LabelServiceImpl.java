package cn.guanmai.station.impl.jingcai;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.jingcai.LabelBean;
import cn.guanmai.station.interfaces.jingcai.LabelService;
import cn.guanmai.station.url.JingcaiURL;
import cn.guanmai.util.JsonUtil;

/**
 * @author: liming
 * @Date: 2020年4月28日 上午10:43:46
 * @description:
 * @version: 1.0
 */

public class LabelServiceImpl implements LabelService {
	private BaseRequest baseRequest;

	public LabelServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public BigDecimal createLabel(String name) throws Exception {
		String url = JingcaiURL.label_create_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("name", name);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0 ? new BigDecimal(retObj.getJSONObject("data").getString("id")) : null;
	}

	@Override
	public boolean deleteLabel(BigDecimal id) throws Exception {
		String url = JingcaiURL.label_delete_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", String.valueOf(id));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<LabelBean> searchLabel(String q) throws Exception {
		String url = JingcaiURL.label_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("q", q);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), LabelBean.class)
				: null;
	}

	@Override
	public LabelBean getLabelByName(String name) throws Exception {
		String url = JingcaiURL.label_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("q", name);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		LabelBean label = null;
		if (retObj.getInteger("code") == 0) {
			List<LabelBean> labels = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), LabelBean.class);
			label = labels.stream().filter(l -> l.getName().equals(name)).findAny().orElse(null);

		} else {
			throw new Exception("搜索过滤商品加工标签失败");
		}
		return label;

	}

	@Override
	public LabelBean getLabelByID(BigDecimal id) throws Exception {
		String url = JingcaiURL.label_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, new HashMap<String, String>());

		LabelBean label = null;
		if (retObj.getInteger("code") == 0) {
			List<LabelBean> labels = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), LabelBean.class);
			label = labels.stream().filter(l -> l.getId().compareTo(id) == 0).findAny().orElse(null);

		} else {
			throw new Exception("搜索过滤商品加工标签失败");
		}
		return label;
	}

	@Override
	public BigDecimal initLabel() throws Exception {
		String url = JingcaiURL.label_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("q", "净菜");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		BigDecimal label_id = null;
		if (retObj.getInteger("code") == 0) {
			List<LabelBean> labels = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), LabelBean.class);
			LabelBean label = labels.stream().filter(l -> l.getName().equals("净菜")).findAny().orElse(null);

			if (label == null) {
				url = JingcaiURL.label_create_url;

				paramMap = new HashMap<String, String>();
				paramMap.put("name", "净菜");

				retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

				if (retObj.getInteger("code") == 0) {
					retObj.getJSONObject("data").getString("id");
				} else {
					throw new Exception("新建商品加工标签失败");
				}
			} else {
				label_id = label.getId();
			}
		} else {
			throw new Exception("搜索过滤商品加工标签失败");
		}
		return label_id;
	}

}
