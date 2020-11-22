package cn.guanmai.station.interfaces.base;

/**
 * @author liming
 * @date 2020年1月10日
 * @time 上午10:35:12
 * @des TODO
 */

public interface DownloadService {
	/**
	 * 下载文件
	 * 
	 * @param download_url
	 * @return
	 * @throws Exception
	 */
	public String downloadFile(String download_url) throws Exception;
}
