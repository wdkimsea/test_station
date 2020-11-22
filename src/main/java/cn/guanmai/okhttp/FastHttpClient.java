package cn.guanmai.okhttp;

import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.guanmai.okhttp.ssl.X509TrustManagerImpl;
import okhttp3.OkHttpClient;

/**
 * 
 * @author icecooly
 *
 */
public class FastHttpClient {
	//
	public static Logger logger = LoggerFactory.getLogger(FastHttpClient.class);
	//
	public static final String VERSION = "1.5";
	//
	private static HttpClient httpClient = new HttpClient(getDefaultOkHttpClient());

	//
	private static OkHttpClient getDefaultOkHttpClient() {
		OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
		//
		final X509TrustManager trustManager = new X509TrustManagerImpl();
		SSLSocketFactory sslSocketFactory = null;
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, new TrustManager[] { trustManager }, new SecureRandom());
			sslSocketFactory = sslContext.getSocketFactory();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return builder.sslSocketFactory(sslSocketFactory, trustManager).hostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		}).build();
	}

	//
	public static FastHttpClientBuilder newBuilder() {
		return new FastHttpClientBuilder(httpClient.getOkHttpClient());
	}

	//
	public static FastHttpClientBuilder newBuilder(OkHttpClient client) {
		return new FastHttpClientBuilder(client);
	}

	//
	public static GetBuilder get() {
		return httpClient.get();
	}

	//
	public static PostBuilder post() {
		return httpClient.post();
	}

	//
	public static PutBuilder put() {
		return httpClient.put();
	}

	public static OkHttpRequestBuilder<?> requestType(RequestType type) {
		switch (type) {
		case GET:
			return httpClient.get();
		case POST:
			return httpClient.post();
		case PUT:
			return httpClient.put();
		default:
			return httpClient.get();
		}
	}
}
