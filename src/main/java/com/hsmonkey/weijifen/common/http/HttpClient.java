package com.hsmonkey.weijifen.common.http;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: 驭宝</p>
 * <p>创建时间: 2015-5-30  上午1:20:40</p>
 * <p>作者：niepeng</p>
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParser;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.LineParser;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.lang.utils.EscapeUtil;

/**
 * 针对httpclient4.3的工具类，连接池解决并发问题，加入缓存技术
 */
public class HttpClient {

	protected final static Logger log = LoggerFactory.getLogger(HttpClient.class);

	private CloseableHttpClient httpClient;

	// 证书提供者SSL
	private static CredentialsProvider credentialsProvider;
	// cache配置
	private static CacheConfig cacheConfig;
	// 默认request配置
	private static RequestConfig defaultRequestConfig;
	// httpclient连接管理
	private static PoolingHttpClientConnectionManager connManager;
	// request配置
	private static RequestConfig requestConfig;
	// proxy request 配置
	private RequestConfig proxyRequestConfig;
	// cookie容器
	private final CookieStore cookieStore;
	// 连接超时时间
	private static final int TIMEOUT = 20000;
	// proxy是超时时间
	private static final int PROXY_TIMEOUT = 5000;
	// 每个路由最大连接数
	private static final int MAX_PER_ROUTE = 500;
	// httpclient最大连接数
	private static final int MAX_TOTAL = 10000;
	// 重发睡眠时间
	private static final long RESTART_TIME = 1500;
	// 淘宝搜索入口睡眠时间，（因为json数据的处理需要时间，线程优先级锁的等待时间，带session的client去采集，所以减少睡眠时间）
	private static final long TAOBAO_SEARCH_SLEEP = 500;
	// 天猫搜索入口睡眠时间，（因为线程优先级锁的等待时间，带session的client去采集，所以减少睡眠时间）
	private static final long TMALL_SEARCH_SLEEP = 2000;
	// 魔方采集睡眠时间
	private final static int MOFANG_SLEEP_TIME = 1000;
	// 默认访问淘宝睡眠时间
	private final static int DEFAULT_SLEEP_TIME = 100;

	private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
	private SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private boolean KeepSession;

	static {
		// response工厂
		HttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {
			@Override
			public HttpMessageParser<HttpResponse> create(SessionInputBuffer buffer, MessageConstraints constraints) {
				LineParser lineParser = new BasicLineParser() {
					@Override
					public Header parseHeader(final CharArrayBuffer buffer) {
						try {
							return super.parseHeader(buffer);
						} catch (ParseException ex) {
							return new BasicHeader(buffer.toString(), null);
						}
					}
				};
				return new DefaultHttpResponseParser(buffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints) {
					@Override
					protected boolean reject(final CharArrayBuffer line, int count) {
						return false;
					}
				};
			}
		};

		// request工厂
		HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();
		// 连接工厂
		HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(requestWriterFactory, responseParserFactory);

		cacheConfig = CacheConfig.custom().setMaxCacheEntries(1000).setMaxObjectSize(8192).build();

		SSLContext sslcontext = SSLContexts.createSystemDefault();

		X509HostnameVerifier hostnameVerifier = new BrowserCompatHostnameVerifier();

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create().register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", new SSLConnectionSocketFactory(sslcontext, hostnameVerifier)).build();

		DnsResolver dnsResolver = new SystemDefaultDnsResolver() {

			@Override
			public InetAddress[] resolve(final String host) throws UnknownHostException {
				if (host.equalsIgnoreCase("myhost")) {
					return new InetAddress[] { InetAddress.getByAddress(new byte[] { 127, 0, 0, 1 }) };
				} else {
					return super.resolve(host);
				}
			}

		};

		connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, connFactory, dnsResolver);

		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();

		connManager.setDefaultSocketConfig(socketConfig);

		MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200).setMaxLineLength(2000).build();

		ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE).setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
				.setMessageConstraints(messageConstraints).build();

		connManager.setDefaultConnectionConfig(connectionConfig);

		connManager.setMaxTotal(MAX_TOTAL);
		connManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);

		credentialsProvider = new BasicCredentialsProvider();

		defaultRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BEST_MATCH).setExpectContinueEnabled(true).setStaleConnectionCheckEnabled(true)
				.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST)).setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();

		requestConfig = RequestConfig.copy(defaultRequestConfig)
		// 必须开启circularRedirect，否则抛出CircularRedirect异常
				.setCircularRedirectsAllowed(true).setSocketTimeout(TIMEOUT).setConnectTimeout(TIMEOUT).setConnectionRequestTimeout(TIMEOUT).build();

	}

	/**
	 * @param f
	 */
	public HttpClient(boolean f) {
		this.KeepSession = f;
		this.cookieStore = new BasicCookieStore();

//		HttpClientBuilder builder = CachingHttpClients.custom().setCacheConfig(cacheConfig).setConnectionManager(connManager).setDefaultCookieStore(cookieStore)
//				.setDefaultCredentialsProvider(credentialsProvider).setDefaultRequestConfig(defaultRequestConfig);
		HttpClientBuilder builder = CachingHttpClients.custom();
		this.httpClient = builder.build();
	}
	
	public HttpClient(boolean f, File certFile, String certPsw) {
		SSLConnectionSocketFactory sslsf = null;
		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			FileInputStream instream = new FileInputStream(certFile);
			try {
				keyStore.load(instream, certPsw.toCharArray());
				SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, certPsw.toCharArray()).build();
				// Allow TLSv1 protocol only
				sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

			} catch(Exception e) {
				log.error("httpclientInitLoadCertFileError", e);
			}
			finally {
				instream.close();
			}
		} catch (Exception e) {
			log.error("HttpClientInitError", e);
		}

		this.KeepSession = f;
		this.cookieStore = new BasicCookieStore();

