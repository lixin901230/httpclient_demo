package httpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

	/** 
     * 发送GET请求 
     *  
     * @param url 
     *            目的地址 
     * @param parameters 
     *            请求参数，Map类型。 
     * @return 远程响应结果 
     */  
    public static String sendGet(String url, Map<String, String> parameters) {
    	
        String result="";
        BufferedReader in = null;// 读取响应输入流  
        StringBuffer sb = new StringBuffer();// 存储参数  
        String params = "";// 编码之后的参数
        try {
        	// 编码请求参数  
        	if(parameters != null && parameters.size() > 0) {
            	List<String> keys = new ArrayList<String>(parameters.keySet());
            	for (int i = 0; i < keys.size(); i++) {
            		sb.append(keys.get(i)).append("=").append(URLEncoder.encode(parameters.get(keys.get(i)), "UTF-8"));
            		if(i < keys.size() - 1) {
            			sb.append("&");
            		}
				}
                params = sb.toString();  
        	}
            String full_url = url + "?" + params; 
            System.out.println(full_url); 
            // 创建URL对象  
            URL connURL = new URL(full_url);  
            // 打开URL连接  
            HttpURLConnection httpConn = (HttpURLConnection) connURL.openConnection();  
            // 设置通用属性  
            httpConn.setRequestProperty("Accept", "*/*");  
            httpConn.setRequestProperty("Connection", "Keep-Alive");  
            httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");  
            // 建立实际的连接  
            httpConn.connect();
            // 响应头部获取  
            /*Map<String, List<String>> headers = httpConn.getHeaderFields();  
            // 遍历所有的响应头字段  
            for (String key : headers.keySet()) {  
                System.out.println(key + "\t：\t" + headers.get(key));  
            }*/
            // 定义BufferedReader输入流来读取URL的响应,并设置编码方式  
            in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));  
            String line;  
            // 读取返回的内容  
            while ((line = in.readLine()) != null) {  
                result += line;  
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {  
                if (in != null) {  
                    in.close();  
                }
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
        }
        return result ;
    }  
  
    /** 
     * 发送POST请求 
     *  
     * @param url 
     *            目的地址 
     * @param parameters 
     *            请求参数，Map类型。 
     * @return 远程响应结果 
     */  
    public static String sendPost(String url, Map<String, String> parameters) {  
        String result = "";// 返回的结果  
        BufferedReader in = null;// 读取响应输入流  
        PrintWriter out = null;  
        StringBuffer sb = new StringBuffer();// 处理请求参数  
        String params = "";// 编码之后的参数  
        try {
        	if(parameters != null && parameters.size() > 0) {
            	List<String> keys = new ArrayList<String>(parameters.keySet());
            	for (int i = 0; i < keys.size(); i++) {
            		sb.append(keys.get(i)).append("=").append(URLEncoder.encode(parameters.get(keys.get(i)), "UTF-8"));
            		if(i < keys.size() - 1) {
            			sb.append("&");
            		}
				}
                params = sb.toString();  
        	}

        	// 创建URL对象  
            URL connURL = new URL(url);
            // 打开URL连接  
            HttpURLConnection httpConn = (HttpURLConnection) connURL.openConnection();
           
            // 设置通用属性  
            httpConn.setRequestProperty("Accept", "*/*");  
            httpConn.setRequestProperty("Connection", "Keep-Alive");  
            httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");  
            // 设置POST方式  
            httpConn.setDoInput(true);  
            httpConn.setDoOutput(true);  
            // 获取HttpURLConnection对象对应的输出流  
            out = new PrintWriter(httpConn.getOutputStream());  
            // 发送请求参数  
            if(StringUtils.isNotBlank(params)) {
            	out.write(params);  
            }
            // flush输出流的缓冲  
            out.flush();  
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式  
            in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));  
            String line;  
            // 读取返回的内容  
            while ((line = in.readLine()) != null) {  
                result += line;  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (out != null) {  
                    out.close();  
                }  
                if (in != null) {  
                    in.close();  
                }  
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
        }  
        return result;  
    }
  
    
    /**
     * ################################################################################################
     */
    
    
    
    /** 
     * 发送 get请求 
     */  
    public String get(String url, Map<String, Object> params) {  
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        try {
        	URIBuilder uriBuilder = new URIBuilder();
        	uriBuilder.setPath(url);
        	
        	// 创建参数队列  
            List<NameValuePair> nameValuePairs = convertMapToNameValuePairs(params);
            uriBuilder.addParameters(nameValuePairs);
        	
            // 创建httpget.    
            HttpGet httpget = new HttpGet(uriBuilder.build());
            // 执行get请求.    
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {  
                // 获取响应实体    
                HttpEntity entity = response.getEntity();
                // 打印响应状态    
                System.out.println(response.getStatusLine());
                if (entity != null) {
                	String result = EntityUtils.toString(entity);
                	return result;
                }
            } finally {  
                response.close();  
            }  
        } catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }
        return "";
    }
   
    /** 
     * 发送 post请求访问本地应用并根据传递参数不同返回不同结果 
     * @return 
     */  
    public String post(String url, Map<String, Object> params) {  
        // 创建默认的httpClient实例.    
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        // 创建httppost    
        HttpPost httppost = new HttpPost(url);
        // 创建参数队列  
        List<NameValuePair> nameValuePairs = convertMapToNameValuePairs(params);
        try {  
        	UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");  
            httppost.setEntity(uefEntity);  
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {  
                HttpEntity entity = response.getEntity();
                // 打印响应状态    
                System.out.println(response.getStatusLine());
                if (entity != null) {
                	String result = EntityUtils.toString(entity, "UTF-8");
                	return result;
                }  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }
        return "";
    }
    
    /** 
     * HttpClient连接SSL 测试
     */  
    public void sslTest() {  
        CloseableHttpClient httpclient = null;  
        try {  
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());  
            FileInputStream instream = new FileInputStream(new File("d:\\tomcat.keystore"));  
            try {  
                // 加载keyStore d:\\tomcat.keystore    
                trustStore.load(instream, "123456".toCharArray());  
            } catch (CertificateException e) {  
                e.printStackTrace();  
            } finally {  
                try {  
                    instream.close();  
                } catch (Exception ignore) {  
                }  
            }  
            // 相信自己的CA和所有自签名的证书  
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();  
            // 只允许使用TLSv1协议  
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,  
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);  
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();  
            // 创建http请求(get方式)  
            HttpGet httpget = new HttpGet("https://localhost:8443/myDemo/Ajax/serivceJ.action");  
            System.out.println("executing request" + httpget.getRequestLine());  
            CloseableHttpResponse response = httpclient.execute(httpget);  
            try {  
                HttpEntity entity = response.getEntity();  
                System.out.println("----------------------------------------");  
                System.out.println(response.getStatusLine());  
                if (entity != null) {  
                    System.out.println("Response content length: " + entity.getContentLength());  
                    System.out.println(EntityUtils.toString(entity));  
                    EntityUtils.consume(entity);  
                }  
            } finally {  
                response.close();  
            }  
        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (KeyManagementException e) {  
            e.printStackTrace();  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (KeyStoreException e) {  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (httpclient != null) {  
                try {  
                    httpclient.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }
  
    /** 
     * 上传文件  测试
     */  
    public void uploadTest() {  
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        try {  
            HttpPost httppost = new HttpPost("http://localhost:8080/myDemo/Ajax/serivceFile.action");  
  
            FileBody bin = new FileBody(new File("F:\\image\\sendpix0.jpg"));  
            StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);  
  
            HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("bin", bin).addPart("comment", comment).build();  
  
            httppost.setEntity(reqEntity);  
  
            System.out.println("executing request " + httppost.getRequestLine());  
            CloseableHttpResponse response = httpclient.execute(httppost);  
            try {  
                System.out.println("----------------------------------------");  
                System.out.println(response.getStatusLine());  
                HttpEntity resEntity = response.getEntity();  
                if (resEntity != null) {  
                    System.out.println("Response content length: " + resEntity.getContentLength());  
                }  
                EntityUtils.consume(resEntity);  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }
    
    /**
     * 将map集合中的参数转换为BasicNameValuePair请求参数对集合
     * @param params
     * @return
     */
    private static List<NameValuePair> convertMapToNameValuePairs(Map<String, Object> params) {
    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	if(params != null && params.size() > 0) {
    		for (String key : params.keySet()) {
    			Object object = params.get(key);
    			nameValuePairs.add(new BasicNameValuePair(key, String.valueOf(object)));
    		}
    	}
    	return nameValuePairs;
    }
    
}
