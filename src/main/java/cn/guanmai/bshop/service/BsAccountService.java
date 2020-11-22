package cn.guanmai.bshop.service;

import java.util.List;

import cn.guanmai.bshop.bean.account.BsAccountBean;
import cn.guanmai.bshop.bean.account.BsRegisterAreaBean;
import cn.guanmai.bshop.bean.account.param.BsAddressParam;
import cn.guanmai.bshop.bean.account.param.BsRegisterParam;

/**
 * @author: liming
 * @Date: 2020年6月15日 下午3:11:14
 * @description:
 * @version: 1.0
 */

public interface BsAccountService {
	/**
	 * 获取BSHOP登录账号信息
	 *
	 * @return
	 * @throws Exception
	 */
	public BsAccountBean getAccountInfo() throws Exception;

	/**
	 * 设置选择的店铺
	 * 
	 * @param address_id
	 * @return
	 * @throws Exception
	 */
	public boolean setAddress(String address_id) throws Exception;

	/**
	 * 新商户注册
	 * 
	 * @param registerParam
	 * @return
	 * @throws Exception
	 */
	public boolean register(BsRegisterParam registerParam) throws Exception;

	/**
	 * 获取注册所在地区
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<BsRegisterAreaBean> getRegisterArea() throws Exception;

	/**
	 * 添加店铺
	 * @return
	 * @throws Exception
	 */
	public boolean addAddress(BsAddressParam addressAddParam) throws Exception;

}
