package common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.CookieStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

public class FileDownloader {

	public void dowloadFile(String dowloadURL, String outputFileLocation, String outputFileName) throws Exception {
		SSLContextBuilder builder = new SSLContextBuilder();
		builder.loadTrustMaterial(null, new TrustStrategy() {
			public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				return true;
			}
		});
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());

		CookieStore cookieStore = seleniumCookiesToCookieStore();

		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
				.setDefaultCookieStore((org.apache.http.client.CookieStore) cookieStore).build();

		try {
			String baseURL = null;
			HttpGet httpget = new HttpGet(baseURL + dowloadURL);
			CloseableHttpResponse response = httpClient.execute(httpget);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					File directoriesFile = new File(outputFileLocation);
					directoriesFile.mkdirs();
					File outputFile = new File(outputFileLocation + "\\" + outputFileName);
					InputStream inputStream = entity.getContent();
					FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
					int read = 0;
					byte[] bytes = new byte[1024];
					while ((read = inputStream.read(bytes)) != -1) {
						fileOutputStream.write(bytes, 0, read);
					}
					fileOutputStream.close();
					// log.info("Downloaded " + outputFile.length() + " bytes. "
					// + entity.getContentType());
				} else {
					// log.warn("Download failed!");
				}
			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}
	}

	private CookieStore seleniumCookiesToCookieStore() {
		Set<org.openqa.selenium.Cookie> seleniumCookies = CommonLib.GetDriver().manage().getCookies();
		CookieStore cookieStore = (CookieStore) new BasicCookieStore();

		for (org.openqa.selenium.Cookie seleniumCookie : seleniumCookies) {
			BasicClientCookie basicClientCookie = new BasicClientCookie(seleniumCookie.getName(),
					seleniumCookie.getValue());
			basicClientCookie.setDomain(seleniumCookie.getDomain());
			basicClientCookie.setExpiryDate(seleniumCookie.getExpiry());
			basicClientCookie.setPath(seleniumCookie.getPath());
			((BasicCookieStore) cookieStore).addCookie(basicClientCookie);
		}
		return cookieStore;
	}

}