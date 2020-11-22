package cn.guanmai.okhttp.callback;

import cn.guanmai.okhttp.Response;
import okhttp3.Call;

/**
 * 
 * @author icecooly
 */
public abstract class Callback{
	//
	public abstract void onFailure(Call call,Exception e,int id);
	//
	public abstract void onResponse(Call call,Response response, int id);
}