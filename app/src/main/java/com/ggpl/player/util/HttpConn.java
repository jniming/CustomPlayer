package com.ggpl.player.util;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class HttpConn {
	// post����
	public static String post(String url, String content) {
		HttpURLConnection conn = null;
		// ����һ��URL����
		String response = "";
		InputStream is = null;
		OutputStream out = null;

		try {
			URL murl = new URL(url);
			trustAllHosts();
			if (murl.getProtocol().toLowerCase().equals("https")) {
				HttpsURLConnection https = (HttpsURLConnection) murl
						.openConnection();
				https.setHostnameVerifier(DO_NOT_VERIFY);
				conn = https;
			} else {
				conn = (HttpURLConnection) murl.openConnection();
			}
			conn.setRequestMethod("POST");// �������󷽷�Ϊpost
			conn.setDoOutput(true);// ���ô˷���,������������������// post����Ĳ���
            conn.setConnectTimeout(3000);
			out = conn.getOutputStream();// ���һ�������,�������д����out.write(data.getBytes());
			out.write(content.getBytes());
			out.flush();
			out.close();
			int responseCode = conn.getResponseCode();// ���ô˷����Ͳ�����ʹ��conn.connect()����if

			if (responseCode == 200) {
				is = conn.getInputStream();
				response = getStringFromInputStream(is);
				if (response.startsWith("\"")) {
					response = response.replace("\"", "");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return response;

	}

	// get����
	public static String get(String url) {
		String response = "";
		InputStream is = null;
		HttpURLConnection conn = null;
		try {
			URL murl = new URL(url);
			trustAllHosts();
			if (murl.getProtocol().toLowerCase().equals("https")) {
				HttpsURLConnection https = (HttpsURLConnection) murl
						.openConnection();
				https.setHostnameVerifier(DO_NOT_VERIFY);
				conn = https;
			} else {
				conn = (HttpURLConnection) murl.openConnection();
			}
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5000);
			conn.setConnectTimeout(10000);

			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				is = conn.getInputStream();
				response = getStringFromInputStream(is);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return response;
	}

	private static String getStringFromInputStream(InputStream is) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		String state = "";
		try {
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			is.close();
			state = os.toString().trim();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return state;
	}

	/**
	 * ����ͼƬ,������֤md5ֵ
	 * 
	 * @param context
	 * @param image_url
	 * @return
	 */
	@SuppressWarnings("resource")
	public static File DownImage(final Context context, final String image_url) {
		File downloadedFile = null;
		InputStream in = null;
		FileOutputStream out = null;
		downloadedFile = new File(context.getFilesDir(), getFileName(image_url));
		if(downloadedFile.exists()){
			
			return downloadedFile;
		}
		HttpURLConnection con = null;
		try {
			URL url = new URL(image_url);
			trustAllHosts();
			if (url.getProtocol().toLowerCase().equals("https")) {
				HttpsURLConnection https = (HttpsURLConnection) url
						.openConnection();
				https.setHostnameVerifier(DO_NOT_VERIFY);
				con = https;
			} else {
				con = (HttpURLConnection) url.openConnection();
			}
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			if (responseCode == 200) {
				in = con.getInputStream();
				out = new FileOutputStream(downloadedFile);
				byte[] buffer = new byte[1024];
				int len;
				while (-1 != (len = in.read(buffer))) {
					out.write(buffer, 0, len);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (null != downloadedFile && downloadedFile.exists()) {
				downloadedFile.delete();
			}
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (con != null) {
				con.disconnect();
				con = null;
			}
		}
		return downloadedFile;
	}

//	/**
//	 * ����ͼƬ������֤md5ֵ
//	 *
//	 * @param image_url
//	 * @param listener
//	 */
//	public static File downloadImage(final Context context,
//			final String image_url, final String fileMd5) throws Exception {
//
//		File downloadedFile = null;
//		InputStream in = null;
//		FileOutputStream out = null;
//		HttpURLConnection con = null;
//		try {
//			// �ж��Ƿ����㹻�Ŀռ���д洢
//			downloadedFile = new File(context.getFilesDir(),
//					getFileName(image_url));
//			if (downloadedFile.exists()) {// ���ļ��Ѵ�����������ֱ�ӷ���
//				if (null != fileMd5
//						&& fileMd5.equals(MD5Util.getFileMd5(downloadedFile))) {
//					LogUtil.d("ͼƬ����-->ֱ��ʹ��");
//					return downloadedFile;
//				}
//			}
//
//			URL url = new URL(image_url);
//			trustAllHosts();
//			if (url.getProtocol().toLowerCase().equals("https")) {
//				HttpsURLConnection https = (HttpsURLConnection) url
//						.openConnection();
//				https.setHostnameVerifier(DO_NOT_VERIFY);
//				con = https;
//			} else {
//				con = (HttpURLConnection) url.openConnection();
//			}
//			con.setRequestMethod("GET");
//			int responseCode = con.getResponseCode();
//			MessageDigest digest = MessageDigest.getInstance("md5");
//			if (responseCode == 200) {
//				in = con.getInputStream();
//				out = new FileOutputStream(downloadedFile);
//				byte[] buffer = new byte[1024 * 10];
//				int len;
//				while (-1 != (len = in.read(buffer))) {
//					out.write(buffer, 0, len);
//					digest.update(buffer, 0, len);
//				}
//				out.flush();
//
//				String md5Value = MD5Util.bufferToHex(digest.digest());// �ļ�md5ֵ
//
//				LogUtil.d(getFileName(image_url) + "complete�� md5��"
//						+ md5Value);
//
//				if (0 == fileMd5.compareToIgnoreCase(md5Value)) {
//					// MD5 ��֤��ȷ
//					return downloadedFile;
//				} else {
//					LogUtil.d("md5 check wrong, rollback~");
//					if (null != downloadedFile && downloadedFile.exists()) {
//						downloadedFile.delete();
//					}
//					return null;
//				}
//			} else {
//				LogUtil.d("failed to connect");
//				if (null != downloadedFile && downloadedFile.exists()) {
//					downloadedFile.delete();
//				}
//				return null;
//			}
//
//		} finally {
//			if (null != out) {
//				try {
//					out.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			if (null != in) {
//				try {
//					in.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			if (con != null) {
//				con.disconnect();
//				con = null;
//			}
//		}
//	}
//
//	/**
//	 * �ϵ���������
//	 * 
//	 * @param context
//	 * @param downloadUrl
//	 * @param fileLength
//	 * @param fileMd5
//	 * @param fileType
//	 * @return
//	 * @throws ConnectTimeoutException
//	 * @throws NoEnoughSpaceException
//	 * @throws FileCheckErrorException
//	 * @throws ConnectFailException
//	 * @throws Exception
//	 */
//	public static File download(Context context, String downloadUrl,
//			int fileLength, String fileMd5) {
//
//		RandomAccessFile rdAcessFile = null;
//		InputStream in = null;
//		File downloadedFile = null;
//		int downloadLength = 0;
//		HttpURLConnection con = null;
//
//		try {
//			URL url = new URL(downloadUrl);
//			trustAllHosts();
//			if (url.getProtocol().toLowerCase().equals("https")) {
//				HttpsURLConnection https = (HttpsURLConnection) url
//						.openConnection();
//				https.setHostnameVerifier(DO_NOT_VERIFY);
//				con = https;
//			} else {
//				con = (HttpURLConnection) url.openConnection();
//			}
//			con.setRequestMethod("GET");
//			downloadedFile = createFile(context, downloadUrl, fileLength);
//
//			if (null != downloadedFile && downloadedFile.exists()
//					&& downloadedFile.length() == fileLength) {// ���ļ��Ѵ�����������ֱ�ӷ���
//				if (null != fileMd5
//						&& fileMd5
//								.equals(DigestUtil.getFileMd5(downloadedFile))) {
//					return downloadedFile;
//
//				}
//			}
//
//			rdAcessFile = new RandomAccessFile(downloadedFile, "rwd");
//			// ��¼��ȡ��������Ϣ
//			DownloadProcessDao processDao = new DownloadProcessDao(context);
//			DownloadProcessInfo query = processDao.query(DigestUtil
//					.mMD5Digest(downloadUrl));
//
//			if (null != query) {
//				downloadLength = query.getDownloadedLen();
//				fileLength = query.getFileLen();
//				rdAcessFile.seek(downloadLength);
//			} else {
//				processDao.insert(DigestUtil.mMD5Digest(downloadUrl),
//						downloadLength, fileLength);
//				rdAcessFile.setLength(fileLength);
//				rdAcessFile.seek(0);
//			}
//
//			LogUtil.d("���ص�ַΪ-->" + downloadUrl);
//
//			// ��������ͷ
//			con.setRequestProperty("Range", "bytes=" + downloadLength + "-"
//					+ (fileLength - 1));
//
//			// // ������Դ�ļ�
//			// con.addHeader("Range", "bytes=" + downloadLength + "-"
//			// + (fileLength - 1));
//
//			int statusCode = con.getResponseCode();
//			LogUtil.d("download file  response code��" + statusCode);
//
//			if (206 == statusCode || 200 == statusCode) {
//				Log.d("core1", "d---d");
//				in = con.getInputStream();
//				byte[] buf = new byte[1024];
//				int len;
//				LogUtil.d("start downloading��");
//				long start = System.currentTimeMillis();
//
//				while (-1 != (len = in.read(buf))) {
//					rdAcessFile.write(buf, 0, len);
//					downloadLength += len;
//					processDao.updateProcess(
//							DigestUtil.mMD5Digest(downloadUrl), downloadLength);
//					LogUtil.d("rdAcessFile length=" + rdAcessFile.length()
//							+ ",downloadLength=" + downloadLength);
//					// // �ص�
//					// if (downloadListener != null) {
//					// downloadListener.onDownloading(downloadUrl,
//					// downloadedFile.getAbsolutePath(),
//					// downloadLength, fileLength,
//					// downloadedFile.getName());
//					// }
//				}
//
//				long end = System.currentTimeMillis();
//				LogUtil.d("consume time:" + (end - start));
//
//				processDao.delete(DigestUtil.mMD5Digest(downloadUrl));// ������ɣ�ɾ���ϵ�������Ϣ
//
//				// �ļ�md5ֵ����Ϊ�Ƕϵ����������Բ��������ع�����ͬ������md5ֵ
//				String md5Value = DigestUtil.getFileMd5(downloadedFile);// �ļ�md5ֵ
//
//				LogUtil.d(downloadedFile.getName() + " complete��  md5��"
//						+ md5Value);
//
//				if (0 == fileMd5.compareToIgnoreCase(md5Value)
//						&& downloadedFile.length() == downloadLength) {
//					// MD5 ��֤��ȷ
//
//					String filePath = downloadedFile.getAbsolutePath();
//					// �޸��ļ�Ȩ�� ,�����޷��ļ��޷���
//					Runtime runtime = Runtime.getRuntime();
//					String command = "chmod 777 " + filePath;
//					try {
//						runtime.exec(command);
//					} catch (IOException e) {
//						downloadedFile.delete();
//						e.printStackTrace();
//					}
//
//					return downloadedFile;
//					// downloadListener.onDownloadSuccess(downloadUrl,
//					// downloadedFile.getAbsolutePath(),
//					// downloadedFile.getName(), downloadedFile.length());
//				} else {
//					LogUtil.d("md5 check wrong, rollback~");
//					if (null != downloadedFile && downloadedFile.exists()) {
//						downloadedFile.delete();
//					}
//					return null;
//				}
//			} else {
//				LogUtil.d(downloadUrl + "  failed to connect��");
//				if (null != downloadedFile && downloadedFile.exists()) {
//					downloadedFile.delete();
//				}
//				return null;
//			}
//		} catch (Exception e) {
//			LogUtil.d("���س���--" + e.getMessage());
//			downloadedFile = null;
//			e.printStackTrace();
//		} finally {
//
//			if (null != rdAcessFile) {
//				try {
//					rdAcessFile.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			if (null != in) {
//				try {
//					in.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return downloadedFile;
//	}
//
//	/**
//	 * ���ɱ����ļ�����
//	 * 
//	 * @param context
//	 * @param image_url
//	 * @param fileLength
//	 * @param downloadedFile
//	 * @param fileType
//	 * @return
//	 * @throws NoEnoughSpaceException
//	 */
//	private static File createFile(final Context context,
//			final String image_url, final int fileLength) throws Exception {
//
//		File downloadedFile = null;
//		String fileName = getFileName(image_url);
//
//		// �ж��Ƿ����㹻�Ŀռ���д洢
//		LogUtil.d("mem size: " + MemoryManager.getAvailableInternalMemorySize());
//		if (MemoryManager.getAvailableInternalMemorySize() > fileLength) {// �ж��ڲ��洢�ռ��Ƿ��㹻
//
//			downloadedFile = new File(context.getFilesDir(), fileName);
//
//		} else {// û���㹻�Ŀռ�
//			throw new Exception("no enough space");
//		}
//
//		return downloadedFile;
//	}
//
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	/**
	 * ��������֤�� #http://blog.csdn.net/jianglili611/article/details/46290431
	 */
	private static void trustAllHosts() {
		final String TAG = "trustAllHosts";
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
				Log.i(TAG, "checkClientTrusted");
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
				Log.i(TAG, "checkServerTrusted");
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getFileName(String url) {
		int start = url.lastIndexOf(".");
		String urlMd5Str = MD5Util.mMD5Digest(url);

		return urlMd5Str + url.substring(start);
	}

}