//		HttpClientBuilder builder = CachingHttpClients.custom().setCacheConfig(cacheConfig).setConnectionManager(connManager).setDefaultCookieStore(cookieStore)
//				.setDefaultCredentialsProvider(credentialsProvider).setDefaultRequestConfig(defaultRequestConfig);
		HttpClientBuilder builder = CachingHttpClients.custom();

		log.error("setSSLSocketFactoryStart");
		if (sslsf != null) {
			builder.setSSLSocketFactory(sslsf);
			log.error("setSSLSocketFactorySuccess");
		}

		this.httpClient = builder.build();
	}
	

	/**
	 * 判断是否为搜索入口，是就睡眠SEARCH_SLEEP
	 */
	private static Lock tb_ss_lock = new ReentrantLock();
	private static Lock tm_ss_lock = new ReentrantLock();

	/**
	 * 判断当前时间是否在凌晨至2点半之间，将暂停所有对数据魔方的请求
	 */
	private static Lock mf_lock = new ReentrantLock();

	/**
	 * 访问指定网址前的睡眠操作，以免出现限制访问现象
	 *
	 * @param url
	 */
	private void SleepBeforeAccess(String url) {
		// if (!KeepSession) {
		// cookieStore.clear();
		// }

		int hadSleeped = 0;

		if (url.indexOf(".taobao.com/search") != -1) {
			tb_ss_lock.lock();
			try {
				TimeUnit.MILLISECONDS.sleep(TAOBAO_SEARCH_SLEEP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				tb_ss_lock.unlock();
				hadSleeped = 1;
			}
		} else if (url.indexOf(".tmall.com/search") != -1) {
			tm_ss_lock.lock();
			try {
				TimeUnit.MILLISECONDS.sleep(TMALL_SEARCH_SLEEP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				tm_ss_lock.unlock();
				hadSleeped = 2;
			}
		} else if (url.indexOf("mofang.taobao.com/p/proxy/query") != -1) {
			mf_lock.lock();
			try {
				Date now = new Date();
				long sleeptime = (long) ((Math.random() + 1) * MOFANG_SLEEP_TIME);
				// 如果现在的时间小于凌晨2点半，就停止任务
				if (df.format(now).compareTo("02:30:00") < 0) {
					String n = df2.format(now);
					Date d = df3.parse(n + " 02:30:00");
					sleeptime = d.getTime() - now.getTime();
					System.out.println("MofangQuery-SLEEP:" + sleeptime);
					TimeUnit.MILLISECONDS.sleep(sleeptime);
				} else {
					System.out.println("MofangQuery-SLEEP:" + sleeptime);
					TimeUnit.MILLISECONDS.sleep(sleeptime);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				mf_lock.unlock();
				hadSleeped = 3;
			}
		}

		if (hadSleeped == 0) {
			try {
				TimeUnit.MILLISECONDS.sleep(DEFAULT_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public String subGet(String url, String refererUrl, String code) {
		return subGet(url, refererUrl, code, false);
	}
	
	public void subGetImage(String url, String refererUrl, String code, File file) {
		// lzsession解析问题
		url = url.replaceAll("%3d", "=");
		FileOutputStream fileOutPut = null;
		InputStream in = null;
		HttpGet httpget = new HttpGet(url);
		SleepBeforeAccess(url);
		httpget.setConfig(requestConfig);
		try {
			httpget.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + code);
			httpget.addHeader("User-Agent",
			// "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36");
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Dragon/36.1.1.3 Chrome/36.0.1985.97 Safari/537.36");
			httpget.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			httpget.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
			httpget.addHeader("Accept-Encoding", "gzip,deflate,sdch");
			httpget.addHeader("Referer", refererUrl == null ? url : refererUrl);
			CloseableHttpResponse response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			// 使用stream方式，避免java heap space overflow
			fileOutPut = new FileOutputStream(file);
			in = entity.getContent();
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = in.read(b)) != -1) {
				fileOutPut.write(b, 0, len);
			}
		} catch (ConnectTimeoutException e) {
			if (e.getMessage().indexOf("reset") != -1) {
				try {
					TimeUnit.MILLISECONDS.sleep(RESTART_TIME);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpget.releaseConnection();
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (fileOutPut != null) {
				try {
					fileOutPut.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	public void subGetFile(String url, String referUrl, String code, File file) {
		StringBuilder sb = new StringBuilder();
		// lzsession解析问题
		url = url.replaceAll("%3d", "=");
		HttpGet httpget = new HttpGet(url);
		SleepBeforeAccess(url);
		httpget.setConfig(requestConfig);
		try {
			httpget.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + code);
			httpget.addHeader("User-Agent",
			// "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36");
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Dragon/36.1.1.3 Chrome/36.0.1985.97 Safari/537.36");
			httpget.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			httpget.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
			httpget.addHeader("Accept-Encoding", "gzip,deflate,sdch");
			httpget.addHeader("Referer", referUrl == null ? url : referUrl);
			CloseableHttpResponse response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			 InputStream in = entity.getContent();
			// 使用stream方式，避免java heap space overflow
//			BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), code));
//			int c;
//			while ((c = br.read()) != -1) {
//				sb.append((char) c);
//			}
//			br.close();
			FileOutputStream fout = new FileOutputStream(file);  
            int l = -1;  
            byte[] tmp = new byte[1024];  
            while ((l = in.read(tmp)) != -1) {  
                fout.write(tmp, 0, l);  
                // 注意这里如果用OutputStream.write(buff)的话，图片会失真，大家可以试试  
            }  
            fout.flush();  
            fout.close();  
            
		} catch (ConnectTimeoutException e) {
			if (e.getMessage().indexOf("reset") != -1) {
				try {
					TimeUnit.MILLISECONDS.sleep(RESTART_TIME);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpget.releaseConnection();
		}
	
	}

	public String subGet(String url, String refererUrl, String code, boolean proxy) {
		String r = "";
		StringBuilder sb = new StringBuilder();
		// lzsession解析问题
		url = url.replaceAll("%3d", "=");
		HttpGet httpget = new HttpGet(url);
		if (proxy) {
			ProxyIp pi = ProxyUtil.getProxyIp();
			System.out.println("使用 " + pi.getIp() + ":" + pi.getPort());
			proxyRequestConfig = RequestConfig.copy(requestConfig).setSocketTimeout(PROXY_TIMEOUT).setConnectTimeout(PROXY_TIMEOUT).setConnectionRequestTimeout(PROXY_TIMEOUT)
					.setProxy(new HttpHost(pi.getIp(), pi.getPort())).build();
			httpget.setConfig(proxyRequestConfig);
		} else {
			SleepBeforeAccess(url);
			httpget.setConfig(requestConfig);
		}
		try {
			httpget.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + code);
			httpget.addHeader("User-Agent",
			// "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36");
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Dragon/36.1.1.3 Chrome/36.0.1985.97 Safari/537.36");
			httpget.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			httpget.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
			httpget.addHeader("Accept-Encoding", "gzip,deflate,sdch");
			httpget.addHeader("Referer", refererUrl == null ? url : refererUrl);
			CloseableHttpResponse response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			// 使用stream方式，避免java heap space overflow
			BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), code));
			int c;
			while ((c = br.read()) != -1) {
				sb.append((char) c);
			}
			br.close();
		} catch (ConnectTimeoutException e) {
			if (e.getMessage().indexOf("reset") != -1) {
				try {
					TimeUnit.MILLISECONDS.sleep(RESTART_TIME);
					r = subGet(url, code);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpget.releaseConnection();
			r = sb.toString();
		}
		return r;

	}

	public String subGet(String url, String code) {
		return subGet(url, code, false);
	}

	/**
	 * @desc 以get方式访问指定url
	 * @param httpClient
	 * @param url
	 * @return
	 */
	public String subGet(String url, String code, boolean proxy) {
		return subGet(url, null, code, proxy);
	}

	/**
	 * 加的这个方法，是能够请求到和浏览器差别不大的数据
	 */
	public static String getContextByURLConnection(String url_, String code) {
		StringBuilder cidContent = new StringBuilder(102400);
		BufferedReader mybr = null;
		URLConnection rulConnection = null;
		HttpURLConnection httpUrlConnection = null;
		try {
			URL url = new URL(url_);
			rulConnection = url.openConnection();
			httpUrlConnection = (HttpURLConnection) rulConnection;
			httpUrlConnection.setRequestMethod("GET");
			httpUrlConnection.connect();
			InputStream is = httpUrlConnection.getInputStream();
			mybr = new BufferedReader(new InputStreamReader(is, code));
			String line;
			while ((line = mybr.readLine()) != null) {
				cidContent.append(line);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				mybr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			httpUrlConnection.disconnect();
		}
		return cidContent.toString();
	}

	/**
	 * 以get方式发送无线端请求
	 */
	public String subGetPhone(String url, String code) {

		String r = "";
		StringBuilder sb = new StringBuilder();
		// lzsession解析问题
		url = url.replaceAll("%3d", "=");
		HttpGet httpget = new HttpGet(url);
		httpget.setConfig(requestConfig);
		try {
			httpget.addHeader("Content-Type", "application/json;charset=" + code);
			httpget.addHeader("User-Agent",
			// "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36");
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/3.5 Safari/536.11");
			httpget.addHeader("Accept", "*/*");
			httpget.addHeader("Referer", url);
			HttpClientContext context = HttpClientContext.create();
			CloseableHttpResponse response = httpClient.execute(httpget, context);
			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), code));
			int c;
			while ((c = br.read()) != -1) {
				sb.append((char) c);
			}
			br.close();
		} catch (SocketTimeoutException e) {
		} catch (ConnectException e) {
		} catch (SocketException e) {
			if (e.getMessage().indexOf("reset") != -1) {
				try {
					TimeUnit.MILLISECONDS.sleep(RESTART_TIME);
					r = subGetPhone(url, code);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpget.releaseConnection();
			r = sb.toString();
		}
		return r;
	}
	
	public String subPost(String url, String content, String code) {
		return subPostFrom(url, content, code, null);
	}
	
	public String subPostFrom(String url, String content, String code, Map<String, String> headerMap) {
		HttpPost httpPost = new HttpPost(url);
		try {
			StringEntity postEntity = new StringEntity(content, code);
			if(headerMap != null && headerMap.size() > 0) {
				for(Entry<String, String> entry : headerMap.entrySet()) {
					httpPost.addHeader(entry.getKey(), entry.getValue());
				}
			} else {
				httpPost.addHeader("Content-Type", "text/xml");
			}
			httpPost.setEntity(postEntity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, code);
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 * 只是给请求 api.eefield.com 的时候使用，因为返回结果不仅仅是json格式，可能还带有前面有数字和后面有数字的情况，
	 * 因为服务端api开发人员无法解决该问题，导致这里只能这么扯淡的解决该问题，请见谅
	 * 
	 * 例如返回结果可能是：
	 * 4d
	 * {"devGap": "60", "code": 0, "devName": "niepengonline", "area": "1号仓库"}
	 * 0
	 * 我们只是需要中间的json部分内容，所以需要做截取工作, 
	 * 
	 * 注意：同时也有可能是[xxxx] 这种类型的
	 * 
	 * @return
	 */
	public String subPostForOnlyOneClient(String url, String content, String code, Map<String, String> headerMap) {
		String resultValue = subPostFrom(url, content, code, headerMap);
		if (resultValue != null) {

			int length = resultValue.length();
			int start1 = resultValue.indexOf("{");
			int start2 = resultValue.indexOf("[");
			int end = -1;
			if ((start1 < start2 || start2 == -1) && start1 >= 0) {
				end = resultValue.lastIndexOf("}");
				if (length - 1 >= end) {
					end++;
				}
				return resultValue.substring(start1, end);
			}

			if ((start2 < start1 || start1 == -1) && start2 >= 0) {
				end = resultValue.lastIndexOf("]");
				if (length - 1 >= end) {
					end++;
				}
				return resultValue.substring(start2, end);
			}
		}
		return resultValue;
	}
	
	/**
	 * 以post方式访问指定url
	 *
	 * @param hm
	 * @param url
	 * @return
	 */
	public String subPost(HashMap hm, String url, String code) {

		String r = "";
		StringBuilder sb = new StringBuilder();
		// lzsession解析问题
		url = url.replaceAll("%3d", "=");
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + code);
			httpPost.addHeader("Referer", url);
			httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Dragon/36.1.1.3 Chrome/36.0.1985.97 Safari/537.36");
			httpPost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			Iterator iter = hm.entrySet().iterator();
			while (iter.hasNext()) {
				Entry entry = (Entry) iter.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				nvps.add(new BasicNameValuePair(key, value));
			}
			// post消息体加密方式
			UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(nvps, Charset.forName(code));
			httpPost.setEntity(postEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), code));
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
		} catch (SocketTimeoutException e) {
		} catch (ConnectException e) {
		} catch (SocketException e) {
			if (e.getMessage().indexOf("reset") != -1) {
				try {
					TimeUnit.MILLISECONDS.sleep(RESTART_TIME);
					r = subPost(hm, url, code);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpPost.releaseConnection();
			r = sb.toString();
		}
		return r;
	}
	/**
	 * @param hm
	 * @param url
	 * @return
	 */
	public String subPostMultipart(Map<String, Object> hm, String url, String code) {
		
		String r = "";
		StringBuilder sb = new StringBuilder();
		// lzsession解析问题
		url = url.replaceAll("%3d", "=");
		HttpPost httpPost = new HttpPost(url);
		try {
//			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + code);
			httpPost.addHeader("Referer", url);
			httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Dragon/36.1.1.3 Chrome/36.0.1985.97 Safari/537.36");
			httpPost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//			Iterator iter = hm.entrySet().iterator();
//			while (iter.hasNext()) {
//				Entry entry = (Entry) iter.next();
//				String key = (String) entry.getKey();
//				String value = (String) entry.getValue();
//				nvps.add(new BasicNameValuePair(key, value));
//			}
			// post消息体加密方式
//			UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(nvps, Charset.forName(code));
//			httpPost.setEntity(postEntity);
			
			MultipartEntityBuilder reqEntityBuilder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE).setCharset(Charset.forName(code));
			if (hm != null) {
				for (Entry<String, Object> enter : hm.entrySet()) {
					if (enter.getValue() instanceof File) {
						reqEntityBuilder.addPart(enter.getKey(), new FileBody((File) (enter.getValue())));
						continue;
					}
					if (enter.getValue() instanceof byte[]) {
						reqEntityBuilder.addBinaryBody(enter.getKey(), (byte[]) (enter.getValue()));
						continue;
					}
					reqEntityBuilder.addTextBody(enter.getKey(), String.valueOf(enter.getValue()), ContentType.DEFAULT_TEXT);
				}
			}

			httpPost.setEntity(reqEntityBuilder.build());
			CloseableHttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), code));
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
		} catch (SocketTimeoutException e) {
		} catch (ConnectException e) {
		} catch (SocketException e) {
			if (e.getMessage().indexOf("reset") != -1) {
				try {
					TimeUnit.MILLISECONDS.sleep(RESTART_TIME);
					r = subPostMultipart(hm, url, code);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpPost.releaseConnection();
			r = sb.toString();
		}
		return EscapeUtil.unescapeHtml(r);
	}

	/**
	 * 得到cookie
	 *
	 * @param name
	 * @return
	 */
	public String getCookie(String name) {
		String value = "";
		List<Cookie> cookies = cookieStore.getCookies();
		for (Cookie c : cookies) {
			if (name.equals(c.getName())) {
				value = c.getValue();
			}
		}
		return value;
	}

	public List<Cookie> getCookies() {
		return cookieStore.getCookies();
	}

	public void clearCookies() {
		if (cookieStore != null) {
			cookieStore.clear();
		}
	}

	/**
	 * 设置cookie
	 *
	 * @param name
	 * @param value
	 * @param domain
	 * @param path
	 */
	public void setCookie(String name, String value, String domain, String path) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 30);
		Date d = cal.getTime();

		BasicClientCookie c = new BasicClientCookie(name, value);
		c.setDomain(domain);
		c.setPath(path);
		c.setExpiryDate(d);
		cookieStore.addCookie(c);
	}



}
