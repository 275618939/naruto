package com.movie.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class KeyValueParser {

	public static Map<String,String> parser(InputStream is,String charset) throws IOException,UnsupportedEncodingException{
		int length=0;
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		byte data[]=new byte[2048];
		try{
			while((length=is.read(data))>0){
				baos.write(data,0,length);
			}
			baos.flush();
		}
		finally{
			if(baos!=null)try{baos.close();}catch(IOException e){}
		}
		
		return parser(baos.toString(charset),charset);
	}
	
	public static Map<String,String> parser(InputStreamReader reader) throws IOException,UnsupportedEncodingException{
		int length=0;
		StringBuffer sb=new StringBuffer();
		char data[]=new char [2048];
		
		while((length=reader.read(data))>0){
			sb.append(data,0,length);
		}
		
		
		return parser(sb.toString(),reader.getEncoding());
	}

	public static String parserCharset(String contentType,String defaultCharset){
		if(contentType!=null){
			int pos=contentType.lastIndexOf(';');
			if(pos>1){
				String charset=contentType.substring(pos+1).trim();
				pos=charset.lastIndexOf('=');
				if(pos>1){
					return charset.substring(pos+1).trim();
				}
			}
		}
		return defaultCharset;
	}
	public static Map<String,String> parser(String data,String charset) throws UnsupportedEncodingException{
		boolean findKey=true;
		Map<String,String> params=new HashMap<String,String>();
		StringBuffer key=new StringBuffer(),value=new StringBuffer();
		
		int i=0,length=data.length();
		for(i=0;i<length;i++){
			char c=data.charAt(i);
			if(findKey){
				if(c=='='){
					findKey=false;
				}
				else{
					key.append(c);
				}
			}
			else{
				if(c=='&'){
					params.put(URLDecoder.decode(key.toString(),charset), URLDecoder.decode(value.toString(),charset));
					key=new StringBuffer();
					value=new StringBuffer();
					findKey=true;
				}
				else{
					value.append(c);
				}
			}
		}
		if(key.length()>0){
			params.put(URLDecoder.decode(key.toString(),charset), URLDecoder.decode(value.toString(),charset));
		}
		return params;
	}
}
