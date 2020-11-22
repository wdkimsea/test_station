package cn.guanmai.bshop.impl;

import cn.guanmai.bshop.bean.BshopOptionalInfoBean;
import cn.guanmai.bshop.bean.BshopPickUpBean;
import cn.guanmai.bshop.service.BshopPickUpService;
import cn.guanmai.bshop.url.BsURL;
import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.util.JsonUtil;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by yangjinhai on 2019/8/28.
 *** 
 * @des 自提店相关实现类
 */
public class BshopPickUpServiceImpl implements BshopPickUpService {

	// private Logger logger =
	// LoggerFactory.getLogger(BshopPickUpStaionServerImpl.class);
	private BaseRequest baseRequest;

	public BshopPickUpServiceImpl(Map<String, String> cookie) {
		baseRequest = new BaseRequestImpl(cookie);
	}

	/**
	 * 获取商铺注册类型 1:店铺 2:个人 3:店铺+个人 店铺收货方式 1:配送 2:自提 3:配送+自提 司机是否展示, 1:展示 2:隐藏
	 */
	@Override
	public BshopOptionalInfoBean getOptionInfo() throws Exception {
		String urlStr = BsURL.get_optional_info;
		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), BshopOptionalInfoBean.class)
				: null;
	}

	/**
	 * 获取bshop自提点信息
	 *
	 * @throws Exception
	 */
	@Override
	public List<BshopPickUpBean> getPickUps() throws Exception {
		String urlStr = BsURL.get_pick_up_station_list;
		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), BshopPickUpBean.class)
				: null;
	}

}
