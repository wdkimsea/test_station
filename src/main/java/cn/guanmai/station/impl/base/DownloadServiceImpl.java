package cn.guanmai.station.impl.base;

import java.util.HashMap;
import java.util.Map;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.interfaces.base.DownloadService;

/**
 * @author liming
 * @date 2020年1月10日
 * @time 上午10:36:52
 * @des TODO
 */

public class DownloadServiceImpl implements DownloadService {
	private BaseRequest baseRequest;

	public DownloadServiceImpl(Map<String, String> headers) {
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public String downloadFile(String download_url) throws Exception {
		String file_path = baseRequest.baseExport(download_url, RequestType.GET, new HashMap<String, String>(),
				"temp.xlsx");
		return file_path;
	}

}
