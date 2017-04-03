package httpClient;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;

/**
 * httpclient工具类；基于commons-httpclient-3.0.1 版本jar包封装
 * whaty
 */
public class CommonHttpClientUtil{
	

	public HttpClient initHttpClient(){
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setParameter(HttpMethodParams.USER_AGENT,"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		return httpClient;
	}
	
	/**
	 * httpClient的GET请求
	 * 返回ResponseBody
	 * @param url
	 * @param paramsMap
	 * @return 
	 * @throws Exception
	 */
	public static String get(String url, Map<String,String> paramsMap)
    		throws Exception{
    	String result = null;
    	CommonHttpClientUtil commonHttpClient = new CommonHttpClientUtil();
        HttpClient httpClient = commonHttpClient.initHttpClient();
        GetMethod getMethod= commonHttpClient.getMethod(url, paramsMap);
        // 请求成功
        int status = httpClient.executeMethod(getMethod);
        if (status == 200){
        	result = getMethod.getResponseBodyAsString();
        } else {
        	throw new Exception("get请求失败，http请求状态吗："+status+"\n url："+url);
        }
    	return result;
    }
    
	/**
	 * httpClient的POST请求
	 * 返回ResponseBody
	 * @param url
	 * @param paramsMap
	 * @return
	 * @throws Exception
	 */
    public static String post(String url, Map<String,String> paramsMap)
    		throws Exception{
    	String result = null;
    	CommonHttpClientUtil commonHttpClient = new CommonHttpClientUtil();
        HttpClient httpClient = commonHttpClient.initHttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
		if(paramsMap != null && paramsMap.size() > 0) {
			NameValuePair[] params = new NameValuePair[paramsMap.keySet().size()];
			Iterator<String> it = paramsMap.keySet().iterator(); 
			int i = 0;
			String key = "";
			while(it.hasNext()){
				key = it.next();
				params[i] = new NameValuePair(key, paramsMap.get(key));
				i++;
			}
			postMethod.setQueryString(params);
		}
		postMethod.releaseConnection();
        // 请求成功
        int status = httpClient.executeMethod(postMethod);
        if (status == 200){
        	result = postMethod.getResponseBodyAsString();
        }
    	return result;
    }
    

	/**
	 * httpClient的GET请求
	 * 返回ResponseBody
	 * <h3>传递header参数</h3>
	 * @param url
	 * @param headerName	header参数key
	 * @param headerValue	header参数值
	 * @return 
	 * @throws Exception
	 */
	public static String get(String url, String headerName, String headerValue) throws Exception{
		String result = null;
		CommonHttpClientUtil commonHttpClient = new CommonHttpClientUtil();
		HttpClient httpClient = commonHttpClient.initHttpClient();
		GetMethod getMethod= commonHttpClient.getMethod(url, headerName, headerValue);
		// 请求成功
		int status = httpClient.executeMethod(getMethod);
		if (status == 200){
			result = getMethod.getResponseBodyAsString();
		}
		return result;
	}
	
	
	/**
	 * post请求方法
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public PostMethod postMethod(String url, Map<String, String> parameter) throws IOException {
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
		if(parameter != null && parameter.size() > 0) {
			NameValuePair[] params = new NameValuePair[parameter.keySet().size()];
			Iterator<String> it = parameter.keySet().iterator(); 
			int i = 0;
			String key = "";
			while(it.hasNext()){
				key = it.next();
				params[i] = new NameValuePair(key, parameter.get(key));
				i++;
			}
	//			{ new NameValuePair("eIdListString", parameter)};
			post.setRequestBody(params);
		}
		post.releaseConnection();

		return post;
	}

	/**
	 * post请求方法
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public PostMethod postMethodSimple(String url, Map<String, String> parameter) throws IOException {
		PostMethod postMethod = new PostMethod(url);
		postMethod.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
		if(parameter != null && parameter.size() > 0) {
			NameValuePair[] params = new NameValuePair[parameter.keySet().size()];
			Iterator<String> it = parameter.keySet().iterator(); 
			int i = 0;
			String key = "";
			while(it.hasNext()){
				key = it.next();
				params[i] = new NameValuePair(key, parameter.get(key));
				i++;
			}
			postMethod.setQueryString(params);
		}
		postMethod.releaseConnection();
		return postMethod;
	}
	
	/**
	 * GET请求方法
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public GetMethod getMethod(String url, Map<String, String> parameter)
			throws IOException {
		GetMethod get = new GetMethod(url);
		//get.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
		if (parameter != null) {
			NameValuePair[] params = new NameValuePair[parameter.keySet().size()];
			Iterator<String> it = parameter.keySet().iterator(); 
			int i = 0;
			String key = "";
			while(it.hasNext()){
				key = it.next();
				params[i] = new NameValuePair(key, parameter.get(key));
				i++;
			}
			get.setQueryString(params);
		}
		return get;
	}
	
	/**
	 * GET请求方法
	 * <h3>传递header参数</h3>
	 * @param url
	 * @param headerName	header参数key
	 * @param headerValue	header参数值
	 * @return
	 * @throws IOException
	 */
	public GetMethod getMethod(String url, String headerName, String headerValue)
			throws IOException {
		GetMethod get = new GetMethod(url);
		//get.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
		if (StringUtils.isNotBlank(headerName) && StringUtils.isNotBlank(headerValue)) {
			get.setRequestHeader(headerName, headerValue);
		}
		return get;
	}
	
	public String md5(String sessionKey ,String method, String now) throws Exception{
		String sign = "123456" + now + sessionKey + method;
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(sign.getBytes());
		byte b[] = md.digest();

		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}
}
