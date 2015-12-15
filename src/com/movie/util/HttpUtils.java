package com.movie.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

public class HttpUtils {

	private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识
	private static final String PREFIX = "--";
	private static final String LINE_END = "\r\n";
	private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型  multipart/form-data

	
	public static byte[] requestGetByte(String url,Map<String,String>headers){
		return requestGetByte("GET",url,headers);
	}
	public static String requestGet(String url,Map<String,String>headers){
		return requestGetDelete("GET",url,headers);
	}

	public static String requestDelete(String url,Map<String,String>headers){
		return requestGetDelete("DELETE",url,headers);
	}
	public static String requestPost(String url,String params,Map<String,String>headers){
		return requestPostPut("POST",url,params,headers);
	}
	public static String requestPostImage(String url,File file,Map<String,Object> params,Map<String,String>headers) {
		return requestImage("POST",url,getParams(params),file,headers);
	}
	public static String requestPut(String url,String params,Map<String,String>headers){
		return requestPostPut("PUT",url,params,headers);
	}
	public static String requestPost(String url,Map<String,String>headers,Map<String,Object>params){
		return requestPostPut("POST",url,getParams(params),headers);
	}
	public static String requestPut(String url,Map<String,String>headers,Map<String,Object>params){
		return requestPostPut("PUT",url,getParams(params),headers);
	}
	private static byte[] requestGetByte(String method,String url,Map<String,String>headers){
		
		InputStream is=null;
		int respCode=0;
		ByteArrayOutputStream baos=null;
		HttpURLConnection conn = null;
		
		try{
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			conn.setRequestProperty("Accept", "*/*");
			if(headers!=null){
				for(Map.Entry<String, String> entry:headers.entrySet()){
					conn.setRequestProperty(entry.getKey(),entry.getValue());
				}
			}
			conn.setRequestMethod(method);
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(180000);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			
			conn.connect();
			
			respCode=conn.getResponseCode();
		
			if (respCode>=200&&respCode<300) {
				String charset=KeyValueParser.parserCharset(conn.getHeaderField("Content-Type"), "UTF-8");
				is = conn.getInputStream();
			
				byte buffer[] = new byte[2048];
				int len;
				baos = new ByteArrayOutputStream();
				while ((len = is.read(buffer)) > 0) {
					baos.write(buffer, 0, len);
				}
				baos.flush();
				baos.close();
				
				return baos.toByteArray();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(is!=null)try{is.close();}catch(IOException e){};
			if(conn!=null)try{conn.disconnect();}catch(Exception e){};
		
		}
		return null;
	}

	private static String requestGetDelete(String method,String url,Map<String,String>headers){
	
		InputStream is=null;
		int respCode=0;
		ByteArrayOutputStream baos=null;
		HttpURLConnection conn = null;
		
		try{
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			conn.setRequestProperty("Accept", "*/*");
			if(headers!=null){
				for(Map.Entry<String, String> entry:headers.entrySet()){
					conn.setRequestProperty(entry.getKey(),entry.getValue());
				}
			}
			
			conn.setRequestMethod(method);
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(180000);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			
			conn.connect();
			
			respCode=conn.getResponseCode();
		
			if (respCode>=200&&respCode<300) {
				String charset=KeyValueParser.parserCharset(conn.getHeaderField("Content-Type"), "UTF-8");
				is = conn.getInputStream();
			
				byte buffer[] = new byte[2048];
				int len;
				baos = new ByteArrayOutputStream();
				while ((len = is.read(buffer)) > 0) {
					baos.write(buffer, 0, len);
				}
				baos.flush();
				baos.close();
				
				return new String(baos.toByteArray(),charset);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(is!=null)try{is.close();}catch(IOException e){};
			if(conn!=null)try{conn.disconnect();}catch(Exception e){};
		
		}
		return null;
	}
	private static String requestImage(String method,String url,String params,File file,Map<String,String>headers) {
		
		OutputStream os=null;
		InputStream is=null;
		int respCode=0;
		ByteArrayOutputStream baos=null;
		HttpURLConnection conn = null;
		
		try{
			URL u = new URL(url);
			
			conn = (HttpURLConnection) u.openConnection();
			conn.setDoOutput(true);	
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("Charset", "utf-8"); 
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="+ BOUNDARY);
			//conn.setRequestProperty("Content-Length",Integer.toString(data.length));
			if(headers!=null){
				for(Map.Entry<String, String> entry:headers.entrySet()){
					conn.setRequestProperty(entry.getKey(),entry.getValue());
				}
			}
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(180000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.connect();
			os = conn.getOutputStream();
			if(params!=null&&!params.isEmpty()){
				byte data[] = params.getBytes();
				os.write(data);
				os.flush();
			}
			/* 上传文件 */
			StringBuffer sb = new StringBuffer();
			sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
			sb.append("Content-Disposition:form-data; name=\"image\"; filename=\"" + file.getName() + "\"" + LINE_END);
			sb.append("Content-Type:image/png" + LINE_END); // 这里配置的Content-type很重要的																// ，用于服务器端辨别文件的类型的
			sb.append(LINE_END);
			
			os.write(sb.toString().getBytes());
			
			is = new FileInputStream(file);
			byte[] bytes = new byte[1024];
			int len = 0;
			while ((len = is.read(bytes)) != -1) {
				os.write(bytes, 0, len);
			}
			is.close();
			os.write(LINE_END.getBytes());
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
			os.write(end_data);
			os.flush();
			os.close();
			
			respCode=conn.getResponseCode();
		
			if (respCode>=200&&respCode<300) {
				String charset=KeyValueParser.parserCharset(conn.getHeaderField("Content-Type"), "UTF-8");
				is = conn.getInputStream();
			
				byte buffer[] = new byte[4096];
				len=0;
				baos = new ByteArrayOutputStream();
				while ((len = is.read(buffer)) > 0) {
					baos.write(buffer, 0, len);
				}
				baos.flush();
				baos.close();
				return new String(baos.toByteArray(),charset);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(is!=null)try{is.close();}catch(IOException e){};
			if(conn!=null)try{conn.disconnect();}catch(Exception e){};
		
		}
		return null;
	}
	private static String requestPostPut(String method,String url,String params,Map<String,String>headers) {
		
		OutputStream os=null;
		InputStream is=null;
		int respCode=0;
		ByteArrayOutputStream baos=null;
		HttpURLConnection conn = null;
		
		try{
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			byte data[] = params.getBytes();
			conn.setDoOutput(true);	
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("Content-Type", "text/plain;charset=utf8");
			conn.setRequestProperty("Content-Length",Integer.toString(data.length));
			if(headers!=null){
				for(Map.Entry<String, String> entry:headers.entrySet()){
					conn.setRequestProperty(entry.getKey(),entry.getValue());
				}
			}
			
			conn.setRequestMethod(method);
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(180000);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			
			conn.connect();
			
			os = conn.getOutputStream();
			os.write(data);
			os.flush();
			os.close();
			
			respCode=conn.getResponseCode();
		
			if (respCode>=200&&respCode<300) {
				String charset=KeyValueParser.parserCharset(conn.getHeaderField("Content-Type"), "UTF-8");
				is = conn.getInputStream();
			
				byte buffer[] = new byte[4096];
				int len;
				baos = new ByteArrayOutputStream();
				while ((len = is.read(buffer)) > 0) {
					baos.write(buffer, 0, len);
				}
				baos.flush();
				baos.close();
				return new String(baos.toByteArray(),charset);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(is!=null)try{is.close();}catch(IOException e){};
			if(conn!=null)try{conn.disconnect();}catch(Exception e){};
		
		}
		return null;
	}
	
	private static String getParams(Map<String,Object>params){
		if(params!=null){
			boolean once=false;
			StringBuffer sb=new StringBuffer();
			for(Map.Entry<String, Object> entry:params.entrySet()){
				if(once){
					sb.append('&');
				}
				else{
					once=true;
				}
				sb.append(entry.getKey());
				sb.append('=');
				sb.append(entry.getValue());
			}
			return sb.toString();
		}
		return null;
	}
	public boolean ping(String ip) {
		Runtime run = Runtime.getRuntime();
		Process proc = null;
		try {
			String str = "ping -c 1 -i 0.2 -W 1 " + ip;
			System.out.println(str);
			proc = run.exec(str);
			int result = proc.waitFor();
			if (result == 0) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			proc.destroy();
		}
		return false;

	}
}
