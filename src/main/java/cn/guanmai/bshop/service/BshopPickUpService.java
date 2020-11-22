package cn.guanmai.bshop.service;

import cn.guanmai.bshop.bean.BshopOptionalInfoBean;
import cn.guanmai.bshop.bean.BshopPickUpBean;

import java.util.List;

/**
 * Created by abc on 2019/8/28. 自提点相关接口类
 */
public interface BshopPickUpService {
	/**
	 * 获取商铺注册类型 1:店铺 2:个人 3:店铺+个人 店铺收货方式 1:配送 2:自提 3:配送+自提 司机是否展示, 1:展示 2:隐藏
	 */
	public BshopOptionalInfoBean getOptionInfo() throws Exception;

	/**
	 * 获取bshop自提点信息
	 * 
	 * @throws Exception
	 */
	public List<BshopPickUpBean> getPickUps() throws Exception;

}
