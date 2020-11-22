package cn.guanmai.bshop.service;

import cn.guanmai.bshop.bean.*;
import cn.guanmai.bshop.bean.account.BsAccountBean;
import cn.guanmai.bshop.bean.order.PayMethod;
import cn.guanmai.bshop.bean.product.BsProductBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

/* 
* @author liming 
* @date Jan 12, 2019 10:42:54 AM 
* @des bshop相关接口
* @version 1.0 
*/
public interface BshopService {

	/**
	 * 获取登录账号信息
	 *
	 * @return
	 * @throws Exception
	 */
	public BsAccountBean getAccountInfo() throws Exception;

	/**
	 * 选择店铺
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean setAddress(SetAddressBean setAddressBean, String stationId) throws Exception;

	/**
	 * 选择店铺
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean setAddress(String address_id) throws Exception;

	/**
	 * 搜索销售商品
	 *
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public List<BsProductBean> searchSaleProducts(String text) throws Exception;

	/**
	 * 更新购物车
	 *
	 * @param skuMap
	 * @return
	 * @throws Exception
	 */
	public CartUpdateResult updateCart(Map<String, BigDecimal> skuMap) throws Exception;

	/**
	 * 获取报价单列表
	 *
	 * @return
	 * @throws Exception
	 */
	public JSONArray getSalemenuArray() throws Exception;

	/**
	 * 判断此时报价单中的商品是否可以正常购买
	 *
	 * @return
	 * @throws Exception
	 */
	public Map<String, Boolean> salemenuStatus() throws Exception;

	/**
	 * 设置收货时间
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean setReceiveTime() throws Exception;

	/**
	 * 
	 * @param address_id
	 * @param receive_way
	 * @param pick_up_st_id 选传参数,当 receive_way=2时必须传
	 * @return
	 * @throws Exception
	 */
	public boolean setReceiveAddress(String address_id, int receive_way, String pick_up_st_id) throws Exception;

	/**
	 * 设置付款方式
	 *
	 * @param paymethod
	 * @return
	 * @throws Exception
	 */
	public boolean setPaymethod(PayMethod paymethod) throws Exception;

	/**
	 * 提交购物车
	 *
	 * @param combineOrder
	 * @return
	 * @throws Exception
	 */
	public List<String> submitCart(boolean combineOrder) throws Exception;

	/**
	 * 获取购物车
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean getCart() throws Exception;

	/**
	 * 自定义设置
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean favoriteList() throws Exception;

	/**
	 * 营销活动列表
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean promotionList() throws Exception;

	/**
	 * 热门商品搜索
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean hotSearch() throws Exception;

	/**
	 * 用户信息
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean userAccount() throws Exception;

	/**
	 * 未支付订单列表接口
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean unpayOrderList() throws Exception;

	/**
	 * 已支付订单列表接口
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean payOrderList() throws Exception;

	/**
	 * 订单总数接口
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean orderCount() throws Exception;

	/**
	 * 订单详细接口
	 *
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	public OrderDetailBean orderDetail(String order_id) throws Exception;

	/**
	 * 商品加入常用列表
	 *
	 * @param list_ids
	 * @param spu_id
	 * @return
	 */
	public boolean addFavoriteSpu(JSONArray list_ids, String spu_id) throws Exception;

	/**
	 * 随机获取一个SPU
	 *
	 * @return
	 * @throws Exception
	 */
	public String getRandomSpu() throws Exception;

	/**
	 * 修改用户信息
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean editUserInfo() throws Exception;

	/**
	 * 添加子账号
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean addSubaccount() throws Exception;

	/**
	 * 删除子账号
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean deleteSubaccount() throws Exception;

	/**
	 * 修改密码
	 *
	 * @return
	 * @throws Exception
	 */
	public int changePassword() throws Exception;

	/**
	 * 获取首页定制
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean getHomepageCustomized() throws Exception;

	public boolean getPromotionSku() throws Exception;

	/**
	 * 获取已领优惠券列表
	 *
	 * @return
	 * @throws Exception
	 */
	public List<CouponBean> getAvailCouponList() throws Exception;

	/**
	 * 获取可见的优惠券列
	 *
	 * @return
	 * @throws Exception
	 */
	public List<CouponBean> getVisibleCouponList() throws Exception;
}
