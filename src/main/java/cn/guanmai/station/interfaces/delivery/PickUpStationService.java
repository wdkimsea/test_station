package cn.guanmai.station.interfaces.delivery;

import cn.guanmai.station.bean.delivery.PickUpStationBean;
import cn.guanmai.station.bean.delivery.param.PickUpStationFilterParam;

import java.util.List;

/**
 * Created by yangjinhai on 2019/8/26.
 */
public interface PickUpStationService {

	/**
	 * 三个方法,创建的时候只传入一级城市名,或者二级城市名，或者地区名
	 * 
	 * @param pickUpStationBean
	 * @return
	 * @throws Exception
	 */
	public boolean createPickUpStation(PickUpStationBean pickUpStationBean) throws Exception;

	/**
	 * 查询创建的自提点
	 * 
	 * @param searchText
	 * @return
	 * @throws Exception
	 */
	public List<PickUpStationBean> queryPickUpStations(String business_status, String searchText) throws Exception;

	/**
	 * 根据地理位置以及自提点状态查询自提点
	 * 
	 * @param searchText
	 * @return
	 * @throws Exception
	 */
	public List<PickUpStationBean> queryPickUpStations(PickUpStationFilterParam pickUpStationFilterParam)
			throws Exception;

	/**
	 * 根据ID查询创建的自提点
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PickUpStationBean getPickUpStationDetailInfo(String id) throws Exception;

	/**
	 * 编辑自提点
	 * 
	 * @param pickUpStationBean 自提点信息
	 * @return
	 * @throws Exception
	 */
	public boolean updatePickUpStation(PickUpStationBean pickUpStationBean) throws Exception;

	/**
	 * 删除自提点
	 * 
	 * @param id 自提点Id
	 * @return
	 * @throws Exception
	 */
	public boolean deletePickUpStation(String id) throws Exception;
}
